package com.me.challange.milan.challangeme.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.me.challange.milan.challangeme.GetterSetter.ChallangeResultListRows;
import com.me.challange.milan.challangeme.GetterSetter.UserLoginList;
import com.me.challange.milan.challangeme.R;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by milan on 11/18/2017.
 */
public class ProfileChallangesPlayedListAdaapter extends RecyclerView.Adapter<ProfileChallangesPlayedListAdaapter.MyViewHolder>{
    List list=new ArrayList();
    Firebase ref;
    Typeface type;
    String myFacebookId,myName,myImageUrl;
    RelativeLayout mainLayout;
    Context ctx;
    public ProfileChallangesPlayedListAdaapter(List data,Context context,Firebase ref,String myFacebookId,String name,String imageUrl,RelativeLayout mainheaderLayout) {
        this.type=Typeface.createFromAsset(context.getAssets(),"font/regular.ttf");
        this.ref=ref;
        this.list=data;
        this.myFacebookId=myFacebookId;
        this.myImageUrl=imageUrl;
        this.myName=name;
        this.ctx=context;
        this.mainLayout=mainheaderLayout;
    }

    @Override

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.adapter_profile_layout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        if(position > 1){
            mainLayout.setVisibility(View.GONE);
        }else if(position <= 1){
            mainLayout.setVisibility(View.VISIBLE);
        }

        ChallangeResultListRows rows=(ChallangeResultListRows)list.get(position);
        holder.subject.setText(rows.getChallange_subject());
        holder.coins.setText("Coins: "+rows.getChallange_coins());
        String getDate[]= rows.getChallange_date().split(" ");
        holder.date.setText(getDate[0]);

        //my losser challange
        if(rows.getChallange_looser().equals(myFacebookId)){
            holder.myName.setText(myName);
            holder.result.setText("Loose");
            holder.result.setBackgroundDrawable(ctx.getResources().getDrawable(R.drawable.loser_background_textview));
            Glide.with(ctx).load(myImageUrl).into(holder.myImage);

        }else{
            ref.orderByChild("facebookId").equalTo(rows.getChallange_winner()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot ds:dataSnapshot.getChildren()){
                        UserLoginList list=ds.getValue(UserLoginList.class);
                        holder.challangerName.setText(list.getName());
                        Glide.with(ctx).load(list.getImageUrl()).into(holder.challangerImage);
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
        }

        //my winner match
        if(rows.getChallange_winner().equals(myFacebookId)){
            holder.myName.setText(myName);
            holder.result.setText("Winner");
            holder.result.setBackgroundDrawable(ctx.getResources().getDrawable(R.drawable.winner_background_textview));
            Glide.with(ctx).load(myImageUrl).into(holder.myImage);

        }else{
            ref.orderByChild("facebookId").equalTo(rows.getChallange_looser()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        UserLoginList list = ds.getValue(UserLoginList.class);
                        holder.challangerName.setText(list.getName());
                        Glide.with(ctx).load(list.getImageUrl()).into(holder.challangerImage);
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
        }

        holder.result.setTypeface(type);
        holder.challangerName.setTypeface(type);
        holder.subject.setTypeface(type);
        holder.coins.setTypeface(type);
        holder.date.setTypeface(type);
        holder.myName.setTypeface(type);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView myName,challangerName,coins,date,result,subject;
        CircularImageView myImage,challangerImage;

        public MyViewHolder(View itemView) {
            super(itemView);
            myName=(TextView)itemView.findViewById(R.id.user_game_name_teextview);
            challangerName=(TextView)itemView.findViewById(R.id.user_changes_face_name_textview);
            coins=(TextView)itemView.findViewById(R.id.adapter_coin_text_view);
            date=(TextView)itemView.findViewById(R.id.match_date_text);
            result=(TextView)itemView.findViewById(R.id.match_winner_losser_text);
            subject=(TextView)itemView.findViewById(R.id.tag_subject_text);
            myImage=(CircularImageView)itemView.findViewById(R.id.user_face_image);
            challangerImage=(CircularImageView)itemView.findViewById(R.id.user_changes_face_image);
        }
    }
}
