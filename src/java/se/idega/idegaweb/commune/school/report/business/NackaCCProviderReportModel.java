/*
 * $Id: NackaCCProviderReportModel.java,v 1.2 2004/01/22 08:49:49 anders Exp $
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
 * Report model for child care commune and private providers.
 * <p>
 * Last modified: $Date: 2004/01/22 08:49:49 $ by $Author: anders $
 *
 * @author Anders Lindman
 * @version $Revision: 1.2 $
 */
public class NackaCCProviderReportModel extends ReportModel {

	private final static int ROW_SIZE = 7;
	private final static int COLUMN_SIZE = 2;
	
	private final static int ROW_METHOD_COMMUNE_PRE_SCHOOLS = 1;
	private final static int ROW_METHOD_COMMUNE_AFTER_SCHOOLS = 2;
	private final static int ROW_METHOD_COMMUNE_FAMILY_DAYCARE = 3;
	private final static int ROW_METHOD_PRIVATE_PRE_SCHOOLS = 4;
	private final static int ROW_METHOD_PRIVATE_AFTER_SCHOOLS = 5;
	private final static int ROW_METHOD_PRIVATE_FAMILY_DAYCARE = 6;
	private final static int ROW_METHOD_TOTAL = 7;

	private final static int COLUMN_METHOD_COUNT = 101;
	private final static int COLUMN_METHOD_SHARE = 102;

	private final static String QUERY_CHILD_CARE = "child_care";
	
	private final static int PRE_SCHOOL = 1;
	private final static int AFTER_SCHOOL = 2;
	private final static int FAMILY_DAYCARE = 3;

	private final static int COMMUNE = 1;
	private final static int PRIVATE = 2;
	
	private final static String KEY_REPORT_TITLE = KP + "title_nacka_child_care_providers";

	/**
	 * Constructs this report model.
	 * @param reportBusiness the report business instance for calculating cell values
	 */
	public NackaCCProviderReportModel(ReportBusiness reportBusiness) {
		super(reportBusiness);
		setReportSize(ROW_SIZE, COLUMN_SIZE);
	}
	
	/**
	 * @see se.idega.idegaweb.commune.school.report.business.ReportModel#buildRowHeaders()
	 */
	protected Header[] buildRowHeaders() {
		
		Header[] headers = new Header[7];

		headers[0] = new Header(KEY_COMMUNE_PRE_SCHOOL_PROVIDERS, Header.HEADERTYPE_ROW_HEADER);
		headers[1] = new Header(KEY_COMMUNE_AFTER_SCHOOL_PROVIDERS, Header.HEADERTYPE_ROW_HEADER);
		headers[2] = new Header(KEY_COMMUNE_FAMILY_DAYCARE_PROVIDERS, Header.HEADERTYPE_ROW_HEADER);
		headers[3] = new Header(KEY_PRIVATE_PRE_SCHOOL_PROVIDERS, Header.HEADERTYPE_ROW_HEADER);
		headers[4] = new Header(KEY_PRIVATE_AFTER_SCHOOL_PROVIDERS, Header.HEADERTYPE_ROW_HEADER);
		headers[5] = new Header(KEY_PRIVATE_FAMILY_DAYCARE_PROVIDERS, Header.HEADERTYPE_ROW_HEADER);
		
		Header h = new Header(KEY_TOTAL, Header.HEADERTYPE_ROW_HEADER, 1);
		Header child0 = new Header(KEY_PROVIDER_TOTAL, Header.HEADERTYPE_ROW_NORMAL);
		h.setChild(0, child0);
		headers[6] = h;
		
		return headers;
	}
	
	/**
	 * @see se.idega.idegaweb.commune.school.report.business.ReportModel#buildColumnHeaders()
	 */
	protected Header[] buildColumnHeaders() {
		Header[] headers = new Header[2];
		
		headers[0] = new Header(KEY_NUMBER_OF_PROVIDERS, Header.HEADERTYPE_COLUMN_HEADER);
		headers[1] = new Header(KEY_SHARE_IN_PERCENT, Header.HEADERTYPE_COLUMN_HEADER);
		
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
					columnMethod = COLUMN_METHOD_COUNT;
					break;
				case 1:
					columnMethod = COLUMN_METHOD_SHARE;
					break;
			}

