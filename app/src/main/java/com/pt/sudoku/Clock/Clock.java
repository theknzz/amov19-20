package com.pt.sudoku.Clock;

public class Clock {
    private int minutes = 0;
    private int seconds = 0;

    public Clock(int minutes, int seconds) {
        this.minutes = minutes;
        this.seconds = seconds;
    }

    public Clock(int seconds) {
        this.seconds = seconds;
    }

    public Clock() {
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public void incSeconds() {
        this.seconds++;
    }

    public void resetSeconds() {
        this.seconds = 0;
    }

    public String getAsString(){
        if(seconds<10)
            return minutes + ":0" + seconds;
        return minutes + ":" + seconds;
    }
}
