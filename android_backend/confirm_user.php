<?php
/**
 * Created by PhpStorm.
 * User: Kiley
 * Date: 3/23/17
 * Time: 5:43 PM
 */

/*
 * Following code will set the authenticated field to 1 (true) for the user if the code entered is correct.
 * All product details are read from HTTP Post Request
 */

include('credentials.php');
include('config.php');

// array for JSON response
$response = array();

// check for required fields
if (isset($_POST['user_id']) && isset($_POST['authentication_code'])) {

    //get and store user inputs into variables
    $user_id = trim($_POST['user_id']);
    $authentication_code = trim($_POST['authentication_code']);

    //creating query
    $sql = "UPDATE user SET authenticated = 1 WHERE (user_id = '$user_id' AND authentication_code = '$authentication_code')";

    //confirming query worked
    $sql_select = "SELECT user_id FROM user WHERE (user_id = '$user_id' AND authenticated = 1)";

    mysqli_query($con, $sql); //run the query
    if (mysqli_query($con, $sql_select)->num_rows == 1) { //if authentication code properly updated:
        $response["success"] = 1;
        $response["message"] = "User successfully updated.";
    } else {
        $response["success"] = 0;
        $response["message"] = "An error occurred.";
    }

    echo json_encode($response);
} else {
    // required field(s) is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";

    echo json_encode($response);
}