/*
 * $Id: ReportModel.java,v 1.34 2004/09/24 07:50:05 malin Exp $
 *
 * Copyright (C) 2003 Agura IT. All Rights Reserved.
 *
 * This software is the proprietary information of Agura IT AB.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.school.report.business;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.idega.util.database.ConnectionBroker;

/** 
 * This abstract class holds cell and header values for school statistics reports.
 * Subclasses implements methods for generating report data and cell value calculations.
 * <p>
 * Last modified: $Date: 2004/09/24 07:50:05 $ by $Author: malin $
 *
 * @author Anders Lindman
 * @version $Revision: 1.34 $
 */
public abstract class ReportModel {

	private int _rowSize = 0;
	private int _columnSize = 0;
	
	private ReportBusiness _reportBusiness = null;
	
	private Cell[][] _cells = null;
	private Header[] _rowHeaders = null;
	private Header[] _columnHeaders = null;
	
	private Map _queryCache = null;
	private Connection _connection = null;

	protected final static String KP = "report."; // Localization key prefix
	
	protected final static String KEY_SCHOOL_YEAR_F = KP + "school_year_f";
	protected final static String KEY_SCHOOL_YEAR_1 = KP + "school_year_1";
	protected final static String KEY_SCHOOL_YEAR_2 = KP + "school_year_2";
	protected final static String KEY_SCHOOL_YEAR_3 = KP + "school_year_3";
	protected final static String KEY_SCHOOL_YEAR_4 = KP + "school_year_4";
	protected final static String KEY_SCHOOL_YEAR_5 = KP + "school_year_5";
	protected final static String KEY_SCHOOL_YEAR_6 = KP + "school_year_6";
	protected final static String KEY_SCHOOL_YEAR_7 = KP + "school_year_7";
	protected final static String KEY_SCHOOL_YEAR_8 = KP + "school_year_8";
	protected final static String KEY_SCHOOL_YEAR_9 = KP + "school_year_9";
	protected final static String KEY_SCHOOL_YEAR_10 = KP + "school_year_10";
	protected final static String KEY_SCHOOL_YEAR = KP + "school_year";
	protected final static String KEY_SUM_1_3 = KP + "sum_1_3";
	protected final static String KEY_SUM_4_6 = KP + "sum_4_6";
	protected final static String KEY_SUM_7_10 = KP + "sum_7_10";
	protected final static String KEY_TOTAL_1_10 = KP + "total_1_10";
	protected final static String KEY_TOTAL_F_10 = KP + "total_f_10";
	protected final static String KEY_SUM_7_9 = KP + "sum_7_9";
	protected final static String KEY_TOTAL_1_9 = KP + "total_1_9";
	protected final static String KEY_TOTAL_F_9 = KP + "total_f_9";
	protected final static String KEY_SUM = KP + "sum";
	protected final static String KEY_TOTAL = KP + "total";
	protected final static String KEY_SHARE = KP + "share";
	protected final static String KEY_ELEMENTARY_SCHOOL = KP + "elementary_school";
	protected final static String KEY_NACKA_COMMUNE = KP + "nacka_commune";
	protected final static String KEY_OTHER_COMMUNES = KP + "other_communes";
	protected final static String KEY_PRIVATE_SCHOOLS = KP + "private_schools";
	protected final static String KEY_FOREIGN_SCHOOLS = KP + "foreign_schools";
	protected final static String KEY_COMPULSORY_SCHOOLS = KP + "compulsory_schools";
	protected final static String KEY_SIX_YEARS_STUDENTS = KP + "six_years_students";
	protected final static String KEY_COUNTY_COUNCIL = KP + "county_council";
	protected final static String KEY_FREE_STANDING = KP + "free_standing";
	protected final static String KEY_COMPULSORY_HIGH_SCHOOLS = KP + "compulsory_high_schools";
	protected final static String KEY_STUDY_PATH_CODE_COMPULSORY_HIGH_SCHOOL = KP + "study_path_code_compulsory_high_school";
	protected final static String KEY_STUDENT_AGE_16 = KP + "student_age_16";
	protected final static String KEY_STUDENT_AGE_17 = KP + "student_age_17";
	protected final static String KEY_STUDENT_AGE_18 = KP + "student_age_18";
	protected final static String KEY_STUDENT_AGE_19 = KP + "student_age_19";
	protected final static String KEY_OTHER_COMMUNE_CITIZENS = KP + "other_commune_citizens";
	protected final static String KEY_TOTAL_1_4 = KP + "total_1_4";
	protected final static String KEY_NACKA_STUDENTS = KP + "nacka_students";
	protected final static String KEY_PROVISIONS_PROGRAM = KP + "provisions_program";
	protected final static String KEY_NUMBER_OF_STUDENTS = KP + "number_of_students";
	protected final static String KEY_STUDY_PATH_CODE = KP + "study_path_code";
	protected final static String KEY_SUM_1_4 = KP + "sum_1_4";

