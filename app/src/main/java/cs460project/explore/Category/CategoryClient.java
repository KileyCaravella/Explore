package cs460project.explore.Category;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cs460project.explore.User.UserClient;
import cz.msebera.android.httpclient.Header;

/**
 * This is the Category Client. It is in charge of all calls to our personal backend for anything that has to do with
 * categories including but not limited to creating a new category, adding a yelp business to a category, adding a
 * business to the rejected group, retrieving a user's categories, retrieving businesses in a specific category, removing
 * a business from a category and removing a business from the rejected group. There are both "POST" calls and "GET"
 * calls in this section, so there is a completion listener for "POST" calls and a completion listener for "GET" calls
 * that returns an ArrayList of the information the person wants to retrieve. Yelp does not allow anyone to store all of
 * a business' information, so we are only allowed to store the business' ID, then use the ID to retrieve their information
 * from yelp later.
 */

public class CategoryClient {

    /**
     * For future-proofing, in case we want to store category information, the Category Client is a singleton, meaning
     * it cannot be initialized from the outside but instead through a static initialization.
     */

    public static CategoryClient sharedInstance = new CategoryClient();

    private CategoryClient() {
    }

    //MARK: - URLS and appending URLs

    //    private final String BASE_URL = "http://sample-env-1.jzxt6wkppr.us-east-1.elasticbeanstalk.com/website/";
    private final String BASE_URL = "http://141.133.251.36/website/";
    private final String NEW_CATEGORY_URL = "";
    private final String NEW_CATEGORY_BUSINESS_URL = "";
    private final String NEW_REJECTED_BUSINESS_URL = "";
    private final String GET_CATEGORY_URL = "get_categories.php";
    private final String GET_BUSINESSES_URL = "";
    private final String GET_REJECTED_BUSINESSES_URL = "";
    private final String REMOVE_CATEGORY_URL = "";
    private final String REMOVE_CATEGORY_BUSINESS_URL = "";
    private final String REMOVE_REJECTED_BUSINESS_URL = "";

    //MARK: - Private Variables

    private CompletionListener completionListener;
    private CompletionListenerWithArray completionListenerWithArray;
    private AsyncHttpClient client = new AsyncHttpClient();
    private RequestParams requestParams;


    //MARK: - Response Handlers

