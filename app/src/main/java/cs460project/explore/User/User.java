package cs460project.explore.User;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Kiley on 3/23/17.
 */

public class User {
    public String userID;
    public String password;
    public String email;
    @SerializedName("first_name") public String firstName;
    @SerializedName("last_name") public String lastName;
}
