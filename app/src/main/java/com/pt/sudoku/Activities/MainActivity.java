package com.pt.sudoku.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import com.pt.sudoku.PlayerContents.PlayMenu;
import com.pt.sudoku.R;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onPlay(View view) {
        Intent intent = new Intent(this, PlayMenu.class);
        startActivity(intent);
    }

    public void onHistory(View view) {
        Intent intent = new Intent(this, GameHistoryActivity.class);
        startActivity(intent);
    }

    public void onSettings(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}
