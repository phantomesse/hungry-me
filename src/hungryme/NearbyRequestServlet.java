package hungryme;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.http.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


@SuppressWarnings("serial")
public class NearbyRequestServlet extends HttpServlet {

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		//String feeling req.getAttribute("feeling");

		resp.getWriter().println(Common.processRequest(req));
		resp.setContentType("text/plain");
	}


	static class FoursquareApi{
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
		private static final String API_BASE_URL = "https://api.foursquare.com/v2/venues/explore?" +
				PARAM_CLIENT_ID + PARAM_CLIENT_SECRET + PARAM_OPEN_NOW + PARAM_VERSION;
		private static final String PARAM_MODE_FOOD = "&section=food";
		private static final String PARAM_MODE_DRINK = "&section=drinks";
		private static final String PARAM_LAT_LON_PREFIX = "&ll=";

		public static String[] queryNearbyCategories(Common.Feeling feeling,
				double latitude, double longitude){
			ArrayList<String> nearbyCategories = new ArrayList<String>();
			
			try {

				String sectionParam = null;
				if (feeling.equals(Common.Feeling.HUNGRY)) sectionParam = PARAM_MODE_FOOD;
				else if (feeling.equals(Common.Feeling.THIRSTY)) sectionParam = PARAM_MODE_DRINK;

				URL queryURL = new URL (API_BASE_URL + PARAM_CLIENT_ID + PARAM_CLIENT_SECRET
						+ PARAM_LAT_LON_PREFIX + Double.toString(latitude) + "," 
						+ Double.toString(longitude) + PARAM_OPEN_NOW 
						+ PARAM_VERSION + sectionParam);
				String ApiReplyJson = Common.requestHttp(queryURL);
				
				JsonObject jobject = new JsonParser().parse(ApiReplyJson).getAsJsonObject();
				JsonArray groups = jobject.getAsJsonObject(JSON_RESPONSE_NAME)
						.getAsJsonArray(JSON_GROUPS_ARRAY_NAME);
				Iterator<JsonElement> groupsIter = groups.iterator();
				while (groupsIter.hasNext()){
					JsonArray entries = groupsIter.next().getAsJsonObject()
							.getAsJsonArray(JSON_ITEMS_ARRAY_NAME);
					Iterator<JsonElement> entryIter = entries.iterator();
					while (entryIter.hasNext()){
						JsonArray categories = entryIter.next().getAsJsonObject()
								.getAsJsonObject(JSON_VENUE_NAME).getAsJsonArray(JSON_CATEGORIES_NAME);
						Iterator<JsonElement> categoriesIter = categories.iterator();
						while (categoriesIter.hasNext()){
							String category = categoriesIter.next()
									.getAsJsonObject().getAsJsonPrimitive(JSON_SHORTNAME_NAME).toString();
							System.out.println(category);
							if (!nearbyCategories.contains(category)){
								nearbyCategories.add(category);
							}
						}
					}
				}

				return nearbyCategories.toArray(new String[0]);
				
			} catch (Exception e){
				//TODO: Error
				return null;
			}
		}


	}

}
