package se.idega.idegaweb.commune.school.business;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.ejb.FinderException;

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
import com.idega.user.business.UserBusiness;
import com.idega.user.data.User;

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
}