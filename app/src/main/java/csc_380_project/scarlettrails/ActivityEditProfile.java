package csc_380_project.scarlettrails;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ActivityEditProfile extends Activity {
    Button btnSave;
    Button btnPictureUpload;
    EditText inputFName;
    EditText inputLName;
    EditText inputDob;
    EditText inputEmail;
    EditText inputInterests;
    TextView registerErrorMsg;
    ImageView profilePicture;
    String fileName = "";
    public static final int MAX_IMAGE_DIMENSION = 750;
    //keep track of camera capture intent
    final int CAMERA_CAPTURE = 1;
    //keep track of camera capture intent
    final int GALLERY_CAPTURE = 2;
    //keep track of cropping intent
    private Uri picUri;
    InputStream inputStream;

    Profile user = App.getUserProfile();

    // JSON Response node names
    private static String USER_ID = "user_id";
    private static String FIRST_NAME = "first_name";
    private static String LAST_NAME = "last_name";
    private static String EMAIL = "email";
    private static String DOB = "dob";
    private static String INTERESTS = "interests";
    private static String PICTURE_URL = "picture_url";
    private static String KEY_SUCCESS = "success";
    private static String KEY_ERROR_MSG = "error_msg";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Importing all assets like buttons, text fields
        inputFName = (EditText) findViewById(R.id.fnameField_edit);
        inputLName = (EditText) findViewById(R.id.lnameField_edit);
        inputEmail = (EditText) findViewById(R.id.emailField_edit);
        inputDob = (EditText) findViewById(R.id.dobField_edit);
        inputInterests = (EditText) findViewById(R.id.interests_edit);
        btnSave = (Button) findViewById(R.id.save_edit);
        registerErrorMsg = (TextView) findViewById(R.id.registerErrorMsg_edit);
        btnPictureUpload = (Button) findViewById(R.id.selectImage_edit);
        profilePicture = (ImageView) findViewById(R.id.profilePicture_edit);

        inputFName.setText(user.getFirstName());
        inputLName.setText(user.getLastName());
        inputEmail.setText(user.getEmail());
        inputDob.setText(user.getDateOfBirth());
        inputInterests.setText(user.getInterests().isEmpty() || user.getInterests().equals("null") ? "" : user.getInterests());
        Picasso.with(ActivityEditProfile.this)
                .load(user.getPictureURL())
                        //.placeholder(R.raw.pic9)
                .noFade()
                .resize(400, 400)
                .centerCrop()
                .error(R.raw.image_not_found)
                .into(profilePicture);


        // Save Button Click event
        btnSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String first_name = inputFName.getText().toString();
                String last_name = inputLName.getText().toString();
                String email = inputEmail.getText().toString();
                String dob = inputDob.getText().toString();
                String interests = inputInterests.getText().toString();

                String pictureURL = "";
                if(picUri != null) {
                    fileName = App.getUserProfile().getUsername() + "-" +
                         DateFormat.getDateTimeInstance().format(new Date()).replaceAll("\\p{Z}","") + ".jpg";
                    pictureURL = "http://teamscarlet.webuda.com/PICTURES/USER_PICTURES/" + fileName.trim();
                }
                else
                    pictureURL = user.getPictureURL();

                JSONObject json = new JSONObject();
                UserFunctions userFunction = new UserFunctions();
                json = userFunction.updateUser(App.getProfileId(), first_name, last_name, email, dob,
                                          App.getUserProfile().getUsername(), interests, pictureURL);

                // check for login response
                try {
                    if (json.getString(KEY_SUCCESS) != null) {
                        String res = json.getString(KEY_SUCCESS);
                        if(Integer.parseInt(res) == 1){

                            JSONObject json_user = json.getJSONObject("user");
                            user.setFirstName(json_user.getString(FIRST_NAME));
                            user.setLastName(json_user.getString(LAST_NAME));
                            user.setEmail(json_user.getString(EMAIL));
                            user.setDateOfBirth(json_user.getString(DOB));
                            user.setInterests(json_user.getString(INTERESTS));
                            user.setPictureURL(json_user.getString(PICTURE_URL));

                            App.setUserLoggedIn(true);
                            App.setUserProfile(user);
                            if(picUri != null)
                                sendToServer(picUri);

                            // user successfully updated
                            // Launch Dashboard Screen
                            Intent dashboard = new Intent(getApplicationContext(), ActivityHome.class);
                            // Close all views before launching Dashboard
                            dashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(dashboard);
                            // Close Registration Screen
                            finish();
                        }else{
                            // Error in registration
                            registerErrorMsg.setText(json.getString(KEY_ERROR_MSG));
                        }
                    } else {
                        registerErrorMsg.setText(json.getString(KEY_ERROR_MSG));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        btnPictureUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImageSource();
            }
        });
    }

    private void selectImageSource() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityEditProfile.this);
        builder.setTitle("Select photo");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
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
                        Picasso.with(ActivityEditProfile.this)
                                .load(picUri)
                                        //.placeholder(R.raw.pic9)
                                .noFade()
                                .resize(400, 400)
                                .centerCrop()
                                .error(R.raw.image_not_found)
                                .into(profilePicture);
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
                        Picasso.with(ActivityEditProfile.this)
                                .load(picUri)
                                        //.placeholder(R.raw.pic9)
                                .noFade()
                                .resize(400, 400)
                                .centerCrop()
                                .error(R.raw.image_not_found)
                                .into(profilePicture);
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
        pictureObject.compress(Bitmap.CompressFormat.JPEG, 50, stream);
        byte [] byte_arr = stream.toByteArray();
        String image_str = Base64.encodeBytes(byte_arr);
        final ArrayList<NameValuePair> nameValuePairs = new  ArrayList<NameValuePair>();

        nameValuePairs.add(new BasicNameValuePair("image",image_str));
        nameValuePairs.add(new BasicNameValuePair("filename",fileName.trim()));

        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {
                try{
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost("http://teamscarlet.webuda.com/PICTURES/USER_PICTURES/upload_image.php");
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    HttpResponse response = httpclient.execute(httppost);
                    final String the_string_response = convertResponseToString(response);
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Toast.makeText(ActivityEditProfile.this, "Changes saved", Toast.LENGTH_LONG).show();
                        }
                    });

                    PictureFunctions pictureFunction = new PictureFunctions();
                    JSONObject jsonPicture = new JSONObject();

                    String profileId = App.getProfileId();
                    String filePath = "http://teamscarlet.webuda.com/PICTURES/USER_PICTURES/" + fileName.trim();

                }catch(final Exception e){
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Toast.makeText(ActivityEditProfile.this, "ERROR " + e.getMessage(), Toast.LENGTH_LONG).show();
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