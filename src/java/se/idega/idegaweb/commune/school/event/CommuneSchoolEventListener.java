/*
 * $Id: CommuneSchoolEventListener.java,v 1.1 2005/08/09 16:36:28 laddi Exp $
 * Created on Aug 3, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.school.event;

import java.rmi.RemoteException;
import se.idega.idegaweb.commune.presentation.CitizenChildren;
import se.idega.idegaweb.commune.school.business.CommuneSchoolSession;
import com.idega.business.IBOLookup;
import com.idega.business.IBORuntimeException;
import com.idega.event.IWPageEventListener;
import com.idega.presentation.IWContext;


/**
 * Last modified: $Date: 2005/08/09 16:36:28 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.1 $
 */
public class CommuneSchoolEventListener implements IWPageEventListener {

	public boolean actionPerformed(IWContext iwc) {
		try {
			CommuneSchoolSession session = getCommuneSchoolSession(iwc);
			
			if (iwc.isParameterSet(CitizenChildren.prmChildId)) {
				session.setUser(iwc.getParameter(CitizenChildren.prmChildId));
			}
			else if (iwc.isParameterSet(CitizenChildren.prmChildUniqueId)) {
				session.setUserUniqueID(iwc.getParameter(CitizenChildren.prmChildUniqueId));
			}
			
			return true;
		}
		catch (RemoteException re) {
			return false;
		}
	}

	private CommuneSchoolSession getCommuneSchoolSession(IWContext iwc) {
		try {
			return (CommuneSchoolSession) IBOLookup.getSessionInstance(iwc, CommuneSchoolSession.class);	
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}
}