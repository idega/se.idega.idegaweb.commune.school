/*
 * $Id: SchoolCommuneBusinessHome.java 1.1 Oct 21, 2004 thomas Exp $
 * Created on Oct 21, 2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.school.business;

import com.idega.business.IBOHome;


/**
 * 
 *  Last modified: $Date: 2004/06/28 09:09:50 $ by $Author: thomas $
 * 
 * @author <a href="mailto:thomas@idega.com">thomas</a>
 * @version $Revision: 1.1 $
 */
public interface SchoolCommuneBusinessHome extends IBOHome {

	public SchoolCommuneBusiness create() throws javax.ejb.CreateException, java.rmi.RemoteException;
}
