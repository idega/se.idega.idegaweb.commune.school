/*
 * $Id: SchoolReports.java,v 1.13 2004/01/19 08:49:14 anders Exp $
 *
 * Copyright (C) 2003 Agura IT. All Rights Reserved.
 *
 * This software is the proprietary information of Agura IT AB.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.school.report.presentation;

import java.rmi.RemoteException;

import se.idega.idegaweb.commune.presentation.CommuneBlock;
import se.idega.idegaweb.commune.school.report.business.NackaCitizenElementarySchoolPlacementReportModel;
import se.idega.idegaweb.commune.school.report.business.NackaCommuneHighSchoolPlacementReportModel;
import se.idega.idegaweb.commune.school.report.business.NackaCompulsoryHighSchoolPlacementReportModel;
import se.idega.idegaweb.commune.school.report.business.NackaCompulsorySchoolOCCPlacementReportModel;
import se.idega.idegaweb.commune.school.report.business.NackaCompulsorySchoolPlacementReportModel;
import se.idega.idegaweb.commune.school.report.business.NackaElementarySchoolOCCPlacementReportModel;
import se.idega.idegaweb.commune.school.report.business.NackaElementarySchoolPlacementReportModel;
import se.idega.idegaweb.commune.school.report.business.NackaHighSchoolAgePlacementReportModel;
import se.idega.idegaweb.commune.school.report.business.NackaHighSchoolYearPlacementReportModel;
import se.idega.idegaweb.commune.school.report.business.NackaPrivateHighSchoolPlacementReportModel;
import se.idega.idegaweb.commune.school.report.business.NackaPrivateSchoolOCCPlacementReportModel;
import se.idega.idegaweb.commune.school.report.business.NackaPrivateSchoolPlacementReportModel;
import se.idega.idegaweb.commune.school.report.business.ReportBusiness;
import se.idega.idegaweb.commune.school.report.business.ReportModel;

import com.idega.presentation.ExceptionWrapper;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Break;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.GenericButton;
import com.idega.presentation.ui.SubmitButton;

/** 
 * This block handles selecting and presenting school reports.
 * <p>
 * Last modified: $Date: 2004/01/19 08:49:14 $ by $Author: anders $
 *
 * @author Anders Lindman
 * @version $Revision: 1.13 $
 */
public class SchoolReports extends CommuneBlock {

	public final static String IW_BUNDLE_IDENTIFIER = "se.idega.idegaweb.commune.school";

	private final static String PP = "school_report."; // Parameter prefix
	
	private final static String PARAMETER_REPORT_INDEX = PP + "report_index";
	private final static String PARAMETER_CREATE_REPORT = PP + "create_report";

	private final static String KP = "school_report."; // Localization key prefix
	
	private final static String KEY_REPORT_SELECTOR_TITLE = KP + "select_report";
	private final static String KEY_CREATE_REPORT = KP + "create_report";
	private final static String KEY_NO_REPORT_SELECTED = KP + "no_report_selected";
	private final static String KEY_FOR_PRINTING = KP + "for_printing";	
	
	private final static int ACTION_DEFAULT = 1;
	private final static int ACTION_CREATE_REPORT = 2;

	private Class[] _reportModelClasses = {
				NackaCitizenElementarySchoolPlacementReportModel.class,
				NackaElementarySchoolPlacementReportModel.class,
				NackaElementarySchoolOCCPlacementReportModel.class,
				NackaHighSchoolYearPlacementReportModel.class,
				NackaHighSchoolAgePlacementReportModel.class,
				NackaCommuneHighSchoolPlacementReportModel.class,
				NackaCompulsorySchoolPlacementReportModel.class,
				NackaCompulsorySchoolOCCPlacementReportModel.class,
				NackaCompulsoryHighSchoolPlacementReportModel.class,
				NackaPrivateSchoolPlacementReportModel.class,
				NackaPrivateSchoolOCCPlacementReportModel.class,
				NackaPrivateHighSchoolPlacementReportModel.class
	};
	
