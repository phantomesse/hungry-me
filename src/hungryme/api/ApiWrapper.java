package hungryme.api;

import hungryme.data.Category;
import hungryme.data.Feeling;
import hungryme.data.Location;
import hungryme.data.Venue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public abstract class ApiWrapper {
	
	public abstract Category[] queryCategories(Feeling feeling, Location location);
	public abstract Venue[] queryVenues(Feeling feeling, Location location, String... categories);
	
	protected JsonObject query(String baseUrl, Parameter... parameters) {
		for (Parameter parameter : parameters) {
			baseUrl = baseUrl + "&" + parameter.toString();
		}
		
		try {
			// Create URL connection
			URL queryUrl = new URL(baseUrl);
			System.out.println(baseUrl);
			URLConnection connection = queryUrl.openConnection();
			
			// Read in response JSON
			BufferedReader in = new BufferedReader(
					new InputStreamReader(connection.getInputStream()));
			StringBuilder reply = new StringBuilder();
			String line;
			while ((line = in.readLine()) != null) {
				reply.append(line);
			}
			
			// Close connection
			in.close();
			
			// Convert String to JsonObject
			return new JsonParser().parse(reply.toString())
					.getAsJsonObject();
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return new JsonObject();
		} catch (IOException e) {
			e.printStackTrace();
			return new JsonObject();
		}
	}
}
