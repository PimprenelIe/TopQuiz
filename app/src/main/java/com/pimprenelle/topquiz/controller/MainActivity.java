package com.pimprenelle.topquiz.controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.pimprenelle.topquiz.model.Score;
import com.pimprenelle.topquiz.model.ScoreList;
import com.pimprenelle.topquiz.model.User;
import com.pimprenelle.topquiz.R;

public class MainActivity extends AppCompatActivity {

    private TextView mGreetingText;
    private EditText mNameInput;
    private Button mPlayButton;
    private Button mScoreButton;
    private User mUser;
    private Score mScore;
    private ScoreList mScoreList;
    private static final int GAME_ACTIVITY_REQUEST_CODE = 42;
    private static final int SCORE_ACTIVITY_REQUEST_CODE = 101;
    private static final String PREF_KEY_FIRSTNAME = "firstName";
    private static final String PREF_KEY_SCORE = "finalScore";
    private static final String PREF_KEY_SCORE_LIST = "listScore";
    private static final String PREF_APP = "prefApp";

    private int score;
    private SharedPreferences mPreferences;

    private boolean clearPref;

    //private ScoreList mListScore;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (GAME_ACTIVITY_REQUEST_CODE == requestCode && RESULT_OK == resultCode) {
            // Fetch the score from the Intent
            score = data.getIntExtra(GameActivity.BUNDLE_EXTRA_SCORE, 0);

            // Update score in SharedPreferences
            mPreferences.edit().putInt(PREF_KEY_SCORE, score).apply();

            // Update views
            greetUser();

            //Update score list
            updateScoreList();

        }

        if (SCORE_ACTIVITY_REQUEST_CODE == requestCode && RESULT_OK == resultCode) {
            // Fetch the score from the Intent
            clearPref = data.getBooleanExtra(ScoreActivity.BUNDLE_CLEAR, false);

            // Update preferences
            mPreferences = getSharedPreferences(PREF_APP,MODE_PRIVATE);

            // Update views
            mGreetingText.setText("Bienvenue ! Comment vous appelez-vous ?");
            mNameInput.setText("");
            mNameInput.setSelection(0);
            mPlayButton.setEnabled(false);
            mScoreButton.setVisibility(View.INVISIBLE);

            //Update score list
            importScoreList();

        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        System.out.println("MainActivity::onCreate()");

        mGreetingText = (TextView) findViewById(R.id.activity_main_greeting_txt);
        mNameInput = (EditText) findViewById(R.id.activity_main_name_input);
        mPlayButton = (Button) findViewById(R.id.activity_main_play_btn);
        mScoreButton = (Button) findViewById(R.id.activity_main_score_btn);
        mUser = new User();
        mScoreList = new ScoreList();

        clearPref=false;
        mPlayButton.setEnabled(false);
        mScoreButton.setVisibility(View.INVISIBLE);

        // Import SharedPreferences
        mPreferences = getSharedPreferences(PREF_APP,MODE_PRIVATE);
        //mPreferences.edit().clear().apply(); // To use to reset preferences

        // Load the greeting title
        greetUser();

        // Load Previous Scoring Table
        importScoreList();

        //Listener to enable the Let's Play button
        mNameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mPlayButton.setEnabled(s.toString().length() != 0);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        mPlayButton.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               // Save the name of the player
                                               mUser.setFirstName(mNameInput.getText().toString());
                                               mPreferences.edit().putString(PREF_KEY_FIRSTNAME, mUser.getFirstName()).apply();

                                               // Load the game
                                               Intent gameActivity = new Intent(MainActivity.this, GameActivity.class);
                                               startActivityForResult(gameActivity, GAME_ACTIVITY_REQUEST_CODE);
                                           }
                                       }

        );

        mScoreButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                // Load the Scoring Table
                                                Intent scoreActivity = new Intent(MainActivity.this, ScoreActivity.class);
                                                startActivityForResult(scoreActivity, SCORE_ACTIVITY_REQUEST_CODE);
                                            }
                                        }

        );



    }

    private void greetUser(){
        String firstName = mPreferences.getString(PREF_KEY_FIRSTNAME, null);
        if(firstName!=null){
            int score = mPreferences.getInt(PREF_KEY_SCORE,0);
            mGreetingText.setText("Bonjour "+firstName+" ! Votre dernier score était "+score+". Ferez-vous mieux cette fois ?");
            mNameInput.setText(firstName);
            mNameInput.setSelection(firstName.length());
            mPlayButton.setEnabled(true);
        }

    }

    private void importScoreList(){
        // Initialize "serialisation" to import scoring table from preferences
        System.out.println("MainActivity::importScoreList()");
        Gson gson = new Gson();

        String json = mPreferences.getString(PREF_KEY_SCORE_LIST,null);

        if(json != null) {
            mScoreList = gson.fromJson(json, ScoreList.class);
            // If there are preferences, set the score button visible
            mScoreButton.setVisibility(View.VISIBLE);
        }

    }

    // Add the new score into our Score List.
    // If it's a bad score, the method addList() will delete it or the baddest score.
    private void updateScoreList(){

        mScoreButton.setVisibility(View.VISIBLE);

        System.out.println("MainActivity::updateScoreList()");

        mScore = new Score(mNameInput.getText().toString(),score);

        System.out.println("mNameInput:: "+ mScore.getPlayerName() +" score:: "+ mScore.getScore());


        mScoreList.addToList(mScore);

        // Save the new score list to use it in ScoreActivity
        Gson gson = new Gson();

        mPreferences.edit()
                .putString(PREF_KEY_SCORE_LIST,gson.toJson(mScoreList))
                .apply();
    }



}