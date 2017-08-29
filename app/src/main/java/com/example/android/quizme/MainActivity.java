package com.example.android.quizme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.client.result.TextParsedResult;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {

    private Button scanButton;
    private IntentIntegrator qrScan;
    private String questionSet;
    private TextView textViewQuestionSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scanButton = (Button)findViewById(R.id.scanner);
        textViewQuestionSet = (TextView)findViewById(R.id.question_set);

        qrScan = new IntentIntegrator(this);

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qrScan.initiateScan();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(result!=null){
            if(result.getContents()==null){
                Toast.makeText(this,"Result not found",Toast.LENGTH_SHORT).show();
            }
            else{
                questionSet = result.getContents();
                textViewQuestionSet.setText("Your set is: Set " + questionSet);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
