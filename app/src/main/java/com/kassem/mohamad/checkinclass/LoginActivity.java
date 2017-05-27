package com.kassem.mohamad.checkinclass;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.sql.Time;
import java.util.Calendar;

public class LoginActivity extends AppCompatActivity {

    private static final int REQUEST_SIGNUP = 0;

    EditText emailText;
    EditText passwordText;
    Button loginButton;
    TextView signupLink;
    String result;
    private String mailAddress;
    ProgressDialog progressDialog;
    LoginThread LT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailText = (EditText) findViewById(R.id.input_email_login);
        passwordText = (EditText) findViewById(R.id.input_password_login);
        loginButton = (Button) findViewById(R.id.btn_login);
        signupLink = (TextView) findViewById(R.id.link_signup);
    }

    public void login(View view) {
        if (!validate()) {
            onLoginFailed("Login failed");
            return;
        }
        loginButton.setEnabled(false);

        progressDialog = new ProgressDialog(LoginActivity.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String email = emailText.getText().toString();
        mailAddress = email;
        String password = passwordText.getText().toString();

        // TODO: Implement your own authentication logic here.
        result="";
        LT=new LoginThread(this);
        LT.execute(email,password);
    }

    public void finishlogin()
    {
        if(result.equals("error"))
            onLoginFailed("No connection");
        else if(result.equals("LoginSuccess"))
            onLoginSuccess();
        else
            onLoginFailed(result);
        LT.cancel(true);
        progressDialog.dismiss();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                mailAddress = data.getData().toString();
                onLoginSuccess();
                //this.finish();
            }
        }
    }


    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        //moveTaskToBack(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onLoginSuccess() {
        loginButton.setEnabled(true);
        // save email to login file
        String loginfile = "login";
        FileOutputStream outputStream;
        try{
            outputStream = openFileOutput(loginfile, Context.MODE_PRIVATE);
            outputStream.write(mailAddress.getBytes());
            outputStream.close();
            Intent data = new Intent();
            data.setData(Uri.parse(mailAddress));
            setResult(RESULT_OK, data);
        }
        catch (Exception ex){
        }
        finish();
    }

    public void onLoginFailed(String result) {
        Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();
        loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError("Enter a valid email address");
            valid = false;
        } else {
            emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            passwordText.setError(null);
        }

        return valid;
    }


    public void linkSignup(View view) {
        // Start the Signup activity
        Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
        startActivityForResult(intent, REQUEST_SIGNUP);
    }

}
