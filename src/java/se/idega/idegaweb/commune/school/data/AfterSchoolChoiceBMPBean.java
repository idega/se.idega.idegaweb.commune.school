/*
 * $Id: AfterSchoolChoiceBMPBean.java,v 1.3 2004/10/14 11:42:49 thomas Exp $
 *
 * Copyright (C) 2002 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.school.data;

import java.util.Collection;

import javax.ejb.FinderException;
import se.idega.idegaweb.commune.care.data.ChildCareApplicationBMPBean;

import com.idega.block.process.data.CaseStatus;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolSeason;
import com.idega.data.IDOQuery;

/**
 * This  does something very clever.....
 * 
 * @author palli
 * @version 1.0
 */
public class AfterSchoolChoiceBMPBean extends ChildCareApplicationBMPBean implements AfterSchoolChoice {

	private final static String CASE_CODE_KEY = "MBFRITV";
	private final static String CASE_CODE_KEY_DESC = "Application for after school centre";

	private final static String SCHOOL_SEASON = "school_season_id";

	/**
	 * @see com.idega.block.process.data.AbstractCaseBMPBean#getCaseCodeKey()
	 */
	public String getCaseCodeKey() {
		return CASE_CODE_KEY;
	}

	/**
	 * @see com.idega.block.process.data.AbstractCaseBMPBean#getCaseCodeDescription()
	 */
	public String getCaseCodeDescription() {
		return CASE_CODE_KEY_DESC;
	}
	/* (non-Javadoc)
	 * @see com.idega.data.GenericEntity#initializeAttributes()
	 */
	
	public void initializeAttributes() {
		super.initializeAttributes();
		this.addManyToOneRelationship(SCHOOL_SEASON, SchoolSeason.class);
	}

	public int getSchoolSeasonId() {
		return getIntColumnValue(SCHOOL_SEASON);
	}

	public void setSchoolSeasonId(int schoolSeasonID) {
		setColumn(SCHOOL_SEASON, schoolSeasonID);
	}

	public Collection ejbFindByChildAndSeason(Integer childID, Integer seasonID) throws javax.ejb.FinderException {
		IDOQuery query = super.idoQueryGetSelect().appendWhereEquals(CHILD_ID, childID.intValue());
		query.appendAndEquals(SCHOOL_SEASON, seasonID.intValue());
		query.appendOrderBy(CHOICE_NUMBER);

		return super.idoFindPKsByQuery(query);
	}
	
	public Object ejbFindByChildAndChoiceNumberAndSeason(Integer childID,Integer choiceNumber, Integer seasonID) throws javax.ejb.FinderException {
		IDOQuery query = super.idoQueryGetSelect().appendWhereEquals(CHILD_ID, childID.intValue());
		query.appendAndEquals(SCHOOL_SEASON, seasonID.intValue());
		query.appendAndEquals(CHOICE_NUMBER,choiceNumber.intValue());
		query.appendOrderBy(CHOICE_NUMBER);

		return super.idoFindOnePKByQuery(query);
	}

