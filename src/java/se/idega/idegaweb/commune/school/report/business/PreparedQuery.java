/*
 * $Id: PreparedQuery.java,v 1.38 2004/10/14 11:45:29 anders Exp $
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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolSeason;

/** 
 * Handles the SQL logic for school report calculations.
 * <p>
 * Last modified: $Date: 2004/10/14 11:45:29 $ by $Author: anders $
 *
 * @author Anders Lindman
 * @version $Revision: 1.38 $
 */
public class PreparedQuery {

	private final static String TABLE_CM = "sch_class_member";
	private final static String TABLE_SC = "sch_school_class";
	private final static String TABLE_S = "sch_school";
	private final static String TABLE_SY = "sch_school_year";
	private final static String TABLE_SP = "sch_study_path";
	private final static String TABLE_UA = "ic_user_address";
	private final static String TABLE_A = "ic_address";
	private final static String TABLE_U = "ic_user";
	private final static String TABLE_CA = "comm_childcare_archive";
	private final static String TABLE_C = "comm_childcare";
	private final static String TABLE_ST = "sch_school_sch_school_type";
	private final static String TABLE_R = "cacc_regulation";
	private final static String TABLE_RC = "cacc_conditions";
	
	private final static String CM = "cm";
	private final static String SC = "sc";
	private final static String S = "s";
	private final static String SY = "sy";
	private final static String SP = "sp";	
	private final static String UA = "ua";
	private final static String A = "a";	
	private final static String U = "u";	
	private final static String CA = "ca";	
	private final static String C = "c";	
	private final static String ST = "st";	
	private final static String R = "r";	
	private final static String RC = "rc";	
	
	private String _sqlSelect = null;
	private String _sql = null;
	private Map _sqlFrom = null;
	private List _sqlWhere = null; 
	private String _currentDate = new Date(System.currentTimeMillis()).toString();
	private int _parameterIndex = 0;
	private Connection _connection = null;
	private PreparedStatement _preparedStatement = null;
	private boolean _countSubQuery = false;

	/**
	 * Constructs an empty report query.
	 */	
	public PreparedQuery(Connection connection) {
		_sqlFrom = new HashMap();
		_sqlWhere = new ArrayList();
		_parameterIndex = 1;
		_connection = connection;
	}	
	
	/**
	 * Closes this query.
	 */
	public void close() {
		if (_preparedStatement != null) {
			try {
				_preparedStatement.close();
			} catch (SQLException e) {}
		}
	}

	/**
	 * Sets the query to calulate mean value for child care taker week hours.
	 */
	public void setSelectMeanChildCareWeekHours() {
		_sqlSelect = "select avg(c.care_time)";
		
		_sqlFrom.put(C, TABLE_C);
	}

	/**
	 * Sets the query to calulate sum for child care taker week hours.
	 */
	public void setSelectSumChildCareWeekHours() {
		_sqlSelect = "select sum(c.care_time)";
		
		_sqlFrom.put(C, TABLE_C);
	}

	/**
	 * Sets the query to select max study path amount.
	 */
	public void setSelectMaxStudyPathAmount() {
		_sqlSelect = "select max(r.amount)";
		
		_sqlFrom.put(R, TABLE_R);
	}

	/**
	 * Sets the query to count rows.
	 */
	public void setSelectCount() {
		_sqlSelect = "select count(*)";
	}

	/**
	 * Sets the query to count distinct users.
	 */
	public void setSelectCountDistinctUsers() {
		_sqlSelect = "select count(distinct u.personal_id)";

		_sqlFrom.put(U, TABLE_U);
	}

	/**
	 * Sets the query to count rows.
	 */
	public void setSelectCountSubQuery() {
		_countSubQuery = true;
	}

	/**
	 * Sets the query to select distinct schools.
	 */
	public void setSelectDistinctSchools() {
		_sqlSelect = "select distinct s.*";
	}
	
