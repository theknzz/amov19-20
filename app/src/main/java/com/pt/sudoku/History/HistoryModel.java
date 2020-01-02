package com.pt.sudoku.History;

import android.os.Environment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;

public class HistoryModel {
    String mode, difficulty, result, winner;

    public HistoryModel(String mode, String difficulty, String result, String winner){
        this.mode = mode;
        this.difficulty = difficulty;
        this.result = result;
        this.winner = winner;
    }

    public String getMode() {
        return mode;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getResult() {
        return result;
    }

    public String getWinner() {
        return winner;
    }


    public static boolean writeHistoryJSON(int gameMode, int level, String result, String name){
        File file = new File(Environment.getExternalStorageDirectory(), "history_file.json");

        String strFileJson;
        FileInputStream fin = null;

        JSONObject jsonObj = null;

        if(file.exists()){
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

            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }

            try {
                jsonObj = new JSONObject(strFileJson);
            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            }
        }else{
            jsonObj = new JSONObject();
        }

        String difficulty = "";
        switch (level){
            case 6:
                difficulty = "Easy";
                break;
            case 8:
                difficulty = "Medium";
                break;
            case 10:
                difficulty = "Hard";
                break;
        }

        try {
            JSONObject newObj = new JSONObject();
            newObj.put("mode", gameMode);
            newObj.put("difficulty", difficulty);
            newObj.put("result", result);
            newObj.put("winner", name);
            int size = 0;
            try {
                size = jsonObj.getInt("size");
            }catch (JSONException e){
                size = 0;
            }

            size++;

            jsonObj.put("size", size);

            jsonObj.put("game"+size, newObj);



            Writer toFile = new BufferedWriter(new FileWriter(file));
            toFile.write(jsonObj.toString());
            toFile.close();

            return true;
        } catch (JSONException | IOException e) {
            e.printStackTrace();
            return false;
        }


    }
}
