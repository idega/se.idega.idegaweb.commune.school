/*
 * $Id: SchoolCommuneBusinessHome.java 1.1 Feb 10, 2006 laddi Exp $
 * Created on Feb 10, 2006
 *
 * Copyright (C) 2006 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.school.business;

import com.idega.business.IBOHome;


/**
 * <p>
 * TODO laddi Describe Type SchoolCommuneBusinessHome
 * </p>
 *  Last modified: $Date: 2004/06/28 09:09:50 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.1 $
 */
public interface SchoolCommuneBusinessHome extends IBOHome {

	public SchoolCommuneBusiness create() throws javax.ejb.CreateException, java.rmi.RemoteException;
}
