package hungryme;

import java.io.IOException;

import javax.servlet.http.*;


@SuppressWarnings("serial")
public class NearbyRequestServlet extends HttpServlet {

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		//String feeling req.getAttribute("feeling");

		resp.getWriter().println(Common.processRequest(req));
		resp.setContentType("text/plain");
	}


	static class FoursquareApi{
		private static final String PARAM_CLIENT_ID = "client_id=KCGSHCP354EOWFTEF2133AMA1O1WN5CGMOBOOC44DYZ1LEW3";
		private static final String PARAM_CLIENT_SECRET = "&client_secret=53CES5LFW0EOZGOIJQTGX4JD2OPHH5KGZVV2JLPJ0PKDGJ5S";
		private static final String PARAM_OPEN_NOW = "&openNow=1";
		private static final String PARAM_VERSION = "&v=20140226";
		private static final String API_BASE_URL = "https://api.foursquare.com/v2/venues/explore?" +
				PARAM_CLIENT_ID + PARAM_CLIENT_SECRET + PARAM_OPEN_NOW + PARAM_VERSION;
		private static final String PARAM_MODE_FOOD = "&section=food";
		private static final String PARAM_MODE_DRINK = "&section=drinks";
		
		public static String[] queryNearbyCategories(Common.Feeling feeling,
				double latitude, double longitude){
			
			String sectionParam;
			if (feeling.equals(Common.Feeling.HUNGRY)) sectionParam =  PARAM_MODE_FOOD;
			
			
			return null;
			
		}
		
		
	}

}
