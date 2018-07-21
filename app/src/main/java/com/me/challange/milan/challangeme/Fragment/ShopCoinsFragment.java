package com.me.challange.milan.challangeme.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.me.challange.milan.challangeme.Adapter.ShopCoinsAdapter;
import com.me.challange.milan.challangeme.GetterSetter.ShopCoinRows;
import com.me.challange.milan.challangeme.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by milan on 11/24/2017.
 */
public class ShopCoinsFragment extends Fragment {
    List myList;
    Firebase ref;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_shop_coins_layout,container,false);
        Firebase.setAndroidContext(getActivity());
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        myList=new ArrayList();
        ref=new Firebase("https://challange-me.firebaseio.com/");
        Firebase childRef=ref.child("coins_shop");

        RecyclerView recyclerView=(RecyclerView)getActivity().findViewById(R.id.shop_coin_recycler_adapter);
        final ShopCoinsAdapter adapter=new ShopCoinsAdapter(myList,getActivity());

        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        childRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    ShopCoinRows row=ds.getValue(ShopCoinRows.class);
                    myList.add(row);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
}
