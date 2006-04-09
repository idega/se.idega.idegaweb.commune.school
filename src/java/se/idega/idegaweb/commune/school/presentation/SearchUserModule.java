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
		this.textFontStyleName = null;
		this.headerFontStyleName = null;
		this.buttonStyleName = null;
		this.warningStyleName = null;
		this.interfaceStyleName = null;
		this.textFontStyle = "font-weight:plain;";
		this.headerFontStyle = "font-weight:bold;";
		this.warningFontStyle = "font-weight:bold;fon-color:#FF0000";
		this.buttonStyle =
			"color:#000000;font-size:10px;font-family:Verdana,Arial,Helvetica,sans-serif;font-weight:normal;border-width:1px;border-style:solid;border-color:#000000;";
		this.interfaceStyle =
			"color:#000000;font-size:10px;font-family:Verdana,Arial,Helvetica,sans-serif;font-weight:normal;border-width:1px;border-style:solid;border-color:#000000;";
		this.userID = null;
		this.user = null;
		this.usersFound = null;
		this.hasManyUsers = false;
		this.showFirstNameInSearch = true;
		this.showMiddleNameInSearch = true;
		this.showLastNameInSearch = true;
		this.showPersonalIDInSearch = true;
		this.maxFoundUserRows = 20;
		this.maxFoundUserCols = 3;
		this.bundleIdentifer = null;
		this.processed = false;
		this.maintainedParameters = new Vector();
		this.personalIDLength = 10;
		this.firstNameLength = 10;
		this.middleNameLength = 10;
		this.lastNameLength = 10;
		this.stacked = true;
		this.firstLetterCaseInsensitive = true;
		this.skipResultsForOneFound = true;
		this.OwnFormContainer = true;
		this.showResetButton = true;
		this.showButtons = true;
		this.uniqueIdentifier = "unique";
		this.showOverFlowMessage = true;
		this.addedButtons = null;
		this.showSearchParamsAfterSearch = false;
	}

	private void initStyleNames() {
		if (this.textFontStyleName == null) {
			this.textFontStyleName = getStyleName("Text");
		}
		if (this.headerFontStyleName == null) {
			this.headerFontStyleName = getStyleName("Header");
		}
		if (this.buttonStyleName == null) {
			this.buttonStyleName = getStyleName("Button");
		}
		if (this.warningStyleName == null) {
			this.warningStyleName = getStyleName("Warning");
		}
		if (this.interfaceStyleName == null) {
			this.interfaceStyleName = getStyleName("Interface");
		}
	}

	public void main(IWContext iwc) throws Exception {
		initStyleNames();
		this.iwrb = getResourceBundle(iwc);
		String message = null;
		try {
			process(iwc);
		} catch (RemoteException e) {
			e.printStackTrace();
			message =
				this.iwrb.getLocalizedString("usrch.service_available", "Search service not available");
		} catch (FinderException e) {
			e.printStackTrace();
			message = this.iwrb.getLocalizedString("usrch.no_user_found", "No user found");
		}
		Table T = new Table();
		T.add(presentateCurrentUserSearch(), 1, 1);
		if (!this.skipResultsForOneFound || this.hasManyUsers) {
			T.add(presentateFoundUsers(), 1, 2);
		}
		if (message != null) {
			Text tMessage = new Text(message);
			T.add(tMessage, 1, 3);
		}
		if (this.OwnFormContainer) {
			Form form = new Form();
			form.add(T);
			add(form);
		} else {
			add(T);
		}

	}

	public void process(IWContext iwc) throws FinderException, RemoteException {
		if (this.processed) {
			return;
		}
		if (iwc.isParameterSet(SEARCH_COMMITTED + this.uniqueIdentifier)) {
			processSearch(iwc);
		}
		else if (iwc.isParameterSet("usrch_user_id_" + this.uniqueIdentifier)) {
			this.userID = Integer.valueOf(iwc.getParameter("usrch_user_id_" + this.uniqueIdentifier));
		}
		if (this.userID != null) {
			try {
				UserHome home = (UserHome) IDOLookup.getHome(User.class);
				this.user = home.findByPrimaryKey(this.userID);
			} catch (IDOLookupException e) {
				log(e);
				logWarning("No child found for userID: " + this.userID);
			}
		}
		this.processed = true;
	}

	private void processSearch(IWContext iwc)
		throws IDOLookupException, FinderException, RemoteException {
		UserHome home = (UserHome) IDOLookup.getHome(User.class);
		String first = iwc.getParameter("usrch_search_fname" + this.uniqueIdentifier);
		String middle = iwc.getParameter("usrch_search_mname" + this.uniqueIdentifier);
		String last = iwc.getParameter("usrch_search_lname" + this.uniqueIdentifier);
		String pid = iwc.getParameter("usrch_search_pid" + this.uniqueIdentifier);
		
		
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
		
		
		if (this.firstLetterCaseInsensitive) {
			if (first != null) {
				first = TextSoap.capitalize(first);
			}
			if (middle != null) {
				middle = TextSoap.capitalize(middle);
			}
			if (last != null) {
				last = TextSoap.capitalize(last);
			}
		}
		this.usersFound =
			home.findUsersByConditions(first, middle, last, pid, null, null, -1, -1, -1, -1, null ,null ,true, true);
		if (this.user == null && this.usersFound != null) {
			if (!this.usersFound.isEmpty()) {
				this.hasManyUsers = this.usersFound.size() > 1;
				if (!this.hasManyUsers) {
					this.user = (User) this.usersFound.iterator().next();
				}				
			} else {
				// No user found
			}
		}
	}

	private Table presentateCurrentUserSearch() {
		Table searchTable = new Table();
		int row = 1;
		int col = 1;
		String clearAction = "";
		if (this.showPersonalIDInSearch) {
			Text tPersonalID = new Text(this.iwrb.getLocalizedString("usrch_search_pid", "Personal ID"));
			//Text tPersonalID =this.getLocalizedSmallHeader("usrch_search_pid", "Personal ID");
			tPersonalID.setStyleClass(this.headerFontStyleName);
			//tPersonalID.setStyleClass(getSmallHeaderFontStyle());
			searchTable.add(tPersonalID, col, row);
			TextInput input = new TextInput("usrch_search_pid" + this.uniqueIdentifier);
			input.setStyleClass(this.interfaceStyleName);
			input.setLength(this.personalIDLength);
		// Empty user fields after search
		if (this.user != null && this.user.getPersonalID() != null && this.showSearchParamsAfterSearch) {
			input.setContent(this.user.getPersonalID());
		}
			if (this.stacked) {
				searchTable.add(input, col++, row + 1);
			}
			else {
				searchTable.add(input, ++col, row);
			}
			clearAction =
				clearAction + "this.form.usrch_search_pid" + this.uniqueIdentifier + ".value ='' ;";
		}
		if (this.showLastNameInSearch) {
			Text tLastName = new Text(this.iwrb.getLocalizedString("usrch_search_lname", "Last name"));
			tLastName.setStyleClass(this.headerFontStyleName);
			searchTable.add(tLastName, col, row);
			TextInput input = new TextInput("usrch_search_lname" + this.uniqueIdentifier);
			input.setStyleClass(this.interfaceStyleName);
			input.setLength(this.lastNameLength);
			// Empty user fields after search
			if (this.user != null && this.user.getLastName() != null && this.showSearchParamsAfterSearch) {
				input.setContent(this.user.getLastName());
			}
			if (this.stacked) {
				searchTable.add(input, col++, row + 1);
			}
			else {
				searchTable.add(input, ++col, row);
			}
			clearAction =
				clearAction + "this.form.usrch_search_lname" + this.uniqueIdentifier + ".value ='' ;";
		}
		if (this.showMiddleNameInSearch) {
			Text tMiddleName =
				new Text(this.iwrb.getLocalizedString("usrch_search_mname", "Middle name"));
			tMiddleName.setStyleClass(this.headerFontStyleName);
			searchTable.add(tMiddleName, col, row);
			TextInput input = new TextInput("usrch_search_mname" + this.uniqueIdentifier);
			input.setStyleClass(this.interfaceStyleName);
			input.setLength(this.middleNameLength);
			// Empty user fields after search
			if (this.user != null && this.user.getMiddleName() != null && this.showSearchParamsAfterSearch) {
				input.setContent(this.user.getMiddleName());
			}
			if (this.stacked) {
				searchTable.add(input, col++, row + 1);
			}
			else {
				searchTable.add(input, ++col, row);
			}
			clearAction =
				clearAction + "this.form.usrch_search_mname" + this.uniqueIdentifier + ".value ='' ;";
		}
		if (this.showFirstNameInSearch) {
			Text tFirstName = new Text(this.iwrb.getLocalizedString("usrch_search_fname", "First name"));
			tFirstName.setStyleClass(this.headerFontStyleName);
			searchTable.add(tFirstName, col, row);
			TextInput input = new TextInput("usrch_search_fname" + this.uniqueIdentifier);
			input.setStyleClass(this.interfaceStyleName);
			input.setLength(this.firstNameLength);
			// Empty user fields after search
			if (this.user != null && this.showSearchParamsAfterSearch) {
				input.setContent(this.user.getFirstName());
			}
			if (this.stacked) {
				searchTable.add(input, col++, row + 1);
			}
			else {
				searchTable.add(input, ++col, row);
			}
			clearAction =
				clearAction + "this.form.usrch_search_fname" + this.uniqueIdentifier + ".value ='' ;";
		}
		if (this.showButtons) {
			SubmitButton search = new SubmitButton(
													this.iwrb.getLocalizedImageButton(KEY_BUTTON_SEARCH, "Search"),
													SEARCH_COMMITTED + this.uniqueIdentifier, "true");

			search.setStyleClass(this.buttonStyleName);
			if (this.stacked) {
				searchTable.add(search, col++, row + 1);
			}
			else {
				searchTable.add(search, 1, row + 1);
			}
			if (this.showResetButton) {
				//SubmitButton reset = new SubmitButton(
				//											iwrb.getLocalizedString(KEY_BUTTON_RESET, "Reset"));

				SubmitButton reset = new SubmitButton(
														this.iwrb.getLocalizedImageButton(KEY_BUTTON_RESET, "Reset"));


				reset.setStyleClass(this.buttonStyleName);
				reset.setOnClick(clearAction + "return false;");
				searchTable.add(reset, col++, row + 1);
			}
			
		/*	SubmitButton back = new SubmitButton(
													iwrb.getLocalizedImageButton(KEY_BUTTON_BACK, "Back"),
													CentralPlacementEditor.PARAM_BACK, "true");

			back.setStyleClass(buttonStyleName);			
			searchTable.add(back, col++, row + 1);
		*/	
			
			if (this.addedButtons != null && !this.addedButtons.isEmpty()) {
				for (Iterator iter = this.addedButtons.iterator(); iter.hasNext();) {
					PresentationObject element = (PresentationObject) iter.next();
					if (this.stacked) {
						searchTable.add(element, col++, row + 1);
					}
					else {
						searchTable.add(element, 1, row + 1);
					}
				}

			}
		}
		return searchTable;
	}

	private Table presentateFoundUsers() {
		Table T = new Table();
		if (this.usersFound != null && !this.usersFound.isEmpty()) {
			Iterator iter = this.usersFound.iterator();
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
				if (++row == this.maxFoundUserRows) {
					col++;
					colAdd += 2;
					row = 1;
				}
				if (col == this.maxFoundUserCols) {
					break;
				}
			}

			if (this.showOverFlowMessage && iter.hasNext()) {
				int lastRow = T.getRows() + 1;
				T.mergeCells(1, lastRow, this.maxFoundUserCols, lastRow);
				Text tOverflowMessage =
					new Text(
						this.iwrb.getLocalizedString(
							"usrch_overflow_message",
							"There are more hits in your search than shown, you have to narrow down your searchcriteria"));
				tOverflowMessage.setStyleClass(this.warningStyleName);
				T.add(tOverflowMessage, 1, lastRow);
			}
		}
		return T;
	}

	private void addParameters(Link link) {
		Parameter element;
		for (Iterator iter = this.maintainedParameters.iterator();
			iter.hasNext();
			link.addParameter(element)) {
			element = (Parameter) iter.next();
		}

	}

	public void setShowFirstNameInSearch(boolean b) {
		this.showFirstNameInSearch = b;
	}

	public void setShowLastNameInSearch(boolean b) {
		this.showLastNameInSearch = b;
	}

	public void setShowMiddleNameInSearch(boolean b) {
		this.showMiddleNameInSearch = b;
	}

	public void setShowPersonalIDInSearch(boolean b) {
		this.showPersonalIDInSearch = b;
	}

	public boolean isHasManyUsers() {
		return this.hasManyUsers;
	}

	public int getMaxFoundUserCols() {
		return this.maxFoundUserCols;
	}

	public int getMaxFoundUserRows() {
		return this.maxFoundUserRows;
	}

	public User getUser() {
		return this.user;
	}

	public Collection getUsersFound() {
		return this.usersFound;
	}

	public void setMaxFoundUserCols(int cols) {
		this.maxFoundUserCols = cols;
	}

	public void setMaxFoundUserRows(int rows) {
		this.maxFoundUserRows = rows;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setUsersFound(Collection collection) {
		this.usersFound = collection;
	}

	public void maintainParameter(Parameter parameter) {
		this.maintainedParameters.add(parameter);
	}

	public String getBundleIdentifier() {
		if (this.bundleIdentifer != null) {
			return this.bundleIdentifer;
		}
		else {
			return BUNDLE_IDENTIFIER;
		}
	}

	public void setBundleIdentifer(String string) {
		this.bundleIdentifer = string;
	}

	public Parameter getUniqueUserParameter(Integer userID) {
		return new Parameter(getUniqueUserParameterName(this.uniqueIdentifier), userID.toString());
	}

	public static String getUniqueUserParameterName(String uniqueIdentifier) {
		return "usrch_user_id_" + uniqueIdentifier;
	}

	public Map getStyleNames() {
		HashMap map = new HashMap();
		map.put("Header", this.headerFontStyle);
		map.put("Text", this.textFontStyle);
		map.put("Button", this.buttonStyle);
		map.put("Warning", this.warningFontStyle);
		map.put("Interface", this.interfaceStyle);
		return map;
	}

	public int getFirstNameLength() {
		return this.firstNameLength;
	}

	public String getHeaderFontStyle() {
		return this.headerFontStyle;
	}

	public int getLastNameLength() {
		return this.lastNameLength;
	}

	public int getMiddleNameLength() {
		return this.middleNameLength;
	}

	public int getPersonalIDLength() {
		return this.personalIDLength;
	}

	public boolean isShowFirstNameInSearch() {
		return this.showFirstNameInSearch;
	}

	public boolean isShowLastNameInSearch() {
		return this.showLastNameInSearch;
	}

	public boolean isShowMiddleNameInSearch() {
		return this.showMiddleNameInSearch;
	}

	public boolean isShowPersonalIDInSearch() {
		return this.showPersonalIDInSearch;
	}

	public boolean isStacked() {
		return this.stacked;
	}

	public String getTextFontStyle() {
		return this.textFontStyle;
	}

	public void setFirstNameLength(int length) {
		this.firstNameLength = length;
	}

	public void setHeaderFontStyle(String style) {
		this.headerFontStyle = style;
	}

	public void setLastNameLength(int length) {
		this.lastNameLength = length;
	}

	public void setMiddleNameLength(int length) {
		this.middleNameLength = length;
	}

	public void setPersonalIDLength(int length) {
		this.personalIDLength = length;
	}

	public void setStacked(boolean flag) {
		this.stacked = flag;
	}

	public void setTextFontStyle(String style) {
		this.textFontStyle = style;
	}

	public boolean isOwnFormContainer() {
		return this.OwnFormContainer;
	}

	public void setOwnFormContainer(boolean flag) {
		this.OwnFormContainer = flag;
	}

	public String getUniqueIdentifier() {
		return this.uniqueIdentifier;
	}

	public void setUniqueIdentifier(String identifier) {
		this.uniqueIdentifier = identifier;
	}

	public String getButtonStyle() {
		return this.buttonStyle;
	}

	public String getButtonStyleName() {
		return this.buttonStyleName;
	}

	public String getHeaderFontStyleName() {
		return this.headerFontStyleName;
	}

	public String getTextFontStyleName() {
		return this.textFontStyleName;
	}

	public void setButtonStyle(String string) {
		this.buttonStyle = string;
	}

	public void setButtonStyleName(String string) {
		this.buttonStyleName = string;
	}

	public void setHeaderFontStyleName(String string) {
		this.headerFontStyleName = string;
	}

	public void setTextFontStyleName(String string) {
		this.textFontStyleName = string;
	}

	public boolean isSkipResultsForOneFound() {
		return this.skipResultsForOneFound;
	}

	public void setSkipResultsForOneFound(boolean flag) {
		this.skipResultsForOneFound = flag;
	}

	public boolean isShowResetButton() {
		return this.showResetButton;
	}

	public void setShowResetButton(boolean b) {
		this.showResetButton = b;
	}

	public boolean isShowOverFlowMessage() {
		return this.showOverFlowMessage;
	}

	public void setShowOverFlowMessage(boolean b) {
		this.showOverFlowMessage = b;
	}

	public void addButtonObject(PresentationObject obj) {
		if (this.addedButtons == null) {
			this.addedButtons = new Vector();
		}
		this.addedButtons.add(obj);
	}

	public boolean isShowButtons() {
		return this.showButtons;
	}

	public void setShowButtons(boolean b) {
		this.showButtons = b;
	}

	/**
	 * @return Returns the showSearchParamsAfterSearch.
	 */
	public boolean isShowSearchParamsAfterSearch() {
		return this.showSearchParamsAfterSearch;
	}

	/**
	 * @param showSearchParamsAfterSearch The showSearchParamsAfterSearch to set.
	 */
	public void setShowSearchParamsAfterSearch(boolean showSearchParamsAfterSearch) {
		this.showSearchParamsAfterSearch = showSearchParamsAfterSearch;
	}

}
