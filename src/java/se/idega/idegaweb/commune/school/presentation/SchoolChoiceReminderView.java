package se.idega.idegaweb.commune.school.presentation;

//import com.idega.business.IBOLookup;
import com.idega.presentation.*;
//import com.idega.presentation.text.*;
import com.idega.presentation.ui.*;
//import javax.ejb.*;
import se.idega.idegaweb.commune.presentation.CommuneBlock;

/**
 * SchoolChoiceReminderView is an IdegaWeb block that registers and handles
 * reminder broadcasts to citizens who should make a school choice. It is based
 * on session ejb classes in {@link se.idega.idegaweb.commune.school.business}
 * and entity ejb classes in {@link se.idega.idegaweb.commune.school.data}.
 * <p>
 * <p>
 * Last modified: $Date: 2002/12/17 07:00:28 $ by $Author: staffan $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.2 $
 * @see javax.ejb
 */
public class SchoolChoiceReminderView extends CommuneBlock {
    private static final String PREFIX = "schoolchoicereminder_";
    private static final String ACTION_KEY = PREFIX + "action";
    private static final String CREATE_REMINDER_KEY = PREFIX + "create_reminder";
    private static final String LIST_ALL_REMINDERS_KEY = PREFIX + "list_all_reminders";
    private static final String CREATE_REMINDER_DEFAULT = "Registrera en påminnelse";
    private static final String LIST_ALL_REMINDERS_DEFAULT = "Visa alla registrerade påminnelser";
    private static final String CONTINUE_KEY = PREFIX + "continue";
    private static final String CONTINUE_DEFAULT = "Fortsätt";
    private static final String SCHOOLCHOICEREMINDER_KEY = PREFIX + "schoolchoicereminder";
    private static final String SCHOOLCHOICEREMINDER_DEFAULT = "Påminnelse om skolval";
    

	/**
	 * @param iwc session data like user info etc.
	 */
	public void main(final IWContext iwc) {
		setResourceBundle (getResourceBundle(iwc));
        final String action = iwc.getParameter (ACTION_KEY);

        if (action != null && action.equals (CREATE_REMINDER_KEY))  {
            showCreateRiminderForm (iwc);
        } else if (action != null && action.equals (LIST_ALL_REMINDERS_KEY)) {
            showAllReminders (iwc);
        } else {
            showMainMenu (iwc);
        }
    }

    private void showMainMenu (final IWContext iwc) {
		final Form form = new Form();
		final DropdownMenu dropdown = (DropdownMenu) getStyledInterface
                (new DropdownMenu (ACTION_KEY));
        dropdown.addMenuElement (CREATE_REMINDER_KEY,
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

    private void showCreateRiminderForm (final IWContext iwcontext) {
        throw new UnsupportedOperationException ();
    }

    private void showAllReminders (final IWContext iwcontext) {
        throw new UnsupportedOperationException ();
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
