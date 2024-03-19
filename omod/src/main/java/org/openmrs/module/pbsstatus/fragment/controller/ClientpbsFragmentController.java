package org.openmrs.module.pbsstatus.fragment.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.pbsstatus.api.PbsStatusService;
import org.openmrs.module.pbsstatus.utilities.DateFormatter;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.openmrs.ui.framework.page.PageModel;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class ClientpbsFragmentController {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	PbsStatusService pbsStatusService = Context.getService(PbsStatusService.class);
	
	public void get(PageModel model, @RequestParam(value = "pepfarId", required = false) String pepfarId,
	        @RequestParam(value = "patient_id", required = false) int patient_id) throws Exception {
		
		List<Map<String, String>> baseline;
		List<Map<String, String>> last_recapture;
		List<Map<String, String>> current_recapture;
		String ndrStatus = "";
		String facilityDatimCode = Context.getAdministrationService().getGlobalProperty("facility_datim_code");
		
		baseline = pbsStatusService.getPatientBaselinePBS(pepfarId, patient_id);
		last_recapture = pbsStatusService.getLastRecapture(pepfarId, patient_id);
		current_recapture = pbsStatusService.getCurrentRecapture(pepfarId, patient_id);
		ndrStatus = pbsStatusService.getNDRStatus(pepfarId, facilityDatimCode);
		
		model.addAttribute("baseline", baseline);
		model.addAttribute("last_recapture", last_recapture);
		model.addAttribute("current_recapture", current_recapture);
		model.addAttribute("pepfarId", pepfarId);
		model.addAttribute("ndrStatus", ndrStatus);
		model.addAttribute("dformatter", DateFormatter.class);
	}
	
	public String saveComment(HttpServletRequest request) {
		
		try {
			
			String comment = request.getParameter("comment");
			String pepfarId = request.getParameter("pepfarId");
			
			pbsStatusService.saveComment(comment, pepfarId);
			
			return "Observation saved successfully";
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return "Error saving observation";
		}
	}
	
}
