package se.idega.idegaweb.commune.school.business;

import is.idega.idegaweb.member.business.MemberFamilyLogic;
import is.idega.idegaweb.member.business.NoCustodianFound;

import java.rmi.RemoteException;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import se.idega.idegaweb.commune.business.CommuneUserBusiness;
import se.idega.idegaweb.commune.message.business.MessageBusiness;
import se.idega.idegaweb.commune.school.data.SchoolChoice;

import com.idega.block.process.business.CaseBusiness;
import com.idega.block.process.business.CaseBusinessBean;
import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.business.SchoolClassBusiness;
import com.idega.block.school.business.SchoolClassMemberBusiness;
import com.idega.block.school.business.SchoolSeasonBusiness;
import com.idega.block.school.business.SchoolYearBusiness;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolClass;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.block.school.data.SchoolSeason;
import com.idega.block.school.data.SchoolYear;
import com.idega.business.IBOLookup;
import com.idega.core.data.Email;
import com.idega.idegaweb.IWPropertyList;
import com.idega.user.business.UserBusiness;
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
	
	private Collection getSchoolClasses(int schoolID,int schoolSeasonID) throws RemoteException {
		return getSchoolClassBusiness().findSchoolClassesBySchoolAndSeason(schoolID, schoolSeasonID);
	}
	
	private Collection getSchoolClassMember(int schoolClassID) throws RemoteException {
		return getSchoolClassMemberBusiness().findStudentsInClass(schoolClassID);
	}
	
	private Collection getSchoolYears() throws RemoteException {
		return getSchoolYearBusiness().findAllSchoolYears();	
	}
	
	private Collection getSchoolSeasons() throws RemoteException {
		return getSchoolSeasonBusiness().findAllSchoolSeasons();	
	}
	
	public SchoolBusiness getSchoolBusiness() throws RemoteException {
		return (SchoolBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), SchoolBusiness.class);	
	}

	public SchoolSeasonBusiness getSchoolSeasonBusiness() throws RemoteException {
		return (SchoolSeasonBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), SchoolSeasonBusiness.class);	
	}

	public SchoolYearBusiness getSchoolYearBusiness() throws RemoteException {
		return (SchoolYearBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), SchoolYearBusiness.class);	
	}

	public SchoolClassBusiness getSchoolClassBusiness() throws RemoteException {
		return (SchoolClassBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), SchoolClassBusiness.class);	
	}

	public SchoolClassMemberBusiness getSchoolClassMemberBusiness() throws RemoteException {
		return (SchoolClassMemberBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), SchoolClassMemberBusiness.class);	
	}
	
	public SchoolChoiceBusiness getSchoolChoiceBusiness() throws RemoteException {
		return (SchoolChoiceBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), SchoolChoiceBusiness.class);	
	}
	
	public Map getStudentList(Collection students) throws RemoteException {
		HashMap coll = new HashMap();
			
		if ( !students.isEmpty() ) {
			UserBusiness userBusiness = (UserBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), UserBusiness.class);	
			User user;
			SchoolClassMember member;
			
			Iterator iter = students.iterator();
			while (iter.hasNext()) {
				member = (SchoolClassMember) iter.next();
				user = userBusiness.getUser(member.getClassMemberId());
				coll.put(new Integer(member.getClassMemberId()), user);
			}
		}
		
		return coll;		
	}
	
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
		Collection coll = getSchoolSeasonBusiness().findAllPreviousSchoolSeasons(schoolSeason);
		if ( !coll.isEmpty() ) {
			Iterator iter = coll.iterator();
			while (iter.hasNext()) {
				return (SchoolSeason) iter.next();
			}	
		}
		return null;
	}
	
	public SchoolSeason getPreviousSchoolSeason(int schoolSeasonID) throws RemoteException {
		Collection coll = getSchoolSeasonBusiness().findAllPreviousSchoolSeasons(schoolSeasonID);
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
		Collection schoolYears = getSchoolYearBusiness().findAllSchoolYearsByAge(age);
		if ( !schoolYears.isEmpty() ) {
			Iterator iter = schoolYears.iterator();
			while (iter.hasNext()) {
				return (SchoolYear) iter.next();
			}
		}
		return null;
	}
	
	public int getPreviousSchoolYear(int schoolYearID) throws RemoteException {
		SchoolYear year = getSchoolYearBusiness().getSchoolYear(new Integer(schoolYearID));
		if (year != null) {
			SchoolYear previousYear = getSchoolYear(year.getSchoolYearAge()-1);
			if (previousYear != null)
				return ((Integer)previousYear.getPrimaryKey()).intValue();
		}
		return -1;
	}
	
	public int getGradeForYear(int schoolYearID) throws RemoteException {
		SchoolYear year = getSchoolYearBusiness().getSchoolYear(new Integer(schoolYearID));
		if (year != null)
			return year.getSchoolYearAge();
		return -1;
	}
	
	public Collection getPreviousSchoolClasses(School school, SchoolSeason schoolSeason, SchoolYear schoolYear) throws RemoteException {
		SchoolSeason season = getPreviousSchoolSeason(schoolSeason);
		if ( season != null ) {
			SchoolYear year = getSchoolYear(schoolYear.getSchoolYearAge()-1);
			if ( year != null ) {
				return getSchoolClassBusiness().findSchoolClassesBySchoolAndSeasonAndYear(((Integer)school.getPrimaryKey()).intValue(), ((Integer)season.getPrimaryKey()).intValue(), ((Integer)year.getPrimaryKey()).intValue());
			}
		}
		return new Vector();
	}
	
	public void finalizeGroup(int schoolClassID, String subject, String body) throws RemoteException {
		SchoolClass schoolClass = getSchoolClassBusiness().findSchoolClass(new Integer(schoolClassID));
		School school = getSchoolBusiness().getSchool(new Integer(schoolClass.getSchoolId()));

		Map students = getStudentList(getSchoolClassMemberBusiness().findStudentsInClass(schoolClassID));
		Iterator iter = students.values().iterator();
		while (iter.hasNext()) {
			User student = (User) iter.next();
			try {
				Collection parents = getMemberFamilyLogic().getCustodiansFor(student);
				if (!parents.isEmpty()) {
					Iterator iterator = parents.iterator();
					while (iterator.hasNext()) {
						User parent = (User) iterator.next();
						List emails = new Vector(parent.getEmails());
						if (!emails.isEmpty()) {
							Email email = (Email) emails.get(0);
							try {
								Object[] arguments = { school.getName(), parent.getNameLastFirst(true), schoolClass.getName(), student.getNameLastFirst(true) };
								getMessageBusiness().createUserMessage(parent, subject, MessageFormat.format(body, arguments));
							}
							catch (CreateException ce) {
								ce.printStackTrace();
							}
						}
					}	
				}
			}
			catch (NoCustodianFound ncd) {
				ncd.printStackTrace();
			}
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
			SchoolSeason season = getSchoolSeasonBusiness().getSchoolSeason(new Integer(schoolClass.getSchoolSeasonId()));
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
		SchoolClassMember classMember = getSchoolClassMemberBusiness().findClassMemberInClass(studentID, oldSchoolClassID);
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
	
	private UserBusiness getUserBusiness() throws RemoteException {
		return (UserBusiness) this.getServiceInstance(UserBusiness.class);
	}
		
}