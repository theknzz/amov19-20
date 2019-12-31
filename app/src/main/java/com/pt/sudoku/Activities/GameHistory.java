package com.pt.sudoku.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.LauncherActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.pt.sudoku.History.CustomAdapter;
import com.pt.sudoku.History.HistoryModel;
import com.pt.sudoku.R;

import java.util.ArrayList;

public class GameHistory extends AppCompatActivity {
    ArrayList<HistoryModel> dataModels;

    private static CustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_history);
        ListView listView = (ListView) findViewById(R.id.lvHistory);

        dataModels = new ArrayList<>();

        dataModels.add(new HistoryModel("1", "Easy", "12:45",""));
        dataModels.add(new HistoryModel("3", "Hard", "13","Me"));
        dataModels.add(new HistoryModel("2", "Easy", "10","Me"));
        dataModels.add(new HistoryModel("3", "Easy", "2","Not me"));
        dataModels.add(new HistoryModel("2", "Hard", "23","Me"));
        dataModels.add(new HistoryModel("1", "Easy", "5:45",""));
        dataModels.add(new HistoryModel("1", "Hard", "12:45",""));
        dataModels.add(new HistoryModel("1", "Easy", "12:45",""));
        dataModels.add(new HistoryModel("3", "Hard", "12:45","Not me"));
        dataModels.add(new HistoryModel("1", "Easy", "12:45",""));
        dataModels.add(new HistoryModel("2", "Easy", "12:45","Not me"));
        dataModels.add(new HistoryModel("3", "Hard", "12:45","Not me"));
        dataModels.add(new HistoryModel("1", "Easy", "12:45",""));
        dataModels.add(new HistoryModel("1", "Hard", "12:45",""));
        dataModels.add(new HistoryModel("1", "Easy", "12:45",""));
        dataModels.add(new HistoryModel("3", "Hard", "12:45","Not me"));
        dataModels.add(new HistoryModel("1", "Easy", "12:45",""));
        dataModels.add(new HistoryModel("2", "Easy", "12:45","Not me"));
        dataModels.add(new HistoryModel("3", "Hard", "12:45","Not me"));
        dataModels.add(new HistoryModel("1", "Easy", "12:45",""));
        dataModels.add(new HistoryModel("1", "Hard", "12:45",""));
        dataModels.add(new HistoryModel("1", "Easy", "12:45",""));
        dataModels.add(new HistoryModel("3", "Hard", "12:45","Not me"));
        dataModels.add(new HistoryModel("1", "Easy", "12:45",""));
        dataModels.add(new HistoryModel("2", "Easy", "12:45","Not me"));
        dataModels.add(new HistoryModel("3", "Hard", "12:45","Not me"));
        dataModels.add(new HistoryModel("1", "Easy", "12:45",""));

        adapter= new CustomAdapter(dataModels,getApplicationContext());

        listView.setAdapter(adapter);
    }
}
