package cs460project.explore.Category;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import cs460project.explore.R;
import cs460project.explore.YelpAPI.SingleYelpBusinessActivity;
import cs460project.explore.YelpAPI.YelpAPIClient;
import cs460project.explore.YelpAPI.YelpBusiness;

/**
 * This lists the businesses that are in the current category. If there are no businesses, this view does
 * not appear. If you click on a business, it brings you to the full information view of the business.
 * Because they are pulled from Yelp using the business's id and not a location, all of their locations are
 * set to 0.0 miles.
 */

public class BusinessesInCategoryActivity extends Activity {

    //MARK: - Private Variables

    private TextView categoryNameTextView;
    private ListView locationListView;
    private List<CustomListView> locationList;
    private YelpBusiness[] yelpBusinesses;

    //MARK: - Initialization

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_businesses_in_category);

        //Clearing away any businesses that might have been set in the main array
        YelpAPIClient.sharedInstance.clearBusinessArray();

        setVariables();
        getYelpBusinessFromBundle();
        setupListView();
    }

    //Mark: - Setup

    private void getYelpBusinessFromBundle() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String jsonCategoryNameString = extras.getString("CategoryName");
            String jsonBusinessString = extras.getString("YelpBusinesses");

            categoryNameTextView.setText(jsonCategoryNameString);
            yelpBusinesses = new Gson().fromJson(jsonBusinessString, YelpBusiness[].class);

        } else {
            Log.e("Error Yelp Business", "Did not receive yelpBusiness");
        }
    }

    private void setVariables() {
        categoryNameTextView = (TextView) findViewById(R.id.categoryNameTextView);
        locationListView = (ListView) findViewById(R.id.businessListView);
    }

    private void setupListView() {
        locationList = new ArrayList<>();

        //Looping through businesses retrieved
        if (yelpBusinesses != null) {
            for (YelpBusiness business : yelpBusinesses) {
                locationList.add(new CustomListView(business));
            }
        }

        CustomCategoryAdapter adapter = new CustomCategoryAdapter(this, R.layout.custom_business_cells, locationList);
        locationListView.setAdapter(adapter);

        locationListView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                businessSelectedAtPosition(position);
            }
        });
    }

    //MARK: - Cell selected

    private void businessSelectedAtPosition(int position) {
        Intent intent = new Intent(BusinessesInCategoryActivity.this, SingleYelpBusinessActivity.class);
        YelpBusiness yb = yelpBusinesses[position];
        intent.putExtra("YelpBusiness", new Gson().toJson(yb));
        startActivity(intent);
    }
}
