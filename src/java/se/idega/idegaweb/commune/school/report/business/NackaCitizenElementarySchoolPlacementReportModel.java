/*
 * $Id: NackaCitizenElementarySchoolPlacementReportModel.java,v 1.9 2004/02/17 18:09:44 anders Exp $
 *
 * Copyright (C) 2003 Agura IT. All Rights Reserved.
 *
 * This software is the proprietary information of Agura IT AB.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.school.report.business;

import java.rmi.RemoteException;

/** 
 * Report model for Nacka citizen placement for elementary schools.
 * <p>
 * Last modified: $Date: 2004/02/17 18:09:44 $ by $Author: anders $
 *
 * @author Anders Lindman
 * @version $Revision: 1.9 $
 */
public class NackaCitizenElementarySchoolPlacementReportModel extends ReportModel {

	private final static int ROW_SIZE = 10;
	private final static int COLUMN_SIZE = 16;
	
	private final static int ROW_METHOD_ELEMENTARY_NACKA_COMMUNE = 1;
	private final static int ROW_METHOD_ELEMENTARY_OTHER_COMMUNES = 2;
	private final static int ROW_METHOD_ELEMENTARY_PRIVATE_SCHOOLS = 3;
	private final static int ROW_METHOD_ELEMENTARY_FOREIGN_SCHOOLS = 4;
	private final static int ROW_METHOD_ELEMENTARY_SUM = 5;
	private final static int ROW_METHOD_COMPULSORY_NACKA_COMMUNE = 6;
	private final static int ROW_METHOD_COMPULSORY_OTHER_COMMUNES = 7;
	private final static int ROW_METHOD_COMPULSORY_PRIVATE_SCHOOLS = 8;
	private final static int ROW_METHOD_COMPULSORY_SUM = 9;
	private final static int ROW_METHOD_TOTAL = 10;

	private final static int COLUMN_METHOD_SCHOOL_YEAR = 101;
	private final static int COLUMN_METHOD_SUM_1_3 = 102;
	private final static int COLUMN_METHOD_SUM_4_6 = 103;
	private final static int COLUMN_METHOD_SUM_7_10 = 104;
	private final static int COLUMN_METHOD_TOTAL_1_10 = 105;

	private final static String QUERY_NACKA_COMMUNE = "nacka_commune";
	private final static String QUERY_NACKA_COMMUNE_6_YEAR_STUDENTS = "nacka_commune_6";
	private final static String QUERY_OTHER_COMMUNES = "other_communes";
	private final static String QUERY_OTHER_COMMUNES_6_YEAR_STUDENTS = "other_communes_6";
	private final static String QUERY_PRIVATE = "private";
	private final static String QUERY_PRIVATE_6_YEAR_STUDENTS = "private_6";
	private final static String QUERY_FOREIGN = "foreign";
	private final static String QUERY_FOREIGN_6_YEAR_STUDENTS = "foreign_6";
	private final static String QUERY_COMPULSORY_NACKA = "compulsory_nacka";
	private final static String QUERY_COMPULSORY_NACKA_6_YEAR_STUDENTS = "compulsory_nacka_6";
	private final static String QUERY_COMPULSORY_OTHER_COMMUNES = "compulsory_other_communes";
	private final static String QUERY_COMPULSORY_OTHER_COMMUNES_6_YEAR_STUDENTS = "compulsory_other_communes_6";
	private final static String QUERY_COMPULSORY_PRIVATE = "compulsory_private";
	private final static String QUERY_COMPULSORY_PRIVATE_6_YEAR_STUDENTS = "compulsory_other_communes_6";
	
	private final static String KEY_REPORT_TITLE = KP + "title_nacka_citizen_elementary_school_placements";

	/**
	 * Constructs this report model.
	 * @param reportBusiness the report business instance for calculating cell values
	 */
	public NackaCitizenElementarySchoolPlacementReportModel(ReportBusiness reportBusiness) {
		super(reportBusiness);
	}
	
