package csc_380_project.scarlettrails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;


class CommentCollection {

    private static String TAG_COMMENTLIST = "commentList";
    public static String COMMENT_ID = "comment_id";
    public static String COMMENT_TEXT = "comment_text";
    public static String COMMENT_DATE = "comment_date";
    public static String TRAIL_ID ="trail_id";
    public static String USER_ID = "user_id";
    public static String USERNAME = "username";

    private ArrayList<Comment> commentsList;
    CommentCollection() {
        commentsList = new ArrayList<Comment>();
    }

    public ArrayList<Comment> getCommentCollection(JSONObject json) {
        //Default capacity is 10 but you can this constructor to assign a specific initial size

        try {
            JSONArray comments = json.getJSONArray(TAG_COMMENTLIST);

            // looping through All Comments
            for (int i = 0; i < comments.length(); i++) {
                JSONObject json_comment = comments.getJSONObject(i);
                // Storing each json item in variable
                Comment comment = new Comment(json_comment.getString(COMMENT_ID)
                        , json_comment.getString(COMMENT_TEXT)
                        , json_comment.getString(USER_ID)
                        , json_comment.getString(USERNAME)
                        , json_comment.getString(TRAIL_ID)
                        , json_comment.getString(COMMENT_DATE));

                // adding each child node to HashMap key => value
                commentsList.add(comment);
            }
            return commentsList;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