			Object rowParameter = null;
			int cellType = Cell.CELLTYPE_NORMAL;
			if (column == 1) {
				cellType = Cell.CELLTYPE_PERCENT;
			}

			Cell cell = new Cell(this, row, column, ROW_METHOD_COMMUNE_PRE_SCHOOLS,
					columnMethod, rowParameter, columnParameter, cellType);
			setCell(row++, column, cell);
			
			cell = new Cell(this, row, column, ROW_METHOD_COMMUNE_AFTER_SCHOOLS,
					columnMethod, rowParameter, columnParameter, cellType);
			setCell(row++, column, cell);

			cell = new Cell(this, row, column, ROW_METHOD_COMMUNE_FAMILY_DAYCARE,
					columnMethod, rowParameter, columnParameter, cellType);
			setCell(row++, column, cell);

			cell = new Cell(this, row, column, ROW_METHOD_PRIVATE_PRE_SCHOOLS,
					columnMethod, rowParameter, columnParameter, cellType);
			setCell(row++, column, cell);

			cell = new Cell(this, row, column, ROW_METHOD_PRIVATE_AFTER_SCHOOLS,
					columnMethod, rowParameter, columnParameter, cellType);
			setCell(row++, column, cell);

			cell = new Cell(this, row, column, ROW_METHOD_PRIVATE_FAMILY_DAYCARE,
					columnMethod, rowParameter, columnParameter, cellType);
			setCell(row++, column, cell);

