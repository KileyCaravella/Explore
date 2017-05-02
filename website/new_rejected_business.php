<?php
/**
 * Created by PhpStorm.
 * User: Kiley
 * Date: 5/1/17
 * Time: 3:06 PM
 */

include('config.php');
include('credentials.php');

if (ISSET($_POST['token']) && ISSET($_POST['business_id'])) {
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

    //Once token is validated, the business is grabbed to be inserted into the database.
    $business_id = trim($_POST['business_id']);

    if (!$invalid_token) {
        $sql = "INSERT INTO user_reject (user_id, business_id) VALUES ('$token_user_id', '$business_id')";
        if(mysqli_query($con, $sql)) {
            $response["success"] = 1;
            $response["message"] = "Successfully added category";

            //Once the category is updated, the list of remaining categories is returned to android
            if (ISSET($_POST['android']) && !$invalid_token) {
                $sql_get_categories = "SELECT business_id FROM user_reject WHERE user_id = '$token_user_id'";

                if ($mysqli_response = mysqli_query($con, $sql_get_categories)) {
                    $data = array();

                    //Loops through the response items and adds the item's value to the $data array
                    while ($row = mysqli_fetch_array($mysqli_response)) {
                        $data[] = $row[0];
                    }

                    $response["success"] = 1;
                    $response["message"] = "Business added to rejected group.";
                    $response["category"] = $data;

                } else {
                    $response["success"] = 1;
                    $response["message"] = "Business added, but unable to find any.";
                    $response["category"] = [];
                }
            }

        } else {
            $response["success"] = 0;
            $response["message"] = "Something failed.";
        }
    }

    if (ISSET($_POST['android'])) {
        die(json_encode($response));
    }
}