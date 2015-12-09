package com.alelak.materialup.models;

import java.io.Serializable;

public class Post implements Serializable {

    private static final long serialVersionUID = 1104993515455179916L;
    private int id;
    private String title;
    private String image_url;
    private String preview_url;
    private String url;
    private int upvotes;

    public Post() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getUpvotes() {
        return upvotes;
    }

    public void setUpvotes(int upvotes) {
        this.upvotes = upvotes;
    }

    public String getPreview_url() {
        return preview_url;
    }

    public void setPreview_url(String preview_url) {
        this.preview_url = preview_url;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", image_url='" + image_url + '\'' +
                ", preview_url='" + preview_url + '\'' +
                ", url='" + url + '\'' +
                ", upvotes=" + upvotes +
                '}';
    }
}
