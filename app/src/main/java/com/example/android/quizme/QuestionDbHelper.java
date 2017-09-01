package com.example.android.quizme;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.android.quizme.DatabaseContract;

/**
 * Created by Harshit Maheshwari on 30-08-2017.
 */

public class QuestionDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "questions.db";

    public QuestionDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static final String create_table = "CREATE TABLE " + DatabaseContract.QUESTIONS_TABLE_NAME +
            " (" + DatabaseContract._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            DatabaseContract.USERS_QUESTION_TEXT + " TEXT NOT NULL, " +
            DatabaseContract.USERS_OPTION_A + " TEXT NOT NULL, " +
            DatabaseContract.USERS_OPTION_B + " TEXT NOT NULL, " +
            DatabaseContract.USERS_OPTION_C + " TEXT NOT NULL, " +
            DatabaseContract.USERS_OPTION_D + " TEXT NOT NULL, " +
            DatabaseContract.USERS_CORRECT_ANSWER + " TEXT NOT NULL);";

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(create_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
