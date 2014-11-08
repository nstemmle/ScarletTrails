package csc_380_project.scarlettrails;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Nathan on 10/16/2014.
 */
@SuppressLint("ParcelCreator")
public class CustomLocation implements Parcelable {
    private String locationId;
    private final Double latitude;
    private final Double longitude;
    private String streetAddress;
    private String postalCode;
    private String city;
    private String state;
    private String country; //From the database design picture - is this supposed to be county?

    public CustomLocation(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public CustomLocation(String locationId, Double latitude, Double longitude,
                          String postalCode, String city, String state, String country) {
        this.locationId = locationId;
        this.latitude = latitude;
        this.longitude = longitude;
        //this.streetAddress = streetAddress;
        this.postalCode = postalCode;
        this.city = city;
        this.state = state;
        this.country = country;
    }
    /*
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        if (locationId != 0)
            dest.writeInt(locationId);
        if (streetAddress != null && ! streetAddress.equals(""))
            dest.writeString(streetAddress);
        if (postalCode != 0)
            dest.writeInt(postalCode);
        if (city != null && ! city.equals(""))
            dest.writeString(city);
        if (state != null && ! state.equals(""))
            dest.writeString(state);
        if (country != null && ! country.equals(""))
            dest.writeString(country);
     */

    public CustomLocation(Parcel in) {
        latitude = in.readDouble();
        longitude = in.readDouble();
        if (in.dataAvail() > 0) {
            locationId = in.readString();
            streetAddress = in.readString();
            postalCode = in.readString();
            city = in.readString();
            state = in.readString();
            country = in.readString();
        }
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public LatLng getLatLng() {
        return new LatLng(latitude, longitude);
    }

    public String getCity() {
        return city;
    }

    private void setAddressStrings(String[] addressArray) {
        //addressArray[] indices
        /*addressArray[0] = streetAddress || null;
        addressArray[1] = postalCode || null;
        addressArray[2] = city || null;
        addressArray[3] = state || null;
        addressArray[4] = country || null;*/

        if (addressArray[0] != null)
            streetAddress = addressArray[0];
        if (addressArray[1] != null)
            postalCode = addressArray[1];
        if (addressArray[2] != null)
            city = addressArray[2];
        if (addressArray[3] != null)
            state = addressArray[3];
        if (addressArray[4] != null)
            country = addressArray[4];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    /*
    private String locationId;
    private final Double latitude;
    private final Double longitude;
    private String streetAddress;
    private int postalCode;
    private String city;
    private String state;
    private String country;
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        if (!locationId.isEmpty()) {
            dest.writeString(locationId);
            if (streetAddress != null)
                dest.writeString(streetAddress);
            if (postalCode != null)
                dest.writeString(postalCode);
            if (city != null)
                dest.writeString(city);
            if (state != null)
                dest.writeString(state);
            if (country != null)
                dest.writeString(country);
        }
    }
}