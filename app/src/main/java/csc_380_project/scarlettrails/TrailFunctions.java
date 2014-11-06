package csc_380_project.scarlettrails;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.content.Context;

public class TrailFunctions {

    private JSONParser jsonParser;

    // Testing in localhost using wamp or xampp
    // use http://10.0.2.2/ to connect to your localhost ie http://localhost/
    private static String loginURL = "http://teamscarlet.webuda.com/";
    private static String registerURL = "http://teamscarlet.webuda.com/";

    private static String getTrail = "getTrail";
    private static String getById = "getById";
    private static String getByZipCityName = "getByZipCityName";
    private static String getAllTrails = "getAllTrails";

    // constructor
    public TrailFunctions(){
        jsonParser = new JSONParser();
    }

    /**
     * function get Trail By Trail identification
     * @param searchKey
     * */
    public JSONObject getTrailById(String searchKey){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", getTrail));
        params.add(new BasicNameValuePair("getByTag", getById));
        params.add(new BasicNameValuePair("searchKey", searchKey));
        JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);
        // return json
        // Log.e("JSON", json.toString());
        return json;
    }

    /**
     * function get Trail By zipcode or city or name
     * @param searchKey
     * */
    public JSONObject getTrailByZipcodeOrCityOrName(String searchKey){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", getTrail));
        params.add(new BasicNameValuePair("getByTag", getByZipCityName));
        params.add(new BasicNameValuePair("searchKey", searchKey));
        JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);
        // return json
        // Log.e("JSON", json.toString());
        return json;
    }

    /**
     * function get all Trails
     * */
    public JSONObject getAllTrails(){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", getTrail));
        params.add(new BasicNameValuePair("getByTag", getAllTrails));
        params.add(new BasicNameValuePair("searchKey", null));
        JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);
        // return json
        // Log.e("JSON", json.toString());
        return json;
    }
}