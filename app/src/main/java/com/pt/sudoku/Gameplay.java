package com.pt.sudoku;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;

public class Gameplay extends AppCompatActivity {

    private TextView tvMode;
    private GameBoard board;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameplay);
        tvMode = findViewById(R.id.tvMode);
        tvMode.setText("Modo " + getIntent().getIntExtra("mode", 0));

        FrameLayout flSudokuTable = findViewById(R.id.flSudokuTable);
        flSudokuTable.addView(new GameBoard(this));
    }




}
