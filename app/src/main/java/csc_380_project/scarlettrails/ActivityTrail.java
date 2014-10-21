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

import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;

import java.util.ArrayList;

/**
 * Created by Nathan on 10/20/2014.
 */
public class ActivityTrail extends FragmentActivity implements ActionBar.OnNavigationListener {
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
        setContentView(R.layout.activity_trail);

        initializeNavigationBar();

        mLocWrapper = LocationWrapper.getInstance();
        initializeMap();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.test, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initializeNavigationBar() {
        ActionBar mActionBar = getActionBar();
        mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

        navSpinner = new ArrayList<SpinnerNavItem>();
        //This is how you enter new navigation items. Please use the format provided on next line.
        navSpinner.add(new SpinnerNavItem("Trail"));
        navSpinner.add(new SpinnerNavItem("Home"));
        mAdapter = new NavAdapter(getApplicationContext(), navSpinner);

        mActionBar.setListNavigationCallbacks(mAdapter, this);
        mActionBar.setDisplayShowTitleEnabled(false);
    }

    private void initializeMap() {
        if (mMap == null) {
            mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.trailMapFragment)).getMap();
        }

        //Check to see if successful
        mLocWrapper.checkMapExists(mMap);

        //This line currently sets map to center on Oswego County
        //Later replace with something that returns map to state it was previously in
        mLocWrapper.setUpMapWithDefaults(mMap);
        Location greatBearRecreationArea = new Location(43.26589,-76.351958);
        mLocWrapper.moveCamera(mMap, greatBearRecreationArea, LocationWrapper.TRAIL_ZOOM);
    }

    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
        if (itemPosition == 1) {
            Intent intent = new Intent(getApplicationContext(), ActivityHome.class);
            //if (intent.resolveActivity(getApplicationContext().getPackageManager()) != null)
            startActivity(intent);
            return true;
        }
        return false;
    }

    //When image is clicked, it should expand into a full screen view
    public void trailActivityTrailImageViewOnClick(View view) {

    }

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
