package hungryme;

import hungryme.data.Feeling;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class RequestServlet extends HttpServlet {
	private enum ReturnType {
		CATEGORIES, VENUES;
	}
	
	private static final String PARAM_KEY_FEELING = "feeling";
	private static final String PARAM_KEY_LATITUDE = "latitude";
	private static final String PARAM_KEY_LONGITUDE = "longitude";
	private static final String PARAM_KEY_CATEGORIES = "categories";
	
	protected Feeling feeling;
	protected double latitude;
	protected double longitude;
	protected ReturnType returnType;
	protected String[] categories;

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		// Parse request for parameters

	}

	private void parseHttpRequest(HttpServletRequest request) {
		// Get feeling
		feeling = Feeling.valueOf(request
				.getParameter(PARAM_KEY_FEELING));

		// Get location (lat/lon)
		try {
			latitude = Double.valueOf(request.getParameter(PARAM_KEY_LATITUDE));
			longitude = Double.valueOf(request.getParameter(PARAM_KEY_LONGITUDE));
		} catch (NumberFormatException e) {
			latitude = 0;
			longitude = 0;
		}
		
		// Get return type
		returnType = ReturnType.valueOf(request.getParameter("returnType"));
		
		// Get categories if applicable
		try {
			request.getParameter(PARAM_KEY_CATEGORIES);
		} catch (Exception e) {
			categories = new String[0];
		}
	}
}