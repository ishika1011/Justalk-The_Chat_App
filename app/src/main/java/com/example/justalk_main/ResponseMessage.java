package com.example.justalk_main;

public class ResponseMessage {
    String textMessage;
    boolean isMe;

    public String getTextMessage(){
        return textMessage;
    }
    public void setTextMessage(String textMessage){
        this.textMessage=textMessage;
    }
    public boolean isMe(){
        return isMe;
    }

    public void setMe(boolean me){
        isMe=me;
    }

}
