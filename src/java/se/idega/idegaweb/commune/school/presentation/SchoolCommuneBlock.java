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

import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.business.SchoolTypeBusiness;
import com.idega.block.school.business.SchoolYearComparator;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolClass;
import com.idega.block.school.data.SchoolSeason;
import com.idega.block.school.data.SchoolYear;
import com.idega.business.IBOLookup;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
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
	protected SchoolCommuneSession session;
	private SchoolBusiness sBusiness;
	private SchoolTypeBusiness stBusiness;
	private int _schoolID = -1;
	private int _schoolSeasonID = -1;
	private int _schoolYearID = -1;
	private int _schoolClassID = -1;
	
	public void main(IWContext iwc) throws Exception{
		setResourceBundle(getResourceBundle(iwc));
		business = getSchoolCommuneBusiness(iwc);
		session = getSchoolCommuneSession(iwc);
		sBusiness = getSchoolBusiness(iwc);
		stBusiness = getSchoolTypeBusiness(iwc);
		initialize(iwc);

		init(iwc);
	}
	
	public abstract void init(IWContext iwc) throws Exception;
	
	private void initialize(IWContext iwc) throws RemoteException {
		_schoolID = session.getSchoolID();	
		_schoolSeasonID = session.getSchoolSeasonID();
		_schoolYearID = session.getSchoolYearID();
		_schoolClassID = session.getSchoolClassID();

		if ( _schoolSeasonID == -1 ) {
			_schoolSeasonID = getSchoolCommuneBusiness(iwc).getCurrentSchoolSeasonID();
			session.setSchoolSeasonID(_schoolSeasonID);
		}
	}
	
	protected Table getNavigationTable(boolean showClass) throws RemoteException {
		return getNavigationTable(showClass, false);
	}
	
	protected Table getNavigationTable(boolean showClass, boolean multipleSchools) throws RemoteException {
		Table table = new Table(8,1);
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(3,"8");
		int row = 1;

		if (multipleSchools) {
			table.resize(8, 3);
			table.add(getSmallHeader(localize("school.school","School")+":"+Text.NON_BREAKING_SPACE),1,row);
			table.mergeCells(2, row, 8, row);
			table.add(getSchools(),2,row);
			++row;
			table.setHeight(row, "2");
			++row;
		}

		table.add(getSmallHeader(localize("school.season","Season")+":"+Text.NON_BREAKING_SPACE),1,row);
		DropdownMenu schSeas = getSchoolSeasons();
		table.add(schSeas,2,row);
		table.add(getSmallHeader(localize("school.year","Year")+":"+Text.NON_BREAKING_SPACE),4,row);
		DropdownMenu schYears = getSchoolYears();
			schYears.addMenuElementFirst("-1","");
		table.add(schYears,5,row);
		if (showClass) {
//			table.resize(8, row);
			table.setWidth(6, "8");
			table.add(getSmallHeader(localize("school.class","Class")+":"+Text.NON_BREAKING_SPACE),7,row);
			DropdownMenu schClasses = getSchoolClasses();
			schClasses.addMenuElementFirst("-1","");
			table.add(schClasses, 8, row);
		}
		
		return table;
	}
	
	protected DropdownMenu getSchools() throws RemoteException {
		
		DropdownMenu menu = new DropdownMenu(session.getParameterSchoolID());
		menu.setToSubmit();
		Collection schoolTypeIds = stBusiness.findAllSchoolTypesForSchool();
		Collection schools = business.getSchoolBusiness().findAllSchoolsByType(schoolTypeIds);
		Iterator iter = schools.iterator();
		while (iter.hasNext()) {
			School sCool = (School) iter.next();	
			menu.addMenuElement(sCool.getPrimaryKey().toString(), sCool.getName());
		}	
		menu.addMenuElementFirst("-1", localize("school.all_schools", "All schools"));
		
		if (getSchoolID() != -1) {
			menu.setSelectedElement(getSchoolID());	
		}
		
		return (DropdownMenu) getStyledInterface(menu);	
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
				// Gimmi 13.11.2002
//				if (_schoolSeasonID == -1)
//					_schoolSeasonID = ((Integer)element.getPrimaryKey()).intValue();
			}
		}
		else {
//			menu.addMenuElement(-1, "");	
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
					if ( _schoolYearID == -1 ) {
						// Gimmi 13.11.2002
						//_schoolYearID = ((Integer)element.getPrimaryKey()).intValue();
					}
				}
			}
			else {
				_schoolYearID = -1;
//				menu.addMenuElement(-1, "");	
			}
		}
		else {
//			menu.addMenuElement(-1, "");	
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
					//if ( _schoolClassID == -1 )
						// Gimmi 13.11.2002
						//_schoolClassID = ((Integer)element.getPrimaryKey()).intValue();
				}
			}
			else {
				_schoolClassID = -1;
//				menu.addMenuElement(-1, "");	
			}
		}
		else {
//			menu.addMenuElement(-1, "");	
		}
		
		if ( _schoolClassID != -1 )
			menu.setSelectedElement(_schoolClassID);
		
		return (DropdownMenu) getStyledInterface(menu);	
	}
		
	private SchoolCommuneBusiness getSchoolCommuneBusiness(IWContext iwc) throws RemoteException {
		return (SchoolCommuneBusiness) IBOLookup.getServiceInstance(iwc, SchoolCommuneBusiness.class);	
	}
	
	protected SchoolCommuneSession getSchoolCommuneSession(IWContext iwc) throws RemoteException {
		return (SchoolCommuneSession) IBOLookup.getSessionInstance(iwc, SchoolCommuneSession.class);	
	}
	
	protected SchoolBusiness getSchoolBusiness(IWApplicationContext iwac) throws RemoteException {
		return (SchoolBusiness) IBOLookup.getServiceInstance(iwac, SchoolBusiness.class);
	}
	
	protected SchoolTypeBusiness getSchoolTypeBusiness(IWApplicationContext iwac) throws RemoteException {
		return (SchoolTypeBusiness) IBOLookup.getServiceInstance(iwac, SchoolTypeBusiness.class);
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