/*
 * $Id: SchoolReports.java,v 1.38 2004/11/02 13:17:47 aron Exp $
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
import se.idega.idegaweb.commune.school.report.business.NackaCC15HoursPlacementReportModel;
import se.idega.idegaweb.commune.school.report.business.NackaCC6YearPlacementReportModel;
import se.idega.idegaweb.commune.school.report.business.NackaCC7_9YearPlacementReportModel;
import se.idega.idegaweb.commune.school.report.business.NackaCCCommunePrivatePlacementReportModel;
import se.idega.idegaweb.commune.school.report.business.NackaCCHourIntervalReportModel;
import se.idega.idegaweb.commune.school.report.business.NackaCCPlacementReportModel;
import se.idega.idegaweb.commune.school.report.business.NackaCCPreSchoolPlacementReportModel;
import se.idega.idegaweb.commune.school.report.business.NackaCCProviderReportModel;
import se.idega.idegaweb.commune.school.report.business.NackaCCTotalPlacementReportModel;
import se.idega.idegaweb.commune.school.report.business.NackaCitizenElementarySchoolPlacementReportModel;
import se.idega.idegaweb.commune.school.report.business.NackaCommuneHighSchoolPlacementReportModel;
import se.idega.idegaweb.commune.school.report.business.NackaCommuneHighSchoolStudyPathReportModel;
import se.idega.idegaweb.commune.school.report.business.NackaCompulsoryHighSchoolPlacementReportModel;
import se.idega.idegaweb.commune.school.report.business.NackaCompulsorySchoolOCCPlacementReportModel;
import se.idega.idegaweb.commune.school.report.business.NackaCompulsorySchoolPlacementReportModel;
import se.idega.idegaweb.commune.school.report.business.NackaElementarySchoolOCCPlacementReportModel;
import se.idega.idegaweb.commune.school.report.business.NackaElementarySchoolPlacementReportModel;
import se.idega.idegaweb.commune.school.report.business.NackaHighSchoolAgePlacementReportModel;
import se.idega.idegaweb.commune.school.report.business.NackaHighSchoolStudyPathPlacementReportModel;
import se.idega.idegaweb.commune.school.report.business.NackaHighSchoolYearPlacementReportModel;
import se.idega.idegaweb.commune.school.report.business.NackaPrivateHighSchoolPlacementReportModel;
import se.idega.idegaweb.commune.school.report.business.NackaPrivateSchoolPlacementReportModel;
import se.idega.idegaweb.commune.school.report.business.NackaProviderSummaryReportModel;
import se.idega.idegaweb.commune.school.report.business.ReportBusiness;
import se.idega.idegaweb.commune.school.report.business.ReportModel;
import se.idega.idegaweb.commune.school.report.business.ReportPDFWriter;
import se.idega.idegaweb.commune.school.report.business.ReportXLSWriter;

import com.idega.core.file.data.ICFile;
import com.idega.presentation.ExceptionWrapper;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Break;
import com.idega.presentation.text.Link;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;

/** 
 * This block handles selecting and presenting school reports.
 * <p>
 * Last modified: $Date: 2004/11/02 13:17:47 $ by $Author: aron $
 *
 * @author Anders Lindman
 * @version $Revision: 1.38 $
 */
public class SchoolReports extends CommuneBlock {

	public final static String IW_BUNDLE_IDENTIFIER = "se.idega.idegaweb.commune.school";

	private final static String PP = "school_report."; // Parameter prefix
	
	private final static String PARAMETER_REPORT_INDEX = PP + "report_index";
	private final static String PARAMETER_CREATE_REPORT = PP + "create_report";
	private final static String PARAMETER_CREATE_PDF = PP + "create_pdf";
	private final static String PARAMETER_CREATE_EXCEL = PP + "create_excel";
	private final static String PARAMETER_REPORT_CLASS_NAME = PP + "report_class_name";

	private final static String KP = "school_report."; // Localization key prefix
	
	private final static String KEY_REPORT_SELECTOR_TITLE = KP + "select_report";
	private final static String KEY_CREATE_REPORT = KP + "create_report";
	private final static String KEY_NO_REPORT_SELECTED = KP + "no_report_selected";
//	private final static String KEY_FOR_PRINTING = KP + "for_printing";	
	private final static String KEY_PDF = KP + "pdf";	
	private final static String KEY_EXCEL = KP + "excel";	
	private final static String KEY_BACK = KP + "back";	
	
	private final static int ACTION_DEFAULT = 1;
	private final static int ACTION_CREATE_REPORT = 2;
	private final static int ACTION_CREATE_PDF = 3;
	private final static int ACTION_CREATE_EXCEL = 4;

