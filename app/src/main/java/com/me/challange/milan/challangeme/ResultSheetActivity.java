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
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.me.challange.milan.challangeme.Adapter.FaceChallangeAnswerSheetAdapter;
import com.me.challange.milan.challangeme.GetterSetter.UserLoginList;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ResultSheetActivity extends AppCompatActivity {
    List<String> question,firstL,secondL,thirdL,fourthL,answer,givenAnswer;
    String totallPoints,totalTime,totalQA,coines,winnerId,match_result,key,challangeType;
    Firebase ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_sheet);

        Firebase.setAndroidContext(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ref=new Firebase("https://challange-me.firebaseio.com/");

        question=(ArrayList<String>)getIntent().getSerializableExtra("question");
        firstL=(ArrayList<String>)getIntent().getSerializableExtra("firstL");
        secondL=(ArrayList<String>)getIntent().getSerializableExtra("secondL");
        thirdL=(ArrayList<String>)getIntent().getSerializableExtra("thirdL");
        fourthL=(ArrayList<String>)getIntent().getSerializableExtra("fourthL");
        answer=(ArrayList<String>)getIntent().getSerializableExtra("answer");
        givenAnswer=(ArrayList<String>)getIntent().getSerializableExtra("givenAnswer");
        totallPoints=getIntent().getStringExtra("totalPoints");
        totalTime=getIntent().getStringExtra("totalTime");
        totalQA=getIntent().getStringExtra("totalQA");
        coines=getIntent().getStringExtra("coines");
        winnerId=getIntent().getStringExtra("winnerId");
        match_result=getIntent().getStringExtra("match_result");
        challangeType=getIntent().getStringExtra("type");

        SharedPreferences sharedPreferences=getSharedPreferences("user", Context.MODE_PRIVATE);
        String facebookId=sharedPreferences.getString("facebookId", null);
        String Myc=sharedPreferences.getString("coins", null);

        Typeface type=Typeface.createFromAsset(getAssets(),"font/regular.ttf");
        TextView titleName=(TextView)findViewById(R.id.answersheetToolbarTitleName);
        ImageView okButton=(ImageView)findViewById(R.id.result_activity_act_img);
        titleName.setTypeface(type);

        //redirectinc to actual activity
        if(challangeType == null){
            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    overridePendingTransition(R.anim.slid_in_left, R.anim.slide_out_right);
                }
            });
        }else if(challangeType.equals("challangerChallange")){
            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NotificationActivity.getInstance().finish();
                    finish();
                    Intent intent = new Intent(ResultSheetActivity.this, NotificationActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slid_in_left, R.anim.slide_out_right);
                }
            });
        }else {
            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    Intent intent = new Intent(ResultSheetActivity.this, MainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slid_in_left, R.anim.slide_out_right);
                }
            });
        }

        //for content
        TextView points=(TextView)findViewById(R.id.answer_sheet_points_gain_question);
        TextView totalQuestions=(TextView)findViewById(R.id.answer_sheet_total_asnwer_question);
        TextView time=(TextView)findViewById(R.id.answer_sheet_timer_question);

        points.setTypeface(type);
        totalQuestions.setTypeface(type);
        time.setTypeface(type);

        points.setText(totallPoints);
        time.setText(totalTime);
        totalQuestions.setText(totalQA);

        ListView listView=(ListView)findViewById(R.id.answer_sheet_list_view);
        FaceChallangeAnswerSheetAdapter adapter=new FaceChallangeAnswerSheetAdapter(ResultSheetActivity.this,R.layout.adapter_answer_sheet_layout_items,question,firstL,secondL,thirdL,fourthL,answer,givenAnswer);
        listView.setAdapter(adapter);

        //only fro giving challange answersheet
        if(winnerId == null){
            //giving the challange
            //chainging the coines
            int getMyCoines = Integer.valueOf(Myc);
            int getBitCoines = Integer.valueOf(coines);
            int mytotalCoines = getMyCoines - getBitCoines;
            final String myTC = String.valueOf(mytotalCoines);

            final Firebase childRef = ref.child("user_login");
            childRef.orderByChild("facebookId").equalTo(facebookId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        childRef.child(ds.getKey()).child("coins").setValue(myTC);
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("coins", myTC);
        }else{
            key=getIntent().getStringExtra("key");
            //facing the challange  ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
            if(challangeType.equals("openChallange")){
                //for open challange----------------------------------------------------------------------

                if(match_result.equals("win")){
                    //winner amount update
                    final int totalWinnerAmount= Integer.valueOf(coines);
                    final Firebase childRef=ref.child("user_login");
                    childRef.orderByChild("facebookId").equalTo(winnerId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot ds :dataSnapshot.getChildren()){
                                UserLoginList list =ds.getValue(UserLoginList.class);
                                String myCoins= String.valueOf(Integer.valueOf(list.getCoins())+totalWinnerAmount);
                                childRef.child(ds.getKey()).child("coins").setValue(myCoins);
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });

                    //deleting the challange
                    key=getIntent().getStringExtra("key");
                    Firebase openchallangeChild=ref.child("open_challange");
                    openchallangeChild.child(key).setValue(null);

                }else if(match_result.equals("loose")){
                    //updating the looser amount and winner amount
                    final Firebase childRef=ref.child("user_login");
                    //winer update
                    childRef.orderByChild("facebookId").equalTo(winnerId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot ds :dataSnapshot.getChildren()){
                                UserLoginList list=ds.getValue(UserLoginList.class);
                                String winerAmount=String.valueOf(Integer.valueOf(list.getCoins())+Integer.valueOf(coines)*2);
                                childRef.child(ds.getKey()).child("coins").setValue(winerAmount);
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });

                    //looser update minusing the coines
                    childRef.orderByChild("facebookId").equalTo(facebookId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot ds :dataSnapshot.getChildren()){
                                UserLoginList list=ds.getValue(UserLoginList.class);
                                //minusing the coins
                                String newMyCoins=String.valueOf(Integer.valueOf(list.getCoins())-Integer.valueOf(coines));
                                childRef.child(ds.getKey()).child("coins").setValue(newMyCoins);
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });

                }
                //innser close
            }else if(challangeType.equals("challangerChallange")){
                //for challanger challange----------------------------------------------------------------------------

                if(match_result.equals("win")){
                    //winner amount update
                    final int totalWinnerAmount= Integer.valueOf(coines);
                    final Firebase childRef=ref.child("user_login");
                    childRef.orderByChild("facebookId").equalTo(winnerId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                UserLoginList list = ds.getValue(UserLoginList.class);
                                String myCoins = String.valueOf(Integer.valueOf(list.getCoins()) + totalWinnerAmount);
                                childRef.child(ds.getKey()).child("coins").setValue(myCoins);
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });

                        //deleting the challange
                        Firebase challangerChallangeChild=ref.child("challanger_challange");
                        challangerChallangeChild.child(key).setValue(null);

                }else if(match_result.equals("loose")){
                    //updating the looser amount and winner amount
                    final Firebase childRef=ref.child("user_login");
                    //winer update
                    childRef.orderByChild("facebookId").equalTo(winnerId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot ds :dataSnapshot.getChildren()){
                                UserLoginList list=ds.getValue(UserLoginList.class);
                                String winerAmount=String.valueOf(Integer.valueOf(list.getCoins())+Integer.valueOf(coines)*2);
                                childRef.child(ds.getKey()).child("coins").setValue(winerAmount);
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });

                    //looser update minusing the coines
                    childRef.orderByChild("facebookId").equalTo(facebookId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                UserLoginList list = ds.getValue(UserLoginList.class);
                                //minusing the coins
                                String newMyCoins = String.valueOf(Integer.valueOf(list.getCoins()) - Integer.valueOf(coines));
                                childRef.child(ds.getKey()).child("coins").setValue(newMyCoins);
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });

                        //deleting the challange
                        Firebase challangerChallangeChild=ref.child("challanger_challange");
                        challangerChallangeChild.child(key).setValue(null);

                }
                //innser close

            }
        }

    }

}
