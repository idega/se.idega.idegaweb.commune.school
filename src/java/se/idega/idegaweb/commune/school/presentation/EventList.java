package se.idega.idegaweb.commune.school.presentation;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.FinderException;

import se.idega.idegaweb.commune.message.data.PrintMessage;
import se.idega.idegaweb.commune.message.data.PrintMessageHome;
import se.idega.idegaweb.commune.message.data.PrintedLetterMessage;
import se.idega.idegaweb.commune.message.data.PrintedLetterMessageHome;
import se.idega.idegaweb.commune.presentation.ColumnList;
import se.idega.idegaweb.commune.presentation.CommuneBlock;
import se.idega.idegaweb.commune.printing.business.DocumentBusiness;
import se.idega.idegaweb.commune.school.business.SchoolCommuneSession;

import com.idega.business.IBOLookup;
import com.idega.business.IBORuntimeException;
import com.idega.core.file.data.ICFile;
import com.idega.core.file.data.ICFileHome;
import com.idega.data.IDOHome;
import com.idega.data.IDOLookup;
import com.idega.presentation.ExceptionWrapper;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.HorizontalRule;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.BackButton;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.GenericButton;
import com.idega.presentation.ui.PrintButton;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.util.IWTimestamp;

/**
 * Title: EventList
 * Description: A class to view and manage PrintedLetterMessage in the idegaWeb Commune application
 * Copyright:    Copyright idega Software (c) 2002
 * Company:	idega Software
 * @author <a href="mailto:roar@idega.is">roar</a>
 * @version $Id: EventList.java,v 1.9 2004/01/13 13:09:24 jonas Exp $
 * @since 17.3.2003 
 */

public class EventList extends CommuneBlock {

	private final static String IW_BUNDLE_IDENTIFIER =
		"se.idega.idegaweb.commune";

	private final static int ACTION_VIEW_MESSAGE_LIST = 1;
	private final static int ACTION_VIEW_MESSAGE = 2;
	private final static int ACTION_PRINT_MESSAGE = 7;
	private final static int ACTION_PRINT_SELECTED = 8;

	private final static String PARAM_MESSAGE_ID = "prv_id";
	private final static String PARAM_PRINT_MSG = "prv_pr_msg";
	private final static String PARAM_LETTER_TYPE = "prv_let_tp";
	private final static String PARAM_VIEW_MESSAGE = "prm_view_msg";

	private final static String PRM_STAMP_U_FROM = "prv_ufrm";
	private final static String PRM_STAMP_U_TO = "prv_uto";
	private final static String PRM_P_COUNT = "prv_pcnt";
	private final static String PRM_U_COUNT = "prv_ucnt";
	private final static String PRM_U_CHK = "prv_uchk";

	//private final static String PRM_CURSOR_P = "prv_crs_p";
	private final static String PRM_CURSOR_U = "prv_crs_u";

	private final static String PRM_PRINT_SELECTED = "prv_pr_sel";

	private final static String PRM_SSN = "prv_ssn";
	private final static String PRM_MSGID = "prv_msgid";

	private final static String LOCALE_DATE_FROM = "eventlist.date_from";
	private final static String LOCALE_DATE_TO = "eventlist.date_to";
	private final static String LOCALE_SSN = "eventlist.ssn";
	private final static String LOCALE_MSGID = "eventlist.msgid";
	private final static String LOCALE_DATE_CREATED = "eventlist.created";
	private final static String LOCALE_EVENT = "eventlist.event";
	private final static String LOCALE_RECEIVER = "eventlist.receiver";
	private final static String LOCALE_LAST = "eventlist.last";
	private final static String LOCALE_NEXT = "eventlist.next";
	private final static String LOCALE_MSGID_INT = "eventlist.msgid_int";	

