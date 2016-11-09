package se.climateapp.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import se.climateapp.shared.TemperatureMeasurement;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface GreetingService extends RemoteService {
	ArrayList<TemperatureMeasurement> getMeasurements();
	ArrayList<TemperatureMeasurement> getMeasurements(int start, int length);
}
