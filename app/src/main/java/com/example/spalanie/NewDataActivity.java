package com.example.spalanie;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.icu.util.TimeZone;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.spalanie.model.RefuelData;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.commons.lang3.SerializationUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NewDataActivity extends AppCompatActivity {

    private EditText refuelDate, odometer, fuelVolume, remarks;

    private static final String fileName = "applicationData1";
    final Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_data);

        refuelDate = findViewById(R.id.refuel_date);
        odometer = findViewById(R.id.refuel_odometer);
        fuelVolume = findViewById(R.id.refuel_fuel_volume);
        remarks = findViewById(R.id.refuel_remarks);
        Button saveButton = (Button) findViewById(R.id.save_button);
        updateLabel();


        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        refuelDate.setOnClickListener(View -> {

            // TODO Auto-generated method stub
            new DatePickerDialog(NewDataActivity.this, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();

        });

        saveButton.setOnClickListener(view -> {
            try {
                JSONArray l;
                try {
                    l = openFile();
                } catch (FileNotFoundException ex) {
                    createFile();
                    l = openFile();
                }

                RefuelData data = new RefuelData(
                        Integer.decode(odometer.getText().toString()),
                        Integer.decode(fuelVolume.getText().toString()),
                        new Date(myCalendar.getTime().getTime()),
                        remarks.getText().toString()
                );
                Gson gson = new Gson();
                String jsonString = gson.toJson(data);
//                GsonBuilder builder = new GsonBuilder();

                l.put(new JSONObject(jsonString));
                saveFile(l);


//                Snackbar.make(((MainActivity)this.getParent()).binding.getRoot().getRootView(), "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .show();
                this.finish();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });


    }

    private void updateLabel() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy", Locale.UK);

        refuelDate.setText(sdf.format(myCalendar.getTime()));
    }

    private JSONArray openFile() throws FileNotFoundException, JSONException {
        //Stary plik ma inne pola
        return new JSONArray(SerializationUtils.deserialize(getApplicationContext().openFileInput(fileName)).toString());

    }

    private void saveFile(JSONArray object) {
        FileOutputStream outputStream = null;
        try {
            System.out.println("Saving file:");
            System.out.println("\n" + object.toString(2));
            outputStream = getApplicationContext().openFileOutput(fileName, Context.MODE_PRIVATE);
            outputStream.write(SerializationUtils.serialize(object.toString()));
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void createFile() {

        File file = new File(getApplicationContext().getFilesDir(), fileName);
        try {
            file.createNewFile();
            JSONArray arr = new JSONArray();
            saveFile(arr);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}