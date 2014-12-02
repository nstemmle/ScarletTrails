package csc_380_project.scarlettrails;

import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by nstemmle on 11/11/14.
 */
public class ActivityTrailTabHostTest extends TabActivity {//implements ActionBar.OnNavigationListener {
    //private NavAdapter mAdapter;
    //private ArrayList<SpinnerNavItem> navSpinner;

    //private boolean gps_enabled;
    //private boolean network_enabled;

    private TabHost mTabHost;
    private String mCurrentTab;

    public static Trail mTrail;

    public static final String M_CURRENT_TAB = "M_CURRENT_TAB";
    //public static final int NUM_TABS = 3;
    public static final String TAB_TRAIL = "TAB_MAP";
    public static final String TAB_GALLERY = "TAB_GALLERY";
    public static final String TAB_FORECAST = "TAB_FORECAST";

    /*public static enum Tabs {
        TAB_TRAIL ("TAB_TRAIL", new Intent(ActivityTrail.class)),
        TAB_GALLERY ("TAB_GALLERY", new Intent(ActivityPictureCollection.class)),
        TAB_FORECAST ("TAB_FORECAST", new Intent(ActivityForecast.class));

        private final String label;
        private final Intent intent;
        Tabs(String tagKey, Intent intent) {
            this.tagKey = tagkey;
            this.Intent = intent;
        }
        private String getLabel() { return label; }
    }*/

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setTheme(R.style.ChildTheme);
        setContentView(R.layout.activity_trail_tab_host_test);

        mTrail = getIntent().getParcelableExtra("trail");

        mTabHost = (TabHost) findViewById(android.R.id.tabhost);

        mTabHost.setup();

        initializeTabs();
        //mTabHost.setOnTabChangedListener(new TabChangeListener());

        if (savedInstanceState == null) {
            //gps_enabled = getIntent().getBooleanExtra("gpsEnabled", false);
            //network_enabled = getIntent().getBooleanExtra("networkEnabled", false);
        } else {
            mCurrentTab = savedInstanceState.getString(M_CURRENT_TAB);
            mTabHost.setCurrentTabByTag(mCurrentTab);
        }

        //initializeNavigationBar();
    }

    private void initializeTabs() {
        TabHost.TabSpec tabSpec;
        TextView textView;

        tabSpec = mTabHost.newTabSpec(TAB_TRAIL);
        tabSpec.setContent(new Intent(this, TabTrail.class));
        tabSpec.setIndicator("Info");
        mTabHost.addTab(tabSpec);
        textView = (TextView) mTabHost.getTabWidget().getChildAt(0).findViewById(android.R.id.title);
        textView.setTextColor(Color.parseColor("#FFFFFF"));

        tabSpec = mTabHost.newTabSpec(TAB_GALLERY);
        tabSpec.setContent(new Intent(this, TabGallery.class));
        tabSpec.setIndicator("Gallery");
        mTabHost.addTab(tabSpec);
        textView = (TextView) mTabHost.getTabWidget().getChildAt(1).findViewById(android.R.id.title);
        textView.setTextColor(Color.parseColor("#FFFFFF"));

        tabSpec = mTabHost.newTabSpec(TAB_FORECAST);
        tabSpec.setContent(new Intent(this, TabForecast.class));
        tabSpec.setIndicator("Forecast");
        mTabHost.addTab(tabSpec);
        textView = (TextView) mTabHost.getTabWidget().getChildAt(2).findViewById(android.R.id.title);
        textView.setTextColor(Color.parseColor("#FFFFFF"));


        /*for (int i = 0; i < NUM_TABS; i++) {
            tabSpec = mTabHost.newTabSpec(TABS_ARRAY[i]);
            tabSpec.setContent(new TabHost.TabContentFactory() {
                public View createTabContent(String tag) {
                    return findViewById(R.id.home_framelayout_content);
                }
            });
            tabSpec.setIndicator(TABS_ARRAY[i]);
            mTabHost.addTab(tabSpec);
        }*/
    }

    /*private class TabChangeListener implements TabHost.OnTabChangeListener {
        @Override
        public void onTabChanged(String tabId) {
            mCurrentTab = tabId;

            if (tabId.equals(TAB_TRAIL)) {

            } else if (tabId.equals(TAB_GALLERY)) {

            } else if (tabId.equals(TAB_FORECAST)) {

            }
        }
    }*/

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(M_CURRENT_TAB, mCurrentTab);
    }

    @Override
    protected void onRestoreInstanceState(Bundle inState) {
        super.onRestoreInstanceState(inState);
        if (inState.containsKey(M_CURRENT_TAB)) {
            mCurrentTab = inState.getString(M_CURRENT_TAB);
            mTabHost.setCurrentTabByTag(mCurrentTab);
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

    /*private void initializeNavigationBar() {
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
        if (itemPosition == 1) { //Trails selected
            Intent trails = new Intent(getApplicationContext(), ActivityTrailsList.class);
            startActivity(trails);
            return true;

        } else if (itemPosition == 2) { //Profile selected
            if (App.isUserLoggedIn()) {
                Intent profile = new Intent(getApplicationContext(), ActivityProfile.class);
                startActivity(profile);
                return true;
            }

            //Prompt the user to log in
            else {
                promptUserToLogin();
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
    }*/
}
