package com.example.newsapp;

public class News {
    private String title;
    private String image_url;

    public News(String title,String image_url){
        this.title=title;
        this.image_url=image_url;
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
}
