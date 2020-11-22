package com.example.project_taxio_2020;

// 기사 선택 구조체 by 가희

public class generalDriverItem {

    private String driverName;
    private String driverInfo;
    private String driverPrice;
    private int driverPhoto;

    public String getDriverName() { //기사 이름
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverInfo() { //기사 정보 3가지
        return driverInfo;
    }

    public void setDriverInfo(String driverInfo) {
        this.driverInfo = driverInfo;
    }

    public String getDirverPrice() { //기사 가격
        return driverPrice;
    }

    public void setDirverPrice(String driverPrice) {
        this.driverPrice = driverPrice;
    }

    public int getDriverPhoto() { //기사 사진
        return driverPhoto;
    }

    public void setDriverPhoto(int driverPhoto) {
        this.driverPhoto = driverPhoto;
    }


}
