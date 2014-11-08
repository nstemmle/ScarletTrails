package csc_380_project.scarlettrails;

import android.text.format.Time;

import java.util.Date;

/**
 * Created by Kheiyasa on 10/23/2014.
 */
public class Forecast implements DatabaseInterface {

   //swiping on forecast screen for 10 day?
    private Date date;
    private double tempMin;
    private double tempMax;
    private Time sunset;
    private Time sunrise;
    private String precip;
    private String clouds;

    //single day constructor
    public Forecast(double tempMin, double tempMax, Time sunrise, Time sunset, String precip, String clouds){
        this.tempMin = tempMin;
        this.tempMax = tempMax;
        this.sunset = sunset;
        this.sunrise = sunrise;
        this.precip = precip;
        this.clouds = clouds;

    }

    //five day constructor
    public Forecast(Date date, double tempMin, double tempMax, String precip, String clouds){
        this.date = date;
        this.tempMin = tempMin;
        this.tempMax = tempMax;
        this.precip = precip;
        this.clouds = clouds;
    }


    public Date getDate(){return date;}

    public Double getTempMax(){return tempMax;}

    public Double getTempMin(){return tempMin;}

    public String getPrecip(){return precip;}

    public Time getSunrise(){return sunrise;}

    public Time getSunset(){return sunset;}

    public String getClouds(){return clouds;}

    @Override
    public void query(String lookup) {

    }

    @Override
    public void updateData() {

    }

    @Override
    public String insertData() {
        return null;
    }


}
