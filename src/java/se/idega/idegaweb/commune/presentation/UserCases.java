package se.idega.idegaweb.commune.presentation;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import se.cubecon.bun24.viewpoint.business.ViewpointBusiness;
import se.cubecon.bun24.viewpoint.data.Viewpoint;
import se.cubecon.bun24.viewpoint.presentation.ViewpointForm;
import se.idega.idegaweb.commune.business.CommuneCaseBusiness;
import se.idega.idegaweb.commune.school.business.SchoolChoiceBusiness;
import se.idega.idegaweb.commune.school.data.SchoolChoiceReminder;

import com.idega.block.process.business.CaseBusiness;
import com.idega.block.process.data.Case;
import com.idega.builder.data.IBPage;
import com.idega.business.IBOLookup;
import com.idega.presentation.ExceptionWrapper;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.User;

/**
 * Title: UserCases
 * Description: A class to view Cases for a User in the idegaWeb Commune application
 * Copyright:    Copyright idega Software (c) 2002
 * Company:	idega Software
 * @author <a href="mailto:tryggvi@idega.is">Tryggvi Larusson</a>
 * @version 1.0
 */
public class UserCases extends CommuneBlock {
	private final static String IW_BUNDLE_IDENTIFIER = "se.idega.idegaweb.commune";
	private final static int ACTION_VIEW_CASE_LIST = 1;
	//private final static String PARAM_CASE_ID = "USC_CASE_ID";
	private final static String PARAM_MANAGER_ID = ManagerView.PARAM_MANAGER_ID;
	private int manager_page_id = -1;
	private int viewpointPageId = -1;
	private int reminderPageId = -1;

	private boolean _showName = false;

	private int _startCase = -1;
	private int _endCase = -1;

	public final static String MYCASES_KEY = "usercases.myCases";
	public final static String MYCASES_DEFAULT = "Mina ärenden";
	public final static String CASENUMBER_KEY = "usercases.caseNumber";
	public final static String CASENUMBER_DEFAULT = "Ärendenummer";
	public final static String CONCERNING_KEY = "usercases.concerning";
	public final static String CONCERNING_DEFAULT = "Avser";
	public final static String NAME_KEY = "usercases.name";
	public final static String NAME_DEFAULT = "Namn";
	public final static String DATE_KEY = "usercases.date";
	public final static String DATE_DEFAULT = "Datum";
	public final static String MANAGER_KEY = "usercases.manager";
	public final static String MANAGER_DEFAULT = "Handläggare";
	public final static String STATUS_KEY = "usercases.status";
	public final static String STATUS_DEFAULT = "Status";
	public final static String NOONGOINGCASES_KEY = "usercases.noOngoingCases";
	public final static String NOONGOINGCASES_DEFAULT = "Inga pågående ärenden";
	public final static String UNHANDLEDCASESINMYGROUPS_KEY = "usercases.unhandledCasesInMyGroups";
	public final static String UNHANDLEDCASESINMYGROUPS_DEFAULT = "Pågående ärenden i mina grupper";
	public final static String SUBJECT_KEY = "usercases.subject";
	public final static String SUBJECT_DEFAULT = "Rubrik";
	public final static String HANDLERGROUP_KEY = "usercases.handlerGroup";
	public final static String HANDLERGROUP_DEFAULT = "Handläggargrupp";

