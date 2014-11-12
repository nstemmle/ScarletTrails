package csc_380_project.scarlettrails;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TabGallery extends Activity {

    private NavAdapter mAdapter;
    private ArrayList<SpinnerNavItem> navSpinner;
    private static String KEY_SUCCESS = "success";
    static PictureCollection pictureCollection = new PictureCollection();
    static Uri[] mThumbIds;

    public static final String EXTRA_KEY_USERID = "user_id";
    public static final String EXTRA_KEY_TRAILID = "trail_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_trail_gallery);

        Bundle extras = getIntent().getExtras();
        String userId = "";
        String trailId ="";
        if (extras != null) {
            userId = extras.getString(EXTRA_KEY_USERID);
            trailId = extras.getString(EXTRA_KEY_TRAILID);
        }

        PictureFunctions pictureFunction = new PictureFunctions();
        JSONObject json;

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

                    GridView gridview = (GridView)findViewById(R.id.tab_trail_gallery_gridview);
                    gridview.setAdapter(new ImageAdapter(this));

                    gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(TabGallery.this, ActivityPicture.class);
                            intent.putExtra("position", position);
                            //Bundle bundle = new Bundle();
                            //bundle.putSerializable("listOfPictures", listOfPictures);
                            //intent.putExtras(bundle);
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

    //static Integer[] mThumbIds = {R.raw.pic1, R.raw.pic2, R.raw.pic3, R.raw.pic4,
            //R.raw.pic5, R.raw.pic6, R.raw.pic7, R.raw.pic8, R.raw.pic9,
            //R.raw.pic10, R.raw.pic11, R.raw.pic12, R.raw.pic13, R.raw.pic14,
            //R.raw.pic15, R.raw.pic16, R.raw.pic17, R.raw.pic18, R.drawable.gallery};
}
