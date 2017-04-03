package cs460project.explore.Login;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import cs460project.explore.R;

/**
 * Created by Kiley on 2/23/17.
 */

public class SignUpActivity extends Activity {

    public void signUpPressed(View v) {
        Log.i("Sign Up Confirm", "Sign Up Button Pressed.");

        Intent intent = new Intent(this, SignUpConfirmActivity.class);
        startActivity(intent);
    }
}
