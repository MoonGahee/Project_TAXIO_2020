package com.example.project_taxio_2020;

// 기사 선택 구조체 by 가희

public class generalDriverItem {

    public String driverEmail;
    private String driverName;
    private String driverInfo;
    private String driverPrice;
    private String driverPhoto;
    private String driverCall;

    public String getDriverEmail() {
        return driverEmail;
    }

    public void setDriverEmail(String driverEmail) {
        this.driverEmail = driverEmail;
    }

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

    public String getDriverPhoto() { //기사 사진
        return driverPhoto;
    }

    public void setDriverPhoto(String driverPhoto) {
        this.driverPhoto = driverPhoto;
    }

    public String getDriverCall() {
        return driverCall;
    }

    public void setDriverCall(String driverCall) {
        this.driverCall = driverCall;
    }
}
