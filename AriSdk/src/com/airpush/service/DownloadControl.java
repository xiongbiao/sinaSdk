package com.airpush.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import com.airpush.android.IConstants;
import com.airpush.android.SinException;
import com.airpush.data.MsgInfo;
import com.airpush.util.CheckSumUntil;
import com.airpush.util.LogUtil;
import com.airpush.util.SinDirectoryUtils;

public class DownloadControl {

    private static final String TAG = "DownloadControl";
    
	public static final int FAIL_TYPE_NOALERT_AUTO_CONTINUE = 0;
	public static final int FAIL_TYPE_ALERT_AUTO_CONTINUE = 1;
	public static final int FAIL_TYPE_ALERT_CLICK_CONTINUE_NORMAL = 2;
	public static final int FAIL_TYPE_ALERT_CLICK_CONTINUE_404 = 3;

    private static final int CODE_INVALID_FILE_ERROR = 2;
    private static final int CODE_DOWNLOAD_SUCCESS = 1;
    private static final int CODE_DOWNLOAD_AGAIN = 0;
    private static final int CODE_TIME_OUT_TRY_AGAIN = -1;
    private static final int CODE_DOWNLOAD_FAIL = -2;
    private static final int CODE_DOWNLOAD_404 = -3;

    private static final int TRY_AGAIN_WHILE_CHECK_MD5_ERROR_TIMES = 3;
    private static final int REFRESH_TIME = 1000 * 2;

    private DownloadHandler mDownloadProgressHandler = null;
    private long hadDownLength = 0;
    private long totalDownLength = 0;
    private Bundle downloadInfos;
    
    public boolean isNeedStopDownload = false;
    public static boolean isNetworkAvailable = true;

    private class DownloadHandler extends Handler {
        private SinDownloadListener mDownloadListener = null;

        public DownloadHandler(Looper looper, SinDownloadListener downloadListener) {
            super(looper);
            mDownloadListener = downloadListener;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (isNeedStopDownload) {
                LogUtil.v(TAG, "stop refresh download progress.");
                return;
            }
            if (mDownloadListener != null) {
                mDownloadListener.onDownloading(hadDownLength, totalDownLength);
            }
            mDownloadProgressHandler.sendEmptyMessageDelayed(0, REFRESH_TIME);
        }
    }
    
