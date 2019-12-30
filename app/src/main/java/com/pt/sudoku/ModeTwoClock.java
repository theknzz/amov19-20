package com.pt.sudoku;

import android.os.Handler;
import android.util.Log;
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
        if (manager.isPlayerGotRightGuess()) {
            Log.i("CLOCK", "deu porra");
            timerHandler.removeCallbacksAndMessages(this);
            manager.switchPlayerGotRightGuess();
            return;
        }
        else if (manager.isPlayerLocked()) timerHandler.removeCallbacksAndMessages(this);
        else {
            seconds++;
            seconds = seconds % 60;
            Log.i("CLOCK", manager.getActualPlayer().getName() + " - time: " + seconds + " of " + limSeconds);

            if (seconds >= limSeconds) {
                Log.i("CLOCK", manager.getActualPlayer().getName() + " time limit reached");
                manager.switchPlayerTurn();
                seconds = 0;
                timerHandler.removeCallbacksAndMessages(this);
                return;
            } else {
                tvPlayerClock.setText(String.format("PlayerTimer: %02d", seconds));
            }
            timerHandler.postDelayed(this, 1000);
        }
    }
}
