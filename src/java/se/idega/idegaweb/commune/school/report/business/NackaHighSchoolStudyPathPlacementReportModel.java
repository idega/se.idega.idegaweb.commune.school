/*
 * $Id: NackaHighSchoolStudyPathPlacementReportModel.java,v 1.10 2004/01/23 12:31:57 anders Exp $
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
import java.util.LinkedHashMap;
import java.util.Map;

import com.idega.block.school.data.SchoolStudyPath;

/** 
 * Report model for high school placements for all study paths.
 * <p>
 * Last modified: $Date: 2004/01/23 12:31:57 $ by $Author: anders $
 *
 * @author Anders Lindman
 * @version $Revision: 1.10 $
 */
public class NackaHighSchoolStudyPathPlacementReportModel extends ReportModel {

	private final static int COLUMN_SIZE = 6;
	
	private final static int ROW_METHOD_STUDY_PATH = 1;
	private final static int ROW_METHOD_TOTAL = 2;

	private final static int COLUMN_METHOD_STUDY_PATH_CODE = 101;
	private final static int COLUMN_METHOD_SCHOOL_YEAR = 102;
	private final static int COLUMN_METHOD_COMPULSORY_SCHOOL_YEAR = 103;
	private final static int COLUMN_METHOD_SUM_1_4 = 104;

	private final static String QUERY_STUDY_PATH = "study_path";
	private final static String QUERY_STUDY_PATH_YEAR = "study_path_year";
	
	private final static String KEY_REPORT_TITLE = KP + "title_nacka_high_school_study_path_placements";
	
	private Collection _studyPaths = null;
	private Map _placements = null;
	
	/**
	 * Constructs this report model.
	 * @param reportBusiness the report business instance for calculating cell values
	 */
	public NackaHighSchoolStudyPathPlacementReportModel(ReportBusiness reportBusiness) {
		super(reportBusiness);
		_placements = new LinkedHashMap();
	}
	
