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

public class SignUpConfirmActivity extends Activity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);

    }

    public void confirmPressed(View v) {
        Log.i("Return to Sign In", "Confirm Button Pressed.");

        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
    }
}
