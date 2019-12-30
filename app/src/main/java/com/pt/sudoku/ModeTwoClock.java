package com.pt.sudoku;

import android.os.Handler;
import android.widget.TextView;

public class ModeTwoClock implements Runnable {
    private TextView tvPlayerClock;
    private TextView tvPlayer;
    private PlayerManager manager;
    private int seconds = 0, limSeconds;
    private Handler timerHandler = null;

    public ModeTwoClock(Handler timerHandler,TextView tvPlayerClock, TextView tvPlayer, PlayerManager manager, int seconds) {
        this.tvPlayerClock = tvPlayerClock;
        this.tvPlayer = tvPlayer;
        this.timerHandler = timerHandler;
        this.manager = manager;
        this.limSeconds = seconds;
    }

    @Override
    public void run() {
        seconds++;
        seconds = seconds % 60;

        if (seconds>=limSeconds) {
            manager.switchPlayerTurn();
            seconds = 0;
            timerHandler.removeCallbacks(this);
        }
        tvPlayerClock.setText(String.format("PlayerTimer: %02d", seconds));
        timerHandler.postDelayed(this, 1000);
    }
}
