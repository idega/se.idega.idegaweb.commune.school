/*
 * Created on 2003-okt-06
 *
 * This module is used by CentralPlacingEditor to search for a user. 
 */
package se.idega.idegaweb.commune.school.presentation;


import java.rmi.RemoteException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.ejb.FinderException;

import se.idega.idegaweb.commune.presentation.CommuneBlock;

import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.Parameter;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.user.data.User;
import com.idega.user.data.UserHome;
import com.idega.util.text.TextSoap;


/**
 * This module is used by CentralPlacingEditor to search for a user.
 */
public class SearchUserModule extends CommuneBlock {

	//private static final String SEARCH_PERSONAL_ID = "usrch_search_pid";
	//private static final String SEARCH_LAST_NAME = "usrch_search_lname";
	//private static final String SEARCH_MIDDLE_NAME = "usrch_search_mname";
	//private static final String SEARCH_FIRST_NAME = "usrch_search_fname";
	public static final String SEARCH_COMMITTED = "mbe_act_search";
	public static final String STYLENAME_TEXT = "Text";
	public static final String STYLENAME_HEADER = "Header";
	public static final String STYLENAME_BUTTON = "Button";
	public static final String STYLENAME_WARNING = "Warning";
	public static final String STYLENAME_INTERFACE = "Interface";

	// Localization keys -added by borgman
	private static final String KP = "search_user_module.";
	private static final String KEY_BUTTON_SEARCH = KP+"button_search";
	private static final String KEY_BUTTON_RESET = KP+"button_reset";
	//private static final String KEY_BUTTON_BACK = KP+"button_back";

	private String textFontStyleName;
	private String headerFontStyleName;
	private String buttonStyleName;
	private String warningStyleName;
	private String interfaceStyleName;
	private String textFontStyle;
	private String headerFontStyle;
	private String warningFontStyle;
	private String buttonStyle;
	private String interfaceStyle;
	//private static final String PRM_USER_ID = "usrch_user_id_";
	private Integer userID;
	private User user;
	private Collection usersFound;
	private boolean hasManyUsers;
	private boolean showFirstNameInSearch;
	private boolean showMiddleNameInSearch;
	private boolean showLastNameInSearch;
	private boolean showPersonalIDInSearch;
	private int maxFoundUserRows;
	private int maxFoundUserCols;
	private String bundleIdentifer;
	private static String BUNDLE_IDENTIFIER = "is.idega.idegaweb.member";
	//private IWBundle iwb;
	private IWResourceBundle iwrb;
	private boolean processed;
	private List maintainedParameters;
	private int personalIDLength;
	private int firstNameLength;
	private int middleNameLength;
	private int lastNameLength;
	private boolean stacked;
	private boolean firstLetterCaseInsensitive;
	private boolean skipResultsForOneFound;
	private boolean OwnFormContainer;
	private boolean showResetButton;
	private boolean showButtons;
	private String uniqueIdentifier;
	private boolean showOverFlowMessage;
	private Collection addedButtons;
	private boolean showSearchParamsAfterSearch;

	public SearchUserModule() {
		textFontStyleName = null;
		headerFontStyleName = null;
		buttonStyleName = null;
		warningStyleName = null;
		interfaceStyleName = null;
		textFontStyle = "font-weight:plain;";
		headerFontStyle = "font-weight:bold;";
		warningFontStyle = "font-weight:bold;fon-color:#FF0000";
		buttonStyle =
			"color:#000000;font-size:10px;font-family:Verdana,Arial,Helvetica,sans-serif;font-weight:normal;border-width:1px;border-style:solid;border-color:#000000;";
		interfaceStyle =
			"color:#000000;font-size:10px;font-family:Verdana,Arial,Helvetica,sans-serif;font-weight:normal;border-width:1px;border-style:solid;border-color:#000000;";
		userID = null;
		user = null;
		usersFound = null;
		hasManyUsers = false;
		showFirstNameInSearch = true;
		showMiddleNameInSearch = true;
		showLastNameInSearch = true;
		showPersonalIDInSearch = true;
		maxFoundUserRows = 20;
		maxFoundUserCols = 3;
		bundleIdentifer = null;
		processed = false;
		maintainedParameters = new Vector();
		personalIDLength = 10;
		firstNameLength = 10;
		middleNameLength = 10;
		lastNameLength = 10;
		stacked = true;
		firstLetterCaseInsensitive = true;
		skipResultsForOneFound = true;
		OwnFormContainer = true;
		showResetButton = true;
		showButtons = true;
		uniqueIdentifier = "unique";
		showOverFlowMessage = true;
		addedButtons = null;
		showSearchParamsAfterSearch = false;
	}