	/**
	 * @see se.idega.idegaweb.commune.school.report.business.ReportModel#initReportSize()
	 */
	protected void initReportSize() {
		setReportSize(ROW_SIZE, COLUMN_SIZE);
	}
	
	/**
	 * @see se.idega.idegaweb.commune.school.report.business.ReportModel#buildRowHeaders()
	 */
	protected Header[] buildRowHeaders() {
		Header[] headers = new Header[3];
		
		Header h = new Header(KEY_ELEMENTARY_SCHOOL, Header.HEADERTYPE_ROW_HEADER, 5);
		Header child0 = new Header(KEY_NACKA_COMMUNE, Header.HEADERTYPE_ROW_NORMAL);
		Header child1 = new Header(KEY_OTHER_COMMUNES, Header.HEADERTYPE_ROW_NORMAL);
		Header child2 = new Header(KEY_PRIVATE_SCHOOLS, Header.HEADERTYPE_ROW_NORMAL);
		Header child3 = new Header(KEY_FOREIGN_SCHOOLS, Header.HEADERTYPE_ROW_NORMAL);
		Header child4 = new Header(KEY_SUM, Header.HEADERTYPE_ROW_SUM);
		h.setChild(0, child0);
		h.setChild(1, child1);
		h.setChild(2, child2);
		h.setChild(3, child3);
		h.setChild(4, child4);
		headers[0] = h;
		
		h = new Header(KEY_COMPULSORY_SCHOOLS, Header.HEADERTYPE_ROW_HEADER, 4);
		child0 = new Header(KEY_NACKA_COMMUNE, Header.HEADERTYPE_ROW_NORMAL);
		child1 = new Header(KEY_OTHER_COMMUNES, Header.HEADERTYPE_ROW_NORMAL);
		child2 = new Header(KEY_PRIVATE_SCHOOLS, Header.HEADERTYPE_ROW_NORMAL);
		child3 = new Header(KEY_SUM, Header.HEADERTYPE_ROW_SUM);
		h.setChild(0, child0);
		h.setChild(1, child1);
		h.setChild(2, child2);
		h.setChild(3, child3);
		headers[1] = h;
		
		h = new Header(KEY_TOTAL, Header.HEADERTYPE_ROW_TOTAL);
		headers[2] = h;
		
		return headers;
	}
	
