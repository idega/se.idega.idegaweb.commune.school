/*
 * $Id: NackaCCHourIntervalReportModel.java,v 1.1 2004/01/28 09:43:12 anders Exp $
 *
 * Copyright (C) 2003 Agura IT. All Rights Reserved.
 *
 * This software is the proprietary information of Agura IT AB.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.school.report.business;

/** 
 * Report model for total number of child care placements per child care hour interval.
 * <p>
 * Last modified: $Date: 2004/01/28 09:43:12 $ by $Author: anders $
 *
 * @author Anders Lindman
 * @version $Revision: 1.1 $
 */
public class NackaCCHourIntervalReportModel extends ReportModel {

	private final static int ROW_SIZE = 1;
	private final static int COLUMN_SIZE = 4;
	
	private final static int ROW_METHOD_SUM_PLACEMENTS = 1;

	private final static int COLUMN_METHOD_HOURS_1_15 = 101;
	private final static int COLUMN_METHOD_HOURS_16_25 = 102;
	private final static int COLUMN_METHOD_HOURS_26_35 = 103;
	private final static int COLUMN_METHOD_HOURS_36_ = 104;

	private final static String QUERY_HOUR_INTERVAL_PLACEMENTS = "hour_interval_placements";
	
	private final static String KEY_REPORT_TITLE = KP + "title_nacka_child_care_hour_interval_placements";
	
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
		Header[] headers = new Header[1];
		
		headers[0] = new Header(KEY_CHILDREN_TOTAL, Header.HEADERTYPE_ROW_HEADER);
				
		return headers;
	}
	
	/**
	 * @see se.idega.idegaweb.commune.school.report.business.ReportModel#buildColumnHeaders()
	 */
	protected Header[] buildColumnHeaders() {
		Header[] headers = new Header[1];
		
		Header h = new Header(KEY_CHILD_CARE_HOURS, Header.HEADERTYPE_COLUMN_HEADER, 4);
		Header child0 = new Header("1-15", Header.HEADERTYPE_COLUMN_NONLOCALIZED_HEADER);
		Header child1 = new Header("16-25", Header.HEADERTYPE_COLUMN_NONLOCALIZED_HEADER);
		Header child2 = new Header("26-35", Header.HEADERTYPE_COLUMN_NONLOCALIZED_HEADER);
		Header child3 = new Header(">=36", Header.HEADERTYPE_COLUMN_NONLOCALIZED_HEADER);
		h.setChild(0, child0);
		h.setChild(1, child1);
		h.setChild(2, child2);
		h.setChild(3, child3);
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
					columnMethod = COLUMN_METHOD_HOURS_16_25;
					break;
				case 2:
					columnMethod = COLUMN_METHOD_HOURS_26_35;
					break;
				case 3:
					columnMethod = COLUMN_METHOD_HOURS_36_;
					break;
			}

			Object columnParameter = null;	
			Object rowParameter = null;
			Cell cell = new Cell(this, row, column, ROW_METHOD_SUM_PLACEMENTS, 
					columnMethod, rowParameter, columnParameter, Cell.CELLTYPE_NORMAL);
			setCell(row, column, cell);
		}
	}
	
	/**
	 * @see se.idega.idegaweb.commune.school.report.business.ReportModel#calculate()
	 */
	protected float calculate(Cell cell) {
		float value = 0f;

		switch (cell.getColumnMethod()) {
			case COLUMN_METHOD_HOURS_1_15:
				value = getChildCareHoursPlacementCount(1, 15);
				break;
			case COLUMN_METHOD_HOURS_16_25:
				value = getChildCareHoursPlacementCount(16, 25);
				break;
			case COLUMN_METHOD_HOURS_26_35:
				value = getChildCareHoursPlacementCount(26, 35);
				break;
			case COLUMN_METHOD_HOURS_36_:
				value = getChildCareHoursPlacementCount(36, 168);
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
	protected int getChildCareHoursPlacementCount(int fromHours, int toHours) {
		PreparedQuery query = null;
		query = getQuery(QUERY_HOUR_INTERVAL_PLACEMENTS);
		if (query == null) {
			query = new PreparedQuery(getConnection());
			query.setSelectCount();
			query.setChildCarePlacements();
			query.setChildCareWeekHours(); // parameter 1-2
			query.prepare();
			setQuery(QUERY_HOUR_INTERVAL_PLACEMENTS, query);
		}
		query.setInt(1, fromHours);
		query.setInt(2, toHours);
		
		return query.execute();
	}
}
