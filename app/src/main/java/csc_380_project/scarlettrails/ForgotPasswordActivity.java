package csc_380_project.scarlettrails;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;


public class ForgotPasswordActivity extends Activity {

    Button resetPasswordBtn;
    EditText inputemailFieldFP;
    TextView errorMessage;
    private Mail m;
    String[] email;
    String username = "";
    String password = "";

    // JSON Response node names
    public static String KEY_SUCCESS = "success";
    public static String USERNAME = "username";
    public static String PASSWORD = "password";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Here is where everything is loaded for the app
        setContentView(R.layout.forgot_pw);
        m = new Mail("scarlettrails.noreply@gmail.com", "teamscarlet380");

        // Importing all assets like buttons, text fields
        inputemailFieldFP = (EditText) findViewById(R.id.emailFieldFP);
        resetPasswordBtn = (Button) findViewById(R.id.resetPasswordBtn);
        errorMessage = (TextView) findViewById(R.id.registerErrorMsg);

        // Login button Click Event
        resetPasswordBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                username = null;
                password = null;
                email = new String[] {inputemailFieldFP.getText().toString()};
                String emailAux = inputemailFieldFP.getText().toString();
                UserFunctions userFunction = new UserFunctions();

                JSONObject json = userFunction.getUserByEmail(emailAux);

                try {
                    if (json.getString(KEY_SUCCESS) != null) {
                        errorMessage.setText("");
                        String res = json.getString(KEY_SUCCESS);
                        if(Integer.parseInt(res) == 1) {
                            // user successfully logged in
                            JSONObject json_user = json.getJSONObject("user");
                            username = json_user.getString(USERNAME);
                            password = json_user.getString(PASSWORD);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(username != null && password != null)
                    sendEmail(view);
                else
                    errorMessage.setText("Incorrect Email Address");


                /*
                // check for login response
                if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    //sendEmail(view);
                    errorMessage.setText("A recovery email was sent to you. Check it for more details.");

                } else {
                        // Error in login
                         errorMessage.setText("Invalid e-mail.");
                }
                */

            }
        });
    }

    public void sendEmail(View view){
        m.setTo(email);
        m.setFrom("scarlettrails.noreply@gmail.com");
        m.setSubject("Password Recovery");
        m.setBody("You requested a recovery of you Scarlet Trails' Account. Check your contact" +
                " informations below: \n" + "Username: " + username + "\nPassword: " + password);
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);

        try {
            if(m.send()) {
                // success
                Toast.makeText(ForgotPasswordActivity.this, "A recovery email was sent to you. Check it for more details.", Toast.LENGTH_LONG).show();
            } else {
                // failure
                Toast.makeText(ForgotPasswordActivity.this, "Email was not sent.", Toast.LENGTH_LONG).show();
            }
        } catch(Exception e) {
            // some other problem
            Toast.makeText(ForgotPasswordActivity.this, "There was a problem sending the email.", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        this.finish();
    }
}