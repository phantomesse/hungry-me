package hungryme.data;

import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class Category extends Data {
	private String name;
	private ArrayList<Venue> venues;
	
	public Category(String name) {
		this.name = name;
		venues = new ArrayList<Venue>();
	}
	
	public void addVenue(Venue venue) {
		venues.add(venue);
	}
	
	public String getName() {
		return name;
	}
	
	public Venue[] getVenues() {
		return venues.toArray(new Venue[0]);
	}
	
	public JsonObject toJson() {
		JsonObject json = new JsonObject();
		json = addJsonProperty(json, "name", name);
		
		JsonArray venuesJson = new JsonArray();
		for (Venue venue : venues) {
			venuesJson.add(venue.toJson());
		}
		json.add("venues", venuesJson);
		
		return json;
	}
	
	public String toString() {
		String venueStr = "";
		for (Venue venue : venues) {
			venueStr += "\n" + venue.getName();
		}
		return name + "\n-----" + venueStr;
	}
}
