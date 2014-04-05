package hungryme;

import javax.servlet.http.HttpServletRequest;

public class Common {
	
	public enum Feeling {
		HUNGRY, THIRSTY;
	}
	
	public final static String FEELING_PARAM_NAME = "feeling";
	public final static String CATEGORY_PARAM_NAME = "category";
	public final static String LATITUDE_PARAM_NAME = "lat";
	public final static String LONGITUDE_PARAM_NAME = "long";
	public final static String EXCLUDED_PARAM_NAME = "not";
	
	public final static String LIST_DELIMETER = "`";
	
	public static RequestParams processRequest(HttpServletRequest request) {

		// Get feeling
		Feeling feeling = null;
		try {
			feeling = Feeling.valueOf(((String) request.getParameter(FEELING_PARAM_NAME)).toUpperCase());
		} catch (Exception e) {
			//TODO: There is an error
		}
		
		// Get category (null if not present)
		String category = (String) request.getParameter(CATEGORY_PARAM_NAME);
		
		// Get location
		double latitude = 0, longitude = 0;
		try {
			latitude = Double.parseDouble((String) request.getParameter("LATITUDE_PARAM_NAME"));
			longitude = Double.parseDouble((String) request.getParameter("LONGITUDE_PARAM_NAME"));
		} catch (NumberFormatException e) {
			//TODO: Error
			e.printStackTrace();
		}
		
		// Get excluded
		String[] exclude = request.getParameter(EXCLUDED_PARAM_NAME).split(LIST_DELIMETER);
		
		// Create RequestParams
		return new RequestParams(feeling, category, latitude, longitude, exclude);
		
	}

}
