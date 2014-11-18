package csc_380_project.scarlettrails;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by Kheiyasa on 10/23/2014.
 */
public class Forecast implements Parcelable {

   //swiping on forecast screen for 10 day?
    private int tempMin;
    private int tempMax;
    private String sunset;
    private String sunrise;
    private int description;

    public Forecast(Parcel in) {
        this.tempMin = in.readInt();
        this.tempMax = in.readInt();
        this.sunrise = in.readString();
        this.sunset = in.readString();
        this.description = in.readInt();
    }

    public Forecast(int tempMin, int tempMax, String sunrise, String sunset, int description){
        this.tempMin = tempMin;
        this.tempMax = tempMax;
        this.sunset = sunset;
        this.sunrise = sunrise;
        this.description = description;

    }

    public String toString() {
        return "Forecast: " + "; tempMin="+tempMin + "; tempMax="+tempMax + "; sunset="+sunset + "; sunrise="+sunrise + "; description="+description;
    }


    public int getTempMax(){return tempMax;}

    public int getTempMin(){return tempMin;}

    public String getSunrise(){return sunrise;}

    public String getSunset(){return sunset;}

    public int getDescription(){return description;}


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(tempMin);
        dest.writeInt(tempMax);
        dest.writeString(sunrise);
        dest.writeString(sunset);
        dest.writeInt(description);
    }
}
