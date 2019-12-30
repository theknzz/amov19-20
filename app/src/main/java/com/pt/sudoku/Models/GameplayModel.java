package com.pt.sudoku.Models;

import com.pt.sudoku.Sudoku.BoardView;

import java.io.Serializable;

public class GameplayModel implements Serializable {
    private BoardView board;
    private int level;
    private int mode;

    public GameplayModel(BoardView board, int level, int mode) {
        this.board = board;
        this.level = level;
        this.mode = mode;
    }

    public BoardView getBoard() {
        return board;
    }

    public int getLevel() {
        return level;
    }

    public int getMode() {
        return mode;
    }
}
