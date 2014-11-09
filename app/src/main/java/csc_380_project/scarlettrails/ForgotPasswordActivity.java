package csc_380_project.scarlettrails;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class ForgotPasswordActivity extends Activity {

    Button resetPasswordBtn;
    EditText inputemailFieldFP;
    TextView errorMessage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Here is where everything is loaded for the app
        setContentView(R.layout.forgot_pw);

        // Importing all assets like buttons, text fields
        inputemailFieldFP = (EditText) findViewById(R.id.emailFieldFP);
        resetPasswordBtn = (Button) findViewById(R.id.resetPasswordBtn);
        errorMessage = (TextView) findViewById(R.id.registerErrorMsg);

        // Login button Click Event
        resetPasswordBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String email = inputemailFieldFP.getText().toString();

                // check for login response
                if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

                    errorMessage.setText("A recovery email was sent to you. Check it for more details.");

                } else {
                        // Error in login
                         errorMessage.setText("Invalid e-mail.");
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