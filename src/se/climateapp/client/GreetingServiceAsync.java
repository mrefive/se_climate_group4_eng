package se.climateapp.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

import se.climateapp.shared.TemperatureMeasurement;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface GreetingServiceAsync {
	void getMeasurements(AsyncCallback<ArrayList<TemperatureMeasurement>> callback);
}
