package cs460project.explore.YelpAPI;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Kiley on 2/23/17.
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