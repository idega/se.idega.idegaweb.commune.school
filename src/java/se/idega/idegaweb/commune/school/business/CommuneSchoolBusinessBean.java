/*
 * $Id: CommuneSchoolBusinessBean.java,v 1.27 2006/03/22 09:12:31 laddi Exp $
 * Created on Aug 3, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.school.business;

import is.idega.block.family.business.NoCustodianFound;

import java.rmi.RemoteException;
import java.sql.Date;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.transaction.UserTransaction;

import se.idega.idegaweb.commune.business.CommuneUserBusiness;
import se.idega.idegaweb.commune.message.business.CommuneMessageBusiness;
import se.idega.idegaweb.commune.school.data.SchoolChoice;
import se.idega.idegaweb.commune.school.data.SchoolChoiceHome;

import com.idega.block.process.business.CaseBusiness;
import com.idega.block.process.business.CaseBusinessBean;
import com.idega.block.process.data.Case;
import com.idega.block.process.data.CaseStatus;
import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolClass;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.block.school.data.SchoolDistrict;
import com.idega.block.school.data.SchoolSeason;
import com.idega.block.school.data.SchoolUser;
import com.idega.block.school.data.SchoolYear;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.core.location.data.Address;
import com.idega.core.location.data.Commune;
import com.idega.core.location.data.PostalCode;
import com.idega.data.IDOCreateException;
import com.idega.data.IDOException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.user.data.User;
import com.idega.util.Age;
import com.idega.util.IWTimestamp;
import com.idega.util.PersonalIDFormatter;


/**
 * Last modified: $Date: 2006/03/22 09:12:31 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.27 $
 */
public class CommuneSchoolBusinessBean extends CaseBusinessBean  implements CaseBusiness, CommuneSchoolBusiness{

	public static final String METADATA_CAN_DISPLAY_SCHOOL_IMAGE = "can_display_school_image";
	public static final String METADATA_CAN_DISPLAY_AFTER_SCHOOL_CARE_IMAGE = "can_display_after_school_care_image";
	public static final String METADATA_OTHER_INFORMATION = "after_school_care_information";
	public static final String METADATA_CAN_PARTICIPATE_IN_CHURCH_RECREATION = "can_participate_in_church_recreation";
	public static final String METADATA_CAN_CONTACT_ELEMENTARY_SCHOOL_FOR_INFORMATION = "can_contact_elementary_school_for_information";
	
	private static final String PROPERTY_DEFAULT_SCHOOL_TYPE = "default_school_type";

//	public AfterSchoolBusiness getAfterSchoolBusiness() {
//		try {
//			return (AfterSchoolBusiness) this.getServiceInstance(AfterSchoolBusiness.class);
//		}
//		catch (RemoteException e) {
//			throw new IBORuntimeException(e.getMessage());
//		}
//	}
	
	private CommuneUserBusiness getUserBusiness() {
		try {
			return (CommuneUserBusiness) this.getServiceInstance(CommuneUserBusiness.class);
		}
		catch (RemoteException e) {
			throw new IBORuntimeException(e.getMessage());
		}
	}

	private SchoolBusiness getSchoolBusiness() {
		try {
			return (SchoolBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), SchoolBusiness.class);
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}
	
	private CommuneMessageBusiness getMessageBusiness() {
		try {
			return (CommuneMessageBusiness) this.getServiceInstance(CommuneMessageBusiness.class);
		}
		catch (RemoteException e) {
			throw new IBORuntimeException(e.getMessage());
		}
	}

