package com.example.android.quizme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


public class MainActivity extends Activity {

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
        //scanButton.setTypeface(EasyFonts.walkwayUltraBold(getApplicationContext()));
        nextActivityButton = (Button)findViewById(R.id.next_activity);
        //nextActivityButton.setTypeface(EasyFonts.walkwayUltraBold(getApplicationContext()));
        nextActivityButton.setEnabled(false);
        nextActivityButton.setVisibility(View.INVISIBLE);
        registrationNumber = (EditText)findViewById(R.id.registration_number);
        //registrationNumber.setTypeface(EasyFonts.walkwayUltraBold(getApplicationContext()));
        name = (EditText)findViewById(R.id.name);
        //name.setTypeface(EasyFonts.walkwayUltraBold(getApplicationContext()));
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

        registrationNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String reg = registrationNumber.getText().toString().trim();
                if(registrationNumber.getText().toString().trim().length()!=9){
                    registrationNumber.setError("Incorrect registration number");
                }
                if(reg.length()==9 && !(reg.charAt(0) == '1' && (reg.charAt(1) == '3' || reg.charAt(1) == '4' || reg.charAt(1) == '5' || reg.charAt(1) == '6' || reg.charAt(1) == '7'))){
                    registrationNumber.setError("Incorrect registration number");
                }
                if(reg.length() == 9 && !(reg.charAt(2) == 'B' || reg.charAt(2) == 'M')){
                    registrationNumber.setError("Incorrect registration number");
                }
                if(reg.length() == 9){
                    int k;
                    int z=1;
                    for(int r=5;r<9;r++) {
                        k = (int)reg.charAt(r);
                        Log.i("MainActivity", "Num: " +k);
                        if (k < 48 || k > 57) {
                            z=0;
                        }
                    }
                    for(int r=3;r<5;r++){
                        k = (int)reg.charAt(r);
                        Log.i("MainActivity", "Letters: " +k);
                        if(k<65 || k>90){
                            z=0;
                        }
                    }
                    if(z==0)
                    registrationNumber.setError("Incorrect registration number");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

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
        String reg = registrationNumber.getText().toString().trim();
        Log.i("MainActivity",reg);
        if(name.getText().toString().equals("") || registrationNumber.getText().toString().equals("")){
            Toast.makeText(this,"Please fill name and registration number and then scan the QR code again!",Toast.LENGTH_LONG).show();
        }

        else{
            nextActivityButton.setVisibility(View.VISIBLE);
            nextActivityButton.setEnabled(true);
        }
    }

}
