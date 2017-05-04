package cs460project.explore.User;

import com.google.gson.annotations.SerializedName;

/**
 * This is the user object created to make logging in methods easier.
 */

public class User {
    public String userID;
    public String password;
    public String email;
    @SerializedName("first_name") public String firstName;
    @SerializedName("last_name") public String lastName;
}
