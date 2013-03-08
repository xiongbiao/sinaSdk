package com.kk.msg.ui;

import android.content.Context;
import android.graphics.Color;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.kk.msg.R;

public class PlayView extends LinearLayout{

	private Context mContext;
	private int width;
	private int height;
	
	public PlayView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		mContext = context;
		initCofig();
		initIcon();
		initPlayProgress();
	}
	
	private void initPlayProgress() {
		// TODO Auto-generated method stub
		LinearLayout playLayout = new LinearLayout(mContext);
		ProgressBar pb = new ProgressBar(mContext);
		pb.setMax(100);
		pb.setScrollBarStyle(android.R.style.Widget_ProgressBar_Horizontal);
		LayoutParams iconParams = new LayoutParams(dip2px(mContext,100),dip2px(mContext,10)); 	
		addView(pb,iconParams);
	}

	private void initIcon() {
		// TODO Auto-generated method stub
		LayoutParams iconParams = new LayoutParams(dip2px(mContext,72),dip2px(mContext,72));
		ImageView icon = new ImageView(mContext);
		icon.setImageResource(R.drawable.ic_launcher);
		icon.setScaleType(ScaleType.FIT_XY);
		addView(icon,iconParams);
	}

	private void initCofig(){
		 setOrientation(LinearLayout.HORIZONTAL);
		 width = mContext.getResources().getDisplayMetrics().widthPixels;
		 height = mContext.getResources().getDisplayMetrics().heightPixels;
		 setPadding(5, 5, 5, 5);
		 setBackgroundColor(Color.rgb(117, 117, 117));
		 LayoutParams playParams = new LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
		 setLayoutParams(playParams);
	}
	
	 private int dip2px(Context context, float dpValue){
	        final float scale = context.getResources().getDisplayMetrics().density;
	        return (int) (dpValue * scale);
	 }
}
