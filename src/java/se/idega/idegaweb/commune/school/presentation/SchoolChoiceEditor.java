/*
 * Created on 2004-maj-06
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */


/**
 * @author Malin
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */


package se.idega.idegaweb.commune.school.presentation;

import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Iterator;

import se.idega.idegaweb.commune.accounting.resource.business.ResourceBusiness;
import se.idega.idegaweb.commune.business.CommuneUserBusiness;
import se.idega.idegaweb.commune.childcare.business.ChildCareBusiness;
import se.idega.idegaweb.commune.message.business.MessageBusiness;
import se.idega.idegaweb.commune.provider.business.ProviderSession;
import se.idega.idegaweb.commune.school.business.CentralPlacementBusiness;
import se.idega.idegaweb.commune.school.business.SchoolChoiceBusiness;
import se.idega.idegaweb.commune.school.business.SchoolCommuneBusiness;
import se.idega.idegaweb.commune.school.data.SchoolChoice;
import se.idega.idegaweb.commune.school.event.SchoolEventListener;

import com.idega.block.process.business.CaseBusiness;
import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.business.SchoolBusinessBean;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolClass;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.block.school.data.SchoolClassMemberHome;
import com.idega.block.school.data.SchoolSeason;
import com.idega.block.school.data.SchoolSeasonHome;
import com.idega.block.school.data.SchoolStudyPath;
import com.idega.block.school.data.SchoolStudyPathHome;
import com.idega.business.IBOLookup;
import com.idega.business.IBORuntimeException;
import com.idega.core.contact.data.Email;
import com.idega.core.contact.data.Phone;
import com.idega.core.localisation.data.ICLanguage;
import com.idega.core.localisation.data.ICLanguageHome;
import com.idega.core.location.data.Address;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.IWUserContext;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.Parameter;
import com.idega.presentation.ui.SubmitButton;
import com.idega.user.business.NoPhoneFoundException;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;


public class SchoolChoiceEditor extends SchoolCommuneBlock {
	// *** Localization keys ***
	private static final String KP = "school_choice_editor.";
	private static final String KEY_WINDOW_HEADING = KP + "window_heading";
	private static final String KEY_SEARCH_PUPIL_HEADING = KP + "search_pupil_heading";
	private static final String KEY_PUPIL_HEADING = KP + "pupil_heading";
	
	private static final String KEY_SEARCH_NO_PUPIL_FOUND = KP + "search_no_pupil_found";
	
	
	// Label keys
	private static final String KEY_PERSONAL_ID_LABEL = KP + "personal_id_label";
	private static final String KEY_FIRST_LAST_NAME_LABEL = KP + "first_last_name_label";
	private static final String KEY_ADDRESS_LABEL = KP + "address_label";
	private static final String KEY_PHONE_LABEL = KP + "telephone_label";
	private static final String KEY_SCHOOL_TYPE_LABEL = KP + "school_type_label";
	private static final String KEY_SCHOOL_CHOICE_LABEL = KP + "school_choice_label";
	
	
	public static final String KEY_STORED_MSG_PRFX = KP + "stored_msg_prfx";
	public static final String KEY_STORED_MSG_ERROR = KP + "stored_msg_error";
	
	// Button keys
//	private static final String KEY_BUTTON_SEARCH = KP + "button_search";
	private static final String KEY_BUTTON_PLACE = KP + "button_place";
	private static final String KEY_BUTTON_CANCEL = KP + "button_cancel";
	private static final String KEY_BUTTON_SEND = KP + "button_send";
	
	// Http request parameters  
	public static final String PARAM_ACTION = "param_action";
	public static final String PARAM_PRESENTATION = "param_presentation";
	public static final String PARAM_SCHOOL_CATEGORY = "param_school_category";
	public static final String PARAM_SCHOOL_CATEGORY_CHANGED = "param_school_category_changed";
	public static final String PARAM_PROVIDER = "param_provider";
	
	public static final String PARAM_REACTIVATE_CHOICE_ID = "param_reactivate_choice_id";
	public static final String PARAM_REACTIVATE_CHOICE_ORDER = "param_reactivate_choice_order";
	public static final String PARAM_PLAC_CHOICE_ID = "param_plac_choice_id";
	public static final String PARAM_NUMBER_OF_CHOICES = "param_number_of_choices";
	
	
	// PARAM_BACK is used in SearchUserModule
	public static final String PARAM_BACK = "param_back";
	public static final String PARAM_MSG_TO_PARENT = "msg_to_parent";
	
