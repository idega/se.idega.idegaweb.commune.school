/*
 * Created on 16.10.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package se.idega.idegaweb.commune.presentation;

import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.ejb.FinderException;

import se.cubecon.bun24.viewpoint.business.ViewpointBusiness;
import se.cubecon.bun24.viewpoint.data.Viewpoint;
import se.cubecon.bun24.viewpoint.presentation.ViewpointForm;
import se.idega.idegaweb.commune.school.business.SchoolChoiceBusiness;
import se.idega.idegaweb.commune.school.data.SchoolChoice;
import se.idega.idegaweb.commune.school.data.SchoolChoiceReminder;

import com.idega.block.process.business.CaseBusiness;
import com.idega.block.process.data.Case;
import com.idega.block.process.data.CaseCode;
import com.idega.block.school.data.School;
import com.idega.business.IBOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.user.data.Group;
import com.idega.user.data.User;

/**
 * @author Roar
 * In addition to being a regular UserCases (which displays a list of use cases), 
 * AdminUserCases views cases handled by 
 * the groups the user is member of, and shows the school provider connected to the case, if any.
 */
public class AdminUserCases extends UserCases {

	/**
	 * Adds cases and viewpoints to the table.
	 */
	protected void viewCaseList(IWContext iwc, Table mainTable) throws Exception {
		super.viewCaseList(iwc, mainTable); //This will call getCases and addCasesToMessageList declared in this class.
		User user = iwc.getCurrentUser();
		addViewPoints(iwc, mainTable, ((Integer) user.getPrimaryKey()).intValue());
	}
	
	/**
	 * Returns a list of all the cases for a user, including the cases handled by any of the users groups.
	 * The method does not return viewpoints.
	 */
	protected List getCases(IWContext iwc, User user) throws RemoteException, FinderException, Exception {
		List cases = super.getCases(iwc, user);
		
		// add cases belonging to my group 
		CaseBusiness caseBusiness = (CaseBusiness) IBOLookup.getServiceInstance(iwc, CaseBusiness.class);
		Iterator g = getGroups(iwc, user.getNodeID()).iterator();
		CaseCode [] hiddenCases = getCommuneCaseBusiness(iwc).getUserHiddenCaseCodes();
		while(g.hasNext()){
			cases.addAll(caseBusiness.getAllCasesForGroupExceptCodes((Group) g.next(), hiddenCases));
		}
				
		return cases;
	}
	
	/**
	 * Adds a case to the html table. If a school provider exist for the case, the providers name
	 * is added to a separate column.
	 */

	protected void addCaseToMessageList(final IWContext iwc, final int userId, final Case useCase, final Table messageList, int row) throws Exception {
		super.addCaseToMessageList(iwc, userId, useCase, messageList, row);
		
		//Provider column
		SchoolChoice sc = null;
		try{
			sc = getSchoolChoiceBusiness().getSchoolChoice(useCase.getNodeID());
		} catch (FinderException ex){
			//ignore
		}
		
		if (sc != null){
			School provider = sc.getChosenSchool();
			int column = getProviderColumn();
			messageList.setNoWrap(column, row);
			messageList.add(provider.getName(), column, row);
		}
	}
	
	/**
	 * @return position of the provider column in the html table, counting from 1.
	 */
	int getProviderColumn(){
		return super.getManagerColumn() + 1;
	}	
	
	/**
	 * @return position of the status column in the html table, counting from 1.
	 */
	int getStatusColumn(){
		return super.getStatusColumn() + 1;
	}	
	
	/**
	 * 
	 * @return SchoolChoiceBusiness
	 * @throws RemoteException
	 */
	public SchoolChoiceBusiness getSchoolChoiceBusiness() throws RemoteException {
		return (SchoolChoiceBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), SchoolChoiceBusiness.class);	
	}		
	
	/**
	 * Adds the header labels to the html table. 
	 * @return row index after header row. 
	 */
	protected int addTableHeader(Table table, int row) {
		int ret = super.addTableHeader(table, row);
		table.add(getSmallHeader(localize("usercases.caseProvider", "Provider")), getProviderColumn(), row);
		return ret;
	}
		
		/**
		 * Adds viewpoints to the html table, if any exists.
		 * @param iwc
		 * @param mainTable
		 * @param userId
		 * @throws RemoteException
		 * @throws FinderException
		 * @throws Exception
		 */
	private void addViewPoints(IWContext iwc, Table mainTable, final int userId)
		throws RemoteException, FinderException, Exception {
		// 1. find my groups			
		final Collection groupCollection = getGroups(iwc, userId);
		final Group[] groups = (Group[]) groupCollection.toArray(new Group[0]);
		
		
		// 2. find unhandled cases
		final SchoolChoiceBusiness schoolChoiceBusiness = (SchoolChoiceBusiness) IBOLookup.getServiceInstance(iwc, SchoolChoiceBusiness.class);
		final SchoolChoiceReminder[] reminders = schoolChoiceBusiness.findUnhandledSchoolChoiceReminders(groups);
		final ViewpointBusiness viewpointBusiness = (ViewpointBusiness) IBOLookup.getServiceInstance(iwc, ViewpointBusiness.class);
		final Viewpoint[] viewpoints = viewpointBusiness.findUnhandledViewpointsInGroups(groups);
		
		// 3. display unhandled cases
		if ((viewpoints != null && viewpoints.length > 0) || (reminders != null && reminders.length > 0)) {
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
					addViewpointToMessageList(iwc, viewpoints[i], messageList, row++);
				}
			}
			if (reminders != null) {
				for (int i = 0; i < reminders.length; i++) {
					addReminderToMessageList(iwc, reminders[i], messageList, row++);
				}
			}
			mainTable.add(form, 1, 5);
		}
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
		if (getDateWidth() != null){
			messageList.setWidth(4, getDateWidth());
		}
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
			messageList.setRowColor(row, getZebraColor1());
		else
			messageList.setRowColor(row, getZebraColor2());

		messageList.setNoWrap(1, row);
		messageList.add(caseNumber, 1, row);

		messageList.add(caseType, 2, row);

		final String reminderText = reminder.getText();
		messageList.setNoWrap(3, row);
		messageList.add(reminderText.length() < 25 ? new Text(reminderText) : new Text((reminderText.substring(0, 20) + "...")), 3, row);

		if (getDateWidth() != null){
			messageList.setWidth(4, getDateWidth());
		}
		messageList.setNoWrap(4, row);
		messageList.add(caseDate, 4, row);

		messageList.setNoWrap(5, row);
		messageList.add(group, 5, row);
	}	

}
