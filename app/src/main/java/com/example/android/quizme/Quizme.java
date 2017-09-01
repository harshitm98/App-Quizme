package com.example.android.quizme;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Harshit Maheshwari on 01-09-2017.
 */

public class Quizme extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
