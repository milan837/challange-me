package com.me.challange.milan.challangeme.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.me.challange.milan.challangeme.ExtraClass.CategorryGT;
import com.me.challange.milan.challangeme.GetterSetter.SubjectsLists;
import com.me.challange.milan.challangeme.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by milan on 11/8/2017.
 */
public class SubjectListAdapter extends ArrayAdapter {
    List list=new ArrayList<>();
    Typeface type;

    public SubjectListAdapter(Context context, int resource) {
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
        ViewHolder viewHolder;
        if(convertView == null){
            viewHolder=new ViewHolder();
            LayoutInflater inflater=LayoutInflater.from(getContext());
            convertView=inflater.inflate(R.layout.adapter_subject_list, parent, false);
            viewHolder.category=(TextView)convertView.findViewById(R.id.adapter_subject_list_item);

            convertView.setTag(viewHolder);
        }else{
            viewHolder=(ViewHolder)convertView.getTag();
        }

        viewHolder.row=(SubjectsLists)this.getItem(position);

        viewHolder.category.setText(viewHolder.row.getName());
        viewHolder.category.setTypeface(type);

        return convertView;
    }

    static class ViewHolder{
        TextView category;
        SubjectsLists row;
    }
}
