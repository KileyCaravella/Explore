package cs460project.explore.Login;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Locale;

import cs460project.explore.NavigationActivity;
import cs460project.explore.R;
import cs460project.explore.User.LoginClient;

public class SignInActivity extends Activity implements TextToSpeech.OnInitListener {

    EditText usernameEditText, passwordEditText;
    private TextToSpeech speaker;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        setupVariables();

        //Initialize Text to Speech engine (context, listener object)
        speaker = new TextToSpeech(this, this);
    }

    // Implements TextToSpeech.OnInitListener.
    public void onInit(int status) {
        // status can be either TextToSpeech.SUCCESS or TextToSpeech.ERROR.
        if (status == TextToSpeech.SUCCESS) {
            // Set preferred language to US english.
            // If a language is not be available, the result will indicate it.
            int result = speaker.setLanguage(Locale.US);

            //  int result = speaker.setLanguage(Locale.FRANCE);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                // Language data is missing or the language is not supported.
                Log.e("TTS", "Language is not available.");
            } else {
                // The TTS engine has been successfully initialized
                Log.i("TTS", "TTS Initialization successful.");
            }
        } else {
            // Initialization failed.
            Log.e("TTS", "Could not initialize TextToSpeech.");
        }
    }

    private void setupVariables() {
        usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
    }

    public void loginButtonPressed(View v) {
        Log.i("Login", "Login Button Pressed.");
        final String username = usernameEditText.getText().toString();
        final String password = passwordEditText.getText().toString();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill out both fields.", Toast.LENGTH_LONG).show();
            return;
        }

        //login client call goes here...
        LoginClient.sharedInstance.login(username, password, new LoginClient.GeneralCompletionListener() {
                    @Override
                    public void onSuccessful() {
                        Log.i("Yelp Business Progress", "Successfully retrieved businesses");
                        speak(username);
                        Intent intent = new Intent(SignInActivity.this, NavigationActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailed(String reason) {
                        loginFailedToast();
                    }
                });
    }

    private void speak(String text) {
        //To prevent crashing, we check the build version before calling the appropriate version of speak
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            speaker.speak("Welcome " + text, TextToSpeech.QUEUE_FLUSH, null, "Id 0");
        } else {
            speaker.speak("Welcome " + text, TextToSpeech.QUEUE_FLUSH, null);
        }

    }

    private void loginFailedToast() {
        Toast.makeText(this, "Invalid Username or Password. Please try again", Toast.LENGTH_LONG).show();
    }

    public void forgotPassPressed(View v) {
        Log.i("Forgot Password", "Forgot Password Button Pressed.");

        final String username = usernameEditText.getText().toString();

        if (username.isEmpty()) {
            Toast.makeText(this, "Please fill out Username field.", Toast.LENGTH_LONG).show();
            return;
        }

        LoginClient.sharedInstance.forgotPassword(username, new LoginClient.GeneralCompletionListener() {
            @Override
            public void onSuccessful() {
                Log.i("Forgot Pass Progress", "Successfully sent email.");
                Intent intent = new Intent(SignInActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailed(String reason) {
                passForgotToast();
            }
        });
    }

    private void passForgotToast() {
        Toast.makeText(this, "Forgot password failed. Please try again", Toast.LENGTH_LONG).show();
    }

    public void signUpPressed(View v) {
        Log.i("Sign Up", "Sign Up Button Pressed.");
        Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
        startActivity(intent);
        // sign up intent goes here
    }
}