	/**
	 * @see se.idega.idegaweb.commune.school.report.business.ReportModel#buildColumnHeaders()
	 */
	protected Header[] buildColumnHeaders() {
		Header[] headers = new Header[9];
		
		Header h = new Header(KEY_SCHOOL_YEAR_F, Header.HEADERTYPE_COLUMN_NORMAL);
		headers[0] = h;
		
		h = new Header(KEY_SCHOOL_YEAR, Header.HEADERTYPE_COLUMN_HEADER, 3);
		Header child0 = new Header(KEY_SCHOOL_YEAR_1, Header.HEADERTYPE_COLUMN_NORMAL);
		Header child1 = new Header(KEY_SCHOOL_YEAR_2, Header.HEADERTYPE_COLUMN_NORMAL);
		Header child2 = new Header(KEY_SCHOOL_YEAR_3, Header.HEADERTYPE_COLUMN_NORMAL);
		h.setChild(0, child0);
		h.setChild(1, child1);
		h.setChild(2, child2);
		headers[1] = h;
		
		h = new Header(KEY_SUM, Header.HEADERTYPE_COLUMN_HEADER, 1);
		child0 = new Header(KEY_SUM_1_3, Header.HEADERTYPE_COLUMN_NORMAL);
		h.setChild(0, child0);
		headers[2] = h;
		
		h = new Header(KEY_SCHOOL_YEAR, Header.HEADERTYPE_COLUMN_HEADER, 3);
		child0 = new Header(KEY_SCHOOL_YEAR_4, Header.HEADERTYPE_COLUMN_NORMAL);
		child1 = new Header(KEY_SCHOOL_YEAR_5, Header.HEADERTYPE_COLUMN_NORMAL);
		child2 = new Header(KEY_SCHOOL_YEAR_6, Header.HEADERTYPE_COLUMN_NORMAL);
		h.setChild(0, child0);
		h.setChild(1, child1);
		h.setChild(2, child2);
		headers[3] = h;
		
		h = new Header(KEY_SUM, Header.HEADERTYPE_COLUMN_HEADER, 1);
		child0 = new Header(KEY_SUM_4_6, Header.HEADERTYPE_COLUMN_NORMAL);
		h.setChild(0, child0);
		headers[4] = h;

		h = new Header(KEY_SCHOOL_YEAR, Header.HEADERTYPE_COLUMN_HEADER, 4);
		child0 = new Header(KEY_SCHOOL_YEAR_7, Header.HEADERTYPE_COLUMN_NORMAL);
		child1 = new Header(KEY_SCHOOL_YEAR_8, Header.HEADERTYPE_COLUMN_NORMAL);
		child2 = new Header(KEY_SCHOOL_YEAR_9, Header.HEADERTYPE_COLUMN_NORMAL);
		Header child3 = new Header(KEY_SCHOOL_YEAR_10, Header.HEADERTYPE_COLUMN_NORMAL);
		h.setChild(0, child0);
		h.setChild(1, child1);
		h.setChild(2, child2);
		h.setChild(3, child3);
		headers[5] = h;

		h = new Header(KEY_SUM, Header.HEADERTYPE_COLUMN_HEADER, 1);
		child0 = new Header(KEY_SUM_7_10, Header.HEADERTYPE_COLUMN_NORMAL);
		h.setChild(0, child0);
		headers[6] = h;
		
		h = new Header(KEY_TOTAL, Header.HEADERTYPE_COLUMN_HEADER, 1);
		child0 = new Header(KEY_TOTAL_1_10, Header.HEADERTYPE_COLUMN_NORMAL);
		h.setChild(0, child0);
		headers[7] = h;
		
		h = new Header(KEY_SIX_YEARS_STUDENTS, Header.HEADERTYPE_COLUMN_NORMAL);
		headers[8] = h;
				
		return headers;
	}
	
