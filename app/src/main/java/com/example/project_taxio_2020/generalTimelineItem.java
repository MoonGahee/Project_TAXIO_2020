package com.example.project_taxio_2020;

public class generalTimelineItem {

    private String place;
    private String number;
    private String requied_time;
    private int line;
    private int taxi;

    public generalTimelineItem(String place, String number, String requied_time, int line, int taxi) {
        this.place = place;
        this.number = number;
        this.requied_time = requied_time;
        this.line = line;
        this.taxi = taxi;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public String getRequied_time() {
        return requied_time;
    }

    public void setRequied_time(String requied_time) {
        this.requied_time = requied_time;
    }

    public int getTaxi() {
        return taxi;
    }

    public void setTaxi(int taxi) {
        this.taxi = taxi;
    }
}
