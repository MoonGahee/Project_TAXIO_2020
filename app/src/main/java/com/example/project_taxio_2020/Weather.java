package com.example.project_taxio_2020;

public class Weather {
    public String weather_date;
    public String region;
    public String temperature;
    public String top;
    public String bottom;

    public Weather(){
    }

    public Weather(String weather_date, String region, String temperature, String top, String bottom){
        this.weather_date = weather_date;
        this.region = region;
        this.temperature  = temperature;
        this.top = top;
        this.bottom = bottom;
    }
}
