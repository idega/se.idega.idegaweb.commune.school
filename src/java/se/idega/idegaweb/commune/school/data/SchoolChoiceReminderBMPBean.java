package se.idega.idegaweb.commune.school.data;

import com.idega.block.process.data.*;
import com.idega.data.*;
import com.idega.user.data.*;
import java.rmi.RemoteException;
import java.util.*;
import javax.ejb.FinderException;

/**
 * Last modified: $Date: 2002/12/29 13:49:39 $ by $Author: staffan $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.5 $
 */
public class SchoolChoiceReminderBMPBean extends AbstractCaseBMPBean implements SchoolChoiceReminder {
    private static final String ENTITY_NAME = "sch_reminder";

    private static final String CASE_CODE_DESCRIPTION = "School Choice Reminder";
	private static final String [] CASE_STATUS_DESCRIPTIONS = { };
	private static final String [] CASE_STATUS_KEYS = { };

    private static final String COLUMN_ID = ENTITY_NAME + "_id";
	private static final String COLUMN_USER_ID = "USER_ID";
	private static final String COLUMN_TEXT = "TEXT";
	private static final String COLUMN_EVENT_DATE = "EVENT_DATE";
	private static final String COLUMN_REMINDER_DATE = "REMINDER_DATE";

	public String getEntityName() {
		return ENTITY_NAME;
	}

	public String getCaseCodeKey() {
		return SchoolChoiceReminder.CASE_CODE_KEY;
	}

    public String getCaseCodeDescription () {
        return CASE_CODE_DESCRIPTION;
    }

    public String [] getCaseStatusKeys () {
        return CASE_STATUS_KEYS;
    }

    public String [] getCaseStatusDescriptions () {
        return CASE_STATUS_DESCRIPTIONS;
    }

    public void insertStartData () {
        super.insertStartData ();
        insertCaseCode ();
    }

    public void initializeAttributes () {
        insertCaseCode ();
		addGeneralCaseRelation();
       	addAttribute (COLUMN_USER_ID, "User", true, true, Integer.class,
                      "many-to-one", User.class);
		addAttribute (COLUMN_TEXT, "Text", String.class);
        addAttribute (COLUMN_EVENT_DATE, "Event Date", java.sql.Date.class);
        addAttribute (COLUMN_REMINDER_DATE, "Reminder Date",
                      java.sql.Date.class);
    }

    public String getText () {
        return getStringColumnValue (COLUMN_TEXT);
    }

    public java.util.Date getEventDate () {
        return (java.util.Date) getColumnValue (COLUMN_EVENT_DATE);
    }

    public java.util.Date getReminderDate () {
        return (java.util.Date) getColumnValue (COLUMN_REMINDER_DATE);
    }

    public int getUserId () {
        return getIntegerColumnValue (COLUMN_USER_ID). intValue ();
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

    public void setUser (final User user) throws RemoteException {
        if (user != null) {
            setColumn (COLUMN_USER_ID,
                       ((Integer) user.getPrimaryKey()).intValue());
        }
    }

    Collection ejbFindAll () throws FinderException, RemoteException {
        return idoFindIDsBySQL ("select * from " + ENTITY_NAME);
    }

    Collection ejbFindUnhandled (final Group [] groups)
        throws FinderException, RemoteException {
        final IDOQuery query = idoQuery();
        final Calendar today = Calendar.getInstance ();
        final String date = today.get (Calendar.YEAR)
                + "-" + (today.get (Calendar.MONTH) + 1)
                + "-" + today.get (Calendar.DATE);
        query.appendSelect().append("scr.*").appendFrom().append(getEntityName()).append(" scr").append(", ").append(CaseBMPBean.TABLE_NAME).append(" pc");
        query.appendWhere().append("scr.").append(getIDColumnName()).appendEqualSign().append("pc.").append(CaseBMPBean.TABLE_NAME+"_ID").appendAnd().append("pc.").append(CaseBMPBean.COLUMN_CASE_STATUS).appendEqualSign().appendWithinSingleQuotes("UBEH").appendAnd().append("pc.").append(CaseBMPBean.COLUMN_CASE_CODE).appendEqualSign().appendWithinSingleQuotes(SchoolChoiceReminder.CASE_CODE_KEY).appendAnd().append(COLUMN_REMINDER_DATE).append(" <= ").appendWithinSingleQuotes(date);

        for (int i = 0; i < groups.length; i++) {
            query.append (i == 0 ? " and (" : " or ");
            final int groupId
                    = ((Integer) groups [i].getPrimaryKey ()).intValue ();
            query.append ("pc.handler_group_id = '" + groupId + "'");
            // special notice for super admin group, i.e. 1
            if (groupId == 1) query.append (" or 1 = 1");
        }
        query.append (")");
        final Collection primaryKeys = idoFindPKsByQuery(query);

        return primaryKeys;
    }

    private synchronized void insertCaseCode () {
        try {
            final CaseCodeHome home
                    = (CaseCodeHome) IDOLookup.getHome(CaseCode.class);
            final Collection codes = home.findAllCaseCodes ();
            boolean codeExists = false;
            for (Iterator i = codes.iterator (); i.hasNext ();) {
                final CaseCode code = (CaseCode) i.next ();
                codeExists = code.getCode ().equalsIgnoreCase
                        (SchoolChoiceReminder.CASE_CODE_KEY);
            }
            if (!codeExists) {
                final CaseCode code = home.create ();
                code.setCode (SchoolChoiceReminder.CASE_CODE_KEY);
                code.setDescription ("School choice reminder");
                code.store ();
            }
        } catch (final Exception e) {
            e.printStackTrace ();
        }
    }
}
