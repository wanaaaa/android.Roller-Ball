package com.zybooks.rollerball;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Collections;

import java.util.List;

public class menuActivity extends AppCompatActivity {
    private MediaPlayer mMediaPlayer;
    private TextView hScoreView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        VariableSingle vSing = VariableSingle.getInstance();
        vSing.scoreSQL = new ScoreSQL(getApplicationContext());

        List<Long> timeList = vSing.scoreSQL.getData();
        Collections.sort(timeList);
        long bigLong = timeList.get(timeList.size() -1);
        //Log.d(TAG, "onStartGame: @@@@@@@@@@@@@@@@@@@@@@@@@@ ->" + bigLong);
        hScoreView = findViewById(R.id.topScoreTViewID);
        String hStr = "The highest score: " + Long.toString(bigLong) + " from SQL";
        hScoreView.setText(hStr);
    }

    public void onStartGame(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void onPlayBMusic(View v) {
        //MPlayerApplication mApp = new MPlayerApplication();
        if(mMediaPlayer == null) {
            mMediaPlayer = MediaPlayer.create(this, R.raw.energy);
            mMediaPlayer.start();
        } else {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }


}