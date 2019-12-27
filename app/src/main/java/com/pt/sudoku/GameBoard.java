package com.pt.sudoku;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GameBoard extends View {

    public static final int BOARD_SIZE = 9;
    private int selectedNumber = 0;
    int [][] board = new int[][] {
            {0 , 0, 0, 0, 0 ,0, 0, 0, 0},
            {0 , 0, 0, 0, 0 ,0, 0, 0, 0},
            {0 , 0, 0, 0, 0 ,0, 0, 0, 0},
            {0 , 0, 0, 0, 0 ,0, 0, 0, 0},
            {0 , 0, 0, 0, 0 ,0, 0, 0, 0},
            {0 , 0, 0, 0, 0 ,0, 0, 0, 0},
            {0 , 0, 0, 0, 0 ,0, 0, 0, 0},
            {0 , 0, 0, 0, 0 ,0, 0, 0, 0},
            {0 , 0, 0, 0, 0 ,0, 0, 0, 0},
    };


    private Paint paintMainLines, paintSubLines, paintMainNumbers, paintSmallNumbers;

    public GameBoard(Context context) {
        super(context);
        createPaints();
    }

    public void createPaints() {
        paintMainLines = new Paint(Paint.DITHER_FLAG);
        paintMainLines.setStyle(Paint.Style.FILL_AND_STROKE);
        paintMainLines.setColor(Color.BLACK);
        paintMainLines.setStrokeWidth(8);

        // para evitar repetir tudo
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
                int n = board[r][c];
                if (n != 0) {
                    int x = c * cellW + cellW / 2;
                    int y = r * cellH + cellH / 2 + cellH / 6;
                    if (!isAccordingToRules(r,c,n)) {
                        paintMainNumbers.setColor(Color.RED);
                        canvas.drawText("" + n, x, y, paintMainNumbers);
                    } else {
                        paintMainNumbers.setColor(Color.rgb(0, 0, 120));
                        canvas.drawText("" + n, x, y, paintMainNumbers);
                    }
                }
            }
        }
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

            board[cellX][cellY] = 8;
            invalidate();
        }
        return super.onTouchEvent(event);
    }

    private boolean isAccordingToRules(int x, int y, int number){
        return !hasDoubledInSameRow(x,y, number) && !hasDoubledInSameColumn(x,y,number) && !hasDoubledInSameBlock(x, y, number);
    }

    private boolean hasDoubledInSameBlock(int row, int col, int number) {
        int r = row - row % 3;
        int c = col - col % 3;

        for (int i = r; i < r + 3; i++)
            for (int j = c; j < c + 3; j++)
                if (board[i][j] == number && (i!=row && j!=col))
                    return true;
        return false;
    }

    private boolean hasDoubledInSameColumn(int row, int column, int number) {
        for (int r=0; r < BOARD_SIZE; r++)
        {
            if (r!=row)
                if (board[r][column]==number) return true;
        }
        return false;
    }

    private boolean hasDoubledInSameRow(int row, int column, int number) {
        for (int c=0; c < BOARD_SIZE; c++){
            if (c!=column)
                if (board[row][c]==number) return true;
        }
        return false;
    }

    private void initializeGame() {
        String strJson = Sudoku.generate(7);
        try {
            JSONObject json = new JSONObject(strJson);
            if (json.optInt("result",0) == 1) {
                JSONArray jsonArray = json.getJSONArray("board");
                int[][] board = convert(jsonArray);
                setBoard(board);
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

    private void resolveGame() {
        try {
            JSONObject json = new JSONObject();
            JSONArray jsonArray = convert(board);
            json.put("board", jsonArray);
            String strJson = Sudoku.solve(json.toString(), 1000*2);

            Log.d("TesteSudoku", "JSON:"+strJson);

            try {
                json = new JSONObject(strJson);
                if (json.optInt("result",0) == 1) {
                    jsonArray = json.getJSONArray("board");
                    int[][] board = convert(jsonArray);
                    setBoard(board);
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
}
