/*
 * $Id: NackaCCHourIntervalReportModel.java,v 1.4 2004/09/24 07:44:29 malin Exp $
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
 * Report model for total number of child care placements per child care hour interval.
 * <p>
 * Last modified: $Date: 2004/09/24 07:44:29 $ by $Author: malin $
 *
 * @author Anders Lindman
 * @version $Revision: 1.4 $
 */
public class NackaCCHourIntervalReportModel extends ReportModel {

	private final static int ROW_SIZE = 7;
	private final static int COLUMN_SIZE = 6;
	
	private final static int ROW_METHOD_SUM_HOURS = 1;
	private final static int ROW_METHOD_TOTAL_PRE_SCHOOL_OPERATION = 2;
	private final static int ROW_METHOD_TOTAL_AFTER_SCHOOL_OPERATION = 3;
	private final static int ROW_METHOD_TOTAL = 4;

	/*private final static int COLUMN_METHOD_HOURS_1_15 = 101;
	private final static int COLUMN_METHOD_HOURS_16_25 = 102;
	private final static int COLUMN_METHOD_HOURS_26_35 = 103;
	private final static int COLUMN_METHOD_HOURS_36_ = 104;
*/
	private final static int COLUMN_METHOD_HOURS_1_15 = 101;
	private final static int COLUMN_METHOD_HOURS_16_24 = 102;
	private final static int COLUMN_METHOD_HOURS_25_29 = 103;
	private final static int COLUMN_METHOD_HOURS_30_34 = 104;
	private final static int COLUMN_METHOD_HOURS_35_39 = 105;
	private final static int COLUMN_METHOD_HOURS_40_ = 106;
	
	private final static String QUERY_HOUR_INTERVAL_PLACEMENTS = "hour_interval_placements";
	
	private final static String KEY_REPORT_TITLE = KP + "title_nacka_child_care_hour_interval_placements";

	private final static int SCHOOL_TYPE_PRE_SCHOOL = 1;
	private final static int SCHOOL_TYPE_FAMILY_DAYCARE = 2;
	private final static int SCHOOL_TYPE_AFTER_SCHOOL = 3;
	private final static int SCHOOL_TYPE_FAMILY_AFTER_SCHOOL = 4;

	/**
	 * Constructs this report model.
	 * @param reportBusiness the report business instance for calculating cell values
	 */
	public NackaCCHourIntervalReportModel(ReportBusiness reportBusiness) {
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
		
		Header h = new Header(KEY_CHILD_CARE_HOURS, Header.HEADERTYPE_COLUMN_HEADER, 6);
		Header child0 = new Header("1-15", Header.HEADERTYPE_COLUMN_NONLOCALIZED_HEADER);
		Header child1 = new Header("16-24", Header.HEADERTYPE_COLUMN_NONLOCALIZED_HEADER);
		Header child2 = new Header("25-29", Header.HEADERTYPE_COLUMN_NONLOCALIZED_HEADER);
		Header child3 = new Header("30-34", Header.HEADERTYPE_COLUMN_NONLOCALIZED_HEADER);
		Header child4 = new Header("35-39", Header.HEADERTYPE_COLUMN_NONLOCALIZED_HEADER);
		Header child5 = new Header(KEY_GTEQ_40, Header.HEADERTYPE_COLUMN_HEADER);
		h.setChild(0, child0);
		h.setChild(1, child1);
		h.setChild(2, child2);
		h.setChild(3, child3);
		h.setChild(4, child4);
		h.setChild(5, child5);
		headers[0] = h;
		
		return headers;
	}
	
