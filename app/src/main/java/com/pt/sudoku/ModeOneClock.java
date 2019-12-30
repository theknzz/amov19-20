package com.pt.sudoku;

import android.widget.TextView;
import android.os.Handler;

public class ModeOneClock implements Runnable {
    private int minutes=0;
    private int seconds=0;
    private TextView tvClock;
    private Handler timerHandler = null;

    public ModeOneClock(TextView tvClock, Handler timerHandler) {
        this.tvClock = tvClock;
        this.timerHandler = timerHandler;
    }


    @Override
    public void run() {
        seconds++;
        minutes = seconds / 60;
        seconds = seconds % 60;

        tvClock.setText(String.format("%d:%02d", minutes, seconds));
        timerHandler.postDelayed(this, 1000);
    }
}
