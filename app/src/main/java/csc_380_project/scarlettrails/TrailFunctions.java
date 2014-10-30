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

    // constructor
    public TrailFunctions(){
        jsonParser = new JSONParser();
    }

    /**
     * function get Trail By zipcode or city
     * @param zipcode
     * @param city
     * */
    public JSONObject getTrailByZipcodeOrCity(String zipcode, String city){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", getTrail));
        params.add(new BasicNameValuePair("zipcode", zipcode));
        params.add(new BasicNameValuePair("city", city));
        params.add(new BasicNameValuePair("name", null));
        JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);
        // return json
        // Log.e("JSON", json.toString());
        return json;
    }

    /**
     * function get Trail By trail Name
     * @param name
     * */
    public JSONObject getTrailByName(String name){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", getTrail));
        params.add(new BasicNameValuePair("zipcode", null));
        params.add(new BasicNameValuePair("city", null));
        params.add(new BasicNameValuePair("name", name));
        JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);
        // return json
        // Log.e("JSON", json.toString());
        return json;
    }
}