/*
 * $Id: NackaCCProviderCareTimeReportModel.java,v 1.3 2005/01/20 12:47:12 anders Exp $
 *
 * Copyright (C) 2003 Agura IT. All Rights Reserved.
 *
 * This software is the proprietary information of Agura IT AB.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.school.report.business;

import java.util.Collection;
import java.util.Iterator;

import com.idega.block.school.data.School;

/** 
 * Report model for total number of child care hours in Nacka.
 * <p>
 * Last modified: $Date: 2005/01/20 12:47:12 $ by $Author: anders $
 *
 * @author Anders Lindman
 * @version $Revision: 1.3 $
 */
public class NackaCCProviderCareTimeReportModel extends ReportModel {

	private final static int COLUMN_SIZE = 1;
	
	private final static int ROW_METHOD_PROVIDER = 1;
	private final static int ROW_METHOD_TOTAL = 2;

	private final static int COLUMN_METHOD_SUM_HOURS = 101;

	private final static String QUERY_SUM_HOURS = "sum_hours";
	private final static String QUERY_SUM_HOURS2 = "sum_hours2";
	
	private final static String KEY_REPORT_TITLE = KP + "title_nacka_child_care_provider_time";
	
	/**
	 * Constructs this report model.
	 * @param reportBusiness the report business instance for calculating cell values
	 */
	public NackaCCProviderCareTimeReportModel(ReportBusiness reportBusiness) {
		super(reportBusiness);
	}
	
	/**
	 * @see se.idega.idegaweb.commune.school.report.business.ReportModel#initReportSize()
	 */
	protected void initReportSize() {
		ReportBusiness rb = getReportBusiness();
		Collection providers = null;
		try {
			providers = rb.getChildCareProviders();
			setReportSize(providers.size() + 1, COLUMN_SIZE);
		} catch (Exception e) {}
	}
	
	/**
	 * @see se.idega.idegaweb.commune.school.report.business.ReportModel#buildRowHeaders()
	 */
	protected Header[] buildRowHeaders() {
		Header[] headers = null;

		ReportBusiness rb = getReportBusiness();
		try {
			Collection providers = rb.getChildCareProviders();
			headers = new Header[providers.size() + 1];
			int headerIndex = 0;
			Iterator iter = providers.iterator();			
			while (iter.hasNext()) {
				School school = (School) iter.next();
				String providerName = school.getName(); 
				headers[headerIndex] = new Header(providerName, Header.HEADERTYPE_ROW_NONLOCALIZED_HEADER);
				headerIndex++;
			}
			headers[headerIndex] = new Header(KEY_TOTAL, Header.HEADERTYPE_ROW_HEADER);
		} catch (Exception e) {}

		return headers;
	}
	
	/**
	 * @see se.idega.idegaweb.commune.school.report.business.ReportModel#buildColumnHeaders()
	 */
	protected Header[] buildColumnHeaders() {
		Header[] headers = new Header[1];
		
		headers[0] = new Header(KEY_SUM_CHILD_CARE_HOURS, Header.HEADERTYPE_COLUMN_HEADER);
		
		return headers;
	}
	
	/**
	 * @see se.idega.idegaweb.commune.school.report.business.ReportModel#buildCells()
	 */
	protected void buildCells() {
		for (int column = 0; column < getColumnSize(); column++) {
			int row = 0;
			int columnMethod = COLUMN_METHOD_SUM_HOURS;
			Object columnParameter = null;	
			Object rowParameter = null;
			
			try {
				Collection providers = getReportBusiness().getChildCareProviders();
				Iterator iter = providers.iterator();
				while (iter.hasNext()) {
					School school = (School) iter.next();
					rowParameter = school.getPrimaryKey();
					Cell cell = new Cell(this, row, column, ROW_METHOD_PROVIDER, 
							columnMethod, rowParameter, columnParameter, Cell.CELLTYPE_NORMAL);
					setCell(row++, column, cell);
				}
			} catch (Exception e) {}
			
			Cell cell = new Cell(this, row, column, ROW_METHOD_TOTAL,
					columnMethod, null, columnParameter, Cell.CELLTYPE_NORMAL);
			setCell(row++, column, cell);
		}
	}
	
	/**
	 * @see se.idega.idegaweb.commune.school.report.business.ReportModel#calculate()
	 */
	protected float calculate(Cell cell) {
		float value = 0f;

		int row = cell.getRow();
		
		int schoolId = -1;
		Integer rowParameter = (Integer) cell.getRowParameter();
		if (rowParameter != null) {
			schoolId = rowParameter.intValue(); 
		}
		
		switch (cell.getColumnMethod()) {
			case COLUMN_METHOD_SUM_HOURS:
				switch (cell.getRowMethod()) {
					case ROW_METHOD_PROVIDER:
						value = getProviderCareTime(schoolId);
						break;
					case ROW_METHOD_TOTAL:
						for (int i = 0; i < row; i++) {
							value += getCell(i, 0).getFloatValue();
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
	
	/**
	 * Returns the number of total child care week hours for the specified school.
	 */
	protected float getProviderCareTime(int schoolId) {				
		PreparedQuery query = null;
		PreparedQuery query2 = null;
		query = getQuery(QUERY_SUM_HOURS);
		query2 = getQuery(QUERY_SUM_HOURS2);
		if (query == null) {
			query = new PreparedQuery(getConnection());
			query.setSelectSumChildCareWeekHoursCareTime();
			query.setChildCarePlacementsCareTime();
			query.setSchool(); // parameter 1
			query.prepare();
			setQuery(QUERY_SUM_HOURS, query);

			query2 = new PreparedQuery(getConnection());
			query2.setSelectSumChildCareWeekHoursCareTimeString();
			query2.setChildCarePlacementsCareTimeString();
			query2.setSchool(); // parameter 1
			query2.prepare();
			setQuery(QUERY_SUM_HOURS2, query2);
		}
		query.setInt(1, schoolId);
		query2.setInt(1, schoolId);
		
		return query.executeFloat() + query2.executeFloat();
	}
}
