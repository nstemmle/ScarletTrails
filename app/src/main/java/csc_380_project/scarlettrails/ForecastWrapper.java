package csc_380_project.scarlettrails;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Kheiyasa on 10/23/2014.
 */

public class ForecastWrapper extends AsyncTask<String, Void, String> {

    private Forecast forecast;

    //public static final String BASE_URL = "http://api.openweathermap.org/data/2.5/forecast/city?id=5130081&APPID=322d5966d76cb53db68e74205b3d096f";
    public static final String URL_COORDS_START = "http://api.openweathermap.org/data/2.5/weather?lat=";
    public static final String URL_COORDS_MID = "&lon=";
    public static final String URL_COORDS_END = "&cnt=10&mode=json";

    private static final String TAG = "FORECAST_WRAPPER";

    public ForecastWrapper(Forecast forecast){
        this.forecast = forecast;
    }
    public ForecastWrapper(){}

    public String getWeatherContent(double lat, double lng, boolean h)  {
        String mURL;
        if (h) {
            mURL = URL_COORDS_START + lat + URL_COORDS_MID + lng;
        } else {
            mURL = URL_COORDS_START + lat + URL_COORDS_MID + lng + URL_COORDS_END;
        }
        Log.e(TAG, "mURL: " + mURL);

        HttpURLConnection connection = null;
        InputStream inputStream = null;
        try{
        connection = (HttpURLConnection) (new URL(mURL)).openConnection();
        Log.e(TAG, "1 - connection != null: " + String.valueOf(connection != null));

        connection.setRequestMethod("GET");
        Log.e(TAG, "2 - connection != null: " + String.valueOf(connection != null));
        connection.setDoInput(true);
        Log.e(TAG, "3 - connection != null: " + String.valueOf(connection != null));
        connection.setDoOutput(true);
        Log.e(TAG, "4 - connection != null: " + String.valueOf(connection != null));
        connection.connect();
        Log.e(TAG, "5 - connection != null: " + String.valueOf(connection != null));

        // read response
        StringBuffer buffer = new StringBuffer();
        inputStream = connection.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = null;
        while ((line = bufferedReader.readLine()) != null)
            buffer.append(line + "\r\n");

        inputStream.close();
        connection.disconnect();

        return buffer.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try { inputStream.close(); } catch(Throwable t) {}
            try { connection.disconnect(); } catch(Throwable t) {}
        }
        return null;
    }

    @Override
    protected String doInBackground(String... strings) {

        String response = "";

        // calls http get using the url passed
        for(String url: strings){
            DefaultHttpClient client = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);

            try{
                // the http request for the weather data
                HttpResponse execute = client.execute(httpGet);
                // gets the content of the result returned
                InputStream content = execute.getEntity().getContent();

                BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                String string = "";

                while((string = buffer.readLine())!=null){
                    response += string;
                }
            }
            catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return response;
    }

    public static Forecast getWeather(String info, int num) throws JSONException {

        JSONObject jObj = new JSONObject(info);

        JSONArray jArray = jObj.getJSONArray("list");
        JSONObject listJSON = jArray.getJSONObject(num);

        int temp = getInt("main", listJSON);

        JSONObject mainJSON = getObject("main", jObj);
        int tempmax = ConvertTempToF(getInt("temp_max", mainJSON));
        int tempmin = ConvertTempToF(getInt("temp_min", mainJSON));

        JSONObject weatherJSON = getObject("weather", jObj);
        int weathrid = getInt("id", weatherJSON);

        JSONObject sysJSON = getObject("sys", jObj);
        java.util.Date sunSetTime = new Date((long)sysJSON.getLong("sunset")*1000);
        java.util.Date sunRiseTime = new Date((long)sysJSON.getLong("sunrise")*1000);

        java.util.Date currentDate = new Date((long)listJSON.getLong("dt")*1000);

        return new Forecast(formatDate(currentDate), tempmin, tempmax, formatSun(sunRiseTime), formatSun(sunSetTime), weathrid);
    }

    protected static String formatDate(Date date){
        TimeZone timeZone = TimeZone.getTimeZone("EST");
        DateFormat dateFormat = new SimpleDateFormat("EEE, MMM dd, ' ' yy, hh:mm");
        dateFormat.setTimeZone(timeZone);
        System.out.print(dateFormat.format(date));
        return dateFormat.format(date);
    }

    protected static String formatSun(Date date){
        TimeZone timeZone = TimeZone.getTimeZone("EST");
        DateFormat dateFormat = new SimpleDateFormat("hh:mm");
        dateFormat.setTimeZone(timeZone);
        System.out.print(dateFormat.format(date));
        return dateFormat.format(date);
    }
    // converts temperature from Kelvin to F
    private static int ConvertTempToF(int temp){
        return (int)((temp-273.15)*(9/5)+32);
    }

    private static JSONObject getObject(String tagName, JSONObject jObj)  throws JSONException {
        JSONObject subObj = jObj.getJSONObject(tagName);
        return subObj;
    }

    private static String getString(String tagName, JSONObject jObj) throws JSONException {
        return jObj.getString(tagName);
    }


    private static int  getInt(String tagName, JSONObject jObj) throws JSONException {
        return jObj.getInt(tagName);
    }
}

