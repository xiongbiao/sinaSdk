package com.kk.msg.ui;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;


/**
 * Need reference images from assets:    star_0, star_1, def_icon, def_img
 */
public class MFullView extends LinearLayout {

	private LinearLayout.LayoutParams rParams;
	private LinearLayout.LayoutParams fillParams;
	private LinearLayout.LayoutParams wrapParams;
	private LinearLayout.LayoutParams fillwrapParams;
	private ImageView mIvd;
	private ImageView mIvInfo;
	private ImageView mIvIcon;
	private ImageView mIvX;
	private ImageView mIvClose;
	private ImageView mTopRightButton;
	private Context mContext; 
	private InputStream is = null;
	private BitmapDrawable bd = null;
	private int width;
	private int height;
	private LinearLayout mInfoView;
	private LinearLayout mInfoRightView;
	

	public MFullView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public MFullView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public MFullView(Context context , AdR adR) {
		super(context);
		// TODO Auto-generated constructor stub
		mContext = context;
		width = context.getResources().getDisplayMetrics().widthPixels;
		height = context.getResources().getDisplayMetrics().heightPixels;
		Log.v("XB", " height: "+ height   + " width: "+ width );
		initView();
		
	}
       
	private void initView(){
		
        rParams = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
        fillParams = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
        wrapParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        fillwrapParams = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
        setOrientation(LinearLayout.VERTICAL);
        
        //设置背景图
       
        addCloseView();
        //top 设置
        addInfoImageView();
        addInfoView();
        infoTvView();
        downloadImagView();
                 
	}
	
	private void addCloseView(){
		try {
			LinearLayout mTopRightView = new LinearLayout(mContext);
			mTopRightView.setGravity(Gravity.RIGHT);
			
			mTopRightButton = new ImageView(mContext);
			is = mContext.getResources().getAssets().open("back.png");
	        bd = new BitmapDrawable(is);
	        mTopRightButton.setImageDrawable(bd);
	        is.close();
	        mTopRightButton.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View v) {
	                 Log.d("XB", "点击返回----");
	            }
	        });
	        
			rParams = new LinearLayout.LayoutParams(dip2px(mContext, 30), dip2px(mContext, 30));
	        rParams.rightMargin = dip2px(mContext, 15);
	        rParams.topMargin = dip2px(mContext, 5);
	        rParams.bottomMargin = dip2px(mContext, 5);
	        mTopRightView.addView(mTopRightButton,rParams);
	          
	        addView(mTopRightView, fillwrapParams);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	private void addInfoImageView(){
		try {
			mIvInfo = new ImageView(mContext);
			is = mContext.getResources().getAssets().open("app1.jpg");
	        bd = new BitmapDrawable(is);
	        mIvInfo.setImageDrawable(bd);
	        mIvInfo.setScaleType(ScaleType.FIT_XY);
	        is.close();
	        mIvInfo.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View v) {
	                 Log.d("XB", "点击详情----");
	            }
	        });
			rParams = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, dip2px(mContext, height/5));
//	        rParams.addRule(RelativeLayout.CENTER_IN_PARENT , RelativeLayout.TRUE);
	        rParams.leftMargin = dip2px(mContext, width/18);
		    rParams.rightMargin = dip2px(mContext, width/18);
		    rParams.topMargin = dip2px(mContext, 5);
	        addView(mIvInfo, rParams);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	private void addInfoView(){
		try {
			is = mContext.getResources().getAssets().open("app1.jpg");
	        bd = new BitmapDrawable(is);
			
			mInfoView = new LinearLayout(mContext);
			mInfoView.setOrientation(LinearLayout.HORIZONTAL);
//			ImageView icon = new ImageView(mContext); 
	       
	        is.close();
	        mInfoView.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View v) {
	                 Log.d("XB", "点击详情----");
	            }
	        });
//	        rParams.addRule(RelativeLayout.CENTER_IN_PARENT , RelativeLayout.TRUE);
//	        rParams.topMargin = dip2px(mContext, height/15);
//			mInfoView.addView(icon, rParams);
//	        addView(mInfoView, rParams);
	        
	        
	        rParams = new LinearLayout.LayoutParams(dip2px(mContext, width*8/9), dip2px(mContext, height/7));
//	        rParams.addRule(RelativeLayout.CENTER_IN_PARENT , RelativeLayout.TRUE);
	        rParams.leftMargin = dip2px(mContext, width/18);
	        rParams.rightMargin = dip2px(mContext, width/18);
	        
	        mInfoView = new LinearLayout(mContext);
	        mInfoView.setPadding(0, 5, 5, 5);
	        mInfoView.setOrientation(LinearLayout.HORIZONTAL);
	        mInfoView.setGravity(Gravity.CENTER);
			
			
			LayoutParams iconParams = new LayoutParams(dip2px(mContext,72),dip2px(mContext,72));
			ImageView icon = new ImageView(mContext);
			icon.setImageDrawable(bd);
			icon.setScaleType(ScaleType.FIT_XY);
			mInfoView.addView(icon, iconParams);
			
			LinearLayout.LayoutParams infoRightParams = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT);
			LayoutParams wrapParams = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
			LayoutParams fillWrapParams = new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT);
			
			mInfoRightView = new LinearLayout(mContext);
			mInfoRightView.setOrientation(LinearLayout.VERTICAL);
			mInfoRightView.setGravity(Gravity.CENTER);
