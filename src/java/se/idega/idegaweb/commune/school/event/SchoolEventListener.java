package se.idega.idegaweb.commune.school.event;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import se.idega.idegaweb.commune.school.business.SchoolCommuneBusiness;
import se.idega.idegaweb.commune.school.business.SchoolCommuneSession;

import com.idega.block.school.data.SchoolClass;
import com.idega.block.school.data.SchoolYear;
import com.idega.business.IBOLookup;
import com.idega.event.IWPageEventListener;
import com.idega.presentation.IWContext;

/**
 * @author Laddi
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class SchoolEventListener implements IWPageEventListener {

	private int _schoolID = -1;
	private int _schoolSeasonID = -1;
	private int _schoolYearID = -1;
	private int _schoolClassID = -1;
	private int _studentID = -1;
	
	/**
	 * @see com.idega.business.IWEventListener#actionPerformed(IWContext)
	 */
	public boolean actionPerformed(IWContext iwc) {
		try {
			SchoolCommuneSession session = getSchoolCommuneSession(iwc);
			this._schoolID = session.getSchoolID();	
	
			if (iwc.isParameterSet(session.getParameterSchoolID())) {
				this._schoolID = Integer.parseInt(iwc.getParameter(session.getParameterSchoolID()));
			}
			
			if (iwc.isParameterSet(session.getParameterSchoolSeasonID())) {
				this._schoolSeasonID = Integer.parseInt(iwc.getParameter(session.getParameterSchoolSeasonID()));
			}
			
			if (iwc.isParameterSet(session.getParameterSchoolYearID())) {
				this._schoolYearID = Integer.parseInt(iwc.getParameter(session.getParameterSchoolYearID()));
			}
			
			if (iwc.isParameterSet(session.getParameterSchoolClassID())) {
				this._schoolClassID = Integer.parseInt(iwc.getParameter(session.getParameterSchoolClassID()));
			}
			
			if (iwc.isParameterSet(session.getParameterStudentID())) {
				this._studentID = Integer.parseInt(iwc.getParameter(session.getParameterStudentID()));
			}
			
			if (iwc.isParameterSet(session.getParameterSchoolGroupIDs())) {
				session.setSchoolGroupIDs(iwc.getParameterValues(session.getParameterSchoolGroupIDs()));
			}
			
			if ( this._schoolClassID != -1 && this._schoolYearID != -1 ) {
				validateSchoolClass(iwc);
			}
				
			session.setSchoolClassID(this._schoolClassID);
			session.setSchoolID(this._schoolID);
			session.setSchoolSeasonID(this._schoolSeasonID);
			session.setSchoolYearID(this._schoolYearID);
			session.setStudentID(this._studentID);
			return true;
		}
		catch (RemoteException re) {
			return false;
		}
	}

	private void validateSchoolClass(IWContext iwc) throws RemoteException {
		SchoolClass schoolClass = getSchoolCommuneBusiness(iwc).getSchoolBusiness().findSchoolClass(new Integer(this._schoolClassID));
		SchoolYear schoolYear = getSchoolCommuneBusiness(iwc).getSchoolBusiness().getSchoolYear(new Integer(this._schoolYearID));
		if (!schoolClass.hasRelationToSchoolYear(schoolYear) ) {
			Collection schoolClasses = getSchoolCommuneBusiness(iwc).getSchoolBusiness().findSchoolClassesBySchoolAndSeasonAndYear(this._schoolID, this._schoolSeasonID, this._schoolYearID);
			if ( !schoolClasses.isEmpty() ) {
				Iterator iter = schoolClasses.iterator();
				while (iter.hasNext()) {
					this._schoolClassID = ((Integer)((SchoolClass) iter.next()).getPrimaryKey()).intValue();
					continue;
				}
			}
		}
	}
	
	private SchoolCommuneSession getSchoolCommuneSession(IWContext iwc) throws RemoteException {
		return (SchoolCommuneSession) IBOLookup.getSessionInstance(iwc, SchoolCommuneSession.class);	
	}
	
	private SchoolCommuneBusiness getSchoolCommuneBusiness(IWContext iwc) throws RemoteException {
		return (SchoolCommuneBusiness) IBOLookup.getServiceInstance(iwc, SchoolCommuneBusiness.class);	
	}
	
}
