package se.idega.idegaweb.commune.school.presentation;

import com.idega.business.IBOLookup;
import com.idega.presentation.*;
import com.idega.presentation.text.*;
import com.idega.presentation.ui.*;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.User;
import java.rmi.RemoteException;
import java.util.*;
import javax.ejb.*;
import se.idega.idegaweb.commune.presentation.CommuneBlock;
import se.idega.idegaweb.commune.school.business.*;
import se.idega.idegaweb.commune.school.data.SchoolChoiceReminder;

/**
 * SchoolChoiceReminderView is an IdegaWeb block that registers and handles
 * reminder broadcasts to citizens who should make a school choice. It is based
 * on session ejb classes in {@link se.idega.idegaweb.commune.school.business}
 * and entity ejb classes in {@link se.idega.idegaweb.commune.school.data}.
 * <p>
 * <p>
 * Last modified: $Date: 2002/12/29 13:49:39 $ by $Author: staffan $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.17 $
 * @see javax.ejb
 */
public class SchoolChoiceReminderView extends CommuneBlock {
    private static final String PREFIX = "scr_";
    public static final String ACTION_KEY = PREFIX + "action";
	private final static String CONFIRM_ENTER_DEFAULT = "Din påminnelse är nu registrerad";
	private final static String GOBACKTOMYPAGE_DEFAULT = "Tillbaka till Min sida";
    private final static String CASE_ID_DEFAULT = "Nr.";
    public final static String CASE_ID_KEY = PREFIX + "case_id";
    private final static String CONFIRM_ENTER_KEY = PREFIX + "confirm_enter_reminder";
    private final static String GOBACKTOMYPAGE_KEY = PREFIX + "goBackToMyPage";
    private static final String ADDRESS_DEFAULT = "Adress";
    private static final String ADDRESS_KEY = PREFIX + "address";
    private static final String CHILDREN_COUNT_KEY = PREFIX + "children_count";
    private static final String CONTINUE_DEFAULT = "Fortsätt";
    private static final String CONTINUE_KEY = PREFIX + "continue";
    private static final String CREATE_DEFAULT = "Registrera en påminnelse";
    private static final String CREATE_KEY = PREFIX + "create";
    private static final String CUSTOM_TEXT_DEFAULT = "Egen påminnelsetext";
    private static final String CUSTOM_TEXT_KEY = PREFIX + "custom_text";
    private static final String ERROR_FIELD_CAN_NOT_BE_EMPTY_DEFAULT = "Fältet måste fyllas i";
    private static final String ERROR_FIELD_CAN_NOT_BE_EMPTY_KEY = PREFIX + "error_field_can_not_be_empty";
    private static final String EVENT_DATE_DEFAULT = "Utskicksdatum";
    private static final String EVENT_DATE_KEY = PREFIX + "event_date";
    private static final String GENERATE_LETTER_KEY = PREFIX + "generate_letter";
    private static final String PARENT_NAME_DEFAULT = "Vårdnadshavare";
    private static final String PARENT_NAME_KEY = PREFIX + "parent_name";
    private static final String REMINDER_DATE_DEFAULT = "Visa som ärende";
    private static final String REMINDER_DATE_KEY = PREFIX + "reminder_date";
    private static final String REMINDER_TEXT1_DEFAULT = "Text 1 som kommer från Kristina Lundin.";
    private static final String REMINDER_TEXT1_KEY = PREFIX + "reminder_text1";
    private static final String REMINDER_TEXT2_DEFAULT = "Text 2 som kommer från Kristina Lundin.";
    private static final String REMINDER_TEXT2_KEY = PREFIX + "reminder_text2";
    private static final String REMINDER_TEXT_DEFAULT = "Påminnelsetext";
    private static final String REMINDER_TEXT_KEY = PREFIX + "reminder_text";
    private static final String SCHOOLCHOICEREMINDER_DEFAULT = "Påminnelse om skolval";
    private static final String SCHOOLCHOICEREMINDER_KEY = PREFIX + "schoolchoicereminder";
    public static final String SHOW_DETAILS_KEY = PREFIX + "show_details";
    private static final String SSN_DEFAULT = "Personnummer";
    private static final String SSN_KEY = PREFIX + "ssn";
    private static final String STUDENT_LIST_KEY = PREFIX + "student_list";
    private static final String STUDENT_NAME_DEFAULT = "Elev";
    private static final String STUDENT_NAME_KEY = PREFIX + "student_name";
    private static final String DELETE_DEFAULT = "Radera";
    private static final String DELETE_KEY = PREFIX + "delete";
    private static final String CANCEL_DEFAULT = "Avbryt";
    private static final String CANCEL_KEY = PREFIX + "cancel";
    private static final String ACTIVE_REMINDERS_DEFAULT = "Aktiva påminnelser";
    private static final String ACTIVE_REMINDERS_KEY = PREFIX + "active_reminders";
    private static final String NEW_REMINDER_DEFAULT = "Skapa ny påminnelse";
    private static final String NEW_REMINDER_KEY = PREFIX + "new_reminder";


