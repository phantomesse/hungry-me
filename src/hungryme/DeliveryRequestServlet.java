package hungryme;

import java.io.IOException;
import java.net.MalformedURLException;
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
public class DeliveryRequestServlet extends HttpServlet {

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		RequestParams request = Common.processRequest(req);

		String returnStr = "";
		if (request.getCategory() == null) {
			// if category is null, request determines available categories
			String[] listOfCategories = DeliveryDotComApi
					.queryNearbyCategories(request.getFeeling(),
							request.getLatitude(),
							request.getLongitude());

			for (String category : listOfCategories) {
				returnStr += category + "\n";
			}

		} else {
			// there is a category, so get venues in category
			String category = request.getCategory();
		}

		resp.setContentType("text/plain");
		resp.getWriter().println(returnStr);
	}

	static class DeliveryDotComApi {
		// private static String API_KEY =
		// "MGY1MTQzOWIwZTBkMmUyNjM2NjM2NTM4MjNkNGJjNjE5";
		private static final String API_BASE_URL = "https://api.delivery.com/merchant/search/delivery?client_id=MGY1MTQzOWIwZTBkMmUyNjM2NjM2NTM4MjNkNGJjNjE5";
		private static final String JSON_MERCHANT_ARRAY_NAME = "merchants";
		private static final String JSON_MERCHANT_SUMMARY_NAME = "summary";
		private static final String JSON_MERCHANT_SUMMARY_CUISINES_ARRAY_NAME = "cuisines";
		private static final String PARAM_LATITUDE_PREFIX = "&latitude=";
		private static final String PARAM_LONGITUDE_PREFIX = "&longitude=";
		private static final String PARAM_MODE_FOOD = "&merchant_type=R";
		private static final String PARAM_MODE_DRINK = "&merchant_type=W";

		public static String[] queryNearbyCategories(Common.Feeling feeling, 
				double latitude, double longitude) {
			ArrayList<String> nearbyCategories = new ArrayList<String>();

			try {
				// there are no categories for drinks
				if (feeling.equals(Common.Feeling.THIRSTY)) return new String[0];

				URL queryURL = new URL(API_BASE_URL 
						+ PARAM_LATITUDE_PREFIX	+ Double.toString(latitude) 
						+ PARAM_LONGITUDE_PREFIX + Double.toString(longitude)
						+ PARAM_MODE_FOOD);
				String ApiReplyJson = Common.requestHttp(queryURL);

				/*
				 * {...,"merchants": [ {...,"summary": {...,"cuisines":
				 * ["Deli","Glatt Kosher","Kosher","American"]
				 */

				JsonObject jobject = new JsonParser().parse(ApiReplyJson)
						.getAsJsonObject();
				JsonArray jarray = jobject
						.getAsJsonArray(JSON_MERCHANT_ARRAY_NAME);
				Iterator<JsonElement> iter = jarray.iterator();
				while (iter.hasNext()) {
					jobject = iter.next().getAsJsonObject()
							.getAsJsonObject(JSON_MERCHANT_SUMMARY_NAME);
					try {
						JsonArray cuisines = jobject
								.getAsJsonArray(JSON_MERCHANT_SUMMARY_CUISINES_ARRAY_NAME);
						Iterator<JsonElement> cuisinesIter = cuisines
								.iterator();

						String category;
						while (cuisinesIter.hasNext()) {
							category = cuisinesIter.next().toString();
							if (!nearbyCategories.contains(category)) {
								nearbyCategories.add(category);
							}
						}
					} catch (Exception e) {

					}

				}

				return nearbyCategories.toArray(new String[0]);

			} catch (MalformedURLException e) {
				// TODO: Malformed URL error
				e.printStackTrace();
				return null;
			}
		}


		public static Venue[] getVenues(Common.Feeling feeling, String category, 
				double latitude, double longitude) {
			ArrayList<Venue> venues = new ArrayList<Venue>();

			try {
				URL queryURL;
				if (feeling.equals(Common.Feeling.THIRSTY)) {
					
					queryURL = new URL(API_BASE_URL 
							+ PARAM_LATITUDE_PREFIX + Double.toString(latitude) 
							+ PARAM_LONGITUDE_PREFIX + Double.toString(longitude)
							+ PARAM_MODE_DRINK);
					String ApiReplyJson = Common.requestHttp(queryURL);
					
					JsonObject jobject = new JsonParser().parse(ApiReplyJson)
							.getAsJsonObject();
					JsonArray jarray = jobject
							.getAsJsonArray(JSON_MERCHANT_ARRAY_NAME);
					Iterator<JsonElement> iter = jarray.iterator();
					while (iter.hasNext()) {
						jobject = iter.next().getAsJsonObject();
						JsonArray cuisines = jobject.getAsJsonObject(JSON_MERCHANT_SUMMARY_NAME)
							.getAsJsonArray(JSON_MERCHANT_SUMMARY_CUISINES_ARRAY_NAME);
						//if cuisines contains "category" fill out and add the venue to return
					}
						
					
				} else {
					
				}

			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}

			return venues.toArray(new Venue[0]);
		}

	}

}
