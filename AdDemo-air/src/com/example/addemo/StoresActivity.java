package com.example.addemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

public class StoresActivity extends Activity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stores);
        
        initStores();
    }
    
    private void initStores() {
        GridView gridview = (GridView) findViewById(R.id.stores);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.stores, R.layout.grid_item);
        gridview.setAdapter(adapter);
        
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                String storeName = String.valueOf(((TextView) view).getText());
                Intent intent = new Intent(StoresActivity.this, CouponsActivity.class);
                intent.putExtra("store_name", storeName);
                startActivity(intent);
            }
        
        });
        
        Button backButton = (Button) findViewById(R.id.button_stores_back);
        backButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
            
        });
    }
}