	private void initStyleNames() {
		if (textFontStyleName == null)
			textFontStyleName = getStyleName("Text");
		if (headerFontStyleName == null)
			headerFontStyleName = getStyleName("Header");
		if (buttonStyleName == null)
			buttonStyleName = getStyleName("Button");
		if (warningStyleName == null)
			warningStyleName = getStyleName("Warning");
		if (interfaceStyleName == null)
			interfaceStyleName = getStyleName("Interface");
	}

	public void main(IWContext iwc) throws Exception {
		initStyleNames();
		iwrb = getResourceBundle(iwc);
		String message = null;
		try {
			process(iwc);
		} catch (RemoteException e) {
			e.printStackTrace();
			message =
				iwrb.getLocalizedString("usrch.service_available", "Search service not available");
		} catch (FinderException e) {
			e.printStackTrace();
			message = iwrb.getLocalizedString("usrch.no_user_found", "No user found");
		}
		Table T = new Table();
		T.add(presentateCurrentUserSearch(), 1, 1);
		if (!skipResultsForOneFound || hasManyUsers)
			T.add(presentateFoundUsers(), 1, 2);
		if (message != null) {
			Text tMessage = new Text(message);
			T.add(tMessage, 1, 3);
		}
		if (OwnFormContainer) {
			Form form = new Form();
			form.add(T);
			add(form);
		} else {
			add(T);
		}

	}

	public void process(IWContext iwc) throws FinderException, RemoteException {
		if (processed)
			return;
		if (iwc.isParameterSet(SEARCH_COMMITTED + uniqueIdentifier)) {
			processSearch(iwc);
		}
		else if (iwc.isParameterSet("usrch_user_id_" + uniqueIdentifier))
			userID = Integer.valueOf(iwc.getParameter("usrch_user_id_" + uniqueIdentifier));
		if (userID != null)
			try {
				UserHome home = (UserHome) IDOLookup.getHome(User.class);
				user = home.findByPrimaryKey(userID);
			} catch (IDOLookupException e) {
				log(e);
				logWarning("No child found for userID: " + userID);
			}
		processed = true;
	}

	private void processSearch(IWContext iwc)
		throws IDOLookupException, FinderException, RemoteException {
		UserHome home = (UserHome) IDOLookup.getHome(User.class);
		String first = iwc.getParameter("usrch_search_fname" + uniqueIdentifier);
		String middle = iwc.getParameter("usrch_search_mname" + uniqueIdentifier);
		String last = iwc.getParameter("usrch_search_lname" + uniqueIdentifier);
		String pid = iwc.getParameter("usrch_search_pid" + uniqueIdentifier);
		
		
		if (pid != null && pid.length() > 0) {
			try {
				String temp = pid;
				temp = TextSoap.findAndCut(temp, "-");
				Long.parseLong(temp);
				if (temp.length() == 10 ) {
					int firstTwo = Integer.parseInt(temp.substring(0, 2));
					if (firstTwo < 04) {
						temp = "20"+temp;
					}	else {
						temp = "19"+temp;
					}
				}
				pid = temp;
			}
			catch (NumberFormatException nfe) {}
		}
		
		
		if (firstLetterCaseInsensitive) {
			if (first != null)
				first = TextSoap.capitalize(first);
			if (middle != null)
				middle = TextSoap.capitalize(middle);
			if (last != null)
				last = TextSoap.capitalize(last);
		}
		usersFound =
			home.findUsersByConditions(first, middle, last, pid, null, null, -1, -1, -1, -1, null ,null ,true, true);
		if (user == null && usersFound != null)
			if (!usersFound.isEmpty()) {
				hasManyUsers = usersFound.size() > 1;
				if (!hasManyUsers) {
					user = (User) usersFound.iterator().next();
				}				
			} else {
				// No user found
			}
	}

