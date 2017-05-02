package cs460project.explore.Login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import cs460project.explore.R;
import cs460project.explore.User.UserClient;

/**
 * This is the forgot password activity, where a user enteres a new password and verifies their identity
 * using the authentication code that was emailed to them.
 */

public class ForgotPasswordActivity extends Activity implements TextWatcher {

    //MARK: - Private Variables

    private EditText password1EditText, password2EditText, authenticationCodeEditText;
    private String username = "";
    private ImageView passwordMatchImageView, loadingIndicatorImageView;
    private AnimationDrawable frameAnimation;
    private View loadingIndicatorBackgroundView;

    //MARK: - Initializers

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);

        //Retrieving username from previous view
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            username = extras.getString("Username");
        }

        setupLoadingIndicator();
        setupVariables();
    }

    //MARK: - Setup

    private void setupVariables() {
        password1EditText = (EditText) findViewById(R.id.forgot_pass1);
        password1EditText.addTextChangedListener(this);
        password2EditText = (EditText) findViewById(R.id.forgot_pass2);
        password2EditText.addTextChangedListener(this);
        authenticationCodeEditText = (EditText) findViewById(R.id.ver_code);
        passwordMatchImageView = (ImageView) findViewById(R.id.pass_check);
        passwordMatchImageView.setVisibility(View.INVISIBLE);
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

    //MARK: - TextWatcher

    /* When the user enters text into both password fields, an image appears to indicate whether the two
    match or not */
    @Override
    public void afterTextChanged(Editable s) {

        String password1 = password1EditText.getText().toString();
        String password2 = password2EditText.getText().toString();

        if (password1.matches("") || password2.matches("")) {
            passwordMatchImageView.setVisibility(View.INVISIBLE);
        } else {
            if (password1.equals(password2)) {
                passwordMatchImageView.setImageResource(R.drawable.check);
                passwordMatchImageView.setVisibility(View.VISIBLE);
            } else {
                passwordMatchImageView.setImageResource(R.drawable.x);
                passwordMatchImageView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
    @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}

    //MARK: - OnClick Functions

    public void setPasswordPressed(View v) {
        Log.i("Sign Up Process", "Set Password Button Pressed.");
        String password1 = this.password1EditText.getText().toString();
        String pass2 = password2EditText.getText().toString();
        String authenticationCode = authenticationCodeEditText.getText().toString();

        dismissKeyboard();

        //Check if user entered all necessary information
        if (password1.isEmpty() || pass2.isEmpty() || authenticationCode.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields.", Toast.LENGTH_LONG).show();
            return;
        }

        //Check if the passwords match
        if (!password1.equals(pass2)) {
            Toast.makeText(this, "Passwords entered do not match.", Toast.LENGTH_LONG).show();
        }

        //starting the animation for the loading indicator
        toggleLoadingIndicator(true);

        //Client call
        UserClient.sharedInstance.resetPassword(username, password1, authenticationCode, new UserClient.GeneralCompletionListener() {
            @Override
            public void onSuccessful() {
                toggleLoadingIndicator(false);

                Log.i("Reset Pass Progress", "Successfully reset pass.");
                Intent intent = new Intent(ForgotPasswordActivity.this, SignInActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailed(String reason) {
                toggleLoadingIndicator(false);
                passwordResetFailedToast();
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
        imm.hideSoftInputFromWindow(password1EditText.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(password2EditText.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(authenticationCodeEditText.getWindowToken(), 0);
    }

    //MARK: - Toasts

    private void passwordResetFailedToast() {
        Toast.makeText(this, "Password reset failed. Please try again", Toast.LENGTH_LONG).show();
    }
}
