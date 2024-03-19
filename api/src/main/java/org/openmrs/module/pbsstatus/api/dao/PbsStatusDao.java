package org.openmrs.module.pbsstatus.api.dao;

import java.util.List;
import java.util.Map;

public interface PbsStatusDao {
	
	List<Map<String, String>> getAllPatients();
	
	List<Map<String, String>> getPatientBaselinePBS(String pepfarId, int patientId);
	
	List<Map<String, String>> getLastRecapture(String pepfarId, int patientId);
	
	List<Map<String, String>> getCurrentRecapture(String pepfarId, int patientId);
	
	String getNDRStatus(String pepfarId, String facilityDatimCode) throws Exception;
	
	String getNDRStatusforFacility(String pepfarId, String facilityDatimCode) throws Exception;
	
	List<Map<String, Object>> getPriorityList();
	
	void saveComment(String comment, String pepfarId) throws Exception;
}
