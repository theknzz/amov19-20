package com.pt.sudoku;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class PlayMenu extends AppCompatActivity {

    private Dialog difficultDialog;
    private int mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_menu);
    }

    public void onMode(View view) {
        switch (((Button)view).getText().toString()) {
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
        difficultDialog = new Dialog(this);
        difficultDialog.setContentView(R.layout.pop_up_start);
        difficultDialog.show();
    }

    public void onLevelPick(View view) {

        Intent intent = new Intent(this, Gameplay.class);
        intent.putExtra("mode", mode);

        Button b = (Button) view;
        switch (b.getId()) {
            //button easy
            case R.id.btnEasy:
                intent.putExtra("level", 6);
                break;
            //button medium
            case R.id.btnMedium:
                intent.putExtra("level", 8);
                break;
            //button hard
            case R.id.btnHard:
                //TODO devia ser 12 mas o código do stor fica muito lento por isso ficou 10
                intent.putExtra("level", 10);
                break;
            default: {
                difficultDialog.cancel();
            }
        }
        startActivity(intent);
    }
}
