/*
 * $Id: NackaHighSchoolYearPlacementReportModel.java,v 1.3 2003/12/16 12:20:33 anders Exp $
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
 * Report model for high school placements per year for students in Nacka.
 * <p>
 * Last modified: $Date: 2003/12/16 12:20:33 $ by $Author: anders $
 *
 * @author Anders Lindman
 * @version $Revision: 1.3 $
 */
public class NackaHighSchoolYearPlacementReportModel extends ReportModel {

	private final static int COLUMN_SIZE = 24;
	
	private final static int ROW_METHOD_STUDY_PATH = 1;
	private final static int ROW_METHOD_SHARE = 2;
	private final static int ROW_METHOD_TOTAL = 3;

	private final static int COLUMN_METHOD_NACKA_COMMUNE = 101;
	private final static int COLUMN_METHOD_OTHER_COMMUNES = 102;
	private final static int COLUMN_METHOD_COUNTY_COUNCIL = 103;
	private final static int COLUMN_METHOD_FREE_STANDING = 104;
	private final static int COLUMN_METHOD_TOTAL = 105;
	private final static int COLUMN_METHOD_NACKA_COMMUNE_SUM = 106;
	private final static int COLUMN_METHOD_OTHER_COMMUNES_SUM = 107;
	private final static int COLUMN_METHOD_COUNTY_COUNCIL_SUM = 108;
	private final static int COLUMN_METHOD_FREE_STANDING_SUM = 109;
	
	private final static String KEY_REPORT_TITLE = KP + "title_nacka_high_school_year_placements";

