package com.example.project_taxio_2020;

// 후기 구조체 by 관우

public class generalEpilogueItem {

    private String image;
    private String driver;
    private float rating;

    public generalEpilogueItem(String image, String driver, float rating) {
        this.image = image;
        this.driver = driver;
        this.rating = rating;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

}
