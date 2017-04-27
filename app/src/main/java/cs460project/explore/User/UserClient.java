package cs460project.explore.User;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 *
 * This is the User Client. It is in charge of all calls to our personal backend for anything that has to do with
 * the user in including but not limited to logging in, creating a new user, forgetting a password, resetting a password,
 * and confirming the new account. Because all calls are "POST" calls, the calls do not return anything outside of the
 * class so they are able to use the same completion listener. Logging in is the only function that returns something
 * (which is the token), so it has a different response handler.
 *
 */

public class UserClient {

    /**
     * To make sure the token is not overwritten by re-calling the UserClient method, it is impossible to initialize
     * the method from outside of itself, so all classes call UserClient using the static shared instance (singleton).
     */

    public static UserClient sharedInstance = new UserClient();
    private UserClient() {}

    //MARK: - URLS and appending URLs

//    private final String BASE_URL = "http://sample-env-1.jzxt6wkppr.us-east-1.elasticbeanstalk.com/website/";
    private final String BASE_URL = "http://141.133.250.202/website/";
    private final String LOGIN_URL = "login.php";
    private final String CREATE_USER_URL = "create_user.php";
    private final String FORGOT_PASSWORD_URL = "forgot_password.php";
    private final String RESET_PASSWORD_URL = "reset_password.php";
    private final String CONFIRM_USER_URL = "confirm_user.php";

    //MARK: - Variables

    public String token;
    private GeneralCompletionListener generalCompletionListener;
    private AsyncHttpClient client = new AsyncHttpClient();
    private RequestParams requestParams;

    //MARK: - Completion Listeners

    public interface GeneralCompletionListener {
        void onSuccessful();
        void onFailed(String reason);
    }

    //MARK: - Response Handlers

    private AsyncHttpResponseHandler generalResponseHandler() {
        Log.i("User", "Response Received");

        return new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                try {
                    JSONObject jsonResponse = new JSONObject(new String(response));
                    if (Integer.parseInt(jsonResponse.get("success").toString()) == 1) {
                        generalCompletionListener.onSuccessful();
                    } else {
                        generalCompletionListener.onFailed(jsonResponse.get("message").toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    generalCompletionListener.onFailed(e.toString());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                generalCompletionListener.onFailed(errorResponse.toString());
            }
        };
    }

    private AsyncHttpResponseHandler loginResponseHandler() {
        Log.i("User", "Response Received");

        return new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                try {
                    JSONObject jsonResponse = new JSONObject(new String(response));
                    if (Integer.parseInt(jsonResponse.get("success").toString()) == 1) {
                        token = jsonResponse.get("token").toString();
                        generalCompletionListener.onSuccessful();
                    } else {
                        generalCompletionListener.onFailed(jsonResponse.get("message").toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    generalCompletionListener.onFailed(e.toString());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                generalCompletionListener.onFailed(errorResponse.toString());
            }
        };
    }

    //MARK: - Login

    public void login(String username, String password, GeneralCompletionListener listener) {
        generalCompletionListener = listener;
        setupLoginParams(username, password);
        String loginURL = BASE_URL + LOGIN_URL;

        Log.i("Login Process", "Request Sent...");
        client.post(loginURL, requestParams, loginResponseHandler());
    }

    private void setupLoginParams(String username, String password) {
        requestParams = new RequestParams();
        requestParams.put("user_auth", username);
        requestParams.put("password", password);
        requestParams.put("android", " ");
    }

    //MARK: - Create New User

    public void createNewUser(User user, GeneralCompletionListener listener) {
        generalCompletionListener = listener;
        setupNewUserParams(user);
        String newUserURL = BASE_URL + CREATE_USER_URL;

        Log.i("Create User Process", "Request Sent...");
        client.post(newUserURL, requestParams, generalResponseHandler());
    }

    private void setupNewUserParams(User user) {
        requestParams = new RequestParams();
        requestParams.put("user_id", user.userID);
        requestParams.put("password", user.password);
        requestParams.put("email", user.email);
        requestParams.put("first_name", user.firstName);
        requestParams.put("last_name", user.lastName);
        requestParams.put("android", " ");
    }

    // MARK: - Forgot Password

    public void forgotPassword(String username, GeneralCompletionListener listener) {
        generalCompletionListener = listener;
        setupForgotPasswordParams(username);
        String forgotPassURL = BASE_URL + FORGOT_PASSWORD_URL;

        Log.i("Forgot Password Process", "Request Sent...");
        client.post(forgotPassURL, requestParams, generalResponseHandler());
    }

    private void setupForgotPasswordParams(String userID) {
        requestParams = new RequestParams();
        requestParams.put("forgot_password_user_input", userID);
        requestParams.put("android", " ");
    }

    //MARK: - Reset Password

    public void resetPassword(String username, String password, String verCode, GeneralCompletionListener listener) {
        generalCompletionListener = listener;
        setupResetPasswordParams(username, password, verCode);
        String resetPassURL = BASE_URL + RESET_PASSWORD_URL;

        Log.i("Reset Password Process", "Request Sent...");
        client.post(resetPassURL, requestParams, generalResponseHandler());
    }

    private void setupResetPasswordParams(String userID, String password, String verCode) {
        requestParams = new RequestParams();
        requestParams.put("reset_password_user_input", userID);
        requestParams.put("password", password);
        requestParams.put("authentication_code", verCode);
        requestParams.put("android", " ");
    }

    // MARK: - Confirm Account

    public void confirmUser(String username, String authCode, GeneralCompletionListener listener) {
        generalCompletionListener = listener;
        setupConfirmUserParams(username, authCode);
        String confirmUserURL = BASE_URL + CONFIRM_USER_URL;

        Log.i("Confirm User Process", "Request Sent...");
        client.post(confirmUserURL, requestParams, generalResponseHandler());
    }

    private void setupConfirmUserParams(String userID, String authCode) {
        requestParams = new RequestParams();
        requestParams.put("confirm_user_user_input", userID);
        requestParams.put("authentication_code", authCode);
        requestParams.put("android", " ");
    }
}
