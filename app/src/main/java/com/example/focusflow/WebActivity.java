package com.example.focusflow;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class WebActivity extends AppCompatActivity {
    Button btnForward, btnBackward, btnReload, btnExit;
    WebView webv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_web);

        btnForward = findViewById(R.id.btnForward);
        btnBackward = findViewById(R.id.btnBackward);
        btnReload = findViewById(R.id.btnReload);
        btnExit = findViewById(R.id.btnExit);
        webv = findViewById(R.id.webView);

        WebSettings webSettings = webv.getSettings();  //opens the browser settings so we can configure them
        webSettings.setJavaScriptEnabled(true); //to allow websites to run Javascript

        webv.setWebViewClient(new WebViewClient()); //to keep all webview pages inside the same browser and not another browser

        Intent i = getIntent();

        webv.loadUrl("https://www.youtube.com/results?search_query=" + i.getStringExtra("KEYWORD"));

        btnBackward.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(webv.canGoBack())
                    webv.goBack();
            }
        });

        btnForward.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(webv.canGoForward())
                    webv.goForward();
            }
        });

        btnReload.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                webv.reload();
            }
        });

        btnExit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });
    }
}