package hungryme;

public class Venue {
	private String name;
	private double latitude;
	private double longitude;
	private String address;
	private String url;
	
	public Venue(String name, double latitude, double longitude, String address, String url) {
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
		this.address = address;
		this.url = url;
	}
	
	public String getUrl() {
		return url;
	}

	public String getName() {
		return name;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public String getAddress() {
		return address;
	}
}