	// Actions
	private static final int ACTION_REACTIVATE_CHOICE = 1;	
	private static final int ACTION_REMOVE_SESSION_CHILD = 2;
	private static final int ACTION_SEND_MESSAGES = 3;
	
	
	
	// Presentations
	//private static final int PRESENTATION_SEARCH_FORM = 1;
	public static final String FORM_NAME = "school_choice_editor_form";

	// CSS styles   
	private static final String STYLE_UNDERLINED_SMALL_HEADER =
		"font-style:normal;text-decoration:underline;color:#000000;"
		+ "font-size:10px;font-family:Verdana,Arial,Helvetica;font-weight:bold;";
	private static final  String STYLE_STORED_PLACEMENT_MSG =
		"font-style:normal;color:#0000FF;"
		+ "font-size:10px;font-family:Verdana,Arial,Helvetica;font-weight:normal;";
	// Paths
	private static final String PATH_TRANS_GIF =
		"/idegaweb/bundles/com.idega.core.bundle/resources/transparentcell.gif";

	// Session java bean keys
	public static final String SESSION_KEY_CHILD = KP + "session_key.child";

	// Unique parameter suffix used by SearchUserModule
	private static final String UNIQUE_SUFFIX = "chosen_user";

	// Instance variables
	private IWResourceBundle iwrb;
	private Form form;
	private Table mainTable;
	private int mainTableRow;
	private String mainRowHeight  = null;
	private User child;
	private String uniqueUserSearchParam;
	
	private Image transGIF = new Image(PATH_TRANS_GIF);
	private String errMsgSearch = null;
	private String errMsgMid = null;
	private SchoolClassMember latestPl = null;
	private SchoolClassMember storedPlacement = null;
	private Link pupilOverviewLinkButton = null;
	private Link editLatestPlacementButton = null;
	private ProviderSession _providerSession = null;
	private SchoolSeason currentSeason = null;

	private int _action = -1;
	private boolean _newPlacement = false;
	private boolean _cancelNewPlacement = false;

	public void init(IWContext iwc) throws Exception {
		iwrb = getResourceBundle(iwc);
		form = new Form();
		form.setName(FORM_NAME);
		form.setEventListener(SchoolEventListener.class);
		
		// Parameter name returning chosen User from SearchUserModule
		uniqueUserSearchParam = SearchUserModule.getUniqueUserParameterName(UNIQUE_SUFFIX);
		
				
		form.add(getMainTable());
		parse(iwc);
		
		getSearchResult(iwc);

		currentSeason = getCentralPlacementBusiness(iwc).getCurrentSeason();
		// Perform actions according the _action input parameter
		switch (_action) {
		case ACTION_REACTIVATE_CHOICE :
			try {
				SchoolSeason chosenSeason = getSchoolSeasonHome().
				findByPrimaryKey(new Integer(getSchoolCommuneSession(iwc).getSchoolSeasonID()));
				latestPl = getCentralPlacementBusiness(iwc).getLatestPlacementFromElemAndHighSchool(child, chosenSeason);
				//storedPlacement = storePlacement(iwc, child);
				reactivateSchoolChoice(iwc, child);
			} 
			catch (Exception e1) {log(e1);}			
			break;
		case ACTION_REMOVE_SESSION_CHILD :
			removeSessionChild(iwc);
			break;
		

		}
		// Show main page tables		
		try {
			if (storedPlacement == null) {
				// Show main form parts
				if (!_newPlacement || _cancelNewPlacement) {
					// First part with search, pupil and latest placement
					setMainTableContent(getSearchTable(iwc));
					setMainTableContent(getPupilTable(iwc, child));
					setMainTableContent(getLatestPlacementTable(iwc, child));
					//setMainTableContent(getSchoolChoiceTable(iwc, child));
					
				}
			} 
			
		} catch (Exception e) {
			e.printStackTrace();
			setMainTableContent(new Text("Exception thrown!"));
		}

		add(form);
	}

	private Table getMainTable() {
		mainTable = new Table();
		mainTable.setBorder(0);
		mainTable.setWidth(550);
		mainTable.setCellpadding(0);
		mainTable.setCellspacing(0);
		int col = 1;
		mainTableRow = 1;
		
		//  *** WINDOW HEADING ***
		mainTable.add(
				getLocalizedSmallHeader(KEY_WINDOW_HEADING, "Re-activate school choice"), 
				col, mainTableRow);
		mainTable.setColor(col, mainTableRow, getHeaderColor());
		mainTable.setAlignment(col, mainTableRow, Table.HORIZONTAL_ALIGN_CENTER);
		mainTable.setRowVerticalAlignment(mainTableRow, Table.VERTICAL_ALIGN_MIDDLE);
		mainTable.setRowHeight(mainTableRow++, "20");

		return mainTable;
	}

