package csc_380_project.scarlettrails;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.Marker;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Nathan on 10/20/2014.
 */
public class TabTrail extends Activity { //implements ActionBar.OnNavigationListener {

    ImageButton btnCommentPage;
    ImageButton btnCheckIn;
    private GoogleMap mMap;
    private LocationWrapper mLocWrapper;
    private NavAdapter mAdapter;
    private ArrayList<SpinnerNavItem> navSpinner;
    private Trail mTrail;
    private static String TAG = "TabTrail.java";
    private Forecast mForecast;
    private RatingBar rb;

    String fileName = "";
    final String trailId = ActivityTrailTabHostTest.mTrail.getTrailId();
    public static final int MAX_IMAGE_DIMENSION = 1500;

    //keep track of camera capture intent
    final int CAMERA_CAPTURE = 1;
    //keep track of camera capture intent
    final int GALLERY_CAPTURE = 2;
    //keep track of cropping intent
    private Uri picUri;

    InputStream inputStream;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTrail = ActivityTrailTabHostTest.mTrail;
        setContentView(R.layout.tab_trail_info);

        btnCommentPage = (ImageButton) findViewById(R.id.btnCommentPage);
        btnCheckIn = (ImageButton) findViewById(R.id.btnCheckIn);

        mLocWrapper = LocationWrapper.getInstance();
        initializeMap();

        Thread t = new Thread() {
            public void run() {
                mForecast = ForecastWrapper.createForecast(mTrail);
            }

        };
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
       populatePageWithTrailInfo();
       
       rb = (RatingBar)findViewById(R.id.tab_trail_ratingbar);

