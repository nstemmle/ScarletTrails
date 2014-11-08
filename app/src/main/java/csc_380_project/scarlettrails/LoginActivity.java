package csc_380_project.scarlettrails;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import csc_380_project.scarlettrails.ActivityHome;

public class LoginActivity extends Activity {
    Button btnLogin;
    Button btnLinkToRegister;
    Button btnForgotPassword;
    Button btnSkip;
    EditText inputUsername;
    EditText inputPassword;
    TextView loginErrorMsg;

    // JSON Response node names
    private static String KEY_SUCCESS = "success";
    private static String USER_ID = "user_id";
    private static String FIRST_NAME = "first_name";
    private static String LAST_NAME = "last_name";
    private static String EMAIL = "email";
    private static String DOB = "dob";
    private static String USERNAME = "username";
    private static String KEY_ERROR_MSG = "error_msg";

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
                                                      json_user.getString(USERNAME));

                            // Launch Dashboard Screen
                            Intent dashboard = new Intent(getApplicationContext(), ActivityHome.class);
                            dashboard.putExtra("gpsEnabled", getIntent().getBooleanExtra("gpsEnabled",false));
                            dashboard.putExtra("networkEnabled", getIntent().getBooleanExtra("networkEnabled", false));

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
}