package cs460project.explore.Login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import cs460project.explore.R;

/**
 * Created by Kiley on 2/23/17.
 */

public class ForgotPasswordActivity extends Activity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);

    }

    public void setPasswordPressed(View v) {
        Log.i("Return to Sign In", "Set Password Button Pressed.");

        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
    }
}
