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
import cs460project.explore.User.LoginClient;

/**
 * Created by Kiley on 2/23/17.
 */

public class ForgotPasswordActivity extends Activity implements TextWatcher {

    private EditText password1, password2, verCode;
    private String username = "";
    private ImageView pic;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);

        //Retrieving username from previous view
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            username = extras.getString("Username");
        }

        setupVariables();
    }

    private void setupVariables() {
        password1 = (EditText) findViewById(R.id.forgot_pass1);
        password1.addTextChangedListener(this);
        password2 = (EditText) findViewById(R.id.forgot_pass2);
        password2.addTextChangedListener(this);
        verCode = (EditText) findViewById(R.id.ver_code);
        pic = (ImageView) findViewById(R.id.pass_check);
        pic.setVisibility(View.INVISIBLE);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

        String p1 = password1.getText().toString();
        String p2 = password2.getText().toString();

        if (p1.matches("") || p2.matches("")) {

            pic.setVisibility(View.INVISIBLE);

        } else {

            if (p1.equals(p2)) {
                pic.setImageResource(R.drawable.check);
                pic.setVisibility(View.VISIBLE);
            } else {
                pic.setImageResource(R.drawable.x);
                pic.setVisibility(View.VISIBLE);
            }
        }
    }

    public void setPasswordPressed(View v) {
        Log.i("Return to Sign In", "Set Password Button Pressed.");

        String pass1 = password1.getText().toString();
        String pass2 = password2.getText().toString();
        String vCode = verCode.getText().toString();

        if (pass1.isEmpty() || pass2.isEmpty() || vCode.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields.", Toast.LENGTH_LONG).show();
            return;
        }
        else {
            if (pass1.equals(pass2)) {
                Toast.makeText(this, "Passwords match.", Toast.LENGTH_LONG).show();

                LoginClient.sharedInstance.resetPassword(username, password1.getText().toString(), verCode.getText().toString(), new LoginClient.GeneralCompletionListener() {
                    @Override
                    public void onSuccessful() {

                        Log.i("Reset Pass Progress", "Successfully reset pass.");
                        Intent intent = new Intent(ForgotPasswordActivity.this, SignInActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailed(String reason) {
                        passResetToast();
                    }

                });
            } else {
                pic.setImageResource(R.drawable.x);
                pic.setVisibility(View.VISIBLE);
                Toast.makeText(this, "Passwords entered don't match.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void passResetToast() {
        Toast.makeText(this, "Password reset failed. Please try again", Toast.LENGTH_LONG).show();
    }
}
