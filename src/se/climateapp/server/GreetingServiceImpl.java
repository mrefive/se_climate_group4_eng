package se.climateapp.server;

import java.util.ArrayList;
import java.util.Date;

import se.climateapp.client.GreetingService;
import se.climateapp.shared.MeasurementsQueryResult;
import se.climateapp.shared.TemperatureMeasurement;

import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.google.gwt.user.client.rpc.InvocationException;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements GreetingService {

	public MeasurementsQueryResult getMeasurements(String webSafeCursorString, int pageSize) {
		ArrayList<TemperatureMeasurement> measurementlist = new ArrayList<TemperatureMeasurement>();
		int totalcount = 0;
		int cnt = 0;
		Cursor cursorStart = null;
		Cursor cursorEnd = null;

		try {
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
			Query query = new Query("TptMeas").addSort("index", SortDirection.ASCENDING);
			//query.reverse();
			FetchOptions fetch_options = FetchOptions.Builder.withDefaults();
			PreparedQuery pq = datastore.prepare(query);
			totalcount = pq.countEntities(fetch_options);

			fetch_options.limit(pageSize);
			if (webSafeCursorString != null) {
				cursorStart = Cursor.fromWebSafeString(webSafeCursorString);
				fetch_options.startCursor(cursorStart);
			}		
	
			final QueryResultIterator<Entity> iterator = pq.asQueryResultIterator(fetch_options);

			while (iterator.hasNext()) {
				final Entity entity = iterator.next();
				TemperatureMeasurement measurement = new TemperatureMeasurement();
				measurement.setFieldDate((Date) entity.getProperty("fd"));
				measurement.setFieldAverageTemperature((Double) entity.getProperty("avgTmp"));
				measurement.setFieldAverageTemperatureUncertainty((Double) entity.getProperty("avgTmpUn"));
				measurement.setFieldCity((String) entity.getProperty("City"));
				measurement.setFieldCountry((String) entity.getProperty("Country"));
				measurement.setFieldLatitude((String) entity.getProperty("Lat"));
				measurement.setFieldLongitude((String) entity.getProperty("Long"));

				measurementlist.add(measurement);

				// Set the start cursor to the first entity retrieved.
				if (cursorStart == null) {
					cursorStart = iterator.getCursor();
				}

				cnt++;
				if (cnt >= pageSize)
					break;
			}

			// Set the end cursor to the last entity retrieved.
			cursorEnd = iterator.getCursor();

		} catch (Exception e) {
			throw new InvocationException("Exception connecting to the database", e);
		}

		MeasurementsQueryResult result = new MeasurementsQueryResult();
		result.setList(measurementlist);
		result.setWebSafeCursorString(cursorEnd.toWebSafeString());
		result.setTotalQueryResultCount(totalcount);
		return result;
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
			throw new InvocationException("Exception connecting to the database", e);
		}
	}

}