	/**
	 * @see se.idega.idegaweb.commune.school.report.business.ReportModel#buildCells()
	 */
	protected void buildCells() {
		for (int column = 0; column < getColumnSize(); column++) {
			int row = 0;
			int columnMethod = 0;
			Object columnParameter = null;
			switch (column) {
				case 0:
					columnMethod = COLUMN_METHOD_SCHOOL_YEAR;
					columnParameter = "F";
					break;
				case 1:
					columnMethod = COLUMN_METHOD_SCHOOL_YEAR;
					columnParameter = "1";
					break;
				case 2:
					columnMethod = COLUMN_METHOD_SCHOOL_YEAR;
					columnParameter = "2";
					break;
				case 3:
					columnMethod = COLUMN_METHOD_SCHOOL_YEAR;
					columnParameter = "3";
					break;
				case 4:
					columnMethod = COLUMN_METHOD_SUM_1_3;
					columnParameter = null;
					break;
				case 5:
					columnMethod = COLUMN_METHOD_SCHOOL_YEAR;
					columnParameter = "4";
					break;
				case 6:
					columnMethod = COLUMN_METHOD_SCHOOL_YEAR;
					columnParameter = "5";
					break;
				case 7:
					columnMethod = COLUMN_METHOD_SCHOOL_YEAR;
					columnParameter = "6";
					break;
				case 8:
					columnMethod = COLUMN_METHOD_SUM_4_6;
					columnParameter = null;
					break;
				case 9:
					columnMethod = COLUMN_METHOD_SCHOOL_YEAR;
					columnParameter = "7";
					break;
				case 10:
					columnMethod = COLUMN_METHOD_SCHOOL_YEAR;
					columnParameter = "8";
					break;
				case 11:
					columnMethod = COLUMN_METHOD_SCHOOL_YEAR;
					columnParameter = "9";
					break;
				case 12:
					columnMethod = COLUMN_METHOD_SCHOOL_YEAR;
					columnParameter = "10";
					break;
				case 13:
					columnMethod = COLUMN_METHOD_SUM_7_10;
					columnParameter = null;
					break;
				case 14:
					columnMethod = COLUMN_METHOD_TOTAL_1_10;
					columnParameter = null;
					break;
				case 15:
					columnMethod = COLUMN_METHOD_SCHOOL_YEAR;
					columnParameter = "0";
					break;
			}
			Cell cell = new Cell(this, row, column, ROW_METHOD_ELEMENTARY_NACKA_COMMUNE,
					columnMethod, null, columnParameter, Cell.CELLTYPE_NORMAL);
			setCell(row, column, cell);
			row++;
			cell = new Cell(this, row, column, ROW_METHOD_ELEMENTARY_OTHER_COMMUNES,
					columnMethod, null, columnParameter, Cell.CELLTYPE_NORMAL);
			setCell(row, column, cell);
			row++;
			cell = new Cell(this, row, column, ROW_METHOD_ELEMENTARY_PRIVATE_SCHOOLS,
					columnMethod, null, columnParameter, Cell.CELLTYPE_NORMAL);
			setCell(row, column, cell);
			row++;
			cell = new Cell(this, row, column, ROW_METHOD_ELEMENTARY_FOREIGN_SCHOOLS,
					columnMethod, null, columnParameter, Cell.CELLTYPE_NORMAL);
			setCell(row, column, cell);
			row++;
			cell = new Cell(this, row, column, ROW_METHOD_ELEMENTARY_SUM,
					columnMethod, null, columnParameter, Cell.CELLTYPE_SUM);
			setCell(row, column, cell);
			
			if (columnParameter != null) {
				columnParameter = "S" + columnParameter;
			}
			
			row++;
			cell = new Cell(this, row, column, ROW_METHOD_COMPULSORY_NACKA_COMMUNE,
					columnMethod, null, columnParameter, Cell.CELLTYPE_NORMAL);
			setCell(row, column, cell);
			row++;
			cell = new Cell(this, row, column, ROW_METHOD_COMPULSORY_OTHER_COMMUNES,
					columnMethod, null, columnParameter, Cell.CELLTYPE_NORMAL);
			setCell(row, column, cell);
			row++;
			cell = new Cell(this, row, column, ROW_METHOD_COMPULSORY_PRIVATE_SCHOOLS,
					columnMethod, null, columnParameter, Cell.CELLTYPE_NORMAL);
			setCell(row, column, cell);
			row++;
			cell = new Cell(this, row, column, ROW_METHOD_COMPULSORY_SUM,
					columnMethod, null, columnParameter, Cell.CELLTYPE_SUM);
			setCell(row, column, cell);
			row++;
			cell = new Cell(this, row, column, ROW_METHOD_TOTAL,
					columnMethod, null, columnParameter, Cell.CELLTYPE_TOTAL);
			setCell(row, column, cell);
			row++;		
		}
	}
	
