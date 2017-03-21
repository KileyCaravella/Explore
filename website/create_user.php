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
if (isset($_POST['submit'])) {
    //get and store user inputs into variables
    $user_id = trim($_POST['username']);
    $first_name = trim($_POST['first_name']);
    $last_name = trim($_POST['last_name']);
    $password = trim($_POST['password']);
    $email = trim($_POST['email']);

    //setting date new user signed up
    date_default_timezone_set("America/New_York");
    $date = new DateTime();
    $datetime = $date->format('Y-m-d H:i:s');

    //checking if username is already taken
    $stmt = mysqli_query($con, "SELECT user_id FROM user where user_id='$user_id'");
    $result =$stmt->num_rows;

    //if no results are returned with that user id, continue to create the new user
    if($result==0) {
        $sql = "INSERT INTO user (user_id, password, email, date_created, first_name, last_name)VALUES('$user_id','$password','$email','$datetime','$first_name','$last_name')";
        if(mysqli_query($con, $sql)) {
            $isAvailable = true;
        }
    } else {
        $isAvailable = false; //if an account already exists in the database
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
                if (isset($isAvailable) && $isAvailable == false) {
                    echo '<div class="error" style="color:#FF0000">An account already exists with this username!</div>';
                } else {
                }
                ?>
            </div>
            <form class="form-signin" method="post">
                <input class="w3-input w3-border" type="text" placeholder="username" name="username">
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
