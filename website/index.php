<?php
include('create_user.php');
include('login.php');
include('forgot_password.php');
include('reset_password.php');
include('confirm_user.php');
include('get_categories.php');
include('new_category.php');
?>

<style>
    .hidden{
        display:none;
    }

    .unhidden{
        display:block;
    }
</style>

<!DOCTYPE html>
<html>
<title>Explore!</title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://www.w3schools.com/lib/w3.css">
<link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Raleway">
<style>
    body,h1,h2{font-family: "Raleway", sans-serif}
    body, html {height: 100%}
    p {line-height: 2}
    .bgimg {
        min-height: 100%;
        background-position: center;
        background-size: cover;
    }
    .bgimg {background-image: url("images/explore-wallpaper.jpeg")}

    p {line-height: 2}
    .bgimg2 {
        min-height: 300px;
        background-position: center;
        background-size: cover;
    }
    .bgimg2 {background-image: url("images/ultimate-bucket-list.png")}

    #bucketlisttext {
        float: right;
        margin-right: 100px;
    }


    /* Style the tab */
    div.tab {
        overflow: hidden;
        border: 1px solid #ccc;
        background-color: #f1f1f1;
    }

    /* Change background color of buttons on hover */
    div.tab button:hover {
        background-color: #ddd;
    }

    /* Style the tab content */
    .tabcontent {
        display: none;
        padding: 6px 12px;
        -webkit-animation: fadeEffect 1s;
        animation: fadeEffect 1s;
    }

    /* Fade in tabs */
    @-webkit-keyframes fadeEffect {
        from {opacity: 0;}
        to {opacity: 1;}
    }

    @keyframes fadeEffect {
        from {opacity: 0;}
        to {opacity: 1;}
    }


</style>
<body>

<script>
    function openCity(evt, cityName) {
        var i, tabcontent, tablinks;
        tabcontent = document.getElementsByClassName("tabcontent");
        for (i = 0; i < tabcontent.length; i++) {
            tabcontent[i].style.display = "none";
        }
        tablinks = document.getElementsByClassName("tablinks");
        for (i = 0; i < tablinks.length; i++) {
            tablinks[i].className = tablinks[i].className.replace(" active", "");
        }
        document.getElementById(cityName).style.display = "block";
        evt.currentTarget.className += " active";
    }
</script>
<!-- Navbar (sticky bottom) -->
<div class="w3-bottom w3-hide-small">
    <div class="w3-bar w3-white w3-center w3-padding-8 w3-opacity-min w3-hover-opacity-off">
        <a href="#home" style="width:25%" class="w3-bar-item w3-button">Home</a>
        <a href="#about" style="width:25%" class="w3-bar-item w3-button">About</a>
        <a href="#bucketlist" style="width:25%" class="w3-bar-item w3-button">Bucket List</a>
        <a href="#join" style="width:25%" class="w3-bar-item w3-button">Sign Up!</a>
    </div>
</div>

<!-- Header / Home-->
<header class="w3-display-container w3-wide bgimg " id="home">
    <div class="w3-content">
        <p class="w3-right"> <!-- gets the modal's information from create_user.php file -->
            <button onclick="document.getElementById('create_user').style.display='block'" class="w3-btn w3-round w3-small" style="margin-right:5px">Sign Up</button>
            <?php
            //If there were errors while creating the user, have the modal appear again.
            if($create_user_requested AND (!$create_user_available OR $create_user_email_error OR $create_user_unknown_error)) {
                echo '<script> document.getElementById("create_user").style.display = "block"; </script>';
            } else if ($create_user_requested) {
                $create_user_requested = false;
                echo '<script> document.getElementById("confirm_user").style.display = "block"; </script>';
            }

            //Code for confirm user becuase it spawns from inside of create user
            if($confirm_user_requested AND $confirm_user_invalid_code) {
                echo '<script> document.getElementById("confirm_user").style.display = "block"; </script>';
            } else if ($confirm_user_requested) {
                $confirm_user_requested = false;
                echo '<script> document.getElementById("confirm_confirm_user").style.display = "block"; </script>';
            }
            ?>
        </p>
        <p class="w3-right">
            <button onclick="document.getElementById('login').style.display='block'" class="w3-btn w3-round w3-small" style = "margin-right:5px">Log In</a></button>
            <?php
            //If there was an error while attempting to login, then reload the modal
            if($login_requested AND !$login_valid_user) {
                echo '<script> document.getElementById("login").style.display = "block"; </script>';
            } else if ($login_requested) {
                $login_requested = false;
            }

            //Code for forgot password because it spawns from inside of the login page:
            if($forgot_password_requested AND ($forgot_password_email_error OR !$forgot_password_user_exists)) {
                echo '<script> document.getElementById("forgot").style.display = "block"; </script>';
            } else if ($forgot_password_requested) {
                $forgot_password_requested = false;
                echo '<script> document.getElementById("reset_password").style.display = "block"; </script>';
            }

            //Code for reset password because it spawns from inside of the forgot password page:
            if($reset_password_requested AND $reset_password_invalid_code) {
                echo '<script> document.getElementById("reset_password").style.display = "block"; </script>';
            } else if ($reset_password_requested) {
                $reset_password_requested = false;
                echo '<script> document.getElementById("confirm_reset_password").style.display = "block"; </script>';
            }
            ?>
        </p>
    </div>
