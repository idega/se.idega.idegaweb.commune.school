/*
 * $Id: NackaCCTotalPlacementReportModel.java,v 1.4 2004/02/23 14:55:36 anders Exp $
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
 * Report model for total number of child care placements in Nacka.
 * <p>
 * Last modified: $Date: 2004/02/23 14:55:36 $ by $Author: anders $
 *
 * @author Anders Lindman
 * @version $Revision: 1.4 $
 */
public class NackaCCTotalPlacementReportModel extends ReportModel {

	private final static int ROW_SIZE = 7;
	private final static int COLUMN_SIZE = 1;
	
	private final static int ROW_METHOD_COUNT_PLACEMENTS = 1;
	private final static int ROW_METHOD_TOTAL_PRE_SCHOOL_OPERATION = 2;
	private final static int ROW_METHOD_TOTAL_AFTER_SCHOOL_OPERATION = 3;
	private final static int ROW_METHOD_TOTAL = 4;


	private final static int COLUMN_METHOD_COUNT = 101;

	private final static String QUERY_TOTAL_PLACEMENTS = "total_placements";
	
	private final static String KEY_REPORT_TITLE = KP + "title_nacka_child_care_total_placements";
	
	private final static int SCHOOL_TYPE_PRE_SCHOOL = 1;
	private final static int SCHOOL_TYPE_FAMILY_DAYCARE = 2;
	private final static int SCHOOL_TYPE_AFTER_SCHOOL = 3;
	private final static int SCHOOL_TYPE_FAMILY_AFTER_SCHOOL = 4;
	
	/**
	 * Constructs this report model.
	 * @param reportBusiness the report business instance for calculating cell values
	 */
	public NackaCCTotalPlacementReportModel(ReportBusiness reportBusiness) {
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
		Header[] headers = new Header[9];
		
		headers[0] = new Header(KEY_PRE_SCHOOL, Header.HEADERTYPE_ROW_HEADER);
		headers[1] = new Header(KEY_FAMILY_DAYCARE, Header.HEADERTYPE_ROW_HEADER);
		headers[2] = new Header(KEY_TOTAL_PRE_SCHOOL_OPERATION, Header.HEADERTYPE_ROW_HEADER);
		headers[3] = new Header(null, Header.HEADERTYPE_ROW_SPACER);
		headers[4] = new Header(KEY_AFTER_SCHOOL, Header.HEADERTYPE_ROW_HEADER);
		headers[5] = new Header(KEY_FAMILY_AFTER_SCHOOL, Header.HEADERTYPE_ROW_HEADER);
		headers[6] = new Header(KEY_TOTAL_AFTER_SCHOOL_OPERATION, Header.HEADERTYPE_ROW_HEADER);
		headers[7] = new Header(null, Header.HEADERTYPE_ROW_SPACER);
		headers[8] = new Header(KEY_TOTAL, Header.HEADERTYPE_ROW_HEADER);

		return headers;
	}
	
	/**
	 * @see se.idega.idegaweb.commune.school.report.business.ReportModel#buildColumnHeaders()
	 */
	protected Header[] buildColumnHeaders() {
		Header[] headers = new Header[1];
		
		headers[0] = new Header(KEY_NUMBER_OF_CHILDREN, Header.HEADERTYPE_COLUMN_HEADER);
		
		return headers;
	}
	
