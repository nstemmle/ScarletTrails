package csc_380_project.scarlettrails;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.ByteArrayOutputStream;


import com.squareup.picasso.Picasso;
import java.util.ArrayList;


public class ActivityPicture extends FragmentActivity implements ActionBar.OnNavigationListener {

    private NavAdapter mAdapter;
    private ArrayList<SpinnerNavItem> navSpinner;
    int positionForSwipe;
    TextView picTrailName;
    TextView picUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_picture);

        picTrailName = (TextView) findViewById(R.id.picTrailName);
        picUsername = (TextView) findViewById(R.id.picUsername);

        initializeNavigationBar();

        ImageView imageView = (ImageView) findViewById(R.id.picture);

        final int gallerySize = ActivityPictureCollection.mThumbIds.length;

        final int position = getIntent().getIntExtra("position", -1);

        //final PictureCollection listOfPic = (PictureCollection) getIntent().getSerializableExtra("listOfPictures");

        positionForSwipe = position;

        if (position != -1) {
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
                if(positionForSwipe == gallerySize - 1) {
                    positionForSwipe = 0;
                    Picasso.with(ActivityPicture.this)
                            .load(ActivityPictureCollection.mThumbIds[positionForSwipe])
                                    //.placeholder(R.raw.pic9)
                            .noFade()
                            .resize(600, 600)
                            .centerCrop()
                            .error(R.raw.image_not_found)
                            .into(imageView);
                }
                else {
                    positionForSwipe = positionForSwipe + 1;
                    Picasso.with(ActivityPicture.this)
                            .load(ActivityPictureCollection.mThumbIds[positionForSwipe])
                                    //.placeholder(R.raw.pic9)
                            .noFade()
                            .resize(600, 600)
                            .centerCrop()
                            .error(R.raw.image_not_found)
                            .into(imageView);
                }
                picUsername.setText(ActivityPictureCollection.pictureCollection.
                                          getPictureAtIndex(positionForSwipe).getProfileUsername());
                picTrailName.setText(ActivityPictureCollection.pictureCollection.
                                          getPictureAtIndex(positionForSwipe).getTrailName());
            }

            @Override
            public void onSwipeRight() {
                ImageView imageView = (ImageView) findViewById(R.id.picture);
                if(positionForSwipe == 0){
                    positionForSwipe = gallerySize - 1;
                    Picasso.with(ActivityPicture.this)
                            .load(ActivityPictureCollection.mThumbIds[positionForSwipe])
                                    //.placeholder(R.raw.pic9)
                            .noFade()
                            .resize(600, 600)
                            .centerCrop()
                            .error(R.raw.image_not_found)
                            .into(imageView);
                }
                else {
                    positionForSwipe = positionForSwipe - 1;
                    Picasso.with(ActivityPicture.this)
                            .load(ActivityPictureCollection.mThumbIds[positionForSwipe])
                                    //.placeholder(R.raw.pic9)
                            .noFade()
                            .resize(600, 600)
                            .centerCrop()
                            .error(R.raw.image_not_found)
                            .into(imageView);
                }
                picUsername.setText(ActivityPictureCollection.pictureCollection.
                                          getPictureAtIndex(positionForSwipe).getProfileUsername());
                picTrailName.setText(ActivityPictureCollection.pictureCollection.
                                          getPictureAtIndex(positionForSwipe).getTrailName());
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
        } else if (id == R.id.actionbar_settings) {
            Intent intent = new Intent(getApplicationContext(), ActivitySettings.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initializeNavigationBar() {
        ActionBar mActionBar = getActionBar();
        mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

        navSpinner = new ArrayList<SpinnerNavItem>();
        //This is how you enter new navigation items. Please use the format provided on next line.
        navSpinner.add(new SpinnerNavItem("Picture"));
        navSpinner.add(new SpinnerNavItem("Home"));
        navSpinner.add(new SpinnerNavItem("Trail"));
        navSpinner.add(new SpinnerNavItem("Profile"));
        mAdapter = new NavAdapter(getApplicationContext(), navSpinner);

        mActionBar.setListNavigationCallbacks(mAdapter, this);
        mActionBar.setDisplayShowTitleEnabled(false);
    }

    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
        if (itemPosition == 1) {
            Intent intent = new Intent(getApplicationContext(), ActivityHome.class);
            //if (intent.resolveActivity(getApplicationContext().getPackageManager()) != null)
            startActivity(intent);
            return true;
        }
        else {
            if (itemPosition == 2) {
                Intent intent = new Intent(getApplicationContext(), ActivityTrail.class);
                //if (intent.resolveActivity(getApplicationContext().getPackageManager()) != null)
                startActivity(intent);
                return true;
            } else
            if (itemPosition == 3) {
                Intent intent = new Intent(getApplicationContext(), ActivityProfile.class);
                //if (intent.resolveActivity(getApplicationContext().getPackageManager()) != null)
                startActivity(intent);
            }
        }
        return false;
    }
}