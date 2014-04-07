package hungryme.data;

import java.util.ArrayList;

public class Category {
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
	
	public String toString() {
		String venueStr = "";
		for (Venue venue : venues) {
			venueStr += "\n" + venue.getName();
		}
		return name + "\n-----" + venueStr;
	}
}
