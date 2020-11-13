package com.example.project_taxio_2020;

// DB 구조체 탑승객 by 가희 (완)

public class General {
    public String general_num; //값을 넘겨주기 위한 좌표
    public String general_email; //탑승객 이메일 = 아이디 , 최초로그인 본인 1회 인증 진행
    public String general_password; //탑승객 비번
    public String general_name; //탑승객 이름
    public String general_sex; //탑승객 성별
    public String general_birth; //탑승객 생년월일
    public String general_call; //탑승객 연락처
    public String parent_call; //부모 연락처
    public String general_route; //사진 경로

    public General() {
    }

    public General(String general_num, String general_password, String general_name, String general_sex, String general_birth, String general_call, String general_email, String parent_call, String general_route) {
        this.general_num = general_num;
        this.general_password = general_password;
        this.general_name = general_name;
        this.general_sex = general_sex;
        this.general_birth = general_birth;
        this.general_call = general_call;
        this.general_email = general_email;
        this.parent_call = parent_call;
        this.general_route = general_route;
    }

    public String getGeneral_num() {
        return general_num;
    }

    public void setGeneral_num(String general_num) {
        this.general_num = general_num;
    }

    public String getGeneral_password() {
        return general_password;
    }

    public void setGeneral_password(String general_password) {
        this.general_password = general_password;
    }

    public String getGeneral_name() {
        return general_name;
    }

    public void setGeneral_name(String general_name) {
        this.general_name = general_name;
    }

    public String getGeneral_sex() {
        return general_sex;
    }

    public void setGeneral_sex(String general_sex) {
        this.general_sex = general_sex;
    }

    public String getGeneral_birth() {
        return general_birth;
    }

    public void setGeneral_birth(String general_birth) {
        this.general_birth = general_birth;
    }

    public String getGeneral_call() {
        return general_call;
    }

    public void setGeneral_call(String general_call) {
        this.general_call = general_call;
    }

    public String getGeneral_email() {
        return general_email;
    }

    public void setGeneral_email(String general_email) {
        this.general_email = general_email;
    }

    public String getParent_call() {
        return parent_call;
    }

    public void setParent_call(String parent_call) {
        this.parent_call = parent_call;
    }

    public String getGeneral_route() {
        return general_route;
    }

    public void setGeneral_route(String general_route) {
        this.general_route = general_route;
    }

}
