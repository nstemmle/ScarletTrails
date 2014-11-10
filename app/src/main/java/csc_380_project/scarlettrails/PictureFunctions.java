package csc_380_project.scarlettrails;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.content.Context;

public class PictureFunctions {

    private JSONParser jsonParser;

    // Testing in localhost using wamp or xampp
    // use http://10.0.2.2/ to connect to your localhost ie http://localhost/
    private static String loginURL = "http://teamscarlet.webuda.com/";
    private static String registerURL = "http://teamscarlet.webuda.com/";

    private static String getPicture = "getPicture";
    private static String setPicture = "setPicture";

    // constructor
    public PictureFunctions(){
        jsonParser = new JSONParser();
    }

    /**
     * function get Trail By Trail identification
     * */
    public JSONObject getAllPictures(){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", getPicture));
        params.add(new BasicNameValuePair("user_id", null));
        params.add(new BasicNameValuePair("trail_id", null));
        JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);
        // return json
        // Log.e("JSON", json.toString());
        return json;
    }

    /**
     * function get Trail By zipcode or city or name
     * @param trailId
     * */
    public JSONObject getTrailPictures(String trailId){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", getPicture));
        params.add(new BasicNameValuePair("user_id", null));
        params.add(new BasicNameValuePair("trail_id", trailId));
        JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);
        // return json
        // Log.e("JSON", json.toString());
        return json;
    }

    /**
     * function get Trail By zipcode or city or name
     * @param userId
     * */
    public JSONObject getUserPictures(String userId){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", getPicture));
        params.add(new BasicNameValuePair("user_id", userId));
        params.add(new BasicNameValuePair("trail_id", null));
        JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);
        // return json
        // Log.e("JSON", json.toString());
        return json;
    }

    public JSONObject setPicture(String userId, String trailId, String pictureContent){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", setPicture));
        params.add(new BasicNameValuePair("user_id", userId));
        params.add(new BasicNameValuePair("trail_id", trailId));
        params.add(new BasicNameValuePair("pictureContent", pictureContent));
        JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);
        // return json
        // Log.e("JSON", json.toString());
        return json;
    }
}