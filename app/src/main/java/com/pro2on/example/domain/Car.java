package com.pro2on.example.domain;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.UUID;

/**
 * Created by pro2on on 02.03.15.
 */
public class Car {

    private static final String JSON_ID = "id";
    private static final String JSON_TITLE = "title";
    private static final String JSON_LIKED = "liked";
    private static final String JSON_DATE = "date";
    private static final String JSON_PHOTO = "photo";


    private UUID id;
    private String title;
    private Date date;
    private boolean liked;
    private Photo mPhoto;

    public Car() {
        id = UUID.randomUUID();
        date = new Date();
    }

    public Car(JSONObject json) throws JSONException {
        id = UUID.fromString(json.getString(JSON_ID));
        title = json.getString(JSON_TITLE);
        liked = json.getBoolean(JSON_LIKED);
        date = new Date(json.getLong(JSON_DATE));
        if (json.has(JSON_PHOTO)) mPhoto = new Photo(json.getJSONObject(JSON_PHOTO));
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isLiked() {
        return liked;
    }
    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(JSON_ID, id.toString());
        json.put(JSON_TITLE, title);
        json.put(JSON_LIKED, liked);
        json.put(JSON_DATE, date.getTime());
        if (mPhoto != null) json.put(JSON_PHOTO, mPhoto.toJSON());
        return json;
    }

    public Photo getPhoto() {
        return mPhoto;
    }

    public void setPhoto(Photo p) {
        mPhoto = p;
    }

    @Override
    public String toString() {
        return title;
    }
}
