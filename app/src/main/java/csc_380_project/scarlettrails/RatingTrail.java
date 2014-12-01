package csc_380_project.scarlettrails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RatingTrail {

    private static String TAG_RATINGLIST = "ratingList";
    public static String TRAIL_ID ="trail_id";
    public static String RATING = "rating";
    private static String trailOwnerId;
    private static Float rating;
    private static float ratingTrail;
    static RatingFunctions ratingFunctions = new RatingFunctions();

    public RatingTrail() {}

    public Float getTrailRating(String trailOwnerId) {

        try {
            JSONObject json = ratingFunctions.getRating(trailOwnerId);
            JSONArray jsonRating = json.getJSONArray(TAG_RATINGLIST);

            for (int i = 0; i < jsonRating.length(); i++) {
                JSONObject json_rating = jsonRating.getJSONObject(i);

                ratingTrail = (float) json_rating.getLong(RATING);

            }
            return ratingTrail;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return Float.valueOf(0);
    }

    public Float setTrailRating(String profileOwnerId, String trailOwnerId, Double rating) {

            ratingFunctions.setRating(profileOwnerId, trailOwnerId, rating.toString());

            return getTrailRating(trailOwnerId);
    }

}
