package com.example.spalanie.model;

import java.io.Serializable;
import java.util.Date;

public class RefuelData implements Serializable {

    int odometer = 0,fuelVolume = 0;
    Date refuelDate = new Date();
    String remarks = "";

    public RefuelData(int odometer, int fuelVolume, Date refuelDate, String remarks) {
        this.odometer = odometer;
        this.fuelVolume = fuelVolume;
        this.refuelDate = refuelDate;
        this.remarks = remarks;
    }
}
