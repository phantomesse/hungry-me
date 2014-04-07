package hungryme;

import hungryme.data.Category;
import hungryme.data.Feeling;
import hungryme.data.Venue;

import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

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
		parseHttpRequest(request);

		switch (returnType) {
		case CATEGORIES:
			Category[] categoryResponse = getCategories();
			break;
		case VENUES:
			Venue[] venueResponse = getVenues();
			break;
		}
	}

	protected abstract Category[] getCategories();

	protected abstract Venue[] getVenues();

	private void parseHttpRequest(HttpServletRequest request) {
		// Get feeling
		feeling = Feeling.valueOf(request.getParameter(PARAM_KEY_FEELING));

		// Get location (lat/lon)
		try {
			latitude = Double.valueOf(request.getParameter(PARAM_KEY_LATITUDE));
			longitude = Double.valueOf(request
					.getParameter(PARAM_KEY_LONGITUDE));
		} catch (NumberFormatException e) {
			latitude = 0;
			longitude = 0;
		}

		// Get return type
		returnType = ReturnType.valueOf(request.getParameter("returnType"));

		// Get categories if applicable
		try {
			ArrayList<String> categoriesList = new ArrayList<String>();
			JsonArray categoriesJson = new JsonParser().parse(
					request.getParameter(PARAM_KEY_CATEGORIES))
					.getAsJsonArray();
			Iterator<JsonElement> iter = categoriesJson.iterator();
			while (iter.hasNext()) {
				String category = iter.next().getAsJsonPrimitive()
						.getAsString();
				categoriesList.add(category);
			}
			categories = categoriesList.toArray(new String[0]);
		} catch (Exception e) {
			categories = new String[0];
		}
	}
}