package com.pt.sudoku;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Semaphore;


public class ComunicationBackgroundService extends Service {


    ServerSocket serverSocket;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        final String mode = intent.getStringExtra("mode");
        final String clientIP = intent.getStringExtra("ip");

        final Semaphore semaphoreClients = new Semaphore(0);
        final Semaphore startLocalCliente = new Semaphore(0);

        if (mode != null && mode.equals("server")) {
            Thread server = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        serverSocket = new ServerSocket(9080);

                        int numClients = 0;

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

                                        Gson gson = new Gson();

                                        while (true) {

                                            //espera que tenha autorização oara começar
                                            semaphoreClients.acquire();

                                            //chega aqui qnd o servidor tiver 3 clientes


                                            // aqui é feita a comunicação com o cliente

                                            //ordena o inicio do jogo
                                            out.write("start\n");
                                            out.flush();

                                            //espera inicio do jogo
                                            //String message = in.readLine();
                                        }

                                    } catch (Exception e) {

                                    }
                                }
                            });
                            threadClient.start();
                            numClients++;
                        }
                        semaphoreClients.release(2);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            server.start();
        }

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

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service destroyed by user.", Toast.LENGTH_LONG).show();
    }
}