package com.me.challange.milan.challangeme;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PaytmActivity extends AppCompatActivity {
    String amt,coins;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paytm);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        amt=getIntent().getStringExtra("amt");
        coins=getIntent().getStringExtra("coins");
        coins=coins.replace(",", "");


        TextView toolbarNAme=(TextView)findViewById(R.id.paytm_toolbar_name);
        Typeface type=Typeface.createFromAsset(getAssets(),"font/regular.ttf");
        toolbarNAme.setTypeface(type);

        ImageView backImage=(ImageView)findViewById(R.id.paytm_back_icon);
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slid_in_left,R.anim.slide_out_right);
            }
        });

    }

}
