package hungryme.old;

public class RequestParams {
	
	private Common.Feeling feeling;
	private String category;
	private double latitude;
	private double longitude;
	private String[] excludeList;
	
	public RequestParams(Common.Feeling feeling, String category, 
			double latitude, double longitude, String[] exclude){
		this.feeling = feeling;
		this.category = category;
		this.latitude = latitude;
		this.longitude = longitude;
		this.excludeList = exclude;
	}

	/**
	 * @return the feeling
	 */
	public Common.Feeling getFeeling() {
		return feeling;
	}

	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * @return the latitude
	 */
	public double getLatitude() {
		return latitude;
	}

	/**
	 * @return the longitude
	 */
	public double getLongitude() {
		return longitude;
	}

	/**
	 * @return the excludeList
	 */
	public String[] getExcludeList() {
		return excludeList;
	}

}
