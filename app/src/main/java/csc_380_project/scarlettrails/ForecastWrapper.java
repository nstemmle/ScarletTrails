package csc_380_project.scarlettrails;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


public class ForecastWrapper {

    public static final String URL_COORDS_START = "http://api.openweathermap.org/data/2.5/weather?lat=";
    public static final String URL_COORDS_START2 = "http://api.openweathermap.org/data/2.5/forecast/daily?lat=";
    public static final String URL_COORDS_MID = "&lon=";
    public static final String URL_COORDS_END = "&cnt=10&mode=json";
    public static final String URL_COORDS_COMPLETEEND = "&units=imperial";

    private ForecastWrapper() {
    }

    public static String getWeatherContent(double lat, double lng, boolean h) {
        URL url;
        try {
            String mURL;
            if (h == true) {
                mURL = URL_COORDS_START + lat + URL_COORDS_MID + lng + URL_COORDS_COMPLETEEND;
            } else {
                mURL = URL_COORDS_START2 + lat + URL_COORDS_MID + lng + URL_COORDS_END +URL_COORDS_COMPLETEEND;
            }
            url = new URL(mURL);
            InputStreamReader is = new InputStreamReader(url.openStream());

            BufferedReader input = new BufferedReader(is);
            StringBuffer buffer = new StringBuffer();
            String line;
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

    public static Forecast createForecast(Trail t) {
        String info = (getWeatherContent(t.getLocation().getLatitude(), t.getLocation().getLongitude(), true));
        try {
            return getWeather(info);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Forecast[] createForecastArray(Trail t) {
        String info = (getWeatherContent(t.getLocation().getLatitude(), t.getLocation().getLongitude(), false));
        try {
            return getWeatherFive(info);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Forecast getWeather(String info) throws JSONException {

            JSONObject jObj = new JSONObject(info);

            java.util.Date currentDate = new Date(jObj.getLong("dt") * 1000);

            JSONObject mainJSON = getObject("main", jObj);
            int tempmax = getInt("temp_max", mainJSON);
            int tempmin = getInt("temp_min", mainJSON);

            JSONArray jArr = jObj.getJSONArray("weather");
            JSONObject weatherJSON = jArr.getJSONObject(0);
            int weathrid = getInt("id", weatherJSON);

            JSONObject sysJSON = getObject("sys", jObj);
            java.util.Date sunSetTime = new Date(sysJSON.getLong("sunset") * 1000);
            java.util.Date sunRiseTime = new Date(sysJSON.getLong("sunrise") * 1000);


            Forecast forecast = new Forecast(formatDate(currentDate), tempmin, tempmax, formatSun(sunRiseTime), formatSun(sunSetTime), weathrid);
            return forecast;

    }

    public static Forecast[] getWeatherFive(String info) throws JSONException {
        int count = 0;
        Forecast[] forecastArray = new Forecast[5];
        while (count < 5) {
            JSONObject jObj = new JSONObject(info);

            JSONArray jArray = jObj.getJSONArray("list");
            JSONObject listJSON = jArray.getJSONObject(count);

            java.util.Date currentDate = new Date(listJSON.getLong("dt") * 1000);

            JSONObject mainJSON = getObject("temp", listJSON);
            int tempmax = getInt("max", mainJSON);
            int tempmin = getInt("min", mainJSON);

            JSONArray jArr = listJSON.getJSONArray("weather");
            JSONObject weatherJSON = jArr.getJSONObject(0);
            int weathrid = getInt("id", weatherJSON);

            forecastArray[count] = new Forecast(formatDate(currentDate), tempmin, tempmax, "", "", weathrid);
            count++;
        }
        return forecastArray;
    }

    protected static String formatDate(Date date) {
        TimeZone timeZone = TimeZone.getTimeZone("EST");
        DateFormat dateFormat = new SimpleDateFormat("MMM dd");
        dateFormat.setTimeZone(timeZone);
        System.out.print(dateFormat.format(date));
        return dateFormat.format(date);
    }

    protected static String formatSun(Date date) {
        TimeZone timeZone = TimeZone.getTimeZone("EST");
        DateFormat dateFormat = new SimpleDateFormat("hh:mm");
        dateFormat.setTimeZone(timeZone);
        System.out.print(dateFormat.format(date));
        return dateFormat.format(date);
    }


    private static JSONObject getObject(String tagName, JSONObject jObj) throws JSONException {
        JSONObject subObj = jObj.getJSONObject(tagName);
        return subObj;
    }

    private static int getInt(String tagName, JSONObject jObj) throws JSONException {
        return jObj.getInt(tagName);
    }
}
