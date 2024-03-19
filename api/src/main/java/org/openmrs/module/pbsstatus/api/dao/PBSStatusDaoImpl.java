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

import com.google.gson.Gson;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.api.db.hibernate.DbSessionFactory;
import org.openmrs.module.pbsstatus.ResponseData;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

//@Repository("pbsstatus.PBSStatusDao")
public class PBSStatusDaoImpl implements PbsStatusDao {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	DbSessionFactory sessionFactory;
	
	public List<Map<String, String>> getAllPatients() {
		String SQLqueryString = "select pi.patient_id, pi.identifier, pn.given_name, pn.family_name, pns.match_outcome, pns.otherinfo from patient_identifier pi"
		        + " right join person_name pn on pn.person_id=pi.patient_id "
		        + " right join pbs_ndr_status pns on pns.pepfar_id=pi.identifier "
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
			tempMap.put("match_outcome", (String) row[4]);
			tempMap.put("otherinfo", (String) row[5]);
			patientList.add(tempMap);
		}
		return patientList;
	}
	
	public List<Map<String, String>> getPatientBaselinePBS(String pepfarId, int patient_id) {
		String SQLqueryString = null;
		SQLQuery query;
		
		if (patient_id == 0) {
			patient_id = getPatientIdByPepfarId(pepfarId);
		}
		
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
		if (patient_id == 0) {
			patient_id = getPatientIdByPepfarId(pepfarId);
		}
		
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
		//		String url = "http://41.223.44.116:90/tbqual/api/ndrmatch_status/" + pepfarId + "/" + facilityDatimCode;
		
		// Construct URL
		String url = "http://41.223.44.116:90/ndrstatus/index.php?pepfarid=" + pepfarId + "&fdatimcode=" + facilityDatimCode;
		
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
			System.out.println("Error Local did not work also : " + e.getMessage());
			//	throw e; // Re-throw the exception if needed for further handling
			return getLocalNDRStatus(pepfarId);
		}
	}
	
	public String getNDRStatusforFacility(String pepfarId, String facilityDatimCode) throws Exception {
		// Construct URL
		//		String url = "http://41.223.44.116:90/tbqual/api/ndrmatch_status/" + pepfarId + "/" + facilityDatimCode;
		
		// Construct URL
		String url = "http://41.223.44.116:90/ndrstatus/index.php?pepfarid=" + pepfarId + "&fdatimcode=" + facilityDatimCode;
		
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
		Transaction transaction = null;
		try {
			// Construct JavaType for List<ResponseData>
			JavaType listType = objectMapper.getTypeFactory().constructCollectionType(List.class, ResponseData.class);
			
			// Parse JSON response into a list of objects
			List<ResponseData> responseDataList = objectMapper.readValue(response, listType);
			
			transaction = getSessionFactory().getCurrentSession().beginTransaction();
			
			// Get Hibernate session and create SQL query with ON DUPLICATE KEY UPDATE
			SQLQuery query = getSessionFactory()
			        .getCurrentSession()
			        .createSQLQuery(
			            "INSERT INTO pbs_ndr_status (facility_datim_code, pepfar_id, match_outcome, recapture_date, baseline_replaced, otherinfo) "
			                    + "VALUES (:facilityDatimCode, :pepfarId, :matchOutcome, :recaptureDate, :baselineReplaced, :otherInfo) "
			                    + "ON DUPLICATE KEY UPDATE " + "match_outcome = VALUES(match_outcome), "
			                    + "recapture_date = VALUES(recapture_date), "
			                    + "baseline_replaced = VALUES(baseline_replaced), " + "otherinfo = VALUES(otherinfo)");
			
			// Iterate over the list and execute INSERT/UPDATE statements
			for (ResponseData responseData : responseDataList) {
				
				Timestamp recaptureDate = convertToTimestamp(responseData.getRecapture_date());
				
				query.setParameter("facilityDatimCode", responseData.getFacility_datim_code());
				query.setParameter("pepfarId", responseData.getPepfar_id());
				query.setParameter("matchOutcome", responseData.getMatch_outcome());
				query.setParameter("recaptureDate", recaptureDate);
				query.setParameter("baselineReplaced", responseData.getBaseline_replaced());
				query.setParameter("otherInfo", responseData.getOtherinfo());
				query.executeUpdate();
			}
			
			transaction.commit();
			
			System.out.println("Response saved to database successfully!");
		}
		catch (Exception e) {
			if (transaction != null && transaction.isActive()) {
				transaction.rollback();
			}
			
			e.printStackTrace();
			System.out.println("Failed to save response to database.");
		}
		finally {
			if (getSessionFactory().getCurrentSession() != null && getSessionFactory().getCurrentSession().isOpen()) {
				// No need to close session explicitly due to Hibernate SessionFactory management
			}
		}
	}
	
	// Method to convert date format
	private Timestamp convertToTimestamp(String dateStr) throws ParseException {
		try {
			SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
			Date date = inputFormat.parse(dateStr);
			return new Timestamp(date.getTime());
		}
		catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public String getLocalNDRStatus(String pepfarId) {
		String SQLqueryString = "select match_outcome,recapture_date,baseline_replaced,otherinfo from pbs_ndr_status where pepfar_id=:pepfarID LIMIT 1";
		
		SQLQuery query = getSessionFactory().getCurrentSession().createSQLQuery(SQLqueryString);
		query.setParameter("pepfarID", pepfarId);
		
		List<Object[]> clientLocalNDRStatus = query.list();

		System.out.println(clientLocalNDRStatus);
		
		List<Map<String, String>> clientNDRStatus = new ArrayList<>();
		
		for (Object[] row : clientLocalNDRStatus) {
			Map<String, String> tempMap = new HashMap<>();
			tempMap.put("match_outcome", row[0]!= null ? row[0].toString() : "");
			tempMap.put("recapture_date", row[1]!= null ? row[1].toString() : "");
			tempMap.put("baseline_replaced", row[2]!= null ? row[2].toString() : "");
			tempMap.put("otherinfo", row[3] != null ? row[3].toString() : "");
			clientNDRStatus.add(tempMap);
		}

		String json = new Gson().toJson(clientNDRStatus);


		System.out.println(json);

		return json;
	}
	
	public List<Map<String, Object>> getPriorityList() {
		String SQLqueryString = "select match_outcome,recapture_date,baseline_replaced,otherinfo, pepfar_id from pbs_ndr_status where match_outcome !='Match'";
		
		SQLQuery query = getSessionFactory().getCurrentSession().createSQLQuery(SQLqueryString);
		
		List<Object[]> clientLocalNDRStatus = query.list();
		
		List<Map<String, Object>> clientNDRStatus = new ArrayList<>();
		
		for (Object[] row : clientLocalNDRStatus) {
			Map<String, Object> tempMap = new HashMap<>();
			tempMap.put("match_outcome", row[0] != null ? row[0] : "");
			tempMap.put("recapture_date", row[1] != null ? row[1] : "");
			tempMap.put("baseline_replaced", row[2] != null ? row[2] : "");
			tempMap.put("otherinfo", row[3] != null ? row[3] : "");
			tempMap.put("pepfar_id", row[4] != null ? row[4] : "");
			clientNDRStatus.add(tempMap);
		}
		return clientNDRStatus;
	}
	
	public int getPatientIdByPepfarId(String pepfarId) {
		try {
			Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(Patient.class); // Assuming the entity is named Patient
			criteria.add(Restrictions.eq("pepfarId", pepfarId));
			criteria.setProjection(Projections.property("patient_id")); // Assuming "patient_id" is the property name
			
			Integer patientId = (Integer) criteria.uniqueResult();
			
			return patientId != null ? patientId : -1; // Return -1 if patientId is null
		}
		catch (Exception e) {
			e.printStackTrace();
			return -1; // Return -1 in case of an exception
		}
	}
	
	// Save Observed Comment
	public void saveComment(String comment, String pepfarId) throws Exception {
		Transaction transaction = null;
		try {
			
			transaction = getSessionFactory().getCurrentSession().beginTransaction();
			SQLQuery query = getSessionFactory()
			        .getCurrentSession()
			        .createSQLQuery(
			            "UPDATE pbs_ndr_status SET otherinfo = CASE WHEN otherinfo IS NULL THEN :comment ELSE CONCAT(otherinfo, ', ', :comment) END WHERE pepfar_id = :pepfarid");
			System.out.println("INSERT COMMENT STARTED");
			
			query.setParameter("comment", comment);
			query.setParameter("pepfarid", pepfarId);
			
			query.executeUpdate();
			
			transaction.commit();
			
			sendCommentToAPI(pepfarId, comment);
			System.out.println("COMMENT SAVED!");
		}
		catch (Exception e) {
			if (transaction != null && transaction.isActive()) {
				transaction.rollback();
			}
			sendCommentToAPI(pepfarId, comment);
			e.printStackTrace();
			System.out.println("Failed to save response to database.");
		}
		finally {
			sendCommentToAPI(pepfarId, comment);
			if (getSessionFactory().getCurrentSession() != null && getSessionFactory().getCurrentSession().isOpen()) {
				// No need to close session explicitly due to Hibernate SessionFactory management
			}
		}
	}
	
	public void sendCommentToAPI(String pepfarId, String comment) {
		// Construct URL
		String url = "http://41.223.44.116:90/ndrstatus/index.php";
		
		try {
			// Open connection
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			
			// Set request method and headers
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			con.setRequestProperty("User-Agent", "Mozilla/5.0");
			con.setDoOutput(true);
			
			String urlParameters = "pepfar_id=" + pepfarId + "&comment=" + comment;
			
			// Send POST request
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();
			
			// Get response code
			int responseCode = con.getResponseCode();
			
			System.out.println("\nSending 'GET' request to URL : " + url);
			System.out.println("Response Code : " + responseCode);
			
			System.out.println("The Comment Saved to API!");
			
		}
		catch (UnknownHostException e) {
			
			// Handle no internet connection error
			System.out.println("Error: No internet connection. " + e.getMessage());
			// throw e; // Re-throw the exception if needed for further handling
		}
		catch (Exception e) {
			// Handle other exceptions
			System.out.println("Error: " + e.getMessage());
			// throw e; // Re-throw the exception if needed for further handling
		}
	}
	
	public String getPatientUuid(int patientId) {
		try {
			Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(Patient.class);
			// Assuming the entity is named Patient and patientId is the property name
			
			criteria.createAlias("person", "p"); // Join Patient with Person
			criteria.add(Restrictions.eq("patientId", patientId));
			criteria.setProjection(Projections.property("p.uuid")); // Fetch uuid from Person
			
			String uuid = (String) criteria.uniqueResult();
			
			return uuid != null ? uuid : "";
		}
		catch (Exception e) {
			e.printStackTrace();
			return "";
		}
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
