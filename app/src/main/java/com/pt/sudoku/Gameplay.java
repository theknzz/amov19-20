package com.pt.sudoku;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

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
        board = new GameBoard(this, getIntent().getIntExtra("level", 0));
        flSudokuTable.addView(board);
    }

    public void onNumberPicker(View view) {
        String btnText = ((Button) view).getText().toString();
        board.setSelectedNumber(Integer.parseInt(btnText));
    }
}
