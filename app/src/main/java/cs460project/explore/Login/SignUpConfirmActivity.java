package cs460project.explore.Login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import cs460project.explore.R;
import cs460project.explore.User.UserClient;

/**
 * This is the confirm sign up activity. Once the user enters their information in the sign up activity,
 * an email is sent via the backend with an authentication code for the user to enter in order to validate
 * their new account.
 */

public class SignUpConfirmActivity extends Activity {

    //MARK: - Private Variables

    private EditText usernameEditText, authenticationCodeEditText;
    private ImageView loadingIndicatorImageView;
    private AnimationDrawable frameAnimation;
    private View loadingIndicatorBackgroundView;

    //MARK: - Initialization

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_pass);

        setupLoadingIndicator();
        setupVariables();
    }

    //MARK: - Setup

    private void setupVariables() {
        usernameEditText = (EditText) findViewById(R.id.confirm_user);
        authenticationCodeEditText = (EditText) findViewById(R.id.confirm_code);
    }

    /* This function sets up the loading indicator, its animation, and its background. This code is copied
    throughout all views that have a loading indicator. */
    private void setupLoadingIndicator() {
        loadingIndicatorImageView = (ImageView) findViewById(R.id.animation);
        loadingIndicatorImageView.setBackgroundResource(R.drawable.animation);
        loadingIndicatorImageView.setVisibility(View.INVISIBLE);

        loadingIndicatorBackgroundView = findViewById(R.id.animationBackground);
        loadingIndicatorBackgroundView.setVisibility(View.INVISIBLE);

        frameAnimation = (AnimationDrawable) loadingIndicatorImageView.getBackground();
    }

    //MARK: - OnClick Functions

    public void confirmPressed(View v) {
        Log.i("Confirm User Progress", "Confirm Button Pressed.");
        String username = usernameEditText.getText().toString();
        String authenticationCode = authenticationCodeEditText.getText().toString();

        dismissKeyboard();

        //Check if user entered all necessary information
        if (username.isEmpty() || authenticationCode.isEmpty()) {
            Toast.makeText(this, "Please fill out both fields.", Toast.LENGTH_LONG).show();
            return;
        }

        //Starting the animation for the loading indicator
        toggleLoadingIndicator(true);

        //Client call
        UserClient.sharedInstance.confirmUser(usernameEditText.getText().toString(), authenticationCodeEditText.getText().toString(), new UserClient.GeneralCompletionListener() {
            @Override
            public void onSuccessful() {
                toggleLoadingIndicator(false);

                Log.i("Confirm User Progress", "Successfully created new user.");
                confirmAccountSucceededToast();
                Intent intent = new Intent(SignUpConfirmActivity.this, SignInActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailed(String reason) {
                toggleLoadingIndicator(false);
                confirmAccountFailedToast();
            }
        });
    }

    //MARK: - Toggle Methods

    private void toggleLoadingIndicator(Boolean makeVisible) {
        if (makeVisible) {
            loadingIndicatorImageView.setVisibility(View.VISIBLE);
            loadingIndicatorBackgroundView.setVisibility(View.VISIBLE);
            frameAnimation.start();
        } else {
            loadingIndicatorImageView.setVisibility(View.INVISIBLE);
            loadingIndicatorBackgroundView.setVisibility(View.INVISIBLE);
            frameAnimation.stop();
        }
    }

    //Dismisses the keyboard to see the loading indicator better
    private void dismissKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(usernameEditText.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(authenticationCodeEditText.getWindowToken(), 0);
    }

    //MARK: - Toasts

    private void confirmAccountSucceededToast() {
        Toast.makeText(this, "Your new account has been confirmed.", Toast.LENGTH_LONG).show();
    }

    private void confirmAccountFailedToast() {
        Toast.makeText(this, "Account confirmation failed. Please try again.", Toast.LENGTH_LONG).show();
    }
}
