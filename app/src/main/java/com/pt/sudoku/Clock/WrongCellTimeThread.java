package com.pt.sudoku.Clock;

import android.os.Handler;
import android.util.Log;

import com.pt.sudoku.Sudoku.BoardView;
import com.pt.sudoku.Sudoku.SudokuCell;

public class WrongCellTimeThread implements Runnable {
    private int lim_seconds = -1;
    private SudokuCell cell;
    private Handler timerHandler = null;
    private BoardView game;
    private Clock clock;

    public WrongCellTimeThread(Handler timerHandler, SudokuCell cell, int seconds_lim, BoardView game, Clock clock) {
        this.timerHandler = timerHandler;
        this.lim_seconds = seconds_lim;
        this.cell = cell;
        this.game = game;
        this.clock = clock;
    }

    @Override
    public void run() {
        clock.incSeconds();
        Log.i("WRONG", clock.getSeconds()+" :C");
        if (clock.getSeconds() >= lim_seconds) {
            cell.setValue(0);
            clock.resetSeconds();
            game.invalidate();
            timerHandler.removeCallbacks(this);
            return;
        }
        timerHandler.postDelayed(this, 1000);
    }
}
