package se.idega.idegaweb.commune.school.presentation;

import com.idega.business.IBOLookup;
import com.idega.presentation.*;
import com.idega.presentation.text.*;
import com.idega.presentation.ui.*;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.*;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
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
 * Last modified: $Date: 2003/04/02 16:12:22 $ by $Author: laddi $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.28 $
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
    private final static String CONFIRM_DELETE_KEY = PREFIX + "confirm_delete_reminder";
    private final static String CONFIRM_DELETE_DEFAULT = "Påminnelsen är nu avaktiverad";
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
    private static final String REMINDER_DAYS_BEFORE_DEFAULT = "Antal dagar innan utskick som påminnelse ska visas som ärende";
    private static final String REMINDER_DAYS_BEFORE_KEY = PREFIX + "reminder_days_before";
    private static final String REMINDER_TEXT_1_DEFAULT = "Påminnelse - skolvalsperioden är snart slut!\n\nSkolvalsperioden är 13-31 januari. Vi har noterat att du ännu inte gjort ditt skolval.\n\nAll information om skolvalet finns på www.nacka24.nacka.se där du också ansöker om ditt personliga medborgarkonto.\n\nOm du har frågor angående skolvalet kontakta Kundvalsgruppen på telefon\n08-718 80 00 eller e-post kundvalsgruppen@nacka.se\n\n\nMed vänlig hälsning\n\nKundvalsgruppen";
    private static final String REMINDER_TEXT_1_KEY = PREFIX + "remindertext_1";
    private static final String REMINDER_TEXT_2_DEFAULT = "Påminnelse - sista möjligheten att välja skola!\n\nSkolvalsperioden är nu slut och skolorna påbörjar inom kort arbetet med att placera de barn som har valt skola. Utnyttja möjligheten att själv göra ett aktivt val för ditt barn!\n\nDitt val måste vara gjort på www.nacka24.nacka.se senast fredagen den 7 februari kl. 24.00.\n\nOm du ej själv gör ett aktivt skolval för ditt barn får barnet en skolplacering på närmaste skola som har ledig plats.\n\nOm du har frågor angående skolvalet kontakta Kundvalsgruppen på telefon\n08-718 80 00 eller e-post kundvalsgruppen@nacka.se\n\n\nMed vänlig hälsning\n\nKundvalsgruppen";
    private static final String REMINDER_TEXT_2_KEY = PREFIX + "remindertext_2";
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
    private static final String DELETE_DEFAULT = "Ta bort";
    private static final String DELETE_KEY = PREFIX + "delete";
    private static final String CANCEL_DEFAULT = "Avbryt";
    private static final String CANCEL_KEY = PREFIX + "cancel";
    private static final String ACTIVE_REMINDERS_DEFAULT = "Aktiva påminnelser";
    private static final String ACTIVE_REMINDERS_KEY = PREFIX + "active_reminders";
    private static final String NEW_REMINDER_DEFAULT = "Skapa ny påminnelse";
    private static final String NEW_REMINDER_KEY = PREFIX + "new_reminder";

    private static final SimpleDateFormat dateFormatter
        = new SimpleDateFormat ("yyyy-MM-dd");


	/**
	 * @param iwc session data like user info etc.
	 */
	public void main(final IWContext iwc)
        throws RemoteException, CreateException, FinderException {
		setResourceBundle (getResourceBundle(iwc));
        final String action = iwc.getParameter (ACTION_KEY);

        if (!iwc.isLoggedOn ()) {
            add ("You're not autorized to use this function.");
        } else if (action != null && action.equals (CREATE_KEY)) {
            createReminder (iwc);
        } else if (action != null && action.equals (SHOW_DETAILS_KEY)) {
            showDetails (iwc);
        } else if (action != null && action.equals (GENERATE_LETTER_KEY)) {
            generateLetter (iwc);
        } else if (action != null && action.equals (DELETE_KEY)) {
            deleteReminder (iwc);
        } else {
            showAllReminders (iwc);
            showCreateReminderForm (iwc);
        }
    }

    private void showMainMenu (final IWContext iwc) {
		final Form form = new Form();
		final DropdownMenu dropdown = (DropdownMenu) getStyledInterface
                (new DropdownMenu (ACTION_KEY));
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
        radioGroup.addRadioButton (localize (REMINDER_TEXT_1_KEY, REMINDER_TEXT_1_DEFAULT));
        radioGroup.addRadioButton (localize (REMINDER_TEXT_2_KEY, REMINDER_TEXT_2_DEFAULT));
        radioGroup.addRadioButton (localize (CUSTOM_TEXT_KEY, CUSTOM_TEXT_DEFAULT));
        radioGroup.setSelected (localize (REMINDER_TEXT_1_KEY, REMINDER_TEXT_1_DEFAULT));
		table.add (radioGroup, 1, row++);
		final TextArea textArea = new TextArea (CUSTOM_TEXT_KEY);
		textArea.setColumns (40);
		textArea.setRows (10);
		table.add (textArea, 1, row++);

		table.add(getSmallHeader(localize (EVENT_DATE_KEY, EVENT_DATE_DEFAULT)), 1, row);
		table.add(getSingleInput(iwc, EVENT_DATE_KEY, 8), 3, row++);
		table.add(getSmallHeader(localize (REMINDER_DAYS_BEFORE_KEY, REMINDER_DAYS_BEFORE_DEFAULT)), 1, row);
		table.add(getSingleInput(iwc, REMINDER_DAYS_BEFORE_KEY, 2), 3, row++);

        table.setHeight (row++, 12);
        final SubmitButton submit = getSubmitButton (CREATE_KEY,
                                                     CREATE_DEFAULT);
		table.add(submit, 1, row++);
		form.add(table);
		add(form);
    }

    private void showAllReminders (final IWContext iwc) throws RemoteException,
                                                               FinderException {
        //final UserBusiness userBusiness = (UserBusiness)
                IBOLookup.getServiceInstance(iwc, UserBusiness.class);
        //final User user = iwc.getCurrentUser();
        //final int userId = ((Integer) user.getPrimaryKey()).intValue();
        //final Collection groupCollection = userBusiness.getUserGroups(userId);
        //final Group [] groups = (Group[]) groupCollection.toArray(new Group[0]);
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
            messageList.add (message, col++, row);
            messageList.add (dateFormatter.format (reminder.getEventDate ()),
                             col++, row);
            messageList.add (dateFormatter.format (reminder.getReminderDate ()),
                             col++, row);
            final Link deleteLink
                    = getSmallLink (localize (DELETE_KEY, DELETE_DEFAULT));
            deleteLink.addParameter (CASE_ID_KEY, id);
            deleteLink.addParameter (ACTION_KEY, DELETE_KEY);
            messageList.add (deleteLink, col++, row);
            row++;
        }
		final Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(getWidth());
        row = 1;
        table.add (getHeader(localize (ACTIVE_REMINDERS_KEY,
                                       ACTIVE_REMINDERS_DEFAULT)), 1, row++);
        table.setHeight (row++, 12);
        if (reminders.length > 0) {
            table.add (localize ("scr_click_below", "Om du klickar på id-numret nedan så räknar systemet ut vilka som ska få påminnelse. Observera att detta kan ta 3 minuter eller längre."), 1, row++);
            table.setHeight (row++, 12);
            table.add(form, 1, row++);
        } else {
            table.add (localize ("scr_no_active", "Det finns inga aktiva påminnelser just nu."), 1, row++);
        }
		add(table);
    }

    private void createReminder (final IWContext iwc) throws RemoteException,
                                                             CreateException {
        final String reminderTextChoice = iwc.getParameter (REMINDER_TEXT_KEY);
        final String reminderText = reminderTextChoice.equals
                (localize (CUSTOM_TEXT_KEY, CUSTOM_TEXT_DEFAULT)) ?
                iwc.getParameter (CUSTOM_TEXT_KEY) : reminderTextChoice;
        Date eventDate;
        try {
            eventDate = stringToDate (iwc.getParameter (EVENT_DATE_KEY));
        } catch (IllegalArgumentException e) {
            add ("Felaktigt inmatat datum (ååååmmdd)");
            showCreateReminderForm (iwc);
            return;
        }
        //final int daysBefore = Integer.parseInt (iwc.getParameter(REMINDER_DAYS_BEFORE_KEY));
        final Date reminderDate
                = getDaysBefore (iwc.getParameter (REMINDER_DAYS_BEFORE_KEY),
                                 eventDate);
		final SchoolChoiceBusiness business = getSchoolChoiceBusiness (iwc);
		business.createSchoolChoiceReminder (reminderText, eventDate,
                                             reminderDate,
                                             iwc.getCurrentUser ());
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

    private void deleteReminder (final IWContext iwc) throws RemoteException,
                                                             FinderException {
		final SchoolChoiceBusiness business = getSchoolChoiceBusiness (iwc);
        final int reminderId = Integer.parseInt (iwc.getParameter
                                                 (CASE_ID_KEY));
        final SchoolChoiceReminder  reminder
                = business .findSchoolChoiceReminder (reminderId);
        reminder.setCaseStatus (business.getCaseStatusInactive ());
        reminder.store ();
		final Text text1
                = new Text (getLocalizedString (CONFIRM_DELETE_KEY,
                                                CONFIRM_DELETE_DEFAULT));
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
                = business.findSchoolChoiceReminder (reminderId);
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
                         (new SubmitButton(localize ("scr_print_to_addresses_below", "Skriv ut till adresser nedan"),
                                           ACTION_KEY, GENERATE_LETTER_KEY)),
                         1, 1);
        submitTable.add (getStyledInterface
                         (new SubmitButton(localize (DELETE_KEY, DELETE_DEFAULT), ACTION_KEY, DELETE_KEY)), 2, 1);
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
            //final SchoolChoiceReminder reminder = business.findSchoolChoiceReminder (reminderId);
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
                    final String ssn = receiver.getSsn ();
                    studentList.add (ssn.substring (2, 8) + "-"
                                     + ssn.substring (8, 12), col++, row);
                    final String parentName = receiver.getParentName ();
                    final Text parentText = new Text(parentName);
                    if (parentName.startsWith ("?")) {
                        parentText.setFontColor("#ff0000");
                        parentText.setBold ();
                    }
                    studentList.add (parentText, col++, row);
                    final Text addressText
                            = new Text (receiver.getStreetAddress () + ", "
                                        + receiver.getPostalAddress ());
                    if (receiver.getStreetAddress ().startsWith ("?")
                        || receiver.getPostalAddress ().startsWith ("?")) {
                        addressText.setFontColor("#ff0000");
                        addressText.setBold ();
                    }
                    studentList.add (addressText, col++, row++);
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

    private Date getDaysBefore (final String rawInput, final Date event) {
        if (rawInput == null) {
            return new Date (event.getTime ());
        }
		final StringBuffer digitOnlyInput = new StringBuffer();
		for (int i = 0; i < rawInput.length(); i++) {
			if (Character.isDigit(rawInput.charAt(i))) {
				digitOnlyInput.append(rawInput.charAt(i));
			}
		}
        if (rawInput.length () == 0) {
            return new Date (event.getTime ());
        }

        return new Date (event.getTime()
                         - (Integer.parseInt (digitOnlyInput.toString ())
                            * 1000 * 60 * 60 * 24));
    }

    private Date stringToDate (final String rawInput)
        throws IllegalArgumentException {
        if (rawInput == null) {
            throw new IllegalArgumentException ();
        }
		final StringBuffer digitOnlyInput = new StringBuffer();
		for (int i = 0; i < rawInput.length(); i++) {
			if (Character.isDigit(rawInput.charAt(i))) {
				digitOnlyInput.append(rawInput.charAt(i));
			}
		}
        if (digitOnlyInput.length () == 6) {
			digitOnlyInput.insert(0, 20);
        } 
        if (digitOnlyInput.length () != 8) {
            throw new IllegalArgumentException ();
        }
        final int year = new Integer(digitOnlyInput.substring(0, 4)).intValue();
		final int month = new Integer(digitOnlyInput.substring(4, 6)).intValue() - 1;
		final int day = new Integer(digitOnlyInput.substring(6, 8)).intValue();
        final Date now = Calendar.getInstance ().getTime ();
        final Calendar calendar = Calendar.getInstance ();
        calendar.set (year, month, day);
        final Date event = calendar.getTime ();
        if (event.before (now)) {
            throw new IllegalArgumentException ();
        }
        return event;
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