</header>

<!-- LOG IN modal -->
<div id="login" class="w3-modal">
    <div class="w3-modal-content w3-card-8 w3-animate-zoom w3-padding-jumbo" style="max-width:600px">
        <div class="w3-container w3-white w3-center">
            <span onclick="document.getElementById('login').style.display='none'" class="w3-closebtn w3-hover-text-grey w3-margin">x</span>
            <h1 class="w3-wide">Welcome Back!</h1>
            <p>Please Login</p>
            <form>
                <input class="w3-input w3-border" type="text" placeholder="Username" name="name">
                <input class="w3-input w3-border" type="password" placeholder="Password" name="name">
            </form>
            <p><i>Sincerely, Explore Team</i></p>
            <div class="w3-row">
                <div class="w3">
                    <button onclick="document.getElementById('login').style.display='none'" type="button" class="w3-btn-block w3-green">Log In</button>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- About section -->
<div class="w3-container w3-padding-64 w3-pale-red w3-grayscale-min" id="about">
    <div class="w3-content">
        <h1 class="w3-center w3-text-grey"><b>Explore</b></h1>
        <p><i>Explore gives suggestions of what to do in the area based on a bucket list or random activity concept. You can cross things off your bucket list or explore new places, restaurants, and activities with the help of our randomiziation algorithm. Yelp integration is included to enhance your experience, allowing you to include specific locations. Explore allows you to select, save for later, or delete items from choices curated for you. Ultimately, we envision that Explore will mobilize people, helping them fulfill goals on their bucketlist, and pulling them outside of their comfort zone by offering new places, activities, and things to discover.</i></p><br>
        <p class="w3-center"><a href="#bucketlist" class="w3-btn w3-round w3-padding-large w3-large">Start Exploring!</a></p>
    </div>
</div>

<!-- Bucketlist -->
<div class="w3-display-container bgimg2" id="bucketlist">
    <div class="w3-display-middle w3-text-white w3-center" id="bucketlisttext">
        <h1 class="w3-jumbo"></h1><br>
    </div>
</div>

<!-- Location Modal -->
<div id="find_random" class="w3-modal">
    <div class="w3-modal-content w3-card-8 w3-animate-zoom w3-padding-jumbo" style="max-width:600px">
        <div class="w3-container w3-white w3-center">
            <span onclick="document.getElementById('find_random').style.display='none'" class="w3-closebtn w3-hover-text-grey w3-margin">x</span>
            <h4 class="w3-wide">Explore Random, New Places!</h4>
            <p>Enter your location:</p>
            <form class="form-location" method="post">
                <input type="text" placeholder="Zip, City, State..." id="user_location" name="user_location" size="35">
                <button type="submit" name="submit" size="35">Get Started!</button>
            </form>
        </div>
    </div>
</div>

