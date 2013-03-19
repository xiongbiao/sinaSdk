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

public class CouponsActivity extends Activity {
    private String mStoreName;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupons);
        
        initCoupons();
    }
    
    private void initCoupons() {
        Intent intent = getIntent();
        mStoreName = intent.getExtras().getString("store_name");
        TextView storeName = (TextView) findViewById(R.id.store_name);
        storeName.setText(mStoreName);
        
        GridView gridview = (GridView) findViewById(R.id.coupons);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.coupons, R.layout.grid_item);
        gridview.setAdapter(adapter);
        
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                Intent intent = new Intent(CouponsActivity.this, CouponDetailActivity.class);
                intent.putExtra("coupon_title", mStoreName + ((TextView) view).getText());
                startActivity(intent);
            }
        
        });
        
        Button backButton = (Button) findViewById(R.id.button_coupons_back);
        backButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
            
        });
    }
}
