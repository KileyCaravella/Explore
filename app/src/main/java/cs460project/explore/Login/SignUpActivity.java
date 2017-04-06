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

import cs460project.explore.R;

/**
 * Created by Kiley on 2/23/17.
 */

public class SignUpActivity extends Activity implements TextWatcher {

    private SignUpActivity signUpActivity;
    private ImageView image;
    private EditText username;
    private EditText password1;
    private EditText password2;
    private EditText name;
    private EditText email;
    private EditText phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        image = (ImageView) findViewById(R.id.image_password);
        username = (EditText) findViewById(R.id.username_edittext);
        password1 = (EditText) findViewById(R.id.password1_edittext);
        password1.addTextChangedListener(this);
        password2 = (EditText) findViewById(R.id.password2_edittext);
        password2.addTextChangedListener(this);
        name = (EditText) findViewById(R.id.name_edittext);
        email = (EditText) findViewById(R.id.email_edittext);
        phone = (EditText) findViewById(R.id.phone_edittext);

        image.setVisibility(View.INVISIBLE);

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

        String password1String = password1.getText().toString();
        String password2String = password2.getText().toString();

        if (password1String.matches("") || password2String.matches("")) {

            image.setVisibility(View.INVISIBLE);

        } else {

            if (password1String.equals(password2String)) {
                image.setImageResource(R.drawable.check);
                image.setVisibility(View.VISIBLE);
            } else {
                image.setImageResource(R.drawable.x);
                image.setVisibility(View.VISIBLE);
            }
        }
    }

    public void signUpPressed(View v) {
        Log.i("Sign Up Confirm", "Sign Up Button Pressed.");

        //client call goes here

        Intent intent = new Intent(SignUpActivity.this, SignUpConfirmActivity.class);
        startActivity(intent);
      }
}
