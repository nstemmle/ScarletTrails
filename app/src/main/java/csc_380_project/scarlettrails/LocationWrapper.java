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
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

/*Relevant API and Example pages

https://developers.google.com/maps/documentation/android/start
https://developers.google.com/maps/documentation/android/map
https://developers.google.com/maps/documentation/android/views#setting_boundaries

*/

public class LocationWrapper {
    //DEBUGGING
    private static final String TAG = "LOCATION_WRAPPER";

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

    private LocationWrapper(){}

    public static LocationWrapper getInstance() {
        if (instance == null) {
            instance = new LocationWrapper();
        }
        return instance;
    }

    public Location getCurrentLocation(Context context) {
        LocationManager mLocationManager = (LocationManager)(context.getSystemService(Context.LOCATION_SERVICE));
        /*//mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME,LOCATION_REFRESH_DISTANCE, mLocationListener);
        LocationListener mLocListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

            }
        };
        mLocationManager.requestLocationUpdates(getBestProvider(context), 100l, 1f, mLocListener);*/
        return mLocationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
    }


    public Location getCurrentLocation(Context context, String provider) {
        LocationManager mLocationManager = (LocationManager)(context.getSystemService(Context.LOCATION_SERVICE));
        return mLocationManager.getLastKnownLocation(provider);
    }

    public String getBestProvider (Context context) {
        LocationManager mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        Criteria mBestCriteria = new Criteria();
        mBestCriteria.setAccuracy(Criteria.ACCURACY_FINE);
        return mLocationManager.getBestProvider(mBestCriteria, true);
    }

    public boolean isPassiveProviderEnabled (Context context) {
        LocationManager mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return mLocationManager.isProviderEnabled(LocationManager.PASSIVE_PROVIDER);
    }


