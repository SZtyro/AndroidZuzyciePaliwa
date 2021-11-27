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

    public int getOdometer() {
        return odometer;
    }

    public void setOdometer(int odometer) {
        this.odometer = odometer;
    }

    public int getFuelVolume() {
        return fuelVolume;
    }

    public void setFuelVolume(int fuelVolume) {
        this.fuelVolume = fuelVolume;
    }

    public Date getRefuelDate() {
        return refuelDate;
    }

    public void setRefuelDate(Date refuelDate) {
        this.refuelDate = refuelDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
