package com.tackable.foobar.criminalintent.app;

import java.util.UUID;

/**
 * Created by stevenwoo on 4/23/14.
 */
public class Crime {
    private UUID mId;
    private String mTitle;
    public Crime(){
        mId = UUID.randomUUID();
    }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }
}

