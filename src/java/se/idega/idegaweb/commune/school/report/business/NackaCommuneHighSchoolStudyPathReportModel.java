/*
 * $Id: NackaCommuneHighSchoolStudyPathReportModel.java,v 1.13 2004/02/25 12:15:44 anders Exp $
 *
 * Copyright (C) 2003 Agura IT. All Rights Reserved.
 *
 * This software is the proprietary information of Agura IT AB.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.school.report.business;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.TreeMap;

import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolStudyPath;

/** 
 * Report model for Nacka high school student placements with all study paths listed.
 * <p>
 * Last modified: $Date: 2004/02/25 12:15:44 $ by $Author: anders $
 *
 * @author Anders Lindman
 * @version $Revision: 1.13 $
 */
public class NackaCommuneHighSchoolStudyPathReportModel extends ReportModel {

	private final static int COLUMN_SIZE = 18;
	
	private final static int ROW_METHOD_STUDY_PATH = 1;
	private final static int ROW_METHOD_TOTAL = 2;

	private final static int COLUMN_METHOD_SCHOOL_1 = 101;
	private final static int COLUMN_METHOD_SCHOOL_2 = 102;
	private final static int COLUMN_METHOD_SCHOOL_3 = 103;
	private final static int COLUMN_METHOD_TOTAL = 104;
	private final static int COLUMN_METHOD_OTHER_COMMUNE_CITIZENS = 105;
	private final static int COLUMN_METHOD_NACKA_CITIZENS = 106;

	private final static String QUERY_ALL = "all";
	private final static String QUERY_NACKA_GYMNASIUM = "nacka_gymnasium";
	private final static String QUERY_OTHER_COMMUNES = "other_communes";
	private final static String QUERY_NACKA_COMMUNE = "nacka_commune";
	private final static String QUERY_STUDY_PATH = "study_path";
	private final static String QUERY_STUDY_PATH_AMOUNT = "study_path_amount";

	private final static String KEY_REPORT_TITLE = KP + "title_nacka_commune_high_school_all_study_paths";
	
	private Collection _studyPaths = null;
	private School[] _schools = null;
	private School[] _nackaGymnasium = null;
	private int[] _subtractRow = null;

	/**
	 * Constructs this report model.
	 * @param reportBusiness the report business instance for calculating cell values
	 */
	public NackaCommuneHighSchoolStudyPathReportModel(ReportBusiness reportBusiness) {
		super(reportBusiness);
	}
	
	/**
	 * @see se.idega.idegaweb.commune.school.report.business.ReportModel#initReportSize()
	 */
	protected void initReportSize() {
		try {
			Collection studyPaths = getStudyPaths();
			int rowSize = 0;
			rowSize += studyPaths.size() + 1; 
			setReportSize(rowSize, COLUMN_SIZE);
		} catch (RemoteException e) {
			log(e.getMessage());
		}
	}
	
	/**
	 * @see se.idega.idegaweb.commune.school.report.business.ReportModel#buildRowHeaders()
	 */
	protected Header[] buildRowHeaders() {
		Header[] headers = null;
		
		try {
			Collection studyPaths = getStudyPaths();
			headers = new Header[studyPaths.size() + 1];
			Iterator iter = studyPaths.iterator();
			int headerIndex = 0;
			while (iter.hasNext()) {
				SchoolStudyPath studyPath = (SchoolStudyPath) iter.next();
				headers[headerIndex] = new Header(studyPath.getDescription(), Header.HEADERTYPE_ROW_NONLOCALIZED_HEADER, 1);
				Header child = new Header(studyPath.getCode(), Header.HEADERTYPE_ROW_NONLOCALIZED_NORMAL);
				headers[headerIndex].setChild(0, child);
				headerIndex++;
			}
			
			Header header = new Header(KEY_TOTAL, Header.HEADERTYPE_ROW_TOTAL);
			headers[headerIndex] = header;
			headerIndex++;

		} catch (RemoteException e) {
			log(e.getMessage());
		}
		
		return headers;
	}
	
