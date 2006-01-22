/*
 * $Id: CommuneSchoolBusinessHome.java,v 1.7 2006/01/22 16:25:11 laddi Exp $
 * Created on Jan 22, 2006
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
 *  Last modified: $Date: 2006/01/22 16:25:11 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.7 $
 */
public interface CommuneSchoolBusinessHome extends IBOHome {

	public CommuneSchoolBusiness create() throws javax.ejb.CreateException, java.rmi.RemoteException;
}
