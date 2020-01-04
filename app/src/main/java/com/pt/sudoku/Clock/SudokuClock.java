package com.pt.sudoku.Clock;

import android.os.Handler;
import android.widget.TextView;

import com.pt.sudoku.Sudoku.BoardView;
import com.pt.sudoku.GameRules;
import com.pt.sudoku.PlayerContents.PlayerManager;
import com.pt.sudoku.Sudoku.SudokuCell;

public class SudokuClock {
    private Handler timeHandler = new Handler();
    private Runnable timerRunnable = null;

    public SudokuClock(TextView tvClock, Clock clock) {
        this.timerRunnable = new ModeOneClock(tvClock, timeHandler, clock);
    }

    public SudokuClock(SudokuCell cell, BoardView game, Clock clock) {
        this.timerRunnable = new WrongCellTimeThread(timeHandler, cell, GameRules.TIME_WRONG_CELL_IS_ACTIVE, game, clock);
    }

    public SudokuClock(TextView tvPlayerClock, TextView tvPlayer, PlayerManager manager, Clock clock) {
        this.timerRunnable = new ModeTwoClock(timeHandler, tvPlayerClock, tvPlayer, manager, GameRules.ROUND_TIME, clock);
    }

    public SudokuClock(PlayerManager manager, Clock clock) {
        this.timerRunnable = new ModeThreeClock(timeHandler, manager, GameRules.ROUND_TIME, clock);
    }

    public void startClock() {
        timeHandler.postDelayed(timerRunnable, 0);
    }

    public void resetPlayerClock(TextView tvPlayerClock, TextView tvPlayer, PlayerManager manager, int seconds, Clock clock) {
        timeHandler.removeCallbacksAndMessages(timerRunnable);
        if(tvPlayerClock == null){
            timerRunnable = new ModeTwoClock(timeHandler, tvPlayerClock, tvPlayer, manager, seconds, clock);
        }else {
            timerRunnable = new ModeThreeClock(timeHandler, manager, seconds, clock);
        }
        timeHandler.postDelayed(timerRunnable, 0);
    }

    public void stopClock() {
        timeHandler.removeCallbacksAndMessages(timeHandler);
    }
}
