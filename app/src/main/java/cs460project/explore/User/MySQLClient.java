package cs460project.explore.User;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Kiley on 3/23/17.
 */

/*
 * This is the client that connects with our AWS server to store, retrieve, modify, and delete users
 * and category/bucket list information. It uses RESTful APIS (GET, POST, DELETE, and PUT) to
 * connect to the backend, and interfaces to create completion listeners for when the web service
 * has finished. To add a new API call, add the appending string to the "URLs and appending URLs"
 * section, then follow the other examples to finish the client call.
    * * Currently, we do not have HEADERS set up on the backend for authentication on calls * *
 */

public class MySQLClient {

    //MARK - URLS and appending URLs

    private final String BASE_URL = "http://sample-env-1.jzxt6wkppr.us-east-1.elasticbeanstalk.com/Explore/website/";
//    private final String BASE_URL = "http://141.133.251.69/Explore/website/";
    private final String LOGIN_URL = "login.php";
    private final String CREATE_USER_URL = "create_user.php";

    //MARK: - Private Variables

    private OnCreateUserCompletionListener createUserCompletionListener;
    private OnLoginCompletionListener loginCompletionListener;

    private AsyncHttpClient client = new AsyncHttpClient();
    private RequestParams requestParams;

    //MARK: - Completion Listeners

    public interface OnCreateUserCompletionListener {
        void onUserSubmissionSuccessful();
        void onUserSubmissionFailed(String reason);
    }

    public interface OnLoginCompletionListener {
        void onLoginSuccessful();
        void onLoginFailed(String reason);
    }

    //MARK: - Client to Log into application

    public void login(String username, String password, OnLoginCompletionListener listener) {
        loginCompletionListener = listener;
        setupLoginParams(username, password);
        String loginURL = BASE_URL + LOGIN_URL;

        Log.i("Login Process", "Request Sent...");
        client.post(loginURL, requestParams, loginResponseHandler());
    }

    private AsyncHttpResponseHandler loginResponseHandler() {
        Log.i("Login Process", "Response Received");

        return new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                try {
                    JSONObject jsonResponse = new JSONObject(new String(response));
                    if (Integer.parseInt(jsonResponse.get("success").toString()) == 1) {

                        //TODO: Add token code
                        loginCompletionListener.onLoginSuccessful();
                    } else {
                        loginCompletionListener.onLoginFailed(jsonResponse.get("message").toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                loginCompletionListener.onLoginFailed(errorResponse.toString());
            }
        };
    }

    private void setupLoginParams(String username, String password) {
        requestParams = new RequestParams();
        requestParams.put("user_auth", username);
        requestParams.put("password", password);
        requestParams.put("android", " ");
    }

    //MARK: - Create New User

    /*
     *   Creating a new user takes in a User object (created on sign up page) and makes a POST call
     *   to the backend. It uses OnCreateUserCompletionListener which returns
     *   onUserSubmissionSuccessful() and onUserSubmissionFailed(String reason). The response
     *   handler parses the JSON (JavaScript Object Notation) object to return whether the call was
     *   successful or a failure. If successful, no object is returned because it was a POST method.
     */

    public void createNewUser(User user, OnCreateUserCompletionListener listener) {
        createUserCompletionListener = listener;
        setupNewUserParams(user);
        String newUserURL = BASE_URL + CREATE_USER_URL;

        Log.i("Create User Process", "Request Sent...");
        client.post(newUserURL, requestParams, newUserResponseHandler());
    }

    private AsyncHttpResponseHandler newUserResponseHandler() {
        Log.i("Create User Process", "Response Received");

        return new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                try {
                    JSONObject jsonResponse = new JSONObject(new String(response));
                    if (Integer.parseInt(jsonResponse.get("success").toString()) == 1) {
                        createUserCompletionListener.onUserSubmissionSuccessful();
                    } else {
                        createUserCompletionListener.onUserSubmissionFailed(jsonResponse.get("message").toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                createUserCompletionListener.onUserSubmissionFailed(errorResponse.toString());
            }
        };
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

}
