package cs460project.explore.Category;

import cs460project.explore.YelpAPI.YelpBusiness;

/**
 * This stores the yelp business and provides getters
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
        return yelpBusiness.distance * 0.000621371;
    }

    public String getImageURL() {
        return yelpBusiness.imageURL;
    }

    public YelpBusiness getYelpBusiness() {
        return yelpBusiness;
    };

}