	private SchoolChoiceHome getSchoolChoiceHome() {
		try {
			return (SchoolChoiceHome) IDOLookup.getHome(SchoolChoice.class);
		}
		catch (IDOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}
	
	public boolean canDisplaySchoolImages(User child) {
		String meta = child.getMetaData(METADATA_CAN_DISPLAY_SCHOOL_IMAGE);
		if (meta != null) {
			return new Boolean(meta).booleanValue();
		}
		return false;
	}
	
	public Boolean canDisplayAfterSchoolCareImages(User child) {
		String meta = child.getMetaData(METADATA_CAN_DISPLAY_AFTER_SCHOOL_CARE_IMAGE);
		if (meta != null) {
			return new Boolean(meta);
		}
		return null;
	}
	
	public boolean canParticipateInChurchRecreation(User child) {
		String meta = child.getMetaData(METADATA_CAN_PARTICIPATE_IN_CHURCH_RECREATION);
		if (meta != null && meta.length() > 0) {
			return new Boolean(meta).booleanValue();
		}
		return true;
	}
	
	public boolean canContactElementarySchoolForInformation(User child) {
		String meta = child.getMetaData(METADATA_CAN_CONTACT_ELEMENTARY_SCHOOL_FOR_INFORMATION);
		if (meta != null && meta.length() > 0) {
			return new Boolean(meta).booleanValue();
		}
		return true;
	}
	
	public String getAfterSchoolCareOtherInformation(User child) {
		return child.getMetaData(METADATA_OTHER_INFORMATION);
	}
	
	public Collection getSchoolYearsInSchool(School school) {
		try {
			return school.findRelatedSchoolYearsSortedByName();
		}
		catch (IDOException ie) {
			ie.printStackTrace();
			return new ArrayList();
		}
	}
	
	public School getHomeSchoolForUser(User user) {
		try {
			Address address = getUserBusiness().getUsersMainAddress(user);
			if (address != null) {
				PostalCode code = address.getPostalCode();
				if (code != null) {
					Commune commune = code.getCommune();
					if (commune != null && commune.getIsDefault()) {
						return getHomeSchoolForAddress(user, address);
					}
				}
			}
			return null;
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}
	
	public boolean hasSchoolPlacing(User user, SchoolSeason season) {
		try {
			return getSchoolBusiness().getSchoolClassMemberHome().getNumberOfPlacingsBySeasonAndSchoolCategory(user, season, getSchoolBusiness().getCategoryElementarySchool()) > 0;
		}
		catch (IDOException ie) {
			ie.printStackTrace();
			return false;
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}
	
	public boolean hasSchoolChoice(User user, SchoolSeason season) {
		try {
			String[] statuses = { getCaseStatusDeleted().getStatus(), getCaseStatusDenied().getStatus(), getCaseStatusInactive().getStatus() };
			return getSchoolChoiceHome().getNumberOfChoices(new Integer(user.getPrimaryKey().toString()).intValue(), new Integer(season.getPrimaryKey().toString()).intValue(), statuses) > 0;
		}
		catch (IDOException ie) {
			ie.printStackTrace();
			return false;
		}
	}
	
	public boolean hasAfterSchoolCarePlacing(User user, SchoolSeason season) {
		try {
			return getSchoolBusiness().getSchoolClassMemberHome().getNumberOfPlacingsBySeasonAndSchoolCategory(user, season, getSchoolBusiness().getCategoryChildcare()) > 0;
		}
		catch (IDOException ie) {
			ie.printStackTrace();
			return false;
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}
	
	public SchoolClassMember getSchoolPlacing(User user, SchoolSeason season) {
		try {
			return getSchoolBusiness().getSchoolClassMemberHome().findLatestByUserAndSchCategoryAndSeason(user, getSchoolBusiness().getCategoryElementarySchool(), season);
		}
		catch (FinderException fe) {
			fe.printStackTrace();
			return null;
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}
	
	public School getAfterSchoolProviderForPlacement(SchoolClassMember placement) {
		SchoolClass group = placement.getSchoolClass();
		School school = group.getSchool();
		return school.getAfterSchoolCareProvider();
	}
	
	public boolean hasHomeSchool(User user) {
		School school = getHomeSchoolForUser(user);
		return school != null;
	}
	
	public School getHomeSchoolForAddress(User user, Address address) {
		try {
			SchoolDistrict district = getSchoolBusiness().getSchoolDistrictHome().findByAddress(address.getStreetAddress());
			SchoolYear year = null;
			try {
				year = getSchoolYearForUser(user);
			}
			catch (FinderException fe) {
				//No year found for age;
			}
			
			if (year == null) {
				return district.getSchool();
			}
			else {
				School school = district.getSchool();
				if (getSchoolBusiness().hasSchoolRelationToYear(school, year)) {
					return school;
				}
				else {
					return school.getJuniorHighSchool();
				}
			}
		}
		catch (FinderException fe) {
			fe.printStackTrace();
			return null;
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}
	
	public SchoolClass getDefaultGroup(Object schoolPK, Object seasonPK, Object yearPK) {
		try {
			School school = getSchoolBusiness().getSchool(schoolPK);
			SchoolSeason season = getSchoolBusiness().getSchoolSeason(seasonPK);
			SchoolYear year = getSchoolBusiness().getSchoolYear(yearPK);
			return getDefaultGroup(school, season, year);
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}
	
	public SchoolClass getDefaultGroup(School school, SchoolSeason season, SchoolYear year) {
		try {
			try {
				Collection groups = getSchoolBusiness().getSchoolClassHome().findBySchoolAndSeasonAndYear(school, season, year);
				if (!groups.isEmpty()) {
					Iterator iter = groups.iterator();
					while (iter.hasNext()) {
						return (SchoolClass) iter.next();
					}
				}
				throw new FinderException();
			}
			catch (FinderException fe) {
				try {
					SchoolClass group = getSchoolBusiness().getSchoolClassHome().create();
					group.setSchool(school);
					group.setSchoolSeason(season);
					group.setValid(true);
					group.setSchoolClassName(year.getName());
					group.store();
					
					try {
						group.addSchoolYear(year);
					}
					catch (IDOException ie) {
						ie.printStackTrace();
					}
					
					return group;
				}
				catch (CreateException ce) {
					throw new IBORuntimeException(ce);
				}
			}
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}
	
	public Collection getSchoolYears() {
		try {
			return getSchoolBusiness().findSchoolYearsBySchoolCategory(getSchoolBusiness().getCategoryElementarySchool().getCategory());
		}
		catch (FinderException fe) {
			fe.printStackTrace();
			return new ArrayList();
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}
	
	public boolean approveApplications(Object[] pks, School school, SchoolSeason season, String subject, String body, User performer) {
		UserTransaction trans = getSessionContext().getUserTransaction();

		try {
			trans.begin();
			
			String stID = getSchoolBusiness().getPropertyValue(PROPERTY_DEFAULT_SCHOOL_TYPE);
			int schoolTypeID = -1;
			if (stID == null) {
				schoolTypeID = new Integer(this.getBundle().getProperty(PROPERTY_DEFAULT_SCHOOL_TYPE, "-1")).intValue();
				getSchoolBusiness().setProperty(PROPERTY_DEFAULT_SCHOOL_TYPE, Integer.toString(schoolTypeID));
			}
			else {
				schoolTypeID = Integer.parseInt(stID);
			}
			
			IWTimestamp timeNow = new IWTimestamp();
			
			for (int i = 0; i < pks.length; i++) {
				Object object = pks[i];
				SchoolChoice choice = getSchoolChoice(object);
				SchoolYear year = choice.getSchoolYear();
				User child = choice.getChild();
				

				SchoolClass group = getDefaultGroup(school, season, year);
				
				SchoolClassMember student = getSchoolBusiness().storeSchoolClassMember(group, child);
				student.setSchoolYear(year);
				student.setNotes(choice.getMessage());
				student.setSchoolTypeId(schoolTypeID);
				student.setRegistrationCreatedDate(timeNow.getTimestamp());
				student.setRegisterDate(choice.getPlacementDate() != null ? new IWTimestamp(choice.getPlacementDate()).getTimestamp() : new IWTimestamp(season.getSchoolSeasonStart()).getTimestamp());
				student.store();
				
				Map attributes = new HashMap();
				attributes.put("schoolPlacement",student);
				
				changeCaseStatus(choice, getCaseStatusPlaced().getStatus(), performer,attributes);
				sendMessageToParents(choice, subject, body);
				
				//onApplicationApproval(choice,student);
				
			}
			
			trans.commit();

			return true;
		}
		catch (Exception ex) {
			try {
				trans.rollback();
			}
			catch (javax.transaction.SystemException e) {
				e.printStackTrace();
			}
			ex.printStackTrace();
		}
		
		return false;
	}

	public boolean rejectApplications(Object[] pks, String subject, String body, User performer) {
		UserTransaction trans = getSessionContext().getUserTransaction();

		try {
			trans.begin();
			
			for (int i = 0; i < pks.length; i++) {
				Object object = pks[i];
				SchoolChoice choice = getSchoolChoice(object);
				
				sendMessageToParents(choice, subject, body);

				changeCaseStatus(choice, getCaseStatusDenied().getStatus(), performer);
				
				Collection children = choice.getChildren();
				if (children != null && !children.isEmpty()) {
					Iterator iter = children.iterator();
					while (iter.hasNext()) {
						Case element = (Case) iter.next();
						if (element.getCode().equals("MBSKOLV")) {
							changeCaseStatus(element, getCaseStatusPreliminary().getStatus(), performer);
							break;
						}
					}
				}
			}
			
			trans.commit();

			return true;
		}
		catch (Exception ex) {
			try {
				trans.rollback();
			}
			catch (javax.transaction.SystemException e) {
				e.printStackTrace();
			}
			ex.printStackTrace();
		}
		
		return false;
	}
	
	public boolean saveHomeSchoolChoice(User user, User child, Object schoolPK, Object seasonPK, Object yearPK, String language, String message) throws IDOCreateException {
		UserTransaction trans = getSessionContext().getUserTransaction();

		try {
			trans.begin();
			
			String stID = getSchoolBusiness().getPropertyValue(PROPERTY_DEFAULT_SCHOOL_TYPE);
			int schoolTypeID = -1;
			if (stID == null) {
				schoolTypeID = new Integer(this.getBundle().getProperty(PROPERTY_DEFAULT_SCHOOL_TYPE, "-1")).intValue();
				getSchoolBusiness().setProperty(PROPERTY_DEFAULT_SCHOOL_TYPE, Integer.toString(schoolTypeID));
			} else {
				schoolTypeID = Integer.parseInt(stID);
			}
			
			IWTimestamp timeNow = new IWTimestamp();
			SchoolChoice choice = saveChoice(user, child, schoolPK, seasonPK, yearPK, schoolTypeID, language, message, null, getCaseStatusPlaced(), null, 1, timeNow);
			String subject = getLocalizedString("application.home_school_choice_received_subject", "Home school choice received");
			String body = getLocalizedString("application.home_school_choice_received_body", "{1} has received the application for a school placing for {0}, {2}.  The application has been handled and your child has a placing at the school.");
			sendMessageToParents(choice, subject, body);

			SchoolClass group = getDefaultGroup(schoolPK, seasonPK, yearPK);
			
			SchoolClassMember student = getSchoolBusiness().storeSchoolClassMember(group, child);
			student.setSchoolYear(yearPK);
			student.setLanguage(language);
			student.setNotes(message);
			student.setSchoolTypeId(schoolTypeID);
			student.store();
			
			trans.commit();

			return true;
		}
		catch (Exception ex) {
			try {
				trans.rollback();
			}
			catch (javax.transaction.SystemException e) {
				throw new IDOCreateException(e.getMessage());
			}
			ex.printStackTrace();
			throw new IDOCreateException(ex);
		}
	}
	
	public Collection getChoices(User child, SchoolSeason season) {
		try {
			String[] statuses = { getCaseStatusPlaced().getStatus(), getCaseStatusDeleted().getStatus() };
			return getSchoolChoiceHome().findByChildAndSeason(((Integer) child.getPrimaryKey()).intValue(), ((Integer) season.getPrimaryKey()).intValue(), statuses);
		}
		catch (FinderException fe) {
			fe.printStackTrace();
			return new ArrayList();
		}
	}
	
	public Collection getAllChoices(User child, SchoolSeason season) {
		try {
			String[] statuses = { getCaseStatusDeleted().getStatus() };
			return getSchoolChoiceHome().findByChildAndSeason(((Integer) child.getPrimaryKey()).intValue(), ((Integer) season.getPrimaryKey()).intValue(), statuses);
		}
		catch (FinderException fe) {
			fe.printStackTrace();
			return new ArrayList();
		}
	}
	
	public SchoolChoice getChoice(User child, SchoolSeason season, int choiceNumber) throws FinderException {
		return getSchoolChoiceHome().findByChildAndChoiceNumberAndSeason(child, choiceNumber, season);
	}
	
	public SchoolChoice getSchoolChoice(Object primaryKey) throws FinderException {
		return getSchoolChoiceHome().findByPrimaryKey(primaryKey);
	}
	
	public SchoolChoice saveChoices(User user, User child, Collection schools, Object seasonPK, Object yearPK, String language, String message, Date placementDate) throws IDOCreateException {
		UserTransaction trans = getSessionContext().getUserTransaction();

		try {
			trans.begin();
			
			CaseStatus first = super.getCaseStatusPreliminary();
			CaseStatus other = super.getCaseStatusInactive();
			CaseStatus status = null;
			SchoolChoice choice = null;
			int choiceNumber = 1;
			String stID = getSchoolBusiness().getPropertyValue(PROPERTY_DEFAULT_SCHOOL_TYPE);
			int schoolTypeID = -1;
			if (stID == null) {
				schoolTypeID = new Integer(this.getBundle().getProperty(PROPERTY_DEFAULT_SCHOOL_TYPE, "-1")).intValue();
				getSchoolBusiness().setProperty(PROPERTY_DEFAULT_SCHOOL_TYPE, Integer.toString(schoolTypeID));
			} else {
				schoolTypeID = Integer.parseInt(stID);
			}
			SchoolChoice firstChoice = null;
			
			IWTimestamp timeNow = new IWTimestamp();
			int i = 0;
			Iterator iter = schools.iterator();
			while (iter.hasNext()) {
				Object element = iter.next();
				if (element != null && Integer.parseInt(element.toString()) > 0) {
					if (i == 0) {
						status = first;
					}
					else {
						status = other;
					}
					
					choice = saveChoice(user, child, element, seasonPK, yearPK, schoolTypeID, language, message, placementDate, status, choice, choiceNumber++, timeNow);
					if (firstChoice == null) {
						firstChoice = choice;
					}
					i++;
				}
			}
			trans.commit();

			return firstChoice;
		}
		catch (Exception ex) {
			try {
				trans.rollback();
			}
			catch (javax.transaction.SystemException e) {
				throw new IDOCreateException(e.getMessage());
			}
			ex.printStackTrace();
			throw new IDOCreateException(ex);
		}
	}

	private SchoolChoice saveChoice(User user, User child, Object schoolPK, Object seasonPK, Object yearPK, int schoolTypeID, String language, String message, Date placementDate, CaseStatus status, Case parentCase, int choiceNumber, IWTimestamp stamp) throws IDOCreateException {
		SchoolSeason season = null;
		try {
			season = getSchoolBusiness().getSchoolSeason(new Integer(seasonPK.toString()));
			
			if (placementDate != null) {
				try {
					SchoolSeason placementSeason = getSchoolBusiness().getSchoolSeasonHome().findSeasonByDate(getSchoolBusiness().getCategoryElementarySchool(), placementDate);
					season = placementSeason;
				}
				catch (FinderException fe) {
					log(fe);
					
					try {
						SchoolSeason placementSeason = getSchoolBusiness().getSchoolSeasonHome().findNextSeason(getSchoolBusiness().getCategoryElementarySchool(), placementDate);
						season = placementSeason;
					}
					catch (FinderException fe1) {
						log(fe1);
					}
				}
			}
		}
		catch (RemoteException fe) {
			season = null;
		}
		SchoolChoice choice = null;
		stamp.addSeconds(1 - choiceNumber);

		if (season != null) {
			try {
				choice = getSchoolChoiceHome().findByChildAndChoiceNumberAndSeason(child, choiceNumber, season);
			}
			catch (FinderException fex) {
				choice = null;
			}
		}
		if (choice == null) {
			try {
				choice = getSchoolChoiceHome().create();
			}
			catch (CreateException ce) {
				throw new IDOCreateException(ce);
			}
		}
		choice.setOwner(user);
		choice.setChild(child);
		choice.setSchool(schoolPK);
		choice.setSchoolSeason(season);
		choice.setSchoolYear(yearPK);
		choice.setSchoolTypeId(schoolTypeID);
		choice.setChoiceOrder(choiceNumber);
		choice.setSchoolChoiceDate(stamp.getTimestamp());
		choice.setLanguageChoice(language);
		choice.setMessage(message);
		choice.setPlacementDate(placementDate);
		
		choice.setCreated(stamp.getTimestamp());
		choice.setCaseStatus(status);
		choice.setParentCase(parentCase);
		choice.store();
		
		if (status.equals(getCaseStatusPreliminary())) {
			String subject = getLocalizedString("application.choice_received_subject", "School choice received");
			String body = getLocalizedString("application.choice_received_body", "{1} has received the application for a school placing for {0}, {2}.  The application will be handled as soon as possible.");
			sendMessageToParents(choice, subject, body);
		}

		return choice;
	}
	
	public void storeChildSchoolInformation(User child, boolean canDisplayImage) {
		child.setMetaData(METADATA_CAN_DISPLAY_SCHOOL_IMAGE, String.valueOf(canDisplayImage));
		child.store();
	}

	public void storeChildAfterSchoolCareInformation(User child, boolean canDisplayImage, boolean canParticipateInChurchRecreation, boolean canContactElementarySchoolForInformation, String otherAfterSchoolCareInformation) {
		child.setMetaData(METADATA_CAN_DISPLAY_AFTER_SCHOOL_CARE_IMAGE, String.valueOf(canDisplayImage));
		child.setMetaData(METADATA_CAN_PARTICIPATE_IN_CHURCH_RECREATION, String.valueOf(canParticipateInChurchRecreation));
		child.setMetaData(METADATA_CAN_CONTACT_ELEMENTARY_SCHOOL_FOR_INFORMATION, String.valueOf(canContactElementarySchoolForInformation));
		child.setMetaData(METADATA_OTHER_INFORMATION, otherAfterSchoolCareInformation);
		child.store();
	}

	private void sendMessageToParents(SchoolChoice choice, String subject, String body) {
		sendMessageToParents(choice, subject, body, body, false);
	}

	private void sendMessageToParents(SchoolChoice application, String subject, String body, String letterBody, boolean alwaysSendLetter) {
		try {
			User child = application.getChild();
			Object[] arguments = {child.getName(), application.getChosenSchool().getSchoolName(), PersonalIDFormatter.format(child.getPersonalID(), getIWApplicationContext().getApplicationSettings().getDefaultLocale()) };

			User appParent = application.getOwner();
			if (getUserBusiness().getMemberFamilyLogic().isChildInCustodyOf(child, appParent)) {
				getMessageBusiness().createUserMessage(application, appParent, subject, MessageFormat.format(body, arguments), MessageFormat.format(letterBody, arguments), true, alwaysSendLetter);
			}

			try {
				Collection parents = getUserBusiness().getMemberFamilyLogic().getCustodiansFor(child);
				Iterator iter = parents.iterator();
				while (iter.hasNext()) {
					User parent = (User) iter.next();
					if (!getUserBusiness().haveSameAddress(parent, appParent)) {
						getMessageBusiness().createUserMessage(application, parent, subject, MessageFormat.format(body, arguments), MessageFormat.format(letterBody, arguments), true, alwaysSendLetter);
					}
				}
			}
			catch (NoCustodianFound ncf) {
				getMessageBusiness().createUserMessage(application, child, subject, MessageFormat.format(body, arguments), MessageFormat.format(letterBody, arguments), true, alwaysSendLetter);
			}
		}
		catch (RemoteException re) {
			re.printStackTrace();
		}
	}
	
	public void sendMessageToProvider(SchoolChoice application, String subject, String message, Locale locale, User sender) {
		try {
			Collection users = getSchoolBusiness().getSchoolUsers(application.getChosenSchool());
			User child = application.getChild();
			Object[] arguments = { child.getName(), application.getChosenSchool().getSchoolName(), application.getPlacementDate() != null ? new IWTimestamp(application.getPlacementDate()).getLocaleDate(locale, IWTimestamp.SHORT) : "", PersonalIDFormatter.format(child.getPersonalID(), locale), application.getSchoolSeason().getSchoolSeasonName() };

			if (users != null) {
				CommuneMessageBusiness messageBiz = getMessageBusiness();
				Iterator it = users.iterator();
				while (it.hasNext()) {
					SchoolUser providerUser = (SchoolUser) it.next();
					User user = providerUser.getUser();
					messageBiz.createUserMessage(application, user, sender, subject, MessageFormat.format(message, arguments), false);
				}
			}
		}
		catch (RemoteException re) {
			re.printStackTrace();
		}
	}

	public void sendMessageToProvider(SchoolChoice application, String subject, String message, Locale locale) {
		sendMessageToProvider(application, subject, message, locale, null);
	}

	public SchoolYear getSchoolYearForUser(User user) throws FinderException {
		try {
			if (user.getDateOfBirth() == null) {
				throw new FinderException();
			}
			IWTimestamp stamp = new IWTimestamp(user.getDateOfBirth());
			stamp.setDay(1);
			stamp.setMonth(1);
			
			Age age = new Age(stamp.getDate());
			return getSchoolBusiness().getSchoolYearHome().findByAge(getSchoolBusiness().getCategoryElementarySchool(), age.getYears());
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}
}