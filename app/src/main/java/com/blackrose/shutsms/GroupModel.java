package com.blackrose.shutsms;

import java.util.ArrayList;

public class GroupModel {
    String name;
    String description;
    String image;
    String id;
    String userId;
    ArrayList<String> numbers;

    public GroupModel(String name, String description, String image, String id, ArrayList<String> numbers,String userId) {
        this.name = name;
        this.description = description;
        this.image = image;
        this.id = id;
        this.numbers = numbers;
        this.userId = userId;
    }

    public GroupModel() {
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<String> getNumbers() {
        return numbers;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setNumbers(ArrayList<String> numbers) {
        this.numbers = numbers;
    }

}
