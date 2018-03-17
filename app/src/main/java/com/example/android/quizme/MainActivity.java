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


import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";
    public static String questionSet;
    private Button nextActivityButton;
    public static String mRegistrationNumber, mName, mClassNBR;
    public static EditText registrationNumber;
    public static EditText name;
    private static EditText classNBR;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mCandidateReference, mInfoReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        nextActivityButton = (Button)findViewById(R.id.next_activity);
        registrationNumber = (EditText)findViewById(R.id.registration_number);
        classNBR = (EditText)findViewById(R.id.classNBR);
        name = (EditText)findViewById(R.id.name);


        mFirebaseDatabase = FirebaseDatabase.getInstance();



        nextActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRegistrationNumber  = registrationNumber.getText().toString();
                mName = name.getText().toString();
                mClassNBR = classNBR.getText().toString();
                validateClassNBR();
//                mCandidateReference.child(registrationNumber.getText().toString());
//                mInfoReference = mCandidateReference.child(registrationNumber.getText().toString());
//                mInfoReference.child("reg").setValue(mRegistrationNumber);
//                mInfoReference.child("name").setValue(name.getText().toString());
//                mInfoReference.child("freeze").setValue(0);
//                mInfoReference.child("questions_solved").setValue(0);
//                mInfoReference.child("questions_attempted").setValue(0);
//                Intent i = new Intent(MainActivity.this,QuestionsActivity.class);
//                startActivity(i);
//                finish();
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
    private void validateClassNBR(){
        try{
            mCandidateReference = mFirebaseDatabase.getReference("class").child(mClassNBR).child("classNBR");
            DatabaseReference classNBRreferance = mCandidateReference.child("classNBR");
            mCandidateReference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    if(mClassNBR.equals(dataSnapshot.getValue())){
                        validateTestStatus();
                    }
                    else{
                        Toast.makeText(MainActivity.this, "Class not found", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {                }

                @Override
                public void onCancelled(DatabaseError databaseError) {                }
            });
        }catch(NullPointerException e){
            Toast.makeText(this, "ClassNBR doesn't exist.", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "validateClassNBR: "  + mCandidateReference);
        }



    }
    private void validateTestStatus(){
        //TODO Check the status if the test is live or not
    }
    private void registrationChecker(){
        //TODO Checks if the registration number is in the list
    }
    private void startTest(){
        //TODO After validating all the changes initiate the test
    }

}
