/*
 * $Id: NackaCitizenElementarySchoolPlacementReport.java,v 1.2 2003/12/08 17:44:51 laddi Exp $
 *
 * Copyright (C) 2003 Agura IT. All Rights Reserved.
 *
 * This software is the proprietary information of Agura IT AB.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.school.report.presentation;

import se.idega.idegaweb.commune.school.report.business.NackaCitizenElementarySchoolPlacementReportModel;

import com.idega.presentation.Table;

/** 
 * This report presents number of placements in elementary (and compulory) schools
 * for Nacka students.
 * <p>
 * Last modified: $Date: 2003/12/08 17:44:51 $ by $Author: laddi $
 *
 * @author Anders Lindman
 * @version $Revision: 1.2 $
 */
public class NackaCitizenElementarySchoolPlacementReport extends ReportBlock {
	
	/**
	 * @see ReportBlock#getReportModelClass()
	 */
	protected Class getReportModelClass() {
		return NackaCitizenElementarySchoolPlacementReportModel.class;
	}
	
	/**
	 * @see ReportBlock#buildReportTable()
	 */
	protected void buildReportTable(Table table) {
		buildColumnHeaders(table);
		buildRowHeaders(table);
		buildReportCells(table);
	}
}