    public DownloadControl(Context context, MsgInfo  adEntity,
            Bundle downloadInfos, SinDownloadListener downloadListener, int interval) {
    	LogUtil.v(TAG, "Create downloadControl");
		isNeedStopDownload = false;
		
        this.downloadInfos = downloadInfos;
        mDownloadProgressHandler = new DownloadHandler(context.getMainLooper(), downloadListener);
        mDownloadProgressHandler.sendEmptyMessageDelayed(0, REFRESH_TIME);
        
        int reCheckFileTimes = 0;
        
        while (true) {
        	if (!isNetworkAvailable) {
        		LogUtil.i(TAG, "Network is not available, dont download");
                mDownloadProgressHandler.removeCallbacksAndMessages(null);
        		isNeedStopDownload = true;
                downloadListener.onDownloadFailed(FAIL_TYPE_ALERT_AUTO_CONTINUE);
        		break;
        	}
        	
			if (isNeedStopDownload) {
                LogUtil.i(TAG, "Download is already stopped. Dont start again.");
                mDownloadProgressHandler.removeCallbacksAndMessages(null);
                downloadListener.onDownloadFailed(FAIL_TYPE_ALERT_AUTO_CONTINUE);
                break;
            }
            
            if (adEntity._downloadRetryTimes == 0) { // 重试N次后停止
                LogUtil.w(TAG, "try to connect too much. stop download now.");
                if (null != downloadListener) {
                    isNeedStopDownload = true;
                    SinDownloadService.mDownladTasks.remove(adEntity);
                    mDownloadProgressHandler.removeCallbacksAndMessages(null);
                    downloadListener.onDownloadFailed(FAIL_TYPE_ALERT_CLICK_CONTINUE_NORMAL);
                }
                break;
            }
            
            if (reCheckFileTimes >= TRY_AGAIN_WHILE_CHECK_MD5_ERROR_TIMES) {
                LogUtil.w(TAG, "check md5 error too much. stop download now.");
                if (null != downloadListener) {
                    isNeedStopDownload = true;
                    SinDownloadService.mDownladTasks.remove(adEntity);
                    mDownloadProgressHandler.removeCallbacksAndMessages(null);
                    downloadListener.onDownloadFailed(FAIL_TYPE_ALERT_CLICK_CONTINUE_NORMAL);
                }
                break;
            }
            
            int code = download(context, downloadListener, adEntity);
            adEntity._downloadRetryTimes--;
            
            if (code == CODE_TIME_OUT_TRY_AGAIN) { // 一般为连接超时，继续重试
                LogUtil.d(TAG, "Connect time out, try rest - " + adEntity._downloadRetryTimes);
                try {
                    Thread.sleep(interval);
                } catch (InterruptedException e) {
                }
                
            } else if (code == CODE_DOWNLOAD_AGAIN) { // 重新下载
                LogUtil.d(TAG, "Download again, try rest - " + adEntity._downloadRetryTimes);
                try {
                    Thread.sleep(interval);
                } catch (InterruptedException e) {
                }
                
            } else if (code == CODE_DOWNLOAD_SUCCESS) {
                LogUtil.d(TAG, "Download succeed.");
                mDownloadProgressHandler.removeCallbacksAndMessages(null);
                isNeedStopDownload = true;
                break;
                
            } else if (code == CODE_INVALID_FILE_ERROR) {
                LogUtil.d(TAG, "md5 check error, try again - " + adEntity._downloadRetryTimes);
                reCheckFileTimes ++;
                
            } else if (code == CODE_DOWNLOAD_404) {
                isNeedStopDownload = true;
                SinDownloadService.mDownladTasks.remove(adEntity);
                mDownloadProgressHandler.removeCallbacksAndMessages(null);
                downloadListener.onDownloadFailed(FAIL_TYPE_ALERT_CLICK_CONTINUE_404);
            	break;
                
            } else { // 其他异常情况，停止重试
                LogUtil.d(TAG, "Other exception!!");
                isNeedStopDownload = true;
                SinDownloadService.mDownladTasks.remove(adEntity);
                mDownloadProgressHandler.removeCallbacksAndMessages(null);
                downloadListener.onDownloadFailed(FAIL_TYPE_ALERT_CLICK_CONTINUE_NORMAL);
                break;
            }
        }
    }
    
