package se.climateapp.shared;

import java.util.List;

// http://stackoverflow.com/questions/12808866/storing-the-cursor-for-app-engine-pagination
// wrapper class wrapper class that is transportable so you can pass back it to the client via 
// RequestFactory that can hold the results list and the cursor string.
// To do that you create a normal POJO and then a proxy for it.

public class MeasurementsQueryResult {
    public List<TemperatureMeasurement> list;
    public String webSafeCursorString;
    public int totalQueryResultCount;

    public int getTotalQueryResultCount() {
		return totalQueryResultCount;
	}

	public void setTotalQueryResultCount(int totalQueryResultCount) {
		this.totalQueryResultCount = totalQueryResultCount;
	}

	public List<TemperatureMeasurement> getList() {
        return list;
    }

    public void setList(List<TemperatureMeasurement> list) {
        this.list = list;
    }

    public String getWebSafeCursorString() {
        return this.webSafeCursorString;
    }

    public void setWebSafeCursorString(String webSafeCursorString) {
        this.webSafeCursorString = webSafeCursorString;
    }
  
}
