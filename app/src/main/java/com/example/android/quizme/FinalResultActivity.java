package com.example.android.quizme;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class FinalResultActivity extends AppCompatActivity {

    public TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_result);

        result = (TextView)findViewById(R.id.questions_solved_result);
        result.setText(QuestionsActivity.questionSolved + "");



        switch (QuestionsActivity.questionSolved){
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
                result.setBackground(getDrawable(R.drawable.circle_red));
                break;
            case 5:
            case 6:
            case 7:
                result.setBackground(getDrawable(R.drawable.magnitude_circle));
                break;
            case 8:
            case 9:
            case 10:
                result.setBackground(getDrawable(R.drawable.circle_green));
                break;
        }



    }
}
