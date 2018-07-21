package com.me.challange.milan.challangeme.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.me.challange.milan.challangeme.FaceChallangesActivity;
import com.me.challange.milan.challangeme.GetterSetter.ChallangerChallangeListRow;
import com.me.challange.milan.challangeme.GetterSetter.UserLoginList;
import com.me.challange.milan.challangeme.MainActivity;
import com.me.challange.milan.challangeme.NotificationActivity;
import com.me.challange.milan.challangeme.R;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by milan on 11/17/2017.
 */

public class NotificationGetListAdapter extends RecyclerView.Adapter<NotificationGetListAdapter.MyViewHolder> {
    List list=new ArrayList<>();
    Context ctx;
    Typeface type;
    Firebase ref;
    List<String> pushKey=new ArrayList();

    public NotificationGetListAdapter(List data,List key,Context context,Firebase ref) {
        this.ctx=context;
        this.list=data;
        this.type= Typeface.createFromAsset(ctx.getAssets(), "font/regular.ttf");
        this.ref=ref;
        this.pushKey=key;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.adapter_notification_get_layout,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final ChallangerChallangeListRow row=(ChallangerChallangeListRow)list.get(position);

        String getDate[]=row.getChallange_created_date().split(" ");
        holder.subject.setText(row.getChallange_subject());
        holder.points.setText(row.getChallange_points());
        holder.date.setText(getDate[0]);
        holder.time.setText(row.getChallange_time());
        holder.coins.setText(row.getChallange_coins());

        holder.challangerName.setTypeface(type);
        holder.subject.setTypeface(type);
        holder.points.setTypeface(type);
        holder.challangeOn.setTypeface(type);
        holder.date.setTypeface(type);
        holder.time.setTypeface(type);
        holder.key.setTypeface(type);
        holder.coins.setTypeface(type);

        Firebase userDetailsRef=ref.child("user_login");
        userDetailsRef.orderByChild("facebookId").equalTo(row.getChallange_created_by()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    UserLoginList list=ds.getValue(UserLoginList.class);
                    Glide.with(ctx).load(list.getImageUrl()).into(holder.profilePicture);
                    holder.challangerName.setText(list.getName());
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setting click functions
                NotificationActivity act=(NotificationActivity)ctx;
                SharedPreferences sharedPreferences=ctx.getSharedPreferences("user",Context.MODE_PRIVATE);
                int myCoins=Integer.valueOf(sharedPreferences.getString("coins", null));
                int challangeCoins=Integer.valueOf(row.getChallange_coins());
                if(myCoins >= challangeCoins){
                    Intent intent=new Intent(ctx, FaceChallangesActivity.class);
                    intent.putExtra("type","challangerChallange");
                    intent.putExtra("subject",row.getChallange_subject());
                    intent.putExtra("key",pushKey.get(position));
                    ctx.startActivity(intent);
                    act.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }else{
                    Toast.makeText(ctx, "Yo Donot have enough coins to accept this challange", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView coins,challangeOn,subject,date,points,time,challangerName,key;
        CircularImageView profilePicture;
        public MyViewHolder(View itemView) {
            super(itemView);
            key=(TextView)itemView.findViewById(R.id.get_ntf_push_key);
            coins=(TextView)itemView.findViewById(R.id.get_ntf_user_coines_text);
            challangeOn=(TextView)itemView.findViewById(R.id.get_ntf_on_text_view);
            subject=(TextView)itemView.findViewById(R.id.get_ntf_on_subject_text_view);
            date=(TextView)itemView.findViewById(R.id.get_ntf_date_text_view);
            points=(TextView)itemView.findViewById(R.id.get_ntf_points_text);
            time=(TextView)itemView.findViewById(R.id.get_ntf_total_levels_text);
            challangerName=(TextView)itemView.findViewById(R.id.get_ntf_username_textView);
            profilePicture=(CircularImageView)itemView.findViewById(R.id.get_ntf_user_face_image);
        }
    }
}
