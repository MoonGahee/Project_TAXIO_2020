package com.example.project_taxio_2020;

public class generalWeatherItem {
    private String weather_day;
    private int weather_image;
    private String weather_name;
    private int weather_wear;

    public generalWeatherItem(String weather_day, int weather_image, String weather_name, int weather_wear) {
        this.weather_day = weather_day;
        this.weather_image = weather_image;
        this.weather_name = weather_name;
        this.weather_wear = weather_wear;
    }

    public String getWeather_day() {
        return weather_day;
    }

    public void setWeather_day(String weather_day) {
        this.weather_day = weather_day;
    }

    public int getWeather_image() {
        return weather_image;
    }

    public void setWeather_image(int weather_image) {
        this.weather_image = weather_image;
    }

    public String getWeather_name() {
        return weather_name;
    }

    public void setWeather_name(String weather_name) {
        this.weather_name = weather_name;
    }

    public int getWeather_wear() {
        return weather_wear;
    }

    public void setWeather_wear(int weather_wear) {
        this.weather_wear = weather_wear;
    }
}