<?php
/**
 * Created by PhpStorm.
 * User: Kiley
 * Date: 3/27/17
 * Time: 7:45 AM
 */

/*
 * Following code will allow the user to log in if their userID and password are correct.
 * All product details are read from HTTP Post Request
 */

include('credentials.php');
include('config.php');

// array for JSON response
$response = array();

// check for required fields
if (isset($_POST['user_id']) && isset($_POST['password'])) {

    //get and store user inputs into variables
    $user_auth = trim($_POST['user_id']);
    $password = trim($_POST['password']);

    //user can login with their userID OR email
    $sql = "SELECT user_id FROM user WHERE ((user_id='$user_auth' OR email='$user_auth') AND password='$password')";

    if (mysqli_query($con, $sql)->num_rows == 1) { //if successfully queried:

        $response["success"] = 1;
        $response["message"] = "User successfully logged in.";

        //TODO: Return token when user logs in.
    } else {
        $response["success"] = 0;
        $response["message"] = "An error occurred.";
    }

    echo json_encode($response);
} else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";

    // echoing JSON response
     echo json_encode($response);
}