	protected final static String KEY_PRE_SCHOOL_OPERATION = KP + "pre_school_operation";
	protected final static String KEY_PRE_SCHOOL = KP + "pre_school";
	protected final static String KEY_OF_WHICH_COMMUNE_MANAGEMENT = KP + "of_which_commune_management";
	protected final static String KEY_OF_WHICH_PRIVATE_MANAGEMENT = KP + "of_which_private_management";
	protected final static String KEY_FAMILY_DAYCARE = KP + "family_day_daycare";
	protected final static String KEY_COOPERATIVE = KP + "cooperative";
	protected final static String KEY_SCHOOL_CHILDREN_CARE_6_YEAR = KP + "school_children_care_6_year";
	protected final static String KEY_AFTER_SCHOOL_CENTRE = KP + "after_school_centre";
	protected final static String KEY_SCHOOL_CHILDREN_CARE_7_9_YEAR = KP + "school_children_care_7_9_year";
	protected final static String KEY_FAMILY_AFTER_SCHOOL_CENTRE = KP + "family_after_school_centre";
	protected final static String KEY_SUM_CHILDREN = KP + "sum_children";
	protected final static String KEY_COMMUNE_PRE_SCHOOL_OPERATION = KP + "commune_pre_school_operation";
	protected final static String KEY_PRIVATE_COOPERATIVE_PRE_SCHOOL_OPERATION = KP + "private_cooperative_pre_school_operation";
	protected final static String KEY_COMMUNE_SCHOOL_CHILDREN_CARE_6 = KP + "commun_school_children_care_6";
	protected final static String KEY_PRIVATE_COOPERATIVE_SCHOOL_CHILDREN_CARE_6 = KP + "private_cooperative_school_children_care_6";
	protected final static String KEY_COMMUNE_SCHOOL_CHILDREN_CARE_7_9 = KP + "commune_school_children_care_7_9";
	protected final static String KEY_PRIVATE_SCHOOL_CHILDREN_CARE_7_9 = KP + "private_school_children_care_7_9";
	protected final static String KEY_COMMUNE_SUM = KP + "commune_sum";
	protected final static String KEY_PRIVATE_SUM = KP + "private_sum";
	protected final static String KEY_NUMBER_OF_CHILDREN = KP + "number_of_children";
	protected final static String KEY_SHARE_IN_PERCENT = KP + "share_in_percent";
	protected final static String KEY_CHILDREN_TOTAL = KP + "children_total";
	protected final static String KEY_COMMUNE_PRE_SCHOOL_PROVIDERS = KP + "commune_pre_school_providers";
	protected final static String KEY_COMMUNE_AFTER_SCHOOL_PROVIDERS = KP + "commune_after_school_providers";
	protected final static String KEY_COMMUNE_FAMILY_DAYCARE_PROVIDERS = KP + "commune_family_daycare_providers";
	protected final static String KEY_PRIVATE_PRE_SCHOOL_PROVIDERS = KP + "private_pre_school_providers";
	protected final static String KEY_PRIVATE_AFTER_SCHOOL_PROVIDERS = KP + "private_after_school_providers";
	protected final static String KEY_PRIVATE_FAMILY_DAYCARE_PROVIDERS = KP + "private_family_daycare_providers";
	protected final static String KEY_PROVIDER_TOTAL = KP + "provider_total";
	protected final static String KEY_NUMBER_OF_PROVIDERS = KP + "number_of_providers";
	protected final static String KEY_COMMUNE_UNITS = KP + "commune_units";
	protected final static String KEY_PRIVATE_UNITS = KP + "private_units";
	protected final static String KEY_CHILDREN_IN_FAMILY_DAYCARE = KP + "children_in_family_daycare";
	protected final static String KEY_MEAN_WEEK_HOURS = KP + "mean_week_hours";
	protected final static String KEY_PARENTAL_LEAVE = KP + "parental_leave";
	protected final static String KEY_JOB_CANDIDATE = KP + "job_candidate";
	protected final static String KEY_GENERAL_PRE_SCHOOL = KP + "general_pre_school";
	protected final static String KEY_SUM_CHILD_CARE_HOURS = KP + "sum_child_care_hours";
	protected final static String KEY_CHILD_CARE_HOURS = KP + "child_care_hours";
	protected final static String KEY_SCHOOL_CHILDREN_CARE = KP + "school_children_care";
	protected final static String KEY_TOTAL_PRE_SCHOOL_OPERATION = KP + "total_pre_school_operation";
	protected final static String KEY_AFTER_SCHOOL = KP + "after_school";
	protected final static String KEY_FAMILY_AFTER_SCHOOL = KP + "family_after_school";
	protected final static String KEY_TOTAL_AFTER_SCHOOL_OPERATION = KP + "total_after_school_operation";
	protected final static String KEY_SCHOOL_CHILDREN_CARE_INCLUDING_FAMILY_AFTER_SCHOOL = KP + "school_children_care_including";	
	//protected final static String KEY_GTEQ_36 = KP + "gteq_36";
	
