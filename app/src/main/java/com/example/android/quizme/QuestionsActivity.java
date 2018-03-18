package com.example.android.quizme;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vstechlab.easyfonts.EasyFonts;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

public class QuestionsActivity extends AppCompatActivity {

    private TextView freezeText,timerText;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mQuestionsDatabaseReference, mCandidateDatabaseReference, mCandidatesDatabaseReference;
    private ChildEventListener mChildEventListener, mCandidateChildEventListener;

    public ArrayList<QuestionObject> questionObjects;

    public Button buttonA,buttonB,buttonC,buttonD,next;
    public TextView textViewQuestion;

    int[] questionSequence = {1,2,3,4,5,6,7,8,9,10};

    public String correctAnswer = "";
    public String selectedAnswer = "";
    public LinearLayout layout;
    public RelativeLayout relativeLayout;

    private String photoUrl;

    public ProgressBar progressBar, imageProgressBar;

    private CountDownTimer timer;

    public long min, sec;

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

        questionSolved = 0;
        progressBar = (ProgressBar)findViewById(R.id.progress);
        buttonA = (Button)findViewById(R.id.option_a);
        buttonB = (Button)findViewById(R.id.option_b);
        buttonC = (Button)findViewById(R.id.option_c);
        buttonD = (Button)findViewById(R.id.option_d);
        next = (Button)findViewById(R.id.next);
        textViewQuestion = (TextView)findViewById(R.id.question_text);
        progressBar.setVisibility(View.VISIBLE);
        layout = (LinearLayout)findViewById(R.id.linear_layout);
        layout.setVisibility(View.INVISIBLE);
        imageProgressBar = (ProgressBar)findViewById(R.id.image_progress);
        relativeLayout = (RelativeLayout)findViewById(R.id.relative);

        buttonA.setTypeface(EasyFonts.walkwayUltraBold(getApplicationContext()));
        buttonB.setTypeface(EasyFonts.walkwayUltraBold(getApplicationContext()));
        buttonC.setTypeface(EasyFonts.walkwayUltraBold(getApplicationContext()));
        buttonD.setTypeface(EasyFonts.walkwayUltraBold(getApplicationContext()));
        textViewQuestion.setTypeface(EasyFonts.walkwayUltraBold(getApplicationContext()));


        freezeText = (TextView)findViewById(R.id.freeze);

        timerText = (TextView)findViewById(R.id.timer);
        questionObjects = new ArrayList<QuestionObject>();

        ArrayList<Integer> questionSequenceShuffle = new ArrayList<>();
        for(int i=0;i<questionSequence.length;i++){
            questionSequenceShuffle.add(questionSequence[i]);
        }
        Collections.shuffle(questionSequenceShuffle);


        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mQuestionsDatabaseReference = mFirebaseDatabase.getReference().child("class").child(MainActivity.mClassNBR).child("questions");
        mCandidateDatabaseReference = mFirebaseDatabase.getReference().child("class").child(MainActivity.mClassNBR).child("candidates").child(MainActivity.mRegistrationNumber);
        mCandidatesDatabaseReference = mFirebaseDatabase.getReference().child("class").child(MainActivity.mClassNBR).child("candidates");
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

                questionObjects.add(questionObject);

                if(questionObjects.size() == 10){
                    timer.start();
                    progressBar.setVisibility(View.INVISIBLE );
                    layout.setVisibility(View.VISIBLE);
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
                if(dataSnapshot.child("reg").getValue().toString().equals(MainActivity.mRegistrationNumber)){
                    if(dataSnapshot.child("freeze").getValue().toString().equals("0")){
                        Log.i("QuestionActivity","Check1");
                        freezeText.setVisibility(View.GONE);
                    }
                    if(dataSnapshot.child("freeze").getValue().toString().equals("1")){
                        freezeTheText();
                    }
                }

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {           }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) { }
            @Override
            public void onCancelled(DatabaseError databaseError) {      }
        };

        mQuestionsDatabaseReference.addChildEventListener(mChildEventListener);
        mCandidatesDatabaseReference.addChildEventListener(mCandidateChildEventListener);

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
                    timer.onFinish();
                }
                buttonA.setBackgroundColor(Color.rgb(0, 103, 91));
                buttonB.setBackgroundColor(Color.rgb(0, 103, 91));
                buttonC.setBackgroundColor(Color.rgb(0, 103, 91));
                buttonD.setBackgroundColor(Color.rgb(0, 103, 91));

            }
        });

        buttonA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                next.setEnabled(true);
                buttonA.setBackgroundColor(Color.rgb(82, 199, 184));
                buttonB.setBackgroundColor(Color.rgb(0, 103, 91));
                buttonC.setBackgroundColor(Color.rgb(0, 103, 91));
                buttonD.setBackgroundColor(Color.rgb(0, 103, 91));
                selectedAnswer = buttonA.getText().toString();
            }
        });

        buttonB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                next.setEnabled(true);
                buttonB.setBackgroundColor(Color.rgb(82, 199, 184));
                buttonA.setBackgroundColor(Color.rgb(0, 103, 91));
                buttonC.setBackgroundColor(Color.rgb(0, 103, 91));
                buttonD.setBackgroundColor(Color.rgb(0, 103, 91));
                selectedAnswer = buttonB.getText().toString();
            }
        });

        buttonC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonC.setBackgroundColor(Color.rgb(82, 199, 184));
                buttonB.setBackgroundColor(Color.rgb(0, 103, 91));
                buttonA.setBackgroundColor(Color.rgb(0, 103, 91));
                buttonD.setBackgroundColor(Color.rgb(0, 103, 91));
                next.setEnabled(true);
                selectedAnswer = buttonC.getText().toString();
            }
        });

        buttonD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonD.setBackgroundColor(Color.rgb(82, 199, 184));
                buttonB.setBackgroundColor(Color.rgb(0, 103, 91));
                buttonC.setBackgroundColor(Color.rgb(0, 103, 91));
                buttonA.setBackgroundColor(Color.rgb(0, 103, 91));
                next.setEnabled(true);
                selectedAnswer = buttonD.getText().toString();
            }
        });

        timer = new CountDownTimer(10000*60,1000) {
            @Override
            public void onTick(long l) {
                min = (l/1000)/60 - ((l/1000)%60)/60;
                sec = (l/1000)%60;

                if(min<10 && sec<10){
                    timerText.setText("0"+ min + ":" + "0"+ sec);
                }
                else if(min<10 && sec>=10){
                    timerText.setText("0" + min + ":" + sec);
                }
                else if(min>=10 && sec>=10){
                    timerText.setText(min + ":" + sec);
                }

                if(min == 1 && sec == 0){
                    Toast.makeText(getApplicationContext(),"Last 1 minute left!",Toast.LENGTH_SHORT).show();
                    timerText.setTextColor(Color.RED);
                }
            }

            @Override
            public void onFinish() {
                Intent i = new Intent(QuestionsActivity.this, FinalResultActivity.class);
                startActivity(i);
                finishAffinity();
            }
        };
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
        relativeLayout.setVisibility(View.GONE);
        imageProgressBar.setVisibility(View.GONE);


    }
}
