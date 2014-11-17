package csc_380_project.scarlettrails;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
public class ActivityHome extends Activity implements ActionBar.OnNavigationListener {
    private GoogleMap mMap;
    private LocationWrapper mLocWrapper;
    private NavAdapter mAdapter;
    private ArrayList<SpinnerNavItem> navSpinner;
    private TrailCollection trailCollection;
    private Marker lastMarkerClicked;
    private boolean gps_enabled;
    private boolean network_enabled;

    private boolean gps_ignored = false;
    private Marker mLocMarker;

    private static final String KEY_BOOLEAN_GPS_IGNORED = "gps_ignored";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setTheme(R.style.AppTheme);

        if (savedInstanceState == null) {
            gps_enabled = getIntent().getBooleanExtra("gpsEnabled", false);
            network_enabled = getIntent().getBooleanExtra("networkEnabled", false);
            if (!gps_enabled)
                setContentView(R.layout.activity_home_location_disabled);
            else
                setContentView(R.layout.activity_home);
        } else {
            setContentView(R.layout.activity_home);
        }

        initializeNavigationBar();

        mLocWrapper = LocationWrapper.getInstance();
        initializeMap();

        //Create dummy trails
        Random r = new Random();
        trailCollection = new TrailCollection();
        for (int i = 0; i < 5; i++) {
            CustomLocation tempLoc = new CustomLocation(generateRandomLatitude(r), generateRandomLongitude(r));
            trailCollection.addTrail(new Trail(String.valueOf(i), String.valueOf("Trail " + i), (r.nextInt(9000) + 1000.0), (r.nextInt(950) + 50.0), Trail.DURATION_MEDIUM, Trail.DIFFICULTY_MEDIUM, tempLoc, "gear", "conds", true));
        }

        addTrailCollectionMarkersToMap(trailCollection);

