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
 * Reference: http://www.androidhive.info/2012/05/how-to-connect-android-with-php-mysql/
 */

// array for JSON response
$response = array();

include('credentials.php');
include('config.php');

// check for required fields
if (isset($_POST['submit'])) {
    //get and store user inputs into variables
    $user_id = trim($_POST['email']);
    $first_name = trim($_POST['first_name']);
    $last_name = trim($_POST['last_name']);
    $password = trim($_POST['password']);
    $email = trim($_POST['email']);

    //setting date new user signed up
    date_default_timezone_set("America/New_York");
    $date = new DateTime();
    $datetime = $date->format('Y-m-d H:i:s');
    $stmt = mysqli_query($con, "SELECT user_id FROM user where user_id='$user_id'");
    $result =$stmt->num_rows;

    //if no results are returned with that user id, continue to create the new user
    if($result==0) {
        $sql = "INSERT INTO user (user_id, password, email, date_created, first_name, last_name)VALUES('$user_id','$password','$email','$datetime','$first_name','$last_name')";
        if(mysqli_query($con, $sql)) {

        }
    }
}
?>
<h1 class=" banner" align="center">
    <span class="span-left">Falcon</span>
    <span class="span-right">Finder</span>
</h1>
<div class="container">
    <div class="row">
        <div class="col-sm-6 col-md-12 col-md-offset-3">
            <div class="account-wall">
                <h1 class="text-center login-title">Sign up</h1>
                <div class="error-message">
                    <?php
                    if (isset($isAvailable)) {
                        echo '<h3 class="error">An account already exists for this username!</h3>';
                    } else {
                    }
                    ?>
                </div>
                <div class="col-sm-6 col-md-12 ">
                    <form class="create_user" action="create_user.php" method="post">
                        <input type="text" name="first_name" id="first_name" class="form-control"
                               pattern="[A-Za-z]{1,40}"
                               placeholder="First Name" required title="Please enter a valid name"
                               autofocus><br>
                        <input type="text" name="last_name" id="last_name" class="form-control" placeholder="Last Name"
                               pattern="[A-Za-z]{1,40}"
                               required title="Please enter a valid name"><br>
                        <input type="email" name="email" id="email" class="form-control" placeholder="Email"
                               pattern=".+@bentley.edu"
                               min-length="10"
                               pattern="[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,4}$" required
                               title="must be a valid bentley email address"><br>
                        <input type="password" name="password" id="password" class="form-control" placeholder="Password"
                               minlength="10"
                               required><br>
                        <input id="user-status-student"   name="user-status" type="radio"  value="Student" checked > Student </input>
                        <input id= "user-status-administrator"   name="user-status" type="radio" value="Administrator" > Administrator</input>

                        <button class="btn btn-lg btn-primary btn-block" type="submit" name="submit">
                            Sign Up
                        </button>
                        <a href="login.php" class="text-center new-account">Have an account already? </a>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>

