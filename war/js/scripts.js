var homeAddressError = "Home address error.";
var currentLat, currentLon;

$(document).ready(function() {
  $('div.loading').hide();

  // Scroll to the top
  $(window).scrollTop(0);

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

  // Set home address if exists in cookies
  var cookieStructure = parseCookies();
  if (cookieStructure['homeAddress']) {
    $('input#home-address').val(cookieStructure['homeAddress']);
  }
});

function showLoadingScreen(section) {
  $(section + ' div.loading').show();
}

function hideLoadingScreen(section) {
  $(section + ' div.loading').fadeOut();
}

function feelingHungry() {
  var atHome = isAtHome();

  atHome.done(function(data) {
    console.log("DONE");

    if (data === true) {
    // Clear categories
    $('#category-choices').empty();

    // Show loading screen
    showLoadingScreen('section#categories');

    // Scroll to categories section
    $('html, body').animate({
      scrollTop: $("#categories").offset().top
    }, 500);

    // Get stuff
    $.post("delivery", {'feeling' : 'hungry', 'lat' : currentLat, 'lon' : currentLon, 'not' : '' }).done(function(data) {
      var categories = data.split('\n');

      // Show some categories
      for (var i = 0; i < categories.length - 1; i++) {
        $('#category-choices').append('<div class="choice"><div class="inner"><h2>' + categories[i] + '</h2></div></div>');
      }

      // Set title and subtitle
      $('#categories h1#title').text("It looks like you're at home!");
      $('#categories h2#subtitle').text("Here are some delivery options...");

      $('.choice').click(function() {
        var choice = $($(this).children('.inner')[0]).children('h2').text();

        // Show loading screen
        showLoadingScreen('section#venues');

        // Scroll to categories section
        $('html, body').animate({
          scrollTop: $("#venues").offset().top
        }, 500);

        // Load venues based on choice
        $.post("delivery", {'feeling' : 'hungry', 'lat' : currentLat, 'lon' : currentLon, 'not' : '', 'category' : choice }).done(function(data) {
          console.log(data);


          // Set title and subtitle
          $('#venues h1#title').text("Here are some restaurants!");

          // Show some venues
          

          hideLoadingScreen('section#venues');
        });

      });

      hideLoadingScreen('section#categories');
    });

} else if (data === false) {
    // Set title and subtitle
    $('#categories h1#title').text("It looks like you're outside!");
    $('#categories h2#subtitle').text("Here are some cuisine types near you...");

    // Scroll to categories section
    $('html, body').animate({
      scrollTop: $("#categories").offset().top
    }, 500);
  }
});
}

function feelingThirsty() {
  var atHome = isAtHome();

  if (atHome === true) {
    // Set title and subtitle
    $('#categories h1#title').text("It looks like you're at home!");
    $('#categories h2#subtitle').text("Here are some delivery options...");

    // Scroll to categories section
    $('html, body').animate({
      scrollTop: $("#categories").offset().top
    }, 500);

  } else if (atHome === false) {
    // Set title and subtitle
    $('#categories h1#title').text("It looks like you're outside!");
    $('#categories h2#subtitle').text("Here are some drink types near you...");

    // Scroll to categories section
    $('html, body').animate({
      scrollTop: $("#categories").offset().top
    }, 500);
  }
}

function isAtHome() {
  var deferred = $.Deferred();
  var address, lat, lon;

//  if (cookieStructure['homeAddress'] == null || cookieStructure['homeLat'] == null || cookieStructure['homeLon'] == null) {
    // Check if home address input field has text
    address = $('input#home-address').val().trim();

    if (address === '') {
      $('input#home-address').addClass('error');
      $('input#home-address').focus();
      deferred.resolve(homeAddressError);
    } else {
      // Translate address into lat/lon coordinates
      var geocoder = new google.maps.Geocoder();
      geocoder.geocode({ 'address': address}, function(results, status) {
        if (status == google.maps.GeocoderStatus.OK) {
          var location = results[0].geometry.location;
          var lat = location.k;
          var lon = location.A;

          if (lat == null || lon == null) {
            console.log("HI");
            $('input#home-address').addClass('error');
            $('input#home-address').focus();
            return homeAddressError;
          }

          document.cookie = 'homeAddress=' + address;
          document.cookie = 'homeLat=' + lat;
          document.cookie = 'homeLon=' + lon;

          // Get current lat and lon
          currentLat = Math.round(currentLat * 100) / 100;
          currentLon = Math.round(currentLon * 100) / 100;

          var results = lat <= currentLat + 0.05 && lat >= currentLat - 0.05 && lon <= currentLon + 0.05 && lon >= currentLon - 0.05;
          deferred.resolve(results);
        } else {
          $('input#home-address').addClass('error');
          $('input#home-address').focus();
          deferred.resolve(homeAddressError);
        }
      });
}
return deferred.promise();
/*} else {
    // Home address exists
    address = cookieStructure['homeAddress'];
    lat = Math.round(cookieStructure['homeLat'] * 100) / 100;
    lon = Math.round(cookieStructure['homeLon'] * 100) / 100;

    // Get current lat and lon
    currentLat = Math.round(cookieStructure['currentLat'] * 100) / 100;
    currentLon = Math.round(cookieStructure['currentLon'] * 100) / 100;

    return lat <= currentLat + 0.05 && lat >= currentLat - 0.05 && lon <= currentLon + 0.05 && lon >= currentLon - 0.05;
  }*/
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