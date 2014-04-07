package hungryme.data;

import com.google.gson.JsonObject;

public abstract class Data {
	public abstract JsonObject toJson();
	
	protected JsonObject addJsonProperty(JsonObject json, String key, String value) {
		if (value == null) {
			return json;
		}
		
		json.addProperty(key, value);
		return json;
	}
	
	protected JsonObject addJsonProperty(JsonObject json, String key, double value) {		
		json.addProperty(key, value);
		return json;
	}
}
