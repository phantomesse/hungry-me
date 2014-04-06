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
		if (request.getCategory() == null){
			// if category is null, request determines available categories
			DeliveryDotComApi.queryNearbyCategories(request.getLatitude(), request.getLongitude());
			
			//TODO: RETURN RESULTS HERE
			
		}
		
		
		resp.setContentType("text/plain");
		//resp.getWriter().println("Delivery");
		
		
	}
	
	static class DeliveryDotComApi{
		//private static String API_KEY = "MGY1MTQzOWIwZTBkMmUyNjM2NjM2NTM4MjNkNGJjNjE5";
		private static final String API_BASE_URL = 
				"https://api.delivery.com/merchant/search/delivery?client_id=MGY1MTQzOWIwZTBkMmUyNjM2NjM2NTM4MjNkNGJjNjE5";
		private static final String JSON_MERCHANT_ARRAY_NAME = "merchants";
		private static final String JSON_MERCHANT_SUMMARY_NAME = "summary";
		private static final String JSON_MERCHANT_SUMMARY_CUISINES_ARRAY_NAME = "cuisines";
		
		
		public static String[] queryNearbyCategories(double latitude, double longitude){
			ArrayList<String> nearbyCategories = new ArrayList<String>();
			
			try {
				URL queryURL = new URL(API_BASE_URL + "&" + Double.toString(latitude) + "&" + Double.toString(longitude));
				String ApiReplyJson = Common.requestHttp(queryURL);
				
				/*
				 * {...,"merchants":
				 * 		[
				 * 			{...,"summary":
				 * 				{...,"cuisines":
				 * 					["Deli","Glatt Kosher","Kosher","American"]
				 * 
				 */
				
				JsonObject jobject = new JsonParser().parse(ApiReplyJson).getAsJsonObject();
				JsonArray jarray = jobject.getAsJsonArray(JSON_MERCHANT_ARRAY_NAME);
				Iterator<JsonElement> iter = jarray.iterator();
				while (iter.hasNext()) {
					jobject = iter.next().getAsJsonObject().getAsJsonObject(JSON_MERCHANT_SUMMARY_NAME);
					JsonArray cuisines = jobject.getAsJsonArray(JSON_MERCHANT_SUMMARY_CUISINES_ARRAY_NAME);
					Iterator<JsonElement> cuisinesIter = cuisines.iterator();
					
					String category;
					while(cuisinesIter.hasNext()){
						 category = iter.next().getAsJsonPrimitive().getAsString();
						 if (!nearbyCategories.contains(category)){
							 nearbyCategories.add(category);
						 }
					}
					
				}
				
				return (String[]) nearbyCategories.toArray();
				
			} catch (MalformedURLException e) {
				// TODO: Malformed URL error
				e.printStackTrace();
				return null;
			}
		}
		
	}
	
	
}
