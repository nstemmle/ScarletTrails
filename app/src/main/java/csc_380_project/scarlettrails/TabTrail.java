package csc_380_project.scarlettrails;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;

/**
 * Created by Nathan on 10/20/2014.
 */
public class TabTrail extends Activity { //implements ActionBar.OnNavigationListener {
    private GoogleMap mMap;
    private LocationWrapper mLocWrapper;
    private NavAdapter mAdapter;
    private ArrayList<SpinnerNavItem> navSpinner;
    private Trail mTrail;
    private static String TAG = "TabTrail.java";
    private Forecast mForecast;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTrail = ActivityTrailTabHostTest.mTrail;
        setContentView(R.layout.tab_trail_info);

        mLocWrapper = LocationWrapper.getInstance();
        initializeMap();

        Thread t = new Thread() {
            public void run() {
                mForecast = ForecastWrapper.createForecast(mTrail);
            }

        };
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
       populatePageWithTrailInfo();
    }

    private void initializeMap() {
        if (mMap == null) {
            mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.tab_fragment_map)).getMap();
        }

        //Check to see if successful
        mLocWrapper.checkMapExists(mMap);

        //This line currently sets map to center on Oswego County
        //Later replace with something that returns map to state it was previously in
        mLocWrapper.setUpMapWithDefaults(mMap);
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
    }

    public void populatePageWithTrailInfo() {
        //Trail Name
        Log.e(TAG, "mTrail != null: " + String.valueOf(mTrail != null));
        Log.e(TAG,"mTrail.getName() " + mTrail.getName());

        //Trail difficulty
        ((TextView)findViewById(R.id.tab_trail_textview_difficulty_value)).setText(mTrail.getDifficulty());

        //Trail distance
        ((TextView)findViewById(R.id.tab_trail_textview_distance_value)).setText(String.valueOf(mTrail.getDistance()) + " mi");

        //Trail duration
        ((TextView)findViewById(R.id.tab_trail_textview_duration_value)).setText(mTrail.getDuration());

        //Trail elevation
        ((TextView)findViewById(R.id.tab_trailTxtViewElevationValue)).setText(String.valueOf(mTrail.getElevation()) + " ft");

        //Trail gear
        ((TextView)findViewById(R.id.tab_trail_textview_gear_value)).setText(mTrail.getGear());

        //Trail conditions
        ((TextView)findViewById(R.id.tab_trail_textview_conditions_value)).setText(mTrail.getTrailConditions());

        //Trail pet friendly
        ((TextView)findViewById(R.id.tab_trail_textview_petfriendly_value)).setText(mTrail.isPetFriendly() ? "Yes" : "No");

        //Trail temp max
        ((TextView)findViewById(R.id.tab_trail_textview_tempmax_value)).setText(String.valueOf(mForecast.getTempMax() + "°F"));

        //Trail temp min
        ((TextView)findViewById(R.id.tab_trail_textview_tempmin_value)).setText(String.valueOf(mForecast.getTempMin()) + "°F");

        //Trail clouds/precipitation picture
        setIcon(mForecast.getDescription());

        //Trail sunrise
        ((TextView)findViewById(R.id.tab_trail_textview_sunrise_value)).setText(mForecast.getSunrise());

        //Trail sunset
        ((TextView)findViewById(R.id.tab_trail_textview_sunset_value)).setText(mForecast.getSunset());


        //Trail image
        //((ImageView)findViewById(R.id.trailImgViewTrail)).setImageURI();

        //Trail rating
        RatingBar rb = ((RatingBar)findViewById(R.id.tab_trail_ratingbar));
        rb.setRating(mTrail.getRating().floatValue());
        rb.setStepSize(0.5f);

        //Change colors
        LayerDrawable stars = (LayerDrawable) rb.getProgressDrawable();
        //Fully shaded color (4/5 rating = 4 stars shaded)
        stars.getDrawable(0).setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
        //Partially shaded color
        stars.getDrawable(1).setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
        //No shade color
        stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);

        mLocWrapper.clearMap(mMap);
        mLocWrapper.centerCameraOnCustomLocation(mMap, mTrail.getLocation(), LocationWrapper.STREET_ZOOM);
        Marker marker = mLocWrapper.addMarkerAtCustomLocation(mMap, mTrail.getLocation(), mTrail.getName(), true);
        marker.setSnippet("Click me for directions.");
        marker.showInfoWindow();

        /*Location loc = mLocWrapper.getCurrentLocation(getApplicationContext());
        final CustomLocation mCusLoc = new CustomLocation(loc.getLatitude(), loc.getLongitude());
        final Context context = getApplicationContext();
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                mLocWrapper.launchDirectionsFromCustomLocation(context, mCusLoc, mTrail.getLocation());
            }
        });*/
    }

    //will add implementation on 11/16
    public void setIcon(int weatherid){
        if (weatherid <= 232 || weatherid ==960 || weatherid == 961){
            //storm
        } else if (weatherid <= 321){
            //light rain
        } else if (weatherid <= 531){
            //heavy rain
        } else if (611 <= weatherid || weatherid <= 616){
            //mix
        } else if (weatherid <= 622){
            //snow
        } else if (weatherid == 800){
            //sunny
        } else if (weatherid == 801){
            //few clouds
        } else if (weatherid <= 803){
            //partly cloudy
        } else if (weatherid == 804 ){
            //cloudy
        } else if (weatherid <= 906 ){
            //hail
        }else{
            //default picture
        }
    }

}
