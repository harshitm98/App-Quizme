package com.example.android.quizme;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class FinalResultActivity extends AppCompatActivity {

    public TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_result);

        result = (TextView)findViewById(R.id.result);

        result.setText(QuestionsActivity.questionSolved + "/10");

    }
}
