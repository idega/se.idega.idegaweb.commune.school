/*
 * $Id: NackaCompulsorySchoolOCCPlacementReportModel.java,v 1.2 2004/01/12 13:43:59 anders Exp $
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
 * Report model for placements in Nacka compulsory schools for students in other communes.
 * <p>
 * Last modified: $Date: 2004/01/12 13:43:59 $ by $Author: anders $
 *
 * @author Anders Lindman
 * @version $Revision: 1.2 $
 */
public class NackaCompulsorySchoolOCCPlacementReportModel extends ReportModel {

	private final static int COLUMN_SIZE = 14;
	
	private final static int ROW_METHOD_SCHOOL = 1;
	private final static int ROW_METHOD_SUM = 2;
	private final static int ROW_METHOD_TOTAL = 3;

	private final static int COLUMN_METHOD_SCHOOL_YEAR = 101;
	private final static int COLUMN_METHOD_SUM_1_3 = 102;
	private final static int COLUMN_METHOD_SUM_4_6 = 103;
	private final static int COLUMN_METHOD_SUM_7_10 = 104;
	private final static int COLUMN_METHOD_TOTAL_1_10 = 105;
	
	private final static String KEY_REPORT_TITLE = KP + "title_nacka_compulsory_school_other_commune_citizen_placements";

