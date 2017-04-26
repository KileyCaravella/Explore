package cs460project.explore.YelpAPI;

/**
 * Created by Kiley on 2/20/17.
 *
 * This is the Yelp API Client. It is in charge of all calls to the yelp api including searching and returning
 * specific businesses baSed on their ID.
 */

import android.util.Log;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class YelpAPIClient {

    //MARK: - Singleton

    /**
     * To make sure the token is not overwritten by re-calling the YelpAPIClient method, it is impossible to initialize
     * the method from outside of itself, so all classes call YelpAPIClient using the static shared instance (singleton).
     */

    public static YelpAPIClient sharedInstance = new YelpAPIClient();
    private YelpAPIClient() {}

    //MARK: - URLS, appending URLs, and other static final variables

    private static final String Client_ID = "4NMYNrNO_HRZm9u9mPlH2w";
    private static final String Client_Secret = "roPjBQkz8jRRaIhpw8ScW4y1Z875JJTe22tPF2mKSo7EIoKcW0wNKLp3wFz9yyAF";

    private static final String BASE_URL = "https://api.yelp.com/";
    private static final String Token_URL = "oauth2/token";
    private static final String SEARCH_URL = "v3/businesses/search?";
    private static final String BUSINESS_ID_URL = "v3/businesses/";

    private static final String Grant_Type = "client_credentials";
    private static final int BUSINESSES_RETRIEVED_LIMIT = 10;

    //MARK: - Private Variables

    private String token;
    private AsyncHttpClient client = new AsyncHttpClient();
    private RequestParams requestParams;

    private OnYelpTokenCompletionListener yelpTokenCompletionListener;
    private OnYelpBusinessCompletionListener yelpBusinessCompletionListener;

    //MARK: - Completion Listeners

    public interface OnYelpTokenCompletionListener {
        void onTokenRetrievalSuccessful();
        void onTokenRetrievalFailed(String reason);
    }

    public interface OnYelpBusinessCompletionListener {
        void onBusinessRetrievalSuccessful(YelpBusiness business);
        void onBusinessRetrievalFailed(String reason);
    }

    //MARK: - Response Handlers

    private AsyncHttpResponseHandler tokenResponseHandler() {
        Log.i("Yelp Token Progress", "Response Received...");
        return new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                try {
                    //Retrieving the token from Yelp
                    JSONObject object = new JSONObject(new String(response));
                    token = object.getString("token_type") + " " + object.getString("access_token");
                    Log.i("Yelp Token Progress", "Token: " + token);
                    yelpTokenCompletionListener.onTokenRetrievalSuccessful();
                } catch (JSONException e) {
                    e.printStackTrace();
                    yelpTokenCompletionListener.onTokenRetrievalFailed(e.toString());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                yelpTokenCompletionListener.onTokenRetrievalFailed(errorResponse.toString());
            }
        };
    }

    /**
     * When yelp returns multiple businesses, they are under the category "businesses", whereas single
     * businesses do not have a category. This is used to distinguish between the two calls but use the
     * same general methods.
     */

    private AsyncHttpResponseHandler businessResponseHandler() {
        Log.i("Yelp Search Progress", "Received response...");
        return new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                try {
                    YelpBusiness yelpBusiness;
                    JSONObject jsonObject = new JSONObject(new String(response));

                    if(jsonObject.has("businesses")) {
                        YelpBusiness[] yelpBusinesses = new Gson().fromJson(jsonObject.getString("businesses"), YelpBusiness[].class);

                        //Choosing a random business from the array
                        int businessToTake = (int) Math.floor(Math.random() * BUSINESSES_RETRIEVED_LIMIT);
                        yelpBusiness = yelpBusinesses[businessToTake];

                    } else {
                        yelpBusiness = new Gson().fromJson(jsonObject.toString(), YelpBusiness.class);
                    }

                    yelpBusinessCompletionListener.onBusinessRetrievalSuccessful(yelpBusiness);

                } catch (JSONException e) {
                    e.printStackTrace();
                    yelpBusinessCompletionListener.onBusinessRetrievalFailed(e.toString());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                yelpBusinessCompletionListener.onBusinessRetrievalFailed(errorResponse.toString());
            }
        };
    }

    //MARK: - Get Yelp Token

    private void getYelpToken(OnYelpTokenCompletionListener listener) {
        yelpTokenCompletionListener = listener;
        setupTokenHeaders();
        setupTokenParams();
        String tokenURL = BASE_URL + Token_URL;

        Log.i("Yelp Token Progress", "Retrieving token...");
        client.post(tokenURL, requestParams, tokenResponseHandler());
    }

    //token API call setup (headers and params)
    private void setupTokenHeaders() {
        client.removeAllHeaders();
        client.addHeader("Content_Type", "application/x-www-form-urlencoded");
    }

    private void setupTokenParams() {
        requestParams = new RequestParams();
        requestParams.put("client_id", Client_ID);
        requestParams.put("client_secret", Client_Secret);
        requestParams.put("grant_type", Grant_Type);
    }

    //MARK: - Get Yelp Business

    /**
     * These three functions take different inputs to set up the URL in a unique way to retrieve a business.
     * The URL is then passed to "checkTokenAndGetBusinessWithURL(String url)" which checks if the token has
     * been retrieved from yelp, and retrieves it if it has not yet. Finally, that function calls
     * "getYelpBusinessWithURL(String url)" which actually makes the client call.
     */

    public void getYelpBusinessWithLatAndLong(Double latitude, Double longitude, OnYelpBusinessCompletionListener listener) {
        yelpBusinessCompletionListener = listener;
        final String url = BASE_URL + SEARCH_URL + "limit=" + BUSINESSES_RETRIEVED_LIMIT + "&latitude=" + latitude + "&longitude=" + longitude;
        checkTokenAndGetBusinessWithURL(url);
    }

    public void getYelpBusinessWithLocationName(String locationName, OnYelpBusinessCompletionListener listener) {
        yelpBusinessCompletionListener = listener;

        //The spaces need to be replaced with dashes for it to understand the location the person is requesting
        locationName = locationName.replace(" ", "-");
        String url = BASE_URL + SEARCH_URL + "limit=" + BUSINESSES_RETRIEVED_LIMIT + "&location=" + locationName;
        checkTokenAndGetBusinessWithURL(url);
    }

    public void getYelpBusinessWithBusinessID(String businessID, OnYelpBusinessCompletionListener listener) {
        yelpBusinessCompletionListener = listener;
        String url = BASE_URL + BUSINESS_ID_URL + businessID;
        checkTokenAndGetBusinessWithURL(url);
    }

    //Retrieves the yelp token if it has not done so already
    private void checkTokenAndGetBusinessWithURL(final String url) {
        if (token == null) {
            getYelpToken(new YelpAPIClient.OnYelpTokenCompletionListener() {
                @Override
                public void onTokenRetrievalSuccessful() {
                    getYelpBusinessWithURL(url);
                }

                @Override
                public void onTokenRetrievalFailed(String reason) {
                    Log.e("Error", "Token Retrieval Failed");
                }
            });
        } else {
            getYelpBusinessWithURL(url);
        }
    }

    //Once the token has been retrieved, return the yelp business
    private void getYelpBusinessWithURL(String url) {
        setupSearchHeaders();
        client.get(url, businessResponseHandler());
    }

    private void setupSearchHeaders() {
        client.removeAllHeaders();
        client.addHeader("Authorization",token);
    }
}
