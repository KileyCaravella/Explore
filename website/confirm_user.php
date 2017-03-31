<?php
/**
 * Created by PhpStorm.
 * User: Kiley
 * Date: 3/23/17
 * Time: 5:43 PM
 */

/*
 * Following code will set the authenticated field to 1 (true) for the user if the code entered is correct.
 * All product details are read from HTTP Post Request
 */

include('credentials.php');
include('config.php');

// array for JSON response
$response = array();

// check for required fields
if (isset($_POST['confirm_user_user_input']) && isset($_POST['authentication_code'])) {
    $confirm_user_requested = true;

    //get and store user inputs into variables
    $user_input = trim($_POST['confirm_user_user_input']);
    $authentication_code = trim($_POST['authentication_code']);

    //checking valid user_id/email and authentication_code:
    $sql_check_auth = "SELECT user_id FROM user WHERE ((user_id = '$user_input' OR email = '$user_input') AND authentication_code = '$authentication_code')";
    $numRows = mysqli_query($con, $sql_check_auth)->num_rows;
    echo '<div>' . $numRows . '</div>';
    if (mysqli_query($con, $sql_check_auth)->num_rows == 1) {
        $sql_update = "UPDATE user SET authenticated = 1 WHERE ((user_id = '$user_input' OR email = '$user_input') AND authentication_code = '$authentication_code')";
        mysqli_query($con, $sql_update);

        $response["success"] = 1;
        $response["message"] = "User successfully reset.";
        $confirm_user_invalid_code = false;
    } else {
        $response["success"] = 0;
        $response["message"] = "An error occurred.";
        $confirm_user_invalid_code = true;
    }

    if (isset($_POST['android'])) {
        echo json_encode($response);
    }
}
?>

<!-- Reset Password modal -->
<div id="confirm_user" class="w3-modal">
    <div class="w3-modal-content w3-card-8 w3-animate-zoom w3-padding-jumbo" style="max-width:600px">
        <div class="w3-container w3-white w3-center">
            <span onclick="document.getElementById('reset_password').style.display='none'" class="w3-closebtn w3-hover-text-grey w3-margin">x</span>
            <h3 class="w3-wide">Enter Confirmation Code</h3>
            <p>Enter your email address or username with the confirmation code below</p>
            <?php
            //This creates a red popup text if there is an error at the top of the modal.
            if ($confirm_user_requested AND $confirm_user_invalid_code) {
                echo '<div class="error" id="invalid_confirm_user" style="color:#FF0000">Could not confirm user. Please try again.</div>';
            } else {
                preg_replace('#<div id="invalid_confirm_user">(.*?)</div>#', ' ', "");
            }?>
            <form class="form-reset" method="post">
                <input class="w3-input w3-border" type="text" placeholder="Email/Username" name="confirm_user_user_input">
                <input class="w3-input w3-border" type="text" placeholder="Confirmation Code" name="authentication_code">
                <button class="w3-btn-block w3-green" type="submit" name="submit">Confirm Account</button>
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

<div id="confirm_confirm_user" class="w3-modal">
    <div class="w3-modal-content w3-card-8 w3-animate-zoom w3-padding-jumbo" style="max-width:600px">
        <div class="w3-container w3-white w3-center">
            <h3 class="w3-wide">You account has been confirmed!.</h3>
            <div onclick="document.getElementById('confirm_confirm_user').style.display='none'" class="w3-btn-block w3-green">Start Exploring</div>
        </div>
    </div>
</div>
