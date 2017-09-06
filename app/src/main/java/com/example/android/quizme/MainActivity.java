package com.example.android.quizme;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.vstechlab.easyfonts.EasyFonts;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private Button scanButton;
    private IntentIntegrator qrScan;
    public static String questionSet;
    private Button nextActivityButton;
    public static String mRegistrationNumber, mName;
    public static EditText registrationNumber;
    public static EditText name;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mCandidateReference, mInfoReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scanButton = (Button)findViewById(R.id.scanner);
        scanButton.setTypeface(EasyFonts.walkwayUltraBold(getApplicationContext()));
        nextActivityButton = (Button)findViewById(R.id.next_activity);
        nextActivityButton.setTypeface(EasyFonts.walkwayUltraBold(getApplicationContext()));
        nextActivityButton.setEnabled(false);
        registrationNumber = (EditText)findViewById(R.id.registration_number);
        registrationNumber.setTypeface(EasyFonts.walkwayUltraBold(getApplicationContext()));
        name = (EditText)findViewById(R.id.name);
        name.setTypeface(EasyFonts.walkwayUltraBold(getApplicationContext()));
        qrScan = new IntentIntegrator(this);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mCandidateReference = mFirebaseDatabase.getReference().child("candidates");

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qrScan.initiateScan();
            }
        });




        nextActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRegistrationNumber  = registrationNumber.getText().toString();
                mName = name.getText().toString();
                mCandidateReference.child(registrationNumber.getText().toString());
                mInfoReference = mCandidateReference.child(registrationNumber.getText().toString());
                mInfoReference.child("reg").setValue(mRegistrationNumber);
                mInfoReference.child("name").setValue(name.getText().toString());
                mInfoReference.child("freeze").setValue(0);
                mInfoReference.child("questions_solved").setValue(0);
                mInfoReference.child("questions_attempted").setValue(0);
                Intent i = new Intent(MainActivity.this,QuestionsActivity.class);
                startActivity(i);
                finish();
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
                questionSet = result.getContents().trim();

                if(questionSet.equals("A")||questionSet.equals("B")||questionSet.equals("C")||questionSet.equals("D")){
                    checker();
                }
                else{
                    Toast.makeText(this,"Please make sure that you scanned the right QR code",Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void checker(){
        if((Objects.equals(name.getText().toString(), "")) || (Objects.equals(registrationNumber.getText().toString(), ""))){
            Toast.makeText(this,"Please fill name and registeration number and then scan the QR code again!",Toast.LENGTH_LONG).show();
        }
        else{
            nextActivityButton.setEnabled(true);
        }
    }


}
