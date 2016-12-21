package com.sinia.cyclonecharge.bean;

import java.util.List;

/**
 * Created by 忧郁的眼神 on 2016/12/12 0012.
 */

public class ChargeStationDetailBean {

    private List<ChargeStationImage> imageItems;
    private String time;//预约时间
    private String eqName;
    private String eqnum;
    private double distance;
    private String eqAddress;
    private String price;
    private String serverPrice;
    private String parkingPrice;
    private double longitude;
    private double latitude;

    private String state;// 新设备 状态0.空闲 1、充电中 2、预约 3.无响应
    private String tag;//老设备 状态0占用 1充电 2预约 3.空闲


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public List<ChargeStationImage> getImageItems() {
        return imageItems;
    }

    public void setImageItems(List<ChargeStationImage> imageItems) {
        this.imageItems = imageItems;
    }

    public String getEqName() {
        return eqName;
    }

    public void setEqName(String eqName) {
        this.eqName = eqName;
    }

    public String getEqnum() {
        return eqnum;
    }

    public void setEqnum(String eqnum) {
        this.eqnum = eqnum;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getEqAddress() {
        return eqAddress;
    }

    public void setEqAddress(String eqAddress) {
        this.eqAddress = eqAddress;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getServerPrice() {
        return serverPrice;
    }

    public void setServerPrice(String serverPrice) {
        this.serverPrice = serverPrice;
    }

    public String getParkingPrice() {
        return parkingPrice;
    }

    public void setParkingPrice(String parkingPrice) {
        this.parkingPrice = parkingPrice;
    }

    public class ChargeStationImage {
        private String imageId;
        private String imageUrl;

        public String getImageId() {
            return imageId;
        }

        public void setImageId(String imageId) {
            this.imageId = imageId;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }
    }
}
