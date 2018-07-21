package com.me.challange.milan.challangeme.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.me.challange.milan.challangeme.GetterSetter.NotificationListRow;
import com.me.challange.milan.challangeme.GetterSetter.UserLoginList;
import com.me.challange.milan.challangeme.R;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by milan on 11/21/2017.
 */
public class NotificationListAdapter extends RecyclerView.Adapter<NotificationListAdapter.MyViewHolder> {
    List list=new ArrayList();
    Context ctx;
    Firebase ref;
    String fId;
    Typeface type;

    public NotificationListAdapter(List list, Context ctx, Firebase ref,String facebookId) {
        this.list = list;
        this.ctx = ctx;
        this.fId=facebookId;
        this.ref=ref;
        this.type=Typeface.createFromAsset(ctx.getAssets(),"font/regular.ttf");
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.adapter_notification_list_layout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final NotificationListRow row=(NotificationListRow)list.get(position);

        String getDate[]=row.getChallange_date().split(" ");
        holder.date.setText(getDate[0]);

        holder.date.setTypeface(type);
        holder.ntf_text.setTypeface(type);
        holder.winnerLooser.setTypeface(type);

        if(row.getChallange_winner().equals(fId)){
            ref.orderByChild("facebookId").equalTo(row.getChallange_looser()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot ds:dataSnapshot.getChildren()){
                        UserLoginList list=ds.getValue(UserLoginList.class);
                        holder.winnerLooser.setText("you Win");
                        Glide.with(ctx).load(list.getImageUrl()).into(holder.profilePicture);

                        if(row.getChallange_type().equals("openChallange")){
                            holder.ntf_text.setText("Open challange created by the "+list.getName()+" and earn "+row.getChallange_coins()+" coins On "+ Html.fromHtml("<strong>" + row.getChallange_subject() + "</strong>"));
                        }else{
                            holder.ntf_text.setText("Challange Given by the "+list.getName()+" and earn "+row.getChallange_coins()+" coins On "+Html.fromHtml("<strong>" + row.getChallange_subject() + "</strong>"));
                        }

                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
        }else{
            ref.orderByChild("facebookId").equalTo(row.getChallange_winner()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot ds:dataSnapshot.getChildren()){
                        UserLoginList list=ds.getValue(UserLoginList.class);
                        holder.winnerLooser.setText("you Loose");
                        Glide.with(ctx).load(list.getImageUrl()).into(holder.profilePicture);

                        if(row.getChallange_type().equals("openChallange")){
                            holder.ntf_text.setText("Open challange created by the "+list.getName()+" and loose "+row.getChallange_coins()+" coins On "+ Html.fromHtml("<strong>" + row.getChallange_subject() + "</strong>"));
                        }else{
                            holder.ntf_text.setText("Challange Given by the "+list.getName()+" and loose "+row.getChallange_coins()+" coins On "+ Html.fromHtml("<strong>" + row.getChallange_subject() + "</strong>"));
                        }

                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView date,winnerLooser,ntf_text;
        CircularImageView profilePicture;

        public MyViewHolder(View itemView) {
            super(itemView);
            date=(TextView)itemView.findViewById(R.id.notification_list_date_textview);
            winnerLooser=(TextView)itemView.findViewById(R.id.notification_list_winner_looser_text);
            ntf_text=(TextView)itemView.findViewById(R.id.notification_list_text);
            profilePicture=(CircularImageView)itemView.findViewById(R.id.notification_list_profile_picture);
        }
    }
}
