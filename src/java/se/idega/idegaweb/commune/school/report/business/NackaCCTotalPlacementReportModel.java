/*
 * $Id: NackaCCTotalPlacementReportModel.java,v 1.1 2004/01/27 09:48:40 anders Exp $
 *
 * Copyright (C) 2003 Agura IT. All Rights Reserved.
 *
 * This software is the proprietary information of Agura IT AB.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.school.report.business;

/** 
 * Report model for total number of child care placements in Nacka.
 * <p>
 * Last modified: $Date: 2004/01/27 09:48:40 $ by $Author: anders $
 *
 * @author Anders Lindman
 * @version $Revision: 1.1 $
 */
public class NackaCCTotalPlacementReportModel extends ReportModel {

	private final static int ROW_SIZE = 1;
	private final static int COLUMN_SIZE = 1;
	
	private final static int ROW_METHOD_ALL_PLACEMENTS = 1;

	private final static int COLUMN_METHOD_COUNT = 101;

	private final static String QUERY_TOTAL_PLACEMENTS = "total_placements";
	
	private final static String KEY_REPORT_TITLE = KP + "title_nacka_child_care_total_placements";
	
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
		Header[] headers = new Header[1];
		
		headers[0] = new Header(KEY_NACKA_COMMUNE, Header.HEADERTYPE_ROW_HEADER);
				
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
			Cell cell = new Cell(this, row, column, ROW_METHOD_ALL_PLACEMENTS, 
			columnMethod, rowParameter, columnParameter, Cell.CELLTYPE_NORMAL);
			setCell(row++, column, cell);
		}
	}
	
	/**
	 * @see se.idega.idegaweb.commune.school.report.business.ReportModel#calculate()
	 */
	protected float calculate(Cell cell) {
		float value = 0f;

		switch (cell.getColumnMethod()) {
			case COLUMN_METHOD_COUNT:
				switch (cell.getRowMethod()) {
					case ROW_METHOD_ALL_PLACEMENTS:
						value = getChildCareTotalPlacementCount();
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
	protected int getChildCareTotalPlacementCount() {				
		PreparedQuery query = null;
		query = getQuery(QUERY_TOTAL_PLACEMENTS);
		if (query == null) {
			query = new PreparedQuery(getConnection());
			query.setSelectCount();
			query.setChildCarePlacements();
			query.prepare();
			setQuery(QUERY_TOTAL_PLACEMENTS, query);
		}
		
		return query.execute();
	}
}
