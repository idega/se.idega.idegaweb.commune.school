/*
 * Created on 16.10.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package se.idega.idegaweb.commune.presentation;

import java.rmi.RemoteException;
import java.text.DateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Level;
import javax.ejb.FinderException;

import se.idega.idegaweb.commune.block.pointOfView.business.PointOfViewBusiness;
import se.idega.idegaweb.commune.block.pointOfView.data.PointOfView;
import se.idega.idegaweb.commune.school.business.SchoolChoiceBusiness;
import se.idega.idegaweb.commune.school.data.SchoolChoice;
import se.idega.idegaweb.commune.school.data.SchoolChoiceReminder;
import com.idega.block.process.data.Case;
import com.idega.block.school.data.School;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
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
	
	protected int getNumberOfCases(IWContext iwc, User user) {
		try {
			Collection groups = getGroups(iwc, user.getNodeID());
			return getCommuneCaseBusiness(iwc).getCaseBusiness().getNumberOfCasesForUserAndGroupsExceptCodes(user, groups, getCommuneCaseBusiness(iwc).getUserHiddenCaseCodes());
		}
		catch (RemoteException e) {
			return 0;
		}
		catch (Exception e) {
			return 0;
		}
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
		final Collection groups = getGroups(iwc, userId);
		final Group[] groupArray = (Group[]) groups.toArray(new Group[0]);
		
		
		// 2. find unhandled cases
		final SchoolChoiceBusiness schoolChoiceBusiness = (SchoolChoiceBusiness) IBOLookup.getServiceInstance(iwc, SchoolChoiceBusiness.class);
		final SchoolChoiceReminder[] reminders = schoolChoiceBusiness.findUnhandledSchoolChoiceReminders(groupArray);
		Collection pointOfViews = null;
		try {
			PointOfViewBusiness pointOfViewBusiness = (PointOfViewBusiness) IBOLookup.getServiceInstance(iwc, PointOfViewBusiness.class);
			pointOfViews = pointOfViewBusiness.findUnhandledPointOfViewsInGroups(groups);
		}
		catch (IBOLookupException ex) {
			log(Level.INFO, "[AdminUserCases] PointOfViewBusiness is not installed");
		}
		// 3. display unhandled cases
		if (pointOfViews != null && (! pointOfViews.isEmpty()) || (reminders != null && reminders.length > 0)) {
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

			if (pointOfViews != null) {
				Iterator pointOfViewsIterator = pointOfViews.iterator();
				while (pointOfViewsIterator.hasNext()) {
					PointOfView pointOfView = (PointOfView) pointOfViewsIterator.next();
					addViewpointToMessageList(iwc, pointOfView, messageList, row++);
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
	

	private void addViewpointToMessageList(final IWContext iwc, final PointOfView pointOfView, final Table messageList, int row) throws Exception {

		final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, iwc.getCurrentLocale());
		final Text caseDate = getSmallText(dateFormat.format(new Date(pointOfView.getCreated().getTime())));
		Text caseNumber = getSmallText(pointOfView.getPrimaryKey().toString());
		final Text category = getSmallText(pointOfView.getCategory());
		final Text subject = getSmallText(pointOfView.getSubject());
		final Group handlerGroup = pointOfView.getHandlerGroup();
		final Text group = getSmallText(handlerGroup != null ? handlerGroup.getName() : "-");
		
		if (row % 2 == 0)
			messageList.setRowColor(row, getZebraColor1());
		else
			messageList.setRowColor(row, getZebraColor2());

		if (getViewpointPage() != -1) {
			try {
				PointOfViewBusiness pointOfViewBusiness = (PointOfViewBusiness) IBOLookup.getServiceInstance(iwc, PointOfViewBusiness.class);
				Link pointOfViewLink = pointOfViewBusiness.getLinkToPageForPointOfView(getViewpointPage(), pointOfView);
				caseNumber = getStyleLink(pointOfViewLink, STYLENAME_SMALL_LINK);
			}
			catch (IBOLookupException ex) {
				log(Level.INFO, "[AdminUserCases] Pointof ViewBusiness is not installed");
			}
		}
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

		final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, iwc.getCurrentLocale());
		final Text caseDate = getSmallText(dateFormat.format(new Date(reminder.getCreated().getTime())));
		Text caseNumber = getSmallText(reminder.getPrimaryKey().toString());
		Text caseType = getSmallText(getCaseBusiness(iwc).getLocalizedCaseDescription(reminder, iwc.getCurrentLocale()));
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
