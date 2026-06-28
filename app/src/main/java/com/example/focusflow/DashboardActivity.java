package com.example.focusflow;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity {
    Button btnstudyPlanner, btnresources, btnprofile, btnfocusMode, btnSaveName;
    EditText etName;
    TextView tvGreeting, tvStreak, tvWeekly;
    ListView lvTodayTasks;
    SharedPreferences sp;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);

        btnstudyPlanner = findViewById(R.id.btnStudyPlanner);
        btnresources = findViewById(R.id.btnResources);
        btnprofile = findViewById(R.id.btnProfile);
        btnfocusMode = findViewById(R.id.btnFocusMode);
        btnSaveName = findViewById(R.id.btnSaveName);
        etName = findViewById(R.id.etName);
        tvGreeting = findViewById(R.id.tvGreeting);
        tvStreak = findViewById(R.id.tvStreak);
        tvWeekly = findViewById(R.id.tvWeekly);
        lvTodayTasks = findViewById(R.id.lvTodayTasks);

        sp = getSharedPreferences("FocusFlowPrefs", MODE_PRIVATE);
        db = openOrCreateDatabase("focusflowdb", MODE_PRIVATE, null);

        db.execSQL("CREATE TABLE IF NOT EXISTS tasks(title VARCHAR, subject VARCHAR, day INTEGER)");
        db.execSQL("CREATE TABLE IF NOT EXISTS sessions(subject VARCHAR, minutes INTEGER, day INTEGER)");

        String username = sp.getString("username", null);

        if(username == null || username.equals("")){
            tvGreeting.setText("Please Enter Your Name");
            etName.setVisibility(View.VISIBLE);
            btnSaveName.setVisibility(View.VISIBLE);
        }
        else{
            tvGreeting.setText("Hello, " + username + "!");
            etName.setVisibility(View.GONE);
            btnSaveName.setVisibility(View.GONE);
        }

        btnSaveName.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                if(etName.getText().toString().isEmpty()) Toast.makeText(DashboardActivity.this, "Enter Your Name", Toast.LENGTH_SHORT).show();

                else{
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("username", etName.getText().toString());
                    editor.apply();

                    tvGreeting.setText("Hello, " + etName.getText().toString() + "!");
                    etName.setVisibility(View.GONE);
                    btnSaveName.setVisibility(View.GONE);
                    Toast.makeText(DashboardActivity.this, "Name Saved", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnstudyPlanner.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(DashboardActivity.this, StudyPlannerActivity.class);
                startActivity(i);
            }
        });

        btnresources.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(DashboardActivity.this, ResourcesActivity.class);
                startActivity(i);
            }
        });

        btnprofile.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(DashboardActivity.this, ProfileActivity.class);
                startActivity(i);
            }
        });

        btnfocusMode.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(DashboardActivity.this, FocusModeActivity.class);
                startActivity(i);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        long today = System.currentTimeMillis() / 86400000L;
        ArrayList<String> todayTasks = new ArrayList<String>();
        Cursor c1 = db.rawQuery("SELECT title FROM tasks WHERE day = " + today, null);
        while(c1.moveToNext()){
            todayTasks.add(c1.getString(0));
        }

        if(todayTasks.size() == 0) todayTasks.add("No tasks for today");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, todayTasks);
        lvTodayTasks.setAdapter(adapter);
        Cursor c2 = db.rawQuery("SELECT SUM(minutes) FROM sessions WHERE day >= " + (today - 6), null);
        int weeklyMinutes = 0;
        if(c2.moveToNext()) weeklyMinutes = c2.getInt(0);
        tvWeekly.setText("Weekly Minutes: " + weeklyMinutes);
        Cursor c3 = db.rawQuery("SELECT DISTINCT day FROM sessions ORDER BY day DESC", null);
        //get all study days numbererd by oldest to newest
        int streak = 0;

        long expected = today;

        while(c3.moveToNext()){
            long day = c3.getLong(0);
            if(day == expected){
                streak++;
                expected--;
            }
            else break;
        }
        tvStreak.setText("Streak: " + streak);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(db != null) db.close();
    //when the screen closses, the db closes safely
    }
}