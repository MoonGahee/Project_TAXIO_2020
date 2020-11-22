package com.example.project_taxio_2020;

// DB 구조체 하루 택시 예약 일정 by 가희 (완)

public class Date_Schedule {
    public String schedule_num;
    public String general_num; //탑승객 번호
    public String schedule_date; //해당 날짜(하루)
    public Boolean boarding_status; //택시 탑승 유무
    public String taxi_time; //탑승 이용 시간
    public String start_time; //탑승 시작 시간

    public Date_Schedule() {
    }

    public Date_Schedule(String schedule_num, String general_num, String schedule_date, Boolean boarding_status, String taxi_time, String start_time) {
        this.schedule_num = schedule_num;
        this.general_num = general_num;
        this.schedule_date = schedule_date;
        this.boarding_status = boarding_status;
        this.taxi_time = taxi_time;
        this.start_time = start_time;
    }

    public String getSchedule_num() {
        return schedule_num;
    }

    public void setSchedule_num(String schedule_num) {
        this.schedule_num = schedule_num;
    }

    public String getGeneral_num() {
        return general_num;
    }

    public void setGeneral_num(String general_num) {
        this.general_num = general_num;
    }

    public String getSchedule_date() {
        return schedule_date;
    }

    public void setSchedule_date(String schedule_date) {
        this.schedule_date = schedule_date;
    }

    public Boolean getBoarding_status() {
        return boarding_status;
    }

    public void setBoarding_status(Boolean boarding_status) {
        this.boarding_status = boarding_status;
    }

    public String getTaxi_time() {
        return taxi_time;
    }

    public void setTaxi_time(String taxi_time) {
        if (taxi_time == null) {
            this.taxi_time = "";
        } else {
            this.taxi_time = "("+taxi_time+")";
        }
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        if (start_time == null) {
            this.start_time = "";
        } else {
            this.start_time = start_time;
        }
    }

    public String getPrintText()
    {
        return schedule_date+" "+start_time+" "+taxi_time;
    }

}
