package cs460project.explore.YelpAPI;

/**
 * Created by Kiley on 2/20/17.
 * Write-up at bottom for what you need to make this work
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

    //MARK: Project Permanent Strings

    private static final String Client_ID = "4NMYNrNO_HRZm9u9mPlH2w";
    private static final String Client_Secret = "roPjBQkz8jRRaIhpw8ScW4y1Z875JJTe22tPF2mKSo7EIoKcW0wNKLp3wFz9yyAF";
    private static final String Base_URL = "https://api.yelp.com/";
    private static final String Token_URL = "oauth2/token";
    private static final String Search_URL = "v3/businesses/search?";
    private static final String Grant_Type = "client_credentials";
    private static final int Businesses_Retrieved_Limit = 10;

    //MARK: Variable Strings

    private String token;
    private AsyncHttpClient client = new AsyncHttpClient();
    private RequestParams requestParams;
    private OnYelpTokenCompletionListener yelpTokenCompletionListener;
    private OnYelpBusinessSearchCompletionListener yelpBusinessSearchCompletionListener;

    //MARK: Interfaces

    public interface OnYelpTokenCompletionListener {
        void onTokenRetrievalSuccessful();
        void onTokenRetrievalFailed(String reason);
    }

    public interface OnYelpBusinessSearchCompletionListener {
        void onBusinessesRetrievalSuccessful(YelpBusiness business);
        void onBusinessesRetrievalFailed(String reason);
    }

    //MARK: Acquiring token from Yelp

    private void getYelpToken(OnYelpTokenCompletionListener listener) {
        yelpTokenCompletionListener = listener;
        setupTokenHeaders();
        setupTokenParams();
        String tokenURL = Base_URL + Token_URL;

        Log.i("Yelp Token Progress", "Retrieving token...");
        client.post(tokenURL, requestParams, tokenResponseHandler());
    }

    //Response handler for when information is returned in regards to the yelp token
    private AsyncHttpResponseHandler tokenResponseHandler() {
        Log.i("Yelp Token Progress", "Response Received...");
        return new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                try {
                    JSONObject object = new JSONObject(new String(response));
                    token = object.getString("token_type") + " " + object.getString("access_token");
                    Log.i("Yelp Token Progress", "Token: " + token);
                    yelpTokenCompletionListener.onTokenRetrievalSuccessful();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                yelpTokenCompletionListener.onTokenRetrievalFailed(errorResponse.toString());
            }
        };
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
        requestParams.put("gramt_type", Grant_Type);
    }

    //MARK: - Search Yelp Businesses (can use latitude and longitude based on current location OR an entered location)

    public void searchYelpBusinesses(Double latitude, Double longitude, OnYelpBusinessSearchCompletionListener listener) {
        yelpBusinessSearchCompletionListener = listener;
        final String searchURL = Base_URL + Search_URL + "limit=" + Businesses_Retrieved_Limit + "&latitude=" + latitude + "&longitude=" + longitude;
        checkTokenAndGetBusinessWithURL(searchURL);
    }

    public void searchYelpBusinesses(String locationName, OnYelpBusinessSearchCompletionListener listener) {
        yelpBusinessSearchCompletionListener = listener;
        locationName = locationName.replace(" ", "-");
        String searchURL = Base_URL + Search_URL + "limit=" + Businesses_Retrieved_Limit + "&location=" + locationName;
        checkTokenAndGetBusinessWithURL(searchURL);
    }

    //Retrieves the yelp token if it has not done so already
    public void checkTokenAndGetBusinessWithURL(final String searchURL) {
        if (token == null) {
            getYelpToken(new YelpAPIClient.OnYelpTokenCompletionListener() {
                @Override
                public void onTokenRetrievalSuccessful() {
                    searchYelpBusinessesWithURL(searchURL);
                }

                @Override
                public void onTokenRetrievalFailed(String reason) {
                    Log.e("Error", "Token Retrieval Failed");
                }
            });
        } else {
            searchYelpBusinessesWithURL(searchURL);
        }
    }

    //Actual Yelp Business API call
    public void searchYelpBusinessesWithURL(String searchURL) {
        setupSearchHeaders();
        client.get(searchURL, searchResponseHandler());
    }

    private void setupSearchHeaders() {
        client.removeAllHeaders();
        client.addHeader("Authorization",token);
    }

    //Response handler for when businesses have been retrieved from Yelp API
    private AsyncHttpResponseHandler searchResponseHandler() {
        Log.i("Yelp Search Progress", "Received response...");
        return new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                try {

                    //Deserializing the business found
                    JSONObject jsonObject = new JSONObject(new String(response));
                    YelpBusiness[] yelpBusinesses = new Gson().fromJson(jsonObject.getString("businesses"), YelpBusiness[].class);

                    //Choosing a random business from the array
                    int businessToTake = (int) Math.floor(Math.random() * Businesses_Retrieved_Limit);
                    yelpBusinessSearchCompletionListener.onBusinessesRetrievalSuccessful(yelpBusinesses[businessToTake]);

                } catch (JSONException e) {
                    yelpBusinessSearchCompletionListener.onBusinessesRetrievalFailed(e.toString());
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                Log.i("Error Retrieving Token", statusCode + " " + errorResponse);
                yelpBusinessSearchCompletionListener.onBusinessesRetrievalFailed(errorResponse.toString());

            }
        };
    }
}

/*
// -- SETUP YELP API HELP -- //

    * getting the YelpAPI token uses a completion handler so it is able to get the necessary authentication information from
    * the server before trying to make any other calls.

    private void setupYelpAPIClient() {
        yelpAPIClient = new YelpAPIClient(new YelpAPIClient.OnYelpTokenCompletionListener() {
            @Override
            public void onTokenRetrievalSuccessful() {
                Log.i("Yelp Token Progress", "Successfully retrieved token");

                // -- NEXT CODE GOES HERE -- //
            }

            @Override
            public void onTokenRetrievalFailed(String reason) {
                Log.i("Yelp Token Progress", "Failed to retrieve token");
                failedToAccessYelpToast();
            }
        });
    }

    private void failedToAccessYelpToast() {
        Toast.makeText(this, "Error Accessing Yelp", Toast.LENGTH_LONG).show();
    }


    * Below is the code to get the location information from the user if they choose to use their location.
    * locationManaging() needs to be called first so it can begin gathering data on the user's location.

    private void locationManaging() {
        Log.i("Location Management status", "enabled");
        try {
            lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {

                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            });
        } catch (SecurityException e) {
            Log.e("Security Exception", "Do not have permission " + e);
        }
    }


    * this is the try/catch block to throw in a method to retrieve an array of YelpAPIBusinesses. Currently it returns an array with one
    * business, but this is for future proofing in the app/scaling.

        try {
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Log.i("Location", "Long: " + location.getLongitude() + " Lat: " + location.getLatitude());
            if (location != null) {
                yelpAPIClient.searchYelpBusinesses(location.getLatitude(), location.getLongitude(), new YelpAPIClient.OnYelpBusinessSearchCompletionListener() {
                    @Override
                    public void onBusinessesRetrievalSuccessful(YelpBusiness[] businesses) {
                        Log.i("Yelp Business Progress", "Successfully retrieved businesses");
                        //.. do what you want with the array here
                    }

                    @Override
                    public void onBusinessesRetrievalFailed(String reason) {
                        Log.i("Yelp Business Progress", "Failed to retrieve businesses");
                        failedToGetBusinessesToast();
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Security Exception", "Do not have permission " + e);
        }

 */