	private void setMainTableContent(PresentationObject obj) {
		int col = 1;
		mainTable.add(obj, col, mainTableRow++);
		
	}

	public Table getSearchTable(IWContext iwc) {
		// *** Search Table *** START - the uppermost table
		Table table = new Table();
		table.setBorder(0);
		table.setCellpadding(0);
		table.setCellspacing(0);
		String rowHeight = "25";

		int col = 1;
		int row = 1;

		Image space1 = (Image) transGIF.clone();
		space1.setWidth(6);

		// *** HEADING Search pupil ***
		table.add(space1, col, row);
		Text pupilTxt = new Text(localize(KEY_SEARCH_PUPIL_HEADING, "Search pupil"));
		pupilTxt.setFontStyle(STYLE_UNDERLINED_SMALL_HEADER);
		table.add(pupilTxt, col++, row);
		table.setRowHeight(row, rowHeight);
		//table.setRowVerticalAlignment(row, Table.VERTICAL_ALIGN_BOTTOM);
		col = 1;
		row++;
		
		// User search module - configure and add 
		SearchUserModule searchModule = getSearchUserModule(iwc);
		try {
			// if one user found, set session attribute directly
			searchModule.process(iwc);
			User oneChild = searchModule.getUser();
			if (oneChild != null) {
				iwc.getSession().setAttribute(SESSION_KEY_CHILD, oneChild);
			} else if (iwc.isParameterSet("usrch_search_fname" + UNIQUE_SUFFIX)
					|| iwc.isParameterSet("usrch_search_lname" + UNIQUE_SUFFIX)
					|| iwc.isParameterSet("usrch_search_pid" + UNIQUE_SUFFIX)) {
				errMsgSearch = localize(KEY_SEARCH_NO_PUPIL_FOUND, "No pupil found");
			}
		} catch (Exception e) {}		
		table.add(searchModule, col++, row);
		
		// Get current pupil from session attribute
		child = (User) iwc.getSession().getAttribute(SESSION_KEY_CHILD);		
		
		if (errMsgSearch != null) {
			row++;
			col = 1;
			table.add(Text.getNonBrakingSpace(4), col, row);
			table.add(getSmallErrorText(errMsgSearch), col, row);
		}
		
		return table;
	}
	
