package com.example.project_taxio_2020;

public class generalTaxiItem {
    private String tripDate;

    public generalTaxiItem(String tripDate){
        this.tripDate = tripDate;
    }

    public generalTaxiItem(){}


    public String getTripDate() { //기사 이름
        return tripDate;
    }

    public void setTripDate(String tripDate) {
        this.tripDate = tripDate;
    }

}
