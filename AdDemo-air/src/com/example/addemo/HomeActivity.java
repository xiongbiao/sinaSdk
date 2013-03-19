package com.example.addemo;

import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.VideoView;

public class HomeActivity extends Activity 
        implements View.OnClickListener,
        MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener {
    
    private VideoView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        
        initAdPlayer();
        initLanguagePicker();
        initCityPicker();
        initDate();
        initLaunchPanel();
    }
    
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
//        startPlayer();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        cleanUp();
        super.onDestroy();
    }

    private void initAdPlayer() {
        mAdView = (VideoView) findViewById(R.id.ad_view);
        mAdView.setOnPreparedListener(this);
        mAdView.setOnCompletionListener(this);
        
    }
    
    private void initLanguagePicker() {
        Spinner languagePicker = (Spinner) findViewById(R.id.language_picker);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.languages, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languagePicker.setAdapter(adapter);
        languagePicker.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                    int position, long id) {
                Configuration config = getResources().getConfiguration();
                if (position == 0) {
                    config.locale = Locale.ENGLISH;
                } else {
                    config.locale = Locale.SIMPLIFIED_CHINESE;
                }
                getResources().updateConfiguration(config, getResources().getDisplayMetrics());
                updateScreenLanguage();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
                
            }
            
        });
        Locale currentLocale = getResources().getConfiguration().locale;
        if (currentLocale.equals(Locale.SIMPLIFIED_CHINESE)) {
            languagePicker.setSelection(1);
        }
    }
    
    private void updateScreenLanguage() {
        refreshCityPicker();
        refreshWeather();
        refreshDate();
        refreshLaunchPanel();
    }
    
    private void initCityPicker() {
        Spinner cityPicker = (Spinner) findViewById(R.id.city_picker);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.cities, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cityPicker.setAdapter(adapter);
    }
    
    private void refreshCityPicker() {
        Spinner cityPicker = (Spinner) findViewById(R.id.city_picker);
        int selection = cityPicker.getSelectedItemPosition();
        initCityPicker();
        cityPicker.setSelection(selection);
    }
    
    private void refreshWeather() {
        TextView weather = (TextView) findViewById(R.id.weather);
        weather.setText(R.string.weather);
    }
    
    private void initDate() {
        TextView dateTime = (TextView) findViewById(R.id.date_time);
        dateTime.setText(android.text.format.DateFormat.getDateFormat(this).format(new Date()));
    }
    
    private void refreshDate() {
        TextView dateTime = (TextView) findViewById(R.id.date_time);
        dateTime.setText(android.text.format.DateFormat.getDateFormat(this).format(new Date()));
    }
    
    private void initLaunchPanel() {
        Button button = (Button) findViewById(R.id.button_maps);
        button.setOnClickListener(this);
        button = (Button) findViewById(R.id.button_coupons);
        button.setOnClickListener(this);
    }
    
    private void refreshLaunchPanel() {
        Button button = (Button) findViewById(R.id.button_maps);
        button.setText(R.string.maps);
        button = (Button) findViewById(R.id.button_coupons);
        button.setText(R.string.coupons);
        button = (Button) findViewById(R.id.button_ads);
        button.setText(R.string.ads);
        button = (Button) findViewById(R.id.button_games);
        button.setText(R.string.games);
    }
    
    private void cleanUp() {
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        mAdView.start();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mAdView.start();
    }

    private void startPlayer() {
        try {
            mAdView.setVideoURI(Uri.parse("android.resource://com.example.addemo/raw/sample"));
            mAdView.start();
        } catch (Exception e) {
            Log.d("error", e.toString());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.button_maps:
            startActivity(new Intent(this, MapsActivity.class));
            break;
        case R.id.button_coupons:
            startActivity(new Intent(this, StoresActivity.class));
            break;
        }
    }
}
