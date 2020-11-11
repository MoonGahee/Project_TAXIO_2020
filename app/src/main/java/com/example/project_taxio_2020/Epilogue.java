package com.example.project_taxio_2020;

// DB 구조체 후기 by 가희 (완)

public class Epilogue {
    public String epilogue_id; //후기 일렬번호
    public String driver_num; //기사 id
    public String general_num; //회원 id
    public String general_route; //회원 사진 경로 >> 사진을 띄워야 하니까?
    public Float score; // 별점
    public String memo; //후기 내용

    public Epilogue(){
    }

    public Epilogue(String epilogue_id, String driver_num, String general_num, String general_route, Float score, String memo){
        this.epilogue_id = epilogue_id;
        this.driver_num = driver_num;
        this.general_num = general_num;
        this.general_route = general_route;
        this.score = score;
        this.memo = memo;
    }

    public String getEpilogue_id() {
        return epilogue_id;
    }

    public void setEpilogue_id(String epilogue_id) {
        this.epilogue_id = epilogue_id;
    }

    public String getDriver_num() {
        return driver_num;
    }

    public void setDriver_num(String driver_num) {
        this.driver_num = driver_num;
    }

    public String getGeneral_num() {
        return general_num;
    }

    public void setGeneral_num(String general_num) {
        this.general_num = general_num;
    }

    public String getGeneral_route() {
        return general_route;
    }

    public void setGeneral_route(String general_route) {
        this.general_route = general_route;
    }

    public Float getScore() {
        return score;
    }

    public void setScore(Float score) {
        this.score = score;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
