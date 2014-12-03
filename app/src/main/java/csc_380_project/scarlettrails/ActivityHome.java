package csc_380_project.scarlettrails;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Nathan on 10/19/2014.
 */
public class ActivityHome extends Activity implements ActionBar.OnNavigationListener {
    private Marker lastClicked;
    private TrailCollection trails;
    private MarkerCollection markers;
    private List<Marker> markersForMap;
    LatLng mapCenter;
    private Location userLocation;
    private GoogleMap mMap;
    private LocationWrapper mLocWrapper;
    private NavAdapter mAdapter;
    private ArrayList<SpinnerNavItem> navSpinner;
    private static int numMarkersDisplayed = 10;
    private static int maxDistance = CustomLocation.FT_PER_MILE * 12;

    private boolean gps_enabled;
    private boolean network_enabled;

    private boolean gps_ignored = false;

    private static final String KEY_BOOLEAN_GPS_IGNORED = "gps_ignored";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setTheme(R.style.AppTheme);

        if (savedInstanceState == null) {
            gps_enabled = getIntent().getBooleanExtra("gpsEnabled", false);
            network_enabled = getIntent().getBooleanExtra("networkEnabled", false);
            if (gps_enabled || network_enabled) {
                App.setGpsIgnored(true);
                removeExtraViews();
            }
            if (!gps_enabled && !App.getGpsIgnored())
                setContentView(R.layout.activity_home_location_disabled);
            else
                setContentView(R.layout.activity_home);
        } else {
            setContentView(R.layout.activity_home);
        }

        initializeNavigationBar();

        mLocWrapper = LocationWrapper.getInstance();
        initializeMap();

        trails = App.getTrailCollection();
        markers = new MarkerCollection();
        markersForMap = new ArrayList<Marker>(numMarkersDisplayed);

        updateDisplayedMarkers();

