package org.openmrs.module.pbsstatus.fragment.controller;

import org.openmrs.api.context.Context;
import org.openmrs.module.pbsstatus.api.PbsStatusService;
import org.openmrs.module.pbsstatus.api.dao.PBSStatusDaoImpl;
import org.openmrs.module.pbsstatus.utilities.DateFormatter;
import org.openmrs.ui.framework.fragment.FragmentModel;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import com.google.gson.Gson;

public class PbsstatusFragmentController {
	
	//	PBSStatusDaoImpl pbsStatusDaoImpl = new PBSStatusDaoImpl();
	PbsStatusService pbsStatusService = Context.getService(PbsStatusService.class);
	
	public void controller(FragmentModel model) throws Exception {
		model.addAttribute("patients", pbsStatusService.getAllPatients());
		model.addAttribute("dformatter", DateFormatter.class);
	}
	
}
