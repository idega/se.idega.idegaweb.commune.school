/*
 * $Id: NackaProviderSummaryReportModel.java,v 1.12 2004/05/06 07:24:47 anders Exp $
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
 * Report model that shows a summary of Nacka providers (child care and schools).
 * <p>
 * Last modified: $Date: 2004/05/06 07:24:47 $ by $Author: anders $
 *
 * @author Anders Lindman
 * @version $Revision: 1.12 $
 */
public class NackaProviderSummaryReportModel extends ReportModel {

	private final static int ROW_SIZE = 18;
	private final static int COLUMN_SIZE = 3;
	
	private final static int ROW_METHOD_PROVIDER_TYPE_COMMUNE = 1;
	private final static int ROW_METHOD_PROVIDER_TYPE_PRIVATE = 2;

	private final static int COLUMN_METHOD_PROVIDER_COUNT = 101;
	private final static int COLUMN_METHOD_PLACEMENTS = 102;
	private final static int COLUMN_METHOD_PLACEMENTS_OTHER_COMMUNES = 103;

	private final static String QUERY_PROVIDER_COUNT = "provider_count";
	private final static String QUERY_CHILD_CARE_PLACEMENT_COUNT = "cc_placement_count";
	private final static String QUERY_CHILD_CARE_PLACEMENT_COUNT_OTHER_COMMUNES = "cc_placement_count_oc";
	private final static String QUERY_SCHOOL_PLACEMENT_COUNT = "sch_placement_count";
	private final static String QUERY_SCHOOL_PLACEMENT_COUNT_OTHER_COMMUNES = "sch_placement_count_oc";
	private final static String QUERY_CHILD_CARE_PLACEMENT_COUNT_PRIVATE = "p_cc_placement_count";
	private final static String QUERY_CHILD_CARE_PLACEMENT_COUNT_OTHER_COMMUNES_PRIVATE = "p_cc_placement_count_oc";
	private final static String QUERY_SCHOOL_PLACEMENT_COUNT_PRIVATE = "p_sch_placement_count";
	private final static String QUERY_SCHOOL_PLACEMENT_COUNT_OTHER_COMMUNES_PRIVATE = "p_sch_placement_count_oc";

	private final static int COMMUNE = 1;
	private final static int PRIVATE = 2;
	
	private final static int SCHOOL_TYPE_PRE_SCHOOL = 1;
	private final static int SCHOOL_TYPE_FAMILY_DAYCARE = 2;
	private final static int SCHOOL_TYPE_FAMILY_DAYCARE_PRE_SCHOOL_OPERATION = 3;
	private final static int SCHOOL_TYPE_FAMILY_DAYCARE_AFTER_SCHOOL_OPERATION = 4;
	private final static int SCHOOL_TYPE_AFTER_SCHOOL = 5;
	private final static int SCHOOL_TYPE_ELEMENTARY_SCHOOL = 6;
	private final static int SCHOOL_TYPE_ELEMENTARY_SCHOOL_ONLY = 7;
	private final static int SCHOOL_TYPE_ELEMENTARY_SCHOOL_PRES_SCHOOL_CLASS = 8;
	private final static int SCHOOL_TYPE_HIGH_SCHOOL = 9;
	
	private final static String KEY_REPORT_TITLE = KP + "title_nacka_provider_summary";
	