        updateMap();

        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                if (CustomLocation.distanceBetweenGPSCoords(cameraPosition.target.latitude, cameraPosition.target.longitude, mapCenter.latitude, mapCenter.longitude) >= 500)
                    updateDisplayedMarkers();
                for (Marker m : markersForMap) {
                    if (m != null && lastClicked != null && m.getPosition().equals(lastClicked.getPosition())) {
                        m.showInfoWindow();
                        break;
                     }
                }
                //if (markersForMap.contains(lastClicked))
                //    lastClicked.showInfoWindow();
            }
        });
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                lastClicked  = marker;
                marker.showInfoWindow();
                return false;
            }
        });

        getUserlocation();

        gps_enabled = mLocWrapper.isGPSProviderEnabled(this);
        network_enabled = mLocWrapper.isNetworkProviderEnabled(this);
        if (gps_enabled || network_enabled) {
            removeExtraViews();
            App.setGpsIgnored(true);
        }

    }

    private void setFilterDistance(int miles) {
        maxDistance = CustomLocation.FT_PER_MILE * miles;
    }

    private void updateMap() {
        if (!gps_enabled) {
            if (!network_enabled) {
                userLocation = mLocWrapper.getCurrentLocation(this);
            } else {
                userLocation = mLocWrapper.getCurrentLocation(this, LocationManager.NETWORK_PROVIDER);
                if (userLocation == null)
                    userLocation = mLocWrapper.getCurrentLocation(this);
                Log.e("ActivityHome:updateMap()", "Retrieving location from network provider failed in updateMap()");
            }

        } else {
            userLocation = mLocWrapper.getCurrentLocation(this, LocationManager.GPS_PROVIDER);
        }

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                MarkerWrapper temp = markers.getMarkerById(marker.getId());
                Trail t = null;
                if (temp != null)
                        t = trails.getTrailById(temp.getTrailId());
                if (t != null) {
                    Intent activityTrail = new Intent(getApplicationContext(), ActivityTrailTabHostTest.class);
                    activityTrail.putExtra("trail", t);
                    startActivity(activityTrail);
                }
            }
        });
    }

    public void updateDisplayedMarkers() {
        for (Marker m : markersForMap) {
            m.remove();
        }
        markersForMap.clear();
        mapCenter = mMap.getCameraPosition().target;
        findClosestMarkersToLocation(numMarkersDisplayed, mapCenter);

        for (int i = 0; i < markers.getSize(); i++) {
            if (markers.getMarkerAtIndex(i) != null) {
                markersForMap.add(markers.getMarkerAtIndex(i).createMarker(mMap, mLocWrapper));
            }
        }
    }

    public void findClosestMarkersToLocation(int numMarkers, double lat, double lng) {
        markers.clear();
        CustomLocation center = new CustomLocation(lat, lng);
        int count = 0;
        for (int i = 0; i < trails.getSize(); i++) {
            Trail temp = trails.getTrailAtIndex(i);
            if (CustomLocation.distanceBetweenGPSCoords(temp.getLocation(), center) <= maxDistance && count < numMarkers) {
                markers.addMarker(new MarkerWrapper(temp.getLocation().getLatitude(), temp.getLocation().getLongitude(), String.valueOf(i), temp.getTrailId(), temp.getName()));
                count++;
            }
        }
    }

    public void findClosestMarkersToLocation(int numMarkers, LatLng loc) {
        findClosestMarkersToLocation(numMarkers, loc.latitude, loc.longitude);
    }

    public void findClosestMarkersToLocation(int numMarkers, Location loc) {
        findClosestMarkersToLocation(numMarkers, loc.getLatitude(), loc.getLongitude());
    }

    public void findClosestMarkersToLocation(int numMarkers, CustomLocation loc) {
        findClosestMarkersToLocation(numMarkers, loc.getLatitude(), loc.getLongitude());
    }

    public void addTrailCollectionMarkersToMap(TrailCollection tc) {
        mLocWrapper.clearMap(mMap);
        for (int i = 0; i < tc.getSize(); i++) {
            Trail temp = tc.getTrailAtIndex(i);
            Marker marker = mLocWrapper.addMarkerAtLocation(mMap, temp.getLocation(), temp.getName(), false);
        }
        //tc.addTrailMarkersToMap(mMap, mLocWrapper);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (App.isUserLoggedIn())
            getMenuInflater().inflate(R.menu.action_bar_menu, menu);
        else
            getMenuInflater().inflate(R.menu.action_bar_menu_not_logged_in, menu);
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
        } else if(id == R.id.actionbar_logout) {
            SharedPreferences settings = getSharedPreferences("UserInfo", 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.remove("Username");
            editor.remove("PassWord");
            editor.commit();
            Message myMessage=new Message();
            myMessage.obj="NOTSUCCESS";
            handler.sendMessage(myMessage);
            App.clear();
            finish();
            return true;
        }
        else if (id == R.id.actionbar_edit_profile) {
            if(App.isUserLoggedIn()) {
                Intent intent = new Intent(getApplicationContext(), ActivityEditProfile.class);
                startActivity(intent);
                return true;
            }
            else
                Toast.makeText(this, "You are not logged in. Please, login in to edit your profile", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String loginmsg = (String)msg.obj;
            if(loginmsg.equals("NOTSUCCESS")) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        }
    };

    private void initializeNavigationBar() {
        ActionBar mActionBar = getActionBar();
        assert mActionBar != null;
        mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

        navSpinner = new ArrayList<SpinnerNavItem>();
        //This is how you enter new navigation items. Please use the format provided on next line.
        navSpinner.add(new SpinnerNavItem(App.NAV_HOME));
        navSpinner.add(new SpinnerNavItem(App.NAV_TRAILS));
        navSpinner.add(new SpinnerNavItem(App.NAV_PROFILE));
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

    private void getUserlocation() {
        Location passiveLoc = mLocWrapper.getCurrentLocation(this, LocationManager.PASSIVE_PROVIDER);
        double passiveAcc = 0;
        if (passiveLoc != null) passiveAcc = passiveLoc.getAccuracy();
        Location gpsLoc = mLocWrapper.getCurrentLocation(this, LocationManager.GPS_PROVIDER);
        double gpsAcc = 0;
        if (gpsLoc != null) gpsAcc = gpsLoc.getAccuracy();
        Location networkLoc = mLocWrapper.getCurrentLocation(this, LocationManager.NETWORK_PROVIDER);
        double networkAcc = 0;
        if (networkLoc != null) networkAcc = networkLoc.getAccuracy();

        double bestAcc;
        Location bestLoc;
        if (passiveAcc > gpsAcc)  {
            bestAcc = passiveAcc;
            bestLoc = passiveLoc;
        } else {
            bestAcc = gpsAcc;
            bestLoc = gpsLoc;
        }

        if (networkAcc > bestAcc) {
            bestLoc = networkLoc;
        }
        userLocation = mLocWrapper.getCurrentLocation(this);
        if (bestLoc != null)
            userLocation = (userLocation.getAccuracy() > bestLoc.getAccuracy()) ? userLocation: bestLoc;
        if (userLocation != null) {
            mLocWrapper.centerCameraOnLocation(mMap, userLocation, LocationWrapper.INBETWEEN_ZOOM);
        }
    }

    public void buttonOpenLocationSettingsOnClick(View view) {

        mLocWrapper.openLocationSettings(this, true);
        if (mLocWrapper.isGPSProviderEnabled(getApplicationContext())) {
            removeExtraViews();
        }

    }

    public void buttonIngoreGPSDisabledOnClick(View view) {
        removeExtraViews();
        App.setGpsIgnored(true);
    }


    private void removeExtraViews() {
        ViewGroup viewGroup = (ViewGroup) findViewById(R.id.home_relativelayout_extras);
        viewGroup.removeAllViewsInLayout();
        ViewGroup viewGroupRoot = (ViewGroup) findViewById(R.id.home_linearlayout_root);
        viewGroupRoot.removeView(viewGroup);
    }
}
