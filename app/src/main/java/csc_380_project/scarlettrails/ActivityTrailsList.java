package csc_380_project.scarlettrails;

/**
 * Created by rafaelamfonseca on 11/5/14.
 */
import java.util.ArrayList;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.app.ListActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ActivityTrailsList extends ListActivity implements ActionBar.OnNavigationListener {

    private static String KEY_SUCCESS = "success";
    private static String KEY_ERROR_MSG = "error_msg";
    private static String TAG_TRAILLIST = "trailList";
    private static String TRAIL_ID = "trail_id";
    private static String NAME = "name";
    private static String DISTANCE = "distance";
    private static String ELEVATION = "elevation";
    private static String DURATION = "duration";
    private static String DIFFICULTY = "difficulty";
    private static String LOCATION_ID = "location_id";
    private static String X = "x";
    private static String Y = "y";
    private static String ZIPCODE = "zipcode";
    private static String CITY = "city";
    private static String STATE = "state";
    private static String COUNTRY = "country";
    private static String GEAR = "gear";
    private static String CONDITIONS = "conditions";
    private static String PET_FRIENDLY = "pet_friendly";

    private ArrayList<SpinnerNavItem> navSpinner;
    private NavAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setTheme(R.style.AppTheme);
        initializeNavigationBar();

        Bundle extras = getIntent().getExtras();
        String searchKey = "";
        if (extras != null) {
            searchKey = extras.getString("SEARCH_KEY");
        }

        // 1. pass context and data to the custom adapter
        Adapter adapter = new Adapter(getApplicationContext(), generateData(searchKey));
        //2. setListAdapter
        setListAdapter(adapter);
    }

    private ArrayList<Trail> generateData(String searchKey) {

        TrailFunctions trailFunction = new TrailFunctions();
        JSONObject json = new JSONObject();
        ArrayList<Trail> trailsList = new ArrayList<Trail>();

        if(searchKey != null && !searchKey.isEmpty()) {
             json = trailFunction.getTrailByZipcodeOrCityOrName(searchKey);
        }
        else {
             json = trailFunction.getAllTrails();
        }

        try {
            if (json.getString(KEY_SUCCESS) != null) {
                String res = json.getString(KEY_SUCCESS);
                if (Integer.parseInt(res) == 1) {

                    JSONArray trails = json.getJSONArray(TAG_TRAILLIST);

                    // looping through All Trails
                    for (int i = 0; i < trails.length(); i++) {
                        JSONObject json_trail = trails.getJSONObject(i);
                        // Storing each json item in variable

                        Forecast forecast = new Forecast();
                        Trail trail = new Trail(
                                  json_trail.getString(TRAIL_ID)
                                , json_trail.getString(NAME)
                                , json_trail.getDouble(DISTANCE)
                                , json_trail.getDouble(ELEVATION)
                                , json_trail.getString(DURATION)
                                , json_trail.getString(DIFFICULTY)
                                , new Location(
                                               json_trail.getString(LOCATION_ID)
                                             , json_trail.getDouble(X)
                                             , json_trail.getDouble(Y)
                                             , json_trail.getString(ZIPCODE)
                                             , json_trail.getString(CITY)
                                             , json_trail.getString(STATE)
                                             , json_trail.getString(COUNTRY))
                                , json_trail.getString(GEAR)
                                , json_trail.getString(CONDITIONS)
                                , json_trail.getBoolean(PET_FRIENDLY));

                        trail.setForecast(forecast);

                        // adding each child node to HashMap key => value
                        trailsList.add(trail);
                    }

                    return trailsList;

                } else {
                    // Error in login
                    //loginErrorMsg.setText(json.getString(KEY_ERROR_MSG));
                }
            } else {
                // Error in login
                //loginErrorMsg.setText(json.getString(KEY_ERROR_MSG));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
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
        navSpinner.add(new SpinnerNavItem("Profile"));
        navSpinner.add(new SpinnerNavItem("Home"));
        navSpinner.add(new SpinnerNavItem("Trail"));
        mAdapter = new NavAdapter(getApplicationContext(), navSpinner);

        mActionBar.setListNavigationCallbacks(mAdapter, this);
        mActionBar.setDisplayShowTitleEnabled(false);
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
            Intent intent = new Intent(getApplicationContext(), ActivityTrail.class);
            //if (intent.resolveActivity(getApplicationContext().getPackageManager()) != null)
            startActivity(intent);
            return true;
        }
        return false;
    }
}