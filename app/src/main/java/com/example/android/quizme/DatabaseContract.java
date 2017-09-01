package com.example.android.quizme;

import android.provider.BaseColumns;

/**
 * Created by Harshit Maheshwari on 30-08-2017.
 */

public class DatabaseContract implements BaseColumns {

    public static final String QUESTIONS_TABLE_NAME = "question";

    public static final String _ID = BaseColumns._ID;
    public static final String USERS_QUESTION_TEXT = "question_text";
    //public static final String USERS_QUESTION_IMAGE = "question_image";
    public static final String USERS_OPTION_A = "optionA";
    public static final String USERS_OPTION_B = "optionB";
    public static final String USERS_OPTION_C = "optionC";
    public static final String USERS_OPTION_D = "optionD";
    public static final String USERS_CORRECT_ANSWER = "correct_answer";


}
