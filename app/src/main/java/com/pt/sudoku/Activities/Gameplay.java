package com.pt.sudoku.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.pt.sudoku.Models.GameplayModel;
import com.pt.sudoku.Sudoku.BoardView;
import com.pt.sudoku.R;

public class Gameplay extends AppCompatActivity {

    private TextView tvMode, tvClock, tvPlayer, tvLevel, tvPlayerClock, tvWinOutput;
    private BoardView board;
    private int level, mode;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("model",saveGameSate(board, level, mode));
    }

    private GameplayModel saveGameSate(BoardView board, int level, int mode) {
        return new GameplayModel(board, level, mode);
    }

    private void loadGameState(Bundle bundle) {
        GameplayModel model = (GameplayModel) bundle.getSerializable("model");
        this.level = model.getLevel();
        this.mode = model.getMode();
        if (mode==1)
            this.board = new BoardView(model.getBoard(), this, tvClock, tvWinOutput);
        else if (mode == 2)
            this.board = new BoardView(model.getBoard(), this, tvClock, tvPlayerClock, tvPlayer, tvWinOutput);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameplay);

        tvClock = findViewById(R.id.tvClock);
        tvPlayer = findViewById(R.id.tvPlayer);
        tvMode = findViewById(R.id.tvMode);
        tvLevel = findViewById(R.id.tvLevel);
        tvPlayerClock = findViewById(R.id.tvPlayerTimer);
        tvWinOutput = findViewById(R.id.tvWinOutput);
        FrameLayout flSudokuTable = findViewById(R.id.flSudokuTable);
        level = getIntent().getIntExtra("level", 0);
        mode = getIntent().getIntExtra("mode", 0);

        if (savedInstanceState!=null) {
            loadGameState(savedInstanceState);
            updateInitialTextViews();
            flSudokuTable.addView(board);
        }
        else {
            updateInitialTextViews();
            initializeGameMode();
            flSudokuTable.addView(board);
        }
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
            board = new BoardView(this, level, tvClock, tvWinOutput);
        else if (mode==2)
            board = new BoardView(this, level, tvClock, tvPlayerClock, tvPlayer, tvWinOutput);
    }

    public void onNumberPicker(View view) {
        String btnText = ((Button) view).getText().toString();
        board.setSelectedNumber(Integer.parseInt(btnText));
    }

    public void onNotes(View view) {
        board.switchNotesMode();
    }


    public void onChangeMode(View view) {
        board.switchGameMode();
    }

    public void onCheat(View view) {
        board.cheat();
    }

}
