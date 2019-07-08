package com.example.newsapp;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class News extends RealmObject {
    private String title;
    private String image_url;
    @PrimaryKey
    private String article_url;
    private long timestamp;
    private boolean saved;
    public News(){
    }
    public News(String title,String image_url,String article_url){
        this.title=title;
        this.image_url=image_url;
        this.article_url=article_url;
        this.timestamp=System.currentTimeMillis();
        this.saved=false;
    }
    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArticle_url() {
        return article_url;
    }

    public void setArticle_url(String article_url) {
        this.article_url = article_url;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isSaved() {
        return saved;
    }

    public void setSaved(boolean saved) {
        this.saved = saved;
    }
}