	/**
	 * @see se.idega.idegaweb.commune.school.report.business.ReportModel#buildColumnHeaders()
	 */
	protected Header[] buildColumnHeaders() {
		Header[] headers = new Header[6];
		int headerColumn = 0;
		try {
			ReportBusiness rb = getReportBusiness();
			Collection schools = rb.getNackaCommuneHighSchools();
			_schools = new School[3];
			_nackaGymnasium = new School[3];
			int schoolIndex = 0;
			int nackaGymnasiumIndex = 0;
			Iterator iter = schools.iterator();
			boolean nackaGymnasiumFound = false;
			while (iter.hasNext()) {
				School school = (School) iter.next();
				_schools[schoolIndex] = school;
				String name = school.getName();
				if (name.length() > 15 && name.substring(0, 15).equals("Nacka Gymnasium")) {
					_nackaGymnasium[nackaGymnasiumIndex++] = school;
					if (nackaGymnasiumFound) {
						continue;
					} else {
						name = "Nacka Gymnasium";
						nackaGymnasiumFound = true;
					}
				}
				schoolIndex++;
				Header h = new Header(name, Header.HEADERTYPE_COLUMN_NONLOCALIZED_HEADER, 4);
				Header child0 = new Header(KEY_SCHOOL_YEAR_1, Header.HEADERTYPE_COLUMN_NORMAL);
				Header child1 = new Header(KEY_SCHOOL_YEAR_2, Header.HEADERTYPE_COLUMN_NORMAL);
				Header child2 = new Header(KEY_SCHOOL_YEAR_3, Header.HEADERTYPE_COLUMN_NORMAL);
				Header child3 = new Header(KEY_SUM_1_3, Header.HEADERTYPE_COLUMN_NORMAL);
				h.setChild(0, child0);
				h.setChild(1, child1);
				h.setChild(2, child2);
				h.setChild(3, child3);
				headers[headerColumn++] = h;
			}
		} catch (RemoteException e) {
			log(e.getMessage());
		}
		
		Header h = new Header(KEY_TOTAL, Header.HEADERTYPE_COLUMN_HEADER, 4);
		Header child0 = new Header(KEY_SCHOOL_YEAR_1, Header.HEADERTYPE_COLUMN_NORMAL);
		Header child1 = new Header(KEY_SCHOOL_YEAR_2, Header.HEADERTYPE_COLUMN_NORMAL);
		Header child2 = new Header(KEY_SCHOOL_YEAR_3, Header.HEADERTYPE_COLUMN_NORMAL);
		Header child3 = new Header(KEY_SUM_1_3, Header.HEADERTYPE_COLUMN_NORMAL);
		h.setChild(0, child0);
		h.setChild(1, child1);
		h.setChild(2, child2);
		h.setChild(3, child3);
		headers[headerColumn++] = h;
		
		h = new Header(KEY_OF_WHICH_FROM_OTHER_COMMUNES, Header.HEADERTYPE_COLUMN_HEADER);
		headers[headerColumn++] = h;
		
		h = new Header(KEY_NACKA_STUDENTS, Header.HEADERTYPE_COLUMN_HEADER);
		headers[headerColumn++] = h;
				
		return headers;
	}
	
