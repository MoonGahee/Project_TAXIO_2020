package com.example.project_taxio_2020;

public class RequestItem {
    private String days;
    private String general_name;

    public RequestItem(String days, String general_name) {
        this.days = days;
        this.general_name = general_name;
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
}
