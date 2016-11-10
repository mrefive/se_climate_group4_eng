package se.climateapp.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import se.climateapp.shared.MeasurementsQueryResult;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface GreetingService extends RemoteService {
	MeasurementsQueryResult getMeasurements(String webSafeCursorString, int pageSize);
}
