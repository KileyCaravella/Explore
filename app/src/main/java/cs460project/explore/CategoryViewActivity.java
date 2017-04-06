package cs460project.explore;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HARDY_NATH on 3/29/2017.
 * handles filling the category view with the custom list view
 */

public class CategoryViewActivity extends Activity {

    ListView listAgain;
    TextView cat;
    String category;


    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_category);
        cat = (TextView)findViewById(R.id.viewDescriptionViewCat);




        //creates a list of custom list views and populates it with test data
        List<CustomListView> locationList = new ArrayList<CustomListView>();
        locationList.add(new CustomListView("Sunny Bowl", "1477 Plymouth St, Ste D, Mountain View, CA 94043", "https://s3-media2.fl.yelpcdn.com/bphoto/xlcqWTyQMqEUZeb6gQMZAQ/o.jpg"));
        locationList.add(new CustomListView("Bobs Burgers", "172 Forest st", "http://www.bentley.edu/files/brand/Bentley_Wallpaper_HP_1600x900_4a.jpg"));
        locationList.add(new CustomListView("In-N-Out Burger", "1159 N Rengstorff Ave, Mountain View, CA 94043", "https://s3-media3.fl.yelpcdn.com/bphoto/kZ05IXX2BCw4nSxHpEyHuQ/o.jpg"));
        locationList.add(new CustomListView("Plaza Deli Cafe", "2680 Bayshore Pkwy, Mountain View, CA 94043", "https://s3-media2.fl.yelpcdn.com/bphoto/NMAh_m1vf7oH2WvSP3qoHg/o.jpg"));
        locationList.add(new CustomListView("Los Altos Taqueria", "2105 Old Middlefield Way, Ste E, Mountain View, CA 94043", "https://s3-media4.fl.yelpcdn.com/bphoto/2k4UGNL9G-s-QiyD38QpIA/o.jpg"));

        //binds the variable to the view
        listAgain = (ListView) findViewById(R.id.viewCatListView);

        //instanciates the custom adapter
        CustomCategoryAdapter adapter = new CustomCategoryAdapter(this, R.layout.custom_listview_category, locationList);

        //sets the adapter to the list
        listAgain.setAdapter(adapter);



    }//on create


    //private void findCategory(){
      //  category = BucketListActivity.selectedCategory;
    //}



}
