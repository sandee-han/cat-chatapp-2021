package com.example.chatsys;

public class MessageItem {
    String name;
    String message;
    String time;
    String pofileUrl;

    public MessageItem(String name, String message, String time, String pofileUrl){
        this.name = name;
        this.message = message;
        this.time = time;
        this.pofileUrl = pofileUrl;
    }

    public MessageItem(){
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPofileUrl() {
        return pofileUrl;
    }

    public void setPofileUrl(String pofileUrl) {
        this.pofileUrl = pofileUrl;
    }
}
