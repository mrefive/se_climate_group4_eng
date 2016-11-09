package se.climateapp.server;

import java.util.ArrayList;
import java.util.Date;

import se.climateapp.client.GreetingService;
import se.climateapp.shared.TemperatureMeasurement;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.gwt.user.client.rpc.InvocationException;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server-side implementation of the RPC service.
 */

@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements GreetingService {

	public GreetingServiceImpl() {

	}

	public ArrayList<TemperatureMeasurement> getMeasurements() {
		ArrayList<TemperatureMeasurement> measurementlist = new ArrayList<TemperatureMeasurement>();
		
		try {
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
			// Use class Query to assemble a query
			Query q = new Query("TptMeas");
			
			// Use PreparedQuery interface to retrieve results
			PreparedQuery pq = datastore.prepare(q);
			
			for (Entity entity : pq.asIterable()) {
				TemperatureMeasurement measurement = new TemperatureMeasurement();		
				measurement.setFieldDate((Date) entity.getProperty("fd"));
				measurement.setFieldAverageTemperature((Double) entity.getProperty("avgTmp"));
				measurement.setFieldAverageTemperatureUncertainty((Double) entity.getProperty("avgTmpUn"));
				measurement.setFieldCity((String) entity.getProperty("City"));
				measurement.setFieldCountry((String) entity.getProperty("Country"));
				measurement.setFieldLatitude((String) entity.getProperty("Lat"));
				measurement.setFieldLongitude((String) entity.getProperty("Long"));
				
				measurementlist.add(measurement);
			}
		} catch (Exception e) {
			throw new InvocationException("Exception connecting to the database", e);
		}
		
		return measurementlist;
	}

	public void addMeasurement(TemperatureMeasurement meas) {
		try {
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
			
			Entity entity = new Entity("TptMeas");
			entity.setProperty("fd", meas.getFieldDate());
			entity.setProperty("avgTmp", meas.getFieldAverageTemperature());
			entity.setProperty("avgTmpUn", meas.getFieldAverageTemperatureUncertainty());
			entity.setProperty("City", meas.getFieldCity());
			entity.setProperty("Country", meas.getFieldCountry());
			entity.setProperty("Lat", meas.getFieldLatitude());
			entity.setProperty("Long", meas.getFieldLongitude());
			datastore.put(entity);
			
		} catch (Exception e) {
			throw new InvocationException("Exeption connecting to the database", e);
		}
	}

	@Override
	public ArrayList<TemperatureMeasurement> getMeasurements(int start, int length) {
		// TODO Auto-generated method stub
		return null;
	}
}
