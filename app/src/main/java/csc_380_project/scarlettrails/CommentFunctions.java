package csc_380_project.scarlettrails;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.content.Context;

public class CommentFunctions {

    private JSONParser jsonParser;

    // Testing in localhost using wamp or xampp
    // use http://10.0.2.2/ to connect to your localhost ie http://localhost/
    private static String loginURL = "http://teamscarlet.webuda.com/";
    private static String registerURL = "http://teamscarlet.webuda.com/";

    private static String getComment = "getComment";
    private static String setComment = "setComment";

    // constructor
    public CommentFunctions(){
        jsonParser = new JSONParser();
    }

    /**
     * function get Trail By Trail identification
     * */
    public JSONObject getAllComments(){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", getComment));
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
    public JSONObject getTrailComments(String trailId){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", getComment));
        params.add(new BasicNameValuePair("trail_id", trailId));
        JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);
        // return json
        // Log.e("JSON", json.toString());
        return json;
    }

    public JSONObject setComment(String userId, String trailId, String commmentText){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", setComment));
        params.add(new BasicNameValuePair("user_id", userId));
        params.add(new BasicNameValuePair("trail_id", trailId));
        params.add(new BasicNameValuePair("comment_text", commmentText));
        JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);
        // return json
        // Log.e("JSON", json.toString());
        return json;
    }
}