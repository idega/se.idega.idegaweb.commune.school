/*
 * Created on 1.9.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package se.idega.idegaweb.commune.school.presentation;

import java.io.IOException;
import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import se.idega.idegaweb.commune.presentation.CommuneBlock;
import se.idega.idegaweb.commune.school.business.SchoolChoiceBusiness;
import se.idega.idegaweb.commune.school.business.SchoolCommuneBusiness;

import com.idega.block.datareport.business.DynamicReportDesign;
import com.idega.block.datareport.business.JasperReportBusiness;
import com.idega.block.datareport.util.ReportableCollection;
import com.idega.block.datareport.util.ReportableField;
import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.data.SchoolSeason;
import com.idega.block.school.data.SchoolYear;
import com.idega.business.IBOLookup;
import com.idega.data.IDOException;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.BackButton;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.InterfaceObject;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.util.IWTimestamp;
import com.idega.util.datastructures.QueueMap;

import dori.jasper.engine.JRDataSource;
import dori.jasper.engine.JRException;
import dori.jasper.engine.JasperPrint;
import dori.jasper.engine.design.JasperDesign;

/**
 * Title:		MandatorySchoolReminder
 * Description:
 * Copyright:	Copyright (c) 2003
 * Company:		idega Software
 * @author		2003 - idega team - <br><a href="mailto:gummi@idega.is">Gudmundur Agust Saemundsson</a><br>
 * @version		1.0
 */
public class MandatorySchoolReminder extends CommuneBlock {

	public final static String STYLE = "font-family:arial; font-size:8pt; color:#000000; text-align: justify; border: 1 solid #000000;";
	public final static String STYLE_2 = "font-family:arial; font-size:8pt; color:#000000; text-align: justify;";
	
	private static final String IW_BUNDLE_IDENTIFIER = "se.idega.idegaweb.commune";
	
	private static final String ACTION_INITIALIZE = "init";
	private static final String ACTION_SELECT_SCHOOL_SEASON = "season";
	private static final String ACTION_GET_REPORT = "report";
	private static final String ACTION_CANCEL = "cancel";
	
	private String _action = ACTION_INITIALIZE;
	public static final String PRM_ACTION = "action";
	
	public static final String PRM_SEASON = "season";
	public static final String PRM_DATE = "date";
	
	private Table _fieldTable;
	
	public Date _selectedDate = null;
	
	public String _reportName = "Mandatory school";
	
	/**
	 * 
	 */
	public MandatorySchoolReminder() {
		super();
	}
	
	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}
	
	private void parseAction(IWContext iwc){
		_action = iwc.getParameter(PRM_ACTION);
		if(_action == null){
			_action = ACTION_INITIALIZE;
		}
	}
	
	public void lineUpSeasonSelection(IWContext iwc, IWResourceBundle iwrb){
//		IWMainApplication iwma = iwc.getApplicationContext().getApplication();		
//		IWBundle coreBundle = iwma.getBundle(this.IW_CORE_BUNDLE_IDENTIFIER);
		DateFormat df = SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT,iwc.getCurrentLocale()); 
		Date date = IWTimestamp.getTimestampRightNow();
		
		_fieldTable = new Table(2,2);
		//_fieldTable.setBorder(1);
		
		//
		_fieldTable.add(getFieldLabel(iwrb.getLocalizedString("MandatorySchoolReminder.date", "Date:")),1,1);		
		TextInput dateInput = new TextInput(PRM_DATE);
		dateInput.setContent(df.format(date));
		setStyle(dateInput);
		_fieldTable.add(dateInput,2,1);
		
		//choose schoolseason
