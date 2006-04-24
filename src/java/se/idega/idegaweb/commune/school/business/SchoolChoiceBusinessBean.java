package se.idega.idegaweb.commune.school.business;

import is.idega.block.family.business.FamilyLogic;
import is.idega.block.family.business.NoChildrenFound;
import is.idega.block.family.business.NoCustodianFound;

import java.io.InputStream;
import java.io.OutputStream;
import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import se.cubecon.bun24.viewpoint.business.ViewpointBusiness;
import se.cubecon.bun24.viewpoint.data.SubCategory;
import se.idega.idegaweb.commune.business.CommuneUserBusiness;
import se.idega.idegaweb.commune.care.business.CareBusiness;
import se.idega.idegaweb.commune.care.data.CurrentSchoolSeason;
import se.idega.idegaweb.commune.care.data.CurrentSchoolSeasonHome;
import se.idega.idegaweb.commune.care.resource.business.ResourceBusiness;
import se.idega.idegaweb.commune.care.resource.data.ResourceClassMember;
import se.idega.idegaweb.commune.message.business.CommuneMessageBusiness;
import se.idega.idegaweb.commune.printing.business.DocumentBusiness;
import se.idega.idegaweb.commune.school.data.SchoolChoice;
import se.idega.idegaweb.commune.school.data.SchoolChoiceHome;
import se.idega.idegaweb.commune.school.data.SchoolChoiceReminder;
import se.idega.idegaweb.commune.school.data.SchoolChoiceReminderHome;

import com.idega.block.process.data.Case;
import com.idega.block.process.data.CaseStatus;
import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.business.SchoolBusinessBean;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolClass;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.block.school.data.SchoolClassMemberHome;
import com.idega.block.school.data.SchoolSeason;
import com.idega.block.school.data.SchoolStudyPath;
import com.idega.block.school.data.SchoolStudyPathGroup;
import com.idega.block.school.data.SchoolStudyPathGroupHome;
import com.idega.block.school.data.SchoolStudyPathHome;
import com.idega.block.school.data.SchoolUser;
import com.idega.block.school.data.SchoolYear;
import com.idega.block.school.data.SchoolYearHome;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.core.file.data.ICFile;
import com.idega.core.file.data.ICFileHome;
import com.idega.data.IDOCreateException;
import com.idega.data.IDOException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.IDORelationshipException;
import com.idega.data.IDOStoreException;
import com.idega.idegaweb.IWPropertyList;
import com.idega.io.MemoryFileBuffer;
import com.idega.io.MemoryInputStream;
import com.idega.io.MemoryOutputStream;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.util.Age;
import com.idega.util.IWTimestamp;
import com.idega.util.PersonalIDFormatter;
import com.idega.util.text.Name;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

/**
 * Title: Description: Copyright: Copyright (c) 2002-2003 Company:
 * 
 * @author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @author <a href="http://www.staffannoteberg.com">Staffan N�teberg</a> 
 * @version 1.0
 */
public class SchoolChoiceBusinessBean extends com.idega.block.process.business.CaseBusinessBean implements SchoolChoiceBusiness, com.idega.block.process.business.CaseBusiness {

	//Localization keys ,Messages test merge 1.
	private static final String KP = "school_editor.";
	private static final String KEY_REACTIVATE_SUBJECT1 = KP + "sch_admin_reactivate_subject1";
	//private static final String KEY_REACTIVATE_SUBJECT2 = KP + "sch_admin_reactivate_subject2";
	private static final String KEY_REACTIVATE_BODY1 = KP + "sch_admin_reactivate_body1";
	private static final String KEY_REACTIVATE_BODY2 = KP + "sch_admin_reactivate_body2";
	
	
	private final static int REMINDER_FONTSIZE = 12;
	private final static Font SERIF_FONT = FontFactory.getFont(FontFactory.TIMES, REMINDER_FONTSIZE);
	//private final static Font SANSSERIF_FONT = FontFactory.getFont(FontFactory.HELVETICA, REMINDER_FONTSIZE - 1);

	private Font defaultParagraphFont;
	private Font defaultTextFont;
	
	private CareBusiness careBusiness = null;
	

	
	/**
	 * Method getDefaultTextFont.
	 * @return Font
	 */
	private Font getDefaultTextFont() {
		if (this.defaultTextFont == null) {
			this.defaultTextFont = new Font(Font.HELVETICA);
			this.defaultTextFont.setSize(12);
		}
		return this.defaultTextFont;
	}
	
	/**
	 * Method getDefaultParagraphFont.
	 * @return Font
	 */
	private Font getDefaultParagraphFont() {
		if (this.defaultParagraphFont == null) {
			this.defaultParagraphFont = new Font(Font.HELVETICA, 12, Font.BOLD);
		}
		return this.defaultParagraphFont;
	}

	/*private Font getTextFont() {
	 return getDefaultTextFont();
	 }*/

	public String getBundleIdentifier() {
		return se.idega.idegaweb.commune.presentation.CommuneBlock.IW_BUNDLE_IDENTIFIER;
	}
	public SchoolBusiness getSchoolBusiness() throws java.rmi.RemoteException {
		return (SchoolBusiness) this.getServiceInstance(SchoolBusiness.class);
	}
	public CommuneMessageBusiness getMessageBusiness() throws RemoteException {
		return (CommuneMessageBusiness) this.getServiceInstance(CommuneMessageBusiness.class);
	}

