package com.pt.sudoku;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SudokuBoard {
    private final int BOARD_SIZE = 9;
    private List<SudokuCell> board = new ArrayList<>();

    public SudokuCell get(int row, int col) {
        return board.get(row * BOARD_SIZE + col);
    }

    public SudokuBoard() {
    }

    public SudokuBoard(List<SudokuCell> list) {
        this.board = list;
    }

    public SudokuBoard(int[][] board) {
        for (int i=0; i < BOARD_SIZE; i++) {
            for (int j=0; j < BOARD_SIZE; j++) {
                add(i,j,board[i][j]);
            }
        }
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

    public boolean hasDoubledInSameRow(int row, int column, int number) {
        for (int c=0; c < BOARD_SIZE; c++){
            if (c!=column)
                if (getValueOf(row,c)==number) return true;
        }
        return false;
    }

    public boolean hasDoubledInSameBlock(int row, int col, int number) {
        int r = row - row % 3;
        int c = col - col % 3;

        for (int i = r; i < r + 3; i++)
            for (int j = c; j < c + 3; j++)
                if (getValueOf(i,j) == number && (i!=row && j!=col))
                    return true;
        return false;
    }

    public boolean hasDoubledInSameColumn(int row, int column, int number) {
        for (int r=0; r < BOARD_SIZE; r++)
        {
            if (r!=row)
                if (getValueOf(r,column)==number) return true;
        }
        return false;
    }

    public SudokuBoard getBoardClone() {
        List<SudokuCell> cloneList = new ArrayList<>();
        cloneList.addAll(board);
        return new SudokuBoard(cloneList);
    }

    public int[][] toPrimitiveBoard() {
        int[][] board = new int[BOARD_SIZE][BOARD_SIZE];
        for (int i =0 ; i <BOARD_SIZE; i++)
            for (int j =0; j < BOARD_SIZE; j++) {
                board[i][j] = getValueOf(i,j);
            }
        return board;
    }

    public void addNote(int cellX, int cellY, int selectedNumber) {
        get(cellX, cellY).addNote(selectedNumber);
    }

    public List<Integer> validNotes(int row, int column) {
        List<Integer> rList = validValuesInRow(row, column);
        List<Integer> cList = validValuesInColumn(row, column);
        List<Integer> bList = validValuesInBlock(row, column);
        return mergeLists(rList, cList, bList);
    }

    private List<Integer> mergeLists(List<Integer> rList, List<Integer> cList, List<Integer> bList) {
        List<Integer> list = new ArrayList<>();
        for (int i =1; i < BOARD_SIZE+1;i++){
            if (rList.contains(i) && cList.contains(i) && bList.contains(i))
                list.add(i);
        }
        return list;
    }

    private List<Integer> validValuesInRow(int row, int column) {
        List<Integer> list = new ArrayList<>();
        for (int i =1; i < BOARD_SIZE+1; i++){
            if (!hasDoubledInSameRow(row, column, i))
                list.add(i);
        }
        return list;
    }

    private List<Integer> validValuesInColumn(int row, int column) {
        List<Integer> list = new ArrayList<>();
        for (int i =1; i < BOARD_SIZE+1; i++){
            if (!hasDoubledInSameColumn(row, column, i))
                list.add(i);
        }
        return list;
    }

    private List<Integer> validValuesInBlock(int row, int column) {
        List<Integer> list = new ArrayList<>();
        for (int i =1; i < BOARD_SIZE+1; i++){
            if (!hasDoubledInSameBlock(row, column, i))
                list.add(i);
        }
        return list;
    }
}
