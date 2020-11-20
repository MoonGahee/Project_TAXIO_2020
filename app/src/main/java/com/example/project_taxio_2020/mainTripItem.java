package com.example.project_taxio_2020;

public class mainTripItem {
    String dates;
    String lists;

    public mainTripItem(String dates, String lists) {
        this.dates = dates;
        this.lists = lists;
    }

    public String getDates() {
        return dates;
    }

    public void setDates(String dates) {
        this.dates = dates;
    }

    public String getLists() {
        return lists;
    }

    public void setLists(String lists) {
        this.lists = lists;
    }
}
