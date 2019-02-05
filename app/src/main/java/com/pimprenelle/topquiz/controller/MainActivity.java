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

import com.pimprenelle.topquiz.R;
import com.pimprenelle.topquiz.model.User;

public class MainActivity extends AppCompatActivity {

    private TextView mGreetingText;
    private EditText mNameInput;
    private Button mPlayButton;
    private User mUser;

    public static final int GAME_ACTIVITY_REQUEST_CODE=1;

    private SharedPreferences mPreferences;
    public static final String PREF_KEY_SCORE = "PREF_KEY_SCORE";
    public static final String PREF_KEY_FIRSTNAME = "PREF_KEY_FIRSTNAME";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (GAME_ACTIVITY_REQUEST_CODE==requestCode && RESULT_OK == resultCode){
            int score = data.getIntExtra(GameActivity.BUNDLE_EXTRA_SCORE,0);
            //0 correspond à la valeur par défaut ici
            mUser.setScore(score);
            mPreferences.edit().putInt(PREF_KEY_SCORE,score).apply();

            mGreetingText.setText("Welcome back " + mUser.getFirstName() + "\n You last score was " + mUser.getScore() + ", will you do better this time ?");
            mNameInput.setText(mUser.getFirstName());
            mNameInput.setSelection(mNameInput.getText().length());
            mPlayButton.setEnabled(true);

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGreetingText = (TextView) findViewById(R.id.activity_main_greeting_txt);
        mNameInput = (EditText) findViewById(R.id.activity_main_name_input);
        mPlayButton = (Button) findViewById(R.id.activity_main_play_btn);
        mUser = new User();

        mPreferences = getPreferences(MODE_PRIVATE);

        mPlayButton.setEnabled(false);

        if(mPreferences.getString(PREF_KEY_FIRSTNAME,null)!=null) {
            mUser.setFirstName(mPreferences.getString(PREF_KEY_FIRSTNAME, null));
            mUser.setScore(mPreferences.getInt(PREF_KEY_SCORE, 0));

            mGreetingText.setText("Welcome back " + mUser.getFirstName() + "\n You last score was " + mUser.getScore() + ", will you do better this time ?");
            mNameInput.setText(mUser.getFirstName());
            mNameInput.setSelection(mNameInput.getText().length());
            mPlayButton.setEnabled(true);
        }



        mNameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // This is where we'll check the user input
                mPlayButton.setEnabled(s.toString().length() != 0);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // The user just clicked
                mUser.setFirstName(mNameInput.getText().toString());

                mPreferences.edit().putString(PREF_KEY_FIRSTNAME,mUser.getFirstName()).apply();

                Intent gameActivity = new Intent(MainActivity.this, GameActivity.class);
                startActivityForResult(gameActivity,GAME_ACTIVITY_REQUEST_CODE);
            }
        });




    }
}
