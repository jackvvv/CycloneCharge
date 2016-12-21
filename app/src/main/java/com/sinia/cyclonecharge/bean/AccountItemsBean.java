package com.sinia.cyclonecharge.bean;

import java.io.Serializable;

/**
 * Created by newLamp on 2016/11/23.
 */

public class AccountItemsBean implements Serializable {
    private static final long serialVersionUID = -641211968370030646L;
    private String id;
    private String consumeTime;
    private String content;
    private String money;
    private String accStauts;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getConsumeTime() {
        return consumeTime;
    }

    public void setConsumeTime(String consumeTime) {
        this.consumeTime = consumeTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getAccStauts() {
        return accStauts;
    }

    public void setAccStauts(String accStauts) {
        this.accStauts = accStauts;
    }
}
