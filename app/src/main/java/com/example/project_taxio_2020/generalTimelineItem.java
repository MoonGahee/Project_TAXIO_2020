package com.example.project_taxio_2020;

public class generalTimelineItem {

    private String place;
    private String number;
    private String requied_time;

    public generalTimelineItem(String place, String number, String requied_time) {
        this.place = place;
        this.number = number;
        this.requied_time = requied_time;
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

    public String getRequied_time() {
        return requied_time;
    }

    public void setRequied_time(String requied_time) {
        this.requied_time = requied_time;
    }
}
