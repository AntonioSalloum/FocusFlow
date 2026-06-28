package com.example.focusflow;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ResourcesActivity extends AppCompatActivity {

    Button btnExit, btnMath, btnJava, btnEnglish, btnCustomSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_resources);

        btnExit = findViewById(R.id.btnExit);
        btnMath = findViewById(R.id.btnMath);
        btnJava = findViewById(R.id.btnJava);
        btnEnglish = findViewById(R.id.btnEnglish);
        btnCustomSearch = findViewById(R.id.btnCustomSearch);

        btnExit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });

        btnMath.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(ResourcesActivity.this, WebActivity.class);
                i.putExtra("KEYWORD", "math study resources");
                startActivity(i);
            }
        });

        btnJava.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(ResourcesActivity.this, WebActivity.class);
                i.putExtra("KEYWORD", "java study resources");
                startActivity(i);
            }
        });

        btnEnglish.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(ResourcesActivity.this, WebActivity.class);
                i.putExtra("KEYWORD", "english study resources");
                startActivity(i);
            }
        });

        btnCustomSearch.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(ResourcesActivity.this, SearchActivity.class);
                startActivity(i);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}