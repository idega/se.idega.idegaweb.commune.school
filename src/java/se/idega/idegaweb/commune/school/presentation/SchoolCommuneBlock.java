package se.idega.idegaweb.commune.school.presentation;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import se.idega.idegaweb.commune.presentation.CommuneBlock;
import se.idega.idegaweb.commune.school.business.SchoolCommuneBusiness;
import se.idega.idegaweb.commune.school.business.SchoolCommuneSession;

import com.idega.block.school.business.SchoolYearComparator;
import com.idega.block.school.data.SchoolClass;
import com.idega.block.school.data.SchoolSeason;
import com.idega.block.school.data.SchoolYear;
import com.idega.business.IBOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.DropdownMenu;
/**
 * @author Laddi
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public abstract class SchoolCommuneBlock extends CommuneBlock {

	private SchoolCommuneBusiness business;
	private SchoolCommuneSession session;
	private int _schoolID = -1;
	private int _schoolSeasonID = -1;
	private int _schoolYearID = -1;
	private int _schoolClassID = -1;
	
	public void main(IWContext iwc) throws Exception{
		setResourceBundle(getResourceBundle(iwc));
		business = getSchoolCommuneBusiness(iwc);
		session = getSchoolCommuneSession(iwc);
		initialize(iwc);

		init(iwc);
	}
	
	public abstract void init(IWContext iwc) throws Exception;
	
	private void initialize(IWContext iwc) throws RemoteException {
		_schoolID = session.getSchoolID();	
		_schoolSeasonID = session.getSchoolSeasonID();
		_schoolYearID = session.getSchoolYearID();
		_schoolClassID = session.getSchoolClassID();
	}
	
	protected DropdownMenu getSchoolSeasons() throws RemoteException {
		DropdownMenu menu = new DropdownMenu(session.getParameterSchoolSeasonID());
		menu.setToSubmit();
		
		Collection seasons = business.getSchoolSeasonBusiness().findAllSchoolSeasons();
		if ( !seasons.isEmpty() ) {
			Iterator iter = seasons.iterator();
			while (iter.hasNext()) {
				SchoolSeason element = (SchoolSeason) iter.next();
				menu.addMenuElement(element.getPrimaryKey().toString(), element.getSchoolSeasonName());
				if (_schoolSeasonID == -1)
					_schoolSeasonID = ((Integer)element.getPrimaryKey()).intValue();
			}
		}
		else {
			menu.addMenuElement(-1, "");	
		}
		
		if ( _schoolSeasonID != -1 )
			menu.setSelectedElement(_schoolSeasonID);
		
		return (DropdownMenu) getStyledInterface(menu);	
	}
		
	protected DropdownMenu getSchoolYears() throws RemoteException {
		DropdownMenu menu = new DropdownMenu(session.getParameterSchoolYearID());
		menu.setToSubmit();
		
		if ( _schoolID != -1 ) {
			List years = new Vector(business.getSchoolBusiness().findAllSchoolYearsInSchool(_schoolID));
			if ( !years.isEmpty() ) {
	      Collections.sort(years,new SchoolYearComparator());
				Iterator iter = years.iterator();
				while (iter.hasNext()) {
					SchoolYear element = (SchoolYear) iter.next();
					menu.addMenuElement(element.getPrimaryKey().toString(), element.getSchoolYearName());
					if ( _schoolYearID == -1 )
						_schoolYearID = ((Integer)element.getPrimaryKey()).intValue();
				}
			}
			else {
				_schoolYearID = -1;
				menu.addMenuElement(-1, "");	
			}
		}
		else {
			menu.addMenuElement(-1, "");	
		}
		
		if ( _schoolYearID != -1 )
			menu.setSelectedElement(_schoolYearID);
		
		return (DropdownMenu) getStyledInterface(menu);
	}
	
	protected DropdownMenu getSchoolClasses() throws RemoteException {
		DropdownMenu menu = new DropdownMenu(session.getParameterSchoolClassID());
		menu.setToSubmit();
		
		if ( _schoolID != -1 && _schoolSeasonID != -1 && _schoolYearID != -1 ) {
			Collection classes = business.getSchoolClassBusiness().findSchoolClassesBySchoolAndSeasonAndYear(_schoolID, _schoolSeasonID, _schoolYearID);
			if ( !classes.isEmpty() ) {
				Iterator iter = classes.iterator();
				while (iter.hasNext()) {
					SchoolClass element = (SchoolClass) iter.next();
					menu.addMenuElement(element.getPrimaryKey().toString(), element.getName());
					if ( _schoolClassID == -1 )
						_schoolClassID = ((Integer)element.getPrimaryKey()).intValue();
				}
			}
			else {
				_schoolClassID = -1;
				menu.addMenuElement(-1, "");	
			}
		}
		else {
			menu.addMenuElement(-1, "");	
		}
		
		if ( _schoolClassID != -1 )
			menu.setSelectedElement(_schoolClassID);
		
		return (DropdownMenu) getStyledInterface(menu);	
	}
		
	private SchoolCommuneBusiness getSchoolCommuneBusiness(IWContext iwc) throws RemoteException {
		return (SchoolCommuneBusiness) IBOLookup.getServiceInstance(iwc, SchoolCommuneBusiness.class);	
	}
	
	private SchoolCommuneSession getSchoolCommuneSession(IWContext iwc) throws RemoteException {
		return (SchoolCommuneSession) IBOLookup.getSessionInstance(iwc, SchoolCommuneSession.class);	
	}
	
	/**
	 * Returns the schoolClassID.
	 * @return int
	 */
	public int getSchoolClassID() {
		return _schoolClassID;
	}

	/**
	 * Returns the schoolID.
	 * @return int
	 */
	public int getSchoolID() {
		return _schoolID;
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
	 * Returns the business.
	 * @return SchoolCommuneBusiness
	 */
	public SchoolCommuneBusiness getBusiness() {
		return business;
	}

	/**
	 * Returns the session.
	 * @return SchoolCommuneSession
	 */
	public SchoolCommuneSession getSession() {
		return session;
	}

	/**
	 * Sets the schoolClassID.
	 * @param schoolClassID The schoolClassID to set
	 */
	public void setSchoolClassID(int schoolClassID) {
		_schoolClassID = schoolClassID;
	}
}