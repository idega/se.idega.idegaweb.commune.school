/*
 * $Id: NackaCC15HoursPlacementReportModel.java,v 1.1 2004/01/27 16:05:47 anders Exp $
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
 * Last modified: $Date: 2004/01/27 16:05:47 $ by $Author: anders $
 *
 * @author Anders Lindman
 * @version $Revision: 1.1 $
 */
public class NackaCC15HoursPlacementReportModel extends ReportModel {

	private final static int ROW_SIZE = 3;
	private final static int COLUMN_SIZE = 1;
	
	private final static int ROW_METHOD_PARENTAL_LEAVE = 1;
	private final static int ROW_METHOD_JOB_CANDIDATE = 2;
	private final static int ROW_METHOD_GENERAL_PRE_SCHOOL = 3;

	private final static int COLUMN_METHOD_COUNT = 101;
	
	private final static int PARENTAL_LEAVE = 3;
	private final static int JOB_CANDIDATE = 2;

	private final static String QUERY_WORK_SITUATION = "work_situation";
	private final static String QUERY_GENERAL_PRE_SCHOOL = "general_pre_school";
	
	private final static String KEY_REPORT_TITLE = KP + "title_nacka_child_care_15_hours_placements";
	
	/**
	 * Constructs this report model.
	 * @param reportBusiness the report business instance for calculating cell values
	 */
	public NackaCC15HoursPlacementReportModel(ReportBusiness reportBusiness) {
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
		
		headers[0] = new Header(KEY_PARENTAL_LEAVE, Header.HEADERTYPE_ROW_HEADER);
		headers[1] = new Header(KEY_JOB_CANDIDATE, Header.HEADERTYPE_ROW_HEADER);
		headers[2] = new Header(KEY_GENERAL_PRE_SCHOOL, Header.HEADERTYPE_ROW_HEADER);
				
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
			
			Cell cell = new Cell(this, row, column, ROW_METHOD_PARENTAL_LEAVE, 
			columnMethod, rowParameter, columnParameter, Cell.CELLTYPE_NORMAL);
			setCell(row++, column, cell);
			
			cell = new Cell(this, row, column, ROW_METHOD_JOB_CANDIDATE, 
			columnMethod, rowParameter, columnParameter, Cell.CELLTYPE_NORMAL);
			setCell(row++, column, cell);
			
			cell = new Cell(this, row, column, ROW_METHOD_GENERAL_PRE_SCHOOL, 
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
					case ROW_METHOD_PARENTAL_LEAVE:
						value = getChildCarePlacementCount(PARENTAL_LEAVE);
						break;
					case ROW_METHOD_JOB_CANDIDATE:
						value = getChildCarePlacementCount(JOB_CANDIDATE);
						break;
					case ROW_METHOD_GENERAL_PRE_SCHOOL:
						value = getGeneralPreSchoolPlacementCount();
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
	 * Returns the number of child placements for the specified work situation.
	 */
	protected int getChildCarePlacementCount(int workSituation) {
		PreparedQuery query = null;
		query = getQuery(QUERY_WORK_SITUATION);
		if (query == null) {
			query = new PreparedQuery(getConnection());
			query.setSelectCount();
			query.setChildCarePlacements();
			query.setWorkSituation(); // parameter 1
			query.prepare();
			setQuery(QUERY_WORK_SITUATION, query);
		}
		query.setInt(1, workSituation);
		
		return query.execute();
	}
	
	/**
	 * Returns the number of child placements for general pre schools.
	 */
	protected int getGeneralPreSchoolPlacementCount() {
		PreparedQuery query = null;
		query = getQuery(QUERY_GENERAL_PRE_SCHOOL);
		if (query == null) {
			query = new PreparedQuery(getConnection());
			query.setSelectCount();
			query.setChildCarePlacements();
			query.setSchoolTypeGeneralPreSchool();
			query.prepare();
			setQuery(QUERY_GENERAL_PRE_SCHOOL, query);
		}
		
		return query.execute();
	}
}
