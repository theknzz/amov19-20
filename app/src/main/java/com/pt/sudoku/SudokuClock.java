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
        this.timerRunnable = new WrongCellTimeThread(timeHandler, cell, GameRules.TIME_WRONG_CELL_IS_ACTIVE, game);
    }

    public SudokuClock(TextView tvPlayerClock, TextView tvPlayer, PlayerManager manager) {
        this.timerRunnable = new ModeTwoClock(timeHandler, tvPlayerClock, tvPlayer, manager, GameRules.ROUND_TIME);
    }

    public void startClock() {
        timeHandler.postDelayed(timerRunnable, 0);
    }

    public void resetPlayerClock(TextView tvPlayerClock, TextView tvPlayer, PlayerManager manager, int seconds) {
        timeHandler.removeCallbacksAndMessages(timerRunnable);
        timerRunnable = new ModeTwoClock(timeHandler, tvPlayerClock, tvPlayer, manager, seconds);
        timeHandler.postDelayed(timerRunnable, 0);
    }

    public void stopClock() {
        timeHandler.removeCallbacksAndMessages(timeHandler);
    }
}
