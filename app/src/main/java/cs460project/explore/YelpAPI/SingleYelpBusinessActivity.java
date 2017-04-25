package cs460project.explore.YelpAPI;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import cs460project.explore.R;

/**
 * Created by Kiwi on 3/31/17.
 */

public class SingleYelpBusinessActivity extends AppCompatActivity implements View.OnClickListener {

    private YelpBusiness yelpBusiness;
    private ImageView yelpImageView, yelpBusinessRatingImageView, yelpBurstImageView, googleMapsImageView;
    private TextView yelpBusinessNameTextView, openClosedTextView, yelpNumberOfReviewsTextView, yelpDistanceTextView;
    private Button yelpPhoneButton;
    private GestureDetector gd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_yelp_single_display);

        gd = new GestureDetector(new SwipeGestureDetector());

        getYelpBusinessFromBundle();
        setupActivityVariables();
        setBusinessInformation();
    }

    //MARK: - Get Business from Bungle Provided or Set Up New Business

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

    //MARK: - Setting up Activity Variables to Class Variables

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
    }

    //MARK: - Setting Class Values to Corresponding Business Information

    private void setBusinessInformation() {
        yelpBusinessNameTextView.setText(yelpBusiness.name);

        //Making phone button look like a hyperlink (Underlined and Blue)
        yelpPhoneButton.setText(yelpBusiness.displayPhone);
        if(yelpBusiness.isClosed)
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

        setYelpRatingImage();
    }

    //MARK: - OnClickListener Switch Statement

    public void onClick(View v) throws SecurityException {
        switch (v.getId()) {
            //Opens dialer with business's number plugged in
            case R.id.yelpPhoneButton:
                Uri uri = Uri.parse("tel:" + yelpBusiness.displayPhone);
                Intent phoneIntent = new Intent(Intent.ACTION_DIAL, uri);
                startActivity(phoneIntent);
                break;

            //Opens business's page on Yelp
            case R.id.yelp_burst:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(yelpBusiness.url)));
                break;

            //Shows business's location on google maps if they would like directions
            case R.id.google_maps_image:
                String addressForGMaps = "geo:0,0?q=";
                for (String address:yelpBusiness.location.displayAddress) {
                    addressForGMaps += address;
                }
                addressForGMaps = addressForGMaps.replace(" ", "+");
                addressForGMaps = addressForGMaps.replace(",", "");
                Intent gmapsIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(addressForGMaps));
                startActivity(gmapsIntent);
                break;
        }
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

    // MARK: - Setting up gesture detector for left- and right-swipes

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (gd.onTouchEvent(event)) {
            return true;
        }
        return super.onTouchEvent(event);
    }

    private void onLeftSwipe() {
        AlertDialog dialog = new AlertDialog.Builder(SingleYelpBusinessActivity.this).create();

        dialog.setTitle("Are you sure?");
        dialog.setMessage("This business won't appear again.");
        dialog.setButton(DialogInterface.BUTTON_POSITIVE,"Confirm", new DialogInterface.OnClickListener() {
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

    private void onRightSwipe() {
        // Do something
    }

    private class SwipeGestureDetector extends GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_MIN_DISTANCE = 120;
        private static final int SWIPE_MAX_OFF_PATH = 200;
        private static final int SWIPE_THRESHOLD_VELOCITY = 200;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                float diffAbs = Math.abs(e1.getY() - e2.getY());
                float diff = e1.getX() - e2.getX();

                if (diffAbs > SWIPE_MAX_OFF_PATH) {
                    return false;
                }

                // Left swipe
                if (diff > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    SingleYelpBusinessActivity.this.onLeftSwipe();

                    // Right swipe
                } else if (-diff > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    SingleYelpBusinessActivity.this.onRightSwipe();
                }
            } catch (Exception e) {
                Log.e("Swipe", "Error on gestures");
            }
            return false;
        }
    }
}