	private Table presentateCurrentUserSearch() {
		Table searchTable = new Table();
		int row = 1;
		int col = 1;
		String clearAction = "";
		if (showPersonalIDInSearch) {
			Text tPersonalID = new Text(iwrb.getLocalizedString("usrch_search_pid", "Personal ID"));
			//Text tPersonalID =this.getLocalizedSmallHeader("usrch_search_pid", "Personal ID");
			tPersonalID.setStyleClass(headerFontStyleName);
			//tPersonalID.setStyleClass(getSmallHeaderFontStyle());
			searchTable.add(tPersonalID, col, row);
			TextInput input = new TextInput("usrch_search_pid" + uniqueIdentifier);
			input.setStyleClass(interfaceStyleName);
			input.setLength(personalIDLength);
		// Empty user fields after search
		if (user != null && user.getPersonalID() != null && showSearchParamsAfterSearch)
				input.setContent(user.getPersonalID());
			if (stacked)
				searchTable.add(input, col++, row + 1);
			else
				searchTable.add(input, ++col, row);
			clearAction =
				clearAction + "this.form.usrch_search_pid" + uniqueIdentifier + ".value ='' ;";
		}
		if (showLastNameInSearch) {
			Text tLastName = new Text(iwrb.getLocalizedString("usrch_search_lname", "Last name"));
			tLastName.setStyleClass(headerFontStyleName);
			searchTable.add(tLastName, col, row);
			TextInput input = new TextInput("usrch_search_lname" + uniqueIdentifier);
			input.setStyleClass(interfaceStyleName);
			input.setLength(lastNameLength);
			// Empty user fields after search
			if (user != null && user.getLastName() != null && showSearchParamsAfterSearch)
				input.setContent(user.getLastName());
			if (stacked)
				searchTable.add(input, col++, row + 1);
			else
				searchTable.add(input, ++col, row);
			clearAction =
				clearAction + "this.form.usrch_search_lname" + uniqueIdentifier + ".value ='' ;";
		}
		if (showMiddleNameInSearch) {
			Text tMiddleName =
				new Text(iwrb.getLocalizedString("usrch_search_mname", "Middle name"));
			tMiddleName.setStyleClass(headerFontStyleName);
			searchTable.add(tMiddleName, col, row);
			TextInput input = new TextInput("usrch_search_mname" + uniqueIdentifier);
			input.setStyleClass(interfaceStyleName);
			input.setLength(middleNameLength);
			// Empty user fields after search
			if (user != null && user.getMiddleName() != null && showSearchParamsAfterSearch)
				input.setContent(user.getMiddleName());
			if (stacked)
				searchTable.add(input, col++, row + 1);
			else
				searchTable.add(input, ++col, row);
			clearAction =
				clearAction + "this.form.usrch_search_mname" + uniqueIdentifier + ".value ='' ;";
		}
		if (showFirstNameInSearch) {
			Text tFirstName = new Text(iwrb.getLocalizedString("usrch_search_fname", "First name"));
			tFirstName.setStyleClass(headerFontStyleName);
			searchTable.add(tFirstName, col, row);
			TextInput input = new TextInput("usrch_search_fname" + uniqueIdentifier);
			input.setStyleClass(interfaceStyleName);
			input.setLength(firstNameLength);
			// Empty user fields after search
			if (user != null && showSearchParamsAfterSearch)
				input.setContent(user.getFirstName());
			if (stacked)
				searchTable.add(input, col++, row + 1);
			else
				searchTable.add(input, ++col, row);
			clearAction =
				clearAction + "this.form.usrch_search_fname" + uniqueIdentifier + ".value ='' ;";
		}
		if (showButtons) {
			SubmitButton search = new SubmitButton(
													iwrb.getLocalizedImageButton(KEY_BUTTON_SEARCH, "Search"),
													SEARCH_COMMITTED + uniqueIdentifier, "true");

			search.setStyleClass(buttonStyleName);
			if (stacked)
				searchTable.add(search, col++, row + 1);
			else
				searchTable.add(search, 1, row + 1);
			if (showResetButton) {
				//SubmitButton reset = new SubmitButton(
				//											iwrb.getLocalizedString(KEY_BUTTON_RESET, "Reset"));

				SubmitButton reset = new SubmitButton(
														iwrb.getLocalizedImageButton(KEY_BUTTON_RESET, "Reset"));


				reset.setStyleClass(buttonStyleName);
				reset.setOnClick(clearAction + "return false;");
				searchTable.add(reset, col++, row + 1);
			}
			
		/*	SubmitButton back = new SubmitButton(
													iwrb.getLocalizedImageButton(KEY_BUTTON_BACK, "Back"),
													CentralPlacementEditor.PARAM_BACK, "true");

			back.setStyleClass(buttonStyleName);			
			searchTable.add(back, col++, row + 1);
		*/	
			
			if (addedButtons != null && !addedButtons.isEmpty()) {
				for (Iterator iter = addedButtons.iterator(); iter.hasNext();) {
					PresentationObject element = (PresentationObject) iter.next();
					if (stacked)
						searchTable.add(element, col++, row + 1);
					else
						searchTable.add(element, 1, row + 1);
				}

			}
		}
		return searchTable;
	}