	/**
	 * @see se.idega.idegaweb.commune.school.report.business.ReportModel#buildCells()
	 */
	protected void buildCells() {
		for (int column = 0; column < getColumnSize(); column++) {
			int row = 0;
			int columnMethod = COLUMN_METHOD_COUNT;
			Object columnParameter = null;	
			Object rowParameter = null;
			
			Cell cell = new Cell(this, row, column, ROW_METHOD_COUNT_PLACEMENTS, 
					columnMethod, new Integer(SCHOOL_TYPE_PRE_SCHOOL), columnParameter, Cell.CELLTYPE_NORMAL);
			setCell(row++, column, cell);

			cell = new Cell(this, row, column, ROW_METHOD_COUNT_PLACEMENTS, 
					columnMethod, new Integer(SCHOOL_TYPE_FAMILY_DAYCARE), columnParameter, Cell.CELLTYPE_NORMAL);
			setCell(row++, column, cell);
			
			cell = new Cell(this, row, column, ROW_METHOD_TOTAL_PRE_SCHOOL_OPERATION, 
					columnMethod, rowParameter, columnParameter, Cell.CELLTYPE_NORMAL);
			setCell(row++, column, cell);
			
			cell = new Cell(this, row, column, ROW_METHOD_COUNT_PLACEMENTS, 
					columnMethod, new Integer(SCHOOL_TYPE_AFTER_SCHOOL), columnParameter, Cell.CELLTYPE_NORMAL);
			setCell(row++, column, cell);

			cell = new Cell(this, row, column, ROW_METHOD_COUNT_PLACEMENTS, 
					columnMethod, new Integer(SCHOOL_TYPE_FAMILY_AFTER_SCHOOL), columnParameter, Cell.CELLTYPE_NORMAL);
			setCell(row++, column, cell);
			
			cell = new Cell(this, row, column, ROW_METHOD_TOTAL_AFTER_SCHOOL_OPERATION, 
					columnMethod, rowParameter, columnParameter, Cell.CELLTYPE_NORMAL);
			setCell(row++, column, cell);

			cell = new Cell(this, row, column, ROW_METHOD_TOTAL, 
					columnMethod, rowParameter, columnParameter, Cell.CELLTYPE_NORMAL);
			setCell(row++, column, cell);
		}
	}
	
	/**
	 * @see se.idega.idegaweb.commune.school.report.business.ReportModel#calculate()
	 */
	protected float calculate(Cell cell) throws RemoteException {
		float value = 0f;

		switch (cell.getColumnMethod()) {
			case COLUMN_METHOD_COUNT:
				switch (cell.getRowMethod()) {
					case ROW_METHOD_COUNT_PLACEMENTS:
						Integer schoolType = (Integer) cell.getRowParameter();
						value = getChildCareTotalPlacementCount(schoolType.intValue());
						break;
					case ROW_METHOD_TOTAL_PRE_SCHOOL_OPERATION:
						value = getCell(0, 0).getFloatValue() + getCell(1, 0).getFloatValue();
						break;
					case ROW_METHOD_TOTAL_AFTER_SCHOOL_OPERATION:
						value = getCell(3, 0).getFloatValue() + getCell(4, 0).getFloatValue();
						break;
					case ROW_METHOD_TOTAL:
						value = getCell(2, 0).getFloatValue() + getCell(5, 0).getFloatValue();
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
	
	/**
	 * Returns the number of total child placements in Nacka commune.
	 */
	protected int getChildCareTotalPlacementCount(int schoolType) throws RemoteException {
		ReportBusiness rb = getReportBusiness();
		int schoolType1 = 0;
		int schoolType2 = 0;
		int schoolType3 = 0;
		int schoolType4 = 0;

		switch (schoolType) {
			case SCHOOL_TYPE_PRE_SCHOOL:
				schoolType1 = rb.getPreSchoolTypeId(); 
				schoolType2 = rb.getGeneralPreSchoolTypeId();
				schoolType3 = schoolType2;
				schoolType4 = schoolType2;
				break;
			case SCHOOL_TYPE_FAMILY_DAYCARE:
				schoolType1 = rb.getFamilyDayCareSchoolTypeId();
				schoolType2 = rb.getGeneralFamilyDaycareSchoolTypeId();
				schoolType3 = schoolType2;
				schoolType4 = schoolType2;
				break;
			case SCHOOL_TYPE_AFTER_SCHOOL:
				schoolType1 = rb.getAfterSchool6TypeId();
				schoolType2 = rb.getAfterSchool7_9TypeId();
				schoolType3 = schoolType2;
				schoolType4 = schoolType2;
				break;
			case SCHOOL_TYPE_FAMILY_AFTER_SCHOOL:
				schoolType1 = rb.getFamilyAfterSchool6TypeId();
				schoolType2 = rb.getFamilyAfterSchool7_9TypeId();
				schoolType3 = schoolType2;
				schoolType4 = schoolType2;
				break;
		}
		
		PreparedQuery query = null;
		query = getQuery(QUERY_TOTAL_PLACEMENTS);
		if (query == null) {
			query = new PreparedQuery(getConnection());
			query.setSelectCount();
			query.setChildCarePlacements();
			query.setOnlyNackaCitizens();
			query.setFourSchoolTypes(); // parameter 1-4
			query.prepare();
			setQuery(QUERY_TOTAL_PLACEMENTS, query);
		}
		query.setInt(1, schoolType1);
		query.setInt(2, schoolType2);
		query.setInt(3, schoolType3);
		query.setInt(4, schoolType4);

		return query.execute();
	}
}