<!-- Random or Category Button -->
<div class="tab w3-pale-red w3-center w3-padding-32">
    <div class="w3-section">
        <h3>Get Started!</h3>
        <button onclick="openCity(event, 'random'); document.getElementById('find_random').style.display='block'" class="tablinks; w3-button w3-round" style="padding:12px 30px">Find Random</button>
        <button onclick="openCity(event, 'browsecategory'); document.getElementById('category').style.display='block'" class="tablinks; w3-button w3-round" style="padding:12px 30px">Browse Your Categories</button>
    </div>

    <!-- Check Forget Modal -->
    <div id="checkforget" class="w3-modal">
        <div class="w3-modal-content w3-card-8 w3-animate-zoom w3-padding-jumbo" style="max-width:600px">
            <div class="w3-container w3-white w3-center">
                <span onclick="document.getElementById('checkforget').style.display='none'" class="w3-closebtn w3-hover-text-grey w3-margin">x</span>
                <h4 class="w3-wide">Forget this Place?</h4>
                <p>Are you sure you want to never see this again?</p>
                <div class="w3-section">
                    <button class="w3-button w3-round w3-green">Forget</button>
                    <button class="w3-button w3-round w3-red">No, Keep It!</button>
                </div>
            </div>
        </div>
    </div>

    <!-- Saving Item Into Category -->
    <div id="pickcategories" class="w3-modal">
        <div class="w3-modal-content w3-card-8 w3-animate-zoom w3-padding-jumbo" style="max-width:600px">
            <div class="w3-container w3-white w3-center">
                <span onclick="document.getElementById('pickcategories').style.display='none'" class="w3-closebtn w3-hover-text-grey w3-margin">x</span>
                <h4 class="w3-wide">Save your Item!</h4>
                <p>Select which category:</p>
                <div class = "w3-section">
                    <form class="browse_category">
                        <select name="Categories">
                            <option value="tacos">Tacos</option>
                            <option value="adventure">Adventure</option>
                            <option value="burgers">Burgers</option>
                            <option value="etc">Etc...</option>
                            <option value="newcategory">Add New!</option>
                        </select>
                    </form>
                    <button class="w3-btn w3-round" type="submit" name=submit">Select</button>
                    <button onclick="document.getElementById('new_categories').style.display='block'; document.getElementById('pickcategories').style.display='none'" class="w3-btn w3-round" style="padding:8px 20px">New Category</button>
                </div>
            </div>
        </div>
    </div>

    <!-- Find Random -->
    <div id = "random" class = "tabcontent">
        <div class="w3-content">
            <div class="w3-container w3-center">
                <div class="w3-card-4 w3-dark-grey" style="width:89%; height: 476px;">
                    <div class="w3-container w3-center">
                        <h3>Explore Now!</h3>
                        <a href="http://www.google.com/" target="myFrame"></a>
                        <br/>
                        <iframe name="myFrame" src="http://www.google.com/" width="800" height="300"></iframe>
                        <div class="w3-section">
                            <button onclick="document.getElementById('pickcategories').style.display='block'" class="w3-button w3-round w3-green">Save</button>
                            <button class="w3-button w3-round w3-light-grey">Next</button>
                            <button onclick="document.getElementById('checkforget').style.display='block'" class="w3-button w3-round w3-red">Forget</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Browse Category -->
    <div id = "browsecategory" class = "tabcontent">
        <div class="w3-content">
            <div class="w3-container w3-center">
                <div class="w3-card-4 w3-dark-grey" style="width:89%; height: 476px;">
                    <div class="w3-container w3-center">
                        <h3>Explore Now!</h3>
                        <a href="http://www.google.com/" target="myFrame"></a>
                        <br/>
                        <iframe name="myFrame" src="http://www.google.com/" width="800" height="300"></iframe>
                        <div class="w3-section">
                            <button onclick="document.getElementById('').style.display='block'" class="w3-button w3-round w3-green">Next</button>
                            <button class="w3-button w3-round w3-light-grey">Previous</button>
                            <button onclick="document.getElementById('checkforget').style.display='block'" class="w3-button w3-round w3-red">Delete</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Sign Up section -->
<div class="w3-container w3-padding-64 w3-white w3-center w3-wide" id="join">
    <h1>JOIN TODAY</h1>
    <p class="w3-large">Make an account</p>
    <p class="w3-xlarge">
        <!-- gets the modal's information from create_user.php file -->
        <button onclick="document.getElementById('create_user').style.display='block'" class="w3-btn w3-round w3-red w3-opacity w3-hover-opacity-off" style="padding:8px 60px">SIGN UP</button>
    </p>
</div>

<!-- Footer -->
<footer class="w3-center w3-black w3-padding-16">
    <p>Copyright 2017, Explore, Inc </p>
</footer>
<div class="w3-hide-small" style="margin-bottom:32px"> </div>

</body>
</html>
