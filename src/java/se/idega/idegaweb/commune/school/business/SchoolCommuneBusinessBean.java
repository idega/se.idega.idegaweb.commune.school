package se.idega.idegaweb.commune.school.business;

import is.idega.block.family.business.FamilyLogic;
import is.idega.block.family.business.NoCustodianFound;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import se.idega.idegaweb.commune.accounting.school.data.ProviderAccountingProperties;
import se.idega.idegaweb.commune.accounting.school.data.ProviderAccountingPropertiesHome;
import se.idega.idegaweb.commune.business.CommuneUserBusiness;
import se.idega.idegaweb.commune.message.business.MessageBusiness;
import se.idega.idegaweb.commune.school.data.SchoolChoice;
import se.idega.idegaweb.commune.user.data.Citizen;
import se.idega.idegaweb.commune.user.data.CitizenHome;

import com.idega.block.datareport.util.ReportableCollection;
import com.idega.block.datareport.util.ReportableData;
import com.idega.block.datareport.util.ReportableField;
import com.idega.block.process.business.CaseBusiness;
import com.idega.block.process.business.CaseBusinessBean;
import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.business.SchoolTypeComparator;
import com.idega.block.school.business.SchoolYearComparator;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolClass;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.block.school.data.SchoolClassMemberHome;
import com.idega.block.school.data.SchoolHome;
import com.idega.block.school.data.SchoolSeason;
import com.idega.block.school.data.SchoolStudyPath;
import com.idega.block.school.data.SchoolStudyPathHome;
import com.idega.block.school.data.SchoolType;
import com.idega.block.school.data.SchoolYear;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.core.contact.data.Phone;
import com.idega.core.location.data.Address;
import com.idega.core.location.data.PostalCode;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDOEntityDefinition;
import com.idega.data.IDOException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.IDOQuery;
import com.idega.data.IDORemoveRelationshipException;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWPropertyList;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.user.data.Group;
import com.idega.user.data.GroupRelation;
import com.idega.user.data.GroupRelationHome;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;
import com.idega.util.text.TextSoap;

/**
 * @author Laddi
 * 
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates. To enable and disable the creation of type
 * comments go to Window>Preferences>Java>Code Generation.
 */
public class SchoolCommuneBusinessBean extends CaseBusinessBean implements SchoolCommuneBusiness, CaseBusiness {

	private IWBundle _iwb = null;

	private IWResourceBundle _iwrb = null;

	private final static String IW_BUNDLE_IDENTIFIER = "se.idega.idegaweb.commune";

	/*
	 * Commented out since it is never used... private Collection
	 * getSchoolClasses(int schoolID,int schoolSeasonID) throws RemoteException {
	 * return getSchoolBusiness().findSchoolClassesBySchoolAndSeason(schoolID,
	 * schoolSeasonID); }
	 */

	/*
	 * Commented out since it is never used... private Collection
	 * getSchoolClassMember(int schoolClassID) throws RemoteException { return
	 * getSchoolBusiness().findStudentsInClass(schoolClassID); }
	 */

	/*
	 * Commented out since it is never used... private Collection getSchoolYears()
	 * throws RemoteException { return getSchoolBusiness().findAllSchoolYears(); }
	 */

	/*
	 * Commented out since it is never used... private Collection
	 * getSchoolSeasons() throws RemoteException { return
	 * getSchoolBusiness().findAllSchoolSeasons(); }
	 */

	public Map getYearClassMap(Collection schoolYears, int schoolID, int seasonID, String emptyString, boolean showSubGroups) {
		try {
			SortedMap yearMap = new TreeMap(new SchoolYearComparator());
			if (schoolYears != null) {
				Map groupMap = null;
				SchoolYear year = null;
				SchoolBusiness sb = getSchoolBusiness();
				Collection groups = null;
				SchoolClass group = null;

				Iterator iter = schoolYears.iterator();
				while (iter.hasNext()) {
					groupMap = new HashMap();
					if (emptyString != null) {
						groupMap.put("-1", emptyString);
					}

					year = (SchoolYear) iter.next();
					groups = sb.findSchoolClassesBySchoolAndSeasonAndYear(schoolID, seasonID, ((Integer) year.getPrimaryKey()).intValue(), showSubGroups);
					if (groups != null && groups.size() > 0) {
						Iterator iterator = groups.iterator();
						while (iterator.hasNext()) {
							group = (SchoolClass) iterator.next();
							groupMap.put(group, group);
						}
					}
					yearMap.put(year, groupMap);
				}
			}
			return yearMap;
		}
		catch (Exception e) {
			e.printStackTrace(System.err);
			return null;
		}
	}
	
	public Map getSchoolTypeClassMap(Collection schoolTypes,int schoolID,int seasonID,Boolean showSubGroups,Boolean showNonSeasonGroups,String noSchoolClassFoundEntry){
		try {
			SortedMap typeMap = new TreeMap(new SchoolTypeComparator());
			if (schoolTypes != null) {
				Map groupMap = null;
				SchoolType schoolType = null;
				SchoolBusiness sb = getSchoolBusiness();
				Collection groups = null;
				SchoolClass group = null;

				Iterator iter = schoolTypes.iterator();
				while (iter.hasNext()) {
					groupMap = new HashMap();

					schoolType = (SchoolType) iter.next();
					groups = sb.findSchoolClassesBySchoolAndSchoolTypeAndSeason(schoolID, ((Integer) schoolType.getPrimaryKey()).intValue(),seasonID, showSubGroups,showNonSeasonGroups);
					if (groups != null && groups.size() > 0) {
						Iterator iterator = groups.iterator();
						while (iterator.hasNext()) {
							group = (SchoolClass) iterator.next();
							groupMap.put(group, group);
						}
					}
					else{
						groupMap.put("-1",noSchoolClassFoundEntry);
					}
					typeMap.put(schoolType, groupMap);
				}
			}
			return typeMap;
		}
		catch (Exception e) {
			e.printStackTrace(System.err);
			return null;
		}
	
	}