	/**
	 * Constructs this report model.
	 * @param reportBusiness the report business instance for calculating cell values
	 */
	public NackaProviderSummaryReportModel(ReportBusiness reportBusiness) {
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
		Header[] headers = new Header[2];
		
		Header h = new Header(KEY_COMMUNE_PROVIDERS, Header.HEADERTYPE_ROW_HEADER, 9);
		h.setChild(0, new Header(KEY_PRE_SCHOOLS, Header.HEADERTYPE_ROW_NORMAL));
		h.setChild(1, new Header(KEY_FAMILY_DAYCARE_CENTERS, Header.HEADERTYPE_ROW_NORMAL));
		h.setChild(2, new Header(KEY_WITHIN_PRE_SCHOOL_OPERATION, Header.HEADERTYPE_ROW_NORMAL));
		h.setChild(3, new Header(KEY_WITHIN_AFTER_SCHOOL_OPERATION, Header.HEADERTYPE_ROW_NORMAL));
		h.setChild(4, new Header(KEY_AFTER_SCHOOL_CENTERS_6_9, Header.HEADERTYPE_ROW_NORMAL));
		h.setChild(5, new Header(KEY_ELEMENTARY_SCHOOLS, Header.HEADERTYPE_ROW_NORMAL));
		h.setChild(6, new Header(KEY_WITHIN_ELEMENTARY_SCHOOL, Header.HEADERTYPE_ROW_NORMAL));
		h.setChild(7, new Header(KEY_WITHIN_PRE_SCHOOL_CLASS, Header.HEADERTYPE_ROW_NORMAL));
		h.setChild(8, new Header(KEY_HIGH_SCHOOLS, Header.HEADERTYPE_ROW_NORMAL));
		headers[0] = h;

		h = new Header(KEY_PRIVATE_FREESTANDING_PROVIDERS, Header.HEADERTYPE_ROW_HEADER, 9);
		h.setChild(0, new Header(KEY_PRE_SCHOOLS, Header.HEADERTYPE_ROW_NORMAL));
		h.setChild(1, new Header(KEY_FAMILY_DAYCARE_CENTERS, Header.HEADERTYPE_ROW_NORMAL));
		h.setChild(2, new Header(KEY_WITHIN_PRE_SCHOOL_OPERATION, Header.HEADERTYPE_ROW_NORMAL));
		h.setChild(3, new Header(KEY_WITHIN_AFTER_SCHOOL_OPERATION, Header.HEADERTYPE_ROW_NORMAL));
		h.setChild(4, new Header(KEY_AFTER_SCHOOL_CENTERS_6_9, Header.HEADERTYPE_ROW_NORMAL));
		h.setChild(5, new Header(KEY_ELEMENTARY_SCHOOLS, Header.HEADERTYPE_ROW_NORMAL));
		h.setChild(6, new Header(KEY_WITHIN_ELEMENTARY_SCHOOL, Header.HEADERTYPE_ROW_NORMAL));
		h.setChild(7, new Header(KEY_WITHIN_PRE_SCHOOL_CLASS, Header.HEADERTYPE_ROW_NORMAL));
		h.setChild(8, new Header(KEY_HIGH_SCHOOLS, Header.HEADERTYPE_ROW_NORMAL));
		headers[1] = h;

		return headers;
	}
	
	/**
	 * @see se.idega.idegaweb.commune.school.report.business.ReportModel#buildColumnHeaders()
	 */
	protected Header[] buildColumnHeaders() {
		Header[] headers = new Header[3];
		
		headers[0] = new Header(KEY_NUMBER_OF_PROVIDERS, Header.HEADERTYPE_COLUMN_HEADER);
		headers[1] = new Header(KEY_CHILDREN_STUDENTS, Header.HEADERTYPE_COLUMN_HEADER);
		headers[2] = new Header(KEY_OF_WHICH_FROM_OTHER_COMMUNES, Header.HEADERTYPE_COLUMN_HEADER);
		
		return headers;
	}
	
