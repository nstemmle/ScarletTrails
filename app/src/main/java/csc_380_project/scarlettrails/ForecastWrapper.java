package csc_380_project.scarlettrails;

import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
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
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;



public class ForecastWrapper extends AsyncTask<String, Void, String> {

    private Forecast forecast;

    public static final String URL_COORDS_START = "http://api.openweathermap.org/data/2.5/weather?lat=";
    public static final String URL_COORDS_MID = "&lon=";
    public static final String URL_COORDS_END = "&cnt=10&mode=json";



    public ForecastWrapper(Forecast forecast){
        this.forecast = forecast;
    }
    public ForecastWrapper(){}

    public  String getWeatherContent(double lat, double lng, boolean h){
        URL url = null;
        try {
            String mURL;
            if (h) {
                mURL = URL_COORDS_START + lat + URL_COORDS_MID + lng;
            } else {
                mURL = URL_COORDS_START + lat + URL_COORDS_MID + lng + URL_COORDS_END;
            }
            url = new URL(mURL);
            InputStreamReader is = null;

            is = new InputStreamReader(url.openStream());

        BufferedReader input = new BufferedReader(is);
        StringBuffer buffer = new StringBuffer();
        String line = null;
        while ((line = input.readLine()) != null)
            buffer.append(line + "\r\n");

        is.close();
        input.close();
        return buffer.toString();
    } catch (MalformedURLException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
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

    public static Forecast getWeather(String info, int num ) throws JSONException {
        JSONObject jObj = new JSONObject(info);
        int count = num;

        java.util.Date currentDate = new Date((long)jObj.getLong("dt")*1000);

            JSONObject mainJSON = getObject("main", jObj);
            int tempmax = ConvertTempToF(getInt("temp_max", mainJSON));
            int tempmin = ConvertTempToF(getInt("temp_min", mainJSON));

            JSONArray jArr = jObj.getJSONArray("weather");
            JSONObject weatherJSON = jArr.getJSONObject(0);
            int weathrid = getInt("id", weatherJSON);

            JSONObject sysJSON = getObject("sys", jObj);
            java.util.Date sunSetTime = new Date((long)sysJSON.getLong("sunset")*1000);
            java.util.Date sunRiseTime = new Date((long)sysJSON.getLong("sunrise")*1000);


            Forecast forecast = new Forecast(formatDate(currentDate), tempmin, tempmax, formatSun(sunRiseTime), formatSun(sunSetTime), weathrid);
            return forecast;

    }

    protected static String formatDate(Date date){
        TimeZone timeZone = TimeZone.getTimeZone("EST");
        DateFormat dateFormat = new SimpleDateFormat("MMM dd");
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
