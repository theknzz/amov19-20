package com.pt.sudoku.Clock;

import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import com.pt.sudoku.PlayerContents.PlayerManager;

public class ModeThreeClock implements Runnable{
    private Clock clock;
    private PlayerManager manager;
    private int limSeconds;
    private Handler timerHandler = null;


    public ModeThreeClock(Handler timeHandler, PlayerManager manager, int seconds, Clock clock) {
        this.timerHandler = timeHandler;
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
            }
            timerHandler.postDelayed(this, 1000);
        }
    }
}
