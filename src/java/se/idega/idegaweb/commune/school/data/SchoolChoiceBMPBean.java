package se.idega.idegaweb.commune.school.data;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import javax.ejb.EJBException;
import javax.ejb.FinderException;

import se.idega.idegaweb.commune.school.business.MailReceiver;
import se.idega.idegaweb.commune.school.business.SchoolConstants;

import com.idega.block.process.data.AbstractCaseBMPBean;
import com.idega.block.process.data.Case;
import com.idega.block.process.data.CaseBMPBean;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolClassBMPBean;
import com.idega.block.school.data.SchoolSeason;
import com.idega.block.school.data.SchoolType;
import com.idega.block.school.data.SchoolYear;
import com.idega.data.IDOCompositePrimaryKeyException;
import com.idega.data.IDOEntityDefinition;
import com.idega.data.IDOException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.IDOQuery;
import com.idega.user.business.UserStatusBusinessBean;
import com.idega.user.data.Status;
import com.idega.user.data.StatusHome;
import com.idega.user.data.User;
import com.idega.user.data.UserBMPBean;
import com.idega.util.IWTimestamp;
import com.idega.util.YearPeriod;
import com.idega.util.datastructures.IntHashMap;
import com.idega.util.text.Name;
import com.idega.util.text.PostalAddress;
import com.idega.util.text.StreetAddress;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author <br><a href="mailto:aron@idega.is">Aron Birkir</a><br>
 * @version 1.0
 */

public class SchoolChoiceBMPBean extends AbstractCaseBMPBean implements SchoolChoice, Case {

	final static public String SCHOOLCHOICE = "comm_sch_choice";

	public final static String SCHOOL_SEASON = "school_season_id";
	public final static String SCHOOL_TYPE = "school_type_id";
	public final static String CURRENT_SCHOOL = "curr_school_id";
	public final static String CURRENT_SCHOOL_YEAR = "current_school_year_id";
	//public final static String GRADE = "grade";
	public final static String SCHOOL_YEAR = "school_year_id";
	public final static String CHOSEN_SCHOOL = "school_id";
	public final static String CHILD = "child_id";
	public final static String PREFERRED_PLACEMENT_DATE = "placement_date";

	public final static String CHANGEOFSCHOOL = "change_of_school";
	public final static String KEEPCHILDRENCARE = "keep_children_care";
	public final static String AUTOASSIGNMENT = "auto_assignment";
	public final static String CUSTODIANSAGGREE = "custodians_agree";
	public final static String SCHOOLCATALOGUE = "school_catalogue";
	public final static String FREETIMETHISSCHOOL = "child_care_this_school";
	public final static String FREETIMEOTHER = "child_care_other";
	public final static String EXTRA_MESSAGE = "extra_message";
	
	public final static String LANGUAGECHOICE = "language_choice";
	public final static String SCHOOLCHOICEDATE = "school_choice_date";
	public final static String MESSAGE = "message_body";
	public final static String CHOICEORDER = "choice_order";
	public final static String METHOD = "choice_method";

	public final static String WORK_SITUATION_1 = "work_situation_1";
	public final static String WORK_SITUATION_2 = "work_situation_2";
	public final static String GROUP_PLACE = "group_place";

	public final static String HAS_RECEIVED_PLACEMENT_MESSAGE = "placement_message";
	public final static String HAS_RECEIVED_CONFIRMATION_MESSAGE = "confirmation_message";

	public final static String CASE_STATUS_CREATED = "UBEH";
	public final static String CASE_STATUS_QUIET = "TYST";
	public final static String CASE_STATUS_PRELIMINARY = "PREL";
	public final static String CASE_STATUS_PLACED = "PLAC";
	public final static String CASE_STATUS_GROUPED = "GROU";
	public final static String CASE_STATUS_MOVED = "FLYT";
	public final static String CASE_STATUS_CANCELLED = "UPPS";

	public static final int NAME_SORT = 1;
	public static final int GENDER_SORT = 2;
	public static final int ADDRESS_SORT = 3;
	public static final int PERSONAL_ID_SORT = 4;
	public static final int LANGUAGE_SORT = 5;
	public static final int CREATED_SORT = 6;
	
	public static final int PLACED_SORT = 7;
	public static final int UNPLACED_SORT = 8;

	private static final String[] CASE_STATUS_KEYS = { "UBEH", "TYST", "PREL", "PLAC", "GROU", "FLYT" };
	private static final String[] CASE_STATUS_DESCRIPTIONS = { "Case open", "Sleep", "Preliminary", "Placed", "Grouped", "Moved" };

	public String getCaseStatusCreated() {
		return "UBEH";
	}
	public String getCaseStatusQuiet() {
		return "TYST";
	}
	public String getCaseStatusPreliminary() {
		return "PREL";
	}
	public String getCaseStatusPlaced() {
		return "PLAC";
	}
	public String getCaseStatusGrouped() {
		return "GROU";
	}
	public String getCaseStatusMoved() {
		return "FLYT";
	}

	public void initializeAttributes() {
		addGeneralCaseRelation();
		//this.addAttribute(CHILD, "child_id", true, true, Integer.class, MANY_TO_ONE, User.class);
		//this.addAttribute(CURRENT_SCHOOL, "Current school", true, true, Integer.class, MANY_TO_ONE, School.class);
		//this.addAttribute(GRADE, "Grade", Integer.class);
		//this.addAttribute(CHOSEN_SCHOOL, "Chosen school", true, true, Integer.class, MANY_TO_ONE, School.class);
		this.addManyToOneRelationship(CURRENT_SCHOOL,School.class);
		this.addManyToOneRelationship(CURRENT_SCHOOL_YEAR,SchoolYear.class);
		this.addManyToOneRelationship(CHOSEN_SCHOOL,School.class);
		this.addManyToOneRelationship(CHILD,User.class);
		this.addManyToOneRelationship(SCHOOL_SEASON,SchoolSeason.class);
		this.addManyToOneRelationship(SCHOOL_TYPE,SchoolType.class);
		this.addManyToOneRelationship(SCHOOL_YEAR,SchoolYear.class);

		this.addAttribute(PREFERRED_PLACEMENT_DATE, "Preferred placement date", true, true, Date.class);
		this.addAttribute(WORK_SITUATION_1, "Work situation one", Integer.class);
		this.addAttribute(WORK_SITUATION_2, "Work situation two", Integer.class);
		this.addAttribute(LANGUAGECHOICE, "Language choice", String.class);
		this.addAttribute(GROUP_PLACE, "Language choice", String.class, 10);
		this.addAttribute(SCHOOLCHOICEDATE, "choice date", Timestamp.class);
		this.addAttribute(MESSAGE, "message", String.class, 4000);
		this.addAttribute(CHOICEORDER, "choice order", Integer.class);
		this.addAttribute(METHOD, "method", Integer.class);

		this.addAttribute(CHANGEOFSCHOOL, "Change of school", Boolean.class);
		this.addAttribute(KEEPCHILDRENCARE, "Keep children care", Boolean.class);
		this.addAttribute(AUTOASSIGNMENT, "Autoassignment", Boolean.class);
		this.addAttribute(CUSTODIANSAGGREE, "Custodian agree", Boolean.class);
		this.addAttribute(SCHOOLCATALOGUE, "School catalogue", Boolean.class);
		this.addAttribute(FREETIMETHISSCHOOL, "School catalogue", Boolean.class);
		this.addAttribute(FREETIMEOTHER, "School catalogue", String.class, 255);
		this.addAttribute(EXTRA_MESSAGE, "Extra choice message", String.class, 255);
		
		this.addAttribute(HAS_RECEIVED_PLACEMENT_MESSAGE, "Placement message", Boolean.class);
		this.addAttribute(HAS_RECEIVED_CONFIRMATION_MESSAGE, "Confirmation message", Boolean.class);

		addIndex("IDX_COMM_SCH_CHOICE_2", new String[]{getIDColumnName(), CHILD});
		addIndex("IDX_COMM_SCH_CHOICE_3", new String[]{getIDColumnName(), CHOSEN_SCHOOL});
		addIndex("IDX_COMM_SCH_CHOICE_4", new String[]{getIDColumnName(), SCHOOL_SEASON});
		addIndex("IDX_COMM_SCH_CHOICE_5", new String[]{getIDColumnName(), CHOSEN_SCHOOL, SCHOOL_SEASON});
	}
	public String getEntityName() {
		return SCHOOLCHOICE;
	}
	public String getCaseCodeKey() {
		return SchoolConstants.SCHOOL_CHOICE_CASE_CODE_KEY;
	}
	public String getCaseCodeDescription() {
		return "School choice application";
	}

