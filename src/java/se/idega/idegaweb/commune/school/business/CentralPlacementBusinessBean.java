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

import se.idega.idegaweb.commune.accounting.invoice.business.RegularPaymentBusiness;
import se.idega.idegaweb.commune.accounting.invoice.data.RegularPaymentEntry;
import se.idega.idegaweb.commune.business.CommuneUserBusiness;
import se.idega.idegaweb.commune.care.resource.business.ResourceBusiness;
import se.idega.idegaweb.commune.care.resource.data.ResourceClassMember;
import se.idega.idegaweb.commune.care.resource.data.ResourceClassMemberHome;
import se.idega.idegaweb.commune.school.presentation.CentralPlacementEditor;

import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolCategory;
import com.idega.block.school.data.SchoolCategoryHome;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.block.school.data.SchoolClassMemberHome;
import com.idega.block.school.data.SchoolSeason;
import com.idega.business.IBOLookup;
import com.idega.business.IBOServiceBean;
import com.idega.data.IDOLookup;
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
																							
	//  Keys for error messages
	private static final String KP = "central_placement_business.";
	private static final String KEY_ERROR_CHILD_ID = KP + "error.child_id";
	private static final String KEY_ERROR_SEASON = KP + "error.no.season.found";
	private static final String KEY_ERROR_CATEGORY_ID = KP + "error.category_id";
	private static final String KEY_ERROR_PROVIDER_ID = KP + "error.provider_id";
	private static final String KEY_ERROR_PLACEMENT_DATE = KP + "error.placement_date";
	private static final String KEY_ERROR_LATEST_REMOVED_DATE = KP + "error.latest_removed_date";
	private static final String KEY_ERROR_SCHOOL_TYPE = KP + "error.school_type";
	private static final String KEY_ERROR_SCHOOL_YEAR = KP + "error.school_year";
	private static final String KEY_ERROR_SCHOOL_GROUP = KP + "error.school_group";
	private static final String KEY_ERROR_STORING_PLACEMENT = KP + "error.saving_placement";
	private static final String KEY_ERROR_STUDY_PATH = KP + "error.study_path";
	private static final String KEY_ERROR_PLC_DATE_OUTSIDE_SEASON = KP + "placement_date outside_season";

	/**
	 * Stores a new placement(SchoolClassMember) with resources and ends the current placement
	 */
	public SchoolClassMember storeSchoolClassMember(IWContext iwc, int childID) 
																throws RemoteException, CentralPlacementException {
		int studentID = -1;
		User student = null;
		int schoolClassID = -1;
		int schoolYearID = -1;
		int schoolTypeID = -1;
		int registrator = -1;
		int nativeLanguageID = -1;
		
		String placementDateStr = "-1";
		String sLanguage = null;
		Timestamp registerStamp = null;
		java.sql.Date registerDate = null;
		Timestamp dayBeforeRegStamp = null;
		Date dayBeforeRegDate = null;
		java.sql.Date dayBeforeSqlDate = null;
		//String seeDayBeforeDate = null;
		String notes = null;
		SchoolClassMember newPlacement = null;
		//SchoolClassMember currentPlacement = null;
		SchoolClassMember latestPlacement = null;
		SchoolSeason chosenSeason = null;
		int newPlacementID = -1;
		
	// *** START - Check in params ***
		// pupil
		if (childID == -1) {
			throw new CentralPlacementException(KEY_ERROR_CHILD_ID, "No valid pupil found");
		} else {
			studentID = childID;
			student = getUserBusiness().getUser(studentID);
			if (student == null) 
				throw new CentralPlacementException(KEY_ERROR_CHILD_ID, "No valid pupil found");
			
			try {
				chosenSeason = getSchoolChoiceBusiness().getSchoolSeasonHome().
					findByPrimaryKey(new Integer(getSchoolCommuneSession(iwc).getSchoolSeasonID()));
			} catch (Exception e1) {}
			if (chosenSeason == null)
				throw new CentralPlacementException(KEY_ERROR_SEASON, "Error finding chosen season");
			
			latestPlacement = getLatestPlacementFromElemAndHighSchool(student, chosenSeason);
		}
		
		// operational field
		if (iwc.isParameterSet(CentralPlacementEditor.PARAM_SCHOOL_CATEGORY)) {
			String categoryID = iwc.getParameter(CentralPlacementEditor.PARAM_SCHOOL_CATEGORY);
			if (categoryID.equals("-1")) {
				throw new CentralPlacementException(KEY_ERROR_CATEGORY_ID, 
						"You must chose an operational field for the placement");
			} else {
				SchoolCategoryHome schCatHome = getSchoolBusiness().getSchoolCategoryHome();
			
				SchoolCategory highSchool;
				try {
					highSchool = schCatHome.findHighSchoolCategory();
					String highSchoolPK = (String) highSchool.getPrimaryKey();
				
					// Study path
					if (categoryID.equals(highSchoolPK)) {
						if (iwc.isParameterSet(CentralPlacementEditor.PARAM_STUDY_PATH)) {
							String studyPathID = iwc.getParameter(CentralPlacementEditor.PARAM_STUDY_PATH);
							if (studyPathID.equals("-1")) {
								throw new CentralPlacementException(KEY_ERROR_STUDY_PATH, 
										"You must chose a study path, for a high school placement");
							}						
						} else {
							throw new CentralPlacementException(KEY_ERROR_STUDY_PATH, 
									"You must chose a study path, for a high school placement");
							
						}
					}
				
				} catch (FinderException e1) {
					log(e1);
				}			
			}
		}
		// provider
		if (iwc.isParameterSet(CentralPlacementEditor.PARAM_PROVIDER)) {
			String providerID = iwc.getParameter(CentralPlacementEditor.PARAM_PROVIDER);
			if (providerID.equals("-1")) {
				throw new CentralPlacementException(KEY_ERROR_PROVIDER_ID, 
																				"You must chose a school for the placement");
			} 
		}
		// school type
		if (iwc.isParameterSet(CentralPlacementEditor.PARAM_SCHOOL_TYPE)) {
			String typeID = iwc.getParameter(CentralPlacementEditor.PARAM_SCHOOL_TYPE);
			if (typeID.equals("-1")) {
				throw new CentralPlacementException(KEY_ERROR_SCHOOL_TYPE, 
																				"You must chose a school type");
			} else {
				schoolTypeID = Integer.parseInt(typeID);
			}
		}
		// school year
		if (iwc.isParameterSet(CentralPlacementEditor.PARAM_SCHOOL_YEAR)) {
			String yearID = iwc.getParameter(CentralPlacementEditor.PARAM_SCHOOL_YEAR);
			if (yearID.equals("-1")) {
				throw new CentralPlacementException(KEY_ERROR_SCHOOL_YEAR, 
																				"You must chose a school year");
			} else {
				schoolYearID = Integer.parseInt(yearID);
			}
		}
		
		// language
		if (iwc.isParameterSet(CentralPlacementEditor.PARAM_LANGUAGE)) {
			sLanguage = iwc.getParameter(CentralPlacementEditor.PARAM_LANGUAGE);
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
			//IWTimestamp today = IWTimestamp.RightNow();
			//today.setAsDate();    
			IWTimestamp placeStamp;
			String placeDateStr = iwc.getParameter(CentralPlacementEditor.PARAM_PLACEMENT_DATE);
			if (!placeDateStr.equals("")) {
				placeStamp= new IWTimestamp(placeDateStr);
				placeStamp.setAsDate();
				
				// Check if within chosen season
				java.sql.Date seasonStartDate = chosenSeason.getSchoolSeasonStart();
				java.sql.Date seasonEndDate = chosenSeason.getSchoolSeasonEnd();
				IWTimestamp seasonStartStamp = new IWTimestamp(seasonStartDate.toString());
				IWTimestamp seasonEndStamp = new IWTimestamp(seasonEndDate.toString());
				seasonStartStamp.setAsDate();
				seasonEndStamp.setAsDate();
				
				if (placeStamp.isEarlierThan(seasonStartStamp) 
						|| seasonEndStamp.isEarlierThan(placeStamp)) {
					throw new CentralPlacementException(KEY_ERROR_PLC_DATE_OUTSIDE_SEASON,
							"Placement date outside chosen seasons date boundries");
				}
			  
				// Get dayBeforeRegDate for further use
				IWTimestamp dayBeforeStamp = new IWTimestamp(placeStamp.getDate());
				dayBeforeStamp.addDays(-1);
				dayBeforeRegStamp = dayBeforeStamp.getTimestamp();
				dayBeforeRegDate = dayBeforeStamp.getDate();
				dayBeforeSqlDate = new java.sql.Date(dayBeforeRegDate.getTime());
				
		/* Below *** Removed check if earlier than today ***/
				/*if (placeStamp.isEarlierThan(today)) {
					throw new CentralPlacementException(KEY_ERROR_PLACEMENT_DATE, 
														"Placement date must be set and cannot be earlier than today");
				} else {
				*/
				
				// Check latest placement, if new removed date is before registerdate, throw exception.
				if (latestPlacement != null) {
					Timestamp latestRegDateStamp = latestPlacement.getRegisterDate();
					IWTimestamp latestRegDate = new IWTimestamp(latestRegDateStamp);
					latestRegDate.setAsDate();
					dayBeforeStamp.setAsDate();
					if (dayBeforeStamp.isEarlierThan(latestRegDate)) {
						throw new CentralPlacementException(KEY_ERROR_LATEST_REMOVED_DATE,
								"End date of latest placement, cannot be earlier than its start date");
					}
				}
				
				registerStamp = placeStamp.getTimestamp();
				registerDate = new java.sql.Date(placeStamp.getDate().getTime());
				
				//}
			} else {
				throw new CentralPlacementException(KEY_ERROR_PLACEMENT_DATE, 
														"Placement date must be set");
			}
			placementDateStr = placeDateStr;
		}  else {
			throw new CentralPlacementException(KEY_ERROR_PLACEMENT_DATE, 
												"Placement date must be set");
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
			newPlacement = getSchoolBusiness().storeNewSchoolClassMember(studentID, schoolClassID, 
														schoolYearID, schoolTypeID, registerStamp, registrator, notes, sLanguage);
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
						int pK = Integer.parseInt(iwc.getParameter(CentralPlacementEditor.PARAM_STUDY_PATH));
						newPlacement.setStudyPathId(pK);
					}
				}
				
				//Native language
				if (iwc.isParameterSet(CentralPlacementEditor.PARAM_NATIVE_LANGUAGE)) {
					String studyNativeLangStr = iwc.getParameter(CentralPlacementEditor.PARAM_NATIVE_LANGUAGE);
					if (!studyNativeLangStr.equals("-1")) {
						nativeLanguageID = Integer.parseInt(studyNativeLangStr);
						student.setNativeLanguage(nativeLanguageID);
						student.store();
					}
				}
				
				// Latest invoice date
				if (iwc.isParameterSet(CentralPlacementEditor.PARAM_LATEST_INVOICE_DATE)) {
					IWTimestamp stamp = new IWTimestamp(iwc.getParameter(
																CentralPlacementEditor.PARAM_LATEST_INVOICE_DATE));
					newPlacement.setLatestInvoiceDate(stamp.getTimestamp());
				}
				
				// Resources				
				if (iwc.isParameterSet(CentralPlacementEditor.PARAM_RESOURCES)) {
					String [] arr = iwc.getParameterValues(CentralPlacementEditor.PARAM_RESOURCES);

					for (int i = 0; i < arr.length; i++) {
						int rscPK = Integer.parseInt(arr[i]);
						getResourceBusiness()
							.createResourcePlacement(rscPK, newPlacementID, placementDateStr, registrator);						
					}					
				}
				// Store newPlacement
				newPlacement.store();
				
			//	*** END - Store the rest of the parameters ***	
			}
			
			// End old placement
			if (latestPlacement != null) {
				// Set removed date 
				latestPlacement.setRemovedDate(dayBeforeRegStamp);
				latestPlacement.store();
				
				// finish old resource placements
				Collection rscPlaces = getResourceBusiness().getResourcePlacementsByMemberId(
																				(Integer) latestPlacement.getPrimaryKey());
				for (Iterator iter = rscPlaces.iterator(); iter.hasNext();) {
					ResourceClassMember rscPlace = (ResourceClassMember) iter.next();
					rscPlace.setEndDate(dayBeforeRegDate);
					//String seeDate = seeDayBeforeDate;
					rscPlace.store();
				}
				
				// Finish ongoing regular payments
				School provider = latestPlacement.getSchoolClass().getSchool();
				Collection regPayEntries = getRegularPaymentBusiness()
															.findOngoingRegularPaymentsForUserAndSchoolByDate(
																									student, provider, registerDate);
				for (Iterator iter = regPayEntries.iterator(); iter.hasNext();) {
					RegularPaymentEntry regPay = (RegularPaymentEntry) iter.next();
					regPay.setTo(dayBeforeSqlDate);					
				}
			}
			trans.commit();
			

		} catch (Exception e) {
			try {
				trans.rollback();
				e.printStackTrace();
				throw new CentralPlacementException(KEY_ERROR_STORING_PLACEMENT,
															"Error storing new placement. Transaction is rolled back.");					
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
			season = getSchoolChoiceBusiness().getSchoolSeasonHome().findSeasonByDate(new IWTimestamp().getDate());
		} catch (Exception e) {}
		
		if (season == null) {
			try {
				season = getSchoolChoiceBusiness().getCurrentSeason();
			} catch (Exception e1) {
				e1.printStackTrace();
			} 
		}
		
		return season;
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
	
	private SchoolBusiness getSchoolBusiness() throws RemoteException {
		return (SchoolBusiness) getServiceInstance(SchoolBusiness.class);
	}

	private ResourceBusiness getResourceBusiness() throws RemoteException {
		return (ResourceBusiness) getServiceInstance(ResourceBusiness.class);
	}

	private RegularPaymentBusiness getRegularPaymentBusiness() throws RemoteException {
		return (RegularPaymentBusiness) getServiceInstance(RegularPaymentBusiness.class);
	}
	
	private SchoolClassMemberHome getSchoolClassMemberHome() throws RemoteException {
		return (SchoolClassMemberHome) IDOLookup.getHome(SchoolClassMember.class);
	}
	
	public SchoolCategoryHome getSchoolCategoryHome() throws RemoteException {
		return (SchoolCategoryHome) IDOLookup.getHome(SchoolCategory.class);
	}

}
