/*
 * Created on 17.12.2003
 */
package se.idega.idegaweb.commune.school.business;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.ejb.FinderException;

import se.idega.idegaweb.commune.accounting.extra.business.ResourceBusiness;
import se.idega.idegaweb.commune.business.CommuneUserBusiness;
import se.idega.idegaweb.commune.school.data.SchoolChoice;
import se.idega.util.PIDChecker;

import com.idega.block.datareport.util.ReportableCollection;
import com.idega.block.datareport.util.ReportableData;
import com.idega.block.datareport.util.ReportableField;
import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.block.school.data.SchoolYear;
import com.idega.business.IBOLookup;
import com.idega.business.IBORuntimeException;
import com.idega.business.IBOSessionBean;
import com.idega.core.contact.data.Email;
import com.idega.core.contact.data.Phone;
import com.idega.core.localisation.data.ICLanguage;
import com.idega.core.location.data.Address;
import com.idega.core.location.data.PostalCode;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;
import com.idega.util.PersonalIDFormatter;
import com.idega.util.text.Name;

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
	private List _fields;
	
	private void initializeBundlesIfNeeded() {
		if (_iwb == null) {
			_iwb = this.getIWApplicationContext().getIWMainApplication().getBundle(IW_BUNDLE_IDENTIFIER);
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
		_fields = new ArrayList();
	}
	
	private boolean displayColumn(String columnName) {
		if (_columnsToDisplay.contains(columnName)) {
			return true;
		}
		return false;
	}

	public ReportableCollection getGroupReport(Collection schoolGroups, Collection columnNames, String freeText, Boolean showNativeLanguage, Boolean showTerminated) {
		return getGroupReport(schoolGroups, columnNames, freeText, showNativeLanguage, null, showTerminated);
	}
	
	public ReportableCollection getGroupReport(Collection schoolGroups, Collection columnNames, String freeText, Boolean showNativeLanguage, Boolean showSecondLanguage, Boolean showTerminated) {
		fillColumns(columnNames);
		initializeBundlesIfNeeded();
		Locale currentLocale = this.getUserContext().getCurrentLocale();
		Locale defaultLocale = this.getIWMainApplication().getSettings().getDefaultLocale();
		String nativeLanguageIDs = _iwb.getProperty(SchoolReportBusiness.PROPERTY_RESOURCE_IDS_NATIVE_LANGUAGE, "");
		String secondLanguageIDs = _iwb.getProperty(SchoolReportBusiness.PROPERTY_RESOURCE_IDS_SECOND_LANGUAGE, "");
		
		ReportableCollection reportCollection = new ReportableCollection();
		
		ReportableField personalID = new ReportableField(FIELD_PERSONAL_ID, String.class);
		personalID.setLocalizedName(getLocalizedString(FIELD_PERSONAL_ID, "Personal ID"), currentLocale);
		if (displayColumn(FIELD_PERSONAL_ID)) {
			_fields.add(personalID);
			reportCollection.addField(personalID);
		}
		
		ReportableField name = new ReportableField(FIELD_NAME, String.class);
		name.setLocalizedName(getLocalizedString(FIELD_NAME, "Name"), currentLocale);
		name.setMaxNumberOfCharacters(50);
		if (displayColumn(FIELD_NAME)) {
			_fields.add(name);
			reportCollection.addField(name);
		}
		
		ReportableField address = new ReportableField(FIELD_ADDRESS, String.class);
		address.setLocalizedName(getLocalizedString(FIELD_ADDRESS, "Address"), currentLocale);
		address.setMaxNumberOfCharacters(30);
		if (displayColumn(FIELD_ADDRESS)) {
			_fields.add(address);
			reportCollection.addField(address);
		}
		
		ReportableField zipCode = new ReportableField(FIELD_ZIP_CODE, String.class);
		zipCode.setLocalizedName(getLocalizedString(FIELD_ZIP_CODE, "Zip code"), currentLocale);
		if (displayColumn(FIELD_ZIP_CODE)) {
			_fields.add(zipCode);
			reportCollection.addField(zipCode);
		}
		
		ReportableField area = new ReportableField(FIELD_AREA, String.class);
		area.setLocalizedName(getLocalizedString(FIELD_AREA, "Area"), currentLocale);
		if (displayColumn(FIELD_AREA)) {
			_fields.add(area);
			reportCollection.addField(area);
		}
		
		ReportableField email = new ReportableField(FIELD_EMAIL, String.class);
		email.setLocalizedName(getLocalizedString(FIELD_EMAIL, "E-mail"), currentLocale);
		email.setMaxNumberOfCharacters(40);
		if (displayColumn(FIELD_EMAIL)) {
			_fields.add(email);
			reportCollection.addField(email);
		}
		
		ReportableField phone = new ReportableField(FIELD_PHONE, String.class);
		phone.setLocalizedName(getLocalizedString(FIELD_PHONE, "Phone"), currentLocale);
		if (displayColumn(FIELD_PHONE)) {
			_fields.add(phone);
			reportCollection.addField(phone);
		}
		
		ReportableField gender = new ReportableField(FIELD_GENDER, String.class);
		gender.setLocalizedName(getLocalizedString(FIELD_GENDER, "Gender"), currentLocale);
		if (displayColumn(FIELD_GENDER)) {
			_fields.add(gender);
			reportCollection.addField(gender);
		}
		
		ReportableField language = new ReportableField(FIELD_LANGUAGE, String.class);
		language.setLocalizedName(getLocalizedString(FIELD_LANGUAGE, "Language"), currentLocale);
		if (displayColumn(FIELD_LANGUAGE)) {
			_fields.add(language);
			reportCollection.addField(language);
		}
		
		ReportableField nativeLanguage = new ReportableField(FIELD_NATIVE_LANGUAGE, String.class);
		nativeLanguage.setLocalizedName(getLocalizedString(FIELD_NATIVE_LANGUAGE, "Native language"), currentLocale);
		if (displayColumn(FIELD_NATIVE_LANGUAGE)) {
			_fields.add(nativeLanguage);
			reportCollection.addField(nativeLanguage);
		}
		
		ReportableField swedishLanguage = new ReportableField(FIELD_SWEDISH_AS_SECOND_LANGUAGE, String.class);
		swedishLanguage.setLocalizedName(getLocalizedString(FIELD_SWEDISH_AS_SECOND_LANGUAGE, "Swedish as second language"), currentLocale);
		swedishLanguage.setMaxNumberOfCharacters(30);
		if (displayColumn(FIELD_SWEDISH_AS_SECOND_LANGUAGE)) {
			_fields.add(swedishLanguage);
			reportCollection.addField(swedishLanguage);
		}
		
		ReportableField terminationDate = new ReportableField(FIELD_TERMINATION_DATE, String.class);
		terminationDate.setLocalizedName(getLocalizedString(FIELD_TERMINATION_DATE, "Termination date"), currentLocale);
		if (displayColumn(FIELD_TERMINATION_DATE)) {
			_fields.add(terminationDate);
			reportCollection.addField(terminationDate);
		}
		
		ReportableField custodian = new ReportableField(FIELD_CUSTODIAN, String.class);
		custodian.setLocalizedName(getLocalizedString(FIELD_CUSTODIAN, "Custodian"), currentLocale);
		custodian.setMaxNumberOfCharacters(50);
		if (displayColumn(FIELD_CUSTODIAN)) {
			_fields.add(custodian);
			reportCollection.addField(custodian);
		}
		
		ReportableField alternateAddress = new ReportableField(FIELD_ALTERNATE_ADDRESS, String.class);
		alternateAddress.setLocalizedName(getLocalizedString(FIELD_ALTERNATE_ADDRESS, "Alternate address"), currentLocale);
		alternateAddress.setMaxNumberOfCharacters(30);
		if (displayColumn(FIELD_ALTERNATE_ADDRESS)) {
			_fields.add(alternateAddress);
			reportCollection.addField(alternateAddress);
		}
		
		ReportableField yearsWithLanguage = new ReportableField(FIELD_YEARS_WITH_LANGUAGE, String.class);
		yearsWithLanguage.setLocalizedName(getLocalizedString(FIELD_YEARS_WITH_LANGUAGE, "Years with language"), currentLocale);
		yearsWithLanguage.setMaxNumberOfCharacters(50);
		if (displayColumn(FIELD_YEARS_WITH_LANGUAGE)) {
			_fields.add(yearsWithLanguage);
			reportCollection.addField(yearsWithLanguage);
		}
		
		int numberOfStudents = 0;
		
		try {
			Collection students = getSchoolBusiness().getSchoolClassMemberHome().findBySchoolClasses(schoolGroups);
		
			Iterator iter = students.iterator();
			while (iter.hasNext()) {
				SchoolClassMember student = (SchoolClassMember) iter.next();
				boolean isRemoved = student.getRemovedDate() != null;
				if (showTerminated != null) {
					if (showTerminated.booleanValue() != isRemoved) {
						continue;
					}
				}
				boolean hasNativeLanguage = getResourceBusiness().hasResources(((Integer)student.getPrimaryKey()).intValue(), nativeLanguageIDs);
				if (showNativeLanguage != null) {
					if (showNativeLanguage.booleanValue() != hasNativeLanguage) {
						continue;
					}
				}
				boolean hasSecondLanguage = getResourceBusiness().hasResources(((Integer)student.getPrimaryKey()).intValue(), secondLanguageIDs);
				if (showSecondLanguage != null) {
					if (showSecondLanguage.booleanValue() != hasSecondLanguage) {
						continue;
					}
				}
				
				User user = student.getStudent();
				Address homeAddress = getUserBusiness().getUsersMainAddress(user);
				Phone homePhone = getUserBusiness().getChildHomePhone(user);
				User parent = getUserBusiness().getCustodianForChild(user);
				Email mail = null;
				if (parent != null) {
					mail = getUserBusiness().getEmail(parent);
				}
				
				ReportableData data = new ReportableData();
				if (displayColumn(FIELD_PERSONAL_ID)) {
					data.addData(personalID, PersonalIDFormatter.format(user.getPersonalID(), currentLocale));
				}
				if (displayColumn(FIELD_NAME)) {
					Name userName = new Name(user.getFirstName(), user.getMiddleName(), user.getLastName());
					data.addData(name, userName.getName(defaultLocale, true));
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
							data.addData(area, code.getName());
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
						data.addData(language, _iwrb.getLocalizedString(student.getLanguage(), student.getLanguage()));
					}
					else {
						data.addData(language, "-");
					}
				}
				
				if (displayColumn(FIELD_NATIVE_LANGUAGE)) {
					ICLanguage icl = user.getNativeLanguage();
					String nativeLanguageName = "";
					if (icl != null) {
						nativeLanguageName = icl.getName();
					}
					data.addData(nativeLanguage, nativeLanguageName);
				}
				
				if (displayColumn(FIELD_SWEDISH_AS_SECOND_LANGUAGE)) {
					data.addData(swedishLanguage, _iwrb.getLocalizedString(String.valueOf(hasSecondLanguage), String.valueOf(hasSecondLanguage)));
				}
				
				if (displayColumn(FIELD_TERMINATION_DATE)) {
					if (student.getRemovedDate() != null) {
						data.addData(terminationDate, new IWTimestamp(student.getRemovedDate()).getLocaleDate(currentLocale, IWTimestamp.SHORT));
					}
				}
				
				if (parent != null) {
					if (displayColumn(FIELD_CUSTODIAN)) {
						Name parentName = new Name(parent.getFirstName(), parent.getMiddleName(), parent.getLastName());
						data.addData(custodian, parentName.getName(defaultLocale, true));
					}

					if (displayColumn(FIELD_ALTERNATE_ADDRESS)) {
						Address coAddress = getUserBusiness().getUsersCoAddress(parent);
						if (coAddress != null) {
							data.addData(alternateAddress, coAddress.getStreetAddress());
						}
					}
				}
				if (displayColumn(FIELD_YEARS_WITH_LANGUAGE)) {
					if (student.getLanguage() != null) {
						SchoolYear year = student.getSchoolYear();
						if (year != null) {
							data.addData(yearsWithLanguage, String.valueOf(year.getSchoolYearAge() - 6));
						}
						else {
							data.addData(yearsWithLanguage, "-");
						}
					}
					else {
						data.addData(yearsWithLanguage, "-");
					}
				}
				
				reportCollection.add(data);
				numberOfStudents++;
			}
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
		
		ReportableData count = new ReportableData();
		count.addData((ReportableField)_fields.get(0), getLocalizedString("number_of_students", "Number of students:") + " " + String.valueOf(numberOfStudents));
		reportCollection.add(count);
		
		return reportCollection;
	}
	
	public ReportableCollection getChoicesReport(Collection columnNames, Boolean showLanguageChoice, Boolean showNativeLanguage) {
		fillColumns(columnNames);
		initializeBundlesIfNeeded();
		Locale currentLocale = this.getUserContext().getCurrentLocale();
		Locale defaultLocale = getIWMainApplication().getSettings().getDefaultLocale();
		
		ReportableCollection reportCollection = new ReportableCollection();
		
		ReportableField personalID = new ReportableField(FIELD_PERSONAL_ID, String.class);
		personalID.setLocalizedName(getLocalizedString(FIELD_PERSONAL_ID, "Personal ID"), currentLocale);
		if (displayColumn(FIELD_PERSONAL_ID)) {
			_fields.add(personalID);
			reportCollection.addField(personalID);
		}
		
		ReportableField name = new ReportableField(FIELD_NAME, String.class);
		name.setLocalizedName(getLocalizedString(FIELD_NAME, "Name"), currentLocale);
		if (displayColumn(FIELD_NAME)) {
			_fields.add(name);
			reportCollection.addField(name);
		}
		
		ReportableField address = new ReportableField(FIELD_ADDRESS, String.class);
		address.setLocalizedName(getLocalizedString(FIELD_ADDRESS, "Address"), currentLocale);
		if (displayColumn(FIELD_ADDRESS)) {
			_fields.add(address);
			reportCollection.addField(address);
		}
		
		ReportableField zipCode = new ReportableField(FIELD_ZIP_CODE, String.class);
		zipCode.setLocalizedName(getLocalizedString(FIELD_ZIP_CODE, "Zip code"), currentLocale);
		if (displayColumn(FIELD_ZIP_CODE)) {
			_fields.add(zipCode);
			reportCollection.addField(zipCode);
		}
		
		ReportableField area = new ReportableField(FIELD_AREA, String.class);
		area.setLocalizedName(getLocalizedString(FIELD_AREA, "Area"), currentLocale);
		if (displayColumn(FIELD_AREA)) {
			_fields.add(area);
			reportCollection.addField(area);
		}
		
		ReportableField email = new ReportableField(FIELD_EMAIL, String.class);
		email.setLocalizedName(getLocalizedString(FIELD_EMAIL, "E-mail"), currentLocale);
		if (displayColumn(FIELD_EMAIL)) {
			_fields.add(email);
			reportCollection.addField(email);
		}
		
		ReportableField phone = new ReportableField(FIELD_PHONE, String.class);
		phone.setLocalizedName(getLocalizedString(FIELD_PHONE, "Phone"), currentLocale);
		if (displayColumn(FIELD_PHONE)) {
			_fields.add(phone);
			reportCollection.addField(phone);
		}
		
		ReportableField gender = new ReportableField(FIELD_GENDER, String.class);
		gender.setLocalizedName(getLocalizedString(FIELD_GENDER, "Gender"), currentLocale);
		if (displayColumn(FIELD_GENDER)) {
			_fields.add(gender);
			reportCollection.addField(gender);
		}
		
		ReportableField fromSchool = new ReportableField(FIELD_FROM_SCHOOL, String.class);
		fromSchool.setLocalizedName(getLocalizedString(FIELD_FROM_SCHOOL, "From school"), currentLocale);
		if (displayColumn(FIELD_FROM_SCHOOL)) {
			_fields.add(fromSchool);
			reportCollection.addField(fromSchool);
		}
		
		ReportableField applicationDate = new ReportableField(FIELD_APPLICATION_DATE, String.class);
		applicationDate.setLocalizedName(getLocalizedString(FIELD_APPLICATION_DATE, "From school"), currentLocale);
		if (displayColumn(FIELD_APPLICATION_DATE)) {
			_fields.add(applicationDate);
			reportCollection.addField(applicationDate);
		}
		
		ReportableField message = new ReportableField(FIELD_MESSAGE, String.class);
		message.setLocalizedName(getLocalizedString(FIELD_MESSAGE, "Message"), currentLocale);
		if (displayColumn(FIELD_MESSAGE)) {
			_fields.add(message);
			reportCollection.addField(message);
		}
		
		ReportableField language = new ReportableField(FIELD_LANGUAGE_CHOICE, String.class);
		language.setLocalizedName(getLocalizedString(FIELD_LANGUAGE_CHOICE, "Language choice"), currentLocale);
		if (displayColumn(FIELD_LANGUAGE_CHOICE)) {
			_fields.add(language);
			reportCollection.addField(language);
		}
		
		ReportableField nativeLanguage = new ReportableField(FIELD_NATIVE_LANGUAGE, String.class);
		nativeLanguage.setLocalizedName(getLocalizedString(FIELD_NATIVE_LANGUAGE, "Native language"), currentLocale);
		if (displayColumn(FIELD_NATIVE_LANGUAGE)) {
			_fields.add(nativeLanguage);
			reportCollection.addField(nativeLanguage);
		}
		
		ReportableField custodian = new ReportableField(FIELD_CUSTODIAN, String.class);
		custodian.setLocalizedName(getLocalizedString(FIELD_CUSTODIAN, "Custodian"), currentLocale);
		if (displayColumn(FIELD_CUSTODIAN)) {
			_fields.add(custodian);
			reportCollection.addField(custodian);
		}
		
		ReportableField alternateAddress = new ReportableField(FIELD_ALTERNATE_ADDRESS, String.class);
		alternateAddress.setLocalizedName(getLocalizedString(FIELD_ALTERNATE_ADDRESS, "Alternate address"), currentLocale);
		if (displayColumn(FIELD_ALTERNATE_ADDRESS)) {
			_fields.add(alternateAddress);
			reportCollection.addField(alternateAddress);
		}
		
		int numberOfChoices = 0;

		try {
			String[] validStatuses = new String[] { getSchoolChoiceBusiness().getCaseStatusPreliminary().getStatus(), getSchoolChoiceBusiness().getCaseStatusMoved().getStatus(), getSchoolChoiceBusiness().getCaseStatusPlaced().getStatus() };
			int schoolYearAge = getSchoolCommuneBusiness().getGradeForYear(getSchoolSession().getSchoolYearID());
			if (!getSchoolCommuneBusiness().isOngoingSeason(getSchoolSession().getSchoolSeasonID()))
				schoolYearAge--;
			Collection applicants = getSchoolChoiceBusiness().getApplicantsForSchool(getSchoolSession().getSchoolID(), getSchoolSession().getSchoolSeasonID(), schoolYearAge, validStatuses, null, SchoolChoiceComparator.NAME_SORT, -1, -1);
			
			Iterator iter = applicants.iterator();
			while (iter.hasNext()) {
				SchoolChoice choice = (SchoolChoice) iter.next();
				boolean hasLanguageChoice = choice.getLanguageChoice() != null;
				if (showLanguageChoice != null) {
					if (showNativeLanguage.booleanValue() != hasLanguageChoice) {
						continue;
					}
				}
				User user = choice.getChild();
				boolean hasNativeLanguage = user.getNativeLanguage() != null;
				if (showNativeLanguage != null) {
					if (showNativeLanguage.booleanValue() != hasNativeLanguage) {
						continue;
					}
				}
				Address homeAddress = getUserBusiness().getUsersMainAddress(user);
				Phone homePhone = getUserBusiness().getChildHomePhone(user);
				User parent = getUserBusiness().getCustodianForChild(user);
				Email mail = null;
				if (parent != null) {
					mail = getUserBusiness().getEmail(parent);
				}
				
				ReportableData data = new ReportableData();
				if (displayColumn(FIELD_PERSONAL_ID)) {
					data.addData(personalID, PersonalIDFormatter.format(user.getPersonalID(), currentLocale));
				}
				if (displayColumn(FIELD_NAME)) {
					Name userName = new Name(user.getFirstName(), user.getMiddleName(), user.getLastName());
					data.addData(name, userName.getName(defaultLocale, true));
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
							data.addData(area, code.getName());
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
						data.addData(language, _iwrb.getLocalizedString(choice.getLanguageChoice(), choice.getLanguageChoice()));
					}
				}
				
				if (displayColumn(FIELD_NATIVE_LANGUAGE)) {
					if (user.getNativeLanguage() != null) {
						data.addData(nativeLanguage, user.getNativeLanguage().getName());
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
						Name parentName = new Name(parent.getFirstName(), parent.getMiddleName(), parent.getLastName());
						data.addData(custodian, parentName.getName(defaultLocale, true));
					}

					if (displayColumn(FIELD_ALTERNATE_ADDRESS)) {
						Address coAddress = getUserBusiness().getUsersCoAddress(parent);
						if (coAddress != null) {
							data.addData(alternateAddress, coAddress.getStreetAddress());
						}
					}
				}
				
				reportCollection.add(data);
				numberOfChoices++;
			}
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		
		ReportableData count = new ReportableData();
		count.addData((ReportableField)_fields.get(0), getLocalizedString("number_of_choices", "Number of choices:") + " " +  String.valueOf(numberOfChoices));
		reportCollection.add(count);
		
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

	private ResourceBusiness getResourceBusiness() {
		try {
			return (ResourceBusiness) IBOLookup.getServiceInstance(this.getIWApplicationContext(), ResourceBusiness.class);
		}
		catch (RemoteException e) {
			throw new IBORuntimeException(e);
		}
	}
}