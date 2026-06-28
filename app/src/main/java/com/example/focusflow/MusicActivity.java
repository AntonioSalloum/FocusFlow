package com.example.focusflow;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MusicActivity extends AppCompatActivity {
    Button btnPlay, btnPause, btnStop, btnBack;
    MediaPlayer mp;
    int flag = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_music);
        btnPlay = findViewById(R.id.btnPlay);
        btnPause = findViewById(R.id.btnPause);
        btnStop = findViewById(R.id.btnStop);
        btnBack = findViewById(R.id.btnBack);

        mp = MediaPlayer.create(this, R.raw.jazz);

        btnPause.setEnabled(false);
        btnStop.setEnabled(false);

        btnPlay.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (flag == 0) mp = MediaPlayer.create(MusicActivity.this, R.raw.jazz);
                mp.start();
                btnPlay.setEnabled(false);
                btnPause.setEnabled(true);
                btnStop.setEnabled(true);
                flag = 1;
            }
        });

        btnPause.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mp.pause();
                btnPlay.setEnabled(true);
                btnPause.setEnabled(false);
                btnStop.setEnabled(true);
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mp.stop();
                btnPlay.setEnabled(true);
                btnPause.setEnabled(false);
                btnStop.setEnabled(false);
                flag = 0;
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        if (mp != null){
            mp.release();
            mp = null;
        }
    }
}