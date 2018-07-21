package com.me.challange.milan.challangeme;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.me.challange.milan.challangeme.Adapter.FaceChallangeAnswerSheetAdapter;
import com.me.challange.milan.challangeme.GetterSetter.ChallangerChallangeListRow;
import com.me.challange.milan.challangeme.GetterSetter.OpenChallangeDetails;
import com.me.challange.milan.challangeme.GetterSetter.QuestionsListGT;
import com.me.challange.milan.challangeme.GetterSetter.UserLoginList;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FaceChallangesActivity extends AppCompatActivity {
    int countQuestions=0;
    int totalPoints=0;
    int minutes=0,second=0;
    String challangeType;
    Firebase ref;
    String myFacebookId,key,subject;
    List<String> question,answer,options,firstL,secondL,thirdL,fourthL,givenAnswer;
    String challangeCoines,challangePoints,challangeTime,challangeCreaterId;

    //for questions and button
    TextView questionForUser,firstAnswer,secondAnswer,thirdAnswer,fourthAnswer,questionNumber;
    //for details display layout
    TextView totalQuestionAnswer,totalGainPoints,totalTmeTaken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_challanges);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Array list inititlization
        question=new ArrayList();
        answer=new ArrayList();
        options=new ArrayList();
        firstL=new ArrayList();
        secondL=new ArrayList();
        thirdL=new ArrayList();
        fourthL=new ArrayList();
        givenAnswer=new ArrayList();

        SharedPreferences sharedPreferences=getSharedPreferences("user", Context.MODE_PRIVATE);
        String imageUrl=sharedPreferences.getString("imageUrl",null);
        myFacebookId=sharedPreferences.getString("facebookId",null);

        ref=new Firebase("https://challange-me.firebaseio.com/");

        challangeType=getIntent().getStringExtra("type");
        key=getIntent().getStringExtra("key");
        subject=getIntent().getStringExtra("subject");

        Typeface type=Typeface.createFromAsset(getAssets(),"font/regular.ttf");

        final TextView toolbarName=(TextView)findViewById(R.id.faceChallangeToolbarTitleName);
        final TextView pointsText=(TextView)findViewById(R.id.face_challange_points_text);
        final TextView totalTime=(TextView)findViewById(R.id.face_challange_total_time_text);
        totalGainPoints=(TextView)findViewById(R.id.face_challane_points_gain_question);
        totalTmeTaken=(TextView)findViewById(R.id.face_challane_timer_question);
        totalQuestionAnswer=(TextView)findViewById(R.id.face_challanges_total_asnwer_question);


        questionForUser=(TextView)findViewById(R.id.face_challanges_face_question_question);
        firstAnswer=(TextView)findViewById(R.id.first_answer);
        secondAnswer=(TextView)findViewById(R.id.second_answer);
        thirdAnswer=(TextView)findViewById(R.id.third_answer);
        fourthAnswer=(TextView)findViewById(R.id.fourth_answer);
        questionNumber=(TextView)findViewById(R.id.questionNumber);

        TextView pleaseWaiText=(TextView)findViewById(R.id.please_wait_text);
        final ProgressBar progressBar=(ProgressBar)findViewById(R.id.please_wait_progree_bar);
        final LinearLayout pleaseWaitLayout=(LinearLayout)findViewById(R.id.please_wait_layout);
        CircularImageView myImage=(CircularImageView)findViewById(R.id.open_user_face_image);
        final CircularImageView challangerImage=(CircularImageView)findViewById(R.id.open_user_changes_face_image);

        Glide.with(this).load(imageUrl).into(myImage);

        ImageView backButtOn=(ImageView)findViewById(R.id.face_challange_back_icon);

        pleaseWaiText.setTypeface(type);
        toolbarName.setTypeface(type);
        pointsText.setTypeface(type);
        totalTime.setTypeface(type);
        totalGainPoints.setTypeface(type);
        totalTmeTaken.setTypeface(type);
        totalQuestionAnswer.setTypeface(type);
        questionForUser.setTypeface(type);
        firstAnswer.setTypeface(type);
        secondAnswer.setTypeface(type);
        thirdAnswer.setTypeface(type);
        fourthAnswer.setTypeface(type);

        backButtOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slid_in_left, R.anim.slide_out_right);
            }
        });


        if(challangeType.equals("openChallange")){
            //for open challanger header details

            Firebase childChallangeRef=ref.child("open_challange").child(key);
            childChallangeRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()) {
                        OpenChallangeDetails challangeDetails = dataSnapshot.getValue(OpenChallangeDetails.class);

                        totalTime.setText(challangeDetails.getChallange_time());
                        pointsText.setText(challangeDetails.getChallange_points());
                        toolbarName.setText(challangeDetails.getChallange_subject());

                        //assigning data
                        challangeCoines = challangeDetails.getChallange_coins();
                        challangeCreaterId = challangeDetails.getChallange_created_by();
                        challangePoints = challangeDetails.getChallange_points();
                        challangeTime = challangeDetails.getChallange_time();

                        //fetching image for challanger
                        Firebase userRef = ref.child("user_login");
                        userRef.orderByChild("facebookId").equalTo(challangeDetails.getChallange_created_by()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    UserLoginList list = ds.getValue(UserLoginList.class);
                                    Glide.with(getApplicationContext()).load(list.getImageUrl()).into(challangerImage);
                                }
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }
                        });
                    }

                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });

        }else if(challangeType.equals("challangerChallange")){
            //for challanger challange
            //for open challanger header details
            Firebase childChallangeRef=ref.child("challanger_challange").child(key);

            childChallangeRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()) {
                        ChallangerChallangeListRow challangeDetails = dataSnapshot.getValue(ChallangerChallangeListRow.class);

                        totalTime.setText(challangeDetails.getChallange_time());
                        pointsText.setText(challangeDetails.getChallange_points());
                        toolbarName.setText(challangeDetails.getChallange_subject());

                        //assigning data
                        challangeCoines = challangeDetails.getChallange_coins();
                        challangeCreaterId = challangeDetails.getChallange_created_by();
                        challangePoints = challangeDetails.getChallange_points();
                        challangeTime = challangeDetails.getChallange_time();

                        //fetching image for challanger
                        Firebase userRef = ref.child("user_login");
                        userRef.orderByChild("facebookId").equalTo(challangeDetails.getChallange_created_by()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    UserLoginList list = ds.getValue(UserLoginList.class);
                                    Glide.with(getApplicationContext()).load(list.getImageUrl()).into(challangerImage);
                                }
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }
                        });
                    }

                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
        }


        //fetching questions list
        Firebase questionsChild=ref.child("questions");
        Log.i("subject",subject);
        questionsChild.child("python").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    QuestionsListGT questionsListGT = ds.getValue(QuestionsListGT.class);
                    question.add(questionsListGT.getQues());
                    options.add(questionsListGT.getOptions());
                    answer.add(questionsListGT.getAnswer());

                    String optionSplit[] = questionsListGT.getOptions().split(",");

                    firstL.add(optionSplit[0]);
                    secondL.add(optionSplit[1]);
                    thirdL.add(optionSplit[2]);
                    fourthL.add(optionSplit[3]);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        //hidden question number
        questionNumber.setText("1");

        //linear laout
        final LinearLayout questIOnAnswerLinearLayout=(LinearLayout)findViewById(R.id.face_question_answer_linear_layout);
        final RelativeLayout pointsCountLayout=(RelativeLayout)findViewById(R.id.face_challange_pointes_table_relativeLayout);

        RelativeLayout firstAnswerLayout=(RelativeLayout)findViewById(R.id.first_answer_layout);
        RelativeLayout secondAnswerLayout=(RelativeLayout)findViewById(R.id.second_answer_layout);
        RelativeLayout thirdAnswerLayout=(RelativeLayout)findViewById(R.id.third_answer_layout);
        RelativeLayout fourthAnswerLayout=(RelativeLayout)findViewById(R.id.fourth_answer_layout);

        firstAnswerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                butonFunctions(firstAnswer);
            }
        });

        secondAnswerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                butonFunctions(secondAnswer);
            }
        });

        thirdAnswerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                butonFunctions(thirdAnswer);
            }
        });

        fourthAnswerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                butonFunctions(fourthAnswer);
            }
        });

        //seting timer for initilization
        new CountDownTimer(3000,1000){

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                questIOnAnswerLinearLayout.setVisibility(View.VISIBLE);
                pointsCountLayout.setVisibility(View.VISIBLE);
                pleaseWaitLayout.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);

                questionForUser.setText(question.get(0));

                //answer
                firstAnswer.setText(firstL.get(0));
                secondAnswer.setText(secondL.get(0));
                thirdAnswer.setText(thirdL.get(0));
                fourthAnswer.setText(fourthL.get(0));


            }
        }.start();

        CountDownTimer ccc=new CountDownTimer(600000,1000){

            @Override
            public void onTick(long millisUntilFinished) {
                int count=(int)millisUntilFinished/1000;

                totalTmeTaken.setText(String.valueOf(minutes)+":"+String.valueOf(second));
                if(second == 60){
                    totalTmeTaken.setText(String.valueOf(minutes)+":"+String.valueOf(second));
                    minutes++;
                    second=0;
                }

                second++;
            }

            @Override
            public void onFinish() {
            }
        }.start();

    }

    public void butonFunctions(final TextView buttonText){

        ///answer buttons
                if (question.size() <= 50) {
                    String getAnswer = buttonText.getText().toString().trim();
                    //inserting given answer
                    givenAnswer.add(countQuestions,getAnswer);
                    int count = countQuestions + 1;

                    totalQuestionAnswer.setText(count + "/" + question.size());
                    if (getAnswer.equals(answer.get(countQuestions))) {
                        totalPoints = totalPoints + 5;
                        totalGainPoints.setText(totalPoints + " pts");
                    }

                    countQuestions = countQuestions + 1;

                    String getQuestionNumber=questionNumber.getText().toString().trim();
                    if (question.size() <= (Integer.valueOf(getQuestionNumber))) {
                        //last questions
                        //saving challange
                        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        Date date = new Date();
                        String getPoints=totalGainPoints.getText().toString().trim();
                        String getTime=totalTmeTaken.getText().toString().trim();
                        String getTotalQA=totalQuestionAnswer.getText().toString().trim();

                        //code for checking winner
                        //splitting the current  points and time
                        String playerPoints[]=getPoints.split(" ");
                        int mainPlayerPoints=Integer.valueOf(playerPoints[0]);
                        String playerTime[]=getTime.split(":");
                        int min= Integer.valueOf(playerTime[0]);
                        int sec=Integer.valueOf(playerTime[1]);

                        //splitting the challange points and time
                        String CPoints[]=challangePoints.split(" ");
                        int mainCPoints=Integer.valueOf(CPoints[0]);
                        String CTime[]=challangeTime.split(":");
                        int CMin= Integer.valueOf(CTime[0]);
                        int CSec=Integer.valueOf(CTime[1]);

                        if( ((totalPoints > mainCPoints ) && (min < CMin || sec < CSec)) || (totalPoints >= mainCPoints) && (min < CMin || sec < CSec ) ){
                            //win the challange

                            if(challangeType.equals("openChallange")){
                                //for facing open challange
                                Firebase insertChallange=ref.child("challange_result").push();
                                insertChallange.child("challange_winner").setValue(myFacebookId);
                                insertChallange.child("challange_looser").setValue(challangeCreaterId);
                                insertChallange.child("challange_coins").setValue(challangeCoines);
                                insertChallange.child("challange_points").setValue(getPoints);
                                insertChallange.child("challange_time").setValue(getTime);
                                insertChallange.child("challange_type").setValue("openChallange");
                                insertChallange.child("challange_key").setValue(key);
                                insertChallange.child("challange_subject").setValue(subject);
                                insertChallange.child("challange_date").setValue(dateFormat.format(date));

                                Toast.makeText(getApplicationContext(), "your win the challange", Toast.LENGTH_SHORT).show();

                                Intent intent=new Intent(FaceChallangesActivity.this,ResultSheetActivity.class);
                                intent.putExtra("question", (Serializable) question);
                                intent.putExtra("firstL", (Serializable) firstL);
                                intent.putExtra("secondL", (Serializable) secondL);
                                intent.putExtra("thirdL", (Serializable) thirdL);
                                intent.putExtra("fourthL", (Serializable) fourthL);
                                intent.putExtra("answer", (Serializable) answer);
                                intent.putExtra("givenAnswer", (Serializable) givenAnswer);
                                intent.putExtra("totalPoints",getPoints);
                                intent.putExtra("totalTime",getTime);
                                intent.putExtra("totalQA", getTotalQA);
                                intent.putExtra("coines",challangeCoines);
                                intent.putExtra("winnerId",myFacebookId);
                                intent.putExtra("match_result","win");
                                intent.putExtra("key",key);
                                intent.putExtra("type",challangeType);
                                startActivity(intent);
                                finish();
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                            }else if(challangeType.equals("challangerChallange")){
                                //for facing challanger challange
                                Firebase insertChallange=ref.child("challange_result").push();
                                insertChallange.child("challange_winner").setValue(myFacebookId);
                                insertChallange.child("challange_looser").setValue(challangeCreaterId);
                                insertChallange.child("challange_coins").setValue(challangeCoines);
                                insertChallange.child("challange_points").setValue(getPoints);
                                insertChallange.child("challange_time").setValue(getTime);
                                insertChallange.child("challange_type").setValue("challangerChallange");
                                insertChallange.child("challange_key").setValue(key);
                                insertChallange.child("challange_subject").setValue(subject);
                                insertChallange.child("challange_date").setValue(dateFormat.format(date));

                                Toast.makeText(getApplicationContext(), "your win the challange", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(FaceChallangesActivity.this,ResultSheetActivity.class);
                                intent.putExtra("question", (Serializable) question);
                                intent.putExtra("firstL", (Serializable) firstL);
                                intent.putExtra("secondL", (Serializable) secondL);
                                intent.putExtra("thirdL", (Serializable) thirdL);
                                intent.putExtra("fourthL", (Serializable) fourthL);
                                intent.putExtra("answer", (Serializable) answer);
                                intent.putExtra("givenAnswer", (Serializable) givenAnswer);
                                intent.putExtra("totalPoints",getPoints);
                                intent.putExtra("totalTime",getTime);
                                intent.putExtra("totalQA",getTotalQA);
                                intent.putExtra("coines",challangeCoines);
                                intent.putExtra("winnerId",myFacebookId);
                                intent.putExtra("match_result","win");
                                intent.putExtra("key",key);
                                intent.putExtra("type",challangeType);
                                startActivity(intent);
                                finish();
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            }

                        }else{
                            //loose the challange

                            if(challangeType.equals("openChallange")){
                                //for facing open challange
                                Firebase insertChallange=ref.child("challange_result").push();
                                insertChallange.child("challange_winner").setValue(challangeCreaterId);
                                insertChallange.child("challange_looser").setValue(myFacebookId);
                                insertChallange.child("challange_coins").setValue(challangeCoines);
                                insertChallange.child("challange_points").setValue(getPoints);
                                insertChallange.child("challange_time").setValue(getTime);
                                insertChallange.child("challange_type").setValue("openChallange");
                                insertChallange.child("challange_key").setValue(key);
                                insertChallange.child("challange_subject").setValue(subject);
                                insertChallange.child("challange_date").setValue(dateFormat.format(date));

                                Toast.makeText(getApplicationContext(), "your loose the challange", Toast.LENGTH_SHORT).show();

                                Intent intent=new Intent(FaceChallangesActivity.this,ResultSheetActivity.class);
                                intent.putExtra("question", (Serializable) question);
                                intent.putExtra("firstL", (Serializable) firstL);
                                intent.putExtra("secondL", (Serializable) secondL);
                                intent.putExtra("thirdL", (Serializable) thirdL);
                                intent.putExtra("fourthL", (Serializable) fourthL);
                                intent.putExtra("answer", (Serializable) answer);
                                intent.putExtra("givenAnswer", (Serializable) givenAnswer);
                                intent.putExtra("totalPoints",getPoints);
                                intent.putExtra("totalTime",getTime);
                                intent.putExtra("totalQA",getTotalQA);
                                intent.putExtra("coines",challangeCoines);
                                intent.putExtra("winnerId",challangeCreaterId);
                                intent.putExtra("match_result","loose");
                                intent.putExtra("type",challangeType);
                                intent.putExtra("key",key);
                                startActivity(intent);
                                finish();
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                            }else if(challangeType.equals("challangerChallange")){
                                //for facing challanger challange
                                Firebase insertChallange=ref.child("challange_result").push();
                                insertChallange.child("challange_winner").setValue(challangeCreaterId);
                                insertChallange.child("challange_looser").setValue(myFacebookId);
                                insertChallange.child("challange_coins").setValue(challangeCoines);
                                insertChallange.child("challange_points").setValue(getPoints);
                                insertChallange.child("challange_time").setValue(getTime);
                                insertChallange.child("challange_type").setValue("challangerChallange");
                                insertChallange.child("challange_key").setValue(key);
                                insertChallange.child("challange_subject").setValue(subject);
                                insertChallange.child("challange_date").setValue(dateFormat.format(date));

                                Toast.makeText(getApplicationContext(), "your loose the challange", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(FaceChallangesActivity.this,ResultSheetActivity.class);
                                intent.putExtra("question", (Serializable) question);
                                intent.putExtra("firstL", (Serializable) firstL);
                                intent.putExtra("secondL", (Serializable) secondL);
                                intent.putExtra("thirdL", (Serializable) thirdL);
                                intent.putExtra("fourthL", (Serializable) fourthL);
                                intent.putExtra("answer", (Serializable) answer);
                                intent.putExtra("givenAnswer", (Serializable) givenAnswer);
                                intent.putExtra("totalPoints",getPoints);
                                intent.putExtra("totalTime",getTime);
                                intent.putExtra("totalQA",getTotalQA);
                                intent.putExtra("coines",challangeCoines);
                                intent.putExtra("winnerId",challangeCreaterId);
                                intent.putExtra("match_result","loose");
                                intent.putExtra("type",challangeType);
                                intent.putExtra("key",key);
                                startActivity(intent);
                                finish();
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            }

                        }

                    } else {

                        //saving challange
                        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        Date date = new Date();
                        String getPoints=totalGainPoints.getText().toString().trim();
                        String getTime=totalTmeTaken.getText().toString().trim();
                        String getTotalQA=totalQuestionAnswer.getText().toString().trim();

                        //code for checking winner
                        //splitting the current  points and time
                        String playerPoints[]=getPoints.split(" ");
                        int mainPlayerPoints=Integer.valueOf(playerPoints[0]);
                        String playerTime[]=getTime.split(":");
                        int min= Integer.valueOf(playerTime[0]);
                        int sec=Integer.valueOf(playerTime[1]);

                        //splitting the challange points and time
                        String CPoints[]=challangePoints.split(" ");
                        int mainCPoints=Integer.valueOf(CPoints[0]);
                        String CTime[]=challangeTime.split(":");
                        int CMin= Integer.valueOf(CTime[0]);
                        int CSec=Integer.valueOf(CTime[1]);

                        if( ((totalPoints > mainCPoints ) && (min < CMin || sec < CSec))  || (totalPoints >= mainCPoints) && (min < CMin || sec < CSec ) ){
                            //win the challange

                            if(challangeType.equals("openChallange")){
                                //for facing open challange
                                Firebase insertChallange=ref.child("challange_result").push();
                                insertChallange.child("challange_winner").setValue(myFacebookId);
                                insertChallange.child("challange_looser").setValue(challangeCreaterId);
                                insertChallange.child("challange_coins").setValue(challangeCoines);
                                insertChallange.child("challange_points").setValue(getPoints);
                                insertChallange.child("challange_time").setValue(getTime);
                                insertChallange.child("challange_type").setValue("openChallange");
                                insertChallange.child("challange_key").setValue(key);
                                insertChallange.child("challange_subject").setValue(subject);
                                insertChallange.child("challange_date").setValue(dateFormat.format(date));

                                Toast.makeText(getApplicationContext(), "your win the challange", Toast.LENGTH_SHORT).show();

                                Intent intent=new Intent(FaceChallangesActivity.this,ResultSheetActivity.class);
                                intent.putExtra("question", (Serializable) question);
                                intent.putExtra("firstL", (Serializable) firstL);
                                intent.putExtra("secondL", (Serializable) secondL);
                                intent.putExtra("thirdL", (Serializable) thirdL);
                                intent.putExtra("fourthL", (Serializable) fourthL);
                                intent.putExtra("answer", (Serializable) answer);
                                intent.putExtra("givenAnswer", (Serializable) givenAnswer);
                                intent.putExtra("totalPoints",getPoints);
                                intent.putExtra("totalTime",getTime);
                                intent.putExtra("totalQA",getTotalQA);
                                intent.putExtra("coines",challangeCoines);
                                intent.putExtra("winnerId",myFacebookId);
                                intent.putExtra("match_result","win");
                                intent.putExtra("key",key);
                                intent.putExtra("type",challangeType);
                                startActivity(intent);
                                finish();
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                            }else if(challangeType.equals("challangerChallange")){
                                //for facing challanger challange
                                Firebase insertChallange=ref.child("challange_result").push();
                                insertChallange.child("challange_winner").setValue(myFacebookId);
                                insertChallange.child("challange_looser").setValue(challangeCreaterId);
                                insertChallange.child("challange_coins").setValue(challangeCoines);
                                insertChallange.child("challange_points").setValue(getPoints);
                                insertChallange.child("challange_time").setValue(getTime);
                                insertChallange.child("challange_type").setValue("challangerChallange");
                                insertChallange.child("challange_key").setValue(key);
                                insertChallange.child("challange_subject").setValue(subject);
                                insertChallange.child("challange_date").setValue(dateFormat.format(date));


                                Toast.makeText(getApplicationContext(), "your win the challange", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(FaceChallangesActivity.this,ResultSheetActivity.class);
                                intent.putExtra("question", (Serializable) question);
                                intent.putExtra("firstL", (Serializable) firstL);
                                intent.putExtra("secondL", (Serializable) secondL);
                                intent.putExtra("thirdL", (Serializable) thirdL);
                                intent.putExtra("fourthL", (Serializable) fourthL);
                                intent.putExtra("answer", (Serializable) answer);
                                intent.putExtra("givenAnswer", (Serializable) givenAnswer);
                                intent.putExtra("totalPoints",getPoints);
                                intent.putExtra("totalTime",getTime);
                                intent.putExtra("totalQA",getTotalQA);
                                intent.putExtra("coines",challangeCoines);
                                intent.putExtra("winnerId",myFacebookId);
                                intent.putExtra("match_result","win");
                                intent.putExtra("key",key);
                                intent.putExtra("type",challangeType);
                                startActivity(intent);
                                finish();
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            }
                        }

                        //next answer and questions
                        int qNumber = Integer.valueOf(getQuestionNumber);
                        questionForUser.setText(question.get(qNumber));
                        //answer
                        firstAnswer.setText(firstL.get(qNumber));
                        secondAnswer.setText(secondL.get(qNumber));
                        thirdAnswer.setText(thirdL.get(qNumber));
                        fourthAnswer.setText(fourthL.get(qNumber));

                        qNumber = qNumber + 1;
                        questionNumber.setText(String.valueOf(qNumber));
                    }


                }
    }
}
