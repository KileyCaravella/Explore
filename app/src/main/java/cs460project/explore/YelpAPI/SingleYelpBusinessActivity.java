package cs460project.explore.YelpAPI;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import cs460project.explore.R;

/**
 * This is the Single Yelp Business Activity. Here, a business's full information from the Yelp API is
 * displayed. A user can choose to forget the business, add it to a category, or hit the back button to
 * get a new result (if coming from randomize button on Navigation Activity).
 */

public class SingleYelpBusinessActivity extends Activity implements View.OnClickListener {

    private YelpBusiness yelpBusiness;
    private ImageView yelpImageView, yelpBusinessRatingImageView, yelpBurstImageView, googleMapsImageView;
    private TextView yelpBusinessNameTextView, openClosedTextView, yelpNumberOfReviewsTextView, yelpDistanceTextView;
    private Button yelpPhoneButton, addBusinessButton, forgetBusinessButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yelp_single_display);

        getYelpBusinessFromBundle();
        setupActivityVariables();
        setBusinessInformation();
    }

    //MARK: - Setup

    private void getYelpBusinessFromBundle() {
        String jsonYelpBusiness;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            jsonYelpBusiness = extras.getString("YelpBusiness");
            yelpBusiness = new Gson().fromJson(jsonYelpBusiness, YelpBusiness.class);
        } else {
            Log.e("Error Yelp Business", "Did not receive yelpBusiness");
            yelpBusiness = new YelpBusiness();
        }
    }

    private void setupActivityVariables() {
        yelpImageView = (ImageView) findViewById(R.id.yelpImageView);
        yelpBusinessRatingImageView = (ImageView) findViewById(R.id.yelpBusinessRatingImageView);
        yelpBurstImageView = (ImageView) findViewById(R.id.yelp_burst);
        googleMapsImageView = (ImageView) findViewById(R.id.google_maps_image);
        yelpBusinessNameTextView = (TextView) findViewById(R.id.yelpBusinessNameTextView);
        openClosedTextView = (TextView) findViewById(R.id.openClosedTextView);
        yelpPhoneButton = (Button) findViewById(R.id.yelpPhoneButton);
        yelpNumberOfReviewsTextView = (TextView) findViewById(R.id.yelpNumberOfReviewsTextView);
        yelpDistanceTextView = (TextView) findViewById(R.id.yelpDistanceTextView);
        addBusinessButton = (Button) findViewById(R.id.addButton);
        forgetBusinessButton = (Button) findViewById(R.id.forgetButton);
    }

    private void setBusinessInformation() {
        yelpBusinessNameTextView.setText(yelpBusiness.name);

        //Making phone button look like a hyperlink (Underlined and Blue)
        yelpPhoneButton.setText(yelpBusiness.displayPhone);
        if (yelpBusiness.isClosed)
            openClosedTextView.setText("OPEN"); //default is "CLOSED"

        yelpPhoneButton.setPaintFlags(yelpPhoneButton.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        yelpPhoneButton.setTextColor(Color.parseColor("#0000FF"));
        yelpNumberOfReviewsTextView.setText(yelpBusiness.reviewCount + " Reviews");

        //Distance is given in Meters, so here it is converted to miles.
        yelpBusiness.distance *= 0.000621371;
        yelpDistanceTextView.setText(String.format("%.2f", yelpBusiness.distance) + " miles");

        //Loading yelp photos and default photos using Picasso plugin
        Picasso.with(this)
                .load(yelpBusiness.imageURL)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(yelpImageView);

        yelpPhoneButton.setOnClickListener(this);
        yelpBurstImageView.setOnClickListener(this);
        googleMapsImageView.setOnClickListener(this);
        addBusinessButton.setOnClickListener(this);
        forgetBusinessButton.setOnClickListener(this);

        setYelpRatingImage();
    }

    //MARK: - OnClickListener

    public void onClick(View v) throws SecurityException {
        switch (v.getId()) {
            //Opens dialer with business's number plugged in
            case R.id.yelpPhoneButton:
                yelpPhoneButtonPressed();
                break;

            //Opens business's page on Yelp
            case R.id.yelp_burst:
                yelpBurstPressed();
                break;
            case R.id.google_maps_image:
                googleMapsImagePressed();
                break;
            case R.id.addButton:
                addBusinessButtonPressed();
                break;
            case R.id.forgetButton:
                forgetBusinessButtonPressed();
        }
    }

    //MARK: - OnClick Functions

    private void yelpPhoneButtonPressed() {
        Uri uri = Uri.parse("tel:" + yelpBusiness.displayPhone);
        Intent phoneIntent = new Intent(Intent.ACTION_DIAL, uri);
        startActivity(phoneIntent);
    }

    private void yelpBurstPressed() {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(yelpBusiness.url)));
    }

    //Shows business's location in google maps
    private void googleMapsImagePressed() {
        String addressForGMaps = "geo:0,0?q=";
        for (String address : yelpBusiness.location.displayAddress) {
            addressForGMaps += address;
        }
        addressForGMaps = addressForGMaps.replace(" ", "+");
        addressForGMaps = addressForGMaps.replace(",", "");
        Intent gmapsIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(addressForGMaps));
        startActivity(gmapsIntent);
    }

    private void forgetBusinessButtonPressed() {
        Log.i("pressed", "pressed forget");

        AlertDialog dialog = new AlertDialog.Builder(SingleYelpBusinessActivity.this).create();

        dialog.setTitle("Are you sure?");
        dialog.setMessage("This business won't appear again.");
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Confirm", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Code for what to do when Confirm button pressed
            }
        });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });

        dialog.show();
    }

    private void addBusinessButtonPressed() {
        Log.i("pressed", "pressed add");

        AlertDialog dialog = new AlertDialog.Builder(SingleYelpBusinessActivity.this).create();

        dialog.setTitle("Choose a category.");

        // Code to set up buttons with category names

        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Confirm", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Code for what to do when Confirm button pressed
            }
        });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });

        dialog.show();
    }

    //MARK: - Setting up Yelp Rating Image Based on Rating Received from Yelp (0.0-5.0)

    private void setYelpRatingImage() {

        //Yelp returns a decimal rating so it needs to be multiplied by 10 for switch statement
        double tempNumStar = yelpBusiness.rating;
        tempNumStar = tempNumStar * 10;
        int NumStar = (int) tempNumStar;

        switch (NumStar) {
            case 0:
                Picasso.with(this)
                        .load(R.mipmap.stars_small_0)
                        .into(yelpBusinessRatingImageView);
                break;
            case 10:
                Picasso.with(this)
                        .load(R.mipmap.stars_small_1)
                        .into(yelpBusinessRatingImageView);
                break;
            case 15:
                Picasso.with(this)
                        .load(R.mipmap.stars_small_1_half)
                        .into(yelpBusinessRatingImageView);
                break;
            case 20:
                Picasso.with(this)
                        .load(R.mipmap.stars_small_2)
                        .into(yelpBusinessRatingImageView);
                break;
            case 25:
                Picasso.with(this)
                        .load(R.mipmap.stars_small_2_half)
                        .into(yelpBusinessRatingImageView);
                break;
            case 30:
                Picasso.with(this)
                        .load(R.mipmap.stars_small_3)
                        .into(yelpBusinessRatingImageView);
                break;
            case 35:
                Picasso.with(this)
                        .load(R.mipmap.stars_small_3_half)
                        .into(yelpBusinessRatingImageView);
                break;
            case 40:
                Picasso.with(this)
                        .load(R.mipmap.stars_small_4)
                        .into(yelpBusinessRatingImageView);
                break;
            case 45:
                Picasso.with(this)
                        .load(R.mipmap.stars_small_4_half)
                        .into(yelpBusinessRatingImageView);
                break;
            case 50:
                Picasso.with(this)
                        .load(R.mipmap.stars_small_5)
                        .into(yelpBusinessRatingImageView);
                break;
        }
    }
}
