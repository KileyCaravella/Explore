package cs460project.explore;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

import cs460project.explore.Category.BucketListActivity;
import cs460project.explore.Category.CategoryClient;
import cs460project.explore.YelpAPI.SingleYelpBusinessActivity;
import cs460project.explore.YelpAPI.YelpAPIClient;
import cs460project.explore.YelpAPI.YelpBusiness;

/**
 * This is the navigation activity that is the "home" point for the user once they are in the app. It leads
 * the user between randomizing a yelp business, looking at their bucket list categories, and searching by
 * location. If they choose the randomizing button, it takes their current location and uses that to find
 * a business in their area.
 */

public class NavigationActivity extends Activity {

    //MARK: - Private Variables

    private LocationManager locationManager;
    private ImageView loadingIndicatorImageView;
    private AnimationDrawable frameAnimation;
    private View loadingIndicatorBackgroundView;

    //MARK: - Initialization

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        locationManaging();
        setupLoadingIndicator();
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

    //MARK: - OnClick Functions

    public void randomButtonPressed(View v) {
        Log.i("Navigation Activity", "Random button pressed.");
        try {
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Log.i("Location", "Long: " + location.getLongitude() + " Lat: " + location.getLatitude());

            //starting the animation for the loading indicator
            toggleLoadingIndicator(true);

            //Client call
            YelpAPIClient.sharedInstance.getYelpBusinessWithLatAndLong(location.getLatitude(), location.getLongitude(), new YelpAPIClient.OnYelpBusinessCompletionListener() {
                @Override
                public void onBusinessRetrievalSuccessful(YelpBusiness business) {
                    toggleLoadingIndicator(false);

                    //Sending the yelp business retrieved to the next activity
                    Log.i("Yelp Business Progress", "Successfully retrieved businesses");
                    Intent intent = new Intent(NavigationActivity.this, SingleYelpBusinessActivity.class);
                    intent.putExtra("YelpBusiness", new Gson().toJson(business));
                    startActivity(intent);
                }

                @Override
                public void onBusinessRetrievalFailed(String reason) {
                    Log.i("Yelp Business Progress", "Failed to retrieve businesses");
                    toggleLoadingIndicator(false);
                    failedToGetBusinessesToast();
                }
            });
        } catch (SecurityException e) {
            securityExceptionToast();
            Log.e("Security Exception", "Do not have permission " + e);
        }
    }

    public void bucketButtonPressed(View v) {
        Log.i("Navigation Activity", "Bucket View button pressed.");

        //Sending the categories retrieved to the next activity
        Intent intent = new Intent(NavigationActivity.this, BucketListActivity.class);
        startActivity(intent);
    }


    public void searchButtonPressed(View v) {
        Log.i("Navigation Activity", "Search Button Pressed");
        Intent search = new Intent(NavigationActivity.this, SearchActivity.class);
        startActivity(search);
    }

    //MARK: Location Management (to initialize the user's current location)

    private void locationManaging() {
        Log.i("Location status", "enabled");
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, new LocationListener() {
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
            securityExceptionToast();
            Log.e("Security Exception", "Do not have permission " + e);
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

    //MARK: - Toasts

    private void securityExceptionToast() {
        Toast.makeText(this, "Do not have permission to use your location.", Toast.LENGTH_LONG).show();
    }

    private void failedToGetBusinessesToast() {
        Toast.makeText(this, "Error Retrieving Business", Toast.LENGTH_LONG).show();
    }

    private void failedToGetCategoriesToast() {
        Toast.makeText(this, "Error Retrieving Your Categories", Toast.LENGTH_LONG).show();
    }
}
