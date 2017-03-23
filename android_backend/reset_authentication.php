<?php
/**
 * Created by PhpStorm.
 * User: Kiley
 * Date: 3/27/17
 * Time: 7:30 AM
 */

/*
 * Following code will reset the user's authentication code using either userID OR email as input.
 * All product details are read from HTTP Post Request
 */

include('credentials.php');
include('config.php');

// array for JSON response
$response = array();

// check for required fields
if (isset($_POST['user_id']) OR isset($_POST['email'])) {

    //get and store user inputs into variables
    $user_input = trim($_POST['user_id']);
    if (!isset($user_input)) {
        $user_input = trim($_POST['email']);
        $user_email = $user_input;
    }

    //creating new authentication code
    $characters = '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz';
    $authentication_code = '';
    for ($i = 0; $i < 5; $i++)
        $authentication_code .= $characters[mt_rand(0, 61)];

    //creating query
    $sql = "UPDATE user SET authentication_code = '$authentication_code', authenticated = 0 WHERE (user_id = '$user_input' OR email = '$user_input')";

    if (mysqli_query($con, $sql)) { //if successfully queried:

        if (!isset($user_email)) {
            //retrieving email if it was not the value given
            $sql_retrieve_email = "SELECT email FROM user WHERE user_id = '$user_input'";
            $user_email = mysqli_query($con, $sql_retrieve_email)->fetch_object()->email;
        }

        //Sending email to user with confirmation code

        // Always set content-type when sending HTML email
        $headers = "From: <no-reply@explorebentley.com>";
        $subject = "Your Authentication Code for Explore";
        $message = "Your authentication code has been reset to $authentication_code. Happy Exploring!";

        if (mail($user_email, $subject, $message, $headers)) {
            $response["success"] = 1;
            $response["message"] = "User successfully reset.";

        } else {
            //Problem sending email to user (potential invalid email)
            $response["success"] = 0;
            $response["message"] = "Unable to send email to user.";
        }
    } else {
        //Original update failed
        $response["success"] = 0;
        $response["message"] = "An error occurred.";
    }

    echo json_encode($response);
} else {
    //Missing required fields
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";

    echo json_encode($response);
}
