package com.example.android.earthquakereport;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /** Use ArrayList to hold data that needs to be displayed on the UI */
        //ArrayList words = new ArrayList();
        final ArrayList<ReportWord> words = QueryJSON.getQuakeDetails();

        /** Add location to the ArrayList of ReportWord Object */
        /*words.add(new ReportWord("7.2", "San Francisco", "Feb 2, 2016"));
        words.add(new ReportWord("6.1", "London", "July 20, 2015"));
        words.add(new ReportWord("3.9", "Tokyo", "Nov 10, 2014"));
        words.add(new ReportWord("5.4", "Mexico City", "May 3, 2014"));
        words.add(new ReportWord("2.8", "Moscow", "Jan 31, 2013"));
        words.add(new ReportWord("4.9", "Rio de Janeiro", "Aug 19, 2012"));
        words.add(new ReportWord("1.6", "Paris", "Oct 30, 2011"));*/

        /** Use Subclass of ArrayAdapter to Hold the Array of arbitrary object */
        ReportAdapter adapter = new ReportAdapter(this, words);

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
