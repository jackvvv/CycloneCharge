package com.sinia.cyclonecharge.bean;

import java.io.Serializable;

/**
 * Created by newLamp on 2016/11/24.
 */

public class ChargeStationItemBean implements Serializable {
    private static final long serialVersionUID = -7446784519605205651L;
    private String eqnum;
    private double distance;
    private double longitude;
    private double latitude;
    private String address;
    private String eqName;
    private String eqId;
    private String statee;// 新设备 状态0.空闲 1、充电中 2、预约 3.无响应
    private String isState;//老设备 状态0占用 1充电 2预约 3.空闲

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

    public String getStatee() {
        return statee;
    }

    public void setStatee(String statee) {
        this.statee = statee;
    }

    public String getIsState() {
        return isState;
    }

    public void setIsState(String isState) {
        this.isState = isState;
    }

    public String getEqId() {
        return eqId;
    }

    public void setEqId(String eqId) {
        this.eqId = eqId;
    }

    public String getEqName() {
        return eqName;
    }

    public void setEqName(String eqName) {
        this.eqName = eqName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
}
