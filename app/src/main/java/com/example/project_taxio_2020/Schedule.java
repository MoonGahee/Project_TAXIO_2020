package com.example.project_taxio_2020;

public class Schedule {
    public String general_id; //회원 id
    public String times; // 며칠 여행
    public String region; //여행지역
    public String departure_date; //출발일
    public String arrival_date; // 도착일
    public String travel_state; // 여행 상태 (여행중, 여행완료, 여행준비)
    public String number; // 탑승객의 몇 번째 여행인지

    public Schedule() {
    }

    public Schedule(String general_id, String times, String region, String departure_date, String arrival_date, String travel_state, String number) {
        this.general_id = general_id;
        this.times = times;
        this.region = region;
        this.departure_date = departure_date;
        this.arrival_date = arrival_date;
        this.travel_state = travel_state;
        this.number = number;
    }

    public String getGeneral_id() {
        return general_id;
    }

    public void setGeneral_id(String general_id) {
        this.general_id = general_id;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getDeparture_date() {
        return departure_date;
    }

    public void setDeparture_date(String departure_date) {
        this.departure_date = departure_date;
    }

    public String getArrival_date() {
        return arrival_date;
    }

    public void setArrival_date(String arrival_date) {
        this.arrival_date = arrival_date;
    }

    public String getTravel_state() {
        return travel_state;
    }

    public void setTravel_state(String travel_state) {
        this.travel_state = travel_state;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
