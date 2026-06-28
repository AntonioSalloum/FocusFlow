package com.example.focusflow;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class SearchActivity extends AppCompatActivity {
    EditText etKeyword;
    Button btnSearch, btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search);
        etKeyword = findViewById(R.id.etKeyword);
        btnSearch = findViewById(R.id.btnSearch);
        btnBack = findViewById(R.id.btnBack);

        btnSearch.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String keyword = etKeyword.getText().toString();
                if(keyword.isEmpty()) Toast.makeText(SearchActivity.this, "Please enter a keyword", Toast.LENGTH_SHORT).show();

                else{
                    Intent i = new Intent(SearchActivity.this, WebActivity.class);
                    i.putExtra("KEYWORD", keyword);
                    startActivity(i);
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
            //closes the current activity and goes back to the previous activity
        });
    }
}