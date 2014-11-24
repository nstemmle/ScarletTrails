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

        TextView upload = (TextView) findViewById(R.id.uploadTrailPicture);
        if(App.isUserLoggedIn()) {
            upload.setText("Upload Picture");
            upload.setBackgroundColor(Color.parseColor("#ACFFFFFF"));
        }
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.uploadTrailPicture) {
                    selectImageSource();
                }
            }
        });
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

    private void selectImageSource() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(TabGallery.this);
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
                            Toast.makeText(TabGallery.this, "Picture uploaded", Toast.LENGTH_LONG).show();
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
                    Intent intent = new Intent(TabGallery.this, TabGallery.class);
                    startActivity(intent);

                }catch(final Exception e){
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Toast.makeText(TabGallery.this, "ERROR " + e.getMessage(), Toast.LENGTH_LONG).show();
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
