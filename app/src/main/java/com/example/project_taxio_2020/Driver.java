package com.example.project_taxio_2020;

// DB 구조체 기사 by 가희 (완)

public class Driver{
    public String driver_email;//기사 이메일
    public String driver_password; //비밀번호
    public String driver_name; //이름
    public String driver_sex; //성별
    public String driver_birth;//생년월일

    public String driver_region; //지역
    public String driver_call; //연락처
    public Float driver_avg_score; //평점
    public String driver_route; //기사 자격증 사진

    public Driver(){
    }

    public Driver(String driver_email, String driver_password, String driver_name, String driver_sex, String driver_birth, String driver_region, String driver_call, Float driver_avg_score, String driver_route){
        this.driver_email = driver_email;
        this.driver_password = driver_password;
        this.driver_name = driver_name;
        this.driver_sex = driver_sex;
        this.driver_birth = driver_birth;
        this.driver_region = driver_region;
        this.driver_call = driver_call;
        this.driver_avg_score = driver_avg_score;
        this.driver_route = driver_route;
    }

    public String getDriver_email() {
        return driver_email;
    }

    public void setDriver_email(String driver_email) {
        this.driver_email = driver_email;
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

    public String getDriver_birth() {
        return driver_birth;
    }

    public void setDriver_birth(String driver_birth) {
        this.driver_birth = driver_birth;
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

    public String getDriver_route() {
        return driver_route;
    }

    public void setDriver_route(String driver_route) {
        this.driver_route = driver_route;
    }
}
