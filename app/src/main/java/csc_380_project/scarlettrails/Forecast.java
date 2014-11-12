package csc_380_project.scarlettrails;


import android.graphics.drawable.Drawable;

/**
 * Created by Kheiyasa on 10/23/2014.
 */
public class Forecast {

   //swiping on forecast screen for 10 day?
    private String date;
    private double tempMin;
    private double tempMax;
    private String sunset;
    private String sunrise;
    private int description;

    public Forecast() {}
    public Forecast(String date, double tempMin, double tempMax, String sunrise, String sunset, int description){
        this.date = date;
        this.tempMin = tempMin;
        this.tempMax = tempMax;
        this.sunset = sunset;
        this.sunrise = sunrise;
        this.description = description;

    }

    public String toString() {
        return "Forecast: " + "date="+date + "; tempMin="+tempMin + "; tempMax="+tempMax + "; sunset="+sunset + "; sunrise="+sunrise + "; description="+description;
    }

    public String getDate(){return date;}

    public Double getTempMax(){return tempMax;}

    public Double getTempMin(){return tempMin;}

    public String getSunrise(){return sunrise;}

    public String getSunset(){return sunset;}

    public int getDescription(){return description;}

    
}
