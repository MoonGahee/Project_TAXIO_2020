package com.example.project_taxio_2020;

public class Request {

        public String departure;
        public String arrival;
        public String general_name;
        public String state;
        public String request_num;
        public String date;

        public Request() {
        }

        public Request(String date, String departure,String arrival, String general_name, String state, String request_num) {
            this.date = date;
            this.departure = departure;
            this.arrival =arrival;
            this.general_name = general_name;
            this.state = state;
            this.request_num = request_num;
        }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRequest_num() {
        return request_num;
    }

    public void setRequest_num(String request_num) {
        this.request_num = request_num;
    }


    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getArrival() {
        return arrival;
    }

    public void setArrival(String arrival) {
        this.arrival = arrival;
    }

    public String getGeneral_name() {
            return general_name;
        }

        public void setGeneral_name(String general_name) {
            this.general_name = general_name;
        }

        public String getState() {
        return state;
    }

        public void setState(String state) {
        this.state = state;
    }



}
