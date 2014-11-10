package csc_380_project.scarlettrails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;


    class PictureCollection {

        private static String TAG_PICTURELIST = "pictureList";
        public static String PICTURE_ID = "picture_id";
        public static String PICTURE = "picture";
        public static String PICTURE_DATE = "picture_date";
        public static String TRAIL_ID ="trail_id";
        public static String USER_ID = "user_id";
        public static String USERNAME = "username";

        private ArrayList<Picture> picturesList;
        PictureCollection() {
            picturesList = new ArrayList<Picture>();
        }

        public ArrayList<Picture> getPictureCollection(JSONObject json) {
            //Default capacity is 10 but you can this constructor to assign a specific initial size

            try {
                JSONArray pictures = json.getJSONArray(TAG_PICTURELIST);

                // looping through All Trails
                for (int i = 0; i < pictures.length(); i++) {
                    JSONObject json_picture = pictures.getJSONObject(i);
                    // Storing each json item in variable

                    Picture picture = new Picture(json_picture.getString(PICTURE_ID)
                            , json_picture.getString(PICTURE)
                            , json_picture.getString(USER_ID)
                            , json_picture.getString(USERNAME)
                            , json_picture.getString(TRAIL_ID)
                            , json_picture.getString(PICTURE_DATE));

                    // adding each child node to HashMap key => value
                    picturesList.add(picture);
                }
                return picturesList;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
