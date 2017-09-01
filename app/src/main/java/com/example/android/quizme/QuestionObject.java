package com.example.android.quizme;

/**
 * Created by Harshit Maheshwari on 30-08-2017.
 */

public class QuestionObject {

    private String question_text;
    private String question_number;
    private String option1;
    private String option2;
    private String option3;
    private String option4;
    private String correct_answer;

    public void setQuestion_text(String question_text) {
        this.question_text = question_text;
    }

    public void setQuestion_number(String question_number) {
        this.question_number = question_number;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
    }

    public void setOption4(String option4) {
        this.option4 = option4;
    }

    public void setCorrect_answer(String correct_answer) {
        this.correct_answer = correct_answer;
    }



    public QuestionObject(){    }

//    public QuestionObject(String question_number,String question_text, String option1, String option2, String option3, String option4,
//                          int correct_answer){
//        this.question_number = question_number;
//        this.question_text = question_text;
//        this.option1 = option1;
//        this.option2 = option2;
//        this.option3 = option3;
//        this.option4 = option4;
//        this.correct_answer = correct_answer;
//    }

    public String getQuestionNumber(){
        return question_number;
    }

    public String getQuestionText(){
        return question_text;
    }

    public String getOptionA(){
        return option1;
    }

    public String getOptionB(){
        return option2;
    }

    public String getOptionC(){
        return option3;
    }

    public String getOptionD(){
        return option4;
    }
    public String getCorrect_answer(){
        return correct_answer;
    }

}
