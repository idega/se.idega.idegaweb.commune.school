/*
 * $Id: ReportQuery.java,v 1.19 2004/01/14 13:29:45 anders Exp $
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
import java.util.Collection;
import java.util.Iterator;

import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolSeason;
import com.idega.util.database.ConnectionBroker;

/** 
 * Handles the SQL logic for school report calculations.
 * <p>
 * Last modified: $Date: 2004/01/14 13:29:45 $ by $Author: anders $
 *
 * @author Anders Lindman
 * @version $Revision: 1.19 $
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
				" and sc.sch_school_season_id = " + schoolSeasonId;
	}
	
	/**
	 * Sets the query to select number of placements for a school.
	 * @param schoolSeasonId the school season id for the placements to count
	 * @param schoolId the school id
	 */
	public void setSelectCountPlacements(int schoolSeasonId, int schoolId) {
		sql = "select count(*) from sch_class_member cm," +
				" sch_school_class sc, sch_school s, sch_school_year sy where" +
				" cm.register_date <= '" + currentDate + 
				"' and (cm. removed_date is null or cm.removed_date > '" + currentDate + "')" + 
				" and sc.school_id = s.sch_school_id and sc.sch_school_class_id = cm.sch_school_class_id" +
				" and sc.sch_school_season_id = " + schoolSeasonId +
				" and s.sch_school_id = " + schoolId;
	}
	
	/**
	 * Sets the query to select number of placements for a school for 
	 * citizens in communes outside Nacka.
	 * @param schoolSeasonId the school season id for the placements to count
	 * @param schoolId the school id
	 */
	public void setSelectCountOCCPlacements(int schoolSeasonId, int schoolId) {
		sql = "select count(*) from ic_user u, ic_address a, ic_user_address ua, sch_class_member cm," +
				" sch_school_class sc, sch_school s, sch_school_year sy where" +
				" cm.register_date <= '" + currentDate + 
				"' and (cm. removed_date is null or cm.removed_date > '" + currentDate + "')" + 
				" and sc.school_id = s.sch_school_id and sc.sch_school_class_id = cm.sch_school_class_id" +
				" and sc.sch_school_season_id = " + schoolSeasonId +
				" and s.sch_school_id = " + schoolId + 
				" and ua.ic_user_id = u.ic_user_id and a.ic_address_id = ua.ic_address_id" +
				" and a.ic_address_type_id = 1 and a.ic_commune_id <> 1 and cm.ic_user_id = u.ic_user_id";
	}
	
	/**
	 * Sets the query to select number of six years old student placements for a school.
	 * @param schoolSeasonId the school season id for the placements to count
	 * @param schoolId the school id
	 */
	public void setSelectCountPlacementsSixYearsOld(int schoolSeasonId, int schoolId) {
		sql = "select count(*) from ic_user u, sch_class_member cm," +
				" sch_school_class sc, sch_school s, sch_school_year sy where" +
				" cm.register_date <= '" + currentDate + 
				"' and (cm. removed_date is null or cm.removed_date > '" + currentDate + "')" + 
				" and sc.school_id = s.sch_school_id and sc.sch_school_class_id = cm.sch_school_class_id" +
				" and sc.sch_school_season_id = " + schoolSeasonId +
				" and s.sch_school_id = " + schoolId +
				" and cm.ic_user_id = u.ic_user_id";
	}
	
	/**
	 * Sets the query to select number of placements for the specified study path prefix (2 chars).
	 * @param schoolSeasonId the school season id for the placements to count
	 * @param studyPathPrefix the two letter study path prefix
	 */
	public void setSelectCountStudyPathPlacements(int schoolSeasonId, String studyPathPrefix) {
		sql = "select count(*) from ic_user u, ic_address a, ic_user_address ua, sch_class_member cm," +
				" sch_school_class sc, sch_school s, sch_school_year sy, sch_study_path sp where" +
				" cm.register_date <= '" + currentDate + 
				"' and (cm. removed_date is null or cm.removed_date > '" + currentDate + "')" + 
				" and sc.school_id = s.sch_school_id and sc.sch_school_class_id = cm.sch_school_class_id" +
				" and sc.sch_school_season_id = " + schoolSeasonId +
				" and cm.study_path = sp.sch_study_path_id and sp.study_path_code like '" + studyPathPrefix +"%'";
	}
	
	/**
	 * Sets the query to select number of placements for the specified study path prefix (2 chars) for
	 * student age interval.
	 * @param schoolSeasonId the school season id for the placements to count
	 * @param studyPathPrefix the two letter study path prefix
	 */
	public void setSelectCountStudyPathPlacementsAge(int schoolSeasonId, String studyPathPrefix) {
		sql = "select count(*) from ic_user u, ic_address a, ic_user_address ua, sch_class_member cm," +
				" sch_school_class sc, sch_school s, sch_study_path sp where" +
				" cm.register_date <= '" + currentDate + 
				"' and (cm. removed_date is null or cm.removed_date > '" + currentDate + "')" + 
				" and sc.school_id = s.sch_school_id and sc.sch_school_class_id = cm.sch_school_class_id" +
				" and sc.sch_school_season_id = " + schoolSeasonId +
				" and cm.study_path = sp.sch_study_path_id and sp.study_path_code like '" + studyPathPrefix +"%'";
	}

	/**
	 * Sets the query to select number of placements for the specified study path prefix (2 chars)
	 * for all school years.
	 * @param schoolSeasonId the school season id for the placements to count
	 * @param studyPathPrefix the two letter study path prefix
	 */
	public void setSelectCountStudyPathPlacementsForAllSchoolYears(int schoolSeasonId, String studyPathPrefix) {
		sql = "select count(*) from ic_user u, ic_address a, ic_user_address ua, sch_class_member cm," +
				" sch_school_class sc, sch_school s, sch_study_path sp where" +
				" cm.register_date <= '" + currentDate + 
				"' and (cm. removed_date is null or cm.removed_date > '" + currentDate + "')" + 
				" and sc.school_id = s.sch_school_id and sc.sch_school_class_id = cm.sch_school_class_id" +
				" and sc.sch_school_season_id = " + schoolSeasonId +
				" and cm.study_path = sp.sch_study_path_id and sp.study_path_code like '" + studyPathPrefix +"%'";
	}

	/**
	 * Sets the query to select number of placements for a school for 
	 * citizens in communes outside Nacka for all school years.
	 * @param schoolSeasonId the school season id for the placements to count
	 * @param schoolId the school id
	 */
	public void setSelectCountOCCPlacementsForAllSchoolYears(int schoolSeasonId, int schoolId) {
		sql = "select count(*) from ic_user u, ic_address a, ic_user_address ua, sch_class_member cm," +
				" sch_school_class sc, sch_school s where" +
				" cm.register_date <= '" + currentDate + 
				"' and (cm. removed_date is null or cm.removed_date > '" + currentDate + "')" + 
				" and sc.school_id = s.sch_school_id and sc.sch_school_class_id = cm.sch_school_class_id" +
				" and sc.sch_school_season_id = " + schoolSeasonId +
				" and s.sch_school_id = " + schoolId + 
				" and ua.ic_user_id = u.ic_user_id and a.ic_address_id = ua.ic_address_id" +
				" and a.ic_address_type_id = 1 and a.ic_commune_id <> 1 and cm.ic_user_id = u.ic_user_id";
	}
	
	/**
	 * Set select only Nacka citizens.
	 */
	public void setOnlyNackaCitizens() {
		sql += " and ua.ic_user_id = u.ic_user_id and a.ic_address_id = ua.ic_address_id" +
				" and a.ic_address_type_id = 1 and a.ic_commune_id = 1 and cm.ic_user_id = u.ic_user_id";
	}
	
	/**
	 * Set select only citizens outside Nacka.
	 */
	public void setNotNackaCitizens() {
		sql += " and ua.ic_user_id = u.ic_user_id and a.ic_address_id = ua.ic_address_id" +
				" and a.ic_address_type_id <> 1 and a.ic_commune_id = 1 and cm.ic_user_id = u.ic_user_id";
	}
	
	/**
	 * Set select only the specified school type.
	 */
	public void setSchoolType(int schoolTypeId) {
		sql += " and cm.sch_school_type_id = " + schoolTypeId;
	}
	
	/**
	 * Set select only elementary school.
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
	 * Set select only high schools.
	 */
	public void setSchoolTypeHighSchool() {
		sql += " and cm.sch_school_type_id = 26";
	}
	
	/**
	 * Set select only compulsory high schools.
	 */
	public void setSchoolTypeCompulsoryHighSchool() {
		sql += " and cm.sch_school_type_id = 27";
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
		sql += " and (s.management_type = 'COMPANY' or s.management_type = 'PRIVATE' or s.management_type = 'FOUNDATION' or s.management_type = 'OTHER')";
	}
	
	/**
	 * Set select only other than private schools.
	 */
	public void setNotPrivateSchools() {
		sql += " and s.management_type <> 'COMPANY' and s.management_type <> 'PRIVATE' and s.management_type <> 'FOUNDATION' and s.management_type <> 'OTHER'";
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
	 * Set select only county council schools.
	 */
	public void setOnlyCountyCouncilSchools() {
		sql += " and s.management_type = 'COUNTY COUNCIL'";
	}
	
	/**
	 * Set select only other than county council schools.
	 */
	public void setNotCountyCouncilSchools() {
		sql += " and s.management_type <> 'COUNTY COUNCIL'";
	}
	
	/**
	 * Set select only students born the specified year.
	 */
	public void setOnlyStudentsBorn(int year) {
		sql += " and u.date_of_birth >= '" + year + "-01-01' and u.date_of_birth <= '" + year + "-12-31'";
	}
	
	/**
	 * Set select only the specified school.
	 */
	public void setSchool(int schoolId) {
		sql += " and s.sch_school_id = " + schoolId;
	}

	/**
	 * Set select only the specified schools.
	 */
	public void setSchools(Collection schools) {
		sql += " and s.sch_school_id in (";
		Iterator iter = schools.iterator();
		while (iter.hasNext()) {
			School school = (School) iter.next();
			sql += school.getPrimaryKey();
			if (iter.hasNext()) {
				sql += ", ";
			}
		}
		sql += ")";
	}

	/**
	 * Set select only students in the specified age interval.
	 */
	public void setStudentAge(SchoolSeason season, int ageFrom, int ageTo) {
		String seasonDate = season.getSchoolSeasonStart().toString();
		String dateFrom = null;
		String dateTo = null;
		int seasonYear = Integer.parseInt(seasonDate.substring(0, 4));
		if (ageFrom > 0) {
			dateFrom = "" + (seasonYear - ageFrom) + seasonDate.substring(4);
			sql += " and u.date_of_birth <= '" + dateFrom + "'";
		}
		if (ageTo > 0) {
			dateTo = "" + (seasonYear - ageTo - 1) + seasonDate.substring(4);
			sql += " and u.date_of_birth > '" + dateTo + "'";
		}
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
