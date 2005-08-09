/*
 * $Id: CommuneSchoolSessionBean.java,v 1.1 2005/08/09 16:36:28 laddi Exp $
 * Created on Aug 3, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.school.business;

import java.rmi.RemoteException;
import javax.ejb.FinderException;
import se.idega.idegaweb.commune.business.CommuneUserBusiness;
import com.idega.business.IBORuntimeException;
import com.idega.business.IBOSessionBean;
import com.idega.user.data.User;


/**
 * Last modified: $Date: 2005/08/09 16:36:28 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.1 $
 */
public class CommuneSchoolSessionBean extends IBOSessionBean  implements CommuneSchoolSession{
	
	private String iUserUniqueID;
	private Object iUserPK;
	private User iUser;
	
	public User getUser() {
		try {
			if (iUser == null && iUserPK != null) {
				iUser = getUserBusiness().getUser(new Integer(iUserPK.toString()));
			}
			else if (iUser == null && iUserUniqueID != null) {
				try {
					iUser = getUserBusiness().getUserByUniqueId(iUserUniqueID);
				}
				catch (FinderException fe) {
					fe.printStackTrace();
					iUser = null;
				}
			}
		}
		catch (RemoteException re) {
			iUser = null;
		}
		return iUser;
	}
	
	public void setUser(Object userPK) {
		iUserPK = userPK;
		iUserUniqueID = null;
		iUser = null;
	}
	
	public void setUserUniqueID(String uniqueID) {
		iUserUniqueID = uniqueID;
		iUserPK = null;
		iUser = null;
	}

	private CommuneUserBusiness getUserBusiness() {
		try {
			return (CommuneUserBusiness) this.getServiceInstance(CommuneUserBusiness.class);
		}
		catch (RemoteException e) {
			throw new IBORuntimeException(e.getMessage());
		}
	}
}