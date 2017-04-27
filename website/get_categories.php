<?php
/**
 * Created by PhpStorm.
 * User: Kiley
 * Date: 4/20/17
 * Time: 2:24 PM
 */

include('config.php');
include('credentials.php');

$headers = getallheaders();

if (ISSET($headers['token'])) {
    date_default_timezone_set("America/New_York");

    //**TOKEN VALIDATION**//

    $token_sent = trim($headers['token']);
    $sql_token = "SELECT date_created, user_id FROM token WHERE token = '$token_sent'";

    if (!($token_query = mysqli_query($con, $sql_token))) {
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
        $response["message"] = "Token expired. Please log in again.";
        $invalid_token = true;
    }

    //**END TOKEN VALIDATION**//

    //If the token is valid, get category information.
    if (!$invalid_token) {
        $user_id_get_credentials = trim($all_headers['user_id']);

        $sql = "SELECT category_id FROM category WHERE user_id = '$token_user_id'";

        if ($mysqli_response = mysqli_query($con, $sql)) {
            $data = array();

            //Loops through the response items and adds the item's value to the $data array
            while ($row = mysqli_fetch_array($mysqli_response)) {
                $data[] = $row[0];
            }
            $response["category"] = $data;
            $invalid_token = false;
        }
    }

    if (ISSET($headers['android'])) {
        die(json_encode($response));
    }
}

