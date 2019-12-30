package com.pt.sudoku;

import android.os.Handler;
import android.widget.TextView;

public class SudokuClock {
    private Handler timeHandler = new Handler();
    private Runnable timerRunnable = null;

    public SudokuClock(TextView tvClock) {
        this.timerRunnable = new ModeOneClock(tvClock, timeHandler);
    }

    public SudokuClock(SudokuCell cell, GameBoard game) {
        this.timerRunnable = new WrongCellTimeThread(timeHandler, cell, 3, game);
    }

    public SudokuClock(TextView tvPlayerClock, TextView tvPlayer, PlayerManager manager) {
        this.timerRunnable = new ModeTwoClock(timeHandler, tvPlayerClock, tvPlayer, manager, 5);
    }

    public void startClock() {
        timeHandler.postDelayed(timerRunnable, 0);
    }

    public void resetPlayerClock(TextView tvPlayerClock, TextView tvPlayer, PlayerManager manager, int seconds) {
        timeHandler.removeCallbacks(timerRunnable);
        timerRunnable = new ModeTwoClock(timeHandler, tvPlayerClock, tvPlayer, manager, seconds);
        timeHandler.postDelayed(timerRunnable, 0);
    }

}
