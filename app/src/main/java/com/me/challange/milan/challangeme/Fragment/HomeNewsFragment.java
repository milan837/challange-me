package com.me.challange.milan.challangeme.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.me.challange.milan.challangeme.Adapter.ChallangerListAdapter;
import com.me.challange.milan.challangeme.Adapter.OpenChallangeListAdapter;
import com.me.challange.milan.challangeme.FaceChallangesActivity;
import com.me.challange.milan.challangeme.GetterSetter.OpenChallangeListRows;
import com.me.challange.milan.challangeme.MakeOpenChallange;
import com.me.challange.milan.challangeme.R;
import com.me.challange.milan.challangeme.SelectChallangeCategoryActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by milan on 10/28/2017.
 */
public class HomeNewsFragment extends Fragment {
    String facebookId;
    List myList;
    List<String> pushKey;
    Firebase ref;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_home_news,container,false);
        Firebase.setAndroidContext(getActivity());
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        facebookId=sharedPreferences.getString("facebookId",null);

        myList=new ArrayList();
        pushKey=new ArrayList();

        ref=new Firebase("https://challange-me.firebaseio.com/");
        Firebase childRef=ref.child("user_login");

        RecyclerView recyclerView=(RecyclerView)getActivity().findViewById(R.id.open_challange_recycler_view);
        final OpenChallangeListAdapter openChallangeListAdapter=new OpenChallangeListAdapter(pushKey,myList,getActivity(),childRef);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(openChallangeListAdapter);

        Firebase openChallangeChild=ref.child("open_challange");
        openChallangeChild.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds:dataSnapshot.getChildren()) {
                    OpenChallangeListRows rows=ds.getValue(OpenChallangeListRows.class);
                    myList.add(rows);
                    String getPushKey=ds.getKey();
                    pushKey.add(getPushKey);
                    openChallangeListAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        FloatingActionButton floatingActionButton=(FloatingActionButton)getActivity().findViewById(R.id.create_open_challange_floating_icon);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SelectChallangeCategoryActivity.class);
                intent.putExtra("type", "openChallange");
                intent.putExtra("facebookId", facebookId);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }
}
