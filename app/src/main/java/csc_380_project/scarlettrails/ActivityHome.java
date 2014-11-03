package csc_380_project.scarlettrails;

import android.app.ActionBar;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Nathan on 10/19/2014.
 */
public class ActivityHome extends FragmentActivity implements ActionBar.OnNavigationListener { // implements LoaderManager.LoaderCallbacks<Cursor> {
    private GoogleMap mMap;
    private LocationWrapper mLocWrapper;
    private NavAdapter mAdapter;
    private ArrayList<SpinnerNavItem> navSpinner;
    private ArrayList<Marker> mMarkers;
    private TrailCollection trailCollection;
    private Marker lastMarkerClicked;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_home);

        initializeNavigationBar();

        mLocWrapper = LocationWrapper.getInstance();
        initializeMap();

        //Create dummy trails
        Random r = new Random();
        trailCollection = new TrailCollection();
        for (int i = 0; i < 6; i++) {
            CustomLocation tempLoc = new CustomLocation(generateRandomLatitude(r), generateRandomLongitude(r));
            trailCollection.addTrail(new Trail(i, String.valueOf("Trail " + i), (r.nextInt(9000) + 1000.0), (r.nextInt(950) + 50.0), Trail.DURATION_MEDIUM, Trail.DIFFICULTY_MEDIUM, tempLoc, "gear", "conds", true));
        }

        mMarkers = new ArrayList<Marker>();
        addTrailCollectionMarkersToMap(trailCollection);

        mLocWrapper.registerMarkerClickListener(mMap, new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                lastMarkerClicked = marker;
                return false;
            }
        });

        Location mLoc = mLocWrapper.getCurrentLocation(getApplicationContext());
        final Marker mLocMarker;
        //if (mLoc != null) {
            mLocMarker = mLocWrapper.addMarkerAtGoogleLocation(mMap, mLoc, "My Location", true);
            mLocMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        //}

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                if (marker.getId().equals(lastMarkerClicked.getId()) && !marker.getId().equals(mLocMarker.getId()))  {
                    Trail temp = trailCollection.getTrailbyName(marker.getTitle());
                    if (temp != null) {
                        Intent activityTrail = new Intent(getApplicationContext(), ActivityTrail.class);
                        activityTrail.putExtra("trail", temp);
                        startActivity(activityTrail);
                    }
                }
            }
        });

        mLocMarker.showInfoWindow();
    }

    public Double generateRandomLatitude(Random r) {
        Double baseLat = LocationWrapper.OSWEGO_COUNTY.latitude;
        int pos_neg = r.nextInt(1);
        if (pos_neg == 0) { //Add
            return baseLat + (r.nextDouble() / 10);
        } else { //Subtract
            return baseLat - (r.nextDouble() / 10);
        }
    }

    public Double generateRandomLongitude(Random r) {
        Double baseLng = LocationWrapper.OSWEGO_COUNTY.longitude;
        int pos_neg = r.nextInt(1);
        if (pos_neg == 0) { //Add
            return baseLng + (r.nextDouble());
        } else { //Subtract
            return baseLng - (r.nextDouble());
        }
    }

    public void addTrailCollectionMarkersToMap(TrailCollection tc) {
        for (int i = 0; i < tc.getSize(); i++) {
            Marker temp = mLocWrapper.addTrailMarker(mMap, tc.getTrailAtIndex(i),false);
            mMarkers.add(temp);
        }
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
            Intent intent = new Intent(getApplicationContext(), ActivitySettings.class);
            startActivity(intent);
            return true;
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
        navSpinner.add(new SpinnerNavItem("Profile"));
        mAdapter = new NavAdapter(getApplicationContext(), navSpinner);

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
           Intent intent = new Intent(getApplicationContext(), ActivityProfile.class);
           startActivity(intent);
           return true;
            }
        return false;
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
