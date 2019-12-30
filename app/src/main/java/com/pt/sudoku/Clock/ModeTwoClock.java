package com.pt.sudoku.Clock;

import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import com.pt.sudoku.PlayerContents.PlayerManager;

public class ModeTwoClock implements Runnable {
    private Clock clock;
    private TextView tvPlayerClock;
    private TextView tvPlayer;
    private PlayerManager manager;
    private int limSeconds;
    private Handler timerHandler = null;

    public ModeTwoClock(Handler timerHandler,TextView tvPlayerClock, TextView tvPlayer, PlayerManager manager, int seconds, Clock clock) {
        this.tvPlayerClock = tvPlayerClock;
        this.tvPlayer = tvPlayer;
        this.timerHandler = timerHandler;
        this.manager = manager;
        this.limSeconds = seconds;
        this.clock = clock;
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
            clock.incSeconds();
            Log.i("CLOCK", manager.getActualPlayer().getName() + " - time: " + clock.getSeconds() + " of " + limSeconds);

            if (clock.getSeconds() >= limSeconds) {
                Log.i("CLOCK", manager.getActualPlayer().getName() + " time limit reached");
                manager.switchPlayerTurn();
                clock.resetSeconds();
                timerHandler.removeCallbacksAndMessages(this);
                return;
            } else {
                tvPlayerClock.setText(String.format("PlayerTimer: %02d", clock.getSeconds()));
            }
            timerHandler.postDelayed(this, 1000);
        }
    }
}