	public FamilyLogic getMemberFamilyLogic() throws RemoteException {
		return (FamilyLogic) this.getServiceInstance(FamilyLogic.class);
	}
	public CommuneUserBusiness getUserBusiness() {
		try {
			return (CommuneUserBusiness) this.getServiceInstance(CommuneUserBusiness.class);
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}

	

	public SchoolChoiceHome getSchoolChoiceHome() throws java.rmi.RemoteException {
		return (SchoolChoiceHome) this.getIDOHome(SchoolChoice.class);
	}

	public SchoolClassMemberHome getSchoolClassMemberHome() throws RemoteException {
		return (SchoolClassMemberHome) this.getIDOHome(SchoolClassMember.class);
	}

	public SchoolYearHome getSchoolYearHome() throws RemoteException {
		return (SchoolYearHome) this.getIDOHome(SchoolYear.class);
	}

	public int getPreviousSeasonId() throws RemoteException {
		final int currentSeasonId = getCommuneSchoolBusiness().getCurrentSchoolSeasonID();
		return getCommuneSchoolBusiness().getPreviousSchoolSeasonID(currentSeasonId);
	}

	public SchoolChoice getSchoolChoice(int schoolChoiceId) throws FinderException, RemoteException {
		return getSchoolChoiceHome().findByPrimaryKey(new Integer(schoolChoiceId));
	}
	public School getSchool(int school) throws RemoteException {
		return getSchoolBusiness().getSchool(new Integer(school));
	}

	public SchoolChoice createSchoolChangeChoice(int userId, int childId, int school_type_id, int current_school, int chosen_school, int schoolYearID, int currentYearID, int method, int workSituation1, int workSituation2, String language, String message, boolean keepChildrenCare, boolean autoAssign, boolean custodiansAgree, boolean schoolCatalogue, Date placementDate, SchoolSeason season, String extraMessage) throws IDOCreateException {
		try {
			java.sql.Timestamp time = new java.sql.Timestamp(System.currentTimeMillis());
			CaseStatus unHandledStatus = getCaseStatusMoved();
			IWTimestamp stamp = new IWTimestamp();
			SchoolChoice choice = createSchoolChoice(stamp, userId, childId, school_type_id, current_school, chosen_school, schoolYearID, currentYearID, 1, method, workSituation1, workSituation2, language, message, time, true, keepChildrenCare, autoAssign, custodiansAgree, schoolCatalogue, unHandledStatus, null, placementDate, season, extraMessage);
			ArrayList choices = new ArrayList(1);
			choices.add(choice);
			handleSeparatedParentApplication(userId, choices, true);
			handleSchoolChangeHeadMasters(choice, getUser(childId), current_school, chosen_school);

			//int previousSeasonID = getCommuneSchoolBusiness().getPreviousSchoolSeasonID(getCommuneSchoolBusiness().getCurrentSchoolSeasonID());
			//if (previousSeasonID != -1)
			//	getCommuneSchoolBusiness().setNeedsSpecialAttention(childId, previousSeasonID, true);

			return choice;
		}
		catch (Exception ex) {
			throw new IDOCreateException(ex.getMessage());
		}
	}

	public List createSchoolChoices(int userId, int childId, int school_type_id, int current_school, int chosen_school_1, int chosen_school_2, int chosen_school_3, int schoolYearID, int currentYearID, int method, int workSituation1, int workSituation2, String language, String message, boolean changeOfSchool, boolean keepChildrenCare, boolean autoAssign, boolean custodiansAgree, boolean schoolCatalogue, Date placementDate, SchoolSeason season, String[] extraMessages) throws IDOCreateException {
		if (placementDate != null) {
			try {
				try {
					season = getCareBusiness().getSchoolSeasonHome().findSeasonByDate(getSchoolBusiness().getCategoryElementarySchool(), placementDate);
				}
				catch (FinderException e) {
					season = getCareBusiness().getCurrentSeason();
				}
			}
			catch (FinderException fe) {
				fe.printStackTrace();
			}
			catch (RemoteException re) {
				throw new IBORuntimeException(re);
			}
		}
		
		if (changeOfSchool) {
			SchoolChoice choice = createSchoolChangeChoice(userId, childId, school_type_id, current_school, chosen_school_1, schoolYearID, currentYearID, method, workSituation1, workSituation2, language, message, keepChildrenCare, autoAssign, custodiansAgree, schoolCatalogue, placementDate, season, extraMessages[0]);
			ArrayList list = new ArrayList(1);
			list.add(choice);
			return list;
		}
		else {
			int caseCount = 3;
			java.sql.Timestamp time = new java.sql.Timestamp(System.currentTimeMillis());
			List returnList = new Vector(3);
			javax.transaction.UserTransaction trans = this.getSessionContext().getUserTransaction();
			try {
				trans.begin();
				CaseStatus first = getCaseStatusPreliminary();
				CaseStatus other = getCaseStatusInactive();
				int[] schoolIds = {chosen_school_1, chosen_school_2, chosen_school_3};
				SchoolChoice choice = null;
				IWTimestamp stamp = new IWTimestamp();
				for (int i = 0; i < caseCount; i++) {
					choice = createSchoolChoice(stamp, userId, childId, school_type_id, current_school, schoolIds[i], schoolYearID, currentYearID, i + 1, method, workSituation1, workSituation2, language, message, time, changeOfSchool, keepChildrenCare, autoAssign, custodiansAgree, schoolCatalogue, i == 0 ? first : other, choice, placementDate, season, extraMessages[i]);
					returnList.add(choice);
				}
				handleSeparatedParentApplication(userId, returnList, false);
				trans.commit();

				int previousSeasonID = -1;
				if (season != null) {
					previousSeasonID = getCommuneSchoolBusiness().getPreviousSchoolSeasonID(((Integer)season.getPrimaryKey()).intValue());
				}
				else {
					previousSeasonID = getCommuneSchoolBusiness().getPreviousSchoolSeasonID(getCommuneSchoolBusiness().getCurrentSchoolSeasonID());
				}
				
				if (previousSeasonID != -1) {
					getCommuneSchoolBusiness().setNeedsSpecialAttention(childId, previousSeasonID, true);
				}

				return returnList;
			}
			catch (Exception ex) {
				try {
					trans.rollback();
				}
				catch (javax.transaction.SystemException e) {
					throw new IDOCreateException(e.getMessage());
				}
				throw new IDOCreateException(ex.getMessage());
			}
		}
	}

	
	public List createSchoolChoices(int userId, int childId, int school_type_id, int current_school, int chosen_school_1, int chosen_school_2, int chosen_school_3, int schoolYearID, int currentYearID, int method, int workSituation1, int workSituation2, String language, String message, boolean changeOfSchool, boolean keepChildrenCare, boolean autoAssign, boolean custodiansAgree, boolean schoolCatalogue, Date placementDate, SchoolSeason season, boolean nativeLangIsChecked, int nativeLang, String[] extraMessages, boolean useAsAdmin) throws IDOCreateException {
		return createSchoolChoices(userId, childId, school_type_id, current_school, chosen_school_1, chosen_school_2, chosen_school_3, schoolYearID, currentYearID, method, workSituation1, workSituation2, language, message, changeOfSchool, keepChildrenCare, autoAssign, custodiansAgree, schoolCatalogue, placementDate, season, nativeLangIsChecked, nativeLang, extraMessages, useAsAdmin, false, -1);
	}
	
	/**
	 * Same as above method only Native laguage is added
	 * 
	 * @param userId
	 * @param childId
	 * @param school_type_id
	 * @param current_school
	 * @param chosen_school_1
	 * @param chosen_school_2
	 * @param chosen_school_3
	 * @param grade
	 * @param method
	 * @param workSituation1
	 * @param workSituation2
	 * @param language
	 * @param message
	 * @param changeOfSchool
	 * @param keepChildrenCare
	 * @param autoAssign
	 * @param custodiansAgree
	 * @param schoolCatalogue
	 * @param placementDate
	 * @param season
	 * @param nativeLangIsChecked
	 * @param NativeLang
	 * @return @throws
	 *         IDOCreateException
	 */
	public List createSchoolChoices(int userId, int childId, int school_type_id, int current_school, int chosen_school_1, int chosen_school_2, int chosen_school_3, int schoolYearID, int currentYearID, int method, int workSituation1, int workSituation2, String language, String message, boolean changeOfSchool, boolean keepChildrenCare, boolean autoAssign, boolean custodiansAgree, boolean schoolCatalogue, Date placementDate, SchoolSeason season, boolean nativeLangIsChecked, int nativeLang, String[] extraMessages, boolean useAsAdmin, boolean usePriority, int handicraftId) throws IDOCreateException {
		boolean isInSCPeriod = isInSchoolChoicePeriod();		
		if (placementDate != null) {
			
			try {
				try {
					season = getCareBusiness().getSchoolSeasonHome().findSeasonByDate(getSchoolBusiness().getCategoryElementarySchool(), placementDate);
				}
				catch (FinderException e) {
					season = getCareBusiness().getCurrentSeason();
				}
			}
			catch (FinderException fe) {
				fe.printStackTrace();
			}
			catch (RemoteException re) {
				throw new IBORuntimeException(re);
			}
		}
		
		if (changeOfSchool) {

			try {
				// Set native language for child if param is set
				if (childId != -1 && nativeLangIsChecked && nativeLang != -1) {
					User child = getUserBusiness().getUser(childId);
					if (child != null) {
						child.setNativeLanguage(nativeLang);
						child.store();
					}
				}
			}
			catch (Exception ex) {
				throw new IDOCreateException(ex.getMessage());
			}

			SchoolChoice choice = createSchoolChangeChoice(userId, childId, school_type_id, current_school, chosen_school_1, schoolYearID, currentYearID, method, workSituation1, workSituation2, language, message, keepChildrenCare, autoAssign, custodiansAgree, schoolCatalogue, placementDate, season, extraMessages[0]);
			ArrayList list = new ArrayList(1);
			list.add(choice);
			return list;
		}
		int caseCount = 3;
		java.sql.Timestamp time = new java.sql.Timestamp(System.currentTimeMillis());
		List returnList = new Vector(3);
		javax.transaction.UserTransaction trans = this.getSessionContext().getUserTransaction();
		try {
			trans.begin();
			CaseStatus first = getCaseStatusPreliminary();
			CaseStatus other = getCaseStatusInactive();
			int[] schoolIds = {chosen_school_1, chosen_school_2, chosen_school_3};
			SchoolChoice choice = null;
			IWTimestamp stamp = new IWTimestamp();
			
			for (int i = 0; i < caseCount; i++) {
				choice = createSchoolChoice(stamp, userId, childId, school_type_id, current_school, schoolIds[i], schoolYearID, currentYearID, i + 1, method, workSituation1, workSituation2, language, message, time, changeOfSchool, keepChildrenCare, autoAssign, custodiansAgree, schoolCatalogue, i == 0 ? first : other, choice, placementDate, season, extraMessages[i], usePriority, handicraftId);
				returnList.add(choice);
			}
			if (useAsAdmin){
				handleSeparatedParentApplication(userId, returnList, false, true, true);	
			}
			else if (isInSCPeriod){
				handleSeparatedParentApplication(userId, returnList, false, false, true);
			}
			else {
				handleSeparatedParentApplicationNewlyMovedIn(userId, returnList, false, true);
			}
			trans.commit();

			int previousSeasonID = -1;
			if (season != null) {
				previousSeasonID = getCommuneSchoolBusiness().getPreviousSchoolSeasonID(((Integer)season.getPrimaryKey()).intValue());
			}
			else {
				previousSeasonID = getCommuneSchoolBusiness().getPreviousSchoolSeasonID(getCommuneSchoolBusiness().getCurrentSchoolSeasonID());
			}
			
			if (previousSeasonID != -1) {
				getCommuneSchoolBusiness().setNeedsSpecialAttention(childId, previousSeasonID, true);
			}
			// Set native language for child if param is set
			if (childId != -1 && nativeLangIsChecked && nativeLang != -1) {
				User child = getUserBusiness().getUser(childId);
				if (child != null) {
					child.setNativeLanguage(nativeLang);
					child.store();
				}
			}

			return returnList;
		}
		catch (Exception ex) {
			try {
				trans.rollback();
			}
			catch (javax.transaction.SystemException e) {
				throw new IDOCreateException(e.getMessage());
			}
			throw new IDOCreateException(ex.getMessage());
		}
	}
	
	private SchoolChoice createSchoolChoice(IWTimestamp stamp, int userId, int childId, int school_type_id, int current_school, int chosen_school, int schoolYearID, int currentYearID, int choiceOrder, int method, int workSituation1, int workSituation2, String language, String message, java.sql.Timestamp choiceDate, boolean changeOfSchool, boolean keepChildrenCare, boolean autoAssign, boolean custodiansAgree, boolean schoolCatalogue, CaseStatus caseStatus, Case parentCase, Date placementDate, SchoolSeason season, String extraMessage) throws CreateException, RemoteException {
		return createSchoolChoice(stamp, userId, childId, school_type_id, current_school, chosen_school, schoolYearID, currentYearID, choiceOrder, method, workSituation1, workSituation2, language, message, choiceDate, changeOfSchool, keepChildrenCare, autoAssign, custodiansAgree, schoolCatalogue, caseStatus, parentCase, placementDate, season, extraMessage, false, -1);	
	}
	
	private SchoolChoice createSchoolChoice(IWTimestamp stamp, int userId, int childId, int school_type_id, int current_school, int chosen_school, int schoolYearID, int currentYearID, int choiceOrder, int method, int workSituation1, int workSituation2, String language, String message, java.sql.Timestamp choiceDate, boolean changeOfSchool, boolean keepChildrenCare, boolean autoAssign, boolean custodiansAgree, boolean schoolCatalogue, CaseStatus caseStatus, Case parentCase, Date placementDate, SchoolSeason season, String extraMessage, boolean usePriority, int handicraftId) throws CreateException, RemoteException {
		if (season == null) {
			try {
				season = getCareBusiness().getCurrentSeason();
			}
			catch (FinderException fex) {
				season = null;
			}
		}

		SchoolChoice choice = null;

		if (season != null) {
			Collection choices = this.findByStudentAndSeason(childId, ((Integer) season.getPrimaryKey()).intValue());
			if (!choices.isEmpty()) {
				Iterator iter = choices.iterator();
				while (iter.hasNext()) {
					SchoolChoice element = (SchoolChoice) iter.next();
					if (element.getChoiceOrder() == choiceOrder) {
						if (changeOfSchool) {
							if (element.getStatus().equals(this.getCaseStatusMoved().getStatus())) {
								choice = element;
								continue;
							}
						}
						else {
							choice = element;
							continue;
						}
					}
				}
			}
		}

		if (choice == null) {
			SchoolChoiceHome home = this.getSchoolChoiceHome();
			choice = home.create();
		}

		try {
			choice.setOwner(getUser(userId));
		}
		catch (FinderException fex) {
			throw new IDOCreateException(fex);
		}
		choice.setChildId(childId);
		if (current_school > 0) {
			choice.setCurrentSchoolId(current_school);
		}
		if (chosen_school != -1) {
			choice.setChosenSchoolId(chosen_school);
		}
		if (schoolYearID != -1) {
			choice.setSchoolYear(schoolYearID);
		}
		if (currentYearID != -1) {
			choice.setCurrentSchoolYear(currentYearID);
		}
		choice.setChoiceOrder(choiceOrder);
		choice.setMethod(method);
		choice.setWorksituation1(workSituation1);
		choice.setWorksituation2(workSituation2);
		choice.setLanguageChoice(language);
		choice.setChangeOfSchool(changeOfSchool);
		choice.setKeepChildrenCare(keepChildrenCare);
		choice.setFreetimeInThisSchool(false);
		choice.setAutoAssign(autoAssign);
		choice.setCustodiansAgree(custodiansAgree);
		choice.setSchoolCatalogue(schoolCatalogue);
		choice.setSchoolChoiceDate(choiceDate);
		choice.setSchoolTypeId(school_type_id);
		choice.setMessage(message);
		choice.setExtraChoiceMessage(extraMessage);
		if (season != null) {
			Integer seasonId = (Integer) season.getPrimaryKey();
			choice.setSchoolSeasonId(seasonId.intValue());
		}
		if (placementDate != null) {
			choice.setPlacementDate(placementDate);
		}
		stamp.addSeconds(1 - choiceOrder);
		choice.setCreated(stamp.getTimestamp());
		choice.setCaseStatus(caseStatus);

		//If school is administrated by BUN, set the case handler so that BUN will see it in the UserCase list.
		School provider = getSchoolBusiness().getSchool(new Integer(chosen_school));
		if (provider.getCentralizedAdministration()) {
			choice.setHandler(getBunGroup());
		}

		/*if (caseStatus.getStatus().equalsIgnoreCase(getCaseStatusPreliminary().getStatus())) {
		 sendMessageToParentOrChild(choice, choice.getOwner(), choice.getChild(), getPreliminaryMessageSubject(), getPreliminaryMessageBody(choice),SchoolChoiceMessagePdfHandler.CODE_PRELIMINARY);
		 //			getMessageBusiness().createUserMessage(choice.getOwner(), getPreliminaryMessageSubject(), getPreliminaryMessageBody(choice));
		 }*/		
		
		if (usePriority) {
			//TODO: spec says: In Danderyd, all children applying to elementary school have priority 
			//if an older sibling is placed with the same provider.
			//shouldn't we check, if child applies to elementary school?
			User applyingChild = getUserBusiness().getUser(new Integer(childId)); 
			choice.setPriority(hasPriority(provider, choice.getOwner(), applyingChild));	
		}
		
		if (handicraftId > 0) {
			choice.setHandicraftId(handicraftId);
		}
		
		if (parentCase != null) {
			choice.setParentCase(parentCase);
		}
		try {
			choice.store();
		}
		catch (IDOStoreException idos) {
			throw new IDOCreateException(idos);
		}
		
		boolean isSCPeriod = isInSchoolChoicePeriod(); 
		
		if (!changeOfSchool){
			if (IWTimestamp.RightNow().isBetween(new IWTimestamp(season.getSchoolSeasonStart()), new IWTimestamp(season.getSchoolSeasonEnd())) || !isSCPeriod) {
				if (choiceOrder <= 1){
					try {
						User student = getUser(childId);
						//Object[] arguments = {student.getNameLastFirst(true), PersonalIDFormatter.format(student.getPersonalID(), this.getIWApplicationContext().getApplicationSettings().getDefaultLocale()), choice.getPlacementDate() != null ? new IWTimestamp(choice.getPlacementDate()).getLocaleDate(this.getIWApplicationContext().getApplicationSettings().getDefaultLocale(), IWTimestamp.SHORT) : "" };
						Object[] arguments = {student.getName(), PersonalIDFormatter.format(student.getPersonalID(), this.getIWApplicationContext().getApplicationSettings().getDefaultLocale()), choice.getPlacementDate() != null ? new IWTimestamp(choice.getPlacementDate()).getLocaleDate(this.getIWApplicationContext().getApplicationSettings().getDefaultLocale(), IWTimestamp.SHORT) : "" };
	
						String subject = getLocalizedString("school_choice.newly_moved_in_choice_subject", "Newly moved in student seeks placement");
						String body = MessageFormat.format(getLocalizedString("school_choice.newly_moved_in_choice_body", "{0}, {1}, wants a placement in your school from {2}."), arguments);
						sendMessageToSchool(chosen_school, subject, body, null);
					}
					catch (FinderException fe) {
						log(fe);
					}
				}
			}
		}
		
		
		return choice;
	}
	
	/**
	 * Child has priority, if he's older sibling will be 
	 * going to the same school in this year
	 * 
	 * @param provider
	 * @param parent
	 * @return
	 */
	public boolean hasPriority(School provider, User parent, User applyingChild) {
		boolean priority = false;
				
		Integer applyingChildId = (Integer) applyingChild.getPrimaryKey();
		//Date applyingChildDateOfBirth = applyingChild.getDateOfBirth();
		
		try {
			SchoolBusinessBean schoolBean = new SchoolBusinessBean();
			
			FamilyLogic familyLogic = this.getMemberFamilyLogic();
			Collection children = familyLogic.getChildrenFor(parent);

			for (Iterator iter = children.iterator(); iter.hasNext();) {
				User child = (User) iter.next();
				
				//check, if current child is not the same applying child
				Integer childId = (Integer) child.getPrimaryKey();				
				if (childId.equals(applyingChildId)) {
					continue;
				}
				
				/* commented out 2005.11.17 by Dainis because Asa asked to do so
				//check, if current child is older as applying child
				//  in other words, if current child birth date is before applying child birth date, than it is OK, else not OK
				Date childDateOfBirth = child.getDateOfBirth();
				if(! childDateOfBirth.before(applyingChildDateOfBirth)) {
					continue;
				}				
				*/
				
				//check, if current child has active placement to the same school
				Integer providerId = (Integer) provider.getPrimaryKey();				
				boolean hasActivePlacement = schoolBean.hasActivePlacement(
						childId.intValue(), 
						providerId.intValue(), 
						schoolBean.getCategoryElementarySchool()); //see spec: ... all children applying to elementary school ...
				if (! hasActivePlacement) {
					continue;
				}
				
				//check, if the school that the sibling goes to offers 
				//the next school year for that sibling
				
				// 1. get school years, which are offered by the school				 
				//Malin: yes, elementary school years are fine.
				// but there is no method to get them, so later, later
				Collection schoolYears = provider.findRelatedSchoolYears(); // contains SchoolYear objects
								 
				// 2. get the school year the child is in now, and type must be elementary_school					
				Collection types = getSchoolBusiness().getSchoolTypesForCategory(schoolBean.getCategoryElementarySchool(), false); //false means do not show freetime types
				SchoolClassMember schoolClassMember = getSchoolClassMemberHome().findLatestByUserAndSchool(childId.intValue(), providerId.intValue(), types);					
				
				SchoolYear schoolYear = schoolClassMember.getSchoolYear();		
				 
				// 3.	check, if there is next school year for that child 
				int ageForNextYear = schoolYear.getSchoolYearAge() + 1;
				boolean hasNextYear = false;
				for (Iterator iter2 = schoolYears.iterator(); iter2.hasNext();) {
					SchoolYear sy = (SchoolYear) iter2.next();
					if (sy.getSchoolYearAge() == ageForNextYear) {
						hasNextYear = true;
						break;
					}
				}
				if(!hasNextYear){
					continue;
				}
				
				//current child escaped all the traps above
				priority = true;
				break;
				
			}
		}

		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		} catch (NoChildrenFound ex) {
			throw new IBORuntimeException(ex);
		} catch (FinderException e) {
			throw new IBORuntimeException(e);
		} catch (IDORelationshipException e) {
			throw new IBORuntimeException(e);
		}

		return priority;
	}

