package org.openmrs.module.pbsstatus.fragment.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.pbsstatus.api.PbsStatusService;
import org.openmrs.module.pbsstatus.utilities.DateFormatter;
import org.openmrs.ui.framework.fragment.FragmentModel;

import java.util.List;
import java.util.Map;

public class PrioritylistFragmentController {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	PbsStatusService pbsStatusService = Context.getService(PbsStatusService.class);
	
	public void controller(FragmentModel model) throws Exception {
		
		List<Map<String, String>> prioritylist;
		prioritylist = pbsStatusService.getPriorityList();
		model.addAttribute("prioritylist", prioritylist);
		
	}
}
