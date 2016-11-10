package se.climateapp.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

import se.climateapp.shared.MeasurementsQueryResult;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface GreetingServiceAsync {
	void getMeasurements(String webSafeCursorString, int pageSize, AsyncCallback<MeasurementsQueryResult> callback);
}
