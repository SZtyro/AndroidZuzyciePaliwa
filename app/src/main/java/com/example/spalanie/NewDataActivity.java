package com.example.spalanie;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class NewDataActivity extends AppCompatActivity implements LocationListener {

    private EditText refuelDate, odometer, fuelVolume, remarks, location;

    final Calendar myCalendar = Calendar.getInstance();
    Geocoder gcd;
    List<Address> addresses = new ArrayList<>();
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_data);

        refuelDate = findViewById(R.id.refuel_date);
        odometer = findViewById(R.id.refuel_odometer);
        fuelVolume = findViewById(R.id.refuel_fuel_volume);
        remarks = findViewById(R.id.refuel_remarks);
        location = findViewById(R.id.refuel_location);
        ImageButton locationBtn = findViewById(R.id.refuel_location_button);
        Button saveButton = (Button) findViewById(R.id.save_button);
        updateLabel();


        locationManager = (LocationManager)
                getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        //LocationListener locationListener = new AppLocationListener();
        gcd = new Geocoder(getBaseContext(),
                Locale.getDefault());


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(NewDataActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
        }
        try {

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        } catch (Exception e) {
            e.printStackTrace();
        }


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

        locationBtn.setOnClickListener(view -> {
            if (addresses.size() > 0)
                location.setText(addresses.get(0).getThoroughfare() + " " + addresses.get(0).getLocality());
            else{
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                Toast.makeText(NewDataActivity.this, R.string.cannot_find_localization, Toast.LENGTH_SHORT).show();
            }

        });

        saveButton.setOnClickListener(view -> {
            try {
                Context context = getApplicationContext();
                JSONArray l;
                try {
                    l = FileHelper.openFile(context);
                } catch (FileNotFoundException ex) {
                    FileHelper.createFile(context);
                    l = FileHelper.openFile(context);
                }

                float o = Float.parseFloat(odometer.getText().toString());
                float fv = Float.parseFloat(fuelVolume.getText().toString());
                float result = 0.0f;
                try {


                    JSONObject object1 = l.getJSONObject(l.length() - 1);
                    float odometer1 = Float.parseFloat(String.valueOf(object1.get("odometer")));
                    //float fuelVolume1 = (float) object.get("fuelVolume");

                    float distance = o - odometer1;
                    result = (100 * fv) / distance;

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                JSONObject object = new JSONObject();
                object.put("refuelDate", myCalendar.getTime().getTime());
                object.put("odometer", o);
                object.put("fuelVolume", fv);
                object.put("remarks", remarks.getText().toString());
                object.put("consumption", result);
                object.put("location", location.getText().toString());

                l.put(object);
                FileHelper.saveFile(l, context);

                this.finish();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });


    }

    private void updateLabel() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");

        refuelDate.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public void onLocationChanged(Location l) {

        try {
            addresses = gcd.getFromLocation(l.getLatitude(),
                    l.getLongitude(), 1);

            System.out.println(addresses.get(0).toString());

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(NewDataActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) -> {
                    startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));

                })
                .setNegativeButton("No", (dialog, id) -> dialog.cancel());
        final AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }


}