        rb.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                DialogFragment newFragment = new RatingDialogFragment();
                newFragment.show(getFragmentManager(), "ratings");
                return false;
            }
        });

        btnCommentPage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        ActivityCommentsList.class);
                i.putExtra("TRAIL_ID", mTrail.getTrailId());
                startActivity(i);
            }
        });

        btnCheckIn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                UserFunctions userFunctions = new UserFunctions();
                JSONObject json = userFunctions.checkInUser(App.getProfileId(), mTrail.getTrailId());
                if(json != null) {
                    Toast.makeText(TabTrail.this, "Checked In Sucessfully!", Toast.LENGTH_LONG).show();
                }
            }
        });

        ImageButton upload = (ImageButton) findViewById(R.id.btnCamera);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.btnCamera) {
                    selectImageSource();
                }
            }
        });
    }

    private void initializeMap() {
        if (mMap == null) {
            mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.tab_fragment_map)).getMap();
        }

        //Check to see if successful
        mLocWrapper.checkMapExists(mMap);

        //This line currently sets map to center on Oswego County
        //Later replace with something that returns map to state it was previously in
        mLocWrapper.setUpMapWithDefaults(mMap);
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
    }

    public void populatePageWithTrailInfo() {
        //Trail Name
        Log.e(TAG, "mTrail != null: " + String.valueOf(mTrail != null));
        Log.e(TAG,"mTrail.getName() " + mTrail.getName());

       // over 2,200 ft displayed as mi, under 2,200 displayed as ft

        if(mTrail.getLength() >= 2200) {
            ((TextView) findViewById(R.id.tab_trail_textview_length_value)).setText(String.valueOf(mTrail.getLength()/5280) + " mi");
        }
        else if(mTrail.getLength() < 2200){
            ((TextView) findViewById(R.id.tab_trail_textview_length_value)).setText(String.valueOf(mTrail.getLength()) + " ft");
        }
        // Trail type
        ((TextView) findViewById(R.id.tab_trail_textview_type_value)).setText(mTrail.getType());

        // Trail park
        ((TextView) findViewById(R.id.tab_trail_textview_park_value)).setText(mTrail.getPark());


        // Trail description
        ((TextView) findViewById(R.id.tab_trail_textview_description_value)).setText(mTrail.getDescriptor());

        //Trail temp max
        ((TextView)findViewById(R.id.tab_trail_textview_tempmax_value)).setText(String.valueOf(mForecast.getTempMax() + "°F"));

        //Trail temp min
        ((TextView)findViewById(R.id.tab_trail_textview_tempmin_value)).setText(String.valueOf(mForecast.getTempMin()) + "°F");

        //Trail sunrise
        ((TextView)findViewById(R.id.tab_trail_textview_sunrise_value)).setText(mForecast.getSunrise());

        //Trail sunset
        ((TextView)findViewById(R.id.tab_trail_textview_sunset_value)).setText(mForecast.getSunset());

        //Trail rating
        RatingTrail ratingTrail = new RatingTrail();
        RatingBar rb = ((RatingBar)findViewById(R.id.tab_trail_ratingbar));
        rb.setRating(ratingTrail.getTrailRating(mTrail.getTrailId()));

        //Change colors
        LayerDrawable stars = (LayerDrawable) rb.getProgressDrawable();
        //Fully shaded color (4/5 rating = 4 stars shaded)
        stars.getDrawable(0).setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
        //Partially shaded color
        stars.getDrawable(1).setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
        //No shade color
        stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);

        mLocWrapper.clearMap(mMap);
        mLocWrapper.centerCameraOnLocation(mMap, mTrail.getLocation(), LocationWrapper.STREET_ZOOM);
        Marker marker = mLocWrapper.addMarkerAtLocation(mMap, mTrail.getLocation(), mTrail.getName(), true);
        marker.setSnippet("Click me for directions.");
        marker.showInfoWindow();

        /*Location loc = mLocWrapper.getCurrentLocation(getApplicationContext());
        final CustomLocation mCusLoc = new CustomLocation(loc.getLatitude(), loc.getLongitude());
        final Context context = getApplicationContext();
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                mLocWrapper.launchDirectionsFromCustomLocation(context, mCusLoc, mTrail.getLocation());
            }
        });*/
    }

    //will add implementation on 11/16
    public void setIcon(int weatherid){
        if (weatherid <= 232 || weatherid ==960 || weatherid == 961){
            //storm
        } else if (weatherid <= 321){
            //light rain
        } else if (weatherid <= 531){
            //heavy rain
        } else if (611 <= weatherid && weatherid <= 616){
            //mix
        } else if (weatherid <= 622){
            //snow
        } else if (weatherid == 800){
            //sunny
        } else if (weatherid == 801){
            //few clouds
        } else if (weatherid <= 803){
            //partly cloudy
        } else if (weatherid == 804 ){
            //cloudy
        } else if (weatherid <= 906 ){
            //hail
        }else{
            //default picture
        }
    }

    private void selectImageSource() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(TabTrail.this);
        builder.setTitle("Select photo");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    intent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

                    startActivityForResult(intent, CAMERA_CAPTURE);
                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            GALLERY_CAPTURE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch(requestCode) {
                //user is returning from capturing an image using the camera
                case CAMERA_CAPTURE:
                    if (data.getData() != null) {
                        //get the Uri for the captured image
                        picUri = data.getData();
                        sendToServer(picUri);
                        //performCrop();
                        break;
                    } else{
                        Toast.makeText(this, "Picture was not uploaded", Toast.LENGTH_SHORT).show();
                        Toast.makeText(this, "Please, select another camera option", Toast.LENGTH_SHORT).show();
                        break;
                    }

                case GALLERY_CAPTURE:
                    if (data.getData() != null) {
                        //get the Uri for the captured image
                        picUri = data.getData();
                        sendToServer(picUri);
                        //carry out the crop operation
                        //performCrop();
                        break;
                    } else{
                        Toast.makeText(this, "Picture was not uploaded", Toast.LENGTH_SHORT).show();
                        Toast.makeText(this, "Please, select another picture", Toast.LENGTH_SHORT).show();
                        break;
                    }
            }
        }
    }

    public void sendToServer(Uri uri) {

        String[] fileColumn = { MediaStore.Images.Media.DATA };

        Cursor imageCursor = getContentResolver().query(uri,
                fileColumn, null, null, null);
        imageCursor.moveToFirst();

        int fileColumnIndex = imageCursor.getColumnIndex(fileColumn[0]);
        String picturePath = imageCursor.getString(fileColumnIndex);

        Bitmap pictureObject = loadBitmap(picturePath, getOrientation(this.getBaseContext(), uri), MAX_IMAGE_DIMENSION, MAX_IMAGE_DIMENSION);

        if(pictureObject.getByteCount() < 2000000) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            pictureObject.compress(Bitmap.CompressFormat.JPEG, 50, stream);
        }
        else {
            if(pictureObject.getByteCount() < 5000000) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                pictureObject.compress(Bitmap.CompressFormat.JPEG, 50, stream);
            }
            else {
                if(pictureObject.getByteCount() < 10000000) {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    pictureObject.compress(Bitmap.CompressFormat.JPEG, 25, stream);
                }
                else {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    pictureObject.compress(Bitmap.CompressFormat.JPEG, 10, stream);
                }
            }
        }

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        pictureObject.compress(Bitmap.CompressFormat.JPEG, 50, stream); //compress to which format you want.
        byte [] byte_arr = stream.toByteArray();
        String image_str = Base64.encodeBytes(byte_arr);
        final ArrayList<NameValuePair> nameValuePairs = new  ArrayList<NameValuePair>();
        fileName = "user" + App.getProfileId() + "-" + DateFormat.getDateTimeInstance().format(new Date()).replaceAll("\\p{Z}","") + ".jpg";

        nameValuePairs.add(new BasicNameValuePair("image",image_str));
        nameValuePairs.add(new BasicNameValuePair("filename",fileName.trim()));

        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {
                try{
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost("http://teamscarlet.webuda.com/PICTURES/TRAIL_PICTURES/upload_image.php");
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    HttpResponse response = httpclient.execute(httppost);
                    final String the_string_response = convertResponseToString(response);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(TabTrail.this, "Picture uploaded", Toast.LENGTH_LONG).show();
                            //Toast.makeText(TabGallery.this, "Response " + the_string_response, Toast.LENGTH_LONG).show();
                        }
                    });

                    PictureFunctions pictureFunction = new PictureFunctions();
                    JSONObject jsonPicture = new JSONObject();

                    String profileId = App.getProfileId();
                    String filePath = "http://teamscarlet.webuda.com/PICTURES/TRAIL_PICTURES/" + fileName.trim();

                    if((profileId != null && !profileId.isEmpty())
                            && (trailId != null && !trailId.isEmpty())
                            && (fileName != null && !fileName.isEmpty())) {
                        jsonPicture = pictureFunction.setPicture(profileId, trailId, filePath);
                    }

                }catch(final Exception e){
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Toast.makeText(TabTrail.this, "ERROR " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                    System.out.println("Error in http connection "+e.toString());
                }
            }
        });
        t.start();
    }

    public String convertResponseToString(HttpResponse response) throws IllegalStateException, IOException {
        String res = "";
        StringBuffer buffer = new StringBuffer();
        inputStream = response.getEntity().getContent();
        final int contentLength = (int) response.getEntity().getContentLength(); //getting content length…..

        if (contentLength < 0){
        }
        else {
            byte[] data = new byte[512];
            int len = 0;
            try {
                while (-1 != (len = inputStream.read(data)) ) {
                    buffer.append(new String(data, 0, len)); //converting to string and appending  to stringbuffer…..
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            try {
                inputStream.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            res = buffer.toString();
            final String res2 = res;
        }
        return res;
    }

    public static int getOrientation(Context context, Uri photoUri) {
    /* it's on the external media. */
        Cursor cursor = context.getContentResolver().query(photoUri,
                new String[] { MediaStore.Images.ImageColumns.ORIENTATION }, null, null, null);

        if (cursor.getCount() != 1) {
            return -1;
        }

        cursor.moveToFirst();
        return cursor.getInt(0);
    }

    public static Bitmap loadBitmap(String path, int orientation, final int targetWidth, final int targetHeight) {
        Bitmap bitmap = null;
        try {
            // First decode with inJustDecodeBounds=true to check dimensions
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, options);

            // Adjust extents
            int sourceWidth, sourceHeight;
            if (orientation == 90 || orientation == 270) {
                sourceWidth = options.outHeight;
                sourceHeight = options.outWidth;
            } else {
                sourceWidth = options.outWidth;
                sourceHeight = options.outHeight;
            }

            // Calculate the maximum required scaling ratio if required and load the bitmap
            if (sourceWidth > targetWidth || sourceHeight > targetHeight) {
                float widthRatio = (float)sourceWidth / (float)targetWidth;
                float heightRatio = (float)sourceHeight / (float)targetHeight;
                float maxRatio = Math.max(widthRatio, heightRatio);
                options.inJustDecodeBounds = false;
                options.inSampleSize = (int)maxRatio;
                bitmap = BitmapFactory.decodeFile(path, options);
            } else {
                bitmap = BitmapFactory.decodeFile(path);
            }

            // Rotate the bitmap if required
            if (orientation > 0) {
                Matrix matrix = new Matrix();
                matrix.postRotate(orientation);
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            }

            // Re-scale the bitmap if necessary
            sourceWidth = bitmap.getWidth();
            sourceHeight = bitmap.getHeight();
            if (sourceWidth != targetWidth || sourceHeight != targetHeight) {
                float widthRatio = (float)sourceWidth / (float)targetWidth;
                float heightRatio = (float)sourceHeight / (float)targetHeight;
                float maxRatio = Math.max(widthRatio, heightRatio);
                sourceWidth = (int)((float)sourceWidth / maxRatio);
                sourceHeight = (int)((float)sourceHeight / maxRatio);
                bitmap = Bitmap.createScaledBitmap(bitmap, sourceWidth, sourceHeight, true);
            }
        } catch (Exception e) {
        }
        return bitmap;
    }

}
