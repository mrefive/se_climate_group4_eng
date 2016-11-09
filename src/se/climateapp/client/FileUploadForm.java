package se.climateapp.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class FileUploadForm {
	public FormPanel initialize() {
		VerticalPanel panel = new VerticalPanel();
		// create a FormPanel
		final FormPanel form = new FormPanel();
		// create a file upload widget
		final FileUpload fileUpload = new FileUpload();
		fileUpload.setName("CSVFileUpload");
		// create labels
		Label selectLabel = new Label("Select a CSV file:");
		// create upload button
		Button uploadButton = new Button("Upload File");
		// pass action to the form to point to service handling file
		// receiving operation.
		form.setAction(GWT.getModuleBaseURL() + "fileupload");
		// set form to use the POST method, and multipart MIME encoding.
		form.setEncoding(FormPanel.ENCODING_MULTIPART);
		form.setMethod(FormPanel.METHOD_POST);

		// add a label
		panel.add(selectLabel);
		// add fileUpload widget
		panel.add(fileUpload);
		// add a button to upload the file
		panel.add(uploadButton);
		uploadButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				// get the filename to be uploaded
				String filename = fileUpload.getFilename();
				if (filename.length() == 0) {
					Window.alert("No File Specified!");
				} else {
					// submit the form

					form.submit();
				}
			}
		});

		form.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				// When the form submission is successfully completed, this
				// event is fired. Assuming the service returned a response
				// of type text/html, we can get the result text here
				Window.alert("Data successfully updated! Please reload the page.");
				// Window.alert(event.getResults());
			}
		});
		panel.setSpacing(10);

		// Add form to the root panel.
		form.add(panel);
		return form;
	}
}
