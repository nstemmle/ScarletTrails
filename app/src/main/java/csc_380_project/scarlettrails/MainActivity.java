package csc_380_project.scarlettrails;

import android.app.ActionBar;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity implements ActionBar.OnNavigationListener, GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener {

    //These two need to be created before LocationInterface can be instantiated
    private GoogleMap mMap;
    private Geocoder mGeocoder;
    private LocationClient mLocationClient;
    private LocationManager mLocationManager;
    private LocationWrapper mLocWrapper;
    private NavAdapter mAdapter;
    private ArrayList<SpinnerNavItem> navSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getActionBar().hide();

        setContentView(R.layout.splash_screen);

        setContentView(R.layout.activity_main_map);
        getActionBar().show();


        initializeNavigationBar();

        mLocWrapper = LocationWrapper.getInstance();
        initializeMap();
        mLocWrapper.checkMapExists(mMap);
        //http://stackoverflow.com/questions/22704451/open-google-maps-through-intent-for-specific-location-in-android

    }

    private void initializeNavigationBar() {
        getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        navSpinner = new ArrayList<SpinnerNavItem>();
        //This is how you enter new navigation items. Please use the format provided on next line.
        navSpinner.add(new SpinnerNavItem("Trails"));
        navSpinner.add(new SpinnerNavItem("Profile"));
        mAdapter = new NavAdapter(getApplicationContext(), navSpinner);
        //This line here - second parameter should be changed/investigated
        getActionBar().setListNavigationCallbacks(mAdapter, this);

        getActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mLocationClient = new LocationClient(this, this, this);
        mLocationClient.connect();
    }

    @Override
    protected void onStop() {
        mLocationClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
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

    private void initializeMap() {
        if (mMap == null) {
            mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        }

        //Check to see if successful
        mLocWrapper.checkMapExists(mMap);

        //This line currently sets map to center on Oswego County
        //Later replace with something that returns map to state it was previously in
        mLocWrapper.setUpMapWithDefaults(mMap);
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocWrapper.beginListeningForLocationUpdates(mLocationClient);
        Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDisconnected() {
        Toast.makeText(this, "Disconnected. Please re-connect.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        /*
         * Google Play services can resolve some errors it detects.
         * If the error has a resolution, try sending an Intent to
         * start a Google Play services activity that can resolve
         * error.
         */
        //if (connectionResult.hasResolution()) {
        //    try {
        //        // Start an Activity that tries to resolve the error
        //        connectionResult.startResolutionForResult(
        //                this,
        //                CONNECTION_FAILURE_RESOLUTION_REQUEST);
                /*
                 * Thrown if Google Play services canceled the original
                 * PendingIntent
                 */
        //    } catch (IntentSender.SendIntentException e) {
                // Log the error
        //        e.printStackTrace();
        //    }
        //} else {
        //    /*
        //     * If no resolution is available, display a dialog to the
         //    * user with the error.
         //   */
          //  showErrorDialog(connectionResult.getErrorCode());
        //}
    }

    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
        // Switch to be implemented here depending on which item is selected
        return false;
    }

    /*private class GetAddressTask extends AsyncTask<Location, Void, String> {
        Context mContext;

        public GetAddressTask(Context context) {

        }


        @Override
        protected String doInBackground(Location... params) {
            return null;
        }
    }*/


}
