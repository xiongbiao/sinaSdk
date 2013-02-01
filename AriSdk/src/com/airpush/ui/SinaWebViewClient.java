package com.airpush.ui;
import java.util.List;

import android.Manifest.permission;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.airpush.data.MsgInfo;
import com.airpush.util.AndroidUtil;
import com.airpush.util.LogUtil;

public class SinaWebViewClient extends WebViewClient {
    
    private static final String TAG = "UAWebViewClient";

    private static final String PARAM_DIRECT = "direct=";
    private static final String PARAM_TITLE = "title=";
    private static final String PARAM_CONTENT = "content=";
    private final MsgInfo entity;


	public SinaWebViewClient(MsgInfo entity) {
		this.entity = entity;
	}

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
    	Context context = view.getContext();
    	LogUtil.i(TAG, "Url vaule is :" + url);
    	try {
    		String reportJson = String.format("{\"url\":\"%s\"}", url);
    		if (url.startsWith("tel")){
				Intent browserIntent = new Intent(Intent.ACTION_VIEW,Uri.parse(url));
				context.startActivity(browserIntent);
//				ServiceInterface.reportAdActionResult(entity.adId, StatusCode.RESULT_TYPE_CLICK_CONTENT, reportJson, JPush.mApplicationContext);
				return true;
    		}
    		if (url.endsWith(".mp3")) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(url), "audio/*");
                view.getContext().startActivity(intent);   
                return true;
            } 
    		if (url.endsWith(".mp4") || url.endsWith(".3gp")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW); 
                    intent.setDataAndType(Uri.parse(url), "video/*");
                    view.getContext().startActivity(intent);   
                    return true;
            }
    		
	        if (url.startsWith("http")) {
	            view.loadUrl(url);
//	            ServiceInterface.reportAdActionResult(entity.adId, StatusCode.RESULT_TYPE_CLICK_CONTENT, reportJson, JPush.mApplicationContext);
	        } else if(null != url && (url.startsWith("smsto") || url.startsWith("mailto"))){
	            int index = url.lastIndexOf(PARAM_DIRECT);
	            if (index < 0 && !url.startsWith("mailto")){
	            	if (url.indexOf("?") > 0) {
	            		url += "&direct=false";
	            	} else {
	            		url += "?direct=false";
	            	}
	            	index = url.lastIndexOf(PARAM_DIRECT);
	            }
	            String direct = url.substring(index + PARAM_DIRECT.length());
	            boolean isDirect = false;
	            try {
	            	isDirect = Boolean.parseBoolean(direct);
	            } catch (Exception e) {
	            }
	            
	            index = url.indexOf("?");
	            String uri = url.substring(0, index);
	            String queryString = url.substring(index); 
	            
	            LogUtil.v(TAG, "Uri: " + uri);
	            LogUtil.v(TAG, "QueryString: " + queryString);
	            
	            Intent intent = null;
	            if (uri.startsWith("tel")) {
	            	if (!AndroidUtil.hasPermission(context, permission.CALL_PHONE)) {
	            		LogUtil.d(TAG, "No CALL_PHONE permission");
	            		return true;
	            	}
	                if (isDirect) {
	                    intent = new Intent(Intent.ACTION_CALL, Uri.parse(uri));
	                } else {
	                    intent = new Intent(Intent.ACTION_DIAL, Uri.parse(uri));
	                }
	                
	            } else if (uri.startsWith("smsto")) {
	            	if (!AndroidUtil.hasPermission(context, permission.SEND_SMS)) {
	            		LogUtil.d(TAG, "No SEND_SMS permission");
	            		return true;
	            	}
	            	
	                int fIndex = queryString.indexOf(PARAM_CONTENT) + PARAM_CONTENT.length();
	                int lIndex = queryString.lastIndexOf(PARAM_DIRECT) - 1;
	                String content = queryString.substring(fIndex, lIndex);
	                if (isDirect) {
	                    SmsManager smsManage = SmsManager.getDefault();
	                    List<String> all = smsManage.divideMessage(content);
	                    String[] params = uri.split(":");
	                    if (params.length == 2) {
	                        String phoneNumStr = params[1];
	                        if (!TextUtils.isEmpty(phoneNumStr)) {
	                            for (String sms : all) {
	                                smsManage.sendTextMessage(phoneNumStr, null, sms, null, null);
	                            }
	                        }
	                    }
	                } else {
	                    intent = new Intent(Intent.ACTION_SENDTO, Uri.parse(uri));
	                    intent.putExtra("sms_body", content);
	                }
	                
	            } else if (uri.startsWith("mailto")) {
	                String[] temp = uri.split(":"); 
	                if(null != temp && temp.length == 2){
	                    final String param = "&content=";
	                    int fIndex = queryString.indexOf(PARAM_TITLE) + PARAM_TITLE.length();
	                    int lIndex = queryString.indexOf(param);
	                    String title = queryString.substring(fIndex, lIndex);
	                    String content = queryString.substring(lIndex + param.length());
	                    String[] emailReciver = new String[]{temp[1]};
	                    intent = new Intent(Intent.ACTION_SEND);
	                    intent.setType("plain/text");
	                    intent.putExtra(android.content.Intent.EXTRA_EMAIL, emailReciver);
	                    intent.putExtra(android.content.Intent.EXTRA_SUBJECT, title);
	                    intent.putExtra(android.content.Intent.EXTRA_TEXT, content);
	                }
	            }
	            
	            if(intent != null){
	                context.startActivity(intent);
	            }
//	            ServiceInterface.reportAdActionResult(entity.adId, StatusCode.RESULT_TYPE_CLICK_CONTENT, reportJson, JPush.mApplicationContext);
	            return true;
	        }
	        return false;
    	} catch (Exception e) {
    		LogUtil.e(TAG, "Invalid url");
			return true;
		}
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
    }

    @Override
    public void onLoadResource(WebView view, String url) {
        super.onLoadResource(view, url);
    }

}
