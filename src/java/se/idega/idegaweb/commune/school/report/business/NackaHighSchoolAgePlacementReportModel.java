/*
 * $Id: NackaHighSchoolAgePlacementReportModel.java,v 1.18 2004/09/08 09:17:19 anders Exp $
 *
 * Copyright (C) 2003 Agura IT. All Rights Reserved.
 *
 * This software is the proprietary information of Agura IT AB.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.school.report.business;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import com.idega.block.school.data.SchoolStudyPath;

/** 
 * Report model for high school placements per student age for students in Nacka.
 * <p>
 * Last modified: $Date: 2004/09/08 09:17:19 $ by $Author: anders $
 *
 * @author Anders Lindman
 * @version $Revision: 1.18 $
 */
public class NackaHighSchoolAgePlacementReportModel extends ReportModel {

	private final static int COLUMN_SIZE = 25;
	
	private final static int ROW_METHOD_STUDY_PATH = 1;
	private final static int ROW_METHOD_SHARE = 2;
	private final static int ROW_METHOD_TOTAL = 3;

	private final static int COLUMN_METHOD_NACKA_COMMUNE = 101;
	private final static int COLUMN_METHOD_OTHER_COMMUNES = 102;
	private final static int COLUMN_METHOD_COUNTY_COUNCIL = 103;
	private final static int COLUMN_METHOD_FREE_STANDING = 104;
	private final static int COLUMN_METHOD_TOTAL = 105;

	private final static String QUERY_NACKA_COMMUNE = "nacka_commune";
	private final static String QUERY_OTHER_COMMUNES = "other_communes";
	private final static String QUERY_COUNTY_COUNCIL = "county_council";
	private final static String QUERY_PRIVATE = "private";
	
	private final static String KEY_REPORT_TITLE = KP + "title_nacka_high_school_age_placements";

	private final static int COMPULSORY_AGE_OFFSET = 100;
	
	/**
	 * Constructs this report model.
	 * @param reportBusiness the report business instance for calculating cell values
	 */
	public NackaHighSchoolAgePlacementReportModel(ReportBusiness reportBusiness) {
		super(reportBusiness);
	}
	
	/**
	 * @see se.idega.idegaweb.commune.school.report.business.ReportModel#initReportSize()
	 */
	protected void initReportSize() {
		try {
			Collection studyPaths = getReportBusiness().getAllStudyPaths();
			int rowSize = 0;
			rowSize += studyPaths.size() + 4; 
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
			ReportBusiness rb = getReportBusiness();
			Collection studyPaths = rb.getAllStudyPaths();
			headers = new Header[studyPaths.size() + 4];
			Iterator iter = studyPaths.iterator();
			int headerIndex = 0;
			while (iter.hasNext()) {
				SchoolStudyPath studyPath = (SchoolStudyPath) iter.next();
				headers[headerIndex] = new Header(studyPath.getDescription(), Header.HEADERTYPE_ROW_NONLOCALIZED_HEADER, 1);
				Header child = new Header(studyPath.getCode(), Header.HEADERTYPE_ROW_NONLOCALIZED_NORMAL);
				headers[headerIndex].setChild(0, child);
				headerIndex++;
				if (studyPath.getCode().equals("IB")) {
					headers[headerIndex] = new Header(KEY_PROVISIONS_PROGRAM, Header.HEADERTYPE_ROW_HEADER, 1);
					child = new Header("LP", Header.HEADERTYPE_ROW_NONLOCALIZED_NORMAL);
					headers[headerIndex].setChild(0, child);
					headerIndex++;
				}
			}
			Header header = new Header(KEY_COMPULSORY_HIGH_SCHOOLS, Header.HEADERTYPE_ROW_HEADER, 1);
			Header child = new Header(KEY_STUDY_PATH_CODE_COMPULSORY_HIGH_SCHOOL, Header.HEADERTYPE_ROW_NORMAL);
			header.setChild(0, child);
			headers[headerIndex] = header;
			headerIndex++;
			
			header = new Header(KEY_TOTAL, Header.HEADERTYPE_ROW_TOTAL);
			headers[headerIndex] = header;
			headerIndex++;
			
			header = new Header(KEY_SHARE, Header.HEADERTYPE_ROW_NORMAL);
			headers[headerIndex] = header;
		} catch (RemoteException e) {
			log(e.getMessage());
		}
		
		return headers;
	}
	
