/*
 * $Id: ReportXLSWriter.java,v 1.1 2004/02/27 10:20:39 anders Exp $
 *
 * Copyright (C) 2003 Agura IT. All Rights Reserved.
 *
 * This software is the proprietary information of Agura IT AB.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.school.report.business;

import javax.ejb.FinderException;

import com.idega.core.file.data.ICFile;
import com.idega.core.file.data.ICFileBMPBean;
import com.idega.core.file.data.ICFileHome;
import com.idega.data.IDOLookupException;
import com.idega.presentation.Table;
import com.idega.util.poi.POIUtility;

/** 
 * Creates report files in MS Excel format.
 * <p>
 * Last modified: $Date: 2004/02/27 10:20:39 $ by $Author: anders $
 *
 * @author Anders Lindman
 * @version $Revision: 1.1 $
 */
public class ReportXLSWriter {

	private final static String REPORT_FOLDER_NAME = "School Report Files";

	Table _table = null;
	String _filename = null;
	String _reportTitle = null;
	
	/**
	 * Constructs a report XLS writer object.
	 * @param table the idegaWeb table containing the report
	 * @param reportTitle the title text for the report 
	 */	
	public ReportXLSWriter(Table table, String filename, String reportTitle) {
		_table = table;
		_filename = filename;
		_reportTitle = reportTitle;
	}	
	
	/**
	 * Creates the PDF report file.
	 */
	public ICFile createFile() {
		ICFile reportFolder = null;
		ICFileHome fileHome = null;

		try {
			fileHome = (ICFileHome) com.idega.data.IDOLookup.getHome(ICFile.class);
			reportFolder = fileHome.findByFileName(REPORT_FOLDER_NAME);
		} catch (FinderException e) {
			try {
				ICFile root = fileHome.findByFileName(ICFileBMPBean.IC_ROOT_FOLDER_NAME);
				reportFolder = fileHome.create();
				reportFolder.setName(REPORT_FOLDER_NAME);
				reportFolder.setMimeType("application/vnd.iw-folder");
				reportFolder.store();
				root.addChild(reportFolder);
			} catch (Exception e2) {
				System.out.println(e2);
				return null;
			}
		} catch (IDOLookupException e) {
			System.out.println(e);
			return null;
		}

		ICFile exportFile = null;
				
		try {
			try {
				exportFile = fileHome.findByFileName(_filename);
				if (exportFile != null) {
					exportFile.remove();
				}
			} catch (FinderException e) {}

			exportFile =  POIUtility.createICFileFromTable(_table, _filename, _reportTitle);

			reportFolder.addChild(exportFile);
		} catch (Exception e) {
			System.out.println(e);
		}
		
		return exportFile;
	}
}
