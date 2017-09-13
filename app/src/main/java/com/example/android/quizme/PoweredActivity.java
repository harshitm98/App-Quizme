package com.example.android.quizme;

import android.app.Activity;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class PoweredActivity extends Activity {

    private CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_powered);

        timer = new CountDownTimer(3000,1000) {
            @Override
            public void onTick(long l) {
            }

            @Override
            public void onFinish() {
                Intent i = new Intent(PoweredActivity.this,MainActivity.class);
                startActivity(i);
                finishAffinity();
            }
        };

        timer.start();


    }
}