	protected final static String KEY_GTEQ_40 = KP + "gteq_40";

	protected final static String KEY_COMMUNE_PROVIDERS = KP + "commune_providers";
	protected final static String KEY_PRE_SCHOOLS = KP + "pre_schools";
	protected final static String KEY_FAMILY_DAYCARE_CENTERS = KP + "key_family_daycare_centers";
	protected final static String KEY_WITHIN_PRE_SCHOOL_OPERATION = KP + "within_pre_school_operation";
	protected final static String KEY_WITHIN_AFTER_SCHOOL_OPERATION = KP + "within_after_school_operation";
	protected final static String KEY_AFTER_SCHOOL_CENTERS_6_9 = KP + "after_school_centers_6_9";
	protected final static String KEY_ELEMENTARY_SCHOOLS = KP + "elementary_schools";
	protected final static String KEY_WITHIN_ELEMENTARY_SCHOOL = KP + "within_elementary_school";
	protected final static String KEY_WITHIN_PRE_SCHOOL_CLASS = KP + "within_pre_school_class";
	protected final static String KEY_HIGH_SCHOOLS = KP + "high_schools";
	protected final static String KEY_PRIVATE_FREESTANDING_PROVIDERS = KP + "private_freestanding_providers";
	protected final static String KEY_CHILDREN_STUDENTS = KP + "children_students";
	protected final static String KEY_OF_WHICH_FROM_OTHER_COMMUNES = KP + "of_which_from_other_communes";
	protected final static String KEY_NUMBER_OF_PROVIDERS_CHILDCARE = KP + "number_of_providers_childcare";
	
	/**
	 * Constructs a report model with the specified report business logic. 
	 */	
	public ReportModel(ReportBusiness reportBusiness) {
		_reportBusiness = reportBusiness;
		_queryCache = new HashMap();
	}

