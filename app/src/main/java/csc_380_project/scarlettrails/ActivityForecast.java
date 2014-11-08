package csc_380_project.scarlettrails;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Scott on 11/1/2014.
 */
public class ActivityForecast extends FragmentActivity implements ActionBar.OnNavigationListener {
    private NavAdapter mAdapter;
    private ArrayList<SpinnerNavItem> navSpinner;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.forecast);

        initializeNavigationBar();

        //populate page with forecast

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
        assert mActionBar != null;
        mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

        navSpinner = new ArrayList<SpinnerNavItem>();
        //This is how you enter new navigation items. Please use the format provided on next line.
        navSpinner.add(new SpinnerNavItem("Trail"));
        navSpinner.add(new SpinnerNavItem("Home"));
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
        return false;
    }

    public void populatePageWithForecastInfo(Forecast a, Forecast b, Forecast c, Forecast d, Forecast e) {
        //first day
            //Day
            //Will figure out how to get day out of date provided

            //rain percentage
            ((TextView)findViewById(R.id.forecast_textview_firstday_percipitation)).setText(a.getPrecipitation() + "%");

            //clouds
            ((ImageView)findViewById(R.id.forecast_imgview_firstday_clouds)).setBackgroundDrawable(cloudSelector(a.getClouds()));

            //maxtemp
            ((TextView)findViewById(R.id.forecast_textview_firstday_maxtemp)).setText(a.getTempMax() + "°F");

            //mintemp
            ((TextView)findViewById(R.id.forecast_textview_firstday_mintemp)).setText(a.getTempMin() + "°F");

        //second day
            //Day
            //Will figure out how to get day out of date provided

            //rain percentage
            ((TextView)findViewById(R.id.forecast_textview_secondday_percipitation)).setText(b.getPrecipitation() + "%");

            //clouds
            ((ImageView)findViewById(R.id.forecast_imgview_secondday_clouds)).setBackgroundDrawable(cloudSelector(b.getClouds()));

            //maxtemp
            ((TextView)findViewById(R.id.forecast_textview_secondday_maxtemp)).setText(b.getTempMax() + "°F");

            //mintemp
            ((TextView)findViewById(R.id.forecast_textview_secondday_mintemp)).setText(b.getTempMin() + "°F");
        //third day
            //Day
            //Will figure out how to get day out of date provided

            //rain percentage
            ((TextView)findViewById(R.id.forecast_textview_thirdday_percipitation)).setText(c.getPrecipitation() + "%");

            //clouds
            ((ImageView)findViewById(R.id.forecast_imgview_thirdday_clouds)).setBackgroundDrawable(cloudSelector(c.getClouds()));

            //maxtemp
            ((TextView)findViewById(R.id.forecast_textview_thirdday_maxtemp)).setText(c.getTempMax() + "°F");

            //mintemp
            ((TextView)findViewById(R.id.forecast_textview_thirdday_mintemp)).setText(c.getTempMin() + "°F");
        //fourth day
            //Day
            //Will figure out how to get day out of date provided

            //rain percentage
            ((TextView)findViewById(R.id.forecast_textview_fourthday_percipitation)).setText(d.getPrecipitation() + "%");

            //clouds
            ((ImageView)findViewById(R.id.forecast_imgview_fourthday_clouds)).setBackgroundDrawable(cloudSelector(d.getClouds()));

            //maxtemp
            ((TextView)findViewById(R.id.forecast_textview_fourthday_maxtemp)).setText(d.getTempMax() + "°F");

            //mintemp
            ((TextView)findViewById(R.id.forecast_textview_fourthday_mintemp)).setText(d.getTempMin() + "°F");
        //fifth day
            //Day
            //Will figure out how to get day out of date provided

            //rain percentage
            ((TextView)findViewById(R.id.forecast_textview_fifthday_percipitation)).setText(e.getPrecipitation() + "%");

            //clouds
            ((ImageView)findViewById(R.id.forecast_imgview_fifthday_clouds)).setBackgroundDrawable(cloudSelector(e.getClouds()));

            //maxtemp
            ((TextView)findViewById(R.id.forecast_textview_fifthday_maxtemp)).setText(e.getTempMax() + "°F");

            //mintemp
            ((TextView)findViewById(R.id.forecast_textview_fifthday_mintemp)).setText(e.getTempMin() + "°F");
    }
        //need to change this method, may put in forcast class to decide which drawable to use.
   public Drawable cloudSelector(int cloudPercent){
       if (cloudPercent <= 30){
           return getResources().getDrawable(R.drawable.sunny);
       } else if (31 <= cloudPercent && cloudPercent <= 70){
           return getResources().getDrawable(R.drawable.partlycloudy);
       } else {
           return getResources().getDrawable(R.drawable.cloudy);
       }
   }
}
