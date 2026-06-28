package com.example.focusflow;

import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class TimerActivity extends AppCompatActivity {
    TextView tvTimer;
    Button btnStart, btnPause, btnStop, btnReset, btnBack;
    CountDownTimer countDownTimer;
    long timeLeft = 25 * 60 * 1000; //set the time left to 25mins in milliseconds
    SharedPreferences sp;
    SQLiteDatabase mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_timer);

        tvTimer = findViewById(R.id.tvTimer);
        btnStart = findViewById(R.id.btnStart);
        btnPause = findViewById(R.id.btnPause);
        btnStop = findViewById(R.id.btnStop);
        btnReset = findViewById(R.id.btnReset);
        btnBack = findViewById(R.id.btnBack);

        sp = getSharedPreferences("FocusFlowPrefs", MODE_PRIVATE);
        mydb = openOrCreateDatabase("focusflowdb", MODE_PRIVATE, null);
        mydb.execSQL("CREATE TABLE IF NOT EXISTS sessions(subject VARCHAR, minutes INTEGER, day INTEGER)");
        showTime();

        btnStart.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startTimer();
            }
        });

        btnPause.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (countDownTimer != null) countDownTimer.cancel();
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (countDownTimer != null) countDownTimer.cancel();
                timeLeft = 0;
                showTime();
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (countDownTimer != null) countDownTimer.cancel();
                timeLeft = 25 * 60 * 1000;
                showTime();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void startTimer(){
        countDownTimer = new CountDownTimer(timeLeft, 1000){
            //create a new countdown timer and upate it every one second
            @Override
            public void onTick(long millisUntilFinished){
                timeLeft = millisUntilFinished;
                // this updates the remaining time
                showTime();
                //runs every second while the timer is counting down
            }

            @Override
            public void onFinish() {
                //runs when the timer reaches zero
                String subject = sp.getString("selectedSubject", "General");
                long today = System.currentTimeMillis() / 86400000L;
                mydb.execSQL("INSERT INTO sessions VALUES ('" + subject + "',25," + today + ")");
                AlertDialog.Builder b = new AlertDialog.Builder(TimerActivity.this);
                //alert builder prepares a pop up message
                b.setTitle("Session Complete");
                b.setMessage("Your 25 minute focus session is complete.");
                b.setCancelable(true);
                b.show();

                timeLeft = 25 * 60 * 1000;
                showTime();
            }
        };
        countDownTimer.start();
    }

    public void showTime(){
        int minutes = (int) (timeLeft / 1000) / 60;
        int seconds = (int) (timeLeft / 1000) % 60;
        tvTimer.setText(String.format("%02d:%02d", minutes, seconds));
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        if (countDownTimer != null) countDownTimer.cancel();
        if (mydb != null) mydb.close();
    }
}