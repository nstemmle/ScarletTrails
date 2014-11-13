package csc_380_project.scarlettrails;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;

import java.util.ArrayList;

/**
 * Created by Nathan on 10/20/2014.
 */
public class TabForecast extends Activity { //implements ActionBar.OnNavigationListener {
    private GoogleMap mMap;
    private LocationWrapper mLocWrapper;
    private NavAdapter mAdapter;
    private ArrayList<SpinnerNavItem> navSpinner;
    private Trail mTrail;
    private static String TAG = "TabForecast";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTrail = ActivityTrailTabHostTest.mTrail;
        setContentView(R.layout.forecast);

        //Forecast first = mTrail.createForecast();

        //populatePageWithForecastInfo(first, first, first, first, first);
    }

    public void populatePageWithForecastInfo(Forecast a, Forecast b, Forecast c, Forecast d, Forecast e) {
        //first day
        //Day
        Log.e(TAG, "forecast a != null: " + String.valueOf(a != null));
        Log.e(TAG, a.toString());
        ((TextView) findViewById(R.id.forecast_textview_firstday_day)).setText(a.getDate());

        //description
        ((ImageView)findViewById(R.id.forecast_imgview_firstday_clouds)).setImageDrawable(imgselector(a.getDescription()));

        //maxtemp
        ((TextView) findViewById(R.id.forecast_textview_firstday_maxtemp)).setText(a.getTempMax() + "°F");

        //mintemp
        ((TextView) findViewById(R.id.forecast_textview_firstday_mintemp)).setText(a.getTempMin() + "°F");

        //second day
        //Day
        ((TextView) findViewById(R.id.forecast_textview_secondday_day)).setText(b.getDate());

        //description
        ((ImageView)findViewById(R.id.forecast_imgview_secondday_clouds)).setImageDrawable(imgselector(b.getDescription()));

        //maxtemp
        ((TextView) findViewById(R.id.forecast_textview_secondday_maxtemp)).setText(b.getTempMax() + "°F");

        //mintemp
        ((TextView) findViewById(R.id.forecast_textview_secondday_mintemp)).setText(b.getTempMin() + "°F");
        //third day
        //Day
        ((TextView) findViewById(R.id.forecast_textview_thirdday_day)).setText(c.getDate());

        //description
        ((ImageView)findViewById(R.id.forecast_imgview_thirdday_clouds)).setImageDrawable(imgselector(c.getDescription()));

        //maxtemp
        ((TextView) findViewById(R.id.forecast_textview_thirdday_maxtemp)).setText(c.getTempMax() + "°F");

        //mintemp
        ((TextView) findViewById(R.id.forecast_textview_thirdday_mintemp)).setText(c.getTempMin() + "°F");
        //fourth day
        //Day
        ((TextView) findViewById(R.id.forecast_textview_fourthday_day)).setText(d.getDate());

        //description
        ((ImageView)findViewById(R.id.forecast_imgview_fourthday_clouds)).setImageDrawable(imgselector(d.getDescription()));

        //maxtemp
        ((TextView) findViewById(R.id.forecast_textview_fourthday_maxtemp)).setText(d.getTempMax() + "°F");

        //mintemp
        ((TextView) findViewById(R.id.forecast_textview_fourthday_mintemp)).setText(d.getTempMin() + "°F");
        //fifth day
        //Day
        ((TextView) findViewById(R.id.forecast_textview_fifthday_day)).setText(e.getDate());

        //description
        ((ImageView)findViewById(R.id.forecast_imgview_fifthday_clouds)).setImageDrawable(imgselector(e.getDescription()));

        //maxtemp
        ((TextView) findViewById(R.id.forecast_textview_fifthday_maxtemp)).setText(e.getTempMax() + "°F");

        //mintemp
        ((TextView) findViewById(R.id.forecast_textview_fifthday_mintemp)).setText(e.getTempMin() + "°F");
    }

    //need to change this method, may put in forcast class to decide which drawable to use.
    public Drawable imgselector(int description) {
        return getResources().getDrawable(R.drawable.sunny);
    }
}
