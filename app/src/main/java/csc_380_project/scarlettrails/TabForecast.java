package csc_380_project.scarlettrails;

import android.app.Activity;
 import android.os.Bundle;
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
    private Forecast first;
    private Forecast second;
    private Forecast third;
    private Forecast fourth;
    private Forecast fifth;

     public void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         mTrail = ActivityTrailTabHostTest.mTrail;
         setContentView(R.layout.tab_trail_forecast);

         Thread t = new Thread() {
             public void run() {
                 Forecast[] forecastArray = ForecastWrapper.createForecastArray(mTrail);
                 first = forecastArray[0];
                 second = forecastArray[1];
                 third = forecastArray[2];
                 fourth = forecastArray[3];
                 fifth = forecastArray[4];
             }

         };
         t.start();
         try {
             t.join();
         } catch (InterruptedException e) {
             e.printStackTrace();
         }

         populatePageWithForecastInfo(first, second, third, fourth, fifth);

     }

     public void populatePageWithForecastInfo(Forecast a, Forecast b, Forecast c, Forecast d, Forecast e) {
         //first day
         //Day
         ((TextView) findViewById(R.id.forecast_textview_firstday_day)).setText(a.getDate());

         //description


         //maxtemp
         ((TextView) findViewById(R.id.forecast_textview_firstday_maxtemp)).setText(a.getTempMax() + "°F");

         //mintemp
         ((TextView) findViewById(R.id.forecast_textview_firstday_mintemp)).setText(a.getTempMin() + "°F");

         //second day
         //Day
         ((TextView) findViewById(R.id.forecast_textview_secondday_day)).setText(b.getDate());

         //description


         //maxtemp
         ((TextView) findViewById(R.id.forecast_textview_secondday_maxtemp)).setText(b.getTempMax() + "°F");

         //mintemp
         ((TextView) findViewById(R.id.forecast_textview_secondday_mintemp)).setText(b.getTempMin() + "°F");
         //third day
         //Day
         ((TextView) findViewById(R.id.forecast_textview_thirdday_day)).setText(c.getDate());

         //description


         //maxtemp
         ((TextView) findViewById(R.id.forecast_textview_thirdday_maxtemp)).setText(c.getTempMax() + "°F");

         //mintemp
         ((TextView) findViewById(R.id.forecast_textview_thirdday_mintemp)).setText(c.getTempMin() + "°F");
         //fourth day
         //Day
         ((TextView) findViewById(R.id.forecast_textview_fourthday_day)).setText(d.getDate());

         //description


         //maxtemp
         ((TextView) findViewById(R.id.forecast_textview_fourthday_maxtemp)).setText(d.getTempMax() + "°F");

         //mintemp
         ((TextView) findViewById(R.id.forecast_textview_fourthday_mintemp)).setText(d.getTempMin() + "°F");
         //fifth day
         //Day
         ((TextView) findViewById(R.id.forecast_textview_fifthday_day)).setText(e.getDate());

         //description


         //maxtemp
         ((TextView) findViewById(R.id.forecast_textview_fifthday_maxtemp)).setText(e.getTempMax() + "°F");

         //mintemp
         ((TextView) findViewById(R.id.forecast_textview_fifthday_mintemp)).setText(e.getTempMin() + "°F");
     }

     //will add implementation on 11/16
     public void setIcon(int weatherid){
         if (weatherid <= 232 || weatherid ==960 || weatherid == 961){
             //storm
         } else if (weatherid <= 321){
             //light rain
         } else if (weatherid <= 531){
             //heavy rain
         } else if (611 <= weatherid || weatherid <= 616){
             //mix
         } else if (weatherid <= 622){
             //snow
         } else if (weatherid == 800){
             //sunny
         } else if (weatherid == 801){
             //few clouds
         } else if (weatherid <= 803){
             //partly cloudy
         } else if (weatherid == 804 ){
             //cloudy
         } else if (weatherid <= 906 ){
             //hail
         }else{
             //default picture
         }
     }
 }
