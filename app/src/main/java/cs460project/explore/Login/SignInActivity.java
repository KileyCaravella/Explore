package cs460project.explore.Login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import cs460project.explore.Category.CategoryClient;
import cs460project.explore.NavigationActivity;
import cs460project.explore.R;
import cs460project.explore.User.UserClient;

/**
 * This is the SignInActivity, which is the first Activity that appears when the app launches. It is the central
 * screen for users to navigate signing up, if they forgot their password, and logging in. When the user
 * successfully logs in, the app will welcome them using their username.
 */

public class SignInActivity extends Activity implements TextToSpeech.OnInitListener {

    //MARK: - Private Variables

    private TextToSpeech speaker;
    private ImageView loadingIndicatorImageView;
    private AnimationDrawable frameAnimation;
    private View loadingIndicatorBackgroundView;

    //MARK: - Public Variables

    public EditText usernameEditText, passwordEditText;

    //MARK: - Initialization

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        speaker = new TextToSpeech(this, this);
        setupVariables();
        setupLoadingIndicator();
    }

    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = speaker.setLanguage(Locale.US);

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "Language is not available.");
            } else {
                Log.i("TTS", "TTS Initialization successful.");
            }
        } else {
            Log.e("TTS", "Could not initialize TextToSpeech.");
        }
    }

    //MARK: - Setup

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

    private void setupVariables() {
        usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
    }

    //MARK: - OnClick functions

    public void loginButtonPressed(View v) {
        Log.i("Login", "Login Button Pressed.");
        final String username = usernameEditText.getText().toString();
        final String password = passwordEditText.getText().toString();

        dismissKeyboard();

        //check if user entered all necessary information
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill out both fields.", Toast.LENGTH_LONG).show();
            return;
        }

        //starting the animation for the loading indicator
        toggleLoadingIndicator(true);

        //Client call
        UserClient.sharedInstance.login(username, password, new UserClient.GeneralCompletionListener() {
            @Override
            public void onSuccessful() {
                CategoryClient.sharedInstance.getUsersCategories(new CategoryClient.CompletionListenerWithArray() {
                    @Override
                    public void onSuccessful(ArrayList<String> arrayList) {
                        toggleLoadingIndicator(false);
                        speak(username);

                        //Setting the categoriesList to the returned array for future use
                        CategoryClient.sharedInstance.categoriesList = arrayList;

                        //Once successfully logged in, the user is brought to the Navigation Activity
                        Log.i("Yelp Business Progress", "Successfully retrieved businesses");
                        Intent intent = new Intent(SignInActivity.this, NavigationActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailed(String reason) {
                        toggleLoadingIndicator(false);
                        loginFailedToast();
                    }
                });
            }

            @Override
            public void onFailed(String reason) {
                toggleLoadingIndicator(false);
                loginFailedToast();
            }
        });
    }

    /* When the forgot password button is pressed, it makes a call to our backend that resets the authentication
     * code assigned to that user, and sends them an email with the new one. The user is then brought to a view
     * to enter in their new password, along with the authentication code from their email.
     */
    public void forgotPasswordPressed(View v) {
        Log.i("Forgot Password", "Forgot Password Button Pressed.");
        final String username = usernameEditText.getText().toString();

        dismissKeyboard();

        //Make sure the user entered their username into the appropriate view
        if (username.isEmpty()) {
            Toast.makeText(this, "Please enter your username and press 'ForgotPassword' again.", Toast.LENGTH_LONG).show();
            return;
        }

        //starting the animation for the loading indicator
        toggleLoadingIndicator(true);

        //Client call
        UserClient.sharedInstance.forgotPassword(username, new UserClient.GeneralCompletionListener() {
            @Override
            public void onSuccessful() {
                toggleLoadingIndicator(false);

                Log.i("Forgot Pass Progress", "Successfully sent email.");
                Intent intent = new Intent(SignInActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailed(String reason) {
                toggleLoadingIndicator(false);
                forgotPasswordFailedToast();
            }
        });
    }

    public void signUpPressed(View v) {
        Log.i("Sign Up", "Sign Up Button Pressed.");
        Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
        startActivity(intent);
    }

    //MARK: - Text-To-Speech

    //To prevent crashing, we check the build version before calling the appropriate version of speak
    private void speak(String text) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            speaker.speak("Welcome " + text, TextToSpeech.QUEUE_FLUSH, null, "Id 0");
        } else {
            speaker.speak("Welcome " + text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    //MARK: - Toggling Methods

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
        imm.hideSoftInputFromWindow(passwordEditText.getWindowToken(), 0);
    }

    //MARK: - Toasts

    private void loginFailedToast() {
        Toast.makeText(this, "Invalid username or password. Please try again", Toast.LENGTH_LONG).show();
    }

    private void forgotPasswordFailedToast() {
        Toast.makeText(this, "Invalid username. Please try again", Toast.LENGTH_LONG).show();
    }
}
