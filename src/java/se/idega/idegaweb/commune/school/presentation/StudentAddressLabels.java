/*
 * $Id: StudentAddressLabels.java,v 1.3 2004/10/20 15:46:54 aron Exp $
 *
 * Copyright (C) 2003 Agura IT. All Rights Reserved.
 *
 * This software is the proprietary information of Agura IT AB.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.school.presentation;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import se.idega.idegaweb.commune.school.business.StudentAddressLabelsWriter;
import se.idega.idegaweb.commune.school.event.SchoolEventListener;
import se.idega.idegaweb.commune.school.presentation.inputhandler.SchoolGroupHandler;

import com.idega.block.school.business.SchoolYearComparator;
import com.idega.block.school.data.SchoolYear;
import com.idega.core.file.data.ICFile;
import com.idega.data.IDORelationshipException;
import com.idega.presentation.ExceptionWrapper;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.DownloadLink;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.GenericButton;
import com.idega.presentation.ui.SubmitButton;

/** 
 * This idegaWeb block generates PDF files with student's addresses.
 * <p>
 * Last modified: $Date: 2004/10/20 15:46:54 $ by $Author: aron $
 *
 * @author Anders Lindman
 * @version $Revision: 1.3 $
 */
public class StudentAddressLabels extends SchoolCommuneBlock {

	private final static int ACTION_DEFAULT = 0;
	private final static int ACTION_SELECT_SCHOOL_CLASSES = 1;
	private final static int ACTION_CREATE_ADDRESS_LABELS = 2;

	private final static String PP = "sal_"; // Parameter prefix
	
	private final static String PARAMETER_SELECT_SCHOOL_CLASSES = PP + "select_school_classes";
	private final static String PARAMETER_SCHOOL_CLASS_IDS = PP + "school_class_ids";	
	private final static String PARAMETER_CREATE_ADDRESS_LABELS = PP + "create_address_labels";
	
	private final static String KP = "sal."; // Key prefix
	
	private final static String KEY_STUDENT_ADDRESS_LABELS = KP + "student_address_labels";
	private final static String KEY_BACK = KP + "back";
	private final static String KEY_NO_STUDENTS = KP + "no_students";
	
	/**
	 * @see se.idega.idegaweb.commune.school.presentation.SchoolCommuneBlock#init(com.idega.presentation.IWContext)
	 */
	public void init(IWContext iwc) throws Exception {
		try {
			int action = parseAction(iwc);
			switch (action) {
				case ACTION_DEFAULT:
					handleDefaultAction();
					break;
				case ACTION_SELECT_SCHOOL_CLASSES:
					handleSelectSchoolClassesAction();
					break;
				case ACTION_CREATE_ADDRESS_LABELS:
					handleCreateAddressLabelsAction(iwc);
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

		if (iwc.isParameterSet(PARAMETER_SELECT_SCHOOL_CLASSES)) {
			action = ACTION_SELECT_SCHOOL_CLASSES;
		} else if (iwc.isParameterSet(PARAMETER_CREATE_ADDRESS_LABELS)) {
			action = ACTION_CREATE_ADDRESS_LABELS;
		}

		return action;
	}
	
	/*
	 * Handles the default action by displaying a form with season and year inputs. 
	 */
	private void handleDefaultAction() throws RemoteException {
		Form form = new Form();
		form.setEventListener(SchoolEventListener.class);

		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(getWidth());
		form.add(table);

		int row = 1;
		
		List schoolYears = null;
		try {
			schoolYears = new ArrayList(getSession().getSchool().findRelatedSchoolYears());
		}
		catch (IDORelationshipException e1) {
			schoolYears = new ArrayList();
		}
		if (!schoolYears.isEmpty()) {
			Collections.sort(schoolYears, new SchoolYearComparator());
		}
		
		table.add(getSmallHeader(localize("school.school_season", "Season") + ":"), 1, row);
		table.add(Text.getNonBrakingSpace(), 1, row);
		table.add(getSchoolSeasons(false), 1, row++);
		table.setHeight(row++, 8);
		
		table.add(getSmallHeader(localize("school.school_years", "Years") + ":"), 1, row++);
		table.add(getSmallHeader("&nbsp;"), 1, row++);
		Iterator iter = schoolYears.iterator();
		while (iter.hasNext()) {
			SchoolYear year = (SchoolYear) iter.next();
			CheckBox box = getCheckBox(getSession().getParameterSchoolGroupIDs(), year.getPrimaryKey().toString());
			
			table.setCellpadding(1, row, 2);
			table.add(box, 1, row);
			table.add(Text.getNonBrakingSpace(), 1, row);
			table.add(getSmallText(year.getSchoolYearName()), 1, row++);
		}
		table.setHeight(row++, 15);
		
		SubmitButton button = (SubmitButton) getButton(new SubmitButton(PARAMETER_SELECT_SCHOOL_CLASSES, localize("school.continue", "Continue")));
		button.setToEnableWhenChecked(getSession().getParameterSchoolGroupIDs());
		
		if (getBackPage() != null) {
			GenericButton back = getButton(new GenericButton("back", localize("back", "Back")));
			back.setPageToOpen(getBackPage());
			
			table.add(back, 1, row);
			table.add(Text.getNonBrakingSpace(), 1, row);
		}
		table.add(button, 1, row++);

		add(form);
	}

	/*
	 * Views a form with selector for school classes. 
	 */
	private void handleSelectSchoolClassesAction() {
		Form form = new Form();
		SchoolGroupHandler sgh = new SchoolGroupHandler();
		sgh.setName(PARAMETER_SCHOOL_CLASS_IDS);
		sgh = (SchoolGroupHandler) getStyledInterface(sgh);
		form.add(sgh);

		form.add(Text.getBreak());
		form.add(Text.getBreak());
		
		SubmitButton button = (SubmitButton) getButton(new SubmitButton(PARAMETER_CREATE_ADDRESS_LABELS, localize("school.continue", "Continue")));
//		button.setToEnableWhenSelected(PARAMETER_SCHOOL_CLASS_IDS);
		form.add(button);
		
		add(form);
	}

	/*
	 * Creates a PDF file with student address labels with a link. 
	 */
	private void handleCreateAddressLabelsAction(IWContext iwc) {
		String[] schoolClassIds = iwc.getParameterValues(PARAMETER_SCHOOL_CLASS_IDS);
		Table table = new Table();
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());

		try {
			StudentAddressLabelsWriter pdfWriter = new StudentAddressLabelsWriter();
			ICFile file = pdfWriter.createFile(schoolClassIds, iwc.getIWMainApplication());
			DownloadLink iconLink = new DownloadLink(getBundle().getImage("shared/pdf.gif"));
			//Link iconLink = new Link(getBundle().getImage("shared/pdf.gif"));
			iconLink.setFile(file);
			table.add(iconLink, 1, 1);
			String title = localize(KEY_STUDENT_ADDRESS_LABELS, "Student address labels");
			//Link link = new Link(title);
			DownloadLink link = new DownloadLink(title);
			link.setFile(file);
			table.add(link, 2, 1);
		} catch (Exception e) {
			table.add(getErrorText(localize(KEY_NO_STUDENTS, "No students.")), 1, 1);
		}		
		Form form = new Form();
		SubmitButton back = new SubmitButton("", localize(KEY_BACK, KEY_BACK));
		back = (SubmitButton) getButton(back);
		form.add(back);
		table.add(form, 1, 4);
		table.mergeCells(1, 4, 2, 4);
		add(table);
	}
}
