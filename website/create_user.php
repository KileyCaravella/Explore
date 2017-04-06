<?php
/**
 * Created by PhpStorm.
 * User: Kiley
 * Date: 3/16/17
 *
 */

/*
 * Following code will create a new user row
 * All product details are read from HTTP Post Request
 */

include('credentials.php');
include('config.php');

// check for required fields
// check for required fields
if (isset($_POST['user_id']) AND isset($_POST['first_name']) AND isset($_POST['last_name']) AND isset($_POST['password']) AND isset($_POST['email'])) {
    //indicating to index.php that this file was requested.
    $create_user_requested = true;

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

            $headers = "MIME-Version: 1.0" . "\r\n";
            $headers = "From: <no-reply@explorebentley.com>";

            if (filter_var($email, FILTER_VALIDATE_EMAIL) AND mail($email, $subject, $message, $headers)) {
                $response["success"] = 1;
                $response["message"] = "User successfully added.";
                //TODO: Return token when user logs in.
                $create_user_available = true;
                $create_user_email_error = false;
                $create_user_unknown_error = false;

            } else {
                //Problem sending email to user (potential invalid email)
                $response["success"] = 0;
                $response["message"] = "Unable to send email to user.";

                $sql_delete = "DELETE FROM user WHERE user_id = '$user_id'";
                mysqli_query($con, $sql_delete);
                $create_user_email_error = true;
            }
        } else {
            $response["success"] = 0;
            $response["message"] = "An error occurred.";
            $create_user_unknown_error = true;
        }
    } else {
        $response["success"] = 0;
        $response["message"] = "username or email are already taken.";
        $create_user_available = false;
    }

    if (isset($_POST['android'])) {
        die(json_encode($response));
    }
}
?>

<!-- SIGN UP modal -->
<div id="create_user" class="w3-modal">
    <div class="w3-modal-content w3-card-8 w3-animate-zoom w3-padding-jumbo" style="max-width:600px">
        <div class="w3-container w3-white w3-center">
            <span onclick="document.getElementById('create_user').style.display='none'" class="w3-closebtn w3-hover-text-grey w3-margin">x</span>
            <h1 class="w3-large">Sign up Below by filling out the fields:</h1>
            <div class="error-message">
                <?php
                //These create red popup text if there is an error at the top of the modal.

                //User availability
                if (isset($user_id) AND !$create_user_available) {
                    echo '<div class="error" id="user_not_available" style="color:#FF0000">An account already exists with this username or email!</div>';
                } else {
                    preg_replace('#<div id="user_not_avalable">(.*?)</div>#', ' ', "");
                }

                //Error sending the user an email
                if (isset($user_id) AND $create_user_email_error) {
                    echo '<div class="error" id="email_error" style="color:#FF0000">Invalid email. Please try with a different one.</div>';
                } else {
                    preg_replace('#<div id="email_error">(.*?)</div>#', ' ', "");
                }

                //Unknown error occured
                if (isset($user_id) AND $create_user_unknown_error) {
                    echo '<div class="error" id="unknown_error" style="color:#FF0000">Unknown Error. Please try again.</div>';
                } else {
                    preg_replace('#<div id="unknown_error">(.*?)</div>#', ' ', "");
                }
                ?>
            </div>
            <form class="form-signin" method="post">
                <input class="w3-input w3-border" type="text" placeholder="username" name="user_id">
                <input class="w3-input w3-border" type="text" placeholder="first name" name="first_name" id="first_name" required title="Please enter a valid name" autofocus>
                <input class="w3-input w3-border" type="text" placeholder="last name" name="last_name" id="last_name" pattern="[A-Za-z]{1,40}" required title="Please enter a valid name">
                <input class="w3-input w3-border" type="email" name="email" id="email" class="form-control" placeholder="email" min-length="10" pattern="[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,4}$" required title="must be a valid email address">
                <input class="w3-input w3-border" type="password" name="password" id="password" class="form-control" placeholder="password" minlength="10" required>
                <button class="w3-btn-block w3-green" type="submit" name="submit">Sign Up</button>
            </form>
            <p><i>Sincerely, ActivityFinder Team</i></p>
        </div>
    </div>
</div>
