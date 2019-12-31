package com.pt.sudoku;

import android.app.Service;

import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class ServerBackgroundService extends Service {

    ServerSocket socket;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Start server service", Toast.LENGTH_LONG).show();

        try {
            socket = new ServerSocket(8080);

            int count = 0;

            while (count < 3) {
                final Socket client = socket.accept();

                final Thread tClient = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        BufferedInputStream in = null;
                        try {
                            in = new BufferedInputStream(client.getInputStream());
                            BufferedOutputStream out = new BufferedOutputStream(client.getOutputStream());

                            while (true) {

                                in.read();

                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                count++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service destroyed by user.", Toast.LENGTH_LONG).show();
    }
}