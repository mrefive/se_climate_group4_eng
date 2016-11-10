package se.climateapp.shared;

import java.io.Serializable;
import java.util.List;

public class MeasurementsQueryResult implements Serializable  {
    /**
	 * 
	 */
	private static final long serialVersionUID = 3080871780030999895L;
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
