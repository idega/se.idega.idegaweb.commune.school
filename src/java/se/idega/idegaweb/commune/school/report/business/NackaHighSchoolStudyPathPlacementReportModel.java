/*
 * $Id: NackaHighSchoolStudyPathPlacementReportModel.java,v 1.3 2004/01/22 08:17:35 anders Exp $
 *
 * Copyright (C) 2003 Agura IT. All Rights Reserved.
 *
 * This software is the proprietary information of Agura IT AB.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.school.report.business;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import com.idega.block.school.data.SchoolStudyPath;

/** 
 * Report model for high school placements for all study paths.
 * <p>
 * Last modified: $Date: 2004/01/22 08:17:35 $ by $Author: anders $
 *
 * @author Anders Lindman
 * @version $Revision: 1.3 $
 */
public class NackaHighSchoolStudyPathPlacementReportModel extends ReportModel {

	private final static int COLUMN_SIZE = 1;
	
	private final static int ROW_METHOD_STUDY_PATH = 1;
	private final static int ROW_METHOD_TOTAL = 2;

	private final static int COLUMN_METHOD_COUNT = 101;

	private final static String QUERY_STUDY_PATH = "study_path";
	
	private final static String KEY_REPORT_TITLE = KP + "title_nacka_high_school_study_path_placements";
	
	/**
	 * Constructs this report model.
	 * @param reportBusiness the report business instance for calculating cell values
	 */
	public NackaHighSchoolStudyPathPlacementReportModel(ReportBusiness reportBusiness) {
		super(reportBusiness);
		try {
			Collection studyPaths = reportBusiness.getAllStudyPathsIncludingDirections();
			int rowSize = 0;
			rowSize += studyPaths.size() + 1; 
			setReportSize(rowSize, COLUMN_SIZE);
		} catch (RemoteException e) {
			log(e.getMessage());
		}
	}
	
	/**
	 * @see se.idega.idegaweb.commune.school.report.business.ReportModel#buildRowHeaders()
	 */
	protected Header[] buildRowHeaders() {
		Header[] headers = null;
		
		try {
			ReportBusiness rb = getReportBusiness();
			Collection studyPaths = rb.getAllStudyPathsIncludingDirections();
			headers = new Header[studyPaths.size() + 1];
			Iterator iter = studyPaths.iterator();
			int headerIndex = 0;
			while (iter.hasNext()) {
				SchoolStudyPath studyPath = (SchoolStudyPath) iter.next();
				String label = studyPath.getDescription() + " (" + studyPath.getCode() + ")";
				headers[headerIndex] = new Header(label, Header.HEADERTYPE_ROW_NONLOCALIZED_HEADER);
				headerIndex++;
			}
			
			Header header = new Header(KEY_TOTAL, Header.HEADERTYPE_ROW_TOTAL);
			headers[headerIndex] = header;
			headerIndex++;

		} catch (RemoteException e) {
			log(e.getMessage());
		}
		
		return headers;
	}
	
	/**
	 * @see se.idega.idegaweb.commune.school.report.business.ReportModel#buildColumnHeaders()
	 */
	protected Header[] buildColumnHeaders() {
		Header[] headers = new Header[1];
		
		headers[0] = new Header(KEY_NUMBER_OF_STUDENTS, Header.HEADERTYPE_COLUMN_HEADER);
				
		return headers;
	}
	
	/**
	 * @see se.idega.idegaweb.commune.school.report.business.ReportModel#buildCells()
	 */
	protected void buildCells() {
		for (int column = 0; column < getColumnSize(); column++) {
			int row = 0;
			int columnMethod = 0;
			Integer columnParameter = null;
			switch (column) {
				case 0:
					columnMethod = COLUMN_METHOD_COUNT;
					break;
			}
			
			try {
				ReportBusiness rb = getReportBusiness();
				Collection studyPaths = rb.getAllStudyPathsIncludingDirections();
				Iterator iter = studyPaths.iterator();
				while (iter.hasNext()) {
					SchoolStudyPath studyPath = (SchoolStudyPath) iter.next();
					Object rowParameter = studyPath.getCode();
					Cell cell = new Cell(this, row, column, ROW_METHOD_STUDY_PATH,
							columnMethod, rowParameter, columnParameter, Cell.CELLTYPE_NORMAL);
					setCell(row, column, cell);
					row++;
				}

				Cell cell = new Cell(this, row, column, ROW_METHOD_TOTAL,
						columnMethod, null, columnParameter, Cell.CELLTYPE_TOTAL);
				setCell(row, column, cell);
				row++;
				
			} catch (RemoteException e) {
				log(e.getMessage());
			}
		}
	}
	
	/**
	 * @see se.idega.idegaweb.commune.school.report.business.ReportModel#calculate()
	 */
	protected float calculate(Cell cell) throws RemoteException {
		float value = 0f;
		String studyPathCode = null;
		if (cell.getRowParameter() != null) {
			studyPathCode = (String) cell.getRowParameter();
		}
		int row = cell.getRow();
		int column = cell.getColumn();
		
		switch (cell.getRowMethod()) {
			case ROW_METHOD_STUDY_PATH:
				value = getHighSchoolPlacementCount(studyPathCode);
				break;
			case ROW_METHOD_TOTAL:
				for (int i = 0; i < row; i++) {
					Cell c = getCell(i, column);
					value += c.getFloatValue();
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
	 * Returns the number of student placements for the specified study path code.
	 */
	protected int getHighSchoolPlacementCount(String studyPathCode) throws RemoteException {
		PreparedQuery query = null;
		ReportBusiness rb = getReportBusiness();
		query = getQuery(QUERY_STUDY_PATH);
		if (query == null) {
			query = new PreparedQuery(getConnection());
			query.setSelectCount();
			query.setPlacements(rb.getSchoolSeasonId());
			query.setStudyPathPrefix(); // parameter 1
			query.prepare();
			setQuery(QUERY_STUDY_PATH, query);
		}
		query.setString(1, studyPathCode);
		return query.execute();
	}
}