	/**
	 * @see se.idega.idegaweb.commune.school.report.business.ReportModel#calculate()
	 */
	protected float calculate(Cell cell) throws RemoteException {
		float value = 0f;
		String schoolYearName = (String) cell.getColumnParameter();
		
		if (cell.getColumnMethod() == COLUMN_METHOD_SCHOOL_YEAR) {
			switch (cell.getRowMethod()) {
				case ROW_METHOD_ELEMENTARY_NACKA_COMMUNE:
					value = getElementaryNackaCommunePlacementCount(schoolYearName);
					break;
				case ROW_METHOD_ELEMENTARY_OTHER_COMMUNES:
					value = getElementaryOtherCommunesPlacementCount(schoolYearName);
					break;
				case ROW_METHOD_ELEMENTARY_PRIVATE_SCHOOLS:
					value = getElementaryPrivateSchoolPlacementCount(schoolYearName);
					break;
				case ROW_METHOD_ELEMENTARY_FOREIGN_SCHOOLS:
					value = getElementaryForeignSchoolPlacementCount(schoolYearName);
					break;
				case ROW_METHOD_ELEMENTARY_SUM:
					for (int i = 0; i < 4; i++) {
						value += getCell(i, cell.getColumn()).getFloatValue();
					}
					break;
				case ROW_METHOD_COMPULSORY_NACKA_COMMUNE:
					value = getCompulsoryNackaCommunePlacementCount(schoolYearName);
					break;
				case ROW_METHOD_COMPULSORY_OTHER_COMMUNES:
					value = getCompulsoryOtherCommunesPlacementCount(schoolYearName);
					break;
				case ROW_METHOD_COMPULSORY_PRIVATE_SCHOOLS:
					value = getCompulsoryPrivateSchoolPlacementCount(schoolYearName);
					break;
				case ROW_METHOD_COMPULSORY_SUM:
					for (int i = 5; i < 8; i++) {
						value += getCell(i, cell.getColumn()).getFloatValue();
					}
					break;				
				case ROW_METHOD_TOTAL:
					value = getCell(4, cell.getColumn()).getFloatValue() + 
							getCell(8, cell.getColumn()).getFloatValue();
					break;				
			}
		} else {
			switch (cell.getColumnMethod()) {
				case COLUMN_METHOD_SUM_1_3:
					for (int i = 1; i < 4; i++) {
						value += getCell(cell.getRow(), i).getFloatValue();
					}
					break;
				case COLUMN_METHOD_SUM_4_6:
					for (int i = 5; i < 8; i++) {
						value += getCell(cell.getRow(), i).getFloatValue();
					}
					break;
				case COLUMN_METHOD_SUM_7_10:
					for (int i = 9; i < 13; i++) {
						value += getCell(cell.getRow(), i).getFloatValue();
					}
					break;
				case COLUMN_METHOD_TOTAL_1_10:
					value = getCell(cell.getRow(), 4).getFloatValue() +
							getCell(cell.getRow(), 8).getFloatValue() +
							getCell(cell.getRow(), 13).getFloatValue();
					break;
			}
		}
		
		return value;
	}

	/**
	 * @see se.idega.idegaweb.commune.school.report.business.ReportModel#getReportTitleLocalizationKey()
	 */
	public String getReportTitleLocalizationKey() {
		return KEY_REPORT_TITLE;
	}
	
	/**
	 * Returns the number of student placements for elementary schools
	 * in Nacka commune for the specified school year.
	 * Only students in Nacka commune are counted. 
	 */
	protected int getElementaryNackaCommunePlacementCount(String schoolYearName) throws RemoteException {
		PreparedQuery query = null;
		if (!schoolYearName.equals("0")) {
			ReportBusiness rb = getReportBusiness();
			query = getQuery(QUERY_NACKA_COMMUNE);
			if (query == null) {
				query = new PreparedQuery(getConnection());
				query.setSelectCountDistinctUsers();
				query.setPlacements(rb.getSchoolSeasonId());
				query.setOnlyNackaCitizens();
				query.setOnlyNackaSchools();
				query.setOnlyCommuneSchools();
				query.setNotForeignSchools();
				query.setSchoolType(); // parameter 1
				query.setSchoolYearName(); // parameter 2
				query.prepare();
				setQuery(QUERY_NACKA_COMMUNE, query);
			}
			if (schoolYearName.equals("F")) {
				query.setInt(1, rb.getPreSchoolClassTypeId());
			} else {
				query.setInt(1, rb.getElementarySchoolTypeId());
			}
			query.setString(2, schoolYearName);
		} else { // 6 years old students
			query = getQuery(QUERY_NACKA_COMMUNE_6_YEAR_STUDENTS);
			if (query == null) {
				query = new PreparedQuery(getConnection());
				query.setSelectCountDistinctUsers();
				query.setPlacements(getReportBusiness().getSchoolSeasonId());
				query.setOnlyNackaCitizens();
				query.setOnlyNackaSchools();
				query.setOnlyCommuneSchools();
				query.setOnlyStudentsBorn(getReportBusiness().getSchoolSeasonStartYear() - 6);
				query.setNotForeignSchools();
				query.setSchoolYearName("1");
				query.setSchoolTypeElementarySchool();
				query.prepare();
				setQuery(QUERY_NACKA_COMMUNE_6_YEAR_STUDENTS, query);
			}
		}
		return query.execute();
	}
	
