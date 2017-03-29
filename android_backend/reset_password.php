<?php
/**
 * Created by PhpStorm.
 * User: Kiley
 * Date: 3/27/17
 * Time: 7:37 AM
 */

/*
 * Following code will reset the user's password IF the authentication code is valid.
 * All product details are read from HTTP Post Request
 */

include('credentials.php');
include('config.php');

// array for JSON response
$response = array();

// check for required fields
if (isset($_POST['user_id']) AND isset($_POST['password']) AND isset($_POST['authentication_code'])) {

    //get and store user inputs into variables
    $user_id = trim($_POST['user_id']);
    $password = trim($_POST['password']);
    $authentication_code = trim($_POST['authentication_code']);

    //creating query
    $sql = "UPDATE user SET password = '$password', authenticated = 1 WHERE (user_id = '$user_id' AND authentication_code = '$authentication_code')";

    if (mysqli_query($con, $sql)) { //if successfully queried:
        $response["success"] = 1;
        $response["message"] = "User successfully reset.";

        echo json_encode($response);
    } else {
        $response["success"] = 0;
        $response["message"] = "An error occurred.";

        echo json_encode($response);
    }
} else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";

    // echoing JSON response
    echo json_encode($response);
}