    private int download(Context context, SinDownloadListener downloadListener, MsgInfo adEntity) {
    	String url = adEntity.getDownloadUrl();
    	String md5 = adEntity.getMd5();
    	
        String saveFilePath = "";
        String fileName = "";
        
        if (adEntity.isMsgTypeDownloadAndUpdate()) {
        	saveFilePath = SinDirectoryUtils.getDirectoryAppPath(context);
        	
            fileName = adEntity.msgId + ".apk";
        }
//        else if (adEntity.isAdTypeVideo()) {
//        	saveFilePath = SinDirectoryUtils.getDirectoryVideoPath(context);
//            fileName = adEntity.msgId;
//        }
        LogUtil.d(TAG, "saveFilePath : "+ saveFilePath);
        LogUtil.d(TAG, "fileName : "+ fileName);
        LogUtil.d(TAG, "url : "+ url);
        
        if (!TextUtils.isEmpty(url) && !TextUtils.isEmpty(saveFilePath) && !TextUtils.isEmpty(fileName)) { // 三个参数都必须正确

            LogUtil.i(TAG, "action:download - url:" + url + ", saveFilePath:" + saveFilePath + ", fileName:" + fileName);

            createSavePath(saveFilePath);

            // 获取下载记录
            long lastFileTotalLength = downloadInfos.getLong(url, -1);
            long hadDownloadLength = 0;
            long nowFileTotalLength = 0;

            InputStream is = null;
            BufferedInputStream bis = null;
            FileOutputStream fos = null;
            BufferedOutputStream bos = null;
            HttpEntity httpEntity = null;

            if (lastFileTotalLength <= 0) { // 没有记录，可能是第一次下载，也可能是已经下载过了
                File file = new File(saveFilePath, fileName);
                if (!file.exists() || file.length() <= 0) { // 文件不存在说明这是第一次下载
                    LogUtil.v(TAG, "Download first.");
                    HttpGet httpGet = getHttpGet(url, -1);
                    DefaultHttpClient client = getDefaultHttpClient();
                    try {
                        HttpResponse response = client.execute(httpGet);
                        if (null != response) {
                            int status = response.getStatusLine().getStatusCode();
                            if (status == HttpStatus.SC_OK) {
                                httpEntity = response.getEntity();
                                boolean isStream = httpEntity.isStreaming();
                                if (isStream) {
                                    // 记录文件长度
                                    nowFileTotalLength = getFileLengthFromHttp(response);
                                    downloadInfos.putLong(url, nowFileTotalLength);
                                    
                                    adEntity._downloadRetryTimes = getBestRetryTimes(nowFileTotalLength);
                                    
                                    is = httpEntity.getContent();
                                    if (is != null) {
                                        bis = new BufferedInputStream(is);
                                        file.delete();
                                        file.createNewFile();
                                        fos = new FileOutputStream(file);
                                        bos = new BufferedOutputStream(fos);
                                        byte[] buffer = new byte[1024];
                                        int offset = 0;
                                        while ((offset = bis.read(buffer)) != -1) {
                                            if (isNeedStopDownload) {
                                                LogUtil.w(TAG, "stop download by user, throw UAException.");
                                                throw new SinException("stop download by user.");
                                            }
                                            bos.write(buffer, 0, offset);
                                            hadDownloadLength = hadDownloadLength + offset;
                                            hadDownLength = hadDownloadLength;
                                            totalDownLength = nowFileTotalLength;
                                        }
                                        bos.flush();
                                        if (null != file && file.length() == nowFileTotalLength && CheckSumUntil.checkMd5(md5, file)) {
                                         // 下载成功移除记录
                                            downloadInfos.remove(url);
                                            if (null != downloadListener) {
                                                downloadListener.onDownloadSucceed(file.getAbsolutePath(), false);
                                            }
                                            return CODE_DOWNLOAD_SUCCESS;
                                        } else {
                                            LogUtil.w(TAG, "The download file is not valid, download again");
                                            if (!file.delete()) {
                                                LogUtil.e(TAG, "delete file fail !!!");
                                                return CODE_DOWNLOAD_FAIL;
                                            }
                                            return CODE_INVALID_FILE_ERROR;
                                        }
                                    } else {
                                        LogUtil.w(TAG, "NULL response stream.");
                                        return CODE_DOWNLOAD_AGAIN;
                                    }
                                } else { // 服务端返回的不是流形式
                                    LogUtil.e(TAG, "data mode from server is not stream.");
                                    return CODE_DOWNLOAD_FAIL;
                                }
                                
                            } else {
                                // 服务端返回的状态码不是 200
                            	if (status == 404) {
                            		LogUtil.d(TAG, "The resource does not exist - " + url);
                            		return CODE_DOWNLOAD_404;
                            	} else {
                            		LogUtil.w(TAG, "network connect status code unexpected - " + status);
                            		return CODE_DOWNLOAD_FAIL;
                            	}
                            }
                            
                        } else { // 网络连接失败
                            LogUtil.w(TAG, "NULL response");
                            return CODE_DOWNLOAD_AGAIN;
                        }
                        
                    } catch (NumberFormatException e) {
                        LogUtil.e(TAG, "NumberFormatException, get content length from http fail.", e);
                        return CODE_DOWNLOAD_FAIL;
                    } catch (ClientProtocolException e) {
                        LogUtil.e(TAG, "", e);
                        return CODE_DOWNLOAD_FAIL;
                    } catch (IllegalStateException e) {
                        LogUtil.e(TAG, "", e);
                        return CODE_DOWNLOAD_FAIL;
                    } catch (FileNotFoundException e) {
                        LogUtil.e(TAG, "", e);
                        return CODE_DOWNLOAD_FAIL;
                    } catch (IOException e) {
                        LogUtil.d(TAG, "", e);
                        return CODE_TIME_OUT_TRY_AGAIN;
                    } catch (SinException e) {
                        LogUtil.w(TAG, "SinException", e);
                        return CODE_DOWNLOAD_FAIL;
                    } finally {
                        closeResource(is, bis, fos, bos, httpEntity);
                    }
                    
                } else if (file.length() > 0) { // 文件存在说明已经下载完毕，但是不知道是不是完整，下面判断
                    LogUtil.v(TAG, "No info and File had been exsit.");
                    HttpGet httpGet = getHttpGet(url, -1);
                    DefaultHttpClient client = getDefaultHttpClient();
                    try {
                        HttpResponse response = client.execute(httpGet);
                        long nowFileLength = getFileLengthFromHttp(response);
                        // 避免下载一半，应用卸载重装，记录没了的情况。make sure the file is valid with the MD5 check
                        if (file.length() == nowFileLength && CheckSumUntil.checkMd5(md5, file)) {
                            LogUtil.d(TAG, "Existed file size is same with target. Use it directly.");
                            if (null != downloadListener) {
                                downloadListener.onDownloadSucceed(file.getAbsolutePath(), true);
                            }
                            return CODE_DOWNLOAD_SUCCESS;
                        } else {
                            LogUtil.i(TAG, "Exsit file length:" + file.length() + ", fileTotalLength:" + nowFileLength);
                            if (!file.delete()) {
                                LogUtil.e(TAG, "delete file fail !!!");
                                return CODE_DOWNLOAD_FAIL;
                            }
                            return CODE_INVALID_FILE_ERROR;
                        }
                    } catch (ClientProtocolException e) {
                        LogUtil.e(TAG, "ClientProtocolException", e);
                        return CODE_DOWNLOAD_FAIL;
                    } catch (IOException e) {
                        LogUtil.d(TAG, "IOException", e);
                        return CODE_TIME_OUT_TRY_AGAIN;
                    } catch (SinException e) {
                        LogUtil.w(TAG, "UAException", e);
                        return CODE_DOWNLOAD_FAIL;
                    }
                } else {
                    LogUtil.e(TAG, "unexpected !!");
                    return CODE_DOWNLOAD_FAIL;
                }

            } else { // 有记录，继续断点续传
                LogUtil.i(TAG, "Had record, keep download.");
                long startPostion = 0;
                File file = new File(saveFilePath, fileName);
                if (file.exists()) { // 文件存在，获取断点位置
                    LogUtil.v(TAG, "File exsit, getting the file length.");
                    startPostion = file.length();
                    hadDownloadLength = startPostion; // 已下载的长度
                } else { // 文件不存在了，重头开始下载
                    LogUtil.v(TAG, "File had been delete, start from 0.");
                    startPostion = 0;
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        LogUtil.e(TAG, "createNewFile fail.", e);
                        return CODE_DOWNLOAD_FAIL;
                    }
                }
                
                LogUtil.i(TAG, "startPostion: " + startPostion);
                if (adEntity._downloadRetryTimes == MsgInfo.DOWNLOAD_RETRY_TIMES_NOT_SET) {
                	LogUtil.d(TAG, "Reset download retry times because it ever failed.");
                	adEntity._downloadRetryTimes = getBestRetryTimes(lastFileTotalLength);
                }
                
                HttpGet httpGet = getHttpGet(url, startPostion);
                DefaultHttpClient client = getDefaultHttpClient();
                try {
                    HttpResponse response = client.execute(httpGet);
                    if (null != response) {
                        int status = response.getStatusLine().getStatusCode();
                        if (status == HttpStatus.SC_OK || status == HttpStatus.SC_PARTIAL_CONTENT) {
                            httpEntity = response.getEntity();
                            boolean isStream = httpEntity.isStreaming();
                            if (isStream) {
                                long needDownloadLength = getFileLengthFromHttp(response);
                                if (needDownloadLength + hadDownloadLength == lastFileTotalLength) {
                                    is = httpEntity.getContent();
                                    if (is != null) {
                                        bis = new BufferedInputStream(is);
                                        fos = new FileOutputStream(file, true);
                                        bos = new BufferedOutputStream(fos);
                                        byte[] buffer = new byte[1024];
                                        int offset = 0;
                                        while ((offset = bis.read(buffer)) != -1) {
                                            if (isNeedStopDownload) {
                                                LogUtil.w(TAG, "stop download by user, throw UAException.");
                                                throw new SinException("stop download by user.");
                                            }
                                            bos.write(buffer, 0, offset);
                                            hadDownloadLength = hadDownloadLength + offset;
                                            hadDownLength = hadDownloadLength;
                                            totalDownLength = lastFileTotalLength;
                                        }
                                        bos.flush();
                                        LogUtil.d(TAG, "Download finished");
                                        
                                        if (null != file && file.length() == lastFileTotalLength && CheckSumUntil.checkMd5(md5, file)) {
                                            downloadInfos.remove(url);
                                            if (null != downloadListener) {
                                                downloadListener.onDownloadSucceed(file.getAbsolutePath(), false);
                                            }
                                            return CODE_DOWNLOAD_SUCCESS;
                                        } else {
                                            LogUtil.w(TAG, "The download file is not valid, download again");
                                            if (!file.delete()) {
                                                LogUtil.e(TAG, "delete file fail !!!");
                                                return CODE_DOWNLOAD_FAIL;
                                            }
                                            return CODE_INVALID_FILE_ERROR;
                                        }
                                    } else { // 网络连接失败
                                        LogUtil.w(TAG, "NULL response stream");
                                        return CODE_DOWNLOAD_AGAIN;
                                    }
                                    
                                } else { // 续传文件长度和上次文件长度不一致
                                    LogUtil.e(TAG, "File length between last and now were different.");
                                    // 删除记录
                                    downloadInfos.remove(url);
                                    // 删除文件
                                    if (!file.delete()) {
                                        LogUtil.e(TAG, "delete file fail !!!");
//                                        return CODE_DOWNLOAD_FAIL;
                                    }
                                    // 重新下载
                                    return CODE_DOWNLOAD_AGAIN;
                                }
                            } else { // 服务器返回的不是流形式
                                LogUtil.e(TAG, "data mode from server is not stream.");
                                return CODE_DOWNLOAD_FAIL;
                            }
                            
                        } else if (status == HttpStatus.SC_REQUESTED_RANGE_NOT_SATISFIABLE) { // 服务器同一个url换了不同大小的文件，导致续传请求范围出错
                            LogUtil.e(TAG, "server file length change at the same url, delete all info and download again at 0.");
                            // 删除记录
                            downloadInfos.remove(url);
                            // 删除文件
                            if (!file.delete()) {
                                LogUtil.e(TAG, "delete file fail !!!");
//                                return CODE_DOWNLOAD_FAIL;
                            }
                            // 重新下载
                            return CODE_DOWNLOAD_AGAIN;
                            
                        } else { // 网络连接失败,状态码不是预期的
                        	if (status == 404) {
                        		LogUtil.d(TAG, "The resource does not exist - " + url);
                        		return CODE_DOWNLOAD_404;
                        	} else {
                        		LogUtil.w(TAG, "network connect status code unexpected - " + status);
                        		return CODE_DOWNLOAD_FAIL;
                        	}
                        }
                        
                    } else { // 网络连接失败
                        LogUtil.w(TAG, "NULL response");
                        return CODE_DOWNLOAD_AGAIN;
                    }
                } catch (NumberFormatException e) {
                    LogUtil.e(TAG, "NumberFormatException, get content length from http fail.", e);
                    return CODE_DOWNLOAD_FAIL;
                } catch (ClientProtocolException e) {
                    LogUtil.e(TAG, "ClientProtocolException", e);
                    return CODE_DOWNLOAD_FAIL;
                } catch (IllegalStateException e) {
                    LogUtil.e(TAG, "ClientProtocolException", e);
                    return CODE_DOWNLOAD_FAIL;
                } catch (FileNotFoundException e) {
                    LogUtil.e(TAG, "FileNotFoundException", e);
                    return CODE_DOWNLOAD_FAIL;
                } catch (IOException e) {
                    LogUtil.d(TAG, "IOException", e);
                    return CODE_TIME_OUT_TRY_AGAIN;
                } catch (SinException e) {
                    LogUtil.w(TAG, "UAException", e);
                    return CODE_DOWNLOAD_FAIL;
                } finally {
                    closeResource(is, bis, fos, bos, httpEntity);
                }
            }
        } else { // 传的三个参数有问题
            LogUtil.e(TAG, "Param error !! url:" + url + " savefilePath:" + saveFilePath + " fileName:" + fileName);
            return CODE_DOWNLOAD_FAIL;
        }
    }

    private void closeResource(InputStream is, BufferedInputStream bis, FileOutputStream fos, BufferedOutputStream bos,
            HttpEntity httpEntity) {
        if (bos != null) {
            try {
                bos.close();
            } catch (IOException e) {
            }
        }
        if (null != fos) {
            try {
                fos.close();
            } catch (IOException e) {
            }
        }
        if (null != bis) {
            try {
                bis.close();
            } catch (IOException e) {
            }
        }
        if (null != is) {
            try {
                is.close();
            } catch (IOException e) {
            }
        }
        if (null != httpEntity) {
            try {
                httpEntity.consumeContent();
            } catch (IOException e) {
            }
        }
    }

    private DefaultHttpClient getDefaultHttpClient() {
        HttpParams params = new BasicHttpParams();
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, "UTF-8");
        HttpConnectionParams.setTcpNoDelay(params, true);
        HttpConnectionParams.setConnectionTimeout(params, 1000 * 30);
        HttpConnectionParams.setSoTimeout(params, 1000 * 30);
        return new DefaultHttpClient(params);
    }

    private HttpGet getHttpGet(String url, long startPosition) {
        HttpGet httpGet = new HttpGet(url);
        httpGet.addHeader("Connection", "Close");
        httpGet.addHeader("User-Agent", "UA-SERVICE-1");
        if (startPosition >= 0) {
            httpGet.addHeader("Range", "bytes=" + startPosition + "-");
        }
        return httpGet;
    }

    private void createSavePath(String saveFilePath) {
        File file = new File(saveFilePath);
        if (!file.isDirectory()) {
            file.mkdirs();
        }
    }

    private long getFileLengthFromHttp(HttpResponse response) throws SinException {
        long nowFileTotalLength = 0l;
        Header contentLengthHeader = response.getFirstHeader("Content-Length");
        String contentLength = contentLengthHeader.getValue();
        nowFileTotalLength = Long.valueOf(contentLength);
        if (nowFileTotalLength <= 0) {
            throw new SinException("get the file total length from http is 0.");
        }
        return nowFileTotalLength;
    }
    
    private static final double DOWNLAOD_TIMES_RATE = 1.1;
    private int getBestRetryTimes(long size) {
    	long tenM = IConstants.SIZE_M * 10;
    	
    	long msize = size / tenM;
    	int times;
    	if (msize < 1) {
    		times = 10;
    	} else if (msize > 5) {
    		times = 50;
    	} else {
    		times = (int) (10 * msize);
    	}
    	
    	return (int) (times * DOWNLAOD_TIMES_RATE);
    }
    
    public static boolean isRealFailed(int failType) {
    	return (DownloadControl.FAIL_TYPE_ALERT_CLICK_CONTINUE_NORMAL == failType
        		|| DownloadControl.FAIL_TYPE_ALERT_CLICK_CONTINUE_404 == failType);
    }
    
    public interface SinDownloadListener {
    	
        public void onDownloadSucceed(String fileSaveTotalPath, boolean existed);
        public void onDownloadFailed(int failType);
        public void onDownloading(long downloadLenth, long totalLength);
    }

}