	public String[] getCaseStatusKeys() {
		return CASE_STATUS_KEYS;
	}
	public String[] getCaseStatusDescriptions() {
		return CASE_STATUS_DESCRIPTIONS;
	}

	public int getChildId() {
		return getIntColumnValue(CHILD);
	}

	/**Returns the user (child) that the schoolchoice is done for.
	 * @return User
	 */
	public User getChild() {
		return (User) getColumnValue(CHILD);
	}

	public void setChildId(int id) {
		setColumn(CHILD, id);
	}
	public int getCurrentSchoolId() {
		return getIntColumnValue(CURRENT_SCHOOL);
	}
	public School getCurrentSchool() {
		return (School) getColumnValue(CURRENT_SCHOOL);
	}
	public void setCurrentSchoolId(int id) {
		setColumn(CURRENT_SCHOOL, id);
	}
	public int getSchoolSeasonId() {
		return getIntColumnValue(SCHOOL_SEASON);
	}
	public SchoolSeason getSchoolSeason() {
		return (SchoolSeason) getColumnValue(SCHOOL_SEASON);
	}
	public void setSchoolSeasonId(int id) {
		setColumn(SCHOOL_SEASON, id);
	}
	public int getSchoolTypeId() {
		return getIntColumnValue(SCHOOL_TYPE);
	}
	public SchoolType getSchoolType() {
		return (SchoolType) getColumnValue(SCHOOL_TYPE);
	}
	public void setSchoolTypeId(int id) {
		setColumn(SCHOOL_TYPE, id);
	}
	public int getChosenSchoolId() {
		return getIntColumnValue(CHOSEN_SCHOOL);
	}
	public School getChosenSchool() {
		return (School) getColumnValue(CHOSEN_SCHOOL);
	}
	public void setChosenSchoolId(int id) {
		setColumn(CHOSEN_SCHOOL, id);
	}
	public SchoolYear getSchoolYear() {
		return (SchoolYear) getColumnValue(SCHOOL_YEAR);
	}
	public int getSchoolYearID() {
		return getIntColumnValue(SCHOOL_YEAR);
	}
	public void setSchoolYear(int schoolYearID) {
		setColumn(SCHOOL_YEAR, schoolYearID);
	}
	public SchoolYear getCurrentSchoolYear() {
		return (SchoolYear) getColumnValue(CURRENT_SCHOOL_YEAR);
	}
	public int getCurrentSchoolYearID() {
		return getIntColumnValue(CURRENT_SCHOOL_YEAR);
	}
	public void setCurrentSchoolYear(int schoolYearID) {
		setColumn(CURRENT_SCHOOL_YEAR, schoolYearID);
	}
	public int getWorkSituation1() {
		return getIntColumnValue(WORK_SITUATION_1);
	}
	public void setWorksituation1(int situation) {
		setColumn(WORK_SITUATION_1, situation);
	}
	public int getWorkSituation2() {
		return getIntColumnValue(WORK_SITUATION_1);
	}
	public void setWorksituation2(int situation) {
		setColumn(WORK_SITUATION_2, situation);
	}
	public Date getPlacementDate() {
		return (Date) getColumnValue(PREFERRED_PLACEMENT_DATE);
	}
	public void setPlacementDate(Date date) {
		setColumn(PREFERRED_PLACEMENT_DATE, date);
	}
	public String getLanguageChoice() {
		return getStringColumnValue(LANGUAGECHOICE);
	}
	public void setLanguageChoice(String language) {
		setColumn(LANGUAGECHOICE, language);
	}

	public String getGroupPlace() {
		return getStringColumnValue(GROUP_PLACE);
	}

	public void setGroupPlace(String place) {
		setColumn(GROUP_PLACE, place);
	}

	public Timestamp getSchoolChoiceDate() {
		return (Timestamp) getColumnValue(SCHOOLCHOICEDATE);
	}
	public void setSchoolChoiceDate(Timestamp stamp) {
		setColumn(SCHOOLCHOICEDATE, stamp);
	}
	public String getMessage() {
		return getStringColumnValue(MESSAGE);
	}
	public void setMessage(String msg) {
		setColumn(MESSAGE, msg);
	}
	public int getChoiceOrder() {
		return getIntColumnValue(CHOICEORDER);
	}
	public void setChoiceOrder(int order) {
		setColumn(CHOICEORDER, order);
	}
	public int getMethod() {
		return getIntColumnValue(METHOD);
	}
	public void setMethod(int method) {
		setColumn(METHOD, method);
	}
	public boolean getChangeOfSchool() {
		return getBooleanColumnValue(CHANGEOFSCHOOL);
	}
	public void setChangeOfSchool(boolean change) {
		setColumn(CHANGEOFSCHOOL, change);
	}
	public boolean getKeepChildrenCare() {
		return getBooleanColumnValue(KEEPCHILDRENCARE);
	}
	public void setKeepChildrenCare(boolean keepchildcare) {
		setColumn(KEEPCHILDRENCARE, keepchildcare);
	}
	public boolean getAutoAssign() {
		return getBooleanColumnValue(AUTOASSIGNMENT);
	}
	public void setAutoAssign(boolean auto) {
		setColumn(AUTOASSIGNMENT, auto);
	}
	public boolean getCustodiansAgree() {
		return getBooleanColumnValue(CUSTODIANSAGGREE, true);
	}
	public void setCustodiansAgree(boolean agree) {
		setColumn(CUSTODIANSAGGREE, agree);
	}
	public boolean getSchoolCatalogue() {
		return getBooleanColumnValue(SCHOOLCATALOGUE);
	}
	public void setSchoolCatalogue(boolean catalogue) {
		setColumn(SCHOOLCATALOGUE, catalogue);
	}
	public boolean getFreetimeInThisSchool() {
		return getBooleanColumnValue(FREETIMETHISSCHOOL);
	}
	public void setFreetimeInThisSchool(boolean freetimeInThisSchool) {
		setColumn(FREETIMETHISSCHOOL, freetimeInThisSchool);
	}
	public String getFreetimeOther() {
		return getStringColumnValue(FREETIMEOTHER);
	}
	public void setFreetimeOther(String other) {
		setColumn(FREETIMEOTHER, other);
	}

	public void setHasReceivedPlacementMessage(boolean hasReceivedMessage) {
		setColumn(HAS_RECEIVED_PLACEMENT_MESSAGE, hasReceivedMessage);
	}

	public void setHasReceivedConfirmationMessage(boolean hasReceivedMessage) {
		setColumn(HAS_RECEIVED_CONFIRMATION_MESSAGE, hasReceivedMessage);
	}

	public boolean getHasReceivedPlacementMessage() {
		return getBooleanColumnValue(HAS_RECEIVED_PLACEMENT_MESSAGE, false);
	}

