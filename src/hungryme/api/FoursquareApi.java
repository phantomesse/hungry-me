package hungryme.api;

import hungryme.data.Category;
import hungryme.data.Feeling;
import hungryme.data.Location;
import hungryme.data.Venue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * A wrapper for the Foursquare API.
 */
public class FoursquareApi extends ApiWrapper {
	private static final String CLIENT_ID = "KCGSHCP354EOWFTEF2133AMA1O1WN5CGMOBOOC44DYZ1LEW3";
	private static final String CLIENT_SECRET = "53CES5LFW0EOZGOIJQTGX4JD2OPHH5KGZVV2JLPJ0PKDGJ5S";
	private static final String VERSION = "20140226";
	private static final String BASE_URL = "https://api.foursquare.com/v2/venues/explore?client_id="
			+ CLIENT_ID
			+ "&client_secret="
			+ CLIENT_SECRET
			+ "&v="
			+ VERSION
			+ "&openNow=1";

	private static final String PARAM_KEY_LAT_LON = "ll";
	private static final String PARAM_KEY_SECTION = "section";
	private static final String PARAM_VALUE_FOOD = "food";
	private static final String PARAM_VALUE_DRINK = "drinks";

	@Override
	public Category[] queryCategories(Feeling feeling, Location location) {
		// Convert latitude/longitude into parameters
		Parameter paramLocation = new Parameter(PARAM_KEY_LAT_LON,
				location.getLatitude() + "," + location.getLongitude());

		// Convert feeling to parameters
		Parameter paramFeeling = null;
		switch (feeling) {
		case HUNGRY:
			paramFeeling = new Parameter(PARAM_KEY_SECTION, PARAM_VALUE_FOOD);
			break;
		case THIRSTY:
			paramFeeling = new Parameter(PARAM_KEY_SECTION, PARAM_VALUE_DRINK);
			break;
		}

		// Make query and get JSON
		JsonObject json = query(BASE_URL, paramLocation, paramFeeling);

		// Parse JSON to get list of categories
		Venue[] venues = parseVenuesFromJson(json);
		HashMap<String, Category> categoryHashMap = new HashMap<String, Category>();
		for (Venue venue : venues) {
			String[] venueCategories = venue.getCategories();
			for (String venueCategory : venueCategories) {
				if (categoryHashMap.containsKey(venueCategory)) {
					// Add this venue to the existing category
					categoryHashMap.get(venueCategory).addVenue(venue);
				} else {
					// Add category
					Category category = new Category(venueCategory);
					category.addVenue(venue);
					categoryHashMap.put(venueCategory, category);
				}
			}
		}
		
		// Convert hash map to array of categories
		Iterator<Category> iter = categoryHashMap.values().iterator();
		ArrayList<Category> categories = new ArrayList<Category>();
		while (iter.hasNext()) {
			categories.add(iter.next());
		}
		
		return categories.toArray(new Category[0]);
	}

	@Override
	public Venue[] queryVenues(Feeling feeling, Location location,
			String... categories) {
		// TODO Auto-generated method stub
		return null;
	}

	private Venue[] parseVenuesFromJson(JsonObject json) {
		ArrayList<Venue> venues = new ArrayList<Venue>();
		
		JsonArray items = json.getAsJsonObject("response")
				.getAsJsonArray("groups").get(0).getAsJsonObject()
				.getAsJsonArray("items");
		Iterator<JsonElement> iter = items.iterator();
		while (iter.hasNext()) {
			// Get venue json
			JsonObject venueJson = iter.next().getAsJsonObject()
					.getAsJsonObject("venue");

			// Get venue name
			String venueName = venueJson.getAsJsonPrimitive("name")
					.getAsString();

			// Get venue location
			JsonObject locationJson = venueJson.getAsJsonObject("location");
			String address = locationJson.getAsJsonPrimitive("address")
					.getAsString();
			String city = locationJson.getAsJsonPrimitive("city").getAsString();
			String state = locationJson.getAsJsonPrimitive("state")
					.getAsString();
			String zipCode;
			try {
				zipCode = locationJson.getAsJsonPrimitive("postalCode")
						.getAsString();
			} catch (NullPointerException e) {
				zipCode = null;
			}
			double latitude = locationJson.getAsJsonPrimitive("lat")
					.getAsDouble();
			double longitude = locationJson.getAsJsonPrimitive("lng")
					.getAsDouble();
			Location location = new Location(address, city, state, zipCode,
					latitude, longitude);

			// Create venue object
			Venue venue = new Venue(venueName, location);

			// Get venue rating
			double rating = venueJson.getAsJsonPrimitive("rating")
					.getAsDouble();
			venue.setRating(rating);

			// Get venue url
			try {
				String url = venueJson.getAsJsonObject("menu")
						.getAsJsonPrimitive("url").getAsString();
				venue.setUrl(url);
			} catch (NullPointerException e) {
				venue.setUrl(null);
			}

			// Get venue categories
			JsonArray categoriesJson = venueJson.getAsJsonArray("categories");
			Iterator<JsonElement> categoriesIter = categoriesJson.iterator();
			while (categoriesIter.hasNext()) {
				String category = categoriesIter.next().getAsJsonObject()
						.getAsJsonPrimitive("name").getAsString();
				venue.addCategories(category);
			}
			
			venues.add(venue);
		}

		return venues.toArray(new Venue[0]);
	}
	
	public static void main(String[] args) {
		FoursquareApi api = new FoursquareApi();
		Category[] categories = api.queryCategories(Feeling.THIRSTY, new Location(40.8006, -73.9653));
		for (Category category : categories) {
			System.out.println(category + "\n");
		}
	}
}
