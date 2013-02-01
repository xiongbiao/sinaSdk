package com.airpush.android;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import com.airpush.data.ConfigUtil;
import com.airpush.util.AndroidUtil;
import com.airpush.util.LogUtil;

public  class UserDetails {
	private Context context;
	private Location location;
	private String TAG = LogUtil.makeLogTag(UserDetails.class);

	public UserDetails(Context context) {
		this.context = context;
	}

	public String getImeiNoMd5() {
		try {
			String imeinumber = ((TelephonyManager) this.context.getSystemService("phone")).getDeviceId();
			if ((imeinumber == null) || (imeinumber.equals(""))) {
				Class c = Class.forName("android.os.SystemProperties");
				Method get = c.getMethod("get", new Class[] { String.class });
				imeinumber = (String) get.invoke(c, new Object[] { "ro.serialno" });
				ConfigUtil.setDevice_unique_type("serial");
				if ((imeinumber == null) || (imeinumber.equals("")))
					if (this.context.getPackageManager().checkPermission("android.permission.ACCESS_WIFI_STATE",AndroidUtil.getPackageName(this.context)) == 0) {
						WifiManager manager = (WifiManager) this.context.getSystemService("wifi");
						LogUtil.i(TAG,"WIFI " + manager.isWifiEnabled());
						imeinumber = manager.getConnectionInfo().getMacAddress();

						ConfigUtil.setDevice_unique_type("WIFI_MAC");
					} else {
						imeinumber = new DeviceUuidFactory(this.context).getDeviceUuid().toString();
						ConfigUtil.setDevice_unique_type("UUID");
					}
			} else {
				ConfigUtil.setDevice_unique_type("IMEI");
			}

			return imeinumber;
		} catch (Exception ignored) {
			ignored.printStackTrace();
		}
		return "invalid";
	}

  public boolean setImeiInMd5() {
		try {
			String imeinumber = getImeiNoMd5();
			if ((imeinumber == null) || (imeinumber.equals("")) || (imeinumber.equals("invalid"))) {
				LogUtil.i(TAG, "Can not get device unique id.");
				return false;
			}
			MessageDigest mdEnc = MessageDigest.getInstance("MD5");
			mdEnc.update(imeinumber.getBytes(), 0, imeinumber.length());
			String imei = new BigInteger(1, mdEnc.digest()).toString(16);
			ConfigUtil.setImei(imei);
			return true;
		} catch (NoSuchAlgorithmException algorithmException) {
			LogUtil.i(TAG, "Error occured while converting IMEI to md5." + algorithmException.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

  public Location getLocation() {
		LogUtil.i(TAG, "fetching Location.");
		try {
			if ((!ConfigUtil.getLatitude().equals("0")) || (ConfigUtil.getLastLocationTime() + 900000L > System.currentTimeMillis())) {
				return null;
			}
			synchronized (this.context) {
				if ((!ConfigUtil.getLatitude().equals("0"))
						|| (ConfigUtil.getLastLocationTime() + 900000L > System
								.currentTimeMillis())) {
					LogUtil.i(TAG, "failed in last");
					return null;
				}

				boolean ACCESS_COARSE_LOCATION = this.context.getPackageManager().checkPermission("android.permission.ACCESS_COARSE_LOCATION",this.context.getPackageName()) == 0;

				boolean ACCESS_FINE_LOACTION = this.context.getPackageManager().checkPermission("android.permission.ACCESS_FINE_LOCATION",this.context.getPackageName()) == 0;

				if ((ACCESS_COARSE_LOCATION) && (ACCESS_FINE_LOACTION)) {
					LocationManager mlocManager = (LocationManager) this.context
							.getSystemService("location");
					if (mlocManager == null) {
						LogUtil.i(TAG, "Location manager null");
						return null;
					}

					Criteria criteria = new Criteria();
					criteria.setCostAllowed(false);
					String provider = null;

					if (ACCESS_COARSE_LOCATION) {
						criteria.setAccuracy(2);
						provider = mlocManager.getBestProvider(criteria, true);
					}

					if ((provider == null) && (ACCESS_FINE_LOACTION)) {
						criteria.setAccuracy(1);
						provider = mlocManager.getBestProvider(criteria, true);
					}

					if (provider == null) {
						LogUtil.i(TAG, "Provider null");
						return null;
					}

					this.location = mlocManager.getLastKnownLocation(provider);
					if (this.location != null) {
						LogUtil.i(TAG,
								"Location found via get last known location.");
						return this.location;
					}
					final LocationManager finalizedLocationManager = mlocManager;

					ConfigUtil.setLastLocationTime(System.currentTimeMillis());
					mlocManager.requestLocationUpdates(provider, 0L, 0.0F,
							new LocationListener() {
								public void onLocationChanged(Location location) {
									ConfigUtil.setLastLocationTime(System.currentTimeMillis());

									UserDetails.this.location = location;
									finalizedLocationManager
											.removeUpdates(this);
								}

								public void onProviderDisabled(String provider) {
								}

								public void onProviderEnabled(String provider) {
								}

								public void onStatusChanged(String provider,
										int status, Bundle extras) {
								}
							}, this.context.getMainLooper());
				} else {
					LogUtil.i(TAG, "Location permission not found.");
				}
			}

		} catch (Exception e) {
			LogUtil.e(TAG,"Error occured while fetching location. " + e.getMessage());
		} catch (Throwable e) {
			LogUtil.e(TAG, "Error in location: " + e.getMessage());
		}
		return this.location;
	}

	private class DeviceUuidFactory {
		protected static final String PREFS_FILE = "device_id.xml";
		protected static final String PREFS_DEVICE_ID = "device_id";
		protected UUID uuid;

		public DeviceUuidFactory(Context context) {
			if (this.uuid == null)
				synchronized (DeviceUuidFactory.class) {
					if (this.uuid == null) {
						SharedPreferences prefs = context.getSharedPreferences(PREFS_FILE, 0);
						String id = prefs.getString(PREFS_DEVICE_ID, null);
						if (id != null) {
							this.uuid = UUID.fromString(id);
						} else {
							String androidId = Settings.Secure.getString(context.getContentResolver(), "android_id");
							try {
								if (!"9774d56d682e549c".equals(androidId)) {
									this.uuid = UUID.nameUUIDFromBytes(androidId.getBytes("utf8"));
								} else {
									String deviceId = ((TelephonyManager) context.getSystemService("phone")).getDeviceId();
									this.uuid = (deviceId != null ? UUID.nameUUIDFromBytes(deviceId.getBytes("utf8")) : UUID.randomUUID());
								}
							} catch (UnsupportedEncodingException e) {
								throw new RuntimeException(e);
							}
							prefs.edit().putString(PREFS_DEVICE_ID,this.uuid.toString()).commit();
						}
					}
				}
		}

		public UUID getDeviceUuid() {
			return this.uuid;
		}
	}
}
