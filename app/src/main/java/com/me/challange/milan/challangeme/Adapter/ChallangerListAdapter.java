package com.me.challange.milan.challangeme.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.me.challange.milan.challangeme.GetterSetter.SubjectsLists;
import com.me.challange.milan.challangeme.GetterSetter.UserLoginList;
import com.me.challange.milan.challangeme.MainActivity;
import com.me.challange.milan.challangeme.R;
import com.me.challange.milan.challangeme.SelectChallangeCategoryActivity;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by milan on 11/9/2017.
 */
public class ChallangerListAdapter extends RecyclerView.Adapter<ChallangerListAdapter.MyViewHolder> {
    List list=new ArrayList();
    Typeface type;
    Context ctx;

    public ChallangerListAdapter(List data,Context context) {
        this.list=data;
        this.ctx=context;
        this.type=Typeface.createFromAsset(ctx.getAssets(),"font/regular.ttf");
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.adapter_challanger_user_item,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final UserLoginList rows= (UserLoginList) list.get(position);
        holder.username.setText(rows.getName());
        holder.coins.setText(rows.getCoins());
        holder.totalMatch.setText(rows.getTotalChallange());
        holder.level.setText(rows.getLevel());

        Glide.with(this.ctx).load(rows.getImageUrl())
                .into(holder.profileImage);

        //typeface
        holder.username.setTypeface(type);
        holder.coins.setTypeface(type);
        holder.totalMatch.setTypeface(type);
        holder.level.setTypeface(type);


        //onclick listener
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity act=(MainActivity)ctx;
                Intent intent=new Intent(ctx, SelectChallangeCategoryActivity.class);
                intent.putExtra("facebookId",rows.getFacebookId());
                intent.putExtra("type","challangerChallange");
                ctx.startActivity(intent);
                act.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        CircularImageView profileImage;
        TextView coins,level,username,totalMatch;

        public MyViewHolder(View itemView) {
            super(itemView);
            profileImage=(CircularImageView)itemView.findViewById(R.id.challanger_user_face_image);
            coins=(TextView)itemView.findViewById(R.id.adapter_challanger_user_coines_text);
            level=(TextView)itemView.findViewById(R.id.adapter_challangers_total_levels_text);
            totalMatch=(TextView)itemView.findViewById(R.id.adapter_challanger_total_challange_face_text);
            username=(TextView)itemView.findViewById(R.id.adapter_challanger_user_name_textView);
        }
    }
}
