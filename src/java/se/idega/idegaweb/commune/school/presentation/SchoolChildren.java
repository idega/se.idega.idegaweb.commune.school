/*
 * Created on 30.11.2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package se.idega.idegaweb.commune.school.presentation;

import java.rmi.RemoteException;
import se.idega.idegaweb.commune.presentation.CitizenChildren;
import se.idega.idegaweb.commune.school.event.CommuneSchoolEventListener;
import com.idega.block.school.business.SchoolBusiness;
import com.idega.business.IBOLookup;
import com.idega.business.IBORuntimeException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.presentation.IWContext;
import com.idega.user.data.User;

/**
 * @author laddi
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class SchoolChildren extends CitizenChildren {

	private boolean _showWithPlacement = false;
	
	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.presentation.CitizenChildren#getShowChild(com.idega.user.data.User)
	 */
	protected boolean getShowChild(IWContext iwc, User child) {
		try {
			boolean hasPlacements = getSchoolBusiness(iwc).hasSchoolPlacements(((Integer)child.getPrimaryKey()).intValue());
			if (hasPlacements) {
				return _showWithPlacement;
			}
			else
				return !_showWithPlacement;
		}
		catch (RemoteException re) {
			return false;
		}
	}
	
	/**
	 * @return Returns the showWithPlacement.
	 */
	public boolean isShowWithPlacement() {
		return this._showWithPlacement;
	}
	
	/**
	 * @param showWithPlacement The showWithPlacement to set.
	 */
	public void setShowWithPlacement(boolean showWithPlacement) {
		this._showWithPlacement = showWithPlacement;
	}
	
	protected SchoolBusiness getSchoolBusiness(IWApplicationContext iwac) {
		try {
			return (SchoolBusiness) IBOLookup.getServiceInstance(iwac, SchoolBusiness.class);
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}
	
	protected Class getEventListener() {
		return CommuneSchoolEventListener.class;
	}
}
