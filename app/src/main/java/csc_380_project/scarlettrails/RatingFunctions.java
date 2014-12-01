package csc_380_project.scarlettrails;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.content.Context;

public class RatingFunctions {

    private JSONParser jsonParser;

    private static String loginURL = "http://teamscarlet.webuda.com/";

    private static String getRating = "getRating";
    private static String setRating = "setRating";

    // constructor
    public RatingFunctions(){
        jsonParser = new JSONParser();
    }

    public JSONObject getRating(String trailId){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", getRating));
        params.add(new BasicNameValuePair("trail_id", trailId));
        JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);
        // return json
        // Log.e("JSON", json.toString());
        return json;
    }

    public JSONObject setRating(String userId, String trailId, String rating){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", setRating));
        params.add(new BasicNameValuePair("user_id", userId));
        params.add(new BasicNameValuePair("trail_id", trailId));
        params.add(new BasicNameValuePair("rating", rating));
        JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);
        // return json
        // Log.e("JSON", json.toString());
        return json;
    }
}