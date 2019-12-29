package com.pt.sudoku;

public class SudokuCell {
    private int value;
    private boolean isModifiable;

    public SudokuCell(int value, boolean isModifiable) {
        this.value = value;
        this.isModifiable = isModifiable;
    }

    public SudokuCell(int value) {
        this.value = value;
        this.isModifiable=true;
    }

    public int getValue() {
        return value;
    }

    public boolean setValue(int value) {
        if (isModifiable) {
            this.value = value;
            return true;
        }
        return false;
    }
}
