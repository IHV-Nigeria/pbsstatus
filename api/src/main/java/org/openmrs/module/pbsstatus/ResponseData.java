package org.openmrs.module.pbsstatus;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

// @JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseData {
	
	private String facility_datim_code;
	
	private String pepfar_id;
	
	private String match_outcome;
	
	private String recapture_date;
	
	private String baseline_replaced;
	
	private String otherinfo;
	
	private String comment;
	
	// Constructors, getters, and setters
	public ResponseData() {
		// Default constructor
	}
	
	public ResponseData(String facility_datim_code, String pepfar_id, String match_outcome, String recapture_date,
	    String baseline_replaced, String otherinfo, String comment) {
		this.facility_datim_code = facility_datim_code;
		this.pepfar_id = pepfar_id;
		this.match_outcome = match_outcome;
		this.recapture_date = recapture_date;
		this.baseline_replaced = baseline_replaced;
		this.otherinfo = otherinfo;
		this.comment = comment;
	}
	
	public String getFacility_datim_code() {
		return facility_datim_code;
	}
	
	public void setFacility_datim_code(String facility_datim_code) {
		this.facility_datim_code = facility_datim_code;
	}
	
	public String getPepfar_id() {
		return pepfar_id;
	}
	
	public void setPepfar_id(String pepfar_id) {
		this.pepfar_id = pepfar_id;
	}
	
	public String getMatch_outcome() {
		return match_outcome;
	}
	
	public void setMatch_outcome(String match_outcome) {
		this.match_outcome = match_outcome;
	}
	
	public String getRecapture_date() {
		return recapture_date;
	}
	
	public void setRecapture_date(String recapture_date) {
		this.recapture_date = recapture_date;
	}
	
	public String getBaseline_replaced() {
		return baseline_replaced;
	}
	
	public void setBaseline_replaced(String baseline_replaced) {
		this.baseline_replaced = baseline_replaced;
	}
	
	public void setOtherinfo(String otherinfo) {
		this.otherinfo = otherinfo;
	}
	
	public String getOtherinfo() {
		return otherinfo;
	}
	
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public String getComment() {
		return comment;
	}
}
