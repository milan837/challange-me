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
import com.me.challange.milan.challangeme.GetterSetter.QuestionsListGT;
import com.me.challange.milan.challangeme.GetterSetter.UserLoginList;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GiveChallangeActivity extends AppCompatActivity {
    int countQuestions=0;
    int totalPoints=0;
    String challangeType;
    Firebase ref;
    String facebookId,subject,challgeFacerImageUrl,myFacebookId,getCoines;
    List<String> question,answer,options,firstL,secondL,thirdL,fourthL;
    int minutes=0,second=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_challange);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Firebase.setAndroidContext(this);

        //Array list inititlization
        question=new ArrayList();
        answer=new ArrayList();
        options=new ArrayList();
        firstL=new ArrayList();
        secondL=new ArrayList();
        thirdL=new ArrayList();
        fourthL=new ArrayList();

        SharedPreferences sharedPreferences=getSharedPreferences("user", Context.MODE_PRIVATE);
        String imageUrl=sharedPreferences.getString("imageUrl", null);
        myFacebookId=sharedPreferences.getString("facebookId", null);

        ref=new Firebase("https://challange-me.firebaseio.com/");

        challangeType=getIntent().getStringExtra("type");
        subject=getIntent().getStringExtra("subject");
        facebookId=getIntent().getStringExtra("facebookId");
        getCoines=getIntent().getStringExtra("coines");


        Typeface type=Typeface.createFromAsset(getAssets(),"font/regular.ttf");

        TextView toolbarName=(TextView)findViewById(R.id.giveChallangeToolbarTitleName);
        final TextView totalGainPoints=(TextView)findViewById(R.id.give_challane_points_gain_question);
        final TextView totalTmeTaken=(TextView)findViewById(R.id.give_challane_timer_question);
        final TextView totalQuestionAnswer=(TextView)findViewById(R.id.give_challanges_total_asnwer_question);

        if(challangeType.equals("openChallange")){
            toolbarName.setText("Open Challange");
        }else if(challangeType.equals("challangerChallange")){
            toolbarName.setText("Challange To");
        }


        final TextView questionForUser=(TextView)findViewById(R.id.give_challanges_face_question_question);
        final TextView firstAnswer=(TextView)findViewById(R.id.give_first_answer);
        final TextView secondAnswer=(TextView)findViewById(R.id.give_second_answer);
        final TextView thirdAnswer=(TextView)findViewById(R.id.give_third_answer);
        final TextView fourthAnswer=(TextView)findViewById(R.id.give_fourth_answer);
        final TextView questionNumber=(TextView)findViewById(R.id.give_questionNumber);
        TextView pleaseWaiText=(TextView)findViewById(R.id.give_please_wait_text);
        final ProgressBar progressBar=(ProgressBar)findViewById(R.id.give_please_wait_progree_bar);
        final LinearLayout pleaseWaitLayout=(LinearLayout)findViewById(R.id.give_please_wait_layout);
        CircularImageView playingImage=(CircularImageView)findViewById(R.id.give_challanger_user_face_image);
        final CircularImageView futurePlayerImageView=(CircularImageView)findViewById(R.id.give_user_changes_face_image);

        Glide.with(this).load(imageUrl).into(playingImage);


        ImageView backButtOn=(ImageView)findViewById(R.id.give_challange_back_icon);

        pleaseWaiText.setTypeface(type);
        toolbarName.setTypeface(type);
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


        //fetching questions list
        Firebase questionsChild=ref.child("questions");
        Log.i("subject",subject);
        questionsChild.child("python").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int count = 0;
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

                    count++;
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        //fetching
        Firebase challangerChild=ref.child("user_login");
        challangerChild.orderByChild("facebookId").equalTo(facebookId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    UserLoginList list = ds.getValue(UserLoginList.class);
                    challgeFacerImageUrl = list.getImageUrl();
                    Glide.with(getApplicationContext()).load(challgeFacerImageUrl).into(futurePlayerImageView);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


        final List<String> givenAnswer=new ArrayList<>();

        questionNumber.setText("1");

        //linear laout
        final LinearLayout questIOnAnswerLinearLayout=(LinearLayout)findViewById(R.id.give_question_answer_linear_layout);
        final RelativeLayout pointsCountLayout=(RelativeLayout)findViewById(R.id.give_relativeLayout);
        RelativeLayout firstAnswerLayout=(RelativeLayout)findViewById(R.id.give_first_answer_layout);
        RelativeLayout secondAnswerLayout=(RelativeLayout)findViewById(R.id.give_second_answer_layout);
        RelativeLayout thirdAnswerLayout=(RelativeLayout)findViewById(R.id.give_third_answer_layout);
        RelativeLayout fourthAnswerLayout=(RelativeLayout)findViewById(R.id.give_fourth_answer_layout);

        ///answer buttons
        firstAnswerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (question.size() <= 50) {
                    String getAnswer = firstAnswer.getText().toString().trim();
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

                        //saving challange
                        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        Date date = new Date();
                        String getPoints=totalGainPoints.getText().toString().trim();
                        String getTime=totalTmeTaken.getText().toString().trim();
                        String getTotalQA=totalQuestionAnswer.getText().toString().trim();

                        if(challangeType.equals("openChallange")){
                            Firebase insertChallange=ref.child("open_challange").push();
                            insertChallange.child("challange_created_by").setValue(myFacebookId);
                            insertChallange.child("challange_created_date").setValue(dateFormat.format(date));
                            insertChallange.child("challange_coins").setValue(getCoines);
                            insertChallange.child("challange_points").setValue(getPoints);
                            insertChallange.child("challange_time").setValue(getTime);
                            insertChallange.child("challange_subject").setValue(subject);
                            Toast.makeText(getApplicationContext(), "your Challange is Created", Toast.LENGTH_SHORT).show();

                            Intent intent=new Intent(GiveChallangeActivity.this,ResultSheetActivity.class);
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
                            intent.putExtra("coines", getCoines);
                            startActivity(intent);

                            finish();
                            if(challangeType.equals("openChallange")){
                                MakeOpenChallange.getInstance().finish();
                            }else if(challangeType.equals("challangerChallange")){
                                MakeChallangeActivity.getInstance().finish();
                            }
                            SelectChallangeCategoryActivity.getInstance().finish();
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);


                        }else{
                            Firebase insertChallange=ref.child("challanger_challange").push();
                            insertChallange.child("challange_created_by").setValue(myFacebookId);
                            insertChallange.child("challange_created_for").setValue(facebookId);
                            insertChallange.child("challange_created_date").setValue(dateFormat.format(date));
                            insertChallange.child("challange_coins").setValue(getCoines);
                            insertChallange.child("challange_points").setValue(getPoints);
                            insertChallange.child("challange_time").setValue(getTime);
                            insertChallange.child("challange_subject").setValue(subject);
                            Toast.makeText(getApplicationContext(), "your Challange is Created", Toast.LENGTH_SHORT).show();

                            Intent intent=new Intent(GiveChallangeActivity.this,ResultSheetActivity.class);
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
                            intent.putExtra("coines", getCoines);
                            startActivity(intent);
                            finish();

                            if(challangeType.equals("openChallange")){
                                MakeOpenChallange.getInstance().finish();
                            }else if(challangeType.equals("challangerChallange")){
                                MakeChallangeActivity.getInstance().finish();
                            }
                            SelectChallangeCategoryActivity.getInstance().finish();
                            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                        }

                    } else {
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


                } else {

                }
            }
        });

        secondAnswerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (question.size() <= 50) {
                    String getAnswer = secondAnswer.getText().toString().trim();
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
                    if (question.size() <= (Integer.valueOf(getQuestionNumber)) ) {

                        //saving challange
                        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        Date date = new Date();
                        String getPoints=totalGainPoints.getText().toString().trim();
                        String getTime=totalTmeTaken.getText().toString().trim();
                        String getTotalQA=totalQuestionAnswer.getText().toString().trim();

                        if(challangeType.equals("openChallange")){
                            Firebase insertChallange=ref.child("open_challange").push();
                            insertChallange.child("challange_created_by").setValue(myFacebookId);
                            insertChallange.child("challange_created_date").setValue(dateFormat.format(date));
                            insertChallange.child("challange_coins").setValue(getCoines);
                            insertChallange.child("challange_points").setValue(getPoints);
                            insertChallange.child("challange_time").setValue(getTime);
                            insertChallange.child("challange_subject").setValue(subject);
                            Toast.makeText(getApplicationContext(), "your Challange is Created", Toast.LENGTH_SHORT).show();

                            Intent intent=new Intent(GiveChallangeActivity.this,ResultSheetActivity.class);
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
                            intent.putExtra("coines", getCoines);
                            startActivity(intent);
                            finish();

                            if(challangeType.equals("openChallange")){
                                MakeOpenChallange.getInstance().finish();
                            }else if(challangeType.equals("challangerChallange")){
                                MakeChallangeActivity.getInstance().finish();
                            }
                            SelectChallangeCategoryActivity.getInstance().finish();
                            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);

                        }else{
                            Firebase insertChallange=ref.child("challanger_challange").push();
                            insertChallange.child("challange_created_by").setValue(myFacebookId);
                            insertChallange.child("challange_created_for").setValue(facebookId);
                            insertChallange.child("challange_created_date").setValue(dateFormat.format(date));
                            insertChallange.child("challange_coins").setValue(getCoines);
                            insertChallange.child("challange_points").setValue(getPoints);
                            insertChallange.child("challange_time").setValue(getTime);
                            insertChallange.child("challange_subject").setValue(subject);
                            Toast.makeText(getApplicationContext(), "your Challange is Created", Toast.LENGTH_SHORT).show();

                            Intent intent=new Intent(GiveChallangeActivity.this,ResultSheetActivity.class);
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
                            intent.putExtra("coines", getCoines);
                            startActivity(intent);
                            finish();

                            if(challangeType.equals("openChallange")){
                                MakeOpenChallange.getInstance().finish();
                            }else if(challangeType.equals("challangerChallange")){
                                MakeChallangeActivity.getInstance().finish();
                            }

                            SelectChallangeCategoryActivity.getInstance().finish();
                            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);

                        }


                    } else {
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


                } else {

                }
            }
        });

        thirdAnswerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(question.size() <= 50){
                    String getAnswer=thirdAnswer.getText().toString().trim();
                    //inserting given answer
                    givenAnswer.add(countQuestions,getAnswer);
                    int count=countQuestions+1;

                    totalQuestionAnswer.setText(count + "/" + question.size());
                    if(getAnswer.equals(answer.get(countQuestions))){
                        totalPoints=totalPoints+5;
                        totalGainPoints.setText(totalPoints+" pts");
                    }

                    countQuestions=countQuestions+1;

                    String getQuestionNumber=questionNumber.getText().toString().trim();
                    if (question.size() <= (Integer.valueOf(getQuestionNumber)) ) {

                        //saving challange
                        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        Date date = new Date();
                        String getPoints=totalGainPoints.getText().toString().trim();
                        String getTime=totalTmeTaken.getText().toString().trim();
                        String getTotalQA=totalQuestionAnswer.getText().toString().trim();

                        if(challangeType.equals("openChallange")){
                            Firebase insertChallange=ref.child("open_challange").push();
                            insertChallange.child("challange_created_by").setValue(myFacebookId);
                            insertChallange.child("challange_created_date").setValue(dateFormat.format(date));
                            insertChallange.child("challange_coins").setValue(getCoines);
                            insertChallange.child("challange_points").setValue(getPoints);
                            insertChallange.child("challange_time").setValue(getTime);
                            insertChallange.child("challange_subject").setValue(subject);
                            Toast.makeText(getApplicationContext(), "your Challange is Created", Toast.LENGTH_SHORT).show();

                            Intent intent=new Intent(GiveChallangeActivity.this,ResultSheetActivity.class);
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
                            intent.putExtra("coines", getCoines);
                            startActivity(intent);
                            finish();

                            if(challangeType.equals("openChallange")){
                                MakeOpenChallange.getInstance().finish();
                            }else if(challangeType.equals("challangerChallange")){
                                MakeChallangeActivity.getInstance().finish();
                            }

                            SelectChallangeCategoryActivity.getInstance().finish();
                            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);

                        }else{
                            Firebase insertChallange=ref.child("challanger_challange").push();
                            insertChallange.child("challange_created_by").setValue(myFacebookId);
                            insertChallange.child("challange_created_for").setValue(facebookId);
                            insertChallange.child("challange_created_date").setValue(dateFormat.format(date));
                            insertChallange.child("challange_coins").setValue(getCoines);
                            insertChallange.child("challange_points").setValue(getPoints);
                            insertChallange.child("challange_time").setValue(getTime);
                            insertChallange.child("challange_subject").setValue(subject);
                            Toast.makeText(getApplicationContext(), "your Challange is Created", Toast.LENGTH_SHORT).show();

                            Intent intent=new Intent(GiveChallangeActivity.this,ResultSheetActivity.class);
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
                            intent.putExtra("coines", getCoines);
                            startActivity(intent);
                            finish();

                            if(challangeType.equals("openChallange")){
                                MakeOpenChallange.getInstance().finish();
                            }else if(challangeType.equals("challangerChallange")){
                                MakeChallangeActivity.getInstance().finish();
                            }

                            SelectChallangeCategoryActivity.getInstance().finish();
                            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);

                        }


                    } else {
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

                }else{

                }
            }
        });

        fourthAnswerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (question.size() <= 50) {
                    String getAnswer = fourthAnswer.getText().toString().trim();
                    //inserting given answer
                    givenAnswer.add(countQuestions, getAnswer);
                    int count = countQuestions + 1;

                    totalQuestionAnswer.setText(count + "/" + question.size());
                    if (getAnswer.equals(answer.get(countQuestions))) {
                        totalPoints = totalPoints + 5;
                        totalGainPoints.setText(totalPoints + " pts");
                    }

                    countQuestions = countQuestions + 1;


                    String getQuestionNumber = questionNumber.getText().toString().trim();
                    if (question.size() <= (Integer.valueOf(getQuestionNumber))) {

                        //saving challange
                        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        Date date = new Date();
                        String getPoints=totalGainPoints.getText().toString().trim();
                        String getTime=totalTmeTaken.getText().toString().trim();
                        String getTotalQA=totalQuestionAnswer.getText().toString().trim();

                        if(challangeType.equals("openChallange")){
                            Firebase insertChallange=ref.child("open_challange").push();
                            insertChallange.child("challange_created_by").setValue(myFacebookId);
                            insertChallange.child("challange_created_date").setValue(dateFormat.format(date));
                            insertChallange.child("challange_coins").setValue(getCoines);
                            insertChallange.child("challange_points").setValue(getPoints);
                            insertChallange.child("challange_time").setValue(getTime);
                            insertChallange.child("challange_subject").setValue(subject);
                            Toast.makeText(getApplicationContext(), "your Challange is Created", Toast.LENGTH_SHORT).show();

                            Intent intent=new Intent(GiveChallangeActivity.this,ResultSheetActivity.class);
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
                            intent.putExtra("coines", getCoines);
                            startActivity(intent);
                            finish();

                            if(challangeType.equals("openChallange")){
                                MakeOpenChallange.getInstance().finish();
                            }else if(challangeType.equals("challangerChallange")){
                                MakeChallangeActivity.getInstance().finish();
                            }

                            SelectChallangeCategoryActivity.getInstance().finish();
                            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);

                        }else{
                            Firebase insertChallange=ref.child("challanger_challange").push();
                            insertChallange.child("challange_created_by").setValue(myFacebookId);
                            insertChallange.child("challange_created_for").setValue(facebookId);
                            insertChallange.child("challange_created_date").setValue(dateFormat.format(date));
                            insertChallange.child("challange_coins").setValue(getCoines);
                            insertChallange.child("challange_points").setValue(getPoints);
                            insertChallange.child("challange_time").setValue(getTime);
                            insertChallange.child("challange_subject").setValue(subject);
                            Toast.makeText(getApplicationContext(), "your Challange is Created", Toast.LENGTH_SHORT).show();

                            Intent intent=new Intent(GiveChallangeActivity.this,ResultSheetActivity.class);
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
                            intent.putExtra("coines", getCoines);
                            startActivity(intent);
                            finish();

                            if(challangeType.equals("openChallange")){
                                MakeOpenChallange.getInstance().finish();
                            }else if(challangeType.equals("challangerChallange")){
                                MakeChallangeActivity.getInstance().finish();
                            }

                            SelectChallangeCategoryActivity.getInstance().finish();
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                        }



                    } else {
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

                } else {

                }
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


}
