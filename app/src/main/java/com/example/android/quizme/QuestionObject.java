package com.example.android.quizme;

/**
 * Created by Harshit Maheshwari on 30-08-2017.
 */

public class QuestionObject {

    private String questionText;
    private String questionNumber;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private int correct_answer;

    public QuestionObject(){    }

    public QuestionObject(String questionNumber,String questionText, String optionA, String optionB, String optionC, String optionD,
                          int correct_answer){
        this.questionNumber = questionNumber;
        this.questionText = questionText;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.correct_answer = correct_answer;
    }

    public String getQuestionNumber(){
        return questionNumber;
    }

    public String getQuestionText(){
        return questionText;
    }

    public String getOptionA(){
        return optionA;
    }

    public String getOptionB(){
        return optionB;
    }

    public String getOptionC(){
        return optionC;
    }

    public String getOptionD(){
        return optionD;
    }

    public int getCorrect_answer(){
        return correct_answer;
    }

}
