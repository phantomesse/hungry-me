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
	
	
}
