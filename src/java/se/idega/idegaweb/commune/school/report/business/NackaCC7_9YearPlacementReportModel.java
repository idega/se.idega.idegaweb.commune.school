/*
 * $Id: NackaCC7_9YearPlacementReportModel.java,v 1.6 2005/01/20 12:47:12 anders Exp $
 *
 * Copyright (C) 2003 Agura IT. All Rights Reserved.
 *
 * This software is the proprietary information of Agura IT AB.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.school.report.business;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolArea;

/** 
 * Report model for child care after school and daycare placements 
 * for 7-9 years children in Nacka.
 * <p>
 * Last modified: $Date: 2005/01/20 12:47:12 $ by $Author: anders $
 *
 * @author Anders Lindman
 * @version $Revision: 1.6 $
 */
public class NackaCC7_9YearPlacementReportModel extends ReportModel {

	private final static int COLUMN_SIZE = 2;
	
	private final static int ROW_METHOD_FAMILY_DAYCARE_COMMUNE = 1;
	private final static int ROW_METHOD_PROVIDER_COMMUNE = 2;
	private final static int ROW_METHOD_FAMILY_DAYCARE_PRIVATE = 3;
	private final static int ROW_METHOD_PROVIDER_PRIVATE = 4;
	private final static int ROW_METHOD_SUM = 5;

	private final static int COLUMN_METHOD_COUNT = 101;
	private final static int COLUMN_METHOD_MEAN_HOURS = 102;

	private final static String QUERY_FAMILY_DAYCARE_PLACEMENTS = "family_daycare_placements";
	private final static String QUERY_PROVIDER_PLACEMENTS = "provider_placements";
	private final static String QUERY_FAMILY_DAYCARE_MEAN_HOURS = "family_daycare_mean_hours";
	private final static String QUERY_PROVIDER_MEAN_HOURS = "provider_mean_hours";
	private final static String QUERY_FAMILY_DAYCARE_MEAN_HOURS2 = "family_daycare_mean_hours2";
	private final static String QUERY_PROVIDER_MEAN_HOURS2 = "provider_mean_hours2";
	
	private final static int COMMUNE = 1;
	private final static int PRIVATE = 2;
	
	private final static String KEY_REPORT_TITLE = KP + "title_nacka_child_care_7_9_year_placements";

	private Map _communeSchoolsByArea = null;
	private Map _privateSchoolsByArea = null;
	
	/**
	 * Constructs this report model.
	 * @param reportBusiness the report business instance for calculating cell values
	 */
	public NackaCC7_9YearPlacementReportModel(ReportBusiness reportBusiness) {
		super(reportBusiness);
		_communeSchoolsByArea = new TreeMap();
		_privateSchoolsByArea = new TreeMap();
	}
	
	/**
	 * @see se.idega.idegaweb.commune.school.report.business.ReportModel#initReportSize()
	 */
	protected void initReportSize() {
		int rowSize = 0;
		try {
			ReportBusiness rb = getReportBusiness();
			Collection schoolTypes = new ArrayList();
			schoolTypes.add(new Integer(rb.getAfterSchool7_9TypeId()));
			
			Collection communeManagementTypes = new ArrayList();
			communeManagementTypes.add("COMMUNE");
			
			Collection privateManagementTypes = new ArrayList();
			privateManagementTypes.add("COMPANY");
			privateManagementTypes.add("FOUNDATION");
			privateManagementTypes.add("OTHER");
			privateManagementTypes.add("COOPERATIVE_COMMUNE_LIABILITY");

			Collection areas = rb.getAfterSchool7_9Areas();
			Iterator iter = areas.iterator();
			while (iter.hasNext()) {
				SchoolArea area = (SchoolArea) iter.next();
				String areaName = area.getName();
				int areaId = ((Integer) area.getPrimaryKey()).intValue();
				
				Collection communeSchools = rb.getCommuneSchools(areaId, schoolTypes, communeManagementTypes);
				_communeSchoolsByArea.put(areaName, communeSchools);
				rowSize += communeSchools.size();
				
				Collection privateSchools = rb.getCommuneSchools(areaId, schoolTypes, privateManagementTypes);
				_privateSchoolsByArea.put(areaName, privateSchools);
				rowSize += privateSchools.size();
				
				rowSize += 4; // Sum + family daycare * 2
			}
			setReportSize(rowSize, COLUMN_SIZE);
		} catch (RemoteException e) {}
	}
	