	/**
	 * @see se.idega.idegaweb.commune.school.report.business.ReportModel#buildCells()
	 */
	protected void buildCells() {
		for (int row = 0; row < getRowSize(); row++) {

			boolean isPrivate = false;
			int rowMethod = ROW_METHOD_PROVIDER_TYPE_COMMUNE;
			if (row / 9 > 0) {
				rowMethod = ROW_METHOD_PROVIDER_TYPE_PRIVATE;
				isPrivate = true;
			}
			
			Object rowParameter = null;
			Object columnParameter = null;
			Cell cell = null;
			
			switch (row % 9) {
				case 0:
					rowParameter = new Integer(SCHOOL_TYPE_PRE_SCHOOL);
					
					cell = new Cell(this, row, 0, rowMethod, COLUMN_METHOD_PROVIDER_COUNT,
							rowParameter, columnParameter, Cell.CELLTYPE_NORMAL);
					setCell(row, 0, cell);
					
					cell = new Cell(this, row, 1, rowMethod, COLUMN_METHOD_PLACEMENTS,
							rowParameter, columnParameter, Cell.CELLTYPE_NORMAL);
					setCell(row, 1, cell);
						
					cell = new Cell(this, row, 2, rowMethod, COLUMN_METHOD_PLACEMENTS_OTHER_COMMUNES,
							rowParameter, columnParameter, Cell.CELLTYPE_NORMAL);
					if (isPrivate) {
						cell = new Cell(this, row, 2, rowMethod, COLUMN_METHOD_PLACEMENTS_OTHER_COMMUNES,
								"&nbsp;", columnParameter, Cell.CELLTYPE_ROW_HEADER);						
					}
					setCell(row, 2, cell);					
					break;
				case 1:
					rowParameter = new Integer(SCHOOL_TYPE_FAMILY_DAYCARE);
					
					cell = new Cell(this, row, 0, rowMethod, COLUMN_METHOD_PROVIDER_COUNT,
							rowParameter, columnParameter, Cell.CELLTYPE_NORMAL);
					setCell(row, 0, cell);
						
					cell = new Cell(this, row, 1, rowMethod, COLUMN_METHOD_PLACEMENTS,
							"&nbsp;", columnParameter, Cell.CELLTYPE_ROW_HEADER);
					setCell(row, 1, cell);
							
					cell = new Cell(this, row, 2, rowMethod, COLUMN_METHOD_PLACEMENTS_OTHER_COMMUNES,
							"&nbsp;", columnParameter, Cell.CELLTYPE_ROW_HEADER);
					if (isPrivate) {
						cell = new Cell(this, row, 2, rowMethod, COLUMN_METHOD_PLACEMENTS_OTHER_COMMUNES,
								"&nbsp;", columnParameter, Cell.CELLTYPE_ROW_HEADER);						
					}
					setCell(row, 2, cell);
					break;
				case 2:
					rowParameter = new Integer(SCHOOL_TYPE_FAMILY_DAYCARE_PRE_SCHOOL_OPERATION);
					
					cell = new Cell(this, row, 0, rowMethod, COLUMN_METHOD_PROVIDER_COUNT,
							"&nbsp;", columnParameter, Cell.CELLTYPE_ROW_HEADER);
					setCell(row, 0, cell);
							
					cell = new Cell(this, row, 1, rowMethod, COLUMN_METHOD_PLACEMENTS,
							rowParameter, columnParameter, Cell.CELLTYPE_NORMAL);
					setCell(row, 1, cell);
								
					cell = new Cell(this, row, 2, rowMethod, COLUMN_METHOD_PLACEMENTS_OTHER_COMMUNES,
							rowParameter, columnParameter, Cell.CELLTYPE_NORMAL);
					if (isPrivate) {
						cell = new Cell(this, row, 2, rowMethod, COLUMN_METHOD_PLACEMENTS_OTHER_COMMUNES,
								"&nbsp;", columnParameter, Cell.CELLTYPE_ROW_HEADER);						
					}
					setCell(row, 2, cell);
					break;
				case 3:
					rowParameter = new Integer(SCHOOL_TYPE_FAMILY_DAYCARE_AFTER_SCHOOL_OPERATION);
					
					cell = new Cell(this, row, 0, rowMethod, COLUMN_METHOD_PROVIDER_COUNT,
							"&nbsp;", columnParameter, Cell.CELLTYPE_ROW_HEADER);
					setCell(row, 0, cell);
								
					cell = new Cell(this, row, 1, rowMethod, COLUMN_METHOD_PLACEMENTS,
							rowParameter, columnParameter, Cell.CELLTYPE_NORMAL);
					setCell(row, 1, cell);
									
					cell = new Cell(this, row, 2, rowMethod, COLUMN_METHOD_PLACEMENTS_OTHER_COMMUNES,
							rowParameter, columnParameter, Cell.CELLTYPE_NORMAL);
					if (isPrivate) {
						cell = new Cell(this, row, 2, rowMethod, COLUMN_METHOD_PLACEMENTS_OTHER_COMMUNES,
								"&nbsp;", columnParameter, Cell.CELLTYPE_ROW_HEADER);						
					}
					setCell(row, 2, cell);
					break;
				case 4:
					rowParameter = new Integer(SCHOOL_TYPE_AFTER_SCHOOL);
					
					cell = new Cell(this, row, 0, rowMethod, COLUMN_METHOD_PROVIDER_COUNT,
							rowParameter, columnParameter, Cell.CELLTYPE_NORMAL);
					setCell(row, 0, cell);
						
					cell = new Cell(this, row, 1, rowMethod, COLUMN_METHOD_PLACEMENTS,
							rowParameter, columnParameter, Cell.CELLTYPE_NORMAL);
					setCell(row, 1, cell);
							
					cell = new Cell(this, row, 2, rowMethod, COLUMN_METHOD_PLACEMENTS_OTHER_COMMUNES,
							rowParameter, columnParameter, Cell.CELLTYPE_NORMAL);
					if (isPrivate) {
						cell = new Cell(this, row, 2, rowMethod, COLUMN_METHOD_PLACEMENTS_OTHER_COMMUNES,
								"&nbsp;", columnParameter, Cell.CELLTYPE_ROW_HEADER);						
					}
					setCell(row, 2, cell);					
					break;
				case 5:
					rowParameter = new Integer(SCHOOL_TYPE_ELEMENTARY_SCHOOL);
					
					cell = new Cell(this, row, 0, rowMethod, COLUMN_METHOD_PROVIDER_COUNT,
							rowParameter, columnParameter, Cell.CELLTYPE_NORMAL);
					setCell(row, 0, cell);
							
					cell = new Cell(this, row, 1, rowMethod, COLUMN_METHOD_PLACEMENTS,
							"&nbsp;", columnParameter, Cell.CELLTYPE_ROW_HEADER);
					setCell(row, 1, cell);
								
					cell = new Cell(this, row, 2, rowMethod, COLUMN_METHOD_PLACEMENTS_OTHER_COMMUNES,
							"&nbsp;", columnParameter, Cell.CELLTYPE_ROW_HEADER);
					if (isPrivate) {
						cell = new Cell(this, row, 2, rowMethod, COLUMN_METHOD_PLACEMENTS_OTHER_COMMUNES,
								"&nbsp;", columnParameter, Cell.CELLTYPE_ROW_HEADER);						
					}
					setCell(row, 2, cell);
					break;
				case 6:
					rowParameter = new Integer(SCHOOL_TYPE_ELEMENTARY_SCHOOL_ONLY);
					
					cell = new Cell(this, row, 0, rowMethod, COLUMN_METHOD_PROVIDER_COUNT,
							"&nbsp;", columnParameter, Cell.CELLTYPE_ROW_HEADER);
					setCell(row, 0, cell);
								
					cell = new Cell(this, row, 1, rowMethod, COLUMN_METHOD_PLACEMENTS,
							rowParameter, columnParameter, Cell.CELLTYPE_NORMAL);
					setCell(row, 1, cell);
									
					cell = new Cell(this, row, 2, rowMethod, COLUMN_METHOD_PLACEMENTS_OTHER_COMMUNES,
							rowParameter, columnParameter, Cell.CELLTYPE_NORMAL);
					if (isPrivate) {
						cell = new Cell(this, row, 2, rowMethod, COLUMN_METHOD_PLACEMENTS_OTHER_COMMUNES,
								"&nbsp;", columnParameter, Cell.CELLTYPE_ROW_HEADER);						
					}
					setCell(row, 2, cell);
					break;
				case 7:
					rowParameter = new Integer(SCHOOL_TYPE_ELEMENTARY_SCHOOL_PRES_SCHOOL_CLASS);
					
					cell = new Cell(this, row, 0, rowMethod, COLUMN_METHOD_PROVIDER_COUNT,
							"&nbsp;", columnParameter, Cell.CELLTYPE_ROW_HEADER);
					setCell(row, 0, cell);
								
					cell = new Cell(this, row, 1, rowMethod, COLUMN_METHOD_PLACEMENTS,
							rowParameter, columnParameter, Cell.CELLTYPE_NORMAL);
					setCell(row, 1, cell);
									
					cell = new Cell(this, row, 2, rowMethod, COLUMN_METHOD_PLACEMENTS_OTHER_COMMUNES,
							rowParameter, columnParameter, Cell.CELLTYPE_NORMAL);
					if (isPrivate) {
						cell = new Cell(this, row, 2, rowMethod, COLUMN_METHOD_PLACEMENTS_OTHER_COMMUNES,
								"&nbsp;", columnParameter, Cell.CELLTYPE_ROW_HEADER);						
					}
					setCell(row, 2, cell);
					break;
				case 8:
					rowParameter = new Integer(SCHOOL_TYPE_HIGH_SCHOOL);
					
					cell = new Cell(this, row, 0, rowMethod, COLUMN_METHOD_PROVIDER_COUNT,
							rowParameter, columnParameter, Cell.CELLTYPE_NORMAL);
					setCell(row, 0, cell);
						
					cell = new Cell(this, row, 1, rowMethod, COLUMN_METHOD_PLACEMENTS,
							rowParameter, columnParameter, Cell.CELLTYPE_NORMAL);
					setCell(row, 1, cell);
							
					cell = new Cell(this, row, 2, rowMethod, COLUMN_METHOD_PLACEMENTS_OTHER_COMMUNES,
							rowParameter, columnParameter, Cell.CELLTYPE_NORMAL);
					if (isPrivate) {
						cell = new Cell(this, row, 2, rowMethod, COLUMN_METHOD_PLACEMENTS_OTHER_COMMUNES,
								"&nbsp;", columnParameter, Cell.CELLTYPE_ROW_HEADER);						
					}
					setCell(row, 2, cell);					
					break;
			}			
		}
	}
	