	private Class[] _reportModelClasses = {
				NackaCitizenElementarySchoolPlacementReportModel.class,
				NackaElementarySchoolPlacementReportModel.class,
				NackaElementarySchoolOCCPlacementReportModel.class,
				NackaCompulsorySchoolPlacementReportModel.class,
				NackaCompulsorySchoolOCCPlacementReportModel.class,
				NackaPrivateSchoolPlacementReportModel.class,
				NackaProviderSummaryReportModel.class
	};

	private Class[] _highSchoolReportModelClasses = {
				NackaHighSchoolYearPlacementReportModel.class,
				NackaHighSchoolAgePlacementReportModel.class,
				NackaCommuneHighSchoolPlacementReportModel.class,
				NackaCompulsoryHighSchoolPlacementReportModel.class,
				NackaPrivateHighSchoolPlacementReportModel.class,
				NackaHighSchoolStudyPathPlacementReportModel.class,
				NackaCommuneHighSchoolStudyPathReportModel.class,
	};

	private Class[] _publicHighSchoolReportModelClasses = {
				NackaHighSchoolYearPlacementReportModel.class,
				NackaHighSchoolAgePlacementReportModel.class,
				NackaCommuneHighSchoolPlacementReportModel.class,
				NackaCompulsoryHighSchoolPlacementReportModel.class,
				NackaPrivateHighSchoolPlacementReportModel.class,
				NackaHighSchoolStudyPathPlacementReportModel.class
	};

	private Class[] _childCareReportModelClasses = {
				NackaCCPlacementReportModel.class,
				NackaCCCommunePrivatePlacementReportModel.class,
				NackaCCProviderReportModel.class,
				NackaCCPreSchoolPlacementReportModel.class,
				NackaCC6YearPlacementReportModel.class,
				NackaCC7_9YearPlacementReportModel.class,
				NackaCC15HoursPlacementReportModel.class,
				NackaCCTotalPlacementReportModel.class,
				NackaCCHourIntervalReportModel.class
	};

	private ReportModel[] _reportModels = null;
	
	private boolean _useChildCareReports = false;
	private boolean _useHighSchoolReports = false;
	private boolean _usePublicReports = false;
	