	private Table presentateFoundUsers() {
		Table T = new Table();
		if (usersFound != null && !usersFound.isEmpty()) {
			Iterator iter = usersFound.iterator();
			T.setCellspacing(4);
			int row = 1;
			int col = 1;
			int colAdd = 1;
			while (iter.hasNext()) {
				User u = (User) iter.next();
				T.add(u.getPersonalID(), colAdd, row);
				Link userLink = new Link(u.getName());
				userLink.addParameter(getUniqueUserParameter((Integer) u.getPrimaryKey()));
				addParameters(userLink);
				T.add(userLink, colAdd + 1, row);
				if (++row == maxFoundUserRows) {
					col++;
					colAdd += 2;
					row = 1;
				}
				if (col == maxFoundUserCols)
					break;
			}

			if (showOverFlowMessage && iter.hasNext()) {
				int lastRow = T.getRows() + 1;
				T.mergeCells(1, lastRow, maxFoundUserCols, lastRow);
				Text tOverflowMessage =
					new Text(
						iwrb.getLocalizedString(
							"usrch_overflow_message",
							"There are more hits in your search than shown, you have to narrow down your searchcriteria"));
				tOverflowMessage.setStyleClass(warningStyleName);
				T.add(tOverflowMessage, 1, lastRow);
			}
		}
		return T;
	}

	private void addParameters(Link link) {
		Parameter element;
		for (Iterator iter = maintainedParameters.iterator();
			iter.hasNext();
			link.addParameter(element))
			element = (Parameter) iter.next();

	}

	public void setShowFirstNameInSearch(boolean b) {
		showFirstNameInSearch = b;
	}

	public void setShowLastNameInSearch(boolean b) {
		showLastNameInSearch = b;
	}

	public void setShowMiddleNameInSearch(boolean b) {
		showMiddleNameInSearch = b;
	}

	public void setShowPersonalIDInSearch(boolean b) {
		showPersonalIDInSearch = b;
	}

	public boolean isHasManyUsers() {
		return hasManyUsers;
	}

	public int getMaxFoundUserCols() {
		return maxFoundUserCols;
	}

	public int getMaxFoundUserRows() {
		return maxFoundUserRows;
	}

	public User getUser() {
		return user;
	}

	public Collection getUsersFound() {
		return usersFound;
	}

	public void setMaxFoundUserCols(int cols) {
		maxFoundUserCols = cols;
	}

