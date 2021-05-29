package com.example.justalk_main.models;

public class ModelChatlist {

    String id,image,name,onlineStatus;

    public ModelChatlist(String id, String image, String name, String onlineStatus) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.onlineStatus = onlineStatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(String onlineStatus) {
        this.onlineStatus = onlineStatus;
    }
}
