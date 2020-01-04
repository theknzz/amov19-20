package com.pt.sudoku.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.pt.sudoku.Models.GameplayModel;
import com.pt.sudoku.Sudoku.BoardView;
import com.pt.sudoku.R;
import com.pt.sudoku.Sudoku.GameLogic;

import java.io.Serializable;
import java.util.Locale;

public class GameplayActivity extends AppCompatActivity {

    private TextView tvMode, tvClock, tvPlayer, tvLevel, tvPlayerClock;
    private GameLogic logic;
    private int level, mode;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("model",saveGameSate(logic, level, mode));
    }

    private GameplayModel saveGameSate(GameLogic logic, int level, int mode) {
        return new GameplayModel(logic, level, mode);
    }

    private void loadGameState(Bundle bundle) {
        GameplayModel model = (GameplayModel) bundle.getSerializable("model");
        this.level = model.getLevel();
        this.mode = model.getMode();
        if (mode==1) {
            this.logic = new GameLogic(model.getLogic(), model.getView(), tvClock);
        }
        else if (mode == 2)
            this.logic = new GameLogic(model.getLogic(), model.getView(), tvClock, tvPlayerClock, tvPlayer);
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
        FrameLayout flSudokuTable = findViewById(R.id.flSudokuTable);
        level = getIntent().getIntExtra("level", 0);
        mode = getIntent().getIntExtra("mode", 0);

        if (savedInstanceState!=null) {
            loadGameState(savedInstanceState);
            updateInitialTextViews();
            flSudokuTable.addView(logic.getView());
        }
        else {
            updateInitialTextViews();
            initializeGameMode();
            flSudokuTable.addView(logic.getView());
        }
    }

    private void updateInitialTextViews() {
        tvMode.setText(getString(R.string.mode)+ mode);
        tvPlayer.setText(getString(R.string.player)+" A");
        if (level==6)
            tvLevel.setText(getString(R.string.level)+" "+getString(R.string.easy));
        else if (level==8)
            tvLevel.setText(getString(R.string.level)+" "+getString(R.string.medium));
        else if (level==10)
            tvLevel.setText(getString(R.string.level)+" "+getString(R.string.hard));
    }

    private void initializeGameMode() {
        if (mode==1)
            logic = new GameLogic(this, level, tvClock);
        else if (mode==2)
            logic = new GameLogic(this, level, tvClock, tvPlayerClock, tvPlayer);
    }

    public void onNumberPicker(View view) {
        String btnText = ((Button) view).getText().toString();
        logic.setSelectedNumber(Integer.parseInt(btnText));
    }

    public void onNotes(View view) {
        logic.switchNotesMode();
    }

    public void onChangeMode(View view) {
        logic.switchGameMode();
    }

    public void onCheat(View view) {
        logic.cheat();
    }

    public void gameFinished() {
        logic = null;
        Intent intent = new Intent(this, GameWonActivity.class);
        finish();
        startActivity(intent);
    }

}
