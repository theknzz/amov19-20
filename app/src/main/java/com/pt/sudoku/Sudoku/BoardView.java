package com.pt.sudoku.Sudoku;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
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

public class BoardView extends View implements Serializable {

    private Context context;
    private int selectedNumber = 0;
    private int gameMode;
    private boolean isNotesMode = false;

    private GameLogic logic;

    private Paint paintMainLines, paintSubLines, paintMainNumbers, paintSmallNumbers;

    public BoardView(Context context, SudokuClock clock, GameLogic logic) {
        super(context);
        this.gameMode = 1;
        this.context = context;
        this.logic = logic;
//        this.globalClock = new Clock();
//        this.personalClock = new Clock();
//        this.wrongClock = new Clock();
//        this.clock = new SudokuClock(tvClock, globalClock);
        createPaints();
//        initializeGame(level);
//        resolveGame(board.toPrimitiveBoard());
        clock.startClock();
    }

    public BoardView(Context context, SudokuClock clock, PlayerManager manager, GameLogic logic) {
        super(context);
        this.gameMode= 2;
        this.context = context;
        this.logic = logic;
//        this.globalClock = new Clock();
//        this.clock = new SudokuClock(tvClock, globalClock);
//        this.personalClock = new Clock();
//        this.wrongClock = new Clock();
//        playerManager = new PlayerManager(new Player("A", true), new Player("B"), tvPlayer, tvPlayerClock, personalClock);
        createPaints();
//        initializeGame(level);
//        resolveGame(board.toPrimitiveBoard());
        clock.startClock();
        manager.triggerPlayerClock();
    }

    public BoardView(BoardView board, GameLogic logic, Context context, SudokuClock clock) {
        super(context);
        this.context = context;
        this.gameMode = 1;
        this.logic = logic;
        this.isNotesMode = board.isNotesMode();
        this.selectedNumber = board.getSelectedNumber();
        this.context = board.getContext();
//        this.globalClock = board.getGlobalClock();
//        this.wrongClock = board.getWrongClock();
        createPaints();
//        this.board = board.getBoard();
//        this.gameSolution = board.getGameSolution();
//        this.clock = new SudokuClock(tvClock, globalClock);
        clock.startClock();
    }

    public BoardView(BoardView board, GameLogic logic, Context context, SudokuClock clock, PlayerManager manager) {
        super(context);
        this.context = context;
        this.gameMode= 2;
        this.logic = logic;
        this.selectedNumber = getSelectedNumber();
        this.isNotesMode = board.isNotesMode();
//        this.globalClock = board.getGlobalClock();
//        this.personalClock = board.getPersonalClock();
//        this.wrongClock = board.getWrongClock();
//        this.playerManager = new PlayerManager(board.getPlayerManager(), tvPlayer, tvPlayerClock, personalClock);
//        this.clock = new SudokuClock(tvClock, globalClock);
        createPaints();
//        this.board = board.getBoard();
//        this.gameSolution = board.getGameSolution();
        clock.startClock();
        manager.triggerPlayerClock();
    }

