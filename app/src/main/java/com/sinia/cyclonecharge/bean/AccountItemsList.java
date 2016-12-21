package com.sinia.cyclonecharge.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by newLamp on 2016/11/23.
 */

public class AccountItemsList implements Serializable {
    private static final long serialVersionUID = 2826629555885315124L;
    private List<AccountItemsBean> accountItems;

    public List<AccountItemsBean> getAccountItems() {
        return accountItems;
    }

    public void setAccountItems(List<AccountItemsBean> accountItems) {
        this.accountItems = accountItems;
    }
}
