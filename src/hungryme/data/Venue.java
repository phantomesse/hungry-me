package hungryme.data;

import java.util.ArrayList;


public class Venue {
	private String name;
	private Location location;
	private String url;
	private double rating;
	private ArrayList<String> categories;
	
	public Venue(String name) {
		this(name, null);
	}
	
	public Venue(String name, Location location) {
		this.name = name;
		this.location = location;
		categories = new ArrayList<String>();
	}
	
	public void addCategories(String... categories) {
		for (String category : categories) {
			this.categories.add(category);
		}
	}
	
	public String[] getCategories() {
		return categories.toArray(new String[0]);
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public String getName() {
		return name;
	}
	
	public String toString() {
		return name + "\n" + location.toString() + "\nRating: " + rating + "\nUrl: " + url;
	}
}
