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
    private int precip;

    private int humidity;
    private int windspeed;
    private double tempCurrent;
    private int clouds;

    //single day constructor
    public Forecast(Date date, double tempMin, double tempMax, Time sunrise, Time sunset, int precip, int clouds, int humidity, double tempCurrent, int windspeed){
        this.date = date;
        this.tempMin = tempMin;
        this.tempMax = tempMax;
        this.sunset = sunset;
        this.sunrise = sunrise;
        this.precip = precip;
        this.clouds = clouds;
        this.humidity = humidity;
        this.tempCurrent = tempCurrent;
        this.windspeed = windspeed;

    }

    //five day constructor
    public Forecast(Date date, double tempMin, double tempMax, int precip, int clouds){
        this.date = date;
        this.tempMin = tempMin;
        this.tempMax = tempMax;
        this.precip = precip;
        this.clouds = clouds;
    }

    //five day constructor
    public Forecast(){
    }

    public Date getDate(){return date;}

    public Double getTempMax(){return tempMax;}

    public Double getTempMin(){return tempMin;}

    public int getPrecipitation(){return precip;}

    public Time getSunrise(){return sunrise;}

    public Time getSunset(){return sunset;}

    public int getHumidity(){return humidity;}

    public int getWindspeed(){return windspeed;}

    public double getTempCurrent(){return tempCurrent;}

    public int getClouds(){return clouds;}

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