	private void handleSchoolChangeHeadMasters(SchoolChoice choice, User child, int oldSchoolID, int newSchoolID) throws RemoteException {
		if(oldSchoolID != -1){
			sendMessageToSchool(oldSchoolID, getOldHeadmasterSubject(), getOldHeadmasterBody(choice, child, getSchool(newSchoolID)),SchoolChoiceMessagePdfHandler.CODE_OLD_SCHOOL_CHANGE);	
		}
		
		sendMessageToSchool(newSchoolID, getNewHeadMasterSubject(), getNewHeadmasterBody(choice, child, getSchool(oldSchoolID)),SchoolChoiceMessagePdfHandler.CODE_NEW_SCHOOL_CHANGE);	
		
		
	}

	private void handleSeparatedParentApplication(int applicationParentID, List choices, boolean isSchoolChangeApplication) throws RemoteException {
		handleSeparatedParentApplication(applicationParentID, choices, isSchoolChangeApplication, false);
		
	}

	private void handleSeparatedParentApplication(int applicationParentID, List choices, boolean isSchoolChangeApplication, boolean isAdmin) throws RemoteException {
		handleSeparatedParentApplication(applicationParentID, choices, isSchoolChangeApplication, isAdmin, false);
	}
	
	private void handleSeparatedParentApplication(int applicationParentID, List choices, boolean isSchoolChangeApplication, boolean isAdmin, boolean sendToAllParents) throws RemoteException {
		try {
			if (choices != null) {
				SchoolChoice choice = (SchoolChoice) choices.get(0);
				User appParent = getUser(applicationParentID);
				
				String applyingSubject, applyingBody,applyingCode;
				String nonApplyingSubject , nonApplyingBody,nonApplyingCode;
				if (isAdmin){
					nonApplyingSubject = getNonApplyingSeparateParentSubjectAppl();
					nonApplyingBody = getNonApplyingSeparateParentMessageBodyApplAdmin(choices);
					nonApplyingCode = SchoolChoiceMessagePdfHandler.CODE_NONAPPLYING_SINGLEPARENT_APPLICATION_NEW ; //�ndras till admin
					applyingSubject = getNonApplyingSeparateParentSubjectAppl();//getApplyingSeparateParentSubjectAppl();
					applyingBody = nonApplyingBody;
					applyingCode = nonApplyingCode;
				}
				else if (isSchoolChangeApplication) {
					nonApplyingSubject = getNonApplyingSeparateParentSubjectChange();
					nonApplyingBody = getNonApplyingSeparateParentMessageBodyChange(choice, appParent);
					nonApplyingCode = SchoolChoiceMessagePdfHandler.CODE_NONAPPLYING_SINGLEPARENT_APPLICATION_CHANGE ;
					applyingSubject = getApplyingSeparateParentSubjectChange();
					applyingBody = getApplyingSeparateParentMessageBodyChange(choice, appParent);
					applyingCode = SchoolChoiceMessagePdfHandler.CODE_APPLYING_SINGLEPARENT_APPLICATION_CHANGE ;
				}
				else {
					nonApplyingSubject = getNonApplyingSeparateParentSubjectAppl();
					nonApplyingBody = getNonApplyingSeparateParentMessageBodyAppl(choices, appParent);
					nonApplyingCode = SchoolChoiceMessagePdfHandler.CODE_NONAPPLYING_SINGLEPARENT_APPLICATION_NEW ;
					applyingSubject = getPreliminaryMessageSubject();//getApplyingSeparateParentSubjectAppl();
					applyingBody = getPreliminaryMessageBody(choice);//getApplyingSeparateParentMessageBodyAppl(choices, appParent);
					applyingCode = SchoolChoiceMessagePdfHandler.CODE_APPLYING_SINGLEPARENT_APPLICATION_NEW ;
				}

				sendMessageToParents(choice, nonApplyingSubject, nonApplyingBody,nonApplyingCode,applyingSubject,applyingBody,applyingCode,isSchoolChangeApplication, sendToAllParents);
			}
		}
		catch (Exception ex) {
			throw new RemoteException(ex.getMessage());
		}
	}
	
	private void handleSeparatedParentApplicationNewlyMovedIn(int applicationParentID, List choices, boolean isSchoolChangeApplication, boolean sendToAllParents) throws RemoteException {
		try {
			if (choices != null) {
				SchoolChoice choice = (SchoolChoice) choices.get(0);
				User appParent = getUser(applicationParentID);
				//boolean isInSCPeriod = isInSchoolChoicePeriod();
				
				String applyingSubject, applyingBody,applyingCode;
				String nonApplyingSubject , nonApplyingBody,nonApplyingCode;
				
				nonApplyingSubject = getNonApplyingSeparateParentSubjectApplNew();
				nonApplyingBody = getNonApplyingSeparateParentMessageBodyApplNew(choices, appParent);
				nonApplyingCode = SchoolChoiceMessagePdfHandler.CODE_NONAPPLYING_SINGLEPARENT_APPLICATION_NEW ;
				applyingSubject = getPreliminaryMessageSubjectNew();//getApplyingSeparateParentSubjectAppl();
				applyingBody = getPreliminaryMessageBodyNew(choice);//getApplyingSeparateParentMessageBodyAppl(choices, appParent);
				applyingCode = SchoolChoiceMessagePdfHandler.CODE_APPLYING_SINGLEPARENT_APPLICATION_NEW ;
				
				sendMessageToParents(choice, nonApplyingSubject, nonApplyingBody,nonApplyingCode,applyingSubject,applyingBody,applyingCode,isSchoolChangeApplication, sendToAllParents);
			}
		}
		catch (Exception ex) {
			throw new RemoteException(ex.getMessage());
		}
	}

