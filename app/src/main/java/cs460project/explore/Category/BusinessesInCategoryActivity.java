package cs460project.explore.Category;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cs460project.explore.R;
import cs460project.explore.YelpAPI.SingleYelpBusinessActivity;
import cs460project.explore.YelpAPI.YelpAPIClient;
import cs460project.explore.YelpAPI.YelpBusiness;

/**
 * Created by HARDY_NATH on 3/29/2017.
 * handles filling the category view with the custom list view.
 * Code works by using the custom adapter to fill the list view on the layout with the custom list view
 * 1) get category
 * 2)get list of business id's
 * 3)query yelp for YelpBusiness using business id's
 * 4)add to list of yelpBusinesses called locationList. The other classes should be all set to use a yelp Business
 */

public class BusinessesInCategoryActivity extends Activity {

    private ListView locationListView;
    private List<CustomListView> locationList;
    private YelpBusiness[] yelpBusinesses;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_businesses_in_category);

        getYelpBusinessFromBundle();
        setVariables();
        setupListView();
    }

    private void getYelpBusinessFromBundle() {
        //Clearing away any businesses that might have been set in the main array
        YelpAPIClient.sharedInstance.clearBusinessArray();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String jsonString = extras.getString("YelpBusinesses");
            yelpBusinesses = new Gson().fromJson(jsonString, YelpBusiness[].class);

        } else {
            Log.e("Error Yelp Business", "Did not receive yelpBusiness");
        }
    }

    private void setVariables() {
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

    private void businessSelectedAtPosition(int position) {
        Intent intent = new Intent(BusinessesInCategoryActivity.this, SingleYelpBusinessActivity.class);
        YelpBusiness yb = yelpBusinesses[position];
        intent.putExtra("YelpBusiness", new Gson().toJson(yb));
        startActivity(intent);
    }
}
