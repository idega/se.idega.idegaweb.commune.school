/*
 * $Id: CommuneSchoolBusinessBean.java,v 1.4 2005/08/10 15:10:27 thomas Exp $
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
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.transaction.UserTransaction;
import se.idega.idegaweb.commune.business.CommuneUserBusiness;

import se.idega.idegaweb.commune.message.business.MessageBusiness;
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
import com.idega.block.school.data.SchoolYear;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.core.location.data.Address;
import com.idega.data.IDOCreateException;
import com.idega.data.IDOException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;
import com.idega.util.PersonalIDFormatter;


/**
 * Last modified: $Date: 2005/08/10 15:10:27 $ by $Author: thomas $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.4 $
 */
public class CommuneSchoolBusinessBean extends CaseBusinessBean  implements CaseBusiness, CommuneSchoolBusiness{

	public static final String METADATA_CAN_DISPLAY_SCHOOL_IMAGE = "can_display_school_image";
	public static final String METADATA_CAN_DISPLAY_AFTER_SCHOOL_CARE_IMAGE = "can_display_after_school_care_image";
	public static final String METADATA_OTHER_INFORMATION = "after_school_care_information";
	
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
	
	private MessageBusiness getMessageBusiness() {
		try {
			return (MessageBusiness) this.getServiceInstance(MessageBusiness.class);
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
	
	public boolean canDisplayAfterSchoolCareImages(User child) {
		String meta = child.getMetaData(METADATA_CAN_DISPLAY_AFTER_SCHOOL_CARE_IMAGE);
		if (meta != null) {
			return new Boolean(meta).booleanValue();
		}
		return false;
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
				return getHomeSchoolForAddress(address);
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
	
	public School getHomeSchoolForAddress(Address address) {
		try {
			SchoolDistrict district = getSchoolBusiness().getSchoolDistrictHome().findByAddress(address.getStreetAddress());
			return district.getSchool();
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
					group.setSchoolClassName(season.getName() + " - " + year.getName());
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
	
	public boolean saveHomeSchoolChoice(User user, User child, Object schoolPK, Object seasonPK, Object yearPK, String language, String message) throws IDOCreateException {
		UserTransaction trans = getSessionContext().getUserTransaction();

		try {
			trans.begin();
			
			int schoolTypeID = new Integer(this.getBundle().getProperty(PROPERTY_DEFAULT_SCHOOL_TYPE, "-1")).intValue();
			
			IWTimestamp timeNow = new IWTimestamp();
			saveChoice(user, child, schoolPK, seasonPK, yearPK, schoolTypeID, language, message, getCaseStatusPlaced(), null, 1, timeNow);
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
	
	public boolean saveChoices(User user, User child, Collection schools, Object seasonPK, Object yearPK, String language, String message) throws IDOCreateException {
		UserTransaction trans = getSessionContext().getUserTransaction();

		try {
			trans.begin();
			
			CaseStatus first = super.getCaseStatusPreliminary();
			CaseStatus other = super.getCaseStatusInactive();
			CaseStatus status = null;
			SchoolChoice choice = null;
			int choiceNumber = 1;
			int schoolTypeID = new Integer(this.getBundle().getProperty(PROPERTY_DEFAULT_SCHOOL_TYPE, "-1")).intValue();
			
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
					
					choice = saveChoice(user, child, element, seasonPK, yearPK, schoolTypeID, language, message, status, choice, choiceNumber++, timeNow);
					i++;
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
				throw new IDOCreateException(e.getMessage());
			}
			ex.printStackTrace();
			throw new IDOCreateException(ex);
		}
	}

	private SchoolChoice saveChoice(User user, User child, Object schoolPK, Object seasonPK, Object yearPK, int schoolTypeID, String language, String message, CaseStatus status, Case parentCase, int choiceNumber, IWTimestamp stamp) throws IDOCreateException {
		SchoolSeason season = null;
		try {
			season = getSchoolBusiness().getSchoolSeason(new Integer(seasonPK.toString()));
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
		
		choice.setCreated(stamp.getTimestamp());
		choice.setCaseStatus(status);
		choice.setParentCase(parentCase);
		choice.store();
		
		if (status.equals(getCaseStatusPreliminary())) {
			String subject = getLocalizedString("music_school.choice_received_subject", "Music school choice received");
			String body = getLocalizedString("music_school.choice_received_body", "{1} has received the application for a music school placing for {0}, {2}.  The application will be handled as soon as possible.");
			sendMessageToParents(choice, subject, body);
		}

		return choice;
	}
	
	public void storeChildSchoolInformation(User child, boolean canDisplayImage) {
		child.setMetaData(METADATA_CAN_DISPLAY_SCHOOL_IMAGE, String.valueOf(canDisplayImage));
		child.store();
	}

	public void storeChildAfterSchoolCareInformation(User child, boolean canDisplayImage, String otherAfterSchoolCareInformation) {
		child.setMetaData(METADATA_CAN_DISPLAY_AFTER_SCHOOL_CARE_IMAGE, String.valueOf(canDisplayImage));
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
}