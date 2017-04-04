package cs460project.explore.Login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import cs460project.explore.R;
import cs460project.explore.User.MySQLClient;

/**
 * Created by Kiley on 2/23/17.
 */

public class SignUpConfirmActivity extends Activity {

    EditText username, authCode;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);

    }

    public void confirmPressed(View v) {
        Log.i("Return to Sign In", "Confirm Button Pressed.");

        username = (EditText) findViewById(R.id.confirm_user);
        authCode = (EditText) findViewById(R.id.confirm_code);


        MySQLClient mySQLClient = new MySQLClient();
        mySQLClient.confirmUser(username.getText().toString(), authCode.getText().toString(), new MySQLClient.OnConfirmUserCompletionListener() {
            @Override
            public void onConfirmSuccessful() {
                Log.i("Reset Pass Progress", "Successfully reset pass.");
                Intent intent = new Intent(SignUpConfirmActivity.this, SignInActivity.class);
                startActivity(intent);
            }

            @Override
            public void onConfirmFailed(String reason) {
                confirmAccountToast();
            }
        });
    }

    private void confirmAccountToast() {
        Toast.makeText(this, "Account confirmation failed. Please try again", Toast.LENGTH_LONG).show();
    }
}
