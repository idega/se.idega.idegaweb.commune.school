package se.idega.idegaweb.commune.presentation;

import java.rmi.RemoteException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import javax.ejb.FinderException;
import se.cubecon.bun24.viewpoint.business.ViewpointBusiness;
import se.cubecon.bun24.viewpoint.data.Viewpoint;
import se.idega.idegaweb.commune.business.CommuneCaseBusiness;
import se.idega.idegaweb.commune.care.data.AfterSchoolChoiceBMPBean;
import se.idega.idegaweb.commune.school.business.SchoolCaseBusiness;
import com.idega.block.process.business.CaseBusiness;
import com.idega.block.process.business.CaseCodeManager;
import com.idega.block.process.data.Case;
import com.idega.block.process.data.CaseStatus;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.core.builder.data.ICPage;
import com.idega.idegaweb.IWUserContext;
import com.idega.presentation.CollectionNavigator;
import com.idega.presentation.ExceptionWrapper;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.repository.data.ImplementorRepository;
import com.idega.user.business.UserBusiness;
import com.idega.user.business.UserSession;
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

	private Map pageMap;

	private boolean _showName = false;
	private boolean _showManager = true;
	private boolean _showStatusAfterschoolCare = true;
	private boolean useStyleNames = false;
	//private boolean useBullets = false;
	private int firstColumnPadding = 12;

	private int _startCase = -1;
	private int _numberOfCases = 10;

	public final static String MYCASES_KEY = "usercases.myCases";
	public final static String MYCASES_DEFAULT = "Mina �renden";
	public final static String CASENUMBER_KEY = "usercases.caseNumber";
	public final static String CASENUMBER_DEFAULT = "�rendenummer";
	public final static String CONCERNING_KEY = "usercases.concerning";
	public final static String CONCERNING_DEFAULT = "Avser";
	public final static String NAME_KEY = "usercases.name";
	public final static String NAME_DEFAULT = "Namn";
	public final static String DATE_KEY = "usercases.date";
	public final static String DATE_DEFAULT = "Datum";
	public final static String MANAGER_KEY = "usercases.manager";
	public final static String MANAGER_DEFAULT = "Handl�ggare";
	public final static String STATUS_KEY = "usercases.status";
	public final static String STATUS_DEFAULT = "Status";
	public final static String NOONGOINGCASES_KEY = "usercases.noOngoingCases";
	public final static String NOONGOINGCASES_DEFAULT = "Inga p�g�ende �renden";
	public final static String UNHANDLEDCASESINMYGROUPS_KEY = "usercases.unhandledCasesInMyGroups";
	public final static String UNHANDLEDCASESINMYGROUPS_DEFAULT = "P�g�ende �renden i mina grupper";
	public final static String SUBJECT_KEY = "usercases.subject";
	public final static String SUBJECT_DEFAULT = "Rubrik";
	public final static String HANDLERGROUP_KEY = "usercases.handlerGroup";
	public final static String HANDLERGROUP_DEFAULT = "Handl�ggargrupp";

	public final static String PARAMETER_START_CASE = "case_start_nr";
	public final static String PARAMETER_END_CASE = "case_end_nr";
	
	private boolean iUseUserInSession = false;
	
	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}

	public void main(IWContext iwc) {
		//this.setResourceBundle(getResourceBundle(iwc));
		try {
			int action = parseAction();
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

	private int parseAction() {
		int action = ACTION_VIEW_CASE_LIST;
		return action;
	}

	protected UserSession getUserSession(IWUserContext iwuc) {
		try {
			return (UserSession) IBOLookup.getSessionInstance(iwuc, UserSession.class);
		}
		catch (IBOLookupException e) {
			throw new IBORuntimeException(e);
		}
	}
	
	protected void viewCaseList(IWContext iwc, Table mainTable) throws Exception {
		mainTable.setCellpadding(0);
		mainTable.setCellspacing(0);
		mainTable.setWidth(getWidth());
		add(mainTable); 
		
		boolean showList = false;
		if (iUseUserInSession) {
			showList = getUserSession(iwc).getUser() != null;
		}
		else {
			showList = iwc.isLoggedOn();
		}
		
		if (showList) {
		
			User user = null;
			if (iUseUserInSession) {
				user = getUserSession(iwc).getUser();
			}
			else {
				user = iwc.getCurrentUser();
			}
			final int userId = ((Integer) user.getPrimaryKey()).intValue();
			
			CollectionNavigator navigator = getNavigator(iwc, user);

			//Finding cases
			List cases = getCases(iwc, user, _startCase, _numberOfCases);
					
			if (cases != null & !cases.isEmpty()) {
				Table table = new Table();
				table.setCellpadding(getCellpadding());
				table.setCellspacing(getCellspacing());
				table.setWidth(Table.HUNDRED_PERCENT);
				table.setColumns(getLastColumn());

				if (useStyleNames) {
					table.setRowStyleClass(1, getHeadingRowClass());
					table.mergeCells(1, 1, table.getColumns() - 2, 1);
					table.add(localize("case.cases", "Cases"), 1, 1);
					table.mergeCells(table.getColumns() - 1, 1, table.getColumns(), 1);
					table.setAlignment(table.getColumns() - 1, 1, Table.HORIZONTAL_ALIGN_RIGHT);

					navigator.setUseShortText(true);
					navigator.setWidth(50);
					navigator.setLinkStyle(getStyleName(STYLENAME_SMALL_HEADER_LINK));
					navigator.setTextStyle(getStyleName(STYLENAME_SMALL_HEADER));
					table.add(navigator, table.getColumns() - 1, 1);
				}
				else {
					table.mergeCells(1, 1, table.getColumns(), 1);
					table.add(navigator, 1, 1);
				}

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
						row--;
					}
				}
				
				mainTable.add(form, 1, 1);
			}
			else {
				if (useStyleNames) { 
					mainTable.setCellpaddingLeft(1, 1, firstColumnPadding);
				}
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
		if (useStyleNames) {
			table.setAlignment(getNumberColumn(), row, Table.HORIZONTAL_ALIGN_CENTER);
			table.setCellpaddingLeft(getNumberColumn(), row, firstColumnPadding);
			table.setRowStyleClass(row, getStyleName(STYLENAME_HEADER_ROW));
		}
		else {
			table.setRowColor(row, getHeaderColor());
		}
		table.add(getSmallHeader(localize(CASENUMBER_KEY, CASENUMBER_DEFAULT)), getNumberColumn(), row);
		table.add(getSmallHeader(localize(CONCERNING_KEY, CONCERNING_DEFAULT)), getTypeColumn(), row);
		if (isShowName()){
			table.add(getSmallHeader(localize(NAME_KEY, NAME_DEFAULT)), getNameColumn(), row);
		}
		table.add(getSmallHeader(localize(DATE_KEY, DATE_DEFAULT)), getDateColumn(), row);
		if (_showManager) {
			table.add(getSmallHeader(localize(MANAGER_KEY, MANAGER_DEFAULT)), getManagerColumn(), row);
		}
		table.add(getSmallHeader(localize(STATUS_KEY, STATUS_DEFAULT)), getStatusColumn(), row);
		return row + 1;
	}

	protected ICPage getPage(String caseCode, String caseStatus) {
		if (pageMap != null) {
			Object object = pageMap.get(caseCode);
			if (object != null) {
				if (object instanceof ICPage) {
					return (ICPage) object;
				}
				else if (object instanceof Map) {
					Map statusMap = (Map) object;
					return (ICPage) statusMap.get(caseStatus);
				}
			}
		}
		return null;
	}
	
	public void setPage(String caseCode, String caseStatus, ICPage page) {
		if (pageMap == null) {
			pageMap = new HashMap();
		}
		
		Map statusMap = (Map) pageMap.get(caseCode);
		if (statusMap == null) {
			statusMap = new HashMap();
		}
		statusMap.put(caseStatus, page);
		pageMap.put(caseCode, statusMap);
	}
	
	public void setPage(String caseCode, ICPage page) {
		if (pageMap == null) {
			pageMap = new HashMap();
		}
		
		pageMap.put(caseCode, page);
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
		Text caseNumber = getSmallHeader(useCase.getPrimaryKey().toString());
		CaseBusiness caseBusiness = CaseCodeManager.getInstance().getCaseBusinessOrDefault(useCase.getCaseCode(), iwc);
		
		
		
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
		
		String caseCode = useCase.getCode();
		CaseStatus caseStatus = useCase.getCaseStatus();
		
		CaseStatus caseStatusOpen = caseBusiness.getCaseStatusOpen();
		CaseStatus caseStatusPlaced = caseBusiness.getCaseStatusPlaced();
		
		PresentationObject status = null;
		
		// special case at the beginning
		String caseCodeAS = new AfterSchoolChoiceBMPBean().getCaseCodeKey();
		if (caseCode.equals(caseCodeAS) && !getShowStatusAfterSchoolCare()) {
			status = getSmallText("-");
		}
		else {
			List list = ImplementorRepository.getInstance().newInstances(SchoolCaseBusiness.class, this.getClass());
			Iterator iterator = list.iterator();
			while (iterator.hasNext() && status == null) {
				SchoolCaseBusiness schoolCaseBusiness = (SchoolCaseBusiness) iterator.next();
				if (schoolCaseBusiness.isCase(useCase) && useCase.getCaseStatus().equals(caseStatusPlaced)) {
					if (schoolCaseBusiness.caseIsOpen(useCase, iwc)) {
						status = getStatus(iwc, caseStatusOpen);
					}
					else {
						status = getStatus(iwc, caseStatus);
					}
				}
			}
		}
		// nothing was set yet
		if (status == null) {
			status = getStatus(iwc, caseStatus);
		}
		
		try {
			ViewpointBusiness viewpointBusiness = (ViewpointBusiness) IBOLookup.getServiceInstance(iwc, ViewpointBusiness.class);
			String caseCodeKeyViewpoint = viewpointBusiness.getCaseCodeKeyForViewpoint();
			if (useCase.getCode().equalsIgnoreCase(caseCodeKeyViewpoint)) {
				Viewpoint viewpoint = viewpointBusiness.findViewpoint(Integer.parseInt(useCase.getPrimaryKey().toString()));
				caseType = getSmallText(viewpoint.getCategory());
				if (getViewpointPage() != -1) {
					Link viewpointLink = viewpointBusiness.getLinkToPageForViewpoint(getViewpointPage(), viewpoint);
					caseNumber = getStyleLink(new Link(viewpointLink), STYLENAME_SMALL_LINK);
				}
			}
		}
		catch (IBOLookupException ex) {
			log(Level.INFO, "[UserCases] PointOfViewBusiness is not installed");
			// nothing to do, sometimes point of view bundle is not installed
		}

		if (useStyleNames) {
			messageList.setAlignment(getNumberColumn(), row, Table.HORIZONTAL_ALIGN_CENTER);
			messageList.setCellpaddingLeft(getNumberColumn(), row, firstColumnPadding);
			if (row % 2 == 0) {
				messageList.setRowStyleClass(row, getStyleName(STYLENAME_LIGHT_ROW));
			}
			else {
				messageList.setRowStyleClass(row, getStyleName(STYLENAME_DARK_ROW));
			}
		}
		else {
			if (row % 2 == 0) {
				messageList.setRowColor(row, getZebraColor1()); 
			} else {
				messageList.setRowColor(row, getZebraColor2());
			}
		}

		int column = getNumberColumn();

		messageList.setNoWrap(column, row);
		ICPage page = getPage(caseCode, caseStatus.getStatus());
		if (page != null) {
			Link link = getSmallLink(useCase.getPrimaryKey().toString());
			String parameter = caseBusiness.getPrimaryKeyParameter();
			if (parameter != null) {
				link.addParameter(parameter, useCase.getPrimaryKey().toString());
			}
			Class eventListener = caseBusiness.getEventListener();
			if (eventListener != null) {
				link.setEventListener(eventListener);
			}
			Map parameters = caseBusiness.getCaseParameters(useCase);
			if (parameters != null) {
				link.setParameter(parameters);
			}
			link.setPage(page);
			messageList.add(link, column, row);
		}
		else {
			messageList.add(caseNumber, column, row);
		}

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

		if (_showManager) {
			column = getManagerColumn();
			messageList.setNoWrap(column, row);
			messageList.add(manager, column, row);
		}

		column = getStatusColumn();
		messageList.setNoWrap(column, row);
		messageList.add(status, column, row);
	}
	
	protected PresentationObject getStatus(IWContext iwc, CaseStatus status) throws Exception {
		return getSmallText(getCaseBusiness(iwc).getLocalizedCaseStatusDescription(status, iwc.getCurrentLocale()));
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
		return (_showManager ? getManagerColumn() + 1 : getDateColumn() + 1);
	}	
	int getLastColumn(){
		return getStatusColumn();
	}		
	
	private CollectionNavigator getNavigator(IWContext iwc, User user) {
		CollectionNavigator navigator = new CollectionNavigator(getNumberOfCases(iwc, user));
		navigator.setTextStyle(STYLENAME_SMALL_TEXT);
		navigator.setLinkStyle(STYLENAME_SMALL_LINK);
		navigator.setNumberOfEntriesPerPage(_numberOfCases);
		navigator.setPadding(getCellpadding());
		_startCase = navigator.getStart(iwc);
		
		return navigator;
	}
	
	protected CaseBusiness getCaseBusiness(IWContext iwc) throws Exception {
		return (CaseBusiness) IBOLookup.getServiceInstance(iwc, CaseBusiness.class);
	}

	protected CommuneCaseBusiness getCommuneCaseBusiness(IWContext iwc) throws Exception {
		return (CommuneCaseBusiness) IBOLookup.getServiceInstance(iwc, CommuneCaseBusiness.class);
	}


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
	 * Sets the showName.
	 * @param showName The showName to set
	 */
	public void setShowManager(boolean showManager) {
		_showManager = showManager;
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
	
	/**
	 * @param useStyleNames The useStyleNames to set.
	 */
	public void setUseStyleNames(boolean useStyleNames) {
		this.useStyleNames = useStyleNames;
	}

	/**
	 * @param useBullets The useBullets to set.
	 */
	/*public void setUseBullets(boolean useBullets) {
		this.useBullets = useBullets;
	}*/

	/**
	 * @param firstColumnPadding The firstColumnPadding to set.
	 */
	public void setFirstColumnPadding(int firstColumnPadding) {
		this.firstColumnPadding = firstColumnPadding;
	}
	
	public void setUseUserInSession(boolean useUserInSession) {
		iUseUserInSession = useUserInSession;
	}
}