/* YelpAPI program in node.js */

function authenticateYelp(callback) {
  //Required variables
  var globals = require('./globals.js');
  var qs = require("querystring");
  var http = require("https");

  //Information for API call (type, url, headers)
  var options = {
    "method": "POST",
    "hostname": globals.BASE_URL,
    "port": null,
    "path": globals.AUTH_URL,
    "headers": {
      "content-type": "application/x-www-form-urlencoded",
    }
  };

  console.log("Retrieving Token...");

  //Setting up response when request is made
  var request = http.request(options, function (response) {
    var chunks = [];

    response.on("data", function (chunk) {
      chunks.push(chunk);
    });

    response.on("end", function () {

      //Parsing JSON file and setting the token in the globals file to returned value
      var body = Buffer.concat(chunks);
      var jsonObject = JSON.parse(body.toString('utf8'));
      globals.TOKEN = jsonObject.token_type + " " + jsonObject.access_token;
      console.log("Token Retrieved.");
      callback();
    });
  });

  //Actually Making the request
  request.write(qs.stringify({
    client_id: globals.CLIENT_ID,
    client_secret: globals.CLIENT_SECRET,
    grant_type: 'client_credentials'}));
  request.end();
}

//Function to call outside yelpAPI to get business
function retrieveBusiness(latitude, longitude, callback) {
  //Required variables
  var globals = require('./globals.js');
  var qs = require("querystring");
  var http = require("https");

  //If have not authenticated with Yelp yet, get the token
  if (globals.TOKEN == "") {
    authenticateYelp (function() {
      console.log("Making Businesses API Call...");
      businessesAPICall(latitude, longitude, function(business) {
        console.log("Business Retrieved.");
        callback(business);
      });
    });
  } else {
    console.log("Making Businesses API Call...");
    businessesAPICall(latitude, longitude, function(businesses) {
      console.log("Business Retrieved.");
      callback(businesses);
    });
  }
}

//Actual API Call that has a callback with the business that was found.
function businessesAPICall(latitude, longitude, callback) {
  //Required variables
  var globals = require('./globals.js');
  var qs = require("querystring");
  var http = require("https");

  var path = globals.BUSINESS_URL + "limit=" + globals.BUSINESS_LIMIT + "&latitude=" + latitude + "&longitude=" + longitude;

  var options = {
    "method": "GET",
    "hostname": globals.BASE_URL,
    "port": null,
    "path": path,
    "headers": {
      "authorization": globals.TOKEN,
    }
  };

  var request = http.request(options, function (response) {
    var chunks = [];

    response.on("data", function (chunk) {
      chunks.push(chunk);
    });

    response.on("end", function () {

      //Parsing json object to return a random business from the amount queried
      var body = Buffer.concat(chunks);
      var jsonObject = JSON.parse(body.toString('utf8'));
      var businessObject = jsonObject.businesses;
      var objectNumber = Math.floor(Math.random() * globals.BUSINESS_LIMIT);
      callback(businessObject[objectNumber]);
    });
  });

  request.end();
}
