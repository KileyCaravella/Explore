package cs460project.explore.Login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import cs460project.explore.R;
import cs460project.explore.User.UserClient;

/**
 * Created by Kiley on 2/23/17.
 */

public class SignUpConfirmActivity extends Activity {

    EditText username, authCode;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);

        setupVariables();
    }

    private void setupVariables() {
        username = (EditText) findViewById(R.id.confirm_user);
        authCode = (EditText) findViewById(R.id.confirm_code);
    }

    public void confirmPressed(View v) {
        Log.i("Return to Sign In", "Confirm Button Pressed.");

        String un = username.getText().toString();
        String auth = authCode.getText().toString();

        if (un.isEmpty() || auth.isEmpty()) {
            Toast.makeText(this, "Please fill out both fields.", Toast.LENGTH_LONG).show();
            return;
        }
        else {
            UserClient.sharedInstance.confirmUser(username.getText().toString(), authCode.getText().toString(), new UserClient.GeneralCompletionListener() {
                @Override
                public void onSuccessful() {
                    Log.i("Confirm Button Pressed", "Successfully created new user.");
                    Intent intent = new Intent(SignUpConfirmActivity.this, SignInActivity.class);
                    startActivity(intent);
                }

                @Override
                public void onFailed(String reason) {
                    confirmAccountToast();
                }
            });
        }
    }

    private void confirmAccountToast() {
        Toast.makeText(this, "Account confirmation failed. Please try again", Toast.LENGTH_LONG).show();
    }
}
