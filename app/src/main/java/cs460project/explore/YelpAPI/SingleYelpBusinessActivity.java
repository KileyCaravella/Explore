package cs460project.explore.YelpAPI;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import cs460project.explore.Category.CategoryClient;
import cs460project.explore.R;

/**
 * This is the Single Yelp Business Activity. Here, a business's full information from the Yelp API is
 * displayed. A user can choose to forget the business, add it to a category, or hit the back button to
 * get a new result (if coming from randomize button on Navigation Activity).
 */

public class SingleYelpBusinessActivity extends Activity implements View.OnClickListener {

    //MARK: - Private Variables

    private static final String NEW_CATEGORY_STRING = "<create new category>";

    private String selectedCategory = "";
    private YelpBusiness yelpBusiness;
    private ImageView yelpImageView, yelpBusinessRatingImageView, yelpBurstImageView, googleMapsImageView;
    private TextView yelpBusinessNameTextView, openClosedTextView, yelpNumberOfReviewsTextView, yelpDistanceTextView;
    private Button yelpPhoneButton, addBusinessButton, forgetBusinessButton;
    private ImageView loadingIndicatorImageView;
    private AnimationDrawable frameAnimation;
    private View loadingIndicatorBackgroundView;

    //MARK: - Initialization

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yelp_single_display);

        getYelpBusinessFromBundle();
        setupActivityVariables();
        setBusinessInformation();
        setupLoadingIndicator();
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

    //MARK: - OnClickListener

    public void onClick(View v) throws SecurityException {
        switch (v.getId()) {
            //Opens dialer with business's number plugged in
            case R.id.yelpPhoneButton:
                startDialerActivity();
                break;

            //Opens business's page on Yelp
            case R.id.yelp_burst:
                startWebActivity();
                break;

            //Opens google maps with location added
            case R.id.google_maps_image:
                startGoogleMapsActivity();
                break;

            //Causes an AlertDialog to show to add business to a category
            case R.id.addButton:
                addBusinessDialog();
                break;

            //Causes an AlertDialog to show to add business to rejected list
            case R.id.forgetButton:
                forgetBusinessDialog();
        }
    }

    //MARK: - Activity Functions

    private void startDialerActivity() {
        Uri uri = Uri.parse("tel:" + yelpBusiness.displayPhone);
        Intent phoneIntent = new Intent(Intent.ACTION_DIAL, uri);
        startActivity(phoneIntent);
    }

    private void startWebActivity() {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(yelpBusiness.url)));
    }

    //Shows business's location in google maps
    private void startGoogleMapsActivity() {
        String addressForGMaps = "geo:0,0?q=";
        for (String address : yelpBusiness.location.displayAddress) {
            addressForGMaps += address;
        }
        addressForGMaps = addressForGMaps.replace(" ", "+");
        addressForGMaps = addressForGMaps.replace(",", "");
        Intent gmapsIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(addressForGMaps));
        startActivity(gmapsIntent);
    }

    //MARK: - Alert Dialog Prompts

    private void forgetBusinessDialog() {
        AlertDialog alert = setupForgetAlertDialog();
        alert.show();
    }

    private void addBusinessDialog() {
        AlertDialog alert = setupAddAlertDialog();
        alert.show();
    }

    private void insertNewCategoryDialog() {
        AlertDialog alert = setupNewCategoryAlertDialog();
        alert.show();
    }

    //MARK: - Alert Dialog Setup

    private AlertDialog setupForgetAlertDialog() {
        AlertDialog dialog = new AlertDialog.Builder(SingleYelpBusinessActivity.this).create();

        dialog.setTitle("Are you sure?");
        dialog.setMessage("This business won't appear again.");

        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Confirm", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                addBusinessToRejected();
            }
        });

        //Nothing happens when the user presses cancel
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {}
        });

        return dialog;
    }

    private AlertDialog setupAddAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //Creating Array
        ArrayList<String> categories = new ArrayList<String>();
        categories.add(NEW_CATEGORY_STRING);
        categories.addAll(CategoryClient.sharedInstance.categoriesList);

        //Setting alert dialog view
        LayoutInflater factory = LayoutInflater.from(getApplicationContext());
        final View dialogView = factory.inflate(R.layout.add_business_dialog, null);
        builder.setView(dialogView);

        //Spinner
        final Spinner spinner = (Spinner) dialogView.findViewById(R.id.spinner);
        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, categories);
        spinner.setAdapter(spinnerArrayAdapter);

        for(int i=0;i<spinner.getCount();i++) {
            spinner.setSelection(i, true);
            break;
        }

        //Setting listener for spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //Set selected category string based on selected item in spinner
                String category = spinner.getItemAtPosition(position).toString();

                if (!category.equals(NEW_CATEGORY_STRING)) {
                    selectedCategory = spinner.getItemAtPosition(position).toString();
                    Log.i("category selected", selectedCategory);
                } else {
                    selectedCategory = "";
                }
            }

            //Impossible for nothing to be selected with current setup
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        //Setting buttons on builder
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface d, int i) {
                if (selectedCategory.isEmpty()) {
                    insertNewCategoryDialog();
                } else {
                    addBusinessToCategory(selectedCategory);
                }
            }
        });

        //Nothing happens if Cancel button is pressed.
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface d, int i) {}
        });

        return builder.create();
    }

    private AlertDialog setupNewCategoryAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //Setting view
        LayoutInflater factory = LayoutInflater.from(getApplicationContext());
        final View dialogView = factory.inflate(R.layout.add_new_category_dialog, null);
        builder.setView(dialogView);

        //EditText

        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface d, int i) {
                EditText newCategoryEditText = (EditText) dialogView.findViewById(R.id.new_category_edit_text);
                String newCategoryName = newCategoryEditText.getText().toString();

                if (!newCategoryName.isEmpty()) {
                    createNewCategoryAndAddBusinessToIt(newCategoryName);
                } else {
                    newCategoryEditTextIsEmpty();
                }
            }
        });

        //Nothing happens if Cancel button is pressed.
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface d, int i) {}
        });

        return builder.create();
    }

    //MARK: - Client Calls

    private void addBusinessToRejected() {
        //starting the animation for the loading indicator
        toggleLoadingIndicator(true);

        //Client call
        CategoryClient.sharedInstance.addBusinessToRejected(yelpBusiness.id, new CategoryClient.CompletionListenerWithArray() {
            @Override
            public void onSuccessful(ArrayList<String> arrayList) {

                //Reset list
                CategoryClient.sharedInstance.rejectedList = arrayList;
                toggleLoadingIndicator(false);
                addBusinessToRejectedSucceeded();
            }

            @Override
            public void onFailed(String reason) {
                Log.i("Forget Business Failed", reason);
                toggleLoadingIndicator(false);
                addBusinessToRejectedFailed();
            }
        });
    }

    private void addBusinessToCategory(String categoryName) {
        if (categoryName.isEmpty()) {
            return;
        }
        //starting the animation for the loading indicator
        toggleLoadingIndicator(true);

        //Client call
        CategoryClient.sharedInstance.addBusinessToCategory(yelpBusiness.id, categoryName, new CategoryClient.CompletionListenerWithArray() {
            @Override
            public void onSuccessful(ArrayList<String> arrayList) {
                toggleLoadingIndicator(false);
                addBusinessToCategorySucceeded();
            }

            @Override
            public void onFailed(String reason) {
                Log.i("Failure saving Business", reason);
                toggleLoadingIndicator(false);
                addBusinessToCategoryFailed();
            }
        });
    }

    private void createNewCategoryAndAddBusinessToIt(final String categoryName) {
        //starting the animation for the loading indicator
        toggleLoadingIndicator(true);

        //Client call
        CategoryClient.sharedInstance.createNewCategory(categoryName, new CategoryClient.CompletionListenerWithArray() {
            @Override
            public void onSuccessful(ArrayList<String> arrayList) {

                //Reset list
                CategoryClient.sharedInstance.categoriesList = arrayList;
                addBusinessToCategory(categoryName);
            }

            @Override
            public void onFailed(String reason) {
                toggleLoadingIndicator(false);
                addBusinessToCategoryFailed();
            }
        });
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

    private void addBusinessToRejectedFailed() {
        Toast.makeText(this, "Failed to forget business. Please try again.", Toast.LENGTH_LONG).show();
    }

    private void addBusinessToRejectedSucceeded() {
        Toast.makeText(this, "Successfully forgot business.", Toast.LENGTH_LONG).show();
    }

    private void addBusinessToCategoryFailed() {
        Toast.makeText(this, "Failed to save business. Please try again.", Toast.LENGTH_LONG).show();
    }

    private void addBusinessToCategorySucceeded() {
        Toast.makeText(this, "Successfully saved business.", Toast.LENGTH_LONG).show();
    }

    private void newCategoryEditTextIsEmpty() {
        Toast.makeText(this, "Please enter a name for the new category.", Toast.LENGTH_LONG).show();
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
