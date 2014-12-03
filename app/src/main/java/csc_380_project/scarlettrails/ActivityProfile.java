package csc_380_project.scarlettrails;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class ActivityProfile extends Activity implements ActionBar.OnNavigationListener {

    private GoogleMap mMap;
    private LocationWrapper mLocWrapper;
    private NavAdapter mAdapter;
    private ArrayList<SpinnerNavItem> navSpinner;
    private ImageButton galleryIcon;
    Profile userProfile;
    TextView fullName;
    TextView username;
    TextView interests;
    ImageView profilePicture;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_profile);

        initializeNavigationBar();

        if (getIntent().getExtras() != null) {
            userProfile = getIntent().getParcelableExtra("user");
        }
        else {
            userProfile = App.getUserProfile();
        }

        fullName = (TextView)findViewById(R.id.nameProfilePage);
        fullName.setText(userProfile.getFirstName() + " " + userProfile.getLastName());
        username = (TextView)findViewById(R.id.nicknameProfilePage);
        username.setText(userProfile.getUsername());
        interests = (TextView)findViewById(R.id.interestsProfilePage);
        interests.setText(userProfile.getInterests());
        interests.setGravity(Gravity.CENTER);
        profilePicture = (ImageView) findViewById(R.id.profilePicture2);
        Picasso.with(ActivityProfile.this)
                .load(userProfile.getPictureURL())
                        //.placeholder(R.raw.pic9)
                .noFade()
                .resize(600, 600)
                .centerCrop()
                .error(R.raw.image_not_found)
                .into(profilePicture);

        mLocWrapper = LocationWrapper.getInstance();
        //initializeMap();

        galleryIcon = (ImageButton)findViewById(R.id.galleryButton);
        galleryIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ActivityPictureCollection.class);
                intent.putExtra("user_id", userProfile.getProfileId());
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.action_bar_menu, menu);
        return true;
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
        }
        else if(id == R.id.actionbar_logout) {
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

    private void initializeNavigationBar() {
        ActionBar mActionBar = getActionBar();
        assert mActionBar != null;
        mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

        navSpinner = new ArrayList<SpinnerNavItem>();
        //This is how you enter new navigation items. Please use the format provided on next line.
        navSpinner.add(new SpinnerNavItem(App.NAV_PROFILE));
        navSpinner.add(new SpinnerNavItem(App.NAV_HOME));
        navSpinner.add(new SpinnerNavItem(App.NAV_TRAILS));
        mAdapter = new NavAdapter(getApplicationContext(), navSpinner);

        mActionBar.setListNavigationCallbacks(mAdapter, this);
        mActionBar.setDisplayShowTitleEnabled(false);
    }

    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
        ActionBar mActionBar = getActionBar();
        assert mActionBar != null;
        if (itemPosition == 1) { //Trails selected
            Intent trails = new Intent(getApplicationContext(), ActivityHome.class);
            startActivity(trails);
            mActionBar.setSelectedNavigationItem(0);
            return true;
        } else if (itemPosition == 2) { //Profile selected
            Intent profile = new Intent(getApplicationContext(), ActivityTrailsList.class);
            startActivity(profile);
            mActionBar.setSelectedNavigationItem(0);
            return true;
        }
        return false;
    }

    private void initializeMap() {
        if (mMap == null) {
            mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.profile_fragment_map)).getMap();
        }

        //Check to see if successful
        mLocWrapper.checkMapExists(mMap);

        //This line currently sets map to center on Oswego County
        mLocWrapper.setUpMapWithDefaults(mMap);
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
}
