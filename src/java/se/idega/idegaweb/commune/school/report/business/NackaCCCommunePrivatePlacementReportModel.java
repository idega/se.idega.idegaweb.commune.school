/*
 * $Id: NackaCCCommunePrivatePlacementReportModel.java,v 1.1 2004/01/21 10:35:54 anders Exp $
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
 * Report model for child care placements in Nacka for commune and private providers.
 * <p>
 * Last modified: $Date: 2004/01/21 10:35:54 $ by $Author: anders $
 *
 * @author Anders Lindman
 * @version $Revision: 1.1 $
 */
public class NackaCCCommunePrivatePlacementReportModel extends ReportModel {

	private final static int ROW_SIZE = 8;
	private final static int COLUMN_SIZE = 2;
	
	private final static int ROW_METHOD_COMMUNE_PRE_SCHOOL_OPERATION = 1;
	private final static int ROW_METHOD_PRIVATE_COOPERATIVE_PRE_SCHOOL_OPERATION = 2;
	private final static int ROW_METHOD_COMMUNE_SCHOOL_CHILDREN_CARE_6 = 3;
	private final static int ROW_METHOD_PRIVATE_COOPERATIVE_SCHOOL_CHILDREN_CARE_6 = 4;
	private final static int ROW_METHOD_COMMUNE_SCHOOL_CHILDREN_CARE_7_9 = 5;
	private final static int ROW_METHOD_PRIVATE_SCHOOL_CHILDREN_CARE_7_9 = 6;
	private final static int ROW_METHOD_COMMUNE_SUM = 7;
	private final static int ROW_METHOD_PRIVATE_SUM = 8;

	private final static int COLUMN_METHOD_COUNT = 101;
	private final static int COLUMN_METHOD_SHARE = 102;

	private final static String QUERY_CHILD_CARE = "child_care";
	
	private final static int PRE_SCHOOL = 1;
	private final static int SCHOOL_CHILDREN_CARE_6 = 2;
	private final static int SCHOOL_CHILDREN_CARE_7_9 = 3;

	private final static int COMMUNE = 1;
	private final static int PRIVATE = 2;
	
	private final static String KEY_REPORT_TITLE = KP + "title_nacka_child_care_commune_private_placements";

	/**
	 * Constructs this report model.
	 * @param reportBusiness the report business instance for calculating cell values
	 */
	public NackaCCCommunePrivatePlacementReportModel(ReportBusiness reportBusiness) {
		super(reportBusiness);
		setReportSize(ROW_SIZE, COLUMN_SIZE);
	}
	
	/**
	 * @see se.idega.idegaweb.commune.school.report.business.ReportModel#buildRowHeaders()
	 */
	protected Header[] buildRowHeaders() {
		
		Header[] headers = new Header[7];

		headers[0] = new Header(KEY_COMMUNE_PRE_SCHOOL_OPERATION, Header.HEADERTYPE_ROW_HEADER);
		headers[1] = new Header(KEY_PRIVATE_COOPERATIVE_PRE_SCHOOL_OPERATION, Header.HEADERTYPE_ROW_HEADER);
		headers[2] = new Header(KEY_COMMUNE_SCHOOL_CHILDREN_CARE_6, Header.HEADERTYPE_ROW_HEADER);
		headers[3] = new Header(KEY_PRIVATE_COOPERATIVE_SCHOOL_CHILDREN_CARE_6, Header.HEADERTYPE_ROW_HEADER);
		headers[4] = new Header(KEY_COMMUNE_SCHOOL_CHILDREN_CARE_7_9, Header.HEADERTYPE_ROW_HEADER);
		headers[5] = new Header(KEY_PRIVATE_SCHOOL_CHILDREN_CARE_7_9, Header.HEADERTYPE_ROW_HEADER);
		
		Header h = new Header(KEY_TOTAL, Header.HEADERTYPE_ROW_HEADER, 2);
		Header child0 = new Header(KEY_COMMUNE_SUM, Header.HEADERTYPE_ROW_NORMAL);
		Header child1 = new Header(KEY_PRIVATE_SUM, Header.HEADERTYPE_ROW_NORMAL);
		h.setChild(0, child0);
		h.setChild(1, child1);
		headers[6] = h;
		
		return headers;
	}
	
