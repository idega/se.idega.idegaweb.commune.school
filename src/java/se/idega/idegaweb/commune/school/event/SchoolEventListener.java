package se.idega.idegaweb.commune.school.event;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.FinderException;

import se.idega.idegaweb.commune.school.business.SchoolCommuneBusiness;
import se.idega.idegaweb.commune.school.business.SchoolCommuneSession;

import com.idega.block.school.data.SchoolClass;
import com.idega.business.IBOLookup;
import com.idega.business.IWEventListener;
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
public class SchoolEventListener implements IWEventListener {

	protected static final String PARAMETER_SCHOOL_ID = "sch_s_id";
	protected static final String PARAMETER_SCHOOL_YEAR_ID = "sch_s_y_id";
	protected static final String PARAMETER_SCHOOL_SEASON_ID = "sch_s_s_id";
	protected static final String PARAMETER_SCHOOL_CLASS_ID = "sch_s_c_id";

	private int _schoolID = -1;
	private int _schoolSeasonID = -1;
	private int _schoolYearID = -1;
	private int _schoolClassID = -1;
	/**
	 * @see com.idega.business.IWEventListener#actionPerformed(IWContext)
	 */
	public boolean actionPerformed(IWContext iwc) throws IWException {
		try {
			SchoolCommuneSession session = getSchoolCommuneSession(iwc);
			_schoolID = session.getSchoolID();	
	
			if (iwc.isParameterSet(PARAMETER_SCHOOL_ID))
				_schoolID = Integer.parseInt(iwc.getParameter(PARAMETER_SCHOOL_ID));
			
			if (iwc.isParameterSet(PARAMETER_SCHOOL_SEASON_ID))
				_schoolSeasonID = Integer.parseInt(iwc.getParameter(PARAMETER_SCHOOL_SEASON_ID));
			
			if (iwc.isParameterSet(PARAMETER_SCHOOL_YEAR_ID))
				_schoolYearID = Integer.parseInt(iwc.getParameter(PARAMETER_SCHOOL_YEAR_ID));
			
			if (iwc.isParameterSet(PARAMETER_SCHOOL_CLASS_ID))
				_schoolClassID = Integer.parseInt(iwc.getParameter(PARAMETER_SCHOOL_CLASS_ID));
			
			if ( _schoolSeasonID == -1 )
				setSchoolSeasonID(iwc);
			
			if ( _schoolClassID != -1 && _schoolYearID != -1 )
				validateSchoolClass(iwc);
				
			session.setSchoolClassID(_schoolClassID);
			session.setSchoolID(_schoolID);
			session.setSchoolSeasonID(_schoolSeasonID);
			session.setSchoolYearID(_schoolYearID);
			return true;
		}
		catch (RemoteException re) {
			return false;
		}
	}

	private void setSchoolSeasonID(IWContext iwc) throws RemoteException {
		try {
			_schoolSeasonID = ((Integer)getSchoolCommuneBusiness(iwc).getSchoolChoiceBusiness().getCurrentSeason().getPrimaryKey()).intValue();	
		}
		catch (FinderException fe) {
			_schoolSeasonID = -1;	
		}
	}
	
	private void validateSchoolClass(IWContext iwc) throws RemoteException {
		try {
			SchoolClass schoolClass = getSchoolCommuneBusiness(iwc).getSchoolClassBusiness().findSchoolClass(new Integer(_schoolClassID));
			if ( schoolClass.getSchoolYearId() != _schoolYearID ) {
				Collection schoolClasses = getSchoolCommuneBusiness(iwc).getSchoolClassBusiness().findSchoolClassesBySchoolAndSeasonAndYear(_schoolID, _schoolSeasonID, _schoolYearID);
				if ( !schoolClasses.isEmpty() ) {
					Iterator iter = schoolClasses.iterator();
					while (iter.hasNext()) {
						_schoolClassID = ((Integer)((SchoolClass) iter.next()).getPrimaryKey()).intValue();
						continue;
					}
				}
			}
		}
		catch (FinderException fe) {
			_schoolClassID = -1;
		}
	}
	
	private SchoolCommuneSession getSchoolCommuneSession(IWContext iwc) throws RemoteException {
		return (SchoolCommuneSession) IBOLookup.getSessionInstance(iwc, SchoolCommuneSession.class);	
	}
	
	private SchoolCommuneBusiness getSchoolCommuneBusiness(IWContext iwc) throws RemoteException {
		return (SchoolCommuneBusiness) IBOLookup.getServiceInstance(iwc, SchoolCommuneBusiness.class);	
	}
	
}