//		_fieldTable.add(getFieldLabel(iwrb.getLocalizedString("school_season", "School season:")),1,1);		
//		SchoolSeasonDropdown seasonDropdown = new SchoolSeasonDropdown(PRM_SEASON);
//		setStyle(seasonDropdown);
//		_fieldTable.add(seasonDropdown,2,1);
		
		InterfaceObject generateButton = (InterfaceObject)getSubmitButton(iwrb.getLocalizedString("MandatorySchoolReminder.search"," Search "),ACTION_SELECT_SCHOOL_SEASON);
		_fieldTable.add(generateButton,1,2);
		_fieldTable.mergeCells(1,2,2,2);
		_fieldTable.setColumnAlignment(1,Table.HORIZONTAL_ALIGN_RIGHT);

	}
	
	
	public void main(IWContext iwc) throws Exception{
		IWResourceBundle iwrb = getResourceBundle(iwc);
		
		try {
			parseAction(iwc);
			parseDate(iwc);
				
			if(_action.equals(ACTION_INITIALIZE)){
				lineUpSeasonSelection(iwc,iwrb);
				Form form = new Form();
				form.add(_fieldTable);
				this.add(form);
			} else if(_action.equals(ACTION_SELECT_SCHOOL_SEASON)){
				DateFormat df = SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT,iwc.getCurrentLocale()); 
				
				this.add(getFieldLabel(iwrb.getLocalizedString("MandatorySchoolReminder.date", "Date:")));		
				this.add(getText(df.format(_selectedDate)));
				this.add(Text.getBreak());
				this.add(getText(iwrb.getLocalizedString("number_of_students_not_registered_in_any_class","Number of students not registered in any class")));
				this.add(Text.getBreak());
				presentResultAsCount(iwc,iwrb);
				this.add(Text.getBreak());
				
				InterfaceObject generateButton = (InterfaceObject)getSubmitButton(iwrb.getLocalizedString("MandatorySchoolReminder.generate_report"," Generate "),ACTION_GET_REPORT);
				
				Form form = new Form();
				form.add(generateButton);
				form.add(new HiddenInput(PRM_DATE,iwc.getParameter(PRM_DATE)));
				this.add(form);
				
			} else if(_action.equals(ACTION_GET_REPORT)){
				
				presentResultAsReport(iwc,iwrb);
				
			} else if(_action.equals(ACTION_CANCEL)){
				lineUpSeasonSelection(iwc,iwrb);
				Form form = new Form();
				form.add(_fieldTable);
				this.add(form);
			}
		}  catch (MandatorySchoolReminderException e) {
			add(e.getLocalizedMessage());
			add(Text.getBreak());
			add(Text.getBreak());
			BackButton back = new BackButton();
			setStyle(back);
			add(back);
			
			//TMP
			Throwable cause = e.getCause();
			if(cause != null){
				cause.printStackTrace();
			} else {
				e.printStackTrace();
			}
			
//			if(false){  // if is developer
//				add(Text.getBreak());
//				add(Text.getBreak());
//				Throwable cause = e.getCause();
//				if(cause != null){
//					cause.printStackTrace();
//					add(stackTrace);
//			}
		}

	}
		
	
	/**
	 * @param iwc
	 */
	private void parseDate(IWContext iwc) throws ParseException {

		String date = iwc.getParameter(PRM_DATE);
		if(date != null){
			DateFormat df = SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT,iwc.getCurrentLocale()); 
			_selectedDate = df.parse(date);
		}
		
	}

	/**
	 * 
	 */
	private void presentResultAsCount(IWContext iwc,IWResourceBundle iwrb) throws RemoteException, MandatorySchoolReminderException{
		SchoolBusiness sBusiness =  (SchoolBusiness)IBOLookup.getServiceInstance(iwc,SchoolBusiness.class);
		SchoolChoiceBusiness scBusiness = (SchoolChoiceBusiness)IBOLookup.getServiceInstance(iwc,SchoolChoiceBusiness.class);
		
		
		
		try {
			SchoolSeason currentSeason = scBusiness.getCurrentSeason();
			Collection schoolyears = sBusiness.getSchoolYearHome().findAllSchoolYears();
			IWTimestamp seasonStartDate = new IWTimestamp(currentSeason.getSchoolSeasonStart());
			int currentYear = seasonStartDate.getYear();
			
			Collection classes = sBusiness.getSchoolClassHome().findBySeason(currentSeason);

			Table countTable = new Table(2,1+schoolyears.size());
			countTable.setCellpadding(0);
			int index = 1;
			String labelCourse = iwrb.getLocalizedString("MandatorySchoolReminder.course","Course");
			Iterator iter = schoolyears.iterator();
			while (iter.hasNext()) {
				SchoolYear schoolYear = (SchoolYear)iter.next();
				int schoolYearAge = schoolYear.getSchoolYearAge();
				int yearOfBirth = currentYear-schoolYearAge;
				IWTimestamp firstDateOfBirth = new IWTimestamp(1,1,yearOfBirth);
				IWTimestamp lastDateOfBirth = new IWTimestamp(31,12,yearOfBirth);
				
				int countResult = -1;
				
				try {
					countResult	= sBusiness.getSchoolClassMemberHome().getNumberOfUsersNotAssignedToClassOnGivenDate(new java.sql.Date(_selectedDate.getTime()),classes,firstDateOfBirth.getDate(),lastDateOfBirth.getDate());
				} catch (IDOException e) {
					e.printStackTrace();
				}
							
				
				countTable.add(new Text(labelCourse+" "+schoolYear.getSchoolYearName()+":"),1,index);
				countTable.add(new Text(String.valueOf(countResult)),2,index);
				
				
				
				index++;
			}
			
			
			this.add(countTable);	
		
		} catch (FinderException e) {
			throw new MandatorySchoolReminderException("No current schoolseason found in database",e,iwrb.getLocalizedString("could_not_find_current_school_season","Could not find current school season"));
		}
	}


	/**
	 * 
	 */
	private void presentResultAsReport(IWContext iwc,IWResourceBundle iwrb) throws MandatorySchoolReminderException, IOException, JRException, IDOLookupException, RemoteException, IDOException, CreateException{

		SchoolBusiness sBusiness =  (SchoolBusiness)IBOLookup.getServiceInstance(iwc,SchoolBusiness.class);
		SchoolChoiceBusiness scBusiness = (SchoolChoiceBusiness)IBOLookup.getServiceInstance(iwc,SchoolChoiceBusiness.class);
		JasperReportBusiness jasperBusiness =  (JasperReportBusiness)IBOLookup.getServiceInstance(iwc,JasperReportBusiness.class);
		DateFormat df = SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT,iwc.getCurrentLocale()); 
		
		
		
		try {
			SchoolSeason currentSeason = scBusiness.getCurrentSeason();
//			Collection schoolyears = sBusiness.getSchoolYearHome().findAllSchoolYears();
//			IWTimestamp seasonStartDate = new IWTimestamp(currentSeason.getSchoolSeasonStart());
//			int currentYear = seasonStartDate.getYear();
			
			Collection classes = sBusiness.getSchoolClassHome().findBySeason(currentSeason);
	
			JRDataSource dataSource = getDataSource(iwc,currentSeason,classes);
			Map parameterMap = getParameterMap(iwrb,df,dataSource);
			JasperDesign design = getReportDesign(jasperBusiness,dataSource,parameterMap);
			String reportPath = generateReport(jasperBusiness,dataSource,parameterMap,design);
			
			this.add(getReportLink(iwrb.getLocalizedString("MandatorySchoolReminder.report_name","Mandatory school reminder report"),reportPath));


		
		} catch (FinderException e) {
			throw new MandatorySchoolReminderException("No current schoolseason found in database",e,iwrb.getLocalizedString("could_not_find_current_school_season","Could not find current school season"));
		}





	}


	/**
	 * @param dataSource
	 * @param parameterMap
	 * @param design
	 * @return
	 */
	private String generateReport(JasperReportBusiness jasperBusiness, JRDataSource dataSource, Map parameterMap, JasperDesign design) throws RemoteException, JRException {				
		parameterMap.put(DynamicReportDesign.PRM_REPORT_NAME,_reportName);
		
		JasperPrint print = jasperBusiness.getReport(dataSource,parameterMap,design);
		return jasperBusiness.getExcelReport(print,_reportName);
		//return jasperBusiness.getPdfReport(print,_reportName);
		//return jasperBusiness.getHtmlReport(print,_reportName);
	}

	/**
	 * @param dataSource
	 * @return
	 */
	private JasperDesign getReportDesign(JasperReportBusiness jasperBusiness,JRDataSource dataSource, Map parameters) throws IOException, JRException {
		return jasperBusiness.generateLayout(dataSource,parameters);
	}

	/**
	 * @param dataSource
	 * @return
	 */
	private Map getParameterMap(IWResourceBundle iwrb,DateFormat dateFormat, JRDataSource _dataSource) {
		Map _parameterMap = new QueueMap();
		Map extraParameters = null;
				
		if(_dataSource != null && _dataSource instanceof ReportableCollection){
			ReportableCollection reportData = ((ReportableCollection)_dataSource);
			extraParameters = reportData.getExtraHeaderParameters();
			
			reportData.addExtraHeaderParameter("label_selected_date",iwrb.getLocalizedString("MandatorySchoolReminder.label_selected_date","Date"),"selected_date",dateFormat.format(_selectedDate));

			if(extraParameters!= null){
				_parameterMap.putAll(extraParameters);
			}
				
		}
		return _parameterMap;
	}

	/**
	 * @param currentSeason
	 * @param classes
	 * @return
	 */
	private JRDataSource getDataSource(IWContext iwc, SchoolSeason currentSeason, Collection classes) throws IDOLookupException, RemoteException, IDOException, FinderException, CreateException {
		SchoolCommuneBusiness business = (SchoolCommuneBusiness)IBOLookup.getServiceInstance(iwc,SchoolCommuneBusiness.class);
		return business.getReportOfUsersNotRegisteredInAnyClass(iwc.getCurrentLocale(),_selectedDate,currentSeason,classes);
	}

	/**
	 * @param iwc
	 * @return
	 */
	private PresentationObject getReportLink(String name, String path) {
		Link link = new Link(name,path);
		return link;
	}



	private PresentationObject getSubmitButton(String text,String action){
		SubmitButton button = new SubmitButton(text,PRM_ACTION,action);
		setStyle(button);
		return button;
	}
	
	
	private Text getFieldLabel(String text){
		Text fieldLabel = this.getText(text);
		setStyle(fieldLabel);
		return fieldLabel;
	}
	
	public void setStyle(PresentationObject obj){
		if(obj instanceof Text){
			this.setStyle((Text)obj);
		} else {
			obj.setAttribute("style",STYLE);
		}
	}

	public void setStyle(Text obj){
		obj.setAttribute("style",STYLE_2);
	}
	
	public void setReportName(String name){
		_reportName = name;
	}
	
	
	private class MandatorySchoolReminderException extends Exception{
		
		//	jdk 1.3 - 1.4 fix
		private Throwable _cause = this;
		
		private String _localizedMessage = null;
//		
//		private String _localizedKey = null;
//		private String _defaultUserFriendlyMessage = null;
		
		
		public MandatorySchoolReminderException(String tecnicalMessage, Throwable cause,String localizedMessage) {
			this(tecnicalMessage,cause);
			_localizedMessage = localizedMessage;
//			_localizedKey = localizedKey;
//			_defaultUserFriendlyMessage = defaultUserFriendlyMessage;
		}
		
		/**
		 * @param message
		 * @param cause
		 */
		public MandatorySchoolReminderException(String message, Throwable cause) {
			super(message);
			_localizedMessage = message;
			// jdk 1.3 - 1.4 fix
			_cause = cause;
		}
		
		/**
		 * 
		 */
		private MandatorySchoolReminderException() {
			super();
		}
		/**
		 * @param message
		 */
		private MandatorySchoolReminderException(String message) {
			super(message);
		}

		/**
		 * @param cause
		 */
		private MandatorySchoolReminderException(Throwable cause) {
			super();
			// jdk 1.3 - 1.4 fix
			_cause = cause;
			
		}
		
		//	jdk 1.3 - 1.4 fix
		public Throwable getCause(){
			return _cause;
		}
		
		
//		public String getLocalizedMessageKey(){
//			return _localizedKey;
//		}
//		
//		public String getDefaultLocalizedMessage(){
//			return _defaultUserFriendlyMessage;
//		}
		
		public String getLocalizedMessage(){
			return _localizedMessage;
		}
		
		
		
		
	}

}
