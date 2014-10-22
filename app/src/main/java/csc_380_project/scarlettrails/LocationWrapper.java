package csc_380_project.scarlettrails;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.Settings;

import com.google.android.gms.location.LocationClient;
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
    public static final LatLng OSWEGO_COUNTY = new LatLng(43.482533, -76.1783739);

    //Zoom constants - can pass these in to any method which requires a zoom to get the corresponding zoom level
    // 1.0f corresponds to displaying the entire world in the map frame
    // 21.0f corresponds to displaying an incredibly zoomed in location
    public static final float STATE_ZOOM = 6.0f;
    public static final float COUNTY_ZOOM = 8.0f;
    public static final float CITY_ZOOM = 12.0f; //Might need to be adjusted
    public static final float STREET_ZOOM = 14.0f;
    public static final float TRAIL_ZOOM = 15.0f; //Might need to be adjusted

    //TODO
    //Implement constructor for best criteria or remove
    private static Criteria criteriaBest;
    private static LocationWrapper instance;

    private LocationWrapper() {}

    public static LocationWrapper getInstance() {
        if (instance == null) {
            instance = new LocationWrapper();
        }
        return instance;
    }


    //TODO
    //After comparing which provider generally returns best location, decide if only one or many providers should be checked
    public boolean checkLocationSettingsEnabled (Context context) {
        boolean network_enabled;
        boolean gps_enabled;
        //boolean passive_enabled;
        //boolean best_enabled;
        LocationManager mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        //network_enabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        gps_enabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        //passive_enabled = mLocationManager.isProviderEnabled(LocationManager.PASSIVE_PROVIDER);

        return gps_enabled;

        /*criteriaBest = new Criteria();
        criteriaBest.setHorizontalAccuracy(Criteria.ACCURACY_HIGH);
        criteriaBest.setVerticalAccuracy(Criteria.ACCURACY_HIGH);
        criteriaBest.setSpeedAccuracy(Criteria.ACCURACY_HIGH);
        String bestProvider = mLocationManager.getBestProvider(criteriaBest, false);
        best_enabled = mLocationManager.isProviderEnabled(bestProvider);
        return best_enabled;*/
        //criteriaBest.
        //mLocationManager.getBestProvider()
    }

    public void openLocationSettings(final Context context) {
        AlertDialog.Builder ad = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_DARK);
        ad.setMessage(R.string.dialogLocationSettingsMessage)
                .setTitle(R.string.dialogLocationSettingsTitle)
                .setPositiveButton(R.string.dialogLocationSettingsPositiveButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        //if (intent.resolveActivity(context.getPackageManager()) != null) {
                        context.startActivity(intent);
                        //}
                    }
                })
                .setNegativeButton(R.string.dialogLocationSettingsNegativeButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        AlertDialog alertDialog = ad.create();
        alertDialog.show();
    }

    //TODO
    //Implement retrieving multiple locations from multiple providers and compare them to return best location
    //Launch async task at splash screen or home screen to start getting location read
    public Location getCurrentLocation(Context context) {
        boolean gps_enabled = checkLocationSettingsEnabled(context);
        LocationManager mLocationManager = (LocationManager)(context.getSystemService(Context.LOCATION_SERVICE));
        if (gps_enabled) {
            return mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        //Can enable hook here to prompt user to enable location settings
        //else
        //  openLocationSettings(context);
        return mLocationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
    }

    //Check to see if the map is not null
    public boolean checkMapExists(GoogleMap map) {
        if (map == null) {
            throw new NoMapException("There is no map - no map functionality can be accessed. Please call setGoogleMap(GoogleMap map) in your activity passing the current map object. See the new method");
            //return false - this option should not be used but you can change functionality if you need to for testing
        }
        return true;
    }

    //Remove all markers, polygons, etc. from the map
    public void clearMap(GoogleMap map) {
        map.clear();
    }

    //Verify map then set defaults
    public void setUpMapWithDefaults(GoogleMap mMap) {
        checkMapExists(mMap);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(OSWEGO_COUNTY,COUNTY_ZOOM));
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
    }


    public void displayTrailOnMapInstant(GoogleMap mMap, Trail t, float zoom) {
        centerCameraOnCustomLocation(mMap, t.getLocation(), zoom);
        addMarkerAtCustomLocation(mMap, t.getLocation(), t.getName(), true);
    }

    public void displayTrailOnMapAnimated(GoogleMap mMap, Trail t, float zoom) {
        moveCameraAnimatedZoomToCustomLocation(mMap, t.getLocation(), zoom);
        addMarkerAtCustomLocation(mMap, t.getLocation(), t.getName(), true);
    }

    //Launches a new system activity to get directions from startCoords to endCoords
    //Typically with Google Maps/Navigation but user has choice if they haven't selected a default
    public void getDirectionsFromCoords(Context context, Double latStart, Double lngStart, Double latEnd, Double lngEnd) {
        String uri = "http://maps.google.com/maps?saddr=" + latStart + "," + lngStart + "&daddr=" + latEnd + "," + lngEnd;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        context.startActivity(intent);
    }

    //Launches a new system activity to get directions from locStart to locEnd
    //Typically with Google Maps/Navigation but user has choice if they haven't selected a default
    public void getDirectionsFromCustomLocations(Context context, csc_380_project.scarlettrails.Location customLocStart, csc_380_project.scarlettrails.Location customLocEnd) {
        String uri = "http://maps.google.com/maps?saddr=" + customLocStart.getLatitude() + "," + customLocStart.getLongitude() + "&daddr=" + customLocEnd.getLatitude() + "," + customLocEnd.getLongitude();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        context.startActivity(intent);
    }

    //Launches a new system activity to get directions from locStart to locEnd
    //Typically with Google Maps/Navigation but user has choice if they haven't selected a default
    public void getDirectionsFromGoogleLocations(Context context, Location locStart, Location locEnd) {
        String uri = "http://maps.google.com/maps?saddr=" + locStart.getLatitude() + "," + locStart.getLongitude() + "&daddr=" + locEnd.getLatitude() + "," + locEnd.getLongitude();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        context.startActivity(intent);
    }

    //Center map on Google Location object
    //Lat and Lng must be bounded between
    //latitude < -90 || latitude > 90
    //longitude < -180.0 || longitude > 180.0
    public void centerCameraOnGoogleLocation(GoogleMap mMap, Location l, float zoom) {
        checkMapExists(mMap);
        LatLng tempLatLng = new LatLng(l.getLatitude(),l.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(tempLatLng, zoom));
    }

    //Center map on custom Location object
    //Lat and Lng must be bounded between
    //latitude < -90 || latitude > 90
    //longitude < -180.0 || longitude > 180.0
    public void centerCameraOnCustomLocation(GoogleMap mMap, csc_380_project.scarlettrails.Location customLoc, float zoom) {
        LatLng tempLatLng = new LatLng(customLoc.getLatitude(), customLoc.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(tempLatLng, zoom));
    }

    //Center map on a coordinate pair (Lat, Lng)
    //Lat and Lng must be bounded between
    //latitude < -90 || latitude > 90
    //longitude < -180.0 || longitude > 180.0
    public void centerCameraOnCoords(GoogleMap mMap, Double lat, Double lon, float zoom) {
        LatLng tempLatLng = new LatLng(lat,lon);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(tempLatLng, zoom));
    }

    //Move the camera view of the google map to a certain Location
    //Call this method when the change in distance is small i.e. from one part of Oswego county to another
    public void moveCameraAnimatedToGoogleLocation(GoogleMap mMap, Location l) {
        LatLng tempLatLng = new LatLng(l.getLatitude(),l.getLongitude());
        //A duration value can also be passed to determine how long the animation should take; see android documentation
        mMap.animateCamera(CameraUpdateFactory.newLatLng(tempLatLng));
    }

    //Move the camera view of the google map to a certain Location
    //Call this method when the change in distance is small i.e. from one part of Oswego county to another
    public void moveCameraAnimatedToCustomLocation(GoogleMap mMap, csc_380_project.scarlettrails.Location customLoc) {
        LatLng tempLatLng = new LatLng(customLoc.getLatitude(),customLoc.getLongitude());
        //A duration value can also be passed to determine how long the animation should take; see android documentation
        mMap.animateCamera(CameraUpdateFactory.newLatLng(tempLatLng));
    }

    //Move the camera view of the google map to a certain Location at a specified Zoom level
    //Call this method when the change in distance is small i.e. from one part of Oswego county to another
    //Specify a zoom level to be used - static constants have been declared for your use
    public void moveCameraAnimatedZoomToGoogleLocation(GoogleMap mMap, Location l, float zoom) {
        LatLng tempLatLng = new LatLng(l.getLatitude(),l.getLongitude());
        //A duration value can also be passed to determine how long the animation should take; see android documentation
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(tempLatLng, zoom));
    }

    //Move the camera view of the google map to a certain Location at a specified Zoom level
    //Call this method when the change in distance is small i.e. from one part of Oswego county to another
    //Specify a zoom level to be used - static constants have been declared for your use
    public void moveCameraAnimatedZoomToCustomLocation(GoogleMap mMap, csc_380_project.scarlettrails.Location customLoc, float zoom) {
        LatLng tempLatLng = new LatLng(customLoc.getLatitude(), customLoc.getLongitude());
        //A duration value can also be passed to determine how long the animation should take; see android documentation
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(tempLatLng, zoom));
    }

    //Relevant examples/documentation
    //https://developers.google.com/maps/documentation/android/marker
    public void addMarkerAtGoogleLocation(GoogleMap mMap, Location l, String title, boolean showTitleByDefault) {
        LatLng currentMarkerLatLng = new LatLng(l.getLatitude(), l.getLongitude());
        if (!title.equals(""))
            mMap.addMarker(new MarkerOptions().position(currentMarkerLatLng).title(title));
        else
            mMap.addMarker(new MarkerOptions().position(currentMarkerLatLng));
    }

    public void addMarkerAtCustomLocation(GoogleMap mMap, csc_380_project.scarlettrails.Location customLoc, String title, boolean showTitleByDefault) {
        LatLng currentMarkerLatLng = new LatLng(customLoc.getLatitude(), customLoc.getLongitude());
        if (!title.equals(""))
            if (showTitleByDefault)
                mMap.addMarker(new MarkerOptions().position(currentMarkerLatLng).title(title)).showInfoWindow();
            else
                mMap.addMarker(new MarkerOptions().position(currentMarkerLatLng).title(title));
        else
            if (showTitleByDefault)
                mMap.addMarker(new MarkerOptions().position(currentMarkerLatLng)).showInfoWindow();
            else
                mMap.addMarker(new MarkerOptions().position(currentMarkerLatLng));
    }

    public String[] getAddressFromLocationGoogleLocation(Geocoder geocoder, Location l) {
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

    //Returns distance from start to destination
    public float getDistanceToGoogleLocation(Location origin, Location destination) {
        return origin.distanceTo(destination);
    }

    //TODO
    //From GoogleDirectionAndPlace (GDAP) library
    public void displayDirectionsOnMapWithGoogleLocations(Location origin, Location destination, GoogleMap map) {

    }

    //TODO
    //Implement
    public void beginListeningForLocationUpdates(LocationClient mLocationClient) {
        //mLocationClient.requestLocationUpdates();
    }

    //Add an OnMarkerClickListener to the current map
    //See https://developer.android.com/reference/com/google/android/gms/maps/GoogleMap.OnMarkerClickListener.html
    public void registerMarkerClickListener(GoogleMap mMap, GoogleMap.OnMarkerClickListener omcl) {
        mMap.setOnMarkerClickListener(omcl);
    }

    //Implement if functionality is needed
    //Convert some address strings or a retrieved adddress to a Location object
    public csc_380_project.scarlettrails.Location getLocationFromAddress(Geocoder geocoder, Address address) {
        return new csc_380_project.scarlettrails.Location(address.getLatitude(), address.getLongitude());
        //http://stackoverflow.com/questions/12577168/get-location-latitude-longitude-from-address-without-city-in-android
    }
}