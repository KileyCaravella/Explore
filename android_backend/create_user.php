<?php
/**
 * Created by PhpStorm.
 * User: Kiley
 * Date: 3/27/17
 * Time: 1:19 PM
 */

/*
 * Following code will create a new user row
 * All product details are read from HTTP Post Request
 */

include('credentials.php');
include('config.php');

// check for required fields
if (isset($_POST['user_id']) AND isset($_POST['first_name']) AND isset($_POST['last_name']) AND isset($_POST['password']) AND isset($_POST['email'])) {
    //get and store user inputs into variables
    $user_id = trim($_POST['user_id']);
    $first_name = trim($_POST['first_name']);
    $last_name = trim($_POST['last_name']);
    $password = trim($_POST['password']);
    $email = trim($_POST['email']);

    //setting date new user signed up
    date_default_timezone_set("America/New_York");
    $date = new DateTime();
    $datetime = $date->format('Y-m-d H:i:s');

    //creating new authentication code
    $characters = '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz';
    $authentication_code = '';
    for ($i = 0; $i < 5; $i++)
        $authentication_code .= $characters[mt_rand(0, 61)];

    //checking if username is already taken
    $sql = "SELECT user_id FROM user where (user_id='$user_id' OR email='$email')";

    //if no results are returned with that user id, continue to create the new user
    if(mysqli_query($con, $sql)->num_rows == 0) {
        $sql = "INSERT INTO user (user_id, password, email, date_created, first_name, last_name, authentication_code)VALUES('$user_id','$password','$email','$datetime','$first_name','$last_name', '$authentication_code')";
        if(mysqli_query($con, $sql)) {

            //Sending email to user with confirmation code
            $subject = "Your Authentication Code for Explore";
            $message = "Your authentication code is $authentication_code. Happy Exploring!";
            $headers = "From: no-reply@exploreBentley.com";
            $mail = mail($email, $subject, $message, $headers);

            if ($mail) { //if successful
                $response["success"] = 1;
                $response["message"] = "User successfully added.";

                //TODO: Return token when user logs in.
            } else {
                //Problem sending email to user (potential invalid email)
                $response["success"] = 0;
                $response["message"] = "Unable to send email to user.";
            }
        } else {
            $response["success"] = 0;
            $response["message"] = "An error occurred.";
        }
    } else {
        $response["success"] = 0;
        $response["message"] = "username or email are already taken.";
    }

    echo json_encode($response);
}