	/**
	 * Sets the query to select student placements.
	 * @param schoolSeasonId the school season id for the placements to count
	 */
	public void setPlacements(int schoolSeasonId) {
		String sql = "cm.register_date <= '" + _currentDate + 
		"' and (cm. removed_date is null or cm.removed_date > '" + _currentDate + "')" + 
		" and sc.school_id = s.sch_school_id and sc.sch_school_class_id = cm.sch_school_class_id" +
		" and sc.sch_school_season_id = " + schoolSeasonId +
		" and cm.ic_user_id = u.ic_user_id";
		_sqlWhere.add(sql);
		
		_sqlFrom.put(CM, TABLE_CM);
		_sqlFrom.put(SC, TABLE_SC);
		_sqlFrom.put(S, TABLE_S);
		_sqlFrom.put(U, TABLE_U);
	}
	
	/**
	 * Sets the query to select child care placements.
	 * @param schoolSeasonId the school season id for the placements to count
	 */
	public void setChildCarePlacements() {
		String sql = "ca.sch_class_member_id = cm.sch_class_member_id and ca.application_id = c.comm_childcare_id" +
				" and c.provider_id = s.sch_school_id" +
//				" and (ca.terminated_date is null or ca.terminated_date >= '" + _currentDate + "')" +
//				" and c.application_status in ('F', 'V') AND ca.VALID_FROM_DATE < '" + _currentDate + "'" +
				" and (ca.terminated_date is null or ca.terminated_date >= sysdate)" +
				" and c.application_status in ('F', 'V') AND ca.VALID_FROM_DATE <= sysdate" +
				" and cm.ic_user_id = u.ic_user_id";
		_sqlWhere.add(sql);
		
		_sqlFrom.put(CA, TABLE_CA);
		_sqlFrom.put(C, TABLE_C);
		_sqlFrom.put(CM, TABLE_CM);
		_sqlFrom.put(S, TABLE_S);
		_sqlFrom.put(U, TABLE_U);
	}
	
	/**
	 * Sets the query to select a specific school.
	 * @return the index for the school id parameter
	 */
	public int setSchool() {
		String sql = "s.sch_school_id = ?";
		_sqlWhere.add(sql);
		
		_sqlFrom.put(S, TABLE_S);
		
		int index = _parameterIndex;
		_parameterIndex++;
		return index;
	}
	
	/**
	 * Sets the query to select a specific school area.
	 * @return the index for the school area id parameter
	 */
	public int setSchoolArea() {
		String sql = "s.sch_school_area_id = ?";
		_sqlWhere.add(sql);
		
		_sqlFrom.put(S, TABLE_S);
		
		int index = _parameterIndex;
		_parameterIndex++;
		return index;
	}
	
	/**
	 * Sets the query to select only Nacka citizens.
	 */
	public void setOnlyNackaCitizens() {
		String sql = "ua.ic_user_id = u.ic_user_id and a.ic_address_id = ua.ic_address_id" +
				" and a.ic_address_type_id = 1 and a.ic_commune_id = 1 and cm.ic_user_id = u.ic_user_id";
		_sqlWhere.add(sql);
		
		_sqlFrom.put(U, TABLE_U);
		_sqlFrom.put(A, TABLE_A);
		_sqlFrom.put(UA, TABLE_UA);
		_sqlFrom.put(CM, TABLE_CM);
	}

	/**
	 * Sets the query to select only citizens in communes outside Nacka.
	 */
	public void setNotNackaCitizens() {
		String sql = "ua.ic_user_id = u.ic_user_id and a.ic_address_id = ua.ic_address_id" +
				" and a.ic_address_type_id = 1 and a.ic_commune_id <> 1 and cm.ic_user_id = u.ic_user_id";
		_sqlWhere.add(sql);
		
		_sqlFrom.put(U, TABLE_U);
		_sqlFrom.put(A, TABLE_A);
		_sqlFrom.put(UA, TABLE_UA);
		_sqlFrom.put(CM, TABLE_CM);
	}
	
	/**
	 * Sets the query to select number of placements for a study path prefix (2 chars).
	 * @return the index for the study path prefix parameter
	 */
	public int setStudyPathPrefix() {
		String sql = "cm.study_path = sp.sch_study_path_id and sp.study_path_code like ?";
		_sqlWhere.add(sql);
		
		_sqlFrom.put(CM, TABLE_CM);
		_sqlFrom.put(SP, TABLE_SP);
		
		int index = _parameterIndex;
		_parameterIndex++;
		return index;
	}
	
