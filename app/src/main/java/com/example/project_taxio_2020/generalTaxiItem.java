package com.example.project_taxio_2020;

public class generalTaxiItem {
    private String tripDate, startTime, rentTime;

    public generalTaxiItem(String tripDate, String startTime, String rentTime){
        this.tripDate = tripDate;
        this.startTime = startTime;
        this.rentTime = rentTime;
    }

    public generalTaxiItem(){}

    public String getTripDate() { //기사 이름
        return tripDate;
    }

    public void setTripDate(String tripDate) {
        this.tripDate = tripDate;
    }

    public String getRentTime() { //기사 이름
        return rentTime;
    }

    public void setRentTime(String rentTime) { this.rentTime=rentTime; }

    public String getStartTime() { //기사 이름
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

}
