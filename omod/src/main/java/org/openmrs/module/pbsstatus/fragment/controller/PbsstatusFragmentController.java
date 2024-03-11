package org.openmrs.module.pbsstatus.fragment.controller;

import org.openmrs.api.context.Context;
import org.openmrs.module.pbsstatus.api.PbsStatusService;
import org.openmrs.module.pbsstatus.utilities.DateFormatter;
import org.openmrs.ui.framework.fragment.FragmentModel;

public class PbsstatusFragmentController {
	
	PbsStatusService pbsStatusService = Context.getService(PbsStatusService.class);
	
	public void controller(FragmentModel model) throws Exception {
		
		String facilityDatimCode = Context.getAdministrationService().getGlobalProperty("facility_datim_code");
		
		String updateLocalDB = pbsStatusService.getNDRStatusforFacility("na", facilityDatimCode);
		System.out.println("Update of Local Database " + updateLocalDB);
		
		model.addAttribute("patients", pbsStatusService.getAllPatients());
		model.addAttribute("dformatter", DateFormatter.class);
	}
	
}