	/**
	 * @see se.idega.idegaweb.commune.school.report.business.ReportModel#calculate()
	 */
	protected float calculate(Cell cell) throws RemoteException {
		float value = 0f;
		
		if (cell.getCellType() == Cell.CELLTYPE_ROW_HEADER) {
			return value;
		}

		int managementType = COMMUNE;
		if (cell.getRowMethod() == ROW_METHOD_PROVIDER_TYPE_PRIVATE) {
			managementType = PRIVATE;
		}
		
		int schoolType = 0;
		Integer rowParameter = (Integer) cell.getRowParameter();
		if (rowParameter != null) {
			schoolType = rowParameter.intValue();
		}
		
		switch (cell.getColumnMethod()) {
			case COLUMN_METHOD_PROVIDER_COUNT:
				value = getProviderCount(managementType, schoolType);
				break;
			case COLUMN_METHOD_PLACEMENTS:
				value = getPlacementCount(managementType, schoolType, false);
				break;
			case COLUMN_METHOD_PLACEMENTS_OTHER_COMMUNES:
				value = getPlacementCount(managementType, schoolType, true);
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
	 * Returns the number of providers for the specified management and school type.
	 */
	protected int getProviderCount(int managementType, int schoolType) throws RemoteException {		
		ReportBusiness rb = getReportBusiness();
		
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
				schoolType3 = rb.getFamilyAfterSchool6TypeId();
				schoolType4 = rb.getFamilyAfterSchool7_9TypeId();
				break;
			case SCHOOL_TYPE_FAMILY_DAYCARE_PRE_SCHOOL_OPERATION:
				schoolType1 = rb.getFamilyDayCareSchoolTypeId();
				schoolType2 = rb.getGeneralFamilyDaycareSchoolTypeId();
				schoolType3 = schoolType2;
				schoolType4 = schoolType2;
				break;
			case SCHOOL_TYPE_FAMILY_DAYCARE_AFTER_SCHOOL_OPERATION:
				schoolType1 = rb.getFamilyAfterSchool6TypeId();
				schoolType2 = rb.getFamilyAfterSchool7_9TypeId();
				schoolType3 = schoolType2;
				schoolType4 = schoolType2;
				break;
			case SCHOOL_TYPE_AFTER_SCHOOL:
				schoolType1 = rb.getAfterSchool6TypeId();
				schoolType2 = rb.getAfterSchool7_9TypeId();
				schoolType3 = schoolType2;
				schoolType4 = schoolType2;
				break;
			case SCHOOL_TYPE_ELEMENTARY_SCHOOL:
				schoolType1 = rb.getElementarySchoolTypeId();
				schoolType2 = rb.getPreSchoolClassTypeId();
				schoolType3 = schoolType2;
				schoolType4 = schoolType2;
				break;
			case SCHOOL_TYPE_ELEMENTARY_SCHOOL_ONLY:
				schoolType1 = rb.getElementarySchoolTypeId();
				schoolType2 = schoolType1;
				schoolType3 = schoolType1;
				schoolType4 = schoolType1;
				break;
			case SCHOOL_TYPE_ELEMENTARY_SCHOOL_PRES_SCHOOL_CLASS:
				schoolType1 = rb.getPreSchoolClassTypeId();
				schoolType2 = schoolType1;
				schoolType3 = schoolType1;
				schoolType4 = schoolType1;
				break;
			case SCHOOL_TYPE_HIGH_SCHOOL:
				schoolType1 = rb.getHighSchoolTypeId();
				schoolType2 = schoolType1;
				schoolType3 = schoolType1;
				schoolType4 = schoolType1;
				break;				
		}
		
		PreparedQuery query = null;
		query = getQuery(QUERY_PROVIDER_COUNT);
		if (query == null) {
			query = new PreparedQuery(getConnection());
			query.setSelectCountSubQuery();
			query.setSelectDistinctSchools();
			query.setOnlyNackaSchools();
			query.setFourSchoolTypesForProvidersWithoutPlacements(); // parameter 1-4
			query.setFourManagementTypes(); // parameter 5-8
			query.setNotForeignSchools();
			query.prepare();
			setQuery(QUERY_PROVIDER_COUNT, query);
		}
		query.setInt(1, schoolType1);
		query.setInt(2, schoolType2);
		query.setInt(3, schoolType3);
		query.setInt(4, schoolType4);
		
		query.setString(5, managementType1);
		query.setString(6, managementType2);
		query.setString(7, managementType3);
		query.setString(8, managementType4);
		
		int nrOfProviders = query.execute();
		if (schoolType == SCHOOL_TYPE_HIGH_SCHOOL) {
			// Special case subtract additional 'Nacka Gymnasium'
			nrOfProviders--; 
		}
		return nrOfProviders;
	}
	
	/**
	 * Returns the number of child/student placements for the specifiedmanagement and school type.
	 */
	protected int getPlacementCount(int managementType, int schoolType, boolean isOtherCommunes) throws RemoteException {
		ReportBusiness rb = getReportBusiness();
		
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
		
		int schoolType1 = 0;
		int schoolType2 = 0;
		int schoolType3 = 0;
		int schoolType4 = 0;

		boolean isChildCare = false;
		
		switch (schoolType) {
			case SCHOOL_TYPE_PRE_SCHOOL:
				schoolType1 = rb.getPreSchoolTypeId();
				schoolType2 = rb.getGeneralPreSchoolTypeId();
				schoolType3 = schoolType2;
				schoolType4 = schoolType2;
				isChildCare = true;
				break;
			case SCHOOL_TYPE_FAMILY_DAYCARE:
				schoolType1 = rb.getFamilyDayCareSchoolTypeId();
				schoolType2 = rb.getGeneralFamilyDaycareSchoolTypeId();
				schoolType3 = rb.getFamilyAfterSchool6TypeId();
				schoolType4 = rb.getFamilyAfterSchool7_9TypeId();
				isChildCare = true;
				break;
			case SCHOOL_TYPE_FAMILY_DAYCARE_PRE_SCHOOL_OPERATION:
				schoolType1 = rb.getFamilyDayCareSchoolTypeId();
				schoolType2 = rb.getGeneralFamilyDaycareSchoolTypeId();
				schoolType3 = schoolType2;
				schoolType4 = schoolType2;
				isChildCare = true;
				break;
			case SCHOOL_TYPE_FAMILY_DAYCARE_AFTER_SCHOOL_OPERATION:
				schoolType1 = rb.getFamilyAfterSchool6TypeId();
				schoolType2 = rb.getFamilyAfterSchool7_9TypeId();
				schoolType3 = schoolType2;
				schoolType4 = schoolType2;
				isChildCare = true;
				break;
			case SCHOOL_TYPE_AFTER_SCHOOL:
				schoolType1 = rb.getAfterSchool6TypeId();
				schoolType2 = rb.getAfterSchool7_9TypeId();
				schoolType3 = schoolType2;
				schoolType4 = schoolType2;
				isChildCare = true;
				break;
			case SCHOOL_TYPE_ELEMENTARY_SCHOOL:
				schoolType1 = rb.getElementarySchoolTypeId();
				schoolType2 = rb.getPreSchoolClassTypeId();
				schoolType3 = schoolType2;
				schoolType4 = schoolType2;
				break;
			case SCHOOL_TYPE_ELEMENTARY_SCHOOL_ONLY:
				schoolType1 = rb.getElementarySchoolTypeId();
				schoolType2 = schoolType1;
				schoolType3 = schoolType1;
				schoolType4 = schoolType1;
				break;
			case SCHOOL_TYPE_ELEMENTARY_SCHOOL_PRES_SCHOOL_CLASS:
				schoolType1 = rb.getPreSchoolClassTypeId();
				schoolType2 = schoolType1;
				schoolType3 = schoolType1;
				schoolType4 = schoolType1;
				break;
			case SCHOOL_TYPE_HIGH_SCHOOL:
				schoolType1 = rb.getHighSchoolTypeId();
				schoolType2 = schoolType1;
				schoolType3 = schoolType1;
				schoolType4 = schoolType1;
				break;				
		}
		
		PreparedQuery query = null;
		if (managementType == COMMUNE) {
			query = getQuery(isChildCare, isOtherCommunes);
		} else {
			query = getQueryPrivate(isChildCare, isOtherCommunes);			
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
	
	/*
	 * Returns a prepared query for the specified parameters.
	 */
	private PreparedQuery getQuery(boolean isChildCare, boolean isOtherCommunes) throws RemoteException {
		PreparedQuery query = null;
		ReportBusiness rb = getReportBusiness();
		
		if (!isChildCare && !isOtherCommunes) {
			query = getQuery(QUERY_SCHOOL_PLACEMENT_COUNT);
			if (query == null) {
				query = new PreparedQuery(getConnection());
				query.setSelectCountDistinctUsers();
				query.setPlacements(rb.getSchoolSeasonId());
				query.setOnlyNackaSchools();
				query.setFourSchoolTypesForProviders(); // parameter 1-4
				query.setFourManagementTypes(); // parameter 5-8
				query.setNotForeignSchools();
				query.prepare();
				setQuery(QUERY_SCHOOL_PLACEMENT_COUNT, query);
			}		
		} else if (!isChildCare && isOtherCommunes) {
			query = getQuery(QUERY_SCHOOL_PLACEMENT_COUNT_OTHER_COMMUNES);
			if (query == null) {
				query = new PreparedQuery(getConnection());
				query.setSelectCountDistinctUsers();
				query.setPlacements(rb.getSchoolSeasonId());
				query.setOnlyNackaSchools();
				query.setNotNackaCitizens();
				query.setFourSchoolTypesForProviders(); // parameter 1-4
				query.setFourManagementTypes(); // parameter 5-8
				query.setNotForeignSchools();
				query.prepare();
				setQuery(QUERY_SCHOOL_PLACEMENT_COUNT_OTHER_COMMUNES, query);
			}					
		} else if (isChildCare && !isOtherCommunes) {
			query = getQuery(QUERY_CHILD_CARE_PLACEMENT_COUNT);
			if (query == null) {
				query = new PreparedQuery(getConnection());
				query.setSelectCountDistinctUsers();
				query.setChildCarePlacements();
				query.setOnlyNackaSchools();
				query.setFourSchoolTypesForProviders(); // parameter 1-4
				query.setFourManagementTypes(); // parameter 5-8
				query.setNotForeignSchools();
				query.prepare();
				setQuery(QUERY_CHILD_CARE_PLACEMENT_COUNT, query);
			}		
		} else if (isChildCare && isOtherCommunes) {
			query = getQuery(QUERY_CHILD_CARE_PLACEMENT_COUNT_OTHER_COMMUNES);
			if (query == null) {
				query = new PreparedQuery(getConnection());
				query.setSelectCountDistinctUsers();
				query.setChildCarePlacements();
				query.setOnlyNackaSchools();
				query.setNotNackaCitizens();
				query.setFourSchoolTypesForProviders(); // parameter 1-4
				query.setFourManagementTypes(); // parameter 5-8
				query.setNotForeignSchools();
				query.prepare();
				setQuery(QUERY_CHILD_CARE_PLACEMENT_COUNT_OTHER_COMMUNES, query);
			}								
		}
		
		return query;
	}
	
	/*
	 * Returns a prepared query for the specified parameters for private providers.
	 */
	private PreparedQuery getQueryPrivate(boolean isChildCare, boolean isOtherCommunes) throws RemoteException {
		PreparedQuery query = null;
		ReportBusiness rb = getReportBusiness();
		
		if (!isChildCare && !isOtherCommunes) {
			query = getQuery(QUERY_SCHOOL_PLACEMENT_COUNT_PRIVATE);
			if (query == null) {
				query = new PreparedQuery(getConnection());
				query.setSelectCountDistinctUsers();
				query.setPlacements(rb.getSchoolSeasonId());
				query.setOnlyNackaCitizens();
				query.setOnlyNackaSchools();
				query.setFourSchoolTypesForProviders(); // parameter 1-4
				query.setFourManagementTypes(); // parameter 5-8
				query.setNotForeignSchools();
				query.prepare();
				setQuery(QUERY_SCHOOL_PLACEMENT_COUNT_PRIVATE, query);
			}		
		} else if (!isChildCare && isOtherCommunes) {
			query = getQuery(QUERY_SCHOOL_PLACEMENT_COUNT_OTHER_COMMUNES_PRIVATE);
			if (query == null) {
				query = new PreparedQuery(getConnection());
				query.setSelectCountDistinctUsers();
				query.setPlacements(rb.getSchoolSeasonId());
				query.setOnlyNackaSchools();
				query.setNotNackaCitizens();
				query.setFourSchoolTypesForProviders(); // parameter 1-4
				query.setFourManagementTypes(); // parameter 5-8
				query.setNotForeignSchools();
				query.prepare();
				setQuery(QUERY_SCHOOL_PLACEMENT_COUNT_OTHER_COMMUNES_PRIVATE, query);
			}					
		} else if (isChildCare && !isOtherCommunes) {
			query = getQuery(QUERY_CHILD_CARE_PLACEMENT_COUNT_PRIVATE);
			if (query == null) {
				query = new PreparedQuery(getConnection());
				query.setSelectCountDistinctUsers();
				query.setChildCarePlacements();
				query.setOnlyNackaSchools();
				query.setOnlyNackaCitizens();
				query.setFourSchoolTypesForProviders(); // parameter 1-4
				query.setFourManagementTypes(); // parameter 5-8
				query.setNotForeignSchools();
				query.prepare();
				setQuery(QUERY_CHILD_CARE_PLACEMENT_COUNT_PRIVATE, query);
			}		
		} else if (isChildCare && isOtherCommunes) {
			query = getQuery(QUERY_CHILD_CARE_PLACEMENT_COUNT_OTHER_COMMUNES_PRIVATE);
			if (query == null) {
				query = new PreparedQuery(getConnection());
				query.setSelectCountDistinctUsers();
				query.setChildCarePlacements();
				query.setOnlyNackaSchools();
				query.setNotNackaCitizens();
				query.setFourSchoolTypesForProviders(); // parameter 1-4
				query.setFourManagementTypes(); // parameter 5-8
				query.setNotForeignSchools();
				query.prepare();
				setQuery(QUERY_CHILD_CARE_PLACEMENT_COUNT_OTHER_COMMUNES_PRIVATE, query);
			}								
		}
		
		return query;
	}
}
