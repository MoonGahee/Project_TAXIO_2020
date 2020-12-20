package com.example.project_taxio_2020;

// DB 구조체 후기 by 가희 (완)

public class Epilogue {
    public String epilogue_id; //후기 일렬번호
    public String driver_num; //기사 id
    public String general_num; //회원 id
    public String  driver_route; //기사 사진 경로 >> 사진을 띄워야 하니까?
    public Float score; // 별점
    public String review;
    public String image;

    public Epilogue(){
    }

    public Epilogue(String epilogue_id, String driver_num, String general_num, String driver_route, Float score, String review, String image){
        this.epilogue_id = epilogue_id;
        this.driver_num = driver_num;
        this.general_num = general_num;
        this.driver_route = driver_route;
        this.score = score;
        this.review = review;
        this.image = image;
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

    public Float getScore() {
        return score;
    }

    public void setScore(Float score) {
        this.score = score;
    }

    public String getReview() { return review; }

    public void setReview(String review) { this.review = review; }

    public String getDriver_route() { return driver_route; }

    public void setDriver_route(String driver_route) { this.driver_route = driver_route; }

    public String getImage() { return image; }

    public void setImage(String image) { this.image = image; }
}
