package com.syswow.vkwall;

import android.util.Log;

public class Post {
    private int ID, authorID, ownerID;
    private long date;
    private String text;

    Post(int ID, int authorID, int ownerID, long date, String text) {
        this.ID = ID;
        this.authorID = authorID;
        this.ownerID = ownerID;
        this.date = date;
        this.text = text;

        Log.d("VKWALL", text);
    }

    public int getID() {
        return ID;
    }

    public int getAuthorID() {
        return authorID;
    }

    public int getOwnerID() {
        return ownerID;
    }

    public long getDate() {
        return date;
    }

    public String getText() {
        return text;
    }
}