	public SchoolBusiness getSchoolBusiness() throws RemoteException {
		return (SchoolBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), SchoolBusiness.class);
	}

	public SchoolChoiceBusiness getSchoolChoiceBusiness() {
		try {
			return (SchoolChoiceBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), SchoolChoiceBusiness.class);
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}

	public Map getStudentList(Collection students) throws RemoteException {
		HashMap coll = new HashMap();

		if (!students.isEmpty()) {
			Collection users = getCommuneUserBusiness().getUsers(this.getUserIDsFromClassMembers(students));
			User user;

			Iterator iter = users.iterator();
			while (iter.hasNext()) {
				user = (User) iter.next();
				coll.put(user.getPrimaryKey(), user);
			}
		}

		return coll;
	}

	public boolean isOngoingSeason(int seasonID) throws RemoteException {
		SchoolSeason season = getSchoolBusiness().getSchoolSeason(new Integer(seasonID));
		if (season != null) {
			IWTimestamp stamp = new IWTimestamp();
			return stamp.isBetween(new IWTimestamp(season.getSchoolSeasonStart()), new IWTimestamp(season.getSchoolSeasonEnd()));
		}
		return false;
	}

	public boolean isAlreadyInSchool(int userID, int schoolID, int seasonID) throws RemoteException {
		try {
			Collection types = getSchoolBusiness().getSchoolTypeHome().findAllByCategory(getSchoolBusiness().getCategoryElementarySchool().getCategory(), false);
			SchoolClassMember member = getSchoolBusiness().getSchoolClassMemberHome().findByUserAndSchoolAndSeason(userID, schoolID, seasonID, types);
			if (member != null) return true;
			return false;
		}
		catch (FinderException e) {
			return false;
		}
	}

	public boolean isPlacedAtSchool(int userID, int schoolID) {
		try {
			return (getSchoolBusiness().getSchoolClassMemberHome().getNumberOfPlacingsAtSchool(userID, schoolID) > 0);
		}
		catch (RemoteException e) {
			return false;
		}
		catch (IDOException e) {
			return false;
		}
	}

	public Map getUserMapFromChoices(IDOQuery query) throws RemoteException {
		HashMap coll = new HashMap();

		User user;
		Collection students = getCommuneUserBusiness().getUsers(query);
		if (students != null) {
			Iterator iterator = students.iterator();
			while (iterator.hasNext()) {
				user = (User) iterator.next();
				coll.put(user.getPrimaryKey(), user);
			}
		}

		return coll;
	}

	public Map getUserMapFromChoices(Collection choices) throws RemoteException {
		HashMap coll = new HashMap();

		User user;
		Collection students = getCommuneUserBusiness().getUsers(getUserIDsFromChoices(choices));
		if (students != null) {
			Iterator iterator = students.iterator();
			while (iterator.hasNext()) {
				user = (User) iterator.next();
				coll.put(user.getPrimaryKey(), user);
			}
		}

		return coll;
	}

	public Map getUserAddressesMapFromChoices(Collection choices) throws RemoteException {
		HashMap coll = new HashMap();

		if (!choices.isEmpty()) {
			Address address;
			Collection students = getCommuneUserBusiness().getUsersMainAddresses(getUserIDsFromChoices(choices));
			if (students != null) {
				Iterator iterator = students.iterator();
				while (iterator.hasNext()) {
					address = (Address) iterator.next();
					coll.put(address.getPrimaryKey(), address);
				}
			}
		}

		return coll;
	}

	public Map getUserAddressesMapFromChoices(IDOQuery query) throws RemoteException {
		HashMap coll = new HashMap();

		Address address;
		Collection students = getCommuneUserBusiness().getUsersMainAddresses(query);
		if (students != null) {
			Iterator iterator = students.iterator();
			while (iterator.hasNext()) {
				address = (Address) iterator.next();
				coll.put(address.getPrimaryKey(), address);
			}
		}

		return coll;
	}

	public Map getUserPhoneMapFromChoicesUserIdPK(Collection choices) throws RemoteException {
		HashMap coll = new HashMap();
		if (!choices.isEmpty()) {
			SchoolChoice choice = null;
			Iterator it = choices.iterator();
			while (it.hasNext()) {
				choice = (SchoolChoice) it.next();
				Phone phone = getCommuneUserBusiness().getChildHomePhone(choice.getChildId());
				if (phone != null) coll.put(new Integer(choice.getChildId()), phone);
			}
		}

		return coll;
	}

	public Map getUserAddressMapFromChoicesUserIdPK(Collection choices) throws RemoteException {
		HashMap coll = new HashMap();
		if (!choices.isEmpty()) {
			Address address = null;
			SchoolChoice choice = null;
			Iterator it = choices.iterator();
			while (it.hasNext()) {
				choice = (SchoolChoice) it.next();
				address = getCommuneUserBusiness().getUserAddress1(choice.getChildId());
				coll.put(new Integer(choice.getChildId()), address);
			}
		}

		return coll;
	}

	private String[] getUserIDsFromChoices(Collection choices) {
		if (choices != null) {
			String[] userIDs = new String[choices.size()];
			SchoolChoice choice;

			int a = 0;
			Iterator iter = choices.iterator();
			while (iter.hasNext()) {
				choice = (SchoolChoice) iter.next();
				userIDs[a] = String.valueOf(choice.getChildId());
				a++;
			}
			return userIDs;
		}
		return null;
	}

	private String[] getUserIDsFromClassMembers(Collection classMembers) {
		if (classMembers != null) {
			String[] userIDs = new String[classMembers.size()];
			SchoolClassMember classMember;

			int a = 0;
			Iterator iter = classMembers.iterator();
			while (iter.hasNext()) {
				classMember = (SchoolClassMember) iter.next();
				userIDs[a] = String.valueOf(classMember.getClassMemberId());
				a++;
			}
			return userIDs;
		}
		return null;
	}

	/*
	 * Commented out since it is never used... private String[]
	 * getUserIDsFromStudents(Collection choices) throws RemoteException { if
	 * (choices != null) { String[] userIDs = new String[choices.size()];
	 * SchoolClassMember student;
	 * 
	 * int a = 0; Iterator iter = choices.iterator(); while (iter.hasNext()) {
	 * student = (SchoolClassMember) iter.next(); userIDs[a] =
	 * String.valueOf(student.getClassMemberId()); a++; } return userIDs; } return
	 * null; }
	 */

	public Map getStudentChoices(Collection students, int seasonID) throws RemoteException {
		HashMap coll = new HashMap();

		if (!students.isEmpty()) {
			Collection choices;
			SchoolClassMember member;

			Iterator iter = students.iterator();
			while (iter.hasNext()) {
				member = (SchoolClassMember) iter.next();
				choices = getSchoolChoiceBusiness().findByStudentAndSeason(member.getClassMemberId(), seasonID);
				coll.put(new Integer(member.getClassMemberId()), choices);
			}
		}

		return coll;
	}

	public boolean hasChosenOtherSchool(Collection choices, int schoolID) {
		if (choices != null && !choices.isEmpty()) {
			Iterator iter = choices.iterator();
			while (iter.hasNext()) {
				SchoolChoice element = (SchoolChoice) iter.next();
				String caseStatus = element.getCaseStatus().toString();
				if ((caseStatus.equalsIgnoreCase("PREL") || caseStatus.equalsIgnoreCase("PLAC") || caseStatus.equalsIgnoreCase("FLYT")) && element.getChosenSchoolId() != schoolID) return true;
			}
		}

		return false;
	}

	public boolean[] hasSchoolChoices(int userID, int seasonID) throws RemoteException {
		boolean[] returnValue = { false, false};
		int numberOfChoices = getSchoolChoiceBusiness().getNumberOfApplicationsForStudents(userID, seasonID);
		if (numberOfChoices > 0) {
			if (numberOfChoices == 1) returnValue[1] = true;
			returnValue[0] = true;
		}
		return returnValue;
	}

	public boolean hasMoveChoiceToOtherSchool(int userID, int schoolID, int seasonID) {
		try {
			int numberOfChoices = getSchoolChoiceBusiness().getSchoolChoiceHome().getMoveChoices(userID, schoolID, seasonID);
			if (numberOfChoices > 0) return true;
			return false;
		}
		catch (RemoteException e) {
			return false;
		}
		catch (IDOException e) {
			return false;
		}
	}

	public boolean hasChoicesForSeason(int userID, int seasonID) {
		try {
			String[] statuses = { getCaseStatusDeleted().getStatus(), getCaseStatusInactive().getStatus()};
			int numberOfChoices = getSchoolChoiceBusiness().getSchoolChoiceHome().getChoices(userID, seasonID, statuses);
			if (numberOfChoices > 0) return true;
			return false;
		}
		catch (RemoteException e) {
			return false;
		}
		catch (IDOException e) {
			return false;
		}
	}

	public boolean hasChoiceToThisSchool(int userID, int schoolID, int seasonID) {
		try {
			String[] statuses = { getCaseStatusDeleted().getStatus(), getCaseStatusInactive().getStatus()};
			int numberOfChoices = getSchoolChoiceBusiness().getSchoolChoiceHome().getChoices(userID, schoolID, seasonID, statuses);
			if (numberOfChoices > 0) return true;
			return false;
		}
		catch (RemoteException e) {
			return false;
		}
		catch (IDOException e) {
			return false;
		}
	}

	public int getChosenSchoolID(Collection choices) {
		if (choices != null && !choices.isEmpty()) {
			Iterator iter = choices.iterator();
			while (iter.hasNext()) {
				SchoolChoice element = (SchoolChoice) iter.next();
				String caseStatus = element.getCaseStatus().toString();
				if (caseStatus.equalsIgnoreCase("PREL") || caseStatus.equalsIgnoreCase("PLAC") || caseStatus.equalsIgnoreCase("FLYT")) return ((Integer) element.getPrimaryKey()).intValue();
			}
		}

		return -1;
	}

	public SchoolSeason getPreviousSchoolSeason(SchoolSeason schoolSeason) throws RemoteException {
		return getPreviousSchoolSeason(((Integer) schoolSeason.getPrimaryKey()).intValue());
	}

	public SchoolSeason getPreviousSchoolSeason(int schoolSeasonID) throws RemoteException {
		return getSchoolBusiness().findPreviousSchoolSeason(schoolSeasonID);
	}

	public int getPreviousSchoolSeasonID(int schoolSeasonID) throws RemoteException {
		SchoolSeason season = getPreviousSchoolSeason(schoolSeasonID);
		if (season != null) { return ((Integer) season.getPrimaryKey()).intValue(); }
		return -1;
	}

	public int getCurrentSchoolSeasonID() throws RemoteException {
		try {
			return ((Integer) getSchoolChoiceBusiness().getCurrentSeason().getPrimaryKey()).intValue();
		}
		catch (FinderException fe) {
			fe.printStackTrace();
			return -1;
		}
	}

	public SchoolSeason getCurrentSchoolSeason() throws FinderException {
		try {
			return getSchoolChoiceBusiness().getCurrentSeason();
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}

	public SchoolYear getSchoolYear(int age) throws RemoteException {
		Collection schoolYears = getSchoolBusiness().findAllSchoolYearsByAge(age);
		if (!schoolYears.isEmpty()) {
			Iterator iter = schoolYears.iterator();
			while (iter.hasNext()) {
				return (SchoolYear) iter.next();
			}
		}
		return null;
	}
	
	public SchoolYear getSchoolYear(int schoolYear, int test) throws RemoteException {
		SchoolYear schYear = getSchoolBusiness().getSchoolYear(new Integer(schoolYear));
		if (schYear != null) {
			return schYear;
		}
		return null;
	}

	public int getPreviousSchoolYear(int schoolYearID) throws RemoteException {
		SchoolYear year = getSchoolBusiness().getSchoolYear(new Integer(schoolYearID));
		if (year != null) {
			SchoolYear previousYear = getSchoolYear(year.getSchoolYearAge() - 1);
			if (previousYear != null) return ((Integer) previousYear.getPrimaryKey()).intValue();
		}
		return -1;
	}

	public SchoolYear getNextSchoolYear(SchoolYear schoolYear) throws RemoteException {
		SchoolYear nextYear = getSchoolYear(schoolYear.getSchoolYearAge() + 1);
		if (nextYear != null) return nextYear;
		return null;
	}

	public int getGradeForYear(int schoolYearID) throws RemoteException {
		SchoolYear year = getSchoolBusiness().getSchoolYear(new Integer(schoolYearID));
		if (year != null) return year.getSchoolYearAge();
		return -1;
	}

	public Collection getPreviousSchoolClasses(School school, SchoolSeason schoolSeason, SchoolYear schoolYear) throws RemoteException {
		SchoolSeason season = getPreviousSchoolSeason(schoolSeason);
		if (season != null) {
			SchoolYear year = getSchoolYear(schoolYear.getSchoolYearAge() - 1);
			if (year != null) { return getSchoolBusiness().findSchoolClassesBySchoolAndSeasonAndYear(((Integer) school.getPrimaryKey()).intValue(), ((Integer) season.getPrimaryKey()).intValue(), ((Integer) year.getPrimaryKey()).intValue()); }
		}
		return new Vector();
	}

	public void finalizeGroup(SchoolClass schoolClass, String subject, String body, boolean confirmation) throws RemoteException {
		SchoolChoice choice;
		User student;
		body = TextSoap.findAndReplace(body, "$barn$", "{0}");
		Collection choices = getSchoolChoiceBusiness().getApplicationsInClass(schoolClass, confirmation);
		Iterator iter = choices.iterator();
		String newBody;

		while (iter.hasNext()) {
			choice = (SchoolChoice) iter.next();
			student = choice.getChild();

			if (confirmation) {
				choice.setHasReceivedConfirmationMessage(true);
			}
			else {
				choice.setHasReceivedPlacementMessage(true);
			}
			choice.store();

			//Object[] arguments = { student.getNameLastFirst(true)};
			Object[] arguments = { student.getName()};
			newBody = MessageFormat.format(body, arguments);

			sendMessageToParents(choice, subject, newBody);
		}
	}

	public void setStudentAsSpeciallyPlaced(SchoolClassMember schoolMember) {
		schoolMember.setSpeciallyPlaced(true);
		schoolMember.store();
	}

	public void importStudentInformationToNewClass(SchoolClassMember schoolMember, SchoolSeason previousSeason) throws RemoteException {
		SchoolClassMember oldStudentInfo = getSchoolBusiness().findByStudentAndSeason(schoolMember, previousSeason);
		if (oldStudentInfo != null) {
			boolean doUpdate = false;
			if (oldStudentInfo.getSpeciallyPlaced()) {
				schoolMember.setSpeciallyPlaced(true);
				doUpdate = true;
			}
			if (oldStudentInfo.getLanguage() != null) {
				schoolMember.setLanguage(oldStudentInfo.getLanguage());
				doUpdate = true;
			}

			if (doUpdate) schoolMember.store();
		}
	}

	public void setNeedsSpecialAttention(int studentID, int schoolSeasonID, boolean needsAttention) throws RemoteException {
		setNeedsSpecialAttention(getSchoolBusiness().findByStudentAndSeason(studentID, schoolSeasonID), needsAttention);
	}

	public void setNeedsSpecialAttention(SchoolClassMember schoolMember, SchoolSeason schoolSeason, boolean needsAttention) throws RemoteException {
		setNeedsSpecialAttention(getSchoolBusiness().findByStudentAndSeason(schoolMember, schoolSeason), needsAttention);
	}

	public void setNeedsSpecialAttention(SchoolClassMember schoolMember, boolean needsAttention) {
		if (schoolMember != null) {
			schoolMember.setNeedsSpecialAttention(needsAttention);
			schoolMember.store();
		}
	}

	public void addSchoolAdministrator(User user) throws RemoteException, FinderException, CreateException {
		Group rootAdminGroup = getCommuneUserBusiness().getRootSchoolAdministratorGroup();
		getUserBusiness().getGroupBusiness().addUser(((Integer) rootAdminGroup.getPrimaryKey()).intValue(), user);
	}

	public void markSchoolClassReady(SchoolClass schoolClass) {
		schoolClass.setReady(true);
		schoolClass.setReadyDate(new IWTimestamp().getTimestamp());
		schoolClass.store();
	}

	public void markSchoolClassLocked(SchoolClass schoolClass) {
		schoolClass.setLocked(true);
		schoolClass.setLockedDate(new IWTimestamp().getTimestamp());
		schoolClass.store();
	}

	public boolean canMarkSchoolClass(SchoolClass schoolClass, String propertyName) throws RemoteException {
		if (schoolClass != null) {
			SchoolSeason season = getSchoolBusiness().getSchoolSeason(new Integer(schoolClass.getSchoolSeasonId()));
			IWTimestamp seasonStart = new IWTimestamp(season.getSchoolSeasonStart());
			IWTimestamp timeNow = new IWTimestamp();
			if (timeNow.getYear() == 2002) return true;

			IWPropertyList properties = getIWApplicationContext().getSystemProperties().getProperties("school_properties");
			String ready = properties.getProperty(propertyName);
			if (ready != null) {
				seasonStart.setDay(Integer.parseInt(ready.substring(0, 2)));
				seasonStart.setMonth(Integer.parseInt(ready.substring(3)));
				if (timeNow.isEarlierThan(seasonStart)) return false;
			}
		}
		return true;
	}

	public void moveToGroup(int studentID, int schoolClassID, int oldSchoolClassID, int schoolYearID) throws RemoteException {
		SchoolClass group = getSchoolBusiness().findSchoolClass(new Integer(oldSchoolClassID));
		if (group.getIsSubGroup()) {
			try {
				SchoolClass newGroup = getSchoolBusiness().findSchoolClass(new Integer(schoolClassID));
				SchoolClassMember member = getSchoolBusiness().getSchoolClassMemberHome().findByUserAndSchoolAndSeason(studentID, group.getSchoolId(), group.getSchoolSeasonId());
				member.removeFromGroup(group);
				member.addToGroup(newGroup);
			}
			catch (FinderException fe) {
				fe.printStackTrace();
			}
			catch (IDORemoveRelationshipException irre) {
				irre.printStackTrace();
			}
			catch (IDOAddRelationshipException iare) {
				iare.printStackTrace();
			}
		}
		else {
			SchoolClassMember classMember = getSchoolBusiness().findClassMemberInClass(studentID, oldSchoolClassID);
			classMember.setSchoolClassId(schoolClassID);
			classMember.setSchoolYear(schoolYearID);
			classMember.store();
		}
	}

	private MessageBusiness getMessageBusiness() throws RemoteException {
		return (MessageBusiness) com.idega.business.IBOLookup.getServiceInstance(getIWApplicationContext(), MessageBusiness.class);
	}

	private CommuneUserBusiness getCommuneUserBusiness() throws RemoteException {
		return (CommuneUserBusiness) com.idega.business.IBOLookup.getServiceInstance(getIWApplicationContext(), CommuneUserBusiness.class);
	}

	public CommuneUserBusiness getUserBusiness() throws RemoteException {
		return (CommuneUserBusiness) this.getServiceInstance(CommuneUserBusiness.class);
	}

	public String getLocalizedSchoolTypeKey(SchoolType type) {
		String key = type.getLocalizationKey();
		if (key == null || key.length() == 0) key = "school.school_type_" + type.getSchoolTypeName();
		return key;
	}

	public void resetSchoolClassStatus(int schoolClassID) throws RemoteException {
		SchoolClass schoolClass = getSchoolBusiness().findSchoolClass(new Integer(schoolClassID));
		schoolClass.setLocked(false);
		schoolClass.setReady(false);
		schoolClass.store();
	}

	public boolean removeSubGroupPlacements(int userID, int schoolID, int seasonID) {
		try {
			Collection placements = getSchoolBusiness().findSubGroupPlacements(userID, schoolID, seasonID);
			Iterator iter = placements.iterator();
			while (iter.hasNext()) {
				SchoolClassMember element = (SchoolClassMember) iter.next();
				element.remove();
			}
			return true;
		}
		catch (FinderException fe) {
			return false;
		}
		catch (RemoteException re) {
			log(re);
			return false;
		}
		catch (RemoveException re) {
			log(re);
			return false;
		}
	}

	/**
	 * Retreive all currently placed school members with invoice interval set and
	 * school type as given in parameter.
	 * 
	 * @return SchoolClassMember objects that follows the method spec
	 * @param operationalField
	 *          only search in schools of this type
	 * @exception RemoteException
	 *              when methods in data layer fails
	 * @author <a href="http://www.staffannoteberg.com">Staffan N�teberg </a>
	 */
	public SchoolClassMember[] getCurrentMembersWithInvoiceInterval(final String operationalField) throws RemoteException {
		if (null == operationalField || 0 >= operationalField.length()) { return new SchoolClassMember[0]; }
		final Collection schools = new ArrayList();
		try {
			final ProviderAccountingPropertiesHome pHome = (ProviderAccountingPropertiesHome) IDOLookup.getHome(ProviderAccountingProperties.class);
			final Collection schoolIds = pHome.findAllIdsByPaymentByInvoice(true);
			final SchoolHome sHome = (SchoolHome) IDOLookup.getHome(School.class);
			schools.addAll(sHome.findAllByPrimaryKeys(schoolIds));
		}
		catch (FinderException e) {
			// No problem, no schools found
		}

		final SchoolBusiness business = getSchoolBusiness();
		final SchoolClassMemberHome home = business.getSchoolClassMemberHome();
		try {
			final Collection result = home.findAllCurrentInvoiceCompensationBySchoolTypeAndSchools(operationalField, schools);
			return (SchoolClassMember[]) result.toArray(new SchoolClassMember[result.size()]);
		}
		catch (FinderException fe) {
			return new SchoolClassMember[0];
		}
	}

	/**
	 * Retreive info about membership in a school class for this particular user
	 * in current season.
	 * 
	 * @param user
	 *          user to search for
	 * @return SchoolClassMember or null if not found
	 * @exception RemoteException
	 *              when methods in data layer fails
	 * @author <a href="http://www.staffannoteberg.com">Staffan N�teberg </a>
	 */
	public SchoolClassMember getCurrentSchoolClassMembership(final User user) throws RemoteException {
		try {
			final SchoolSeason season = getSchoolChoiceBusiness().getCurrentSeason();
			final SchoolClassMember student = getSchoolBusiness().findByStudentAndSeason(user, season);
			return (null == student || null != student.getRemovedDate()) ? null : student;
		}
		catch (final FinderException e) {
			return null;
		}
	}

	/**
	 * Retreive info about membership in a school class for this particular user
	 * and school in current season.
	 * 
	 * @param user
	 *          user to search for
	 * @param schoolId
	 *          only accept answers from this school
	 * @return SchoolClassMember or null if not found
	 * @exception RemoteException
	 *              when methods in data layer fails
	 * @author <a href="http://www.staffannoteberg.com">Staffan N�teberg </a>
	 */
	public SchoolClassMember getCurrentSchoolClassMembership(final User user, final int schoolId) throws RemoteException {
		try {
			final SchoolSeason season = getSchoolChoiceBusiness().getCurrentSeason();
			final int userId = ((Integer) user.getPrimaryKey()).intValue();
			final int seasonId = ((Integer) season.getPrimaryKey()).intValue();
			final SchoolClassMember student = getSchoolBusiness().findByStudentAndSchoolAndSeason(userId, schoolId, seasonId);
			return (null == student || null != student.getRemovedDate()) ? null : student;
		}
		catch (final FinderException e) {
			return null;
		}
	}

	/**
	 * Retreive study path for this student or null if doesn't exist
	 * 
	 * @param student
	 *          the one who has a study path
	 * @return SchoolStudyPath or null if not found
	 * @author <a href="http://www.staffannoteberg.com">Staffan N�teberg </a>
	 */
	public SchoolStudyPath getStudyPath(final SchoolClassMember student) {
		try {
			final SchoolStudyPathHome home = (SchoolStudyPathHome) IDOLookup.getHome(SchoolStudyPath.class);
			final int studyPathId = student.getStudyPathId();
			return 0 < studyPathId ? home.findByPrimaryKey(new Integer(studyPathId)) : null;
		}
		catch (FinderException e) {
			return null;
		}
		catch (IDOLookupException e) {
			e.printStackTrace();
			return null;
		}
	}

	public SchoolStudyPath[] getAllStudyPaths() {
		SchoolStudyPath[] result = new SchoolStudyPath[0];
		try {
			final SchoolStudyPathHome home = (SchoolStudyPathHome) IDOLookup.getHome(SchoolStudyPath.class);
			final Collection studyPathCollection = home.findAllStudyPaths();
			if (null != studyPathCollection) {
				result = (SchoolStudyPath[]) studyPathCollection.toArray(new SchoolStudyPath[studyPathCollection.size()]);
			}
			return result;
		}
		catch (FinderException e) {
			return result;
		}
		catch (IDOLookupException e) {
			e.printStackTrace();
			return result;
		}
	}

	private void initializeBundlesIfNeeded(Locale currentLocale) {
		if (_iwb == null) {
			_iwb = this.getIWApplicationContext().getIWMainApplication().getBundle(IW_BUNDLE_IDENTIFIER);
		}
		_iwrb = _iwb.getResourceBundle(currentLocale);
	}

	public ReportableCollection getReportOfUsersNotRegisteredInAnyClass(Locale currentLocale, Date selectedDate, SchoolSeason currentSeason, Collection classes) throws IDOException, RemoteException, CreateException, FinderException {
		initializeBundlesIfNeeded(currentLocale);
		CommuneUserBusiness _communeUserService = (CommuneUserBusiness) IBOLookup.getServiceInstance(this.getIWApplicationContext(), CommuneUserBusiness.class);
		FamilyLogic _familyLogic = (FamilyLogic) IBOLookup.getServiceInstance(this.getIWApplicationContext(), FamilyLogic.class);
		Group communeGroup = _communeUserService.getRootCitizenGroup();

		ReportableCollection reportData = new ReportableCollection();

		SchoolBusiness sBusiness = (SchoolBusiness) IBOLookup.getServiceInstance(this.getIWApplicationContext(), SchoolBusiness.class);

		SchoolClassMemberHome scmHome = sBusiness.getSchoolClassMemberHome();

		Collection schoolyears = sBusiness.getSchoolYearHome().findAllSchoolYears();
		IWTimestamp seasonStartDate = new IWTimestamp(currentSeason.getSchoolSeasonStart());
		int currentYear = seasonStartDate.getYear();

		Iterator yIter = schoolyears.iterator();
		int minYearOfBirth = Integer.MAX_VALUE;
		int maxYearOfBirth = 0;
		while (yIter.hasNext()) {
			SchoolYear schoolYear = (SchoolYear) yIter.next();
			int schoolYearAge = schoolYear.getSchoolYearAge();
			int yearOfBirth = currentYear - schoolYearAge;
			if (minYearOfBirth > yearOfBirth) {
				minYearOfBirth = yearOfBirth;
			}
			if (maxYearOfBirth < yearOfBirth) {
				maxYearOfBirth = yearOfBirth;
			}
		}

		IWTimestamp firstDateOfBirth = new IWTimestamp(1, 1, minYearOfBirth);
		IWTimestamp lastDateOfBirth = new IWTimestamp(31, 12, maxYearOfBirth);

		CitizenHome citizenHome = (CitizenHome) IDOLookup.getHome(Citizen.class);
		//maindata
		Collection students = citizenHome.findCitizensNotAssignedToClassOnGivenDate(communeGroup, new java.sql.Date(selectedDate.getTime()), classes, firstDateOfBirth.getDate(), lastDateOfBirth.getDate());

		GroupRelationHome gRelationHome = ((GroupRelationHome) IDOLookup.getHome(GroupRelation.class));

		//initializing fields
		IDOEntityDefinition userDef = IDOLookup.getEntityDefinitionForClass(User.class);
		IDOEntityDefinition grRelDef = IDOLookup.getEntityDefinitionForClass(GroupRelation.class);
		IDOEntityDefinition addrDef = IDOLookup.getEntityDefinitionForClass(Address.class);
		DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, currentLocale);

		//Child - Fields
		ReportableField childPersonalID = new ReportableField(userDef.findFieldByUniqueName(User.FIELD_PERSONAL_ID));
		childPersonalID.setCustomMadeFieldName("child_ssn");
		childPersonalID.setLocalizedName(_iwrb.getLocalizedString("CommuneReportBusiness.child_ssn", "Personal ID"), currentLocale);
		reportData.addField(childPersonalID);

		ReportableField childLastName = new ReportableField(userDef.findFieldByUniqueName(User.FIELD_LAST_NAME));
		childLastName.setCustomMadeFieldName("child_last_name");
		childLastName.setLocalizedName(_iwrb.getLocalizedString("CommuneReportBusiness.child_last_name", "LastName"), currentLocale);
		reportData.addField(childLastName);

		ReportableField childFirstName = new ReportableField(userDef.findFieldByUniqueName(User.FIELD_FIRST_NAME));
		childFirstName.setCustomMadeFieldName("child_first_name");
		childFirstName.setLocalizedName(_iwrb.getLocalizedString("CommuneReportBusiness.child_first_name", "FirstName"), currentLocale);
		reportData.addField(childFirstName);

		ReportableField childAddress = new ReportableField(addrDef.findFieldByUniqueName(Address.FIELD_STREET_NAME));
		childAddress.setValueClass(String.class);
		childAddress.setCustomMadeFieldName("child_address");
		childAddress.setLocalizedName(_iwrb.getLocalizedString("CommuneReportBusiness.child_address", "Address"), currentLocale);
		reportData.addField(childAddress);

		ReportableField childGroupInvitationDate = new ReportableField(grRelDef.findFieldByUniqueName(GroupRelation.FIELD_INITIATION_DATE));
		childGroupInvitationDate.setValueClass(String.class);
		childGroupInvitationDate.setCustomMadeFieldName("child_gr_initiation_date");
		childGroupInvitationDate.setLocalizedName(_iwrb.getLocalizedString("CommuneReportBusiness.child_gr_initiation_date", "Invitiation date"), currentLocale);
		reportData.addField(childGroupInvitationDate);

		ReportableField lastPlacementField = new ReportableField("last_placement", String.class);
		lastPlacementField.setLocalizedName(_iwrb.getLocalizedString("SchoolCommuneBusiness.lastPlacement", "Last known school or child care / date"), currentLocale);
		reportData.addField(lastPlacementField);

		//Parent1 - Fields
		ReportableField parent1PersonalID = new ReportableField(userDef.findFieldByUniqueName(User.FIELD_PERSONAL_ID));
		parent1PersonalID.setCustomMadeFieldName("parent1_ssn");
		parent1PersonalID.setLocalizedName(_iwrb.getLocalizedString("CommuneReportBusiness.parent1_ssn", "Parent1 Personal ID"), currentLocale);
		reportData.addField(parent1PersonalID);

		ReportableField parent1LastName = new ReportableField(userDef.findFieldByUniqueName(User.FIELD_LAST_NAME));
		parent1LastName.setCustomMadeFieldName("parent1_last_name");
		parent1LastName.setLocalizedName(_iwrb.getLocalizedString("CommuneReportBusiness.parent1_last_name", "Parent1 LastName"), currentLocale);
		reportData.addField(parent1LastName);

		ReportableField parent1FirstName = new ReportableField(userDef.findFieldByUniqueName(User.FIELD_FIRST_NAME));
		parent1FirstName.setCustomMadeFieldName("parent1_first_name");
		parent1FirstName.setLocalizedName(_iwrb.getLocalizedString("CommuneReportBusiness.parent1_first_name", "Parent1 FirstName"), currentLocale);
		reportData.addField(parent1FirstName);

		ReportableField parent1Address = new ReportableField(addrDef.findFieldByUniqueName(Address.FIELD_STREET_NAME));
		parent1Address.setValueClass(String.class);
		parent1Address.setCustomMadeFieldName("parent1_address");
		parent1Address.setLocalizedName(_iwrb.getLocalizedString("CommuneReportBusiness.parent1_address", "Parent1 Address"), currentLocale);
		reportData.addField(parent1Address);

		ReportableField parent1GroupInvitationDate = new ReportableField(grRelDef.findFieldByUniqueName(GroupRelation.FIELD_INITIATION_DATE));
		parent1GroupInvitationDate.setValueClass(String.class);
		parent1GroupInvitationDate.setCustomMadeFieldName("parent1_gr_initiation_date");
		parent1GroupInvitationDate.setLocalizedName(_iwrb.getLocalizedString("CommuneReportBusiness.parent1_gr_initiation_date", "Parent1 Invitiation date"), currentLocale);
		reportData.addField(parent1GroupInvitationDate);

		//Parent2 - Fields
		ReportableField parent2PersonalID = new ReportableField(userDef.findFieldByUniqueName(User.FIELD_PERSONAL_ID));
		parent2PersonalID.setCustomMadeFieldName("parent2_ssn");
		parent2PersonalID.setLocalizedName(_iwrb.getLocalizedString("CommuneReportBusiness.parent2_ssn", "Parent2 Personal ID"), currentLocale);
		reportData.addField(parent2PersonalID);

		ReportableField parent2LastName = new ReportableField(userDef.findFieldByUniqueName(User.FIELD_LAST_NAME));
		parent2LastName.setCustomMadeFieldName("parent2_last_name");
		parent2LastName.setLocalizedName(_iwrb.getLocalizedString("CommuneReportBusiness.parent2_last_name", "Parent2 LastName"), currentLocale);
		reportData.addField(parent2LastName);

		ReportableField parent2FirstName = new ReportableField(userDef.findFieldByUniqueName(User.FIELD_FIRST_NAME));
		parent2FirstName.setCustomMadeFieldName("parent2_first_name");
		parent2FirstName.setLocalizedName(_iwrb.getLocalizedString("CommuneReportBusiness.parent2_first_name", "Parent2 FirstName"), currentLocale);
		reportData.addField(parent2FirstName);

		ReportableField parent2Address = new ReportableField(addrDef.findFieldByUniqueName(Address.FIELD_STREET_NAME));
		parent2Address.setValueClass(String.class);
		parent2Address.setCustomMadeFieldName("parent2_address");
		parent2Address.setLocalizedName(_iwrb.getLocalizedString("CommuneReportBusiness.parent2_address", "Parent2 Address"), currentLocale);
		reportData.addField(parent2Address);

		ReportableField parent2GroupInvitationDate = new ReportableField(grRelDef.findFieldByUniqueName(GroupRelation.FIELD_INITIATION_DATE));
		parent2GroupInvitationDate.setValueClass(String.class);
		parent2GroupInvitationDate.setCustomMadeFieldName("parent2_gr_initiation_date");
		parent2GroupInvitationDate.setLocalizedName(_iwrb.getLocalizedString("CommuneReportBusiness.parent2_gr_initiation_date", "Parent2 Invitiation date"), currentLocale);
		reportData.addField(parent2GroupInvitationDate);

		IWTimestamp currentTime = IWTimestamp.RightNow();
		long oneYearAgo = new IWTimestamp(currentTime.getDay(), currentTime.getMonth(), currentTime.getYear() - 1).getTimestamp().getTime();

		//Creating report data and adding to collection
		Iterator iter = students.iterator();
		while (iter.hasNext()) {
			User child = (User) iter.next();
			ReportableData data = new ReportableData();

			//ChildData
			data.addData(childPersonalID, child.getPersonalID());
			data.addData(childLastName, child.getLastName());
			data.addData(childFirstName, child.getFirstName());
			reportData.add(data);

			Address childAddressEntiy = _communeUserService.getUsersMainAddress(child);
			String childAddressString = this.getAddressString(childAddressEntiy, _iwrb);
			if (childAddressString != null) {
				data.addData(childAddress, childAddressString);
			}

			Collection coll = gRelationHome.findGroupsRelationshipsContainingGroupsAndStatus(communeGroup, child, GroupRelation.STATUS_ACTIVE);
			Iterator iterator = coll.iterator();
			if (iterator.hasNext()) {
				GroupRelation rel = (GroupRelation) iterator.next();
				Timestamp time = rel.getInitiationDate();
				if (time != null) {
					if (time.getTime() > oneYearAgo) {
						data.addData(childGroupInvitationDate, dateFormat.format(time));
					}
				}
				else {
					data.addData(childGroupInvitationDate, "No time specified");
				}

			}
			else {
				data.addData(childGroupInvitationDate, "No relation to commune specified");
			}

			//lastPlacementField

			try {
				SchoolClassMember latestSCM = scmHome.findLatestByUser(child);
				if (latestSCM != null) {
					SchoolClass sc = latestSCM.getSchoolClass();
					School school = sc.getSchool();
					data.addData(lastPlacementField, school.getSchoolName() + " " + sc.getSchoolClassName() + " / " + dateFormat.format(latestSCM.getRegisterDate()));
				}
				else {
					data.addData(lastPlacementField, _iwrb.getLocalizedString("no_history", "No history"));
				}
			}
			catch (FinderException e1) {
				//e1.printStackTrace();
				data.addData(lastPlacementField, _iwrb.getLocalizedString("no_history", "No history"));
			}

			try {
				//Parent data
				Collection parents = null;
				try {
					parents = _familyLogic.getCustodiansFor(child);
				}
				catch (EJBException e) {
					System.out.println("[" + this.getClass() + "]: " + e.getMessage());
					System.out.println("[" + this.getClass() + "]: user:" + child);
					e.printStackTrace();
				}

				if (parents != null) {
					Iterator pIter = parents.iterator();
					//parent1
					if (pIter.hasNext()) {
						User parent = (User) pIter.next();
						ReportableData pData = new ReportableData();

						data.addData(parent1PersonalID, parent.getPersonalID());
						data.addData(parent1LastName, parent.getLastName());
						data.addData(parent1FirstName, parent.getFirstName());
						reportData.add(pData);

						Address parent1AddressEntiy = _communeUserService.getUsersMainAddress(child);
						String parent1AddressString = this.getAddressString(parent1AddressEntiy, _iwrb);
						if (parent1AddressString != null) {
							data.addData(parent1Address, parent1AddressString);
						}

						Collection pColl = gRelationHome.findGroupsRelationshipsContainingGroupsAndStatus(communeGroup, parent, GroupRelation.STATUS_ACTIVE);
						Iterator pIterator = pColl.iterator();
						while (pIterator.hasNext()) {
							GroupRelation rel = (GroupRelation) pIterator.next();
							Timestamp time = rel.getInitiationDate();
							if (time != null) {
								data.addData(parent1GroupInvitationDate, dateFormat.format(time));
							}
							else {
								data.addData(parent1GroupInvitationDate, _iwrb.getLocalizedString("CommuneReportBusiness.no_time_specified", "No time specified"));
							}
						}
					}

					//Parent2
					if (pIter.hasNext()) {
						User parent = (User) pIter.next();
						ReportableData pData = new ReportableData();

						data.addData(parent2PersonalID, parent.getPersonalID());
						data.addData(parent2LastName, parent.getLastName());
						data.addData(parent2FirstName, parent.getFirstName());
						reportData.add(pData);

						Address parent2AddressEntiy = _communeUserService.getUsersMainAddress(child);
						String parent2AddressString = this.getAddressString(parent2AddressEntiy, _iwrb);
						if (parent2AddressString != null) {
							data.addData(parent2Address, parent2AddressString);
						}

						Collection pColl = gRelationHome.findGroupsRelationshipsContainingGroupsAndStatus(communeGroup, parent, GroupRelation.STATUS_ACTIVE);
						Iterator pIterator = pColl.iterator();
						if (pIterator.hasNext()) {
							GroupRelation rel = (GroupRelation) pIterator.next();
							Timestamp time = rel.getInitiationDate();
							if (time != null) {
								data.addData(parent2GroupInvitationDate, dateFormat.format(time));
							}
							else {
								data.addData(parent2GroupInvitationDate, _iwrb.getLocalizedString("CommuneReportBusiness.no_time_specified", "No time specified"));
							}

						}
					}
				}
			}
			catch (NoCustodianFound e) {
				//System.out.println("["+this.getClass()+"]: "+e.getMessage());
				//e.printStackTrace();
			}

		}

		DateFormat dateTimeFormat = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, currentLocale);

		reportData.addExtraHeaderParameter("label_current_date", _iwrb.getLocalizedString("CommuneReportBusiness.label_current_date", "Current date"), "current_date", dateTimeFormat.format(IWTimestamp.getTimestampRightNow()));

		return reportData;
	}

	public void sendMessageToParents(SchoolChoice choice, String subject, String body) {
		try {
			User child = choice.getChild();
			//Object[] arguments = { child.getNameLastFirst(true), choice.getChosenSchool().getSchoolName()};
			Object[] arguments = { child.getName(), choice.getChosenSchool().getSchoolName()};

			User appParent = choice.getOwner();
			if (getUserBusiness().getMemberFamilyLogic().isChildInCustodyOf(child, appParent)) {
				getMessageBusiness().createUserMessage(choice, appParent, subject, MessageFormat.format(body, arguments), true);
			}

			try {
				Collection parents = getUserBusiness().getMemberFamilyLogic().getCustodiansFor(child);
				Iterator iter = parents.iterator();
				while (iter.hasNext()) {
					User parent = (User) iter.next();
					if (!getUserBusiness().haveSameAddress(parent, appParent)) {
						getMessageBusiness().createUserMessage(choice, parent, subject, MessageFormat.format(body, arguments), true);
					}
					else if (!parent.equals(appParent)) {
						getMessageBusiness().createUserMessage(choice, parent, subject, MessageFormat.format(body, arguments), false);
					}
				}
			}
			catch (NoCustodianFound ncf) {
				ncf.printStackTrace();
			}
		}
		catch (RemoteException re) {
			re.printStackTrace();
		}
	}

	private String getAddressString(Address addressEntiy, IWResourceBundle iwrb) {
		if (addressEntiy != null) {
			String stName = addressEntiy.getStreetName();
			String number = addressEntiy.getStreetNumber();
			String postalCode = iwrb.getLocalizedString("CommuneReportBusiness.not_available", "N/A");
			PostalCode pCodeObj = addressEntiy.getPostalCode();
			String city = addressEntiy.getCity();
			if (pCodeObj != null) {
				postalCode = pCodeObj.getPostalCode();
			}
			else if (city == null) {
				String childAddressString = stName + ((number == null) ? "" : (" " + number));
				return childAddressString;
			}

			String childAddressString = stName + ((number == null) ? "" : (" " + number));
			return childAddressString + ", " + postalCode + " " + city;

		}
		return "";
	}

}