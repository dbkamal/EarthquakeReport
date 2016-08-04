package com.example.android.earthquakereport;

/**
 * Created by 502537203 on 8/3/2016.
 */

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/** Custom Loader class */
public class EarthquakeLoader extends AsyncTaskLoader<List<ReportWord>> {

    private String today = getDate(0);
    private String yesterday = getDate(-1);

    private String USGS_URL_JSON=
            "http://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime="+yesterday+"&endtime="+today+"&minmag=4&limit=10";
    //"http://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2014-01-01&endtime=2014-01-02";

    StringBuilder stringBuilder = new StringBuilder();

    private QueryJSON classQueryJSON;
    List<ReportWord> words;

    public URL url;

    /** Public Constructor */
    public EarthquakeLoader(Context context){
        super(context);
    }

    /** Once Loader created, this method start the Loader and initiate the Background Job via forceLoad */
    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<ReportWord> loadInBackground() {

        URL url;
        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        String jsonResponse = null;

        /** Create URL object. It throws the MalformedURLException */
        try{
            url = new URL(USGS_URL_JSON);
        }
        catch (MalformedURLException e){
            Log.v("USGS_JSON", "" + e);
            return null;
        }

        /** Perform a HTTP request */
        try{
            /** Check if the url is null or not. If yes, return empty string */
            if (url == null){
                return null;
            }

            httpURLConnection = (HttpURLConnection) url.openConnection();

            //Set Request Type
            httpURLConnection.setRequestMethod("GET");

            //Set Read time-out
            httpURLConnection.setReadTimeout(10000 /* milliseconds */);

            //Set Connect time-out
            httpURLConnection.setConnectTimeout(15000 /* milliseconds */);

            //Connect the URL
            httpURLConnection.connect();

            try{
                if (httpURLConnection.getResponseCode() == 200){

                    /** Returns an input stream (byte) that reads from this open connection */
                    inputStream = httpURLConnection.getInputStream();

                    /** Convert byte stream to character stream using specific charset */
                    inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));

                    /** Reads text from a character-input stream. Buffer character */
                    bufferedReader = new BufferedReader(inputStreamReader);

                    /** Read text from the buffer character and append into String object */
                    jsonResponse = bufferedReader.readLine();

                    /** while loop to append each line into the String object */
                    while (jsonResponse != null){

                        //Append
                        stringBuilder.append(jsonResponse);

                        //Read next line
                        jsonResponse = bufferedReader.readLine();
                    }
                }

            }catch (IOException e){
                Log.e("Earthquake Report", e.toString());
            }

        }
        catch (IOException e){
            Log.v("USGS_JSON_HTTP", "" + e);
            return null;
        }

        finally {
            if (httpURLConnection != null)
                httpURLConnection.disconnect();

            if (inputStream != null){
                try{
                    inputStream.close();
                }
                catch (IOException e){
                    Log.v("InputStream", "" + e);
                }
            }
        }


        classQueryJSON = new QueryJSON(stringBuilder.toString());

        /** Use ArrayList to hold data that needs to be displayed on the UI */
        //ArrayList words = new ArrayList();
        final List<ReportWord> words = classQueryJSON.getQuakeDetails();

        Log.v("TRACK", "Is Empty " + words.isEmpty());

        return words;
    }

    public String getDate(int difference){

        /** Format the UNIX Time in human readable */
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, difference);

        Log.v("Date", dateFormat.format(cal.getTime()));

        return dateFormat.format(cal.getTime());
    }
}