	/**
	 * @see se.idega.idegaweb.commune.school.report.business.ReportModel#buildCells()
	 */
	protected void buildCells() {
		for (int column = 0; column < getColumnSize(); column++) {
			int row = 0;
			int columnMethod = 0;
			String columnParameter = null;
			int cellType = Cell.CELLTYPE_NORMAL;
			switch (column) {
				case 0:
					columnMethod = COLUMN_METHOD_SCHOOL_1;
					columnParameter = "G1";
					break;
				case 1:
					columnMethod = COLUMN_METHOD_SCHOOL_1;
					columnParameter = "G2";
					break;
				case 2:
					columnMethod = COLUMN_METHOD_SCHOOL_1;
					columnParameter = "G3";
					break;
				case 3:
					columnMethod = COLUMN_METHOD_SCHOOL_1;
					columnParameter = null;
					cellType = Cell.CELLTYPE_SUM;
					break;
				case 4:
					columnMethod = COLUMN_METHOD_SCHOOL_2;
					columnParameter = "G1";
					break;
				case 5:
					columnMethod = COLUMN_METHOD_SCHOOL_2;
					columnParameter = "G2";
					break;
				case 6:
					columnMethod = COLUMN_METHOD_SCHOOL_2;
					columnParameter = "G3";
					break;
				case 7:
					columnMethod = COLUMN_METHOD_SCHOOL_2;
					columnParameter = null;
					cellType = Cell.CELLTYPE_SUM;
					break;
				case 8:
					columnMethod = COLUMN_METHOD_SCHOOL_3;
					columnParameter = "G1";
					break;
				case 9:
					columnMethod = COLUMN_METHOD_SCHOOL_3;
					columnParameter = "G2";
					break;
				case 10:
					columnMethod = COLUMN_METHOD_SCHOOL_3;
					columnParameter = "G3";
					break;
				case 11:
					columnMethod = COLUMN_METHOD_SCHOOL_3;
					columnParameter = null;
					cellType = Cell.CELLTYPE_SUM;
					break;
				case 12:
					columnMethod = COLUMN_METHOD_TOTAL;
					columnParameter = "G1";
					break;
				case 13:
					columnMethod = COLUMN_METHOD_TOTAL;
					columnParameter = "G2";
					break;
				case 14:
					columnMethod = COLUMN_METHOD_TOTAL;
					columnParameter = "G3";
					break;
				case 15:
					columnMethod = COLUMN_METHOD_TOTAL;
					columnParameter = null;
					cellType = Cell.CELLTYPE_TOTAL;
					break;
				case 16:
					columnMethod = COLUMN_METHOD_OTHER_COMMUNE_CITIZENS;
					columnParameter = null;
					break;
				case 17:
					columnMethod = COLUMN_METHOD_NACKA_CITIZENS;
					columnParameter = null;
					break;
			}
			
			try {
				Collection studyPaths = getStudyPaths();
				Iterator iter = studyPaths.iterator();
				while (iter.hasNext()) {
					SchoolStudyPath studyPath = (SchoolStudyPath) iter.next();
					Object rowParameter = studyPath.getCode();
					Cell cell = new Cell(this, row, column, ROW_METHOD_STUDY_PATH,
							columnMethod, rowParameter, columnParameter, cellType);
					setCell(row, column, cell);
					row++;
				}
				Cell cell = new Cell(this, row, column, ROW_METHOD_TOTAL,
						columnMethod, null, columnParameter, Cell.CELLTYPE_TOTAL);
				setCell(row, column, cell);
				row++;
			} catch (RemoteException e) {
				log(e.getMessage());
			}
		}
	}
	
	/**
	 * @see se.idega.idegaweb.commune.school.report.business.ReportModel#calculate()
	 */
	protected float calculate(Cell cell) throws RemoteException {
		float value = 0f;
		String studyPathPrefix = null;
		if (cell.getRowParameter() != null) {
			studyPathPrefix = (String) cell.getRowParameter();
		}
		String schoolYearName = (String) cell.getColumnParameter();
		int row = cell.getRow();
		int column = cell.getColumn();
		
		switch (cell.getRowMethod()) {
			case ROW_METHOD_STUDY_PATH:
				switch (cell.getColumnMethod()) {
					case COLUMN_METHOD_SCHOOL_1:
						if (schoolYearName == null) {
							value += getCell(row, 0).getFloatValue();
							value += getCell(row, 1).getFloatValue();
							value += getCell(row, 2).getFloatValue();
						} else {
							value = getHighSchoolNackaCommunePlacementCount(_schools[0], schoolYearName, studyPathPrefix);
							value -= subtractRows(row, column);
						}
						break;
					case COLUMN_METHOD_SCHOOL_2:
						if (schoolYearName == null) {
							value += getCell(row, 4).getFloatValue();
							value += getCell(row, 5).getFloatValue();
							value += getCell(row, 6).getFloatValue();
						} else {
							value = getNackaGymnasiumPlacementCount(schoolYearName, studyPathPrefix);
							value -= subtractRows(row, column);
						}
						break;
					case COLUMN_METHOD_SCHOOL_3:
						if (schoolYearName == null) {
							value += getCell(row, 8).getFloatValue();
							value += getCell(row, 9).getFloatValue();
							value += getCell(row, 10).getFloatValue();
						} else {
							value = getHighSchoolNackaCommunePlacementCount(_schools[2], schoolYearName, studyPathPrefix);
							value -= subtractRows(row, column);
						}
						break;
					case COLUMN_METHOD_TOTAL:
						for (int i = column % 4; i < 12; i += 4) {
							value += getCell(row, i).getFloatValue();
						}
						break;
					case COLUMN_METHOD_OTHER_COMMUNE_CITIZENS:
						value = getHighSchoolOCCPlacementCount(studyPathPrefix);
						value -= subtractRows(row, column);
						break;
					case COLUMN_METHOD_NACKA_CITIZENS:
						value = getHighSchoolNackaCitizenPlacementCount(studyPathPrefix);
						value -= subtractRows(row, column);
						break;
				}
				break;
			case ROW_METHOD_TOTAL:
				for (int i = 0; i < row; i++) {
					Cell c = getCell(i, column);
					value += c.getFloatValue();
				}
				break;
		}
		
		return value;
	}

