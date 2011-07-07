package se.idega.idegaweb.commune.school.data;

import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.FinderException;

import com.idega.block.process.data.AbstractCaseBMPBean;
import com.idega.block.process.data.Case;
import com.idega.block.process.data.CaseBMPBean;
import com.idega.block.process.data.CaseCode;
import com.idega.block.process.data.CaseCodeHome;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOQuery;
import com.idega.data.IDORemoveRelationshipException;
import com.idega.user.data.Group;
import com.idega.user.data.User;

/**
 * Last modified: $Date: 2006/04/09 11:39:53 $ by $Author: laddi $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.15 $
 */
public class SchoolChoiceReminderBMPBean extends AbstractCaseBMPBean implements SchoolChoiceReminder, Case {

	private static final long serialVersionUID = 2496684940085886013L;

	private static final String ENTITY_NAME = "sch_reminder";

	private static final String CASE_CODE_DESCRIPTION = "School Choice Reminder";
	private static final String[] CASE_STATUS_DESCRIPTIONS = {
	};
	private static final String[] CASE_STATUS_KEYS = {
	};

	//private static final String COLUMN_ID = ENTITY_NAME + "_id";
	private static final String COLUMN_USER_ID = "USER_ID";
	private static final String COLUMN_TEXT = "REMINDER_TEXT";
	private static final String COLUMN_EVENT_DATE = "EVENT_DATE";
	private static final String COLUMN_REMINDER_DATE = "REMINDER_DATE";

	@Override
	public String getEntityName() {
		return ENTITY_NAME;
	}

	@Override
	public String getCaseCodeKey() {
		return SchoolChoiceReminder.CASE_CODE_KEY;
	}

	@Override
	public String getCaseCodeDescription() {
		return CASE_CODE_DESCRIPTION;
	}

	@Override
	public String[] getCaseStatusKeys() {
		return CASE_STATUS_KEYS;
	}

	@Override
	public String[] getCaseStatusDescriptions() {
		return CASE_STATUS_DESCRIPTIONS;
	}

	@Override
	public void insertStartData() {
		insertCaseCode();
	}

	@Override
	public void initializeAttributes() {
		addGeneralCaseRelation();
		addAttribute(COLUMN_USER_ID, "User", true, true, Integer.class, "many-to-one", User.class);
		addAttribute(COLUMN_TEXT, "Text", true, true, String.class, 4096);
		addAttribute(COLUMN_EVENT_DATE, "Event Date", java.sql.Date.class);
		addAttribute(COLUMN_REMINDER_DATE, "Reminder Date", java.sql.Date.class);
	}

	@Override
	public String getText() {
		final String text = getStringColumnValue(COLUMN_TEXT);
		return text != null ? text : "";
	}

	@Override
	public java.util.Date getEventDate() {
		final java.util.Date eventDate = (java.util.Date) getColumnValue(COLUMN_EVENT_DATE);
		return eventDate != null ? eventDate : new java.util.Date();
	}

	@Override
	public java.util.Date getReminderDate() {
		final java.util.Date reminderDate = (java.util.Date) getColumnValue(COLUMN_REMINDER_DATE);
		return reminderDate != null ? reminderDate : new java.util.Date();
	}

	@Override
	public int getUserId() {
		final Integer userId = getIntegerColumnValue(COLUMN_USER_ID);
		return userId != null ? userId.intValue() : -1;
	}

	@Override
	public void setText(final String text) {
		setColumn(COLUMN_TEXT, text != null ? text : "");
	}

	@Override
	public void setEventDate(final java.util.Date eventDate) {
		setColumn(COLUMN_EVENT_DATE, new java.sql.Date(eventDate != null ? eventDate.getTime() : new java.util.Date().getTime()));
	}

	@Override
	public void setReminderDate(final java.util.Date reminderDate) {
		setColumn(COLUMN_REMINDER_DATE, new java.sql.Date(reminderDate != null ? reminderDate.getTime() : new java.util.Date().getTime()));
	}

