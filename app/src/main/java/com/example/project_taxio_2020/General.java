package com.example.project_taxio_2020;

import androidx.annotation.NonNull;

public class General {
    public String general_id;
    public String general_password;
    public String general_name;
    public String general_sex;
    public String general_birth;
    public String general_call;
    public String general_email;
    public String parent_call;
    public String general_route;

    public General() {
    }

    public General(String general_id, String general_password, String general_name, String general_sex, String general_birth, String general_call, String general_email, String parent_call, String general_route) {
        this.general_id = general_id;
        this.general_password = general_password;
        this.general_name = general_name;
        this.general_sex = general_sex;
        this.general_birth = general_birth;
        this.general_call = general_call;
        this.general_email = general_email;
        this.parent_call = parent_call;
        this.general_route = general_route;
    }

    public String getGeneral_id() {
        return general_id;
    }

    public void setGeneral_id(String general_id) {
        this.general_id = general_id;
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
