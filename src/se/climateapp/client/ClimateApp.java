package se.climateapp.client;

import java.util.ArrayList;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import se.climateapp.shared.TemperatureMeasurement;

import com.google.gwt.user.client.ui.FlexTable;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ClimateApp implements EntryPoint {
	private FlexTable climateFlexTable = new FlexTable();

	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while " + "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	/**
	 * Create a remote service proxy to talk to the server-side Greeting
	 * service.
	 */
	private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);

	private void initializeTheTable() {
		// Create table for stock data.
		climateFlexTable.setText(0, 0, "Testing");
		climateFlexTable.setText(0, 1, "Column1");
		climateFlexTable.setText(0, 2, "Column2");
		climateFlexTable.setText(0, 3, "Column3");
		HTMLTable.RowFormatter rf = climateFlexTable.getRowFormatter();
		rf.addStyleName(0, "FlexTable-HeaderRow");
		
		// Associate the table with the HTML host page.
		RootPanel.get("climateTable").add(climateFlexTable);
	}

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		initializeTheTable();

		final Button sendButton = new Button("Populate Table");
		//final TextBox nameField = new TextBox();
		//nameField.setText("GWT User");
		final Label errorLabel = new Label();

		// We can add style names to widgets
		sendButton.addStyleName("sendButton");

		// Add the nameField and sendButton to the RootPanel
		// Use RootPanel.get() to get the entire body element
		//RootPanel.get("nameFieldContainer").add(nameField);
		RootPanel.get("sendButtonContainer").add(sendButton);
		RootPanel.get("errorLabelContainer").add(errorLabel);

		// Focus the cursor on the name field when the app loads
		//nameField.setFocus(true);
		//nameField.selectAll();

		// Create the popup dialog box
		final DialogBox dialogBox = new DialogBox();
		dialogBox.setText("Remote Procedure Call");
		dialogBox.setAnimationEnabled(true);
		final Button closeButton = new Button("Close");
		// We can set the id of a widget by accessing its Element
		closeButton.getElement().setId("closeButton");
		final Label textToServerLabel = new Label();
		final HTML serverResponseLabel = new HTML();
		VerticalPanel dialogVPanel = new VerticalPanel();
		dialogVPanel.addStyleName("dialogVPanel");
		//dialogVPanel.add(new HTML("<b>Sending name to the server:</b>"));
		dialogVPanel.add(textToServerLabel);
		dialogVPanel.add(new HTML("<br><b>Server replies:</b>"));
		dialogVPanel.add(serverResponseLabel);
		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		dialogVPanel.add(closeButton);
		dialogBox.setWidget(dialogVPanel);

		// Add a handler to close the DialogBox
		closeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				dialogBox.hide();
				sendButton.setEnabled(true);
				sendButton.setFocus(true);
			}
		});

		// Create a handler for the sendButton and nameField
		class MyHandler implements ClickHandler, KeyUpHandler {
			/**
			 * Fired when the user clicks on the sendButton.
			 */
			public void onClick(ClickEvent event) {
					getMeasurementsAsync();
			}

			/**
			 * Fired when the user types in the nameField.
			 */
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					getMeasurementsAsync();
				}
			}
		}

		// Add a handler to send the name to the server
		MyHandler handler = new MyHandler();
		sendButton.addClickHandler(handler);
		//nameField.addKeyUpHandler(handler);
	}

	private void getMeasurementsAsync() {
		greetingService.getMeasurements(new AsyncCallback<ArrayList<TemperatureMeasurement>>() {
			public void onFailure(Throwable caught) {
				System.out.println("Failure");
				Window.alert(caught.getMessage());
			}

			public void onSuccess(ArrayList<TemperatureMeasurement> result) {
				System.out.println("Success");
				fillTable(result);
			}

		});
	}

	private void fillTable(ArrayList<TemperatureMeasurement> data) {
		if (data == null)
			return;

		climateFlexTable.setText(0, 0, "Date");
		climateFlexTable.setText(0, 1, "Avg. Temperature");
		climateFlexTable.setText(0, 2, "Avg. Uncertainty");
		climateFlexTable.setText(0, 3, "City");
		climateFlexTable.setText(0, 4, "Country");
		climateFlexTable.setText(0, 5, "Latitude");
		climateFlexTable.setText(0, 6, "Longitude");
		
		for(int i = 0; i < data.size(); i++) {
			TemperatureMeasurement meas = data.get(i);
			climateFlexTable.setText(i+1, 0, meas.getFieldDate().toString());
			climateFlexTable.setText(i+1, 1, String.valueOf(meas.getFieldAverageTemperature()));
			climateFlexTable.setText(i+1, 2, String.valueOf(meas.getFieldAverageTemperatureUncertainty()));
			climateFlexTable.setText(i+1, 3, meas.getFieldCity());
			climateFlexTable.setText(i+1, 4, meas.getFieldCountry());
			climateFlexTable.setText(i+1, 5, meas.getFieldLatitude());
			climateFlexTable.setText(i+1, 6, meas.getFieldLongitude());
			i++;
		}
	}
}
