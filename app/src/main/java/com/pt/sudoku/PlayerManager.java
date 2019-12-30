package com.pt.sudoku;

import android.util.Log;
import android.widget.TextView;

public class PlayerManager {
    private Player playerA;
    private Player playerB;
    private TextView tvPlayer, tvPlayerClock;
    private SudokuClock playerClock;

    public PlayerManager(Player playerA, Player playerB, TextView tvPlayer, TextView tvPlayerClock) {
        this.playerA = playerA;
        this.playerB = playerB;
        this.tvPlayer = tvPlayer;
        this.tvPlayerClock = tvPlayerClock;
        playerClock = new SudokuClock(tvPlayerClock, tvPlayer, this);
    }

    public void switchPlayerTurn() {
        if (playerA.isPlaying())
        {
            playerA.setPlaying(false);
            playerB.setPlaying(true);
        }
        else
        {
            playerA.setPlaying(true);
            playerB.setPlaying(false);
        }
        tvPlayer.setText("Player: " + getActualPlayer().getName());
        playerClock.resetPlayerClock(tvPlayerClock, tvPlayer, this, 5);
    }

    public Player getActualPlayer() {
        return playerA.isPlaying()? playerA:playerB;
    }

    public void addWrongGuessToActualPlayer() {
        getActualPlayer().addWrongGuess();
    }

    public void addRightGuessToActualPlayer() {
        getActualPlayer().addRightGuess();
        playerClock.resetPlayerClock(tvPlayerClock, tvPlayer, this, 20);
    }

    public void triggerPlayerClock() {
        playerClock.startClock();
    }
}