	/**
	 * @see se.idega.idegaweb.commune.school.report.business.ReportModel#buildRowHeaders()
	 */
	protected Header[] buildRowHeaders() {
		Header[] headers = null;
		try {
			ReportBusiness rb = getReportBusiness();
			Collection areas = rb.getAfterSchool7_9Areas();
			headers = new Header[areas.size()];
			int headerIndex = 0;
			Iterator areaIter = areas.iterator();
			while (areaIter.hasNext()) {
				SchoolArea area = (SchoolArea) areaIter.next();
				String areaName = area.getName();
				Collection communeSchools = (Collection) _communeSchoolsByArea.get(areaName);
				Collection privateSchools = (Collection) _privateSchoolsByArea.get(areaName);
				int nrOfRows = communeSchools.size() + privateSchools.size() + 7;
				Header areaHeader = new Header(areaName, Header.HEADERTYPE_ROW_NONLOCALIZED_HEADER, nrOfRows);
				
				int childIndex = 0;
				
				Header child = new Header(KEY_COMMUNE_UNITS, Header.HEADERTYPE_ROW_LABEL);
				areaHeader.setChild(childIndex++, child);

				child = new Header(KEY_CHILDREN_IN_FAMILY_DAYCARE, Header.HEADERTYPE_ROW_NORMAL);
				areaHeader.setChild(childIndex++, child);
				
				Iterator communeSchoolIter = communeSchools.iterator();
				while (communeSchoolIter.hasNext()) {
					School school = (School) communeSchoolIter.next();
					child = new Header(school.getName(), Header.HEADERTYPE_ROW_NONLOCALIZED_NORMAL);
					areaHeader.setChild(childIndex++, child);
				}

				child = new Header(KEY_SUM, Header.HEADERTYPE_ROW_SUM);
				areaHeader.setChild(childIndex++, child);
				
				child = new Header(null, Header.HEADERTYPE_ROW_SPACER);
				areaHeader.setChild(childIndex++, child);
				
				child = new Header(KEY_PRIVATE_UNITS, Header.HEADERTYPE_ROW_LABEL);
				areaHeader.setChild(childIndex++, child);
				
				child = new Header(KEY_CHILDREN_IN_FAMILY_DAYCARE, Header.HEADERTYPE_ROW_NORMAL);
				areaHeader.setChild(childIndex++, child);

				Iterator privateSchoolIter = privateSchools.iterator();
				while (privateSchoolIter.hasNext()) {
					School school = (School) privateSchoolIter.next();
					child = new Header(school.getName(), Header.HEADERTYPE_ROW_NONLOCALIZED_NORMAL);
					areaHeader.setChild(childIndex++, child);
				}

				child = new Header(KEY_SUM, Header.HEADERTYPE_ROW_NORMAL);
				areaHeader.setChild(childIndex++, child);

				headers[headerIndex] = areaHeader;
				headerIndex++;				
			}
		} catch (RemoteException e) {}
		
		return headers;
	}
	
	/**
	 * @see se.idega.idegaweb.commune.school.report.business.ReportModel#buildColumnHeaders()
	 */
	protected Header[] buildColumnHeaders() {
		Header[] headers = new Header[2];
		
		headers[0] = new Header(KEY_NUMBER_OF_CHILDREN, Header.HEADERTYPE_COLUMN_HEADER);
		headers[1] = new Header(KEY_MEAN_WEEK_HOURS, Header.HEADERTYPE_COLUMN_HEADER);
		
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
					columnMethod = COLUMN_METHOD_MEAN_HOURS;
					break;
			}
	
