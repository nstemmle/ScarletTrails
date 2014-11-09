package csc_380_project.scarlettrails;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterActivity extends Activity {
    Button btnRegister;
    EditText inputFName;
    EditText inputLName;
    EditText inputDob;
    EditText inputEmail;
    EditText inputUsername;
    EditText inputPassword;
    EditText inputPassConf;
    TextView registerErrorMsg;

    // JSON Response node names
    private static String USER_ID = "user_id";
    private static String FIRST_NAME = "first_name";
    private static String LAST_NAME = "last_name";
    private static String EMAIL = "email";
    private static String DOB = "dob";
    private static String USERNAME = "username";
    private static String KEY_SUCCESS = "success";
    private static String KEY_ERROR_MSG = "error_msg";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        // Importing all assets like buttons, text fields
        inputFName = (EditText) findViewById(R.id.fnameField);
        inputLName = (EditText) findViewById(R.id.lnameField);
        inputEmail = (EditText) findViewById(R.id.emailField);
        inputDob = (EditText) findViewById(R.id.dobField);
        inputUsername = (EditText) findViewById(R.id.usernameField);
        inputPassword = (EditText) findViewById(R.id.passwordField);
        inputPassConf = (EditText) findViewById(R.id.confirmpasswordField);
        btnRegister = (Button) findViewById(R.id.signinbutton);
        registerErrorMsg = (TextView) findViewById(R.id.registerErrorMsg);

        // Register Button Click event
        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String first_name = inputFName.getText().toString();
                String last_name = inputLName.getText().toString();
                String email = inputEmail.getText().toString();
                String dob = inputDob.getText().toString();
                String username = inputUsername.getText().toString();
                String password = inputPassword.getText().toString();
                String passwordConf = inputPassConf.getText().toString();

                JSONObject json = new JSONObject();

                if (password.equals(passwordConf)) {
                    UserFunctions userFunction = new UserFunctions();
                    json = userFunction.registerUser(first_name, last_name, email, dob, username, password);
                }
                // check for login response
                try {
                    if (json.getString(KEY_SUCCESS) != null) {
                        String res = json.getString(KEY_SUCCESS);
                        if(Integer.parseInt(res) == 1){

                            JSONObject json_user = json.getJSONObject("user");
                            Profile profile = new Profile(json.getString(USER_ID),
                                    json_user.getString(FIRST_NAME),
                                    json_user.getString(LAST_NAME),
                                    json_user.getString(EMAIL),
                                    json_user.getString(DOB),
                                    json_user.getString(USERNAME));

                            // user successfully registred
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
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.finish();
    }
}