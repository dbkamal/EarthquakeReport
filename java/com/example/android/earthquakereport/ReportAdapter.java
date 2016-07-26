package com.example.android.earthquakereport;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by 502537203 on 7/21/2016.
 */
public class ReportAdapter extends ArrayAdapter<ReportWord> {

    ReportWord reportWord;
    int colorForMagnitude;

    /** Create public constructor */
    public ReportAdapter(Context context, ArrayList<ReportWord> reports){
        super(context, 0, reports);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        /** Get the data item for this position */
        reportWord = getItem(position);

        /** Check if an existing view is being reused, otherwise inflate the view */
        if (convertView == null){
            convertView =
                    LayoutInflater.from(getContext()).inflate(R.layout.earthquake_report, parent, false);
        }

        /** Lookup view for data population */
        TextView magnitude = (TextView) convertView.findViewById(R.id.textMagnitude);
        TextView location_offset = (TextView) convertView.findViewById(R.id.location_offset);
        TextView primary_location = (TextView) convertView.findViewById(R.id.primary_location);
        TextView date = (TextView) convertView.findViewById(R.id.date);
        TextView time = (TextView) convertView.findViewById(R.id.time);

        /** Get the proper background shape for magnitude */
        GradientDrawable magnitudeColor = (GradientDrawable) magnitude.getBackground();

        /** Check the proper background color for Magnitude */
        int magnitudeColorVal = getMagnitudeColor();
        Log.v("Color Code: ", "" + magnitudeColorVal);

        /** Set background color for circle */
        //magnitudeColor.setColor(magnitudeColorVal);
        magnitudeColor.setColor(ContextCompat.getColor(getContext(), magnitudeColorVal));

        /** Split Location */
        int positionLocation = reportWord.getLocation().indexOf("of");

        /** Set Text for the TextView */
        magnitude.setText(reportWord.getMagnitude());

        if (positionLocation > 0) {
            location_offset.setText(reportWord.getLocation().substring(0, positionLocation+2));
            primary_location.setText(reportWord.getLocation().substring(positionLocation+3));
        }
        else {
            location_offset.setText("");
            primary_location.setText(reportWord.getLocation());
        }


        positionLocation = reportWord.getDate().indexOf(":");
        date.setText(reportWord.getDate().substring(0, positionLocation));
        time.setText(reportWord.getDate().substring(positionLocation+3));

        return convertView;
    }

    public int getMagnitudeColor(){
        double colorVal = Double.parseDouble(reportWord.getMagnitude());

        int colorValInteger = (int) Math.floor(colorVal);

        switch (colorValInteger) {
            case 0:
                break;
            case 1:
                colorForMagnitude = R.color.magnitude1;
                break;
            case 2:
                colorForMagnitude = R.color.magnitude2;
                break;
            case 3:
                colorForMagnitude = R.color.magnitude3;
                break;
            case 4:
                colorForMagnitude = R.color.magnitude4;
                break;
            case 5:
                colorForMagnitude = R.color.magnitude5;
                break;
            case 6:
                colorForMagnitude = R.color.magnitude6;
                break;
            case 7:
                colorForMagnitude = R.color.magnitude7;
                break;
            case 8:
                colorForMagnitude = R.color.magnitude8;
                break;
            case 9:
                colorForMagnitude = R.color.magnitude9;
                break;
            default:
                colorForMagnitude = R.color.magnitude10plus;
                break;
        }
        return colorForMagnitude;
    }
}
