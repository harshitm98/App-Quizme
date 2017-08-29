package com.example.android.quizme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {

    private Button scanButton;
    private IntentIntegrator qrScan;
    public static String questionSet;
    private Button nextActivityButton;
    public static EditText registrationNumber;
    public static EditText name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scanButton = (Button)findViewById(R.id.scanner);
        nextActivityButton = (Button)findViewById(R.id.next_activity);
        nextActivityButton.setEnabled(false);
        registrationNumber = (EditText)findViewById(R.id.registration_number);
        name = (EditText)findViewById(R.id.name);
        qrScan = new IntentIntegrator(this);

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qrScan.initiateScan();
            }
        });

        nextActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,QuestionsActivity.class);
                startActivity(i);
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

                if(questionSet!=null){
                    checker();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void checker(){
        if((name.getText().toString() == null) || (registrationNumber.getText().toString() == null)){
            Toast.makeText(this,"Please fill name and registeration number and then scan the QR code again!",Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(this,registrationNumber.getText().toString() + name.getText().toString(),Toast.LENGTH_LONG).show();
            nextActivityButton.setEnabled(true);
        }
    }


}
