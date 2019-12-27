package com.pt.sudoku;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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
        Intent intent = new Intent(this, GameHistory.class);
        startActivity(intent);
    }

    public void onSettings(View view) {
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }
}
