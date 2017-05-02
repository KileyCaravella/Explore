package cs460project.explore;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import cs460project.explore.Category.CustomCategoryAdapter;
import cs460project.explore.Category.CustomListView;
import cs460project.explore.YelpAPI.SingleYelpBusinessActivity;
import cs460project.explore.YelpAPI.YelpAPIClient;
import cs460project.explore.YelpAPI.YelpBusiness;

/**
 * Created by HARDY_NATH on 4/5/2017.
 * Will be used to display results that the user searched for
 */

public class SearchActivity extends Activity {

    private EditText searchEditText;
    private ListView searchList;
    private ImageView loadingIndicatorImageView;
    private AnimationDrawable frameAnimation;
    private View loadingIndicatorBackgroundView;
    private List<CustomListView> locationList;
    private YelpBusiness[] yelpBusinesses;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.area_search);

        setupLoadingIndicator();
        setupVariables();

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

    private void setupVariables() {
        searchEditText = (EditText) findViewById(R.id.searchEditText);
    }

    private void setupListView() {
        locationList = new ArrayList<>();

        //Looping through businesses retrieved
        for(YelpBusiness business : yelpBusinesses) {
            locationList.add(new CustomListView(business));
        }

        searchList = (ListView) findViewById(R.id.searchListView);
        CustomCategoryAdapter adapter = new CustomCategoryAdapter(this, R.layout.custom_listview_category, locationList);
        searchList.setAdapter(adapter);

        searchList.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                businessSelectedAtPosition(position);
            }
        });
    }

    //MARK: - OnClick Functions

    public void searchLocationButtonPressed(View v) {
        String searchText = searchEditText.getText().toString();
        if (searchText.isEmpty()) {
            didNotEnterLocation();
            return;
        }

        //Commas are removed and spaces are turned into hyphens to fit the call's format.
        searchText = searchText.replace(",", "");
        searchText = searchText.replace(" ", "-");

        dismissKeyboard();

        //starting the animation for the loading indicator
        toggleLoadingIndicator(true);

        //Client call -- we want to clear the business array in the client in case the user randomizes.
        YelpAPIClient.sharedInstance.getYelpBusinessWithLocationName(searchText, new YelpAPIClient.OnYelpBusinessCompletionListener() {
            @Override
            public void onBusinessRetrievalSuccessful(YelpBusiness business) {
                toggleLoadingIndicator(false);
                yelpBusinesses = YelpAPIClient.sharedInstance.getYelpBusinesses();
                YelpAPIClient.sharedInstance.clearBusinessArray();
                setupListView();
            }

            @Override
            public void onBusinessRetrievalFailed(String reason) {
                toggleLoadingIndicator(false);
                failedToRetrieveBusinesses();
            }
        });
    }

    private void businessSelectedAtPosition(int position) {
        Intent intent = new Intent(SearchActivity.this, SingleYelpBusinessActivity.class);
        YelpBusiness yb = yelpBusinesses[position];
        intent.putExtra("YelpBusiness", new Gson().toJson(yb));
        startActivity(intent);
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

    private void dismissKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchEditText.getWindowToken(), 0);
    }

    //MARK: - Toasts

    private void didNotEnterLocation() {
        Toast.makeText(this, "Please enter a location", Toast.LENGTH_LONG).show();
    }

    private void failedToRetrieveBusinesses() {
        Toast.makeText(this, "Failed to find any businesses in area requested.", Toast.LENGTH_LONG).show();
    }
}