package com.me.challange.milan.challangeme;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.FacebookActivity;
import com.facebook.FacebookButtonBase;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.me.challange.milan.challangeme.Fragment.GiftCardsFragment;
import com.me.challange.milan.challangeme.Fragment.HomeFragment;
import com.me.challange.milan.challangeme.Fragment.MyAccountFragment;
import com.me.challange.milan.challangeme.Fragment.ShopCoinsFragment;
import com.me.challange.milan.challangeme.GetterSetter.UserLoginList;
import com.mikhaellopez.circularimageview.CircularImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    String facebookId,coins,level,imageUrl,totalMatch,gender,name,looseMatch,winMatch;
    Firebase ref;
    TextView toolbarTitle;
    public static MainActivity instance=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        //instance of this activity
        instance=this;
        //firebase for context of this activity
        Firebase.setAndroidContext(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //fetching data from sharedprefrence
        final SharedPreferences sharedPreferences=getSharedPreferences("user", Context.MODE_PRIVATE);
        facebookId=sharedPreferences.getString("facebookId",null);
        final String myImageUrl=sharedPreferences.getString("imageUrl", null);

        //firebase fatchin user_login node details
        ref=new Firebase("https://challange-me.firebaseio.com/");
        Firebase childRef=ref.child("user_login");
        childRef.orderByChild("facebookId").equalTo(facebookId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    UserLoginList list = ds.getValue(UserLoginList.class);
                    coins = list.getCoins();
                    level = list.getLevel();
                    imageUrl = list.getImageUrl();
                    totalMatch = list.getTotalChallange();
                    gender = list.getGender();
                    name = list.getName();
                    looseMatch = list.getLooseMatch();
                    winMatch = list.getWinMatch();
                }

                //sharepreference for saving teh data
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("coins", coins);
                editor.putString("name", name);
                editor.putString("level", level);
                editor.putString("imageUrl", imageUrl);
                editor.putString("totalMatch", totalMatch);
                editor.putString("gender", gender);
                editor.putString("looseMatch", looseMatch);
                editor.putString("winMatch", winMatch);
                editor.commit();

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        Typeface type=Typeface.createFromAsset(getAssets(),"font/regular.ttf");

        toolbarTitle=(TextView)findViewById(R.id.toolbarTitleName);
        toolbarTitle.setTypeface(type);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Inage fro nava view
        final CircularImageView navProfile=(CircularImageView)navigationView.findViewById(R.id.nav_my_profile_picture);


        //for invoking the openchallange fragment
        HomeFragment homeFragment=new HomeFragment();
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        transaction.add(R.id.main_activity_fragment_layout, homeFragment, "homeFragment");
        transaction.addToBackStack("homeFragmentStack");
        transaction.commit();

        ImageView notificationIcon=(ImageView)findViewById(R.id.notification_icon);
        notificationIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NotificationActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

/*
        //for inserting questions
        Firebase queRef=ref.child("questions");
        Firebase java=queRef.child("java");
        Firebase html=queRef.child("html");
        Firebase tech=queRef.child("python");
        Firebase cplus=queRef.child("php");
        String questios[]={"What is the full form of the java","What is the name of mYine","how are your","goog means data","plus pluse data"};
        String options[]={"myjava,yourjava,ourjava,alljava","milan,shrestha,anil,shrestha","suman,kiran,kumar,biwas","anil,sweta,likitha,aisweria"," r r,skit,saptageri,faran"};
        String answer[]={"ourjava","milan","biwas","sweta","r r"};

        for(int i=0; i<questios.length; i++) {
            Firebase newChild=java.push();
            newChild.child("ques").setValue(questios[i]);
            newChild.child("options").setValue(options[i]);
            newChild.child("answer").setValue(answer[i]);
        }
        for(int i=0; i<questios.length; i++) {
            Firebase newChild=tech.push();
            newChild.child("ques").setValue(questios[i]);
            newChild.child("options").setValue(options[i]);
            newChild.child("answer").setValue(answer[i]);
        }
        for(int i=0; i<questios.length; i++) {
            Firebase newChild=cplus.push();
            newChild.child("ques").setValue(questios[i]);
            newChild.child("options").setValue(options[i]);
            newChild.child("answer").setValue(answer[i]);
        }
*/
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();

        if (id == R.id.nav_home) {
            HomeFragment homeFragment=new HomeFragment();
            fragmentTransaction.replace(R.id.main_activity_fragment_layout,homeFragment,"homeFragment");
            fragmentTransaction.addToBackStack("homeFragmentStack");
            fragmentTransaction.setCustomAnimations(R.anim.slide_in_right,R.anim.slide_out_left);
            fragmentTransaction.commit();

        } else if (id == R.id.nav_profile) {
            MyAccountFragment myAccountFragment=new MyAccountFragment();
            fragmentTransaction.replace(R.id.main_activity_fragment_layout,myAccountFragment,"myAccountFragment");
            fragmentTransaction.addToBackStack("myAccountFragmentStack");
            fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
            fragmentTransaction.commit();

        } else if (id == R.id.nav_logout) {
            SharedPreferences deleteData=getSharedPreferences("user",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor=deleteData.edit();
            editor.clear();
            editor.commit();
            LoginManager.getInstance().logOut();
            Intent intent=new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slid_in_left, R.anim.slide_out_right);
        } else if (id == R.id.nav_gift_cards) {
            toolbarTitle.setText("Gift Cards");
            GiftCardsFragment giftCardsFragment=new GiftCardsFragment();
            fragmentTransaction.replace(R.id.main_activity_fragment_layout,giftCardsFragment,"giftFragment");
            fragmentTransaction.addToBackStack("giftFragment");
            fragmentTransaction.setCustomAnimations(R.anim.slide_in_right,R.anim.slide_out_left);
            fragmentTransaction.commit();
        }else if(id == R.id.nav_shop_coines){
            toolbarTitle.setText("Shop Coins");
            ShopCoinsFragment shopCoinsFragment=new ShopCoinsFragment();
            fragmentTransaction.replace(R.id.main_activity_fragment_layout,shopCoinsFragment,"shopCoinFragment");
            fragmentTransaction.addToBackStack("shopCoinFragment");
            fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
            fragmentTransaction.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public static MainActivity getInstance(){
        return instance;
    }
}