	/**
	 * Constructs this report model.
	 * @param reportBusiness the report business instance for calculating cell values
	 */
	public NackaCompulsorySchoolOCCPlacementReportModel(ReportBusiness reportBusiness) {
		super(reportBusiness);
		try {
			Collection areas = reportBusiness.getCompulsorySchoolAreas();
			int rowSize = 1;
			Iterator iter = areas.iterator();
			while (iter.hasNext()) {
				SchoolArea area = (SchoolArea) iter.next();
				Collection schools = reportBusiness.getCompulsorySchools(area);
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
			Collection areas = rb.getCompulsorySchoolAreas();
			headers = new Header[areas.size() + 1];
			Iterator areaIter = areas.iterator();
			int headerIndex = 0;
			while (areaIter.hasNext()) {
				SchoolArea area = (SchoolArea) areaIter.next();
				Collection schools = getReportBusiness().getCompulsorySchools(area);
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
		Header[] headers = new Header[7];
		
		Header h = new Header(KEY_SCHOOL_YEAR, Header.HEADERTYPE_COLUMN_NORMAL, 3);
		Header child0 = new Header(KEY_SCHOOL_YEAR_1, Header.HEADERTYPE_COLUMN_NORMAL);
		Header child1 = new Header(KEY_SCHOOL_YEAR_2, Header.HEADERTYPE_COLUMN_NORMAL);
		Header child2 = new Header(KEY_SCHOOL_YEAR_3, Header.HEADERTYPE_COLUMN_NORMAL);
		h.setChild(0, child0);
		h.setChild(1, child1);
		h.setChild(2, child2);
		headers[0] = h;
		
		h = new Header(KEY_SUM, Header.HEADERTYPE_COLUMN_HEADER, 1);
		child0 = new Header(KEY_SUM_1_3, Header.HEADERTYPE_COLUMN_NORMAL);
		h.setChild(0, child0);
		headers[1] = h;
		
		h = new Header(KEY_SCHOOL_YEAR, Header.HEADERTYPE_COLUMN_HEADER, 3);
		child0 = new Header(KEY_SCHOOL_YEAR_4, Header.HEADERTYPE_COLUMN_NORMAL);
		child1 = new Header(KEY_SCHOOL_YEAR_5, Header.HEADERTYPE_COLUMN_NORMAL);
		child2 = new Header(KEY_SCHOOL_YEAR_6, Header.HEADERTYPE_COLUMN_NORMAL);
		h.setChild(0, child0);
		h.setChild(1, child1);
		h.setChild(2, child2);
		headers[2] = h;
		
		h = new Header(KEY_SUM, Header.HEADERTYPE_COLUMN_HEADER, 1);
		child0 = new Header(KEY_SUM_4_6, Header.HEADERTYPE_COLUMN_NORMAL);
		h.setChild(0, child0);
		headers[3] = h;

		h = new Header(KEY_SCHOOL_YEAR, Header.HEADERTYPE_COLUMN_HEADER, 4);
		child0 = new Header(KEY_SCHOOL_YEAR_7, Header.HEADERTYPE_COLUMN_NORMAL);
		child1 = new Header(KEY_SCHOOL_YEAR_8, Header.HEADERTYPE_COLUMN_NORMAL);
		child2 = new Header(KEY_SCHOOL_YEAR_9, Header.HEADERTYPE_COLUMN_NORMAL);
		Header child3 = new Header(KEY_SCHOOL_YEAR_10, Header.HEADERTYPE_COLUMN_NORMAL);
		h.setChild(0, child0);
		h.setChild(1, child1);
		h.setChild(2, child2);
		h.setChild(3, child3);
		headers[4] = h;

		h = new Header(KEY_SUM, Header.HEADERTYPE_COLUMN_HEADER, 1);
		child0 = new Header(KEY_SUM_7_9, Header.HEADERTYPE_COLUMN_NORMAL);
		h.setChild(0, child0);
		headers[5] = h;
		
		h = new Header(KEY_TOTAL, Header.HEADERTYPE_COLUMN_HEADER, 1);
		child0 = new Header(KEY_TOTAL_1_10, Header.HEADERTYPE_COLUMN_NORMAL);
		h.setChild(0, child0);
		headers[6] = h;
		
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
					columnParameter = "S1";
					break;
				case 1:
					columnMethod = COLUMN_METHOD_SCHOOL_YEAR;
					columnParameter = "S2";
					break;
				case 2:
					columnMethod = COLUMN_METHOD_SCHOOL_YEAR;
					columnParameter = "S3";
					break;
				case 3:
					columnMethod = COLUMN_METHOD_SUM_1_3;
					columnParameter = null;
					break;
				case 4:
					columnMethod = COLUMN_METHOD_SCHOOL_YEAR;
					columnParameter = "S4";
					break;
				case 5:
					columnMethod = COLUMN_METHOD_SCHOOL_YEAR;
					columnParameter = "S5";
					break;
				case 6:
					columnMethod = COLUMN_METHOD_SCHOOL_YEAR;
					columnParameter = "S6";
					break;
				case 7:
					columnMethod = COLUMN_METHOD_SUM_4_6;
					columnParameter = null;
					break;
				case 8:
					columnMethod = COLUMN_METHOD_SCHOOL_YEAR;
					columnParameter = "S7";
					break;
				case 9:
					columnMethod = COLUMN_METHOD_SCHOOL_YEAR;
					columnParameter = "S8";
					break;
				case 10:
					columnMethod = COLUMN_METHOD_SCHOOL_YEAR;
					columnParameter = "S9";
					break;
				case 11:
					columnMethod = COLUMN_METHOD_SCHOOL_YEAR;
					columnParameter = "S10";
					break;
				case 12:
					columnMethod = COLUMN_METHOD_SUM_7_10;
					columnParameter = null;
					break;
				case 13:
					columnMethod = COLUMN_METHOD_TOTAL_1_10;
					columnParameter = null;
					break;
			}

			try {
				ReportBusiness rb = getReportBusiness();
				Collection areas = rb.getCompulsorySchoolAreas();
				Iterator areaIter = areas.iterator();
				while (areaIter.hasNext()) {
					SchoolArea area = (SchoolArea) areaIter.next();
					Collection schools = getReportBusiness().getCompulsorySchools(area);
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
					value = getCompulsorySchoolPlacementCount(schoolId, schoolYearName);
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
					for (int i = 0; i < 3; i++) {
						value += getCell(cell.getRow(), i).getFloatValue();
					}
					break;
				case COLUMN_METHOD_SUM_4_6:
					for (int i = 4; i < 7; i++) {
						value += getCell(cell.getRow(), i).getFloatValue();
					}
					break;
				case COLUMN_METHOD_SUM_7_10:
					for (int i = 8; i < 12; i++) {
						value += getCell(cell.getRow(), i).getFloatValue();
					}
					break;
				case COLUMN_METHOD_TOTAL_1_10:
					value = getCell(cell.getRow(), 3).getFloatValue() +
							getCell(cell.getRow(), 7).getFloatValue() +
							getCell(cell.getRow(), 12).getFloatValue();
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
	 * Returns the number of student placements for the specified school and school year.
	 */
	protected int getCompulsorySchoolPlacementCount(int schoolId, String schoolYearName) throws RemoteException {
		ReportQuery query = new ReportQuery();
		query.setSelectCountOCCPlacements(getReportBusiness().getSchoolSeasonId(), schoolId);
		query.setSchoolTypeCompulsorySchool();
		query.setSchoolYear(schoolYearName);			
		return query.execute();
	}
}
