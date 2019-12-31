package com.pt.sudoku.Sudoku;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SudokuCell implements Serializable {
    private int value;
    private boolean isModifiable;
    private List<Integer> notes = new ArrayList<>();

    public SudokuCell(int value, boolean isModifiable) {
        this.value = value;
        this.isModifiable = isModifiable;
    }

    public SudokuCell(int value) {
        this.value = value;
        this.isModifiable=true;
    }

    public void addNote(int nr) {
        if (notes.size()<10)
            notes.add(nr);
    }

    public boolean hasNotes() {
        return notes.size()!=0;
    }

    public int getValue() {
        return value;
    }

    public boolean setValue(int value) {
        notes.clear();
        if (isModifiable) {
            this.value = value;
            return true;
        }
        return false;
    }

    public List<Integer> getNotes() {
        return this.notes;
    }

    public void removeInvalidNotes(List<Integer> validNotes) {
        List<Integer> result = new ArrayList<>();
        for (Integer nr : validNotes)
            if (notes.contains(nr))
                result.add(nr);
        this.notes = result;
    }
}
