package com.pt.sudoku;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

public class Gameplay extends AppCompatActivity {

    private TextView tvMode, tvClock, tvPlayer, tvLevel, tvPlayerClock;
    private GameBoard board;
    private int level, mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameplay);

        tvClock = findViewById(R.id.tvClock);
        tvPlayer = findViewById(R.id.tvPlayer);
        tvMode = findViewById(R.id.tvMode);
        tvLevel = findViewById(R.id.tvLevel);
        tvPlayerClock = findViewById(R.id.tvPlayerTimer);
        level = getIntent().getIntExtra("level", 0);
        mode = getIntent().getIntExtra("mode", 0);

        updateInitialTextViews();
        initializeGameMode();

        FrameLayout flSudokuTable = findViewById(R.id.flSudokuTable);
        flSudokuTable.addView(board);
    }

    private void updateInitialTextViews() {
        tvMode.setText("Mode: " + mode);
        tvPlayer.setText("Player: A");
        if (level==6)
            tvLevel.setText("Level: Easy");
        else if (level==8)
            tvLevel.setText("Level: Medium");
        else if (level==10)
            tvLevel.setText("Level: Hard");
    }

    private void initializeGameMode() {
        if (mode==1)
            board = new GameBoard(this, level, tvClock);
        else if (mode==2)
            board = new GameBoard(this, level, tvClock, tvPlayerClock, tvPlayer);
    }

    public void onNumberPicker(View view) {
        String btnText = ((Button) view).getText().toString();
        board.setSelectedNumber(Integer.parseInt(btnText));
    }

    public void onNotes(View view) {
        board.switchNotesMode();
    }
}
