package com.example.focusflow;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ProfileActivity extends AppCompatActivity{
    Button btnBack, btnClear;
    TextView tvUsername, tvSubject, tvStudyMode;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        btnBack = findViewById(R.id.btnBack);
        btnClear = findViewById(R.id.btnClear);
        tvUsername = findViewById(R.id.tvUsername);
        tvSubject = findViewById(R.id.tvSubject);
        tvStudyMode = findViewById(R.id.tvStudyMode);

        sp = getSharedPreferences("FocusFlowPrefs", MODE_PRIVATE);

        String username = sp.getString("username", "No username saved");
        String subject = sp.getString("selectedSubject", "No subject saved");
        String mode = sp.getString("studyMode", "No study mode saved");

        tvUsername.setText("Username: " + username);
        tvSubject.setText("Subject: " + subject);
        tvStudyMode.setText("Study Mode: " + mode);

        btnBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                SharedPreferences.Editor editor = sp.edit();
                editor.clear();
                editor.apply();

                SQLiteDatabase db = openOrCreateDatabase("focusflowdb", MODE_PRIVATE, null);
                db.execSQL("CREATE TABLE IF NOT EXISTS tasks(title VARCHAR, subject VARCHAR, day INTEGER)");
                db.execSQL("CREATE TABLE IF NOT EXISTS sessions(subject VARCHAR, minutes INTEGER, day INTEGER)");

                db.execSQL("DELETE FROM tasks");
                db.execSQL("DELETE FROM sessions");

                db.close();

                Toast.makeText(ProfileActivity.this, "All Data Cleared", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(ProfileActivity.this, DashboardActivity.class);
                startActivity(i);
                finish();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}