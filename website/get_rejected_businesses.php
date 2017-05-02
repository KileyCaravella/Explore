<?php
/**
 * Created by PhpStorm.
 * User: Kiley
 * Date: 5/1/17
 * Time: 3:16 PM
 */

include('config.php');
include('credentials.php');

if (ISSET($_POST['token'])) {
    date_default_timezone_set("America/New_York");
    $invalid_token = false;

    //**TOKEN VALIDATION**//

    $token_sent = trim($_POST['token']);
    $sql_token = "SELECT date_created, user_id FROM token WHERE token = '$token_sent'";

    if (!($token_query = mysqli_query($con, $sql_token))) {
        $response["success"] = 0;
        $response["message"] = "Invalid token.";
        $invalid_token = true;
    }

    //Creates an array from the result with [date, user_id], which is then separated below
    $token_response = mysqli_fetch_array($token_query);
    $token_date_created = $token_response[0];
    $token_user_id = $token_response[1];

    //Tokens "expire" after 24 hours/1 day, so to check it we need to add 1 day
    $token_date_24 = date("Y-m-d H:i:s", strtotime($token_date_created . '+1 day'));

    //Creating date object with current date
    $current_date = new DateTime();
    $current_datetime = $current_date->format('Y-m-d H:i:s');

    //Comparing the two date objects
    if ($token_date_24 < $current_datetime) {
        $response["success"] = 0;
        $response["message"] = "Token expired. Please log in again.";
        $invalid_token = true;
    }

    //**END TOKEN VALIDATION**//

    if (!$invalid_token) {
        $sql_rejected_businesses = "SELECT business_id FROM user_reject WHERE (user_id = '$token_user_id')";
        if ($mysqli_response = mysqli_query($con, $sql_rejected_businesses)) {
            $data = array();

            //Loops through the response items and adds the item's value to the $data array
            while ($row = mysqli_fetch_array($mysqli_response)) {
                $data[] = $row[0];
            }

            $response["success"] = 1;
            $response["message"] = "Rejected businesses acquired.";
            $response["category"] = $data;

        } else {
            $response["success"] = 0;
            $response["message"] = "Unable to find any rejected businesses.";
            $response["category"] = [];
        }
    }

    if (ISSET($_POST['android'])) {
        die(json_encode($response));
    }

}