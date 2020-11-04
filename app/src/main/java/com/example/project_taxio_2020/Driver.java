package com.example.project_taxio_2020;

public class Driver{
    public String driver_id;
    public String driver_password;
    public String driver_name;
    public String driver_sex;
    public String driver_region;
    public String driver_call;
    public Float driver_avg_score;

    public Driver(){
    }

    public Driver(String driver_id, String driver_password, String driver_name, String driver_sex, String driver_region, String driver_call, Float driver_avg_score){
        this.driver_id = driver_id;
        this.driver_password = driver_password;
        this.driver_name = driver_name;
        this.driver_sex = driver_sex;
        this.driver_region = driver_region;
        this.driver_call = driver_call;
        this.driver_avg_score = driver_avg_score;
    }

    public String getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(String driver_id) {
        this.driver_id = driver_id;
    }

    public String getDriver_password() {
        return driver_password;
    }

    public void setDriver_password(String driver_password) {
        this.driver_password = driver_password;
    }

    public String getDriver_name() {
        return driver_name;
    }

    public void setDriver_name(String driver_name) {
        this.driver_name = driver_name;
    }

    public String getDriver_sex() {
        return driver_sex;
    }

    public void setDriver_sex(String driver_sex) {
        this.driver_sex = driver_sex;
    }

    public String getDriver_region() {
        return driver_region;
    }

    public void setDriver_region(String driver_region) {
        this.driver_region = driver_region;
    }

    public String getDriver_call() {
        return driver_call;
    }

    public void setDriver_call(String driver_call) {
        this.driver_call = driver_call;
    }

    public Float getDriver_avg_score() {
        return driver_avg_score;
    }

    public void setDriver_avg_score(Float driver_avg_score) {
        this.driver_avg_score = driver_avg_score;
    }
}
