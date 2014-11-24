package csc_380_project.scarlettrails;

/**
 * Created by rafaelamfonseca on 11/5/14.
 */
import java.util.ArrayList;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.ListActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.SearchView;

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
                    Intent intent = new Intent(getApplicationContext(), ActivitySearchTrail.class);
                    intent.putExtra("KEY_ERROR_MSG", json.getString(KEY_ERROR_MSG));
                    startActivity(intent);
                }
            } else {
                // No Comments Found
                Intent intent = new Intent(getApplicationContext(), ActivitySearchTrail.class);
                intent.putExtra("KEY_ERROR_MSG", json.getString(KEY_ERROR_MSG));
                startActivity(intent);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.action_bar_menu, menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        return super.onCreateOptionsMenu(menu);
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
        if (id == R.id.actionbar_settings) {
            Intent settings = new Intent(getApplicationContext(), ActivitySettings.class);
            startActivity(settings);
            return true;
        } else if (id == R.id.actionbar_search){
            Intent intent = new Intent(getApplicationContext(), ActivitySearchTrail.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
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
        if (itemPosition == 1) { //Home selected
            Intent home = new Intent(getApplicationContext(), ActivityHome.class);
            startActivity(home);
            return true;
        }

        else if (itemPosition == 2) { //Profile selected
            if (App.isUserLoggedIn()) {
                Intent profile = new Intent(getApplicationContext(), ActivityProfile.class);
                startActivity(profile);
                return true;
            }

            //Prompt the user to log in
            else {
                promptUserToLogin();
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
