package com.tackable.foobar.criminalintent.app;

import android.app.Activity;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Date;
import java.util.UUID;

/**
 * Created by stevenwoo on 4/23/14.
 */
public class Crime {
    private UUID mId;
    private String mTitle;
    private Date mDate;
    private boolean mSolved;
    private Photo mPhoto;
    private String mSuspect;

    private static final String JSON_ID = "id";
    private static final String JSON_TITLE = "title";
    private static final String JSON_SOLVED = "solved";
    private static final String JSON_DATE = "date";
    private static final String JSON_PHOTO = "photo";
    private static final String JSON_SUSPECT = "suspect";


    public Crime(){
        mId = UUID.randomUUID();
        mDate = new Date();
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(JSON_ID, mId.toString());
        json.put(JSON_TITLE, mTitle);
        json.put(JSON_SOLVED, mSolved);
        json.put(JSON_DATE, mDate.getTime());
        if( mPhoto != null ){
            json.put(JSON_PHOTO, mPhoto.toJSON());
        }
        if( mSuspect != null){
            json.put(JSON_SUSPECT, mSuspect);
        }
        return json;
    }

    public Crime(JSONObject json) throws JSONException {
        mId = UUID.fromString(json.getString(JSON_ID));
        mTitle = json.getString(JSON_TITLE);
        mDate = new Date(json.getLong(JSON_DATE));
        mSolved = json.getBoolean(JSON_SOLVED);
        if(json.has(JSON_PHOTO)){
            mPhoto = new Photo(json.getJSONObject(JSON_PHOTO));
        }
        if(json.has(JSON_SUSPECT)){
            mSuspect = json.getString(JSON_SUSPECT);
        }
    }

    public Photo getPhoto(){
        return mPhoto;
    }

    public void setPhoto(Photo p){
        mPhoto = p;
    }

    public void deletePhoto(Activity activity){
        if( mPhoto != null){
            String path = activity.getFileStreamPath(
                    mPhoto.getFilename()).getAbsolutePath();
            Log.v("Crime", "deleting" + path );
            File f = new File(path);
            f.delete();
            mPhoto = null;
        }
    }

    @Override
    public String toString(){
        return mTitle + "foo";
    }

    public UUID getId() {
        return mId;
    }

    public String getSuspect(){
        return mSuspect;
    }

    public void setSuspect(String suspect){
        mSuspect = suspect;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date mDate) {
        this.mDate = mDate;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean mSolved) {
        this.mSolved = mSolved;
    }


}

