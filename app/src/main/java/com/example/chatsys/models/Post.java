package com.example.chatsys.models;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Post {

    private String documentID;
    private String title;
    private String nickname;
    private String contents;
    @ServerTimestamp
    private Date date;


    public Post() {

    }

    public Post(String documentID, String nickname, String title, String contents) {
        this.documentID = documentID;
        this.title = title;
        this.nickname= nickname;
        this.contents = contents;

    }

    public String getDocumentID() {
        return documentID;
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Post{" +
                "documentID='" + documentID + '\'' +
                ", title='" + title + '\'' +
                ", nickname='" + nickname + '\'' +
                ", contents='" + contents + '\'' +
                ", date=" + date +
                '}';
    }
}