	public boolean getHasReceivedConfirmationMessage() {
		return getBooleanColumnValue(HAS_RECEIVED_CONFIRMATION_MESSAGE, false);
	}
	
	public String getExtraChoiceMessage() {
		return getStringColumnValue(EXTRA_MESSAGE);
	}

	public void setExtraChoiceMessage(String extraMessage) {
		setColumn(EXTRA_MESSAGE, extraMessage);
	}

	public int ejbHomeCountBySchoolIDAndSeasonIDAndStatus(int schoolId, int seasonId, String[] statuses) throws IDOException {
		try {
			IDOQuery query = getIDOQueryFromSchoolIdSeasonIdAndStatus(schoolId, seasonId, statuses);
			query.setToCount();
			return idoGetNumberOfRecords(query);
		}catch (IDOLookupException e1) {
			throw new IDOException(e1.getMessage());
		}catch (IDOCompositePrimaryKeyException e2) {
			throw new IDOException(e2.getMessage());
		}
	}
	
	public Collection ejbFindBySchoolIDAndSeasonIDAndStatus(int schoolId, int seasonId, String[] statuses, int returningEntries, int startingEntries) throws FinderException {
		try {
			IDOQuery query = getIDOQueryFromSchoolIdSeasonIdAndStatus(schoolId, seasonId, statuses);
			return idoFindPKsByQuery(query, returningEntries, startingEntries);
		}catch (IDOLookupException e1) {
			throw new FinderException(e1.getMessage());
		}catch (IDOCompositePrimaryKeyException e2) {
			throw new FinderException(e2.getMessage());
		}
	}

	private IDOQuery getIDOQueryFromSchoolIdSeasonIdAndStatus(int schoolId, int seasonId, String[] statuses) throws IDOLookupException, IDOCompositePrimaryKeyException {
		IDOQuery query = this.idoQuery();
		IDOEntityDefinition caseDef = IDOLookup.getEntityDefinitionForClass(Case.class);
		IDOEntityDefinition userDef = IDOLookup.getEntityDefinitionForClass(User.class);
		query.appendSelectAllFrom(this).append(" sc,").append(caseDef.getSQLTableName()).append(" c,")
		.append(userDef.getSQLTableName()).append(" u ")
		.appendWhere("sc.").append(this.getIDColumnName()).append(" = c.").append(caseDef.getPrimaryKeyDefinition().getField().getSQLFieldName())
		.appendAnd().append(" u.").append(userDef.getPrimaryKeyDefinition().getField().getSQLFieldName())
		.append(" = sc.").append(CHILD);
		if (schoolId > 0) {
			query.appendAnd().append(" sc.").appendEquals(CHOSEN_SCHOOL, schoolId);
		}
		if (statuses != null && statuses.length > 0) {
			query.append(" and c.CASE_STATUS in (");
			for (int i = 0; i < statuses.length; i++) {
				if (i > 0)
					query.append(",");
				query.append("'");
				query.append(statuses[i]);
				query.append("'");
			}
			query.append(" ) ");
		}
		if (seasonId > 0) {
			query.appendAnd().append("sc.").appendEquals(SCHOOL_SEASON, seasonId);
		}
		query.appendOrderBy("u.last_name, u.first_name, u.middle_name");
		return query;
	}
	public Collection ejbFindByChosenSchoolId(int chosenSchoolId, int schoolSeasonId) throws javax.ejb.FinderException {
		return idoFindPKsBySQL("select * from " + getEntityName() + " where " + CHOSEN_SCHOOL + " = " + chosenSchoolId + " and " + SCHOOL_SEASON + " = " + schoolSeasonId);
	}

	public Collection ejbFindByChildId(int childId) throws javax.ejb.FinderException {
		return idoFindPKsBySQL("select * from " + getEntityName() + " where " + CHILD + " = " + childId);
	}

	public Collection ejbFindByChildId(int childId, int schoolSeasonId) throws javax.ejb.FinderException {
		return idoFindPKsBySQL("select * from " + getEntityName() + " where " + CHILD + " = " + childId + " and " + SCHOOL_SEASON + " = " + schoolSeasonId);
	}

	public Collection ejbFindByCodeAndStatus(String caseCode, String[] caseStatus, int schoolId, int schoolSeasonId) throws javax.ejb.FinderException {
		return ejbFindByCodeAndStatus(caseCode, caseStatus, schoolId, schoolSeasonId, null);
	}

