package csc_380_project.scarlettrails;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;

import java.util.concurrent.TimeUnit;

/**
 * Created by Nathan on 10/19/2014.
 */
public class ActivitySplashScreen extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        setTheme(R.style.SplashTheme);
        LocationWrapper mLocWrapper = LocationWrapper.getInstance();
        boolean gps_enabled = mLocWrapper.isGPSProviderEnabled(getApplicationContext());
        boolean network_enabled = mLocWrapper.isNetworkProviderEnabled(getApplicationContext());

        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        if (!gps_enabled)
            intent.putExtra("gpsEnabled", false);
        else
            intent.putExtra("gpsEnabled", true);
        if (!network_enabled)
            intent.putExtra("networkEnabled", false);
        else
            intent.putExtra("networkEnabled", true);
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.finish();
    }
}
