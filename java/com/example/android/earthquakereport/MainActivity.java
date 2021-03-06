    package com.example.android.earthquakereport;

    import android.content.AsyncTaskLoader;
    import android.content.Context;
    import android.content.Intent;
    import android.net.Uri;
    import android.os.AsyncTask;
    import android.support.v7.app.AppCompatActivity;
    import android.os.Bundle;
    import android.util.Log;
    import android.view.View;
    import android.widget.AdapterView;
    import android.widget.ArrayAdapter;
    import android.widget.ListView;
    import android.widget.Toast;

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
    import java.util.Date;

    public class MainActivity extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            /** Kick off the network request */
            EarthquakeAsync earthquakeAsync = new EarthquakeAsync();
            earthquakeAsync.execute();
        }

        public class EarthquakeAsync extends AsyncTask<URL, Void, ArrayList<ReportWord>> {

            private String today = getDate(0);
            private String yesterday = getDate(-1);

            private String USGS_URL_JSON=
                    "http://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime="+yesterday+"&endtime="+today+"&minmag=4&limit=10";
                    //"http://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2014-01-01&endtime=2014-01-02";

            StringBuilder stringBuilder = new StringBuilder();

            private QueryJSON classQueryJSON;
            ArrayList<ReportWord> words;

            @Override
            protected ArrayList<ReportWord> doInBackground(URL... urls) {

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
                final ArrayList<ReportWord> words = classQueryJSON.getQuakeDetails();

                return words;
            }

            @Override
            protected void onPostExecute(final ArrayList<ReportWord> words) {
                //super.onPostExecute(s);

/*                classQueryJSON = new QueryJSON(stringBuilder.toString());

                *//** Use ArrayList to hold data that needs to be displayed on the UI *//*
                //ArrayList words = new ArrayList();
                final ArrayList<ReportWord> words = classQueryJSON.getQuakeDetails();*/

                /** Add location to the ArrayList of ReportWord Object */

                /** Use Subclass of ArrayAdapter to Hold the Array of arbitrary object */
                ReportAdapter adapter = new ReportAdapter(getApplicationContext(), words);

                /** Use ListView to display scrollable list */
                ListView listView = (ListView) findViewById(R.id.listView);

                /** Set the adapter that provides the data and the views to represent that data in the widget */
                listView.setAdapter(adapter);

                /** Set the onClickListener so that it will populate details in a web browser after clicking each earthquake report */
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                        /** Get the url details from the position */
                        ReportWord newWord = words.get(position);
                        String url = newWord.getURL();

                        /** Create a new Intent to open a browser when user clicks on the report */
                        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(webIntent);
                    }
                });
            }
        }


        /*public String getDate(long time){
            *//** Format the UNIX Time in human readable *//*
            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-DD");
            String date = dateFormatter.format(new Date(time));

            return date;
        }*/

        public String getDate(int difference){

            /** Format the UNIX Time in human readable */
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, difference);

            Log.v("Date", dateFormat.format(cal.getTime()));

            return dateFormat.format(cal.getTime());
        }

    }
