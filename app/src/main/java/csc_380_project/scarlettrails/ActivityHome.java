package csc_380_project.scarlettrails;

import android.app.ActionBar;
import android.content.Intent;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
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
        mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

        navSpinner = new ArrayList<SpinnerNavItem>();
        //This is how you enter new navigation items. Please use the format provided on next line.
        navSpinner.add(new SpinnerNavItem("Home"));
        navSpinner.add(new SpinnerNavItem("Trail"));
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
           Intent intent = new Intent(getApplicationContext(), ActivityTrail.class);
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
