package cs460project.explore;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.google.gson.Gson;

import cs460project.explore.Category.BucketListActivity;
import cs460project.explore.YelpAPI.SingleYelpBusinessActivity;
import cs460project.explore.YelpAPI.YelpAPIClient;
import cs460project.explore.YelpAPI.YelpBusiness;

/**
 * Created by Kiley on 3/7/17.
 */

public class NavigationActivity extends AppCompatActivity {

    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation);
        locationManaging();
    }

    public void randomButtonPressed(View v) {
        Log.i("Random Button View", "Random button pressed.");
        try {
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Log.i("Location", "Long: " + location.getLongitude() + " Lat: " + location.getLatitude());
            if (location != null) {
                YelpAPIClient.sharedInstance.getYelpBusinessWithLatAndLong(location.getLatitude(), location.getLongitude(), new YelpAPIClient.OnYelpBusinessCompletionListener() {
                    @Override
                    public void onBusinessRetrievalSuccessful(YelpBusiness business) {
                        Log.i("Yelp Business Progress", "Successfully retrieved businesses");
                        Intent intent = new Intent(NavigationActivity.this, SingleYelpBusinessActivity.class);
                        intent.putExtra("YelpBusiness", new Gson().toJson(business));
                        startActivity(intent);
                    }

                    @Override
                    public void onBusinessRetrievalFailed(String reason) {
                        Log.i("Yelp Business Progress", "Failed to retrieve businesses");
                        failedToGetBusinessesToast();
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Security Exception", "Do not have permission " + e);
        }
    }

    private void failedToGetBusinessesToast() {
        Toast.makeText(this, "Error Retrieving Business", Toast.LENGTH_LONG).show();
    }

    public void bucketButtonPressed(View v) {
        Log.i("Bucket View", "Bucket View button pressed.");
        Intent intent = new Intent(NavigationActivity.this, BucketListActivity.class);
        startActivity(intent);
    }


    public void searchButtonPressed(View v){
        Log.i("Search View", "Search View Button Pressed");
        Intent search = new Intent(NavigationActivity.this, SearchActivity.class);
        startActivity(search);
    }



    //MARK: Location Management (for when current location is chosen)

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
            Log.e("Security Exception", "Do not have permission " + e);
        }
    }
}
