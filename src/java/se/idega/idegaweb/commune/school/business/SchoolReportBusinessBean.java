/*
 * Created on 17.12.2003
 */
package se.idega.idegaweb.commune.school.business;

import java.rmi.RemoteException;
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
	private final static String PREFIX = "school_report.";
	
	private IWBundle _iwb;
	private IWResourceBundle _iwrb;

	private void initializeBundlesIfNeeded() {
		if (_iwb == null) {
			_iwb = this.getIWApplicationContext().getApplication().getBundle(IW_BUNDLE_IDENTIFIER);
		}
		_iwrb = _iwb.getResourceBundle(this.getUserContext().getCurrentLocale());
	}
	
	public ReportableCollection getGroupReport(Collection schoolGroups, Collection columnNames) {
		initializeBundlesIfNeeded();
		Locale currentLocale = this.getUserContext().getCurrentLocale();
		
		ReportableCollection reportCollection = new ReportableCollection();
		
		reportCollection.addExtraHeaderParameter(
				"school_group_report",
				getLocalizedString("extra_information", "Extra information"),
				"label",
				"Mamaseika");
		
		ReportableField personalID = new ReportableField(FIELD_PERSONAL_ID, String.class);
		personalID.setLocalizedName(getLocalizedString("personal_id", "Personal ID"), currentLocale);
		reportCollection.addField(personalID);
		
		ReportableField name = new ReportableField(FIELD_NAME, String.class);
		name.setLocalizedName(getLocalizedString("name", "Name"), currentLocale);
		reportCollection.addField(name);
		
		ReportableField address = new ReportableField(FIELD_ADDRESS, String.class);
		address.setLocalizedName(getLocalizedString("address", "Address"), currentLocale);
		reportCollection.addField(address);
		
		ReportableField zipCode = new ReportableField(FIELD_ZIP_CODE, String.class);
		zipCode.setLocalizedName(getLocalizedString("zip_code", "Zip code"), currentLocale);
		reportCollection.addField(zipCode);
		
		ReportableField area = new ReportableField(FIELD_AREA, String.class);
		area.setLocalizedName(getLocalizedString("area", "Area"), currentLocale);
		reportCollection.addField(area);
		
		ReportableField email = new ReportableField(FIELD_EMAIL, String.class);
		email.setLocalizedName(getLocalizedString("email", "E-mail"), currentLocale);
		reportCollection.addField(email);
		
		ReportableField phone = new ReportableField(FIELD_PHONE, String.class);
		phone.setLocalizedName(getLocalizedString("phone", "Phone"), currentLocale);
		reportCollection.addField(phone);
		
		ReportableField gender = new ReportableField(FIELD_GENDER, String.class);
		gender.setLocalizedName(getLocalizedString("gender", "Gender"), currentLocale);
		reportCollection.addField(gender);
		
		ReportableField language = new ReportableField(FIELD_LANGUAGE, String.class);
		language.setLocalizedName(getLocalizedString("languge", "Language"), currentLocale);
		reportCollection.addField(language);
		
		ReportableField custodian = new ReportableField(FIELD_CUSTODIAN, String.class);
		custodian.setLocalizedName(getLocalizedString("custodian", "Custodian"), currentLocale);
		reportCollection.addField(custodian);
		
		ReportableField alternateAddress = new ReportableField(FIELD_ALTERNATE_ADDRESS, String.class);
		alternateAddress.setLocalizedName(getLocalizedString("alternate_address", "Alternate address"), currentLocale);
		reportCollection.addField(alternateAddress);
		
		ReportableField yearsWithLanguage = new ReportableField(FIELD_YEARS_WITH_LANGUAGE, String.class);
		yearsWithLanguage.setLocalizedName(getLocalizedString("years_with_language", "Years with language"), currentLocale);
		reportCollection.addField(yearsWithLanguage);
		
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
				data.addData(personalID, PersonalIDFormatter.format(user.getPersonalID(), currentLocale));
				data.addData(name, user.getNameLastFirst(true));
				if (address != null) {
					data.addData(address, homeAddress.getStreetAddress());
					PostalCode code = homeAddress.getPostalCode();
					if (code != null) {
						data.addData(zipCode, code.getPostalCode());
						data.addData(area, code.getPostalAddress());
					}
				}
				if (mail != null) {
					data.addData(email, mail.getEmailAddress());
				}
				if (homePhone != null) {
					data.addData(phone, homePhone.getNumber());
				}
				
				String genderString = null;
				if (PIDChecker.getInstance().isFemale(user.getPersonalID()))
					genderString = _iwrb.getLocalizedString("school.girl", "Girl");
				else
					genderString = _iwrb.getLocalizedString("school.boy", "Boy");
				data.addData(gender, genderString);
				
				if (student.getLanguage() != null) {
					data.addData(language, student.getLanguage());
				}
				
				if (parent != null) {
					data.addData(custodian, parent.getNameLastFirst(true));
					Address coAddress = getUserBusiness().getUsersCoAddress(parent);
					if (coAddress != null) {
						data.addData(alternateAddress, coAddress.getStreetAddress());
					}
				}
				data.addData(yearsWithLanguage, "1");
				
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
		initializeBundlesIfNeeded();
		Locale currentLocale = this.getUserContext().getCurrentLocale();
		
		ReportableCollection reportCollection = new ReportableCollection();
		
		reportCollection.addExtraHeaderParameter(
				"school_choice_report",
				getLocalizedString("extra_information", "Extra information"),
				"label",
				"Mamaseika");
		
		ReportableField personalID = new ReportableField(FIELD_PERSONAL_ID, String.class);
		personalID.setLocalizedName(getLocalizedString("personal_id", "Personal ID"), currentLocale);
		reportCollection.addField(personalID);
		
		ReportableField name = new ReportableField(FIELD_NAME, String.class);
		name.setLocalizedName(getLocalizedString("name", "Name"), currentLocale);
		reportCollection.addField(name);
		
		ReportableField address = new ReportableField(FIELD_ADDRESS, String.class);
		address.setLocalizedName(getLocalizedString("address", "Address"), currentLocale);
		reportCollection.addField(address);
		
		ReportableField zipCode = new ReportableField(FIELD_ZIP_CODE, String.class);
		zipCode.setLocalizedName(getLocalizedString("zip_code", "Zip code"), currentLocale);
		reportCollection.addField(zipCode);
		
		ReportableField area = new ReportableField(FIELD_AREA, String.class);
		area.setLocalizedName(getLocalizedString("area", "Area"), currentLocale);
		reportCollection.addField(area);
		
		ReportableField email = new ReportableField(FIELD_AREA, String.class);
		email.setLocalizedName(getLocalizedString("email", "E-mail"), currentLocale);
		reportCollection.addField(email);
		
		ReportableField phone = new ReportableField(FIELD_PHONE, String.class);
		phone.setLocalizedName(getLocalizedString("phone", "Phone"), currentLocale);
		reportCollection.addField(phone);
		
		ReportableField gender = new ReportableField(FIELD_GENDER, String.class);
		gender.setLocalizedName(getLocalizedString("gender", "Gender"), currentLocale);
		reportCollection.addField(gender);
		
		ReportableField fromSchool = new ReportableField(FIELD_FROM_SCHOOL, String.class);
		fromSchool.setLocalizedName(getLocalizedString("from_school", "From school"), currentLocale);
		reportCollection.addField(fromSchool);
		
		ReportableField applicationDate = new ReportableField(FIELD_APPLICATION_DATE, String.class);
		applicationDate.setLocalizedName(getLocalizedString("application_date", "From school"), currentLocale);
		reportCollection.addField(applicationDate);
		
		ReportableField message = new ReportableField(FIELD_MESSAGE, String.class);
		message.setLocalizedName(getLocalizedString("message", "Message"), currentLocale);
		reportCollection.addField(message);
		
		ReportableField language = new ReportableField(FIELD_LANGUAGE_CHOICE, String.class);
		language.setLocalizedName(getLocalizedString("languge_choice", "Language choice"), currentLocale);
		reportCollection.addField(language);
		
		ReportableField custodian = new ReportableField(FIELD_CUSTODIAN, String.class);
		custodian.setLocalizedName(getLocalizedString("custodian", "Custodian"), currentLocale);
		reportCollection.addField(custodian);
		
		ReportableField alternateAddress = new ReportableField(FIELD_ALTERNATE_ADDRESS, String.class);
		alternateAddress.setLocalizedName(getLocalizedString("alternate_address", "Alternate address"), currentLocale);
		reportCollection.addField(alternateAddress);
		
		ReportableField yearsWithLanguage = new ReportableField(FIELD_YEARS_WITH_LANGUAGE, String.class);
		yearsWithLanguage.setLocalizedName(getLocalizedString("years_with_language", "Years with language"), currentLocale);
		reportCollection.addField(yearsWithLanguage);
		
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
				data.addData(personalID, PersonalIDFormatter.format(user.getPersonalID(), currentLocale));
				data.addData(name, user.getNameLastFirst(true));
				if (address != null) {
					data.addData(address, homeAddress.getStreetAddress());
					PostalCode code = homeAddress.getPostalCode();
					if (code != null) {
						data.addData(zipCode, code.getPostalCode());
						data.addData(area, code.getPostalAddress());
					}
				}
				if (mail != null) {
					data.addData(email, mail.getEmailAddress());
				}
				if (homePhone != null) {
					data.addData(phone, homePhone.getNumber());
				}
				
				String genderString = null;
				if (PIDChecker.getInstance().isFemale(user.getPersonalID()))
					genderString = _iwrb.getLocalizedString("school.girl", "Girl");
				else
					genderString = _iwrb.getLocalizedString("school.boy", "Boy");
				data.addData(gender, genderString);
				
				if (choice.getLanguageChoice() != null) {
					data.addData(language, choice.getLanguageChoice());
				}
				
				School previousSchool = choice.getCurrentSchool();
				if (previousSchool != null) {
					data.addData(fromSchool, previousSchool.getSchoolName());
				}
				IWTimestamp stamp = new IWTimestamp(choice.getCreated());
				data.addData(applicationDate, stamp.getLocaleDate(currentLocale, IWTimestamp.SHORT));
				if (choice.getMessage() != null) {
					data.addData(message, choice.getMessage());
				}
				
				if (parent != null) {
					data.addData(custodian, parent.getNameLastFirst(true));
					Address coAddress = getUserBusiness().getUsersCoAddress(parent);
					if (coAddress != null) {
						data.addData(alternateAddress, coAddress.getStreetAddress());
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