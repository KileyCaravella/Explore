package cs460project.explore;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import cs460project.explore.YelpAPI.YelpAPIClient;
import cs460project.explore.YelpAPI.YelpBusiness;


public class MainActivity extends AppCompatActivity {

    //Create Text Views and ImageViews
    ImageView yelpPic, rating, linkToYelpSite;
    TextView locationName, reviewCount, isClosed, locationPhone, website,
            distance, address;
    String yelpPicURL = "https://cms-assets.tutsplus.com/uploads/users/21/posts/19431/featured_image/CodeFeature.jpg";

    private YelpAPIClient yelpAPIClient;
    private Location location;
    private LocationManager lm;


    public MainActivity ()  {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yelp_display);

        //Bind the variables to their respective layout elements
        //String ImageURL = "https://s3-media3.fl.yelpcdn.com/bphoto/4VDxrlrV7Bk2o-RnkU97HQ/o.jpg";

        yelpPic = (ImageView) findViewById(R.id.locationPic);
        rating = (ImageView) findViewById(R.id.rating);
        linkToYelpSite = (ImageView) findViewById(R.id.linkYelpSite);
        locationName = (TextView) findViewById(R.id.name);
        reviewCount = (TextView) findViewById(R.id.reviewCount);
        isClosed = (TextView) findViewById(R.id.isClosed);
        locationPhone = (TextView) findViewById(R.id.dispPhone);
        website = (TextView) findViewById(R.id.url);
        distance = (TextView) findViewById(R.id.distance);
        address = (TextView) findViewById(R.id.location);

        //creates api client

//switch statement to hopefully change which rating image is displayed
        //not sure what format Yelp returns

        int NumStar = 1;
        switch (NumStar){
            case 0:
                Picasso.with(this)
                        .load(R.mipmap.rating_star_0)
                        .into(rating);
                break;
            case 1:
                Picasso.with(this)
                        .load(R.mipmap.rating_star_1)
                        .into(rating);
                break;
            case 2:
                Picasso.with(this)
                        .load(R.mipmap.rating_star_1_5)
                        .into(rating);
                break;
            case 3:
                Picasso.with(this)
                        .load(R.mipmap.rating_star_2)
                        .into(rating);
                break;
            case 4:
                Picasso.with(this)
                        .load(R.mipmap.rating_star_2_5)
                        .into(rating);
                break;
            case 5:
                Picasso.with(this)
                        .load(R.mipmap.rating_star_3)
                        .into(rating);
                break;
            case 6:
                Picasso.with(this)
                        .load(R.mipmap.rating_star_3_5)
                        .into(rating);
                break;
            case 7:
                Picasso.with(this)
                        .load(R.mipmap.rating_star_4)
                        .into(rating);
                break;
            case 8:
                Picasso.with(this)
                        .load(R.mipmap.rating_star_4_5)
                        .into(rating);
                break;
            case 9:
                Picasso.with(this)
                        .load(R.mipmap.rating_star_5)
                        .into(rating);
                break;

        }//switch statement



//loads Yelp picture, handles an error occuring and loads a default picture
        Picasso.with(this)
                .load(yelpPicURL)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.rating_star_0)
                .into(yelpPic);

//this will have a pic of the Yelp logo and link to the yelp page when clicked
        Picasso.with(this)
                .load(R.mipmap.ic_launcher)
                .into(linkToYelpSite);

        //calls method that calls yelp api
setupYelpAPIClient();

    }//on create

    private void setupYelpAPIClient() {
        yelpAPIClient = new YelpAPIClient(new YelpAPIClient.OnYelpTokenCompletionListener() {
            @Override
            public void onTokenRetrievalSuccessful() {
                Log.i("Yelp Token Progress", "Successfully retrieved token");

                GetBusiness();
            }

            @Override
            public void onTokenRetrivalFailed(String reason) {
                Log.i("Yelp Token Progress", "Failed to retrieve token");
                failedToAccessYelpToast();
            }
        });
    }

    private void failedToAccessYelpToast() {
        Toast.makeText(this, "Error Accessing Yelp", Toast.LENGTH_LONG).show();
    }

    private void failedToGetBusinessesToast() {
        Toast.makeText(this, "Error Accessing Yelp", Toast.LENGTH_LONG).show();
    }

    private void GetBusiness(){
        try {
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Log.i("Location", "Long: " + location.getLongitude() + " Lat: " + location.getLatitude());
            if (location != null) {
                yelpAPIClient.searchYelpBusinesses(location.getLatitude(), location.getLongitude(), new YelpAPIClient.OnYelpBusinessSearchCompletionListener() {
                    @Override
                    public void onBusinessesRetrievalSuccessful(YelpBusiness[] businesses) {
                        Log.i("Yelp Business Progress", "Successfully retrieved businesses");
                        //.. do what you want with the array here
                        Log.i("Business name", businesses[0].name);
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
    }

}//Main
