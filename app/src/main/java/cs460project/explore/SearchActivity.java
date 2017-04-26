package cs460project.explore;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import cs460project.explore.Category.CustomCategoryAdapter;
import cs460project.explore.Category.CustomListView;

/**
 * Created by HARDY_NATH on 4/5/2017.
 * Will be used to display results that the user searched for
 */

public class SearchActivity extends Activity {

    private EditText searchText;
    private Button search;
    private ListView searchList;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.area_search);


        //creates a list of custom list views and populates it with test data
        List<CustomListView> locationList = new ArrayList<CustomListView>();
        locationList.add(new CustomListView("Sunny Bowl", "1477 Plymouth St, Ste D, Mountain View, CA 94043", "https://s3-media2.fl.yelpcdn.com/bphoto/xlcqWTyQMqEUZeb6gQMZAQ/o.jpg"));
        locationList.add(new CustomListView("Bobs Burgers", "172 Forest st", "http://www.bentley.edu/files/brand/Bentley_Wallpaper_HP_1600x900_4a.jpg"));
        locationList.add(new CustomListView("In-N-Out Burger", "1159 N Rengstorff Ave, Mountain View, CA 94043", "https://s3-media3.fl.yelpcdn.com/bphoto/kZ05IXX2BCw4nSxHpEyHuQ/o.jpg"));
        locationList.add(new CustomListView("Plaza Deli Cafe", "2680 Bayshore Pkwy, Mountain View, CA 94043", "https://s3-media2.fl.yelpcdn.com/bphoto/NMAh_m1vf7oH2WvSP3qoHg/o.jpg"));
        locationList.add(new CustomListView("Los Altos Taqueria", "2105 Old Middlefield Way, Ste E, Mountain View, CA 94043", "https://s3-media4.fl.yelpcdn.com/bphoto/2k4UGNL9G-s-QiyD38QpIA/o.jpg"));

        //binds the variable to the view
        searchList = (ListView) findViewById(R.id.searchListView);
       //instanciates the custom adapter
        CustomCategoryAdapter adapter = new CustomCategoryAdapter(this, R.layout.custom_listview_category, locationList);
       //sets the adapter to the list
        searchList.setAdapter(adapter);
    }//on create

}