package com.learnandroid.huynh.music_app;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Locale;

public class LanguageActivity extends AppCompatActivity {
    Spinner spinnerctrl;
    Locale myLocale;
    private RadioGroup languageRadioG;
    private RadioButton tiengvietRadio;
    private RadioButton englistRadio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        languageRadioG = findViewById(R.id.languageRadioG);
        tiengvietRadio = languageRadioG.findViewById(languageRadioG.getCheckedRadioButtonId());
        englistRadio = languageRadioG.findViewById(languageRadioG.getCheckedRadioButtonId());
        languageRadioG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                tiengvietRadio = group.findViewById(R.id.tiengvietRadio);
                boolean isCheckedTiengViet = tiengvietRadio.isChecked();
                if (isCheckedTiengViet) {
                    Toast.makeText(LanguageActivity.this, "Tiếng Việt", Toast.LENGTH_SHORT).show();
                    setLocale("vi");
                    tiengvietRadio.setChecked(true);
                } else {
                    Toast.makeText(LanguageActivity.this, "English", Toast.LENGTH_SHORT).show();
                    setLocale("en");
                }
            }
        });
//        spinnerctrl = (Spinner) findViewById(R.id.spinner1);
//        spinnerctrl.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
//                if (pos == 1) {
//                    Toast.makeText(parent.getContext(), "You have selected English", Toast.LENGTH_SHORT).show();
//                    setLocale("en");
//                } else if (pos == 2) {
//                    Toast.makeText(parent.getContext(),
//                            "You have selected Tieng Viet", Toast.LENGTH_SHORT)
//                            .show();
//                    setLocale("vi");
//                }
//            }
//
//            public void onNothingSelected(AdapterView<?> arg0) {
//                // TODO Auto-generated method stub
//            }
//
//
//        });
    }

    public void setLocale(String lang) {
        myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(this, settingActivity.class);
        startActivity(refresh);
    }
}
