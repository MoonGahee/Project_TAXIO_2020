package com.example.project_taxio_2020;

public class Request {

        public String days;
        public String general_name;
        public String state;

        public Request() {
        }

        public Request(String days, String general_name, String state) {
            this.days = days;
            this.general_name = general_name;
            this.state = state;
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
