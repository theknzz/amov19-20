package com.pt.sudoku.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.pt.sudoku.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onPlay(View view) {
        Intent intent = new Intent(this, PlayMenuActivity.class);
        startActivity(intent);
    }

    public void onHistory(View view) {
        Intent intent = new Intent(this, GameHistoryActivity.class);
        startActivity(intent);
    }

    public void onSettings(View view) {
        Intent intent = new Intent(this, CreditsActivity.class);
        startActivity(intent);
    }
}
