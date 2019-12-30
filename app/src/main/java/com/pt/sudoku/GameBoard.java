package com.pt.sudoku;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;
import pt.isec.ans.sudokulibrary.Sudoku;

public class GameBoard extends View {

    public static final int BOARD_SIZE = 9;
    private int selectedNumber = 0;
    private int level;
    private Context context;
    private boolean isNotesMode = false;

    private SudokuBoard board = new SudokuBoard();
    private SudokuBoard gameSolution = new SudokuBoard();
    private SudokuClock clock = null;
    private SudokuClock playerClock = null;

    private PlayerManager playerManager;
    private int gameMode;

    private Paint paintMainLines, paintSubLines, paintMainNumbers, paintSmallNumbers;

    private TextView tvPlayerClock, tvPlayer, tvClock;


    public GameBoard(Context context, int level, TextView tvClock) {
        super(context);
        this.gameMode = 1;
        this.context = context;
        this.level = level;
        this.clock = new SudokuClock(tvClock);
        createPaints();
        initializeGame(level);
        resolveGame(board.toPrimitiveBoard());
        clock.startClock();
    }

    public GameBoard(Context context, int level, TextView tvClock, TextView tvPlayerClock, TextView tvPlayer) {
        super(context);
        this.gameMode= 2;
        this.tvPlayerClock = tvPlayerClock;
        this.tvPlayer = tvPlayer;
        this.context = context;
        this.level = level;
        this.clock = new SudokuClock(tvClock);
        playerManager = new PlayerManager(new Player("A", true), new Player("B"), tvPlayer, tvPlayerClock);
        createPaints();
        initializeGame(level);
        resolveGame(board.toPrimitiveBoard());
        clock.startClock();
        playerManager.triggerPlayerClock();
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

        int w = getWidth(), cellW = w / BOARD_SIZE;
        int h = getHeight(), cellH = h / BOARD_SIZE;

        for (int i =0; i <= BOARD_SIZE; i++) {
            canvas.drawLine(0, cellH * i, w, cellH * i, i % 3 == 0? paintMainLines: paintSubLines);
            canvas.drawLine(cellW * i, 0, cellW * i, w, i % 3 == 0? paintMainLines: paintSubLines);
        }

        if (board == null)
            return;

        paintMainNumbers.setTextSize(cellH/2);
        paintSmallNumbers.setTextSize(cellH/4);

        for (int r =0; r < BOARD_SIZE; r++) {
            for (int c = 0; c < BOARD_SIZE; c++) {
                SudokuCell cell = board.get(r,c);
                int n = cell.getValue();
                if (n != 0) {
                    int x = c * cellW + cellW / 2;
                    int y = r * cellH + cellH / 2 + cellH / 6;
                    if (!isAccordingToRules(r,c,n) || !endsInAPossibleWin(r,c,n)) {
                        SudokuClock wrongCellClock = new SudokuClock(board.get(r, c), this);
                        wrongCellClock.startClock();
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
                        for (int p = 1; p <= BOARD_SIZE; p++) {
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
               board.get(i,j).removeInvalidNotes(validNotes(i,j));
    }

    private void updateColumnNotes(int c) {
        for (int i=0; i < BOARD_SIZE; i++)
            board.get(i, c).removeInvalidNotes(validNotes(i,c));
    }

    private void updateRowNotes(int r) {
        for (int i=0; i < BOARD_SIZE; i++)
            board.get(r, i).removeInvalidNotes(validNotes(r,i));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN)
            return true;
        if (event.getAction() == MotionEvent.ACTION_UP) {
            int px = (int) event.getX();
            int py = (int) event.getY();

            int w = getWidth(), cellW = w / BOARD_SIZE;
            int h = getHeight(), cellH = h / BOARD_SIZE;

            int cellY = px / cellW;
            int cellX = py / cellH;

            if (gameMode==1) {
                if (!isNotesMode) {
                    if (!board.setValue(cellX, cellY, selectedNumber))
                        Toast.makeText(context, "can't change initial value", Toast.LENGTH_SHORT).show();
                    else
                        updateNotes(cellX, cellY);
                } else {
                    board.addNote(cellX, cellY, selectedNumber);
                }
            } else if (gameMode==2) {
                if (!isNotesMode) {
                    if (!board.setValue(cellX, cellY, selectedNumber, playerManager))
                        Toast.makeText(context, "can't change initial value", Toast.LENGTH_SHORT).show();
                    else
                        updateNotes(cellX, cellY);
                } else {
                    board.addNote(cellX, cellY, selectedNumber);
                }
                Toast.makeText(context, playerManager.getActualPlayer().getRightGuesses()+"", Toast.LENGTH_SHORT).show();
            }
            invalidate();
        }
        return super.onTouchEvent(event);
    }

    private boolean isAccordingToRules(int x, int y, int number){
        return !hasDoubledInSameRow(x,y, number) && !hasDoubledInSameColumn(x,y,number) && !hasDoubledInSameBlock(x, y, number);
    }

    private boolean hasDoubledInSameBlock(int row, int col, int number) {
        return board.hasDoubledInSameBlock(row, col, number);
    }

    private boolean hasDoubledInSameColumn(int row, int column, int number) {
       return board.hasDoubledInSameColumn(row, column, number);
    }

    private boolean hasDoubledInSameRow(int row, int column, int number) {
       return board.hasDoubledInSameRow(row, column, number);
    }

    private boolean endsInAPossibleWin(int row, int col, int number) {
        return gameSolution.getValueOf(row, col) == number;
    }

    private List<Integer> validNotes(int row, int column) {
        return board.validNotes(row, column);
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
        playerManager.lockPlayer(tvClock);
    }
}
