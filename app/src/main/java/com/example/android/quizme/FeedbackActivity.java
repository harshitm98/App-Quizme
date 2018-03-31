package com.example.android.quizme;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class FeedbackActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        Button submit = (Button)findViewById(R.id.submit_feedback);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(FeedbackActivity.this, "Feedback has been submitted", Toast.LENGTH_SHORT).show();
                Toast.makeText(FeedbackActivity.this, "Thanks for taking the feedback", Toast.LENGTH_SHORT).show();
                finish();
                System.exit(0);
            }
        });
    }
}
