package cs460project.explore.Login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import cs460project.explore.R;
import cs460project.explore.User.MySQLClient;

/**
 * Created by Kiley on 2/23/17.
 */

public class ForgotPasswordActivity extends Activity {

    EditText password1, password2, verCode;
    ImageView pic;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);

        setupVariables();
    }

    private void setupVariables() {
        password1 = (EditText) findViewById(R.id.forgot_pass1);
        password2 = (EditText) findViewById(R.id.forgot_pass2);
        verCode = (EditText) findViewById(R.id.ver_code);
        pic = (ImageView) findViewById(R.id.passMatchCheck);
    }

    public void setPasswordPressed(View v) {
        Log.i("Return to Sign In", "Set Password Button Pressed.");

        Intent i = getIntent();
        String pass1 = password1.getText().toString();
        String pass2 = password2.getText().toString();
        String vCode = verCode.getText().toString();

        if (pass1.equals(pass2)) {
            pic.setImageResource(R.drawable.check);
            Toast.makeText(this, "Passwords match.", Toast.LENGTH_LONG).show();
        }
        else {
            pic.setImageResource(R.drawable.x);
            Toast.makeText(this, "Passwords entered don't match.", Toast.LENGTH_LONG).show();
        }

        if (pass1.isEmpty() || pass2.isEmpty() || vCode.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields.", Toast.LENGTH_LONG).show();
            return;
        }

        MySQLClient mySQLClient = new MySQLClient();
        mySQLClient.resetPassword(i.getStringExtra("Username"), password1.getText().toString(), verCode.getText().toString(), new MySQLClient.OnResetPasswordCompletionListener() {
            @Override
            public void onResetSuccessful() {
                Log.i("Reset Pass Progress", "Successfully reset pass.");
                Intent intent = new Intent(ForgotPasswordActivity.this, SignInActivity.class);
                startActivity(intent);
            }

            @Override
            public void onResetFailed(String reason) {
                passResetToast();
            }
        });
    }

    private void passResetToast() {
        Toast.makeText(this, "Password reset failed. Please try again", Toast.LENGTH_LONG).show();
    }
}
