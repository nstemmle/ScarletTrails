package csc_380_project.scarlettrails;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends Activity {
    Button btnLogin;
    Button btnLinkToRegister;
    Button btnForgotPassword;
    Button btnSkip;
    EditText inputUsername;
    EditText inputPassword;
    TextView loginErrorMsg;

    // JSON Response node names
    public static String KEY_SUCCESS = "success";
    public static String USER_ID = "user_id";
    public static String FIRST_NAME = "first_name";
    public static String LAST_NAME = "last_name";
    public static String EMAIL = "email";
    public static String DOB = "dob";
    public static String USERNAME = "username";
    public static String INTERESTS = "interests";
    public static String PICTURE_URL = "picture_url";
    public static String KEY_ERROR_MSG = "error_msg";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Here is where everything is loaded for the app
        setContentView(R.layout.signin);

        // Importing all assets like buttons, text fields
        inputUsername = (EditText) findViewById(R.id.username);
        inputPassword = (EditText) findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.signinbutton);
        btnLinkToRegister = (Button) findViewById(R.id.register);
        btnForgotPassword = (Button) findViewById(R.id.forgotpassword);
        loginErrorMsg = (TextView) findViewById(R.id.login_error);
        btnSkip = (Button) findViewById(R.id.skip);

        // Login button Click Event
        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String username = inputUsername.getText().toString();
                String password = inputPassword.getText().toString();
                UserFunctions userFunction = new UserFunctions();
                JSONObject json = userFunction.loginUser(username, password);

                if(isNetworkAvailable()) {
                    // check for login response
                    try {
                        if (json.getString(KEY_SUCCESS) != null) {
                            loginErrorMsg.setText("");
                            String res = json.getString(KEY_SUCCESS);
                            if(Integer.parseInt(res) == 1){
                                // user successfully logged in
                                JSONObject json_user = json.getJSONObject("user");
                                Profile profile = new Profile(json.getString(USER_ID),
                                        json_user.getString(FIRST_NAME),
                                        json_user.getString(LAST_NAME),
                                        json_user.getString(EMAIL),
                                        json_user.getString(DOB),
                                        json_user.getString(USERNAME),
                                        json_user.getString(INTERESTS),
                                        json_user.getString(PICTURE_URL));

                                App.setUserLoggedIn(true);
                                App.setUserProfile(profile);

                                // Launch Dashboard Screen
                                Intent dashboard = new Intent(getApplicationContext(), ActivityHome.class);
                                dashboard.putExtra("gpsEnabled", getIntent().getBooleanExtra("gpsEnabled",false));
                                dashboard.putExtra("networkEnabled", getIntent().getBooleanExtra("networkEnabled", false));

                                //Saving credentials
                                SharedPreferences settings = getSharedPreferences("UserInfo", 0);
                                SharedPreferences.Editor editor = settings.edit();
                                editor.putString("Username",username);
                                editor.putString("Password",password);
                                editor.commit();

                                // Close all views before launching Dashboard
                                dashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(dashboard);

                                // Close Login Screen
                                finish();
                            } else {
                                // Error in login
                                loginErrorMsg.setText(json.getString(KEY_ERROR_MSG));
                            }
                        } else {
                            // Error in login
                            loginErrorMsg.setText(json.getString(KEY_ERROR_MSG));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else
                    loginErrorMsg.setText("No internet connection");
            }
        });

        // Link to Register Screen
        btnLinkToRegister.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        RegisterActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });

        btnForgotPassword.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        ForgotPasswordActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });

        btnSkip.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        ActivityHome.class);
                i.putExtra("gpsEnabled", getIntent().getBooleanExtra("gpsEnabled",false));
                i.putExtra("networkEnabled", getIntent().getBooleanExtra("networkEnabled", false));
                startActivity(i);
            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
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
        if (id == R.id.actionbar_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
        // Switch to be implemented here depending on which item is selected
        return false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.finish();
    }

    @Override
    public void onBackPressed() {

    }
}