package com.sinia.cyclonecharge.bean;

import java.io.Serializable;

/**
 * Created by newLamp on 2016/11/24.
 */

public class TelectricpileItemBean implements Serializable {
    private static final long serialVersionUID = -2099305872525171686L;
    private String eqId;
    private String eqName;
    private String address;
    private String pileQRCode;
    private String coordinate;
    private double distance;

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

    public String getPileQRCode() {
        return pileQRCode;
    }

    public void setPileQRCode(String pileQRCode) {
        this.pileQRCode = pileQRCode;
    }

    public String getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(String coordinate) {
        this.coordinate = coordinate;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
