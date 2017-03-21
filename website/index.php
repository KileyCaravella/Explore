<?php
include('create_user.php');
?>
<!DOCTYPE html>
<html>
<title>ActivityFinder</title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://www.w3schools.com/lib/w3.css">
<link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Raleway">
<style>
body,h1,h2{font-family: "Raleway", sans-serif}
body, html {height: 100%}
p {line-height: 2}
.bgimg, .bgimg2 {
    min-height: 100%;
    background-position: center;
    background-size: cover;
}
.bgimg {background-image: url("images/explore-wallpaper.jpeg")}
.bgimg2 {background-image: url("images/singapore1.jpg")}
</style>
<body>

<!-- Header / Home-->
<header class="w3-display-container w3-wide bgimg " id="home">
  <div class="w3-display-middle w3-text-white w3-center">
    <h1 class="w3-jumbo">ActivityFinder</h1>
    <h2><b>Explore</b> </h2>
    <h2><b>Adventure</b></h2>
  </div>
</header>

<!-- Navbar (sticky bottom) -->
<div class="w3-bottom w3-hide-small">
  <div class="w3-bar w3-white w3-center w3-padding-8 w3-opacity-min w3-hover-opacity-off">
    <a href="#home" style="width:25%" class="w3-bar-item w3-button">Home</a>
    <a href="#us" style="width:25%" class="w3-bar-item w3-button">Random</a>
    <a href="#wedding" style="width:25%" class="w3-bar-item w3-button">Bucket List</a>
    <a href="#rsvp" style="width:25%" class="w3-bar-item w3-button">Help</a>
  </div>
</div>

<!-- About section -->
<div class="w3-container w3-padding-64 w3-pale-red w3-grayscale-min" id="us">
  <div class="w3-content">
    <h1 class="w3-center w3-text-grey"><b>Activity Finder</b></h1>
    <!-- <img class="w3-round w3-grayscale-min" src="/singapore1.jpg/" style="width:100%;margin:32px 0"> -->
    <p><i> ActivityFinder gives suggestions of what to do in the area based on a bucket list or random activity concept. You can cross things off your bucket list or explore new places, restaurants, and activities with the help of our web services. There is Yelp integration to return random results which you can add to your bucket list, opt out of, or decide to do now. Ultimately, we envision that this will mobilize people, help them set aside time to achieve things they want, and let them discover new places in their area.
</i>
    </p><br>
    <p class="w3-center"><a href="#wedding" class="w3-btn w3-round w3-padding-large w3-large">Cross things off your bucket list</a></p>
  </div>
</div>

<!-- Background photo -->
<div class="w3-display-container bgimg2">
  <div class="w3-display-middle w3-text-white w3-center">
    <h1 class="w3-jumbo">Randomize an activity</h1><br>
    <h2>Use Yelp data</h2>
  </div>
</div>

<!-- Find/bucketlist information -->
<div class="w3-container w3-padding-64 w3-pale-red w3-grayscale-min w3-center" id="wedding">
  <div class="w3-content">
    <h1 class="w3-text-grey"><b>Yelp Listings</b></h1>
    <!-- <img class="w3-round-large w3-grayscale-min" src="singapore1.jpg/" style="width:100%;margin:64px 0"> -->
    <div class="w3-row">
      <div class="w3-half">
        <h2>Find around me</h2>
        <p>Food</p>
        <p>Drinks</p>
      </div>
      <div class="w3-half">
        <h2>Add to your bucket list</h2>
        <p>Yes</p>
        <p>No</p>
      </div>
    </div>
  </div>
</div>

<!-- JOIN section -->
<div class="w3-container w3-padding-64 w3-pale-red w3-center w3-wide" id="rsvp">
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
  <p>Copyright 2017, ActivityFinder, Inc </p>
</footer>
<div class="w3-hide-small" style="margin-bottom:32px"> </div>

</body>
</html>
