package com.pt.sudoku.Models;

import com.pt.sudoku.Sudoku.BoardView;
import com.pt.sudoku.Sudoku.GameLogic;

import java.io.Serializable;

public class GameplayModel implements Serializable {
    private GameLogic logic;
    private int level;
    private int mode;
    private String language;

    public GameplayModel(GameLogic logic, int level, int mode, String language) {
        this.logic = logic;
        this.level = level;
        this.mode = mode;
        this.language = language;
    }

    public String getLanguage() {
        return language;
    }

    public GameLogic getLogic() {
        return logic;
    }

    public BoardView getView() {
        return logic.getView();
    }

    public int getLevel() {
        return level;
    }

    public int getMode() {
        return mode;
    }
}
