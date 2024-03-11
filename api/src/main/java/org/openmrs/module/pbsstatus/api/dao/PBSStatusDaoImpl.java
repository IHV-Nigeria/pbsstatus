/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.pbsstatus.api.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;
import org.hibernate.SQLQuery;
import org.openmrs.api.db.hibernate.DbSessionFactory;
import org.openmrs.module.pbsstatus.ResponseData;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@Repository("pbsstatus.PBSStatusDao")
public class PBSStatusDaoImpl implements PbsStatusDao {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	DbSessionFactory sessionFactory;
	
	public List<Map<String, String>> getAllPatients() {
		String SQLqueryString = "select pi.patient_id, pi.identifier, pn.given_name, pn.family_name from patient_identifier pi"
		        + " right join person_name pn on pn.person_id=pi.patient_id "
		        + " where pi.identifier_type=4 AND pn.voided = false ";
		
		SQLQuery query = getSessionFactory().getCurrentSession().createSQLQuery(SQLqueryString);
		
		List<Object[]> allActivePatients = query.list();
		
		List<Map<String, String>> patientList = new ArrayList<Map<String, String>>();
		
		for (Object[] row : allActivePatients) {
			Map<String, String> tempMap = new HashMap<String, String>();
			Integer patientId = (Integer) row[0];
			tempMap.put("patient_id", patientId.toString());
			tempMap.put("pepfarId", (String) row[1]);
			tempMap.put("given_name", (String) row[2]);
			tempMap.put("family_name", (String) row[3]);
			patientList.add(tempMap);
		}
		return patientList;
	}
	
	public List<Map<String, String>> getPatientBaselinePBS(String pepfarId, int patient_id) {
		String SQLqueryString = null;
		SQLQuery query;
		
		if (pepfarId == null || pepfarId.equals("")) {
			SQLqueryString = "select bi.patient_id, bi.template, bi.imageWidth, bi.imageHeight, bi.imageDPI, bi.imageQuality, bi.fingerPosition, bi.date_created"
			        + "  from biometricinfo bi where bi.patient_id = :patientId";
			query = getSessionFactory().getCurrentSession().createSQLQuery(SQLqueryString);
			
			query.setParameter("patientId", patient_id);
			
		} else {
			
			SQLqueryString = "select bi.patient_id, bi.template, bi.imageWidth, bi.imageHeight, bi.imageDPI, bi.imageQuality, bi.fingerPosition, bi.date_created"
			        + "  from biometricinfo bi where bi.patient_id = (select pi.patient_id from patient_identifier pi where pi.identifier_type=4 AND pi.identifier = :pepfarid AND pi.voided = false LIMIT 1)";
			query = getSessionFactory().getCurrentSession().createSQLQuery(SQLqueryString);
			
			query.setParameter("pepfarid", pepfarId);
		}
		
		List<Object[]> patientsBaseline = query.list();
		
		List<Map<String, String>> patientBaselinePBS = new ArrayList<Map<String, String>>();
		
		for (Object[] row : patientsBaseline) {
			Map<String, String> tempMap = new HashMap<String, String>();
			Integer patientId = (Integer) row[0];
			
			Integer imagewidth = (Integer) row[2];
			Integer imageheight = (Integer) row[3];
			Integer imagedpi = (Integer) row[4];
			Integer imagequality = (Integer) row[5];
			
			tempMap.put("patient_id", patientId.toString());
			tempMap.put("template", (String) row[1]);
			tempMap.put("imageWidth", imagewidth.toString());
			tempMap.put("imageHeight", imageheight.toString());
			tempMap.put("imageDPI", imagedpi.toString());
			tempMap.put("imageQuality", imagequality.toString());
			tempMap.put("fingerPosition", (String) row[6]);
			tempMap.put("date_created", row[7].toString());
			patientBaselinePBS.add(tempMap);
		}
		
		return patientBaselinePBS;
		
	}
	
