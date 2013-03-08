package com.sin180.android.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sin180.android.sendmsg.FileUtil;
import com.sin180.android.sendmsg.MsgVo;
import com.sin180.android.sendmsg.R;

public class MsgAdapter extends BaseAdapter {


	private Context mContext;
	private int mResource;
	private List<MsgVo> mData = new ArrayList<MsgVo>();
	
	public MsgAdapter(Context context , int resource , List<MsgVo> dataList){
		mContext = context;
		mResource = resource ;
		mData = dataList;
	   // dgidc221.221
	}

	@Override
	public int getCount() {
		if(mData == null)
			return 0;
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		if(mData == null || mData.size() == 0 || mData.get(position) == null)
			return null;
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		 Holder holder = null;
		if(convertView==null)
		{
			convertView=LayoutInflater.from(mContext).inflate(mResource, null);
			holder=new Holder();
			holder.title=(TextView) convertView.findViewById(R.id.item_title);
			holder.author=(TextView) convertView.findViewById(R.id.item_author);
			holder.level=(TextView) convertView.findViewById(R.id.item_level);
			holder.number=(TextView) convertView.findViewById(R.id.item_number);
			holder.call=(Button) convertView.findViewById(R.id.item_call);
			holder.msg=(Button) convertView.findViewById(R.id.item_sendmsg);
			holder.msgEdit=(EditText) convertView.findViewById(R.id.item_msg);
			holder.tag = position;
			convertView.setTag(holder);
		}else
		{
			holder=(Holder) convertView.getTag();
			holder.title.setText("");
			holder.author.setText("");
			holder.level.setText("");
			holder.number.setText("");
			holder.tag = position;
		}
       String msgType = mData.get(position).getMsgType();
		holder.author.setText("time:"+mData.get(position).getMsgTime());
		holder.level.setText( mData.get(position).getMsgData());
		holder.number.setText(msgType);
		if("2".equals(msgType)||"1-1".equals(msgType)){
//			holder.call.setVisibility(View.GONE);
//			holder.msg.setVisibility(View.GONE);
		}
		final EditText temp = holder.msgEdit;
		temp.setText(mData.get(position).getMsgData());
		final String send  = mData.get(position).getMsgSend();
		holder.title.setText("send:"+send);
		if(!TextUtils.isEmpty(send)){
			holder.msg.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					try {
						final String msgE = temp.getText().toString();
						Log.i("XB", "msgE : ---->>>>"+msgE);
						if(TextUtils.isEmpty(msgE)){
							Toast.makeText(mContext, "msg is null", Toast.LENGTH_SHORT).show();
							return;
						}
						sendSMS(send, msgE);
						FileUtil.writeLogtoFile(send,"2",msgE);
					} catch (Exception e) {
					}
				}
			});
			
            holder.call.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
				}
			});
		}
		return convertView;
	}
	
	class Holder{
		int tag;
		TextView title;
		TextView author;
		TextView level;
		TextView number;
		Button call;
		Button msg;
		EditText msgEdit;
	}

	private static void sendSMS(String phoneNumber, String message) {
		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(phoneNumber, null, message, null, null);
	}
}
