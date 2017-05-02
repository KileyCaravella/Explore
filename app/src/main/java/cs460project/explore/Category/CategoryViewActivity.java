package cs460project.explore.Category;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cs460project.explore.R;
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

public class CategoryViewActivity extends Activity {

    ListView listAgain;
    TextView cat;
    String category;
    String userID;
    List<CustomListView> locationList;
    private ArrayList<String> businessIDArray;
    private CategoryClient.CompletionListenerWithArray completionListenerWithArray;
    private int length;
    private YelpBusiness yelpBusiness;
    //private ArrayAdapter<String> businessAdapter;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_category);


        //get the category name that was opened
        Intent i = getIntent();
        category = i.getStringExtra("Category");

        //get the current user
        //UserClient.sharedInstance.confirmUser();

        //bind layout textview and set text
        cat = (TextView)findViewById(R.id.viewDescriptionViewCat);
        cat.setText(category);


        locationList = new ArrayList<>();

        //binds the variable to the view
        listAgain = (ListView) findViewById(R.id.viewCatListView);

        //instanciates the custom adapter
        CustomCategoryAdapter adapter = new CustomCategoryAdapter(this, R.layout.custom_listview_category, locationList);

        //sets the adapter to the list
        listAgain.setAdapter(adapter);


        //creates a list of custom list views and populates it with test data
        //List<CustomListView> locationList = new ArrayList<CustomListView>();
        /*
        locationList.add(new CustomListView("Sunny Bowl", "1477 Plymouth St, Ste D, Mountain View, CA 94043", "https://s3-media2.fl.yelpcdn.com/bphoto/xlcqWTyQMqEUZeb6gQMZAQ/o.jpg"));
        locationList.add(new CustomListView("Bobs Burgers", "172 Forest st", "http://www.bentley.edu/files/brand/Bentley_Wallpaper_HP_1600x900_4a.jpg"));
        locationList.add(new CustomListView("In-N-Out Burger", "1159 N Rengstorff Ave, Mountain View, CA 94043", "https://s3-media3.fl.yelpcdn.com/bphoto/kZ05IXX2BCw4nSxHpEyHuQ/o.jpg"));
        locationList.add(new CustomListView("Plaza Deli Cafe", "2680 Bayshore Pkwy, Mountain View, CA 94043", "https://s3-media2.fl.yelpcdn.com/bphoto/NMAh_m1vf7oH2WvSP3qoHg/o.jpg"));
        locationList.add(new CustomListView("Los Altos Taqueria", "2105 Old Middlefield Way, Ste E, Mountain View, CA 94043", "https://s3-media4.fl.yelpcdn.com/bphoto/2k4UGNL9G-s-QiyD38QpIA/o.jpg"));
        */
        //locationList.add(new CustomListView(yelpBusiness));


        findBusinessIDS(category);
        fillYelpBussinessArray();



    }//on create

    private void findBusinessIDS(String category){
        businessIDArray = (CategoryClient.sharedInstance.getBusinessesFromCategory(category, new CategoryClient.CompletionListenerWithArray() {
            @Override
            public void onSuccessful(ArrayList<String> arrayList) {

                //array of business ids here
            }

            @Override
            public void onFailed(String reason) {

            }
        }));
    }//find Business ID's

    private void fillYelpBussinessArray(){
        length = businessIDArray.size();

        //take location in array, find business id, call yelp api to get yelp business class
        for(int a = 0; a < length; a++){
            String value = businessIDArray.toString();
            yelpBusiness = YelpAPIClient.sharedInstance.getYelpBusinessWithBusinessID(value);
            locationList.add(new CustomListView(yelpBusiness));
        }
    }//fill yelp business array

}
