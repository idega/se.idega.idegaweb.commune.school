package se.idega.idegaweb.commune.school.presentation;

import com.idega.presentation.text.Text;
import com.idega.presentation.*;
import com.idega.presentation.ui.*;
import se.idega.idegaweb.commune.presentation.CommuneBlock;

/**
 * SchoolChoiceReminderView is an IdegaWeb block that registers and handles
 * reminder broadcasts to citizens who should make a school choice. It is based
 * on session ejb classes in {@link se.idega.idegaweb.commune.school.business}
 * and entity ejb classes in {@link se.idega.idegaweb.commune.school.data}.
 * <p>
 * <p>
 * Last modified: $Date: 2002/12/17 14:13:03 $ by $Author: staffan $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.3 $
 * @see javax.ejb
 */
public class SchoolChoiceReminderView extends CommuneBlock {
    private static final String PREFIX = "schoolchoicereminder_";
    private static final String ACTION_KEY = PREFIX + "action";
    private static final String CONTINUE_DEFAULT = "Fortsätt";
    private static final String CONTINUE_KEY = PREFIX + "continue";
    private static final String CREATE_REMINDER_DEFAULT = "Registrera en påminnelse";
    private static final String CREATE_REMINDER_KEY = PREFIX + "create_reminder";
    private static final String SHOW_CREATE_REMINDER_FORM_KEY = PREFIX + "show_create_reminder_form";
    private static final String LIST_ALL_REMINDERS_DEFAULT = "Visa alla registrerade påminnelser";
    private static final String LIST_ALL_REMINDERS_KEY = PREFIX + "list_all_reminders";
    private static final String SCHOOLCHOICEREMINDER_DEFAULT = "Påminnelse om skolval";
    private static final String SCHOOLCHOICEREMINDER_KEY = PREFIX + "schoolchoicereminder";
    private static final String REMINDER_TEXT_DEFAULT = "Påminnelsetext";
    private static final String REMINDER_TEXT_KEY = PREFIX + "reminder_text";
    private static final String REMINDER_TEXT1_DEFAULT = "Text 1 som kommer från Kristina Lundin.";
    private static final String REMINDER_TEXT1_KEY = PREFIX + "reminder_text1";
    private static final String REMINDER_TEXT2_DEFAULT = "Text 2 som kommer från Kristina Lundin.";
    private static final String REMINDER_TEXT2_KEY = PREFIX + "reminder_text2";
    private static final String CUSTOM_REMINDER_TEXT_DEFAULT = "Egen påminnelsetext";
    private static final String CUSTOM_REMINDER_TEXT_KEY = PREFIX + "custom_reminder_text";
    private static final String EVENT_DATE_DEFAULT = "Datum för utskick (ååååmmdd)";
    private static final String EVENT_DATE_KEY = PREFIX + "event_date";
    private static final String REMINDER_DATE_DEFAULT = "Datum för påminnelse till Kundvalsgruppen (ååååmmdd)";
    private static final String REMINDER_DATE_KEY = PREFIX + "reminder_date";
    private static final String ERROR_FIELD_CAN_NOT_BE_EMPTY_DEFAULT = "Fältet måste fyllas i";
    private static final String ERROR_FIELD_CAN_NOT_BE_EMPTY_KEY = PREFIX + "error_field_can_not_be_empty";
    
    private static final int REMINDER_TEXT1_ID = 0;
    private static final int REMINDER_TEXT2_ID = 1;
    private static final int CUSTOM_REMINDER_TEXT_ID = 2;

	/**
	 * @param iwc session data like user info etc.
	 */
	public void main(final IWContext iwc) {
		setResourceBundle (getResourceBundle(iwc));
        final String action = iwc.getParameter (ACTION_KEY);

        if (action != null && action.equals (SHOW_CREATE_REMINDER_FORM_KEY))  {
            showCreateReminderForm (iwc);
        } else if (action != null && action.equals (LIST_ALL_REMINDERS_KEY)) {
            showAllReminders (iwc);
        } else if (action != null && action.equals (CREATE_REMINDER_KEY)) {
            add ("REMINDER_TEXT_KEY=" + iwc.getParameter (REMINDER_TEXT_KEY) + "<br/>");
            add ("CUSTOM_REMINDER_TEXT_KEY=" + iwc.getParameter (CUSTOM_REMINDER_TEXT_KEY) + "<br/>");
            add ("EVENT_DATE_KEY=" + iwc.getParameter (EVENT_DATE_KEY) + "<br/>");
            add ("REMINDER_DATE_KEY=" + iwc.getParameter (REMINDER_DATE_KEY) + "<br/>");
        } else {
            showMainMenu (iwc);
        }
    }