	/**
	 * Set select only the specified school type.
	 */
	public void setSchoolType(int schoolTypeId) {
		String sql = "cm.sch_school_type_id = " + schoolTypeId;
		_sqlWhere.add(sql);
		
		_sqlFrom.put(CM, TABLE_CM);
	}
	
	/**
	 * Set select only the specified school type.
	 * @return the index for the school type id
	 */
	public int setSchoolType() {
		String sql = "cm.sch_school_type_id = ?";
		_sqlWhere.add(sql);
		
		_sqlFrom.put(CM, TABLE_CM);
		
		int index = _parameterIndex;
		_parameterIndex++;
		return index;
	}
	
	/**
	 * Set select only the specified four school types.
	 * @return the index for the first school type id
	 */
	public int setFourSchoolTypes() {
		String sql = "(cm.sch_school_type_id = ? or cm.sch_school_type_id = ? or cm.sch_school_type_id = ? or cm.sch_school_type_id = ?)";
		_sqlWhere.add(sql);
		
		_sqlFrom.put(CM, TABLE_CM);
		
		int index = _parameterIndex;
		_parameterIndex += 4;
		return index;
	}
	
	/**
	 * Set select only the specified school type.
	 * @return the index for the school type id
	 */
	public int setSchoolTypeForProvider() {
		String sql = "s.sch_school_id = st.sch_school_id and st.sch_school_type_id = cm.sch_school_type_id and st.sch_school_type_id = ?";
		_sqlWhere.add(sql);
		
		_sqlFrom.put(S, TABLE_S);
		_sqlFrom.put(ST, TABLE_ST);
		_sqlFrom.put(CM, TABLE_CM);
		
		int index = _parameterIndex;
		_parameterIndex++;
		return index;
	}
	
	/**
	 * Set select only the specified four school types.
	 * @return the index for the first school type id
	 */
	public int setFourSchoolTypesForProviders() {
		String sql = "s.sch_school_id = st.sch_school_id and st.sch_school_type_id = cm.sch_school_type_id and (st.sch_school_type_id = ? or st.sch_school_type_id = ? or st.sch_school_type_id = ? or st.sch_school_type_id = ?)";
		_sqlWhere.add(sql);
		
		_sqlFrom.put(S, TABLE_S);
		_sqlFrom.put(ST, TABLE_ST);
		_sqlFrom.put(CM, TABLE_CM);
		
		int index = _parameterIndex;
		_parameterIndex += 4;
		return index;
	}
	
	/**
	 * Set select only the specified four school types.
	 * @return the index for the first school type id
	 */
	public int setFourSchoolTypesForProvidersWithoutPlacements() {
		String sql = "s.sch_school_id = st.sch_school_id and (st.sch_school_type_id = ? or st.sch_school_type_id = ? or st.sch_school_type_id = ? or st.sch_school_type_id = ?)";
		_sqlWhere.add(sql);
		
		_sqlFrom.put(S, TABLE_S);
		_sqlFrom.put(ST, TABLE_ST);
		
		int index = _parameterIndex;
		_parameterIndex += 4;
		return index;
	}
	
	/**
	 * Set select only elementary school.
	 */
	public void setSchoolTypeElementarySchool() {
		String sql = "cm.sch_school_type_id = 4";
		_sqlWhere.add(sql);
		
		_sqlFrom.put(CM, TABLE_CM);
	}
	
	/**
	 * Set select only school type compulsory school.
	 */
	public void setSchoolTypeCompulsorySchool() {
		String sql = "cm.sch_school_type_id = 28";
		_sqlWhere.add(sql);
		
		_sqlFrom.put(CM, TABLE_CM);
	}
	
	/**
	 * Set select only school type pre-school class.
	 */
	public void setSchoolTypePreSchoolClass() {
		String sql = "cm.sch_school_type_id = 5";
		_sqlWhere.add(sql);
		
		_sqlFrom.put(CM, TABLE_CM);
	}
	
	/**
	 * Set select only high schools.
	 */
	public void setSchoolTypeHighSchool() {
		String sql = "cm.sch_school_type_id = 26";
		_sqlWhere.add(sql);
		
		_sqlFrom.put(CM, TABLE_CM);
	}
	
	/**
	 * Set select only compulsory high schools.
	 */
	public void setSchoolTypeCompulsoryHighSchool() {
		String sql = "cm.sch_school_type_id = 27";
		_sqlWhere.add(sql);
		
		_sqlFrom.put(CM, TABLE_CM);
	}
	
