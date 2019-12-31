package com.pt.sudoku.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;

import com.pt.sudoku.ComunicationBackgroundService;
import com.pt.sudoku.R;

public class GameQueue extends AppCompatActivity {

    static boolean running = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game_queue);

        Intent intentComm = new Intent(this, ComunicationBackgroundService.class);


        intentComm.putExtra("mode", getIntent().getStringExtra("mode"));

        // Thread.sleep(1000);
        // startService(intentClient);

        if (running == false) {
            startService(intentComm);

            //ContextCompat.startForegroundService(this, intentClient);
            running = true;
        }
    }
}
