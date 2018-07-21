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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.client.Firebase;
import com.mikhaellopez.circularimageview.CircularImageView;

public class MakeOpenChallange extends AppCompatActivity {
    String challangeType,subject,name,myCoines,myLevel,myGames,imageUrl;
    public static MakeOpenChallange instance=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_open_challange);
        Firebase.setAndroidContext(this);

        instance=this;

        SharedPreferences sharedPreferences=getSharedPreferences("user", Context.MODE_PRIVATE);
        name=sharedPreferences.getString("name",null);
        myCoines=sharedPreferences.getString("coins",null);
        myLevel=sharedPreferences.getString("level",null);
        myGames=sharedPreferences.getString("totalMatch",null);
        imageUrl=sharedPreferences.getString("imageUrl",null);



        challangeType=getIntent().getStringExtra("type");
        subject=getIntent().getStringExtra("subject");

        Toast.makeText(getApplicationContext(),challangeType,Toast.LENGTH_LONG).show();
        Typeface type= Typeface.createFromAsset(getAssets(),"font/regular.ttf");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView toolbarName=(TextView)findViewById(R.id.make_open_ChallangeToolbarTitleName);
        toolbarName.setTypeface(type);


        ImageView backButton=(ImageView)findViewById(R.id.make_open_challange_back_icon);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slid_in_left,R.anim.slide_out_right);
            }
        });

        TextView level=(TextView)findViewById(R.id.make_open_challanger_user_total_levels_text);
        final TextView coins=(TextView)findViewById(R.id.make_open_challanger_user_coines_text);
        TextView totalChallanges=(TextView)findViewById(R.id.make_open_challanger_user_total_challange_face_text);
        TextView makeChallangeTitleName=(TextView)findViewById(R.id.make_open_challanger_user_name_textView);
        TextView note=(TextView)findViewById(R.id.note_text);
        final EditText takeCoins=(EditText)findViewById(R.id.make_open_take_coins_edit_text);
        Button challangeButton=(Button)findViewById(R.id.make_open_makeChallangeButton);
        TextView titleSecondLayout=(TextView)findViewById(R.id.make_open_set_coin_for_challanger_textview);
        TextView tag=(TextView)findViewById(R.id.open_challange_tag_on_subject_text_view);
        CircularImageView imageView=(CircularImageView)findViewById(R.id.make_open_challanger_user_face_image);

        level.setTypeface(type);
        coins.setTypeface(type);
        totalChallanges.setTypeface(type);
        makeChallangeTitleName.setTypeface(type);
        takeCoins.setTypeface(type);
        challangeButton.setTypeface(type);
        note.setTypeface(type);
        titleSecondLayout.setTypeface(type);
        tag.setTypeface(type);

        makeChallangeTitleName.setText(name);
        coins.setText(myCoines);
        level.setText(myLevel);
        totalChallanges.setText(myGames);
        tag.setText(subject);

        Glide.with(getApplicationContext()).load(imageUrl).into(imageView);


        challangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getCoins=takeCoins.getText().toString().trim();
                String totalCoins=coins.getText().toString().trim();
                if(getCoins.isEmpty()){
                    Toast.makeText(getApplicationContext(), "please enter coins", Toast.LENGTH_LONG).show();
                }else{
                    int userCoins=Integer.valueOf(totalCoins);
                    int bitCoins=Integer.valueOf(getCoins);
                    //only 100 coins challange can be played
                    if(bitCoins <= 100){
                        if(bitCoins <= userCoins){
                            Intent intent=new Intent(MakeOpenChallange.this,GiveChallangeActivity.class);
                            intent.putExtra("type",challangeType);
                            intent.putExtra("subject",subject);
                            intent.putExtra("coines",getCoins);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                        }else{
                            Toast.makeText(getApplicationContext(),"you dont have "+bitCoins+" coins to challange ",Toast.LENGTH_LONG).show();
                        }

                    }else{
                        Toast.makeText(getApplicationContext(),"you can  only make a challange less than 100 coins ",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    public static MakeOpenChallange getInstance(){
        return instance;
    }

}
