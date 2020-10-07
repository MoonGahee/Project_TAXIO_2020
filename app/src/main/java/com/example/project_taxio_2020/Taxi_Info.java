package com.example.project_taxio_2020;

public class Taxi_Info {
    public String car_number;
    public String seater;
    public String trunk;

    public Taxi_Info(){
    }

    public Taxi_Info(String car_number, String seater, String trunk){
        this.car_number = car_number;
        this.seater = seater;
        this.trunk = trunk;
    }
}


//fk.driver_id