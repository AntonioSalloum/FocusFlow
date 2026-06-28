package com.example.focusflow;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class FocusModeActivity extends AppCompatActivity {

    Button btnMusic, btnTimer, btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_focus_mode);

        btnMusic = findViewById(R.id.btnMusic);
        btnTimer = findViewById(R.id.btnTimer);
        btnBack = findViewById(R.id.btnBack);

        btnMusic.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                Intent i = new Intent(FocusModeActivity.this, MusicActivity.class);
                startActivity(i);
            }
        });

        btnTimer.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(FocusModeActivity.this, TimerActivity.class);
                startActivity(i);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });
    }
}