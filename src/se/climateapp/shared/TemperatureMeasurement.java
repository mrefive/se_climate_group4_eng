package se.climateapp.shared;

import java.io.Serializable;
import java.util.Date;

//import com.google.gwt.i18n.client.NumberFormat;

//import com.google.gwt.i18n.shared.DateTimeFormat;

public class TemperatureMeasurement implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4657652688518865427L;

	//private static DateTimeFormat df = DateTimeFormat.getFormat("YYYY-MM-DD");
	
	private Date fieldDate; 
	private double fieldAverageTemperature;
	private double fieldAverageTemperatureUncertainty;
	private String fieldCity;
	private String fieldCountry;
	private String fieldLatitude;
	private String fieldLongitude;		

	public TemperatureMeasurement() {
		fieldDate = new Date();
	}
	
	public Date getFieldDate() {
		return fieldDate;
	}
	public void setFieldDate(Date fieldDate) {
		this.fieldDate = fieldDate;
	}
	public void setFieldDate(String fieldDate) {
	//		Date parsedDate = df.parse(fieldDate); 
	//		this.fieldDate = parsedDate;
	}
	public double getFieldAverageTemperature() {
		return fieldAverageTemperature;
	}
	public void setFieldAverageTemperature(double fieldAverageTemperature) {
		this.fieldAverageTemperature = fieldAverageTemperature;
	}
	public double getFieldAverageTemperatureUncertainty() {
		return fieldAverageTemperatureUncertainty;
	}
	public void setFieldAverageTemperatureUncertainty(double fieldAverageTemperatureUncertainty) {
		this.fieldAverageTemperatureUncertainty = fieldAverageTemperatureUncertainty;
	}
	public String getFieldCity() {
		return fieldCity;
	}
	public void setFieldCity(String fieldCity) {
		this.fieldCity = fieldCity;
	}
	public String getFieldCountry() {
		return fieldCountry;
	}
	public void setFieldCountry(String fieldCountry) {
		this.fieldCountry = fieldCountry;
	}	
	public String getFieldLatitude() {
		return fieldLatitude;
	}
	public void setFieldLatitude(String fieldLatitude) {
		this.fieldLatitude = fieldLatitude;
	}
	public String getFieldLongitude() {
		return fieldLongitude;
	}
	public void setFieldLongitude(String fieldLongitude) {
		this.fieldLongitude = fieldLongitude;
	}
}