	public List<Map<String, String>> getLastRecapture(String pepfarId, int patient_id) {
		
		String SQLqueryString = null;
		SQLQuery query;
		
		if (pepfarId == null || pepfarId.equals("")) {
			SQLqueryString = "select bi.patient_id, bi.template, bi.imageWidth, bi.imageHeight, bi.imageDPI, bi.imageQuality, bi.fingerPosition, bi.date_created, bi.recapture_count"
			        + "  from biometricverificationinfo bi where bi.patient_id = :patientId";
			query = getSessionFactory().getCurrentSession().createSQLQuery(SQLqueryString);
			
			query.setParameter("patientId", patient_id);
			
		} else {
			
			SQLqueryString = "select bi.patient_id, bi.template, bi.imageWidth, bi.imageHeight, bi.imageDPI, bi.imageQuality, bi.fingerPosition, bi.date_created, bi.recapture_count"
			        + "  from biometricverificationinfo bi where bi.patient_id = (select pi.patient_id from patient_identifier pi where pi.identifier_type=4 AND pi.identifier = :pepfarid AND pi.voided = false LIMIT 1)";
			query = getSessionFactory().getCurrentSession().createSQLQuery(SQLqueryString);
			
			query.setParameter("pepfarid", pepfarId);
		}
		
		List<Object[]> patientsBaseline = query.list();
		
		List<Map<String, String>> lastRecapture = new ArrayList<Map<String, String>>();
		
		for (Object[] row : patientsBaseline) {
			Map<String, String> tempMap = new HashMap<String, String>();
			Integer patientId = (Integer) row[0];
			Integer recaptureCount = (Integer) row[8];
			
			tempMap.put("patient_id", patientId.toString());
			
			Integer imagewidth = (Integer) row[2];
			Integer imageheight = (Integer) row[3];
			Integer imagedpi = (Integer) row[4];
			Integer imagequality = (Integer) row[5];
			
			tempMap.put("template", (String) row[1]);
			tempMap.put("imageWidth", imagewidth.toString());
			tempMap.put("imageHeight", imageheight.toString());
			tempMap.put("imageDPI", imagedpi.toString());
			tempMap.put("imageQuality", imagequality.toString());
			tempMap.put("fingerPosition", (String) row[6]);
			tempMap.put("date_created", row[7].toString());
			tempMap.put("recapture_count", recaptureCount.toString());
			
			lastRecapture.add(tempMap);
		}
		
		return lastRecapture;
		
	}
	
	public String getNDRStatus(String pepfarId, String facilityDatimCode) throws Exception {
		// Construct URL
		String url = "http://41.223.44.116:90/tbqual/api/ndrmatch_status/" + pepfarId + "/" + facilityDatimCode;
		
		try {
			// Open connection
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			
			// Set request method and headers
			con.setRequestMethod("GET");
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("User-Agent", "Mozilla/5.0");
			
			// Get response code
			int responseCode = con.getResponseCode();
			System.out.println("\nSending 'GET' request to URL : " + url);
			System.out.println("Response Code : " + responseCode);
			
			// Read response
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			
			System.out.println("The Response: " + response);
			
			return response.toString();
		}
		catch (UnknownHostException e) {
			
			// Handle no internet connection error
			System.out.println("Error: No internet connection. " + e.getMessage());
			// throw e; // Re-throw the exception if needed for further handling
			
			return getLocalNDRStatus(pepfarId);
		}
		catch (Exception e) {
			// Handle other exceptions
			System.out.println("Error: " + e.getMessage());
			//	throw e; // Re-throw the exception if needed for further handling
			return getLocalNDRStatus(pepfarId);
		}
	}
	
	public String getNDRStatusforFacility(String pepfarId, String facilityDatimCode) throws Exception {
		// Construct URL
		String url = "http://41.223.44.116:90/tbqual/api/ndrmatch_status/" + pepfarId + "/" + facilityDatimCode;
		
		try {
			// Open connection
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			
			// Set request method and headers
			con.setRequestMethod("GET");
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("User-Agent", "Mozilla/5.0");
			
			// Get response code
			int responseCode = con.getResponseCode();
			
			System.out.println("\nSending 'GET' request to URL : " + url);
			System.out.println("Response Code : " + responseCode);
			
			// Read response
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			
			System.out.println("The NDR Status saved to Database");
			
			saveResponseToDatabase(response.toString());
			
			return "Successful";
			
		}
		catch (UnknownHostException e) {
			
			// Handle no internet connection error
			System.out.println("Error: No internet connection. " + e.getMessage());
			// throw e; // Re-throw the exception if needed for further handling
			return "Unsuccessful";
		}
		catch (Exception e) {
			// Handle other exceptions
			System.out.println("Error: " + e.getMessage());
			// throw e; // Re-throw the exception if needed for further handling
			return "Unsuccessful";
		}
	}
	
	private final ObjectMapper objectMapper = new ObjectMapper();
	
	public void saveResponseToDatabase(String response) {
		try {
			// Construct JavaType for List<ResponseData>
			JavaType listType = objectMapper.getTypeFactory().constructCollectionType(List.class, ResponseData.class);
			
			// Parse JSON response into a list of objects
			List<ResponseData> responseDataList = objectMapper.readValue(response, listType);
			
			// Get Hibernate session and create SQL query
			SQLQuery query = getSessionFactory()
			        .getCurrentSession()
			        .createSQLQuery(
			            "INSERT INTO pbs_ndr_status (facility_datim_code, pepfar_id, match_outcome, recapture_date, baseline_replaced) VALUES (:facilityDatimCode, :pepfarId, :matchOutcome, :recaptureDate, :baselineReplaced)");
			
			// Iterate over the list and execute INSERT statements
			for (ResponseData responseData : responseDataList) {
				query.setParameter("facilityDatimCode", responseData.getFacilityDatimCode());
				query.setParameter("pepfarId", responseData.getPepfarId());
				query.setParameter("matchOutcome", responseData.getMatchOutcome());
				query.setParameter("recaptureDate", responseData.getRecaptureDate());
				query.setParameter("baselineReplaced", responseData.getBaselineReplaced());
				query.executeUpdate();
			}
			
			System.out.println("Response saved to database successfully!");
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("Failed to save response to database.");
		}
	}
	