	/**
	 * @see com.idega.presentation.PresentationObject#getBundleIdentifier()
	 */
	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}

	/**
	 * Returns true if child care reports should be used.
	 */
	public boolean getChildCare() {
		return _useChildCareReports;
	}

	/**
	 * Sets if child care reports should be used.
	 */
	public void setChildCare(boolean useChildCareReports) {
		_useChildCareReports = useChildCareReports;
	}

	/**
	 * Returns true if high school reports should be used.
	 */
	public boolean getHighSchool() {
		return _useHighSchoolReports;
	}

	/**
	 * Sets if high school reports should be used.
	 */
	public void setHighSchool(boolean useHighSchoolReports) {
		_useHighSchoolReports = useHighSchoolReports;
	}

	/**
	 * Returns true if public reports should be used.
	 */
	public boolean getPublicReports() {
		return _usePublicReports;
	}

	/**
	 * Sets if public reports should be used.
	 */
	public void setPublicReports(boolean usePublicReports) {
		_usePublicReports = usePublicReports;
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
				case ACTION_CREATE_PDF:
					handleCreatePDFAction(iwc);
					break;
				case ACTION_CREATE_EXCEL:
					handleCreateExcelAction(iwc);
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
		} else if (iwc.isParameterSet(PARAMETER_CREATE_PDF)) {
			action = ACTION_CREATE_PDF;
		} else if (iwc.isParameterSet(PARAMETER_CREATE_EXCEL)) {
			action = ACTION_CREATE_EXCEL;
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
		Class[] reportModelClasses = _reportModelClasses;
		if (_useChildCareReports) {
			reportModelClasses = _childCareReportModelClasses;
		} else if (_useHighSchoolReports) {
			reportModelClasses = _highSchoolReportModelClasses;
			if (_usePublicReports) {
				reportModelClasses = _publicHighSchoolReportModelClasses;
			}
		}
		Class reportModelClass = reportModelClasses[Integer.parseInt(reportIndex)];

//		GenericButton button = new GenericButton("", localize(KEY_FOR_PRINTING, "For printing"));
//		button = getButton(button);
//		button.setWindowToOpen(ReportWindow.class);
//		table.add(button, 1, 1);
		
		Form form = new Form();
		SubmitButton pdfButton = new SubmitButton(PARAMETER_CREATE_PDF, localize(KEY_PDF, "PDF"));
		pdfButton = (SubmitButton) getButton(pdfButton);
		table.add(pdfButton, 1, 1);
		SubmitButton xlsButton = new SubmitButton(PARAMETER_CREATE_EXCEL, localize(KEY_EXCEL, "Excel"));
		xlsButton = (SubmitButton) getButton(xlsButton);
		table.add(xlsButton, 2, 1);
		form.add(table);
		HiddenInput reportClassName = new HiddenInput(PARAMETER_REPORT_CLASS_NAME, reportModelClass.getName());
		form.add(reportClassName);		
		add(form);

		add(new Break());
		
		table = new Table();
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());
		table.add(new ReportBlock(reportModelClass), 1, 1);
		add(table);

	}

	/*
	 * Creates PDF file with link.
	 */
	private void handleCreatePDFAction(IWContext iwc) {
		Table table = new Table();
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());

		ReportModel reportModel = null;
		try {
			reportModel = (ReportModel) iwc.getSession().getAttribute(ReportBlock.PARAMETER_REPORT_MODEL);
		} catch (Exception e) {}

		try {
			if (reportModel == null) {
				String reportModelClassName = iwc.getParameter(PARAMETER_REPORT_CLASS_NAME);
				Class reportModelClass = Class.forName(reportModelClassName);
				reportModel = getReportBusiness(iwc).createReportModel(reportModelClass);
			}
			ReportPDFWriter pdfWriter = new ReportPDFWriter(reportModel, getResourceBundle());
			ICFile file = pdfWriter.createFile();
			Link iconLink = new Link(getBundle().getImage("shared/pdf.gif"));
			iconLink.setFile(file);
			table.add(iconLink, 1, 1);
			String titleKey = reportModel.getReportTitleLocalizationKey();
			String title = localize(titleKey, titleKey);
			Link link = new Link(title);
			link.setFile(file);
			table.add(link, 2, 1);
			Form form = new Form();
			SubmitButton back = new SubmitButton("", localize(KEY_BACK, KEY_BACK));
			back = (SubmitButton) getButton(back);
			form.add(back);
			table.add(form, 1, 4);
			table.mergeCells(1, 4, 2, 4);
			add(table);

		} catch (Exception e) {
			log(e);
		}
	}
	
	/*
	 * Creates Excel file with link.
	 */
	private void handleCreateExcelAction(IWContext iwc) {
		Table table = new Table();
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());

		ReportModel reportModel = null;
		try {
			reportModel = (ReportModel) iwc.getSession().getAttribute(ReportBlock.PARAMETER_REPORT_MODEL);
		} catch (Exception e) {}

		try {
			if (reportModel == null) {
				String reportModelClassName = iwc.getParameter(PARAMETER_REPORT_CLASS_NAME);
				Class reportModelClass = Class.forName(reportModelClassName);
				reportModel = getReportBusiness(iwc).createReportModel(reportModelClass);
			}
			ReportBlock rb = new ReportBlock(reportModel);
			rb.setResourceBundle(getResourceBundle());
			Table t = new Table();
			rb.buildReportTable(t);
			String key = reportModel.getReportTitleLocalizationKey();
			String reportTitle = localize(key, key);
			String filename = reportModel.getReportTitleLocalizationKey() + ".xls";
			ReportXLSWriter xlsWriter = new ReportXLSWriter(t, filename, reportTitle);
			ICFile file = xlsWriter.createFile();
			Link iconLink = new Link(getBundle().getImage("shared/xls.gif"));
			iconLink.setFile(file);
			table.add(iconLink, 1, 1);
			String titleKey = reportModel.getReportTitleLocalizationKey();
			String title = localize(titleKey, titleKey);
			Link link = new Link(title);
			link.setFile(file);
			table.add(link, 2, 1);
			Form form = new Form();
			SubmitButton back = new SubmitButton("", localize(KEY_BACK, KEY_BACK));
			back = (SubmitButton) getButton(back);
			form.add(back);
			table.add(form, 1, 4);
			table.mergeCells(1, 4, 2, 4);
			add(table);

		} catch (Exception e) {
			log(e);
		}
	}
	
	private Form getSelectorForm(IWContext iwc) {
		Form form = new Form();
		form.setTarget("_blank");
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
		Class[] reportModelClasses = _reportModelClasses;
		if (_useChildCareReports) {
			reportModelClasses = _childCareReportModelClasses;
		} else if (_useHighSchoolReports) {
			reportModelClasses = _highSchoolReportModelClasses;
			if (_usePublicReports) {
				reportModelClasses = _publicHighSchoolReportModelClasses;
			}
		}
		ReportBusiness rb = getReportBusiness(iwc);
		_reportModels = new ReportModel[reportModelClasses.length];
		for (int i = 0; i < reportModelClasses.length; i++) {
			_reportModels[i] = rb.createReportModel(reportModelClasses[i]);
		}
	}
	
	/*
	 * Returns a report business object.
	 */
	private ReportBusiness getReportBusiness(IWContext iwc) throws RemoteException {
		return (ReportBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, ReportBusiness.class);
	}	
}
