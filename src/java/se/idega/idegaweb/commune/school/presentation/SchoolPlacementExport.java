/*
 * $Id: SchoolPlacementExport.java,v 1.1 2004/01/29 14:11:52 anders Exp $
 *
 * Copyright (C) 2004 Agura IT. All Rights Reserved.
 *
 * This software is the proprietary information of Agura IT AB.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.school.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import se.idega.idegaweb.commune.presentation.CommuneBlock;
import se.idega.idegaweb.commune.school.business.SchoolExportBusiness;

import com.idega.core.file.data.ICFile;
import com.idega.presentation.ExceptionWrapper;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Break;
import com.idega.presentation.text.Link;
import com.idega.presentation.ui.SubmitButton;
import com.idega.util.IWTimestamp;

/** 
 * This idegaWeb block exports school placement to text files.
 * <p>
 * Last modified: $Date: 2004/01/29 14:11:52 $ by $Author: anders $
 *
 * @author Anders Lindman
 * @version $Revision: 1.1 $
 */
public class SchoolPlacementExport extends CommuneBlock {

	private final static int ACTION_DEFAULT = 0;
	private final static int ACTION_EXPORT_ELEMENTARY_SCHOOL_PLACEMENTS = 1;
	private final static int ACTION_EXPORT_HIGH_SCHOOL_PLACEMENTS = 2;
	
	private final static String PP = "sch_export_"; // Parameter prefix 

	private final static String PARAMETER_ELEMENTARY_SCHOOL_EXPORT = PP + "e_sch_exp";
	private final static String PARAMETER_HIGH_SCHOOL_EXPORT = PP + "h_sch_exp";
	

	/**
	 * @see com.idega.presentation.Block#main()
	 */
	public void main(final IWContext iwc) {
		try {
			int action = parseAction(iwc);
			switch (action) {
				case ACTION_DEFAULT:
					handleDefaultAction(iwc);
					break;
				case ACTION_EXPORT_ELEMENTARY_SCHOOL_PLACEMENTS:
					handleExportElementarySchoolPlacements(iwc);
					break;
				case ACTION_EXPORT_HIGH_SCHOOL_PLACEMENTS:
					handleExportHighSchoolPlacements(iwc);
					break;
			}
		}
		catch (Exception e) {
			add(new ExceptionWrapper(e));
		}
	}

	/*
	 * Returns the action constant for the action to perform based 
	 * on the POST parameters in the specified context.
	 */
	private int parseAction(IWContext iwc) {
		int action = ACTION_DEFAULT;
		
		if (iwc.isParameterSet(PARAMETER_ELEMENTARY_SCHOOL_EXPORT)) {
			action = ACTION_EXPORT_ELEMENTARY_SCHOOL_PLACEMENTS;
		} else if (iwc.isParameterSet(PARAMETER_HIGH_SCHOOL_EXPORT)) {
			action = ACTION_EXPORT_HIGH_SCHOOL_PLACEMENTS;
		}
		
		return action;
	}

	/*
	 * Handles the default action for this block.
	 */	
	private void handleDefaultAction(IWContext iwc) throws RemoteException {
		Table table = new Table();
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());
		
		SubmitButton elementarySchoolButton = new SubmitButton(PARAMETER_ELEMENTARY_SCHOOL_EXPORT, 
				"Export Elementary School Students");
		SubmitButton highSchoolButton = new SubmitButton(PARAMETER_HIGH_SCHOOL_EXPORT, 
				"Export High School Students");
		
		table.add(elementarySchoolButton, 1, 1);
		table.add(highSchoolButton, 2, 1);
		add(table);
		
		add(new Break());

		table = new Table();
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());
		int row = 1;
		Iterator iter = getSchoolExportBusiness(iwc).getAllExportFiles();
		if (iter != null) {
			while (iter.hasNext()) {
				ICFile file = (ICFile) iter.next();
				Link link = new Link(file.getName());
				link.setFile(file);
				table.add(link, 1, row++);
			}
		}
		add(table);
	}
	
	/*
	 * Handles elementary school placement export.
	 */	
	private void handleExportElementarySchoolPlacements(IWContext iwc) throws RemoteException {
		SchoolExportBusiness seb = getSchoolExportBusiness(iwc);
		IWTimestamp now = IWTimestamp.RightNow();
		String today = now.getDateString("yyMMdd");
		String fileName = "elementary_school_" + today + ".txt";
		Collection placements = seb.findAllElementarySchoolPlacements();
		seb.exportFile(placements, fileName);
		handleDefaultAction(iwc);
	}
	
	/*
	 * Handles high school placement export.
	 */	
	private void handleExportHighSchoolPlacements(IWContext iwc) throws RemoteException {
		SchoolExportBusiness seb = getSchoolExportBusiness(iwc);
		IWTimestamp now = IWTimestamp.RightNow();
		String today = now.getDateString("yyMMdd");
		String fileName = "high_school_" + today + ".txt";
		Collection placements = seb.findAllHighSchoolPlacements();
		seb.exportFile(placements, fileName);
		handleDefaultAction(iwc);
	}

	/*
	 * Returns a school export business object
	 */
	private SchoolExportBusiness getSchoolExportBusiness(IWContext iwc) throws RemoteException {
		return (SchoolExportBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, SchoolExportBusiness.class);
	}	
}