	/**
	 * @see se.idega.idegaweb.commune.school.report.business.ReportModel#buildCells()
	 */
	protected void buildCells() {
		for (int column = 0; column < getColumnSize(); column++) {
			int row = 0;
			int columnMethod = 0;
			
			switch (column) {
				case 0:
					columnMethod = COLUMN_METHOD_HOURS_1_15;
					break;
				case 1:
					columnMethod = COLUMN_METHOD_HOURS_16_24;
					break;
				case 2:
					columnMethod = COLUMN_METHOD_HOURS_25_29;
					break;
				case 3:
					columnMethod = COLUMN_METHOD_HOURS_30_34;
					break;
				case 4:
					columnMethod = COLUMN_METHOD_HOURS_35_39;
					break;
				case 5:
					columnMethod = COLUMN_METHOD_HOURS_40_;
					break;
			}

			Object columnParameter = null;	
			Object rowParameter = null;
			Cell cell = new Cell(this, row, column, ROW_METHOD_SUM_HOURS, 
					columnMethod, new Integer(SCHOOL_TYPE_PRE_SCHOOL), columnParameter, Cell.CELLTYPE_NORMAL);
			setCell(row++, column, cell);

			cell = new Cell(this, row, column, ROW_METHOD_SUM_HOURS, 
					columnMethod, new Integer(SCHOOL_TYPE_FAMILY_DAYCARE), columnParameter, Cell.CELLTYPE_NORMAL);
			setCell(row++, column, cell);
			
			cell = new Cell(this, row, column, ROW_METHOD_TOTAL_PRE_SCHOOL_OPERATION, 
					columnMethod, rowParameter, columnParameter, Cell.CELLTYPE_NORMAL);
			setCell(row++, column, cell);
			
			cell = new Cell(this, row, column, ROW_METHOD_SUM_HOURS, 
					columnMethod, new Integer(SCHOOL_TYPE_AFTER_SCHOOL), columnParameter, Cell.CELLTYPE_NORMAL);
			setCell(row++, column, cell);

			cell = new Cell(this, row, column, ROW_METHOD_SUM_HOURS, 
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
		int column = cell.getColumn();
		
		switch (cell.getRowMethod()) {
			case ROW_METHOD_SUM_HOURS:
				int schoolType = ((Integer) cell.getRowParameter()).intValue();
				switch (cell.getColumnMethod()) {
					case COLUMN_METHOD_HOURS_1_15:
						value = getChildCareHoursPlacementCount(1, 15, schoolType);
						break;
					case COLUMN_METHOD_HOURS_16_24:
						value = getChildCareHoursPlacementCount(16, 24, schoolType);
						break;
					case COLUMN_METHOD_HOURS_25_29:
						value = getChildCareHoursPlacementCount(25, 29, schoolType);
						break;
					case COLUMN_METHOD_HOURS_30_34:
						value = getChildCareHoursPlacementCount(30, 34, schoolType);
						break;
					case COLUMN_METHOD_HOURS_35_39:
						value = getChildCareHoursPlacementCount(35, 39, schoolType);
						break;
					case COLUMN_METHOD_HOURS_40_:
						value = getChildCareHoursPlacementCount(40, 168, schoolType);
						break;
				}
				break;
			case ROW_METHOD_TOTAL_PRE_SCHOOL_OPERATION:
				value = getCell(0, column).getFloatValue() + getCell(1, column).getFloatValue();
				break;
			case ROW_METHOD_TOTAL_AFTER_SCHOOL_OPERATION:
				value = getCell(3, column).getFloatValue() + getCell(4, column).getFloatValue();
				break;
			case ROW_METHOD_TOTAL:
				value = getCell(2, column).getFloatValue() + getCell(5, column).getFloatValue();
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
	 * Returns the number of total child placements for the specified hours per week interval.
	 */
	protected int getChildCareHoursPlacementCount(int fromHours, int toHours, int schoolType) 
			throws RemoteException {
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
		query = getQuery(QUERY_HOUR_INTERVAL_PLACEMENTS);
		if (query == null) {
			query = new PreparedQuery(getConnection());
			query.setSelectCount();
			query.setChildCarePlacements();
			query.setChildCareWeekHours(); // parameter 1-2
			query.setFourSchoolTypes(); // parameter 3-6
			query.prepare();
			setQuery(QUERY_HOUR_INTERVAL_PLACEMENTS, query);
		}
		query.setInt(1, fromHours);
		query.setInt(2, toHours);
		
		query.setInt(3, schoolType1);
		query.setInt(4, schoolType2);
		query.setInt(5, schoolType3);
		query.setInt(6, schoolType4);
		
		return query.execute();
	}
}
