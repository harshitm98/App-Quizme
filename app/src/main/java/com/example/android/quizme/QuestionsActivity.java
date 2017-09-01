package com.example.android.quizme;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class QuestionsActivity extends AppCompatActivity {

    private TextView questionText;
    private ImageView questionImage;
//    private Button buttonA, buttonB, buttonC, buttonD;
    private String set;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mQuestionsDatabaseReference, mCandidateDatabaseReference, mInfoCandidateReference;
    private ChildEventListener mChildEventListener;

    public QuestionDbHelper mDbHelper;
    public SQLiteDatabase database;
    private ContentValues values;

    public Cursor cursor;
    public int questionTextColumns;
    public int optionAColumns;
    public int optionBColumns;
    public int optionCColumns;
    public int optionDColumns;

    public ArrayList<QuestionObject> questionObjects;

    public Button buttonA,buttonB,buttonC,buttonD,next;
    public TextView textViewQuestion;

    int[] questionSequence = {1,2,3,4,5,6,7,8,9,10};



    int questionSolved=0;
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

        set = "set" + MainActivity.questionSet;
        mDbHelper = new QuestionDbHelper(this);
        database = mDbHelper.getWritableDatabase();
        questionObjects = new ArrayList<QuestionObject>();

        ArrayList<Integer> questionSequenceShuffle = new ArrayList<>();
        for(int i=0;i<questionSequence.length;i++){
            questionSequenceShuffle.add(questionSequence[i]);
        }
        Collections.shuffle(questionSequenceShuffle);


        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mQuestionsDatabaseReference = mFirebaseDatabase.getReference().child("questions").child(set);
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
                    Collections.shuffle(questionObjects);
                    displayQuestions(0);
                }

//                values = new ContentValues();
//                values.put(DatabaseContract.USERS_QUESTION_TEXT,questionObject.getQuestionText());
//                values.put(DatabaseContract.USERS_CORRECT_ANSWER,questionObject.getCorrect_answer());
//                values.put(DatabaseContract.USERS_OPTION_A,questionObject.getOptionA());
//                values.put(DatabaseContract.USERS_OPTION_B,questionObject.getOptionB());
//                values.put(DatabaseContract.USERS_OPTION_C,questionObject.getOptionC());
//                values.put(DatabaseContract.USERS_OPTION_D,questionObject.getOptionD());



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
        mQuestionsDatabaseReference.addChildEventListener(mChildEventListener);

//        for(int j=0;j<questionObjects.size();j++){
//            Log.i("MainActivity", questionObjects.get(j).getCorrect_answer());
//            Log.i("MainActivity", questionObjects.get(j).getQuestionText());
//        }
       // readQuestions();
        //readParticularQuestion(2);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                questionsAttempted++;
                displayQuestions(questionsAttempted);
            }
        });

    }

    public void displayQuestions(int k){


        String[] option = {questionObjects.get(k).getOptionA(),questionObjects.get(k).getOptionB(),questionObjects.get(k).getOptionC(),questionObjects.get(k).getOptionD()};
        ArrayList<String> optionList = new ArrayList<String>(Arrays.asList(option));
        Collections.shuffle(optionList);

        buttonA.setText(optionList.get(0));
        buttonB.setText(optionList.get(1));
        buttonC.setText(optionList.get(2));
        buttonD.setText(optionList.get(3));
        textViewQuestion.setText(questionObjects.get(k).getQuestionText());
    }



    public void readParticularQuestion(int i){
//        cursor.moveToPosition(i);
//        String questionText = cursor.getString(questionTextColumns);
//        String optionA = cursor.getString(optionAColumns);
//        String optionB = cursor.getString(optionBColumns);
//        String optionC = cursor.getString(optionCColumns);
//        String optionD = cursor.getString(optionDColumns);
//
//        Log.i("MainActivity", optionA);
//        Log.i("MainActivity", optionB);
//        Log.i("MainActivity", optionC);
//        Log.i("MainActivity", optionD);
//
//
//        String options[] =  { optionA, optionB, optionC, optionD};

//
//        buttonA.setText(optionList.get(0));
//        buttonB.setText(optionList.get(1));
//        buttonC.setText(optionList.get(2));
//        buttonD.setText(optionList.get(3));
//        textViewQuestion.setText(questionText);

    }
//
//    public void readQuestions(){
//        String[] projection = {
//                DatabaseContract.USERS_QUESTION_TEXT,
//                DatabaseContract.USERS_OPTION_A,
//                DatabaseContract.USERS_OPTION_B,
//                DatabaseContract.USERS_OPTION_C,
//                DatabaseContract.USERS_OPTION_D,
//                DatabaseContract.USERS_CORRECT_ANSWER
//        };
//
//        cursor = database.query(
//                DatabaseContract.QUESTIONS_TABLE_NAME ,
//                projection,
//                null,
//                null,
//                null,
//                null,
//                null
//        );
//
//        questionTextColumns = cursor.getColumnIndex(DatabaseContract.USERS_QUESTION_TEXT);
//        optionAColumns = cursor.getColumnIndex(DatabaseContract.USERS_OPTION_A);
//        optionBColumns = cursor.getColumnIndex(DatabaseContract.USERS_OPTION_B);
//        optionCColumns = cursor.getColumnIndex(DatabaseContract.USERS_OPTION_C);
//        optionDColumns = cursor.getColumnIndex(DatabaseContract.USERS_OPTION_D);
//    }


    @Override
    public void onBackPressed() {
    }
}
