package com.me.challange.milan.challangeme.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.me.challange.milan.challangeme.Fragment.ShopCoinsFragment;
import com.me.challange.milan.challangeme.GetterSetter.ShopCoinRows;
import com.me.challange.milan.challangeme.MainActivity;
import com.me.challange.milan.challangeme.PaytmActivity;
import com.me.challange.milan.challangeme.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by milan on 11/24/2017.
 */
public class ShopCoinsAdapter extends RecyclerView.Adapter<ShopCoinsAdapter.MyViewHolder> {
    List list=new ArrayList<>();
    Context ctx;
    Firebase ref;
    Typeface type;

    public ShopCoinsAdapter(List list, Context ctx) {
        this.list = list;
        this.ctx = ctx;
        this.type = Typeface.createFromAsset(ctx.getAssets(),"font/regular.ttf");
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.adapter_shop_coins_layout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final ShopCoinRows rows=(ShopCoinRows)list.get(position);
        holder.amt.setText("Rs "+rows.getAmt());
        holder.coin.setText(rows.getCoin());

        holder.byText.setTypeface(type);
        holder.coin.setTypeface(type);
        holder.amt.setTypeface(type);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity act=(MainActivity)ctx;
                Intent intent=new Intent(ctx, PaytmActivity.class);
                intent.putExtra("amt", rows.getAmt());
                intent.putExtra("coins", rows.getCoin());
                ctx.startActivity(intent);
                act.overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView amt,coin,byText;
        public MyViewHolder(View itemView) {
            super(itemView);
            amt=(TextView)itemView.findViewById(R.id.adapter_buy_coin_amount);
            coin=(TextView)itemView.findViewById(R.id.adapter_buy_total_coin);
            byText=(TextView)itemView.findViewById(R.id.adapter_buy_coin_with_paytm);
        }
    }
}
