/*
 * $Id: CommuneSchoolBusinessHome.java,v 1.8 2006/01/30 15:57:24 laddi Exp $
 * Created on Jan 30, 2006
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
 * TODO laddi Describe Type CommuneSchoolBusinessHome
 * </p>
 *  Last modified: $Date: 2006/01/30 15:57:24 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.8 $
 */
public interface CommuneSchoolBusinessHome extends IBOHome {

	public CommuneSchoolBusiness create() throws javax.ejb.CreateException, java.rmi.RemoteException;
}
