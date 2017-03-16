<?php
/**
 * Created by PhpStorm.
 * User: Kiley
 * Date: 3/16/17
 *
 * Referenced http://www.androidhive.info/2012/05/how-to-connect-android-with-php-mysql/
 */

/*
 * Following code will create a new user row
 * All product details are read from HTTP Post Request
 */

// array for JSON response
$response = array();

// check for required fields
if (isset($_POST['first_name']) && isset($_POST['last_name']) && isset($_POST['password']) && isset($_POST['email'])) {

    $user_id = '0001';
    $first_name = $_POST['first_name'];
    $last_name = $_POST['last_name'];
    $password = $_POST['password'];
    $email = $_POST['email'];

    // include db connect class
    include_once('credentials.php');

    // connecting to db
    $db = new DB_CONNECT();

    // mysql inserting a new row
    $result = mysqli_query("INSERT INTO USERNAME_T(user_id, first_name, last_name, password, email) VALUES('$user_id', '$first_name', '$last_name', '$password', '$email')");

    // check if row inserted or not
    if ($result) {
        // successfully inserted into database
        $response["success"] = 1;
        $response["message"] = "Product successfully created.";

        // echoing JSON response
        echo json_encode($response);
    } else {
        // failed to insert row
        $response["success"] = 0;
        $response["message"] = "Oops! An error occurred.";

        // echoing JSON response
        echo json_encode($response);
    }
} else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";

    // echoing JSON response
    echo json_encode($response);
}
?>


?>