	/**
	 * Returns the number of student placements for elementary schools
	 * outside Nacka commune for the specified school year. 
	 * Only students in Nacka commune are counted. 
	 */
	protected int getElementaryOtherCommunesPlacementCount(String schoolYearName) throws RemoteException {
		PreparedQuery query = null;
		if (!schoolYearName.equals("0")) {
			ReportBusiness rb = getReportBusiness();
			query = getQuery(QUERY_OTHER_COMMUNES);
			if (query == null) {
				query = new PreparedQuery(getConnection());
				query.setSelectCountDistinctUsers();
				query.setPlacements(rb.getSchoolSeasonId());
				query.setOnlyNackaCitizens();
				query.setOnlySchoolsInOtherCommunes();
				query.setOnlyCommuneSchools();
				query.setNotForeignSchools();
				query.setSchoolType(); // parameter 1
				query.setSchoolYearName(); // parameter 2
				query.prepare();
				setQuery(QUERY_OTHER_COMMUNES, query);
			}
			if (schoolYearName.equals("F")) {
				query.setInt(1, rb.getPreSchoolClassTypeId());
			} else {
				query.setInt(1, rb.getElementarySchoolTypeId());
			}
			query.setString(2, schoolYearName);
		} else { // 6 years old students
			query = getQuery(QUERY_OTHER_COMMUNES_6_YEAR_STUDENTS);
			if (query == null) {
				query = new PreparedQuery(getConnection());
				query.setSelectCountDistinctUsers();
				query.setPlacements(getReportBusiness().getSchoolSeasonId());
				query.setOnlyNackaCitizens();
				query.setOnlySchoolsInOtherCommunes();
				query.setOnlyCommuneSchools();
				query.setOnlyStudentsBorn(getReportBusiness().getSchoolSeasonStartYear() - 6);
				query.setNotForeignSchools();
				query.setSchoolYearName("1");
				query.setSchoolTypeElementarySchool();
				query.prepare();
				setQuery(QUERY_OTHER_COMMUNES_6_YEAR_STUDENTS, query);
			}
		}
		return query.execute();
	}
	
	/**
	 * Returns the number of student placements for elementary private schools
	 * for the specified school year. 
	 * Only students in Nacka commune are counted. 
	 */
	protected int getElementaryPrivateSchoolPlacementCount(String schoolYearName) throws RemoteException {
		PreparedQuery query = null;
		if (!schoolYearName.equals("0")) {
			ReportBusiness rb = getReportBusiness();
			query = getQuery(QUERY_PRIVATE);
			if (query == null) {
				query = new PreparedQuery(getConnection());
				query.setSelectCountDistinctUsers();
				query.setPlacements(rb.getSchoolSeasonId());
				query.setOnlyNackaCitizens();
				query.setOnlyPrivateSchools();
				query.setNotForeignSchools();
				query.setSchoolType(); // parameter 1
				query.setSchoolYearName(); // parameter 2
				query.prepare();
				setQuery(QUERY_PRIVATE, query);
			}
			if (schoolYearName.equals("F")) {
				query.setInt(1, rb.getPreSchoolClassTypeId());
			} else {
				query.setInt(1, rb.getElementarySchoolTypeId());
			}
			query.setString(2, schoolYearName);
		} else { // 6 years old students
			query = getQuery(QUERY_PRIVATE_6_YEAR_STUDENTS);
			if (query == null) {
				query = new PreparedQuery(getConnection());
				query.setSelectCountDistinctUsers();
				query.setPlacements(getReportBusiness().getSchoolSeasonId());
				query.setOnlyNackaCitizens();
				query.setOnlyStudentsBorn(getReportBusiness().getSchoolSeasonStartYear() - 6);
				query.setOnlyPrivateSchools();
				query.setNotForeignSchools();
				query.setSchoolYearName("1");
				query.setSchoolTypeElementarySchool();
				query.prepare();
				setQuery(QUERY_PRIVATE_6_YEAR_STUDENTS, query);
			}
		}
		return query.execute();
	}
	
