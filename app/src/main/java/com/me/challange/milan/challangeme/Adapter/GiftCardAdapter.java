package com.me.challange.milan.challangeme.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.me.challange.milan.challangeme.GetterSetter.GiftCardsRows;
import com.me.challange.milan.challangeme.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by milan on 11/23/2017.
 */
public class GiftCardAdapter  extends RecyclerView.Adapter<GiftCardAdapter.MyViewHolder> {
    Context ctx;
    List list=new ArrayList();
    Typeface typeface;

    public GiftCardAdapter(Context ctx, List list) {
        this.ctx = ctx;
        this.list = list;
        this.typeface = Typeface.createFromAsset(ctx.getAssets(),"font/regular.ttf");
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.adapter_gift_card_layout,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        GiftCardsRows rows=(GiftCardsRows)list.get(position);

        holder.brandName.setText(rows.getBrandName());
        holder.amount.setText("Rs "+rows.getAmt());
        holder.coins.setText(rows.getCoins());

        Glide.with(ctx).load(rows.getImageUrl()).into(holder.image);

        holder.brandName.setTypeface(typeface);
        holder.giftname.setTypeface(typeface);
        holder.amount.setTypeface(typeface);
        holder.coins.setTypeface(typeface);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView amount,giftname,brandName,coins;
        ImageView image;
        public MyViewHolder(View itemView) {
            super(itemView);
            amount=(TextView)itemView.findViewById(R.id.adapter_card_amount);
            giftname=(TextView)itemView.findViewById(R.id.adapter_card_amount_text);
            brandName=(TextView)itemView.findViewById(R.id.adapter_website_name);
            coins=(TextView)itemView.findViewById(R.id.adapter_gift_card_total_coin);
            image=(ImageView)itemView.findViewById(R.id.gift_brnad_image);

        }
    }
}
