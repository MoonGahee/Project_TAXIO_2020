package com.example.project_taxio_2020;

public class Drvier_Schedule {
    public String course;
    public String days;
    public String general_name;
    public String start_time;
    public String time;

    public Drvier_Schedule() {
    }

    public Drvier_Schedule(String course, String days, String general_name, String start_time, String time) {
        this.course = course;
        this.days = days;
        this.general_name = general_name;
        this.start_time = start_time;
        this.time = time;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getGeneral_name() {
        return general_name;
    }

    public void setGeneral_name(String general_name) {
        this.general_name = general_name;
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
