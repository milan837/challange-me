package com.me.challange.milan.challangeme.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.me.challange.milan.challangeme.Adapter.NotificationListAdapter;
import com.me.challange.milan.challangeme.GetterSetter.NotificationListRow;
import com.me.challange.milan.challangeme.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by milan on 10/29/2017.
 */
public class NotificationSendChallangeFragment extends Fragment {
    List myList;
    String facebookId;
    Firebase ref;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_notification_send_challange,container,false);
        Firebase.setAndroidContext(getActivity());
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ref=new Firebase("https://challange-me.firebaseio.com/");
        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        facebookId=sharedPreferences.getString("facebookId",null);
        myList=new ArrayList();

        Firebase childUserRef=ref.child("user_login");
        RecyclerView recyclerView=(RecyclerView)getActivity().findViewById(R.id.notification_list_recycler_view);
        final NotificationListAdapter adapter=new NotificationListAdapter(myList,getActivity(),childUserRef,facebookId);

        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        Firebase resultNodeRef=ref.child("challange_result");
        resultNodeRef.orderByChild("challange_looser").equalTo(facebookId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    NotificationListRow listRow = ds.getValue(NotificationListRow.class);
                    myList.add(listRow);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        resultNodeRef.orderByChild("challange_winner").equalTo(facebookId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    NotificationListRow listRow = ds.getValue(NotificationListRow.class);
                    myList.add(listRow);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
}
