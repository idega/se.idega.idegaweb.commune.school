/*
 * $Id: NackaElementarySchoolOCCPlacementReportModel.java,v 1.5 2004/01/15 12:50:05 anders Exp $
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

import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolArea;

/** 
 * Report model for placements in Nacka elementary schools for commune citizens outside Nacka.
 * <p>
 * Last modified: $Date: 2004/01/15 12:50:05 $ by $Author: anders $
 *
 * @author Anders Lindman
 * @version $Revision: 1.5 $
 */
public class NackaElementarySchoolOCCPlacementReportModel extends ReportModel {

	private final static int COLUMN_SIZE = 16;
	
	private final static int ROW_METHOD_SCHOOL = 1;
	private final static int ROW_METHOD_SUM = 2;
	private final static int ROW_METHOD_TOTAL = 3;

	private final static int COLUMN_METHOD_SCHOOL_YEAR = 101;
	private final static int COLUMN_METHOD_SUM_1_3 = 102;
	private final static int COLUMN_METHOD_SUM_4_6 = 103;
	private final static int COLUMN_METHOD_SUM_7_9 = 104;
	private final static int COLUMN_METHOD_TOTAL_1_9 = 105;
	private final static int COLUMN_METHOD_TOTAL_F_9 = 106;

	private final static String QUERY_ELEMENTARY = "elementary";
	private final static String QUERY_ELEMENTARY_6_YEAR_STUDENTS = "elementary_6";	
	
	private final static String KEY_REPORT_TITLE = KP + "title_nacka_elementary_school_other_commune_citizens_placements";

