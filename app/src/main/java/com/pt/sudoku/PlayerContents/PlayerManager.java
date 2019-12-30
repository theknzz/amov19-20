package com.pt.sudoku.PlayerContents;

import android.widget.TextView;

import com.pt.sudoku.Clock.Clock;
import com.pt.sudoku.Clock.SudokuClock;
import com.pt.sudoku.GameRules;

public class PlayerManager {
    private Player playerA;
    private Player playerB;
    private TextView tvPlayer, tvPlayerClock;
    private SudokuClock playerClock;
    private boolean isPlayerLocked;
    private boolean playerGotRightGuess;

    public PlayerManager(Player playerA, Player playerB, TextView tvPlayer, TextView tvPlayerClock, Clock clock) {
        this.playerA = playerA;
        this.playerB = playerB;
        this.tvPlayer = tvPlayer;
        this.tvPlayerClock = tvPlayerClock;
        playerClock = new SudokuClock(tvPlayerClock, tvPlayer, this, clock);
    }

    public PlayerManager(PlayerManager manager, TextView tvPlayer, TextView tvPlayerClock, Clock clock) {
        this.playerA = manager.getPlayerA();
        this.playerB = manager.getPlayerB();
        this.tvPlayer = tvPlayer;
        this.tvPlayerClock = tvPlayerClock;
        this.playerClock = new SudokuClock(tvPlayerClock, tvPlayer, this, clock);
        this.isPlayerLocked = manager.isPlayerLocked();
        this.playerGotRightGuess = manager.isPlayerGotRightGuess();
    }

    private Player getPlayerB() {
        return this.playerB;
    }

    private Player getPlayerA() {
        return this.playerA;
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
        playerClock.resetPlayerClock(tvPlayerClock, tvPlayer, this, GameRules.ROUND_TIME, new Clock());
    }

    public Player getActualPlayer() {
        return playerA.isPlaying()? playerA:playerB;
    }

    public boolean isPlayerGotRightGuess() {
        return playerGotRightGuess;
    }

    public void switchPlayerGotRightGuess() {
        playerGotRightGuess = false;
        playerClock.resetPlayerClock(tvPlayerClock, tvPlayer, this, GameRules.EXTRA_TIME_FOR_RIGHT_GUESS, new Clock());
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

    public void lockPlayer() {
        isPlayerLocked = true;
        tvPlayerClock.setText("");
        playerClock.stopClock();
    }

    public Player getWinner() {
        return playerA.getRightGuesses()>playerB.getRightGuesses()?playerA:playerB;
    }
}
