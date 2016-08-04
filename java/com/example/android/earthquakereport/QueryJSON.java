package com.example.android.earthquakereport;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by 502537203 on 7/23/2016.
 */
public class QueryJSON {

    private static String JSONString;

    /** Public constructor */
    public QueryJSON(String jsonResponse){

        /** */
        if (jsonResponse != null){
            JSONString = jsonResponse;
        }
    }

    public QueryJSON(){
    }

    //EarthquakeAsync earthquakeAsync = new EarthquakeAsync();

    /** Create this method to pull the magnitude, location and date in a ArrayList */
    public static List<ReportWord> getQuakeDetails(){

        List<ReportWord> words = new ArrayList<ReportWord>();

        try{
            JSONObject rootObject = new JSONObject(JSONString);


            JSONArray firstJSONArray = rootObject.getJSONArray("features");

            /** Loop through the Array of JSONObject */
            for(int i = 0; i < firstJSONArray.length(); i++){

                JSONObject featuresJSONObject = firstJSONArray.getJSONObject(i);

                JSONObject propertiesJSONObject = featuresJSONObject.getJSONObject("properties");

                String magnitude = propertiesJSONObject.getString("mag");
                String location = propertiesJSONObject.getString("place");
                long time = propertiesJSONObject.getLong("time");
                String url = propertiesJSONObject.getString("url");

                /*int positionString = location.indexOf("of");

                if(positionString > 0){
                    location = location.substring(0, positionString+2) + "\n" + location.substring(positionString+3);
                }*/

                /** Format the UNIX Time in human readable */
                SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM dd, yyyy : HH:mm a");
                String date = dateFormatter.format(new Date(time));

                Log.v("JSON_DATE", date);

                /*positionString = date.indexOf(":");

                if(positionString > 0){
                    date = date.substring(0, positionString) + "\n" + date.substring(positionString+1);
                }*/

                words.add(new ReportWord(magnitude, location, date, url));
            }

        }
        catch (Exception e){
            Log.v("JSON Exception", "" + e.toString());
        }
        return words;
    }
}