//			mInfoRightView.setPadding(0, 5, 5, 5);
			
			LayoutParams tvTopTitleParams = new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT);
		    TextView	tvTopTitle = new TextView(mContext);
			tvTopTitle.setMaxEms(10);
			tvTopTitle.setMaxLines(1);
			tvTopTitle.setSingleLine(true);
			tvTopTitle.setTextColor(Color.WHITE);
			tvTopTitle.setPadding(15, 0, 0, 0);
			tvTopTitle.setTextSize(16);
			tvTopTitle.setText("炫影时空 ");
			mInfoRightView.addView(tvTopTitle,tvTopTitleParams);
			
			
			
			
			TextView	tvTopInfo = new TextView(mContext);
			tvTopInfo.setMaxEms(10);
			tvTopInfo.setMaxLines(1);
			tvTopInfo.setSingleLine(true);
			tvTopInfo.setTextColor(Color.WHITE);
			tvTopInfo.setPadding(15, 0, 0, 0);
			tvTopInfo.setTextSize(12);
			tvTopInfo.setText("版本:1.0 大小:0.2 MB ");
			
			mInfoRightView.addView(tvTopInfo,tvTopTitleParams);
			
			
			TextView	tvDInfo = new TextView(mContext);
			tvDInfo.setMaxEms(10);
			tvDInfo.setMaxLines(1);
			tvDInfo.setSingleLine(true);
			tvDInfo.setTextColor(Color.WHITE);
			tvDInfo.setPadding(15, 0, 0, 0);
			tvDInfo.setTextSize(12);
			tvDInfo.setText("下载：123271次");
			
			mInfoRightView.addView(tvDInfo,tvTopTitleParams);
			
			LayoutParams xingParams = new LayoutParams(dip2px(mContext,80),dip2px(mContext,15));
			ImageView xing = new ImageView(mContext);
			is = mContext.getResources().getAssets().open("xing.png");
	        bd = new BitmapDrawable(is);
	        xing.setImageDrawable(bd);
	        xing.setScaleType(ScaleType.FIT_XY);
	        xingParams.leftMargin = dip2px(mContext,-120);
	        mInfoRightView.addView(xing, xingParams);
	        
			mInfoView.addView(mInfoRightView,infoRightParams);
			addView(mInfoView, rParams);
	        
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	
	private void infoTvView(){
		TextView tvInfo = new TextView(mContext);
		tvInfo.setMaxEms(10);
		tvInfo.setTextColor(Color.WHITE);
		tvInfo.setPadding(0, 0, 0, 0);
		tvInfo.setTextSize(12);
		tvInfo.setText("《明珠轩辕》是掌上明珠首款无职业锁定网游，突破现有任何游戏极限大作，您可以成为法师中最强的战士，刺客中的五行术士，完全脱离固有游戏模式。更有上古珍兽坐骑、金身炼化功能等功能给你耳目一新的感觉。2012年宏大上古神话游戏巨作，再现古代神话山海经的壮阔诗篇务再现古代神话山海经的壮阔诗篇务。突破现有任何游戏极限大作，您可以成为法师中最强的战士，刺客中的五行术士，完全脱离固有游戏模式。更有上古珍兽坐骑、金身炼化功能等功能给你耳目一新的感觉。2012年宏大上古神话游戏巨作，再现古代神话山海经的壮阔诗篇务再现古代神话山海经的壮阔诗篇务。");
		rParams = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, dip2px(mContext, width/4));
	    rParams.leftMargin = dip2px(mContext, width/18);
	    rParams.rightMargin = dip2px(mContext, width/18);
		addView(tvInfo, rParams);
	}
	
	
	private void downloadImagView(){
		try {
			Button bottomButton = new Button(mContext);
            bottomButton.setTextColor(Color.WHITE);
            is = mContext.getResources().getAssets().open("download.png");
            bd = new BitmapDrawable(is);
            bottomButton.setBackgroundDrawable(bd);
            is.close();
            
            bottomButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                	 Log.d("XB", "点击下载----");
                }
            });
            
            rParams = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
            rParams.bottomMargin = dip2px(mContext, 3);
            addView(bottomButton, rParams);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	 private int dip2px(Context context, float dpValue) {
	        final float scale = context.getResources().getDisplayMetrics().density;
	        return (int) (dpValue * scale);
	    }
}
