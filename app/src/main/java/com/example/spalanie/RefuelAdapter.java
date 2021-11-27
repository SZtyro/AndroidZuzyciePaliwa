package com.example.spalanie;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.spalanie.model.RefuelData;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RefuelAdapter extends ArrayAdapter<JSONObject> {
    public RefuelAdapter(@NonNull Context context, ArrayList<JSONObject> data) {
        super(context, 0, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        JSONObject report = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.home_view_list_elem, parent, false);
        }
        // Lookup view for data population
        TextView date = (TextView) convertView.findViewById(R.id.entries_elem_date);
        TextView odometer = (TextView) convertView.findViewById(R.id.entries_elem_odometer);
        TextView fuelVolume = (TextView) convertView.findViewById(R.id.entries_elem_fuel_volume);
        try {
            DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT);
            date.setText(dateFormat.format(new Date(report.getLong("refuelDate"))));
            odometer.setText(report.get("odometer").toString());
            fuelVolume.setText(report.get("fuelVolume").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return convertView;
    }
}
