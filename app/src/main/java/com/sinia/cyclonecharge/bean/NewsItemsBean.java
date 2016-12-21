package com.sinia.cyclonecharge.bean;

import java.io.Serializable;

/**
 * Created by newLamp on 2016/11/23.
 */

public class NewsItemsBean implements Serializable {
    private static final long serialVersionUID = -4878017309957460910L;
    private String newsId;
    private String title;
    private String time;
    private String path;
    private String content;

    public String getNewsId() {
        return newsId;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
