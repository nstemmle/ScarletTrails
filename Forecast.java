package csc_380_project.scarlettrails;


import android.text.format.Time;

import java.util.Date;

/**
 * Created by Kheiyasa on 10/23/2014.
 */
public class Forecast {
  // skeleton
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
    }

    public void refresh(){

    }

    public String displayForecast(){
     return null;
    }

}
