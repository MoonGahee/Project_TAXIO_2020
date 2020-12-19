package com.example.project_taxio_2020;

public class RequestDay {
    public String start_time;
    public String time;
    public String course;
    public String day;

    public RequestDay() {
    }


    public RequestDay(String start_time, String time, String course, String day) {
        this.course = course;
        this.start_time = start_time;
        this.time = time;
        this.day = day;
    }


    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
