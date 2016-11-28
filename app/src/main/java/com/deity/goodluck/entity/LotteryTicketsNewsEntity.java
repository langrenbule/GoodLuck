package com.deity.goodluck.entity;

/**
 * Created by Deity on 2016/11/4.
 */

public class LotteryTicketsNewsEntity {
    /**标题*/
    private String newsTitle;
    /**新闻内容*/
    private String newsDescription;
    /**图片地址*/
    private String newsImageUrl;
    /**文章地址*/
    private String articleUrl;
    /**日期*/
    private String newsDate;

    public String getNewsTitle() {
        return newsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }

    public String getNewsDescription() {
        return newsDescription;
    }

    public void setNewsDescription(String newsDescription) {
        this.newsDescription = newsDescription;
    }

    public String getNewsImageUrl() {
        return newsImageUrl;
    }

    public void setNewsImageUrl(String newsImageUrl) {
        this.newsImageUrl = newsImageUrl;
    }

    public String getNewsDate() {
        return newsDate;
    }

    public void setNewsDate(String newsDate) {
        this.newsDate = newsDate;
    }

    public String getArticleUrl() {
        return articleUrl;
    }

    public void setArticleUrl(String articleUrl) {
        this.articleUrl = articleUrl;
    }
}
