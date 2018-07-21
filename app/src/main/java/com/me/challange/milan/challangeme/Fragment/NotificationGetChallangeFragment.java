package com.me.challange.milan.challangeme.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
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
import com.me.challange.milan.challangeme.Adapter.NotificationGetListAdapter;
import com.me.challange.milan.challangeme.GetterSetter.ChallangerChallangeListRow;
import com.me.challange.milan.challangeme.GetterSetter.OpenChallangeListRows;
import com.me.challange.milan.challangeme.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by milan on 10/29/2017.
 */
public class NotificationGetChallangeFragment extends Fragment {
    List myList;
    List<String> postKey;
    Firebase ref;
    String myfacebookId;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_notification_get_challange,container,false);
        Firebase.setAndroidContext(getActivity());
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        myfacebookId=sharedPreferences.getString("facebookId",null);

        myList=new ArrayList();
        postKey=new ArrayList<>();

        ref=new Firebase("https://challange-me.firebaseio.com/");

        RecyclerView recyclerView=(RecyclerView)getActivity().findViewById(R.id.get_ntf_recyclerView);
        final NotificationGetListAdapter adapter=new NotificationGetListAdapter(myList,postKey,getActivity(),ref);

        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        Firebase childRef=ref.child("challanger_challange");
        childRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ChallangerChallangeListRow listRows = ds.getValue(ChallangerChallangeListRow.class);
                    myList.add(listRows);
                    postKey.add(ds.getKey());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
}
