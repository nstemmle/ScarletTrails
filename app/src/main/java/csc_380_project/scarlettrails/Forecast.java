package csc_380_project.scarlettrails;

import android.text.format.Time;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Kheiyasa on 10/23/2014.
 */
public class Forecast {

   //swiping on forecast screen for 10 day?
    private Calendar date;
    private double tempMin;
    private double tempMax;
    private Time sunset;
    private Time sunrise;
    private int description;

    public Forecast() {}
    //single day constructor
    public Forecast(double tempMin, double tempMax, Time sunrise, Time sunset, int description){
        this.tempMin = tempMin;
        this.tempMax = tempMax;
        this.sunset = sunset;
        this.sunrise = sunrise;
        this.description = description;

    }

    //five day constructor
    public Forecast(Calendar date, double tempMin, double tempMax, int description){
        this.date = date;
        this.tempMin = tempMin;
        this.tempMax = tempMax;
        this.description = description;
    }


    public Calendar getDate(){return date;}

    public Double getTempMax(){return tempMax;}

    public Double getTempMin(){return tempMin;}

    public Time getSunrise(){return sunrise;}

    public Time getSunset(){return sunset;}

    public int getDescription(){return description;}
}