	/*
	 * Subtract sub study path rows. 
	 */
	private int subtractRows(int row, int column) {
		int subtract = 0;
		if (_subtractRow[row] != -1) {
			return 0;
		}
		for (int i = 0; i < _subtractRow.length; i++) {
			int subtractRow = _subtractRow[i];
			if (row == subtractRow) {
				Cell cell = getCell(i, column);
				subtract += cell.getValue();
			}
			
		}
		return subtract;
	}

	/**
	 * @see se.idega.idegaweb.commune.school.report.business.ReportModel#getReportTitleLocalizationKey()
	 */
	public String getReportTitleLocalizationKey() {
		return KEY_REPORT_TITLE;
	}
	
	/*
	 * Returns the value from executing the specified study path query.
	 */
	private int executeStudyPathQuery(int parameterIndex, String studyPathPrefix, PreparedQuery query) {
		if (studyPathPrefix.length() == 2) {
			studyPathPrefix += "%";
		}
		query.setString(parameterIndex, studyPathPrefix);
		return query.execute();
	}
	
	/**
	 * Returns the number of student placements for high schools
	 * in Nacka commune for the specified school year.
	 */
	protected int getHighSchoolNackaCommunePlacementCount(School school, String schoolYearName, String studyPathPrefix) throws RemoteException {
		PreparedQuery query = null;
		ReportBusiness rb = getReportBusiness();
		query = getQuery(QUERY_ALL);
		if (query == null) {
			query = new PreparedQuery(getConnection());
			query.setSelectCountDistinctUsers();
			query.setPlacements(rb.getSchoolSeasonId());
			query.setSchoolTypeHighSchool();
			query.setSchool(); // parameter 1
			query.setSchoolYearName(); // parameter 2
			query.setStudyPathPrefix(); // parameter 3
			query.prepare();
			setQuery(QUERY_ALL, query);
		}
		query.setInt(1, ((Integer) school.getPrimaryKey()).intValue());
		query.setString(2, schoolYearName);
		return executeStudyPathQuery(3, studyPathPrefix, query);
	}
	
	/**
	 * Returns the number of student placements for Nacka Gymnasium
	 * in Nacka commune for the specified school year.
	 */
	protected int getNackaGymnasiumPlacementCount(String schoolYearName, String studyPathPrefix) throws RemoteException {
		PreparedQuery query = null;
		ReportBusiness rb = getReportBusiness();
		query = getQuery(QUERY_NACKA_GYMNASIUM);
		if (query == null) {
			query = new PreparedQuery(getConnection());
			query.setSelectCountDistinctUsers();
			query.setPlacements(rb.getSchoolSeasonId());
			query.setSchoolTypeHighSchool();
			query.setSchools(_nackaGymnasium);
			query.setSchoolYearName(); // parameter 1
			query.setStudyPathPrefix(); // parameter 2
			query.prepare();
			setQuery(QUERY_NACKA_GYMNASIUM, query);
		}
		query.setString(1, schoolYearName);
		return executeStudyPathQuery(2, studyPathPrefix, query);
	}
	
	/**
	 * Returns the number of student placements for Nacka commune high schools
	 * for the specified study path prefix.
	 * Only citizens outside Nacka commune are counted. 
	 */
	protected int getHighSchoolOCCPlacementCount(String studyPathPrefix) throws RemoteException{
		PreparedQuery query = null;
		ReportBusiness rb = getReportBusiness();
		query = getQuery(QUERY_OTHER_COMMUNES);
		if (query == null) {
			query = new PreparedQuery(getConnection());
			query.setSelectCountDistinctUsers();
			query.setPlacements(rb.getSchoolSeasonId());
			query.setNotNackaCitizens();
			query.setSchoolTypeHighSchool();
			query.setSchools(rb.getNackaCommuneHighSchools());
			query.setStudyPathPrefix(); // parameter 1
			query.prepare();
			setQuery(QUERY_OTHER_COMMUNES, query);
		}
		return executeStudyPathQuery(1, studyPathPrefix, query);
	}	
	
