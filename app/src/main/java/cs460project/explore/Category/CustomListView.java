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
    private YelpBusiness yelpBusiness;

     public CustomListView(YelpBusiness yelpBusiness){
         this.yelpBusiness = yelpBusiness;
     }

    public String getLocationName() {
        return yelpBusiness.name;
    }

    public Double getDistance() {
        return yelpBusiness.distance;
    }

    public String getImageURL() {
        return yelpBusiness.imageURL;
    }

}
