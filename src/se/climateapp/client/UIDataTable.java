package se.climateapp.client;

import java.util.Date;
import java.util.List;

import com.google.gwt.cell.client.DateCell;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ListDataProvider;

import se.climateapp.shared.TemperatureMeasurement;

public class UIDataTable {
	private CellTable<TemperatureMeasurement> table = new CellTable<TemperatureMeasurement>();
remoteService
	//ListDataProvider<TemperatureMeasurement> dataProvider = new ListDataProvider<TemperatureMeasurement>();
	
	// Associate an async data provider to the table
    AsyncDataProvider<TemperatureMeasurement> provider = new AsyncDataProvider<TemperatureMeasurement>() {
      @Override
      protected void onRangeChanged(HasData<TemperatureMeasurement> display) {
        final int start = display.getVisibleRange().getStart();
        int length = display.getVisibleRange().getLength();
        AsyncCallback<List<TemperatureMeasurement>> callback = new AsyncCallback<List<TemperatureMeasurement>>() {
          @Override
          public void onFailure(Throwable caught) {
            Window.alert(caught.getMessage());
          }
          @Override
          public void onSuccess(List<TemperatureMeasurement> result) {
            updateRowData(start, result);
          }
        };
        // The remote service that should be implemented
        remoteService.fetchPage(start, length, callback);
      }
    };
	
	
	public Widget initialize() {
	    // Add the date column.
	    DateCell dateCell = new DateCell();
	    Column<TemperatureMeasurement, Date> dateColumn = new Column<TemperatureMeasurement, Date>(dateCell) {
	      @Override
	      public Date getValue(TemperatureMeasurement object) {
	        return object.getFieldDate();
	      }
	    };
	    table.addColumn(dateColumn, "Date");
	    dateColumn.setSortable(true);
	    
	    // Add a text column to show the country.
	    TextColumn<TemperatureMeasurement> countryColumn = new TextColumn<TemperatureMeasurement>() {
	      @Override
	      public String getValue(TemperatureMeasurement object) {
	        return object.getFieldCountry();
	      }
	    };
	    table.addColumn(countryColumn, "Country");
		
	    // Add a text column to show the city.
	    TextColumn<TemperatureMeasurement> cityColumn = new TextColumn<TemperatureMeasurement>() {
	      @Override
	      public String getValue(TemperatureMeasurement object) {
	        return object.getFieldCity();
	      }
	    };
	    table.addColumn(cityColumn, "City");	    

	    // Add a text column to show the latitude.
	    TextColumn<TemperatureMeasurement> latitudeColumn = new TextColumn<TemperatureMeasurement>() {
	      @Override
	      public String getValue(TemperatureMeasurement object) {
	        return object.getFieldLatitude();
	      }
	    };
	    table.addColumn(latitudeColumn, "Latitude");
	    
	    // Add a text column to show the longitude.
	    TextColumn<TemperatureMeasurement> longitudeColumn = new TextColumn<TemperatureMeasurement>() {
	      @Override
	      public String getValue(TemperatureMeasurement object) {
	        return object.getFieldLongitude();
	      }
	    };
	    table.addColumn(longitudeColumn, "Longitude");
	    
	    
	    dataProvider.addDataDisplay(table);

	    SimplePager pager = new SimplePager();
	    pager.setDisplay(table);
	    pager.setPageSize(100); //rows that will be shown at a time

	    VerticalPanel vPanel = new VerticalPanel();
	    vPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
	    vPanel.add(table);
	    vPanel.add(pager);
	    
		return vPanel;
	}
	
	public void updateTable(List<TemperatureMeasurement> measurements) {
	   dataProvider.setList(measurements);
	   
		/*
		// Set the total row count. This isn't strictly necessary, but it affects
	    // paging calculations, so its good habit to keep the row count up to date.
	    table.setRowCount(measurements.size(), true);
	    
		// Push the data into the widget.
	    table.setRowData(0, measurements);
	    */
	    
	}
}
