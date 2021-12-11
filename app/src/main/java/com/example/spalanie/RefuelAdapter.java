package com.example.spalanie;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.spalanie.model.RefuelData;
import com.example.spalanie.ui.entries.EntriesFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;

public class RefuelAdapter extends ArrayAdapter<JSONObject> {

    EntriesFragment fragment;
    private static final DecimalFormat df = new DecimalFormat("0.00");

    public RefuelAdapter(@NonNull Context context, ArrayList<JSONObject> data, EntriesFragment parent) {
        super(context, 0, data);
        fragment = parent;
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
        TextView consumption = (TextView) convertView.findViewById(R.id.entries_consumption);
        TextView location = (TextView) convertView.findViewById(R.id.entries_elem_location);
        try {
            DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT);
            date.setText(dateFormat.format(new Date(report.getLong("refuelDate"))));
            odometer.setText(String.format("%s km", report.get("odometer").toString()));
            fuelVolume.setText(String.format("%s l",(df.format(report.get("fuelVolume")))));
            consumption.setText(String.format("%s", (df.format(report.get("consumption")))));
            location.setText(report.get("location").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        convertView.findViewById(R.id.entries_elem_delete).setOnClickListener(view -> {
            System.out.println(position);
            AlertDialog.Builder adb=new AlertDialog.Builder(getContext());
            adb.setTitle(R.string.delete_ask);
            adb.setMessage(R.string.sure_delete);
            adb.setNegativeButton(R.string.cancel, null);
            adb.setPositiveButton("Ok", (dialog, which) -> {
                fragment.deleteElem(position);
                notifyDataSetChanged();
            });
            adb.show();

        });



        return convertView;
    }
}
