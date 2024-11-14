package com.example.myapplication;

import static java.security.AccessController.getContext;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class landing extends AppCompatActivity {

    TextView login, register;
    TextView[] txtlanding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        login = (TextView)findViewById(R.id.login);
        register = (TextView)findViewById(R.id.register);
        txtlanding = new TextView[]{login, register};

        StyleUpdater.updateTextStyles(login, txtlanding, this);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.framelogin, new frag_login())
                .commit();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StyleUpdater.updateTextStyles(login, txtlanding, view.getContext());

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.framelogin, new frag_login())
                        .commit();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StyleUpdater.updateTextStyles(register, txtlanding, v.getContext());

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.framelogin, new frag_register())
                        .commit();
            }
        });
    }

    public void switchToLoginFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        frag_login loginFragment = new frag_login();
        int containerId = R.id.framelogin;
        fragmentTransaction.replace(containerId, loginFragment);

        StyleUpdater.updateTextStyles(login, txtlanding, getApplicationContext());

        fragmentTransaction.commit();
    }
}