package com.pt.sudoku;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.pt.sudoku.Activities.Gameplay;
import com.pt.sudoku.Clock.Clock;
import com.pt.sudoku.PlayerContents.Player;
import com.pt.sudoku.PlayerContents.PlayerManager;
import com.pt.sudoku.Sudoku.SudokuBoard;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Semaphore;

import pt.isec.ans.sudokulibrary.Sudoku;

import static com.pt.sudoku.Sudoku.BoardView.BOARD_SIZE;


public class ComunicationBackgroundService extends Service {


    ServerSocket serverSocket;
    int numClients = 0;
    int level;
    int currClient=0;
    boolean update0 = false, update1 = false, update2 = false;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        final String mode = intent.getStringExtra("mode");
        final String clientIP = intent.getStringExtra("ip");
        level = intent.getIntExtra("level", 8);

        final Semaphore semaphoreClients = new Semaphore(0);
        final Semaphore startLocalCliente = new Semaphore(0);

        final PlayerManager playerManager = new PlayerManager(new Player("A"), new Player("B"), new Player("C"), new Clock());


        if (mode != null && mode.equals("server")) {
            Thread server = new Thread(new Runnable() {

                @Override
                public void run() {
                    final Object sync = new Object();
                    final boolean update = true;
                    try {
                        serverSocket = new ServerSocket(9080);
                        numClients = 0;
                        final SudokuBoard board = new SudokuBoard();
                        SudokuBoard solvedBoard = new SudokuBoard();


                        while (numClients < 3) {

                            //deixa ele proprio ligar-se
                            startLocalCliente.release(1);
                            final Socket clientSocket = serverSocket.accept();

                            Thread threadClient = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                                        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                                        int cliID = numClients;

                                        JSONObject json;

                                        StringBuilder sb = new StringBuilder();

                                         if (numClients<3){
                                             while (true){
                                                 if (numClients>=3) {
                                                     if (in.ready()) {
                                                         String line;
                                                         while ((line = in.readLine()) != null) {
                                                             sb = new StringBuilder();
                                                             sb.append(in.read());
                                                         }
                                                         json = new JSONObject(sb.toString());


                                                         if (currClient == cliID) {
                                                             if (json.get("action") == "add") {
                                                                 int x = json.getInt("x");
                                                                 int y = json.getInt("y");
                                                                 int num = json.getInt("num");

                                                                 if (board.add(x, y, num)) {
                                                                     playerManager.addRightGuessToActualPlayer();
                                                                 }else {
                                                                     playerManager.addWrongGuessToActualPlayer();
                                                                 }
                                                             }
                                                         } else {
                                                             JSONObject JsonMsg = new JSONObject();
                                                             JsonMsg.put("msg", "WrongUser");
                                                         }

                                                     }
                                                     if(cliID == 0 && isUpdate0()){
                                                        sendUpdatedBoard(out);
                                                     }else if(cliID == 1 && isUpdate1()){

                                                     }else if(cliID == 2 && isUpdate2()){

                                                     }
                                                 }

                                             }
                                            //espera que tenha autorização oara começar

                                            //chega aqui qnd o servidor tiver 3 clientes


                                            // aqui é feita a comunicação com o cliente

                                            //ordena o inicio do jogo


                                            //espera inicio do jogo
                                            //String message = in.readLine();
                                         }

                                    } catch (Exception e) {

                                    }
                                }

                                private void sendUpdatedBoard(BufferedWriter out) {
                                    JSONObject json = new JSONObject();
                                    try {
                                        json.put("board", board.convert(board.boardToInt()));
                                        out.write(json.toString());
                                    } catch (JSONException | IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            threadClient.start();
                            numClients++;
                        }
                        semaphoreClients.release(2);
                        createBoard(board, solvedBoard, level);

                        while (true){
                            if(update){
                                setUpdate0(true);
                                setUpdate1(true);
                                setUpdate2(true);
                            }


                        }






                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            server.start();
        }

        /*
        Thread threadCiente = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //se for o mesmo dispositivo que está a ser de servidor, tem que esperar que o server ligue para se ligar
                    if (mode.equals("server")) {
                        startLocalCliente.acquire();
                    }

                    Socket socket = new Socket(clientIP, 9080);

                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));


                    //aqui é feita a comunicação com o servidor
                    String s = in.readLine();
                    System.exit(1);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        threadCiente.start();
        */
        return START_STICKY;
    }

    private boolean isUpdate0() {
        return update0;
    }

    private boolean isUpdate1() {
        return update1;
    }

    private boolean isUpdate2() {
        return update2;
    }


    private void setUpdate0(boolean b) {
        update0 = b;
    }

    private void setUpdate1(boolean b) {
        update1 = b;
    }

    private void setUpdate2(boolean b) {
        update2 = b;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service destroyed by user.", Toast.LENGTH_LONG).show();
    }

    private void createBoard(SudokuBoard board, SudokuBoard solved, int level){



        String strBoard = Sudoku.generate(level);
        String strSolved = Sudoku.solve(strBoard, 1000*2);

        board = strToBoard(strBoard);
        solved = strToBoard(strSolved);
    }

    private SudokuBoard strToBoard(String strBoard){
        int[][] boardIntTable = null;
        SudokuBoard board = new SudokuBoard();
        try {
            JSONObject jsonBoard = new JSONObject(strBoard);
            if (jsonBoard.optInt("result",0) == 1) {
                JSONArray jsonArray = jsonBoard.getJSONArray("board");
                try {
                    int rows = jsonArray.length(), columns = 0;
                    for (int r=0; r < rows; r++) {
                        JSONArray jsonRow = jsonArray.getJSONArray(r);
                        if (r==0) {
                            columns = jsonRow.length();
                            boardIntTable = new int[rows][columns];
                        }
                        for (int c=0; c<columns; c++) {
                            boardIntTable[r][c] = jsonRow.getInt(c);
                        }
                    }
                } catch (Exception e) {
                    boardIntTable = null;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int i=0; i < BOARD_SIZE; i++)
            for (int j=0; j <BOARD_SIZE; j++) {
                board.addToInitialBoard(i,j, boardIntTable[i][j]);
            }

        return board;


    }
}