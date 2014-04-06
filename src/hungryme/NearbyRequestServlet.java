package hungryme;

import hungryme.Common.Feeling;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@SuppressWarnings("serial")
public class NearbyRequestServlet extends HttpServlet {

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		RequestParams request = Common.processRequest(req);
		
		System.out.println("feeling = " + request.getFeeling());
		System.out.println("lat = " + request.getLatitude());
		System.out.println("lon = " + request.getLongitude());

		String returnStr = "";
		if (request.getCategory() == null) {
			// if category is null, request determines available categories
			String[] listOfCategories = FoursquareApi.queryNearbyCategories(
					request.getFeeling(), request.getLatitude(),
					request.getLongitude());

			for (String category : listOfCategories) {
				category = category.substring(1, category.length() - 1).trim();
				returnStr += category + "\n";
			}
			returnStr = returnStr.trim();

		}

		resp.setContentType("text/plain");
		resp.getWriter().println(returnStr);
	}

	static class FoursquareApi {
		private static final String JSON_RESPONSE_NAME = "response";
		private static final String JSON_GROUPS_ARRAY_NAME = "groups";
		private static final String JSON_ITEMS_ARRAY_NAME = "items";
		private static final String JSON_VENUE_NAME = "venue";
		private static final String JSON_CATEGORIES_NAME = "categories";
		private static final String JSON_SHORTNAME_NAME = "shortName";
		private static final String PARAM_CLIENT_ID = "client_id=KCGSHCP354EOWFTEF2133AMA1O1WN5CGMOBOOC44DYZ1LEW3";
		private static final String PARAM_CLIENT_SECRET = "&client_secret=53CES5LFW0EOZGOIJQTGX4JD2OPHH5KGZVV2JLPJ0PKDGJ5S";
		private static final String PARAM_OPEN_NOW = "&openNow=1";
		private static final String PARAM_VERSION = "&v=20140226";
		private static final String API_BASE_URL = "https://api.foursquare.com/v2/venues/explore?";
		private static final String PARAM_MODE_FOOD = "&section=food";
		private static final String PARAM_MODE_DRINK = "&section=drinks";
		private static final String PARAM_LAT_LON_PREFIX = "&ll=";

		public static String[] queryNearbyCategories(Common.Feeling feeling,
				double latitude, double longitude) {
			ArrayList<String> nearbyCategories = new ArrayList<String>();

			try {

				String sectionParam = null;

				switch (feeling) {
				case HUNGRY:
					sectionParam = PARAM_MODE_FOOD;
					break;
				case THIRSTY:
					sectionParam = PARAM_MODE_DRINK;
					break;
				}

				URL queryURL = new URL(API_BASE_URL + PARAM_CLIENT_ID
						+ PARAM_CLIENT_SECRET + PARAM_LAT_LON_PREFIX
						+ Double.toString(latitude) + ","
						+ Double.toString(longitude) + PARAM_OPEN_NOW
						+ PARAM_VERSION + sectionParam);
				String ApiReplyJson = Common.requestHttp(queryURL);

				JsonObject jobject = new JsonParser().parse(ApiReplyJson)
						.getAsJsonObject();
				JsonArray groups = jobject.getAsJsonObject(JSON_RESPONSE_NAME)
						.getAsJsonArray(JSON_GROUPS_ARRAY_NAME);
				Iterator<JsonElement> groupsIter = groups.iterator();
				while (groupsIter.hasNext()) {
					JsonArray entries = groupsIter.next().getAsJsonObject()
							.getAsJsonArray(JSON_ITEMS_ARRAY_NAME);
					Iterator<JsonElement> entryIter = entries.iterator();
					while (entryIter.hasNext()) {
						JsonArray categories = entryIter.next()
								.getAsJsonObject()
								.getAsJsonObject(JSON_VENUE_NAME)
								.getAsJsonArray(JSON_CATEGORIES_NAME);
						Iterator<JsonElement> categoriesIter = categories
								.iterator();
						while (categoriesIter.hasNext()) {
							String category = categoriesIter.next()
									.getAsJsonObject()
									.getAsJsonPrimitive(JSON_SHORTNAME_NAME)
									.toString();
							if (!nearbyCategories.contains(category)) {
								nearbyCategories.add(category);
							}
						}
					}
				}

				return nearbyCategories.toArray(new String[0]);

			} catch (Exception e) {
				// TODO: Error
				return null;
			}
		}

	}

}
