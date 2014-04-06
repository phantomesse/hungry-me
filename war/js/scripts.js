var homeAddressError = "Home address error.";
var currentLat, currentLon;

$(document).ready(function() {
// Get current location
if (navigator.geolocation) {
  navigator.geolocation.getCurrentPosition(function(location) {
    // Get latitude and longitude
    currentLat = location.coords.latitude;
    currentLon = location.coords.longitude;

    // Store latitude and longitude in cookies
    document.cookie = 'currentLat=' + currentLat + ';';
    document.cookie = 'currentLon=' + currentLon + ';';
  }, function() {
    console.log("Error. Could not find location.");
  });
}
});

function feelingHungry() {
  console.log("I'm feeling hungry!");
  console.log("Home = " + isAtHome());
}

function feelingThirsty() {
  console.log("I'm feeling thirsty!");
}

// TODO
function setHomeAddress(address) {
// Set home address

// Set home latitude and longitude
}

function isAtHome() {
  var cookieStructure = parseCookies();

  var address, lat, lon;
  if (cookieStructure['homeAddress'] == null || cookieStructure['homeLat'] == null || cookieStructure['homeLon'] == null) {
    // Check if home address input field has text
    address = $('input#home-address').val().trim();

    if (address === '') {
      $('input#home-address').focus();
      return homeAddressError;
    } else {
      // Translate address into lat/lon coordinates
      var geocoder = new google.maps.Geocoder();
      geocoder.geocode({ 'address': '411 W 116th Street, New York, NY'}, function(results, status) {
        if (status == google.maps.GeocoderStatus.OK) {
          var location = results[0].geometry.location;
          var lat = location.k;
          var lon = location.A;

          document.cookie = 'homeAddress=' + address;
          document.cookie = 'homeLat' + lat;
          document.cookie = 'homeLon' + lon;
        } else {
          $('input#home-address').focus();
          return homeAddressError;
        }
      });
    }
  } else {
    // Home address exists
    address = cookieStructure['homeAddress'];
    lat = cookieStructure['homeLat'];
    lon = cookieStructure['homeLon'];
  }

  // Get current lat and lon
  currentLat = currentLat | cookieStructure['currentLat'];
  currentLon = currentLon | cookieStructure['currentLon'];

  console.log("Home lat is " + lat);
  console.log("Home lon is " + lon);

  return lat == currentLat && lon == currentLon;
}

// Parses cookies into a hash table of key-value pairs
function parseCookies() {
  var cookieStructure = {};

  var cookies = document.cookie.split(";");
  for (var i = 0; i < cookies.length; i++) {
    var cookie = cookies[i].trim().split("=");
    var key = cookie[0];
    var value = cookie[1];

    cookieStructure[key] = value;
  }

  return cookieStructure;
}