package com.me.challange.milan.challangeme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.client.Firebase;
import com.mikhaellopez.circularimageview.CircularImageView;

public class LoginDetailsActivity extends AppCompatActivity {
String facebookId,facebookToken,getName,getImageUrl,getGender;
    Firebase ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_details);
        Firebase.setAndroidContext(this);

        ref=new Firebase("https://challange-me.firebaseio.com/");

        facebookId=getIntent().getStringExtra("facebookId");
        facebookToken=getIntent().getStringExtra("facebookToken");
        getName=getIntent().getStringExtra("name");
        getImageUrl=getIntent().getStringExtra("imageUrl");
        getGender=getIntent().getStringExtra("gender");

        Log.i("id",facebookId);
        Log.i("token",facebookToken);

        Typeface type= Typeface.createFromAsset(getAssets(),"font/regular.ttf");

        final EditText name=(EditText)findViewById(R.id.nick_name);
        final EditText gender=(EditText)findViewById(R.id.gender);
        Button saveDataButton=(Button)findViewById(R.id.save_login_date_button);
        TextView toolBarName=(TextView)findViewById(R.id.login_detailsToolbarTitleName);
        ImageView backButton=(ImageView)findViewById(R.id.login_details_back_icon);
        CircularImageView imageView=(CircularImageView)findViewById(R.id.challanger_user_face_image);

        name.setTypeface(type);
        gender.setTypeface(type);
        saveDataButton.setTypeface(type);
        toolBarName.setTypeface(type);

        name.setText(getName);
        gender.setText(getGender);
        Log.i("image", getImageUrl);

        Glide.with(this).load(getImageUrl)
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);


        saveDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nickName = name.getText().toString().trim();
                String sex = gender.getText().toString().trim();
                if (nickName.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter nick name", Toast.LENGTH_LONG).show();
                } else if (sex.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "please enter your gender", Toast.LENGTH_LONG).show();
                } else {
                    finish();
                    Intent intent = new Intent(LoginDetailsActivity.this, TagsSelectorActivity.class);
                    intent.putExtra("facebookId",facebookId);
                    intent.putExtra("facebookToken",facebookToken);
                    intent.putExtra("name",nickName);
                    intent.putExtra("gender",sex);
                    intent.putExtra("imageUrl",getImageUrl);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent=new Intent(LoginDetailsActivity.this,LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slid_in_left,R.anim.slide_out_right);
            }
        });
    }

}