	public Table getPupilTable(IWContext iwc, User child) {
		// *** Search Table *** START - the uppermost table
		Table table = new Table();
		table.setWidth("100%");
		table.setBorder(0);
		table.setCellpadding(2);
		table.setCellspacing(0);
		transGIF.setHeight("1");
		transGIF.setWidth("1");
		String rowHeight = "20";
		
		if (mainRowHeight != null)
			rowHeight = mainRowHeight;
		
		int row = 1;
		int col = 1;
		// add empty space row
		table.add(transGIF, col++, row);
		table.add(transGIF, col++, row);
		table.add(transGIF, col++, row);
		table.add(transGIF, col++, row);
		table.add(transGIF, col++, row);
		// Set COLUMN WIDTH for column 1 to 5
		table.setWidth(1, row, "100");
		//table.setWidth(2, row, "70");
		//table.setWidth(3, row, "70");
		//table.setWidth(4, row, "70");
		//table.setWidth(5, row, "104");
		table.setRowHeight(row, "1");
		
		row++;
		col = 1;

		// *** HEADING Pupil ***
		/*Text pupilTxt = new Text(localize(KEY_PUPIL_HEADING, "Pupil"));
		pupilTxt.setFontStyle(STYLE_UNDERLINED_SMALL_HEADER);
		table.add(pupilTxt, col++, row);
		table.setRowHeight(row, rowHeight);
		row++;
		col = 1;
		*/
		
		// Last Name
		table.add(getSmallHeader(localize(KEY_FIRST_LAST_NAME_LABEL, "Name: ")), col++, row);

		/*Table nameTable = new Table();
		col = 1;
		nameTable.setCellpadding(0);
		nameTable.setCellspacing(0);
		*/
		if (child != null)
			table.add(getSmallText(child.getLastName()+ ", " + child.getFirstName()), col++, row);
		
		// First Name       
		//nameTable.add(getSmallHeader(localize(KEY_FIRST_LAST_NAME_LABEL, "Name: ")), col++, 1);
		//if (child != null)
		//	nameTable.add(getSmallText(child.getLastName()+ ", " + child.getFirstName()), col++, 1);
		/*nameTable.setWidth(1, 1, "100");
		nameTable.setWidth(2, 1, "100");
		nameTable.setWidth(3, 1, "100");
		*/
	//	table.add(nameTable, 2, row);
		//table.mergeCells(2, row, 5, row);
		table.setRowHeight(row, rowHeight);
		row++;
		col = 1;
		// Personal Id Number
		table.add(getSmallHeader(localize(KEY_PERSONAL_ID_LABEL, "Personal id: ")), col++, row);
		if (child != null)
			table.add(getSmallText(child.getPersonalID()), col++, row);
		table.setRowHeight(row, rowHeight);
		row++;
		col = 1;
		
		// Address and Phone
		table.add(getSmallHeader(localize(KEY_ADDRESS_LABEL, "Address: ")), col++, row);
		table.setRowHeight(row, rowHeight);
		row++;
		col = 1;
		table.add(getSmallHeader(localize(KEY_PHONE_LABEL, "Phone: ")), col++, row);
		if (child != null) {
			try {
				// child address
				Address address = getUserBusiness(iwc).getUsersMainAddress(child);
				StringBuffer aBuf = new StringBuffer(address.getStreetAddress());
				aBuf.append(", ");
				aBuf.append(address.getPostalCode().getPostalAddress());
				row--;
				table.add(getSmallText(aBuf.toString()), col, row);
				row++;
				// Get child phones
				Collection phones = child.getPhones();
				int i = 0;
				int phonesSize = phones.size();
				StringBuffer pBuf = new StringBuffer();
				for (Iterator iter = phones.iterator(); iter.hasNext(); i++) {
					Phone phone = (Phone) iter.next();
					pBuf.append(phone.getNumber());
					if (i < phonesSize - 1)
						pBuf.append(", ");
				}
				pBuf.append("&nbsp;");
				table.add(getSmallText(pBuf.toString()), col, row);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		row++;
		col = 1;
		
		return table;
	}
	
	
	
	public Table getLatestPlacementTable(IWContext iwc, User child) throws RemoteException {
		// *** Search Table *** START - the uppermost table
		Table table = new Table();
		table.setWidth("100%");
		table.setBorder(0);
		table.setCellpadding(2);
		table.setCellspacing(0);
		transGIF.setHeight("1");
		transGIF.setWidth("1");
		String rowHeight = "20";

		if (mainRowHeight != null)
			rowHeight = mainRowHeight;
		
		int row = 1;
		int col = 1;
		// add empty space row
		table.add(transGIF, col++, row);
		table.add(transGIF, col++, row);
		table.add(transGIF, col++, row);
		table.add(transGIF, col++, row);
		table.add(transGIF, col++, row); 
		// Set COLUMN WIDTH for column 1 to 5
		table.setWidth(1, row, "100");
		table.setWidth(2, row, "100");
		//table.setWidth(3, row, "70");
		//table.setWidth(4, row, "70");
		//table.setWidth(5, row, "104");
		table.setRowHeight(row, rowHeight);		
		row++;
		col = 1;
		table.setBorder(0);
		// *** HEADING Latest placement ***
		//	Text schoolChoiceTxt =
		//		new Text(localize(KEY_SCHOOL_CHOICE_LABEL, "School choice"));
		//schoolChoiceTxt.setFontStyle(STYLE_UNDERLINED_SMALL_HEADER);
		
		//table.add(schoolChoiceTxt, col, row);
		table.add(getSmallHeader(localize(KEY_SCHOOL_CHOICE_LABEL, "School choice")), col, row);
		table.setRowHeight(row, rowHeight);
		table.setRowVerticalAlignment(row, Table.VERTICAL_ALIGN_TOP);
		table.mergeCells(col, row, col+1, row);	

		// Table with School Choices 
		col = 3;
		table.add(getSchoolChoiceTable(iwc, child), col, row);
		table.setAlignment(5,row, Table.HORIZONTAL_ALIGN_RIGHT);
		table.setRowHeight(row, rowHeight);
		row++;
		col = 1;
		
		// Empty space row
		table.add(transGIF, col, row);
		table.setRowHeight(row, rowHeight);
		row++;
		col = 1;
		


		return table;
	}

	
	
	
	protected void removeSessionChild(IWContext iwc) {
		iwc.getSession().removeAttribute(SESSION_KEY_CHILD);
		child = null;
	}
		public Table getSchoolChoiceTable(IWContext iwc, User child) throws RemoteException {
		Table table = new Table();
		table.setBorder(0);
		table.setCellpadding(2);
		table.setCellspacing(0);
		transGIF.setHeight("1");
		transGIF.setWidth("1");
		String rowHeight = "20";
		
		if (mainRowHeight != null)
			rowHeight = mainRowHeight;
		
		int row = 1;
		int col = 1;
		// add empty space row
		table.add(transGIF, col++, row);
		table.add(transGIF, col++, row);
		table.add(transGIF, col++, row);
		
		// Set COLUMN WIDTH for column 1 and 3
		table.setWidth(1, row, "10");
		table.setWidth(3, row, "20");
		
		int pendingSchoolId = -1;
		boolean showChangePlacementDate = false;
		School oldSchool = null;
		Link reactivate;
		int choiceOrder = 0;
		if (child != null){
			Collection choices = getSchoolCommuneBusiness(iwc).getSchoolChoiceBusiness().findByStudentAndSeason(child.getID(), getSchoolCommuneSession(iwc).getSchoolSeasonID());
			
			if (!choices.isEmpty()) {
				School school;
				SchoolChoice choice;
				Iterator iter = choices.iterator();
				
				while (iter.hasNext()) {
					choice = (SchoolChoice) iter.next();
				
					if (choice.getChosenSchoolId() != -1) {
						school = getSchoolCommuneBusiness(iwc).getSchoolBusiness().getSchool(new Integer(choice.getChosenSchoolId()));
						String string = String.valueOf(choice.getChoiceOrder()) + ". " + school.getName() + " (" + getSchoolCommuneBusiness(iwc).getLocalizedCaseStatusDescription(choice.getCaseStatus(), iwc.getCurrentLocale()) + ")";
						int choiceID =
							((Integer) choice.getPrimaryKey()).intValue();
						table.add(new HiddenInput(PARAM_PLAC_CHOICE_ID + String.valueOf(choice.getChoiceOrder()), String.valueOf(choiceID)), 4, row);
						if (choice.getStatus().equalsIgnoreCase("PREL") || choice.getStatus().equalsIgnoreCase("PLAC") || choice.getStatus().equalsIgnoreCase("FLYT")) {
							if (pendingSchoolId == -1)
								pendingSchoolId = choice.getChosenSchoolId();
						}
						if (choice.getStatus().equalsIgnoreCase("TYST") && choice.getChoiceOrder() == 1){
							Image editImg =
								getEditIcon(localize("school_choice_editor.reactivate", "Reactivate school choice"));
							
							
							SubmitButton editButt = new SubmitButton(editImg);
							editButt.setValueOnClick(
									PARAM_ACTION,
									String.valueOf(ACTION_REACTIVATE_CHOICE));
							editButt.setValueOnClick(
									PARAM_REACTIVATE_CHOICE_ID,
									String.valueOf(choiceID));
							editButt.setValueOnClick(
									PARAM_REACTIVATE_CHOICE_ORDER,
									String.valueOf(choice.getChoiceOrder()));			
							
							
							editButt.setSubmitConfirm(
									localize(
											"school_choice_editor.confirm_reactivate_school_choice_msg",
											"Do you really want to activate this school choice?" +
											"If one of the school choices has a placement," +
									"that placement will be deleted"));
							table.add(editButt, 3, row);
							
						}
						
						
						if (choice.getStatus().equalsIgnoreCase("PLAC")){
							
							table.add(new HiddenInput(PARAM_PLAC_CHOICE_ID, String.valueOf(choiceID)), 4, row);
						}
						
						if (choice.getStatus().equalsIgnoreCase("PREL") || choice.getStatus().equalsIgnoreCase("PLAC")) {
							table.add(this.getSmallHeader(string), 2, row);
							
						}
						else {
							table.add(getSmallText(string), 2, row);
							if (choice.getStatus().equalsIgnoreCase("AVSL")){
								Image editImg =
									getEditIcon(localize("school_choice_editor.reactivate", "Reactivate school choice"));
								
								
								SubmitButton editButt = new SubmitButton(editImg);
								editButt.setValueOnClick(
										PARAM_ACTION,
										String.valueOf(ACTION_REACTIVATE_CHOICE));
								editButt.setValueOnClick(
										PARAM_REACTIVATE_CHOICE_ID,
										String.valueOf(choiceID));
								editButt.setValueOnClick(
										PARAM_REACTIVATE_CHOICE_ORDER,
										String.valueOf(choice.getChoiceOrder()));			
								
								
								editButt.setSubmitConfirm(
										localize(
												"school_choice_editor.confirm_reactivate_school_choice_msg",
										"Do you really want to activate this school choice?" +
										"If one of the school choices has a placement," +
										"that placement will be deleted"));
								table.add(editButt, 3, row);
								
															
							}
								
						}
					}
					else {
						table.add(getSmallHeader(localize("school.moving_out_of_community", "Moving out of community")), 2, row);
					}
					
					if (choiceOrder < choice.getChoiceOrder()){
						choiceOrder = choice.getChoiceOrder();
					}
					
					if (iter.hasNext())
						row++;
				
					
					
				}
			
			}
		}
		table.add(new HiddenInput(PARAM_REACTIVATE_CHOICE_ORDER, "-1"), 4, row);
		table.add(new HiddenInput(PARAM_REACTIVATE_CHOICE_ID, "-1"), 5, row);
		table.add(new HiddenInput(PARAM_ACTION, "-1"), 5, row);
		table.add(new HiddenInput(PARAM_NUMBER_OF_CHOICES, String.valueOf(choiceOrder)), 5, 1);
		return table;
	}
	
	
	
	

	/**
	 * Process the search for a pupil. If the uniqueUserSearchParam is set the User is fetched and put in 
	 * session for further use. So here the User session bean is renew if uniqueuserSearchParam is set.
	 * @param iwc Request object context
	 */
		
		public void getSearchResult(IWContext iwc) {
			if (iwc.isParameterSet(uniqueUserSearchParam)) {
				Integer userID = Integer.valueOf(iwc.getParameter(uniqueUserSearchParam));
				try {
					child = getUserBusiness(iwc).getUser(userID);
					// Put User object in session
					iwc.getSession().setAttribute(SESSION_KEY_CHILD, child);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			} else {
				child = (User) iwc.getSession().getAttribute(SESSION_KEY_CHILD);
			}
		}		


	private SearchUserModule getSearchUserModule(IWContext iwc) {
		SearchUserModule searcher = new SearchUserModule();
		searcher.setShowMiddleNameInSearch(false);
		searcher.setOwnFormContainer(false);
		searcher.setUniqueIdentifier(UNIQUE_SUFFIX);
		searcher.setSkipResultsForOneFound(true);
		searcher.setHeaderFontStyleName(getStyleName(STYLENAME_SMALL_HEADER));
		searcher.setButtonStyleName(getStyleName(STYLENAME_INTERFACE_BUTTON));
		searcher.setPersonalIDLength(12);
		searcher.setFirstNameLength(15);
		searcher.setLastNameLength(20);
		searcher.setShowSearchParamsAfterSearch(false);

		String prmChild = SearchUserModule.getUniqueUserParameterName("child");
		if (iwc.isParameterSet(prmChild)) {
			searcher.maintainParameter(new Parameter(prmChild, iwc.getParameter(prmChild)));
		}

		return searcher;
	}

	

		private String getDateString(Timestamp stamp) {
		IWTimestamp iwts = null;
		String dateStr = "";
		if (stamp != null) {
			iwts = new IWTimestamp(stamp);
			dateStr = iwts.getDateString("yyyy-MM-dd");
		}
		return dateStr;
	}

		
	private void reactivateSchoolChoice(IWContext iwc, User child) throws RemoteException  {
		int choiceID = Integer.valueOf(iwc.getParameter(PARAM_REACTIVATE_CHOICE_ID)).intValue();
		Integer orderID = Integer.valueOf(iwc.getParameter(PARAM_REACTIVATE_CHOICE_ORDER));
		int numberChoices = Integer.valueOf(iwc.getParameter(PARAM_NUMBER_OF_CHOICES)).intValue();
		boolean removedPlacement = false;
		//String schoolRemovedFrom = "";
		boolean hasReceivedPlacementMessage = false;
		boolean hasReceivedConfirmationMessage = false;
		int removedSchoolID = -1;
		String activatedSchool = "";
		School school = null;
		for (int i = 1; i <= numberChoices; i++) {
			int currentChoiceID = Integer.valueOf(iwc.getParameter(PARAM_PLAC_CHOICE_ID + String.valueOf(i))).intValue();
			try {
				SchoolChoice choice =  getSchoolCommuneBusiness(iwc).getSchoolChoiceBusiness().getSchoolChoice(currentChoiceID);
				
			
				if (choice.getStatus().equalsIgnoreCase("PLAC")){
					//delete school class member and change status to TYST
					SchoolClassMember schCMember = getBusiness().getSchoolBusiness().findByStudentAndSeason(choice.getChild().getID(), choice.getSchoolSeasonId());
					//schoolRemovedFrom = schCMember.getSchoolClass().getSchool().getSchoolName();
					removedSchoolID = schCMember.getSchoolClass().getSchoolId();
					hasReceivedPlacementMessage = choice.getHasReceivedPlacementMessage();
					//hasReceivedConfirmationMessage = choice.getHasReceivedConfirmationMessage();
					
					Integer schCMemberPK = ((Integer) schCMember.getPrimaryKey());
					
					try {
						getCentralPlacementBusiness(iwc).removeSchoolClassMember(schCMemberPK);
						choice.setHasReceivedPlacementMessage(false);
						choice.setHasReceivedConfirmationMessage(false);						
						//removedPlacement = true;
					}catch (Exception e) {
						logWarning("Error erasing SchooClassMember with PK: " + schCMemberPK.toString());
						log(e);
					}
					
					getSchoolCommuneBusiness(iwc).getSchoolChoiceBusiness().setAsInactive(choice, iwc.getCurrentUser());
				}
				else if (currentChoiceID ==  choiceID) {
					//change status to active
					getSchoolChoiceBusiness(iwc).setAsPreliminary(choice, iwc.getCurrentUser());
					school = getSchoolCommuneBusiness(iwc).getSchoolChoiceBusiness().getSchool(choiceID);
					activatedSchool =  school.getName();
				
				}
				else if (choice.getStatus().equalsIgnoreCase("PREL") && currentChoiceID !=  choiceID) {
					//set the rest to inactive
					getSchoolCommuneBusiness(iwc).getSchoolChoiceBusiness().setAsInactive(choice, iwc.getCurrentUser());
				}
				
				//sendMessages(iwc, removedPlacement, schoolRemovedFrom, activatedSchool, choice);
			}
			catch (Exception e){
				
			}
			
		}	
		getSchoolCommuneBusiness(iwc).getSchoolChoiceBusiness().reactivateApplication(choiceID, removedSchoolID, hasReceivedPlacementMessage);
	
	
	
	
	}
	
	/** send message to parents and school
	 * 
	 * */
	
	private void sendMessages(IWContext iwc, boolean removedPlacement, String schoolRemovedFrom, String activatedSchool, SchoolChoice choice) throws RemoteException {
		String subject = "";
		String subject2 = "";
		String body = "";
		String body2 = "";

		String propNameSubj = null;
		String propNameBody = null;

		int schoolClassID = getSchoolCommuneSession(iwc).getSchoolClassID();
		SchoolClass schoolClass = getSchoolCommuneBusiness(iwc).getSchoolBusiness().findSchoolClass(new Integer(schoolClassID));
		/*if (schoolClass != null) {
			if (!removedPlacement){
				subject = localize(KEY_REACTIVATE_SUBJECT1, "School choice reactivated");
				body = localize(KEY_REACTIVATE_BODY1, "School choice for $barn$ regarding {1} has been reactivated");
				getSchoolCommuneBusiness(iwc).sendMessage(schoolClass, subject, body, false, schoolRemovedFrom, activatedSchool);
			}
			else if (removedPlacement && !schoolClass.getReady()) {
				subject = localize(KEY_REACTIVATE_SUBJECT1, "School choice reactivated");
				body = localize(KEY_REACTIVATE_BODY1, "School choice for $barn$ regarding {1} has been reactivated");
				subject2 = localize(KEY_REACTIVATE_SUBJECT2, "School choice reactivated");
				body2 = localize(KEY_REACTIVATE_BODY2, "The placement for $barn$ in $skola$ has been removed since school choice in $skola2$ has been reactivated");
				getSchoolCommuneBusiness(iwc).sendMessage(schoolClass, subject, body, false, schoolRemovedFrom, activatedSchool);
				getSchoolCommuneBusiness(iwc).sendMessage(schoolClass, subject2, body2, false, schoolRemovedFrom, activatedSchool);
			}
			else if (removedPlacement && schoolClass.getReady()) {
				subject = localize(KEY_REACTIVATE_SUBJECT2, "School choice reactivated");
				body = localize(KEY_REACTIVATE_BODY2, "The placement for $barn$ in $skola$ has been removed since school choice in $skola2$ has been reactivated");
				
				getSchoolCommuneBusiness(iwc).sendMessage(schoolClass, subject, body, true, schoolRemovedFrom, activatedSchool);
			}
			
		}

		URLUtil URL = new URLUtil(BuilderLogic.getInstance().getIBPageURL(iwc, _pageID));
		URL.addParameter(SchoolClassEditor.PARAMETER_ACTION, SchoolClassEditor.ACTION_SAVE);
		getParentPage().setParentToRedirect(URL.toString());
		getParentPage().close();
		*/
		User user = iwc.getCurrentUser();
		Email mail = getUserBusiness(iwc).getUserMail(user);
		
		String email = "";
		if (mail != null)
			email = mail.getEmailAddress();
		String workphone = "";
		try {
			Phone phone = getUserBusiness(iwc).getUsersWorkPhone(user);
			workphone = phone.getNumber();
		}
		catch (NoPhoneFoundException npfe) {
			workphone = "";
		}
		
		Object[] arguments = {user.getName(), email, workphone, choice.getChild().getNameLastFirst(true), choice.getChosenSchool().getName()};

		String message = MessageFormat.format(localize("school.reject_student_message", "We are sorry that we cannot offer {3} a place in {4} at present, if you have any questions, please contact {0} via either phone ({1}) or e-mail ({2})."), arguments);
		
		
		
	}
	
	
	
	/**
	 * Parse input request parameters 
	 * @param iwc Request object context
	 */
	private void parse(IWContext iwc) {
		if (iwc.isParameterSet(PARAM_ACTION)) {
			String actionStr = iwc.getParameter(PARAM_ACTION);
			_action = Integer.parseInt(actionStr);

		}
		
	}

	// *** ACTIONS ***
	
	private ICLanguageHome getICLanguageHome() throws RemoteException {
		return (ICLanguageHome) IDOLookup.getHome(ICLanguage.class);
	}
	
	protected ProviderSession getCentralPlacementProviderSession(IWUserContext iwuc) {
		try {
			return (ProviderSession) IBOLookup.getSessionInstance(iwuc, ProviderSession.class);
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re.getMessage());
		}
	}

	
	private CentralPlacementBusiness getCentralPlacementBusiness(IWContext iwc) 
	throws RemoteException {
		return (CentralPlacementBusiness) 
		IBOLookup.getServiceInstance(iwc, CentralPlacementBusiness.class);
	}


	private CommuneUserBusiness getUserBusiness(IWContext iwc) throws RemoteException {
		return (CommuneUserBusiness) 
		IBOLookup.getServiceInstance(iwc, CommuneUserBusiness.class);
	}

	private SchoolBusiness getSchoolBusiness(IWContext iwc) throws RemoteException {
		return (SchoolBusiness) IBOLookup.getServiceInstance(iwc, SchoolBusiness.class);
	}

	private SchoolChoiceBusiness getSchoolChoiceBusiness(IWContext iwc) throws RemoteException {
		return (SchoolChoiceBusiness) IBOLookup.getServiceInstance(iwc, SchoolChoiceBusiness.class);
	}

	protected CaseBusiness getCaseBusiness(IWContext iwc) throws Exception {
		return (CaseBusiness) IBOLookup.getServiceInstance(iwc, CaseBusiness.class);
	}
	
	/*	private SchoolCommuneBusiness getSchoolCommuneBusiness(IWContext iwc) 
	 throws RemoteException {
	 return (SchoolCommuneBusiness) IBOLookup.getServiceInstance(iwc, 
	 SchoolCommuneBusiness.class);
	 }
	 */
	private ChildCareBusiness getChildCareBusiness(IWContext iwc) throws RemoteException {
		return (ChildCareBusiness)
		IBOLookup.getServiceInstance(iwc, ChildCareBusiness.class);
	}

	private ResourceBusiness getResourceBusiness(IWContext iwc) throws RemoteException {
		return (ResourceBusiness) IBOLookup.getServiceInstance(iwc, ResourceBusiness.class);
	}
	
	public SchoolStudyPathHome getStudyPathHome() throws java.rmi.RemoteException {
		return (SchoolStudyPathHome) IDOLookup.getHome(SchoolStudyPath.class);
	}
	
	public SchoolSeasonHome getSchoolSeasonHome() throws RemoteException {
		return (SchoolSeasonHome) IDOLookup.getHome(SchoolSeason.class);
	}
	
	public SchoolClassMemberHome getSchoolClassMemberHome() throws RemoteException {
		return (SchoolClassMemberHome) IDOLookup.getHome(SchoolClassMember.class);
	}

	private MessageBusiness getMessageBusiness(IWContext iwc) throws RemoteException {
		return (MessageBusiness) IBOLookup.getServiceInstance(iwc, MessageBusiness.class);
	}

	public CommuneUserBusiness getCommuneUserBusiness(IWContext iwc) throws RemoteException {
		return (CommuneUserBusiness) IBOLookup.getServiceInstance(iwc, CommuneUserBusiness.class);
	}

	private SchoolCommuneBusiness getSchoolCommuneBusiness(IWContext iwc) throws RemoteException {
		return (SchoolCommuneBusiness) IBOLookup.getServiceInstance(iwc, SchoolCommuneBusiness.class);
	}
}