	/**
	 * @see se.idega.idegaweb.commune.school.report.business.ReportModel#buildColumnHeaders()
	 */
	protected Header[] buildColumnHeaders() {
		Header[] headers = new Header[2];
		
		headers[0] = new Header(KEY_NUMBER_OF_CHILDREN, Header.HEADERTYPE_COLUMN_HEADER);
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

			Cell cell = new Cell(this, row, column, ROW_METHOD_COMMUNE_PRE_SCHOOL_OPERATION,
					columnMethod, rowParameter, columnParameter, Cell.CELLTYPE_NORMAL);
			setCell(row++, column, cell);
			
			cell = new Cell(this, row, column, ROW_METHOD_PRIVATE_COOPERATIVE_PRE_SCHOOL_OPERATION,
					columnMethod, rowParameter, columnParameter, Cell.CELLTYPE_NORMAL);
			setCell(row++, column, cell);

			cell = new Cell(this, row, column, ROW_METHOD_COMMUNE_SCHOOL_CHILDREN_CARE_6,
					columnMethod, rowParameter, columnParameter, Cell.CELLTYPE_NORMAL);
			setCell(row++, column, cell);

			cell = new Cell(this, row, column, ROW_METHOD_PRIVATE_COOPERATIVE_SCHOOL_CHILDREN_CARE_6,
					columnMethod, rowParameter, columnParameter, Cell.CELLTYPE_NORMAL);
			setCell(row++, column, cell);

			cell = new Cell(this, row, column, ROW_METHOD_COMMUNE_SCHOOL_CHILDREN_CARE_7_9,
					columnMethod, rowParameter, columnParameter, Cell.CELLTYPE_NORMAL);
			setCell(row++, column, cell);

			cell = new Cell(this, row, column, ROW_METHOD_PRIVATE_SCHOOL_CHILDREN_CARE_7_9,
					columnMethod, rowParameter, columnParameter, Cell.CELLTYPE_NORMAL);
			setCell(row++, column, cell);

			cell = new Cell(this, row, column, ROW_METHOD_COMMUNE_SUM,
					columnMethod, rowParameter, columnParameter, Cell.CELLTYPE_NORMAL);
			setCell(row++, column, cell);

			cell = new Cell(this, row, column, ROW_METHOD_PRIVATE_SUM,
					columnMethod, rowParameter, columnParameter, Cell.CELLTYPE_NORMAL);
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
					case ROW_METHOD_COMMUNE_PRE_SCHOOL_OPERATION:
						value = getChildCarePlacementCount(PRE_SCHOOL, COMMUNE);
						break;
					case ROW_METHOD_PRIVATE_COOPERATIVE_PRE_SCHOOL_OPERATION:
						value = getChildCarePlacementCount(PRE_SCHOOL, PRIVATE);
						break;
					case ROW_METHOD_COMMUNE_SCHOOL_CHILDREN_CARE_6:
						value = getChildCarePlacementCount(SCHOOL_CHILDREN_CARE_6, COMMUNE);
						break;
					case ROW_METHOD_PRIVATE_COOPERATIVE_SCHOOL_CHILDREN_CARE_6:
						value = getChildCarePlacementCount(SCHOOL_CHILDREN_CARE_6, PRIVATE);
						break;
					case ROW_METHOD_COMMUNE_SCHOOL_CHILDREN_CARE_7_9:
						value = getChildCarePlacementCount(SCHOOL_CHILDREN_CARE_7_9, COMMUNE);
						break;
					case ROW_METHOD_PRIVATE_SCHOOL_CHILDREN_CARE_7_9:
						value = getChildCarePlacementCount(SCHOOL_CHILDREN_CARE_7_9, PRIVATE);
						break;
					case ROW_METHOD_COMMUNE_SUM:
						value = getCell(0, column).getFloatValue() + 
								getCell(2, column).getFloatValue() +
								getCell(4, column).getFloatValue();
						break;
					case ROW_METHOD_PRIVATE_SUM:
						value = getCell(1, column).getFloatValue() + 
								getCell(3, column).getFloatValue() +
								getCell(5, column).getFloatValue();
						break;
				}
				break;
			case COLUMN_METHOD_SHARE:
				switch (cell.getRowMethod()) {
					case ROW_METHOD_COMMUNE_PRE_SCHOOL_OPERATION:
						float total = getCell(0, 0).getFloatValue() + getCell(0, 1).getFloatValue();
						if (total > 0) {
							value = getCell(0, row).getFloatValue() / total;
						}
						break;
					case ROW_METHOD_PRIVATE_COOPERATIVE_PRE_SCHOOL_OPERATION:
						total = getCell(0, 0).getFloatValue() + getCell(0, 1).getFloatValue();
						if (total > 0) {
							value = getCell(0, row).getFloatValue() / total;
						}
						break;
					case ROW_METHOD_COMMUNE_SCHOOL_CHILDREN_CARE_6:
						total = getCell(0, 2).getFloatValue() + getCell(0, 3).getFloatValue();
						if (total > 0) {
							value = getCell(0, row).getFloatValue() / total;
						}
						break;
					case ROW_METHOD_PRIVATE_COOPERATIVE_SCHOOL_CHILDREN_CARE_6:
						total = getCell(0, 2).getFloatValue() + getCell(0, 3).getFloatValue();
						if (total > 0) {
							value = getCell(0, row).getFloatValue() / total;
						}
						break;
					case ROW_METHOD_COMMUNE_SCHOOL_CHILDREN_CARE_7_9:
						total = getCell(0, 4).getFloatValue() + getCell(0, 5).getFloatValue();
						if (total > 0) {
							value = getCell(0, row).getFloatValue() / total;
						}
						break;
					case ROW_METHOD_PRIVATE_SCHOOL_CHILDREN_CARE_7_9:
						total = getCell(0, 4).getFloatValue() + getCell(0, 5).getFloatValue();
						if (total > 0) {
							value = getCell(0, row).getFloatValue() / total;
						}
						break;
					case ROW_METHOD_COMMUNE_SUM:
						total = getCell(0, 6).getFloatValue() + getCell(0, 7).getFloatValue();
						if (total > 0) {
							value = getCell(0, row).getFloatValue() / total;
						}
						break;
					case ROW_METHOD_PRIVATE_SUM:
						total = getCell(0, 6).getFloatValue() + getCell(0, 7).getFloatValue();
						if (total > 0) {
							value = getCell(0, row).getFloatValue() / total;
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
	 * Returns the number of student placements for the specified high school and school year.
	 */
	protected int getChildCarePlacementCount(int schoolType, int managementType) throws RemoteException {
		ReportBusiness rb = getReportBusiness();
		
		int schoolType1 = 0;
		int schoolType2 = 0;
		int schoolType3 = 0;
		int schoolType4 = 0;
		
		switch (schoolType) {
			case PRE_SCHOOL:
				schoolType1 = rb.getPreSchoolTypeId(); 
				schoolType2 = rb.getGeneralPreSchoolTypeId();
				schoolType3 = schoolType1;
				schoolType4 = schoolType2;
				break;
			case SCHOOL_CHILDREN_CARE_6:
				schoolType1 = rb.getAfterSchool6TypeId();
				schoolType2 = rb.getFamilyAfterSchool6TypeId();
				schoolType3 = schoolType1;
				schoolType4 = schoolType2;
				break;
			case SCHOOL_CHILDREN_CARE_7_9:
				schoolType1 = rb.getAfterSchool7_9TypeId();
				schoolType2 = rb.getFamilyAfterSchool7_9TypeId();
				schoolType3 = schoolType1;
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
			query.setChildCarePlacements();
			query.setFourSchoolTypes(); // parameter 1-4
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