	/**
	 * @param iwc session data like user info etc.
	 */
	public void main(final IWContext iwc)
        throws RemoteException, CreateException, FinderException {
		setResourceBundle (getResourceBundle(iwc));
        final String action = iwc.getParameter (ACTION_KEY);

        if (action != null && action.equals (CREATE_KEY)) {
            createReminder (iwc);
        } else if (action != null && action.equals (SHOW_DETAILS_KEY)) {
            showDetails (iwc);
        } else if (action != null && action.equals (GENERATE_LETTER_KEY)) {
            generateLetter (iwc);
        } else {
            showAllReminders (iwc);
            showCreateReminderForm (iwc);
        }
    }

    private void showMainMenu (final IWContext iwc) {
		final Form form = new Form();
		final DropdownMenu dropdown = (DropdownMenu) getStyledInterface
                (new DropdownMenu (ACTION_KEY));
        /*
        dropdown.addMenuElement (SHOW_CREATE_FORM_KEY,
                                 localize (CREATE_KEY,
                                           CREATE_DEFAULT));
        dropdown.addMenuElement (LIST_ALL_KEY,
                                 localize (LIST_ALL_KEY, LIST_ALL_DEFAULT));
        */
		final SubmitButton submit = getSubmitButton (CONTINUE_KEY,
                                                     CONTINUE_DEFAULT);
		final Table table = new Table ();
		table.setWidth (getWidth ());
		table.setCellspacing (0);
		table.setCellpadding (0);
		int row = 1;
		table.add (getLocalizedSmallHeader
                   (SCHOOLCHOICEREMINDER_KEY, SCHOOLCHOICEREMINDER_DEFAULT), 1,
                   row++);
		table.add (dropdown, 1, row++);
		table.setHeight (row++, 12);

		table.add (submit, 1, row++);
		form.add (table);
		add (form);        
    }

    private void showCreateReminderForm (final IWContext iwc) {
		final Form form = new Form();
        final HiddenInput hidden = new HiddenInput (ACTION_KEY,
                                                    CREATE_KEY);
        form.add (hidden);
		final Table table = new Table();
		table.setWidth (getWidth ());
		table.setCellspacing (0);
		table.setCellpadding (getCellpadding ());
        int row = 1;
        table.add (getHeader(localize (NEW_REMINDER_KEY,
                                       NEW_REMINDER_DEFAULT)), 1, row++);
        table.setHeight (row++, 4);
		table.add(getSmallHeader (localize(REMINDER_TEXT_KEY, REMINDER_TEXT_DEFAULT)), 1, row);
		final RadioGroup radioGroup = new RadioGroup (REMINDER_TEXT_KEY);
        radioGroup.addRadioButton (localize (REMINDER_TEXT1_KEY, REMINDER_TEXT1_DEFAULT));
        radioGroup.addRadioButton (localize (REMINDER_TEXT2_KEY, REMINDER_TEXT2_DEFAULT));
        radioGroup.addRadioButton (localize (CUSTOM_TEXT_KEY, CUSTOM_TEXT_DEFAULT));
        radioGroup.setSelected (localize (REMINDER_TEXT1_KEY, REMINDER_TEXT1_DEFAULT));
		table.add (radioGroup, 1, row++);
		final TextArea textArea = new TextArea (CUSTOM_TEXT_KEY);
		textArea.setColumns (40);
		textArea.setRows (10);
		table.add (textArea, 1, row++);

		table.add(getSmallHeader(localize (EVENT_DATE_KEY, EVENT_DATE_DEFAULT)), 1, row);
		table.add(getSingleInput(iwc, EVENT_DATE_KEY, 8), 3, row++);
		table.add(getSmallHeader(localize (REMINDER_DATE_KEY, REMINDER_DATE_DEFAULT)), 1, row);
		table.add(getSingleInput(iwc, REMINDER_DATE_KEY, 8), 3, row++);

        table.setHeight (row++, 12);
        final SubmitButton submit = getSubmitButton (CREATE_KEY,
                                                     CREATE_DEFAULT);
		table.add(submit, 1, row++);
		form.add(table);
		add(form);
    }

