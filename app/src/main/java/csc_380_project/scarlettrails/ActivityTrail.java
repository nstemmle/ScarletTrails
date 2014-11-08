package csc_380_project.scarlettrails;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.GridView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Nathan on 10/20/2014.
 */
public class ActivityTrail extends Activity implements ActionBar.OnNavigationListener {
    private GoogleMap mMap;
    private LocationWrapper mLocWrapper;
    private NavAdapter mAdapter;
    private ArrayList<SpinnerNavItem> navSpinner;
    private Trail mTrail;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTrail = getIntent().getParcelableExtra("trail");
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);

        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_trail);
        assert getActionBar() != null;
        getActionBar().setTitle(mTrail.getName());

        initializeNavigationBar();

        mLocWrapper = LocationWrapper.getInstance();
        initializeMap();

       populatePageWithTrailInfo();

       //GridView gridview = (GridView) findViewById(R.id.trailGridView);
       GridView gridview = (GridView) findViewById(R.id.trail_gridview_thumbnails);
       gridview.setAdapter(new ImageAdapter(this));
       //Will implement sample pics later

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.action_bar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.actionbar_settings) {
            return true;
        } else if (id == R.id.actionbar_search){
            Intent intent = new Intent(getApplicationContext(), ActivitySearchTrail.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void initializeNavigationBar() {
        ActionBar mActionBar = getActionBar();
        assert mActionBar != null;
        mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

        navSpinner = new ArrayList<SpinnerNavItem>();
        //This is how you enter new navigation items. Please use the format provided on next line.
        navSpinner.add(new SpinnerNavItem("Trail"));
        navSpinner.add(new SpinnerNavItem("Home"));
        navSpinner.add(new SpinnerNavItem("Profile"));
        mAdapter = new NavAdapter(getApplicationContext(), navSpinner);

        mActionBar.setListNavigationCallbacks(mAdapter, this);
        mActionBar.setDisplayShowTitleEnabled(false);
    }

    private void initializeMap() {
        if (mMap == null) {
            mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.trail_fragment_map)).getMap();
        }

        //Check to see if successful
        mLocWrapper.checkMapExists(mMap);

        //This line currently sets map to center on Oswego County
        //Later replace with something that returns map to state it was previously in
        mLocWrapper.setUpMapWithDefaults(mMap);
    }

    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
        if (itemPosition == 1) {
            Intent intent = new Intent(getApplicationContext(), ActivityHome.class);
            //if (intent.resolveActivity(getApplicationContext().getPackageManager()) != null)
            startActivity(intent);
            return true;
        }
        else
            if (itemPosition == 2) {
                Intent intent = new Intent(getApplicationContext(), ActivityProfile.class);
                //if (intent.resolveActivity(getApplicationContext().getPackageManager()) != null)
                startActivity(intent);
                return true;
            }
        return false;
    }

    //When image is clicked, it should expand into a full screen view
    public void trailActivityTrailImageViewOnClick(View view) {

    }

    public void populatePageWithTrailInfo() {
        //Trail Name
        ((TextView)findViewById(R.id.trail_textview_trailname_value)).setText(mTrail.getName());

        //Trail difficulty
        ((TextView)findViewById(R.id.trail_textview_difficulty_value)).setText(mTrail.getDifficulty());

        //Trail distance
        ((TextView)findViewById(R.id.trail_textview_distance_value)).setText(String.valueOf(mTrail.getDistance()) + " mi");

        //Trail duration
        ((TextView)findViewById(R.id.trail_textview_duration_value)).setText(mTrail.getDuration());

        //Trail elevation
        ((TextView)findViewById(R.id.trailTxtViewElevationValue)).setText(String.valueOf(mTrail.getElevation()) + " ft");

        //Trail gear
        ((TextView)findViewById(R.id.trail_textview_gear_value)).setText(mTrail.getGear());

        //Trail conditions
        ((TextView)findViewById(R.id.trail_textview_conditions_value)).setText(mTrail.getTrailConditions());

        //Trail pet friendly
        ((TextView)findViewById(R.id.trail_textview_petfriendly_value)).setText(mTrail.isPetFriendly() ? "Yes" : "No");

        //Trail temp max
        ((TextView)findViewById(R.id.trail_textview_tempmax_value)).setText(String.valueOf(mTrail.getForecast().getTempMax()) + "°F");

        //Trail temp min
        ((TextView)findViewById(R.id.trail_textview_tempmin_value)).setText(String.valueOf(mTrail.getForecast().getTempMin()) + "°F");

        //Trail clouds/precipitation picture

        //Trail sunrise
        ((TextView)findViewById(R.id.trail_textview_sunrise_value)).setText(String.valueOf(mTrail.getForecast().getSunrise()));

        //Trail sunset
        ((TextView)findViewById(R.id.trail_textview_sunset_value)).setText(String.valueOf(mTrail.getForecast().getSunset()));


        //Trail image
        //((ImageView)findViewById(R.id.trailImgViewTrail)).setImageURI();

        //Trail rating
        RatingBar rb = ((RatingBar)findViewById(R.id.trail_ratingbar));
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
