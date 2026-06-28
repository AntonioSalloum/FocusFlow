package com.example.focusflow;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.content.SharedPreferences;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class StudyPlannerActivity extends AppCompatActivity{
    Spinner subjectSpinner;
    EditText etTask;
    Button btnAdd, btnSave, btnBack;
    ListView lvTasks;
    RadioGroup radioStudyMode;
    RadioButton rbtnPomodoro, rbtnDeepStudy, rbtnQuickView;
    ArrayList<String> tasks;
    ArrayAdapter tasksAdapter;
    SharedPreferences sp;
    SQLiteDatabase mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_study_planner);

        subjectSpinner = findViewById(R.id.subjectSpinner);
        etTask = findViewById(R.id.etTask);
        btnAdd = findViewById(R.id.btnAddTask);
        btnSave = findViewById(R.id.btnSave);
        btnBack = findViewById(R.id.btnBack);
        lvTasks = findViewById(R.id.lvTasks);
        radioStudyMode = findViewById(R.id.radioStudyMode);
        rbtnPomodoro = findViewById(R.id.rbtnPomodoro);
        rbtnDeepStudy = findViewById(R.id.rbtnDeepStudy);
        rbtnQuickView = findViewById(R.id.rbtnQuickReview);

        sp = getSharedPreferences("FocusFlowPrefs", MODE_PRIVATE);

        mydb = openOrCreateDatabase("focusflowdb", MODE_PRIVATE, null);
        mydb.execSQL("CREATE TABLE IF NOT EXISTS tasks(title VARCHAR, subject VARCHAR, day INTEGER)");

        String[] subjects = {"Mobile Computing Infrastructure", "Programming I", "Programming II", "Rhetoric I", "Rhetoric II", "Calculus I", "Calculus II"};

        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, subjects);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subjectSpinner.setAdapter(adapter);

        subjectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l){
                String selectedSubject = subjects[position];
                Toast.makeText(StudyPlannerActivity.this, "Selected: " + selectedSubject, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent){
                Toast.makeText(StudyPlannerActivity.this, "Nothing Was Selected", Toast.LENGTH_SHORT).show();
            }
        });

        tasks = new ArrayList<String>();

        btnAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(etTask.getText().toString().isEmpty()) Toast.makeText(StudyPlannerActivity.this, "Cannot Have Empty Tasks", Toast.LENGTH_SHORT).show();

                else{
                    String task = etTask.getText().toString();
                    String subject = subjectSpinner.getSelectedItem().toString();
                    long today = System.currentTimeMillis() / 86400000L;
                    mydb.execSQL("INSERT INTO tasks VALUES ('" + task + "','" + subject + "'," + today + ")");
                    tasks.add(task);
                    tasksAdapter = new ArrayAdapter<String>(StudyPlannerActivity.this, android.R.layout.simple_list_item_1, tasks);
                    lvTasks.setAdapter(tasksAdapter);
                    etTask.setText("");

                    Toast.makeText(StudyPlannerActivity.this, "Task Added Successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                String selectedSubject = subjectSpinner.getSelectedItem().toString();

                int selectedId = radioStudyMode.getCheckedRadioButtonId();
                //checks which radio button study mode is clicked
                if (selectedId == -1) Toast.makeText(StudyPlannerActivity.this, "Please select a study mode", Toast.LENGTH_SHORT).show();
                else{
                    RadioButton selectedRadioButton = findViewById(selectedId);
                    String selectedMode = selectedRadioButton.getText().toString();
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("selectedSubject", selectedSubject);
                    editor.putString("studyMode", selectedMode);
                    editor.apply();
                    Toast.makeText(StudyPlannerActivity.this, "Study Plan Saved Successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(
                    systemBars.left,
                    systemBars.top,
                    systemBars.right,
                    systemBars.bottom
            );
            return insets;
        });
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        if (mydb != null) mydb.close();
    }
    //its used to close the database once the activity is done
}