	/**
	 * @see se.idega.idegaweb.commune.school.report.business.ReportModel#buildColumnHeaders()
	 */
	protected Header[] buildColumnHeaders() {
		Header[] headers = new Header[5];
		
		Header h = new Header(KEY_NACKA_COMMUNE, Header.HEADERTYPE_COLUMN_HEADER, 5);
		Header child0 = new Header(KEY_STUDENT_AGE_16, Header.HEADERTYPE_COLUMN_NORMAL);
		Header child1 = new Header(KEY_STUDENT_AGE_17, Header.HEADERTYPE_COLUMN_NORMAL);
		Header child2 = new Header(KEY_STUDENT_AGE_18, Header.HEADERTYPE_COLUMN_NORMAL);
		Header child3 = new Header(KEY_STUDENT_AGE_19, Header.HEADERTYPE_COLUMN_NORMAL);
		Header child4 = new Header(KEY_SUM, Header.HEADERTYPE_COLUMN_NORMAL);
		h.setChild(0, child0);
		h.setChild(1, child1);
		h.setChild(2, child2);
		h.setChild(3, child3);
		h.setChild(4, child4);
		headers[0] = h;
		
		h = new Header(KEY_OTHER_COMMUNES, Header.HEADERTYPE_COLUMN_HEADER, 5);
		child0 = new Header(KEY_STUDENT_AGE_16, Header.HEADERTYPE_COLUMN_NORMAL);
		child1 = new Header(KEY_STUDENT_AGE_17, Header.HEADERTYPE_COLUMN_NORMAL);
		child2 = new Header(KEY_STUDENT_AGE_18, Header.HEADERTYPE_COLUMN_NORMAL);
		child3 = new Header(KEY_STUDENT_AGE_19, Header.HEADERTYPE_COLUMN_NORMAL);
		child4 = new Header(KEY_SUM, Header.HEADERTYPE_COLUMN_NORMAL);
		h.setChild(0, child0);
		h.setChild(1, child1);
		h.setChild(2, child2);
		h.setChild(3, child3);
		h.setChild(4, child4);
		headers[1] = h;
		
		h = new Header(KEY_COUNTY_COUNCIL, Header.HEADERTYPE_COLUMN_HEADER, 5);
		child0 = new Header(KEY_STUDENT_AGE_16, Header.HEADERTYPE_COLUMN_NORMAL);
		child1 = new Header(KEY_STUDENT_AGE_17, Header.HEADERTYPE_COLUMN_NORMAL);
		child2 = new Header(KEY_STUDENT_AGE_18, Header.HEADERTYPE_COLUMN_NORMAL);
		child3 = new Header(KEY_STUDENT_AGE_19, Header.HEADERTYPE_COLUMN_NORMAL);
		child4 = new Header(KEY_SUM, Header.HEADERTYPE_COLUMN_NORMAL);
		h.setChild(0, child0);
		h.setChild(1, child1);
		h.setChild(2, child2);
		h.setChild(3, child3);
		h.setChild(4, child4);
		headers[2] = h;
		
		h = new Header(KEY_FREE_STANDING, Header.HEADERTYPE_COLUMN_HEADER, 5);
		child0 = new Header(KEY_STUDENT_AGE_16, Header.HEADERTYPE_COLUMN_NORMAL);
		child1 = new Header(KEY_STUDENT_AGE_17, Header.HEADERTYPE_COLUMN_NORMAL);
		child2 = new Header(KEY_STUDENT_AGE_18, Header.HEADERTYPE_COLUMN_NORMAL);
		child3 = new Header(KEY_STUDENT_AGE_19, Header.HEADERTYPE_COLUMN_NORMAL);
		child4 = new Header(KEY_SUM, Header.HEADERTYPE_COLUMN_NORMAL);
		h.setChild(0, child0);
		h.setChild(1, child1);
		h.setChild(2, child2);
		h.setChild(3, child3);
		h.setChild(4, child4);
		headers[3] = h;
				
		h = new Header(KEY_TOTAL, Header.HEADERTYPE_COLUMN_HEADER, 5);
		child0 = new Header(KEY_STUDENT_AGE_16, Header.HEADERTYPE_COLUMN_NORMAL);
		child1 = new Header(KEY_STUDENT_AGE_17, Header.HEADERTYPE_COLUMN_NORMAL);
		child2 = new Header(KEY_STUDENT_AGE_18, Header.HEADERTYPE_COLUMN_NORMAL);
		child3 = new Header(KEY_STUDENT_AGE_19, Header.HEADERTYPE_COLUMN_NORMAL);
		child4 = new Header(KEY_TOTAL, Header.HEADERTYPE_COLUMN_NORMAL);
		h.setChild(0, child0);
		h.setChild(1, child1);
		h.setChild(2, child2);
		h.setChild(3, child3);
		h.setChild(4, child4);
		headers[4] = h;
				
		return headers;
	}
	
