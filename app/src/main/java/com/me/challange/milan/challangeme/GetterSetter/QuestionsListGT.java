package com.me.challange.milan.challangeme.GetterSetter;

/**
 * Created by milan on 11/10/2017.
 */
public class QuestionsListGT {
    String answer,options,ques;

    public QuestionsListGT() {

    }

    public QuestionsListGT(String answer, String options, String ques) {
        this.answer = answer;
        this.options = options;
        this.ques = ques;
    }

    public String getAnswer() {
        return answer;
    }

    public String getOptions() {
        return options;
    }

    public String getQues() {
        return ques;
    }
}
