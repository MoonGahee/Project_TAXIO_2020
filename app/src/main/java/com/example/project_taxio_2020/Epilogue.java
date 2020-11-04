package com.example.project_taxio_2020;

public class Epilogue {
    public String epilogue_id; //후기 일렬번호
    public String driver_id; //기사 id
    public String general_id; //회원id
    public Float score; // 별점
    public String memo; //후기 내용

    public Epilogue(){
    }

    public Epilogue(String epilogue_id, String driver_id, String general_id, Float score, String memo){
        this.epilogue_id = epilogue_id;
        this.driver_id = driver_id;
        this.general_id = general_id;
        this.score = score;
        this.memo = memo;
    }

    public String getEpilogue_id() {
        return epilogue_id;
    }

    public void setEpilogue_id(String epilogue_id) {
        this.epilogue_id = epilogue_id;
    }

    public String getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(String driver_id) {
        this.driver_id = driver_id;
    }

    public String getGeneral_id() {
        return general_id;
    }

    public void setGeneral_id(String general_id) {
        this.general_id = general_id;
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
