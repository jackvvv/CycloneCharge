package com.sinia.cyclonecharge.bean;

import java.io.Serializable;

/**
 * Created by newLamp on 2016/11/22.
 */

public class PersonDetailBean implements Serializable {
    private static final long serialVersionUID = -7911091316118236162L;
    private String phone;
    private String sex;
    private String email;
    private String address;
    private String nickname;
    private String imageurl;
    private String balance;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }
}
