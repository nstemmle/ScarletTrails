package csc_380_project.scarlettrails;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Nathan on 10/16/2014.
 */
public class Location implements DatabaseInterface {
    private String locationId;
    private final Double latitude;
    private final Double longitude;
    private String address;
    private String postalCode;
    private String city;
    private String state;
    private String country; //From the database design picture - is this supposed to be county?

    public Location(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Location(String locationId, Double latitude, Double longitude, String address,
                    String postalCode, String city, String state, String country) {
        this.locationId = locationId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.postalCode = postalCode;
        this.city = city;
        this.state = state;
        this.country = country;
    }

    public Location(String locationId, Double latitude, Double longitude,
                    String postalCode, String city, String state, String country) {
        this.locationId = locationId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.postalCode = postalCode;
        this.city = city;
        this.state = state;
        this.country = country;
    }

    public Double getLatitude() {
       return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public LatLng createLatLng() {
        return new LatLng(latitude, longitude);
    }

    private void setStrings(String[] addressArray) {
        //addressArray[] indices
        /*addressArray[0] = streetAddress || null;
        addressArray[1] = postalCode || null;
        addressArray[2] = city || null;
        addressArray[3] = state || null;
        addressArray[4] = country || null;*/

        if (addressArray[0] != null)
            address = addressArray[0];
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
