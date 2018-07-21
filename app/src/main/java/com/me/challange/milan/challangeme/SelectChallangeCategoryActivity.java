package com.me.challange.milan.challangeme;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.me.challange.milan.challangeme.Adapter.CategoryAdapter;
import com.me.challange.milan.challangeme.Adapter.SubjectListAdapter;
import com.me.challange.milan.challangeme.ExtraClass.CategorryGT;
import com.me.challange.milan.challangeme.GetterSetter.SubjectsLists;

import java.util.List;

public class SelectChallangeCategoryActivity extends AppCompatActivity {
    String challangeType;
    String facebookId;
    Firebase ref;
    public static SelectChallangeCategoryActivity instance=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_challange_category);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Firebase.setAndroidContext(this);

        instance=this;

        //getting data ...
        challangeType=getIntent().getStringExtra("type");
        facebookId=getIntent().getStringExtra("facebookId");

        final Typeface type= Typeface.createFromAsset(getAssets(),"font/regular.ttf");

        final TextView toolbarName=(TextView)findViewById(R.id.categoryChallangeToolbarTitleName);
        toolbarName.setTypeface(type);

        ImageView backButton=(ImageView)findViewById(R.id.category_challange_back_icon);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });


        ListView listView=(ListView)findViewById(R.id.select_category_list_view);
        final SubjectListAdapter adapter=new SubjectListAdapter(this,R.layout.adapter_category);
        listView.setAdapter(adapter);

        //firebase ini..
        ref=new Firebase("https://challange-me.firebaseio.com/");
        Firebase childRef;

        if(challangeType.equals("openChallange")){
            childRef=ref.child("subjects");
            childRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot ds: dataSnapshot.getChildren()){
                        SubjectsLists lists=ds.getValue(SubjectsLists.class);
                        adapter.add(lists);
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
        }else if(challangeType.equals("challangerChallange")){

            childRef=ref.child("user_tags");
            childRef.child(facebookId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        SubjectsLists lists = ds.getValue(SubjectsLists.class);
                        adapter.add(lists);
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView subject=(TextView)view.findViewById(R.id.adapter_subject_list_item);
                String getSubject=subject.getText().toString().trim();

                Toast.makeText(getApplicationContext(), challangeType, Toast.LENGTH_LONG).show();
                if(challangeType.equals("openChallange")){
                    Intent intent = new Intent(SelectChallangeCategoryActivity.this, MakeOpenChallange.class);
                    intent.putExtra("type",challangeType);
                    intent.putExtra("subject",getSubject);
                    intent.putExtra("facebookId",facebookId);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }else if(challangeType.equals("challangerChallange")){
                    Intent intent = new Intent(SelectChallangeCategoryActivity.this, MakeChallangeActivity.class);
                    intent.putExtra("type",challangeType);
                    intent.putExtra("subject",getSubject);
                    intent.putExtra("facebookId",facebookId);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }


            }
        });


    }

    public static SelectChallangeCategoryActivity getInstance(){
        return instance;
    }
}
