package csc_380_project.scarlettrails;

import android.text.format.Time;

import java.util.Date;

/**
 * Created by Kheiyasa on 10/23/2014.
 */
public class Forecast implements DatabaseInterface {

    private Date date;
    private double tempMin;
    private double tempMax;
    private Time sunset;
    private Time sunrise;
    private int precip;

    public Forecast(Date date, double tempMin, double tempMax, Time sunrise, Time sunset, int precip){
        this.date = date;
        this.tempMin = tempMin;
        this.tempMax = tempMax;
        this.sunset = sunset;
        this.sunrise = sunrise;
        this.precip = precip;
    }

    public Double getTempMax(){return tempMax;}

    public Double getTempMin(){return tempMin;}

    public int getPrecipitation(){return precip;}

    public Time getSunrise(){return sunrise;}

    public Time getSunset(){return sunset;}

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
