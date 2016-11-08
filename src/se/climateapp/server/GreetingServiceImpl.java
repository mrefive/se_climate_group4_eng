package se.climateapp.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import se.climateapp.client.GreetingService;
import se.climateapp.shared.TemperatureMeasurement;
import se.climateapp.shared.TemperatureMeasurementSet;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.utils.SystemProperty;
import com.google.gwt.user.client.rpc.InvocationException;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server-side implementation of the RPC service.
 */

@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements GreetingService {

	private ArrayList<String> executeQuery(String query) {
		ArrayList<String> queryResult = new ArrayList<String>();

		Connection connection = null;
		ResultSet resultSet = null;
		ResultSetMetaData rsmd = null;
		Statement statement = null;
		String url;
		String classForName;
		String instanceName = "ISNTANCEMNAME";
		String databaseName = "?DBNAME?";
		String projectID = "climateapp-148823";

		if (SystemProperty.environment.value() == SystemProperty.Environment.Value.Production) {
			// Load the class that provides the new "jdbc:google:mysql://"
			// prefix.
			classForName = "com.mysql.jdbc.GoogleDriver";
			url = "jdbc:google:mysql://" + projectID + ":" + instanceName + "/" + databaseName + "?user=root";
		} else {
			// Local MySQL instance to use during development.
			classForName = "com.mysql.jdbc.Driver";
			url = "jdbc:mysql://127.0.0.1:3306/" + databaseName + "?user=root";
		}

		try {
			Class.forName(classForName);
			connection = DriverManager.getConnection(url);

			if (connection != null) {
				statement = connection.createStatement();
			}

			if (statement != null) {
				resultSet = statement.executeQuery(query);
			}

			rsmd = resultSet.getMetaData();

			queryResult.add(rsmd.getColumnCount() + "");

			for (int i = 1; i <= rsmd.getColumnCount(); i++) {
				queryResult.add(rsmd.getColumnName(i));
			}

			while (resultSet.next()) {
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					queryResult.add(resultSet.getString(i));
				}

			}
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (SQLException e) {
		}

		return queryResult;
	}

	public ArrayList<String> getTableData() {
		String tableName = "movies1";
		return executeQuery("SELECT name, length, language, origin FROM " + tableName + " WHERE year=2000");
	}

	public GreetingServiceImpl() {
		addTestMeasurements();
	}
	
	private static DateTimeFormat df = DateTimeFormat.getFormat("YYYY-MM-DD");
	
	public void addTestMeasurements() {
		TemperatureMeasurement meas;
		
		meas = new TemperatureMeasurement();
		meas.setFieldDate("1856-03-01");
		meas.setFieldAverageTemperature(Double.parseDouble("27.297"));
		meas.setFieldAverageTemperatureUncertainty(Double.parseDouble("27.297"));		
		meas.setFieldCity("Abidjan");
		meas.setFieldCountry("Côte D'Ivoire");
		meas.setFieldLatitude("5.63N");
		meas.setFieldLongitude("3.23W");
		addMeasurement(meas);
		meas.setFieldDate("1857-03-01");
		meas.setFieldAverageTemperature(Double.parseDouble("26.297"));
		addMeasurement(meas);
		meas.setFieldDate("1858-03-01");
		meas.setFieldAverageTemperature(Double.parseDouble("26.89"));
		addMeasurement(meas);
		meas.setFieldDate("1859-03-01");
		meas.setFieldAverageTemperature(Double.parseDouble("22.277"));
		addMeasurement(meas);
		meas.setFieldDate("1860-03-01");
		meas.setFieldAverageTemperature(Double.parseDouble("19.297"));
		addMeasurement(meas);
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
				//measurement.setFieldDate((String) entity.getProperty("fd"));
				measurement.setFieldAverageTemperature((Double) entity.getProperty("avgTmp"));
				measurement.setFieldAverageTemperatureUncertainty((Double) entity.getProperty("avgTmpUn"));
				measurement.setFieldCity((String) entity.getProperty("City"));
				measurement.setFieldCountry((String) entity.getProperty("Country"));
				
				System.out.println("Country: " + entity.getProperty("Country"));
				measurement.setFieldLatitude((String) entity.getProperty("Lat"));
				measurement.setFieldLongitude((String) entity.getProperty("Long"));
				
				measurementlist.add(measurement);
			}
		} catch (Exception e) {
			throw new InvocationException("Exception connecting to the database", e);
		}

		System.out.println("received " + measurementlist.size());

		return measurementlist;
	}

	public void addMeasurement(TemperatureMeasurement meas) {
		try {
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
			
			Entity entity = new Entity("TptMeas");
			//entity.setProperty("fd", meas.getFieldDate());
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
}
