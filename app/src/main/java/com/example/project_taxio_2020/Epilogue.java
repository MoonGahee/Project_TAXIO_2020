package com.example.project_taxio_2020;

public class Epilogue {
    public String driver_id;
    public String driver_password;
    public String driver_name;
    public String driver_sex;
    public String driver_route;
    public String driver_region;
    public Double grade;
    public String driver_call;
    public String driver_email;

    public Epilogue(){
    }

    public Epilogue(String driver_id, String driver_password, String driver_name, String driver_sex, String driver_route, String driver_region, Double grade, String driver_call, String driver_email){
        this.driver_id = driver_id;
        this.driver_password = driver_password;
        this.driver_name = driver_name;
        this.driver_sex = driver_sex;
        this.driver_route = driver_route;
        this.driver_region = driver_region;
        this.grade = grade;
        this.driver_call = driver_call;
        this.driver_email = driver_email;
    }
}
