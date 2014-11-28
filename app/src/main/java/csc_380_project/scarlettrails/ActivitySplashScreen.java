package csc_380_project.scarlettrails;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Nathan on 10/19/2014.
 */
public class ActivitySplashScreen extends Activity {

    // JSON Response node names
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

            // check for login response
            try {
                if (json.getString(KEY_SUCCESS) != null) {
                    String res = json.getString(KEY_SUCCESS);
                    if(Integer.parseInt(res) == 1){
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
                        dashboard.putExtra("gpsEnabled", getIntent().getBooleanExtra("gpsEnabled",false));
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
