package csc_380_project.scarlettrails;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

 import com.google.android.gms.maps.GoogleMap;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

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

     @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
     public void populatePageWithForecastInfo(Forecast a, Forecast b, Forecast c, Forecast d, Forecast e) {
         //first day
         //Day, for example Monday, R.id.forecast_textview_firstday_day


         //date, for example Nov 16, R.id.forecast_textview_firstday_date



         //description
         (findViewById(R.id.forecast_imgview_firstday_clouds)).setBackground(setIcon(a.getDescription()));

         //maxtemp
         ((TextView) findViewById(R.id.forecast_textview_firstday_maxtemp)).setText(a.getTempMax() + "°F");

         //mintemp
         ((TextView) findViewById(R.id.forecast_textview_firstday_mintemp)).setText(a.getTempMin() + "°F");

         //second day
         //Day


         //date


         //description
         (findViewById(R.id.forecast_imgview_secondday_clouds)).setBackground(setIcon(b.getDescription()));

         //maxtemp
         ((TextView) findViewById(R.id.forecast_textview_secondday_maxtemp)).setText(b.getTempMax() + "°F");

         //mintemp
         ((TextView) findViewById(R.id.forecast_textview_secondday_mintemp)).setText(b.getTempMin() + "°F");

         //third day
         //Day


         //date


         //description
         (findViewById(R.id.forecast_imgview_thirdday_clouds)).setBackground(setIcon(c.getDescription()));

         //maxtemp
         ((TextView) findViewById(R.id.forecast_textview_thirdday_maxtemp)).setText(c.getTempMax() + "°F");

         //mintemp
         ((TextView) findViewById(R.id.forecast_textview_thirdday_mintemp)).setText(c.getTempMin() + "°F");
         //fourth day
         //Day


         //date


         //description
         (findViewById(R.id.forecast_imgview_fourthday_clouds)).setBackground(setIcon(d.getDescription()));

         //maxtemp
         ((TextView) findViewById(R.id.forecast_textview_fourthday_maxtemp)).setText(d.getTempMax() + "°F");

         //mintemp
         ((TextView) findViewById(R.id.forecast_textview_fourthday_mintemp)).setText(d.getTempMin() + "°F");
         //fifth day
         //Day


         //date


         //description
         (findViewById(R.id.forecast_imgview_fifthday_clouds)).setBackground(setIcon(e.getDescription()));

         //maxtemp
         ((TextView) findViewById(R.id.forecast_textview_fifthday_maxtemp)).setText(e.getTempMax() + "°F");

         //mintemp
         ((TextView) findViewById(R.id.forecast_textview_fifthday_mintemp)).setText(e.getTempMin() + "°F");
     }


     public Drawable setIcon(int weatherid){
         if (weatherid <= 232 || weatherid ==960 || weatherid == 961){
             //storm
             return getResources().getDrawable(R.drawable.storm);
         } else if (weatherid <= 321){
             //light rain
             return getResources().getDrawable(R.drawable.lightrain);
         } else if (weatherid <= 531){
             //heavy rain
             return getResources().getDrawable(R.drawable.heavyrain);
         } else if (weatherid >=611 && weatherid <= 616){
             //mix
             return getResources().getDrawable(R.drawable.mix);
         } else if (weatherid <= 622){
             //snow
             return getResources().getDrawable(R.drawable.snow);
         } else if (weatherid == 800){
             //sunny
             return getResources().getDrawable(R.drawable.sunny);
         } else if (weatherid == 801){
             //few clouds
             return getResources().getDrawable(R.drawable.fewclouds);
         } else if (weatherid <= 803){
             //partly cloudy
             return getResources().getDrawable(R.drawable.partlycloudy);
         } else if (weatherid == 804 ){
             //cloudy
             return getResources().getDrawable(R.drawable.cloudy);
         } else if (weatherid <= 906 ){
             //hail
             return getResources().getDrawable(R.drawable.hail);
         }else{
             //default picture
             return getResources().getDrawable(R.drawable.ic_launcher);
         }
     }

    public String dayOfWeek(int n){
        if (n==1){
            return "Sunday";
        }else if(n==7){
            return "Monday";
        }else if(n==2){
            return "Tuesday";
        }else if(n==3){
            return "Wednesday";
        }else if(n==4){
            return "Thursday";
        }else if(n==5){
            return "Friday";
        }else {
            return "Saturday";
        }
    }

    public String monthOfYear(int n){
        if (n==0){
            return "Jan";
        }else if(n==1){
            return "Feb";
        }else if(n==2){
            return "Mar";
        }else if(n==3){
            return "Apr";
        }else if(n==4){
            return "May";
        }else if(n==5){
            return "Jun";
        }else if(n==6){
            return "Jul";
        }else if(n==7) {
            return "Aug";
        }else if(n==8) {
            return "Sep";
        }else if(n==9) {
            return "Oct";
        }else if(n==10) {
            return "Nov";
        }else{
            return "Dec";
        }
    }
 }
