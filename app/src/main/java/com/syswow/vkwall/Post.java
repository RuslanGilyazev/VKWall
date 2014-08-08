package com.syswow.vkwall;

import android.text.format.DateFormat;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Date;

public class Post {
    private int ID, authorID, ownerID;
    private long longDate;
    private String text, date;
    JSONArray profiles;
    String author;

    Post(JSONArray profiles, int ID, int authorID, int ownerID, long date, String text) {
        this.ID = ID;
        this.authorID = authorID;
        this.ownerID = ownerID;
        this.longDate = date;
        this.text = text;
        this.profiles = profiles;

        Log.d("VKWALL", text);
    }

    public int getID() {
        return ID;
    }

    public int getAuthorID() {
        return authorID;
    }

    public String getAuthor() {
        try {
            for (int profilesCount = 0; profilesCount < profiles.length(); profilesCount++) {
                if (authorID == profiles.getJSONObject(profilesCount).getInt("id"))
                    author = profiles.getJSONObject(profilesCount).getString("first_name") +
                            profiles.getJSONObject(profilesCount).getString("last_name");
            }
        }   catch (JSONException e) {
            Log.d("VKWALL", e.toString());
            e.printStackTrace();
        }
        return author;
    }

    public int getOwnerID() {
        return ownerID;
    }

    public String getDate() {
        date = DateFormat
                .format("dd.MM.yy kk:mm",
                        new Date(longDate * 1000)
                ).toString();

        return date;
    }

    public String getText() {
        return text;
    }
}
