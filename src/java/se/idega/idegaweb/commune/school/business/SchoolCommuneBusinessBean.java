package se.idega.idegaweb.commune.school.business;

import is.idega.idegaweb.member.business.MemberFamilyLogic;
import is.idega.idegaweb.member.business.NoCustodianFound;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import se.idega.idegaweb.commune.business.CommuneUserBusiness;
import se.idega.idegaweb.commune.message.business.MessageBusiness;
import se.idega.idegaweb.commune.school.data.SchoolChoice;

import com.idega.block.process.business.CaseBusiness;
import com.idega.block.process.business.CaseBusinessBean;
import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolClass;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.block.school.data.SchoolClassMemberHome;
import com.idega.block.school.data.SchoolSeason;
import com.idega.block.school.data.SchoolType;
import com.idega.block.school.data.SchoolYear;
import com.idega.business.IBOLookup;
import com.idega.core.data.Address;
import com.idega.core.data.Phone;
import com.idega.data.IDOException;
import com.idega.data.IDOQuery;
import com.idega.idegaweb.IWPropertyList;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;

/**
 * @author Laddi
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class SchoolCommuneBusinessBean extends CaseBusinessBean implements SchoolCommuneBusiness,CaseBusiness {
	
	/* Commented out since it is never used...
	private Collection getSchoolClasses(int schoolID,int schoolSeasonID) throws RemoteException {
		return getSchoolBusiness().findSchoolClassesBySchoolAndSeason(schoolID, schoolSeasonID);
	}*/
	
	/* Commented out since it is never used...
	private Collection getSchoolClassMember(int schoolClassID) throws RemoteException {
		return getSchoolBusiness().findStudentsInClass(schoolClassID);
	}*/
	
	/* Commented out since it is never used...
	private Collection getSchoolYears() throws RemoteException {
		return getSchoolBusiness().findAllSchoolYears();	
	}*/
	
	/* Commented out since it is never used...
	private Collection getSchoolSeasons() throws RemoteException {
		return getSchoolBusiness().findAllSchoolSeasons();	
	}*/
	
	public SchoolBusiness getSchoolBusiness() throws RemoteException {
		return (SchoolBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), SchoolBusiness.class);	
	}

	public SchoolChoiceBusiness getSchoolChoiceBusiness() throws RemoteException {
		return (SchoolChoiceBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), SchoolChoiceBusiness.class);	
	}

	public Map getStudentList(Collection students) throws RemoteException {
		HashMap coll = new HashMap();
			
		if ( !students.isEmpty() ) {
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
			SchoolClassMember member = getSchoolBusiness().getSchoolClassMemberHome().findByUserAndSchoolAndSeason(userID, schoolID, seasonID);
			if (member != null)
				return true;
			return false;
		}
		catch (FinderException e) {
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
			
		if ( !choices.isEmpty() ) {
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
				choice = (SchoolChoice)it.next();
				Phone phone = getCommuneUserBusiness().getChildHomePhone(choice.getChildId());
				if (phone != null)
					coll.put(new Integer(choice.getChildId()),phone);
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
				choice = (SchoolChoice)it.next();
				address = getCommuneUserBusiness().getUserAddress1(choice.getChildId());
				coll.put(new Integer(choice.getChildId()),address);
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
	
	private String[] getUserIDsFromClassMembers(Collection classMembers) throws RemoteException {
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
	
	/* Commented out since it is never used...
	private String[] getUserIDsFromStudents(Collection choices) throws RemoteException {
		if (choices != null) {
			String[] userIDs = new String[choices.size()];
			SchoolClassMember student;
			
			int a = 0;
			Iterator iter = choices.iterator();
			while (iter.hasNext()) {
				student = (SchoolClassMember) iter.next();
				userIDs[a] = String.valueOf(student.getClassMemberId());
				a++;
			}
			return userIDs;
		}
		return null;
	}*/
	
	public Map getStudentChoices(Collection students, int seasonID) throws RemoteException {
		HashMap coll = new HashMap();
			
		if ( !students.isEmpty() ) {
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
	
	public boolean hasChosenOtherSchool(Collection choices, int schoolID) throws RemoteException {
		if (choices != null && !choices.isEmpty()) {
			Iterator iter = choices.iterator();
			while (iter.hasNext()) {
				SchoolChoice element = (SchoolChoice) iter.next();
				String caseStatus = element.getCaseStatus().toString();
				if ((caseStatus.equalsIgnoreCase("PREL") || caseStatus.equalsIgnoreCase("PLAC") || caseStatus.equalsIgnoreCase("FLYT")) && element.getChosenSchoolId() != schoolID)
					return true;
			}
		}
		
		return false;	
	}
	
	public boolean[] hasSchoolChoices(int userID, int seasonID) throws RemoteException {
		boolean[] returnValue = {false,false};
		int numberOfChoices = getSchoolChoiceBusiness().getNumberOfApplicationsForStudents(userID, seasonID);	
		if (numberOfChoices > 0) {
			if (numberOfChoices == 1)
				returnValue[1] = true;
			returnValue[0] = true;
		}
		return returnValue;
	}
	
	public boolean hasMoveChoiceToOtherSchool(int userID, int schoolID, int seasonID) {
		try {
			int numberOfChoices = getSchoolChoiceBusiness().getSchoolChoiceHome().getMoveChoices(userID, schoolID, seasonID);
			if (numberOfChoices > 0)
				return true;
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
			int numberOfChoices = getSchoolChoiceBusiness().getSchoolChoiceHome().getChoices(userID, schoolID, seasonID);
			if (numberOfChoices > 0)
				return true;
			return false;
		}
		catch (RemoteException e) {
			return false;
		}
		catch (IDOException e) {
			return false;
		}
	}
	
	public int getChosenSchoolID(Collection choices) throws RemoteException {
		if (choices != null && !choices.isEmpty()) {
			Iterator iter = choices.iterator();
			while (iter.hasNext()) {
				SchoolChoice element = (SchoolChoice) iter.next();
				String caseStatus = element.getCaseStatus().toString();
				if (caseStatus.equalsIgnoreCase("PREL") || caseStatus.equalsIgnoreCase("PLAC") || caseStatus.equalsIgnoreCase("FLYT"))
					return ((Integer)element.getPrimaryKey()).intValue();
			}
		}
		
		return -1;	
	}
	
	public SchoolSeason getPreviousSchoolSeason(SchoolSeason schoolSeason) throws RemoteException {
		Collection coll = getSchoolBusiness().findAllPreviousSchoolSeasons(schoolSeason);
		if ( !coll.isEmpty() ) {
			Iterator iter = coll.iterator();
			while (iter.hasNext()) {
				return (SchoolSeason) iter.next();
			}	
		}
		return null;
	}
	
	public SchoolSeason getPreviousSchoolSeason(int schoolSeasonID) throws RemoteException {
		Collection coll = getSchoolBusiness().findAllPreviousSchoolSeasons(schoolSeasonID);
		if ( !coll.isEmpty() ) {
			Iterator iter = coll.iterator();
			while (iter.hasNext()) {
				return (SchoolSeason) iter.next();
			}	
		}
		return null;
	}
	
	public int getPreviousSchoolSeasonID(int schoolSeasonID) throws RemoteException {
		SchoolSeason season = getPreviousSchoolSeason(schoolSeasonID);
		if (season != null) {
			return ((Integer)season.getPrimaryKey()).intValue();
		}
		return -1;
	}
		
	public int getCurrentSchoolSeasonID() throws RemoteException {
		try {
			return ((Integer)getSchoolChoiceBusiness().getCurrentSeason().getPrimaryKey()).intValue();	
		}
		catch (FinderException fe) {
			fe.printStackTrace();
			return -1;	
		}
	}
	
	public SchoolYear getSchoolYear(int age) throws RemoteException {
		Collection schoolYears = getSchoolBusiness().findAllSchoolYearsByAge(age);
		if ( !schoolYears.isEmpty() ) {
			Iterator iter = schoolYears.iterator();
			while (iter.hasNext()) {
				return (SchoolYear) iter.next();
			}
		}
		return null;
	}
	
	public int getPreviousSchoolYear(int schoolYearID) throws RemoteException {
		SchoolYear year = getSchoolBusiness().getSchoolYear(new Integer(schoolYearID));
		if (year != null) {
			SchoolYear previousYear = getSchoolYear(year.getSchoolYearAge()-1);
			if (previousYear != null)
				return ((Integer)previousYear.getPrimaryKey()).intValue();
		}
		return -1;
	}
	
	public SchoolYear getNextSchoolYear(SchoolYear schoolYear) throws RemoteException {
		SchoolYear nextYear = getSchoolYear(schoolYear.getSchoolYearAge()+1);
		if (nextYear != null)
			return nextYear;
		return null;
	}
	
	public int getGradeForYear(int schoolYearID) throws RemoteException {
		SchoolYear year = getSchoolBusiness().getSchoolYear(new Integer(schoolYearID));
		if (year != null)
			return year.getSchoolYearAge();
		return -1;
	}
	
	public Collection getPreviousSchoolClasses(School school, SchoolSeason schoolSeason, SchoolYear schoolYear) throws RemoteException {
		SchoolSeason season = getPreviousSchoolSeason(schoolSeason);
		if ( season != null ) {
			SchoolYear year = getSchoolYear(schoolYear.getSchoolYearAge()-1);
			if ( year != null ) {
				return getSchoolBusiness().findSchoolClassesBySchoolAndSeasonAndYear(((Integer)school.getPrimaryKey()).intValue(), ((Integer)season.getPrimaryKey()).intValue(), ((Integer)year.getPrimaryKey()).intValue());
			}
		}
		return new Vector();
	}
	
	public void finalizeGroup(SchoolClass schoolClass, String subject, String body, boolean confirmation) throws RemoteException {
		SchoolChoice choice;
		User student;
		Collection choices = getSchoolChoiceBusiness().getApplicationsInClass(schoolClass, confirmation);
		Iterator iter = choices.iterator();
		
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
			
			User parent = choice.getOwner();
			User child = choice.getChild();	
					
			if (getSchoolChoiceBusiness().getReceiver(parent, child) == child){
				getMessageBusiness().createUserMessage(child, subject, body);	
							
			} else {
				getMessageBusiness().createUserMessage(parent, subject, body);
	
				try {
					Collection parents = getMemberFamilyLogic().getCustodiansFor(student);
					if (!parents.isEmpty()) {
						Iterator iterator = parents.iterator();
						while (iterator.hasNext()) {
							User otherParent = (User) iterator.next();
							if (!getUserBusiness().haveSameAddress(parent, otherParent)) {
								getMessageBusiness().createUserMessage(otherParent, subject, body);
							}
						}	
					}
				}
				catch (NoCustodianFound ncd) {
					ncd.printStackTrace();
				}
			}
		}
	}
	
	public void setStudentAsSpeciallyPlaced(SchoolClassMember schoolMember) throws RemoteException {
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
			
			if (doUpdate)
				schoolMember.store();
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
		getUserBusiness().getGroupBusiness().addUser(((Integer)rootAdminGroup.getPrimaryKey()).intValue(), user);
	}
	
	public void markSchoolClassReady(SchoolClass schoolClass) throws RemoteException {
		schoolClass.setReady(true);
		schoolClass.store();	
	}
	
	public void markSchoolClassLocked(SchoolClass schoolClass) throws RemoteException {
		schoolClass.setLocked(true);
		schoolClass.store();
	}
	
	public boolean canMarkSchoolClass(SchoolClass schoolClass, String propertyName) throws RemoteException {
		if (schoolClass != null) {
			SchoolSeason season = getSchoolBusiness().getSchoolSeason(new Integer(schoolClass.getSchoolSeasonId()));
			IWTimestamp seasonStart = new IWTimestamp(season.getSchoolSeasonStart());
			IWTimestamp timeNow = new IWTimestamp();
			if (timeNow.getYear() == 2002)
				return true;
			
			IWPropertyList properties = getIWApplicationContext().getSystemProperties().getProperties("school_properties");
			String ready = properties.getProperty(propertyName);
			if (ready != null) {
				seasonStart.setDay(Integer.parseInt(ready.substring(0, 2)));
				seasonStart.setMonth(Integer.parseInt(ready.substring(3)));
				if (timeNow.isEarlierThan(seasonStart))
					return false;
			}
		}
		return true;	
	}
	
	public void moveToGroup(int studentID, int schoolClassID, int oldSchoolClassID) throws RemoteException {
		SchoolClassMember classMember = getSchoolBusiness().findClassMemberInClass(studentID, oldSchoolClassID);
		classMember.setSchoolClassId(schoolClassID);
		classMember.store();
	}

	private MemberFamilyLogic getMemberFamilyLogic() throws RemoteException {
		return (MemberFamilyLogic) com.idega.business.IBOLookup.getServiceInstance(getIWApplicationContext(), MemberFamilyLogic.class);
	}

	private MessageBusiness getMessageBusiness() throws RemoteException {
		return (MessageBusiness) com.idega.business.IBOLookup.getServiceInstance(getIWApplicationContext(), MessageBusiness.class);
	}
	
	private CommuneUserBusiness getCommuneUserBusiness() throws RemoteException {
		return (CommuneUserBusiness) com.idega.business.IBOLookup.getServiceInstance(getIWApplicationContext(), CommuneUserBusiness.class);
	}
	
	private CommuneUserBusiness getUserBusiness() throws RemoteException {
		return (CommuneUserBusiness) this.getServiceInstance(CommuneUserBusiness.class);
	}
	
	public String getLocalizedSchoolTypeKey(SchoolType type) {
		String key = type.getLocalizationKey();
		if (key == null || key.length() == 0)
			key = "school.school_type_" + type.getSchoolTypeName();
		return key;
	}
	
	public void resetSchoolClassStatus(int schoolClassID) throws RemoteException {
		SchoolClass schoolClass = getSchoolBusiness().findSchoolClass(new Integer(schoolClassID));
		schoolClass.setLocked(false);
		schoolClass.setReady(false);
		schoolClass.store();
	}
		
    // The method getCurrentMembersWithInvoiceInterval is specified in the
    // interface SchoolCommuneBusiness.  /Staffan
    public SchoolClassMember [] getCurrentMembersWithInvoiceInterval ()
        throws RemoteException {
        final Map result = new TreeMap ();
        final String [] invoiceIntervals = { "Månad", "Kvartal", "Termin",
                                               "År" };
        final SchoolBusiness business = getSchoolBusiness ();
        final SchoolClassMemberHome home = business.getSchoolClassMemberHome ();
        final int currentSeasonId = getCurrentSchoolSeasonID ();
        for (int i = 0; i < invoiceIntervals.length; i++) {
            // retreive members for a invoice interval value
            Collection members;
            try {
                members = home.findAllBySeasonAndInvoiceInterval
                        (currentSeasonId, invoiceIntervals [i]);
            } catch (final FinderException e) {
                members = java.util.Collections.EMPTY_LIST;
            }
            for (Iterator m = members.iterator (); m.hasNext ();) {
                // sort retreived members
                final SchoolClassMember member = (SchoolClassMember) m.next ();
                if (member.getHasCompensationByInvoice ()) {    
                    final User user = member.getStudent ();
                    result.put (user.getPersonalID (), member);
                }
            }
        }
        // convert the sorted members to an array return value
        final Collection values = result.values ();
        return (SchoolClassMember []) values.toArray (new SchoolClassMember
                                                      [values.size ()]);
    }

    // The method getCurrentSchoolClassMembership is specified in the
    // interface SchoolCommuneBusiness.  /Staffan
    public SchoolClassMember getCurrentSchoolClassMembership (final User user)
        throws RemoteException {
        try {
            final SchoolSeason season
                    = getSchoolChoiceBusiness ().getCurrentSeason ();
            final SchoolClassMember student = getSchoolBusiness ()
                    .findByStudentAndSeason (user, season);
            return (null == student || null != student.getRemovedDate ())
                    ? null : student;
        } catch (final FinderException e) {
            return null;
        }
    }
}
