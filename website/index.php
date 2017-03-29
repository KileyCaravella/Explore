<?php
include('create_user.php');
?>
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

</style>
<body>

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
      if(isset($isAvailable) && $isAvailable==false) {
         echo '<script> document.getElementById("create_user").style.display = "block"; </script>';
      }
    ?></p>
    <p class="w3-right"><button onclick="document.getElementById('login').style.display='block'" class="w3-btn w3-round w3-small" style = "margin-right:5px">Log In</a></button></p>
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
    <img class="w3-round w3-grayscale-min" src="images/singapore1.jpg" style="width:100%;margin:32px 0">
    <p><i><center>Explore gives suggestions of what to do in the area based on a bucket list or random activity concept. You can cross things off your bucket list or explore new places, restaurants, and activities with the help of our randomiziation algorithm. Yelp integration is included to enhance your experience, allowing you to include specific locations. Explore allows you to select, save for later, or delete items from choices curated for you. Ultimately, we envision that Explore will mobilize people, helping them fulfill goals on their bucketlist, and pulling them outside of their comfort zone by offering new places, activities, and things to discover.
</center></i>
    </p><br>
    <p class="w3-center"><a href="#bucketlist" class="w3-btn w3-round w3-padding-large w3-large">Start Exploring!</a></p>
  </div>
</div>

<!-- Bucketlist -->
<div class="w3-display-container bgimg2" id="bucketlist">
  <div class="w3-display-middle w3-text-white w3-center" id="bucketlisttext">
    <h1 class="w3-jumbo"></h1><br>
  </div>
</div>

<!-- Find/bucketlist information -->
<div class="w3-container w3-padding-64 w3-pale-red w3-center" id="random">
  <div class="w3-content">
    <h1 class="w3-text-grey"><b>Yelp Listings</b></h1>
    <img class="w3-round-large" src="images/singapore1.jpg" style="width:100%;margin:64px 0">
    <div class="w3-row">
      <div class="w3-third">
        <h3>Find around me</h3>
 <p class="w3-center"><a href="#food" class="w3-btn w3-round w3-padding-small w3-medium">Food</a></p>
 <p class="w3-center"><a href="#activities" class="w3-btn w3-round w3-padding-small w3-medium">Activites</a></p>
 <p class="w3-center"><a href="#more" class="w3-btn w3-round w3-padding-small w3-medium">...More!</a></p>
      </div>
      <div class="w3-third">
 <p><img src="images/yelplisting.png" alt="Yelp Listing" height="290" width="359"></p>
      </div>
      <div class="w3-third">
        <h3>	Add to your bucket list</h3>
 <p class="w3-center"><a href="#add" class="w3-btn w3-round w3-padding-small w3-medium">Yes!</a></p>
 <p class="w3-center"><a href="#delete" class="w3-btn w3-round w3-padding-small w3-medium">No :(</a></p>
 <p class="w3-center"><a href="#customize" class="w3-btn w3-round w3-padding-small w3-medium">Add Your Own</a></p>
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
    <?php
      if(isset($isAvailable) && $isAvailable==false) {
         echo '<script> document.getElementById("create_user").style.display = "block"; </script>';
      }
    ?>
  </p>
</div>

<!-- Footer -->
<footer class="w3-center w3-black w3-padding-16">
  <p>Copyright 2017, Explore, Inc </p>
</footer>
<div class="w3-hide-small" style="margin-bottom:32px"> </div>

</body>
</html>
