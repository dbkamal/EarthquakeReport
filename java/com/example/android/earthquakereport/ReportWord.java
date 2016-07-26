package com.example.android.earthquakereport;

/**
 * Created by 502537203 on 7/21/2016.
 */
public class ReportWord {

    /** Private class variable to hold magnitude, location and date from source data */
    private String mMagnitude, mLocation, mDate, mURL;

    /** Create a public constructor */
    public ReportWord(String magnitude, String location, String date, String url){
        mMagnitude = magnitude;
        mLocation = location;
        mDate = date;
        mURL = url;
    }

    /** Getter method to return the value */
    public String getMagnitude(){
        return mMagnitude;
    }

    public String getLocation(){
        return mLocation;
    }

    public String getDate(){
        return mDate;
    }

    public String getURL() { return mURL; }
}