    private void showAllReminders (final IWContext iwc) throws RemoteException,
                                                               FinderException {
		final Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(getWidth());
		add(table);
		final SchoolChoiceBusiness business = getSchoolChoiceBusiness (iwc);
        final SchoolChoiceReminder [] reminders
                = business.findAllSchoolChoiceReminders ();
        final Table messageList = new Table();
        messageList.setCellpadding(getCellpadding());
        messageList.setCellspacing(getCellspacing());
        messageList.setWidth(Table.HUNDRED_PERCENT);
        messageList.setColumns (5);
        int row = 1;
        int col = 1;
        Form form = new Form();
        form.add(messageList);
        messageList.setRowColor(row, getHeaderColor());
        messageList.add(getSmallHeader(localize (CASE_ID_KEY,
                                                 CASE_ID_DEFAULT)), col++, row);
        messageList.add(getSmallHeader(localize (REMINDER_TEXT_KEY,
                                                 REMINDER_TEXT_DEFAULT)), col++,
                        row);
        messageList.add(getSmallHeader(localize (EVENT_DATE_KEY,
                                                 EVENT_DATE_DEFAULT)), col++,
                        row);
        messageList.add(getSmallHeader(localize (REMINDER_DATE_KEY,
                                                 REMINDER_DATE_DEFAULT)), col++,
                        row);
        messageList.add(getSmallHeader(localize (DELETE_KEY, DELETE_DEFAULT)),
                        col++, row++);
        for (int i = 0; i < reminders.length; i++) {
            col = 1;
            messageList.setRowColor(row, (row % 2 == 0) ? getZebraColor1()
                              : getZebraColor2());
            final SchoolChoiceReminder reminder = reminders [i];
            final String id = "" + reminder.getPrimaryKey ();
            final Link idLink = getSmallLink (id);
            idLink.addParameter (CASE_ID_KEY, id);
            idLink.addParameter (ACTION_KEY, SHOW_DETAILS_KEY);
            messageList.add (idLink, col++, row);
            final String text = reminder.getText ();
            final String message = text.length () > 33
                    ? text.substring (0, 30) + "..." : text;
            messageList.add(message, col++, row);
            messageList.add("" + reminder.getEventDate (), col++, row);
            messageList.add("" + reminder.getReminderDate (), col++, row);
            row++;
        }
        table.add (getHeader(localize (ACTIVE_REMINDERS_KEY,
                                       ACTIVE_REMINDERS_DEFAULT)), 1, 1);
        table.setHeight (2, 12);
        table.add(form, 1, 3);
    }

    private void createReminder (final IWContext iwc) throws RemoteException,
                                                             CreateException {
		final SchoolChoiceBusiness business = getSchoolChoiceBusiness (iwc);
		business.createSchoolChoiceReminder
                (iwc.getParameter (REMINDER_TEXT_KEY),
                 Calendar.getInstance ().getTime (),
                 Calendar.getInstance ().getTime (), iwc.getCurrentUser ());

		final Text text1
                = new Text (getLocalizedString (CONFIRM_ENTER_KEY,
                                                CONFIRM_ENTER_DEFAULT));
		text1.setWidth (Table.HUNDRED_PERCENT);
		final Table table = new Table ();
		int row = 1;
		table.setWidth (getWidth ());
		table.setCellspacing (0);
		table.setCellpadding (0);
		table.add (text1, 1, row++);
		table.setHeight (row++, 12);
		table.add (getUserHomePageLink (iwc), 1, row++);
		add (table);
    }

