package csc_380_project.scarlettrails;

/**
 * Created by rafaelamfonseca on 11/5/14.
 */
import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.ListActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ActivityPicturesList extends Activity {

    private static String KEY_SUCCESS = "success";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        String userId = "";
        String trailId ="";
        if (extras != null) {
            userId = extras.getString("user_id");
            trailId = extras.getString("trail_id");
        }

        PictureFunctions pictureFunction = new PictureFunctions();
        JSONObject json = new JSONObject();

        if((userId != null && !userId.isEmpty()) && (trailId == null && trailId.isEmpty())) {
            json = pictureFunction.getUserPictures(userId);
        }
        else if((userId == null && userId.isEmpty()) && (trailId != null && !trailId.isEmpty())) {
            json = pictureFunction.getTrailPictures(trailId);
        }
        else {
            json = pictureFunction.getAllPictures();
        }

        try {
            if (json.getString(KEY_SUCCESS) != null) {
                String res = json.getString(KEY_SUCCESS);
                if (Integer.parseInt(res) == 1) {

                    PictureCollection pictureCollection = new PictureCollection();
                    pictureCollection.getPictureCollection(json);

                } else {
                    // No Trails Found
                }
            } else {
                // No Trails Found
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}