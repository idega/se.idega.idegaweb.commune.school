package se.idega.idegaweb.commune.school.business;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import se.idega.idegaweb.commune.business.CommuneUserBusiness;

import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolHome;
import com.idega.business.IBOLookup;
import com.idega.business.IBOSessionBean;
import com.idega.user.data.Group;
import com.idega.user.data.User;

/**
 * @author Laddi
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class SchoolCommuneSessionBean extends IBOSessionBean implements SchoolCommuneSession {

	protected int _schoolID = -1;
	protected int _schoolYearID = -1;
	protected int _schoolSeasonID = -1;
	protected int _schoolClassID = -1;

	public CommuneUserBusiness getCommuneUserBusiness() throws RemoteException {
		return (CommuneUserBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), CommuneUserBusiness.class);
	}
	
	/**
	 * Returns the schoolID.
	 * @return int
	 */
	public int getSchoolID() throws RemoteException {
		if (_schoolID != -1) {
			return _schoolID;
		}
		else {
			User user = getUserContext().getCurrentUser();
			if (user != null) {
				try {
					School school = getCommuneUserBusiness().getFirstManagingSchoolForUser(user);
					if (school != null) {
						_schoolID = ((Integer) school.getPrimaryKey()).intValue();
					}
				}
				catch (FinderException fe) {
					_schoolID = -1;
				}
			}
			return _schoolID;
		}
	}

	/**
	 * Returns the schoolClassID.
	 * @return int
	 */
	public int getSchoolClassID() {
		return _schoolClassID;
	}

	/**
	 * Returns the schoolSeasonID.
	 * @return int
	 */
	public int getSchoolSeasonID() {
		return _schoolSeasonID;
	}

	/**
	 * Returns the schoolYearID.
	 * @return int
	 */
	public int getSchoolYearID() {
		return _schoolYearID;
	}

	/**
	 * Sets the schoolClassID.
	 * @param schoolClassID The schoolClassID to set
	 */
	public void setSchoolClassID(int schoolClassID) {
		_schoolClassID = schoolClassID;
	}

	/**
	 * Sets the schoolID.
	 * @param schoolID The schoolID to set
	 */
	public void setSchoolID(int schoolID) {
		_schoolID = schoolID;
	}

	/**
	 * Sets the schoolSeasonID.
	 * @param schoolSeasonID The schoolSeasonID to set
	 */
	public void setSchoolSeasonID(int schoolSeasonID) {
		_schoolSeasonID = schoolSeasonID;
	}

	/**
	 * Sets the schoolYearID.
	 * @param schoolYearID The schoolYearID to set
	 */
	public void setSchoolYearID(int schoolYearID) {
		_schoolYearID = schoolYearID;
	}

}
