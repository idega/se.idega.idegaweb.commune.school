package se.idega.idegaweb.commune.school.presentation;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.ejb.FinderException;

import se.idega.idegaweb.commune.accounting.presentation.OperationalFieldsMenu;
import se.idega.idegaweb.commune.care.business.AccountingSession;
import se.idega.idegaweb.commune.presentation.CommuneBlock;
import se.idega.idegaweb.commune.school.business.SchoolClassWriter;
import se.idega.idegaweb.commune.school.business.SchoolCommuneBusiness;
import se.idega.idegaweb.commune.school.business.SchoolCommuneSession;

import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.business.SchoolClassComparator;
import com.idega.block.school.business.SchoolYearComparator;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolCategory;
import com.idega.block.school.data.SchoolCategoryHome;
import com.idega.block.school.data.SchoolClass;
import com.idega.block.school.data.SchoolSeason;
import com.idega.block.school.data.SchoolType;
import com.idega.block.school.data.SchoolYear;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWMainApplication;
import com.idega.idegaweb.UnavailableIWContext;
import com.idega.io.MediaWritable;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.RadioButton;
import com.idega.presentation.ui.Window;
/**
 * @author Laddi
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public abstract class SchoolCommuneBlock extends CommuneBlock {

	private final static String PARAM_BUNADM = "PARAM_BUNADM";
	
	private SchoolCommuneBusiness business;
	protected SchoolCommuneSession session;
	private AccountingSession accountingSession;
	private SchoolBusiness sBusiness;
	private int _schoolID = -1;
	private int _schoolSeasonID = -1;
	private int _schoolYearID = -1;
	private int _schoolClassID = -1;
	private boolean _centralAdmin = false;
	
	public static final String IS_SPECIALLY_PLACED_COLOR = "#EAF1FF";
	public static final String HAS_SCHOOL_CHOICE_COLOR = "#EAFFEE";
	public static final String HAS_MOVE_CHOICE_COLOR = "#FFEAEA";
	public static final String HAS_MOVE_CHOICE_COLOR_THIS_SCHOOL = "#FFEAFF";
	public static final String HAS_REJECTED_FIRST_CHOICE_COLOR = "#FDFFDD";
	public static final String HAS_MOVED_TO_COMMUNE_COLOR = "#DDDDFF";
	
	
	public void main(IWContext iwc) throws Exception{
		setResourceBundle(getResourceBundle(iwc));
		business = getSchoolCommuneBusiness(iwc);
		session = getSchoolCommuneSession(iwc);
		sBusiness = getSchoolBusiness(iwc);
		initialize(iwc);

		init(iwc);
	}
	
	public abstract void init(IWContext iwc) throws Exception;
	
	private void initialize(IWContext iwc) throws RemoteException {
		_centralAdmin = iwc.getParameter(PARAM_BUNADM) != null && iwc.getParameter(PARAM_BUNADM).equals("true"); 
		_schoolID = session.getSchoolID();	
		_schoolSeasonID = session.getSchoolSeasonID();
		_schoolYearID = session.getSchoolYearID();
		_schoolClassID = session.getSchoolClassID();

		if ( _schoolSeasonID == -1 ) {
			_schoolSeasonID = getSchoolCommuneBusiness(iwc).getCurrentSchoolSeasonID();
			session.setSchoolSeasonID(_schoolSeasonID);
		}
		
		if (_schoolYearID == -1) {
			_schoolClassID = -1;
		}
	}
	
	/**
	 * AccountingSession is used to get the school category value stored by the OperatingFieldsMenu block
	 * @see OperatingFieldsMeny
	 * @return AccountingSession
	 */
	public AccountingSession getAccountingSession() {
		if(accountingSession==null){
			try
			{
				accountingSession = (AccountingSession)IBOLookup.getSessionInstance(IWContext.getInstance(),AccountingSession.class);
			}
			catch (IBOLookupException e)
			{
				System.err.print("AccountingBlock.getSession(): Error looking up AccountingSession");
				e.printStackTrace();
			}
			catch (UnavailableIWContext e)
			{
				System.err.print("AccountingBlock.getSession(): Error getting IWContext");
				e.printStackTrace();
			}
		}
		return accountingSession;
	}
		
	protected Table getNavigationTable(boolean showClass) throws RemoteException {
		return getNavigationTable(showClass, false, false);
	}
		
	protected Table getNavigationTable(boolean showClass, boolean multipleSchools) throws RemoteException {
		return getNavigationTable(showClass, multipleSchools, false);
	}
	protected Table getNavigationTable(boolean showClass, boolean multipleSchools, boolean centralizedAdminChoice) throws RemoteException {
		return getNavigationTable(showClass, multipleSchools, centralizedAdminChoice, getAccountingSession().getOperationalField());
	}
	
	protected Table getNavigationTable(boolean showClass, boolean multipleSchools, boolean centralizedAdminChoice, String category) throws RemoteException {
		Table table = new Table(9,2);
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(3,"8");
		int row = 1;
		
		if (multipleSchools) {
			table.add(getSmallHeader(localize("school.opfield", "Operationalfield: ")), 1, row);
			table.mergeCells(2, row, 7, row);
			table.add(new OperationalFieldsMenu(), 2, row++);
			table.setHeight(row++, 15);			
			
			if (centralizedAdminChoice){
				table.resize(9, row + 2);
				table.add(getSmallHeader(localize("school.bun_adm","Show only BUN administrated schools")+":"+Text.NON_BREAKING_SPACE),1,row);
//				table.mergeCells(2, row, 8, row);
				
		
				RadioButton rb1 = new RadioButton(PARAM_BUNADM, ""+true);
				RadioButton rb2 = new RadioButton(PARAM_BUNADM, ""+false);		
						
				if (_centralAdmin){
					rb1.setSelected();
				} else{
					rb2.setSelected();
				}

				rb1.setToSubmit();
				rb2.setToSubmit();
				table.add(rb1,2,row);
				table.add(getSmallHeader(localize("school.yes","Yes")+Text.NON_BREAKING_SPACE),2,row);
					
				table.setNoWrap(3, row);
				table.add(rb2,3,row);
				table.add(getSmallHeader(localize("school.no","No")+Text.NON_BREAKING_SPACE),3,row);

				++row;
				table.setHeight(row, "2");
				++row;			
		
			}
						
			table.resize(9, row + 2);
			table.add(getSmallHeader(localize("school.school_list","School")+":"+Text.NON_BREAKING_SPACE),1,row);
			table.mergeCells(2, row, 8, row);
			table.add(getSchools(_centralAdmin, category),2,row);
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
			table.setWidth(6, "10");
			table.add(getSmallHeader(localize("school.class","Class")+":"+Text.NON_BREAKING_SPACE),7,row);
			DropdownMenu schClasses = getSchoolClasses();
			schClasses.addMenuElementFirst("-1","");
			table.add(schClasses, 8, row);
		}
		
		table.setColumnWidth(9, "10");
		
		return table;
	}
	


	protected DropdownMenu getSchools() throws RemoteException {
		return getSchools(true);
	}	
		
	/**
	 * 
	 * @param includeCentralizedAdministrated If true, includes schools administrated by BUN in the list
	 * @return
	 * @throws RemoteException
	 */
	protected DropdownMenu getSchools(boolean onlyCentralizedAdministrated, String category) throws RemoteException {
		
		DropdownMenu menu = new DropdownMenu(session.getParameterSchoolID());
		menu.setToSubmit();
		Collection schools = null;
		Collection schoolTypeIds = sBusiness.findAllSchoolTypesInCategory(category);
		if (schoolTypeIds == null){
			schoolTypeIds = new java.util.HashSet();
		}
				
		if (onlyCentralizedAdministrated) {
			schools = business.getSchoolBusiness().findAllCentralizedAdministratedByType(schoolTypeIds);			
		} else {
			schools = business.getSchoolBusiness().findAllSchoolsByType(schoolTypeIds);
		}
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
	
	/**
	 * 
	 * @param onlyCentralizedAdministrated
	 * @return
	 * @throws RemoteException
	 */
	protected DropdownMenu getSchools(boolean onlyCentralizedAdministrated) throws RemoteException{
		try{
			return getSchools(onlyCentralizedAdministrated, getSchoolCategoryHome().findElementarySchoolCategory().getCategory());		
		}catch(FinderException ex){
			System.err.println("Could not find Elementary school category.");	
			return null;		
		}
	}
	
	public SchoolCategoryHome getSchoolCategoryHome() throws RemoteException {
		return (SchoolCategoryHome) IDOLookup.getHome(SchoolCategory.class);
	}
		
	protected DropdownMenu getSchoolSeasons() throws RemoteException {
		return getSchoolSeasons(true);
	}
	
	protected DropdownMenu getSchoolSeasons(boolean setToSubmit) throws RemoteException {
		DropdownMenu menu = new DropdownMenu(session.getParameterSchoolSeasonID());
		if (setToSubmit) {
			menu.setToSubmit();
		}
		
		Collection seasons = business.getSchoolBusiness().findAllSchoolSeasons();
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
		int schoolTypeID = -1;
		
		if ( _schoolID != -1 ) {
			List years = new Vector(business.getSchoolBusiness().findAllSchoolYearsInSchool(_schoolID));
			if ( !years.isEmpty() ) {
	      Collections.sort(years,new SchoolYearComparator());
				Iterator iter = years.iterator();
				while (iter.hasNext()) {
					SchoolYear element = (SchoolYear) iter.next();
					if (element.getSchoolTypeId() != schoolTypeID) {
						SchoolType type = element.getSchoolType();
						menu.addDisabledMenuElement("-1", type.getSchoolTypeName());
						schoolTypeID = element.getSchoolTypeId();
					}
					menu.addMenuElement(element.getPrimaryKey().toString(), Text.NON_BREAKING_SPACE + Text.NON_BREAKING_SPACE + Text.NON_BREAKING_SPACE + element.getSchoolYearName());
				}
			}
			else {
				_schoolYearID = -1;
			}
		}
		
		if ( _schoolYearID != -1 )
			menu.setSelectedElement(_schoolYearID);
		
		return (DropdownMenu) getStyledInterface(menu);
	}

	protected DropdownMenu getSchoolClasses() throws RemoteException {
		DropdownMenu menu = new DropdownMenu(session.getParameterSchoolClassID());
		menu.setToSubmit();
		
		if ( _schoolID != -1 && _schoolSeasonID != -1 && _schoolYearID != -1 ) {
			List classes = new ArrayList(business.getSchoolBusiness().findSchoolClassesBySchoolAndSeasonAndYear(_schoolID, _schoolSeasonID, _schoolYearID));
			if ( !classes.isEmpty() ) {
	      Collections.sort(classes,new SchoolClassComparator());
				Iterator iter = classes.iterator();
				int row = 1;
				boolean separator = false;
				while (iter.hasNext()) {
					SchoolClass element = (SchoolClass) iter.next();
					if (row > 1 && element.getIsSubGroup() && !separator) {
						menu.addSeparator();
						separator = true;
					}
					menu.addMenuElement(element.getPrimaryKey().toString(), element.getName());
					//if ( _schoolClassID == -1 )
						// Gimmi 13.11.2002
						//_schoolClassID = ((Integer)element.getPrimaryKey()).intValue();
					row++;
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
		
	public Link getPDFLink(Class classToUse,Image image) {
		Link link = new Link(image);
		link.setWindow(getFileWindow());
		link.addParameter(SchoolClassWriter.prmPrintType, SchoolClassWriter.PDF);
		link.addParameter(MediaWritable.PRM_WRITABLE_CLASS, IWMainApplication.getEncryptedClassName(classToUse));
		return link;
	}

	public Link getXLSLink(Class classToUse,Image image) {
		Link link = new Link(image);
		link.setWindow(getFileWindow());
		link.addParameter(SchoolClassWriter.prmPrintType, SchoolClassWriter.XLS);
		link.addParameter(MediaWritable.PRM_WRITABLE_CLASS, IWMainApplication.getEncryptedClassName(classToUse));
		return link;
	}

	public Window getFileWindow() {
		Window w = new Window(localize("school.class", "School class"), getIWApplicationContext().getIWMainApplication().getMediaServletURI());
		w.setResizable(true);
		w.setMenubar(true);
		w.setHeight(400);
		w.setWidth(500);
		return w;
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
	
	protected Table getLegendTable() {
		return getLegendTable(false);
	}
	
	protected Table getLegendTable(boolean showWithMoveToSchool) {
		return getLegendTable(showWithMoveToSchool, false);
	}
	
	protected Table getLegendTable(boolean showWithMoveToSchool, boolean showWithMovedToCommune) {
		Table table = new Table(8,1);
		table.setHeight(1, 12);
		table.setWidth(1, "12");
		table.setWidth(3, "12");
		table.setWidth(4, "12");
		table.setWidth(6, "12");
		table.setWidth(7, "12");
		
		if (showWithMoveToSchool) {
			table.add(getColorTable(HAS_MOVE_CHOICE_COLOR_THIS_SCHOOL), 1, 1);
			table.add(getColorTable(HAS_REJECTED_FIRST_CHOICE_COLOR), 4, 1);
			if (showWithMovedToCommune) {
				table.add(getColorTable(HAS_MOVED_TO_COMMUNE_COLOR), 7, 1);
			}

			table.add(getSmallHeader(localize("school.student_has_move_choice_to_this_school","Student has move choice to this school")), 2, 1);
			table.add(getSmallHeader(localize("school.student_has_rejected_first_choice","Student has rejected first choice")), 5, 1);
			if (showWithMovedToCommune) {
				table.add(getSmallHeader(localize("school.student_has_moved_to_commune","Student has moved to commune")), 8, 1);
			}
		}
		else {
			table.add(getColorTable(HAS_MOVE_CHOICE_COLOR), 1, 1);
			table.add(getColorTable(HAS_SCHOOL_CHOICE_COLOR), 4, 1);
			table.add(getColorTable(IS_SPECIALLY_PLACED_COLOR), 7, 1);
		
			table.add(getSmallHeader(localize("school.student_has_move_choice","Student has move choice")), 2, 1);
			table.add(getSmallHeader(localize("school.student_has_school_choice","Student has school choice")), 5, 1);
			table.add(getSmallHeader(localize("school.student_is_specially_placed","Student is specially placed")), 8, 1);
		}
		
		return table;
	}
	
	private Table getColorTable(String color) {
		Table colorTable = new Table(1, 1);
		colorTable.setHeight(1, 1, "12");
		colorTable.setWidth(1, 1, "12");
		colorTable.setColor("#000000");
		colorTable.setColor(1, 1, color);
		colorTable.setCellpadding(0);
		colorTable.setCellspacing(1);
		
		return colorTable;		
	}
	
}