			Object rowParameter = null;
			int cellType = Cell.CELLTYPE_NORMAL;
			if (column == 1) {
				cellType = Cell.CELLTYPE_PERCENT;
			}
			try {
				ReportBusiness rb = getReportBusiness();
				Collection areas = rb.getAfterSchool7_9Areas();
				Iterator areaIter = areas.iterator();
				while (areaIter.hasNext()) {
					SchoolArea area = (SchoolArea) areaIter.next();
					String areaName = area.getName();
					Object areaId = area.getPrimaryKey();
					
					Cell cell = new Cell(this, row, column, ROW_METHOD_FAMILY_DAYCARE_COMMUNE,
							columnMethod, areaId, columnParameter, cellType);
					setCell(row++, column, cell);
					
					Collection communeSchools = (Collection) _communeSchoolsByArea.get(areaName);
					Iterator communeIter = communeSchools.iterator();
					while (communeIter.hasNext()) {
						School school = (School) communeIter.next();
						rowParameter = school.getPrimaryKey();
						cell = new Cell(this, row, column, ROW_METHOD_PROVIDER_COMMUNE,
								columnMethod, rowParameter, columnParameter, cellType);
						setCell(row++, column, cell);
					}
					
					cell = new Cell(this, row, column, ROW_METHOD_SUM,
							columnMethod, rowParameter, columnParameter, cellType);
					setCell(row++, column, cell);

					cell = new Cell(this, row, column, ROW_METHOD_FAMILY_DAYCARE_PRIVATE,
							columnMethod, areaId, columnParameter, cellType);
					setCell(row++, column, cell);

					Collection privateSchools = (Collection) _privateSchoolsByArea.get(areaName);
					Iterator privateIter = privateSchools.iterator();
					while (privateIter.hasNext()) {
						School school = (School) privateIter.next();
						rowParameter = school.getPrimaryKey();
						cell = new Cell(this, row, column, ROW_METHOD_PROVIDER_PRIVATE,
								columnMethod, rowParameter, columnParameter, cellType);
						setCell(row++, column, cell);
					}

					cell = new Cell(this, row, column, ROW_METHOD_SUM,
							columnMethod, rowParameter, columnParameter, cellType);
					setCell(row++, column, cell);
				}
			} catch (RemoteException e) {}
		}
	}
	
	/**
	 * @see se.idega.idegaweb.commune.school.report.business.ReportModel#calculate()
	 */
	protected float calculate(Cell cell) throws RemoteException {
		float value = 0f;
		int row = cell.getRow();
		int schoolId = -1;
		if (cell.getRowParameter() != null) {
			schoolId = ((Integer) cell.getRowParameter()).intValue();
		}
		int areaId = schoolId; // if type family daycare

		switch (cell.getColumnMethod()) {
			case COLUMN_METHOD_COUNT:
				switch (cell.getRowMethod()) {
					case ROW_METHOD_FAMILY_DAYCARE_COMMUNE:
						value = getFamilyDaycarePlacementCount(areaId, COMMUNE);
						break;
					case ROW_METHOD_PROVIDER_COMMUNE:
						value = getProviderPlacementCount(schoolId);
						break;
					case ROW_METHOD_FAMILY_DAYCARE_PRIVATE:
						value = getFamilyDaycarePlacementCount(areaId, PRIVATE);
						break;
					case ROW_METHOD_PROVIDER_PRIVATE:
						value = getProviderPlacementCount(schoolId);
						break;
					case ROW_METHOD_SUM:
						int i = row - 1;
						while (i >= 0 && getCell(i, 0).getRowMethod() != ROW_METHOD_SUM) {
							value += getCell(i, 0).getFloatValue();
							i--;
						}
						break;
				}
				break;
			case COLUMN_METHOD_MEAN_HOURS:
			switch (cell.getRowMethod()) {
				case ROW_METHOD_FAMILY_DAYCARE_COMMUNE:
					value = getFamilyDaycareMeanHours(areaId, COMMUNE);
					break;
				case ROW_METHOD_PROVIDER_COMMUNE:
					value = getProviderMeanHours(schoolId);
					break;
				case ROW_METHOD_FAMILY_DAYCARE_PRIVATE:
					value = getFamilyDaycareMeanHours(areaId, PRIVATE);
					break;
				case ROW_METHOD_PROVIDER_PRIVATE:
					value = getProviderMeanHours(schoolId);
					break;
				case ROW_METHOD_SUM:
					int i = row - 1;
					int rowCount = 0;
					while (i >= 0 && getCell(i, 1).getRowMethod() != ROW_METHOD_SUM) {
						float hours = getCell(i, 1).getFloatValue(); 
						value += hours;
						if (hours > 0) {
							rowCount++;
						}
						i--;
					}
					if (rowCount > 0) {
						value /= rowCount;
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
	 * Returns the number of child placements in family daycare for the specified management type.
	 */
	protected int getFamilyDaycarePlacementCount(int areaId, int managementType) throws RemoteException {
		ReportBusiness rb = getReportBusiness();
		
		int schoolType = rb.getFamilyAfterSchool7_9TypeId();
				
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
		query = getQuery(QUERY_FAMILY_DAYCARE_PLACEMENTS);
		if (query == null) {
			query = new PreparedQuery(getConnection());
			query.setSelectCount();
			query.setChildCarePlacements();
			query.setSchoolTypeForProvider(); // parameter 1
			query.setFourManagementTypes(); // parameter 2-5
			query.setSchoolArea(); // parameter 6
			query.prepare();
			setQuery(QUERY_FAMILY_DAYCARE_PLACEMENTS, query);
		}
		query.setInt(1, schoolType);
		
		query.setString(2, managementType1);
		query.setString(3, managementType2);
		query.setString(4, managementType3);
		query.setString(5, managementType4);
		
		query.setInt(6, areaId);
		
		return query.execute();
	}
	
	/**
	 * Returns the number of child placements for the specified school.
	 */
	protected int getProviderPlacementCount(int schoolId) throws RemoteException {
		ReportBusiness rb = getReportBusiness();
		int schoolType = rb.getAfterSchool7_9TypeId();

		PreparedQuery query = null;
		query = getQuery(QUERY_PROVIDER_PLACEMENTS);
		if (query == null) {
			query = new PreparedQuery(getConnection());
			query.setSelectCount();
			query.setChildCarePlacements();
			query.setSchool(); // parameter 1
			query.setSchoolTypeForProvider(); // parameter 2
			query.prepare();
			setQuery(QUERY_PROVIDER_PLACEMENTS, query);
		}
		query.setInt(1, schoolId);
		query.setInt(2, schoolType);
		
		return query.execute();
	}
	
	/**
	 * Returns the mean week hours in family daycare for the specified management type.
	 */
	protected float getFamilyDaycareMeanHours(int areaId, int managementType) throws RemoteException {
		ReportBusiness rb = getReportBusiness();
		
		int schoolType = rb.getFamilyAfterSchool7_9TypeId();
				
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
		PreparedQuery query2 = null;
		query = getQuery(QUERY_FAMILY_DAYCARE_MEAN_HOURS);
		query2 = getQuery(QUERY_FAMILY_DAYCARE_MEAN_HOURS2);
		if (query == null) {
			query = new PreparedQuery(getConnection());
			query.setSelectMeanChildCareWeekHoursCareTime();
			query.setChildCarePlacementsCareTime();
			query.setSchoolTypeForProvider(); // parameter 1
			query.setFourManagementTypes(); // parameter 2-5
			query.setSchoolArea(); // parameter 6
			query.prepare();
			setQuery(QUERY_FAMILY_DAYCARE_MEAN_HOURS, query);
			
			query2 = new PreparedQuery(getConnection());
			query2.setSelectMeanChildCareWeekHoursCareTimeString();
			query2.setChildCarePlacementsCareTimeString();
			query2.setSchoolTypeForProvider(); // parameter 1
			query2.setFourManagementTypes(); // parameter 2-5
			query2.setSchoolArea(); // parameter 6
			query2.prepare();
			setQuery(QUERY_FAMILY_DAYCARE_MEAN_HOURS2, query2);
		}
		
		query.setInt(1, schoolType);		
		query.setString(2, managementType1);
		query.setString(3, managementType2);
		query.setString(4, managementType3);
		query.setString(5, managementType4);		
		query.setInt(6, areaId);
		
		query2.setInt(1, schoolType);		
		query2.setString(2, managementType1);
		query2.setString(3, managementType2);
		query2.setString(4, managementType3);
		query2.setString(5, managementType4);		
		query2.setInt(6, areaId);
		
		return (query.executeFloat() + query2.executeFloat()) / 2;
	}
	
	/**
	 * Returns the number of child placement mean hours for the specified school.
	 */
	protected float getProviderMeanHours(int schoolId) throws RemoteException {
		ReportBusiness rb = getReportBusiness();
		int schoolType = rb.getAfterSchool7_9TypeId();

		PreparedQuery query = null;
		PreparedQuery query2 = null;
		query = getQuery(QUERY_PROVIDER_MEAN_HOURS);
		query2 = getQuery(QUERY_PROVIDER_MEAN_HOURS2);
		if (query == null) {
			query = new PreparedQuery(getConnection());
			query.setSelectMeanChildCareWeekHoursCareTime();
			query.setChildCarePlacementsCareTime();
			query.setSchool(); // parameter 1
			query.setSchoolTypeForProvider(); // parameter 2
			query.prepare();
			setQuery(QUERY_PROVIDER_MEAN_HOURS, query);
			
			query2 = new PreparedQuery(getConnection());
			query2.setSelectMeanChildCareWeekHoursCareTimeString();
			query2.setChildCarePlacementsCareTimeString();
			query2.setSchool(); // parameter 1
			query2.setSchoolTypeForProvider(); // parameter 2
			query2.prepare();
			setQuery(QUERY_PROVIDER_MEAN_HOURS2, query2);
		}

		query.setInt(1, schoolId);
		query.setInt(2, schoolType);

		query2.setInt(1, schoolId);
		query2.setInt(2, schoolType);

		return (query.executeFloat() + query2.executeFloat()) / 2;
	}
	
}
