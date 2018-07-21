package com.me.challange.milan.challangeme.Fragment;

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
import com.me.challange.milan.challangeme.Adapter.GiftCardAdapter;
import com.me.challange.milan.challangeme.GetterSetter.GiftCardsRows;
import com.me.challange.milan.challangeme.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by milan on 11/22/2017.
 */
public class GiftCardsFragment extends Fragment {
    List myList;
    Firebase ref;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_gift_card_layout,container,false);
        Firebase.setAndroidContext(getActivity());
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        myList=new ArrayList();
        ref=new Firebase("https://challange-me.firebaseio.com/");

        Firebase childRef=ref.child("gift_cards");

        RecyclerView recyclerView=(RecyclerView)getActivity().findViewById(R.id.gift_card_fragment_recycler_view);
        final GiftCardAdapter adapter=new GiftCardAdapter(getActivity(),myList);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        String amount[]={"10","20","30","50"};
        String brand[]={"Paytm","Flipkart","Amozone","Myntra"};
        String image[]={"http://cdn.lmitassets.com/gograbon/images/web-images/uploads/paytmlogominpng1475669696902.png",
                        "https://pbs.twimg.com/profile_images/786187830091407360/FUH63jZv.jpg",
                        "http://g-ec2.images-amazon.com/images/G/01/social/api-share/amazon_logo_500500.png",
        "https://pbs.twimg.com/profile_images/707599074791284739/E5Aea4nl.jpg"};
        String coins[]={"70,000","80,000","90,000","1,40,000"};


        childRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    GiftCardsRows rows=ds.getValue(GiftCardsRows.class);
                    myList.add(rows);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
}
