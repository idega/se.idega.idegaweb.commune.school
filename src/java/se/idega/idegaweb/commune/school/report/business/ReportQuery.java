/*
 * $Id: ReportQuery.java,v 1.4 2003/12/09 14:16:45 anders Exp $
 *
 * Copyright (C) 2003 Agura IT. All Rights Reserved.
 *
 * This software is the proprietary information of Agura IT AB.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.school.report.business;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;

import com.idega.util.database.ConnectionBroker;

/** 
 * Handles the SQL logic for school report calculations.
 * <p>
 * Last modified: $Date: 2003/12/09 14:16:45 $ by $Author: anders $
 *
 * @author Anders Lindman
 * @version $Revision: 1.4 $
 */
public class ReportQuery {

	private String sql = null;
	private String currentDate = new Date(System.currentTimeMillis()).toString();
	
	/**
	 * Constructs an empty report query.
	 */	
	public ReportQuery() {
	}	
	
	/**
	 * Sets the query to select number of placements.
	 * @param schoolSeasonId the school season id for the placements to count
	 */
	public void setSelectCountPlacements(int schoolSeasonId) {
		sql = "select count(*) from ic_user u, ic_address a, ic_user_address ua, sch_class_member cm," +
				" sch_school_class sc, sch_school s, sch_school_year sy where" +
				" cm.register_date <= '" + currentDate + 
				"' and (cm. removed_date is null or cm.removed_date > '" + currentDate + "')" + 
				" and sc.school_id = s.sch_school_id and sc.sch_school_class_id = cm.sch_school_class_id" +
				" sc.sch_school_season_id = " + schoolSeasonId;
	}
	
	/**
	 * Set select only Nacka citizens.
	 */
	public void setOnlyNackaCitizens() {
		sql += " and ua.ic_user_id = u.ic_user_id and a.ic_address_id = ua.ic_address_id" +
				" and a.ic_address_type_id = 1 and a.ic_commune_id = 1 and cm.ic_user_id = u.ic_user_id";
	}
	
	/**
	 * Set select only the specified school type.
	 */
	public void setSchoolType(int schoolTypeId) {
		sql += " and cm.sch_school_type_id = " + schoolTypeId;
	}
	
	/**
	 * Set select only elementart school.
	 */
	public void setSchoolTypeElementarySchool() {
		sql += " and cm.sch_school_type_id = 4";
	}
	
	/**
	 * Set select only school type compulsory school.
	 */
	public void setSchoolTypeCompulsorySchool() {
		sql += " and cm.sch_school_type_id = 28";
	}
	
	/**
	 * Set select only school type pre-school class.
	 */
	public void setSchoolTypePreSchoolClass() {
		sql += " and cm.sch_school_type_id = 5";
	}
	
	/**
	 * Set select only the specified school year.
	 */
	public void setSchoolYear(String schoolYearName) {
		sql += " and cm.sch_school_year_id = sy.sch_school_year_id and sy.year_name = '" + schoolYearName + "'";
	}
	
	/**
	 * Set select only schools in Nacka commune.
	 */
	public void setOnlyNackaSchools() {
		sql += " and s.commune = 1";
	}
	
	/**
	 * Set select only schools in communes other than Nacka.
	 */
	public void setOnlySchoolsInOtherCommunes() {
		sql += " and s.commune <> 1";
	}
	
	/**
	 * Set select only private schools.
	 */
	public void setOnlyPrivateSchools() {
		sql += " and (s.management_type = 'COMPANY' or s.management_type = 'PRIVATE' or s.management_type = 'FOUNDATION')";
	}
	
	/**
	 * Set select only other than private schools.
	 */
	public void setNotPrivateSchools() {
		sql += " and s.management_type <> 'COMPANY' and s.management_type <> 'PRIVATE' and s.management_type <> 'FOUNDATION'";
	}
	
	/**
	 * Set select only foreign schools.
	 */
	public void setOnlyForieignSchools() {
		sql += " and s.school_name = 'Utlandselever'";
	}
	
	/**
	 * Set select only foreign schools.
	 */
	public void setNotForieignSchools() {
		sql += " and s.school_name <> 'Utlandselever'";
	}
	
	/**
	 * Executes this query and returns the integer from the first row in the result set.
	 * Returns -1 if error or no row found. 
	 */
	public int execute() {
		int result = -1;
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		
		try {
			connection = ConnectionBroker.getConnection();
			statement = connection.createStatement();
			resultSet = statement.executeQuery(sql);
			if (resultSet.next()) {
				result = resultSet.getInt(1);
			}
		} catch (Exception e) {
			System.out.println("ReportQuery.execute() exception, sql = " + sql);
			System.out.println(e.getMessage());
		} finally {
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (Exception e) {}
			}
			if (statement != null) {
				try {
					statement.close();
				} catch (Exception e) {}
			}
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (Exception e) {}
			}
			if (connection != null) {
				ConnectionBroker.freeConnection(connection);
			}
		}

		return result;
	}
}
