package csc_380_project.scarlettrails;

import android.text.format.Time;

import java.util.Date;

/**
 * Created by Kheiyasa on 10/23/2014.
 */
public class Forecast {

   //swiping on forecast screen for 10 day?
    private Date date;
    private double tempMin;
    private double tempMax;
    private Time sunset;
    private Time sunrise;
    private String description;

    public Forecast() {}
    //single day constructor
    public Forecast(double tempMin, double tempMax, Time sunrise, Time sunset, String description){
        this.tempMin = tempMin;
        this.tempMax = tempMax;
        this.sunset = sunset;
        this.sunrise = sunrise;
        this.description = description;

    }

    //five day constructor
    public Forecast(Date date, double tempMin, double tempMax, String description){
        this.date = date;
        this.tempMin = tempMin;
        this.tempMax = tempMax;
        this.description = description;
    }


    public Date getDate(){return date;}

    public Double getTempMax(){return tempMax;}

    public Double getTempMin(){return tempMin;}

    public Time getSunrise(){return sunrise;}

    public Time getSunset(){return sunset;}

    public String getDescription(){return description;}
}
