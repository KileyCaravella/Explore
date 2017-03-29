package cs460project.explore.Login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import cs460project.explore.NavigationActivity;
import cs460project.explore.R;

public class SignInActivity extends Activity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

    }

    public void loginButtonPressed(View v) {
        Log.i("Login", "Login Button Pressed.");

        //login client call goes here...

        //The following code goes inside of completion listener "onSuccessful"
        Log.i("Yelp Business Progress", "Successfully retrieved businesses");
        Intent intent = new Intent(SignInActivity.this, NavigationActivity.class);
        startActivity(intent);
    }
}