	/**
	 * Set select only general pre schools.
	 */
	public void setSchoolTypeGeneralPreSchool() {
		String sql = "cm.sch_school_type_id = 33";
		_sqlWhere.add(sql);
		
		_sqlFrom.put(CM, TABLE_CM);
	}
	
	/**
	 * Set select only a specific school year name.
	 */
	public void setSchoolYearName(String schoolYearName) {
		String sql = "cm.sch_school_year_id = sy.sch_school_year_id and sy.year_name = '" + schoolYearName + "'";
		_sqlWhere.add(sql);
		
		_sqlFrom.put(CM, TABLE_CM);
		_sqlFrom.put(SY, TABLE_SY);
	}
	
	/**
	 * Set select only a specific school year name.
	 * @return the index for the school year name parameter 
	 */
	public int setSchoolYearName() {
		String sql = "cm.sch_school_year_id = sy.sch_school_year_id and sy.year_name = ?";
		_sqlWhere.add(sql);
		
		_sqlFrom.put(CM, TABLE_CM);
		_sqlFrom.put(SY, TABLE_SY);
		
		int index = _parameterIndex;
		_parameterIndex++;
		return index;
	}
	
	/**
	 * Set select not a specific school year name.
	 */
	public void setNotSchoolYearName(String schoolYearName) {
		String sql = "cm.sch_school_year_id = sy.sch_school_year_id and sy.year_name <> '" + schoolYearName + "'";
		_sqlWhere.add(sql);
		
		_sqlFrom.put(CM, TABLE_CM);
		_sqlFrom.put(SY, TABLE_SY);
	}
	
	/**
	 * Set select only schools in Nacka commune.
	 */
	public void setOnlyNackaSchools() {
		String sql = "s.commune = 1";
		_sqlWhere.add(sql);
		
		_sqlFrom.put(S, TABLE_S);
	}
	
	/**
	 * Set select only schools in communes other than Nacka.
	 */
	public void setOnlySchoolsInOtherCommunes() {
		String sql = "s.commune <> 1";
		_sqlWhere.add(sql);
		
		_sqlFrom.put(S, TABLE_S);
	}
	
	/**
	 * Set select only private schools.
	 */
	public void setOnlyPrivateSchools() {
		String sql = "(s.management_type = 'COMPANY' or s.management_type = 'FOUNDATION' or s.management_type = 'OTHER')";
		_sqlWhere.add(sql);
		
		_sqlFrom.put(S, TABLE_S);		
	}
	
	/**
	 * Set select only other than private schools.
	 */
	public void setNotPrivateSchools() {
		String sql = "s.management_type <> 'COMPANY' and s.management_type <> 'FOUNDATION' and s.management_type <> 'OTHER'";
		_sqlWhere.add(sql);
		
		_sqlFrom.put(S, TABLE_S);
	}
	
	/**
	 * Set select only commune schools.
	 */
	public void setOnlyCommuneSchools() {
		String sql = "s.management_type = 'COMMUNE'";
		_sqlWhere.add(sql);
		
		_sqlFrom.put(S, TABLE_S);		
	}
	
	/**
	 * Set select only foreign schools.
	 */
	public void setOnlyForeignSchools() {
		String sql = "s.school_name = 'Utlandselever'";
		_sqlWhere.add(sql);
		
		_sqlFrom.put(S, TABLE_S);
	}
	
	/**
	 * Set select not foreign schools.
	 */
	public void setNotForeignSchools() {
		String sql = "s.school_name <> 'Utlandselever'";
		_sqlWhere.add(sql);
		
		_sqlFrom.put(S, TABLE_S);
	}
	
	/**
	 * Set select only county council schools.
	 */
	public void setOnlyCountyCouncilSchools() {
		String sql = "s.management_type = 'COUNTY COUNCIL'";
		_sqlWhere.add(sql);
		
		_sqlFrom.put(S, TABLE_S);
	}
	
	/**
	 * Set select only other than county council schools.
	 */
	public void setNotCountyCouncilSchools() {
		String sql = "s.management_type <> 'COUNTY COUNCIL'";
		_sqlWhere.add(sql);
		
		_sqlFrom.put(S, TABLE_S);
	}
	
