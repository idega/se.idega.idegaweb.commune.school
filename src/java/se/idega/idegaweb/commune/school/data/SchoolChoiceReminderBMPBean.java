package se.idega.idegaweb.commune.school.data;

import com.idega.data.GenericEntity;

/**
 * Last modified: $Date: 2002/12/17 14:56:47 $ by $Author: staffan $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.2 $
 */
public class SchoolChoiceReminderBMPBean extends GenericEntity implements SchoolChoiceReminder {
    private static final String ENTITY_NAME = "sch_reminder";

    private static final String COLUMN_ID = ENTITY_NAME + "_id";
	private static final String COLUMN_TEXT = "TEXT";
	private static final String COLUMN_EVENT_DATE = "EVENT_DATE";
	private static final String COLUMN_REMINDER_DATE = "REMINDER_DATE";

	public String getEntityName() {
		return ENTITY_NAME;
	}

    public void initializeAttributes () {
        addAttribute(COLUMN_ID, "Id", Integer.class);
        setAsPrimaryKey (COLUMN_ID, true);
		addAttribute (COLUMN_TEXT, "Text", String.class);
        addAttribute (COLUMN_EVENT_DATE, "Event Date", java.sql.Date.class);
        addAttribute (COLUMN_REMINDER_DATE, "Reminder Date",
                      java.sql.Date.class);
    }

    public void setText (final String text) {
        setColumn (COLUMN_TEXT, text);
    }

    public void setEventDate (final java.util.Date eventDate) {
        setColumn (COLUMN_EVENT_DATE, new java.sql.Date (eventDate.getTime ()));
    }
    
    public void setReminderDate (final java.util.Date reminderDate) {
        setColumn (COLUMN_REMINDER_DATE,
                   new java.sql.Date (reminderDate.getTime ()));
    }
}
