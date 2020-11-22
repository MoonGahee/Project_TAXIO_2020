package com.example.project_taxio_2020;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

// 여행 일정 DB
public class Schedule {
    public String schedule_num; //회원의 여행 번호
    public String times; // 며칠 여행 > SDate
    public String region; //여행지역 >SRegion
    public String departure_date = "2000-01-01"; //출발일 >SDate
    public String arrival_date; // 도착일 >SDate
    public String travel_state; // 여행 상태 (여행중, 여행완료, 여행준비)
    public String taxi_driver;

    public Schedule() {
    }

    public Schedule(String schedule_num, String times, String region, String departure_date, String arrival_date, String taxi_driver) {
        this.schedule_num = schedule_num;
        this.times = times;
        this.region = region;
        this.departure_date = departure_date;
        setArrival_date(arrival_date);
        this.taxi_driver = taxi_driver;
    }

    public String getSchedule_num() {
        return schedule_num;
    }

    public void setSchedule_num(String schedule_num) {
        this.schedule_num = schedule_num;
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
        try {
            setTravel_state();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public String getTravel_state() {
        return travel_state;
    }

    public void setTravel_state() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 MM월 dd일");

        Calendar now = Calendar.getInstance();
        Calendar arrivalDate = Calendar.getInstance();
        Calendar departureDate = Calendar.getInstance();

        now.setTime(dateFormat.parse(dateFormat.format(now.getTime())));
        arrivalDate.setTime(dateFormat.parse(arrival_date));
        departureDate.setTime(dateFormat.parse(departure_date));

        int versusDeparture = now.compareTo(departureDate);
        int versusArrival = now.compareTo(arrivalDate);

        if (versusDeparture < 0) {
            Log.d("Moon", "여행준비");
        } else if (versusDeparture > 0 && versusArrival < 0) {
            Log.d("Moon", "여행중");
        } else if (versusArrival == 0) {
            Log.d("Moon", "여행중");
        } else if (versusArrival > 0) {
            Log.d("Moon", "여행완료");
        }
    }


    public String getPrintDate(){return  arrival_date + " - " + departure_date; }

    public String getTaxi_driver() {
        return taxi_driver;
    }

    public void setTaxi_driver(String taxi_driver) {
        this.taxi_driver = taxi_driver;
    }
}
