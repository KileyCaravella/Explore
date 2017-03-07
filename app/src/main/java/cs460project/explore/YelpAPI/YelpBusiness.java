package cs460project.explore.YelpAPI;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Kiley on 2/23/17.
 */

public class YelpBusiness {
    public String id;
    public String name;
    @SerializedName("review_count") public int reviewCount;
    public double rating;
    public double distance;
    public ArrayList<HashMap> categories;
    public String url;
    public String phone;
    @SerializedName("display_phone") public String displayPhone;
    @SerializedName("is_closed") public boolean isClosed;
    public YelpBusinessLocation location;
    @SerializedName("image_url") public String imageURL;
    public HashMap coordinates;
}