	/**
	 * @see se.idega.idegaweb.commune.school.report.business.ReportModel#buildCells()
	 */
	protected void buildCells() {
		for (int column = 0; column < getColumnSize(); column++) {
			int row = 0;
			int columnMethod = 0;
			Integer columnParameter = null;
			int cellType = Cell.CELLTYPE_NORMAL;
			switch (column) {
				case 0:
					columnMethod = COLUMN_METHOD_NACKA_COMMUNE;
					columnParameter = new Integer(16);
					break;
				case 1:
					columnMethod = COLUMN_METHOD_NACKA_COMMUNE;
					columnParameter = new Integer(17);
					break;
				case 2:
					columnMethod = COLUMN_METHOD_NACKA_COMMUNE;
					columnParameter = new Integer(18);
					break;
				case 3:
					columnMethod = COLUMN_METHOD_NACKA_COMMUNE;
					columnParameter = new Integer(19);
					break;
				case 4:
					columnMethod = COLUMN_METHOD_NACKA_COMMUNE;
					columnParameter = null;
					cellType = Cell.CELLTYPE_SUM;
					break;
				case 5:
					columnMethod = COLUMN_METHOD_OTHER_COMMUNES;
					columnParameter = new Integer(16);
					break;
				case 6:
					columnMethod = COLUMN_METHOD_OTHER_COMMUNES;
					columnParameter = new Integer(17);
					break;
				case 7:
					columnMethod = COLUMN_METHOD_OTHER_COMMUNES;
					columnParameter = new Integer(18);
					break;
				case 8:
					columnMethod = COLUMN_METHOD_OTHER_COMMUNES;
					columnParameter = new Integer(19);
					break;
				case 9:
					columnMethod = COLUMN_METHOD_OTHER_COMMUNES;
					columnParameter = null;
					cellType = Cell.CELLTYPE_SUM;
					break;
				case 10:
					columnMethod = COLUMN_METHOD_COUNTY_COUNCIL;
					columnParameter = new Integer(16);
					break;
				case 11:
					columnMethod = COLUMN_METHOD_COUNTY_COUNCIL;
					columnParameter = new Integer(17);
					break;
				case 12:
					columnMethod = COLUMN_METHOD_COUNTY_COUNCIL;
					columnParameter = new Integer(18);
					break;
				case 13:
					columnMethod = COLUMN_METHOD_COUNTY_COUNCIL;
					columnParameter = new Integer(19);
					break;
				case 14:
					columnMethod = COLUMN_METHOD_COUNTY_COUNCIL;
					columnParameter = null;
					cellType = Cell.CELLTYPE_SUM;
					break;
				case 15:
					columnMethod = COLUMN_METHOD_FREE_STANDING;
					columnParameter = new Integer(16);
					break;
				case 16:
					columnMethod = COLUMN_METHOD_FREE_STANDING;
					columnParameter = new Integer(17);
					break;
				case 17:
					columnMethod = COLUMN_METHOD_FREE_STANDING;
					columnParameter = new Integer(18);
					break;
				case 18:
					columnMethod = COLUMN_METHOD_FREE_STANDING;
					columnParameter = new Integer(19);
					break;
				case 19:
					columnMethod = COLUMN_METHOD_FREE_STANDING;
					columnParameter = null;
					cellType = Cell.CELLTYPE_SUM;
					break;
				case 20:
					columnMethod = COLUMN_METHOD_TOTAL;
					columnParameter = new Integer(16);
					break;
				case 21:
					columnMethod = COLUMN_METHOD_TOTAL;
					columnParameter = new Integer(17);
					break;
				case 22:
					columnMethod = COLUMN_METHOD_TOTAL;
					columnParameter = new Integer(18);
					break;
				case 23:
					columnMethod = COLUMN_METHOD_TOTAL;
					columnParameter = new Integer(19);
					break;
				case 24:
					columnMethod = COLUMN_METHOD_TOTAL;
					columnParameter = null;
					cellType = Cell.CELLTYPE_TOTAL;
					break;
			}
			
			try {
				ReportBusiness rb = getReportBusiness();
				Collection studyPaths = rb.getAllStudyPaths();
				Iterator iter = studyPaths.iterator();
				while (iter.hasNext()) {
					SchoolStudyPath studyPath = (SchoolStudyPath) iter.next();
					Object rowParameter = studyPath.getCode();
					Cell cell = new Cell(this, row, column, ROW_METHOD_STUDY_PATH,
							columnMethod, rowParameter, columnParameter, cellType);
					setCell(row, column, cell);
					row++;
					if (studyPath.getCode().equals("IB")) {
						cell = new Cell(this, row, column, ROW_METHOD_STUDY_PATH,
								columnMethod, "LP", columnParameter, cellType);
						setCell(row, column, cell);
						row++;
					}					
				}
				Integer compulsoryAge = null;
				if (columnParameter != null) {
					compulsoryAge = new Integer(columnParameter.intValue() + COMPULSORY_AGE_OFFSET);
				}
				Cell cell = new Cell(this, row, column, ROW_METHOD_STUDY_PATH,
						columnMethod, "GY", compulsoryAge, Cell.CELLTYPE_NORMAL);
				setCell(row, column, cell);
				row++;
				cell = new Cell(this, row, column, ROW_METHOD_TOTAL,
						columnMethod, null, columnParameter, Cell.CELLTYPE_TOTAL);
				setCell(row, column, cell);
				row++;
				cell = new Cell(this, row, column, ROW_METHOD_SHARE,
						columnMethod, null, columnParameter, Cell.CELLTYPE_PERCENT);
				setCell(row, column, cell);				
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
		Integer studentAge = (Integer) cell.getColumnParameter();
		boolean isCompulsory = false;
		int age = -1;
		if (studentAge != null) {
			age = studentAge.intValue();
			if (age > COMPULSORY_AGE_OFFSET) {
				age -= COMPULSORY_AGE_OFFSET;
				isCompulsory = true;
			}
		}
		int row = cell.getRow();
		int column = cell.getColumn();
		
		switch (cell.getRowMethod()) {
			case ROW_METHOD_STUDY_PATH:
				switch (cell.getColumnMethod()) {
					case COLUMN_METHOD_NACKA_COMMUNE:
						if (studentAge == null) {
							value += getCell(row, 0).getFloatValue();
							value += getCell(row, 1).getFloatValue();
							value += getCell(row, 2).getFloatValue();
							value += getCell(row, 3).getFloatValue();
						} else {
							value = getHighSchoolNackaCommunePlacementCount(age, studyPathPrefix, isCompulsory);
						}
						break;
					case COLUMN_METHOD_OTHER_COMMUNES:
						if (studentAge == null) {
							value += getCell(row, 5).getFloatValue();
							value += getCell(row, 6).getFloatValue();
							value += getCell(row, 7).getFloatValue();
							value += getCell(row, 8).getFloatValue();
						} else {
							value = getHighSchoolOtherCommunesPlacementCount(age, studyPathPrefix, isCompulsory);
						}
						break;
					case COLUMN_METHOD_COUNTY_COUNCIL:
						if (studentAge == null) {
							value += getCell(row, 10).getFloatValue();
							value += getCell(row, 11).getFloatValue();
							value += getCell(row, 12).getFloatValue();
							value += getCell(row, 13).getFloatValue();
						} else {
							value = getHighSchoolCountyCouncilPlacementCount(age, studyPathPrefix, isCompulsory);
						}
						break;
					case COLUMN_METHOD_FREE_STANDING:
						if (studentAge == null) {
							value += getCell(row, 15).getFloatValue();
							value += getCell(row, 16).getFloatValue();
							value += getCell(row, 17).getFloatValue();
							value += getCell(row, 18).getFloatValue();
						} else {
							value = getHighSchoolPrivatePlacementCount(age, studyPathPrefix, isCompulsory);
						}
						break;
					case COLUMN_METHOD_TOTAL:
						if (studentAge == null) {
							value += getCell(row, 20).getFloatValue();
							value += getCell(row, 21).getFloatValue();
							value += getCell(row, 22).getFloatValue();
							value += getCell(row, 23).getFloatValue();
						} else {
							for (int i = column - 20; i < column; i += 5) {
								Cell c = getCell(row, i);
								value += c.getFloatValue();
							}
						}
						break;
				}
				break;
			case ROW_METHOD_TOTAL:
				for (int i = 0; i < row; i++) {
					Cell c = getCell(i, column);
					value += c.getFloatValue();
				}
				break;
			case ROW_METHOD_SHARE:
				switch (cell.getColumnMethod()) {
					case COLUMN_METHOD_NACKA_COMMUNE:
						if (studentAge == null) {
							float total = 0f;
							total += getCell(row - 1, 4).getFloatValue();
							total += getCell(row - 1, 9).getFloatValue();
							total += getCell(row - 1, 14).getFloatValue();
							total += getCell(row - 1, 19).getFloatValue();
							if (total > 0) {
								value = getCell(row - 1, 4).getFloatValue() / total;								
							}
							value *= 100;
						}
						break;
					case COLUMN_METHOD_OTHER_COMMUNES:
						if (studentAge == null) {
							float total = 0f;
							total += getCell(row - 1, 4).getFloatValue();
							total += getCell(row - 1, 9).getFloatValue();
							total += getCell(row - 1, 14).getFloatValue();
							total += getCell(row - 1, 19).getFloatValue();
							if (total > 0) {
								value = getCell(row - 1, 9).getFloatValue() / total;								
							}
							value *= 100;
						}
						break;
					case COLUMN_METHOD_COUNTY_COUNCIL:
						if (studentAge == null) {
							float total = 0f;
							total += getCell(row - 1, 4).getFloatValue();
							total += getCell(row - 1, 9).getFloatValue();
							total += getCell(row - 1, 14).getFloatValue();
							total += getCell(row - 1, 19).getFloatValue();
							if (total > 0) {
								value = getCell(row - 1, 14).getFloatValue() / total;								
							}
							value *= 100;
						}
						break;
					case COLUMN_METHOD_FREE_STANDING:
						if (studentAge == null) {
							float total = 0f;
							total += getCell(row - 1, 4).getFloatValue();
							total += getCell(row - 1, 9).getFloatValue();
							total += getCell(row - 1, 14).getFloatValue();
							total += getCell(row - 1, 19).getFloatValue();
							if (total > 0) {
								value = getCell(row - 1, 19).getFloatValue() / total;								
							}
							value *= 100;
						}
						break;
					case COLUMN_METHOD_TOTAL:
						if (studentAge == null) {
							value = 100.0f;
						} else {
							float total = getCell(row - 1, 24).getFloatValue();
							if (total > 0) {
								value = getCell(row - 1, column).getFloatValue() / total;								
							}
							value *= 100;
						}
						break;
				}
				break;
		}
		
		return value;
	}

	/**
	 * @see se.idega.idegaweb.commune.school.report.business.ReportModel#getReportTitleLocalizationKey()
	 */
	public String getReportTitleLocalizationKey() {
		return KEY_REPORT_TITLE;
	}
	
	/*
	 * Returns the value from executing the specified study path query.
	 * Special case for study path IB handled.
	 */
	private int executeStudyPathQuery(int parameterIndex, String studyPathPrefix, PreparedQuery query) {
		query.setString(parameterIndex, studyPathPrefix + "%");
		int value = query.execute();
		if (studyPathPrefix.equals("IB")) {
			query.setString(parameterIndex, "PRE%");
			value += query.execute();
		}
		return value;		
	}
	
	/**
	 * Returns the number of student placements for high schools
	 * in Nacka commune for the specified student age.
	 * Only students in Nacka commune are counted. 
	 */
	protected int getHighSchoolNackaCommunePlacementCount(int age, String studyPathPrefix, boolean isCompulsory) throws RemoteException {
		PreparedQuery query = null;
		ReportBusiness rb = getReportBusiness();
		query = getQuery(QUERY_NACKA_COMMUNE + age);
		if (query == null) {
			query = new PreparedQuery(getConnection());
			query.setSelectCountDistinctUsers();
			query.setPlacements(rb.getSchoolSeasonId());
			query.setOnlyNackaCitizens();
			query.setOnlyNackaSchools();
			query.setNotPrivateSchools();
			query.setNotCountyCouncilSchools();
			query.setStudentAge(rb.getCurrentSchoolSeason(), rb.getHighSchoolStudentAgeFrom(age), rb.getHighSchoolStudentAgeTo(age));			
			query.setSchoolType(); // parameter 1
			query.setStudyPathPrefix(); // parameter 2
			query.prepare();
			setQuery(QUERY_NACKA_COMMUNE + age, query);
		}
		if (isCompulsory) {
			query.setInt(1, rb.getCompulsoryHighSchoolTypeId());
		} else {
			query.setInt(1, rb.getHighSchoolTypeId());
		}
		return executeStudyPathQuery(2, studyPathPrefix, query);
	}
	
	/**
	 * Returns the number of student placements for high schools
	 * outside Nacka commune for the specified student age.
	 * Only students in Nacka commune are counted. 
	 */
	protected int getHighSchoolOtherCommunesPlacementCount(int age, String studyPathPrefix, boolean isCompulsory) throws RemoteException {
		PreparedQuery query = null;
		ReportBusiness rb = getReportBusiness();
		query = getQuery(QUERY_OTHER_COMMUNES + age);
		if (query == null) {
			query = new PreparedQuery(getConnection());
			query.setSelectCountDistinctUsers();
			query.setPlacements(rb.getSchoolSeasonId());
			query.setOnlyNackaCitizens();
			query.setOnlySchoolsInOtherCommunes();
			query.setNotPrivateSchools();
			query.setNotCountyCouncilSchools();
			query.setStudentAge(rb.getCurrentSchoolSeason(), rb.getHighSchoolStudentAgeFrom(age), rb.getHighSchoolStudentAgeTo(age));			
			query.setSchoolType(); // parameter 1
			query.setStudyPathPrefix(); // parameter 2
			query.prepare();
			setQuery(QUERY_OTHER_COMMUNES + age, query);
		}
		if (isCompulsory) {
			query.setInt(1, rb.getCompulsoryHighSchoolTypeId());
		} else {
			query.setInt(1, rb.getHighSchoolTypeId());
		}
		return executeStudyPathQuery(2, studyPathPrefix, query);
	}
	
	/**
	 * Returns the number of student placements for county council high schools.
	 * for the specified student age.
	 * Only students in Nacka commune are counted. 
	 */
	protected int getHighSchoolCountyCouncilPlacementCount(int age, String studyPathPrefix, boolean isCompulsory) throws RemoteException {
		PreparedQuery query = null;
		ReportBusiness rb = getReportBusiness();
		query = getQuery(QUERY_COUNTY_COUNCIL + age);
		if (query == null) {
			query = new PreparedQuery(getConnection());
			query.setSelectCountDistinctUsers();
			query.setPlacements(rb.getSchoolSeasonId());
			query.setOnlyNackaCitizens();
			query.setOnlyCountyCouncilSchools();
			query.setStudentAge(rb.getCurrentSchoolSeason(), rb.getHighSchoolStudentAgeFrom(age), rb.getHighSchoolStudentAgeTo(age));			
			query.setSchoolType(); // parameter 1
			query.setStudyPathPrefix(); // parameter 2
			query.prepare();
			setQuery(QUERY_COUNTY_COUNCIL + age, query);
		}
		if (isCompulsory) {
			query.setInt(1, rb.getCompulsoryHighSchoolTypeId());
		} else {
			query.setInt(1, rb.getHighSchoolTypeId());
		}
		return executeStudyPathQuery(2, studyPathPrefix, query);
	}
	
	/**
	 * Returns the number of student placements for private high schools
	 * in Nacka commune for the specified student age.
	 * Only students in Nacka commune are counted. 
	 */
	protected int getHighSchoolPrivatePlacementCount(int age, String studyPathPrefix, boolean isCompulsory) throws RemoteException {
		PreparedQuery query = null;
		ReportBusiness rb = getReportBusiness();
		query = getQuery(QUERY_PRIVATE + age);
		if (query == null) {
			query = new PreparedQuery(getConnection());
			query.setSelectCountDistinctUsers();
			query.setPlacements(rb.getSchoolSeasonId());
			query.setOnlyNackaCitizens();
			query.setOnlyPrivateSchools();
			query.setStudentAge(rb.getCurrentSchoolSeason(), rb.getHighSchoolStudentAgeFrom(age), rb.getHighSchoolStudentAgeTo(age));			
			query.setSchoolType(); // parameter 1
			query.setStudyPathPrefix(); // parameter 2
			query.prepare();
			setQuery(QUERY_PRIVATE + age, query);
		}
		if (isCompulsory) {
			query.setInt(1, rb.getCompulsoryHighSchoolTypeId());
		} else {
			query.setInt(1, rb.getHighSchoolTypeId());
		}
		return executeStudyPathQuery(2, studyPathPrefix, query);
	}
}
