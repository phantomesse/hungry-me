package hungryme;

import hungryme.Common.Feeling;

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
		if (request.getCategory() == null
				&& request.getFeeling() != Feeling.THIRSTY) {
			// if category is null, request determines available categories
			String[] listOfCategories = DeliveryDotComApi
					.queryNearbyCategories(request.getFeeling(),
							request.getLatitude(), request.getLongitude());

			for (String category : listOfCategories) {
				category = category.substring(1, category.length() - 1).trim();
				returnStr += category + "\n";
			}
			returnStr = returnStr.trim();

		} else {
			// there is a category, so get venues in category
			Venue[] venues = DeliveryDotComApi.getVenues(request.getFeeling(),
					request.getCategory(), request.getLatitude(),
					request.getLongitude());
			if (venues == null) {
				resp.setContentType("application/json");
				resp.getWriter().write("{}");
				return;
			}

			JsonArray venueArray = new JsonArray();
			for (Venue venue : venues) {
				JsonObject venueObject = new JsonObject();
				venueObject.addProperty("name", venue.getName());
				venueObject.addProperty("address", venue.getAddress());
				venueObject.addProperty("latitude", venue.getLatitude());
				venueObject.addProperty("longitude", venue.getLongitude());
				venueObject.addProperty("url", venue.getUrl());
				venueArray.add(venueObject);
			}

			resp.setContentType("application/json");
			resp.getWriter().write(venueArray.toString());
			return;
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
				if (feeling.equals(Common.Feeling.THIRSTY))
					return new String[0];

				URL queryURL = new URL(API_BASE_URL + PARAM_LATITUDE_PREFIX
						+ Double.toString(latitude) + PARAM_LONGITUDE_PREFIX
						+ Double.toString(longitude) + PARAM_MODE_FOOD);
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

		public static Venue[] getVenues(Common.Feeling feeling,
				String findCategory, double latitude, double longitude) {
			ArrayList<Venue> venues = new ArrayList<Venue>();

			try {
				URL queryURL = null;
				switch (feeling) {
				case HUNGRY:
					queryURL = new URL(API_BASE_URL + PARAM_LATITUDE_PREFIX
							+ Double.toString(latitude)
							+ PARAM_LONGITUDE_PREFIX
							+ Double.toString(longitude) + PARAM_MODE_FOOD);

				case THIRSTY:
					queryURL = new URL(API_BASE_URL + PARAM_LATITUDE_PREFIX
							+ Double.toString(latitude)
							+ PARAM_LONGITUDE_PREFIX
							+ Double.toString(longitude) + PARAM_MODE_DRINK);

				}
				String ApiReplyJson = Common.requestHttp(queryURL);

				JsonObject jobject = new JsonParser().parse(ApiReplyJson)
						.getAsJsonObject();
				JsonArray jarray = jobject
						.getAsJsonArray(JSON_MERCHANT_ARRAY_NAME);
				Iterator<JsonElement> iter = jarray.iterator();

				while (iter.hasNext()) {
					jobject = iter.next().getAsJsonObject();
					String name = jobject
							.getAsJsonObject(JSON_MERCHANT_SUMMARY_NAME)
							.getAsJsonPrimitive("name").getAsString();
					String url = jobject
							.getAsJsonObject(JSON_MERCHANT_SUMMARY_NAME)
							.getAsJsonObject("url")
							.getAsJsonPrimitive("complete").getAsString();
					JsonObject location = jobject.getAsJsonObject("location");
					String address = location.getAsJsonPrimitive("street")
							.getAsString();
					double lon = Double.parseDouble(location
							.getAsJsonPrimitive("longitude").getAsString());
					double lat = Double.parseDouble(location
							.getAsJsonPrimitive("latitude").getAsString());
					
					if (feeling == Feeling.THIRSTY) {
						Venue venue = new Venue(name, lat, lon,
								address, url);
						venues.add(venue);
						continue;
					}

					// Check if categorymatches
					try {
						JsonArray cuisines = jobject.getAsJsonObject(
								JSON_MERCHANT_SUMMARY_NAME).getAsJsonArray(
								JSON_MERCHANT_SUMMARY_CUISINES_ARRAY_NAME);

						Iterator<JsonElement> cuisinesIter = cuisines
								.iterator();

						while (cuisinesIter.hasNext()) {
							String category = cuisinesIter.next().toString();
							category = category.substring(1,
									category.length() - 1).trim();

							System.out
									.println("findcategory = " + findCategory);
							System.out.println("category = " + category);

							if (category.equals(findCategory)) {
								Venue venue = new Venue(name, lat, lon,
										address, url);
								venues.add(venue);
								break;
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

				}

			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}

			return venues.toArray(new Venue[0]);
		}

	}

}