        updateMap();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_BOOLEAN_GPS_IGNORED, gps_ignored);
    }

    @Override
    protected void onRestoreInstanceState(Bundle inState) {
        super.onRestoreInstanceState(inState);
        if (inState.containsKey(KEY_BOOLEAN_GPS_IGNORED)) {
            gps_ignored = inState.getBoolean(KEY_BOOLEAN_GPS_IGNORED);
        }
    }

    public Double generateRandomLatitude(Random r) {
        Double baseLat = LocationWrapper.OSWEGO_COUNTY.latitude;
        int pos_neg = r.nextInt(2);
        if (pos_neg == 0) { //Add
            return baseLat + (r.nextDouble() / 3);
        } else { //Subtract
            return baseLat - (r.nextDouble() / 3);
        }
    }

    public Double generateRandomLongitude(Random r) {
        Double baseLng = LocationWrapper.OSWEGO_COUNTY.longitude;
        int pos_neg = r.nextInt(2);
        if (pos_neg == 0) { //Add
            return baseLng + (r.nextDouble() / 3);
        } else { //Subtract
            return baseLng - (r.nextDouble() / 3);
        }
    }

    public void addTrailCollectionMarkersToMap(TrailCollection tc) {
        mLocWrapper.clearMap(mMap);
        for (int i = 0; i < tc.getSize(); i++) {
            Marker temp = mLocWrapper.addTrailMarker(mMap, tc.getTrailAtIndex(i),false);
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
            Intent intent = new Intent(getApplicationContext(), ActivitySearchTrail.class);
            startActivity(intent);
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
        navSpinner.add(new SpinnerNavItem(App.NAV_HOME));
        navSpinner.add(new SpinnerNavItem(App.NAV_TRAILS));
        navSpinner.add(new SpinnerNavItem(App.NAV_PROFILE));
        navSpinner.add(new SpinnerNavItem(App.NAV_UPLOAD_PICTURE));
        mAdapter = new NavAdapter(getApplicationContext(), navSpinner);

        mActionBar.setListNavigationCallbacks(mAdapter, this);
        mActionBar.setDisplayShowTitleEnabled(false);
    }

    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
        ActionBar mActionBar = getActionBar();
        assert mActionBar != null;
        if (itemPosition == 1) { //Trails selected
            Intent trails = new Intent(getApplicationContext(), ActivityTrailsList.class);
            startActivity(trails);
            mActionBar.setSelectedNavigationItem(0);
            return true;
        } else if (itemPosition == 2) { //Profile selected
            if (App.isUserLoggedIn()) {
                Intent profile = new Intent(getApplicationContext(), ActivityProfile.class);
                startActivity(profile);
                mActionBar.setSelectedNavigationItem(0);
                return true;
            }
            //Prompt the user to log in
            else {
                promptUserToLogin();
                mActionBar.setSelectedNavigationItem(0);
            }
        }
        else if (itemPosition == 3) {
            Intent upload = new Intent(getApplicationContext(), Upload.class);
            startActivity(upload);
            mActionBar.setSelectedNavigationItem(0);
            return true;
        }
        return false;
    }

    private void promptUserToLogin() {
        AlertDialog.Builder ad = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_DARK);
        ad.setMessage(R.string.dialog_login_message)
                .setTitle(R.string.dialog_login_title)
                .setPositiveButton(R.string.dialog_login_positive_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent login = new Intent(getApplicationContext(), LoginActivity.class);
                        login.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getApplicationContext().startActivity(login);
                    }
                })
                .setNegativeButton(R.string.dialog_login_negative_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        AlertDialog alertDialog = ad.create();
        alertDialog.show();
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

    private void updateMap() {
        Location mLoc;
        if (!gps_enabled) {
            if (!network_enabled) {
                mLoc = mLocWrapper.getCurrentLocation(getApplicationContext());
            } else {
                mLoc = mLocWrapper.getCurrentLocation(getApplicationContext(), LocationManager.NETWORK_PROVIDER);
                if (mLoc == null)
                    mLoc = mLocWrapper.getCurrentLocation(getApplicationContext());
                    Log.e("ActivityHome:updateMap()", "Retrieving location from network provider failed in updateMap()");
            }

        } else {
            mLoc = mLocWrapper.getCurrentLocation(getApplicationContext(), LocationManager.GPS_PROVIDER);
        }


        if (mLoc != null) {
            mLocMarker = mLocWrapper.addMarkerAtGoogleLocation(mMap, mLoc, "My Location", true);
            mLocMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        } else {
            mLocMarker = mLocWrapper.addMarkerAtLatLng(mMap, LocationWrapper.OSWEGO_COUNTY, null, false);
            mLocMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        }


        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (marker.getId().equals(mLocMarker.getId())){
                    return true;
                }
                lastMarkerClicked = marker;
                return false;
            }
        });

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                if (marker.getId().equals(lastMarkerClicked.getId()) && !marker.getId().equals(mLocMarker.getId()))  {
                    Trail temp = trailCollection.getTrailbyName(marker.getTitle());
                    if (temp != null) {
                        Intent activityTrail = new Intent(getApplicationContext(), ActivityTrailTabHostTest.class);
                        activityTrail.putExtra("trail", temp);
                        startActivity(activityTrail);
                    }
                }
            }
        });

        mLocMarker.showInfoWindow();
    }

    public void buttonOpenLocationSettingsOnClick(View view) {
        mLocWrapper.openLocationSettings(this, true);
        if (mLocWrapper.isGPSProviderEnabled(getApplicationContext())) {
            removeExtraViews();
        }
    }

    public void buttonIngoreGPSDisabledOnClick(View view) {
        removeExtraViews();
        gps_ignored = true;
    }


    private void removeExtraViews() {
        ViewGroup viewGroup = (ViewGroup) findViewById(R.id.home_relativelayout_extras);
        viewGroup.removeAllViewsInLayout();
        ViewGroup viewGroupRoot = (ViewGroup) findViewById(R.id.home_linearlayout_root);
        viewGroupRoot.removeView(viewGroup);
    }
}
