    package com.example.android.earthquakereport;

    import android.app.LoaderManager.LoaderCallbacks;
    import android.content.AsyncTaskLoader;
    import android.content.Context;
    import android.content.Intent;
    import android.net.ConnectivityManager;
    import android.net.NetworkInfo;
    import android.net.Uri;
    import android.os.AsyncTask;
    import android.app.LoaderManager;
    import android.content.Loader;
    import android.support.v7.app.AppCompatActivity;
    import android.os.Bundle;
    import android.util.Log;
    import android.view.View;
    import android.widget.AdapterView;
    import android.widget.ArrayAdapter;
    import android.widget.ListView;
    import android.widget.ProgressBar;
    import android.widget.TextView;
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
    import java.util.List;

    /** When Android App Launches, this class is being called.
     * This implement LoaderManager to handle background task while Users interacting with
     * Main UI Threads
     */

    public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<ReportWord>>{

        ReportAdapter adapter;
        ListView listView;
        TextView emptyView;
        ProgressBar progressBar;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            /** Use Subclass of ArrayAdapter to Hold the Array of arbitrary object */
            adapter = new ReportAdapter(this, new ArrayList<ReportWord>());

            /** Use ListView to display scrollable list */
            listView = (ListView) findViewById(R.id.listView);

            /** Use empty View to display blank screen in case Adapter is empty */
            emptyView = (TextView) findViewById(R.id.emptyView);
            listView.setEmptyView(emptyView);

            /** Set the progress bar until Loader is finished */
            progressBar = (ProgressBar) findViewById(R.id.progressBar);
            progressBar.setVisibility(View.VISIBLE);

            /** Set the adapter that provides the data and the views to represent that data in the widget
             * Initially Adapter do not have any data. Once Loader is executed, Adapter will update the UI with the new data
             */
            listView.setAdapter(adapter);

            /** Set the onClickListener so that it will populate details in a web browser after clicking each earthquake report */
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                    /** Get the url details from the position */
                    ReportWord newWord = adapter.getItem(position);
                    String url = newWord.getURL();

                    /** Create a new Intent to open a browser when user clicks on the report */
                    Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

                    //Verify that the Intent will resolve to an Activity
                    if (webIntent.resolveActivity(getPackageManager()) != null)
                        startActivity(webIntent);
                }
            });

            /** Pull the corrent state of Network connectivity */
            ConnectivityManager cntManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

            /** Describe the status of the Network. Calling getActiveNetworkInfo
             * will return details about the currently active default data network
             */
            NetworkInfo networkInfo = cntManager.getActiveNetworkInfo();

            /** If Network is available, Call the Loader Manager. Otherwise, No Network Connection. */
            if (networkInfo != null && networkInfo.isConnected()){
                /** Prepare the loader. Either re-connect with an existing one, or start a new one. */
                getLoaderManager().initLoader(0, null, this);
            }
            else {
                /** Set the progress Bar invisible once Loader is finished */
                progressBar.setVisibility(View.GONE);

                //Set empty view to display No Network Connection
                emptyView.setText("No Network Connection. Try Later");
            }
        }

        /** If Loaded doesn't exist then this method will create a custom Loader object */

        @Override
        public Loader<List<ReportWord>> onCreateLoader(int id, Bundle args) {
            return new EarthquakeLoader(this);
        }

        /** This method get called when Loaded is finished loading it's background job */

        @Override
        public void onLoadFinished(Loader<List<ReportWord>> loader, List<ReportWord> data) {

            /** Set the progress Bar invisible once Loader is finished */
            progressBar.setVisibility(View.GONE);

            //Set empty view to display No Earthquake Found
            emptyView.setText("No Earthquake Found");

            //Check if Loader is fetched any data
            if(data != null && !data.isEmpty())
                adapter.addAll(data);
        }

        @Override
        public void onLoaderReset(Loader<List<ReportWord>> loader) {
            adapter.clear();
        }
}
