package cs460project.explore.YelpAPI;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * This is the yelp location object. When the yelp business is returned, this information is saved
 * inside of a Hashmap within the Hashmap of the object, so this was created to help with organization
 * and make it easier to call the information.
 */

public class YelpBusinessLocation {
    public String address1;
    public String address2;
    public String address3;
    public String city;
    public String state;
    @SerializedName("zip_code") public String zipCode;
    public String country;
    @SerializedName("display_address") public ArrayList<String> displayAddress;
}