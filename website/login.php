<?php
/**
 * Created by PhpStorm.
 * User: Kiley
 * Date: 3/27/17
 * Time: 7:45 AM
 */

/*
 * Following code will allow the user to log in if their userID and password are correct.
 * All product details are read from HTTP Post Request
 */

include('credentials.php');
include('config.php');

if (!isset($_POST['android'])) {
    include('forgot_password.php');
}

// check for required fields
if (isset($_POST['user_auth']) && isset($_POST['password'])) {
    $login_requested = true;

    //get and store user inputs into variables
    $user_auth = trim($_POST['user_auth']);
    $password = trim($_POST['password']);

    //user can login with their userID OR email
    $sql = "SELECT user_id FROM user WHERE ((user_id='$user_auth' OR email='$user_auth') AND password='$password')";

    if (mysqli_query($con, $sql)->num_rows == 1) { //if successfully queried:

        $response["success"] = 1;
        $response["message"] = "User successfully logged in.";

        $login_valid_user = true;

        //TODO: Return token when user logs in.
    } else {
        $response["success"] = 0;
        $response["message"] = "An error occurred.";
        $login_valid_user = false;
    }

    if (isset($_POST['android'])) {
        echo json_encode($response);
    }
}
?>

<!-- LOG IN modal -->

<div id="login" class="w3-modal">
    <div class="w3-modal-content w3-card-8 w3-animate-zoom w3-padding-jumbo" style="max-width:600px">
        <div class="w3-container w3-white w3-center">
            <span onclick="document.getElementById('login').style.display='none'" class="w3-closebtn w3-hover-text-grey w3-margin">x</span>
            <h3 class="w3-wide">Welcome Back!</h3>
            <div>Please Login</div>
            <div class="error-message">
            <?php
            //This creates a red popup text if there is an error at the top of the modal.
            if (isset($user_auth) AND !$login_valid_user) {
                echo '<div class="error" id="invalid_login" style="color:#FF0000">Invalid username or password. Please try again.</div>';
            } else {
                preg_replace('#<div id="invalid_login">(.*?)</div>#', ' ', "");
            }
            ?>
            </div>
            <form class="form-login" method="post">
                <input class="w3-input w3-border" type="text" placeholder="Username" name="user_auth" id="user_auth">
                <input class="w3-input w3-border" type="password" placeholder="Password" name="password" id="password">
                <button class="w3-btn-block w3-green" type="submit" name="submit">Login</button>
                <button onclick="document.getElementById('forgot').style.display='block'; document.getElementById('login').style.display='none'" type="button" class="w3-btn-block w3-red" id="forgot_password_button">Forgot Username/Password</button>
            </form>
            <p><i>Sincerely, Explore Team</i></p>
        </div>
    </div>
</div>
