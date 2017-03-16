<?php
/**
 * Created by PhpStorm.
 * User: Kiley
 * Date: 3/16/17
 *
 * Referenced http://www.androidhive.info/2012/05/how-to-connect-android-with-php-mysql/
 */

//** When you want to connect to MySQL database, use this: $db = new DB_CONNECT(); **/

class DB_CONNECT {

    // constructor
    function __construct() {
        // connecting to database
        $this->connect();
    }

    // destructor
    function __destruct() {
        // closing db connection
        $this->close();
    }

    /**
     * Function to connect with database
     */
    function connect() {
        // import database connection variables
        include_once('credentials.php');

        // Connecting to mysql database
        $con = mysqli_connect(DB_SERVER, DB_USER, DB_PASSWORD) or die(mysql_error());

        // Selecing database
        $db = mysqli_select_db(DB_DATABASE) or die(mysql_error()) or die(mysql_error());

        // returing connection cursor
        return $con;
    }

    /**
     * Function to close db connection
     */
    function close() {
        // closing db connection
        mysqli_close();
    }
}

?>