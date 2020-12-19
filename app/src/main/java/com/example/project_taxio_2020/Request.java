package com.example.project_taxio_2020;

public class Request {

        public String days;
        public String general_name;
        public String state;
        public String request_num;

        public Request() {
        }

        public Request(String days, String general_name, String state, String request_num) {
            this.days = days;
            this.general_name = general_name;
            this.state = state;
            this.request_num = request_num;
        }

    public String getRequest_num() {
        return request_num;
    }

    public void setRequest_num(String request_num) {
        this.request_num = request_num;
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

        public String getState() {
        return state;
    }

        public void setState(String state) {
        this.state = state;
    }



}
