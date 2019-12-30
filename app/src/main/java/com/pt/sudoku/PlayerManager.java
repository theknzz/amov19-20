package com.pt.sudoku;

import android.widget.TextView;

public class PlayerManager {
    private Player playerA;
    private Player playerB;
    private TextView tvPlayer, tvPlayerClock;
    private SudokuClock playerClock;
    private boolean isPlayerLocked;
    private boolean playerGotRightGuess;

    public PlayerManager(Player playerA, Player playerB, TextView tvPlayer, TextView tvPlayerClock) {
        this.playerA = playerA;
        this.playerB = playerB;
        this.tvPlayer = tvPlayer;
        this.tvPlayerClock = tvPlayerClock;
        playerClock = new SudokuClock(tvPlayerClock, tvPlayer, this);
    }

    public boolean isPlayerLocked() {
        return isPlayerLocked;
    }

    public void switchPlayerTurn() {
        if (isPlayerLocked) return;
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
        playerClock.resetPlayerClock(tvPlayerClock, tvPlayer, this, GameRules.ROUND_TIME);
    }

    public Player getActualPlayer() {
        return playerA.isPlaying()? playerA:playerB;
    }

    public boolean isPlayerGotRightGuess() {
        return playerGotRightGuess;
    }

    public void switchPlayerGotRightGuess() {
        playerGotRightGuess = false;
        playerClock.resetPlayerClock(tvPlayerClock, tvPlayer, this, GameRules.EXTRA_TIME_FOR_RIGHT_GUESS);
    }

    public void addWrongGuessToActualPlayer() {
        getActualPlayer().addWrongGuess();
    }

    public void addRightGuessToActualPlayer() {
        getActualPlayer().addRightGuess();
        playerGotRightGuess = true;
    }

    public void triggerPlayerClock() {
        playerClock.startClock();
    }

    public void lockPlayer(TextView tvClock) {
        isPlayerLocked = true;
        tvPlayerClock.setText("");
        playerClock.stopClock();
    }

    public Player getWinner() {
        return playerA.getRightGuesses()>playerB.getRightGuesses()?playerA:playerB;
    }
}
