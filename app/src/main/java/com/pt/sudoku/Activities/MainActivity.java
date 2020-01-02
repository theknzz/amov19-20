package com.pt.sudoku.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;

import com.pt.sudoku.PlayerContents.PlayMenu;
import com.pt.sudoku.R;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // erro de network na thread
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }

    public void onPlay(View view) {
        Intent intent = new Intent(this, PlayMenu.class);
        startActivity(intent);
    }

    public void onHistory(View view) {
        Intent intent = new Intent(this, GameHistory.class);
        startActivity(intent);
    }

    public void onSettings(View view) {
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }

}