	public void createCurrentSchoolSeason(Integer newKey, Integer oldKey) throws java.rmi.RemoteException {
		CurrentSchoolSeasonHome shome = getCareBusiness().getCurrentSchoolSeasonHome();
		CurrentSchoolSeason season;
		try {
			season = shome.findByPrimaryKey(oldKey);
			if (oldKey.intValue() != newKey.intValue()) {
				season.remove();
				season = shome.create();
			}
		}
		catch (javax.ejb.RemoveException rme) {
			throw new java.rmi.RemoteException(rme.getMessage());
		}
		catch (javax.ejb.CreateException cre) {
			throw new java.rmi.RemoteException(cre.getMessage());
		}
		catch (javax.ejb.FinderException fe) {
			//fe.printStackTrace();
			try {
				season = shome.create();
			}
			catch (javax.ejb.CreateException ce) {
				//ce.printStackTrace();
				throw new java.rmi.RemoteException(ce.getMessage());
			}
		}
		season.setCurrent(newKey);
		season.store();
	}
	public void createTestFamily() {
		javax.transaction.UserTransaction trans = getSessionContext().getUserTransaction();
		try {
			trans.begin();
			com.idega.user.data.User[] familyMembers = new com.idega.user.data.User[5];
			is.idega.block.family.business.FamilyLogic ml = (is.idega.block.family.business.FamilyLogic) getServiceInstance(is.idega.block.family.business.FamilyLogic.class);
			se.idega.idegaweb.commune.business.CommuneUserBusiness ub = (se.idega.idegaweb.commune.business.CommuneUserBusiness) getServiceInstance(se.idega.idegaweb.commune.business.CommuneUserBusiness.class);
			familyMembers[0] = ub.createOrUpdateCitizenByPersonalID("Gunnar", "P?ll", "Dan?elsson", "0101");
			familyMembers[1] = ub.createOrUpdateCitizenByPersonalID("P?ll", "", "Helgason", "0202");
			familyMembers[2] = ub.createOrUpdateCitizenByPersonalID("Tryggvi", "", "L?russon", "0303");
			familyMembers[3] = ub.createOrUpdateCitizenByPersonalID("??rhallur", "", "Dan?elsson", "0404");
			familyMembers[4] = ub.createOrUpdateCitizenByPersonalID("J?n", "Karl", "J?nsson", "0505");
			ml.setAsChildFor(familyMembers[1], familyMembers[0]);
			ml.setAsChildFor(familyMembers[2], familyMembers[0]);
			ml.setAsChildFor(familyMembers[3], familyMembers[1]);
			ml.setAsChildFor(familyMembers[4], familyMembers[1]);
			trans.commit();
		}
		catch (Exception ex) {
			try {
				trans.rollback();
			}
			catch (Exception e) {
			}
		}
	}
	public boolean noRoomAction(Integer pk, User performer) {
		javax.transaction.UserTransaction trans = getSessionContext().getUserTransaction();
		try {
			trans.begin();
			SchoolChoice choice = this.getSchoolChoiceHome().findByPrimaryKey(pk);
			super.changeCaseStatus(choice, getCaseStatusDenied().getStatus(), performer);
			choice.store();
			Iterator children = choice.getChildrenIterator();
			if (children.hasNext()) {
				Case child = (Case) children.next();
				super.changeCaseStatus(child, getCaseStatusPreliminary().getStatus(), performer, (Group)null);
				child.store();
			}
			trans.commit();
		}
		catch (Exception ex) {
			ex.printStackTrace();
			try {
				trans.rollback();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public void sendMessageToParents(SchoolChoice application, String nonApplyingSubject, String nonApplyingBody,String nonApplyingCode,String applyingSubject,String applyingBody,String applyingCode,boolean isChangeApplication) {
		sendMessageToParents(application, nonApplyingSubject, nonApplyingBody, nonApplyingCode, applyingSubject, applyingBody, applyingCode, isChangeApplication, false);
	}
	
	private void sendMessageToParents(SchoolChoice application, String nonApplyingSubject, String nonApplyingBody,String nonApplyingCode,String applyingSubject,String applyingBody,String applyingCode,boolean isChangeApplication, boolean sendToAllParents) {
		try {
			User child = application.getChild();
			//Object[] arguments = {child.getNameLastFirst(true), application.getChosenSchool().getSchoolName()};
			Object[] arguments = {child.getName(), application.getChosenSchool().getSchoolName()};

			if (isOfAge(child)) {
				String subject,body,code;
				if(isChangeApplication){
					subject = getLocalizedString("school_choice.child_self_change_subj", "School application received for you");
					body = getLocalizedString("school_choice.child_self_change_mesg_body", "Dear mr./ms./mrs. ");
					code = SchoolChoiceMessagePdfHandler.CODE_CHILD_SELF_APPLICATION_CHANGE;
				}
				else{
					subject = getLocalizedString("school_choice.child_self_appl_subj", "School application received for you");
					body = getLocalizedString("school_choice.child_self_change_mesg_body", "Dear mr./ms./mrs. ");
					code = SchoolChoiceMessagePdfHandler.CODE_CHILD_SELF_APPLICATION_NEW;
				}
				getMessageBusiness().createUserMessage(application, child,null,null, subject, MessageFormat.format(body, arguments), true,code);

			}
			else {
				User appParent = application.getOwner();
			    
				


				try {
					if (sendToAllParents) {
					User parent2 = null;						
					Collection parents = getUserBusiness().getMemberFamilyLogic().getCustodiansFor(child);
					Iterator iter = parents.iterator();
					while (iter.hasNext()) {
						User parent = (User) iter.next();
						if (parent.equals(appParent)) {
							continue;
						}
						else parent2 = parent;
					}
					if(parent2 == null){
						if((appParent.getEmails() != null) &&(!appParent.getEmails().isEmpty()) ){
							getMessageBusiness().createUserMessage(application, appParent,null,null,applyingSubject,MessageFormat.format(applyingBody, arguments), null, null, false,null,false,true); 
						}
						else{
						 	getMessageBusiness().createUserMessage(application, appParent,null,null,applyingSubject,MessageFormat.format(applyingBody, arguments), null, null, true,null,true,true);
						}				
					}
					else{
						if(getUserBusiness().haveSameAddress(parent2, appParent)){
							if((appParent.getEmails() != null) &&(!appParent.getEmails().isEmpty()) ){
								getMessageBusiness().createUserMessage(application, appParent,null,null,applyingSubject,MessageFormat.format(applyingBody, arguments), null, null, false,null,false,true); 
							}
							else{
							 	getMessageBusiness().createUserMessage(application, appParent,null,null,applyingSubject,MessageFormat.format(applyingBody, arguments), null, null, true,null,true,true);
							}
							getMessageBusiness().createUserMessage(application, parent2,null,null,applyingSubject,MessageFormat.format(applyingBody, arguments), null, null, false,null,false,true); 
						} 
						else { // not same address
							if((appParent.getEmails() != null) &&(!appParent.getEmails().isEmpty()) ){
								getMessageBusiness().createUserMessage(application, appParent,null,null,applyingSubject,MessageFormat.format(applyingBody, arguments), null, null, false,null,false,true); 
							}
							else{
							 	getMessageBusiness().createUserMessage(application, appParent,null,null,applyingSubject,MessageFormat.format(applyingBody, arguments), null, null, true,null,true,true);
							}

							if((parent2.getEmails() != null) &&(!parent2.getEmails().isEmpty()) ){
								getMessageBusiness().createUserMessage(application, parent2,null,null,applyingSubject,MessageFormat.format(applyingBody, arguments), null, null, false,null,false,true); 
							}
							else{
							 	getMessageBusiness().createUserMessage(application, parent2,null,null,applyingSubject,MessageFormat.format(applyingBody, arguments), null, null, true,null,true,true);
							}

						} // end not same address

					}	// end parent2!=null
				   }	
					else { // send only for one parent   
						if((appParent.getEmails() != null) &&(!appParent.getEmails().isEmpty()) ){
							getMessageBusiness().createUserMessage(application, appParent,null,null,applyingSubject,MessageFormat.format(applyingSubject, arguments), MessageFormat.format(applyingSubject, arguments), null, false,null,false,true); 
						}
						else{
							getMessageBusiness().createUserMessage(application, appParent,null,null,applyingSubject,MessageFormat.format(applyingSubject, arguments), MessageFormat.format(applyingSubject, arguments), null, true,null,true,true);
						}				
					}					
				} // end try

				catch (NoCustodianFound ncf) {
					ncf.printStackTrace();
				}

			}// end else
		}
		catch (RemoteException re) {
			re.printStackTrace();
		}

	}

	/**
	 * @param schoolID
	 * @param subject
	 * @param body
	 * @throws RemoteException
	 */
	private void sendMessageToSchool(int schoolID, String subject, String body,String code) throws RemoteException {
		try {
			School school = getSchool(schoolID);
			
			if (school != null) {
				//If school is centralized administrated (by BUN), the message shall be marked as such, so that it will show in BUN's messagebox.
				Group bunGroup = school.getCentralizedAdministration() ? getBunGroup() : null;
				
				Collection coll = getSchoolBusiness().getSchoolUserBusiness().getSchoolUserHome().findBySchool(school);
				if (!coll.isEmpty()) {
					Iterator iter = coll.iterator();
					while (iter.hasNext()) {
						SchoolUser user = (SchoolUser) iter.next();
						getMessageBusiness().createUserMessage(user.getUser(), subject, bunGroup, body, false,code);
					}
				}
			}
		}
		catch (FinderException fe) {
			throw new RemoteException(fe.getMessage());
		}
	}

	private Group getBunGroup() throws RemoteException {
		Group bunGroup = null;
		try {
			bunGroup = getCommuneUserBusiness().getRootCustomerChoiceGroup();
		}
		catch (CreateException ex) {
			System.err.println("Could not create group");
			ex.printStackTrace(System.err);
		}
		catch (FinderException fe) {
			throw new RemoteException(fe.getMessage());
		}
		return bunGroup;
	}
	
	public void reactivateApplication(int applicationID, int removedSchoolID, boolean hasReceivedPlacementMessage) throws RemoteException {
		//change PDF-handler to a new for reactivation		
		reactivateApplication(applicationID,removedSchoolID,hasReceivedPlacementMessage,SchoolChoiceMessagePdfHandler.CODE_APPLICATION_REACTIVATE);
	}
	public void reactivateApplication(int applicationID, int removedSchoolID, boolean hasReceivedPlacementMessage, String code) throws RemoteException {
		try {
			SchoolChoice choice = this.getSchoolChoiceHome().findByPrimaryKey(new Integer(applicationID));
			//User child = choice.getChild();
			
			String messageSubject1 = getReactivatedMessageSubject();
			String messageBody1 = getReactivatedMessageBody(choice);
			String messageSubject2 = getReactivatedMessageSubject(); 
			String messageBody2 = "";
			
			if (removedSchoolID != -1) {
				messageBody2 = getReactivatedRemovedMessageBody(choice, removedSchoolID);
				if (!hasReceivedPlacementMessage) {
					sendMessageToSchool(removedSchoolID, messageSubject2, messageBody2, code);
					sendMessageToSchool(choice.getChosenSchoolId(), messageSubject2, messageBody2, code);
					sendMessageToParents(choice, messageSubject1, messageBody1,code, messageSubject1,messageBody1,code,false);
				}
				else {
					sendMessageToSchool(removedSchoolID, messageSubject2, messageBody2, code);
					sendMessageToSchool(choice.getChosenSchoolId(), messageSubject2, messageBody2, code);
					sendMessageToParents(choice, messageSubject2, messageBody2,code, messageSubject2,messageBody2,code,false);
				}
			}
			else{
				sendMessageToSchool(choice.getChosenSchoolId(), messageSubject1, messageBody1, code);
				sendMessageToParents(choice, messageSubject1, messageBody1,code, messageSubject1,messageBody1,code,false);
			}
			
			
		}
		catch (FinderException fe) {
			fe.printStackTrace();
		}
		
	}


	public boolean preliminaryAction(Integer pk, User performer) {
		try {
			SchoolChoice choice = getSchoolChoiceHome().findByPrimaryKey(pk);
			super.changeCaseStatus(choice, getCaseStatusPreliminary().getStatus(), performer);
			choice.store();
			sendMessageToParentOrChild(choice, choice.getOwner(), choice.getChild(), getPreliminaryMessageSubject(), getPreliminaryMessageBody(choice),SchoolChoiceMessagePdfHandler.CODE_PRELIMINARY);
			return true;
		}
		catch (Exception e) {
		}
		return false;
	}

	public void setAsPreliminary(SchoolChoice choice, User performer) {
		try {
			super.changeCaseStatus(choice, getCaseStatusPreliminary().getStatus(), performer);
		}
		catch (Exception e) {
		}
	}
	
	public void setAsInactive(SchoolChoice choice, User performer) {
		try {
			super.changeCaseStatus(choice, getCaseStatusInactive().getStatus(), performer);
		}
		catch (Exception e) {
		}
	}

	public void setChildcarePreferences(User performer, int childID, boolean freetimeInThisSchool, String otherMessage, String messageSubject, String messageBody) throws RemoteException {
		try {
			SchoolSeason season = getCareBusiness().getCurrentSeason();
			if (season != null) {
				Collection choices = this.findByStudentAndSeason(childID, ((Integer) season.getPrimaryKey()).intValue());
				if (!choices.isEmpty()) {
					Iterator iter = choices.iterator();
					while (iter.hasNext()) {
						SchoolChoice element = (SchoolChoice) iter.next();
						element.setFreetimeInThisSchool(freetimeInThisSchool);
						if (!freetimeInThisSchool && otherMessage != null && otherMessage.length() > 0) {
							element.setFreetimeOther(otherMessage);
							ViewpointBusiness vpb = (ViewpointBusiness) getServiceInstance(ViewpointBusiness.class);
							SubCategory subCategory = vpb.findSubCategory("Fritids");
							if (subCategory != null) {
								try {
									vpb.createViewpoint(performer, messageSubject, messageBody, subCategory.getName(), getUserBusiness().getRootAdministratorGroupID(), -1);
								}
								catch (CreateException ce) {
									ce.printStackTrace();
								}
							}
						}
						element.store();
					}
				}
			}
		}
		catch (FinderException fe) {
			fe.printStackTrace();
		}
	}

	public void changeSchoolYearForChoice(int userID, int schoolSeasonID, int schoolYearID) throws RemoteException {
		Collection choices = findByStudentAndSeason(userID, schoolSeasonID);
		if (!choices.isEmpty() && schoolYearID != -1) {
			Iterator iter = choices.iterator();
			while (iter.hasNext()) {
				SchoolChoice element = (SchoolChoice) iter.next();
				element.setSchoolYear(schoolYearID);
				element.store();
			}
		}
	}

	public SchoolChoice groupPlaceAction(Integer pk, User performer) {
		try {
			SchoolChoice choice = getSchoolChoiceHome().findByPrimaryKey(pk);
			if (choice.getCaseStatus().equals(getCaseStatusMoved())) {
				if (choice.getPlacementDate() != null && choice.getCurrentSchoolId() != -1) {
					terminateOldPlacement(choice);
				}
				User child = choice.getChild();
				String placementDate = choice.getPlacementDate() != null ? new IWTimestamp(choice.getPlacementDate()).getLocaleDate(this.getIWApplicationContext().getApplicationSettings().getDefaultLocale(), IWTimestamp.SHORT) : "";
				Object[] arguments = {child.getName(), choice.getChosenSchool().getSchoolName(), this.getIWApplicationContext().getApplicationSettings().getDefaultLocale(), placementDate};				
				String body = getLocalizedString("school_choice.student_moved_from_school_body", "Dear headmaster, {0} has been moved from your school and placed at {1} from {2}."); 
				this.sendMessageToSchool(choice.getCurrentSchoolId(), getLocalizedString("school_choice.student_moved_from_school_subject", "Student moved from your school"), MessageFormat.format(body, arguments),SchoolChoiceMessagePdfHandler.CODE_SCHOOL_MOVEAWAY);
			}
			super.changeCaseStatus(choice, getCaseStatusPlaced().getStatus(), performer);
			return choice;
		}
		catch (Exception e) {
			e.printStackTrace(System.err);
		}
		return null;
	}
	
	private void terminateOldPlacement(SchoolChoice choice) {
		try {
			IWTimestamp stamp = new IWTimestamp(choice.getPlacementDate());
			stamp.addDays(-1);
			Collection types = getSchoolBusiness().getSchoolTypesForCategory(getSchoolBusiness().getCategoryElementarySchool(), false);
			SchoolClassMember member = null;
			if (choice.getCurrentSchoolId() != choice.getChosenSchoolId()) {
				member = getSchoolBusiness().getSchoolClassMemberHome().findLatestByUserAndSchoolAndPlacementDate(choice.getChildId(), choice.getCurrentSchoolId(), types, choice.getPlacementDate());
			}
			else {
				SchoolSeason season = getCareBusiness().getSchoolSeasonHome().findSeasonByDate(getSchoolBusiness().getCategoryElementarySchool(), choice.getPlacementDate());
				member = getSchoolBusiness().getSchoolClassMemberHome().findLatestByUserAndSchCategoryAndSeason(choice.getChild(), getSchoolBusiness().getCategoryElementarySchool(), season);
			}
			member.setRemovedDate(stamp.getTimestamp());
			member.store();
			
			ResourceBusiness business = (ResourceBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), ResourceBusiness.class);
			Collection resources = business.getResourcePlacementsByMemberId((Integer)member.getPrimaryKey());
			if (resources != null) {
				Iterator iter = resources.iterator();
				while (iter.hasNext()) {
					ResourceClassMember element = (ResourceClassMember) iter.next();
					element.setEndDate(stamp.getDate());
					element.store();
				}
			}
		}
		catch (FinderException fe) {
			log(fe);
		}
		catch (RemoteException re) {
			log(re);
		}	
	}

	protected String getReactivatedMessageBody(SchoolChoice theCase) {
		SchoolYear year = theCase.getSchoolYear();
		//Object[] arguments = {theCase.getChosenSchool().getName(), theCase.getChild().getNameLastFirst(true), theCase.getChild().getPersonalID(), year != null ? year.getSchoolYearName() : "" };
		Object[] arguments = {theCase.getChosenSchool().getName(), theCase.getChild().getName(), theCase.getChild().getPersonalID(), year != null ? year.getSchoolYearName() : "" };
		String body = MessageFormat.format(getLocalizedString(KEY_REACTIVATE_BODY1, "The school choice for child {1}, {2}, has been reactivated in {0} in year {3}"), arguments);
		
		/*
		 * StringBuffer body = new StringBuffer(this.getLocalizedString("school_choice.prelim_mesg_body1", "Dear mr./ms./mrs. ")); body.append().append("\n"); body.append(this.getLocalizedString("school_choice.prelim_mesg_body2", "Your child has been preliminary accepted in: ."));
		 */
		return body;
	}
	
	protected String getReactivatedRemovedMessageBody(SchoolChoice theCase, int removedSchoolID) {
		SchoolYear year = theCase.getSchoolYear();
		School removedSchool = null;
		try {
			removedSchool = getSchool(removedSchoolID);	
		}
		catch (RemoteException re){
			return null;
		}
		
		//Object[] arguments = {theCase.getChosenSchool().getName(), theCase.getChild().getNameLastFirst(true), theCase.getChild().getPersonalID(), removedSchool.getName(), year != null ? year.getSchoolYearName() : "" };
		Object[] arguments = {theCase.getChosenSchool().getName(), theCase.getChild().getName(), theCase.getChild().getPersonalID(), removedSchool.getName(), year != null ? year.getSchoolYearName() : "" };
		String body = MessageFormat.format(getLocalizedString(KEY_REACTIVATE_BODY2, "The placement for child {1}, {2}, in school {3} has been removed since school choice in {0}:  for year {4}, has been reactivated."), arguments);
		/*
		 * StringBuffer body = new StringBuffer(this.getLocalizedString("school_choice.prelim_mesg_body1", "Dear mr./ms./mrs. ")); body.append().append("\n"); body.append(this.getLocalizedString("school_choice.prelim_mesg_body2", "Your child has been preliminary accepted in: ."));
		 */
		return body;
	}
	
	protected String getPlacemReactivatedMessageBody(SchoolChoice theCase) {
		SchoolYear year = theCase.getSchoolYear();
		//Object[] arguments = {theCase.getChosenSchool().getName(), theCase.getOwner().getNameLastFirst(true), theCase.getChild().getNameLastFirst(true), year != null ? year.getSchoolYearName() : "" };
		Object[] arguments = {theCase.getChosenSchool().getName(), theCase.getOwner().getName(), theCase.getChild().getName(), year != null ? year.getSchoolYearName() : "" };
		String body = MessageFormat.format(getLocalizedString("school_choice.prelim_mesg_body", "Dear mr./ms./mrs. {1}\n Your child, {2} has been preliminary accepted in: {0} for year {3}"), arguments);

		/*
		 * StringBuffer body = new StringBuffer(this.getLocalizedString("school_choice.prelim_mesg_body1", "Dear mr./ms./mrs. ")); body.append().append("\n"); body.append(this.getLocalizedString("school_choice.prelim_mesg_body2", "Your child has been preliminary accepted in: ."));
		 */
		return body;
	}
	
	public String getPreliminaryMessageBody(SchoolChoice theCase) {
		SchoolYear year = theCase.getSchoolYear();
		
		//Object[] arguments = {theCase.getChosenSchool().getName(), theCase.getOwner().getNameLastFirst(true), theCase.getChild().getNameLastFirst(true), year != null ? year.getSchoolYearName() : "" };
		Object[] arguments = {theCase.getChosenSchool().getName(), theCase.getOwner().getName(), theCase.getChild().getName(), year != null ? year.getSchoolYearName() : "" };
		String body = MessageFormat.format(getLocalizedString("school_choice.prelim_mesg_body", "Dear mr./ms./mrs. {1}\n Your child, {2} has been preliminary accepted in: {0} for year {3}"), arguments);

		/*
		 * StringBuffer body = new StringBuffer(this.getLocalizedString("school_choice.prelim_mesg_body1", "Dear mr./ms./mrs. ")); body.append().append("\n"); body.append(this.getLocalizedString("school_choice.prelim_mesg_body2", "Your child has been preliminary accepted in: ."));
		 */
		return body;
	}
	protected String getPreliminaryMessageBodyNew(SchoolChoice theCase) {
		SchoolYear year = theCase.getSchoolYear();
		String placementDate = theCase.getPlacementDate() != null ? new IWTimestamp(theCase.getPlacementDate()).getLocaleDate(this.getIWApplicationContext().getApplicationSettings().getDefaultLocale(), IWTimestamp.SHORT) : "";
		
		//Object[] arguments = {theCase.getChosenSchool().getName(), theCase.getOwner().getNameLastFirst(true), theCase.getChild().getNameLastFirst(true), year != null ? year.getSchoolYearName() : "", placementDate };
		Object[] arguments = {theCase.getChosenSchool().getName(), theCase.getOwner().getName(), theCase.getChild().getName(), year != null ? year.getSchoolYearName() : "", placementDate };
		String body = MessageFormat.format(getLocalizedString("school_choice.prelim_mesg_body_new", "Dear mr./ms./mrs. {1}\n Your child, {2} has been preliminary accepted in: {0} for year {3} with placement date {4}"), arguments);

		/*
		 * StringBuffer body = new StringBuffer(this.getLocalizedString("school_choice.prelim_mesg_body1", "Dear mr./ms./mrs. ")); body.append().append("\n"); body.append(this.getLocalizedString("school_choice.prelim_mesg_body2", "Your child has been preliminary accepted in: ."));
		 */
		return body;
	}
	
	protected String getGroupedMessageBody(SchoolChoice theCase) throws RemoteException {
		StringBuffer body = new StringBuffer(this.getLocalizedString("acc.app.acc.body1", "Dear mr./ms./mrs. "));

		User owner = theCase.getOwner();
		Name name = new Name(owner.getFirstName(), owner.getMiddleName(), owner.getLastName());
		body.append(name.getName(this.getIWMainApplication().getSettings().getDefaultLocale())).append("\n");
		
		body.append(this.getLocalizedString("school_choice.group_mesg_body2", "Your child has been grouped  in ."));
		body.append(theCase.getGroupPlace()).append("\n");
		body.append(this.getLocalizedString("school_choice.group_mesg_body3", "in school ."));
		body.append(getSchool(theCase.getChosenSchoolId()).getSchoolName()).append("\n");
		return body.toString();
	}

	protected String getApplyingSeparateParentMessageBodyAppl(List choices, User parent) throws RemoteException {
		return getSeparateParentMessageBodyAppl( getLocalizedString("school_choice.applying_sep_parent_appl_mesg_body", "Dear mr./ms./mrs. "),choices,  parent);
	}
	protected String getNonApplyingSeparateParentMessageBodyAppl(List choices, User parent) throws RemoteException {
		return getSeparateParentMessageBodyAppl( getLocalizedString("school_choice.sep_parent_appl_mesg_body", "Dear mr./ms./mrs. "),choices,  parent);
	}
	protected String getNonApplyingSeparateParentMessageBodyApplAdmin(List choices) throws RemoteException {
		return getSeparateParentMessageBodyApplAdmin( getLocalizedString("school_choice.sep_parent_appl_mesg_body_admin", "Dear mr./ms./mrs. "),choices);
	}
	protected String getNonApplyingSeparateParentMessageBodyApplNew(List choices, User parent) throws RemoteException {
		return getSeparateParentMessageBodyAppl( getLocalizedString("school_choice.sep_parent_appl_mesg_body_new", "Dear mr./ms./mrs. "),choices,  parent);
	}
	private String getSeparateParentMessageBodyApplAdmin(String text,List choices) throws RemoteException {
		Object[] arguments = new Object[6];
		//arguments[0] = parent.getNameLastFirst(true);
		IWTimestamp today = new IWTimestamp();
		arguments[0] = today.getLocaleDate(this.getIWApplicationContext().getApplicationSettings().getDefaultLocale(), IWTimestamp.SHORT);
		
		Iterator iter = choices.iterator();
		int count = 1;
		boolean addedChildName = false;
		while (iter.hasNext()) {
			SchoolChoice choice = (SchoolChoice) iter.next();
			if (!addedChildName) {
				arguments[count++] = choice.getChild().getName();
				arguments[count++] = PersonalIDFormatter.format(choice.getChild().getPersonalID(), this.getIWApplicationContext().getApplicationSettings().getDefaultLocale());
				addedChildName = true;
			}
			arguments[count++] = getSchool(choice.getChosenSchoolId()).getSchoolName();
		}
		if (choices.size() == 1) {
			arguments[4] = "";
			arguments[5] = "";
		}

		String body = MessageFormat.format(text, arguments);
		/*
		 * StringBuffer body = new StringBuffer(this.getLocalizedString("school_choice.sep_parent_appl_mesg_body1", "Dear mr./ms./mrs. ")); body.append(parent.getNameLastFirst()).append("\n"); body.append(this.getLocalizedString("school_choice.separate_parent_appl_mesg_body2", "School application for your child has been received \n The schools are: "));
		 */
		return body;
	}
	
	private String getSeparateParentMessageBodyAppl(String text,List choices, User parent) throws RemoteException {
		Object[] arguments = new Object[5];
		//arguments[0] = parent.getNameLastFirst(true);
		arguments[0] = parent.getName();
		Iterator iter = choices.iterator();
		int count = 1;
		boolean addedChildName = false;
		while (iter.hasNext()) {
			SchoolChoice choice = (SchoolChoice) iter.next();
			if (!addedChildName) {
				arguments[count++] = choice.getChild().getName();
				addedChildName = true;
			}
			arguments[count++] = getSchool(choice.getChosenSchoolId()).getSchoolName();
		}
		if (choices.size() == 1) {
			arguments[3] = "";
			arguments[4] = "";
		}

		String body = MessageFormat.format(text, arguments);
		/*
		 * StringBuffer body = new StringBuffer(this.getLocalizedString("school_choice.sep_parent_appl_mesg_body1", "Dear mr./ms./mrs. ")); body.append(parent.getNameLastFirst()).append("\n"); body.append(this.getLocalizedString("school_choice.separate_parent_appl_mesg_body2", "School application for your child has been received \n The schools are: "));
		 */
		return body;
	}

	protected String getNonApplyingSeparateParentMessageBodyChange(SchoolChoice theCase, User parent) throws RemoteException {
		//Object[] arguments = {parent.getNameLastFirst(true), theCase.getChild().getNameLastFirst(true), getSchool(theCase.getChosenSchoolId()).getSchoolName(), theCase.getPlacementDate() != null ? new IWTimestamp(theCase.getPlacementDate()).getLocaleDate(this.getIWApplicationContext().getApplicationSettings().getDefaultLocale(), IWTimestamp.SHORT) : "", PersonalIDFormatter.format(theCase.getChild().getPersonalID(), this.getIWApplicationContext().getApplicationSettings().getDefaultLocale()) };
		Object[] arguments = {parent.getName(), theCase.getChild().getName(), getSchool(theCase.getChosenSchoolId()).getSchoolName(), theCase.getPlacementDate() != null ? new IWTimestamp(theCase.getPlacementDate()).getLocaleDate(this.getIWApplicationContext().getApplicationSettings().getDefaultLocale(), IWTimestamp.SHORT) : "", PersonalIDFormatter.format(theCase.getChild().getPersonalID(), this.getIWApplicationContext().getApplicationSettings().getDefaultLocale()) };
		String body = MessageFormat.format(getLocalizedString("school_choice.sep_parent_change_mesg_body", "Dear mr./ms./mrs. "), arguments);
		return body;
	}
	
	protected String getApplyingSeparateParentMessageBodyChange(SchoolChoice theCase, User parent) throws RemoteException {
		//Object[] arguments = {parent.getNameLastFirst(true), theCase.getChild().getNameLastFirst(true), getSchool(theCase.getChosenSchoolId()).getSchoolName(), theCase.getPlacementDate() != null ? new IWTimestamp(theCase.getPlacementDate()).getLocaleDate(this.getIWApplicationContext().getApplicationSettings().getDefaultLocale(), IWTimestamp.SHORT) : "", PersonalIDFormatter.format(theCase.getChild().getPersonalID(), this.getIWApplicationContext().getApplicationSettings().getDefaultLocale()) };
		Object[] arguments = {parent.getName(), theCase.getChild().getName(), getSchool(theCase.getChosenSchoolId()).getSchoolName(), theCase.getPlacementDate() != null ? new IWTimestamp(theCase.getPlacementDate()).getLocaleDate(this.getIWApplicationContext().getApplicationSettings().getDefaultLocale(), IWTimestamp.SHORT) : "", PersonalIDFormatter.format(theCase.getChild().getPersonalID(), this.getIWApplicationContext().getApplicationSettings().getDefaultLocale()) };
		String body = MessageFormat.format(getLocalizedString("school_choice.applying_sep_parent_change_mesg_body", "Dear mr./ms./mrs. "), arguments);
		return body;
	}
	

	protected String getOldHeadmasterBody(SchoolChoice choice, User student, School newSchool) {
		//Object[] arguments = {student.getNameLastFirst(true), newSchool.getSchoolName(), PersonalIDFormatter.format(student.getPersonalID(), this.getIWApplicationContext().getApplicationSettings().getDefaultLocale()), choice.getPlacementDate() != null ? new IWTimestamp(choice.getPlacementDate()).getLocaleDate(this.getIWApplicationContext().getApplicationSettings().getDefaultLocale(), IWTimestamp.SHORT) : "" };
		Object[] arguments = {student.getName(), newSchool.getSchoolName(), PersonalIDFormatter.format(student.getPersonalID(), this.getIWApplicationContext().getApplicationSettings().getDefaultLocale()), choice.getPlacementDate() != null ? new IWTimestamp(choice.getPlacementDate()).getLocaleDate(this.getIWApplicationContext().getApplicationSettings().getDefaultLocale(), IWTimestamp.SHORT) : "" };
		String body = MessageFormat.format(getLocalizedString("school_choice.old_headmaster_body", "Dear headmaster"), arguments);
		/*
		 * StringBuffer body = new StringBuffer(this.getLocalizedString("school_choice.old_headmaster_body1", "Dear headmaster ")); body.append(student.getName()).append("\n");
		 */
		return body;
	}

	protected String getNewHeadmasterBody(SchoolChoice choice, User student, School oldSchool) {
		//Object[] arguments = {student.getNameLastFirst(true), oldSchool.getSchoolName(), PersonalIDFormatter.format(student.getPersonalID(), this.getIWApplicationContext().getApplicationSettings().getDefaultLocale()), choice.getPlacementDate() != null ? new IWTimestamp(choice.getPlacementDate()).getLocaleDate(this.getIWApplicationContext().getApplicationSettings().getDefaultLocale(), IWTimestamp.SHORT) : "" };
		Object[] arguments = {student.getName(), oldSchool.getSchoolName(), PersonalIDFormatter.format(student.getPersonalID(), this.getIWApplicationContext().getApplicationSettings().getDefaultLocale()), choice.getPlacementDate() != null ? new IWTimestamp(choice.getPlacementDate()).getLocaleDate(this.getIWApplicationContext().getApplicationSettings().getDefaultLocale(), IWTimestamp.SHORT) : "" };
		String body = MessageFormat.format(getLocalizedString("school_choice.new_headmaster_body", "Dear headmaster"), arguments);
		/*
		 * StringBuffer body = new StringBuffer(this.getLocalizedString("school_choice.old_headmaster_body1", "Dear headmaster ")); body.append(student.getName()).append("\n");
		 */
		return body;
	}

	public String getReactivatedMessageSubject() {
		return this.getLocalizedString(KEY_REACTIVATE_SUBJECT1, "School choice reactivated");
	}
	public String getPreliminaryMessageSubject() {
		return this.getLocalizedString("school_choice.prelim_mesg_subj", "Prelimininary school acceptance");
	}
	public String getPreliminaryMessageSubjectNew() {
		return this.getLocalizedString("school_choice.prelim_mesg_subj_new", "Prelimininary school acceptance");
	}
	public String getGroupedMessageSubject() {
		return this.getLocalizedString("school_choice.group_mesg_subj", "School grouping");
	}

	public String getApplyingSeparateParentSubjectAppl() {
		return this.getLocalizedString("school_choice.applying_sep_parent_appl_subj", "School application received for your child");
	}
	public String getNonApplyingSeparateParentSubjectAppl() {
		return this.getLocalizedString("school_choice.sep_parent_appl_subj", "School application received for your child");
	}
	public String getApplyingSeparateParentSubjectChange() {
		return this.getLocalizedString("school_choice.applying_sep_parent_change_subj", "School change application received for your child");
	}
	public String getNonApplyingSeparateParentSubjectChange() {
		return this.getLocalizedString("school_choice.sep_parent_change_subj", "School change application received for your child");
	}
	public String getApplyingSeparateParentSubjectApplNew() {
		return this.getLocalizedString("school_choice.applying_sep_parent_appl_subj", "School application received for your child");
	}
	public String getNonApplyingSeparateParentSubjectApplNew() {
		return this.getLocalizedString("school_choice.sep_parent_appl_subj_new", "School application received for your child");
	}
	
	public String getOldHeadmasterSubject() {
		return this.getLocalizedString("school_choice.old_headmaster_subj", "School change request");
	}

	public String getNewHeadMasterSubject() {
		return this.getLocalizedString("school_choice.new_headmaster_subj", "School change request");
	}

	protected SchoolChoice getSchoolChoiceInstance(Case theCase) throws RuntimeException {
		String caseCode = "unreachable";
		try {
			caseCode = theCase.getCode();
			if (SchoolConstants.SCHOOL_CHOICE_CASE_CODE_KEY.equals(caseCode)) {
				int caseID = ((Integer) theCase.getPrimaryKey()).intValue();
				return this.getSchoolChoice(caseID);
			}
		}
		catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		throw new ClassCastException("Case with casecode: " + caseCode + " cannot be converted to a schoolchoice");
	}
	
	public String getSchoolChoiceCaseCode() {
		return SchoolConstants.SCHOOL_CHOICE_CASE_CODE_KEY;
	}

	
	public String getLocalizedCaseDescription(Case theCase, Locale locale) {
		SchoolChoice choice = getSchoolChoiceInstance(theCase);
		String firstName = null;
		try {
			firstName = getUserBusiness().getUser(choice.getChildId()).getFirstName();
		}
		catch (RemoteException re) {
			firstName = "";
		}
		Object[] arguments = {firstName, String.valueOf(choice.getChoiceOrder()), choice.getChosenSchool().getName()};

		String desc = super.getLocalizedCaseDescription(theCase, locale);
		return MessageFormat.format(desc, arguments);
	}

	public SchoolChoice findByStudentAndSchoolAndSeason(int studentID, int schoolID, int seasonID) throws RemoteException {
		try {
			Collection coll = getSchoolChoiceHome().findByChildAndSchoolAndSeason(studentID, schoolID, seasonID);
			if (coll != null && !coll.isEmpty()) {
				Iterator iter = coll.iterator();
				while (iter.hasNext()) {
					return (SchoolChoice) iter.next();
				}
			}
			return null;
		}
		catch (FinderException fe) {
			return null;
		}
	}

	public Collection findBySchoolAndFreeTime(int schoolId, int schoolSeasonID, boolean freeTimeInSchool) throws RemoteException {
		try {
			return getSchoolChoiceHome().findBySchoolAndFreeTime(schoolId, schoolSeasonID, freeTimeInSchool);
		}
		catch (FinderException fe) {
			return new Vector();
		}
	}

	public Collection findByStudentAndSeason(int studentID, int seasonID) throws RemoteException {
		try {
			String[] statuses = { getCaseStatusDeleted().getStatus() };
			return getSchoolChoiceHome().findByChildAndSeason(studentID, seasonID, statuses);
		}
		catch (FinderException fe) {
			return new Vector();
		}
	}
	
	public int getNumberOfApplicationsByUserAndSchool(int userID, int schoolID) throws RemoteException {
		try {
			String[] statuses = { getCaseStatusPreliminary().getStatus(), getCaseStatusPlaced().getStatus() };
			return getSchoolChoiceHome().getCountByChildAndSchoolAndStatus(userID, schoolID, statuses);
		} catch (IDOException e) {
			return 0;
		}
	}
	
	public Collection findByStudentAndSchool(int studentID, int schoolID) throws RemoteException {
		try {
			return getSchoolChoiceHome().findByChildAndSchool(studentID, schoolID);
		}
		catch (FinderException fe) {
			return new Vector();
		}
	}
	
	public int getNumberOfApplications(int schoolID, int schoolSeasonID) throws RemoteException {
		try {
			return getSchoolChoiceHome().getNumberOfApplications(getCaseStatusPreliminary().getStatus(), schoolID, schoolSeasonID);
		}
		catch (IDOException ie) {
			return 0;
		}
	}

	public int getNumberOfApplicationsForStudents(int userID, int schoolSeasonID) throws RemoteException {
		try {
			String[] statuses = { getCaseStatusDeleted().getStatus(), getCaseStatusInactive().getStatus() };
			return getSchoolChoiceHome().getNumberOfChoices(userID, schoolSeasonID, statuses);
		}
		catch (IDOException ie) {
			return 0;
		}
	}

	public int getNumberOfApplications(int schoolID, int schoolSeasonID, int schoolYearID) throws RemoteException {
		try {
			return getSchoolChoiceHome().getNumberOfApplications(getCaseStatusPreliminary().getStatus(), schoolID, schoolSeasonID, schoolYearID);
		}
		catch (IDOException ie) {
			return 0;
		}
	}

	private CommuneUserBusiness getCommuneUserBusiness() throws RemoteException {
		return (CommuneUserBusiness) getServiceInstance(CommuneUserBusiness.class);
	}

	private SchoolCommuneBusiness getCommuneSchoolBusiness() throws RemoteException {
		return (SchoolCommuneBusiness) getServiceInstance(SchoolCommuneBusiness.class);
	}
	
	private CareBusiness getCareBusiness() throws RemoteException {
		if (this.careBusiness == null) {
			this.careBusiness = (CareBusiness) getServiceInstance(CareBusiness.class);
		}
		return this.careBusiness;
	}
	
	public Collection getApplicantsForSchool(int schoolID, int seasonID, int schoolYearID, String[] validStatuses, String searchString, int orderBy, int numberOfEntries, int startingEntry) throws RemoteException {
		return getApplicantsForSchool(schoolID, seasonID, schoolYearID, null, validStatuses, searchString, orderBy, numberOfEntries, startingEntry);
	}

	public Collection getApplicantsForSchool(int schoolID, int seasonID, int schoolYearID, int[] choiceOrder, String[] validStatuses, String searchString, int orderBy, int numberOfEntries, int startingEntry) throws RemoteException {
		try {
			return getSchoolChoiceHome().findChoices(schoolID, seasonID, schoolYearID, choiceOrder, validStatuses, searchString, orderBy, numberOfEntries, startingEntry);
		}
		catch (FinderException fe) {
			fe.printStackTrace(System.err);
			return new Vector();
		}
	}
	
	public Collection getApplicantsForSchool(int schoolID, int seasonID, int schoolYearID, String[] validStatuses, String searchString, int orderBy, int numberOfEntries, int startingEntry, int placementType) throws RemoteException {
		try {
			return getSchoolChoiceHome().findChoices(schoolID, seasonID, schoolYearID, null, validStatuses, searchString, orderBy, numberOfEntries, startingEntry, placementType);
		}
		catch (FinderException fe) {
			fe.printStackTrace(System.err);
			return new Vector();
		}
	}

	public int getNumberOfApplicantsForSchool(int schoolID, int seasonID, int schoolYearID, int[] choiceOrder, String[] validStatuses, String searchString) throws RemoteException {
		try {
			return getSchoolChoiceHome().getCount(schoolID, seasonID, schoolYearID, choiceOrder, validStatuses, searchString);
		}
		catch (IDOException ie) {
			ie.printStackTrace(System.err);
			return 0;
		}
	}
	
	public int getNumberOfApplicantsForSchool(int schoolID, int seasonID, int schoolYearID, int[] choiceOrder, String[] validStatuses, String searchString, int placementType) throws RemoteException {
		try {
			return getSchoolChoiceHome().getCount(schoolID, seasonID, schoolYearID, choiceOrder, validStatuses, searchString, placementType);
		}
		catch (IDOException ie) {
			ie.printStackTrace(System.err);
			return 0;
		}
	}

	public int getNumberOfApplicants(String[] validStatuses) throws RemoteException {
		try {
			int seasonID = -1;
			try {
				seasonID = ((Integer) getCareBusiness().getCurrentSeason().getPrimaryKey()).intValue();
			}
			catch (FinderException fe) {
				seasonID = -1;
			}
			return getSchoolChoiceHome().getCount(validStatuses, seasonID);
		}
		catch (IDOException ie) {
			ie.printStackTrace(System.err);
			return 0;
		}
	}

	public int getNumberOfApplicants () {
		try {
			final Date startDate = getSchoolChoiceStartDate ().getDate ();
			final Date endDate = getSchoolChoiceEndDate ().getDate ();
			return getSchoolChoiceHome ().getCount
			(getCareBusiness().getCurrentSeason (), startDate, endDate);
		}	catch (Exception e) {
			e.printStackTrace ();
			return 0;
		}
	}

	public IWTimestamp getSchoolChoiceStartDate () throws RemoteException,
	FinderException {
		IWTimestamp stamp = getTimestampFromProperty ("choice_start_date");
		if (stamp == null) {
			stamp = new IWTimestamp(1, 1, 2000);
		}
		return stamp;
	}

	public IWTimestamp getSchoolChoiceEndDate () throws RemoteException,
	FinderException {
		IWTimestamp stamp = getTimestampFromProperty ("choice_end_date");
		if (stamp == null) {
			stamp = new IWTimestamp(01, 01, 2000);
		}
		return stamp;
	}
	
	public IWTimestamp getSchoolChoiceCriticalDate () throws RemoteException,
	FinderException {
		IWTimestamp stamp = getTimestampFromProperty ("choice_critical_date");
		if (stamp == null) {
			stamp = new IWTimestamp(01, 01, 2000);
		}
		return stamp;
	}

	private IWTimestamp getTimestampFromProperty (final String key)
	throws RemoteException, FinderException {
		final IWPropertyList properties = getIWApplicationContext ()
		.getSystemProperties ().getProperties ("school_properties");
		final String valueAsString = properties.getProperty(key);
		if (valueAsString != null) {
			final IWTimestamp seasonStart
			= new IWTimestamp (getCareBusiness().getCurrentSeason ().getSchoolSeasonStart ());
			final IWTimestamp result = new IWTimestamp (seasonStart);
			result.setDay (Integer.parseInt (valueAsString.substring (0, 2)));
			result.setMonth (Integer.parseInt (valueAsString.substring (3, 5)));
			
			if (valueAsString.length() == 10) { // the property is set in format dd.mm.yyyy
				result.setYear(Integer.parseInt(valueAsString.substring(6, 10)));
			}
			
			return result;
		}
		return null;
	}

	public Collection getApplicantsForSchoolAndSeasonAndGrade(int schoolID, int seasonID, int schoolYearID) throws RemoteException {
		try {
			return getSchoolChoiceHome().findBySchoolAndSeasonAndGrade(schoolID, seasonID, schoolYearID);
		}
		catch (FinderException fe) {
			return new Vector();
		}
	}

	public Collection getApplicationsInClass(SchoolClass schoolClass, boolean confirmation) throws RemoteException {
		try {
			return getSchoolChoiceHome().findChoicesInClassAndSeasonAndSchool(((Integer) schoolClass.getPrimaryKey()).intValue(), schoolClass.getSchoolSeasonId(), schoolClass.getSchoolId(), confirmation);
		}
		catch (FinderException fe) {
			return new Vector();
		}
	}

	public void createSchoolChoiceReminder(final String text, final java.util.Date eventDate, final java.util.Date reminderDate, final User user) throws CreateException, RemoteException {
		final SchoolChoiceReminder reminder = getSchoolChoiceReminderHome().create();
		reminder.setText(text);
		reminder.setEventDate(eventDate);
		reminder.setReminderDate(reminderDate);
		reminder.setUser(user);
		try {
			reminder.setHandler(getUserBusiness().getRootCustomerChoiceGroup());
			reminder.store();
		}
		catch (FinderException e) {
			throw new CreateException(e.getMessage());
		}
	}

	public SchoolChoiceReminder[] findAllSchoolChoiceReminders() throws RemoteException, FinderException {
		//return getSchoolChoiceReminderHome ().findAll ();
		Collection reminders = getSchoolChoiceReminderHome().findAll();
		return (SchoolChoiceReminder[]) reminders.toArray(new SchoolChoiceReminder[0]);
	}

	public SchoolChoiceReminder[] findUnhandledSchoolChoiceReminders(final Group[] groups) throws RemoteException, FinderException {
		//return getSchoolChoiceReminderHome ().findUnhandled (groups);
		Collection reminders = getSchoolChoiceReminderHome().findUnhandled(groups);
		return (SchoolChoiceReminder[]) reminders.toArray(new SchoolChoiceReminder[0]);
	}

	public SchoolChoiceReminder findSchoolChoiceReminder(final int id) throws RemoteException, FinderException {
		return getSchoolChoiceReminderHome().findByPrimaryKey(new Integer(id));
	}

	private SchoolChoiceReminderHome getSchoolChoiceReminderHome() throws RemoteException {
		return (SchoolChoiceReminderHome) IDOLookup.getHome(SchoolChoiceReminder.class);
	}

	/**
	 * @return int id of document
	 */
	public int generateReminderLetter(final int reminderId, final MailReceiver[] receivers) throws RemoteException {
		try {
			DocumentBusiness docBusiness = getDocumentBusiness();
			Font nameFont = getDefaultParagraphFont();
			nameFont.setSize(9);
			Font textFont = getDefaultTextFont();
			textFont.setSize(9);
			final MemoryFileBuffer buffer = new MemoryFileBuffer();
			final OutputStream outStream = new MemoryOutputStream(buffer);
			final Document document = new Document(PageSize.A4, docBusiness.getPointsFromMM(30), docBusiness.getPointsFromMM(30), docBusiness.getPointsFromMM(0), docBusiness.getPointsFromMM(0));
			final PdfWriter writer = PdfWriter.getInstance(document, outStream);
			writer.setViewerPreferences(PdfWriter.PageModeUseThumbs | PdfWriter.HideMenubar | PdfWriter.PageLayoutOneColumn | PdfWriter.FitWindow | PdfWriter.CenterWindow);

			document.addCreationDate();
			document.open();
			final SchoolChoiceReminder reminder = findSchoolChoiceReminder(reminderId);
			final PdfPCell emptyCell = new PdfPCell(new Phrase(""));
			emptyCell.setBorder(0);
			emptyCell.setNoWrap(true);
			final String rawReminderString = reminder.getText();
			final Chunk subjectChunk = getSubjectChunk(rawReminderString);
			final Chunk bodyChunk = getBodyChunk(rawReminderString);
			final Phrase reminderPhrase = new Phrase(docBusiness.getPointsFromMM(20), subjectChunk);
			reminderPhrase.add(bodyChunk);
			final PdfPCell reminderCell = new PdfPCell(reminderPhrase);
			reminderCell.setBorder(0);
			reminderCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			reminderCell.setMinimumHeight(docBusiness.getPointsFromMM(125));
			final PdfPTable reminderText = new PdfPTable(1);
			reminderText.setWidthPercentage(100f);
			reminderText.addCell(reminderCell);
			int length = receivers.length;
			for (int i = 0; i < length; i++) {
				if (i != 0) {
					document.newPage();
				}
				
				//commented out above and added following
				docBusiness.createHeaderDate(document,writer,IWTimestamp.RightNow().getLocaleDate(getIWApplicationContext().getApplicationSettings().getDefaultLocale()));
				docBusiness.createLogoContent(document);
				//if(receivers!=null)// for testing
				docBusiness.createAddressContent(getReceiverAddressString(receivers[i]),writer);
				docBusiness.createNewlinesContent(document);
				document.add(reminderText);
				docBusiness.createCommuneFooter(writer);
				
			}
			document.close();
			final ICFileHome icFileHome = (ICFileHome) getIDOHome(ICFile.class);
			final ICFile file = icFileHome.create();
			final InputStream inStream = new MemoryInputStream(buffer);
			file.setFileValue(inStream);
			file.setMimeType("application/x-pdf");
			file.setName("reminder_" + reminderId + ".pdf");
			file.setFileSize(buffer.length());
			file.store();
			return ((Integer) file.getPrimaryKey()).intValue();
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new RemoteException("Couldn't generate reminder " + reminderId, e);
		}
	}

	public MailReceiver[] findAllStudentsThatMustDoSchoolChoiceButHaveNot(SchoolSeason season, SchoolYear[] years, boolean isOnlyInCommune,boolean onlyLastGrade) {
		Collection coll = new ArrayList();
		for (int i = 0; i < years.length; i++) {
			SchoolYear year = years[i];
			//MailReceiver[] receivers = findAllStudentsThatMustDoSchoolChoiceButHaveNot(season, year,isOnlyInCommune, isOnlyInSchoolsLastGrade);
			try {
                MailReceiver[] receivers = getStudentsWithoutSchoolChoice(season,year,isOnlyInCommune,onlyLastGrade);
                for (int j = 0; j < receivers.length; j++) {
                	MailReceiver receiver = receivers[j];
                	coll.add(receiver);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (FinderException e) {
                e.printStackTrace();
            }

		}
		return (MailReceiver[]) coll.toArray(new MailReceiver[0]);
	}

	/**
	 * Gets The number of students who should make a schoolchoice for the SchoolYear year and SchoolSeason season
	 */
	public int getNumberOfStudentsThatMustDoSchoolChoiceButHaveNot(SchoolSeason season, SchoolYear year,boolean isOnlyInCommune,boolean onlyLastGrade) {
		try {
            /*
            MailReceiver[] students = findAllStudentsThatMustDoSchoolChoiceButHaveNot(season, year,isOnlyInCommune, isOnlyInSchoolsLastGrade);
            if (students != null) {
            	return students.length;
            }
            else {
            	return 0;
            }*/
            return countStudentsWithoutSchoolChoice(season,year,isOnlyInCommune,onlyLastGrade);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
	}
	
	/**
	 * Count all students that need to make a school choice for the given season.
	 * @param season
	 * @param year
	 * @param onlyInCommune
	 * @param onlyLastSchoolYear
	 * @return
	 * @throws SQLException
	 * @throws RemoteException
	 */
	public int countStudentsWithoutSchoolChoice(SchoolSeason season,SchoolYear year, boolean onlyInCommune,boolean onlyLastGrade) throws RemoteException, SQLException{
	    com.idega.util.Timer timer = new com.idega.util.Timer();
		timer.start();
//		 TODO get the max age from somwhere ( aron )
		int count = getSchoolChoiceHome().countChildrenWithoutSchoolChoice(season,year,onlyInCommune,onlyLastGrade,14);
		
		timer.stop();
		System.err.println("Found student count " + timer.getTime() + " msec");
		return count;
	}
	
	public MailReceiver[] getStudentsWithoutSchoolChoice(SchoolSeason season, SchoolYear year, boolean onlyInCommune,boolean onlyLastGrade) throws RemoteException, FinderException{
	    com.idega.util.Timer timer = new com.idega.util.Timer();
		timer.start();
		// TODO get the max age from somwhere ( aron )
		MailReceiver[] receivers = getSchoolChoiceHome().getChildrenWithoutSchoolChoice(season,year,onlyInCommune,onlyLastGrade,14);
		
		timer.stop();
		System.err.println("Found students " + timer.getTime() + " msec");
		return receivers;
	}

	/**
	 * Gets an array of SchoolChoiceReminderReceiver for all students who should make a schoolchoice for the SchoolYear year and SchoolSeason season
	 */
	public MailReceiver[] findAllStudentsThatMustDoSchoolChoiceButHaveNot(SchoolSeason season, SchoolYear year,boolean isOnlyInCommune, boolean isOnlyInSchoolsLastGrade) {
		try {
			com.idega.util.Timer timer = new com.idega.util.Timer();
			timer.start();
			final Set ids = new HashSet();
			ids.addAll(this.findAllStudentsThatMustDoSchoolChoice(season, year,isOnlyInSchoolsLastGrade));
			System.err.println("ids.size=" + ids.size());
			try {
				ids.removeAll(findStudentIdsWhoChosedForSeason(season));
			}
			catch (FinderException e) {
				e.printStackTrace();
			}
			System.err.println("ids.size=" + ids.size());
			timer.stop();
			System.err.println("Total search time=" + timer.getTime() + " msec");
			timer.start();

			final int idCount = ids.size();
			final Map receivers = new TreeMap();
			final Iterator iter = ids.iterator();
			final FamilyLogic familyLogic = getMemberFamilyLogic();
			final UserBusiness userBusiness = getUserBusiness();
			for (int i = 0; i < idCount; i++) {
				final Integer id = (Integer) iter.next();
				try {
					final MailReceiver receiver = new MailReceiver(familyLogic, userBusiness, id);
					if (!isOnlyInCommune || receiver.isInDefaultCommune ()) {
						receivers.put(receiver.getSsn(), receiver);
					}
				} catch (Exception e) {
					e.printStackTrace ();
				}
			}
			timer.stop();
			System.err.println("Found parents and addresses in " + timer.getTime() + " msec");
			return (MailReceiver[]) receivers.values().toArray(new MailReceiver[receivers.size()]);

		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}

	public Collection findAllStudentsThatMustDoSchoolChoice(SchoolSeason season, SchoolYear year, boolean isOnlyInSchoolsLastGrade) {
		com.idega.util.Timer timer = new com.idega.util.Timer();
		timer.start();
		final Set ids = new HashSet();
		if (year.getSchoolYearName().equals("F")) {
			try {
				ids.addAll(findPreSchooolStarters(season));
			}
			catch (FinderException e) {
				e.printStackTrace();
			}
			System.err.println("SchoolYearName=F ids.size=" + ids.size());
		}
		else if (year.getSchoolYearName().equals("1")) {
			try {
				ids.addAll(findSchoolStartersNotInPreSchool(season));
			}
			catch (FinderException e) {
				e.printStackTrace();
			}
			System.err.println("SchoolYearName=1 ids.size=" + ids.size());
		}
		else {
			try {
				ids.addAll(findStudentsThatMustDoSchoolChoice(season, year, isOnlyInSchoolsLastGrade));
			}
			catch (FinderException e) {
				e.printStackTrace();
			}
			System.err.println("SchoolYearName=" + year.getSchoolYearName() + " ids.size=" + ids.size());
		}
		timer.stop();
		return ids;
	}

	/**
	 * Returns the Ids for all the students who made a schoolchoice for the season
	 * 
	 * @param season
	 * @return Collection of Integer primary keys to Users
	 * @throws RemoteException
	 * @throws FinderException
	 */
	private Set findStudentIdsWhoChosedForSeason(SchoolSeason season) throws RemoteException, FinderException {
		com.idega.util.Timer timer = new com.idega.util.Timer();
		timer.start();
		final int seasonId = ((Integer) season.getPrimaryKey ()).intValue ();
		final Collection choices = getSchoolChoiceHome().findBySeason(seasonId);
		final Set ids = new HashSet();
		for (Iterator i = choices.iterator(); i.hasNext(); ) {
			final SchoolChoice choice = (SchoolChoice) i.next();
			ids.add(new Integer(choice.getChildId()));
		}
		timer.stop();
		System.err.println("Found " + choices.size() + " chosedstudents in " + timer.getTime() + " msec");
		return ids;
	}

	/**
	 * Find All the Students that have a placement for season season and SchoolYear year and are in the last year of their school
	 * 
	 * @param season
	 *          The Season for the students to do schoolchoice for
	 * @param year
	 *          the SchoolYear
	 * @return A Set of primary Keys for students
	 * @throws FinderException
	 *           if an error occured during search
	 */
	private Set findStudentsThatMustDoSchoolChoice(SchoolSeason season, SchoolYear year,boolean isOnlyInSchoolsLastGrade) throws FinderException {
		try {
			com.idega.util.Timer timer = new com.idega.util.Timer();
			timer.start();

			//final int previousSeasonId = getPreviousSeasonId ();
			SchoolSeason previousSeason = season.getPreviousSeason();
			SchoolYear previousYear = year.getPreviousSchoolYearFromAge();
			final Collection students;
			if (isOnlyInSchoolsLastGrade) {
				students = getSchoolClassMemberHome().findAllLastYearStudentsBySeasonAndYear(previousSeason, previousYear);
			} else {
				students = getSchoolClassMemberHome ().findAllBySeasonAndSchoolYear (previousSeason, previousYear);
			}
			final Set ids = new HashSet();
			for (Iterator i = students.iterator(); i.hasNext(); ) {
				final SchoolClassMember student = (SchoolClassMember) i.next();
				ids.add(new Integer(student.getClassMemberId()));
			}
			timer.stop();
			System.err.println("Found " + students.size() + " finalstudents in " + timer.getTime() + " msec");
			return ids;
		}
		catch (RemoteException re) {
			throw new FinderException(re.getMessage());
		}

	}
	/**
	 * Finds all students who are beginning in school for SchoolSeason season and were not in preschool
	 * 
	 * @param season
	 *          the Season to begin in.
	 * @return a Set of Integer PKs for students.
	 * @throws FinderException
	 */
	private Set findSchoolStartersNotInPreSchool(SchoolSeason season) throws FinderException {
		try {
			com.idega.util.Timer timer = new com.idega.util.Timer();
			timer.start();
			final SchoolYear firstClass = getSchoolYearHome().findByYearName("1");
			Calendar schoolStart = new GregorianCalendar();
			schoolStart.setTime(season.getSchoolSeasonStart());
			final int yearOfSchoolStart = schoolStart.get(Calendar.YEAR);
			final int yearOfBirth = yearOfSchoolStart - firstClass.getSchoolYearAge();
			final Collection students = getUserHome().findUsersByYearOfBirth(yearOfBirth, yearOfBirth);
			final Set ids = new HashSet();
			final SchoolClassMemberHome classMemberHome = getSchoolClassMemberHome();
			SchoolSeason previousSeason = season.getPreviousSeason();
			int previousSeasonId = ((Integer) previousSeason.getPrimaryKey()).intValue();
			for (Iterator i = students.iterator(); i.hasNext(); ) {
				final User student = (User) i.next();
				final Integer studentId = (Integer) student.getPrimaryKey();
				SchoolClassMember member = null;
				try {
					member = classMemberHome.findByUserAndSeason(studentId.intValue(), previousSeasonId);
				}
				catch (Exception e) {
					// not a school Class member - handle in 'finally' clause
				}
				finally {
					if (null == member) {
						ids.add(studentId);
					}
				}
			}
			timer.stop();
			System.err.println("Found " + ids.size() + " (" + students.size() + ") schoolstartersnotinchildcare in " + timer.getTime() + " msec (" + yearOfBirth + ")");
			return ids;
		}
		catch (RemoteException re) {
			throw new FinderException(re.getMessage());
		}
	}

	/**
	 * Finds all students who are beginning in childCareClass ("F") for SchoolSeason season
	 * 
	 * @param season
	 *          the Season to begin in.
	 * @return a Set of Integer PKs for students.
	 * @throws FinderException
	 */
	private Set findPreSchooolStarters(SchoolSeason season) throws FinderException {
		try {
			com.idega.util.Timer timer = new com.idega.util.Timer();
			timer.start();
			final SchoolYear childCare = getSchoolYearHome().findByYearName("F");
			Calendar schoolStart = new GregorianCalendar();
			schoolStart.setTime(season.getSchoolSeasonStart());
			final int yearOfSchoolStart = schoolStart.get(Calendar.YEAR);
			final int yearOfBirth = yearOfSchoolStart - childCare.getSchoolYearAge();
			final Collection students = getUserHome().findUsersByYearOfBirth(yearOfBirth, yearOfBirth);
			final Set ids = new HashSet();
			//TL: Note: I don't think the following clause is valid for PreSchool as those students have never
			//			gone in a SchoolClass the previous year/season.
			//
			/*
			 * final SchoolClassMemberHome classMemberHome = getSchoolClassMemberHome (); SchoolSeason previousSeason = season.getPreviousSeason(); int previousSeasonId = ((Integer)previousSeason.getPrimaryKey()).intValue(); for (Iterator i = students.iterator (); i.hasNext ();) { final User student = (User) i.next (); final Integer studentId = (Integer) student.getPrimaryKey (); SchoolClassMember member = null; try { member = classMemberHome.findByUserAndSeason (studentId.intValue (), previousSeasonId); } catch (Exception e) { // not a school Class member - handle in 'finally' clause } finally { if (null == member) { ids.add (studentId); } }
			 */
			for (Iterator i = students.iterator(); i.hasNext(); ) {
				User student = (User) i.next();
				Integer studentId = (Integer) student.getPrimaryKey();
				ids.add(studentId);
			}
			timer.stop();
			System.err.println("Found " + students.size() + " childcarestarters in " + timer.getTime() + " msec (" + yearOfBirth + ")");
			return ids;
		}
		catch (RemoteException re) {
			throw new FinderException(re.getMessage());
		}
	}
	/* TODO remove when others things have been tested (aron)
	 private PdfPTable getAddressTable(final SchoolChoiceReminderReceiver receiver) {
	 final PdfPTable address = new PdfPTable(new float[]{1, 1});
	 address.setWidthPercentage(100f);
	 final PdfPCell defaultCell = address.getDefaultCell();
	 defaultCell.setBorder(0);
	 defaultCell.setFixedHeight(mmToPoints(55));
	 defaultCell.setPadding(0);
	 defaultCell.setNoWrap(true);
	 defaultCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	 address.addCell(new Phrase(""));
	 address.addCell(new Phrase(getReceiverChunk(receiver)));
	 return address;
	 }
	 */
	private Chunk getSubjectChunk(final String rawString) {
		final int newlineIndex = rawString.indexOf('\n');
		final Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, REMINDER_FONTSIZE);
		return new Chunk(newlineIndex != -1 ? rawString.substring(0, newlineIndex) : "", font);
	}

	private Chunk getBodyChunk(final String rawString) {
		final int newlineIndex = rawString.indexOf('\n');
		final String bodyString = rawString.substring(newlineIndex != -1 ? newlineIndex : 0);
		return new Chunk(bodyString, SERIF_FONT);
	}
	/* TODO remove later
	 private Chunk getReceiverChunk(SchoolChoiceReminderReceiver receiver) {
	 StringBuffer address = new StringBuffer(getReceiverAddressString(receiver));
	 address.append("\n\n\n\n\n\n\n\n");
	 return new Chunk(address.toString(), SERIF_FONT);
	 }
	 */
	private String getReceiverAddressString(MailReceiver receiver) {
		//String ssn = PersonalIDFormatter.format(receiver.getSsn(), getIWApplicationContext().getApplicationSettings().getDefaultLocale());
		StringBuffer address = new StringBuffer();
		address.append(getLocalizedString("school.spokesperson_for", "Spokesperson for")).append(": ").append("\n");
		address.append(receiver.getStudentName()).append("\n"); 
		address.append(receiver.getStreetAddress()).append("\n");
		address.append(receiver.getPostalAddress()).append("\n");
		return address.toString();
	}

	// TODO remove later
	/*
	 private Chunk getDateChunk() {
	 return new Chunk(new IWTimestamp().getLocaleDate(getIWApplicationContext().getApplicationSettings().getDefaultLocale(), IWTimestamp.SHORT), SERIF_FONT);
	 }*/
	/*// TODO belongs to DocumentBusiness
	 private float mmToPoints(final float mm) {
	 return mm * 72 / 25.4f;
	 }
	 */
	public void sendMessageToParentOrChild(SchoolChoice choice, User parent, User child, String subject, String body,String contentCode) throws RemoteException {
		getMessageBusiness().createUserMessage(choice, getReceiver(parent, child),null,null, subject, body, true,contentCode);
	}

	public User getReceiver(User parent, User child) {
		return isOfAge(child) ? child : parent;
	}

	/**
	 * @return true if person's age >= 18
	 */
	private boolean isOfAge(User person) {
		try {
			Age age = new Age(person.getDateOfBirth());
			return age.getYears() >= 18;
		}
		catch (NullPointerException e) {
			return false;
		}
	}

	public boolean isInSchoolChoicePeriod() {
		boolean isSCPeriod = false;
		try {
			isSCPeriod = IWTimestamp.RightNow().isBetween(new IWTimestamp(getSchoolChoiceStartDate()), new IWTimestamp(getSchoolChoiceEndDate()));
		} catch (Exception fe) {
			log(fe);
		}
		
		return isSCPeriod;
	}
	
	/**
	 * Returns the SchoolYears that are mandatory to do a schoolChoice for
	 * 
	 * @return A Collection of SchoolYear entities
	 */
	public Collection getMandatorySchoolChoiceYears() {
		//TODO: Move these years to a database table
		Collection years = new ArrayList();
		try {
			SchoolYear preSchoolClass = getSchoolYearHome().findByYearName("F");
			SchoolYear firstClass = getSchoolYearHome().findByYearName("1");
			SchoolYear fourthClass = getSchoolYearHome().findByYearName("4");
			SchoolYear sixthClass = getSchoolYearHome().findByYearName("6");
			SchoolYear seventhClass = getSchoolYearHome().findByYearName("7");

			years.add(preSchoolClass);
			years.add(firstClass);
			years.add(fourthClass);
			years.add(sixthClass);
			years.add(seventhClass);

		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return years;

	}
	
	public List getConnectedSchoolchoices(SchoolChoice selectedChoice){
		
		SchoolChoice current =selectedChoice;
		ArrayList list =new ArrayList();
		try {
			SchoolChoiceHome chome =getSchoolChoiceHome();
			if(current!=null){
				// find leaf
				Collection childs  =chome.findByParent(current);
				while(!childs.isEmpty()){
					for (Iterator iter = childs.iterator(); iter.hasNext();) {
						current = (SchoolChoice) iter.next();
						childs =chome.findByParent(current);
					}
				}
				list.add(0,current);
				Case parent;
				while((parent=current.getParentCase())!=null){
					current =chome.findByPrimaryKey(parent.getPrimaryKey());
					list.add(0,current);
				}
				
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	private DocumentBusiness getDocumentBusiness() throws RemoteException{
		return (DocumentBusiness)getServiceInstance(DocumentBusiness.class);
	}
	
	public void importLanguageToPlacement() {
		try {
			SchoolSeason season = getCareBusiness().getCurrentSeason();
			String[] status = { getCaseStatusInactive().getStatus(), getCaseStatusDeleted().getStatus() };
			Collection choices = getSchoolChoiceHome().findAllWithLanguageWithinSeason(season, status);
			int size = choices.size();
			int number = 1;
			Iterator iter = choices.iterator();
			while (iter.hasNext()) {
				SchoolChoice choice = (SchoolChoice) iter.next();
				System.out.print("[IMPORT] (" + (number++) +"/"+ size + ") Adding language choice for user with ID="+choice.getChildId()+": ");
				try {
					SchoolClassMember member = getSchoolBusiness().getSchoolClassMemberHome().findByUserAndSchoolAndSeason(choice.getChildId(), choice.getChosenSchoolId(), choice.getSchoolSeasonId());
					member.setLanguage(choice.getLanguageChoice());
					member.store();
					System.out.println("Done!");
				}
				catch (FinderException e) {
					System.out.println("No placement found...");
					//Has no placement so nothing is done...
				}
			}
		}
		catch (RemoteException re) {
			re.printStackTrace();
		}
		catch (FinderException fe) {
			fe.printStackTrace();
		}
	}
	
	
	/**
	 * <p>
	 * Returns Collection of SchoolStudyPath's or empty Vector if no paths found or exception occurs. 
	 * </p>
	 * @param schoolStudyPathGroupId id of the school study path group, to which school study paths belong
	 * @return
	 * @throws RemoteException
	 */
	public Collection findHandicraftOptions(int schoolStudyPathGroupId) {		
		SchoolStudyPathGroup pathGroup;
		try {
			pathGroup = this.getSchoolStudyPathGroupHome().findByPrimaryKey(new Integer(schoolStudyPathGroupId));
			return this.getSchoolStudyPathHome().findBySchoolStudyPathGroup(pathGroup);
		}
		catch (FinderException e) {
			e.printStackTrace();
			return new Vector();
		}
	}	
	
	private SchoolStudyPathHome getSchoolStudyPathHome() {
		try {
			return (SchoolStudyPathHome) IDOLookup.getHome(SchoolStudyPath.class);
		}		
		catch (IDOLookupException e) {			
			e.printStackTrace();
			return null;
		}
	}
	
	private SchoolStudyPathGroupHome getSchoolStudyPathGroupHome() {
		try {
			return (SchoolStudyPathGroupHome) IDOLookup.getHome(SchoolStudyPathGroup.class);
		}		
		catch (IDOLookupException e) {			
			e.printStackTrace();
			return null;
		}
	}	
		
}
