package csc_380_project.scarlettrails;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.Toast;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;


public class Upload extends Activity { //implements ActionBar.OnNavigationListener {
    //keep track of camera capture intent
    final int CAMERA_CAPTURE = 1;
    //keep track of camera capture intent
    final int GALLERY_CAPTURE = 2;
    //keep track of cropping intent
    final int PIC_CROP = 3;
    //captured picture uri
    private Uri picUri;

    InputStream inputStream;

    private NavAdapter mAdapter;
    private ArrayList<SpinnerNavItem> navSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_upload);

        //initializeNavigationBar();

        //retrieve a reference to the UI button
        Button upload = (Button) findViewById(R.id.upload);
        //handle button clicks
        upload.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.upload)
                    selectImageSource();
            }
        });
    }

    private void selectImageSource() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(Upload.this);
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

                //user is returning from cropping the image
                case PIC_CROP:
                    //get the returned data
                    Bundle extras = data.getExtras();
                    //get the cropped bitmap
                    Bitmap thePic = extras.getParcelable("data");

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    thePic.compress(Bitmap.CompressFormat.PNG, 100, stream); //compress to which format you want.
                    byte [] byte_arr = stream.toByteArray();
                    String image_str = Base64.encodeBytes(byte_arr);
                    final ArrayList<NameValuePair> nameValuePairs = new  ArrayList<NameValuePair>();

                    nameValuePairs.add(new BasicNameValuePair("image",image_str));

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
                                        Toast.makeText(Upload.this, "Response " + the_string_response, Toast.LENGTH_LONG).show();
                                    }
                                });

                            }catch(final Exception e){
                                runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {
                                        Toast.makeText(Upload.this, "ERROR " + e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });
                                System.out.println("Error in http connection "+e.toString());
                            }
                        }
                    });
                    t.start();
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

        Bitmap pictureObject = BitmapFactory.decodeFile(picturePath);


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
        String fileName = "user " + App.getProfileId() + " - " + DateFormat.getDateTimeInstance().format(new Date()) + ".jpg";

        nameValuePairs.add(new BasicNameValuePair("image",image_str));
        nameValuePairs.add(new BasicNameValuePair("filename",fileName));

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
                            Toast.makeText(Upload.this, "Response " + the_string_response, Toast.LENGTH_LONG).show();
                        }
                    });

                }catch(final Exception e){
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Toast.makeText(Upload.this, "ERROR " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                    System.out.println("Error in http connection "+e.toString());
                }
            }
        });
        t.start();
    }

    public String convertResponseToString(HttpResponse response) throws IllegalStateException, IOException{
        String res = "";
        StringBuffer buffer = new StringBuffer();
        inputStream = response.getEntity().getContent();
        final int contentLength = (int) response.getEntity().getContentLength(); //getting content length…..
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(Upload.this, "contentLength : " + contentLength, Toast.LENGTH_LONG).show();
            }
        });

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

            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    Toast.makeText(Upload.this, "Result : " + res2, Toast.LENGTH_LONG).show();
                }
            });
            //System.out.println("Response => " +  EntityUtils.toString(response.getEntity()));
        }
        return res;
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private void performCrop(){
        try {
            //call the standard crop action intent (the user device may not support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            //indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
            //set crop properties
            cropIntent.putExtra("crop", "true");
            //indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            cropIntent.putExtra("scale", true);
            //retrieve data on return
            cropIntent.putExtra("return-data", false);
            //start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PIC_CROP);
        }
        catch(ActivityNotFoundException anfe){
            //display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    /*@Override
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

    private void initializeNavigationBar() {
        ActionBar mActionBar = getActionBar();
        mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

        navSpinner = new ArrayList<SpinnerNavItem>();
        //This is how you enter new navigation items. Please use the format provided on next line.
        navSpinner.add(new SpinnerNavItem("Upload Picture"));
        navSpinner.add(new SpinnerNavItem("Home"));
        navSpinner.add(new SpinnerNavItem("Trails"));
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
                Intent intent = new Intent(getApplicationContext(), ActivityTrailsList.class);
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
    }*/
}
