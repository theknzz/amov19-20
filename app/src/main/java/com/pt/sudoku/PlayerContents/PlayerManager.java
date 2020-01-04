package com.pt.sudoku.PlayerContents;

import android.widget.TextView;

import com.pt.sudoku.Clock.Clock;
import com.pt.sudoku.Clock.SudokuClock;
import com.pt.sudoku.GameRules;

public class PlayerManager {
    private Player playerA;
    private Player playerB;
    private Player playerC;
    private TextView tvPlayer, tvPlayerClock;
    private SudokuClock playerClock;
    private boolean isPlayerLocked;
    private boolean playerGotRightGuess;

    public PlayerManager(Player playerA, Player playerB, TextView tvPlayer, TextView tvPlayerClock, Clock clock) {
        this.playerA = playerA;
        this.playerB = playerB;
        this.playerC = null;
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

    public PlayerManager(Player playerA, Player playerB, Player playerC, Clock clock) {
        this.playerA = playerA;
        this.playerB = playerB;
        this.playerC = playerC;
        playerClock = new SudokuClock(this, clock);
    }

    private Player getPlayerC() {
        return this.playerC;
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
        else if(playerB.isPlaying())
        {
            playerB.setPlaying(false);
            if(playerC == null){
                playerA.setPlaying(true);
            }else{
                playerC.setPlaying(true);
            }
        }else if(playerC!=null && playerC.isPlaying()){
            playerC.setPlaying(false);
            playerA.setPlaying(true);
        }
        if(tvPlayer!=null) {
            tvPlayer.setText("Player: " + getActualPlayer().getName());
        }
        playerClock.resetPlayerClock(tvPlayerClock, tvPlayer, this, GameRules.ROUND_TIME, new Clock());

    }

    public Player getActualPlayer() {
        if(playerA.isPlaying())
            return playerA;
        if (playerB.isPlaying())
            return playerB;
        return playerC;
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
        if(playerC==null)
            return playerA.getRightGuesses()>playerB.getRightGuesses() ? playerA:playerB;
        Player max;
        max = playerA.getRightGuesses() > playerB.getRightGuesses()? playerA:playerB;
        return max.getRightGuesses() > playerC.getRightGuesses()?max:playerC;
    }


}
