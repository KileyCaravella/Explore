package cs460project.explore;

import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import cs460project.explore.YelpAPI.YelpAPIClient;
import cs460project.explore.YelpAPI.YelpBusiness;


public class YelpActivity extends AppCompatActivity {

    //Create Text Views and ImageViews
    ImageView yelpPic, rating, linkToYelpSite;
    TextView locationName, reviewCount, isClosed, locationPhone, website,
            distance, address;
    String yelpPicURL;

    private YelpAPIClient yelpAPIClient;
    private Location location;
    private LocationManager lm;
    private YelpBusiness yelpBusiness;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yelp_display);

        //MARK: - Deserializing packaged YelpBusiness object

        String jsonYelpBusiness;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            jsonYelpBusiness = extras.getString("YelpBusiness");
            yelpBusiness = new Gson().fromJson(jsonYelpBusiness, YelpBusiness.class);
        } else {
            Log.e("Error Yelp Business", "Did not receive yelpBusiness");
            yelpBusiness = new YelpBusiness();
        }

        //MARK: - Binding variables to their respective layout elements

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

        setupYelpBusinessInView();

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
        }
    }

    //MARK: - Setting text in view from business

    private void setupYelpBusinessInView() {
        locationName.setText(yelpBusiness.name);
        reviewCount.setText("" + yelpBusiness.reviewCount);
        if (yelpBusiness.isClosed) {
            isClosed.setText("CLOSED");
        } else {
            isClosed.setText("OPEN");
        }
        locationPhone.setText(yelpBusiness.displayPhone);

        //Distance is given in Meters, so here it is converted to miles.
        yelpBusiness.distance *= 0.000621371;

        distance.setText(String.format("%.2f", yelpBusiness.distance) + " miles away");
        address.setText(""+ yelpBusiness.location.displayAddress);

        //MARK: - Loading Yelp Photos and Default Photos

        Picasso.with(this)
                .load(yelpBusiness.imageURL)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.rating_star_0)
                .into(yelpPic);

        Picasso.with(this)
                .load(R.mipmap.ic_launcher)
                .into(linkToYelpSite);
    }

}
