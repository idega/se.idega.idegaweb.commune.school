/*
 * $Id: ProcapitaSchoolBMPBean.java,v 1.1 2004/03/01 08:36:13 anders Exp $
 *
 * Copyright (C) 2003 Agura IT. All Rights Reserved.
 *
 * This software is the proprietary information of Agura IT AB.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.school.data;

import com.idega.block.school.data.School;
import com.idega.data.GenericEntity;
import com.idega.data.IDOQuery;
import java.util.Collection;
import javax.ejb.FinderException;

/**
 * Entity bean mapping school names in the Procapita system to schools in the eGov system.
 * <p>
 * Last modified: $Date: 2004/03/01 08:36:13 $ by $Author: anders $
 *
 * @author Anders Lindman
 * @version $Revision: 1.1 $
 */
public class ProcapitaSchoolBMPBean extends GenericEntity implements ProcapitaSchool {

	private static final String ENTITY_NAME = "comm_procapita_school";

	private static final String COLUMN_SCHOOL_ID = "sch_school_id";
	private static final String COLUMN_SCHOOL_NAME = "school_name";
		
	/**
	 * @see com.idega.data.GenericEntity#getEntityName()
	 */
	public String getEntityName() {
		return ENTITY_NAME;
	}
	
	/**
	 * @see com.idega.data.GenericEntity#getIdColumnName()
	 */
	public String getIDColumnName() {
		return COLUMN_SCHOOL_ID;
	}

	/**
	 * @see com.idega.data.GenericEntity#initializeAttributes()
	 */
	public void initializeAttributes() {
		addOneToOneRelationship(getIDColumnName(), School.class);
		setAsPrimaryKey (getIDColumnName(), true);
		addAttribute(COLUMN_SCHOOL_NAME, "Procapita school name", true, true, String.class);
	}
	
	public School getSchool() {
		return (School) getColumnValue(COLUMN_SCHOOL_ID);	
	}

	public int getSchoolId() {
		return getIntColumnValue(COLUMN_SCHOOL_ID);	
	}
	
	public String getSchoolName() {
		return getStringColumnValue(COLUMN_SCHOOL_NAME);	
	}

	public void setSchoolId(int id) { 
		setColumn(COLUMN_SCHOOL_ID, id); 
	}

	public void setSchoolName(String name) { 
		setColumn(COLUMN_SCHOOL_NAME, name); 
	}

	public Collection ejbFindAll() throws FinderException {
		final IDOQuery query = idoQuery();
		query.appendSelectAllFrom(getTableName());
		return idoFindPKsByQuery(query);		
	}
}
