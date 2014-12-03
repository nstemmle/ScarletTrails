package csc_380_project.scarlettrails;

/**
 * Created by rafaelamfonseca on 11/5/14.
 */
import java.util.ArrayList;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.ListActivity;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ActivityTrailsList extends ListActivity implements ActionBar.OnNavigationListener {

    public static String KEY_SUCCESS = "success";
    public static String KEY_ERROR_MSG = "error_msg";
    public static String TAG_TRAILLIST = "trailList";
    public static String TRAIL_ID = "trail_id";
    public static String NAME = "name";
    public static String LOCATION_ID = "location_id";
    public static String X = "x";
    public static String Y = "y";
    public static String LENGTH = "distance";
    public static String PARK = "park";
    public static String TYPE = "type";
    public static String DESCRIPTOR = "descriptor";
    public static String RATING = "rating";
    //public static String DISTANCE = "distance";
    //public static String ELEVATION = "elevation";
    //public static String DURATION = "duration";
    //public static String DIFFICULTY = "difficulty";
    //public static String ZIPCODE = "zipcode";
    //public static String CITY = "city";
    //public static String STATE = "state";
    //public static String COUNTRY = "country";
    //public static String GEAR = "gear";
    //public static String CONDITIONS = "conditions";
    //public static String PET_FRIENDLY = "pet_friendly";

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
        if(adapter.getTrailsArrayList() != null) {
            setListAdapter(adapter);
        }
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

                        Trail trail = new Trail(
                                json_trail.getString(TRAIL_ID),
                                json_trail.getString(NAME),
                                json_trail.getInt(LENGTH),
                                json_trail.getString(TYPE),
                                json_trail.getString(PARK),
                                json_trail.getString(DESCRIPTOR),
                                json_trail.getDouble(RATING), null,
                                new CustomLocation( json_trail.getString(LOCATION_ID),
                                              json_trail.getDouble(X),
                                              json_trail.getDouble(Y)));

                        // adding each child node to HashMap key => value
                        trailsList.add(trail);
                    }

                    return trailsList;

                } else {
                    // No Trails Found
                    Intent intent = new Intent(getApplicationContext(), ActivitySearchTrail.class);
                    intent.putExtra("KEY_ERROR_MSG", json.getString(KEY_ERROR_MSG));
                    startActivity(intent);
                }
            } else {
                // No Trails Found
                Intent intent = new Intent(getApplicationContext(), ActivitySearchTrail.class);
                intent.putExtra("KEY_ERROR_MSG", json.getString(KEY_ERROR_MSG));
                startActivity(intent);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
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
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Trail trail = (Trail) getListAdapter().getItem(position);
        Intent intent = new Intent(getApplicationContext(), ActivityTrailTabHostTest.class);
        intent.putExtra("trail", trail);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.actionbar_search){
            Intent intent = new Intent(getApplicationContext(), ActivitySearchTrail.class);
            startActivity(intent);
        }
        else if(id == R.id.actionbar_logout) {
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
    private void initializeNavigationBar() {
        ActionBar mActionBar = getActionBar();
        mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

        navSpinner = new ArrayList<SpinnerNavItem>();
        //This is how you enter new navigation items. Please use the format provided on next line.
        navSpinner.add(new SpinnerNavItem(App.NAV_TRAILS));
        navSpinner.add(new SpinnerNavItem(App.NAV_HOME));
        navSpinner.add(new SpinnerNavItem(App.NAV_PROFILE));
        mAdapter = new NavAdapter(getApplicationContext(), navSpinner);

        mActionBar.setListNavigationCallbacks(mAdapter, this);
        mActionBar.setDisplayShowTitleEnabled(false);
    }

    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
        ActionBar mActionBar = getActionBar();
        assert mActionBar != null;
        if (itemPosition == 1) { //Home selected
            Intent home = new Intent(getApplicationContext(), ActivityHome.class);
            startActivity(home);
            mActionBar.setSelectedNavigationItem(0);
            return true;
        }

        else if (itemPosition == 2) { //Profile selected
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
}
