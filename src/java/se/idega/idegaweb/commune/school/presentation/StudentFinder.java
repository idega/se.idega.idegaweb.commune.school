/*
 * Created on 19.10.2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package se.idega.idegaweb.commune.school.presentation;

import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.EJBException;

import se.idega.idegaweb.commune.presentation.CommuneUserFinder;
import se.idega.idegaweb.commune.school.business.SchoolCommuneBusiness;
import se.idega.idegaweb.commune.school.business.SchoolCommuneSession;
import se.idega.idegaweb.commune.school.event.SchoolEventListener;

import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.presentation.IWContext;
import com.idega.user.data.User;

/**
 * @author laddi
 */
public class StudentFinder extends CommuneUserFinder {

	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.presentation.CommuneUserFinder#addUser(com.idega.presentation.IWContext, com.idega.user.data.User)
	 */
	public boolean addUser(IWContext iwc, User user) {
		try {
			boolean tempBoolean;
			tempBoolean = getSchoolBusiness(iwc).isPlacedAtSchool(((Integer)user.getPrimaryKey()).intValue(), getSchoolSession(iwc).getSchoolID());
			if (!tempBoolean) {
				tempBoolean = (getSchoolBusiness(iwc).getSchoolChoiceBusiness().getNumberOfApplicationsByUserAndSchool(Integer.parseInt(user.getPrimaryKey().toString()), getSchoolSession(iwc).getSchoolID()) > 0);
			}
			return tempBoolean;
			//this.getSchoolBusiness(iwc).getSchoolChoiceBusiness().f
		}
		catch (RemoteException e) {
			return false;
		}
		catch (EJBException e) {
			return false;
		}
	}
	
	protected Collection getUsers(IWContext iwc, String searchString) throws RemoteException {
		return getUserBusiness(iwc).findSchoolChildrenBySearchCondition(searchString);
	}
	
	protected Collection getUser(IWContext iwc, String firstName, String middleName, String lastName, String pid) throws RemoteException {
		return getUserBusiness(iwc).findSchoolChildrenByConditions(firstName, middleName, lastName, pid);
	}


	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.presentation.CommuneUserFinder#getParameterName(com.idega.presentation.IWContext)
	 */
	public String getParameterName(IWContext iwc) {
		try {
			return getSchoolSession(iwc).getParameterStudentID();
		}
		catch (RemoteException e) {
			throw new IBORuntimeException(e.getMessage());
		}
	}
	
	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.presentation.CommuneUserFinder#getParameterName(com.idega.presentation.IWContext)
	 */
	public String getParameterUniqueName(IWContext iwc) {
		try {
			return getSchoolSession(iwc).getParameterStudentID();
		}
		catch (RemoteException e) {
			throw new IBORuntimeException(e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.presentation.CommuneUserFinder#getEventListener()
	 */
	public Class getEventListener() {
		return SchoolEventListener.class;
	}

	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.presentation.CommuneUserFinder#getSubmitDisplay()
	 */
	public String getSubmitDisplay() {
		return localize("school.show_placings","Show placings");
	}

	/**
	 * @see se.idega.idegaweb.commune.presentation.CommuneUserFinder#getSearchSubmitDisplay()
	 */
	public String getSearchSubmitDisplay() {
		return localize("school.find_student","Find student");
	}

	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.presentation.CommuneUserFinder#getNoUserFoundString()
	 */
	public String getNoUserFoundString() {
		return localize("school.no_student_found","No student found");
	}

	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.presentation.CommuneUserFinder#getFoundUsersString()
	 */
	public String getFoundUsersString() {
		return localize("school.found_students","Found students");
	}

	private SchoolCommuneBusiness getSchoolBusiness(IWContext iwc) {
		try {
			return (SchoolCommuneBusiness) IBOLookup.getServiceInstance(iwc, SchoolCommuneBusiness.class);
		}
		catch (RemoteException e) {
			throw new IBORuntimeException(e.getMessage());
		}	
	}	
	private SchoolCommuneSession getSchoolSession(IWContext iwc) {
		try {
			return (SchoolCommuneSession) IBOLookup.getSessionInstance(iwc, SchoolCommuneSession.class);
		}
		catch (IBOLookupException e) {
			throw new IBORuntimeException(e.getMessage());
		}	
	}	
}