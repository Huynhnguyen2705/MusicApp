package com.learnandroid.huynh.music_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;


public class settingActivity extends AppCompatActivity {
    private android.support.v7.widget.Toolbar toolbar_Setting;
    private TextView languageTv;
    private TextView aboutTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        toolbar_Setting = findViewById(R.id.setting_toolbar);
        setSupportActionBar(toolbar_Setting);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        languageTv = findViewById(R.id.languageTv);
        languageTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_LanguageActivity = new Intent(settingActivity.this, LanguageActivity.class);
                startActivity(intent_LanguageActivity);
                overridePendingTransition(R.anim.anim_right, R.anim.anim_left);
            }
        });
        aboutTv = findViewById(R.id.aboutTv);
        aboutTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_AboutActivity = new Intent(settingActivity.this, AboutActivity.class);
                startActivity(intent_AboutActivity);
                overridePendingTransition(R.anim.anim_right, R.anim.anim_left);
            }
        });

    }
}
