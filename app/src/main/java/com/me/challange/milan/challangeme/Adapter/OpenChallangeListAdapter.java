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
import com.me.challange.milan.challangeme.GetterSetter.OpenChallangeListRows;
import com.me.challange.milan.challangeme.GetterSetter.UserLoginList;
import com.me.challange.milan.challangeme.MainActivity;
import com.me.challange.milan.challangeme.R;
import com.me.challange.milan.challangeme.SelectChallangeCategoryActivity;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by milan on 11/12/2017.
 */
public class OpenChallangeListAdapter extends RecyclerView.Adapter<OpenChallangeListAdapter.MyViewHolder> {
    List list= new ArrayList();
    Context ctx;
    Typeface type;
    List<String> pushKey=new ArrayList();
    Firebase userRef;
    public OpenChallangeListAdapter(List pushKey,List data,Context context,Firebase userRef) {
        this.list=data;
        this.ctx=context;
        this.type=Typeface.createFromAsset(ctx.getAssets(),"font/regular.ttf");
        this.userRef=userRef;
        this.pushKey=pushKey;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.adapter_open_challange_layout,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final OpenChallangeListRows rows=(OpenChallangeListRows)list.get(position);

        holder.challangeType.setText("Open Challange");
        holder.subject.setText(rows.getChallange_subject());
        holder.points.setText(rows.getChallange_points());
        holder.coines.setText(rows.getChallange_coins());
        holder.time.setText(rows.getChallange_time());
        holder.openPushKey.setText(pushKey.get(position));

        userRef.orderByChild("facebookId").equalTo(rows.getChallange_created_by()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    UserLoginList userLoginList = ds.getValue(UserLoginList.class);
                    holder.name.setText(userLoginList.getName());
                    Glide.with(ctx).load(userLoginList.getImageUrl()).into(holder.imageView);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity act=(MainActivity)ctx;
                SharedPreferences sharedPreferences=ctx.getSharedPreferences("user",Context.MODE_PRIVATE);
                int myCoins=Integer.valueOf(sharedPreferences.getString("coins", null));
                int challangeCoins=Integer.valueOf(rows.getChallange_coins());
                if(myCoins >= challangeCoins){
                    Intent intent=new Intent(ctx, FaceChallangesActivity.class);
                    intent.putExtra("type","openChallange");
                    intent.putExtra("subject",rows.getChallange_subject());
                    intent.putExtra("key",pushKey.get(position));
                    ctx.startActivity(intent);
                    act.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }else{
                    Toast.makeText(ctx,"Yo Donot have enough coins to accept this challange",Toast.LENGTH_LONG).show();
                }

            }
        });

        holder.name.setTypeface(type);
        holder.coines.setTypeface(type);
        holder.points.setTypeface(type);
        holder.subject.setTypeface(type);
        holder.time.setTypeface(type);
        holder.challangeType.setTypeface(type);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView challangeType,subject,coines,time,points,name,openPushKey;
        CircularImageView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            challangeType=(TextView)itemView.findViewById(R.id.open_challange_user_name_textView);
            subject=(TextView)itemView.findViewById(R.id.open_challange_subject_textView);
            coines=(TextView)itemView.findViewById(R.id.open_challange_user_coines_text);
            time=(TextView)itemView.findViewById(R.id.open_challange_total_levels_text);
            points=(TextView)itemView.findViewById(R.id.open_challange_face_text);
            imageView=(CircularImageView)itemView.findViewById(R.id.open_challange_user_face_image);
            name=(TextView)itemView.findViewById(R.id.open_challange_username_textView);
            openPushKey=(TextView)itemView.findViewById(R.id.open_challange_push_key);
        }
    }
}
