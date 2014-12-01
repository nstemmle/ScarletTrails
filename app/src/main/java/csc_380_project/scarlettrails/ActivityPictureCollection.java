package csc_380_project.scarlettrails;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.BitSet;

public class ActivityPictureCollection extends FragmentActivity {

    private NavAdapter mAdapter;
    private ArrayList<SpinnerNavItem> navSpinner;
    private static String KEY_SUCCESS = "success";
    static PictureCollection pictureCollection = new PictureCollection();
    static Uri[] mThumbIds;
    public static final String EXTRA_KEY_USERID = "user_id";
    public static final String EXTRA_KEY_TRAILID = "trail_id";

    public static final String EXTRA_KEY_ACTIVITY = "picture_collection";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setTheme(R.style.ActionBarChildTheme);
        setContentView(R.layout.activity_picture_collection);




        Bundle extras = getIntent().getExtras();
        String userId = "";
        String trailId ="";
        if (extras != null) {
            userId = extras.getString(EXTRA_KEY_USERID);
            trailId = extras.getString(EXTRA_KEY_TRAILID);
        }

        PictureFunctions pictureFunction = new PictureFunctions();
        JSONObject json = new JSONObject();

        if((userId != null && !userId.isEmpty()) && (trailId == null)) {
            json = pictureFunction.getUserPictures(userId);
        }
        else if((userId == null) && (trailId != null && !trailId.isEmpty())) {
            json = pictureFunction.getTrailPictures(trailId);
        }
        else {
            json = pictureFunction.getAllPictures();
        }

        try {
            if (json.getString(KEY_SUCCESS) != null) {
                String res = json.getString(KEY_SUCCESS);
                if (Integer.parseInt(res) == 1) {

                    pictureCollection.clear();
                    mThumbIds = null;
                    pictureCollection.getPictureCollection(json);
                    mThumbIds = getListOfUris(pictureCollection);

                    GridView gridview = (GridView)findViewById(R.id.gridView);
                    gridview.setAdapter(new ImageAdapter(this));

                    gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(ActivityPictureCollection.this, ActivityPicture.class);
                            intent.putExtra("position", position);
                            intent.putExtra(ActivityPicture.EXTRA_ACTIVITY, EXTRA_KEY_ACTIVITY);
                            startActivity(intent);
                        }
                    });

                } else {
                    // No Trails Found
                }
            } else {
                // No Trails Found
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(pictureCollection.getSize() <= 0) {
            //GridView gridview = (GridView) findViewById(R.id.trailGridView);
            TextView noPicture = (TextView) findViewById(R.id.noPicturesMessage);
            noPicture.setText("No picture");
        }
    }

    public static Uri [] getListOfUris(PictureCollection listOfPictures) {
        int size = pictureCollection.getSize();
        Uri[] list = new Uri[size];

        for(int i=0; i < size; i++)
            list[i] = Uri.parse(listOfPictures.getPictureAtIndex(i).getPictureUrl());

        return list;
    }

    //    our custom adapter
    private class ImageAdapter extends BaseAdapter {
        private Context mContext;

        public ImageAdapter(Context context) {
            mContext = context;
        }

        @Override
        public int getCount() {
            return mThumbIds.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView,
                            ViewGroup parent) {
            ImageView imageView;
//            check to see if we have a view
            if (convertView == null) {
//                no view - so create a new one
                imageView = new ImageView(mContext);
            } else {
//                use the recycled view object
                imageView = (ImageView) convertView;
            }

//            Picasso.with(MainActivity.this).setDebugging(true);
            Picasso.with(ActivityPictureCollection.this)
                    .load(mThumbIds[position])
                        //.placeholder(R.raw.loading)
                        //.error(R.raw.image_not_found)
                    .noFade().resize(350, 350)
                    .centerCrop()
                    .into(imageView);
            return imageView;
        }
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
