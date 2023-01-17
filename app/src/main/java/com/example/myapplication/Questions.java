package com.example.myapplication;

import java.util.List;

public class Questions {
    String question , answer , questionId ;
    List<String> chooselist;

    public Questions(String question, String answer, List<String> chooselist , String questionId) {
        this.question = question;
        this.answer = answer;
        this.chooselist = chooselist;
        this.questionId = questionId;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public Questions() {
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public List<String> getChooselist() {
        return chooselist;
    }

    public void setChooselist(List<String> chooselist) {
        this.chooselist = chooselist;
    }
}
