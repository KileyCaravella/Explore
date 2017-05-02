package cs460project.explore.Category;

import android.os.Bundle;
import android.util.Log;
import android.content.Intent;
import com.google.gson.Gson;
import cs460project.explore.YelpAPI.YelpBusiness;
import cs460project.explore.YelpAPI.YelpBusinessLocation;

/**
 * Created by HARDY_NATH on 3/29/2017.
 * Just contains location name, address, and image url
 * This class is meant to be overloaded and used in a list view
 */

public class CustomListView {
    private String locationName;
    private YelpBusinessLocation address;
    private String imageURL;
    private Double locationDistance;
    private YelpBusiness yelpBusiness;


     /*
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
    }//bundle
*/


     public CustomListView(YelpBusiness yelpBusiness){
         this.yelpBusiness = yelpBusiness;
         this.locationName = yelpBusiness.name;
         this.address = yelpBusiness.location;
         this.imageURL = yelpBusiness.imageURL;
         //Distance is given in Meters, so here it is converted to miles.
         this.locationDistance = yelpBusiness.distance *= 0.000621371;
     }


    public CustomListView(){

    }

    public String getLocationName() {
        return locationName;
    }

    public Double getDistance(){return locationDistance;}

    public String getImageURL() {
        return imageURL;
    }



}//main