    public boolean isNetworkProviderEnabled (Context context) {
        LocationManager mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public boolean isGPSProviderEnabled(Context context) {
        LocationManager mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public void openLocationSettings(final Context context, boolean promptUser) {
        if (!promptUser) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } else {
            AlertDialog.Builder ad = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_DARK);
            ad.setMessage(R.string.dialogLocationSettingsMessage)
                    .setTitle(R.string.dialogLocationSettingsTitle)
                    .setPositiveButton(R.string.dialogLocationSettingsPositiveButton, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
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
    }

    //Check to see if the map is not null
    public boolean checkMapExists(GoogleMap map) {
        if (map == null) {
            throw new NoMapException("There is no map - no map functionality can be accessed. Please ensure that you have retrieved the most recent map on the current activity before using it.");
        }
        return true;
    }

    //Remove all markers, polygons, etc. from the map
    public void clearMap(GoogleMap map) {
        map.clear();
    }

    //Verify map then set defaults
    public void setUpMapWithDefaults(GoogleMap map) {
        checkMapExists(map);
        //map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(OSWEGO_COUNTY,COUNTY_ZOOM));
        map.setMyLocationEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(true);
    }

    public void displayTrailOnMapInstant(GoogleMap map, Trail trail, float zoom) {
        centerCameraOnCustomLocation(map, trail.getLocation(), zoom);
        addMarkerAtCustomLocation(map, trail.getLocation(), trail.getName(), true);
    }

    public void displayTrailOnMapAnimated(GoogleMap map, Trail trail, float zoom) {
        moveCameraAnimatedZoomToCustomLocation(map, trail.getLocation(), zoom);
        addMarkerAtCustomLocation(map, trail.getLocation(), trail.getName(), true);
    }

    //Launches a new system activity to get directions from startCoords to endCoords
    //Typically with Google Maps/Navigation but user has choice if they haven't selected a default
    public void launchDirectionsFromCoords(Context context, Double latStart, Double lngStart, Double latEnd, Double lngEnd) {
        String uri = "http://maps.google.com/maps?saddr=" + latStart + "," + lngStart + "&daddr=" + latEnd + "," + lngEnd;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    //Launches a new system activity to get directions from locStart to locEnd
    //Typically with Google Maps/Navigation but user has choice if they haven't selected a default
    public void launchDirectionsFromCustomLocation(Context context, CustomLocation customLocStart, CustomLocation customLocEnd) {
        String uri = "http://maps.google.com/maps?saddr=" + customLocStart.getLatitude() + "," + customLocStart.getLongitude() + "&daddr=" + customLocEnd.getLatitude() + "," + customLocEnd.getLongitude();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    //Launches a new system activity to get directions from locStart to locEnd
    //Typically with Google Maps/Navigation but user has choice if they haven't selected a default
    public void launchDirectionsFromGoogleLocation(Context context, Location locStart, Location locEnd) {
        String uri = "http://maps.google.com/maps?saddr=" + locStart.getLatitude() + "," + locStart.getLongitude() + "&daddr=" + locEnd.getLatitude() + "," + locEnd.getLongitude();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    //Center map on Google Location object
    //Lat and Lng must be bounded between
    //latitude < -90 || latitude > 90
    //longitude < -180.0 || longitude > 180.0
    public void centerCameraOnGoogleLocation(GoogleMap map, Location location, float zoom) {
        checkMapExists(map);
        LatLng tempLatLng = new LatLng(location.getLatitude(),location.getLongitude());
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(tempLatLng, zoom));
    }

    //Center map on custom Location object
    //Lat and Lng must be bounded between
    //latitude < -90 || latitude > 90
    //longitude < -180.0 || longitude > 180.0
    public void centerCameraOnCustomLocation(GoogleMap map, CustomLocation customLoc, float zoom) {
        LatLng tempLatLng = new LatLng(customLoc.getLatitude(), customLoc.getLongitude());
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(tempLatLng, zoom));
    }

    //Center map on a coordinate pair (Lat, Lng)
    //Lat and Lng must be bounded between
    //latitude < -90 || latitude > 90
    //longitude < -180.0 || longitude > 180.0
    public void centerCameraOnCoords(GoogleMap map, Double lat, Double lon, float zoom) {
        LatLng tempLatLng = new LatLng(lat,lon);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(tempLatLng, zoom));
    }

    //Move the camera view of the google map to a certain Location
    //Call this method when the change in distance is small i.e. from one part of Oswego county to another
    public void moveCameraAnimatedToGoogleLocation(GoogleMap map, Location location) {
        LatLng tempLatLng = new LatLng(location.getLatitude(),location.getLongitude());
        //A duration value can also be passed to determine how long the animation should take; see android documentation
        map.animateCamera(CameraUpdateFactory.newLatLng(tempLatLng));
    }

    //Move the camera view of the google map to a certain Location
    //Call this method when the change in distance is small i.e. from one part of Oswego county to another
    public void moveCameraAnimatedToCustomLocation(GoogleMap map, CustomLocation customLoc) {
        LatLng tempLatLng = new LatLng(customLoc.getLatitude(),customLoc.getLongitude());
        //A duration value can also be passed to determine how long the animation should take; see android documentation
        map.animateCamera(CameraUpdateFactory.newLatLng(tempLatLng));
    }

    //Move the camera view of the google map to a certain Location at a specified Zoom level
    //Call this method when the change in distance is small i.e. from one part of Oswego county to another
    //Specify a zoom level to be used - static constants have been declared for your use
    public void moveCameraAnimatedZoomToGoogleLocation(GoogleMap map, Location location, float zoom) {
        LatLng tempLatLng = new LatLng(location.getLatitude(),location.getLongitude());
        //A duration value can also be passed to determine how long the animation should take; see android documentation
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(tempLatLng, zoom));
    }

    //Move the camera view of the google map to a certain Location at a specified Zoom level
    //Call this method when the change in distance is small i.e. from one part of Oswego county to another
    //Specify a zoom level to be used - static constants have been declared for your use
    public void moveCameraAnimatedZoomToCustomLocation(GoogleMap map, CustomLocation customLoc, float zoom) {
        LatLng tempLatLng = new LatLng(customLoc.getLatitude(), customLoc.getLongitude());
        //A duration value can also be passed to determine how long the animation should take; see android documentation
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(tempLatLng, zoom));
    }

