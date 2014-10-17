package csc_380_project.scarlettrails;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

/*Relevant API and Example pages

https://developers.google.com/maps/documentation/android/start
https://developers.google.com/maps/documentation/android/map
https://developers.google.com/maps/documentation/android/views#setting_boundaries

*/

public class LocationWrapper {

    //CONSTANTS
    //Oswego County LatLng pair
    private static final LatLng OSWEGO_COUNTY = new LatLng(43.482533, -76.1783739);

    //Zoom constants - can pass these in to any method which requires a zoom to get the corresponding zoom level
    // 1.0f corresponds to displaying the entire world in the map frame
    // 21.0f corresponds to displaying an incredibly zoomed in location
    private static final float STATE_ZOOM = 6.0f;
    private static final float COUNTY_ZOOM = 8.0f;
    private static final float CITY_ZOOM = 12.0f; //Might need to be adjusted
    private static final float STREET_ZOOM = 14.0f;
    private static final float OBJECT_ZOOM = 17.0f; //Might need to be adjusted

    private static LocationWrapper instance;

    private LocationManager mLocationManager;

    private LocationWrapper() {}

    public static LocationWrapper getInstance() {
        if (instance == null) {
            instance = new LocationWrapper();
        }
        return instance;
    }

    public boolean checkMapExists(GoogleMap map) {
        if (map == null) {
            throw new NoMapException("There is no map - no map functionality can be accessed. Please call setGoogleMap(GoogleMap map) in your activity passing the current map object. See the new method");
            //return false - this option should not be used but you can change functionality if you need to for testing
        }
        return true;
    }

    public void beginListeningForLocationUpdates() {

    }

    public void checkLocationSettingsEnabled () {

    }

    public Location getCurrentLocation() {
        checkLocationSettingsEnabled();
        return mLocationManager.getLastKnownLocation("passive");
    }

    //Returns distance from start to destination
    public float getDistanceTo(Location origin, Location destination) {
        return origin.distanceTo(destination);
    }

    public void displayDirectionsOnMap(Location origin, Location destination, GoogleMap map) {

    }

    public void setUpMapWithDefaults(GoogleMap mMap) {
        checkMapExists(mMap);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(OSWEGO_COUNTY,COUNTY_ZOOM));
    }

    //Move the camera view of the google map to a certain Location
    public void moveCamera(GoogleMap mMap, Location l) {
        checkMapExists(mMap);
        LatLng tempLatLng = new LatLng(l.getLatitude(),l.getLongitude());
        mMap.animateCamera(CameraUpdateFactory.newLatLng(tempLatLng));
    }

    //Move the camera view of the google map to a certain Location
    //Call this method when the change in distance is small i.e. from one part of Oswego county to another
    public void moveCameraAnimated(GoogleMap mMap, Location l) {
        checkMapExists(mMap);
        LatLng tempLatLng = new LatLng(l.getLatitude(),l.getLongitude());
        //A duration value can also be passed to determine how long the animation should take; see android documentation
        mMap.animateCamera(CameraUpdateFactory.newLatLng(tempLatLng));
    }

    //Move the camera view of the google map to a certain Location at a specified Zoom level
    //Call this method when the change in distance is small i.e. from one part of Oswego county to another
    //Specify a zoom level to be used - static constants have been declared for your use
    public void moveCameraAnimatedZoom(GoogleMap mMap, Location l, float f) {
        checkMapExists(mMap);
        LatLng tempLatLng = new LatLng(l.getLatitude(),l.getLongitude());
        //A duration value can also be passed to determine how long the animation should take; see android documentation
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(tempLatLng, f));
    }

    //Relevant examples/documentation
    //https://developers.google.com/maps/documentation/android/marker
    public void addMarker(GoogleMap mMap, Location l, String title) {
        checkMapExists(mMap);
        LatLng currentMarkerLatLng = new LatLng(l.getLatitude(), l.getLongitude());
        if (!title.equals("") || title == null)
            mMap.addMarker(new MarkerOptions().position(currentMarkerLatLng).title(title));
        else
            mMap.addMarker(new MarkerOptions().position(currentMarkerLatLng));
    }

    //Add an OnMarkerClickListener to the current map
    //See https://developer.android.com/reference/com/google/android/gms/maps/GoogleMap.OnMarkerClickListener.html
    public void registerMarkerClickListener(GoogleMap mMap, GoogleMap.OnMarkerClickListener omcl) {
        mMap.setOnMarkerClickListener(omcl);
    }

    public String[] getAddressFromLocation(Geocoder geocoder, Location l) {
        String[] addressArray = new String[5];
        String streetAddress;
        String postalCode;
        String city;
        String state;
        String country;
        Address address;
        List<Address> addresses;

        try { //This will throw an exception if the location latitude and longitude are outside the android defined bounds
            //i.e.
            //latitude < -90 || latitude > 90
            //longitude < -180.0 || longitude > 180.0
            addresses = geocoder.getFromLocation(l.getLatitude(), l.getLongitude(), 1);
            if (addresses != null && addresses.size() > 0) {
                address = addresses.get(0);

                //This value can be null if there was no street address found
                streetAddress = address.getAddressLine(0);

                //This value can be null if there was no postal code found
                postalCode = address.getPostalCode();

                //This value can be null if there was no city found
                city = address.getLocality();

                //This value can be null if no state was found
                state = address.getAdminArea();

                //This value can be null if it is unknown
                country = address.getCountryName();
                //Can also call getCountryCode() to return "US" instead of "United States of America"
            } else {
                return null;
            }
            addressArray[0] = streetAddress;
            addressArray[1] = postalCode;
            addressArray[2] = city;
            addressArray[3] = state;
            addressArray[4] = country;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return addressArray;
    }

    //Implement if functionality is needed
    //Convert some address strings or a retrieved adddress to a Location object
    public csc_380_project.scarlettrails.Location getLocationFromAddress(Geocoder geocoder, Address address) {
        return new csc_380_project.scarlettrails.Location(address.getLatitude(), address.getLongitude());
        //http://stackoverflow.com/questions/12577168/get-location-latitude-longitude-from-address-without-city-in-android
    }

}