	/**
	 * @see se.idega.idegaweb.commune.school.report.business.ReportModel#initReportSize()
	 */
	protected void initReportSize() {
		try {
			Collection studyPaths = getStudyPaths();
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
			Collection studyPaths = getStudyPaths();
			headers = new Header[studyPaths.size() + 1];
			Iterator iter = studyPaths.iterator();
			int headerIndex = 0;
			while (iter.hasNext()) {
				SchoolStudyPath studyPath = (SchoolStudyPath) iter.next();
				String label = studyPath.getDescription();
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
		Header[] headers = new Header[2];
		
		Header h = new Header(KEY_STUDY_PATH_CODE, Header.HEADERTYPE_COLUMN_HEADER);
		headers[0] = h;
		
		h = new Header(KEY_SCHOOL_YEAR, Header.HEADERTYPE_COLUMN_HEADER, 5);
		Header child0 = new Header(KEY_SCHOOL_YEAR_1, Header.HEADERTYPE_COLUMN_NORMAL);
		Header child1 = new Header(KEY_SCHOOL_YEAR_2, Header.HEADERTYPE_COLUMN_NORMAL);
		Header child2 = new Header(KEY_SCHOOL_YEAR_3, Header.HEADERTYPE_COLUMN_NORMAL);
		Header child3 = new Header(KEY_SCHOOL_YEAR_4, Header.HEADERTYPE_COLUMN_NORMAL);
		Header child4 = new Header(KEY_SUM_1_4, Header.HEADERTYPE_COLUMN_NORMAL);
		h.setChild(0, child0);
		h.setChild(1, child1);
		h.setChild(2, child2);
		h.setChild(3, child3);
		h.setChild(4, child4);
		headers[1] = h;
				
		return headers;
	}
	
	/**
	 * @see se.idega.idegaweb.commune.school.report.business.ReportModel#buildCells()
	 */
	protected void buildCells() {
		for (int column = 0; column < getColumnSize(); column++) {
			int row = 0;
			int columnMethod = 0;
			String columnParameter = null;
			switch (column) {
				case 0:
					columnMethod = COLUMN_METHOD_STUDY_PATH_CODE;
					break;
				case 1:
					columnMethod = COLUMN_METHOD_SCHOOL_YEAR;
					columnParameter = "1";
					break;
				case 2:
					columnMethod = COLUMN_METHOD_SCHOOL_YEAR;
					columnParameter = "2";
					break;
				case 3:
					columnMethod = COLUMN_METHOD_SCHOOL_YEAR;
					columnParameter = "3";
					break;
				case 4:
					columnMethod = COLUMN_METHOD_SCHOOL_YEAR;
					columnParameter = "4";
					break;
				case 5:
					columnMethod = COLUMN_METHOD_SUM_1_4;
					break;
			}
			
			try {
				Collection studyPaths = getStudyPaths();
				Iterator iter = studyPaths.iterator();
				while (iter.hasNext()) {
					SchoolStudyPath studyPath = (SchoolStudyPath) iter.next();
					String studyPathCode = studyPath.getCode();
					Object rowParameter = studyPathCode;
					String schoolYear = columnParameter;
					if (columnMethod == COLUMN_METHOD_COMPULSORY_SCHOOL_YEAR) {
						columnMethod = COLUMN_METHOD_SCHOOL_YEAR;
					} else if (columnMethod == COLUMN_METHOD_SCHOOL_YEAR) {
						if (studyPathCode.length() > 3 && studyPathCode.substring(0, 3).equals("GYS")) {
							columnMethod = COLUMN_METHOD_COMPULSORY_SCHOOL_YEAR;
							schoolYear = "GS" + columnParameter; 
						} else {
							schoolYear = "G" + columnParameter;
						}
					}
					int cellType = Cell.CELLTYPE_NORMAL;
					if (columnMethod == COLUMN_METHOD_STUDY_PATH_CODE) {
						cellType = Cell.CELLTYPE_ROW_HEADER;
					}
					Cell cell = new Cell(this, row, column, ROW_METHOD_STUDY_PATH,
							columnMethod, rowParameter, schoolYear, cellType);
					setCell(row, column, cell);
					row++;
				}

				int cellType = Cell.CELLTYPE_TOTAL;
				if (columnMethod == COLUMN_METHOD_STUDY_PATH_CODE) {
					cellType = Cell.CELLTYPE_ROW_HEADER;
				}
				Cell cell = new Cell(this, row, column, ROW_METHOD_TOTAL,
						columnMethod, "", columnParameter, cellType);
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
		
		String schoolYearName = (String) cell.getColumnParameter();
		
		switch (cell.getRowMethod()) {
			case ROW_METHOD_STUDY_PATH:
				switch (cell.getColumnMethod()) {
					case COLUMN_METHOD_SCHOOL_YEAR:
						value = getHighSchoolPlacementCount(studyPathCode, schoolYearName, false);
						break;
					case COLUMN_METHOD_COMPULSORY_SCHOOL_YEAR:
						value = getHighSchoolPlacementCount(studyPathCode, schoolYearName, true);
						break;
					case COLUMN_METHOD_SUM_1_4:
						value = getCell(row, 1).getFloatValue() +
								getCell(row, 2).getFloatValue() +
								getCell(row, 3).getFloatValue() +
								getCell(row, 4).getFloatValue();
						break;
				}
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
	 * Returns the number of student placements for the specified study path code and school year.
	 */
	protected int getHighSchoolPlacementCount(String studyPathCode, String schoolYearName, boolean isCompulsory) 
			throws RemoteException {
		PreparedQuery query = null;
		ReportBusiness rb = getReportBusiness();
		query = getQuery(QUERY_STUDY_PATH_YEAR);
		if (query == null) {
			query = new PreparedQuery(getConnection());
			query.setSelectCount();
			query.setPlacements(rb.getSchoolSeasonId());
			query.setOnlyNackaCitizens();
			query.setStudyPathPrefix(); // parameter 1
			query.setSchoolYearName(); // parameter 2
			query.setSchoolType(); // parameter 3
			query.prepare();
			setQuery(QUERY_STUDY_PATH_YEAR, query);
		}
		query.setString(1, studyPathCode);
		query.setString(2, schoolYearName);
		if (isCompulsory) {
			query.setInt(3, rb.getCompulsoryHighSchoolTypeId());
		} else {
			query.setInt(3, rb.getHighSchoolTypeId());
		}
		return query.execute();
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
			query.setOnlyNackaCitizens();
			query.setStudyPathPrefix(); // parameter 1
			query.prepare();
			setQuery(QUERY_STUDY_PATH, query);
		}
		query.setString(1, studyPathCode);
		return query.execute();
	}
	
	/*
	 * Returns a filtered list with only Nacka study paths. 
	 */
	private Collection getStudyPaths() throws RemoteException {
		if (_studyPaths == null) {
			_studyPaths = new ArrayList();
			ReportBusiness rb = getReportBusiness();
			Collection c = rb.getAllStudyPathsIncludingDirections();
			Iterator iter = c.iterator();
			while (iter.hasNext()) {
				SchoolStudyPath sp = (SchoolStudyPath) iter.next();
				String code = sp.getCode();
				int count = getHighSchoolPlacementCount(code);
				if (count > 0) {
					_studyPaths.add(sp);
					_placements.put(code, new Integer(count));
				}
			}			
		}
		return _studyPaths;
	}
}
