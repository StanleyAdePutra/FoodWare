package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    ViewPager mViewPager;
    ImageView imghome, imgorder, imgprofile;
    TextView txthome, txtorder, txtprofile;
    TextView[] textnav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent i = getIntent();

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(new ViewPagerAdapter(
                getSupportFragmentManager()
        ));

        LinearLayout home = findViewById(R.id.btnhome);
        imghome = findViewById(R.id.imageView3);
        txthome = findViewById(R.id.textView3);
        LinearLayout order = findViewById(R.id.btnorder);
        imgorder = findViewById(R.id.imageView4);
        txtorder = findViewById(R.id.textView4);
        LinearLayout profile = findViewById(R.id.btnprofile);
        imgprofile = findViewById(R.id.imageView5);
        txtprofile = findViewById(R.id.textView5);
        textnav = new TextView[]{txthome, txtorder, txtprofile};

        StyleUpdater.changeImageColor(imghome, this);
        StyleUpdater.updateTextStyles(txthome, textnav, this);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(0);
                updateStyles(0);
            }
        });

        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(1);
                updateStyles(1);
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(2);
                updateStyles(2);
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Not needed for this example
            }

            @Override
            public void onPageSelected(int position) {
                updateStyles(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // Not needed for this example
            }
        });
    }
    private void updateStyles(int currentPosition) {
        switch (currentPosition) {
            case 0:
                StyleUpdater.changeImageColor(imghome, getApplicationContext());
                StyleUpdater.updateTextStyles(txthome, textnav, getApplicationContext());
                break;
            case 1:
                StyleUpdater.changeImageColor(imgorder, getApplicationContext());
                StyleUpdater.updateTextStyles(txtorder, textnav, getApplicationContext());
                break;
            case 2:
                StyleUpdater.changeImageColor(imgprofile, getApplicationContext());
                StyleUpdater.updateTextStyles(txtprofile, textnav, getApplicationContext());
                break;
        }
    }
}