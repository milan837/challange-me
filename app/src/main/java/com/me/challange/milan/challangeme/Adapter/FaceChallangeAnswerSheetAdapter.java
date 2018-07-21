package com.me.challange.milan.challangeme.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.me.challange.milan.challangeme.R;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by milan on 10/31/2017.
 */
public class FaceChallangeAnswerSheetAdapter extends ArrayAdapter {
    List<String> questions,firstAns,secondAns,thirdAns,fourthAns,correctAns,givenAns=new ArrayList<>();
    Context ctx;
    Typeface type;
    public FaceChallangeAnswerSheetAdapter(Context context, int resource,List<String> question,List<String> first,List<String> second,List<String> third,List<String> fourth,List<String> correct,List<String> given)  {
        super(context, resource,given);
        this.questions=question;
        this.firstAns=first;
        this.secondAns=second;
        this.thirdAns=third;
        this.fourthAns=fourth;
        this.correctAns=correct;
        this.givenAns=given;
        this.ctx=context;
        this.type= Typeface.createFromAsset(context.getAssets(),"font/regular.ttf");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=LayoutInflater.from(getContext());
        View view=inflater.inflate(R.layout.adapter_answer_sheet_layout_items, parent, false);

        final TextView questionForUser=(TextView)view.findViewById(R.id.adapter_face_challanges_face_question_question);
        final TextView firstAnswer=(TextView)view.findViewById(R.id.adapter_first_answer);
        final TextView secondAnswer=(TextView)view.findViewById(R.id.adapter_second_answer);
        final TextView thirdAnswer=(TextView)view.findViewById(R.id.adapter_third_answer);
        final TextView fourthAnswer=(TextView)view.findViewById(R.id.adapter_fourth_answer);
        final TextView correctAnswer=(TextView)view.findViewById(R.id.adapter_correct_answer);
        final TextView givenAnswer=(TextView)view.findViewById(R.id.adapter_given_answer);
        ImageView imageView=(ImageView)view.findViewById(R.id.adapter_face_challanges_face_right_wrong);

        questionForUser.setText(questions.get(position));
        firstAnswer.setText(firstAns.get(position));
        secondAnswer.setText(secondAns.get(position));
        thirdAnswer.setText(thirdAns.get(position));
        fourthAnswer.setText(fourthAns.get(position));
        givenAnswer.setText("Your Ans : "+givenAns.get(position));
        correctAnswer.setText("Answer : "+correctAns.get(position));

        if(givenAns.get(position).equals(correctAns.get(position))){
            imageView.setImageDrawable(ctx.getResources().getDrawable(R.mipmap.ic_correct));
        }else{
            imageView.setImageDrawable(ctx.getResources().getDrawable(R.mipmap.ic_cross));
        }

        questionForUser.setTypeface(type);
        firstAnswer.setTypeface(type);
        secondAnswer.setTypeface(type);
        thirdAnswer.setTypeface(type);
        fourthAnswer.setTypeface(type);
        correctAnswer.setTypeface(type);
        givenAnswer.setTypeface(type);


        return view;
    }
}
