package com.pt.sudoku.History;

public class HistoryModel {
    String mode, difficulty, result, winner;

    public HistoryModel(String mode, String difficulty, String result, String winner){
        this.mode = mode;
        this.difficulty = difficulty;
        this.result = result;
        this.winner = winner;
    }

    public String getMode() {
        return mode;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getResult() {
        return result;
    }

    public String getWinner() {
        return winner;
    }
}
