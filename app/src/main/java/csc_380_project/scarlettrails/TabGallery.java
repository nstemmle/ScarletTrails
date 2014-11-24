package csc_380_project.scarlettrails;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TabGallery extends Activity {

    private NavAdapter mAdapter;
    private ArrayList<SpinnerNavItem> navSpinner;
    private static String KEY_SUCCESS = "success";
    static PictureCollection pictureCollection = new PictureCollection();
    static Uri[] mThumbIds;
    final String trailId = ActivityTrailTabHostTest.mTrail.getTrailId();

    public static final String EXTRA_KEY_USERID = "user_id";
    public static final String EXTRA_KEY_TRAILID = "trail_id";
    public static final String EXTRA_KEY_ACTIVITY = "tab_gallery";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_trail_gallery);

        PictureFunctions pictureFunction = new PictureFunctions();
        JSONObject json;

        if(trailId != null && !trailId.isEmpty()) {
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

                    GridView gridview = (GridView)findViewById(R.id.tab_trail_gallery_gridview);
                    gridview.setAdapter(new ImageAdapter(this));

                    gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(TabGallery.this, ActivityPicture.class);
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
            Picasso.with(TabGallery.this)
                    .load(mThumbIds[position])
                        //.placeholder(R.raw.loading)
                        //.error(R.raw.image_not_found)
                    .noFade().resize(350, 350)
                    .centerCrop()
                    .into(imageView);
            return imageView;
        }
    }
}
