/*
 * Created on 17.12.2003
 */
package se.idega.idegaweb.commune.school.business;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;

import javax.ejb.FinderException;

import se.idega.idegaweb.commune.business.CommuneUserBusiness;
import se.idega.idegaweb.commune.school.data.SchoolChoice;
import se.idega.util.PIDChecker;

import com.idega.block.datareport.util.ReportableCollection;
import com.idega.block.datareport.util.ReportableData;
import com.idega.block.datareport.util.ReportableField;
import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.business.IBOLookup;
import com.idega.business.IBORuntimeException;
import com.idega.business.IBOSessionBean;
import com.idega.core.contact.data.Email;
import com.idega.core.contact.data.Phone;
import com.idega.core.location.data.Address;
import com.idega.core.location.data.PostalCode;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;
import com.idega.util.PersonalIDFormatter;

/**
 * @author laddi
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class SchoolReportBusinessBean extends IBOSessionBean implements SchoolReportBusiness {

	private final static String IW_BUNDLE_IDENTIFIER = "se.idega.idegaweb.commune";
	
	private IWBundle _iwb;
	private IWResourceBundle _iwrb;
	private Collection _columnsToDisplay;

	private void initializeBundlesIfNeeded() {
		if (_iwb == null) {
			_iwb = this.getIWApplicationContext().getApplication().getBundle(IW_BUNDLE_IDENTIFIER);
		}
		_iwrb = _iwb.getResourceBundle(this.getUserContext().getCurrentLocale());
	}
	
	private void fillColumns(Collection columns) {
		if (columns.size() == 0) {
			_columnsToDisplay = new ArrayList();
			_columnsToDisplay.add(FIELD_PERSONAL_ID);
			_columnsToDisplay.add(FIELD_NAME);
			_columnsToDisplay.add(FIELD_ADDRESS);
			_columnsToDisplay.add(FIELD_GENDER);
		}
		else {
			_columnsToDisplay = columns;
		}
	}
	
	private boolean displayColumn(String columnName) {
		if (_columnsToDisplay.contains(columnName)) {
			return true;
		}
		return false;
	}
	
	public ReportableCollection getGroupReport(Collection schoolGroups, Collection columnNames, String freeText) {
		fillColumns(columnNames);
		initializeBundlesIfNeeded();
		Locale currentLocale = this.getUserContext().getCurrentLocale();
		
		ReportableCollection reportCollection = new ReportableCollection();
		
		reportCollection.addExtraHeaderParameterAtBeginning(
				"school_group_report",
				getLocalizedString("extra_information", "Extra information"),
				"label",
				freeText);
		
		ReportableField personalID = new ReportableField(FIELD_PERSONAL_ID, String.class);
		personalID.setLocalizedName(getLocalizedString(FIELD_PERSONAL_ID, "Personal ID"), currentLocale);
		if (displayColumn(FIELD_PERSONAL_ID)) {
			reportCollection.addField(personalID);
		}
		
		ReportableField name = new ReportableField(FIELD_NAME, String.class);
		name.setLocalizedName(getLocalizedString(FIELD_NAME, "Name"), currentLocale);
		if (displayColumn(FIELD_NAME)) {
			reportCollection.addField(name);
		}
		
		ReportableField address = new ReportableField(FIELD_ADDRESS, String.class);
		address.setLocalizedName(getLocalizedString(FIELD_ADDRESS, "Address"), currentLocale);
		if (displayColumn(FIELD_ADDRESS)) {
			reportCollection.addField(address);
		}
		
		ReportableField zipCode = new ReportableField(FIELD_ZIP_CODE, String.class);
		zipCode.setLocalizedName(getLocalizedString(FIELD_ZIP_CODE, "Zip code"), currentLocale);
		if (displayColumn(FIELD_ZIP_CODE)) {
			reportCollection.addField(zipCode);
		}
		
		ReportableField area = new ReportableField(FIELD_AREA, String.class);
		area.setLocalizedName(getLocalizedString(FIELD_AREA, "Area"), currentLocale);
		if (displayColumn(FIELD_AREA)) {
			reportCollection.addField(area);
		}
		
		ReportableField email = new ReportableField(FIELD_EMAIL, String.class);
		email.setLocalizedName(getLocalizedString(FIELD_EMAIL, "E-mail"), currentLocale);
		if (displayColumn(FIELD_EMAIL)) {
			reportCollection.addField(email);
		}
		
		ReportableField phone = new ReportableField(FIELD_PHONE, String.class);
		phone.setLocalizedName(getLocalizedString(FIELD_PHONE, "Phone"), currentLocale);
		if (displayColumn(FIELD_PHONE)) {
			reportCollection.addField(phone);
		}
		
		ReportableField gender = new ReportableField(FIELD_GENDER, String.class);
		gender.setLocalizedName(getLocalizedString(FIELD_GENDER, "Gender"), currentLocale);
		if (displayColumn(FIELD_GENDER)) {
			reportCollection.addField(gender);
		}
		
		ReportableField language = new ReportableField(FIELD_LANGUAGE, String.class);
		language.setLocalizedName(getLocalizedString(FIELD_LANGUAGE, "Language"), currentLocale);
		if (displayColumn(FIELD_LANGUAGE)) {
			reportCollection.addField(language);
		}
		
		ReportableField custodian = new ReportableField(FIELD_CUSTODIAN, String.class);
		custodian.setLocalizedName(getLocalizedString(FIELD_CUSTODIAN, "Custodian"), currentLocale);
		if (displayColumn(FIELD_CUSTODIAN)) {
			reportCollection.addField(custodian);
		}
		
		ReportableField alternateAddress = new ReportableField(FIELD_ALTERNATE_ADDRESS, String.class);
		alternateAddress.setLocalizedName(getLocalizedString(FIELD_ALTERNATE_ADDRESS, "Alternate address"), currentLocale);
		if (displayColumn(FIELD_ALTERNATE_ADDRESS)) {
			reportCollection.addField(alternateAddress);
		}
		
		ReportableField yearsWithLanguage = new ReportableField(FIELD_YEARS_WITH_LANGUAGE, String.class);
		yearsWithLanguage.setLocalizedName(getLocalizedString(FIELD_YEARS_WITH_LANGUAGE, "Years with language"), currentLocale);
		if (displayColumn(FIELD_YEARS_WITH_LANGUAGE)) {
			reportCollection.addField(yearsWithLanguage);
		}
		
		try {
			Collection students = getSchoolBusiness().getSchoolClassMemberHome().findBySchoolClasses(schoolGroups);
		
			Iterator iter = students.iterator();
			while (iter.hasNext()) {
				SchoolClassMember student = (SchoolClassMember) iter.next();
				User user = student.getStudent();
				Address homeAddress = getUserBusiness().getUsersMainAddress(user);
				Email mail = getUserBusiness().getEmail(user);
				Phone homePhone = getUserBusiness().getChildHomePhone(user);
				User parent = getUserBusiness().getCustodianForChild(user);
				
				ReportableData data = new ReportableData();
				if (displayColumn(FIELD_PERSONAL_ID)) {
					data.addData(personalID, PersonalIDFormatter.format(user.getPersonalID(), currentLocale));
				}
				if (displayColumn(FIELD_NAME)) {
					data.addData(name, user.getNameLastFirst(true));
				}

				if (homeAddress != null) {
					if (displayColumn(FIELD_ADDRESS)) {
						data.addData(address, homeAddress.getStreetAddress());
					}
					PostalCode code = homeAddress.getPostalCode();
					
					if (code != null) {
						if (displayColumn(FIELD_ZIP_CODE)) {
							data.addData(zipCode, code.getPostalCode());
						}

						if (displayColumn(FIELD_AREA)) {
							data.addData(area, code.getPostalAddress());
						}
					}
				}
				if (mail != null) {
					if (displayColumn(FIELD_EMAIL)) {
						data.addData(email, mail.getEmailAddress());
					}
				}
				if (homePhone != null) {
					if (displayColumn(FIELD_PHONE)) {
						data.addData(phone, homePhone.getNumber());
					}
				}
				
				if (displayColumn(FIELD_GENDER)) {
					String genderString = null;
					if (PIDChecker.getInstance().isFemale(user.getPersonalID()))
						genderString = _iwrb.getLocalizedString("school.girl", "Girl");
					else
						genderString = _iwrb.getLocalizedString("school.boy", "Boy");
					data.addData(gender, genderString);
				}
				
				if (displayColumn(FIELD_LANGUAGE)) {
					if (student.getLanguage() != null) {
						data.addData(language, student.getLanguage());
					}
				}
				
				if (parent != null) {
					if (displayColumn(FIELD_CUSTODIAN)) {
						data.addData(custodian, parent.getNameLastFirst(true));
					}

					if (displayColumn(FIELD_ALTERNATE_ADDRESS)) {
						Address coAddress = getUserBusiness().getUsersCoAddress(parent);
						if (coAddress != null) {
							data.addData(alternateAddress, coAddress.getStreetAddress());
						}
					}
				}
				if (displayColumn(FIELD_YEARS_WITH_LANGUAGE)) {
					data.addData(yearsWithLanguage, "1");
				}
				
				reportCollection.add(data);
			}
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
		
		return reportCollection;
	}
	
	public ReportableCollection getChoicesReport(Collection columnNames) {
		fillColumns(columnNames);
		initializeBundlesIfNeeded();
		Locale currentLocale = this.getUserContext().getCurrentLocale();
		
		ReportableCollection reportCollection = new ReportableCollection();
		
		ReportableField personalID = new ReportableField(FIELD_PERSONAL_ID, String.class);
		personalID.setLocalizedName(getLocalizedString(FIELD_PERSONAL_ID, "Personal ID"), currentLocale);
		if (displayColumn(FIELD_PERSONAL_ID)) {
			reportCollection.addField(personalID);
		}
		
		ReportableField name = new ReportableField(FIELD_NAME, String.class);
		name.setLocalizedName(getLocalizedString(FIELD_NAME, "Name"), currentLocale);
		if (displayColumn(FIELD_NAME)) {
			reportCollection.addField(name);
		}
		
		ReportableField address = new ReportableField(FIELD_ADDRESS, String.class);
		address.setLocalizedName(getLocalizedString(FIELD_ADDRESS, "Address"), currentLocale);
		if (displayColumn(FIELD_ADDRESS)) {
			reportCollection.addField(address);
		}
		
		ReportableField zipCode = new ReportableField(FIELD_ZIP_CODE, String.class);
		zipCode.setLocalizedName(getLocalizedString(FIELD_ZIP_CODE, "Zip code"), currentLocale);
		if (displayColumn(FIELD_ZIP_CODE)) {
			reportCollection.addField(zipCode);
		}
		
		ReportableField area = new ReportableField(FIELD_AREA, String.class);
		area.setLocalizedName(getLocalizedString(FIELD_AREA, "Area"), currentLocale);
		if (displayColumn(FIELD_AREA)) {
			reportCollection.addField(area);
		}
		
		ReportableField email = new ReportableField(FIELD_EMAIL, String.class);
		email.setLocalizedName(getLocalizedString(FIELD_EMAIL, "E-mail"), currentLocale);
		if (displayColumn(FIELD_EMAIL)) {
			reportCollection.addField(email);
		}
		
		ReportableField phone = new ReportableField(FIELD_PHONE, String.class);
		phone.setLocalizedName(getLocalizedString(FIELD_PHONE, "Phone"), currentLocale);
		if (displayColumn(FIELD_PHONE)) {
			reportCollection.addField(phone);
		}
		
		ReportableField gender = new ReportableField(FIELD_GENDER, String.class);
		gender.setLocalizedName(getLocalizedString(FIELD_GENDER, "Gender"), currentLocale);
		if (displayColumn(FIELD_GENDER)) {
			reportCollection.addField(gender);
		}
		
		ReportableField fromSchool = new ReportableField(FIELD_FROM_SCHOOL, String.class);
		fromSchool.setLocalizedName(getLocalizedString(FIELD_FROM_SCHOOL, "From school"), currentLocale);
		if (displayColumn(FIELD_FROM_SCHOOL)) {
			reportCollection.addField(fromSchool);
		}
		
		ReportableField applicationDate = new ReportableField(FIELD_APPLICATION_DATE, String.class);
		applicationDate.setLocalizedName(getLocalizedString(FIELD_APPLICATION_DATE, "From school"), currentLocale);
		if (displayColumn(FIELD_APPLICATION_DATE)) {
			reportCollection.addField(applicationDate);
		}
		
		ReportableField message = new ReportableField(FIELD_MESSAGE, String.class);
		message.setLocalizedName(getLocalizedString(FIELD_MESSAGE, "Message"), currentLocale);
		if (displayColumn(FIELD_MESSAGE)) {
			reportCollection.addField(message);
		}
		
		ReportableField language = new ReportableField(FIELD_LANGUAGE_CHOICE, String.class);
		language.setLocalizedName(getLocalizedString(FIELD_LANGUAGE_CHOICE, "Language choice"), currentLocale);
		if (displayColumn(FIELD_LANGUAGE_CHOICE)) {
			reportCollection.addField(language);
		}
		
		ReportableField custodian = new ReportableField(FIELD_CUSTODIAN, String.class);
		custodian.setLocalizedName(getLocalizedString(FIELD_CUSTODIAN, "Custodian"), currentLocale);
		if (displayColumn(FIELD_CUSTODIAN)) {
			reportCollection.addField(custodian);
		}
		
		ReportableField alternateAddress = new ReportableField(FIELD_ALTERNATE_ADDRESS, String.class);
		alternateAddress.setLocalizedName(getLocalizedString(FIELD_ALTERNATE_ADDRESS, "Alternate address"), currentLocale);
		if (displayColumn(FIELD_ALTERNATE_ADDRESS)) {
			reportCollection.addField(alternateAddress);
		}
		
		try {
			String[] validStatuses = new String[] { getSchoolChoiceBusiness().getCaseStatusPreliminary().getStatus(), getSchoolChoiceBusiness().getCaseStatusMoved().getStatus() };
			int schoolYearAge = getSchoolCommuneBusiness().getGradeForYear(getSchoolSession().getSchoolYearID());
			Collection applicants = getSchoolChoiceBusiness().getApplicantsForSchool(getSchoolSession().getSchoolID(), getSchoolSession().getSchoolSeasonID(), schoolYearAge, validStatuses, null, SchoolChoiceComparator.NAME_SORT, -1, -1);
			
			Iterator iter = applicants.iterator();
			while (iter.hasNext()) {
				SchoolChoice choice = (SchoolChoice) iter.next();
				User user = choice.getChild();
				Address homeAddress = getUserBusiness().getUsersMainAddress(user);
				Email mail = getUserBusiness().getEmail(user);
				Phone homePhone = getUserBusiness().getChildHomePhone(user);
				User parent = getUserBusiness().getCustodianForChild(user);
				
				ReportableData data = new ReportableData();
				if (displayColumn(FIELD_PERSONAL_ID)) {
					data.addData(personalID, PersonalIDFormatter.format(user.getPersonalID(), currentLocale));
				}
				if (displayColumn(FIELD_NAME)) {
					data.addData(name, user.getNameLastFirst(true));
				}

				if (homeAddress != null) {
					if (displayColumn(FIELD_ADDRESS)) {
						data.addData(address, homeAddress.getStreetAddress());
					}
					PostalCode code = homeAddress.getPostalCode();
					
					if (code != null) {
						if (displayColumn(FIELD_ZIP_CODE)) {
							data.addData(zipCode, code.getPostalCode());
						}

						if (displayColumn(FIELD_AREA)) {
							data.addData(area, code.getPostalAddress());
						}
					}
				}
				if (mail != null) {
					if (displayColumn(FIELD_EMAIL)) {
						data.addData(email, mail.getEmailAddress());
					}
				}
				if (homePhone != null) {
					if (displayColumn(FIELD_PHONE)) {
						data.addData(phone, homePhone.getNumber());
					}
				}
				
				if (displayColumn(FIELD_GENDER)) {
					String genderString = null;
					if (PIDChecker.getInstance().isFemale(user.getPersonalID()))
						genderString = _iwrb.getLocalizedString("school.girl", "Girl");
					else
						genderString = _iwrb.getLocalizedString("school.boy", "Boy");
					data.addData(gender, genderString);
				}
				
				if (displayColumn(FIELD_LANGUAGE_CHOICE)) {
					if (choice.getLanguageChoice() != null) {
						data.addData(language, choice.getLanguageChoice());
					}
					
				}
				
				if (displayColumn(FIELD_FROM_SCHOOL)) {
					School previousSchool = choice.getCurrentSchool();
					if (previousSchool != null) {
						data.addData(fromSchool, previousSchool.getSchoolName());
					}
				}

				if (displayColumn(FIELD_APPLICATION_DATE)) {
					IWTimestamp stamp = new IWTimestamp(choice.getCreated());
					data.addData(applicationDate, stamp.getLocaleDate(currentLocale, IWTimestamp.SHORT));
				}

				if (displayColumn(FIELD_MESSAGE)) {
					if (choice.getMessage() != null) {
						data.addData(message, choice.getMessage());
					}
				}
				
				if (parent != null) {
					if (displayColumn(FIELD_CUSTODIAN)) {
						data.addData(custodian, parent.getNameLastFirst(true));
					}

					if (displayColumn(FIELD_ALTERNATE_ADDRESS)) {
						Address coAddress = getUserBusiness().getUsersCoAddress(parent);
						if (coAddress != null) {
							data.addData(alternateAddress, coAddress.getStreetAddress());
						}
					}
				}
				
				reportCollection.add(data);
			}
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		
		return reportCollection;
	}
	
	private String getLocalizedString(String key, String defaultValue) {
		return _iwrb.getLocalizedString(PREFIX + key, defaultValue);
	}
	
	private SchoolBusiness getSchoolBusiness() {
		try {
			return (SchoolBusiness) IBOLookup.getServiceInstance(this.getIWApplicationContext(), SchoolBusiness.class);
		}
		catch (RemoteException e) {
			throw new IBORuntimeException(e);
		}
	}

	private SchoolChoiceBusiness getSchoolChoiceBusiness() {
		try {
			return (SchoolChoiceBusiness) IBOLookup.getServiceInstance(this.getIWApplicationContext(), SchoolChoiceBusiness.class);
		}
		catch (RemoteException e) {
			throw new IBORuntimeException(e);
		}
	}

	private SchoolCommuneBusiness getSchoolCommuneBusiness() {
		try {
			return (SchoolCommuneBusiness) IBOLookup.getServiceInstance(this.getIWApplicationContext(), SchoolCommuneBusiness.class);
		}
		catch (RemoteException e) {
			throw new IBORuntimeException(e);
		}
	}

	private SchoolCommuneSession getSchoolSession() {
		try {
			return (SchoolCommuneSession) IBOLookup.getSessionInstance(this.getUserContext(), SchoolCommuneSession.class);
		}
		catch (RemoteException e) {
			throw new IBORuntimeException(e);
		}
	}

	private CommuneUserBusiness getUserBusiness() {
		try {
			return (CommuneUserBusiness) IBOLookup.getServiceInstance(this.getIWApplicationContext(), CommuneUserBusiness.class);
		}
		catch (RemoteException e) {
			throw new IBORuntimeException(e);
		}
	}
}