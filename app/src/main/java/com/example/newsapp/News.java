package com.example.newsapp;

import java.io.Serializable;

public class News implements Serializable {
    private String title;
    private String image_url;
    private String article_url;
    public News(String title,String image_url,String article_url){
        this.title=title;
        this.image_url=image_url;
        this.article_url=article_url;
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
}
