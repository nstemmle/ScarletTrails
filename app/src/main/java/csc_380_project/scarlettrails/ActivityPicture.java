package csc_380_project.scarlettrails;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ActivityPicture extends FragmentActivity {// implements ActionBar.OnNavigationListener {

    private NavAdapter mAdapter;
    private ArrayList<SpinnerNavItem> navSpinner;
    int positionForSwipe;
    Button picTrailName;
    Button picUsername;
    JSONObject jsonObject = new JSONObject();
    TrailFunctions trailFunctions = new TrailFunctions();
    UserFunctions userFunctions = new UserFunctions();

    public static final String EXTRA_ACTIVITY = "activity";
    private String activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setTheme(R.style.ActionBarTheme);
        setContentView(R.layout.activity_picture);

        picTrailName = (Button) findViewById(R.id.picTrailName);
        picUsername = (Button) findViewById(R.id.picUsername);

        ImageView imageView = (ImageView) findViewById(R.id.picture);

        activity = getIntent().getStringExtra(EXTRA_ACTIVITY);

        final int gallerySize;

        if (activity.equals(TabGallery.EXTRA_KEY_ACTIVITY)) {
            gallerySize = TabGallery.mThumbIds.length;
        } else if (activity.equals(ActivityPictureCollection.EXTRA_KEY_ACTIVITY)) {
            gallerySize = ActivityPictureCollection.mThumbIds.length;
        } else {
            gallerySize = 0;
        }

        final int position = getIntent().getIntExtra("position", -1);

        positionForSwipe = position;

        if (position != -1) {
            if (activity.equals(TabGallery.EXTRA_KEY_ACTIVITY)) {
                Picasso.with(ActivityPicture.this)
                        .load(TabGallery.mThumbIds[position])
                                //.placeholder(R.raw.pic9)
                        .noFade()
                        .resize(400, 400)
                        .centerCrop()
                        .error(R.raw.image_not_found)
                        .into(imageView);
                picUsername.setText(TabGallery.pictureCollection.getPictureAtIndex(position).getProfileUsername());
                picTrailName.setText(TabGallery.pictureCollection.getPictureAtIndex(position).getTrailName());
            } else if (activity.equals(ActivityPictureCollection.EXTRA_KEY_ACTIVITY)) {
                Picasso.with(ActivityPicture.this)
                        .load(ActivityPictureCollection.mThumbIds[position])
                                //.placeholder(R.raw.pic9)
                        .noFade()
                        .resize(400, 400)
                        .centerCrop()
                        .error(R.raw.image_not_found)
                        .into(imageView);
                picUsername.setText(ActivityPictureCollection.pictureCollection.getPictureAtIndex(position).getProfileUsername());
                picTrailName.setText(ActivityPictureCollection.pictureCollection.getPictureAtIndex(position).getTrailName());
            }
        } else {
            Picasso.with(ActivityPicture.this)
                    .load(R.raw.loading)
                    .noFade()
                    .resize(400, 400)
                    .centerCrop()
                    .into(imageView);
        }

        imageView.setOnTouchListener(new OnSwipeTouchListener(this) {
            @Override
            public void onSwipeLeft() {
                ImageView imageView = (ImageView) findViewById(R.id.picture);
                if (positionForSwipe == gallerySize - 1) {
                    //positionForSwipe = 0;
                    return;
                } else {
                    positionForSwipe = positionForSwipe + 1;
                    if (activity.equals(TabGallery.EXTRA_KEY_ACTIVITY)) {
                        Picasso.with(ActivityPicture.this)
                                .load(TabGallery.mThumbIds[positionForSwipe])
                                        //.placeholder(R.raw.pic9)
                                .noFade()
                                .resize(600, 600)
                                .centerCrop()
                                .error(R.raw.image_not_found)
                                .into(imageView);
                        picUsername.setText(TabGallery.pictureCollection.
                                getPictureAtIndex(positionForSwipe).getProfileUsername());
                        picTrailName.setText(TabGallery.pictureCollection.
                                getPictureAtIndex(positionForSwipe).getTrailName());
                    } else if (activity.equals(ActivityPictureCollection.EXTRA_KEY_ACTIVITY)) {
                        Picasso.with(ActivityPicture.this)
                                .load(ActivityPictureCollection.mThumbIds[positionForSwipe])
                                        //.placeholder(R.raw.pic9)
                                .noFade()
                                .resize(600, 600)
                                .centerCrop()
                                .error(R.raw.image_not_found)
                                .into(imageView);
                        picUsername.setText(ActivityPictureCollection.pictureCollection.
                                getPictureAtIndex(positionForSwipe).getProfileUsername());
                        picTrailName.setText(ActivityPictureCollection.pictureCollection.
                                getPictureAtIndex(positionForSwipe).getTrailName());

                    }
                }
            }

            @Override
            public void onSwipeRight() {
                ImageView imageView = (ImageView) findViewById(R.id.picture);
                if(positionForSwipe == 0){
                    //positionForSwipe = gallerySize - 1;
                    return;
                }
                else {
                    positionForSwipe = positionForSwipe - 1;
                }
                if (activity.equals(TabGallery.EXTRA_KEY_ACTIVITY)) {
                    Picasso.with(ActivityPicture.this)
                            .load(TabGallery.mThumbIds[positionForSwipe])
                                    //.placeholder(R.raw.pic9)
                            .noFade()
                            .resize(600, 600)
                            .centerCrop()
                            .error(R.raw.image_not_found)
                            .into(imageView);
                    picUsername.setText(TabGallery.pictureCollection.
                            getPictureAtIndex(positionForSwipe).getProfileUsername());
                    picTrailName.setText(TabGallery.pictureCollection.
                            getPictureAtIndex(positionForSwipe).getTrailName());
                } else if (activity.equals(ActivityPictureCollection.EXTRA_KEY_ACTIVITY)) {
                    Picasso.with(ActivityPicture.this)
                            .load(ActivityPictureCollection.mThumbIds[positionForSwipe])
                                    //.placeholder(R.raw.pic9)
                            .noFade()
                            .resize(600, 600)
                            .centerCrop()
                            .error(R.raw.image_not_found)
                            .into(imageView);
                    picUsername.setText(ActivityPictureCollection.pictureCollection.
                            getPictureAtIndex(positionForSwipe).getProfileUsername());
                    picTrailName.setText(ActivityPictureCollection.pictureCollection.
                            getPictureAtIndex(positionForSwipe).getTrailName());
                }

            }
        });

        // Link to Activity Trail Page
        picTrailName.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String trailId = "";
                if (activity.equals(ActivityPictureCollection.EXTRA_KEY_ACTIVITY)) {
                    trailId = ActivityPictureCollection.pictureCollection.getPictureAtIndex(positionForSwipe).getTrailOwnerId();
                } else if (activity.equals(TabGallery.EXTRA_KEY_ACTIVITY)) {
                    trailId = TabGallery.pictureCollection.getPictureAtIndex(positionForSwipe).getTrailOwnerId();
                }

                jsonObject = trailFunctions.getTrailById(trailId);
                try {
                    JSONArray trails = jsonObject.getJSONArray(ActivityTrailsList.TAG_TRAILLIST);
                    //JSONObject json = trails.getJSONObject(0);
                    JSONObject json_trail = trails.getJSONObject(0);
                    // Storing each json item in variable

                    Trail trail = new Trail(
                            json_trail.getString(ActivityTrailsList.TRAIL_ID),
                            json_trail.getString(ActivityTrailsList.NAME),
                            json_trail.getInt(ActivityTrailsList.LENGTH),
                            json_trail.getString(ActivityTrailsList.TYPE),
                            json_trail.getString(ActivityTrailsList.PARK),
                            json_trail.getString(ActivityTrailsList.DESCRIPTOR),
                            json_trail.getDouble(ActivityTrailsList.RATING), null,
                            new CustomLocation( json_trail.getString(ActivityTrailsList.LOCATION_ID),
                                    json_trail.getDouble(ActivityTrailsList.X),
                                    json_trail.getDouble(ActivityTrailsList.Y)));

                    Intent intent = new Intent(getApplicationContext(), ActivityTrailTabHostTest.class);
                    intent.putExtra("trail", trail);
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        // Link to Activity Trail Page
        picUsername.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ActivityProfile.class);
                String userId = "";
                if (activity.equals(ActivityPictureCollection.EXTRA_KEY_ACTIVITY)) {
                    userId = ActivityPictureCollection.pictureCollection.getPictureAtIndex(positionForSwipe).getProfileOwnerId();
                } else if (activity.equals(TabGallery.EXTRA_KEY_ACTIVITY)) {
                    userId = TabGallery.pictureCollection.getPictureAtIndex(positionForSwipe).getProfileOwnerId();
                }

                if (!App.getProfileId().equals(userId)) {
                    jsonObject = userFunctions.getUserById(userId);
                    try {
                        JSONArray trails = jsonObject.getJSONArray("usersList");
                        JSONObject json = trails.getJSONObject(0);
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
        });
    }

    //Not used
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
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
}