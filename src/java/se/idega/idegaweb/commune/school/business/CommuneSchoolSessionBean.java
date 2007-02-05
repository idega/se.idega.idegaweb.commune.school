/*
 * $Id: CommuneSchoolSessionBean.java,v 1.3 2007/02/05 22:25:55 laddi Exp $
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
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import se.idega.idegaweb.commune.business.CommuneUserBusiness;
import com.idega.business.IBORuntimeException;
import com.idega.business.IBOSessionBean;
import com.idega.user.data.User;


/**
 * Last modified: $Date: 2007/02/05 22:25:55 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.3 $
 */
public class CommuneSchoolSessionBean extends IBOSessionBean  implements CommuneSchoolSession, HttpSessionBindingListener {
	
	public void valueBound(HttpSessionBindingEvent arg0) {
	}

	public void valueUnbound(HttpSessionBindingEvent arg0) {
		iUserUniqueID = null;
		iUserPK = null;
		iUser = null;
	}

	private String iUserUniqueID;
	private Object iUserPK;
	private User iUser;
	
	public User getUser() {
		try {
			if (this.iUser == null && this.iUserPK != null) {
				this.iUser = getUserBusiness().getUser(new Integer(this.iUserPK.toString()));
			}
			else if (this.iUser == null && this.iUserUniqueID != null) {
				try {
					this.iUser = getUserBusiness().getUserByUniqueId(this.iUserUniqueID);
				}
				catch (FinderException fe) {
					fe.printStackTrace();
					this.iUser = null;
				}
			}
		}
		catch (RemoteException re) {
			this.iUser = null;
		}
		return this.iUser;
	}
	
	public void setUser(Object userPK) {
		this.iUserPK = userPK;
		this.iUserUniqueID = null;
		this.iUser = null;
	}
	
	public void setUserUniqueID(String uniqueID) {
		this.iUserUniqueID = uniqueID;
		this.iUserPK = null;
		this.iUser = null;
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