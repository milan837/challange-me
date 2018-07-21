package com.me.challange.milan.challangeme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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
import com.me.challange.milan.challangeme.GetterSetter.SubjectsLists;
import com.me.challange.milan.challangeme.GetterSetter.UserLoginList;

import java.util.ArrayList;
import java.util.List;

public class TagsSelectorActivity extends AppCompatActivity {
    Firebase ref;
    SubjectListAdapter adapter;
    List<String> selectedList;
    String facebookId,facebookToken,getName,getImageUrl,getGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tags_selector);
        Firebase.setAndroidContext(this);

        facebookId=getIntent().getStringExtra("facebookId");
        facebookToken=getIntent().getStringExtra("facebookToken");
        getName=getIntent().getStringExtra("name");
        getImageUrl=getIntent().getStringExtra("imageUrl");
        getGender=getIntent().getStringExtra("gender");

        ref=new Firebase("https://challange-me.firebaseio.com/");
        Firebase childRef=ref.child("subjects");

        final Firebase updateRef=ref.child("user_login");

        selectedList=new ArrayList<>();



        ListView listView=(ListView)findViewById(R.id.tag_selector_listview);
        adapter=new SubjectListAdapter(this,R.layout.adapter_subject_list);
        listView.setAdapter(adapter);

        childRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
             for(DataSnapshot data : dataSnapshot.getChildren()){
                 SubjectsLists lists=data.getValue(SubjectsLists.class);
                 adapter.add(lists);
             }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        Typeface type= Typeface.createFromAsset(getAssets(),"font/regular.ttf");
        TextView textView=(TextView)findViewById(R.id.login_detailsToolbarTitleName);
        Button goButton=(Button)findViewById(R.id.bottom_button);
        textView.setTypeface(type);
        goButton.setTypeface(type);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView name = (TextView) view.findViewById(R.id.adapter_subject_list_item);
                String getName = name.getText().toString().trim();
                if (selectedList.contains(getName)) {
                    Toast.makeText(getApplication(), "already selected", Toast.LENGTH_LONG).show();
                } else {
                    selectedList.add(getName);
                }
                name.setTextColor(Color.BLACK);
                adapter.notifyDataSetChanged();
            }
        });

        //for tags
        final Firebase userTagRef=ref.child("user_tags");

        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(selectedList.size() >= 4){
                    Firebase childref=ref.child("user_login").push();
                    childref.child("facebookId").setValue(facebookId);
                    childref.child("facebookToken").setValue(facebookToken);
                    childref.child("name").setValue(getName);
                    childref.child("gender").setValue(getGender);
                    childref.child("imageUrl").setValue(getImageUrl);
                    childref.child("coins").setValue("500");
                    childref.child("level").setValue("E");
                    childref.child("winMatch").setValue("0");
                    childref.child("looseMatch").setValue("0");
                    childref.child("totalChallange").setValue("0");

                    for(int i=0;i<selectedList.size();i++){
                        userTagRef.child(facebookId).push().child("name").setValue(selectedList.get(i));
                    }

                    SharedPreferences sharedPreferences=getSharedPreferences("user", MODE_PRIVATE);
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString("facebookId",facebookId);
                    editor.putString("facebookToken",facebookToken);
                    editor.commit();

                    finish();
                    Intent intent = new Intent(TagsSelectorActivity.this, MainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                }else{
                    Toast.makeText(getApplicationContext(),"please select al lease 4 subjects",Toast.LENGTH_LONG).show();
                }

            }
        });



    }

}
