/*
 * Created on 2003-okt-08
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package se.idega.idegaweb.commune.school.business;

import java.rmi.RemoteException;
import java.sql.Timestamp;

import javax.ejb.FinderException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import se.idega.idegaweb.commune.school.presentation.CentralPlacementEditor;

import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.block.school.data.SchoolSeason;
import com.idega.business.IBOLookup;
import com.idega.business.IBOServiceBean;
import com.idega.presentation.IWContext;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;

/**
 * @author Göran Borgman
 *
 * Business object with helper methods for CentralPlacingEditor
 */
public class CentralPlacementBusinessBean extends IBOServiceBean 
																						implements CentralPlacementBusiness {
																							
	//  Keys for error messages
	private static final String KP = "central_placement_business.";
	private static final String KEY_ERROR_CHILD_ID = KP + "error.child_id";
	private static final String KEY_ERROR_PROVIDER_ID = KP + "error.provider_id";
	private static final String KEY_ERROR_PLACEMENT_DATE = KP + "error.placement_date";
	private static final String KEY_ERROR_SCHOOL_YEAR = KP + "error.school_year";
	private static final String KEY_ERROR_SCHOOL_GROUP = KP + "error.school_group";

	/**
	 * Stores a new placement(SchoolClassMember) with resources and ends the current placement
	 */
	public SchoolClassMember storeSchoolClassMember(IWContext iwc, int childID) 
																	throws RemoteException, CentralPlacementException {
		int studentID = -1;
		int schoolClassID = -1;
		int registrator = -1;
		Timestamp registerDate = null;
		Timestamp dayBeforeRegDate = null;
		String notes = null;
		SchoolClassMember placement = null;
		SchoolClassMember currentPlacement = null;
		
		// Get current placement from user http session. Put there from CentralPlacementEditor
		User pupil = (User) iwc.getSession().getAttribute(CentralPlacementEditor.SESSION_KEY_CHILD);
		currentPlacement = getCurrentSchoolClassMembership(pupil, iwc);
		
	// *** START - Check in params ***
		// pupil
		if (childID == -1) {
			throw new CentralPlacementException(KEY_ERROR_CHILD_ID, "No valid pupil found");
		} else {
			studentID = childID;
		}
		// provider
		if (iwc.isParameterSet(CentralPlacementEditor.PARAM_PROVIDER)) {
			String providerID = iwc.getParameter(CentralPlacementEditor.PARAM_PROVIDER);
			if (providerID.equals("-1")) {
				throw new CentralPlacementException(KEY_ERROR_PROVIDER_ID, 
																				"You must chose a school for the placement");
			} 
		}
		
		// registerDate
		if (iwc.isParameterSet(CentralPlacementEditor.PARAM_PLACEMENT_DATE)) {
			IWTimestamp today = IWTimestamp.RightNow();
			today.setAsDate();    
			IWTimestamp placeStamp;
			String placeDateStr = iwc.getParameter(CentralPlacementEditor.PARAM_PLACEMENT_DATE);
			if (!placeDateStr.equals("")) {
				placeStamp= new IWTimestamp(placeDateStr);
				placeStamp.setAsDate();
			  
				// Get dayBeforeRegDate for further use
				IWTimestamp dayBeforeStamp = new IWTimestamp(placeStamp.getDate());
				dayBeforeStamp.addDays(-1);
				dayBeforeRegDate = dayBeforeStamp.getTimestamp();
				//String seeDate = dayBeforeStamp.getDateString("yyyyMMdd"); // test
				//int seeDayInt = dayBeforeRegDate.getDay();  // test
			  
				if (placeStamp.isEarlierThan(today)) {
					throw new CentralPlacementException(KEY_ERROR_PLACEMENT_DATE, 
														"Placement date must be set and cannot be earlier than today");
				} else {
					registerDate = placeStamp.getTimestamp();
				}
			} else {
				throw new CentralPlacementException(KEY_ERROR_PLACEMENT_DATE, 
														"Placement date must be set and cannot be earlier than today");
			}
		}
		
		// registrator
		int currentUser = iwc.getCurrentUserId();
		registrator = currentUser;
		
		// school year
		if (iwc.isParameterSet(CentralPlacementEditor.PARAM_SCHOOL_YEAR)) {
			String providerID = iwc.getParameter(CentralPlacementEditor.PARAM_SCHOOL_YEAR);
			if (providerID.equals("-1")) {
				throw new CentralPlacementException(KEY_ERROR_SCHOOL_YEAR, 
																				"You must chose a school year");
			}
		}
		
		// school group
		if (iwc.isParameterSet(CentralPlacementEditor.PARAM_SCHOOL_GROUP)) {
			String providerID = iwc.getParameter(CentralPlacementEditor.PARAM_SCHOOL_GROUP);
			if (providerID.equals("-1")) {
				throw new CentralPlacementException(KEY_ERROR_SCHOOL_GROUP, 
																				"You must chose a school group");
			} else {
				schoolClassID = Integer.parseInt(providerID);
			}
		}				
	// *** END - Check in params ***
																											 	
						// int studentID, int schoolClassID, Timestamp registerDate, int registrator, String notes)		

	// *** START - Store new placement and end current placement ***
		UserTransaction trans = getSessionContext().getUserTransaction();
		try {
			// Start transaction
			trans.begin();
			// Create new placement
			placement = getSchoolBusiness(iwc).storeSchoolClassMember(studentID, schoolClassID, 
																									registerDate, registrator, notes);
			//Integer newPlaceID = (Integer) placement.getPrimaryKey(); // test
			// End old placement
			if (currentPlacement != null) {
				//Integer currID = (Integer) currentPlacement.getPrimaryKey(); // test
				currentPlacement.setRemovedDate(dayBeforeRegDate);
			}
			trans.commit();
		} catch (Exception e) {
			try {
				trans.rollback();
				e.printStackTrace();
			} catch (IllegalStateException e1) {
				e1.printStackTrace();
			} catch (SecurityException e1) {
				e1.printStackTrace();
			} catch (SystemException e1) {
				e1.printStackTrace();
			}

		}									
	// *** END - Store new placement and end current placement ***
											
		// int studentID, int schoolClassID, Timestamp registerDate, int registrator, String notes
		return placement; 
		
	}

	public SchoolClassMember getCurrentSchoolClassMembership(User user, IWContext iwc)
		throws RemoteException {
		try {
			final SchoolSeason season = getSchoolChoiceBusiness(iwc).getCurrentSeason();
			//int childID = ((Integer) child.getPrimaryKey()).intValue();
			//final SchoolClassMember placement = 
			//    getSchoolBusiness(iwc).getSchoolClassMemberHome().findByUserAndSeason(childID, 2);
			final SchoolClassMember placement =
				getSchoolBusiness(iwc).getSchoolClassMemberHome().findByUserAndSeason(user, season);
			return (null == placement || null != placement.getRemovedDate()) ? null : placement;
		} catch (final FinderException e) {
			return null;
		}
	}

	
	private SchoolBusiness getSchoolBusiness(IWContext iwc) throws RemoteException {
		return (SchoolBusiness) IBOLookup.getServiceInstance(iwc, SchoolBusiness.class);
	}

/*	private SchoolCommuneBusiness getSchoolCommuneBusiness(IWContext iwc) 
																											throws RemoteException {
			return (SchoolCommuneBusiness) IBOLookup.getServiceInstance(iwc, 
																									SchoolCommuneBusiness.class);
	}
*/	
	private SchoolChoiceBusiness getSchoolChoiceBusiness(IWContext iwc) throws RemoteException {
		return (SchoolChoiceBusiness) IBOLookup.getServiceInstance(iwc, SchoolChoiceBusiness.class);
	}

}
