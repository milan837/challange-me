package com.me.challange.milan.challangeme.Fragment;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.me.challange.milan.challangeme.GetterSetter.UserLoginList;
import com.me.challange.milan.challangeme.MakeChallangeActivity;
import com.me.challange.milan.challangeme.R;
import com.me.challange.milan.challangeme.SelectChallangeCategoryActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by milan on 10/28/2017.
 */
public class HomeChallangersFragment extends Fragment {
    Firebase ref;
    List myList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_home_challangers,container,false);
        Firebase.setAndroidContext(getActivity());

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        myList=new ArrayList();

        Typeface type = Typeface.createFromAsset(getActivity().getAssets(), "font/regular.ttf");

        RecyclerView recyclerView=(RecyclerView)getActivity().findViewById(R.id.challanger_recycler_view);
        final ChallangerListAdapter challangerListAdapter=new ChallangerListAdapter(myList,getActivity());

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(challangerListAdapter);

        ref=new Firebase("https://challange-me.firebaseio.com/");
        Firebase childRef=ref.child("user_login");
        childRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    UserLoginList list = ds.getValue(UserLoginList.class);
                    myList.add(list);
                    challangerListAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        //settiting challange for each user



    }
}