	public String getLocalNDRStatus(String pepfarId) {
		String SQLqueryString = "select match_outcome,recapture_date,baseline_replaced,otherinfo from pbs_ndr_status where pepfar_id=:pepfarID";
		
		SQLQuery query = getSessionFactory().getCurrentSession().createSQLQuery(SQLqueryString);
		query.setParameter("pepfarID", pepfarId);
		
		List<Object[]> clientLocalNDRStatus = query.list();
		
		List<Map<String, String>> clientNDRStatus = new ArrayList<Map<String, String>>();
		
		for (Object[] row : clientLocalNDRStatus) {
			Map<String, String> tempMap = new HashMap<String, String>();
			Integer patientId = (Integer) row[0];
			tempMap.put("match_outcome", patientId.toString());
			tempMap.put("recapture_date", (String) row[1]);
			tempMap.put("baseline_replaced", (String) row[2]);
			tempMap.put("otherinfo", (String) row[3]);
			clientNDRStatus.add(tempMap);
		}
		return clientNDRStatus.toString();
	}
	
	public String getPriorityList() {
		String SQLqueryString = "select match_outcome,recapture_date,baseline_replaced,otherinfo from pbs_ndr_status where match_outcome !='Match'";
		
		SQLQuery query = getSessionFactory().getCurrentSession().createSQLQuery(SQLqueryString);
		
		List<Object[]> clientLocalNDRStatus = query.list();
		
		List<Map<String, String>> clientNDRStatus = new ArrayList<Map<String, String>>();
		
		for (Object[] row : clientLocalNDRStatus) {
			Map<String, String> tempMap = new HashMap<String, String>();
			Integer patientId = (Integer) row[0];
			tempMap.put("match_outcome", patientId.toString());
			tempMap.put("recapture_date", (String) row[1]);
			tempMap.put("baseline_replaced", (String) row[2]);
			tempMap.put("otherinfo", (String) row[3]);
			clientNDRStatus.add(tempMap);
		}
		return clientNDRStatus.toString();
	}
	
	// Connection Methods below
	@Override
	public List<Map<String, String>> getCurrentRecapture(String pepfarId, int patient_id) {
		
		String SQLqueryString = null;
		SQLQuery query;
		
		if (pepfarId == null || pepfarId.equals("")) {
			SQLqueryString = "select bi.group_id, bi.patient_id, bi.date_created, bi.match_status, bi.finger_position, bi.br_status, bi.brstatus_recapturecount "
			        + "  from biometricverificationinfo_match_trail bi where bi.patient_id = :patientId AND bi.group_id = (SELECT MAX(g.group_id) FROM biometricverificationinfo_match_trail g WHERE g.patient_id=bi.patient_id)";
			query = getSessionFactory().getCurrentSession().createSQLQuery(SQLqueryString);
			
			query.setParameter("patientId", patient_id);
			
		} else {
			
			SQLqueryString = "select bi.group_id, bi.patient_id, bi.date_created, bi.match_status, bi.finger_position, bi.brstatus, bi.brstatus_recapturecount "
			        + "  from biometricverificationinfo_match_trail bi where bi.patient_id = (select pi.patient_id from patient_identifier pi where pi.identifier_type=4 AND pi.identifier = :pepfarid AND pi.voided = false LIMIT 1) AND bi.group_id = (SELECT MAX(g.group_id) FROM biometricverificationinfo_match_trail g WHERE g.patient_id=bi.patient_id)";
			query = getSessionFactory().getCurrentSession().createSQLQuery(SQLqueryString);
			
			query.setParameter("pepfarid", pepfarId);
		}
		
		List<Object[]> patientsBaseline = query.list();
		
		List<Map<String, String>> instantRecapture = new ArrayList<Map<String, String>>();
		
		for (Object[] row : patientsBaseline) {
			
			Integer patientId = (Integer) row[1];
			Integer recaptureCount = (Integer) row[5];
			
			Map<String, String> tempMap = new HashMap<String, String>();
			tempMap.put("group_id", (String) row[0]);
			tempMap.put("patient_id", patientId.toString());
			tempMap.put("date_created", row[2].toString());
			tempMap.put("match_status", (String) row[3]);
			tempMap.put("finger_position", (String) row[4]);
			tempMap.put("br_status", recaptureCount.toString());
			
			instantRecapture.add(tempMap);
		}
		
		return instantRecapture;
		
	}
	
	public DbSessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	public void setSessionFactory(DbSessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
}
