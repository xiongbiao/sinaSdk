package com.example.addemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

public class MapsActivity extends Activity {
    private final static String URL_AIRPORT = "file:///android_asset/map_airport.html";
    private final static String URL_T2 = "file:///android_asset/map_t2.html";
    
    private WebView mMapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        
        init();
    }

    private void init() {
        mMapView = (WebView) findViewById(R.id.map_view);
        mMapView.getSettings().setJavaScriptEnabled(true);
        mMapView.addJavascriptInterface(new WebAppInterface(this), "Android");
        mMapView.getSettings().setBuiltInZoomControls(true);
        mMapView.loadUrl(URL_AIRPORT);
        
        Button backButton = (Button) findViewById(R.id.button_map_back);
        backButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mMapView.getOriginalUrl().equals(URL_AIRPORT)) {
                    finish();
                } else if (mMapView.getOriginalUrl().equals(URL_T2)) {
                    if (mMapView.canGoBack()) {
                        mMapView.goBack();
                    }
                }
            }
            
        });
    }
    
    private class WebAppInterface {
        Context mContext;

        /** Instantiate the interface and set the context */
        WebAppInterface(Context c) {
            mContext = c;
        }

        /** Show a toast from the web page */
        public void onClick(String id) {
            if (id.equals("t2")) {
                mMapView.loadUrl(URL_T2);
            } else if (id.equals("shit")) {
                Intent intent = new Intent(MapsActivity.this, CouponsActivity.class);
                intent.putExtra("store_name", MapsActivity.this.getResources().getStringArray(R.array.stores)[0]);
                startActivity(intent);
            }
        }
    }
}
