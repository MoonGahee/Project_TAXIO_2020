package com.example.project_taxio_2020;

public class reservationItem {
    private String recruit;
    private String recruitplace;

    public reservationItem(String recruit, String recruitplace) {
        this.recruit = recruit;
        this.recruitplace = recruitplace;
    }

    public String getRecruit() {
        return recruit;
    }

    public void setRecruit(String recruit) {
        this.recruit = recruit;
    }

    public String getRecruitplace() {
        return recruitplace;
    }

    public void setRecruitplace(String recruitplace) {
        this.recruitplace = recruitplace;
    }
}