	/**
	 * Constructs this report model.
	 * @param reportBusiness the report business instance for calculating cell values
	 */
	public NackaElementarySchoolOCCPlacementReportModel(ReportBusiness reportBusiness) {
		super(reportBusiness);
		try {
			Collection areas = reportBusiness.getElementarySchoolAreas();
			int rowSize = 0;
			Iterator iter = areas.iterator();
			while (iter.hasNext()) {
				SchoolArea area = (SchoolArea) iter.next();
				Collection schools = reportBusiness.getElementarySchools(area);
				rowSize += schools.size() + 1; // Sum row
			}
			rowSize += 1; // Total row 
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
			Collection areas = rb.getElementarySchoolAreas();
			headers = new Header[areas.size() + 1];
			Iterator areaIter = areas.iterator();
			int headerIndex = 0;
			while (areaIter.hasNext()) {
				SchoolArea area = (SchoolArea) areaIter.next();
				Collection schools = getReportBusiness().getElementarySchools(area);
				headers[headerIndex] = new Header(area.getName(), Header.HEADERTYPE_ROW_NONLOCALIZED_HEADER, schools.size() + 1);
				Iterator schoolIter = schools.iterator();
				int childIndex = 0;
				while (schoolIter.hasNext()) {
					School school = (School) schoolIter.next();
					Header child = new Header(school.getName(), Header.HEADERTYPE_ROW_NONLOCALIZED_NORMAL);
					headers[headerIndex].setChild(childIndex, child);
					childIndex++;
				}
				Header header = new Header(KEY_SUM, Header.HEADERTYPE_ROW_SUM);
				headers[headerIndex].setChild(childIndex, header);
				headerIndex++;
			}
			Header header = new Header(KEY_TOTAL, Header.HEADERTYPE_ROW_TOTAL);
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
		Header[] headers = new Header[10];
		
		Header h = new Header(KEY_SCHOOL_YEAR_F, Header.HEADERTYPE_COLUMN_NORMAL);
		headers[0] = h;
		
		h = new Header(KEY_SCHOOL_YEAR, Header.HEADERTYPE_COLUMN_NORMAL, 3);
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

		h = new Header(KEY_SCHOOL_YEAR, Header.HEADERTYPE_COLUMN_HEADER, 3);
		child0 = new Header(KEY_SCHOOL_YEAR_7, Header.HEADERTYPE_COLUMN_NORMAL);
		child1 = new Header(KEY_SCHOOL_YEAR_8, Header.HEADERTYPE_COLUMN_NORMAL);
		child2 = new Header(KEY_SCHOOL_YEAR_9, Header.HEADERTYPE_COLUMN_NORMAL);
		h.setChild(0, child0);
		h.setChild(1, child1);
		h.setChild(2, child2);
		headers[5] = h;

		h = new Header(KEY_SUM, Header.HEADERTYPE_COLUMN_HEADER, 1);
		child0 = new Header(KEY_SUM_7_9, Header.HEADERTYPE_COLUMN_NORMAL);
		h.setChild(0, child0);
		headers[6] = h;
		
		h = new Header(KEY_TOTAL, Header.HEADERTYPE_COLUMN_HEADER, 1);
		child0 = new Header(KEY_TOTAL_1_9, Header.HEADERTYPE_COLUMN_NORMAL);
		h.setChild(0, child0);
		headers[7] = h;
		
		h = new Header(KEY_TOTAL, Header.HEADERTYPE_COLUMN_HEADER, 1);
		child0 = new Header(KEY_TOTAL_F_9, Header.HEADERTYPE_COLUMN_NORMAL);
		h.setChild(0, child0);
		headers[8] = h;

		h = new Header(KEY_SIX_YEARS_STUDENTS, Header.HEADERTYPE_COLUMN_NORMAL);
		headers[9] = h;
				
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
					columnMethod = COLUMN_METHOD_SUM_7_9;
					columnParameter = null;
					break;
				case 13:
					columnMethod = COLUMN_METHOD_TOTAL_1_9;
					columnParameter = null;
					break;
				case 14:
					columnMethod = COLUMN_METHOD_TOTAL_F_9;
					columnParameter = null;
					break;
				case 15:
					columnMethod = COLUMN_METHOD_SCHOOL_YEAR;
					columnParameter = "0";
					break;
			}

			try {
				ReportBusiness rb = getReportBusiness();
				Collection areas = rb.getElementarySchoolAreas();
				Iterator areaIter = areas.iterator();
				while (areaIter.hasNext()) {
					SchoolArea area = (SchoolArea) areaIter.next();
					Collection schools = getReportBusiness().getElementarySchools(area);
					Iterator schoolIter = schools.iterator();
					while (schoolIter.hasNext()) {
						School school = (School) schoolIter.next();
						Object rowParameter = school.getPrimaryKey();
						Cell cell = new Cell(this, row, column, ROW_METHOD_SCHOOL,
								columnMethod, rowParameter, columnParameter, Cell.CELLTYPE_NORMAL);
						setCell(row, column, cell);
						row++;
					}
					Cell cell = new Cell(this, row, column, ROW_METHOD_SUM,
							columnMethod, null, columnParameter, Cell.CELLTYPE_SUM);
					setCell(row, column, cell);
					row++;
				}
				Cell cell = new Cell(this, row, column, ROW_METHOD_TOTAL,
						columnMethod, null, columnParameter, Cell.CELLTYPE_TOTAL);
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
		int schoolId = -1;
		if (cell.getRowParameter() != null) {
			schoolId = ((Integer) cell.getRowParameter()).intValue();
		}
		String schoolYearName = (String) cell.getColumnParameter();
		
		if (cell.getColumnMethod() == COLUMN_METHOD_SCHOOL_YEAR) {
			switch (cell.getRowMethod()) {
				case ROW_METHOD_SCHOOL:
					value = getElementarySchoolOCCPlacementCount(schoolId, schoolYearName);
					break;
				case ROW_METHOD_SUM:
					int rowIndex = cell.getRow() - 1;
					if (rowIndex < 0) {
						break;
					}
					Cell c = getCell(rowIndex, cell.getColumn()); 
					while (rowIndex >= 0 && c.getCellType() == Cell.CELLTYPE_NORMAL) {						
						value += c.getFloatValue();
						rowIndex--;
						if (rowIndex >= 0) {
							c = getCell(rowIndex, cell.getColumn());
						}
					}
					break;
				case ROW_METHOD_TOTAL:
					rowIndex = cell.getRow() - 1;
					while (rowIndex >= 0) {
						c = getCell(rowIndex, cell.getColumn());
						if (c.getCellType() == Cell.CELLTYPE_NORMAL) {
							value += c.getFloatValue();
						}
						rowIndex--;
					}
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
				case COLUMN_METHOD_SUM_7_9:
					for (int i = 9; i < 12; i++) {
						value += getCell(cell.getRow(), i).getFloatValue();
					}
					break;
				case COLUMN_METHOD_TOTAL_1_9:
					value = getCell(cell.getRow(), 4).getFloatValue() +
							getCell(cell.getRow(), 8).getFloatValue() +
							getCell(cell.getRow(), 12).getFloatValue();
					break;
				case COLUMN_METHOD_TOTAL_F_9:
					value = getCell(cell.getRow(), 13).getFloatValue() +
							getCell(cell.getRow(), 0).getFloatValue();
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
	 * Returns the number of student placements for the specified school and school year
	 * for students in communes outside Nacka (OCC = other commune citizens).
	 */
	protected int getElementarySchoolOCCPlacementCount(int schoolId, String schoolYearName) throws RemoteException {
		PreparedQuery query = null;
		if (!schoolYearName.equals("0")) {
			ReportBusiness rb = getReportBusiness();
			query = getQuery(QUERY_ELEMENTARY);
			if (query == null) {
				query = new PreparedQuery(getConnection());
				query.setSelectCount();
				query.setPlacements(rb.getSchoolSeasonId());
				query.setNotNackaCitizens();
				query.setSchoolType(); // parameter 1
				query.setSchoolYearName(); // parameter 2
				query.setSchool(); // parameter 3
				query.prepare();
				setQuery(QUERY_ELEMENTARY, query);
			}
			if (schoolYearName.equals("F")) {
				query.setInt(1, rb.getPreSchoolClassTypeId());
			} else {
				query.setInt(1, rb.getElementarySchoolTypeId());
			}
			query.setString(2, schoolYearName);
			query.setInt(3, schoolId);
		} else { // 6 years old students
			query = getQuery(QUERY_ELEMENTARY_6_YEAR_STUDENTS);
			if (query == null) {
				query = new PreparedQuery(getConnection());
				query.setSelectCount();
				query.setPlacements(getReportBusiness().getSchoolSeasonId());
				query.setNotNackaCitizens();
				query.setOnlyStudentsBorn(getReportBusiness().getSchoolSeasonStartYear() - 6);
				query.setSchoolYearName("1");
				query.setSchoolTypeElementarySchool();
				query.setSchool(); // parameter 1
				query.prepare();
				setQuery(QUERY_ELEMENTARY_6_YEAR_STUDENTS, query);
			}
			query.setInt(1, schoolId);
		}
		return query.execute();
	}
}
