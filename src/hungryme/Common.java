package hungryme;

import javax.servlet.http.HttpServletRequest;

public class Common {
	public static String processRequest(HttpServletRequest request) {
		// Get feeling
		String feeling = (String) request.get("feeling");
		return feeling;
				
		// Get location
		/*String[] location = ((String) request.getAttribute("location")).split(",");
		double latitude, longitude;
		try {
			latitude = Double.parseDouble(location[0]);
			longitude = Double.parseDouble(location[1]);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}*/
		
		// Get 
	}
}
