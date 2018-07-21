package com.me.challange.milan.challangeme.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.me.challange.milan.challangeme.Adapter.ProfileChallangesPlayedListAdaapter;
import com.me.challange.milan.challangeme.GetterSetter.ChallangeResultListRows;
import com.me.challange.milan.challangeme.R;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by milan on 10/29/2017.
 */
public class MyAccountFragment extends Fragment {
    String facebookId,name,imageUrl,level,coins,totalMatch;
    List myList;
    Firebase ref;
    int lastViewId,totalCount,firstViewId;
    boolean isVisibae;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_my_account,container,false);
        Firebase.setAndroidContext(getActivity());
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        facebookId=sharedPreferences.getString("facebookId",null);
        name=sharedPreferences.getString("name",null);
        imageUrl=sharedPreferences.getString("imageUrl",null);
        level=sharedPreferences.getString("level",null);
        coins=sharedPreferences.getString("coins",null);
        totalMatch=sharedPreferences.getString("totalMatch",null);

        myList=new ArrayList();

        ref=new Firebase("https://challange-me.firebaseio.com/");

        Typeface type= Typeface.createFromAsset(getActivity().getAssets(),"font/regular.ttf");

        TextView titleName=(TextView)getActivity().findViewById(R.id.toolbarTitleName);
        titleName.setText(name);
        TextView levelText=(TextView)getActivity().findViewById(R.id.total_levels_text);
        TextView coineText=(TextView)getActivity().findViewById(R.id.coine_text);
        TextView gameText=(TextView)getActivity().findViewById(R.id.total_challange_face_text);
        CircularImageView myProfilleImage=(CircularImageView)getActivity().findViewById(R.id.profile_picture);
        final RelativeLayout relativeLayout=(RelativeLayout)getActivity().findViewById(R.id.profile_info_layout);

        Glide.with(getActivity()).load(imageUrl).into(myProfilleImage);

        levelText.setTypeface(type);
        coineText.setTypeface(type);
        gameText.setTypeface(type);
        levelText.setText(level);
        coineText.setText(coins);
        gameText.setText(totalMatch);

        Firebase childRe=ref.child("user_login");
        RecyclerView recyclerView=(RecyclerView)getActivity().findViewById(R.id.profile_recyclerView);
        RelativeLayout profileInformationLayout=(RelativeLayout)getActivity().findViewById(R.id.profile_info_layout);
        final ProfileChallangesPlayedListAdaapter adapter=new ProfileChallangesPlayedListAdaapter(myList,getActivity(),childRe,facebookId,name,imageUrl,profileInformationLayout);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        Firebase result=ref.child("challange_result");
        result.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ChallangeResultListRows listRows = ds.getValue(ChallangeResultListRows.class);
                    myList.add(listRows);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        final LinearLayoutManager linearLayoutManager=(LinearLayoutManager)recyclerView.getLayoutManager();

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalCount=linearLayoutManager.getItemCount();
                firstViewId=linearLayoutManager.findFirstVisibleItemPosition();
                lastViewId=linearLayoutManager.findLastVisibleItemPosition();

                if(firstViewId >1){
                    relativeLayout.setVisibility(View.VISIBLE);
                }else if(firstViewId ==  1){
                    relativeLayout.setVisibility(View.VISIBLE);
                }else{
                    Log.i("lop","asd");

                }

            }
        });
    }

}
