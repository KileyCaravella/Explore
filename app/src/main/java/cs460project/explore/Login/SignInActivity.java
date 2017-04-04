package cs460project.explore.Login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import cs460project.explore.NavigationActivity;
import cs460project.explore.R;
import cs460project.explore.User.MySQLClient;

public class SignInActivity extends Activity {

    EditText usernameEditText, passwordEditText;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        setupVariables();
    }

    private void setupVariables() {
        usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
    }

    public void loginButtonPressed(View v) {
        Log.i("Login", "Login Button Pressed.");
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill out both fields.", Toast.LENGTH_LONG).show();
            return;
        }

        //login client call goes here...
        MySQLClient mySQLClient = new MySQLClient();
        mySQLClient.login(usernameEditText.getText().toString(), passwordEditText.getText().toString(), new MySQLClient.OnLoginCompletionListener() {
                    @Override
                    public void onLoginSuccessful() {
                        Log.i("Yelp Business Progress", "Successfully retrieved businesses");
                        Intent intent = new Intent(SignInActivity.this, NavigationActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onLoginFailed(String reason) {
                        loginFailedToast();
                    }
                });
    }

    private void loginFailedToast() {
        Toast.makeText(this, "Invalid Username or Password. Please try again", Toast.LENGTH_LONG).show();
    }

    public void forgotPassPressed(View v) {
        Log.i("Forgot Password", "Forgot Password Button Pressed.");

        String username = usernameEditText.getText().toString();

        if (username.isEmpty()) {
            Toast.makeText(this, "Please fill out Username field.", Toast.LENGTH_LONG).show();
            return;
        }

        MySQLClient mySQLClient = new MySQLClient();
        mySQLClient.forgotPassword(usernameEditText.getText().toString(), new MySQLClient.OnForgotPasswordCompletionListener() {
            @Override
            public void onForgotSuccessful() {
                Log.i("Forgot Pass Progress", "Successfully sent email.");
                Intent intent = new Intent(SignInActivity.this, ForgotPasswordActivity.class);
                intent.putExtra("Username", usernameEditText.getText().toString());
                startActivity(intent);
            }

            @Override
            public void onForgotFailed(String reason) {
                passForgotToast();
            }
        });
    }

    private void passForgotToast() {
        Toast.makeText(this, "Forgot password failed. Please try again", Toast.LENGTH_LONG).show();
    }
}
