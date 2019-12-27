package com.pt.sudoku;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PlayMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_menu);
    }

    public void onMode(View view) {
        Intent intent = new Intent(this, Gameplay.class);
        switch (((Button)view).getText().toString()) {
            case "Mode 1":
                intent.putExtra("mode", 1);
                break;
            case "Mode 2":
                intent.putExtra("mode", 2);
                break;
            case "Mode 3":
                intent.putExtra("mode", 3);
                break;
        }
        startActivity(intent);
    }
}
