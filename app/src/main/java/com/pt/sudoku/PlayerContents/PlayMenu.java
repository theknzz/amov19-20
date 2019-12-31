package com.pt.sudoku.PlayerContents;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.pt.sudoku.Activities.GameplayActivity;
import com.pt.sudoku.R;

public class PlayMenu extends AppCompatActivity {

    private Dialog difficultDialog;
    private int mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_menu);
    }

    public void onMode(View view) {
        String str = ((Button)view).getText().toString();
        if (str.equals(getString(R.string.mode_1)))
            mode = 1;
        else if (str.equals(getString(R.string.mode_2)))
            mode = 2;
        else if (str.equals(getString(R.string.mode_3)))
            mode = 3;
        difficultDialog = new Dialog(this);
        difficultDialog.setContentView(R.layout.pop_up_start);
        difficultDialog.show();
    }

    public void onLevelPick(View view) {

        Intent intent = new Intent(this, GameplayActivity.class);
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
