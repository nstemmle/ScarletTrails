package csc_380_project.scarlettrails;

import android.app.ActionBar;
import android.content.Intent;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.SimpleCursorAdapter;

import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;

import java.util.ArrayList;

/**
 * Created by Nathan on 10/19/2014.
 */
public class ActivityHome extends FragmentActivity implements ActionBar.OnNavigationListener { // implements LoaderManager.LoaderCallbacks<Cursor> {
    SimpleCursorAdapter mCursor;
    private GoogleMap mMap;
    private Geocoder mGeocoder;
    private LocationClient mLocationClient;
    private LocationManager mLocationManager;
    private LocationWrapper mLocWrapper;
    private NavAdapter mAdapter;
    private ArrayList<SpinnerNavItem> navSpinner;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_home);

        initializeNavigationBar();

        mLocWrapper = LocationWrapper.getInstance();
        initializeMap();
        mLocWrapper.checkLocationSettingsEnabled(getApplication());
        //ProgressBar progressBar = new ProgressBar(this);
        //progressBar.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        //progressBar.setIndeterminate(true);
        //getListView().setEmptyView(progressBar);

        //ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
        //root.addView(progressBar);
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
        if (id == R.id.actionbar_search) {
            return true;
        } else if (id == R.id.actionbar_settings) {

        }
        return super.onOptionsItemSelected(item);
    }

    private void initializeNavigationBar() {
        ActionBar mActionBar = getActionBar();
        assert mActionBar != null;
        mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

        navSpinner = new ArrayList<SpinnerNavItem>();
        //This is how you enter new navigation items. Please use the format provided on next line.
        navSpinner.add(new SpinnerNavItem("Home"));
        navSpinner.add(new SpinnerNavItem("Trail"));
        mAdapter = new NavAdapter(getApplication(), navSpinner);

        mActionBar.setListNavigationCallbacks(mAdapter, this);
        mActionBar.setDisplayShowTitleEnabled(false);
    }

    private void initializeMap() {
        if (mMap == null) {
            mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.home_fragment_map)).getMap();
        }

        //Check to see if successful
        mLocWrapper.checkMapExists(mMap);

        //This line currently sets map to center on Oswego County
        //Later replace with something that returns map to state it was previously in
        mLocWrapper.setUpMapWithDefaults(mMap);
    }

    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
        // Switch to be implemented here depending on which item is selected
       if (itemPosition == 1) {
           Intent intent = new Intent(getApplication(), ActivityTrail.class);
           startActivity(intent);
           return true;
           }
        return false;
    }

    /*public void getDirections(Double latStart, Double lngStart, Double latEnd, Double lngEnd) {
        String uri = "http://maps.google.com/maps?saddr=" + latStart + "," + lngStart + "&daddr=" + latEnd + "," + lngEnd;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(intent);
    }*/


    public void buttonLaunchNavigationClick(View view) {
        //This line prompts user to open Location Settings
        //mLocWrapper.openLocationSettings(ActivityHome.this);

        //Start from Oswego County and go to Great Bear Recreation Area
        //getDirections(LocationWrapper.OSWEGO_COUNTY.latitude, LocationWrapper.OSWEGO_COUNTY.longitude, 43.26589,-76.351958);
        mLocWrapper.getDirectionsFromCoords(ActivityHome.this, LocationWrapper.OSWEGO_COUNTY.latitude, LocationWrapper.OSWEGO_COUNTY.longitude, 43.26589, -76.351958);
    }

    //These lines need to be implemented for dynamically fetching and retrieving data

    /*@Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public void onListItemClick(ListView listview, View view, int position, long id) {

    }*/
}
