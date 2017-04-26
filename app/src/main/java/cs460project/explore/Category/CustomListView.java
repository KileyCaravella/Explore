package cs460project.explore.Category;

/**
 * Created by HARDY_NATH on 3/29/2017.
 * Just contains location name, address, and image url
 * This class is meant to be overloaded and used in a list view
 */

public class CustomListView {
    private String locationName;
    private String address;
    private String imageURL;


    public CustomListView(String locationName, String address, String imageURL) {
        this.locationName = locationName;
        this.address = address;
        this.imageURL = imageURL;
    }
    public CustomListView(String locationName, String address) {
        this.locationName = locationName;
        this.address = address;
    }

    public CustomListView(){

    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
