package com.sin180.android.sendmsg;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;
 
public class SMS_Receiver extends BroadcastReceiver {
 
	private String TAG = "!!!!!!";
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		System.out.println("收到短信");
		 String action = intent.getAction();  
		 Log.d(TAG, "action : "+action);
		if(action.equals("android.provider.Telephony.SMS_RECEIVED")){
			Object[] pdus=(Object[])intent.getExtras().get("pdus");
			//不知道为什么明明只有一条消息，传过来的却是数组，也许是为了处理同时同分同秒同毫秒收到多条短信
			//但这个概率有点小
			SmsMessage[] message=new SmsMessage[pdus.length];
			StringBuilder sb=new StringBuilder();
			System.out.println("pdus长度"+pdus.length);
			for(int i=0;i<pdus.length;i++){
				//虽然是循环，其实pdus长度一般都是1
				message[i]=SmsMessage.createFromPdu((byte[])pdus[i]);
				sb.append("接收到短信来自:\n");
				sb.append(message[i].getDisplayOriginatingAddress()+"\n");
				sb.append("内容:"+message[i].getDisplayMessageBody());
				FileUtil.writeLogtoFile(message[i].getDisplayOriginatingAddress(),1+"-1",message[i].getDisplayMessageBody() );
//				startService(new Intent(TestMsgActivity.this, BootService.class));   
			}
			System.out.println(sb.toString());
            Toast.makeText(context, sb.toString(), Toast.LENGTH_SHORT);
		  }else if (Intent.ACTION_SCREEN_ON.equals(action))
          {  
               Log.d(TAG, "screen is on..sisisi.");  
          }
          else if(Intent.ACTION_SCREEN_OFF.equals(action))
          {  
               Log.d(TAG, "screen is off...");  
          }  
          
          else if(Intent.ACTION_USER_PRESENT.equals(action))
          {
        	  Intent it = new Intent(context, BootService.class );
        	  context.startService(it);
       	      Log.d(TAG, "screen is unlock..."); 
          }
		
	}
 
}