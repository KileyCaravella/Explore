<?php
/**
 * Created by PhpStorm.
 * User: Kiley
 * Date: 3/27/17
 * Time: 7:30 AM
 */

/*
 * Following code will reset the user's authentication code using either userID OR email as input.
 * All product details are read from HTTP Post Request
 */

include('credentials.php');
include('config.php');

// array for JSON response
$response = array();

// check for required fields
if (isset($_POST['forgot_password_user_input'])) {
    $forgot_password_requested = true;

    //get and store user inputs into variables
    $user_input = trim($_POST['forgot_password_user_input']);

    //creating new authentication code
    $characters = '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz';
    $authentication_code = '';
    for ($i = 0; $i < 5; $i++)
        $authentication_code .= $characters[mt_rand(0, 61)];

    //making sure the user actually exists before continuing
    $sql_check_exists = "SELECT user_id FROM user WHERE (user_id = '$user_input' OR email = '$user_input')";

    if (mysqli_query($con, $sql_check_exists)->num_rows == 1) { //if successfully queried:
        $sql = "UPDATE user SET authentication_code = '$authentication_code', authenticated = 0 WHERE (user_id = '$user_input' OR email = '$user_input')";
        mysqli_query($con, $sql);

        $sql_retrieve_email = "SELECT email FROM user WHERE (user_id = '$user_input' OR email = '$user_input')";
        $user_email = mysqli_query($con, $sql_retrieve_email)->fetch_object()->email;

        //Sending email to user with confirmation code
        $headers = "From: <no-reply@explorebentley.com>";
        $subject = "Your Authentication Code for Explore";
        $message = "Your authentication code has been reset to $authentication_code. Happy Exploring!";

        if (mail($user_email, $subject, $message, $headers)) {
            $response["success"] = 1;
            $response["message"] = "User successfully reset.";

            $forgot_password_email_error = false;
            $forgot_password_user_exists = true;
        } else {
            //Problem sending email to user (potential invalid email)
            $response["success"] = 0;
            $response["message"] = "Unable to send email to user.";
            $forgot_password_email_error = true;
        }
    } else {
        //Original update failed
        $response["success"] = 0;
        $response["message"] = "An error occurred.";
        $forgot_password_user_exists = false;
    }
    if(isset($_POST['android'])) {
        echo json_encode($response);
    }
}
?>

<!-- Forgot Password modal -->
<div id="forgot" class="w3-modal">
    <div class="w3-modal-content w3-card-8 w3-animate-zoom w3-padding-jumbo" style="max-width:600px">
        <div class="w3-container w3-white w3-center">
            <span onclick="document.getElementById('forgot').style.display='none'" class="w3-closebtn w3-hover-text-grey w3-margin">x</span>
            <h3 class="w3-wide">Reset your password</h3>
            <div>Enter your email address or username</div>
            <?php
                //This creates a red popup text if there is an error at the top of the modal.
                if ($forgot_password_requested AND $forgot_password_email_error) {
                    echo '<div class="error" id="invalid_email" style="color:#FF0000">Could not send email to requested address. Please try again.</div>';
                } else {
                    preg_replace('#<div id="invalid_email">(.*?)</div>#', ' ', "");
                }

                if ($forgot_password_requested AND !$forgot_password_user_exists) {
                    echo '<div class="error" id="invalid_user_input" style="color:#FF0000">User does not exist with this name or email. Please try again.</div>';
                } else {
                    preg_replace('#<div id="invalid_user_input">(.*?)</div>#', ' ', "");
                }
            ?>
            <form class="form-forgot" method="post">
                <input class="w3-input w3-border" type="text" placeholder="Email/Username" id="user_input" name="forgot_password_user_input">
                <button class="w3-btn-block w3-green" type="submit" name="submit">Reset Password</button>
            </form>
            <p></p>
            <div class="w3-row">
                <div class="w3">
                </div>
            </div>
        </div>
    </div>
</div>