	public Collection ejbFindAllWithLanguageWithinSeason(SchoolSeason season, String[] caseStatus) throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelectAllFrom(this).append(" s, proc_case c");
		query.appendWhereEquals("s."+getIDColumnName(), "c.proc_case_id");
		query.appendAndEquals("s."+SCHOOL_SEASON, season);
		query.appendAnd().append("s."+LANGUAGECHOICE).append(" is not null");
		query.appendAnd().append("c.case_status").appendNotInArrayWithSingleQuotes(caseStatus);
		return idoFindPKsByQuery(query);
	}
	
	public Collection ejbFindByCodeAndStatus(String caseCode, String[] caseStatus, int schoolId, int schoolSeasonId, String ordered) throws javax.ejb.FinderException {

		StringBuffer sql = new StringBuffer("select s.* from ");
		sql.append(getEntityName()).append(" s ");
		sql.append(",").append(CaseBMPBean.TABLE_NAME).append(" c ");
		sql.append(" where s.COMM_SCH_CHOICE_ID = c.PROC_CASE_ID ");
		//try{

		sql.append(" and c.CASE_CODE = '").append(caseCode).append("' ");
		sql.append(" and s.SCHOOL_ID = ").append(schoolId);
		sql.append(" and ").append(SCHOOL_SEASON).append(" = ").append(schoolSeasonId);
		sql.append(" and c.CASE_STATUS in (");
		for (int i = 0; i < caseStatus.length; i++) {
			if (i > 0)
				sql.append(",");
			sql.append("'");
			sql.append(caseStatus[i]);
			sql.append("'");
		}
		sql.append(" ) ");
		if (ordered != null && !"".equals(ordered)) {
			sql.append(" order by ").append(ordered);
		}
		/*}
		catch(java.rmi.RemoteException ex){}*/
		//System.err.println(" \n "+sql.toString()+" \n");
		return idoFindPKsBySQL(sql.toString());

	}

	public int ejbHomeGetNumberOfApplications(String caseStatus, int schoolID, int schoolSeasonID) throws IDOException {
		StringBuffer sql = new StringBuffer("select count(*) from ");
		sql.append(getEntityName()).append(" s ");
		sql.append(",").append(CaseBMPBean.TABLE_NAME).append(" c ");
		sql.append(" where s.COMM_SCH_CHOICE_ID = c.PROC_CASE_ID ");
		sql.append(" and c.CASE_CODE = '").append(SchoolConstants.SCHOOL_CHOICE_CASE_CODE_KEY).append("'");
		if (schoolID != -1)
			sql.append(" and s.SCHOOL_ID = ").append(schoolID);
		sql.append(" and s.").append(SCHOOL_SEASON).append(" = ").append(schoolSeasonID);
		sql.append(" and c.CASE_STATUS = '").append(caseStatus).append("'");

		return super.idoGetNumberOfRecords(sql.toString());
	}

	public int ejbHomeGetNumberOfApplications(String caseStatus, int schoolID, int schoolSeasonID, int schoolYearID) throws IDOException {
		StringBuffer sql = new StringBuffer("select count(*) from ");
		sql.append(getEntityName()).append(" s ");
		sql.append(",").append(CaseBMPBean.TABLE_NAME).append(" c ");
		sql.append(" where s.COMM_SCH_CHOICE_ID = c.PROC_CASE_ID ");
		sql.append(" and c.CASE_CODE = '").append(SchoolConstants.SCHOOL_CHOICE_CASE_CODE_KEY).append("'");
		if (schoolID != -1)
			sql.append(" and s.SCHOOL_ID = ").append(schoolID);
		sql.append(" and s.").append(SCHOOL_SEASON).append(" = ").append(schoolSeasonID);
		sql.append(" and s.").append(SCHOOL_YEAR).append(" = ").append(schoolYearID);
		sql.append(" and c.CASE_STATUS = '").append(caseStatus).append("'");

		return super.idoGetNumberOfRecords(sql.toString());
	}

	public int ejbHomeGetNumberOfHandledMoves(int seasonID) throws IDOException {
		
		
		StringBuffer sql = new StringBuffer("select count(*) from " +			"proc_case p, comm_sch_choice c " +			"where " +			"p.proc_case_id = c.comm_sch_choice_id and " +			"p.case_status = 'FLYT' and c.school_season_id = "+ seasonID);
			
		return super.idoGetNumberOfRecords(sql.toString());
	}

	public int ejbHomeGetNumberOfUnHandledMoves(int seasonID) throws IDOException {
		StringBuffer sql = new StringBuffer("select count(*) from " +			"proc_case p, proc_case_log l, comm_sch_choice c " +			"where " +			"p.proc_case_id = l.case_id and " +			"p.proc_case_id = c.comm_sch_choice_id and " +			"l.case_status_before = 'FLYT' and " +			"c.school_season_id =" + seasonID);			return super.idoGetNumberOfRecords(sql.toString());
	}
	
	public Collection ejbFindByChildAndSeason(int childID, int seasonID, String[] notInStatuses) throws javax.ejb.FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this).append(" sc, ").append("PROC_CASE pc ");
		sql.appendWhereEquals(CHILD, childID).appendAndEquals(SCHOOL_SEASON, seasonID);
		sql.appendAndEquals("sc." + getIDColumnName(), "pc.proc_case_id");
		if (notInStatuses != null) {
			sql.appendAnd().append("pc.case_status").appendNotInArrayWithSingleQuotes(notInStatuses);
		}
		return super.idoFindPKsBySQL(sql.toString());
	}

	public Collection ejbFindAllPlacedBySeason(int seasonID) throws javax.ejb.FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this).append(" sc, ").append("PROC_CASE pc ");
		sql.appendWhereEquals(SCHOOL_SEASON, seasonID);
		sql.appendAndEquals("sc." + getIDColumnName(), "pc.proc_case_id");
		sql.appendAndEqualsQuoted("pc.case_status", CASE_STATUS_PLACED);
		return super.idoFindPKsBySQL(sql.toString());
	}
	
	public Object ejbFindByChildAndChoiceNumberAndSeason(Integer childID,Integer choiceNumber, Integer seasonID) throws javax.ejb.FinderException {
		return super.idoFindOnePKByQuery(idoQueryGetSelect().appendWhereEquals(CHILD,childID).appendAndEquals(SCHOOL_SEASON,seasonID).appendAndEquals(CHOICEORDER,choiceNumber) );
	}

	public Collection ejbFindBySeason(final int seasonId) throws javax.ejb.FinderException {
		StringBuffer sql = new StringBuffer("select * from ");
		sql.append(SCHOOLCHOICE);
		sql.append(" where ");
		sql.append(SCHOOL_SEASON);
		sql.append(" = ");
		sql.append(seasonId);
		sql.append(" order by ");
		sql.append(CHOICEORDER);
		return super.idoFindPKsBySQL(sql.toString());
	}

	/**
	 * Finds the schoolchoices applied for a specific school year and season.
	 * It uses the Grade column to compare to the schoolYear (GRADE=SchoolYear.YearAge-1)
	 * @param season
	 * @param year
	 * @return A Collection of SchoolChoices who match the criteria
	 * @throws javax.ejb.FinderException
	 */
	public Collection ejbFindBySeasonAndSchoolYear(SchoolSeason season,SchoolYear year) throws javax.ejb.FinderException {
		StringBuffer sql = new StringBuffer("select * from ");
		sql.append(SCHOOLCHOICE);
		sql.append(" where ");
		sql.append(SCHOOL_SEASON);
		sql.append(" = ");
		int seasonId = ((Integer)season.getPrimaryKey()).intValue();
		sql.append(seasonId);
		sql.append(" and ");
		sql.append(SCHOOL_YEAR);
		sql.append(" = ");
		//int grade = year.getSchoolYearAge()-1;
		sql.append(year);
		sql.append(" order by ");
		sql.append(CHOICEORDER);
		return super.idoFindPKsBySQL(sql.toString());
	}

	public int ejbHomeGetCountByChildAndSchool(int childID, int schoolID) throws IDOException {
		return ejbHomeGetCountByChildAndSchoolAndStatus(childID, schoolID, null);
	}
	
	public int ejbHomeGetCountByChildAndSchoolAndStatus(int childID, int schoolID, String[] caseStatus) throws IDOException {
		IDOQuery query =idoQuery();
		query.appendSelectCountFrom().append(getEntityName()).append(" csc");
		query.append(", ").append(CaseBMPBean.TABLE_NAME).append(" pc");
		query.append(" where ");
		query.appendEquals("csc."+getIDColumnName(), "pc."+CaseBMPBean.TABLE_NAME + "_ID");
		query.appendAnd().appendEquals("csc."+CHILD, childID);
		query.appendAnd().appendEquals("csc."+CHOSEN_SCHOOL, schoolID);
		if (caseStatus != null) {
			query.appendAnd().append(CaseBMPBean.COLUMN_CASE_STATUS).appendInArrayWithSingleQuotes(caseStatus);
		}
		return super.idoGetNumberOfRecords(query);
	}
	
	public Collection ejbFindByChildAndSchool(int childID, int schoolID) throws javax.ejb.FinderException {
		IDOQuery query =idoQuery();
		query.appendSelectAllFrom().append(getEntityName()).append(" csc");
		query.append(", ").append(CaseBMPBean.TABLE_NAME).append(" pc");
		query.append(" where ");
		query.appendEquals("csc."+getIDColumnName(), "pc."+CaseBMPBean.TABLE_NAME + "_ID");
		query.appendAnd().appendEquals("csc."+CHILD, childID);
		query.appendAnd().appendEquals("csc."+CHOSEN_SCHOOL, schoolID);
		query.appendOrderBy(CaseBMPBean.COLUMN_CREATED);
		
		return super.idoFindPKsByQuery(query);
	}
	
	public Collection ejbFindByChildAndSchoolAndSeason(int childID, int schoolID, int seasonID) throws javax.ejb.FinderException {
		StringBuffer sql = new StringBuffer("select * from ");
		sql.append(SCHOOLCHOICE);
		sql.append(" where ");
		sql.append(CHILD);
		sql.append(" = ");
		sql.append(childID);
		sql.append(" and ");
		sql.append(SCHOOL_SEASON);
		sql.append(" = ");
		sql.append(seasonID);
		sql.append(" and ");
		sql.append(CHOSEN_SCHOOL);
		sql.append(" = ");
		sql.append(schoolID);
		//System.out.println(sql.toString());
		return super.idoFindPKsBySQL(sql.toString());
	}

	public Collection ejbFindAll() throws FinderException {
		return this.idoFindPKsBySQL("select * from " + getEntityName());
	}

	public Collection ejbFindChoices(int schoolID, int seasonID, int gradeYear, String[] validStatuses, String searchStringForUser, int orderBy, int numberOfEntries, int startingEntry) throws FinderException {
		return ejbFindChoices(schoolID, seasonID, gradeYear, new int[] {
		}, validStatuses, searchStringForUser, orderBy, numberOfEntries, startingEntry);
	}

	public int ejbHomeGetCount(String[] validStatuses) throws IDOException {
		return ejbHomeGetCount(-1, validStatuses);
	}

	public int ejbHomeGetCount(String[] validStatuses, int seasonID) throws IDOException {
		return ejbHomeGetCount(-1, seasonID, validStatuses);
	}

	public int ejbHomeGetCount(int schoolId, String[] validStatuses) throws IDOException {
		return ejbHomeGetCount(schoolId, -1, validStatuses);
	}
	
	public int ejbHomeGetCount (final SchoolSeason schoolSeason,
															final Date startDate, final Date endDate)
		throws IDOException {
		final Calendar dayAfterEndDate = Calendar.getInstance ();
		dayAfterEndDate.setTimeInMillis(endDate.getTime () + (1000 * 60 * 60 * 24));
		IDOQuery sql = idoQuery();
		sql.appendSelect ().append (" count (distinct " + CHILD + ") ");
		sql.appendFrom ().append (getEntityName());
		sql.appendWhereEquals (SCHOOL_SEASON, schoolSeason);
		sql.appendAnd ().append (SCHOOLCHOICEDATE).appendGreaterThanOrEqualsSign();
		sql.append(startDate);
		sql.appendAnd ().append (SCHOOLCHOICEDATE).appendLessThanSign();
		sql.append (new Date (dayAfterEndDate.getTimeInMillis ()));
		return idoGetNumberOfRecords(sql);
	}

	public int ejbHomeGetCount(int schoolId, int seasonID, String[] validStatuses) throws IDOException {
		if (validStatuses != null && validStatuses.length > 0) {
			IDOQuery query = idoQuery();
			query.appendSelectCountFrom().append(getEntityName()).append(" csc");
			query.append(", ").append(CaseBMPBean.TABLE_NAME).append(" pc");
			query.append(" where ");
			query.append("csc.").append(getIDColumnName()).appendEqualSign().append("pc.").append(CaseBMPBean.TABLE_NAME + "_ID").appendAnd().append("pc.").append(CaseBMPBean.COLUMN_CASE_STATUS).appendIn();
			query.appendWithinParentheses(idoQuery().appendCommaDelimitedWithinSingleQuotes(validStatuses));
			query.appendAnd().append("pc.").append(CaseBMPBean.COLUMN_CASE_CODE).appendEqualSign().appendWithinSingleQuotes(SchoolConstants.SCHOOL_CHOICE_CASE_CODE_KEY);

			if (schoolId > 0) {
				query.appendAnd().append("csc.").append(CHOSEN_SCHOOL).appendEqualSign().append(schoolId);
			}
			if (seasonID > 0) {
				query.appendAnd().append("csc.").append(SCHOOL_SEASON).appendEqualSign().append(seasonID);
			}
			
			return this.idoGetNumberOfRecords(query);
		}
		else {
			IDOQuery query = idoQuery();
			query.appendSelectCountFrom(this);
			return this.idoGetNumberOfRecords(query);

		}
	}

	public int ejbHomeGetCount(int schoolID, int seasonID, int gradeYear, int[] choiceOrder, String[] validStatuses, String searchStringForUser) throws IDOException {
		IDOQuery query = getIDOQuery(schoolID, seasonID, gradeYear, choiceOrder, validStatuses, searchStringForUser, true, false, -1);
		return this.idoGetNumberOfRecords(query);
	}
	
	public int ejbHomeGetCount(int schoolID, int seasonID, int gradeYear, int[] choiceOrder, String[] validStatuses, String searchStringForUser, int placementType) throws IDOException {
		IDOQuery query = getIDOQuery(schoolID, seasonID, gradeYear, choiceOrder, validStatuses, searchStringForUser, true, false, -1, placementType);
		return this.idoGetNumberOfRecords(query);
	}
	
	public int ejbHomeGetCountOutsideInterval(int schoolID, int seasonID, int gradeYear, int[] choiceOrder, String[] validStatuses, String searchStringForUser, Date from, Date to) throws IDOException {
		IDOQuery query = getIDOQuery(schoolID, seasonID, gradeYear, choiceOrder, validStatuses, searchStringForUser, true, false, -1);
		query.appendAnd().appendLeftParenthesis().append(SCHOOLCHOICEDATE).appendLessThanSign().appendWithinSingleQuotes(from)
		.appendOr().append(SCHOOLCHOICEDATE).appendGreaterThanSign().appendWithinSingleQuotes(to).appendRightParenthesis();
		return this.idoGetNumberOfRecords(query);
	}

	public Collection ejbFindChoices(int schoolID, int seasonID, int gradeYear, int[] choiceOrder, String[] validStatuses, String searchStringForUser, int orderBy, int numberOfEntries, int startingEntry) throws FinderException {
		IDOQuery query = getIDOQuery(schoolID, seasonID, gradeYear, choiceOrder, validStatuses, searchStringForUser, false, false, orderBy);
		return this.idoFindPKsByQuery(query, numberOfEntries, startingEntry);
	}
	
	public Collection ejbFindChoices(int schoolID, int seasonID, int gradeYear, int[] choiceOrder, String[] validStatuses, String searchStringForUser, int orderBy, int numberOfEntries, int startingEntry, int placementType) throws FinderException {
		IDOQuery query = getIDOQuery(schoolID, seasonID, gradeYear, choiceOrder, validStatuses, searchStringForUser, false, false, false, orderBy, placementType);
		return this.idoFindPKsByQuery(query, numberOfEntries, startingEntry);
	}

	public IDOQuery getIDOQuery(int schoolID, int seasonID, int gradeYear, int[] choiceOrder, String[] validStatuses, String searchStringForUser, boolean selectCount, boolean selectOnlyChildIDs, int orderBy) {
		return getIDOQuery(schoolID, seasonID, gradeYear, choiceOrder, validStatuses, searchStringForUser, selectCount, selectOnlyChildIDs, orderBy, -1);
		
	}
	
	public IDOQuery getIDOQuery(int schoolID, int seasonID, int schoolYear, int[] choiceOrder, String[] validStatuses, String searchStringForUser, boolean selectCount, boolean selectOnlyChildIDs, int orderBy, int placementType) {
		return getIDOQuery(schoolID, seasonID, schoolYear, choiceOrder, validStatuses, searchStringForUser, false, false, true, orderBy, placementType);
	}
	
	public IDOQuery getIDOQuery(int schoolID, int seasonID, int schoolYear, int[] choiceOrder, String[] validStatuses, String searchStringForUser, boolean selectCount, boolean selectOnlyChildIDs, boolean searchOnAddr, int orderBy, int placementType) {
		boolean search = searchStringForUser != null && !searchStringForUser.equals("");
		boolean statuses = validStatuses != null && validStatuses.length > 0;

		IDOQuery query = idoQuery();
		if (schoolID < 1 && seasonID < 1 && schoolYear < 1 && !search && !statuses) {
			if (selectCount) {
				query.appendSelectCountFrom(this);
			}
			else {
				query.appendSelectAllFrom(this);
			}
			//return ejbFindAll();
			return query;
		}

		boolean needAnd = false;

		if (selectOnlyChildIDs) {
			query.appendSelect().append("csc.child_id").appendFrom();
		}
		else {
			if (selectCount) {
				query.appendSelectCountFrom();
			}
			else {
				query.appendSelect().append("*").appendFrom();
			}
		}

		query.append(getEntityName()).append(" csc");
		query.append(", ").append(UserBMPBean.TABLE_NAME).append(" u");
		query.append(", ").append(CaseBMPBean.TABLE_NAME).append(" pc");
		if(searchOnAddr){
			query.append(", ic_address a, ic_user_address ua");
		}
		
		query.appendWhere();
		query.append("u.").append(UserBMPBean.getColumnNameUserID()).appendEqualSign().append("csc.").append(CHILD);
		query.appendAnd().append("csc.").append(getIDColumnName()).appendEqualSign().append("pc.").append(CaseBMPBean.TABLE_NAME + "_ID");
		needAnd = true;
		if(searchOnAddr){
		    if (needAnd) {
				query.appendAnd();
			}
			query.append("csc.").append(CHILD).appendEqualSign().append("ua.").append(UserBMPBean.getColumnNameUserID());
			query.appendAnd().append("ua.ic_address_id").appendEqualSign().append("a.ic_address_id");
			needAnd = true;
		}

		if (statuses) {
			if (needAnd) {
				query.appendAnd();
			}
			query.append("pc.").append(CaseBMPBean.COLUMN_CASE_STATUS).appendIn();
			query.appendWithinParentheses(idoQuery().appendCommaDelimitedWithinSingleQuotes(validStatuses));
			query.appendAnd().append("pc.").append(CaseBMPBean.COLUMN_CASE_CODE).appendEqualSign().appendWithinSingleQuotes(SchoolConstants.SCHOOL_CHOICE_CASE_CODE_KEY);
			needAnd = true;
		}

		if (search) {
			if (needAnd) {
				query.appendAnd();
			}
			query.append("(").append("u.").append(UserBMPBean.getColumnNameFirstName()).append(" like '%").append(searchStringForUser).append("%'").appendOr().append("u.").append(UserBMPBean.getColumnNameLastName()).append(" like '%").append(searchStringForUser).append("%'").appendOr().append("u.").append(UserBMPBean.getColumnNameMiddleName()).append(" like '%").append(searchStringForUser).append("%'").appendOr().append("u.").append(UserBMPBean.getColumnNamePersonalID()).append(" like '%").append(searchStringForUser).append("%'");
			query.append(")");
			needAnd = true;
		}

		if (seasonID > 0) {
			if (needAnd) {
				query.appendAnd();
			}
			query.append("csc.").append(SCHOOL_SEASON).appendEqualSign().append(seasonID);
			needAnd = true;
		}

		if (schoolID > 0) {
			if (needAnd) {
				query.appendAnd();
			}
			query.append("csc.").append(CHOSEN_SCHOOL).appendEqualSign().append(schoolID);
			needAnd = true;
		}

		if (schoolYear > 0) {
			if (needAnd) {
				query.appendAnd();
			}
			query.append("csc.").append(SCHOOL_YEAR).appendEqualSign().append(schoolYear);
			needAnd = true;
		}

		if (choiceOrder != null && choiceOrder.length > 0) {
			if (needAnd) {
				query.appendAnd();
			}
			query.append("csc.").append(CHOICEORDER).append(" in (");
			for (int i = 0; i < choiceOrder.length; i++) {
				if (i != 0) {
					query.append(", ");
				}
				query.append(choiceOrder[i]);
			}
			query.append(")");
			needAnd = true;
		}
		
		if (placementType != -1){
			if (placementType == PLACED_SORT){
				query.appendAnd().append("csc.child_id in (");	
			}
			else {
				query.appendAnd().append("csc.child_id not in (");	
			}
			
			query.appendSelect().append("scm.ic_user_id");
			query.appendFrom().append("sch_class_member").append(" scm");
			query.append(", ").append("sch_school_class").append(" sc");
			//query.append(", ").append("sch_school_year").append(" sy");
			query.appendWhere();
			query.appendEquals("scm.sch_school_class_id", "sc.SCH_SCHOOL_CLASS_ID");
			//query.appendAndEquals("scm.sch_school_year_id", "sy.sch_school_year_id");
			
			if (schoolID > 0){
				query.appendAndEquals("sc.school_id", schoolID);
				query.appendAnd().append("(sc." + SchoolClassBMPBean.COLUMN_VALID).appendEqualSign().appendWithinSingleQuotes("Y").appendOr().append("sc." + SchoolClassBMPBean.COLUMN_VALID).append(" is null)");		
			}
			if (seasonID > 0){
				query.appendAndEquals("sc.sch_school_season_id", seasonID);	
			}
			/*if (gradeYear > 0){
				query.appendAndEquals("sy.year_age", gradeYear);	
			}
			*/
			query.appendRightParenthesis();
		}
		
		if (orderBy != -1) {
			if (orderBy == NAME_SORT)
				query.appendOrderBy("u.last_name,u.first_name,u.middle_name");
			else if (orderBy == ADDRESS_SORT)
				query.appendOrderBy("a.street_name,a.street_number,u.last_name,u.first_name,u.middle_name");
			else if (orderBy == GENDER_SORT)
				query.appendOrderBy("u.ic_gender_id,u.last_name,u.first_name,u.middle_name");
			else if (orderBy == PERSONAL_ID_SORT)
				query.appendOrderBy("u.personal_id,u.last_name,u.first_name,u.middle_name");
			else if (orderBy == LANGUAGE_SORT)
				query.appendOrderBy("csc.language_choice,u.last_name,u.first_name,u.middle_name");
			else if (orderBy == CREATED_SORT)
				query.appendOrderBy("pc.created desc,u.last_name,u.first_name,u.middle_name");
		}

//		System.out.println(query.toString());
		return query;
	}

	public Collection ejbFindBySchoolAndSeasonAndGrade(int schoolID, int seasonID, int schoolYear) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(getEntityName()).appendWhere().append(CHOSEN_SCHOOL).appendEqualSign().append(schoolID).appendAnd().append(SCHOOL_SEASON).appendEqualSign().append(seasonID).appendAnd().append(SCHOOL_YEAR).appendEqualSign().append(schoolYear);

		return super.idoFindPKsBySQL(sql.toString());
	}

	public Collection ejbFindBySchoolAndFreeTime(int schoolId, int schoolSeasonID, boolean freeTimeInSchool) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.append("select sc.* from ").append(getEntityName()).append(" sc,");
		sql.append("PROC_CASE pc ");
		sql.appendWhere();
		sql.append(CHOSEN_SCHOOL);
		sql.appendEqualSign();
		sql.append(schoolId);
		sql.appendAnd();
		sql.append(SCHOOL_SEASON);
		sql.appendEqualSign();
		sql.append(schoolSeasonID);
		sql.appendAnd();
		sql.append(FREETIMETHISSCHOOL);
		sql.appendEqualSign();
		if (freeTimeInSchool)
			sql.appendWithinSingleQuotes("Y");
		else
			sql.appendWithinSingleQuotes("N");
		sql.appendAnd().append("pc.PROC_CASE_ID").appendEqualSign().append("sc.COMM_SCH_CHOICE_ID");
		sql.appendAnd().append("pc.CASE_STATUS").appendIn().appendLeftParenthesis().appendWithinSingleQuotes("PREL");
		sql.append(",").appendWithinSingleQuotes("PLAC").append(",").appendWithinSingleQuotes("FLYT").appendRightParenthesis();

		return super.idoFindPKsBySQL(sql.toString());
	}

	public Collection ejbFindChoicesInClassAndSeasonAndSchool(int classID, int seasonID, int schoolID, boolean confirmation) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.append("select sc.* from ").append(getEntityName()).append(" sc,");
		sql.append("sch_school_class c, sch_class_member cm ");
		sql.appendWhere();
		sql.append("sc.").append(CHOSEN_SCHOOL);
		sql.appendEqualSign();
		sql.append(schoolID);
		sql.appendAndEquals("sc." + SCHOOL_SEASON, seasonID);
		sql.appendAndEquals("c.school_id", "sc." + CHOSEN_SCHOOL);
		sql.appendAndEquals("c.sch_school_class_id", classID);
		sql.appendAndEquals("c.sch_school_class_id", "cm.sch_school_class_id");
		sql.appendAndEquals("cm.ic_user_id", "sc." + CHILD);
		if (confirmation) {
			sql.appendAnd().appendLeftParenthesis();
			sql.append("sc." + HAS_RECEIVED_CONFIRMATION_MESSAGE).appendEqualSign().append("'N'");
			sql.appendOr().append("sc." + HAS_RECEIVED_CONFIRMATION_MESSAGE).append(" is null").appendRightParenthesis();
		}
		else {
			sql.appendAnd().appendLeftParenthesis();
			sql.append("sc." + HAS_RECEIVED_PLACEMENT_MESSAGE).appendEqualSign().append("'N'");
			sql.appendOr().append("sc." + HAS_RECEIVED_PLACEMENT_MESSAGE).append(" is null").appendRightParenthesis();
		}

		return idoFindPKsBySQL(sql.toString());
	}

	public int ejbHomeGetNumberOfChoices(int userID, int seasonID) throws IDOException {
		return ejbHomeGetNumberOfChoices(userID, seasonID, null);
	}
	
	public int ejbHomeGetNumberOfChoices(int userID, int seasonID, String[] notInStatuses) throws IDOException {
		IDOQuery sql = idoQuery();
		sql.appendSelectCountFrom(this).append(" sc, ").append("PROC_CASE pc ");
		sql.appendWhereEquals(CHILD, userID).appendAndEquals(SCHOOL_SEASON, seasonID);
		sql.appendAndEquals("sc." + getIDColumnName(), "pc.proc_case_id");
		if (notInStatuses == null) {
			sql.appendAnd().append("pc.case_status").appendNotInArrayWithSingleQuotes(notInStatuses);
		}
		return super.idoGetNumberOfRecords(sql.toString());
	}

	public int ejbHomeGetMoveChoices(int userID, int schoolID, int seasonID) throws IDOException {
		IDOQuery sql = idoQuery();
		sql.append("select count(*) from ").append(getEntityName()).append(" sc,").append("PROC_CASE pc ");
		sql.appendWhereEquals("sc." + CHILD, userID);
		sql.appendAndEquals("sc." + getIDColumnName(), "pc.proc_case_id");
		sql.appendAndEquals("sc." + SCHOOL_SEASON, seasonID);
		sql.appendAnd().append(CHOSEN_SCHOOL).appendNOTEqual().append(schoolID);
		sql.appendAnd().append("pc.case_status").appendIn("'FLYT','PLAC'");
		return super.idoGetNumberOfRecords(sql.toString());
	}

	public int ejbHomeGetChoices(int userID, int seasonID, String[] notInStatus) throws IDOException {
		return ejbHomeGetChoices(userID, -1, seasonID, notInStatus);
	}
	
	public int ejbHomeGetChoices(int userID, int schoolID, int seasonID, String[] notInStatus) throws IDOException {
		IDOQuery sql = idoQuery();
		sql.appendSelectCountFrom(this).append(" sc,").append("PROC_CASE pc ");
		sql.appendWhereEquals("sc." + CHILD, userID);
		sql.appendAndEquals("sc." + getIDColumnName(), "pc.proc_case_id");
		sql.appendAndEquals("sc." + SCHOOL_SEASON, seasonID);
		if (schoolID != -1 ) {
			sql.appendAndEquals("sc." + CHOSEN_SCHOOL, schoolID);
		}
		if (notInStatus != null) {
			sql.appendAnd().append("pc.case_status").appendNotInArrayWithSingleQuotes(notInStatus);
		}
		return super.idoGetNumberOfRecords(sql.toString());
	}
	
	public Collection ejbFindByParent(Case parent)throws FinderException{
		return super.ejbFindSubCasesUnder(parent);
	}
	
	public String getSQLForChildrenWithouWithoutSchoolChoice(SchoolSeason season,SchoolYear year, boolean onlyInCommune,boolean onlyLastGrade,int maxAge,boolean count){
	    Integer seasonID = (Integer)season.getPrimaryKey();
	    //Integer yearID = (Integer)year.getPrimaryKey();
        int yearOfBirth = new IWTimestamp(season.getSchoolSeasonStart()).getYear() - year.getSchoolYearAge();
        YearPeriod period = new YearPeriod(yearOfBirth,yearOfBirth);
        IWTimestamp dateFrom = period.getFirstTimestamp();
        IWTimestamp dateTo = period.getLastTimestamp();
        boolean addAgeCheck = year.getSchoolYearName().equals("F") || year.getSchoolYearName().equals("1");
        Integer statusID = null;
        try {
            statusID = (Integer)((StatusHome) IDOLookup.getHome(Status.class)).findByStatusKey(UserStatusBusinessBean.STATUS_DECEASED).getPrimaryKey();
        } catch (IDOLookupException e1) {
            e1.printStackTrace();
        } catch (EJBException e1) {
            e1.printStackTrace();
        } catch (FinderException e1) {
            e1.printStackTrace();
        }
        IDOQuery sql = idoQuery();
        if(count)
            sql.append(" select count( *) ").append("\n");
        else{
            sql.append(" select distinct u.ic_user_id, u.personal_id, u.first_name,u.middle_name, u.last_name, ").append("\n");
            sql.append(" a.street_name,a.street_number, p.postal_code, p.name , c.commune_name, c.default_commune, ").append("\n");
            sql.append(" mom.ic_user_id, mom.first_name,mom.middle_name,mom.last_name, ").append("\n");
            sql.append(" dad.ic_user_id,dad.first_name,dad.middle_name,dad.last_name ").append("\n");
            sql.append(" ,us.status_id ").append("\n");
        }
            
        sql.append(" from ic_address a left join ic_commune c on (a.ic_commune_id = c.ic_commune_id) ").append("\n");
        sql.append(" left join ic_postal_code p on (a.postal_code_id = p.ic_postal_code_id), ic_user_address ua, ic_user u ").append("\n");
        if(!count){
            sql.append(" left   join  (select p1.ic_user_id , p1.first_name, p1.middle_name, p1.last_name, gr1.related_ic_group_id from ic_user p1, ic_group_relation gr1 ").append("\n");
            sql.append(" where p1.ic_user_id = gr1.ic_group_id and (gr1.relationship_type = 'FAM_PARENT' or gr1.relationship_type = 'FAM_CUSTODIAN')  and p1.ic_gender_id = 2  ").append("\n");
            sql.append(" and gr1.group_relation_status = 'ST_ACTIVE'    ) ").append("\n");
            sql.append(" mom  on ( u.ic_user_id = mom.related_ic_group_id ) ").append("\n");
            sql.append(" left   join  (select p2.ic_user_id , p2.first_name, p2.middle_name, p2.last_name, gr2.related_ic_group_id from ic_user p2, ic_group_relation gr2 ").append("\n");
            sql.append(" where p2.ic_user_id = gr2.ic_group_id and (gr2.relationship_type = 'FAM_PARENT' or gr2.relationship_type = 'FAM_CUSTODIAN' ) and p2.ic_gender_id = 1").append("\n");  
            sql.append(" and gr2.group_relation_status = 'ST_ACTIVE'    ) ").append("\n");
            sql.append(" dad  on ( u.ic_user_id = dad.related_ic_group_id ) ").append("\n");
        }
        sql.append(" left join ic_usergroup_status us on (u.ic_user_id = us.ic_user_id and us.status_id = ").append(statusID).append(" )  ").append("\n");
        sql.append(" where  u.ic_user_id = ua.ic_user_id ").append("\n");
        sql.append(" and ua.ic_address_id = a.ic_address_id ").append("\n");
        if(addAgeCheck) {
            sql.append(" and u.date_of_birth >= ").append(dateFrom.getDate()).append("\n");
            sql.append(" and u.date_of_birth <= ").append(dateTo.getDate()).append("\n");
        }
        sql.append(" and u.ic_user_id not in( ").append("\n");
        sql.append(" select u.ic_user_id ").append("\n");
        sql.append("  from comm_sch_choice ch, ic_user u ").append("\n");
        sql.append("  where u.ic_user_id = ch.child_id ").append("\n");
        sql.append("  and ch.school_season_id = ").append(seasonID);
        // only check age for F and 1 years
        if(addAgeCheck) {
            sql.append(" and u.date_of_birth >= ").append(dateFrom.getDate()).append("\n");
            sql.append(" and u.date_of_birth <= ").append(dateTo.getDate()).append("\n");
        }
        sql.append(" )");
        //temporary check
//        if (!year.getSchoolYearName().equals("F")) {
            if((year.getSchoolYearName().equals("1")) || (!onlyLastGrade) ){
	            
		            try {
		                SchoolSeason previousSeason = season.getPreviousSeason();
		                if(previousSeason!=null){
		                    int previousSeasonId = ((Integer) previousSeason.getPrimaryKey()).intValue();
				            sql.append(" and u.ic_user_id not in( ").append("\n");
				            sql.append("     select u.ic_user_id ").append("\n");
				            sql.append("     from sch_class_member mb,sch_school_class cl, ic_user u ").append("\n");
				            sql.append("     where mb.sch_school_class_id = cl.sch_school_class_id ").append("\n");
				            sql.append("     and u.ic_user_id = mb.ic_user_id ").append("\n");
				            sql.append("     and cl.sch_school_season_id = ").append(previousSeasonId).append("\n");
				            sql.append("    and u.date_of_birth >= ").append(dateFrom.getDate()).append("\n");
				            sql.append("    and u.date_of_birth <= ").append(dateTo.getDate()).append("\n");
				            sql.append("     ) ").append("\n");
		                }
		            } catch (FinderException e2) {
		                e2.printStackTrace();
		            }
				
            }
            else{
                try {
                    SchoolSeason lastSeason = season.getPreviousSeason();
                    SchoolYear lastYear = year.getPreviousSchoolYearFromAge();
                    if(lastSeason!=null){
                        Integer lastSeasonID = ((Integer) lastSeason.getPrimaryKey());
                        Integer lastYearID = (Integer) lastYear.getPrimaryKey();
                    sql.append(" and u.ic_user_id in( ").append("\n");
                    sql.append("select student.ic_user_id").append("\n");
                    	sql.append(" from sch_school_sch_school_year school,sch_school_year schoolyear, sch_school_class class, sch_class_member student").append("\n");
                    	sql.append(" where school.sch_school_year_id = schoolyear.sch_school_year_id").append("\n");
                    	sql.append(" and student.sch_school_year_id  = school.sch_school_year_id").append("\n");
                    	sql.append(" and class.school_id = school.sch_school_id").append("\n");
                    	sql.append(" and class.sch_school_season_id = " + lastSeasonID).append("\n");
                    	sql.append(" and class.sch_school_class_id = student.sch_school_class_id").append("\n");
                    	if (year != null) {
                    		sql.append(" and schoolyear.sch_school_year_id = " + lastYearID).append("\n");
                    	}
                    	sql.append(" and exists").append("\n");
                    	sql.append(" (").append("\n");
                    	sql.append(" select s.sch_school_id, max (y.year_age)").append("\n");
                    	sql.append(" from sch_school_sch_school_year s, sch_school_year y").append("\n");
                    	sql.append(" where s.sch_school_year_id = y.sch_school_year_id").append("\n");
                    	sql.append(" group by s.sch_school_id").append("\n");
                    	sql.append(" having max (y.year_age) <=").append(maxAge).append("\n");
                    	sql.append(" and schoolyear.year_age = max (y.year_age)").append("\n");
                    	//sql.append (" and school.sch_school_year_id =s.sch_school_year_id");
                    	sql.append(" and school.sch_school_id = s.sch_school_id").append("\n");
                    	sql.append(" )").append("\n");
                    	sql.append(" ) ").append("\n");
                    }
                } catch (EJBException e) {
                    e.printStackTrace();
                } catch (FinderException e) {
                    e.printStackTrace();
                }
            }
//        }
        if(onlyInCommune)
            sql.append(" and c.default_commune  = 'Y'").append("\n");
        sql.append(" and us.status_id is null ").append("\n");
        logSQL(sql.toString());
        return sql.toString();
	}
	
	public int ejbHomeCountChildrenWithoutSchoolChoice(SchoolSeason season,SchoolYear year, boolean onlyInCommune,boolean onlyLastGrade,int maxAge) throws SQLException{
	    
		try {
		    String sql = getSQLForChildrenWithouWithoutSchoolChoice(season,year,onlyInCommune,onlyLastGrade,maxAge,true).toString();
            //System.out.println(sql.toString());
            return getIntTableValue(sql.toString());
           
        } catch (Exception e) {
            throw new SQLException(e.getMessage());
        }
        
	}
	
	public MailReceiver[] ejbHomeGetChildrenWithoutSchoolChoice(SchoolSeason season,SchoolYear year, boolean onlyInCommune,boolean onlyLastGrade,int maxAge) throws FinderException {
	    try {
            String sql = getSQLForChildrenWithouWithoutSchoolChoice(season,year,onlyInCommune,onlyLastGrade,maxAge,false);
            
            logSQL(sql.toString());
            Connection conn = null;
            Statement  stmt = null;
            ResultSet rs = null;
            
            try {
                conn = getConnection();
                stmt = conn.createStatement();
                rs = stmt.executeQuery(sql.toString());
                ArrayList receivers = new ArrayList();
                MailReceiver receiver;
                IntHashMap childIds = new IntHashMap();
                while(rs.next()){
                   int childID = rs.getInt(1);
                   if(!childIds.containsKey(childID)){
                    	receiver = new MailReceiver();
                     receiver.setSsn(rs.getString(2));
                     receiver.setStudentName(new Name(rs.getString(3),rs.getString(4),rs.getString(5)).getName());
                     receiver.setStreetAddress(new StreetAddress(rs.getString(6),rs.getString(7)).toString());
                     receiver.setPostalAddress(new PostalAddress(rs.getString(8),rs.getString(9)).toString());
                     // parents
                     int parent1 = rs.getInt(12);
                     int parent2 = rs.getInt(16);
                     if(parent1!=0)
                         receiver.setParentName(new Name(rs.getString(13),rs.getString(14),rs.getString(15)).getName());
                     else if(parent2!=0){
                         receiver.setParentName(new Name(rs.getString(17),rs.getString(18),rs.getString(19)).getName());
                     }
                     else{
                         receiver.setParentName("?");
                     }
                     String defaultCommune = rs.getString(11);
                     receiver.setInDefaultCommune(defaultCommune!=null && defaultCommune.equals("Y") );
                     receivers.add(receiver);
                    	childIds.put(childID,"1");
                   }
                }
                return (MailReceiver[]) receivers.toArray(new MailReceiver[receivers.size()]);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            finally{
                try {
                    if(rs!=null)
                        rs.close();
                    if(stmt!=null)
                        stmt.close();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                if(conn!=null)
                    freeConnection(conn);
            }
          
	    }catch (EJBException e) {
        		throw new FinderException(e.getMessage());
        }
        return null;
	}

}
