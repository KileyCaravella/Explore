package cs460project.explore;

import android.content.Intent;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View.OnClickListener;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import cs460project.explore.YelpAPI.YelpAPIClient;
import cs460project.explore.YelpAPI.YelpBusiness;


public class YelpActivity extends AppCompatActivity implements OnClickListener {

    //Create Text Views and ImageViews
    ImageView yelpPic, rating, linkToYelpSite;
    TextView locationName, reviewCount, isClosed, locationPhone, website,
            distance, address;
    Button websiteBtn, phoneBtn, directions;


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
        locationName = (TextView) findViewById(R.id.name);
        reviewCount = (TextView) findViewById(R.id.reviewCount);
        isClosed = (TextView) findViewById(R.id.isClosed);
        website = (TextView) findViewById(R.id.url);
        distance = (TextView) findViewById(R.id.distance);
        address = (TextView) findViewById(R.id.location);

        //NH: Button to bring the user to the restaurants website
        Button websiteBtn = (Button) findViewById(R.id.url);
        websiteBtn.setOnClickListener(this);

        //NH: Button to bring up dialer with the locations phone number
        Button locationPhoneBtn = (Button) findViewById(R.id.dispPhone);
        locationPhoneBtn.setOnClickListener(this);
        locationPhoneBtn.setText(yelpBusiness.displayPhone);

        //NH: Button to send the locations address to google maps
        Button directionBtn = (Button) findViewById(R.id.directions);
        directionBtn.setOnClickListener(this);

        //NH Imageview for the yelp logo, has onClickListener to handle being clicked
        ImageView linkToYelpSite = (ImageView) findViewById(R.id.linkYelpSite);
        linkToYelpSite.setOnClickListener(this);


        setupYelpBusinessInView();


        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //NH:
        //Yelp returns the url to simply grab the rating pic, use that or stick with mipmap file
        //grabbing from yelp would increase data usagage and possible slow down the app

        //Yelp returns 1, 1.5 ...4.5, 5 etc so it needs to be multiplied by 10 since the switch statement uses int
        //there is no 0 star, Yelp starts at 1, hence the switch statement starting at 10
        double tempNumStar = yelpBusiness.rating;
        tempNumStar = tempNumStar * 10;
        int NumStar = (int) tempNumStar;

        switch (NumStar) {
            case 0:
                Picasso.with(this)
                        .load(R.mipmap.rating_star_0)
                        .into(rating);
                break;
            case 10:
                Picasso.with(this)
                        .load(R.mipmap.rating_star_1)
                        .into(rating);
                break;
            case 15:
                Picasso.with(this)
                        .load(R.mipmap.rating_star_1_5)
                        .into(rating);
                break;
            case 20:
                Picasso.with(this)
                        .load(R.mipmap.rating_star_2)
                        .into(rating);
                break;
            case 25:
                Picasso.with(this)
                        .load(R.mipmap.rating_star_2_5)
                        .into(rating);
                break;
            case 30:
                Picasso.with(this)
                        .load(R.mipmap.rating_star_3)
                        .into(rating);
                break;
            case 35:
                Picasso.with(this)
                        .load(R.mipmap.rating_star_3_5)
                        .into(rating);
                break;
            case 40:
                Picasso.with(this)
                        .load(R.mipmap.rating_star_4)
                        .into(rating);
                break;
            case 45:
                Picasso.with(this)
                        .load(R.mipmap.rating_star_4_5)
                        .into(rating);
                break;
            case 50:
                Picasso.with(this)
                        .load(R.mipmap.rating_star_5)
                        .into(rating);
                break;
        }//switch

        //String returnedWeb = yelpBusiness.url;
        //Resources res = getResources();
        //String web = String.format(Resources.getString(R.string.location_website),returnedWeb);
        // String web = this.getString(R.string.location_website, yelpBusiness.url);
        //website.setText(Resources.getString(R.string.location_website,yelpBusiness.url));
        //website.setText(web);
    }//on create


    //MARK: - Setting text in view from business

    private void setupYelpBusinessInView() {
        locationName.setText(yelpBusiness.name);
        reviewCount.setText("Based on " + yelpBusiness.reviewCount + " Reviews");
        if (yelpBusiness.isClosed) {
            isClosed.setText("CLOSED");
        } else {
            isClosed.setText("OPEN");
        }

        //Distance is given in Meters, so here it is converted to miles.
        yelpBusiness.distance *= 0.000621371;

        distance.setText(String.format("%.2f", yelpBusiness.distance) + " miles away");
        address.setText("" + yelpBusiness.location.displayAddress);

        //MARK: - Loading Yelp Photos and Default Photos
        Picasso.with(this)
                .load(yelpBusiness.imageURL)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.rating_star_0)
                .into(yelpPic);

       // Log.i("Business Name", yelpBusiness.name);
       // Log.i("Business Address", ""+yelpBusiness.location.displayAddress);
       // Log.i("Pic URL", yelpBusiness.imageURL);


    }//setup Yelp Business

    //NH: Handles the clicking of the image views and the buttons
    public void onClick(View v) throws SecurityException {
        switch (v.getId()) {

            //opens the yelp homepage
            case R.id.linkYelpSite:
                Uri uri = Uri.parse("https://www.yelp.com/sf");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                //startActivity(intent);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
                break;

            //opens a web page with the locations yelp website
            case R.id.url:
                Uri uri2 = Uri.parse(yelpBusiness.url);
                Intent intent2 = new Intent(Intent.ACTION_VIEW, uri2);

                if (intent2.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent2);
                }
                break;

            //sends the locations phone number to the dialer
            case R.id.dispPhone:

                String tempPhone;
                tempPhone = yelpBusiness.phone;
                Uri uri3 = Uri.parse("tel:" + tempPhone);
                Intent intent3 = new Intent(Intent.ACTION_DIAL, uri3);
                startActivity(intent3);
                break;
            //sends the locations address to google maps for directions
            case R.id.directions:
                //HashMap coordinates = yelpBusiness.coordinates;
                System.out.println("Coordinates " + yelpBusiness.coordinates);
                //System.out.println("Coordinates lat "+yelpBusiness.coordinates.latitude);

                Uri uri4 = Uri.parse("geo:yelpBusiness.location");
                //Uri uri4 = Uri.parse("geo:0,0?q=175+forest+street+waltham+ma");

                // Uri uri4 = Uri.parse("geo"+yelpBusiness.coordinates);
                Intent intent4 = new Intent(Intent.ACTION_VIEW, uri4);
                startActivity(intent4);
                break;


        }//swtich
    }
}


