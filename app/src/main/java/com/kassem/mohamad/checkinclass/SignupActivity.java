package com.kassem.mohamad.checkinclass;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;

public class SignupActivity extends AppCompatActivity {

    EditText nameText;
    EditText emailText;
    EditText passwordText;
    Button signupButton;
    TextView loginLink;
    String result;
    private  String mailAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        nameText = (EditText) findViewById(R.id.input_name_signup);
        emailText = (EditText) findViewById(R.id.input_email_signup);
        passwordText = (EditText) findViewById(R.id.input_password_signup);
        signupButton = (Button) findViewById(R.id.btn_signup);
        loginLink = (TextView) findViewById(R.id.link_login);
    }

    public void signup(View view) {

        if (!validate()) {
            onSignupFailed("Invalid Information");
            return;
        }

        signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String name = nameText.getText().toString();
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        mailAddress = email;

        // TODO: Implement your own signup logic here.
        result = "";
        SignUpThread th = new SignUpThread(this);
        th.execute(email, password, name);
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        if(result.equals("SignUpSuccess")) {
                            onSignupSuccess();
                        }
                        else if(result.equals("")){
                            onSignupFailed("No connection");
                        }
                        else
                            onSignupFailed(result);

                        progressDialog.dismiss();
                    }
                }, 4000);

    }


    public void onSignupSuccess() {
        signupButton.setEnabled(true);
        String loginfile = "login";
        FileOutputStream outputStream;
        try{
            outputStream = openFileOutput(loginfile, Context.MODE_PRIVATE);
            outputStream.write(mailAddress.getBytes());
            outputStream.close();
        }
        catch (Exception ex){

        }
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed(String r) {
        Toast.makeText(getBaseContext(), r, Toast.LENGTH_LONG).show();

        signupButton.setEnabled(true);
    }

    public boolean validate() {

        boolean valid = true;

        String name = nameText.getText().toString();
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            nameText.setError("At least 3 characters");
            valid = false;
        } else {
            nameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError("Enter a valid email address");
            valid = false;
        } else {
            emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            passwordText.setError("Between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            passwordText.setError(null);
        }

        return valid;
    }

    public void linkLogin(View view) {
        // Finish the registration screen and return to the Login activity
        finish();
    }
}
