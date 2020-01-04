package com.pt.sudoku.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.pt.sudoku.R;

public class GameWonActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_won);
    }

    public void onPlayAgain(View view) {
        Intent intent = new Intent(this, PlayMenuActivity.class);
        finish();
        startActivity(intent);
    }

    public void onCancel(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        finish();
        startActivity(intent);
    }
}
