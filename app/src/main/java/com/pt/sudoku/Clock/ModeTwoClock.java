package com.pt.sudoku.Clock;

import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import com.pt.sudoku.PlayerContents.PlayerManager;
import com.pt.sudoku.Sudoku.GameLogic;

public class ModeTwoClock implements Runnable {
    private Clock clock;
    private TextView tvPlayerClock;
    private TextView tvPlayer;
    private PlayerManager manager;
    private int limSeconds;
    private Handler timerHandler = null;
    private GameLogic logic;

    public ModeTwoClock(Handler timerHandler, TextView tvPlayerClock, TextView tvPlayer, PlayerManager manager, int seconds, Clock clock, GameLogic logic) {
        this.tvPlayerClock = tvPlayerClock;
        this.tvPlayer = tvPlayer;
        this.timerHandler = timerHandler;
        this.manager = manager;
        this.limSeconds = seconds;
        this.clock = clock;
        this.logic = logic;
    }

    @Override
    public void run() {
        if (logic.isFinished()) {
            logic.launchWonActivity();
            timerHandler.removeCallbacksAndMessages(this);
            return;
        }
        else if (manager.isPlayerGotRightGuess()) {
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
