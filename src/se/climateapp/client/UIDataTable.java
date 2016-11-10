package se.climateapp.client;

import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.i18n.client.DateTimeFormat;
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

import se.climateapp.shared.MeasurementsQueryResult;
import se.climateapp.shared.TemperatureMeasurement;

public class UIDataTable {
	private CellTable<TemperatureMeasurement> table = new CellTable<TemperatureMeasurement>();
	public GreetingServiceAsync remoteService;
	private String webSafeCursorString = "";
	
	// ListDataProvider<TemperatureMeasurement> dataProvider = new
	// ListDataProvider<TemperatureMeasurement>();

	// Associate an async data provider to the table
	AsyncDataProvider<TemperatureMeasurement> dataProvider = new AsyncDataProvider<TemperatureMeasurement>() {
		@Override
		protected void onRangeChanged(HasData<TemperatureMeasurement> display) {
			final int start = display.getVisibleRange().getStart();
			int length = display.getVisibleRange().getLength();
			AsyncCallback<MeasurementsQueryResult> callback = new AsyncCallback<MeasurementsQueryResult>() {
				@Override
				public void onFailure(Throwable caught) {
					Window.alert(caught.getMessage());
				}

				@Override
				public void onSuccess(MeasurementsQueryResult result) {
					System.out.println("success " + start);
					updateRowCount(result.getTotalQueryResultCount(), true);
					updateRowData(start, result.getList());
					webSafeCursorString = result.getWebSafeCursorString();
				}
			};
			// The remote service that should be implemented
			if (remoteService != null) {
				remoteService.getMeasurements(webSafeCursorString, length, callback);
			}
		}
	};

	public Widget initialize() {
		// Add the date column.
		TextCell dateCell = new TextCell();
		final DateTimeFormat df = DateTimeFormat.getFormat("yyyy-mm-dd");
		Column<TemperatureMeasurement, String> dateColumn = new Column<TemperatureMeasurement, String>(dateCell) {
			@Override
			public String getValue(TemperatureMeasurement object) {
				return df.format(object.getFieldDate());
			}
		};
		table.addColumn(dateColumn, "Date");
		dateColumn.setSortable(true);

		// Add a number column to show the temperature.
		Column<TemperatureMeasurement, Number> avgTempColumn = new Column<TemperatureMeasurement, Number>(new NumberCell()) {
			@Override
			public Double getValue(TemperatureMeasurement object) {
				return object.getFieldAverageTemperature();
			}
		};
		table.addColumn(avgTempColumn, "avg. \nTemperature");
		avgTempColumn.setSortable(true);

		// Add a number column to show the temperature uncertainty.
		Column<TemperatureMeasurement, Number> avgTempUncertaintyColumn = new Column<TemperatureMeasurement, Number>(new NumberCell()) {
			@Override
			public Double getValue(TemperatureMeasurement object) {
				return object.getFieldAverageTemperatureUncertainty();
			}
		};
		table.addColumn(avgTempUncertaintyColumn, "avg. Temp. Uncertainty");
		avgTempUncertaintyColumn.setSortable(true);

		// Add a text column to show the country.
		TextColumn<TemperatureMeasurement> countryColumn = new TextColumn<TemperatureMeasurement>() {
			@Override
			public String getValue(TemperatureMeasurement object) {
				return object.getFieldCountry();
			}
		};
		table.addColumn(countryColumn, "Country");
		countryColumn.setSortable(true);

		// Add a text column to show the city.
		TextColumn<TemperatureMeasurement> cityColumn = new TextColumn<TemperatureMeasurement>() {
			@Override
			public String getValue(TemperatureMeasurement object) {
				return object.getFieldCity();
			}
		};
		table.addColumn(cityColumn, "City");
		cityColumn.setSortable(true);

		// Add a text column to show the latitude.
		TextColumn<TemperatureMeasurement> latitudeColumn = new TextColumn<TemperatureMeasurement>() {
			@Override
			public String getValue(TemperatureMeasurement object) {
				return object.getFieldLatitude();
			}
		};
		table.addColumn(latitudeColumn, "Latitude");
		latitudeColumn.setSortable(true);

		// Add a text column to show the longitude.
		TextColumn<TemperatureMeasurement> longitudeColumn = new TextColumn<TemperatureMeasurement>() {
			@Override
			public String getValue(TemperatureMeasurement object) {
				return object.getFieldLongitude();
			}
		};
		table.addColumn(longitudeColumn, "Longitude");
		longitudeColumn.setSortable(true);

		dataProvider.addDataDisplay(table);

		SimplePager pager = new SimplePager();
		pager.setDisplay(table);
		pager.setPageSize(25); // rows that will be shown at a time

		VerticalPanel vPanel = new VerticalPanel();
		vPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vPanel.add(table);
		vPanel.add(pager);

		return vPanel;
	}

	public void updateTable(MeasurementsQueryResult measurements) {
		dataProvider.updateRowCount(measurements.getTotalQueryResultCount(), true);
		dataProvider.updateRowData(0, measurements.getList());
		// dataProvider.setList(measurements.getList());

		/*
		 * // Set the total row count. This isn't strictly necessary, but it
		 * affects // paging calculations, so its good habit to keep the row
		 * count up to date. table.setRowCount(measurements.size(), true);
		 * 
		 * // Push the data into the widget. table.setRowData(0, measurements);
		 */

	}
}