	private ReportModel[] _reportModels = null;
	
	/**
	 * @see com.idega.presentation.PresentationObject#getBundleIdentifier()
	 */
	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}
	
	/**
	 * @see com.idega.presentation.Block#main(com.idega.presentation.IWContext)
	 */
	public void main(IWContext iwc) {
		try {
			createReportModels(iwc);
			int action = parseAction(iwc);
			switch (action) {
				case ACTION_DEFAULT:
					handleDefaultAction(iwc);
					break;
				case ACTION_CREATE_REPORT:
					handleCreateReportAction(iwc);
					break;
			}
		} catch (Exception e) {
			log(e);
			add(new ExceptionWrapper(e, this));
		}
	}

	/*
	 * Returns the action constant for the action to perform based 
	 * on the POST parameters in the specified context.
	 */
	private int parseAction(IWContext iwc) {
		int action = ACTION_DEFAULT;
		if (iwc.isParameterSet(PARAMETER_CREATE_REPORT)) {
			action = ACTION_CREATE_REPORT;
		}
		return action;
	}

	/*
	 * Handles the default action for this block.
	 */	
	private void handleDefaultAction(IWContext iwc) {
		add(getSelectorForm(iwc));
	}

	/*
	 * Handles the create report action for this block.
	 */	
	private void handleCreateReportAction(IWContext iwc) {
		add(getSelectorForm(iwc));
		add(new Break());
		Table table = new Table();
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());
		String reportIndex = iwc.getParameter(PARAMETER_REPORT_INDEX);
		if (reportIndex == null || reportIndex.equals("-1")) {
			table.add(getErrorText(localize(KEY_NO_REPORT_SELECTED, "No report selected.")), 1, 1);
			add(table);
			return;
		}
		table.add(new ReportBlock(_reportModelClasses[Integer.parseInt(reportIndex)]), 1, 1);
		add(table);
		add(new Break());
		GenericButton button = new GenericButton("", localize(KEY_FOR_PRINTING, "For printing"));
		button = getButton(button);
		button.setWindowToOpen(ReportWindow.class);
		table = new Table();
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());
		table.add(button, 1, 1);
		add(table);
	}
	
	private Form getSelectorForm(IWContext iwc) {
		Form form = new Form();
		Table table = new Table();
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());
		
		DropdownMenu reportSelector = new DropdownMenu(PARAMETER_REPORT_INDEX);
		reportSelector.addMenuElement(-1, localize(KEY_REPORT_SELECTOR_TITLE, "Select report"));
		for (int i = 0; i < _reportModels.length; i++) {
			String s = _reportModels[i].getReportTitleLocalizationKey();
			reportSelector.addMenuElement(i, localize(s, s));			
		}
		String selectedIndex = iwc.getParameter(PARAMETER_REPORT_INDEX);
		if (selectedIndex != null) {
			reportSelector.setSelectedElement(selectedIndex);
		}
		reportSelector = (DropdownMenu) getStyledInterface(reportSelector);
		table.add(reportSelector, 1, 1);
		
		SubmitButton button = new SubmitButton(PARAMETER_CREATE_REPORT,
				localize(KEY_CREATE_REPORT, "Create report"));
		button = (SubmitButton) getStyledInterface(button);
		table.add(button, 2, 1);
		
		form.add(table);
		
		return form;
	}

	/*
	 * Creates all report models from the report model classes. 
	 */
	private void createReportModels(IWContext iwc) throws RemoteException {
		ReportBusiness rb = getReportBusiness(iwc);
		_reportModels = new ReportModel[_reportModelClasses.length];
		for (int i = 0; i <_reportModelClasses.length; i++) {
			_reportModels[i] = rb.createReportModel(_reportModelClasses[i]);
		}
	}

	/*
	 * Returns a report business object.
	 */
	private ReportBusiness getReportBusiness(IWContext iwc) throws RemoteException {
		return (ReportBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, ReportBusiness.class);
	}	
}
