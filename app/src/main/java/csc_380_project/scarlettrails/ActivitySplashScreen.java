package csc_380_project.scarlettrails;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Nathan on 10/19/2014.
 */
public class ActivitySplashScreen extends Activity {

    // JSON Response nodenames
    public static String KEY_SUCCESS = "success";
    public static String USER_ID = "user_id";
    public static String FIRST_NAME = "first_name";
    public static String LAST_NAME = "last_name";
    public static String EMAIL = "email";
    public static String DOB = "dob";
    public static String USERNAME = "username";
    public static String INTERESTS = "interests";
    public static String PICTURE_URL = "picture_url";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        setTheme(R.style.SplashTheme);
        LocationWrapper mLocWrapper = LocationWrapper.getInstance();
        boolean gps_enabled = mLocWrapper.isGPSProviderEnabled(getApplicationContext());
        boolean network_enabled = mLocWrapper.isNetworkProviderEnabled(getApplicationContext());

        SharedPreferences settings = getSharedPreferences("UserInfo", 0);
        if(!settings.getString("Username", "").isEmpty() && !settings.getString("Password", "").isEmpty()) {
            String username = settings.getString("Username", "");
            String password = settings.getString("Password", "");

            UserFunctions userFunction = new UserFunctions();
            JSONObject json = userFunction.loginUser(username, password);

            if(isNetworkAvailable()) {
                // check for login response
                try {
                    if (json.getString(KEY_SUCCESS) != null) {
                        String res = json.getString(KEY_SUCCESS);
                        if (Integer.parseInt(res) == 1) {
                            // user successfully logged in
                            JSONObject json_user = json.getJSONObject("user");
                            Profile profile = new Profile(json.getString(USER_ID),
                                    json_user.getString(FIRST_NAME),
                                    json_user.getString(LAST_NAME),
                                    json_user.getString(EMAIL),
                                    json_user.getString(DOB),
                                    json_user.getString(USERNAME),
                                    json_user.getString(INTERESTS),
                                    json_user.getString(PICTURE_URL));

                            App.setUserLoggedIn(true);
                            App.setUserProfile(profile);

                            // Launch Dashboard Screen
                            Intent dashboard = new Intent(getApplicationContext(), ActivityHome.class);
                            dashboard.putExtra("gpsEnabled", getIntent().getBooleanExtra("gpsEnabled", false));
                            dashboard.putExtra("networkEnabled", getIntent().getBooleanExtra("networkEnabled", false));

                            // Close all views before launching Dashboard
                            dashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(dashboard);

                            // Close Login Screen
                            finish();
                        } else {
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            if (!gps_enabled)
                                intent.putExtra("gpsEnabled", false);
                            else
                                intent.putExtra("gpsEnabled", true);
                            if (!network_enabled)
                                intent.putExtra("networkEnabled", false);
                            else
                                intent.putExtra("networkEnabled", true);
                            startActivity(intent);
                        }
                    }
                } catch (JSONException e) {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    if (!gps_enabled)
                        intent.putExtra("gpsEnabled", false);
                    else
                        intent.putExtra("gpsEnabled", true);
                    if (!network_enabled)
                        intent.putExtra("networkEnabled", false);
                    else
                        intent.putExtra("networkEnabled", true);
                    startActivity(intent);
                }
            }
            else {
                settings = getSharedPreferences("UserInfo", 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.remove("Username");
                editor.remove("PassWord");
                editor.commit();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        }

        else {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            if (!gps_enabled)
                intent.putExtra("gpsEnabled", false);
            else
                intent.putExtra("gpsEnabled", true);
            if (!network_enabled)
                intent.putExtra("networkEnabled", false);
            else
                intent.putExtra("networkEnabled", true);
            startActivity(intent);
        }

        try {
            SAXParserFactory mSAXParserFactory = SAXParserFactory.newInstance();
            SAXParser mSAXPaser = mSAXParserFactory.newSAXParser();

            TrailXMLHandler handler = new TrailXMLHandler();

            AssetManager mngr = getAssets();
            InputStream xml = mngr.open("trails.xml");

            mSAXPaser.parse(xml, handler);

            App.setTrailCollection(handler.returnTrails());
        } catch (ParserConfigurationException|SAXException|IOException e){
            Log.e("ActivitySplashScreen - XML Parsing","Alex said never to do this but it's happening.");
            e.printStackTrace();
        }

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.finish();
    }
}
