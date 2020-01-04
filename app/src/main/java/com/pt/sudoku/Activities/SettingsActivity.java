package com.pt.sudoku.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.pt.sudoku.R;

import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {

    public static String lang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState!=null)
        {
            changeDisplayLanguage((String)savedInstanceState.getSerializable("lang"));
        }
        setContentView(R.layout.activity_settings);
    }

    private void changeDisplayLanguage(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        this.lang = lang;
        Intent refresh = new Intent(this, MainActivity.class);
        finish();
        startActivity(refresh);
    }

    public void onLanguageChange(View view) {
        Button btn = (Button)view;
        String aux = btn.getText().toString();
        String lang = "en";
        if (aux.equals(getString(R.string.change_to_portuguese)))
            lang = "pt";
        changeDisplayLanguage(lang);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("lang", lang);
        super.onSaveInstanceState(outState);
    }
}
