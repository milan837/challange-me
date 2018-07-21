package com.me.challange.milan.challangeme.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.me.challange.milan.challangeme.ExtraClass.CategorryGT;
import com.me.challange.milan.challangeme.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by milan on 11/3/2017.
 */
public class CategoryAdapter extends ArrayAdapter {
    List list=new ArrayList<>();
    Typeface type;

    public CategoryAdapter(Context context, int resource) {
        super(context, resource);
        this.type=Typeface.createFromAsset(context.getAssets(),"font/regular.ttf");
    }

    @Override
    public void add(Object object) {
        super.add(object);
        list.add(object);
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=LayoutInflater.from(getContext());
        View view=inflater.inflate(R.layout.adapter_category, parent, false);
        CategorryGT row=(CategorryGT)this.getItem(position);

        TextView category=(TextView)view.findViewById(R.id.adapter_category_item);
        category.setText(row.getCatagory());
        category.setTypeface(type);

        return view;
    }
}