    //Relevant examples/documentation
    //https://developers.google.com/maps/documentation/android/marker
    public Marker addMarkerAtGoogleLocation(GoogleMap map, Location location, String title, boolean showTitleByDefault) {
        LatLng currentMarkerLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        Marker current;
        if (title != null) {
            current = map.addMarker(new MarkerOptions()
                    .position(currentMarkerLatLng)
                    .title(title));
        } else {
            current = map.addMarker(new MarkerOptions()
                    .position(currentMarkerLatLng));
        }
        if (showTitleByDefault)
            current.showInfoWindow();
        return current;
    }

    public Marker addMarkerAtCustomLocation(GoogleMap map, CustomLocation customLoc, String title, boolean showTitleByDefault) {
        LatLng currentMarkerLatLng = new LatLng(customLoc.getLatitude(), customLoc.getLongitude());
        Marker current;
        if (title != null) {
            current = map.addMarker(new MarkerOptions()
                    .position(currentMarkerLatLng)
                    .title(title));
        } else {
            current = map.addMarker(new MarkerOptions()
                    .position(currentMarkerLatLng));
        }
        if (showTitleByDefault)
            current.showInfoWindow();
        return current;
    }

    public Marker addMarkerAtCoords(GoogleMap map, Double latitude, Double longitude, String title, boolean showTitleByDefault) {
        LatLng currentMarkerLatLng = new LatLng(latitude, longitude);
        Marker current;
        if (title != null) {
            current = map.addMarker(new MarkerOptions()
                    .position(currentMarkerLatLng)
                    .title(title));
        } else {
            current = map.addMarker(new MarkerOptions()
                    .position(currentMarkerLatLng));
        }
        if (showTitleByDefault)
            current.showInfoWindow();
        return current;
    }

    public Marker addMarkerAtLatLng(GoogleMap map, LatLng markerLatLng, String title, boolean showTitleByDefault) {
        Marker current;
        if (title != null) {
            current = map.addMarker(new MarkerOptions()
                    .position(markerLatLng)
                    .title(title));
        } else {
            current = map.addMarker(new MarkerOptions()
                    .position(markerLatLng));
        }
        if (showTitleByDefault)
            current.showInfoWindow();
        return current;
    }

    public Marker addTrailMarker(GoogleMap map, Trail trail, boolean showTitleByDefault) {
        Marker trailMarker = addMarkerAtCustomLocation(map, trail.getLocation(),trail.getName(),showTitleByDefault);
        trailMarker.setSnippet("Click me to see trail info.");
        return trailMarker;
    }

    public String[] getAddressFromGoogleLocation(Geocoder geocoder, Location location) {
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
            //will throw an IOException if Network is unavailable
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
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

    public String[] getAddressFromCustomLocation(Geocoder geocoder, CustomLocation customLoc) {
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
            //will throw an IOException if Network is unavailable
            addresses = geocoder.getFromLocation(customLoc.getLatitude(), customLoc.getLongitude(), 1);
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
    //Implement
    //When are constant location updates needed? More context will help guide implementing this
    public void beginListeningForLocationUpdates() {}

    //Add an OnMarkerClickListener to the current map
    //See https://developer.android.com/reference/com/google/android/gms/maps/GoogleMap.OnMarkerClickListener.html
    public void registerMarkerClickListener(GoogleMap map, GoogleMap.OnMarkerClickListener omcl) {
        map.setOnMarkerClickListener(omcl);
    }

    //Implement if functionality is needed
    //Convert some address strings or a retrieved adddress to a customLocation object
    public CustomLocation getLocationFromAddress(Geocoder geocoder, Address address) {
        return new CustomLocation(address.getLatitude(), address.getLongitude());
        //http://stackoverflow.com/questions/12577168/get-location-latitude-longitude-from-address-without-city-in-android
    }
}