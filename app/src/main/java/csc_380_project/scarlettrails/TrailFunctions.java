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
     * function get Trail By zipcode or city or name
     * @param searchKey
     * */
    public JSONObject getTrailByZipcodeOrCityOrName(String searchKey){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", getTrail));
        params.add(new BasicNameValuePair("searchKey", searchKey));
        JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);
        // return json
        // Log.e("JSON", json.toString());
        return json;
    }
}