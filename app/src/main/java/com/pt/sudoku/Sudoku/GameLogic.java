package com.pt.sudoku.Sudoku;

import android.content.Context;
import android.widget.TextView;
import com.pt.sudoku.Activities.GameplayActivity;
import com.pt.sudoku.Clock.Clock;
import com.pt.sudoku.Clock.SudokuClock;
import com.pt.sudoku.PlayerContents.Player;
import com.pt.sudoku.PlayerContents.PlayerManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.Serializable;
import java.util.List;
import pt.isec.ans.sudokulibrary.Sudoku;

public class GameLogic implements Serializable {

    public static final int BOARD_SIZE = 9;
    private int level;
    private BoardView view;

    private SudokuBoard board = new SudokuBoard();
    private SudokuBoard gameSolution = new SudokuBoard();

    private SudokuClock clock = null;

    private PlayerManager playerManager;

    private Clock globalClock, personalClock, wrongClock;

    private GameplayActivity context;

    public GameLogic(GameLogic logic, BoardView view, TextView tvClock) {
        this.globalClock = logic.getGlobalClock();
        this.personalClock = logic.getPersonalClock();
        this.wrongClock = logic.getWrongClock();
        this.clock = new SudokuClock(tvClock, globalClock, this);
        this.board = logic.getBoard();
        this.level = logic.getLevel();
        this.context = (GameplayActivity) view.getContext();
        this.gameSolution = logic.getGameSolution();
        this.playerManager = logic.getPlayerManager();
        this.view = new BoardView(view, this, view.getContext(), clock);
    }

    public GameLogic(GameLogic logic, BoardView view, TextView tvClock, TextView tvPlayerClock, TextView tvPlayer) {
        this.globalClock = logic.getGlobalClock();
        this.personalClock = logic.getPersonalClock();
        this.wrongClock = logic.getWrongClock();
        this.context = (GameplayActivity) view.getContext();
        this.clock = new SudokuClock(tvClock, globalClock, this);
        this.board = logic.getBoard();
        this.level = logic.getLevel();
        this.gameSolution = logic.getGameSolution();
        this.playerManager = logic.getPlayerManager();
        this.view = new BoardView(view, this, view.getContext(), clock, playerManager);
    }

    public GameLogic(Context context, int level, TextView tvClock) {
        this.level = level;
        this.globalClock = new Clock();
        this.personalClock = new Clock();
        this.wrongClock = new Clock();
        this.context = (GameplayActivity) context;
        this.clock = new SudokuClock(tvClock, globalClock, this);
        initializeGame(level);
        resolveGame(board.toPrimitiveBoard());
        this.view = new BoardView(context, clock, this);
    }

    public GameLogic(Context context, int level, TextView tvClock, TextView tvPlayerClock, TextView tvPlayer) {
        this.level = level;
        this.globalClock = new Clock();
        this.personalClock = new Clock();
        this.wrongClock = new Clock();
        this.context = (GameplayActivity) context;
        this.clock = new SudokuClock(tvClock, globalClock, this);
        this.playerManager = new PlayerManager(new Player("A", true), new Player("B"),tvPlayer, tvPlayerClock, personalClock, this);
        initializeGame(level);
        resolveGame(board.toPrimitiveBoard());
        this.view = new BoardView(context, clock, playerManager, this);
    }

    public BoardView getView() {
        return this.view;
    }

    public void setSelectedNumber(int nr) {
        view.setSelectedNumber(nr);
    }

    public void switchNotesMode() {
        view.switchNotesMode();
    }

    public void switchGameMode() {
        playerManager.lockPlayer();
        view.switchGameMode();
    }

    public void cheat() {
        board.setBoard(gameSolution.getBoard());
        view.invalidate();
    }


    private void createBoard(int [][] b) {
        for (int i=0; i < BOARD_SIZE; i++)
            for (int j=0; j <BOARD_SIZE; j++) {
                board.addToInitialBoard(i,j, b[i][j]);
            }
    }

