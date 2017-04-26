package cs460project.explore.Login;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import cs460project.explore.R;
import cs460project.explore.User.UserClient;
import cs460project.explore.User.User;

/**
 * This is the Sign Up Activity, where the user registers for a new account. There is an passwordMatchImageView that changes
 * based on whether or not the passwords that the user enters match each other. This information is sent to
 * our backend to be stored, and also causes an email to be sent to the submitted email address with an
 * authentication code to be entered at the next view.
 */

public class SignUpActivity extends Activity implements TextWatcher {

    //MARK: - Private Variables

    private ImageView passwordMatchImageView;
    private EditText username;
    private EditText password1;
    private EditText password2;
    private EditText name;
    private EditText email;
    private EditText lastname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        setupVariables();
    }

    private void setupVariables() {
        passwordMatchImageView = (ImageView) findViewById(R.id.image_password);
        passwordMatchImageView.setVisibility(View.INVISIBLE);

        username = (EditText) findViewById(R.id.username_edittext);
        password1 = (EditText) findViewById(R.id.password1_edittext);
        password1.addTextChangedListener(this);
        password2 = (EditText) findViewById(R.id.password2_edittext);
        password2.addTextChangedListener(this);
        name = (EditText) findViewById(R.id.name_edittext);
        email = (EditText) findViewById(R.id.email_edittext);
        lastname = (EditText) findViewById(R.id.lastname_edittext);
    }

    //MARK: - TextWatcher

    /* When the user enters text into both password fields, an image appears to indicate whether the two
    match or not */
    @Override
    public void afterTextChanged(Editable s) {

        String password1String = password1.getText().toString();
        String password2String = password2.getText().toString();

        if (password1String.matches("") || password2String.matches("")) {
            passwordMatchImageView.setVisibility(View.INVISIBLE);
        } else {
            if (password1String.equals(password2String)) {
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

        User user = new User();
        user.userID = username.getText().toString();
        user.password = password1.getText().toString();
        user.firstName = name.getText().toString();
        user.lastName = lastname.getText().toString();
        user.email = email.getText().toString();

        //TODO: - put loading indicator here

        UserClient.sharedInstance.createNewUser(user, new UserClient.GeneralCompletionListener() {
            @Override
            public void onSuccessful() {
                //To confirmation view
                Intent intent = new Intent(SignUpActivity.this, SignUpConfirmActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailed(String reason) {
                Log.i("Fail", reason);
                signUpFailedToast();
            }
        });
    }

    //MARK: - Toasts

    private void signUpFailedToast() {
        Toast.makeText(this, "Something went wrong. Please try again", Toast.LENGTH_LONG).show();
    }
}