    private void showDetails (final IWContext iwc)
        throws RemoteException, CreateException, FinderException {
		final Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(getWidth());
		final SchoolChoiceBusiness business = getSchoolChoiceBusiness (iwc);
        final int reminderId = Integer.parseInt (iwc.getParameter
                                                 (CASE_ID_KEY));
        final SchoolChoiceReminder  reminder
                = business .findSchoolChoiceReminder (reminderId);
        int row = 1;
        table.add(getSmallHeader(localize (REMINDER_TEXT_KEY,
                                           REMINDER_TEXT_DEFAULT)), 1, row++);
		table.setHeight (row++, 6);
        table.add(reminder.getText (), 1, row++);
		table.setHeight (row++, 12);
        table.add(getSmallHeader(localize (EVENT_DATE_KEY,
                                           EVENT_DATE_DEFAULT)), 1, row++);
		table.setHeight (row++, 6);
        table.add("" + reminder.getEventDate (), 1, row++);
		table.setHeight (row++, 12);
        table.add(getSmallHeader(localize (REMINDER_DATE_KEY,
                                           REMINDER_DATE_DEFAULT)), 1, row++);
		table.setHeight (row++, 6);
        table.add("" + reminder.getReminderDate (), 1, row++);
		table.setHeight (row++, 24);
        final Table submitTable = new Table ();
        submitTable.add (getStyledInterface
                         (new SubmitButton("Skriv ut till adresser nedan",
                                           ACTION_KEY, GENERATE_LETTER_KEY)),
                         1, 1);
        submitTable.add (getStyledInterface(new SubmitButton(localize (DELETE_KEY, DELETE_DEFAULT))), 2, 1);
        submitTable.add (getStyledInterface(new SubmitButton(localize (CANCEL_KEY, CANCEL_DEFAULT))), 3, 1);
        table.add (submitTable, 1, row++);
		table.setHeight (row++, 24);
        table.add (getStudentList (iwc, business, reminderId), 1, row++);
        final Form form = new Form ();
		form.maintainParameter(CASE_ID_KEY);
        form.add (table);
        add (form);
    }

    private Table getStudentList (final IWContext iwc,
                                  final SchoolChoiceBusiness business,
                                  final int reminderId) throws RemoteException {
        final Table studentList = new Table();
        studentList.setCellpadding(getCellpadding ());
        studentList.setCellspacing(getCellspacing ());
        studentList.setWidth(Table.HUNDRED_PERCENT);
        studentList.setColumns (5);
        int col = 2;
        int row = 1;
        studentList.setRowColor(row, getHeaderColor());
        studentList.add(getSmallHeader(localize (STUDENT_NAME_KEY,
                                                 STUDENT_NAME_DEFAULT)), col++,
                        row);
        studentList.add(getSmallHeader(localize (SSN_KEY,
                                                 SSN_DEFAULT)), col++, row);
        studentList.add(getSmallHeader(localize (PARENT_NAME_KEY,
                                                 PARENT_NAME_DEFAULT)), col++,
                        row);
        studentList.add(getSmallHeader(localize (ADDRESS_KEY,
                                                 ADDRESS_DEFAULT)), col++,
                        row++);
        try {
            final SchoolChoiceReminder  reminder
                    = business.findSchoolChoiceReminder (reminderId);
            final SchoolChoiceReminderReceiver [] receivers
                    = business.findAllStudentsThatMustDoSchoolChoice ();
            iwc.getSession ().setAttribute (STUDENT_LIST_KEY, receivers);
            for (int i = 0; i < receivers.length; i++) {
                try {
                    final CheckBox checkBox
                            = new CheckBox (CHILDREN_COUNT_KEY + i, "" + i);
                    checkBox.setChecked (true);

                    final SchoolChoiceReminderReceiver receiver = receivers [i];
                    col = 1;
                    studentList.setRowColor(row, (i % 2 == 0)
                                            ? getZebraColor1()
                                            : getZebraColor2());
                    studentList.add (checkBox, col++, row);
                    studentList.add (receiver.getStudentName (), col++, row);
                    studentList.add (receiver.getSsn (), col++, row);
                    studentList.add (receiver.getParentName (), col++, row);
                    studentList.add (receiver.getStreetAddress () + ", "
                                     + receiver.getPostalAddress (), col++,
                                     row++);
                } catch (Exception e) {
                    e.printStackTrace ();
                }
            }
        } catch (FinderException e) {
            e.printStackTrace ();
        }
        return studentList;
    }

