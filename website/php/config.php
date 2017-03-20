<?php
/**
 * Created by PhpStorm.
 * User: Kiley
 * Date: 3/16/17
 *
 * Referenced: XIE_XIAO
 */

include_once('credentials.php');
$dbhost = $_SERVER['RDS_HOSTNAME'];
$dbport = $_SERVER['RDS_PORT'];
$dbname = $_SERVER['RDS_DB_NAME'];
$charset = 'utf8' ;

$username = $_SERVER['RDS_USERNAME'];
$password = $_SERVER['RDS_PASSWORD'];

?>