	public Object ejbFindByChildAndChoiceNumberAndSeason(Integer childID,Integer choiceNumber, Integer seasonID, String[] caseStatus) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this).append(" c, proc_case p");
		sql.appendWhereEquals("c."+getIDColumnName(), "p.proc_case_id");
		sql.appendAndEquals("c."+SCHOOL_SEASON, seasonID.intValue());
		sql.appendAndEquals("c."+CHOICE_NUMBER,choiceNumber.intValue());
		sql.appendAndEquals("c."+CHILD_ID,childID.intValue());
		sql.appendAnd().append("p.case_status").appendInArrayWithSingleQuotes(caseStatus);
		sql.appendAnd().appendEqualsQuoted("p.case_code",CASE_CODE_KEY);
		sql.appendOrderBy("c."+QUEUE_DATE+",c."+QUEUE_ORDER);

		return super.idoFindOnePKByQuery(sql);
	}
	
	public Object ejbFindByChildAndProviderAndSeason(int childID, int providerID, int seasonID, String[] caseStatus) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this).append(" c, proc_case p");
		sql.appendWhereEquals("c."+getIDColumnName(), "p.proc_case_id");
		sql.appendAndEquals("c."+SCHOOL_SEASON, seasonID);
		sql.appendAndEquals("c."+PROVIDER_ID,providerID);
		sql.appendAndEquals("c."+CHILD_ID,childID);
		sql.appendAnd().append("p.case_status").appendInArrayWithSingleQuotes(caseStatus);
		sql.appendAnd().appendEqualsQuoted("p.case_code",CASE_CODE_KEY);
		sql.appendOrderBy("c."+QUEUE_DATE+",c."+QUEUE_ORDER);

		return super.idoFindOnePKByQuery(sql);
	}
	
	public Collection ejbFindAllCasesByProviderAndStatus(int providerId, CaseStatus caseStatus) throws FinderException {
		return ejbFindAllCasesByProviderStatus(providerId, caseStatus.getStatus());
	}

	public Collection ejbFindAllCasesByProviderAndStatus(School provider, String caseStatus) throws FinderException {
		return ejbFindAllCasesByProviderStatus(((Integer)provider.getPrimaryKey()).intValue(), caseStatus);
	}
	
	public Collection ejbFindAllCasesByProviderAndStatus(School provider, CaseStatus caseStatus) throws FinderException {
		return ejbFindAllCasesByProviderStatus(((Integer)provider.getPrimaryKey()).intValue(), caseStatus.getStatus());
	}
	
	public Collection ejbFindAllCasesByProviderAndStatus(int providerId, String caseStatus) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this).append(" c, proc_case p");
		sql.appendWhereEquals("c."+getIDColumnName(), "p.proc_case_id");
		sql.appendAndEquals("c."+PROVIDER_ID,providerId);
		sql.appendAnd().appendEqualsQuoted("p.case_status",caseStatus);
		sql.appendAnd().appendEqualsQuoted("p.case_code",CASE_CODE_KEY);
		sql.appendOrderBy("c."+QUEUE_DATE+",c."+QUEUE_ORDER);

		return idoFindPKsBySQL(sql.toString());
	}	
	
	public Collection ejbFindAllCasesByProviderAndNotInStatus(int providerId, String[] caseStatus) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this).append(" c, proc_case p");
		sql.appendWhereEquals("c."+getIDColumnName(), "p.proc_case_id");
		sql.appendAndEquals("c."+PROVIDER_ID,providerId);
		sql.appendAnd().append("p.case_status").appendNotInArrayWithSingleQuotes(caseStatus);
		sql.appendAnd().appendEqualsQuoted("p.case_code",CASE_CODE_KEY);
		sql.appendOrderBy("c."+QUEUE_DATE+",c."+QUEUE_ORDER);

		return idoFindPKsBySQL(sql.toString());
	}	
	
	//same as above but with a parameter for sorting and also selects from ic_user
	public Collection ejbFindAllCasesByProviderAndNotInStatus(int providerId, String[] caseStatus, String sorting) throws FinderException {
		IDOQuery sql = idoQuery();
		//sql.appendSelect().append("*.c").append(", *.p, iu.first_name, iu.last_name");
		sql.appendSelectAllFrom(this).append(" c, proc_case p, ic_user iu");
		//sql.append(", ic_user iu");
		//sql.appendFrom().append(this).append(" c, proc_case p");
		sql.appendWhereEquals("c."+getIDColumnName(), "p.proc_case_id");
		sql.appendAndEquals("c."+PROVIDER_ID,providerId);
		sql.appendAnd().appendEquals("c.child_id","iu.ic_user_id");
		sql.appendAnd().append("p.case_status").appendNotInArrayWithSingleQuotes(caseStatus);
		sql.appendAnd().appendEqualsQuoted("p.case_code",CASE_CODE_KEY);
		sql.appendOrderBy(sorting+",c."+QUEUE_ORDER);

		return idoFindPKsBySQL(sql.toString());
	}	
	
}