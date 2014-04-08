package hungryme;

import hungryme.api.FoursquareApi;
import hungryme.data.Category;
import hungryme.data.Venue;

public class NearbyRequestServlet extends RequestServlet {

	@Override
	protected Category[] getCategories() {
		FoursquareApi api = new FoursquareApi(feeling, location);
		return api.queryCategories();
	}

	@Override
	protected Venue[] getVenues() {
		FoursquareApi api = new FoursquareApi(feeling, location);
		return api.queryVenues(categories);
	}

}
