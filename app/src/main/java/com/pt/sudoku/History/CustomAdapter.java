package com.pt.sudoku.History;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.pt.sudoku.R;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<HistoryModel> {
    private ArrayList<HistoryModel> dataSet;
    Context mContext;

    private static class ViewHolder {
        TextView tvMode;
        TextView tvDifficulty;
        TextView tvResult;
        TextView tvWinner;
    }


    public CustomAdapter(ArrayList<HistoryModel> data, Context context) {
        super(context, R.layout.history_list_item, data);
        this.dataSet = data;
        this.mContext=context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        HistoryModel dataModel = getItem(position);

        ViewHolder viewHolder;

        final View result;

        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.history_list_item, parent, false);
            viewHolder.tvMode = (TextView) convertView.findViewById(R.id.tvHistMode);
            viewHolder.tvDifficulty = (TextView) convertView.findViewById(R.id.tvHistDifficulty);
            viewHolder.tvResult = (TextView) convertView.findViewById(R.id.tvHistResult);
            viewHolder.tvWinner = (TextView) convertView.findViewById(R.id.tvHistWinner);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
        viewHolder = (ViewHolder) convertView.getTag();
        result=convertView;
    }
        viewHolder.tvMode.setText(dataModel.getMode());
        viewHolder.tvDifficulty.setText(dataModel.getDifficulty());
        viewHolder.tvResult.setText(dataModel.getResult());
        viewHolder.tvWinner.setText(dataModel.getWinner());
        // Return the completed view to render on screen
        return convertView;
    }
}
