package org.openmrs.module.pbsstatus.api;

import java.util.List;
import java.util.Map;

public interface PbsStatusService {
	
	List<Map<String, String>> getAllPatients();
	
	List<Map<String, String>> getPatientBaselinePBS(String pepfarId, int patient_id);
	
	List<Map<String, String>> getLastRecapture(String pepfarId, int patient_id);
	
	List<Map<String, String>> getCurrentRecapture(String pepfarId, int i);
	
	String getNDRStatus(String pepfarId, String facilityDatimCode) throws Exception;
	
	String getNDRStatusforFacility(String na, String facilityDatimCode);
	
	List<Map<String, String>> getPriorityList();
}