			cell = new Cell(this, row, column, ROW_METHOD_TOTAL,
					columnMethod, rowParameter, columnParameter, cellType);
			setCell(row++, column, cell);
		}
	}
	
	/**
	 * @see se.idega.idegaweb.commune.school.report.business.ReportModel#calculate()
	 */
	protected float calculate(Cell cell) throws RemoteException {
		float value = 0f;
		int row = cell.getRow();
		int column = cell.getColumn();

		switch (cell.getColumnMethod()) {
			case COLUMN_METHOD_COUNT:
				switch (cell.getRowMethod()) {
					case ROW_METHOD_COMMUNE_PRE_SCHOOLS:
						value = getChildCareProviderCount(PRE_SCHOOL, COMMUNE);
						break;
					case ROW_METHOD_COMMUNE_AFTER_SCHOOLS:
						value = getChildCareProviderCount(AFTER_SCHOOL, COMMUNE);
						break;
					case ROW_METHOD_COMMUNE_FAMILY_DAYCARE:
						value = getChildCareProviderCount(FAMILY_DAYCARE, COMMUNE);
						break;
					case ROW_METHOD_PRIVATE_PRE_SCHOOLS:
						value = getChildCareProviderCount(PRE_SCHOOL, PRIVATE);
						break;
					case ROW_METHOD_PRIVATE_AFTER_SCHOOLS:
						value = getChildCareProviderCount(AFTER_SCHOOL, PRIVATE);
						break;
					case ROW_METHOD_PRIVATE_FAMILY_DAYCARE:
						value = getChildCareProviderCount(FAMILY_DAYCARE, PRIVATE);
						break;
					case ROW_METHOD_TOTAL:
						for (int i = 0; i < row; i++) {
							value += getCell(i, column).getFloatValue();
						}
						break;
				}
				break;
			case COLUMN_METHOD_SHARE:
				switch (cell.getRowMethod()) {
					case ROW_METHOD_COMMUNE_PRE_SCHOOLS:
						float total = getCell(0, 0).getFloatValue() + getCell(1, 0).getFloatValue();
						if (total > 0) {
							value = 100 * getCell(row, 0).getFloatValue() / total;
						}
						break;
					case ROW_METHOD_COMMUNE_AFTER_SCHOOLS:
						total = getCell(0, 0).getFloatValue() + getCell(1, 0).getFloatValue();
						if (total > 0) {
							value = 100 * getCell(row, 0).getFloatValue() / total;
						}
						break;
					case ROW_METHOD_COMMUNE_FAMILY_DAYCARE:
						total = getCell(2, 0).getFloatValue() + getCell(3, 0).getFloatValue();
						if (total > 0) {
							value = 100 * getCell(row, 0).getFloatValue() / total;
						}
						break;
					case ROW_METHOD_PRIVATE_PRE_SCHOOLS:
						total = getCell(2, 0).getFloatValue() + getCell(3, 0).getFloatValue();
						if (total > 0) {
							value = 100 * getCell(row, 0).getFloatValue() / total;
						}
						break;
					case ROW_METHOD_PRIVATE_AFTER_SCHOOLS:
						total = getCell(4, 0).getFloatValue() + getCell(5, 0).getFloatValue();
						if (total > 0) {
							value = 100 * getCell(row, 0).getFloatValue() / total;
						}
						break;
					case ROW_METHOD_PRIVATE_FAMILY_DAYCARE:
						total = getCell(4, 0).getFloatValue() + getCell(5, 0).getFloatValue();
						if (total > 0) {
							value = 100 * getCell(row, 0).getFloatValue() / total;
						}
						break;
					case ROW_METHOD_TOTAL:
						total = 100.0f;
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
	 * Returns the number of providers for the specified school type and management type.
	 */
	protected int getChildCareProviderCount(int schoolType, int managementType) throws RemoteException {
		ReportBusiness rb = getReportBusiness();
		
		int schoolType1 = 0;
		int schoolType2 = 0;
		int schoolType3 = 0;
		int schoolType4 = 0;
		
		switch (schoolType) {
			case PRE_SCHOOL:
				schoolType1 = rb.getPreSchoolTypeId(); 
				schoolType2 = rb.getGeneralPreSchoolTypeId();
				schoolType3 = schoolType2;
				schoolType4 = schoolType2;
				break;
			case AFTER_SCHOOL:
				schoolType1 = rb.getAfterSchool6TypeId();
				schoolType2 = rb.getFamilyAfterSchool6TypeId();
				schoolType3 = rb.getAfterSchool7_9TypeId();
				schoolType4 = rb.getFamilyAfterSchool7_9TypeId();
				break;
			case FAMILY_DAYCARE:
				schoolType1 = rb.getFamilyDayCareSchoolTypeId();
				schoolType2 = rb.getGeneralFamilyDaycareSchoolTypeId();
				schoolType3 = schoolType2;
				schoolType4 = schoolType2;
				break;
		}
		
		String managementType1 = null;
		String managementType2 = null;
		String managementType3 = null;
		String managementType4 = null;
		
		switch (managementType) {
			case COMMUNE:
				managementType1 = "COMMUNE";
				managementType2 = managementType1;
				managementType3 = managementType1;
				managementType4 = managementType1;
				break;
			case PRIVATE:
				managementType1 = "COMPANY";
				managementType2 = "FOUNDATION";
				managementType3 = "OTHER";
				managementType4 = "COOPERATIVE_COMMUNE_LIABILITY";
				break;
		}
		
		PreparedQuery query = null;
		query = getQuery(QUERY_CHILD_CARE);
		if (query == null) {
			query = new PreparedQuery(getConnection());
			query.setSelectCount();
			query.setFourSchoolTypesForProviders(); // parameter 1-4
			query.setFourManagementTypes(); // parameter 5-8
			query.prepare();
			setQuery(QUERY_CHILD_CARE, query);
		}
		query.setInt(1, schoolType1);
		query.setInt(2, schoolType2);
		query.setInt(3, schoolType3);
		query.setInt(4, schoolType4);
		
		query.setString(5, managementType1);
		query.setString(6, managementType2);
		query.setString(7, managementType3);
		query.setString(8, managementType4);
		
		return query.execute();
	}
}
