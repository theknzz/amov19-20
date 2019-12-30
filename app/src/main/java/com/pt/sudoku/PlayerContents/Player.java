package com.pt.sudoku.PlayerContents;

public class Player {
    private String name;
    private boolean isPlaying=false;
    private int wrongGuesses=0;
    private int rightGuesses=0;

    public Player(String name) {
        this.name = name;
    }

    public Player(String name, boolean isPlaying) {
        this.name = name;
        this.isPlaying = isPlaying;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean b) {
        isPlaying=b;
    }

    public void addWrongGuess() {
        wrongGuesses++;
    }

    public void addRightGuess() {
        rightGuesses++;
    }

    public String getName() {
        return name;
    }

    public int getRightGuesses() {
        return rightGuesses;
    }
}
