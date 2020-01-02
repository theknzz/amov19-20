package com.pt.sudoku.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.LauncherActivity;
import android.os.Bundle;
import android.os.Environment;
import android.util.JsonReader;
import android.widget.ListView;

import com.pt.sudoku.History.CustomAdapter;
import com.pt.sudoku.History.HistoryModel;
import com.pt.sudoku.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
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

        loadHistory(dataModels);

        adapter= new CustomAdapter(dataModels,getApplicationContext());

        listView.setAdapter(adapter);
    }

    private void loadHistory(ArrayList<HistoryModel> dataModels) {
        File file = new File(Environment.getExternalStorageDirectory(), "history_file.json");
        if(!file.exists())
            return;

        String strFileJson;
        FileInputStream fin = null;

        JSONObject jsonObj = null;

        try {
            fin = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fin));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            strFileJson = sb.toString();
            fin.close();

            jsonObj = new JSONObject(strFileJson);
            int size = 0;
            try {
                size = jsonObj.getInt("size");
            }catch (JSONException e){
            }
            JSONObject game = null;
            for(int i = 1; i <= size; i++){
                game = (JSONObject) jsonObj.get("game"+i);
                dataModels.add(new HistoryModel(
                                game.getString("mode"),
                                game.getString("difficulty"),
                                game.getString("result"),
                                game.getString("winner")));
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }


    }
}
