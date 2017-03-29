<?php
/**
 * Created by PhpStorm.
 * User: Kiley
 * Date: 3/16/17
 */

//Connects to backend
include_once ('credentials.php');
$con = mysqli_connect(RDS_SERVER,RDS_USERNAME,RDS_PASSWORD,RDS_DB_NAME, RDS_PORT);

// Check connection
if (mysqli_connect_errno()) {
    echo "Failed to connect to MySQL: " . mysqli_connect_error();
}