package csc_380_project.scarlettrails;

import android.app.Application;
import android.location.Location;

/**
 * Created by nstemmle on 11/8/14.
 */
public class App extends Application {
    private static boolean logged_in = false;
    private static String profile_id;
    private static Profile user_profile;

    public static final String NAV_HOME = "Home";
    public static final String NAV_TRAILS = "Trails";
    public static final String NAV_PROFILE = "Profile";
    public static final String NAV_UPLOAD_PICTURE = "Upload Picture";
    public static final String NAV_COMMENTS = "Comments";

    static void setUserLoggedIn(boolean loggedIn) {
        logged_in = loggedIn;
    }

    static boolean isUserLoggedIn() {
        return logged_in;
    }

    private static void setProfileId(String profileId) {
        profile_id = profileId;
    }

    static String getProfileId() {
        return profile_id;
    }

    static Profile getUserProfile() {
        return user_profile;
    }

    static void setUserProfile(Profile profile) {
        user_profile = profile;
        setProfileId(profile.getProfileId());
    }
    
    //for us of keeping application context on non-activities
    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();

        sContext = getApplicationContext();

    }

    public static Context getContext() {
        return sContext;
    }
}
