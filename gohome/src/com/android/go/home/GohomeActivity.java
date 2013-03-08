package com.android.go.home;

import java.util.Date;

import android.app.Activity;
import android.os.Bundle;

public class GohomeActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        SqlHelp s = new SqlHelp(getApplicationContext());
        s.insertData(getApplicationContext(), new Date().toLocaleString());
        s.selectAll(getApplicationContext());
    }
    
    public void c(){
    	
    }
    
    /**
     * view
     */
    
}