package org.openmrs.module.pbsstatus.utilities;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Tony
 */
public class DateFormatter {
	
	/**
	 * @param myDate
	 * @return
	 * @throws java.text.ParseException
	 */
	public static String formatDate(String myDate) throws java.text.ParseException {
		
		Date newDate = new SimpleDateFormat("yyyy-MM-dd").parse(myDate);
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM, yyyy");
		String strDate = formatter.format(newDate);
		
		return strDate;
	}
	
	public static boolean isNumeric(String strNum) {
		if (strNum == null) {
			return false;
		}
		try {}
		catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}
	
}