	public void setMaxFoundUserRows(int rows) {
		maxFoundUserRows = rows;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setUsersFound(Collection collection) {
		usersFound = collection;
	}

	public void maintainParameter(Parameter parameter) {
		maintainedParameters.add(parameter);
	}

	public String getBundleIdentifier() {
		if (bundleIdentifer != null)
			return bundleIdentifer;
		else
			return BUNDLE_IDENTIFIER;
	}

	public void setBundleIdentifer(String string) {
		bundleIdentifer = string;
	}

	public Parameter getUniqueUserParameter(Integer userID) {
		return new Parameter(getUniqueUserParameterName(uniqueIdentifier), userID.toString());
	}

	public static String getUniqueUserParameterName(String uniqueIdentifier) {
		return "usrch_user_id_" + uniqueIdentifier;
	}

	public Map getStyleNames() {
		HashMap map = new HashMap();
		map.put("Header", headerFontStyle);
		map.put("Text", textFontStyle);
		map.put("Button", buttonStyle);
		map.put("Warning", warningFontStyle);
		map.put("Interface", interfaceStyle);
		return map;
	}

	public int getFirstNameLength() {
		return firstNameLength;
	}

	public String getHeaderFontStyle() {
		return headerFontStyle;
	}

	public int getLastNameLength() {
		return lastNameLength;
	}

	public int getMiddleNameLength() {
		return middleNameLength;
	}

	public int getPersonalIDLength() {
		return personalIDLength;
	}

	public boolean isShowFirstNameInSearch() {
		return showFirstNameInSearch;
	}

	public boolean isShowLastNameInSearch() {
		return showLastNameInSearch;
	}

	public boolean isShowMiddleNameInSearch() {
		return showMiddleNameInSearch;
	}

	public boolean isShowPersonalIDInSearch() {
		return showPersonalIDInSearch;
	}

	public boolean isStacked() {
		return stacked;
	}

	public String getTextFontStyle() {
		return textFontStyle;
	}

	public void setFirstNameLength(int length) {
		firstNameLength = length;
	}

	public void setHeaderFontStyle(String style) {
		headerFontStyle = style;
	}

	public void setLastNameLength(int length) {
		lastNameLength = length;
	}

	public void setMiddleNameLength(int length) {
		middleNameLength = length;
	}

	public void setPersonalIDLength(int length) {
		personalIDLength = length;
	}

	public void setStacked(boolean flag) {
		stacked = flag;
	}

	public void setTextFontStyle(String style) {
		textFontStyle = style;
	}

	public boolean isOwnFormContainer() {
		return OwnFormContainer;
	}

	public void setOwnFormContainer(boolean flag) {
		OwnFormContainer = flag;
	}

	public String getUniqueIdentifier() {
		return uniqueIdentifier;
	}

	public void setUniqueIdentifier(String identifier) {
		uniqueIdentifier = identifier;
	}

	public String getButtonStyle() {
		return buttonStyle;
	}

	public String getButtonStyleName() {
		return buttonStyleName;
	}

	public String getHeaderFontStyleName() {
		return headerFontStyleName;
	}

	public String getTextFontStyleName() {
		return textFontStyleName;
	}

	public void setButtonStyle(String string) {
		buttonStyle = string;
	}

	public void setButtonStyleName(String string) {
		buttonStyleName = string;
	}

	public void setHeaderFontStyleName(String string) {
		headerFontStyleName = string;
	}

	public void setTextFontStyleName(String string) {
		textFontStyleName = string;
	}

	public boolean isSkipResultsForOneFound() {
		return skipResultsForOneFound;
	}

	public void setSkipResultsForOneFound(boolean flag) {
		skipResultsForOneFound = flag;
	}

	public boolean isShowResetButton() {
		return showResetButton;
	}

	public void setShowResetButton(boolean b) {
		showResetButton = b;
	}

	public boolean isShowOverFlowMessage() {
		return showOverFlowMessage;
	}

	public void setShowOverFlowMessage(boolean b) {
		showOverFlowMessage = b;
	}

	public void addButtonObject(PresentationObject obj) {
		if (addedButtons == null)
			addedButtons = new Vector();
		addedButtons.add(obj);
	}

	public boolean isShowButtons() {
		return showButtons;
	}

	public void setShowButtons(boolean b) {
		showButtons = b;
	}

	/**
	 * @return Returns the showSearchParamsAfterSearch.
	 */
	public boolean isShowSearchParamsAfterSearch() {
		return showSearchParamsAfterSearch;
	}

	/**
	 * @param showSearchParamsAfterSearch The showSearchParamsAfterSearch to set.
	 */
	public void setShowSearchParamsAfterSearch(boolean showSearchParamsAfterSearch) {
		this.showSearchParamsAfterSearch = showSearchParamsAfterSearch;
	}

}
