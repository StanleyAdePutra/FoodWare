package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

public class webView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        ImageView back = findViewById(R.id.btnBackWeb);

        WebView webview = findViewById(R.id.webView1);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setWebViewClient(new WebViewClient());
        webview.loadUrl("https://www.google.com/maps/place/Multimedia+Nusantara+University/@-6.2558858,106.617259,18.75z/data=!4m6!3m5!1s0x2e69fb56b25975f9:0x50c7d605ba8542f5!8m2!3d-6.2562238!4d106.6185624!16s%2Fm%2F0cc66sf?entry=ttu");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}