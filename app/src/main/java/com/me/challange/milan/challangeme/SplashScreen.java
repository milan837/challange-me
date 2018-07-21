package com.me.challange.milan.challangeme;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Typeface type=Typeface.createFromAsset(getAssets(),"font/regular.ttf");

        TextView name=(TextView)findViewById(R.id.app_name_spashscreen);
        name.setTypeface(type);

         new Thread(){
             @Override
             public void run() {
                 try {
                     sleep(500);
                     SharedPreferences sharedPreferences=getSharedPreferences("user", Context.MODE_PRIVATE);
                     String facebookId=sharedPreferences.getString("facebookId",null);
                     if(facebookId == null){
                         Intent intent=new Intent(SplashScreen.this,LoginActivity.class);
                         startActivity(intent);
                         overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                         finish();
                     }else{
                         Intent intent=new Intent(SplashScreen.this,MainActivity.class);
                         startActivity(intent);
                         overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                         finish();
                     }



                 } catch (InterruptedException e) {
                     e.printStackTrace();
                 }
             }
         }.start();
    }

}
