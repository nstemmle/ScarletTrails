package csc_380_project.scarlettrails;

/**
 * Created by rafaelamfonseca on 11/5/14.
 */
import java.util.ArrayList;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.ListActivity;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ActivityCommentsList extends ListActivity implements ActionBar.OnNavigationListener {

    public static String KEY_SUCCESS = "success";
    public static String KEY_ERROR_MSG = "error_msg";
    public static String TAG_COMMENTLIST = "commentList";
    static CommentCollection commentCollection = new CommentCollection();
    JSONObject jsonObject = new JSONObject();
    UserFunctions userFunctions = new UserFunctions();
    private ArrayList<SpinnerNavItem> navSpinner;
    private NavAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setTheme(R.style.AppTheme);
        initializeNavigationBar();
        
        DialogFragment dialog = new CommentDialogFragment();
        dialog.show(getFragmentManager(), "NoticeDialogFragment");

        Bundle extras = getIntent().getExtras();
        String searchKey = "";
        if (extras != null) {
            searchKey = extras.getString("TRAIL_ID");
        }

        // 1. pass context and data to the custom adapter
        AdapterComments adapter = new AdapterComments(getApplicationContext(), generateData(searchKey));
        //2. setListAdapter
        if(adapter.getCommentsArrayList() != null) {
            setListAdapter(adapter);
        }
    }

    private ArrayList<Comment> generateData(String searchKey) {

        CommentFunctions commentFunction = new CommentFunctions();
        JSONObject json = new JSONObject();
        ArrayList<Comment> commentsList = new ArrayList<Comment>();

        if(searchKey != null && !searchKey.isEmpty()) {
            json = commentFunction.getTrailComments(searchKey);
        }

        try {
            if (json.getString(KEY_SUCCESS) != null) {
                String res = json.getString(KEY_SUCCESS);
                if (Integer.parseInt(res) == 1) {

                    commentCollection.clear();
                    commentsList = commentCollection.getCommentCollection(json);

                    return commentsList;

                } else {
                    // No Comments Found
                }
            } else {
                // No Comments Found
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (App.isUserLoggedIn())
            getMenuInflater().inflate(R.menu.action_bar_menu, menu);
        else
            getMenuInflater().inflate(R.menu.action_bar_menu_not_logged_in, menu);
        return true;
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Comment comment = (Comment) getListAdapter().getItem(position);
        String userId = "";
        userId = comment.getProfileOwnerId();
        Intent intent = new Intent(getApplicationContext(), ActivityProfile.class);
        if (!App.getProfileId().equals(userId)) {
            jsonObject = userFunctions.getUserById(userId);
            try {
                JSONArray users = jsonObject.getJSONArray("usersList");
                JSONObject json = users.getJSONObject(0);
                Profile profile = new Profile(json.getString(LoginActivity.USER_ID),
                        json.getString(LoginActivity.FIRST_NAME),
                        json.getString(LoginActivity.LAST_NAME),
                        json.getString(LoginActivity.EMAIL),
                        json.getString(LoginActivity.DOB),
                        json.getString(LoginActivity.USERNAME),
                        json.getString(LoginActivity.INTERESTS),
                        json.getString(LoginActivity.PICTURE_URL));

                intent.putExtra("user", profile);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.actionbar_search) {
            Intent intent = new Intent(getApplicationContext(), ActivitySearchTrail.class);
            startActivity(intent);
            return true;
        } else if(id == R.id.actionbar_logout) {
            SharedPreferences settings = getSharedPreferences("UserInfo", 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.remove("Username");
            editor.remove("PassWord");
            editor.commit();
            Message myMessage=new Message();
            myMessage.obj="NOTSUCCESS";
            handler.sendMessage(myMessage);
            App.clear();
            finish();
            return true;
        }
        else if (id == R.id.actionbar_edit_profile) {
            if(App.isUserLoggedIn()) {
                Intent intent = new Intent(getApplicationContext(), ActivityEditProfile.class);
                startActivity(intent);
                return true;
            }
            else
                Toast.makeText(this, "You are not logged in. Please, login in to edit your profile", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String loginmsg = (String)msg.obj;
            if(loginmsg.equals("NOTSUCCESS")) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        }
    };

    private void initializeNavigationBar() {
        ActionBar mActionBar = getActionBar();
        mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

        navSpinner = new ArrayList<SpinnerNavItem>();
        //This is how you enter new navigation items. Please use the format provided on next line.
        navSpinner.add(new SpinnerNavItem(App.NAV_COMMENTS));
        navSpinner.add(new SpinnerNavItem(App.NAV_HOME));
        navSpinner.add(new SpinnerNavItem(App.NAV_PROFILE));
        mAdapter = new NavAdapter(getApplicationContext(), navSpinner);

        mActionBar.setListNavigationCallbacks(mAdapter, this);
        mActionBar.setDisplayShowTitleEnabled(false);
    }

    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
        ActionBar mActionBar = getActionBar();
        assert mActionBar != null;
        if (itemPosition == 1) { //Home selected
            Intent home = new Intent(getApplicationContext(), ActivityHome.class);
            startActivity(home);
            mActionBar.setSelectedNavigationItem(0);
            return true;
        }
        else if (itemPosition == 2) { //Profile selected
            if (App.isUserLoggedIn()) {
                Intent profile = new Intent(getApplicationContext(), ActivityProfile.class);
                startActivity(profile);
                mActionBar.setSelectedNavigationItem(0);
                return true;
            }
            //Prompt the user to log in
            else {
                promptUserToLogin();
                mActionBar.setSelectedNavigationItem(0);
            }
        }
        return false;
    }

    private void promptUserToLogin() {
        AlertDialog.Builder ad = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_DARK);
        ad.setMessage(R.string.dialog_login_message)
                .setTitle(R.string.dialog_login_title)
                .setPositiveButton(R.string.dialog_login_positive_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent login = new Intent(getApplicationContext(), LoginActivity.class);
                        login.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getApplicationContext().startActivity(login);
                    }
                })
                .setNegativeButton(R.string.dialog_login_negative_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        AlertDialog alertDialog = ad.create();
        alertDialog.show();
    }
}
