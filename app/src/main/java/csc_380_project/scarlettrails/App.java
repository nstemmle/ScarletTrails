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

    static void setUserLoggedIn(boolean loggedIn) {
        logged_in = loggedIn;
    }

    static boolean isUserLoggedIn() {
        return logged_in;
    }

    static void setProfileId(String profileId) {
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
    }
}