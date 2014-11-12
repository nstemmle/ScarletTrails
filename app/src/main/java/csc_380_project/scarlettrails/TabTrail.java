package csc_380_project.scarlettrails;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;

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

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTrail = ActivityTrailTabHostTest.mTrail;
        setContentView(R.layout.tab_trail_info);

        mLocWrapper = LocationWrapper.getInstance();
        initializeMap();

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
        //((TextView)findViewById(R.id.trail_textview_tempmax_value)).setText(String.valueOf(mTrail.getForecast().getTempMax()) + "°F");

        //Trail temp min
        //((TextView)findViewById(R.id.trail_textview_tempmin_value)).setText(String.valueOf(mTrail.getForecast().getTempMin()) + "°F");

        //Trail clouds/precipitation picture

        //Trail sunrise
        //((TextView)findViewById(R.id.trail_textview_sunrise_value)).setText(String.valueOf(mTrail.getForecast().getSunrise()));

        //Trail sunset
        //((TextView)findViewById(R.id.trail_textview_sunset_value)).setText(String.valueOf(mTrail.getForecast().getSunset()));


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
        mLocWrapper.centerCameraOnCustomLocation(mMap, mTrail.getLocation(), LocationWrapper.TRAIL_ZOOM);
        mLocWrapper.addMarkerAtCustomLocation(mMap, mTrail.getLocation(), mTrail.getName(), true);
    }
}
