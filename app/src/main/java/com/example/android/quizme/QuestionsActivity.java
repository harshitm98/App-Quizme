package com.example.android.quizme;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

public class QuestionsActivity extends AppCompatActivity {

    private TextView questionText, freezeText;
    private ImageView questionImage;
    private String set;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mQuestionsDatabaseReference, mCandidateDatabaseReference, mInfoCandidateReference;
    private ChildEventListener mChildEventListener, mCandidateChildEventListener;

    public ArrayList<QuestionObject> questionObjects;

    public Button buttonA,buttonB,buttonC,buttonD,next;
    public TextView textViewQuestion;

    int[] questionSequence = {1,2,3,4,5,6,7,8,9,10};

    public String correctAnswer = "";
    public String selectedAnswer = "";

    private String photoUrl;

    @Override
    protected void onPause() {
        super.onPause();
        freezeTheText();

    }

    public void freezeTheText(){
        freezeText.setVisibility(View.VISIBLE);
        mCandidateDatabaseReference.child("freeze").setValue(1);
    }

    @Override
    public void onBackPressed() {
    }

    public static int questionSolved = 0;
    int questionsAttempted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        buttonA = (Button)findViewById(R.id.option_a);
        buttonB = (Button)findViewById(R.id.option_b);
        buttonC = (Button)findViewById(R.id.option_c);
        buttonD = (Button)findViewById(R.id.option_d);
        next = (Button)findViewById(R.id.next);
        textViewQuestion = (TextView)findViewById(R.id.question_text);
        freezeText = (TextView)findViewById(R.id.freeze);
        questionImage = (ImageView)findViewById(R.id.question_image);

        set = "set" + MainActivity.questionSet;
        questionObjects = new ArrayList<QuestionObject>();

        ArrayList<Integer> questionSequenceShuffle = new ArrayList<>();
        for(int i=0;i<questionSequence.length;i++){
            questionSequenceShuffle.add(questionSequence[i]);
        }
        Collections.shuffle(questionSequenceShuffle);


        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mQuestionsDatabaseReference = mFirebaseDatabase.getReference().child("questions").child(set);
        mCandidateDatabaseReference = mFirebaseDatabase.getReference().child("candidates").child(MainActivity.mRegistrationNumber);
        //Log.i("QuestionsActivity",MainActivity.mRegistrationNumber);
        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                QuestionObject questionObject = new QuestionObject();
                questionObject.setQuestion_text(dataSnapshot.child("question_text").getValue().toString());
                questionObject.setCorrect_answer(dataSnapshot.child("correct_answer").getValue().toString());
                questionObject.setOption1(dataSnapshot.child("option1").getValue().toString());
                questionObject.setOption2(dataSnapshot.child("option2").getValue().toString());
                questionObject.setOption3(dataSnapshot.child("option3").getValue().toString());
                questionObject.setOption4(dataSnapshot.child("option4").getValue().toString());
                questionObject.setQuestion_image(dataSnapshot.child("question_image").getValue().toString());

                questionObjects.add(questionObject);

                if(questionObjects.size() == 10){
                    Collections.shuffle(questionObjects);
                    displayQuestions(0);
                }
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {            }
            @Override
            public void onCancelled(DatabaseError databaseError) {            }
        };

        mCandidateChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                String vFreeze = dataSnapshot.child("freeze").getValue(String.class);
                Log.i("QuestionActivity", "" + dataSnapshot);
                Log.i("QuestionActivity", "" + dataSnapshot.getValue().toString());

                if(dataSnapshot.getValue().toString().equals("0")){
                    Log.i("QuestionActivity","Check1");
                    freezeText.setVisibility(View.GONE);
                }
//                if(Objects.equals(vFreeze.trim(), "0")){
//                    freezeText.setVisibility(View.GONE);
//                    Log.i("QuestionActivity", "Umm what is this?");
//                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {           }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) { }
            @Override
            public void onCancelled(DatabaseError databaseError) {      }
        };

        mQuestionsDatabaseReference.addChildEventListener(mChildEventListener);
        mCandidateDatabaseReference.addChildEventListener(mCandidateChildEventListener);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                questionsAttempted++;
                mCandidateDatabaseReference.child("questions_attempted").setValue(questionsAttempted);
                if(Objects.equals(correctAnswer, selectedAnswer)){
                    questionSolved++;
                    mCandidateDatabaseReference.child("questions_solved").setValue(questionSolved);
                }
                if(questionsAttempted < 10){
                    displayQuestions(questionsAttempted);
                }
                else{
                    Intent i = new Intent(QuestionsActivity.this, FinalResultActivity.class);
                    startActivity(i);
                    finishAffinity();
                }

            }
        });

        buttonA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                next.setEnabled(true);
                selectedAnswer = buttonA.getText().toString();
            }
        });

        buttonB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                next.setEnabled(true);
                selectedAnswer = buttonB.getText().toString();
            }
        });

        buttonC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                next.setEnabled(true);
                selectedAnswer = buttonC.getText().toString();
            }
        });

        buttonD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                next.setEnabled(true);
                selectedAnswer = buttonD.getText().toString();
            }
        });
    }

    public void displayQuestions(int k) {

        next.setEnabled(false);
        String[] option = {questionObjects.get(k).getOptionA(), questionObjects.get(k).getOptionB(), questionObjects.get(k).getOptionC(), questionObjects.get(k).getOptionD()};
        ArrayList<String> optionList = new ArrayList<String>(Arrays.asList(option));
        Collections.shuffle(optionList);

        buttonA.setText(optionList.get(0));
        buttonB.setText(optionList.get(1));
        buttonC.setText(optionList.get(2));
        buttonD.setText(optionList.get(3));
        textViewQuestion.setText(questionObjects.get(k).getQuestionText());
        correctAnswer = questionObjects.get(k).getCorrect_answer();
        photoUrl = questionObjects.get(k).getQuestion_image();
        if(photoUrl.equals("null")){
            questionImage.setVisibility(View.GONE);
        }
        else{
            questionImage.setVisibility(View.VISIBLE);
            Glide.with(QuestionsActivity.this).load(photoUrl).into(questionImage);
        }

    }
}
