package csc_380_project.scarlettrails;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
/**
 * Created by Nathan on 10/16/2014.
 */
public class CustomLocation implements Parcelable {
    //Constants for distance and unit conversions
    private static final int RADIUS_EARTH_FEET = 20902253;

    public static final int CM_PER_M = 100;
    public static final int M_PER_KM = 1000;
    public static final double CM_PER_INCH = 2.54;
    public static final int INCH_PER_FOOT = 12;
    public static final int FT_PER_MILE = 5280;

    private final String locationId;
    private final String trailId;
    private final double latitude;
    private final double longitude;

    public CustomLocation(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;

        this.locationId = "";
        this.trailId = "";
    }

    public CustomLocation(String trailId, double latitude, double longitude) {
        this.trailId = trailId;
        this.latitude = latitude;
        this.longitude = longitude;

        this.locationId = "";
    }

    public CustomLocation(String trailId, String locationId, double latitude, double longitude) {
        this.trailId = trailId;
        this.locationId = locationId;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getLocationId() {
        return locationId;
    }

    public String getTrailId() {
        return trailId;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public CustomLocation(Parcel in) {
        locationId = in.readString();
        trailId = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(locationId);
        dest.writeString(trailId);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return "LocationId: " + locationId + ", trailId: " + trailId + "; latitude: " + latitude + ", longitude: " + longitude;
    }

    //The formulas/methods taken from the following site:
    //http://www.movable-type.co.uk/scripts/latlong.html
    //Takes GPS coords in complete decimal format (not HH MM SS)
    //Returns distance between coords in Feet
    public static double distanceBetweenCoordsQuick(double latOrigin, double lngOrigin, double latDest, double lngDest) {
        double phi1 = Math.toRadians(latOrigin);
        double phi2 = Math.toRadians(latDest);
        double deltalambda = Math.toRadians(lngDest - lngOrigin);
        return ((Math.acos(Math.sin(phi1)*Math.sin(phi2))) + (Math.cos(phi1) * Math.cos(phi2) * Math.cos(deltalambda))) * RADIUS_EARTH_FEET;
    }

    public static double distanceBetweenCoordsQuick(CustomLocation origin, CustomLocation dest) {
        return distanceBetweenCoordsQuick(origin.latitude, origin.longitude, dest.latitude, dest.longitude);
    }

    public static double distanceBetweenGPSCoords(double latOrigin, double lngOrigin, double latDest, double lngDest) {
        double phi1 = Math.toRadians(latOrigin);
        double phi2 = Math.toRadians(latDest);
        double deltaphi = Math.toRadians(latDest - latOrigin);
        double deltalambda = Math.toRadians(lngDest - lngOrigin);

        double a = Math.sin(deltaphi/2) * Math.sin(deltaphi/2) + Math.cos(phi1) * Math.cos(phi2) * Math.sin(deltalambda/2) * Math.sin(deltalambda/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return RADIUS_EARTH_FEET * c;
    }

    public static double distanceBetweenGPSCoords(CustomLocation origin, CustomLocation dest) {
        return distanceBetweenGPSCoords(origin.latitude, origin.longitude, dest.latitude, dest.longitude);
    }

    public static double calculateTraversalDistance(List<CustomLocation> coords) {
        if (coords.size() == 0) return -1d;
        double distance = 0d;
        CustomLocation last = coords.get(0);
        for (int i = 1; i < coords.size(); i++) {
            distance += distanceBetweenGPSCoords(last, coords.get(i));
            last = coords.get(i);
        }
        return distance;
    }

    public static double convertFeetToMiles(double feet) {
        return feet / FT_PER_MILE;
    }

    public static double convertMilesToFeet(double miles) {
        return miles * FT_PER_MILE;
    }

    public static double convertMetersToFeet(double meters) {
        return ((meters * CM_PER_M) / CM_PER_INCH) / INCH_PER_FOOT;
    }

    public static double convertFeetToMeters(double feet) {
        return (feet * INCH_PER_FOOT * CM_PER_INCH) / CM_PER_M;
    }

    public static double convertMetersToKM(double meters) {
        return meters * M_PER_KM;
    }

    public static double convertKMToMeters(double km) {
        return km / M_PER_KM;
    }
}