	/**
	 * Returns the number of student placements for foreign schools
	 * for the specified school year. 
	 * Only students in Nacka commune are counted. 
	 */
	protected int getElementaryForeignSchoolPlacementCount(String schoolYearName) throws RemoteException {
		PreparedQuery query = null;
		if (!schoolYearName.equals("0")) {
			ReportBusiness rb = getReportBusiness();
			query = getQuery(QUERY_FOREIGN);
			if (query == null) {
				query = new PreparedQuery(getConnection());
				query.setSelectCountDistinctUsers();
				query.setPlacements(rb.getSchoolSeasonId());
				query.setOnlyNackaCitizens();
				query.setOnlyForeignSchools();
				query.setSchoolType(); // parameter 1
				query.setSchoolYearName(); // parameter 2
				query.prepare();
				setQuery(QUERY_FOREIGN, query);
			}
			if (schoolYearName.equals("F")) {
				query.setInt(1, rb.getPreSchoolClassTypeId());
			} else {
				query.setInt(1, rb.getElementarySchoolTypeId());
			}
			query.setString(2, schoolYearName);
		} else { // 6 years old students
			query = getQuery(QUERY_FOREIGN_6_YEAR_STUDENTS);
			if (query == null) {
				query = new PreparedQuery(getConnection());
				query.setSelectCountDistinctUsers();
				query.setPlacements(getReportBusiness().getSchoolSeasonId());
				query.setOnlyNackaCitizens();
				query.setOnlyStudentsBorn(getReportBusiness().getSchoolSeasonStartYear() - 6);
				query.setOnlyForeignSchools();
				query.setSchoolYearName("1");
				query.setSchoolTypeElementarySchool();
				query.prepare();
				setQuery(QUERY_FOREIGN_6_YEAR_STUDENTS, query);
			}
		}
		return query.execute();
	}
	
	/**
	 * Returns the number of student placements for compulsory schools
	 * in Nacka commune for the specified school year.
	 * Only students in Nacka commune are counted. 
	 */
	protected int getCompulsoryNackaCommunePlacementCount(String schoolYearName) throws RemoteException {
		PreparedQuery query = null;
		if (!schoolYearName.equals("0")) {
			ReportBusiness rb = getReportBusiness();
			query = getQuery(QUERY_COMPULSORY_NACKA);
			if (query == null) {
				query = new PreparedQuery(getConnection());
				query.setSelectCountDistinctUsers();
				query.setPlacements(rb.getSchoolSeasonId());
				query.setOnlyNackaCitizens();
				query.setOnlyNackaSchools();
				query.setOnlyCommuneSchools();
				query.setNotPrivateSchools();
				query.setNotForeignSchools();
				query.setSchoolTypeCompulsorySchool();
				query.setSchoolYearName(); // parameter 1
				query.prepare();
				setQuery(QUERY_COMPULSORY_NACKA, query);
			}
			if (schoolYearName.equals("F")) {
				return 0;
			}
			query.setString(1, schoolYearName);
		} else { // 6 years old students
			query = getQuery(QUERY_COMPULSORY_NACKA_6_YEAR_STUDENTS);
			if (query == null) {
				query = new PreparedQuery(getConnection());
				query.setSelectCountDistinctUsers();
				query.setPlacements(getReportBusiness().getSchoolSeasonId());
				query.setOnlyNackaCitizens();
				query.setOnlyNackaSchools();
				query.setOnlyCommuneSchools();
				query.setOnlyStudentsBorn(getReportBusiness().getSchoolSeasonStartYear() - 6);
				query.setNotPrivateSchools();
				query.setNotForeignSchools();
				query.setSchoolYearName("S1");
				query.setSchoolTypeCompulsorySchool();
				query.prepare();
				setQuery(QUERY_COMPULSORY_NACKA_6_YEAR_STUDENTS, query);
			}
		}
		return query.execute();
	}
	
