package com.blackrose.shutsms;

public class MessageModel {
    public String title;
    public String description;
    public String id;
    public String userId;

    public  MessageModel(String title, String description, String id, String userId) {
        this.title = title;
        this.description = description;
        this.id = id;
        this.userId = userId;
    }

    public MessageModel() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