	/**
	 * Constructs a report model with the specified size and report business logic. 
	 */	
	public ReportModel(int rowSize, int columnSize, ReportBusiness reportBusiness) {
		this(reportBusiness);
		setReportSize(rowSize, columnSize);
	}
	
	/**
	 * Sets the size of this report. 
	 */
	protected void setReportSize(int rowSize, int columnSize) {
		_rowSize = rowSize;
		_columnSize = columnSize;
	}

	/*
	 * Initializes the model information (headers and cells).
	 */
	private void init() {
		if (_cells == null) {
			initReportSize();
			_cells = new Cell[_rowSize][_columnSize];
			_rowHeaders = buildRowHeaders();
			_columnHeaders = buildColumnHeaders();
			buildCells();							
		}
	}
	
	/**
	 * Returns the number of rows in this report model. 
	 */
	public int getRowSize() {
		return _rowSize;
	}
	
	/**
	 * Returns the number of columns in this report model. 
	 */
	public int getColumnSize() {
		return _columnSize;
	}
	
	/**
	 * Returns the report business for this report model.
	 */
	ReportBusiness getReportBusiness() {
		return _reportBusiness;
	}
	
	/**
	 * Returns the row headers for this report model. 
	 */
	public Header[] getRowHeaders() {
		init();
		return _rowHeaders;
	}
	
	/**
	 * Returns the column headers for this report model. 
	 */
	public Header[] getColumnHeaders() {
		init();
		return _columnHeaders;
	}
	
	/**
	 * Returns the cell for the specified position. 
	 */
	public Cell getCell(int row, int column) {
		init();
		return _cells[row][column];
	}

	/**
	 * Logs the specified message on the default system log. 
	 */
	public void log(String msg) {
		try {
			_reportBusiness.log(msg);
		} catch (RemoteException e) {
			// Failed to access the system log, using System.err to log message
			System.err.println(msg);
		}
	}
	
	/**
	 * Returns a database connection. 
	 */
	protected Connection getConnection() {
		if (_connection == null) {
			_connection = ConnectionBroker.getConnection();
		}
		return _connection;
	}
	
	/**
	 * Returns the prepared query for the specified key. 
	 */
	protected PreparedQuery getQuery(String key) {
		return (PreparedQuery) _queryCache.get(key);
	}
	
	/**
	 * Sets the prepared query with the specified key. 
	 */
	protected void setQuery(String key, PreparedQuery query) {
		_queryCache.put(key, query);
	}
	
	/**
	 * Sets the cell for the specified position. 
	 */
	protected void setCell(int row, int column, Cell cell) {
		init();
		_cells[row][column] = cell;
	}

	/**
	 * Closes used resources for this report model.
	 */
	public void close() {
		Iterator iter = _queryCache.values().iterator();
		while (iter.hasNext()) {
			PreparedQuery query = (PreparedQuery) iter.next();
			query.close();
		}
		if (_connection != null) {
			ConnectionBroker.freeConnection(_connection);
		}
		_queryCache = new HashMap();
		_connection = null;
	}
	
	/**
	 * Initializes the report size (rows, columns). 
	 */
	abstract protected void initReportSize();
	
	/**
	 * Builds the report model row headers. 
	 */
	abstract protected Header[] buildRowHeaders();
	
	/**
	 * Builds the report model column headers. 
	 */
	abstract protected Header[] buildColumnHeaders();
	
	/**
	 * Builds the report model cells. 
	 */
	abstract protected void buildCells();
	
	/**
	 * Returns the title for this report in the form of a localization key. 
	 */
	abstract public String getReportTitleLocalizationKey();
	
	/**
	 * Returns the calculated value for the specified cell.
	 * @throws RemoteException if error occurred during calculation
	 */
	abstract protected float calculate(Cell cell) throws RemoteException;
}