	/**
	 * Returns the number of student placements for compulsory schools
	 * outside Nacka commune for the specified school year. 
	 * Only students in Nacka commune are counted. 
	 */
	protected int getCompulsoryOtherCommunesPlacementCount(String schoolYearName) throws RemoteException {
		PreparedQuery query = null;
		if (!schoolYearName.equals("0")) {
			ReportBusiness rb = getReportBusiness();
			query = getQuery(QUERY_COMPULSORY_OTHER_COMMUNES);
			if (query == null) {
				query = new PreparedQuery(getConnection());
				query.setSelectCountDistinctUsers();
				query.setPlacements(rb.getSchoolSeasonId());
				query.setOnlyNackaCitizens();
				query.setOnlySchoolsInOtherCommunes();
				query.setOnlyCommuneSchools();
				query.setNotPrivateSchools();
				query.setNotForeignSchools();
				query.setSchoolTypeCompulsorySchool();
				query.setSchoolYearName(); // parameter 1
				query.prepare();
				setQuery(QUERY_COMPULSORY_OTHER_COMMUNES, query);
			}
			if (schoolYearName.equals("F")) {
				return 0;
			}
			query.setString(1, schoolYearName);
		} else { // 6 years old students
			query = getQuery(QUERY_COMPULSORY_OTHER_COMMUNES_6_YEAR_STUDENTS);
			if (query == null) {
				query = new PreparedQuery(getConnection());
				query.setSelectCountDistinctUsers();
				query.setPlacements(getReportBusiness().getSchoolSeasonId());
				query.setOnlyNackaCitizens();
				query.setOnlySchoolsInOtherCommunes();
				query.setOnlyCommuneSchools();
				query.setOnlyStudentsBorn(getReportBusiness().getSchoolSeasonStartYear() - 6);
				query.setNotPrivateSchools();
				query.setNotForeignSchools();
				query.setSchoolYearName("S1");
				query.setSchoolTypeCompulsorySchool();
				query.prepare();
				setQuery(QUERY_COMPULSORY_OTHER_COMMUNES_6_YEAR_STUDENTS, query);
			}
		}
		return query.execute();
	}
	
	/**
	 * Returns the number of student placements for compulsory private schools
	 * for the specified school year. 
	 * Only students in Nacka commune are counted. 
	 */
	protected int getCompulsoryPrivateSchoolPlacementCount(String schoolYearName) throws RemoteException {
		PreparedQuery query = null;
		if (!schoolYearName.equals("0")) {
			ReportBusiness rb = getReportBusiness();
			query = getQuery(QUERY_COMPULSORY_PRIVATE);
			if (query == null) {
				query = new PreparedQuery(getConnection());
				query.setSelectCountDistinctUsers();
				query.setPlacements(rb.getSchoolSeasonId());
				query.setOnlyNackaCitizens();
				query.setOnlyPrivateSchools();
				query.setNotForeignSchools();
				query.setSchoolTypeCompulsorySchool();
				query.setSchoolYearName(); // parameter 1
				query.prepare();
				setQuery(QUERY_COMPULSORY_PRIVATE, query);
			}
			if (schoolYearName.equals("F")) {
				return 0;
			}
			query.setString(1, schoolYearName);
		} else { // 6 years old students
			query = getQuery(QUERY_COMPULSORY_PRIVATE_6_YEAR_STUDENTS);
			if (query == null) {
				query = new PreparedQuery(getConnection());
				query.setSelectCountDistinctUsers();
				query.setPlacements(getReportBusiness().getSchoolSeasonId());
				query.setOnlyNackaCitizens();
				query.setOnlyStudentsBorn(getReportBusiness().getSchoolSeasonStartYear() - 6);
				query.setOnlyPrivateSchools();
				query.setNotForeignSchools();
				query.setSchoolYearName("S1");
				query.setSchoolTypeCompulsorySchool();
				query.prepare();
				setQuery(QUERY_COMPULSORY_PRIVATE_6_YEAR_STUDENTS, query);
			}
		}
		return query.execute();
	}
}
