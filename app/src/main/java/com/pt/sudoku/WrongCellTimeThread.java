package com.pt.sudoku;

import android.os.Handler;
import android.util.Log;

public class WrongCellTimeThread implements Runnable {
    private int seconds=0;
    private int lim_seconds = -1;
    private SudokuCell cell;
    private Handler timerHandler = null;
    private GameBoard game;

    public WrongCellTimeThread(Handler timerHandler, SudokuCell cell, int seconds_lim, GameBoard game) {
        this.timerHandler = timerHandler;
        this.lim_seconds = seconds_lim;
        this.cell = cell;
        this.game = game;
    }

    @Override
    public void run() {
        seconds++;
        seconds = seconds % 60;

        if (seconds >= lim_seconds) {
            cell.setValue(0);
            game.invalidate();
            timerHandler.removeCallbacks(this);
        }
        timerHandler.postDelayed(this, 1000);
    }
}
