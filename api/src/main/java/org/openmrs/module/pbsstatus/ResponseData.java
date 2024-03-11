package org.openmrs.module.pbsstatus;

public class ResponseData {
	
	private String facilityDatimCode;
	
	private String pepfarId;
	
	private String matchOutcome;
	
	private String recaptureDate;
	
	private String baselineReplaced;
	
	// Constructors, getters, and setters
	public ResponseData() {
		// Default constructor
	}
	
	public ResponseData(String facilityDatimCode, String pepfarId, String matchOutcome, String recaptureDate,
	    String baselineReplaced) {
		this.facilityDatimCode = facilityDatimCode;
		this.pepfarId = pepfarId;
		this.matchOutcome = matchOutcome;
		this.recaptureDate = recaptureDate;
		this.baselineReplaced = baselineReplaced;
	}
	
	public String getFacilityDatimCode() {
		return facilityDatimCode;
	}
	
	public void setFacilityDatimCode(String facilityDatimCode) {
		this.facilityDatimCode = facilityDatimCode;
	}
	
	public String getPepfarId() {
		return pepfarId;
	}
	
	public void setPepfarId(String pepfarId) {
		this.pepfarId = pepfarId;
	}
	
	public String getMatchOutcome() {
		return matchOutcome;
	}
	
	public void setMatchOutcome(String matchOutcome) {
		this.matchOutcome = matchOutcome;
	}
	
	public String getRecaptureDate() {
		return recaptureDate;
	}
	
	public void setRecaptureDate(String recaptureDate) {
		this.recaptureDate = recaptureDate;
	}
	
	public String getBaselineReplaced() {
		return baselineReplaced;
	}
	
	public void setBaselineReplaced(String baselineReplaced) {
		this.baselineReplaced = baselineReplaced;
	}
}