    private void generateLetter (final IWContext iwc) throws RemoteException,
                                                             FinderException {
        final SchoolChoiceReminderReceiver [] allReceivers
                = (SchoolChoiceReminderReceiver [])
                iwc.getSession ().getAttribute (STUDENT_LIST_KEY);
        if (allReceivers == null) {
            add ("Lost session data - please try again.");
            showMainMenu (iwc);
            return;
        }

        iwc.getSession ().removeAttribute (STUDENT_LIST_KEY);
		final SchoolChoiceBusiness business = getSchoolChoiceBusiness (iwc);
        final int reminderId = Integer.parseInt (iwc.getParameter
                                                 (CASE_ID_KEY));
        final List checkedReceivers = new ArrayList ();
        for (int i = 0; i < allReceivers.length; i++) {
            if (iwc.isParameterSet (CHILDREN_COUNT_KEY + i)) {
                checkedReceivers.add (allReceivers [i]);
            }
        }
        final SchoolChoiceReminderReceiver [] receiverArray
                = (SchoolChoiceReminderReceiver []) checkedReceivers.toArray
                (new SchoolChoiceReminderReceiver [checkedReceivers.size ()]);
        final int docId = business.generateReminderLetter (reminderId,
                                                           receiverArray);
        final Table table = new Table ();
		table.setWidth(getWidth());
        add (table);
        int row = 1;
        table.add (getSmallHeader ("För att skriva ut påminnelsebreven så gör du enligt följande"), 1, row++);
        table.setHeight (row++, 12);
        table.add (getSmallText ("1. Klicka på länken 'Öppna breven i Acrobat Reader' nedan - ett nytt fönster med breven som ska skrivas ut öppnas"), 1, row++);
        table.add (getSmallText ("2. Klicka på skrivarikonen i det nya fönstret"), 1, row++);
        table.add (getSmallText ("3. Välj skrivare och skriv ut"), 1, row++);
        table.add (getSmallText ("4. Stäng fönstret med breven - bakom finns det här fönstret"), 1, row++);
        final Link viewLink = new Link("Öppna breven i Acrobat Reader");
        viewLink.setFile (docId);
        viewLink.setTarget ("letter_window");
        table.add (viewLink, 1, row++);
        table.add (getUserHomePageLink (iwc), 1, row++);
    }

    private static void logTime (final Table table, final int row) {
        System.err.println (Calendar.getInstance ().getTime ().toString ());
        table.add (Calendar.getInstance ().getTime ().toString (), 1, row);
    }

	private TextInput getSingleInput (IWContext iwc, final String paramId,
                                      final int maxLength) {
		TextInput textInput = (TextInput) getStyledInterface
                (new TextInput(paramId));
		textInput.setMaxlength(maxLength);
        final String fieldCanNotBeEmpty = localize
                (ERROR_FIELD_CAN_NOT_BE_EMPTY_KEY,
                 ERROR_FIELD_CAN_NOT_BE_EMPTY_DEFAULT);
        final String name = localize(paramId, paramId);
        textInput.setAsNotEmpty(fieldCanNotBeEmpty + ": " + name);
		String param = iwc.getParameter(paramId);
		if (param != null) {
			textInput.setContent(param);
		}
		return textInput;
	}

	private Link getUserHomePageLink (final IWContext iwc) {
		final Text userHomePageText
                = new Text (getLocalizedString (GOBACKTOMYPAGE_KEY,
                                                GOBACKTOMYPAGE_DEFAULT));
		final Link link = new Link (userHomePageText);
        try {
            final UserBusiness userBusiness = (UserBusiness)
                    IBOLookup.getServiceInstance (iwc, UserBusiness.class);
            final User user = iwc.getCurrentUser ();
            link.setPage (userBusiness.getHomePageIDForUser (user));
        } catch (RemoteException e) {
            e.printStackTrace ();
        }
		return link;
	}

	private Text getHeader(final String paramId, final String defaultText) {
		return getSmallHeader(localize(paramId, defaultText));
	}

    private SubmitButton getSubmitButton (final String key,
                                          final String defaultName) {
        final String name
                = getResourceBundle().getLocalizedString (key, defaultName);
		return (SubmitButton) getButton (new SubmitButton (name));
    }

	private SchoolChoiceBusiness getSchoolChoiceBusiness (final IWContext iwc)
        throws RemoteException {
		return (SchoolChoiceBusiness) IBOLookup.getServiceInstance
                (iwc, SchoolChoiceBusiness.class);
	}

	private String getLocalizedString(final String key, final String value) {
		return getResourceBundle().getLocalizedString(key, value);
	}
}
