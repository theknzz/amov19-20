package com.pt.sudoku;

import java.util.ArrayList;
import java.util.List;

public class SudokuBoard {
    private final int BOARD_SIZE = 9;
    private List<SudokuCell> board = new ArrayList<>();

    public SudokuCell get(int row, int col) {
        return board.get(row * BOARD_SIZE + col);
    }

    public boolean add(int row, int col, int value) {
        if (!isValidInsert(col,row,value)) return false;
        return board.add(new SudokuCell(value));
    }

    public boolean addToInitialBoard(int row, int col, int value) {
        if (!isValidInsert(col,row,value))return false;
        if (value == 0)
            return board.add(new SudokuCell(value));
        else
            return board.add(new SudokuCell(value, false));
    }

    private boolean isValidInsert(int row, int col, int value) {
        return col < 10 && row < 10 && value < 10 && col > -1 && row > -1 && value > -1;
    }

    public int getValueOf(int row, int col) {
        return get(row,col).getValue();
    }

    public boolean setValue(int row, int col, int value) {
        if (!isValidInsert(row, col, value)) return false;
        return get(row,col).setValue(value);
    }
}