    private void showMainMenu (final IWContext iwc) {
		final Form form = new Form();
		final DropdownMenu dropdown = (DropdownMenu) getStyledInterface
                (new DropdownMenu (ACTION_KEY));
        dropdown.addMenuElement (SHOW_CREATE_REMINDER_FORM_KEY,
                                 localize (CREATE_REMINDER_KEY,
                                           CREATE_REMINDER_DEFAULT));
        dropdown.addMenuElement (LIST_ALL_REMINDERS_KEY,
                                 localize (LIST_ALL_REMINDERS_KEY,
                                           LIST_ALL_REMINDERS_DEFAULT));
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
                                                    CREATE_REMINDER_KEY);
        form.add (hidden);
		final Table table = new Table();
		table.setWidth (getWidth ());
		table.setCellspacing (0);
		table.setCellpadding (getCellpadding ());
        int row = 1;
		table.add(getLocalizedHeader(REMINDER_TEXT_KEY, REMINDER_TEXT_DEFAULT), 1, row);
		final RadioGroup radioGroup = new RadioGroup (REMINDER_TEXT_KEY);
        /*
        radioGroup.addRadioButton (REMINDER_TEXT1_ID, localize (REMINDER_TEXT1_KEY, REMINDER_TEXT1_DEFAULT));
        radioGroup.addRadioButton (REMINDER_TEXT2_ID, localize (REMINDER_TEXT2_KEY, REMINDER_TEXT2_DEFAULT));
        radioGroup.addRadioButton (CUSTOM_REMINDER_TEXT_ID, localize (CUSTOM_REMINDER_TEXT_KEY, CUSTOM_REMINDER_TEXT_DEFAULT));
        */
        radioGroup.addRadioButton (localize (REMINDER_TEXT1_KEY, REMINDER_TEXT1_DEFAULT));
        radioGroup.addRadioButton (localize (REMINDER_TEXT2_KEY, REMINDER_TEXT2_DEFAULT));
        radioGroup.addRadioButton (localize (CUSTOM_REMINDER_TEXT_KEY, CUSTOM_REMINDER_TEXT_DEFAULT));
        radioGroup.setSelected (localize (REMINDER_TEXT1_KEY, REMINDER_TEXT1_DEFAULT));
		table.add (radioGroup, 1, row++);
		final TextArea textArea = new TextArea (CUSTOM_REMINDER_TEXT_KEY);
		textArea.setColumns (40);
		textArea.setRows (10);
		table.add (textArea, 1, row++);

		table.add(getHeader(EVENT_DATE_KEY, EVENT_DATE_DEFAULT), 1, row);
		table.add(getSingleInput(iwc, EVENT_DATE_KEY, 8), 3, row++);
		table.add(getHeader(REMINDER_DATE_KEY, REMINDER_DATE_DEFAULT), 1, row);
		table.add(getSingleInput(iwc, REMINDER_DATE_KEY, 8), 3, row++);

        final SubmitButton submit = getSubmitButton (CREATE_REMINDER_KEY,
                                                     CREATE_REMINDER_DEFAULT);
		table.add(submit, 1, row++);
		form.add(table);
		add(form);
    }

    private void showAllReminders (final IWContext iwcontext) {
        add ("The method showAllReminders is not implemented yet. /<a href=http://www.staffannoteberg.com>Staffan Nöteberg</a>");
    }

	private TextInput getSingleInput (IWContext iwc, final String paramId, final int maxLength) {
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

	private Text getHeader(final String paramId, final String defaultText) {
		return getSmallHeader(localize(paramId, defaultText));
	}

    private SubmitButton getSubmitButton (final String key,
                                          final String defaultName) {
        final String name
                = getResourceBundle().getLocalizedString (key, defaultName);
		return (SubmitButton) getButton (new SubmitButton (name));
    }
	private String getLocalizedString(final String key, final String value) {
		return getResourceBundle().getLocalizedString(key, value);
	}
}
