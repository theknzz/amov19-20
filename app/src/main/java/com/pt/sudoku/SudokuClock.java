package com.pt.sudoku;

import android.os.Handler;
import android.widget.TextView;

public class SudokuClock {
    private Handler timeHandler = new Handler();
    private Runnable timerRunnable = null;

    public SudokuClock(TextView tvClock) {
        this.timerRunnable = new TimerThread(tvClock, timeHandler);
    }

    public SudokuClock(SudokuCell cell, GameBoard game) {
        this.timerRunnable = new WrongCellTimeThread(timeHandler, cell, 3, game);
    }

    public void startClock() {
        timeHandler.postDelayed(timerRunnable, 0);
    }

}
