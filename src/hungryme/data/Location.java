package hungryme.data;

import com.google.gson.JsonObject;


public class Location extends Data {
	private String address;
	private String city;
	private String state;
	private String zipCode;
	private double latitude;
	private double longitude;
	
	public Location(double latitude, double longitude) {
		this(null, null, null, null, latitude, longitude);
	}
	
	public Location(String address) {
		this(address, null, null);
	}
	
	public Location(String address, String city, String state) {
		this(address, city, state, null);
	}
	
	public Location(String address, String city, String state, String zipCode) {
		this(address, city, state, zipCode, 0, 0);
	}
	
	public Location(String address, String city, String state, String zipCode, double latitude, double longitude) {
		this.address = address;
		this.city = city;
		this.state = state;
		this.zipCode = zipCode;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public JsonObject toJson() {
		JsonObject json = new JsonObject();
		json = addJsonProperty(json, "address", address);
		json = addJsonProperty(json, "city", city);
		json = addJsonProperty(json, "state", state);
		json = addJsonProperty(json, "zipCode", zipCode);
		json = addJsonProperty(json, "latitude", latitude);
		json = addJsonProperty(json, "longitude", longitude);
		return json;
	}
	
	public String toString() {
		return address + ", " + city + ", " + state + " " + zipCode;
	}
}
