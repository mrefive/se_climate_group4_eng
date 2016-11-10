package se.climateapp.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

public class CSVFileLoad extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7001425738146659463L;

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		boolean isMultipart = ServletFileUpload.isMultipartContent(request);

		System.out.println("File Upload request");

		if (!isMultipart) {
			System.out.println("Not Multipart!");
			return;
		}

		clearDataStore();

		ServletFileUpload upload = new ServletFileUpload();
		try {
			FileItemIterator iter = upload.getItemIterator(request);

			while (iter.hasNext()) {
				FileItemStream item = iter.next();
				String fileName = item.getName();
				System.out.println("Filename: " + fileName);

				try {
					InputStream stream = item.openStream();
					InputStreamReader streamreader = new InputStreamReader(stream, "UTF-8");
					BufferedReader br = new BufferedReader(streamreader);
					DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
					//Transaction txn = datastore.beginTransaction();
					try {
						String line;
						String separator = ",";
						DateFormat df = new SimpleDateFormat("yyyy-mm-dd");

						line = br.readLine(); // skip first line
						line = br.readLine();
						int index = 0;

						// load data in batches
						final int batchSize = 500;
						while (line != null) {
							int batchIndex = 0;
							List<Entity> batchList = new ArrayList<Entity>();

							while (line != null && batchIndex < batchSize) {
								String[] data = line.split(separator);
								if (data.length != 7) {
									line = null;
									break;
								}

								Entity entity = new Entity("TptMeas");
								entity.setProperty("index", index++);
								entity.setProperty("fd", df.parse(data[0]));
								entity.setProperty("avgTmp", Double.parseDouble(data[1]));
								entity.setProperty("avgTmpUn", Double.parseDouble(data[2]));
								entity.setProperty("City", data[3]);
								entity.setProperty("Country", data[4]);
								entity.setProperty("Lat", data[5]);
								entity.setProperty("Long", data[6]);
								batchList.add(entity);
								batchIndex++;
								line = br.readLine();
							}
							datastore.put(batchList);
							//datastore.put(txn, batchList);
						}
						//txn.commit();
					} finally {
						//if (txn.isActive()) {
						//	txn.rollback();
						//}
					}

				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	private void clearDataStore() {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		// Use class Query to assemble a query
		Query q = new Query("TptMeas");

		// Use PreparedQuery interface to retrieve results
		PreparedQuery pq = datastore.prepare(q);

		for (Entity result : pq.asIterable()) {
			datastore.delete(result.getKey());
		}
	}
}
