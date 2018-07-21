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
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.me.challange.milan.challangeme.GetterSetter.UserLoginList;
import com.mikhaellopez.circularimageview.CircularImageView;

public class MakeChallangeActivity extends AppCompatActivity {
String challangeType,subject,cfacebookId;
    Firebase ref;
    public static MakeChallangeActivity instance=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_challange);
        instance=this;

        Firebase.setAndroidContext(this);
        ref=new Firebase("https://challange-me.firebaseio.com/");
        Firebase childRef=ref.child("user_login");

        challangeType=getIntent().getStringExtra("type");
        subject=getIntent().getStringExtra("subject");
        cfacebookId=getIntent().getStringExtra("facebookId");

        Toast.makeText(getApplicationContext(),challangeType,Toast.LENGTH_LONG).show();
        Typeface type= Typeface.createFromAsset(getAssets(),"font/regular.ttf");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView toolbarName=(TextView)findViewById(R.id.nmakeChallangeToolbarTitleName);
        toolbarName.setTypeface(type);

        ImageView backButton=(ImageView)findViewById(R.id.nomakeChallange_back_icon);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slid_in_left,R.anim.slide_out_right);
            }
        });

        final TextView level=(TextView)findViewById(R.id.challanger_user_total_levels_text);
        final TextView coins=(TextView)findViewById(R.id.challanger_user_coines_text);
        final TextView totalChallanges=(TextView)findViewById(R.id.challanger_user_total_challange_face_text);
        TextView makeChallangeTitleName=(TextView)findViewById(R.id.set_coin_for_challanger_textview);
        final EditText takeCoins=(EditText)findViewById(R.id.take_coins_edit_text);
        TextView challangeOnText=(TextView)findViewById(R.id.make_challanger_on_textView);
        final TextView username=(TextView)findViewById(R.id.make_challanger_username_textView);
        Button challangeButton=(Button)findViewById(R.id.makeChallangeButton);
        final CircularImageView imageView=(CircularImageView)findViewById(R.id.challanger_user_face_image);
        TextView tag=(TextView)findViewById(R.id.make_challange_tag_on_subject_text_view);

        level.setTypeface(type);
        coins.setTypeface(type);
        totalChallanges.setTypeface(type);
        makeChallangeTitleName.setTypeface(type);
        takeCoins.setTypeface(type);
        challangeButton.setTypeface(type);
        username.setTypeface(type);
        challangeOnText.setTypeface(type);
        tag.setTypeface(type);

        tag.setText(subject);
        childRef.orderByChild("facebookId").equalTo(cfacebookId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds:dataSnapshot.getChildren()) {
                    UserLoginList list=ds.getValue(UserLoginList.class);

                    username.setText(list.getName());
                    coins.setText(list.getCoins());
                    totalChallanges.setText(list.getTotalChallange());
                    level.setText(list.getLevel());

                    Glide.with(getApplicationContext()).load(list.getImageUrl()).into(imageView);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        challangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);

                String myCoins = sharedPreferences.getString("coins",null);
                String getCoins = takeCoins.getText().toString().trim();


                if (getCoins.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "please enter coins", Toast.LENGTH_LONG).show();
                } else {
                    int userCoins = Integer.valueOf(myCoins);
                    int bitCoins = Integer.valueOf(getCoins);
                    if(bitCoins <= 100){
                        if (bitCoins <= userCoins) {
                            Intent intent = new Intent(MakeChallangeActivity.this, GiveChallangeActivity.class);
                            intent.putExtra("type", challangeType);
                            intent.putExtra("subject",subject);
                            intent.putExtra("facebookId",cfacebookId);
                            intent.putExtra("coines",getCoins);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        } else {
                            Toast.makeText(getApplicationContext(), "you dont have " + bitCoins + " coins to challange ", Toast.LENGTH_LONG).show();
                        }

                    }else{
                        Toast.makeText(getApplicationContext(),"you can  only make a challange less than 100 coins ",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    public static MakeChallangeActivity getInstance(){
        return instance;
    }

}
