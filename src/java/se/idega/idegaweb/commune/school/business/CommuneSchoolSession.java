/*
 * $Id: CommuneSchoolSession.java,v 1.1 2005/08/09 16:36:28 laddi Exp $
 * Created on Aug 3, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.school.business;

import com.idega.business.IBOSession;
import com.idega.user.data.User;


/**
 * Last modified: $Date: 2005/08/09 16:36:28 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.1 $
 */
public interface CommuneSchoolSession extends IBOSession {

	/**
	 * @see se.idega.idegaweb.commune.school.business.CommuneSchoolSessionBean#getUser
	 */
	public User getUser() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.CommuneSchoolSessionBean#setUser
	 */
	public void setUser(Object userPK) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.CommuneSchoolSessionBean#setUserUniqueID
	 */
	public void setUserUniqueID(String uniqueID) throws java.rmi.RemoteException;
}