	public boolean showTypesAsDropdown = false;
	private String currentType = ""; 
	//private int msgID = -1;
	private IWTimestamp today = IWTimestamp.RightNow(),
	uFrom = null, uTo = null;
	private int defaultDays = 7;
	public int defaultShown = 25;
	//private int cursor_p = 0;
	private int cursor_u = 0;
	private int count_p = 25;
	private int count_u = 25;

	private String searchSsn = "";
	private String searchMsgId = "";

	private Table mainTable = null;
	
	private IWContext _iwc = null;

	public EventList() {
	}

	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}

	public void main(IWContext iwc) {
		//this.debugParameters(iwc);
		_iwc = iwc;
		this.setResourceBundle(getResourceBundle(iwc));
		if (iwc.isLoggedOn()) {
			try {
				initDates(iwc);
				initCursors(iwc);
				initSearch(iwc);
				int action = parseAction(iwc);
				switch (action) {
					case ACTION_VIEW_MESSAGE_LIST :
						viewMessages(iwc);
						break;
					case ACTION_VIEW_MESSAGE :
						viewMessage(iwc);
						break;
					case ACTION_PRINT_SELECTED :
						printSelected(iwc);
						break;

					default :
						break;
				}
				super.add(mainTable);
			} catch (Exception e) {
				super.add(new ExceptionWrapper(e, this));
			}
		} else {
			add(
				getLocalizedHeader(
					"printdoc.not_logged_on",
					"You must be logged on to use this function"));
		}
	}

	public void add(PresentationObject po) {
		if (mainTable == null) {
			mainTable = new Table();
			mainTable.setCellpadding(14);
			mainTable.setCellspacing(0);
			mainTable.setColor(getBackgroundColor());
			mainTable.setWidth(getWidth());
		}
		mainTable.add(po);
	}

	private int parseAction(IWContext iwc) throws Exception {
		int action = ACTION_VIEW_MESSAGE_LIST;

		if (iwc.isParameterSet(PARAM_VIEW_MESSAGE)) {
			action = ACTION_VIEW_MESSAGE;
		} else if (iwc.isParameterSet(PARAM_PRINT_MSG)) {
			action = ACTION_PRINT_MESSAGE;
		} else if (iwc.isParameterSet(PRM_PRINT_SELECTED)) {
			action = ACTION_PRINT_SELECTED;
		}


		/*if (iwc.isParameterSet(PARAM_MESSAGE_ID)) {
			msgID = Integer.parseInt(iwc.getParameter(PARAM_MESSAGE_ID));
		}*/

		//	add(new Text("Action: " + action + "<br>"));
		return action;
	}

	private void initDates(IWContext iwc) throws Exception {
		uFrom = IWTimestamp.RightNow();
		uFrom.addDays(-defaultDays);
		uTo = IWTimestamp.RightNow();

		if (iwc.isParameterSet(PRM_STAMP_U_FROM))
			uFrom = new IWTimestamp(iwc.getParameter(PRM_STAMP_U_FROM));

		if (iwc.isParameterSet(PRM_STAMP_U_TO))
			uTo = new IWTimestamp(iwc.getParameter(PRM_STAMP_U_TO));
	}

	private void initSearch(IWContext iwc) throws Exception {
		if (iwc.isParameterSet(PRM_SSN)) {
			searchSsn = iwc.getParameter(PRM_SSN);
		}
		if (iwc.isParameterSet(PRM_MSGID)) {
			searchMsgId = iwc.getParameter(PRM_MSGID);
		}

	}

	private void initCursors(IWContext iwc) throws Exception {
		/*if (iwc.isParameterSet(PRM_CURSOR_P))
			cursor_p = Integer.parseInt(iwc.getParameter(PRM_CURSOR_P));*/
		if (iwc.isParameterSet(PRM_CURSOR_U))
			cursor_u = Integer.parseInt(iwc.getParameter(PRM_CURSOR_U));
		if (iwc.isParameterSet(PRM_P_COUNT))
			count_p = Integer.parseInt(iwc.getParameter(PRM_P_COUNT));
		if (iwc.isParameterSet(PRM_U_COUNT))
			count_u = Integer.parseInt(iwc.getParameter(PRM_U_COUNT));
	}



	private void viewMessage(IWContext iwc) throws Exception {
		String id = iwc.getParameter(PARAM_MESSAGE_ID);
		if (id == null) { //no messages selected
			addMessagesList(iwc);

		} else {
			viewMessages(new String[] { id });
		}
	}

	private void printSelected(IWContext iwc) throws Exception {
		String[] ids = iwc.getParameterValues(PRM_U_CHK);
		if (ids == null || ids.length == 0) { //no messages selected
			addMessagesList(iwc);

		} else if (ids != null && ids.length > 0) {
			viewMessages(ids);
		}
	}

	private void viewMessages(String[] ids)
		throws FinderException {
		Table layout = new Table();
		Collection selectedLetters = getPrintedLetter().findLetters(ids);

		Iterator iter = selectedLetters.iterator();

		//int bulkId;
		int layoutRow = 1;
		while (iter.hasNext()) {
			int row = 1;

			Table message = new Table(2, 8);
			PrintedLetterMessage msg = (PrintedLetterMessage) iter.next();

			addField(
				message,
				localize(LOCALE_MSGID, "Message Id"),
				String.valueOf(msg.getNodeID()),
				row++);
			addField(
				message,
				localize(LOCALE_DATE_CREATED, "Message created"),
				msg.getCreated().toString(),
				row++);
			//		addField(message, "From", msg.getSenderName(), row++);
			addField(
				message,
				localize(LOCALE_RECEIVER, "Receiver"),
				msg.getOwner().getName(),
				row++);
			addField(
				message,
				localize(LOCALE_SSN, "SSN"),
				msg.getOwner().getPersonalID(),
				row++);
			//		addField(layout, "Type:", msg.getMessageType(), row++);
			//		addField(layout, "Type:", msg.msg.getLetterType(), row++);
			addField(
				message,
				localize(LOCALE_EVENT, "Event"),
				msg.getSubject(),
				row++);
			message.add(new Text(""), 1, row++);
			//Body
			message.mergeCells(1, row, 2, row);
			message.add(getSmallText(msg.getBody()), 1, row++);
			
			int fileID = createPrintableMessage(msg);
			System.out.println("parent case for EventList message is " + msg.getParentCase());
			if(fileID!=-1) {
				System.out.println("adding link for pdf");
				Link viewLink = new Link(localize("printdoc.view", "View"));
				viewLink.setFile(fileID);
				message.add("", 1, row);
				message.add(viewLink, 2, row++);
			} else {
				System.out.println("Could not create pdf, no link added");
			}

			layout.add(message, 1, layoutRow++);

			if (iter.hasNext()) {
				layout.add(new HorizontalRule(), 1, layoutRow);
				layout.setHeight(layoutRow++, 1, "70");
			}
		}

		Table toolbar = new Table();
		toolbar.setAlignment(Table.HORIZONTAL_ALIGN_RIGHT);
		toolbar.setCellpadding(2);
		GenericButton printBtn = getButton(new PrintButton(localize("school.print","Print")));
		GenericButton backBtn = getButton(new BackButton());

		toolbar.add(backBtn, 1, 1);
		toolbar.add(printBtn, 2, 1);

		layout.add(toolbar, 1, layoutRow);
		add(layout);
	}
	
	/* creates the pdf file and returns the ICFile identifier */
	private int createPrintableMessage(PrintMessage msg) {
		try {
			System.out.println("EventList message Case is \"" + msg.getParentCase() + "\"");
			DocumentBusiness docBiz = getDocumentBusiness();
			String userName = _iwc.getCurrentUser().getName();
			String fileName = "EventListLetter-" + msg.getNodeID() + "-" + _iwc.getCurrentLocaleId() + ".pdf";
			ICFile file = null;
			try {
				file = getICFileHome().findByFileName(fileName);
				System.out.println("found pdf file " + fileName);
			} catch(FinderException e) {
				// ok, just means we need to create this file
			}
			if(file==null) {
				System.out.println("creating pdf file " + fileName);
				return docBiz.writePDF(msg, _iwc.getCurrentUser(), fileName, _iwc.getCurrentLocale(), false);
			} else {
				System.out.println("Using existing pdf");
				return Integer.parseInt(file.getPrimaryKey().toString());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}
	
	private DocumentBusiness getDocumentBusiness() throws RemoteException {
		return (DocumentBusiness) com.idega.business.IBOLookup.getServiceInstance(
			_iwc,
			DocumentBusiness.class);
	}
	
	private ICFileHome getICFileHome() {
		try {
			return (ICFileHome) getIDOHome(ICFile.class);
		} catch (Exception e) {
			throw new IBORuntimeException(e);
		}
	}

	private void addField(Table layout, String label, String value, int row) {
		Text lbl = getSmallText(label + ":");
		lbl.setStyleAttribute("font-weight:bold");
		layout.add(lbl, 1, row);
		layout.add(getSmallText(value), 2, row);
		layout.setColor(1, row, "grey");
		layout.setColor(2, row, "grey");
	}

	public Collection getPrintedMessagesByPrimaryKeys(
		String[] primaryKeys)
		throws FinderException {
		PrintMessageHome msgHome = null;
		PrintMessage msg;
		ArrayList coll = new ArrayList(primaryKeys.length);
		msgHome = getPrintedLetterMessageHome();
		if (msgHome != null) {
			for (int i = 0; i < primaryKeys.length; i++) {
				msg = (PrintMessage) msgHome.findByPrimaryKey(primaryKeys[i]);
				coll.add(msg);
			}
		}
		return coll;
	}

	public PrintedLetterMessageHome getPrintedLetterMessageHome() {
		try {
			return (PrintedLetterMessageHome) getIDOHome(
				PrintedLetterMessage.class);
		} catch (Exception e) {
			throw new IBORuntimeException(e);
		}
	}

	protected IDOHome getIDOHome(Class beanClass) throws RemoteException {
		return IDOLookup.getHome(beanClass);
	}

	private void viewMessages(IWContext iwc) throws Exception {
		addMessagesList(iwc);
	}

	private DropdownMenu getCountDrop(String name, int selected) {
		DropdownMenu drp = new DropdownMenu(name);
		//	drp.addMenuElement(String.valueOf(3)); //only for test
		drp.addMenuElement(String.valueOf(10));
		drp.addMenuElement(String.valueOf(25));
		drp.addMenuElement(String.valueOf(50));
		drp.addMenuElement(String.valueOf(75));
		drp.addMenuElement(String.valueOf(100));
		drp.addMenuElement(100000, localize("printdoc.all", "All"));
		drp.setSelectedElement(selected);
		return drp;
	}

	private PresentationObject getSearchForm(IWContext iwc) {

		Table T = new Table();

		DateInput from = new DateInput(PRM_STAMP_U_FROM);
		from = (DateInput) getStyledInterface(from);
		from.setYearRange(today.getYear() - 5, today.getYear() + 2);

		DateInput to = new DateInput(PRM_STAMP_U_TO);
		to = (DateInput) getStyledInterface(to);
		to.setYearRange(today.getYear() - 5, today.getYear() + 2);

		from.setDate(uFrom.getDate());
		to.setDate(uTo.getDate());

		TextInput msgid = new TextInput(PRM_MSGID);
		msgid = (TextInput) getStyledInterface(msgid);
		if (iwc.getParameter(PRM_MSGID) != null) {
			msgid.setValue(iwc.getParameter(PRM_MSGID));
		}
		msgid.setAsIntegers(localize(LOCALE_MSGID_INT, "Message id must be an integer."));
		

		TextInput ssn = new TextInput(PRM_SSN);
		ssn = (TextInput) getStyledInterface(ssn);
		if (iwc.getParameter(PRM_SSN) != null) {
			ssn.setValue(iwc.getParameter(PRM_SSN));
		}

		SubmitButton search =
			new SubmitButton(
				localize(
					"printdoc.fetch",
					"Fetch"));
		search = (SubmitButton) getButton(search);

		T.add(
			getHeader(
				localize(
					LOCALE_DATE_FROM,
					"From")
					+ ":"),
			1,
			1);
		T.mergeCells(2, 1, 8, 1);
		T.add(from, 2, 1);

		T.add(
			getHeader(
				localize(LOCALE_DATE_TO, "To")
					+ ":"),
			1,
			2);
		T.mergeCells(2, 2, 8, 2);
		T.add(to, 2, 2);

		T.add(
			getHeader(
				localize(
					LOCALE_MSGID,
					"Message Id")
					+ ":"),
			1,
			3);
		T.add(msgid, 2, 3);
		T.add(
			getHeader(
				localize(LOCALE_SSN, "SSN")
					+ ":"),
			4,
			3);
		T.add(ssn, 5, 3);
		//T.add(getHeader(localize("printdoc.count","Count")),5,1);
		T.setWidth(6, 3, 50);
		T.add(getStyledInterface(getCountDrop(PRM_U_COUNT, count_u)), 7, 3);
		T.add(search, 8, 3);

		T.setStyleAttribute("border-bottom: medium solid black");
		T.setStyleAttribute("border-top: medium solid black");
		//	T.setName("searchtable");
		//	T.setTopLine(true);
		//	T.setLeftLine(true);
		//	T.setBottomLine(true);
		T.setHeight(75);
		return T;
	}

	private PresentationObject getPrintButton() {
		Table T = new Table();
		T.setAlignment(Table.HORIZONTAL_ALIGN_RIGHT);
		T.setCellpadding(2);
		SubmitButton print =
			new SubmitButton(
				PRM_PRINT_SELECTED,
				localize(
					"printdoc.print",
					"Print"));
		print = (SubmitButton) getButton(print);
		T.add(print, 1, 1);
		return T;
	}

	private PresentationObject getCursorLinks(
		int totalsize,
		int cursor,
		String cursorPrm,
		int step) {
		Table T = new Table();
		T.setAlignment(Table.HORIZONTAL_ALIGN_RIGHT);
		T.setCellpadding(2);
		if (cursor > 0) {
			Link prev = new Link(localize(LOCALE_LAST, "last") + "  " + step);
			prev.addParameter(PARAM_LETTER_TYPE, currentType);
			prev.addParameter(cursorPrm, String.valueOf(cursor - step));
			addDateParametersToLink(prev);
			T.add(prev, 1, 1);
		}
		if (cursor <= (totalsize - step)) {
			Link next = new Link(localize(LOCALE_NEXT, "next") + "  " + step);
			next.addParameter(PARAM_LETTER_TYPE, currentType);
			next.addParameter(cursorPrm, String.valueOf(cursor + step));
			addDateParametersToLink(next);
			T.add(next, 3, 1);
		}

		return T;
	}

	private void addDateParametersToLink(Link link) {
		//		link.addParameter(PRM_STAMP_P_FROM,pFrom.toString());
		link.addParameter(PRM_STAMP_U_FROM, uFrom.toString());
		//		link.addParameter(PRM_STAMP_P_TO,pTo.toString());
		link.addParameter(PRM_STAMP_U_TO, uTo.toString());
		link.addParameter(PRM_U_COUNT, count_u);
		link.addParameter(PRM_P_COUNT, count_p);
		link.addParameter(PRM_SSN, searchSsn);
		link.addParameter(PRM_MSGID, searchMsgId);
	}


	private void addMessagesList(IWContext iwc) throws Exception {
		Form uForm = new Form();

		Table uT = new Table();
		uForm.add(uT);
		add(uForm);
		ColumnList unPrintedLetterDocs = new ColumnList(6);

		int urow = 1;
		int schoolID = getSchoolSession(iwc).getSchoolID();
		Collection unprintedLetters =
			getPrintedLetter().findAllLettersBySchool(
				schoolID,
				searchSsn,
				searchMsgId,
				uFrom,
				uTo);

		uT.add(getSearchForm(iwc), 1, urow++);
		uT.setStyle(1, urow - 1, "padding-bottom", "15px");

		uT.add(unPrintedLetterDocs, 1, urow++);
		uT.add(getPrintButton(), 1, urow++);
		uT.add(
			getCursorLinks(
				unprintedLetters.size(),
				cursor_u,
				PRM_CURSOR_U,
				count_u),
			1,
			urow++);

		unPrintedLetterDocs.setWidth(Table.HUNDRED_PERCENT);
		unPrintedLetterDocs.setBackroundColor("#e0e0e0");
		unPrintedLetterDocs.setHeader(localize(LOCALE_EVENT, "Event"), 1);
		unPrintedLetterDocs.setHeader(localize(LOCALE_MSGID, "Message Id"), 2);
		unPrintedLetterDocs.setHeader(localize(LOCALE_RECEIVER, "Receiver"), 3);
		unPrintedLetterDocs.setHeader(localize(LOCALE_SSN, "SSN"), 4);
		unPrintedLetterDocs.setHeader(
			localize(LOCALE_DATE_CREATED, "Message created"),
			5);

		CheckBox checkAll = new CheckBox("checkall");
		checkAll.setToCheckOnClick(PRM_U_CHK, true);
		unPrintedLetterDocs.setHeader(checkAll, 6);

		Iterator iter = unprintedLetters.iterator();
		int count = cursor_u + 1;
		int ccu = count_u + cursor_u;
		if (cursor_u > 0) {
			while (iter.hasNext() && cursor_u > 0) {
				iter.next();
				cursor_u--;
			}
		}

		//int bulkId;
		while (iter.hasNext() && count <= ccu) {
			PrintedLetterMessage msg = (PrintedLetterMessage) iter.next();

			Link link = new Link(msg.getSubject());
			link.addParameter(PARAM_VIEW_MESSAGE, "true");
			link.addParameter(PARAM_MESSAGE_ID, msg.getNodeID());
			unPrintedLetterDocs.add(link);
			unPrintedLetterDocs.add(String.valueOf(msg.getNodeID()));

			unPrintedLetterDocs.add(msg.getOwner().getName());
			unPrintedLetterDocs.add(" " + msg.getOwner().getPersonalID());
			unPrintedLetterDocs.add(msg.getCreated().toString());

			CheckBox box =
				new CheckBox(PRM_U_CHK, msg.getPrimaryKey().toString());
			uForm.addParameter(PARAM_LETTER_TYPE, currentType);
			unPrintedLetterDocs.add(box);
			count++;
		}

	}



//	private DocumentBusiness getDocumentBusiness(IWContext iwc)
//		throws Exception {
//		return (
//			DocumentBusiness) com.idega.business.IBOLookup.getServiceInstance(
//			iwc,
//			DocumentBusiness.class);
//	}


	public void setShowTypesInDropdown(boolean showInDropdown) {
		this.showTypesAsDropdown = showInDropdown;
	}

	public void setDefaultNumberOfShownMessages(int number) {
		this.defaultShown = number;
	}

	public void setDefaultNumberOfShownDays(int number) {
		this.defaultDays = number;
	}

	private PrintedLetterMessageHome getPrintedLetter() {
		try {
			return (PrintedLetterMessageHome) IDOLookup.getHome(
				PrintedLetterMessage.class);
		} catch (RemoteException e) {
			return null;
		}
	}

	private SchoolCommuneSession getSchoolSession(IWContext iwc)
		throws RemoteException {
		return (SchoolCommuneSession) IBOLookup.getSessionInstance(
			iwc,
			SchoolCommuneSession.class);
	}

}
