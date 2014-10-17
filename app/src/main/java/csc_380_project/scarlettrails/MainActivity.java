package csc_380_project.scarlettrails;

import android.content.Context;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;

public class MainActivity extends FragmentActivity implements GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener {

    //These two need to be created before LocationInterface can be instantiated
    private GoogleMap mMap;
    private Geocoder mGeocoder;
    private LocationClient mLocationClient;
    private LocationManager mLocationManager;
    private LocationWrapper mLocInt;

    private Location mCurrentLocation;
    private LatLng mLatLng;

    private static final LatLng OSWEGO_COUNTY = new LatLng(43.482533, -76.1783739);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_map);
        mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        mLocInt = LocationWrapper.getInstance();
        mLocInt.checkMapExists(mMap);
        //http://stackoverflow.com/questions/22704451/open-google-maps-through-intent-for-specific-location-in-android

    }

    public void onButtonGetLocationPress(View view) {
        //Called when the button identifed by the id "buttonGetLocation" is pressed
        //To add new elements, add the identifier in the ids.xml file
        //Add the element type in your layout.xml file with the relevant attributes
        //Specify the following tag
        //android:onClick="desiredMethodNameHere"
        //This method was created by the line
        //android:onClick="onButtonGetLocationPress"
        mLocInt.addMarker(mMap, mLocInt.getCurrentLocation(), "My Location");
    }

    public void onButtonShowMarkerPress(View view) {
        //Called when the button identifed by the id "buttonShowMarker" is pressed
        //To add new elements, add the identifier in the ids.xml file
        //Add the element type in your layout.xml file with the relevant attributes
        //Specify the following tag
        //android:onClick="desiredMethodNameHere"
        //This method was created by the line
        //android:onClick="onButtonShowMarkerPress"
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

    @Override
    public void onConnected(Bundle bundle) {
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

    private class GetAddressTask extends AsyncTask<Location, Void, String> {
        Context mContext;

        public GetAddressTask(Context context) {

        }


        @Override
        protected String doInBackground(Location... params) {
            return null;
        }
    }


}
