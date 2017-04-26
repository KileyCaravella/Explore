<?php
/**
 * Created by PhpStorm.
 * User: Kiley
 * Date: 4/20/17
 * Time: 2:24 PM
 */

include('config.php');
include('credentials.php');

if (!ISSET($_POST['android'])) {
    include('login.php');
}

$headers = getallheaders();
$invalid_token = false;


    date_default_timezone_set("America/New_York");

    //**TOKEN VALIDATION**//
    if (ISSET($_POST['android'])) {
        $token = trim($_POST['token']);
    }

    $sql_token = "SELECT date_created, user_id FROM token WHERE token = '$token'";

    if (!($token_query = mysqli_query($con, $sql_token))) {
        $response["message"] = "Invalid token.";
        $invalid_token = true;
    }

    //Creates an array from the result with [date, user_id], which is then separated below
    $token_response = mysqli_fetch_array($token_query);
    $token_date_created = $token_response[0];
    $token_user_id = $token_response[1];

    //Tokens "expire" after 24 hours/1 day, so to check it we need to add 1 day
    $token_date_24 = date("Y-m-d H:i:s", strtotime($token_date_created.'+1 day'));

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

    //If the token is valid, get category information.
    if(!$invalid_token) {
        //$user_id_get_credentials = trim($all_headers['user_id']);

        $new_category = trim($_POST['new_category']);
        $sql = "INSERT INTO category (user_id, category_id) VALUES ('$token_user_id', '$new_category')";
        if(mysqli_query($con, $sql)) {
            $response["success"] = 1;
            $response["message"] = "Successfully added category";
        } else {
            $response["success"] = 0;
            $response["message"] = "Something failed.";
        }
    }

    if (ISSET($_POST['android'])) {
        die(json_encode($response));
    }

    if (ISSET($_POST['new_category'])) {
        echo $new_category;
    }

?>

<!-- New Category Modal -->
<div id="new_categories" class="w3-modal">
    <div class="w3-modal-content w3-card-8 w3-animate-zoom w3-padding-jumbo" style="max-width:600px">
        <div class="w3-container w3-white w3-center">
            <span onclick="document.getElementById('new_categories').style.display='none'" class="w3-closebtn w3-hover-text-grey w3-margin">x</span>
            <h4 class="w3-wide">Add a New Category!</h4>
            <p>Enter a Category Name:</p>
            <div class = "w3-section">
                <form class="form-new_category" method="post">
                    <input class="w3-input w3-border" type="text" placeholder="Category Name" name="new_category" id="new_category">
                    <button onclick="document.getElementById('new_categories').style.display='none'" type="submit" name="submit">Save</button>
                </form>
            </div>
        </div>
    </div>
</div>