	/**
	 * Set select only students born the specified year.
	 */
	public void setOnlyStudentsBorn(int year) {
		String sql = "u.date_of_birth >= '" + year + "-01-01' and u.date_of_birth <= '" + year + "-12-31'" + 
				" and cm.ic_user_id = u.ic_user_id";
		_sqlWhere.add(sql);
		
		_sqlFrom.put(U, TABLE_U);
		_sqlFrom.put(CM, TABLE_CM);
	}
	
	/**
	 * Set select only the specified school.
	 */
	public void setSchool(int schoolId) {
		String sql = "s.sch_school_id = " + schoolId;
		_sqlWhere.add(sql);
		
		_sqlFrom.put(S, TABLE_S);
	}

	/**
	 * Set select only the specified schools.
	 */
	public void setSchools(Collection schools) {
		String sql = "s.sch_school_id in (";
		Iterator iter = schools.iterator();
		while (iter.hasNext()) {
			School school = (School) iter.next();
			sql += school.getPrimaryKey();
			if (iter.hasNext()) {
				sql += ", ";
			}
		}
		sql += ")";
		_sqlWhere.add(sql);
		
		_sqlFrom.put(S, TABLE_S);
	}

	/**
	 * Set select only the specified schools.
	 */
	public void setSchools(School[] schools) {
		String sql = "s.sch_school_id in (";
		for (int i = 0; i < schools.length; i++) {
			School school = schools[i];
			sql += school.getPrimaryKey();
			if (i != schools.length - 1) {
				sql += ", ";
			}
		}
		sql += ")";
		_sqlWhere.add(sql);
		
		_sqlFrom.put(S, TABLE_S);
	}

	/**
	 * Set select only students in the specified age interval.
	 */
	public void setStudentAge(SchoolSeason season, int ageFrom, int ageTo) {
		String sql = null;
		String seasonDate = season.getSchoolSeasonStart().toString();
		String dateFrom = null;
		String dateTo = null;
		int seasonYear = Integer.parseInt(seasonDate.substring(0, 4));
		if (ageFrom > 0) {
			dateFrom = "" + (seasonYear - ageFrom) + seasonDate.substring(4);
			sql = "u.date_of_birth <= '" + dateFrom + "'";
		}
		if (ageTo > 0) {
			dateTo = "" + (seasonYear - ageTo - 1) + seasonDate.substring(4);
			if (sql != null) {
				sql += " and u.date_of_birth > '" + dateTo + "'";
			} else {
				sql =  "u.date_of_birth > '" + dateTo + "'";
			}
		}
		_sqlWhere.add(sql);
		
		_sqlFrom.put(U, TABLE_U);
	}

	/**
	 * Set select only students in the specified age interval calculated from current date.
	 */
	public void setStudentAge(int ageFrom, int ageTo) {
		String sql = null;
		String dateFrom = null;
		String dateTo = null;
		int currentYear = Integer.parseInt(_currentDate.substring(0, 4));
		if (ageFrom > 0) {
			dateFrom = "" + (currentYear - ageFrom) + _currentDate.substring(4);
			sql = "u.date_of_birth <= '" + dateFrom + "'";
		}
		if (ageTo > 0) {
			dateTo = "" + (currentYear - ageTo - 1) + _currentDate.substring(4);
			if (sql != null) {
				sql += " and u.date_of_birth > '" + dateTo + "'";
			} else {
				sql =  "u.date_of_birth > '" + dateTo + "'";
			}
		}
		_sqlWhere.add(sql);
		
		_sqlFrom.put(U, TABLE_U);
	}
	
	/**
	 * Set select only the specified three management types.
	 * @return the index for the first management type id
	 */
	public int setThreeManagementTypes() {
		String sql = "(s.management_type = ? or s.management_type = ? or s.management_type = ?)";
		_sqlWhere.add(sql);
		
		_sqlFrom.put(S, TABLE_S);
		
		int index = _parameterIndex;
		_parameterIndex += 3;
		return index;
	}
	
