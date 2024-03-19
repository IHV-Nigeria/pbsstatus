package org.openmrs.module.pbsstatus.api.impl;

import org.openmrs.module.pbsstatus.api.PbsStatusService;
import org.openmrs.module.pbsstatus.api.dao.PbsStatusDao;

import java.util.List;
import java.util.Map;

public class PbsStatusServiceImpl implements PbsStatusService {
	
	private PbsStatusDao pbsStatusDao;
	
	public void setDao(PbsStatusDao pbsStatusDao) {
		this.pbsStatusDao = pbsStatusDao;
	}
	
	@Override
	public List<Map<String, String>> getAllPatients() {
		return pbsStatusDao.getAllPatients();
	}
	
	@Override
	public List<Map<String, String>> getPatientBaselinePBS(String pepfarId, int patient_id) {
		return pbsStatusDao.getPatientBaselinePBS(pepfarId, patient_id);
	}
	
	@Override
	public List<Map<String, String>> getLastRecapture(String pepfarId, int patient_id) {
		return pbsStatusDao.getLastRecapture(pepfarId, patient_id);
	}
	
	@Override
	public List<Map<String, String>> getCurrentRecapture(String pepfarId, int patient_id) {
		return pbsStatusDao.getCurrentRecapture(pepfarId, patient_id);
	}
	
	@Override
	public String getNDRStatus(String pepfarId, String facilityDatimCode) throws Exception {
		return pbsStatusDao.getNDRStatus(pepfarId, facilityDatimCode);
	}
	
	@Override
	public String getNDRStatusforFacility(String pepfarId, String facilityDatimCode) throws Exception {
		
		return pbsStatusDao.getNDRStatusforFacility(pepfarId, facilityDatimCode);
	}
	
	@Override
	public List<Map<String, Object>> getPriorityList() {
		return pbsStatusDao.getPriorityList();
	}
	
	@Override
	public void saveComment(String comment, String pepfarId) throws Exception {
		pbsStatusDao.saveComment(comment, pepfarId);
	}
	
	@Override
	public String getPatientUuid(int patient_id) {
		return pbsStatusDao.getPatientUuid(patient_id);
	}
}
