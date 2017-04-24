<?php
/**
 * Created by PhpStorm.
 * User: Kiley
 * Date: 4/20/17
 * Time: 2:24 PM
 */

include('config.php');
include('credentials.php');

if (ISSET($_GET['user_id'])) {

    $user_id_get_credentials = trim($_GET['user_id']);

    $sql = "SELECT * FROM category WHERE user_id == '$user_id_get_credentials'";

    $response['category'] = mysqli_query($con, $sql);

    if (ISSET($_GET['android'])) {
        die(json_encode($response));
    }
}






