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
import com.idega.idegaweb.IWException;
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
	public boolean actionPerformed(IWContext iwc) throws IWException {
		try {
			SchoolCommuneSession session = getSchoolCommuneSession(iwc);
			_schoolID = session.getSchoolID();	
	
			if (iwc.isParameterSet(session.getParameterSchoolID()))
				_schoolID = Integer.parseInt(iwc.getParameter(session.getParameterSchoolID()));
			
			if (iwc.isParameterSet(session.getParameterSchoolSeasonID()))
				_schoolSeasonID = Integer.parseInt(iwc.getParameter(session.getParameterSchoolSeasonID()));
			
			if (iwc.isParameterSet(session.getParameterSchoolYearID()))
				_schoolYearID = Integer.parseInt(iwc.getParameter(session.getParameterSchoolYearID()));
			
			if (iwc.isParameterSet(session.getParameterSchoolClassID()))
				_schoolClassID = Integer.parseInt(iwc.getParameter(session.getParameterSchoolClassID()));
			
			if (iwc.isParameterSet(session.getParameterStudentID()))
				_studentID = Integer.parseInt(iwc.getParameter(session.getParameterStudentID()));
			
			if ( _schoolClassID != -1 && _schoolYearID != -1 )
				validateSchoolClass(iwc);
				
			session.setSchoolClassID(_schoolClassID);
			session.setSchoolID(_schoolID);
			session.setSchoolSeasonID(_schoolSeasonID);
			session.setSchoolYearID(_schoolYearID);
			session.setStudentID(_studentID);
			return true;
		}
		catch (RemoteException re) {
			return false;
		}
	}

	private void validateSchoolClass(IWContext iwc) throws RemoteException {
		SchoolClass schoolClass = getSchoolCommuneBusiness(iwc).getSchoolBusiness().findSchoolClass(new Integer(_schoolClassID));
		SchoolYear schoolYear = getSchoolCommuneBusiness(iwc).getSchoolBusiness().getSchoolYear(new Integer(_schoolYearID));
		if (!schoolClass.hasRelationToSchoolYear(schoolYear) ) {
			Collection schoolClasses = getSchoolCommuneBusiness(iwc).getSchoolBusiness().findSchoolClassesBySchoolAndSeasonAndYear(_schoolID, _schoolSeasonID, _schoolYearID);
			if ( !schoolClasses.isEmpty() ) {
				Iterator iter = schoolClasses.iterator();
				while (iter.hasNext()) {
					_schoolClassID = ((Integer)((SchoolClass) iter.next()).getPrimaryKey()).intValue();
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
