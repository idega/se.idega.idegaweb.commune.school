/*
 * Created on 2003-okt-08
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package se.idega.idegaweb.commune.school.business;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Iterator;
import javax.ejb.FinderException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import se.idega.idegaweb.commune.business.CommuneUserBusiness;
import se.idega.idegaweb.commune.care.business.CareBusiness;
import se.idega.idegaweb.commune.care.resource.data.ResourceClassMember;
import se.idega.idegaweb.commune.care.resource.data.ResourceClassMemberHome;
import se.idega.idegaweb.commune.school.presentation.CentralPlacementEditorConstants;
import com.idega.block.school.data.SchoolCategory;
import com.idega.block.school.data.SchoolCategoryHome;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.block.school.data.SchoolClassMemberHome;
import com.idega.block.school.data.SchoolSeason;
import com.idega.block.school.data.SchoolStudyPath;
import com.idega.block.school.data.SchoolStudyPathHome;
import com.idega.block.school.data.SchoolYear;
import com.idega.business.IBOLookup;
import com.idega.business.IBOServiceBean;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;

/**
 * @author Göran Borgman
 *
 * Business object with helper methods for CentralPlacementEditor
 */
public class CentralPlacementBusinessBean extends IBOServiceBean implements CentralPlacementBusiness {
																							


	
	/**
	 * Removes a SchoolClassMember(placement) and its attached ResourceClassMembers
	 * (resource placements)
	 * 
	 * @param schoolClassMemberPK
	 */
	
	public void removeSchoolClassMember(Integer schoolClassMemberPK) {
		UserTransaction trans = null;
		try {
			trans = getSessionContext().getUserTransaction();
			trans.begin();
			
			SchoolClassMember schClMember = getSchoolClassMemberHome()
																					.findByPrimaryKey(schoolClassMemberPK);
			ResourceClassMemberHome rcmHome = (ResourceClassMemberHome) 
																		IDOLookup.getHome(ResourceClassMember.class);
			// Remove resource placements
			Collection rscPlacements = rcmHome.findAllByClassMemberId(schoolClassMemberPK);
			for (Iterator iter = rscPlacements.iterator(); iter.hasNext();) {
				ResourceClassMember element = (ResourceClassMember) iter.next();
				element.remove();				
			}
			
			// Remove placement
			schClMember.remove();

			trans.commit();
		} catch (Exception e) {
			try {
				trans.rollback();
			} catch (IllegalStateException e1) {
				log(e1);
			} catch (SecurityException e1) {
				log(e1);
			} catch (SystemException e1) {
				log(e1);
			}

		}
	}
	
	
	public SchoolClassMember getLatestPlacementFromElemAndHighSchool(User pupil) throws RemoteException {
		SchoolClassMember mbr = null;
		try {
			if (pupil != null) {
				mbr = getSchoolClassMemberHome().findLatestFromElemAndHighSchoolByUser(pupil);
			}
		} catch (FinderException fe1) {}
		
		return mbr;		
	}

	public SchoolClassMember getLatestPlacementFromElemAndHighSchool(User pupil, SchoolSeason season)  throws RemoteException {
		SchoolClassMember mbr = null;
		try { 
			if (pupil != null && season != null) {
				mbr = getSchoolClassMemberHome().findLatestFromElemAndHighSchoolByUserAndSeason(pupil, season);
			}
		} catch (FinderException fe) {
			log(fe);
		}
		
		return mbr;		
	}
	
	
	public String getDateString(Timestamp stamp, String pattern) {
		IWTimestamp iwts = null;
		String dateStr = "";
		if (stamp != null) {
			iwts = new IWTimestamp(stamp);
			dateStr = iwts.getDateString(pattern);
		}
		return dateStr;
	}
	
	public SchoolSeason getCurrentSeason() {
		SchoolSeason season = null;
		
		try {
			season = getCareBusiness().getSchoolSeasonHome().findSeasonByDate(new IWTimestamp().getDate());
		} catch (Exception e) {}
		
		if (season == null) {
			try {
				season = getCareBusiness().getCurrentSeason();
			} catch (Exception e1) {
				e1.printStackTrace();
			} 
		}
		
		return season;
	}
	
	public String getPlacementString(SchoolClassMember placement, User user, IWResourceBundle iwrb) {
		// Placement
		StringBuffer buf = new StringBuffer("");
		try {
			// add school name
			buf.append(placement.getSchoolClass().getSchool().getName());						
		} catch (Exception e) {}
		try {
			// school year
			SchoolYear theYear = placement.getSchoolYear();
			if (theYear != null)
				buf.append(", " + iwrb.getLocalizedString(CentralPlacementEditorConstants.KEY_SCHOOL_YEAR, "school year") + " "
								   												+ theYear.getName());						
		} catch (Exception e) {}
		try {
			// add school group
			buf.append(", " + iwrb.getLocalizedString(CentralPlacementEditorConstants.KEY_SCHOOL_GROUP, "group") + " "
								   + placement.getSchoolClass().getSchoolClassName());						
		} catch (Exception e) {}
		try {
			// add study path
			if (placement.getStudyPathId() != -1) {
				SchoolStudyPathHome  home = (SchoolStudyPathHome) IDOLookup.getHome(SchoolStudyPath.class);
				SchoolStudyPath sp = home.findByPrimaryKey(new Integer(placement.getStudyPathId()));
				buf.append(", " + iwrb.getLocalizedString(CentralPlacementEditorConstants.KEY_STUDY_PATH, "Study path") + " "+ sp.getCode());
			}
		} catch (Exception e) {}
		
		try {
			// add language
			if (placement.getLanguage() != null && !("-1").equals(placement.getLanguage())) {
				buf.append(", " + iwrb.getLocalizedString(CentralPlacementEditorConstants.KEY_LANGUAGE, "Language") + " "+ iwrb.getLocalizedString(placement.getLanguage(), ""));
			}
		} catch (Exception e) {}

		try {
			// add native language
			if (user.getNativeLanguage() != null) {
				buf.append(", " + iwrb.getLocalizedString(CentralPlacementEditorConstants.KEY_NATIVE_LANGUAGE, "Native language") + " "+ user.getNativeLanguage());
			}
		} catch (Exception e) {}
		return buf.toString();
	}
	
	protected SchoolCommuneSession getSchoolCommuneSession(IWContext iwc) throws RemoteException {
		return (SchoolCommuneSession) IBOLookup.getSessionInstance(iwc, SchoolCommuneSession.class);	
	}
	
	public CommuneUserBusiness getCommuneUserBusiness() throws RemoteException {
		return (CommuneUserBusiness) getServiceInstance(CommuneUserBusiness.class);
	}

	public SchoolChoiceBusiness getSchoolChoiceBusiness() throws RemoteException {
		return (SchoolChoiceBusiness) getServiceInstance(SchoolChoiceBusiness.class);
	}
	
	public UserBusiness getUserBusiness() throws RemoteException {
		return (UserBusiness) getServiceInstance(UserBusiness.class);
	}
	
	private SchoolClassMemberHome getSchoolClassMemberHome() throws RemoteException {
		return (SchoolClassMemberHome) IDOLookup.getHome(SchoolClassMember.class);
	}
	
	public SchoolCategoryHome getSchoolCategoryHome() throws RemoteException {
		return (SchoolCategoryHome) IDOLookup.getHome(SchoolCategory.class);
	}

	private CareBusiness getCareBusiness() throws RemoteException {
		return (CareBusiness) getServiceInstance(CareBusiness.class);
	}
}
