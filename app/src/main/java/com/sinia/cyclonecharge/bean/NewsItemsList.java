package com.sinia.cyclonecharge.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by newLamp on 2016/11/23.
 */

public class NewsItemsList implements Serializable {
    private static final long serialVersionUID = -1963932828556122988L;
    private List<NewsItemsBean> newsItems;

    public List<NewsItemsBean> getNewsItems() {
        return newsItems;
    }

    public void setNewsItems(List<NewsItemsBean> newsItems) {
        this.newsItems = newsItems;
    }
}