    private AsyncHttpResponseHandler responseHandler() {
        Log.i("Create User Process", "Response Received");

        return new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                try {
                    JSONObject jsonResponse = new JSONObject(new String(response));
                    if (Integer.parseInt(jsonResponse.get("success").toString()) == 1) {
                        completionListener.onSuccessful();
                    } else {
                        completionListener.onFailed(jsonResponse.get("message").toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    completionListener.onFailed(e.toString());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                completionListener.onFailed(errorResponse.toString());
            }
        };
    }

    private AsyncHttpResponseHandler responseHandlerWithArray() {
        Log.i("Create User Process", "Response Received");

        return new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                try {
                    JSONObject jsonResponse = new JSONObject(new String(response));

                    //Parsing jsonResponse array as a jsonArray
                    ArrayList<String> arrayResponse = new ArrayList<String>();
                    JSONArray jsonArray = (JSONArray) jsonResponse.get("category");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        arrayResponse.add(jsonArray.getString(i));
                    }

                    Log.i("Array", arrayResponse.get(0));
                    completionListenerWithArray.onSuccessful(arrayResponse);

                } catch (JSONException e) {
                    e.printStackTrace();
                    completionListenerWithArray.onFailed(e.toString());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                completionListenerWithArray.onFailed(errorResponse.toString());
            }
        };
    }

    //MARK: - Completion Listeners

    public interface CompletionListener {
        void onSuccessful();

        void onFailed(String reason);
    }

    public interface CompletionListenerWithArray {
        void onSuccessful(ArrayList<String> arrayList);

        void onFailed(String reason);
    }

    //MARK: - Creating New Category

    public void createNewCategory(String categoryName, CompletionListener listener) {
        completionListener = listener;
        setupCreateNewCategoryParams(categoryName);
        String url = BASE_URL + NEW_CATEGORY_URL;

        Log.i("New Category Process", "Request Sent...");
        client.post(url, requestParams, responseHandler());
    }

    private void setupCreateNewCategoryParams(String categoryName) {
        requestParams = new RequestParams();
        requestParams.put("category_name", categoryName);
        requestParams.put("token", UserClient.sharedInstance.token);
        requestParams.put("android", " ");
    }

    //MARK: - Adding Yelp Business to a Category

    public void addBusinessToCategory(String businessID, String categoryName, CompletionListener listener) {
        completionListener = listener;
        setupAddBusinessToCategoryParams(businessID, categoryName);
        String url = BASE_URL + NEW_CATEGORY_BUSINESS_URL;

        Log.i("Add Business Process", "Request Sent...");
        client.post(url, requestParams, responseHandler());
    }

    private void setupAddBusinessToCategoryParams(String businessID, String categoryName) {
        requestParams = new RequestParams();
        requestParams.put("business_id", businessID);
        requestParams.put("category_name", categoryName);
        requestParams.put("token", UserClient.sharedInstance.token);
        requestParams.put("android", " ");
    }

    //MARK: - Adding Yelp Business to Rejected Group

    public void addBusinessToRejected(String businessID, CompletionListener listener) {
        completionListener = listener;
        setupAddBusinessToRejectedParams(businessID);
        String url = BASE_URL + NEW_REJECTED_BUSINESS_URL;

        Log.i("Reject Business Process", "Request Sent...");
        client.post(url, requestParams, responseHandler());
    }

    private void setupAddBusinessToRejectedParams(String businessID) {
        requestParams = new RequestParams();
        requestParams.put("business_id", businessID);
        requestParams.put("token", UserClient.sharedInstance.token);
        requestParams.put("android", " ");
    }

    //MARK: - Getting User's Categories

    public void getUsersCategories(CompletionListenerWithArray listener) {
        completionListenerWithArray = listener;
        setupGetUsersCategoriesHeaders();
        String url = BASE_URL + GET_CATEGORY_URL;

        Log.i("Get Categories Process", "Request Sent...");
        client.removeAllHeaders();
        Log.i("token", UserClient.sharedInstance.token);
        client.addHeader("token", UserClient.sharedInstance.token);
        client.addHeader("android", " ");
        client.post(url, requestParams, responseHandlerWithArray());
    }

    private void setupGetUsersCategoriesHeaders() {

    }

    //MARK: - Getting Businesses in Specific Category

    public void getBusinessesFromCategory(String category, CompletionListenerWithArray listener) {
        completionListenerWithArray = listener;
        setupGetBusinessesFromCategoryParams(category);
        String url = BASE_URL + GET_BUSINESSES_URL;

        Log.i("Get Businesses Process", "Request Sent...");
        client.post(url, requestParams, responseHandlerWithArray());
    }

    private void setupGetBusinessesFromCategoryParams(String category) {
        requestParams = new RequestParams();
        requestParams.put("category_id", category);
        requestParams.put("token", UserClient.sharedInstance.token);
        requestParams.put("android", " ");
    }

    //MARK: - Getting Rejected Businesses

    public void getBusinessesFromRejected(CompletionListenerWithArray listener) {
        completionListenerWithArray = listener;
        setupGetBusinessesFromRejectedParams();
        String url = BASE_URL + GET_REJECTED_BUSINESSES_URL;

        Log.i("Get Rejected Process", "Request Sent...");
        client.post(url, requestParams, responseHandlerWithArray());
    }

    private void setupGetBusinessesFromRejectedParams() {
        requestParams = new RequestParams();
        requestParams.put("token", UserClient.sharedInstance.token);
        requestParams.put("android", " ");
    }

    //MARK: - Remove Category

    public void removeCategory(String category, CompletionListenerWithArray listener) {
        completionListenerWithArray = listener;
        setupRemoveCategoryParams(category);
        String url = BASE_URL + REMOVE_CATEGORY_URL;

        Log.i("Remove Category Process", "Request Sent...");
        client.post(url, requestParams, responseHandler());
    }

    private void setupRemoveCategoryParams(String category) {
        requestParams = new RequestParams();
        requestParams.put("category_id", category);
        requestParams.put("token", UserClient.sharedInstance.token);
        requestParams.put("android", " ");
    }

    //MARK: - Removing Business from a Category

    public void removeBusinessFromCategory(String business, String category, CompletionListenerWithArray listener) {
        completionListenerWithArray = listener;
        setupRemoveBusinessFromCategoryParams(business, category);
        String url = BASE_URL + REMOVE_CATEGORY_BUSINESS_URL;

        Log.i("Remove Business Process", "Request Sent...");
        client.post(url, requestParams, responseHandler());
    }

    private void setupRemoveBusinessFromCategoryParams(String business, String category) {
        requestParams = new RequestParams();
        requestParams.put("business_ID", business);
        requestParams.put("category_ID", category);
        requestParams.put("token", UserClient.sharedInstance.token);
        requestParams.put("android", " ");
    }

    //MARK: - Removing Business from Rejected Group

    public void removeBusinessFromRejected(String business, CompletionListenerWithArray listener) {
        completionListenerWithArray = listener;
        setupRemoveBusinessFromRejectedParams(business);
        String url = BASE_URL + REMOVE_REJECTED_BUSINESS_URL;

        Log.i("Remove Business Process", "Request Sent...");
        client.post(url, requestParams, responseHandler());
    }

    private void setupRemoveBusinessFromRejectedParams(String business) {
        requestParams = new RequestParams();
        requestParams.put("business_ID", business);
        requestParams.put("token", UserClient.sharedInstance.token);
        requestParams.put("android", " ");
    }
}
