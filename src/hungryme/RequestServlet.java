package hungryme;

import hungryme.data.Category;
import hungryme.data.Feeling;
import hungryme.data.Location;
import hungryme.data.Venue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

@SuppressWarnings("serial")
public abstract class RequestServlet extends HttpServlet {
	private enum ReturnType {
		CATEGORIES, VENUES;
	}

	private static final String PARAM_KEY_FEELING = "feeling";
	private static final String PARAM_KEY_LATITUDE = "latitude";
	private static final String PARAM_KEY_LONGITUDE = "longitude";
	private static final String PARAM_KEY_CATEGORIES = "categories";

	protected Feeling feeling;
	protected Location location;
	protected ReturnType returnType;
	protected String[] categories;

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		// Parse request for parameters
		parseHttpRequest(request);

		JsonArray returnJson = new JsonArray();
		switch (returnType) {
		case CATEGORIES:
			Category[] categoryResponse = getCategories();

			for (Category category : categoryResponse) {
				returnJson.add(category.toJson());
			}

			break;
		case VENUES:
			Venue[] venueResponse = getVenues();

			for (Venue venue : venueResponse) {
				returnJson.add(venue.toJson());
			}

			break;
		}

		response.setContentType("application/json");
		response.getWriter().write(returnJson.toString());
	}

	protected abstract Category[] getCategories();

	protected abstract Venue[] getVenues();

	private void parseHttpRequest(HttpServletRequest request) {
		// Get feeling
		feeling = Feeling.valueOf(request.getParameter(PARAM_KEY_FEELING).toUpperCase());

		// Get location (lat/lon)
		double latitude, longitude;
		try {
			latitude = Double.valueOf(request.getParameter(PARAM_KEY_LATITUDE));
			longitude = Double.valueOf(request
					.getParameter(PARAM_KEY_LONGITUDE));
		} catch (NumberFormatException e) {
			latitude = 0;
			longitude = 0;
		}
		location = new Location(latitude, longitude);

		// Get return type
		returnType = ReturnType.valueOf(request.getParameter("returnType").toUpperCase());

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
			e.printStackTrace();
		}
	}
}