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
import java.util.Date;
import java.util.Iterator;

import javax.ejb.FinderException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import se.idega.idegaweb.commune.accounting.resource.business.ResourceBusiness;
import se.idega.idegaweb.commune.accounting.resource.data.ResourceClassMember;
import se.idega.idegaweb.commune.school.presentation.CentralPlacementEditor;

import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.data.SchoolCategory;
import com.idega.block.school.data.SchoolCategoryHome;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.block.school.data.SchoolClassMemberHome;
import com.idega.block.school.data.SchoolSeason;
import com.idega.block.school.data.SchoolType;
import com.idega.business.IBOLookup;
import com.idega.business.IBOServiceBean;
import com.idega.data.IDOLookup;
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
		String placementDateStr = "-1";
		Timestamp registerDate = null;
		Timestamp dayBeforeRegDate = null;
		String notes = null;
		SchoolClassMember newPlacement = null;
		SchoolClassMember currentPlacement = null;
		SchoolClassMember latestPlacement = null;
		int newPlacementID = -1;
		
		// Get current placement from user http session. Put there from CentralPlacementEditor
		User pupil = (User) iwc.getSession().getAttribute(CentralPlacementEditor.SESSION_KEY_CHILD);
		if (pupil != null) {
			currentPlacement = getCurrentSchoolClassMembership(pupil, iwc);
			latestPlacement = getLatestPlacement(iwc, pupil);			
		}
		Integer currID = null;
		if (currentPlacement != null)
			currID = (Integer) currentPlacement.getPrimaryKey(); // test
		Integer latestId = null;
		if (latestPlacement != null)
			latestId = (Integer) latestPlacement.getPrimaryKey(); // test

				
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

		// school year
		if (iwc.isParameterSet(CentralPlacementEditor.PARAM_SCHOOL_YEAR)) {
			String yearID = iwc.getParameter(CentralPlacementEditor.PARAM_SCHOOL_YEAR);
			if (yearID.equals("-1")) {
				throw new CentralPlacementException(KEY_ERROR_SCHOOL_YEAR, 
																				"You must chose a school year");
			}
		}
		
		// school group
		if (iwc.isParameterSet(CentralPlacementEditor.PARAM_SCHOOL_GROUP)) {
			String groupID = iwc.getParameter(CentralPlacementEditor.PARAM_SCHOOL_GROUP);
			if (groupID.equals("-1")) {
				throw new CentralPlacementException(KEY_ERROR_SCHOOL_GROUP, 
																				"You must chose a school group");
			} else {
				schoolClassID = Integer.parseInt(groupID);
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
			placementDateStr = placeDateStr;
		}
		
		// registrator
		int currentUser = iwc.getCurrentUserId();
		registrator = currentUser;
						
	// *** END - Check in params ***
																											 	
	// *** START - Store new placement and end current placement ***
		UserTransaction trans = getSessionContext().getUserTransaction();
		try {
			// Start transaction
			trans.begin();
			// Create new placement
			newPlacement = getSchoolBusiness(iwc).storeSchoolClassMember(studentID, schoolClassID, 
																									registerDate, registrator, notes);
			if (newPlacement != null) {
			// *** START - Store the rest of the parameters ***
				newPlacementID = ((Integer) newPlacement.getPrimaryKey()).intValue(); // test
				
				// Compensation by agreement
				if (iwc.isParameterSet(CentralPlacementEditor.PARAM_PAYMENT_BY_AGREEMENT) &&
								!iwc.getParameter(CentralPlacementEditor.PARAM_PAYMENT_BY_AGREEMENT).
																															equals("-1")) {
					String value = 
									iwc.getParameter(CentralPlacementEditor.PARAM_PAYMENT_BY_AGREEMENT);
					if (value.equals(CentralPlacementEditor.KEY_DROPDOWN_YES)) {
						newPlacement.setHasCompensationByAgreement(true);
					} else if (value.equals(CentralPlacementEditor.KEY_DROPDOWN_NO)) {
						newPlacement.setHasCompensationByAgreement(false);
					}
				}
				// Placement paragraph
				if (iwc.isParameterSet(CentralPlacementEditor.PARAM_PLACEMENT_PARAGRAPH)) {
					newPlacement.setPlacementParagraph(
									iwc.getParameter(CentralPlacementEditor.PARAM_PLACEMENT_PARAGRAPH));					
				}
				// Invoice interval
				if (iwc.isParameterSet(CentralPlacementEditor.PARAM_INVOICE_INTERVAL)) {
					newPlacement.setInvoiceInterval(
										iwc.getParameter(CentralPlacementEditor.PARAM_INVOICE_INTERVAL));
				}
				// Study path
				if (iwc.isParameterSet(CentralPlacementEditor.PARAM_STUDY_PATH)) {
					String studyPathIDStr = iwc.getParameter(CentralPlacementEditor.PARAM_STUDY_PATH);
					if (!studyPathIDStr.equals("-1")) {
						int pK = Integer.parseInt(
													iwc.getParameter(CentralPlacementEditor.PARAM_STUDY_PATH));
						newPlacement.setStudyPathId(pK);
					}
				}
				// Resources				
				if (iwc.isParameterSet(CentralPlacementEditor.PARAM_RESOURCES)) {
					String [] arr = iwc.getParameterValues(CentralPlacementEditor.PARAM_RESOURCES);
					for (int i = 0; i < arr.length; i++) {
						int rscPK = Integer.parseInt(arr[i]);
						ResourceClassMember rscPlace = getResourceBusiness(iwc)
											.createResourcePlacement(rscPK, newPlacementID, placementDateStr);						
						Integer rscPlPK = (Integer) rscPlace.getPrimaryKey();
						int intPK = rscPlPK.intValue();
					}					
				}
				// Store newPlacement
				newPlacement.store();
				
			//	*** END - Store the rest of the parameters ***	
			}
			
			// End old placement
			if (latestPlacement != null) {
				latestPlacement.setRemovedDate(dayBeforeRegDate);
				latestPlacement.store();
				// finish old placements
				Collection rscPlaces = getResourceBusiness(iwc).getResourcePlacementsByMemberId(
																				(Integer) latestPlacement.getPrimaryKey());
				for (Iterator iter = rscPlaces.iterator(); iter.hasNext();) {
					ResourceClassMember rscPlace = (ResourceClassMember) iter.next();
					rscPlace.setEndDate(dayBeforeRegDate);
				}
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
		return newPlacement; 
		
	}

	public SchoolClassMember getCurrentSchoolClassMembership(User user, IWContext iwc)
		throws RemoteException {
		try {
			final SchoolSeason season = getSchoolChoiceBusiness(iwc).getCurrentSeason();

			//int childID = ((Integer) child.getPrimaryKey()).intValue();
			//final SchoolClassMember placement = 
			//    getSchoolBusiness(iwc).getSchoolClassMemberHome().findByUserAndSeason(childID, 2);

			final SchoolClassMember placement = getSchoolBusiness(iwc).getSchoolClassMemberHome()
																							.findByUserAndSeason(user, season);
			Integer PK = (Integer) placement.getPrimaryKey();
			Date removedDate = placement.getRemovedDate();
			if (placement == null || removedDate != null)
				return null;
			else
				return placement;
				
			//return (null == placement || null != placement.getRemovedDate()) ? null : placement;
		} catch (final FinderException e) {
			return null;
		}
	}
	
	public SchoolClassMember getLatestPlacement(IWContext iwc, User pupil) 
																											throws RemoteException {
		String schCategoryStr = iwc.getParameter(CentralPlacementEditor.PARAM_SCHOOL_CATEGORY);
		SchoolCategory schCat = null;
		SchoolClassMember mbr = null;
		try {
			if (schCategoryStr != null) {
				schCat = getSchoolCategoryHome().findByPrimaryKey(schCategoryStr);			
			}
			if (schCat != null && pupil != null) {
				// Find latest with removed_date == null
				mbr = getSchoolClassMemberHome().findLatestByUserSchCatNoRemovedDate(pupil, schCat);						
			}
		} catch (FinderException fe1) {
			try {
				// Find latest with removed_date set, by register_date
				mbr = getSchoolClassMemberHome().findLatestByUserAndSchCategory(pupil, schCat);											
			} catch (FinderException fe2) {
				return null;
			}
		}
		return mbr;		
	}

	
	private SchoolBusiness getSchoolBusiness(IWContext iwc) throws RemoteException {
		return (SchoolBusiness) IBOLookup.getServiceInstance(iwc, SchoolBusiness.class);
	}

	private ResourceBusiness getResourceBusiness(IWContext iwc) throws RemoteException {
		return (ResourceBusiness) IBOLookup.getServiceInstance(iwc, ResourceBusiness.class);
	}

/*	private UserBusiness getUserBusiness(IWContext iwc) throws RemoteException {
		return (UserBusiness) IBOLookup.getServiceInstance(iwc, UserBusiness.class);
	}*/

/*	private SchoolCommuneBusiness getSchoolCommuneBusiness(IWContext iwc) 
																											throws RemoteException {
			return (SchoolCommuneBusiness) IBOLookup.getServiceInstance(iwc, 
																									SchoolCommuneBusiness.class);
	}
*/	
	private SchoolChoiceBusiness getSchoolChoiceBusiness(IWContext iwc) throws RemoteException {
		return (SchoolChoiceBusiness) IBOLookup.getServiceInstance(iwc, SchoolChoiceBusiness.class);
	}
	
	private SchoolClassMemberHome getSchoolClassMemberHome() throws RemoteException {
		return (SchoolClassMemberHome) IDOLookup.getHome(SchoolClassMember.class);
	}
	
	public SchoolCategoryHome getSchoolCategoryHome() throws RemoteException {
		return (SchoolCategoryHome) IDOLookup.getHome(SchoolCategory.class);
	}

}
