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
import cs460project.explore.User.User;

/**
 * This is the Sign Up Activity, where the user registers for a new account. There is an passwordMatchImageView that changes
 * based on whether or not the passwords that the user enters match each other. This information is sent to
 * our backend to be stored, and also causes an emailEditText to be sent to the submitted emailEditText address with an
 * authentication code to be entered at the next view.
 */

public class SignUpActivity extends Activity implements TextWatcher {

    //MARK: - Private Variables

    private EditText usernameEditText;
    private EditText password1EditText;
    private EditText password2EditText;
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText emailEditText;
    private ImageView passwordMatchImageView, loadingIndicatorImageView;
    private AnimationDrawable frameAnimation;
    private View loadingIndicatorBackgroundView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        setupLoadingIndicator();
        setupVariables();
    }

    //MARK: - Setup

    private void setupVariables() {
        passwordMatchImageView = (ImageView) findViewById(R.id.image_password);
        passwordMatchImageView.setVisibility(View.INVISIBLE);

        usernameEditText = (EditText) findViewById(R.id.username_edittext);
        password1EditText = (EditText) findViewById(R.id.password1_edittext);
        password1EditText.addTextChangedListener(this);
        password2EditText = (EditText) findViewById(R.id.password2_edittext);
        password2EditText.addTextChangedListener(this);
        firstNameEditText = (EditText) findViewById(R.id.name_edittext);
        emailEditText = (EditText) findViewById(R.id.email_edittext);
        lastNameEditText = (EditText) findViewById(R.id.lastname_edittext);
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

    //Necessary for TextWatcher
    @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
    @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}

    //MARK: - OnClick Functions

    public void signUpPressed(View v) {
        Log.i("Sign Up Confirm", "Sign Up Button Pressed.");
        String username = usernameEditText.getText().toString();
        String password1 = password1EditText.getText().toString();
        String password2 = password2EditText.getText().toString();
        String firstName = firstNameEditText.getText().toString();
        String lastName = lastNameEditText.getText().toString();
        String email = emailEditText.getText().toString();

        dismissKeyboard();

        //Check if user entered all necessary information
        if (username.isEmpty() || password1.isEmpty() || password2.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields.", Toast.LENGTH_LONG).show();
            return;
        }

        //Check that the passwords match
        if (!password1.equals(password2)) {
            Toast.makeText(this, "Passwords entered do not match.", Toast.LENGTH_LONG).show();
            return;
        }

        //Creating user object
        User user = new User();
        user.userID = username;
        user.password = password1;
        user.firstName = firstName;
        user.lastName = lastName;
        user.email = email;

        //starting the animation for the loading indicator
        toggleLoadingIndicator(true);

        UserClient.sharedInstance.createNewUser(user, new UserClient.GeneralCompletionListener() {
            @Override
            public void onSuccessful() {
                toggleLoadingIndicator(false);

                Intent intent = new Intent(SignUpActivity.this, SignUpConfirmActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailed(String reason) {
                toggleLoadingIndicator(false);
                signUpFailedToast();
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

    private void dismissKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(usernameEditText.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(password1EditText.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(password2EditText.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(firstNameEditText.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(lastNameEditText.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(emailEditText.getWindowToken(), 0);
    }

    //MARK: - Toasts

    private void signUpFailedToast() {
        Toast.makeText(this, "Something went wrong. Please try again", Toast.LENGTH_LONG).show();
    }
}
