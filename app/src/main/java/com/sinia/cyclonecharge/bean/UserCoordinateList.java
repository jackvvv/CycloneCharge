package com.sinia.cyclonecharge.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by newLamp on 2016/11/25.
 */

public class UserCoordinateList implements Serializable {
    private static final long serialVersionUID = 537743907138597828L;
    private List<ChargeStationItemBean> chargeStationItem;

    //预约成功的充电桩
    private String eqId;
    private String scanTime;
    private String eqName;
    private String eqnum;
    private String distance;
    private double longitude;
    private double latitude;

    public List<ChargeStationItemBean> getChargeStationItem() {
        return chargeStationItem;
    }

    public void setChargeStationItem(List<ChargeStationItemBean> chargeStationItem) {
        this.chargeStationItem = chargeStationItem;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getEqnum() {
        return eqnum;
    }

    public void setEqnum(String eqnum) {
        this.eqnum = eqnum;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
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

    public String getEqId() {
        return eqId;
    }

    public void setEqId(String eqId) {
        this.eqId = eqId;
    }

    public String getScanTime() {
        return scanTime;
    }

    public void setScanTime(String scanTime) {
        this.scanTime = scanTime;
    }

    public String getEqName() {
        return eqName;
    }

    public void setEqName(String eqName) {
        this.eqName = eqName;
    }
}
