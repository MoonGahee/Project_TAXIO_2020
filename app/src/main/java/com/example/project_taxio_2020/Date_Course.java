package com.example.project_taxio_2020;

// DB 구조체 하루 코스 by 가희 (완)

public class Date_Course {
    public String general_num; //탑승객 번호
    public String schedule_date; //해당 날짜(하루)
    public Boolean boarding_status; //코스별 탑승 유무
    public String course_order; //코스 번호
    public String between_time; //코스 간의 사이 시간
    public String course_place; //번호에 따른 해당 코스 장소이름

    public Date_Course(){
    }

    public Date_Course(String general_num, String schedule_date, Boolean boarding_status, String course_order, String between_time, String course_place){
        this.general_num = general_num;
        this.schedule_date = schedule_date;
        this.boarding_status = boarding_status;
        this.course_order = course_order;
        this.between_time = between_time;
        this.course_place = course_place;
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

    public String getCourse_order() {
        return course_order;
    }

    public void setCourse_order(String course_order) {
        this.course_order = course_order;
    }

    public String getBetween_time() {
        return between_time;
    }

    public void setBetween_time(String between_time) {
        this.between_time = between_time;
    }

    public String getCourse_place() {
        return course_place;
    }

    public void setCourse_place(String course_place) {
        this.course_place = course_place;
    }
}
