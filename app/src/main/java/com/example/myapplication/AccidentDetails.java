package com.example.myapplication;

public class AccidentDetails {
    String time, AccidentAddress, carPlate, firstId, secondId;

    public AccidentDetails() {
    }

    public AccidentDetails(String time, String accidentAddress, String carPlate, String firstId , String secondId) {
        this.time = time;
        this.AccidentAddress = accidentAddress;
        this.carPlate = carPlate;
        this.firstId = firstId;
        this.secondId = secondId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAccidentAddress() {
        return AccidentAddress;
    }

    public void setAccidentAddress(String accidentAddress) {
        AccidentAddress = accidentAddress;
    }

    public String getCarPlate() {
        return carPlate;
    }

    public void setCarPlate(String carPlate) {
        this.carPlate = carPlate;
    }

    public String getSecondId() {
        return secondId;
    }

    public void setSecondId(String secondId) {
        this.secondId = secondId;
    }

    public String getFirstId() {
        return firstId;
    }

    public void setFirstId(String firstId) {
        this.firstId = firstId;
    }
}
