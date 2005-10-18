/*
 * $Id: CommuneSchoolBusinessHome.java,v 1.5 2005/10/18 20:14:24 laddi Exp $
 * Created on Oct 18, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.school.business;

import com.idega.business.IBOHome;


/**
 * Last modified: $Date: 2005/10/18 20:14:24 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.5 $
 */
public interface CommuneSchoolBusinessHome extends IBOHome {

	public CommuneSchoolBusiness create() throws javax.ejb.CreateException, java.rmi.RemoteException;
}
