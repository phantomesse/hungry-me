package hungryme;

import java.io.IOException;
import javax.servlet.http.*;

@SuppressWarnings("serial")
public class DeliveryRequestServlet extends HttpServlet {
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		RequestParams request = Common.processRequest(req);
		if (request.getCategory() == null){
			// if category is null, request determines available categories
			
			
		}
		
		
		resp.setContentType("text/plain");
		//resp.getWriter().println("Delivery");
		
		
	}
	
	static class DeliveryDotComApi{
		private static String API_KEY = "MGY1MTQzOWIwZTBkMmUyNjM2NjM2NTM4MjNkNGJjNjE5";
		
		public static String queryCategory(){
			return null;
		}
		
	}
	
	
}