	public final static String PARAMETER_START_CASE = "case_start_nr";
	public final static String PARAMETER_END_CASE = "case_end_nr";

	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}

	public void main(IWContext iwc) {
		//this.setResourceBundle(getResourceBundle(iwc));
		try {
			int action = parseAction(iwc);
			switch (action) {
				case ACTION_VIEW_CASE_LIST :
					viewCaseList(iwc);
					break;
			}
		}
		catch (Exception e) {
			super.add(new ExceptionWrapper(e, this));
		}
	}

	private int parseAction(IWContext iwc) {
		if (iwc.isParameterSet(PARAMETER_START_CASE))
			_startCase = Integer.parseInt(iwc.getParameter(PARAMETER_START_CASE));
		else
			_startCase = 1;

		if (iwc.isParameterSet(PARAMETER_END_CASE))
			_endCase = Integer.parseInt(iwc.getParameter(PARAMETER_END_CASE));
		else
			_endCase = 10;

		int action = ACTION_VIEW_CASE_LIST;
		return action;
	}

	private void viewCaseList(IWContext iwc) throws Exception {
		Table mainTable = new Table();
		mainTable.setCellpadding(0);
		mainTable.setCellspacing(0);
		mainTable.setWidth(getWidth());
		add(mainTable);

		if (iwc.isLoggedOn()) {
			User user = iwc.getCurrentUser();
			final int userId = ((Integer) user.getPrimaryKey()).intValue();
			List cases = new Vector(getCommuneCaseBusiness(iwc).getAllCasesDefaultVisibleForUser(user));
			Collections.reverse(cases);
			if (cases != null & !cases.isEmpty()) {
				int casesSize = cases.size();

				Table table = new Table();
				table.setCellpadding(getCellpadding());
				table.setCellspacing(getCellspacing());
				table.setWidth(Table.HUNDRED_PERCENT);
				table.setColumns(5);
				if (isShowName())
					table.setColumns(6);
				else
					table.setColumns(5);

				int row = 1;
				int column = 1;

				table.mergeCells(1, row, table.getColumns(), row);
				table.add(getNavigationTable(casesSize), column, row++);

				Form form = new Form();
				form.add(table);

				table.setRowColor(row, getHeaderColor());
				table.add(getSmallHeader(localize(CASENUMBER_KEY, CASENUMBER_DEFAULT)), column++, row);
				table.add(getSmallHeader(localize(CONCERNING_KEY, CONCERNING_DEFAULT)), column++, row);
				if (isShowName())
					table.add(getSmallHeader(localize(NAME_KEY, NAME_DEFAULT)), column++, row);
				table.add(getSmallHeader(localize(DATE_KEY, DATE_DEFAULT)), column++, row);
				table.add(getSmallHeader(localize(MANAGER_KEY, MANAGER_DEFAULT)), column++, row);
				table.add(getSmallHeader(localize(STATUS_KEY, STATUS_DEFAULT)), column, row++);

				//Collection messages = getCaseBusiness(iwc).getAllCasesForUser(iwc.getCurrentUser());

				if (_endCase > casesSize)
					_endCase = casesSize;

				for (int a = _startCase - 1; a < _endCase; a++) {
					try {
						final Case useCase = (Case) cases.get(a);
						addCaseToMessageList(iwc, userId, useCase, table, row++);
					}
					catch (Exception e) {
						add(e);
						e.printStackTrace();
					}
				}

				mainTable.add(form, 1, 1);
			}
			else {
				mainTable.add(getSmallHeader(localize(NOONGOINGCASES_KEY, NOONGOINGCASES_DEFAULT)), 1, 1);
			}

			// 1. find my groups
			final UserBusiness userBusiness = (UserBusiness) IBOLookup.getServiceInstance(iwc, UserBusiness.class);
			final Collection groupCollection
                    = userBusiness.getUserGroups(userId);
            final Group [] groups
                    = (Group[]) groupCollection.toArray(new Group[0]);

			// 2. find unhandled cases
			final SchoolChoiceBusiness schoolChoiceBusiness
                    = (SchoolChoiceBusiness) IBOLookup.getServiceInstance
                    (iwc, SchoolChoiceBusiness.class);
			final SchoolChoiceReminder [] reminders
                    = schoolChoiceBusiness.findUnhandledSchoolChoiceReminders
                    (groups);
			final ViewpointBusiness viewpointBusiness
                    = (ViewpointBusiness) IBOLookup.getServiceInstance
                    (iwc, ViewpointBusiness.class);
			final Viewpoint[] viewpoints
                    = viewpointBusiness.findUnhandledViewpointsInGroups(groups);

			// 3. display unhandled cases
			if ((viewpoints != null && viewpoints.length > 0) ||
                (reminders != null && reminders.length > 0)) {
				mainTable.setHeight(2, 12);
				mainTable.add(getLocalizedSmallHeader(UNHANDLEDCASESINMYGROUPS_KEY, UNHANDLEDCASESINMYGROUPS_DEFAULT), 1, 3);
				mainTable.setHeight(4, 6);

				Table messageList = new Table();
				messageList.setColumns(5);
				messageList.setCellpadding(getCellpadding());
				messageList.setCellspacing(getCellspacing());
				messageList.setWidth(Table.HUNDRED_PERCENT);
				int row = 1;

				Form form = new Form();
				form.add(messageList);

				messageList.setRowColor(row, getHeaderColor());
				messageList.add(getSmallHeader(localize(CASENUMBER_KEY, CASENUMBER_DEFAULT)), 1, row);
				messageList.add(getSmallHeader(localize(CONCERNING_KEY, CONCERNING_DEFAULT)), 2, row);
				messageList.add(getSmallHeader(localize(SUBJECT_KEY, SUBJECT_DEFAULT)), 3, row);
				messageList.add(getSmallHeader(localize(DATE_KEY, DATE_DEFAULT)), 4, row);
				messageList.add(getSmallHeader(localize(HANDLERGROUP_KEY, HANDLERGROUP_DEFAULT)), 5, row++);

                if (viewpoints != null) {
                    for (int i = 0; i < viewpoints.length; i++) {
                        addViewpointToMessageList(iwc, viewpoints[i],
                                                  messageList, row++);
                    }
                }
                if (reminders != null) {
                    for (int i = 0; i < reminders.length; i++) {
                        addReminderToMessageList(iwc, reminders[i], messageList,
                                                 row++);
                    }
                }
				mainTable.add(form, 1, 5);
			}
		}
	}

	private void addCaseToMessageList(final IWContext iwc, final int userId, final Case useCase, final Table messageList, int row) throws Exception {

		DateFormat dateFormat = SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT, iwc.getCurrentLocale());
		Date caseDate = new Date(useCase.getCreated().getTime());
		Text caseNumber = getSmallText(useCase.getPrimaryKey().toString());
		Text caseType = getSmallText(getCaseBusiness(iwc).getCaseBusiness(useCase.getCaseCode()).getLocalizedCaseDescription(useCase, iwc.getCurrentLocale()));

		final Text date = getSmallText(dateFormat.format(caseDate));
		Text manager = null;
		String managerName = "";
		int managerID = -1;
		try {
			final Group handler = useCase.getHandler();
			if (handler != null) {
				managerID = ((Integer) handler.getPrimaryKey()).intValue();
				if (managerID != userId) {
					managerName = getUserBusiness(iwc).getNameOfGroupOrUser(handler);
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		if (managerName == null) {
			manager = getSmallText("-");
		}
		else {
			manager = getSmallText(managerName);
			if (getManagerPage() != -1) {
				Link managerLink = getSmallLink(managerName);
				managerLink.setPage(getManagerPage());
				managerLink.addParameter(PARAM_MANAGER_ID, managerID);
				manager = managerLink;
			}
		}
		final Text caseOwnerName = getSmallText(useCase.getOwner().getFirstName());
		final Text status = getSmallText(getCaseBusiness(iwc).getLocalizedCaseStatusDescription(useCase.getCaseStatus(), iwc.getCurrentLocale()));

		if (useCase.getCode().equalsIgnoreCase(Viewpoint.CASE_CODE_KEY)) {
			final ViewpointBusiness viewpointBusiness = (ViewpointBusiness) IBOLookup.getServiceInstance(iwc, ViewpointBusiness.class);
			final Viewpoint viewpoint = viewpointBusiness.findViewpoint(Integer.parseInt(useCase.getPrimaryKey().toString()));
			caseType = getSmallText(viewpoint.getCategory());
			if (getViewpointPage() != -1) {
				final Link viewpointLink = getSmallLink(useCase.getPrimaryKey().toString());
				viewpointLink.setPage(getViewpointPage());
				viewpointLink.addParameter(ViewpointForm.PARAM_ACTION, ViewpointForm.SHOWVIEWPOINT_ACTION + "");
				viewpointLink.addParameter(ViewpointForm.PARAM_VIEWPOINT_ID, useCase.getPrimaryKey().toString());
				caseNumber = viewpointLink;
			}
		}

		if (row % 2 == 0)
			messageList.setRowColor(row, getZebraColor1());
		else
			messageList.setRowColor(row, getZebraColor2());

		int column = 1;

		messageList.add(caseNumber, column++, row);
		messageList.add(caseType, column++, row);
		if (isShowName())
			messageList.add(caseOwnerName, column++, row);
		messageList.add(date, column++, row);
		messageList.add(manager, column++, row);
		messageList.add(status, column++, row);
	}

	private void addViewpointToMessageList(final IWContext iwc, final Viewpoint viewpoint, final Table messageList, int row) throws Exception {

		final DateFormat dateFormat = SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT, iwc.getCurrentLocale());
		final Text caseDate = getSmallText(dateFormat.format(new Date(viewpoint.getCreated().getTime())));
		Text caseNumber = getSmallText(viewpoint.getPrimaryKey().toString());
		final Text category = getSmallText(viewpoint.getCategory());
		final Text subject = getSmallText(viewpoint.getSubject());
		final Group handlerGroup = viewpoint.getHandlerGroup();
		final Text group = getSmallText(handlerGroup != null ? handlerGroup.getName() : "-");

		if (getViewpointPage() != -1) {
			Link viewpointLink = getSmallLink(viewpoint.getPrimaryKey().toString());
			viewpointLink.setPage(getViewpointPage());
			viewpointLink.addParameter(ViewpointForm.PARAM_ACTION, ViewpointForm.SHOWVIEWPOINT_ACTION + "");
			viewpointLink.addParameter(ViewpointForm.PARAM_VIEWPOINT_ID, viewpoint.getPrimaryKey().toString());
			caseNumber = viewpointLink;
		}

		if (row % 2 == 0)
			messageList.setRowColor(row, getZebraColor1());
		else
			messageList.setRowColor(row, getZebraColor2());

		messageList.add(caseNumber, 1, row);
		messageList.add(category, 2, row);
		messageList.add(subject, 3, row);
		messageList.add(caseDate, 4, row);
		messageList.add(group, 5, row);
	}

	private void addReminderToMessageList(final IWContext iwc, final SchoolChoiceReminder reminder, final Table messageList, int row) throws Exception {

		final DateFormat dateFormat = SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT, iwc.getCurrentLocale());
		final Text caseDate = getSmallText(dateFormat.format(new Date(reminder.getCreated().getTime())));
		Text caseNumber = getSmallText(reminder.getPrimaryKey().toString());
		Text caseType = getSmallText(getCaseBusiness(iwc).getCaseBusiness(reminder.getCaseCode()).getLocalizedCaseDescription(reminder, iwc.getCurrentLocale()));
		final Group handlerGroup = reminder.getHandler();
		final Text group = getSmallText(handlerGroup != null ? handlerGroup.getName() : "-");

		if (getReminderPage() != -1) {
			Link reminderLink = getSmallLink(reminder.getPrimaryKey().toString());
			reminderLink.setPage(getReminderPage());
			caseNumber = reminderLink;
		}

		if (row % 2 == 0)
			messageList.setRowColor (row, getZebraColor1());
		else
			messageList.setRowColor (row, getZebraColor2());

		messageList.add (caseNumber, 1, row);
		messageList.add (caseType, 2, row);
        final String reminderText = reminder.getText ();
        messageList.add (reminderText.length () < 25 ? new Text(reminderText)
                         : new Text((reminderText.substring (0, 20) + "...")), 3, row);
		messageList.add (caseDate, 4, row);
		messageList.add (group, 5, row);
	}

	private Table getNavigationTable(int caseSize) {
		Table navigationTable = new Table(2, 1);
		navigationTable.setCellpadding(0);
		navigationTable.setCellspacing(0);
		navigationTable.setWidth(Table.HUNDRED_PERCENT);
		navigationTable.setAlignment(2, 1, Table.HORIZONTAL_ALIGN_RIGHT);

		if (_startCase > 1) {
			Link previous = getSmallLink(localize("usercases.previous", "<< previous"));
			previous.addParameter(PARAMETER_START_CASE, (_startCase - 10));
			previous.addParameter(PARAMETER_END_CASE, (_endCase - 10));
			navigationTable.add(previous, 1, 1);
		}
		else {
			Text previous = getSmallText(localize("usercases.previous", "<< previous"));
			navigationTable.add(previous, 1, 1);
		}

		if (_endCase < caseSize) {
			Link next = getSmallLink(localize("usercases.next", "next >>"));
			next.addParameter(PARAMETER_START_CASE, (_startCase + 10));
			next.addParameter(PARAMETER_END_CASE, (_endCase + 10));
			navigationTable.add(next, 2, 1);
		}
		else {
			Text next = getSmallText(localize("usercases.next", "next >>"));
			navigationTable.add(next, 2, 1);
		}

		return navigationTable;
	}

	private CaseBusiness getCaseBusiness(IWContext iwc) throws Exception {
		return (CaseBusiness) IBOLookup.getServiceInstance(iwc, CaseBusiness.class);
	}

	private CommuneCaseBusiness getCommuneCaseBusiness(IWContext iwc) throws Exception {
		return (CommuneCaseBusiness) IBOLookup.getServiceInstance(iwc, CommuneCaseBusiness.class);
	}

	private UserBusiness getUserBusiness(IWContext iwc) throws Exception {
		return (UserBusiness) IBOLookup.getServiceInstance(iwc, UserBusiness.class);
	}

	/* Commented out since it is never used...
	private Case getCase(String id, IWContext iwc) throws Exception {
		int msgId = Integer.parseInt(id);
		Case msg = getCaseBusiness(iwc).getCase(msgId);
		return msg;
	}*/

	public void setViewpointPage(final IBPage page) {
		viewpointPageId = page.getID();
	}

	public int getViewpointPage() {
		return viewpointPageId;
	}

	public void setReminderPage(final IBPage page) {
		reminderPageId = page.getID();
	}

	public int getReminderPage() {
		return reminderPageId;
	}

	public void setManagerPage(IBPage page) {
		manager_page_id = page.getID();
	}

	public void setManagerPage(int ib_page_id) {
		manager_page_id = ib_page_id;
	}

	public int getManagerPage() {
		return manager_page_id;
	}
	/**
	 * Sets the showName.
	 * @param showName The showName to set
	 */
	public void setShowName(boolean showName) {
		_showName = showName;
	}

	/**
	 * Returns the showName.
	 * @return boolean
	 */
	public boolean isShowName() {
		return _showName;
	}

}