	/**
	 * Constructs this report model.
	 * @param reportBusiness the report business instance for calculating cell values
	 */
	public NackaHighSchoolYearPlacementReportModel(ReportBusiness reportBusiness) {
		super(reportBusiness);
		try {
			Collection studyPaths = reportBusiness.getAllStudyPaths();
			int rowSize = 0;
			rowSize += studyPaths.size() + 2; 
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
			headers = new Header[studyPaths.size() + 2];
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
		Header child0 = new Header(KEY_SCHOOL_YEAR_1, Header.HEADERTYPE_COLUMN_NORMAL);
		Header child1 = new Header(KEY_SCHOOL_YEAR_2, Header.HEADERTYPE_COLUMN_NORMAL);
		Header child2 = new Header(KEY_SCHOOL_YEAR_3, Header.HEADERTYPE_COLUMN_NORMAL);
		Header child3 = new Header(KEY_SCHOOL_YEAR_4, Header.HEADERTYPE_COLUMN_NORMAL);
		Header child4 = new Header(KEY_SUM, Header.HEADERTYPE_COLUMN_NORMAL);
		h.setChild(0, child0);
		h.setChild(1, child1);
		h.setChild(2, child2);
		h.setChild(3, child3);
		h.setChild(4, child4);
		headers[0] = h;
		
		h = new Header(KEY_OTHER_COMMUNES, Header.HEADERTYPE_COLUMN_HEADER, 5);
		child0 = new Header(KEY_SCHOOL_YEAR_1, Header.HEADERTYPE_COLUMN_NORMAL);
		child1 = new Header(KEY_SCHOOL_YEAR_2, Header.HEADERTYPE_COLUMN_NORMAL);
		child2 = new Header(KEY_SCHOOL_YEAR_3, Header.HEADERTYPE_COLUMN_NORMAL);
		child3 = new Header(KEY_SCHOOL_YEAR_4, Header.HEADERTYPE_COLUMN_NORMAL);
		child4 = new Header(KEY_SUM, Header.HEADERTYPE_COLUMN_NORMAL);
		h.setChild(0, child0);
		h.setChild(1, child1);
		h.setChild(2, child2);
		h.setChild(3, child3);
		h.setChild(4, child4);
		headers[1] = h;
		
		h = new Header(KEY_COUNTY_COUNCIL, Header.HEADERTYPE_COLUMN_HEADER, 4);
		child0 = new Header(KEY_SCHOOL_YEAR_1, Header.HEADERTYPE_COLUMN_NORMAL);
		child1 = new Header(KEY_SCHOOL_YEAR_2, Header.HEADERTYPE_COLUMN_NORMAL);
		child2 = new Header(KEY_SCHOOL_YEAR_3, Header.HEADERTYPE_COLUMN_NORMAL);
		child3 = new Header(KEY_SUM, Header.HEADERTYPE_COLUMN_NORMAL);
		h.setChild(0, child0);
		h.setChild(1, child1);
		h.setChild(2, child2);
		h.setChild(3, child3);
		headers[2] = h;
		
		h = new Header(KEY_FREE_STANDING, Header.HEADERTYPE_COLUMN_HEADER, 5);
		child0 = new Header(KEY_SCHOOL_YEAR_1, Header.HEADERTYPE_COLUMN_NORMAL);
		child1 = new Header(KEY_SCHOOL_YEAR_2, Header.HEADERTYPE_COLUMN_NORMAL);
		child2 = new Header(KEY_SCHOOL_YEAR_3, Header.HEADERTYPE_COLUMN_NORMAL);
		child3 = new Header(KEY_SCHOOL_YEAR_4, Header.HEADERTYPE_COLUMN_NORMAL);
		child4 = new Header(KEY_SUM, Header.HEADERTYPE_COLUMN_NORMAL);
		h.setChild(0, child0);
		h.setChild(1, child1);
		h.setChild(2, child2);
		h.setChild(3, child3);
		h.setChild(4, child4);
		headers[3] = h;
				
		h = new Header(KEY_TOTAL, Header.HEADERTYPE_COLUMN_HEADER, 5);
		child0 = new Header(KEY_SCHOOL_YEAR_1, Header.HEADERTYPE_COLUMN_NORMAL);
		child1 = new Header(KEY_SCHOOL_YEAR_2, Header.HEADERTYPE_COLUMN_NORMAL);
		child2 = new Header(KEY_SCHOOL_YEAR_3, Header.HEADERTYPE_COLUMN_NORMAL);
		child3 = new Header(KEY_SCHOOL_YEAR_4, Header.HEADERTYPE_COLUMN_NORMAL);
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
			Object columnParameter = null;
			switch (column) {
				case 0:
					columnMethod = COLUMN_METHOD_NACKA_COMMUNE;
					columnParameter = "1";
					break;
				case 1:
					columnMethod = COLUMN_METHOD_NACKA_COMMUNE;
					columnParameter = "2";
					break;
				case 2:
					columnMethod = COLUMN_METHOD_NACKA_COMMUNE;
					columnParameter = "3";
					break;
				case 3:
					columnMethod = COLUMN_METHOD_NACKA_COMMUNE;
					columnParameter = "4";
					break;
				case 4:
					columnMethod = COLUMN_METHOD_NACKA_COMMUNE_SUM;
					columnParameter = null;
					break;
				case 5:
					columnMethod = COLUMN_METHOD_OTHER_COMMUNES;
					columnParameter = "1";
					break;
				case 6:
					columnMethod = COLUMN_METHOD_OTHER_COMMUNES;
					columnParameter = "2";
					break;
				case 7:
					columnMethod = COLUMN_METHOD_OTHER_COMMUNES;
					columnParameter = "3";
					break;
				case 8:
					columnMethod = COLUMN_METHOD_OTHER_COMMUNES;
					columnParameter = "4";
					break;
				case 9:
					columnMethod = COLUMN_METHOD_OTHER_COMMUNES_SUM;
					columnParameter = null;
					break;
				case 10:
					columnMethod = COLUMN_METHOD_COUNTY_COUNCIL;
					columnParameter = "1";
					break;
				case 11:
					columnMethod = COLUMN_METHOD_COUNTY_COUNCIL;
					columnParameter = "2";
					break;
				case 12:
					columnMethod = COLUMN_METHOD_COUNTY_COUNCIL;
					columnParameter = "3";
					break;
				case 13:
					columnMethod = COLUMN_METHOD_COUNTY_COUNCIL_SUM;
					columnParameter = null;
					break;
				case 14:
					columnMethod = COLUMN_METHOD_FREE_STANDING;
					columnParameter = "1";
					break;
				case 15:
					columnMethod = COLUMN_METHOD_FREE_STANDING;
					columnParameter = "2";
					break;
				case 16:
					columnMethod = COLUMN_METHOD_FREE_STANDING;
					columnParameter = "3";
					break;
				case 17:
					columnMethod = COLUMN_METHOD_FREE_STANDING;
					columnParameter = "4";
					break;
				case 18:
					columnMethod = COLUMN_METHOD_FREE_STANDING_SUM;
					columnParameter = null;
					break;
				case 19:
					columnMethod = COLUMN_METHOD_TOTAL;
					columnParameter = "1";
					break;
				case 20:
					columnMethod = COLUMN_METHOD_TOTAL;
					columnParameter = "2";
					break;
				case 21:
					columnMethod = COLUMN_METHOD_TOTAL;
					columnParameter = "3";
					break;
				case 22:
					columnMethod = COLUMN_METHOD_TOTAL;
					columnParameter = "4";
					break;
				case 23:
					columnMethod = COLUMN_METHOD_TOTAL;
					columnParameter = null;
					break;
			}

			try {
				ReportBusiness rb = getReportBusiness();
				Collection studyPaths = rb.getAllStudyPaths();
				Iterator iter = studyPaths.iterator();
				while (iter.hasNext()) {
					SchoolStudyPath studyPath = (SchoolStudyPath) iter.next();
					Object rowParameter = studyPath.getPrimaryKey();
					Cell cell = new Cell(this, row, column, ROW_METHOD_STUDY_PATH,
							columnMethod, rowParameter, columnParameter, Cell.CELLTYPE_NORMAL);
					setCell(row, column, cell);
					row++;
				}
				Cell cell = new Cell(this, row, column, ROW_METHOD_TOTAL,
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
//		ReportBusiness reportBusiness = getReportBusiness();
//		int studyPathId = -1;
//		if (cell.getRowParameter() != null) {
//			studyPathId = ((Integer) cell.getRowParameter()).intValue();
//		}
		String schoolYearName = (String) cell.getColumnParameter();
		int row = cell.getRow();
		int column = cell.getColumn();
		
		switch (cell.getRowMethod()) {
			case ROW_METHOD_STUDY_PATH:
				switch (cell.getColumnMethod()) {
					case COLUMN_METHOD_NACKA_COMMUNE:
						if (schoolYearName == null) {
							value += getCell(row, 0).getFloatValue();
							value += getCell(row, 1).getFloatValue();
							value += getCell(row, 2).getFloatValue();
							value += getCell(row, 3).getFloatValue();
						} else {
							//
						}
						break;
					case COLUMN_METHOD_OTHER_COMMUNES:
						if (schoolYearName == null) {
							value += getCell(row, 5).getFloatValue();
							value += getCell(row, 6).getFloatValue();
							value += getCell(row, 7).getFloatValue();
							value += getCell(row, 8).getFloatValue();
						} else {
							//
						}
						break;
					case COLUMN_METHOD_COUNTY_COUNCIL:
						if (schoolYearName == null) {
							value += getCell(row, 10).getFloatValue();
							value += getCell(row, 11).getFloatValue();
							value += getCell(row, 12).getFloatValue();
						} else {
							//
						}
						break;
					case COLUMN_METHOD_FREE_STANDING:
						if (schoolYearName == null) {
							value += getCell(row, 14).getFloatValue();
							value += getCell(row, 15).getFloatValue();
							value += getCell(row, 16).getFloatValue();
							value += getCell(row, 17).getFloatValue();
						} else {
							//
						}
						break;
					case COLUMN_METHOD_TOTAL:
						if (schoolYearName == null) {
							value += getCell(row, 19).getFloatValue();
							value += getCell(row, 20).getFloatValue();
							value += getCell(row, 21).getFloatValue();
							value += getCell(row, 22).getFloatValue();
						} else {
							//
						}
						break;
				}
				break;
			case ROW_METHOD_TOTAL:
				for (int i = 0; i < cell.getRow(); i++) {
					Cell c = getCell(i, column);
					value += c.getFloatValue();
				}
				break;
			case ROW_METHOD_SHARE:
				switch (cell.getColumnMethod()) {
					case COLUMN_METHOD_NACKA_COMMUNE:
						if (schoolYearName == null) {
							float total = 0f;
							total += getCell(row - 1, 0).getFloatValue();
							total += getCell(row - 1, 5).getFloatValue();
							total += getCell(row - 1, 10).getFloatValue();
							total += getCell(row - 1, 14).getFloatValue();
							value = getCell(row - 1, 0).getFloatValue() / total;
						}
						break;
					case COLUMN_METHOD_OTHER_COMMUNES:
						if (schoolYearName == null) {
							float total = 0f;
							total += getCell(row - 1, 0).getFloatValue();
							total += getCell(row - 1, 5).getFloatValue();
							total += getCell(row - 1, 10).getFloatValue();
							total += getCell(row - 1, 14).getFloatValue();
							value = getCell(row - 1, 5).getFloatValue() / total;
						}
						break;
					case COLUMN_METHOD_COUNTY_COUNCIL:
						if (schoolYearName == null) {
							float total = 0f;
							total += getCell(row - 1, 0).getFloatValue();
							total += getCell(row - 1, 5).getFloatValue();
							total += getCell(row - 1, 10).getFloatValue();
							total += getCell(row - 1, 14).getFloatValue();
							value = getCell(row - 1, 10).getFloatValue() / total;
						}
						break;
					case COLUMN_METHOD_FREE_STANDING:
						if (schoolYearName == null) {
							float total = 0f;
							total += getCell(row - 1, 0).getFloatValue();
							total += getCell(row - 1, 5).getFloatValue();
							total += getCell(row - 1, 10).getFloatValue();
							total += getCell(row - 1, 14).getFloatValue();
							value = getCell(row - 1, 14).getFloatValue() / total;
						}
						break;
					case COLUMN_METHOD_TOTAL:
						if (schoolYearName == null) {
							value = 100.0f;
						} else {
							value = getCell(row - 1, column).getFloatValue() / getCell(row - 1, 23).getFloatValue();
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
}