    public void createPaints() {
        paintMainLines = new Paint(Paint.DITHER_FLAG);
        paintMainLines.setStyle(Paint.Style.FILL_AND_STROKE);
        paintMainLines.setColor(Color.BLACK);
        paintMainLines.setStrokeWidth(8);

        paintSubLines = new Paint(paintMainLines);
        paintSubLines.setStrokeWidth(3);

        paintMainNumbers = new Paint(paintSubLines);
        paintMainNumbers.setColor(Color.rgb(0, 0, 120));
        paintMainNumbers.setTextSize(32);
        paintMainNumbers.setTextAlign(Paint.Align.CENTER);

        paintSmallNumbers = new Paint(paintMainNumbers);
        paintSmallNumbers.setTextSize(12);
        paintSmallNumbers.setStrokeWidth(2);
        paintSmallNumbers.setColor(Color.rgb(0x40, 0x80, 0xa0));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int w = getWidth(), cellW = w / logic.BOARD_SIZE;
        int h = getHeight(), cellH = h / logic.BOARD_SIZE;

        for (int i =0; i <= logic.BOARD_SIZE; i++) {
            canvas.drawLine(0, cellH * i, w, cellH * i, i % 3 == 0? paintMainLines: paintSubLines);
            canvas.drawLine(cellW * i, 0, cellW * i, w, i % 3 == 0? paintMainLines: paintSubLines);
        }

        if (logic.getBoard() == null)
            return;

        paintMainNumbers.setTextSize(cellH/2);
        paintSmallNumbers.setTextSize(cellH/4);

        for (int r =0; r < logic.BOARD_SIZE; r++) {
            for (int c = 0; c < logic.BOARD_SIZE; c++) {
                SudokuCell cell = logic.getCell(r,c);
                int n = cell.getValue();
                if (n != 0) {
                    int x = c * cellW + cellW / 2;
                    int y = r * cellH + cellH / 2 + cellH / 6;
                    if (!isAccordingToRules(r,c,n) || !endsInAPossibleWin(r,c,n)) {
                        logic.invokeWrongClock(r,c);
                        paintMainNumbers.setColor(Color.RED);
                        canvas.drawText("" + n, x, y, paintMainNumbers);
                    } else {
                        paintMainNumbers.setColor(Color.rgb(0, 0, 120));
                        canvas.drawText("" + n, x, y, paintMainNumbers);
                    }
                }
                else {
                    if (cell.hasNotes()) {
                        int x = c * cellW + cellW / 6;
                        int y = r * cellH + cellH / 6;
                        List<Integer> notes = cell.getNotes();
                        for (int p = 1; p <= logic.BOARD_SIZE; p++) {
                            if (notes.contains(p)) {
                                int xp = x + (p - 1) % 3 * cellW / 3;
                                int yp = y + (p - 1) / 3 * cellH / 3 + cellH / 9;
                                canvas.drawText(""+p, xp, yp, paintSmallNumbers);
                            }
                        }
                    }
                }
            }
        }
        if (isGameFinished()) {
            if (gameMode==2) {
                Player p = logic.getWinner();
                Toast.makeText(context, "Player won the game (with " + p.getRightGuesses()+ ").", Toast.LENGTH_SHORT).show();
            }
            else if (gameMode==1) {
                Toast.makeText(context, "Game is over!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isGameFinished() {
        return logic.isFinished();
    }

    private void updateNotes(int r, int c) {
        updateRowNotes(r);
        updateColumnNotes(c);
        updateBlockNotes(r,c);
    }

    private void updateBlockNotes(int r, int c) {
        int row = r - r % 3;
        int col = c - c % 3;

        for (int i = row; i < row + 3; i++)
            for (int j = col; j < col + 3; j++)
               logic.getCell(i,j).removeInvalidNotes(validNotes(i,j));
    }

    private void updateColumnNotes(int c) {
        for (int i=0; i <logic.BOARD_SIZE; i++)
            logic.getCell(i, c).removeInvalidNotes(validNotes(i,c));
    }

    private void updateRowNotes(int r) {
        for (int i=0; i < logic.BOARD_SIZE; i++)
            logic.getCell(r, i).removeInvalidNotes(validNotes(r,i));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN)
            return true;
        if (event.getAction() == MotionEvent.ACTION_UP) {
            int px = (int) event.getX();
            int py = (int) event.getY();

            int w = getWidth(), cellW = w / logic.BOARD_SIZE;
            int h = getHeight(), cellH = h / logic.BOARD_SIZE;

            int cellY = px / cellW;
            int cellX = py / cellH;

            if (gameMode==1) {
                if (!isNotesMode) {
                    if (!logic.setValueToCell(cellX, cellY, selectedNumber))
                        Toast.makeText(context, "can't change initial value", Toast.LENGTH_SHORT).show();
                    else
                        updateNotes(cellX, cellY);
                } else {
                    logic.addNoteToCell(cellX, cellY, selectedNumber);
                }
            } else if (gameMode==2) {
                if (!isNotesMode) {
                    if (!logic.setValueToCellModeTwo(cellX, cellY, selectedNumber))
                        Toast.makeText(context, "can't change initial value", Toast.LENGTH_SHORT).show();
                    else
                        updateNotes(cellX, cellY);
                } else {
                    logic.addNoteToCell(cellX, cellY, selectedNumber);
                }
//                Toast.makeText(context, playerManager.getActualPlayer().getRightGuesses()+"", Toast.LENGTH_SHORT).show();
            }
            invalidate();
        }
        return super.onTouchEvent(event);
    }

    private boolean isAccordingToRules(int x, int y, int number){
        return !hasDoubledInSameRow(x,y, number) && !hasDoubledInSameColumn(x,y,number) && !hasDoubledInSameBlock(x, y, number);
    }

    private boolean hasDoubledInSameBlock(int row, int col, int number) {
        return logic.hasDoubledInSameBlock(row, col, number);
    }

    private boolean hasDoubledInSameColumn(int row, int column, int number) {
       return logic.hasDoubledInSameColumn(row, column, number);
    }

    private boolean hasDoubledInSameRow(int row, int column, int number) {
       return logic.hasDoubledInSameRow(row, column, number);
    }

    private boolean endsInAPossibleWin(int row, int col, int number) {
        return logic.endsInAPossibleWin(row, col, number);
    }

    private List<Integer> validNotes(int row, int column) {
        return logic.validNotes(row, column);
    }


    public void setSelectedNumber(int selectedNumber) {
        this.selectedNumber = selectedNumber;
    }

    public void switchNotesMode() {
        if(isNotesMode)
            isNotesMode = false;
        else
            isNotesMode = true;
    }

    public void switchGameMode() {
        gameMode=1;
    }

    public int getSelectedNumber() {
        return selectedNumber;
    }

    public boolean isNotesMode() {
        return isNotesMode;
    }

}
