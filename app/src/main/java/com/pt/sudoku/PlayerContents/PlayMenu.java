package com.pt.sudoku.PlayerContents;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.pt.sudoku.Activities.GameQueue;
import com.pt.sudoku.Activities.Gameplay;
import com.pt.sudoku.R;

public class PlayMenu extends AppCompatActivity {

    private Dialog difficultDialog;
    private Dialog serverClient;
    private int mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_menu);
    }

    public void onMode(View view) {
        switch (((Button) view).getText().toString()) {
            case "Mode 1":
                mode = 1;
                break;
            case "Mode 2":
                mode = 2;
                break;
            case "Mode 3":
                mode = 3;
                break;
        }
        if (mode != 3) {
            difficultDialog = new Dialog(this);
            difficultDialog.setContentView(R.layout.pop_up_start);
            difficultDialog.show();
        } else {

            serverClient = new Dialog(this);
            serverClient.setContentView(R.layout.pop_up_servers);
            serverClient.show();
        }
    }

    public void onChooseType(View view) {
        String type = "";

        switch (((Button) view).getText().toString()) {
            case "server": {
                type = "server";
                difficultDialog = new Dialog(this);
                difficultDialog.setContentView(R.layout.pop_up_start);
                difficultDialog.show();
                break;
            }
            case "client": {
                Intent intent = new Intent(this, GameQueue.class);
                type = "client";
                intent.putExtra("type", type);
                startActivity(intent);
                break;
            }
        }
    }

    public void onLevelPick(View view) {

        Intent intent = new Intent(this, Gameplay.class);
        intent.putExtra("mode", mode);
        int level = 8;

        Button b = (Button) view;
        switch (b.getId()) {
            //button easy
            case R.id.btnEasy:
                level = 6;
                intent.putExtra("level", level);
                break;
            //button medium
            case R.id.btnMedium:
                level = 8;
                intent.putExtra("level", level);
                break;
            //button hard
            case R.id.btnHard:
                //TODO devia ser 12 mas o código do stor fica muito lento por isso ficou 10
                level = 10;
                intent.putExtra("level", level);
                break;
            default: {
                difficultDialog.cancel();
            }
        }
        if(mode==3){
            Intent queueIntent = new Intent(this, GameQueue.class);
            queueIntent.putExtra("mode", mode);
            queueIntent.putExtra("level", level);
            queueIntent.putExtra("type", "server");
            startActivity(queueIntent);
        }
        else
        startActivity(intent);
    }
}
