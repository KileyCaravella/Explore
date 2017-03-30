<?php
/**
 * Created by PhpStorm.
 * User: Kiley
 * Date: 3/27/17
 * Time: 7:37 AM
 */

/*
 * Following code will reset the user's password IF the authentication code is valid.
 * All product details are read from HTTP Post Request
 */

include('credentials.php');
include('config.php');

// array for JSON response
$response = array();

// check for required fields
if (isset($_POST['reset_password_user_input']) AND isset($_POST['password']) AND isset($_POST['authentication_code'])) {
    $reset_password_requested = true;

    //get and store user inputs into variables
    $user_input = trim($_POST['reset_password_user_input']);
    $password = trim($_POST['password']);
    $authentication_code = trim($_POST['authentication_code']);

    //checking valid user_id/email and authentication_code:
    $sql_check_auth = "SELECT user_id FROM user WHERE ((user_id = '$user_input' OR email = '$user_input') AND authentication_code = '$authentication_code')";

    if (mysqli_query($con, $sql_check_auth)->num_rows == 1) {
        $sql_update = "UPDATE user SET password = '$password', authenticated = 1 WHERE ((user_id = '$user_input' OR email = '$user_input') AND authentication_code = '$authentication_code')";
        mysqli_query($con, $sql_update);

        $response["success"] = 1;
        $response["message"] = "User successfully reset.";
        $reset_password_invalid_code = false;
    } else {
        $response["success"] = 0;
        $response["message"] = "An error occurred.";
        $reset_password_invalid_code = true;
    }

    if(isset($_POST['android'])) {
        echo json_encode($response);
    }
}
?>

<!-- Reset Password modal -->
<div id="reset_password" class="w3-modal">
    <div class="w3-modal-content w3-card-8 w3-animate-zoom w3-padding-jumbo" style="max-width:600px">
        <div class="w3-container w3-white w3-center">
            <span onclick="document.getElementById('reset_password').style.display='none'" class="w3-closebtn w3-hover-text-grey w3-margin">x</span>
            <h3 class="w3-wide">Enter Confirmation Code</h3>
            <p>Enter your email address or username with the confirmation code below</p>
            <?php
                //This creates a red popup text if there is an error at the top of the modal.
                if ($reset_password_requested AND $reset_password_invalid_code) {
                    echo '<div class="error" id="invalid_reset_password" style="color:#FF0000">Could not reset password. Please try again.</div>';
                } else {
                    preg_replace('#<div id="invalid_reset_password">(.*?)</div>#', ' ', "");
                }?>
            <form class="form-reset" method="post">
                <input class="w3-input w3-border" type="text" placeholder="Email/Username" name="reset_password_user_input">
                <input class="w3-input w3-border" type="password" placeholder="New Pasword" name="password">
                <input class="w3-input w3-border" type="text" placeholder="Confirmation Code" name="authentication_code">
                <button class="w3-btn-block w3-green" type="submit" name="submit">Confirm New Password</button>
            </form>
            <p></p>
            <div class="w3-row">
                <div class="w3">
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Confirm Password Reset modal -->

<div id="confirm_reset_password" class="w3-modal">
    <div class="w3-modal-content w3-card-8 w3-animate-zoom w3-padding-jumbo" style="max-width:600px">
        <div class="w3-container w3-white w3-center">
            <h3 class="w3-wide">Your password has been reset.</h3>
            <div onclick="document.getElementById('confirm_reset_password').style.display='none'" class="w3-btn-block w3-green">Start Exploring</div>
        </div>
    </div>
</div>
