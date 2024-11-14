package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;

public class splashScreen extends AppCompatActivity {

    private static final int SPLASH_DURATION = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                LoginManager loginManager = new LoginManager(splashScreen.this);
                loginManager.open();

                boolean isLoggedIn = loginManager.isLoggedIn();
                String userEmail = loginManager.getLoggedInUserEmail();

                loginManager.close();

                if (isLoggedIn) {
                    saveEmailToSharedPreferences(userEmail);
                    Intent intent = new Intent(splashScreen.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(splashScreen.this, landing.class);
                    startActivity(intent);
                }

                finish();
            }
        }, SPLASH_DURATION);
    }

    private void saveEmailToSharedPreferences(String userEmail) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(splashScreen.this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user_email", userEmail);
        editor.apply();

        Log.d("SharedPreferences", "user_email saved: " + userEmail);
    }
}