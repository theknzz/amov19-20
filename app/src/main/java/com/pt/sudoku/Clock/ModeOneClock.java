package com.pt.sudoku.Clock;

import android.util.Log;
import android.widget.TextView;
import android.os.Handler;

public class ModeOneClock implements Runnable {
    private int toShowSeconds=0;
    private TextView tvClock;
    private Clock clock;
    private Handler timerHandler = null;

    public ModeOneClock(TextView tvClock, Handler timerHandler, Clock clock) {
        this.tvClock = tvClock;
        this.timerHandler = timerHandler;
        this.clock = clock;
    }

    @Override
    public void run() {
        clock.incSeconds();
        clock.setMinutes(clock.getSeconds() / 60);
        toShowSeconds = clock.getSeconds() % 60;
        tvClock.setText(String.format("%d:%02d", clock.getMinutes(), toShowSeconds));
        Log.i("CLOCK", String.format("%d:%02d", clock.getMinutes(), toShowSeconds));
        timerHandler.postDelayed(this, 1000);
    }
}
