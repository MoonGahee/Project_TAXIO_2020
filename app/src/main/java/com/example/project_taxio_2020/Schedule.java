package com.example.project_taxio_2020;
// 여행 일정 DB
public class Schedule {
    public String general_num; //회원 번호
    public String times; // 며칠 여행
    public String region; //여행지역
    public String departure_date; //출발일
    public String arrival_date; // 도착일
    public String travel_state; // 여행 상태 (여행중, 여행완료, 여행준비)
    public String number; // 탑승객 인원 수

    public Schedule() {
    }

    public Schedule(String general_num, String times, String region, String departure_date, String arrival_date, String travel_state, String number) {
        this.general_num = general_num;
        this.times = times;
        this.region = region;
        this.departure_date = departure_date;
        this.arrival_date = arrival_date;
        this.travel_state = travel_state;
        this.number = number;
    }

    public String getGeneral_num() {
        return general_num;
    }

    public void setGeneral_num(String general_num) {
        this.general_num = general_num;
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