	/**
	 * Set select only the specified four management types.
	 * @return the index for the first management type id
	 */
	public int setFourManagementTypes() {
		String sql = "(s.management_type = ? or s.management_type = ? or s.management_type = ? or s.management_type = ?)";
		_sqlWhere.add(sql);
		
		_sqlFrom.put(S, TABLE_S);
		
		int index = _parameterIndex;
		_parameterIndex += 4;
		return index;
	}
	
	/**
	 * Set select only the specified work situation.
	 * @return the index for the work situation (employer type id)
	 */
	public int setWorkSituation() {
		String sql = "ca.work_situation = ?";
		_sqlWhere.add(sql);
		
		_sqlFrom.put(CA, TABLE_CA);
		
		int index = _parameterIndex;
		_parameterIndex++;
		return index;
	}
	
	/**
	 * Set select only the specified child care week hours interval (from, to).
	 * @return the index for the first interval hours
	 */
	public int setChildCareWeekHours() {
		String sql = "ca.care_time >= ? and ca.care_time <= ?";
		_sqlWhere.add(sql);
		
		_sqlFrom.put(CA, TABLE_CA);
		
		int index = _parameterIndex;
		_parameterIndex += 2;
		return index;
	}
	
	/**
	 * Sets the query to select a specified study path amount.
	 * @return the index for the study path id parameter
	 */
	public int setStudyPathAmount() {
		String sql = "r.cacc_regulation_id = rc.regulation_id and rc.condition_id = 9 and rc.interval_id = ?";
		_sqlWhere.add(sql);
		
		_sqlFrom.put(R, TABLE_R);
		_sqlFrom.put(RC, TABLE_RC);
		
		int index = _parameterIndex;
		_parameterIndex++;
		return index;
	}

	/**
	 * Prepares this query by building the prepared sql statement.
	 */
	public void prepare() {
		String sql = _sqlSelect;
		
		sql += " from ";
		
		Iterator tableKeys = _sqlFrom.keySet().iterator();
		while (tableKeys.hasNext()) {
			String key = (String) tableKeys.next();
			sql += _sqlFrom.get(key) + " " + key;
			if (tableKeys.hasNext()) {
				sql += ", ";
			}
		}
		
		sql += " where ";
		
		Iterator whereClauses = _sqlWhere.iterator();
		while (whereClauses.hasNext()) {
			sql += whereClauses.next();
			if (whereClauses.hasNext()) {
				sql += " and ";
			}
		}
		
		if (_countSubQuery) {
			sql = "select count(*) from (" + sql + ")"; 
		}
		_sql = sql;
		try {
			_preparedStatement = _connection.prepareStatement(sql);			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		System.out.println("Report SQL: " + sql);
	}
	
	/**
	 * Sets the specified integer parameter for the prepared query. 
	 */
	public void setInt(int parameterIndex, int x) {
		try {
			_preparedStatement.setInt(parameterIndex, x);			
		} catch (SQLException e) {
			System.out.println("setInt(): " + e.getMessage());
		}
	}
	
	/**
	 * Sets the specified string parameter for the prepared query. 
	 */
	public void setString(int parameterIndex, String x) {
		try {
			_preparedStatement.setString(parameterIndex, x);			
		} catch (SQLException e) {
			System.out.println("setString(): " + e.getMessage());
		}
	}
	
	/**
	 * Executes this query and returns the integer from the first row in the result set.
	 * Returns -1 if error or no row found. 
	 */
	public int execute() {
		int result = -1;
		ResultSet resultSet = null;
		
		try {
			resultSet = _preparedStatement.executeQuery();
			if (resultSet.next()) {
				result = resultSet.getInt(1);
			}
		} catch (Exception e) {
			System.out.println("execute(): " + e.getMessage());
			e.printStackTrace();
		} finally {
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (Exception e) {}
			}
		}

		return result;
	}
	
	/**
	 * Executes this query and returns the float value from the first row in the result set.
	 * Returns -1 if error or no row found. 
	 */
	public float executeFloat() {
		float result = -1;
		ResultSet resultSet = null;
		
		try {
			resultSet = _preparedStatement.executeQuery();
			if (resultSet.next()) {
				result = resultSet.getFloat(1);
			}
		} catch (Exception e) {
			System.out.println("execute(): " + e.getMessage());
			e.printStackTrace();
		} finally {
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (Exception e) {}
			}
		}

		return result;
	}
	
	public String toString() {
		return _sql;
	}
}