	/**
	 * Returns the number of student placements for Nacka commune high schools
	 * in Nacka commune for the specified school year.
	 * Only citizens in Nacka commune are counted. 
	 */
	protected int getHighSchoolNackaCitizenPlacementCount(String studyPathPrefix) throws RemoteException{
		PreparedQuery query = null;
		ReportBusiness rb = getReportBusiness();
		query = getQuery(QUERY_NACKA_COMMUNE);
		if (query == null) {
			query = new PreparedQuery(getConnection());
			query.setSelectCountDistinctUsers();
			query.setPlacements(rb.getSchoolSeasonId());
			query.setOnlyNackaCitizens();
			query.setSchoolTypeHighSchool();
			query.setSchools(rb.getNackaCommuneHighSchools());
			query.setStudyPathPrefix(); // parameter 1
			query.prepare();
			setQuery(QUERY_NACKA_COMMUNE, query);
		}
		return executeStudyPathQuery(1, studyPathPrefix, query);
	}	
	
	/**
	 * Returns the number of student placements for the specified study path code.
	 */
	protected int getHighSchoolPlacementCount(String studyPathCode) throws RemoteException {
		PreparedQuery query = null;
		ReportBusiness rb = getReportBusiness();
		Collection schools = rb.getNackaCommuneHighSchools();
		query = getQuery(QUERY_STUDY_PATH);
		if (query == null) {
			query = new PreparedQuery(getConnection());
			query.setSelectCountDistinctUsers();
			query.setPlacements(rb.getSchoolSeasonId());
			query.setSchools(schools);
			query.setSchoolTypeHighSchool();
			query.setStudyPathPrefix(); // parameter 1
			query.prepare();
			setQuery(QUERY_STUDY_PATH, query);
		}
		return executeStudyPathQuery(1, studyPathCode, query);
	}
	
	/*
	 * Returns a filtered list with only Nacka study paths. 
	 */
	private Collection getStudyPaths() throws RemoteException {
		if (_studyPaths == null) {
			_studyPaths = new ArrayList();
			ReportBusiness rb = getReportBusiness();
			Collection c = rb.getAllStudyPathsIncludingDirections();
			TreeMap map = new TreeMap();
			_subtractRow = new int[c.size()];
			for (int i = 0; i < _subtractRow.length; i++) {
				_subtractRow[i] = -1;
			}
			Iterator iter = c.iterator();
			while (iter.hasNext()) {
				SchoolStudyPath sp = (SchoolStudyPath) iter.next();
				String code = sp.getCode();
				if (code.equals("HV") && !code.equals("SM")) {
					map.put(sp.getCode(), sp);					
				}
			}
			iter = map.values().iterator();
			int studyPathAmount = 0;
			int row = 0;
			int studyPathRow = -1;
			String studyPathCode = "";
			while (iter.hasNext()) {
				SchoolStudyPath sp = (SchoolStudyPath) iter.next();
				String code = sp.getCode();
				int studyPathId = ((Integer) sp.getPrimaryKey()).intValue();
				int count = getHighSchoolPlacementCount(code);
				if (count > 0) {
					if (code.length() == 2) {
						studyPathAmount = getStudyPathAmount(studyPathId);
						_studyPaths.add(sp);
						studyPathRow = row;
						studyPathCode = code;
						row++;
					} else {
						int subStudyPathAmount = getStudyPathAmount(studyPathId);
						String description = sp.getDescription();
						String subCode = code.substring(0, 2);
						if (!subCode.equals(studyPathCode) ||
								(((studyPathAmount != subStudyPathAmount) && (subStudyPathAmount != -1)) 
								|| description.indexOf("lokal inriktning") != -1)) {
							_studyPaths.add(sp);
							if (subCode.equals(studyPathCode)) {
								_subtractRow[row] = studyPathRow;
							} else {
								studyPathRow = -1;
								studyPathCode = "";
							}
							row++;
						}
					}
				}
			}			
		}
		return _studyPaths;
	}
	
	/**
	 * Returns the amount for the specified study path code.
	 */
	protected int getStudyPathAmount(int studyPathId) {
		PreparedQuery query = null;
		query = getQuery(QUERY_STUDY_PATH_AMOUNT);
		if (query == null) {
			query = new PreparedQuery(getConnection());
			query.setSelectMaxStudyPathAmount();
			query.setStudyPathAmount(); // parameter 1
			query.prepare();
			setQuery(QUERY_STUDY_PATH_AMOUNT, query);
		}
		query.setInt(1, studyPathId);
		return query.execute();
	}
}
