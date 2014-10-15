package csc_380_project.scarlettrails;

import android.location.Location;
import android.location.LocationManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


/*Relevant API and Example pages

https://developers.google.com/maps/documentation/android/start
https://developers.google.com/maps/documentation/android/map
https://developers.google.com/maps/documentation/android/views#setting_boundaries

*/

public final class LocationInterface implements DatabaseInterface {

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

    //These are placeholders for functionality that needs to be implemented
    //This class should let the android system determine which provider is best and return that to this class
    //Will be implemented as such in the future
    private static final String PROVIDER_PASSIVE = "passive";
    private static final String PROVIDER_NETWORK = "network";
    private static final String PROVIDER_GPS = "gps";

    private static LocationInterface instance;
    private static GoogleMap mMap;
    private static Location mCurrentLocation;
    private static LatLng mLatLng;
    private static LocationManager mLocationManager;

    private LocationInterface() {
    }

    static void setGoogleMap(GoogleMap map) {
        mMap = map;
    }

    static void setLocationManager(LocationManager lm) { mLocationManager = lm; }

    public static LocationInterface getInstance() {
        if (instance == null)
            instance = new LocationInterface();
        return instance;
    }

    public static boolean checkMapExists() {
        if (mMap == null) {
            throw new NoMapException("There is no map - no map functionality can be accessed. Please call setGoogleMap(GoogleMap map) in your activity passing the current map object. See the new method");
            //return false - this option should not be used but you can change functionality if you need to for testing
        }
        return true;
    }

    public static void beginListeningForLocationUpdates() {

    }

    public static void checkLocationSettingsEnabled () {

    }

    public static Location getCurrentLocation() {
        checkMapExists();
        checkLocationSettingsEnabled();
        return mLocationManager.getLastKnownLocation(PROVIDER_PASSIVE);
    }

    //Returns distance from start to destination
    public static void getDistanceTo(Location start, Location destination) {
        checkMapExists();
    }

    public static void setUpMapWithDefaults() {
        checkMapExists();
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(OSWEGO_COUNTY,COUNTY_ZOOM));
    }

    //Move the camera view of the google map to a certain Location
    public static void moveCamera(Location l) {
        checkMapExists();
        LatLng tempLatLng = new LatLng(l.getLatitude(),l.getLongitude());
        mMap.animateCamera(CameraUpdateFactory.newLatLng(tempLatLng));
    }

    //Move the camera view of the google map to a certain Location
    //Call this method when the change in distance is small i.e. from one part of Oswego county to another
    public static void moveCameraAnimated(Location l) {
        checkMapExists();
        LatLng tempLatLng = new LatLng(l.getLatitude(),l.getLongitude());
        //A duration value can also be passed to determine how long the animation should take; see android documentation
        mMap.animateCamera(CameraUpdateFactory.newLatLng(tempLatLng));
    }

    //Move the camera view of the google map to a certain Location at a specified Zoom level
    //Call this method when the change in distance is small i.e. from one part of Oswego county to another
    //Specify a zoom level to be used - static constants have been declared for your use
    public static void moveCameraAnimatedZoom(Location l, float f) {
        checkMapExists();
        LatLng tempLatLng = new LatLng(l.getLatitude(),l.getLongitude());
        //A duration value can also be passed to determine how long the animation should take; see android documentation
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(tempLatLng, f));
    }

    //Relevant examples/documentation
    //https://developers.google.com/maps/documentation/android/marker
    public static void addMarker(Location l, String title) {
        checkMapExists();
        LatLng currentMarkerLatLng = new LatLng(l.getLatitude(), l.getLongitude());
        if (!title.equals(""))
            mMap.addMarker(new MarkerOptions().position(currentMarkerLatLng).title(title));
        else
            mMap.addMarker(new MarkerOptions().position(currentMarkerLatLng));
    }

    //Add an OnMarkerClickListener to the current map
    //See https://developer.android.com/reference/com/google/android/gms/maps/GoogleMap.OnMarkerClickListener.html
    public static void registerMarkerClickListener(GoogleMap.OnMarkerClickListener omcl) {
        mMap.setOnMarkerClickListener(omcl);
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