	@Override
	public void setUser(final User user) {
		if (user != null) {
			setColumn(COLUMN_USER_ID, ((Integer) user.getPrimaryKey()).intValue());
		}
	}

	public Collection ejbFindAll() throws FinderException {
		final IDOQuery query = idoQuery();
		query.appendSelect().append("scr.*").appendFrom().append(getEntityName()).append(" scr").append(", ").append(CaseBMPBean.TABLE_NAME).append(" pc");
		query.appendWhere().append("scr.").append(getIDColumnName()).appendEqualSign().append("pc.").append(CaseBMPBean.TABLE_NAME + "_ID").appendAnd().append("pc.").append(CaseBMPBean.COLUMN_CASE_STATUS).appendEqualSign().appendWithinSingleQuotes("UBEH").appendAnd().append("pc.").append(CaseBMPBean.COLUMN_CASE_CODE).appendEqualSign().appendWithinSingleQuotes(SchoolChoiceReminder.CASE_CODE_KEY);

		final Collection primaryKeys = idoFindPKsByQuery(query);

		return primaryKeys;
	}

	public Collection ejbFindUnhandled(final Group[] groups) throws FinderException {
		final IDOQuery query = idoQuery();
		final Calendar today = Calendar.getInstance();
		final String date = today.get(Calendar.YEAR) + "-" + (today.get(Calendar.MONTH) + 1) + "-" + today.get(Calendar.DATE);
		query.appendSelect().append("scr.*").appendFrom().append(getEntityName()).append(" scr").append(", ").append(CaseBMPBean.TABLE_NAME).append(" pc");
		query.appendWhere().append("scr.").append(getIDColumnName()).appendEqualSign().append("pc.").append(CaseBMPBean.TABLE_NAME + "_ID").appendAnd().append("pc.").append(CaseBMPBean.COLUMN_CASE_STATUS).appendEqualSign().appendWithinSingleQuotes("UBEH").appendAnd().append("pc.").append(CaseBMPBean.COLUMN_CASE_CODE).appendEqualSign().appendWithinSingleQuotes(SchoolChoiceReminder.CASE_CODE_KEY).appendAnd().append(COLUMN_REMINDER_DATE).append(" <= ").appendWithinSingleQuotes(date);

		for (int i = 0; i < groups.length; i++) {
			query.append(i == 0 ? " and (" : " or ");
			final int groupId = ((Integer) groups[i].getPrimaryKey()).intValue();
			query.append("pc.handler_group_id = '" + groupId + "'");
			// special notice for super admin group, i.e. 1
			if (groupId == 1) {
				query.append(" or 1 = 1");
			}
		}
		query.append(")");
		final Collection primaryKeys = idoFindPKsByQuery(query);

		return primaryKeys;
	}

	private synchronized void insertCaseCode() {
		try {
			final CaseCodeHome home = (CaseCodeHome) IDOLookup.getHome(CaseCode.class);
			final Collection codes = home.findAllCaseCodes();
			boolean codeExists = false;
			for (Iterator i = codes.iterator(); i.hasNext();) {
				final CaseCode code = (CaseCode) i.next();
				codeExists = code.getCode().equalsIgnoreCase(SchoolChoiceReminder.CASE_CODE_KEY);
			}
			if (!codeExists) {
				final CaseCode code = home.create();
				code.setCode(SchoolChoiceReminder.CASE_CODE_KEY);
				code.setDescription("School choice reminder");
				code.store();
			}
		}
		catch (final Exception e) {
			//e.printStackTrace ();
		}
	}

	@Override
	public void addSubscriber(User subscriber) throws IDOAddRelationshipException {
	}

	@Override
	public Collection<User> getSubscribers() {
		return null;
	}

	@Override
	public void removeSubscriber(User subscriber) throws IDORemoveRelationshipException {
	}
}
