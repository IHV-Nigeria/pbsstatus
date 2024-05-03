package org.openmrs.module.pbsstatus.fragment.controller;

import org.openmrs.api.context.Context;
import org.openmrs.module.pbsstatus.api.PbsStatusService;
import org.openmrs.module.pbsstatus.utilities.DateFormatter;
import org.openmrs.ui.framework.fragment.FragmentModel;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;

public class PbsstatusFragmentController {
	
	PbsStatusService pbsStatusService = Context.getService(PbsStatusService.class);
	
	public void controller(FragmentModel model) throws Exception {
		
		Calendar now = Calendar.getInstance();
		String today = now.get(Calendar.YEAR) + "-" + (now.get(Calendar.MONTH) + 1) + "-" + now.get(Calendar.DATE);
		
		String lastNDRUpdate = Context.getAdministrationService().getGlobalProperty("last_ndr_update");
		if (lastNDRUpdate == null) {
			lastNDRUpdate = today;
			//			Context.getAdministrationService().setGlobalProperty("last_ndr_update", today);
		}
		String facilityDatimCode = Context.getAdministrationService().getGlobalProperty("facility_datim_code");
		String updateLocalDB = pbsStatusService.getNDRStatusforFacility("na", facilityDatimCode);
		System.out.println("Update of Local Database " + updateLocalDB);
		
		model.addAttribute("lastNDRUpdate", lastNDRUpdate);
		model.addAttribute("patients", pbsStatusService.getAllPatients());
		model.addAttribute("dformatter", DateFormatter.class);
	}
	
	public int searchPatient(HttpServletRequest request) {
		
		try {
			
			String pepfarId = request.getParameter("pepfarId");
			
			return pbsStatusService.getPatientIdByPepfarId(pepfarId);
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return 0;
		}
	}
	
}
