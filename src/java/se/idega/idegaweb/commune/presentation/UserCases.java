package se.idega.idegaweb.commune.presentation;

import java.rmi.RemoteException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.ejb.FinderException;

import se.cubecon.bun24.viewpoint.business.ViewpointBusiness;
import se.cubecon.bun24.viewpoint.data.Viewpoint;
import se.cubecon.bun24.viewpoint.presentation.ViewpointForm;
import se.idega.idegaweb.commune.business.CommuneCaseBusiness;
import se.idega.idegaweb.commune.childcare.data.AfterSchoolChoiceBMPBean;
import se.idega.idegaweb.commune.school.business.SchoolChoiceBusiness;
import se.idega.idegaweb.commune.school.business.SchoolChoiceBusinessBean;
import se.idega.idegaweb.commune.school.data.SchoolChoice;
import se.idega.idegaweb.commune.school.data.SchoolChoiceBMPBean;

import com.idega.block.process.business.CaseBusiness;
import com.idega.block.process.data.Case;
import com.idega.block.process.data.CaseStatus;
import com.idega.business.IBOLookup;
import com.idega.core.builder.data.ICPage;
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
	private String _dateWidth;
	private final static String IW_BUNDLE_IDENTIFIER = "se.idega.idegaweb.commune";
	private final static int ACTION_VIEW_CASE_LIST = 1;
	//private final static String PARAM_CASE_ID = "USC_CASE_ID";
	private final static String PARAM_MANAGER_ID = ManagerView.PARAM_MANAGER_ID;
	private int manager_page_id = -1;
	private int viewpointPageId = -1;
	private int reminderPageId = -1;

	private boolean _showName = false;
	private boolean _showStatusAfterschoolCare = true;

	private int _startCase = -1;
	private int _numberOfCases = 10;

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
					viewCaseList(iwc, new Table());
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
			_startCase = 0;

		int action = ACTION_VIEW_CASE_LIST;
		return action;
	}

	protected void viewCaseList(IWContext iwc, Table mainTable) throws Exception {
		mainTable.setCellpadding(0);
		mainTable.setCellspacing(0);
		mainTable.setWidth(getWidth());
		add(mainTable); 
		
		
		if (iwc.isLoggedOn()) {
		
						
			User user = iwc.getCurrentUser();
			final int userId = ((Integer) user.getPrimaryKey()).intValue();
			
			//Finding cases
			List cases = getCases(iwc, user, _startCase, _numberOfCases);
			int numberOfCases = getNumberOfCases(iwc, user);
					
			if (cases != null & !cases.isEmpty()) {
				Table table = new Table();
				table.setCellpadding(getCellpadding());
				table.setCellspacing(getCellspacing());
				table.setWidth(Table.HUNDRED_PERCENT);
				table.setColumns(getLastColumn());

				table.mergeCells(1, 1, table.getColumns(), 1);
				table.add(getNavigationTable(numberOfCases), 1, 1);

				Form form = new Form();
				form.add(table);

				int row = addTableHeader(table, 2); 

				Iterator iter = cases.iterator();
				while (iter.hasNext()) {
					try {
						final Case useCase = (Case) iter.next();
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


		}
	}


	/**
	 * @param iwc
	 * @param user
	 * @return
	 */
	protected int getNumberOfCases(IWContext iwc, User user) {
		try {
			return getCommuneCaseBusiness(iwc).getCaseBusiness().getNumberOfCasesForUserExceptCodes(user, getCommuneCaseBusiness(iwc).getUserHiddenCaseCodes());
		}
		catch (RemoteException e) {
			return 0;
		}
		catch (Exception e) {
			return 0;
		}
	}

	protected int addTableHeader(Table table, int row) {
		table.setRowColor(row, getHeaderColor());
		table.add(getSmallHeader(localize(CASENUMBER_KEY, CASENUMBER_DEFAULT)), getNumberColumn(), row);
		table.add(getSmallHeader(localize(CONCERNING_KEY, CONCERNING_DEFAULT)), getTypeColumn(), row);
		if (isShowName()){
			table.add(getSmallHeader(localize(NAME_KEY, NAME_DEFAULT)), getNameColumn(), row);
		}
		table.add(getSmallHeader(localize(DATE_KEY, DATE_DEFAULT)), getDateColumn(), row);
		table.add(getSmallHeader(localize(MANAGER_KEY, MANAGER_DEFAULT)), getManagerColumn(), row);
		table.add(getSmallHeader(localize(STATUS_KEY, STATUS_DEFAULT)), getStatusColumn(), row);
		return row + 1;
	}

	protected List getCases(IWContext iwc, User user, int startingCase, int numberOfCases) throws RemoteException, FinderException, Exception {
		return new Vector(getCommuneCaseBusiness(iwc).getAllCasesDefaultVisibleForUser(user, startingCase, numberOfCases));
	}

	protected Collection getGroups(IWContext iwc, final int userId)	throws RemoteException {
		UserBusiness userBusiness = (UserBusiness) IBOLookup.getServiceInstance(iwc, UserBusiness.class);
		Collection groupCollection = userBusiness.getUserGroups(userId);
		if (groupCollection == null){
			groupCollection = new ArrayList(); //empty collection
		}
		return groupCollection;
	}

	protected void addCaseToMessageList(final IWContext iwc, final int userId, final Case useCase, final Table messageList, int row) throws Exception {

		DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, iwc.getCurrentLocale());
		Date caseDate = new Date(useCase.getCreated().getTime());
		Text caseNumber = getSmallText(useCase.getPrimaryKey().toString());
		CaseBusiness caseBusiness = getCaseBusiness(iwc).getCaseBusiness(useCase.getCaseCode());
		
		
		
		String localizedCaseDescription = null;
		try{
			localizedCaseDescription = caseBusiness.getLocalizedCaseDescription(useCase, iwc.getCurrentLocale());
		} catch(Exception ex){
			localizedCaseDescription =localize("usercases.localised_description_missing","[ERROR: Localized description missing]");
		}
		
		Text caseType = getSmallText(localizedCaseDescription);

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
		
		String caseCode = useCase.getCode(); //Malin
		
		String caseCodeAS = new AfterSchoolChoiceBMPBean().getCaseCodeKey();
		String caseCodeSc = new SchoolChoiceBMPBean().getCaseCodeKey();
		
		//String caseStatusOpen = caseBusiness.getCaseStatusOpen().toString();
		CaseStatus caseStatusOpen = caseBusiness.getCaseStatusOpen();
		CaseStatus caseStatusPlaced = caseBusiness.getCaseStatusPlaced();
		SchoolChoiceBusiness schBuiz;
		schBuiz = (SchoolChoiceBusiness) IBOLookup.getServiceInstance(iwc, SchoolChoiceBusiness.class);
		
		
		final Text status;
		
			if (caseCode.equals(caseCodeAS) && !getShowStatusAfterSchoolCare()) {
				status = getSmallText("-");
			}
			else if (caseCode.equals(caseCodeSc) && useCase.getCaseStatus().equals(caseStatusPlaced)) {
				SchoolChoice choice = schBuiz.getSchoolChoice(((Integer) useCase.getPrimaryKey()).intValue());
				if (choice != null && !choice.getHasReceivedPlacementMessage())
					status = getSmallText(getCaseBusiness(iwc).getLocalizedCaseStatusDescription(caseStatusOpen, iwc.getCurrentLocale()));
				else
					status = getSmallText(getCaseBusiness(iwc).getLocalizedCaseStatusDescription(useCase.getCaseStatus(), iwc.getCurrentLocale()));
				
			}
			else {			
					status = getSmallText(getCaseBusiness(iwc).getLocalizedCaseStatusDescription(useCase.getCaseStatus(), iwc.getCurrentLocale()));
			}
		
		
			
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

		if (row % 2 == 0) {
			messageList.setRowColor(row, getZebraColor1()); 
		} else {
			messageList.setRowColor(row, getZebraColor2());
		}

		int column = getNumberColumn();

		messageList.setNoWrap(column, row);
		messageList.add(caseNumber, column, row);

		column = getTypeColumn();
		messageList.add(caseType, column, row);
		if (isShowName()) {
			Text caseOwnerName = new Text(localize("usercases.owner_not_available","No name"));
			try{
				User owner = useCase.getOwner();
				caseOwnerName = getSmallText(owner.getFirstName() + " " + owner.getLastName());
			} catch (NullPointerException ex){
				ex.printStackTrace();
			}
						
			column = getNameColumn();			
			messageList.setNoWrap(column, row);
			messageList.add(caseOwnerName, column, row);
		}

		column = getDateColumn();
		if (_dateWidth != null) {
			messageList.setWidth(column, _dateWidth);
		}
		messageList.setNoWrap(column, row);
		messageList.add(date, column, row);

		column = getManagerColumn();
		messageList.setNoWrap(column, row);
		messageList.add(manager, column, row);

		column = getStatusColumn();
		messageList.setNoWrap(column, row);
		messageList.add(status, column, row);
	}
	
	int getNumberColumn(){
		return 1;
	}
	int getTypeColumn(){
		return 2;
	}		
	int getNameColumn(){
		return 3;
	}	
	int getDateColumn(){
		return (isShowName() ? getNameColumn() : getTypeColumn()) + 1;
	}	
	int getManagerColumn(){
		return getDateColumn() + 1;
	}	
	int getStatusColumn(){
		return getDateColumn() + 2;
	}	
	int getLastColumn(){
		return getStatusColumn();
	}		
	


	private Table getNavigationTable(int caseSize) {
		Table navigationTable = new Table(2, 1);
		navigationTable.setCellpadding(0);
		navigationTable.setCellspacing(0);
		navigationTable.setWidth(Table.HUNDRED_PERCENT);
		navigationTable.setAlignment(2, 1, Table.HORIZONTAL_ALIGN_RIGHT);

		if (_startCase > 1) {
			Link previous = getSmallLink(localize("usercases.previous", "<< previous"));
			previous.addParameter(PARAMETER_START_CASE, (_startCase - _numberOfCases));
			navigationTable.add(previous, 1, 1);
		}
		else {
			Text previous = getSmallText(localize("usercases.previous", "<< previous"));
			navigationTable.add(previous, 1, 1);
		}

		if (_startCase + _numberOfCases <= caseSize) {
			Link next = getSmallLink(localize("usercases.next", "next >>"));
			next.addParameter(PARAMETER_START_CASE, (_startCase + _numberOfCases));
			navigationTable.add(next, 2, 1);
		}
		else {
			Text next = getSmallText(localize("usercases.next", "next >>"));
			navigationTable.add(next, 2, 1);
		}

		return navigationTable;
	}

	protected CaseBusiness getCaseBusiness(IWContext iwc) throws Exception {
		return (CaseBusiness) IBOLookup.getServiceInstance(iwc, CaseBusiness.class);
	}

	protected CommuneCaseBusiness getCommuneCaseBusiness(IWContext iwc) throws Exception {
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

	public void setViewpointPage(final ICPage page) {
		viewpointPageId = ((Integer)page.getPrimaryKey()).intValue();
	}

	public int getViewpointPage() {
		return viewpointPageId;
	}

	public void setReminderPage(final ICPage page) {
		reminderPageId = ((Integer)page.getPrimaryKey()).intValue();
	}

	public int getReminderPage() {
		return reminderPageId;
	}

	public void setManagerPage(ICPage page) {
		manager_page_id = ((Integer)page.getPrimaryKey()).intValue();
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

	/**
	 * @param width
	 */
	public void setDateWidth(String width) {
		_dateWidth = width;
	}
	
	public String getDateWidth() {
		return _dateWidth;
	}	

	/**
	 * @param width
	 */
	public void setDateWidth(int width) {
		setDateWidth(String.valueOf(width));
	}
	
	public void setShowStatusAfterSchoolCare(boolean showStatusAfterschoolCare) {
		_showStatusAfterschoolCare = showStatusAfterschoolCare;
	}

	/**
	 * Returns the showStatusAfterschoolCare.
	 * @return boolean
	 */
	public boolean getShowStatusAfterSchoolCare() {
		return _showStatusAfterschoolCare;
	}
	
}