    private void initializeGame(int level) {
        String strJson = Sudoku.generate(level);
        try {
            JSONObject json = new JSONObject(strJson);
            if (json.optInt("result",0) == 1) {
                JSONArray jsonArray = json.getJSONArray("board");
                int[][] board = convert(jsonArray);
                createBoard(board);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private int[][] convert(JSONArray jsonArray) {
        int[][] array = null;
        try {
            int rows = jsonArray.length(), columns = 0;
            for (int r=0; r < rows; r++) {
                JSONArray jsonRow = jsonArray.getJSONArray(r);
                if (r==0) {
                    columns = jsonRow.length();
                    array = new int[rows][columns];
                }
                for (int c=0; c<columns; c++) {
                    array[r][c] = jsonRow.getInt(c);
                }
            }
        } catch (Exception e) {
            array = null;
        }
        return array;
    }

    private void resolveGame(int [][] board) {
        try {
            JSONObject json = new JSONObject();
            JSONArray jsonArray = convert(board);
            json.put("board", jsonArray);
            String strJson = Sudoku.solve(json.toString(), 1000*2);

            try {
                json = new JSONObject(strJson);
                if (json.optInt("result",0) == 1) {
                    jsonArray = json.getJSONArray("board");
                    int[][] b = convert(jsonArray);
                    gameSolution = new SudokuBoard(b);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
        }
    }

    private JSONArray convert(int[][] board) {
        JSONArray jsonArray = new JSONArray();
        int rows = board.length,columns = 0;
        try {
            for (int r = 0; r < rows; r++) {
                JSONArray jsonRow = new JSONArray();
                columns= board[r].length;
                for (int c=0; c < columns; c++) {
                    jsonRow.put(board[r][c]);
                }
                jsonArray.put(jsonRow);
            }
        } catch (Exception e) {

        }
        return jsonArray;
    }

    public SudokuBoard getBoard() {
        return board;
    }

    public SudokuCell getCell(int r, int c) {
        return board.get(r,c);
    }

    public Player getWinner() {
        return playerManager.getWinner();
    }

    public boolean isFinished() {
        return board.equals(gameSolution);
    }

    public boolean setValueToCell(int row, int col, int number) {
        return board.setValue(row, col, number);
    }

    public boolean setValueToCellModeTwo(int row, int col, int number) {
        return board.setValue(row, col, number, playerManager);
    }

    public void addNoteToCell(int row, int col, int number) {
        board.addNote(row, col, number);
    }

    public boolean endsInAPossibleWin(int row, int col, int number) {
        return gameSolution.getValueOf(row, col) == number;
    }

    public boolean hasDoubledInSameRow(int row, int column, int number) {
        return board.hasDoubledInSameRow(row, column, number);
    }

    public boolean hasDoubledInSameColumn(int row, int column, int number) {
        return board.hasDoubledInSameColumn(row, column, number);
    }

    public boolean hasDoubledInSameBlock(int row, int col, int number) {
        return board.hasDoubledInSameBlock(row, col, number);
    }

    public List<Integer> validNotes(int row, int column) {
        return board.validNotes(row, column);
    }

    public void invokeWrongClock(int r, int c) {
        wrongClock = new Clock();
        SudokuClock wrongCellClock = new SudokuClock(board.get(r, c), view, wrongClock, this);
        wrongCellClock.startClock();
    }

    public SudokuClock getClock() {
        return clock;
    }

    public void setView(BoardView view) {
        this.view = view;
    }

    public SudokuBoard getGameSolution() {
        return gameSolution;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public Clock getGlobalClock() {
        return globalClock;
    }

    public Clock getPersonalClock() {
        return personalClock;
    }

    public Clock getWrongClock() {
        return wrongClock;
    }

    private int getLevel() {
        return this.level;
    }

    public void launchWonActivity() {
        context.gameFinished();
    }

}
