package se.idega.idegaweb.commune.school.business;

import is.idega.idegaweb.member.business.MemberFamilyLogic;

import java.io.*;
import java.rmi.RemoteException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import se.cubecon.bun24.viewpoint.business.ViewpointBusiness;
import se.cubecon.bun24.viewpoint.data.SubCategory;
import se.idega.idegaweb.commune.business.CommuneUserBusiness;
import se.idega.idegaweb.commune.message.business.MessageBusiness;
import se.idega.idegaweb.commune.school.data.*;

import com.idega.block.process.data.Case;
import com.idega.block.process.data.CaseStatus;
import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.data.*;
import com.idega.core.data.*;
import com.idega.core.data.Address;
import com.idega.data.*;
import com.idega.io.*;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.user.data.UserHome;
import com.idega.util.IWTimestamp;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.*;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version 1.0
 */
public class SchoolChoiceBusinessBean extends com.idega.block.process.business.CaseBusinessBean implements SchoolChoiceBusiness, com.idega.block.process.business.CaseBusiness {
	public final static String SCHOOL_CHOICE_CASECODE = "MBSKOLV";
    private final static int REMINDER_FONTSIZE = 12;
    private final static Font SERIF_FONT
        = FontFactory.getFont (FontFactory.TIMES, REMINDER_FONTSIZE);
    private final static Font SANSSERIF_FONT
        = FontFactory.getFont (FontFactory.HELVETICA, REMINDER_FONTSIZE - 1);

	public String getBundleIdentifier() {
		return se.idega.idegaweb.commune.presentation.CommuneBlock.IW_BUNDLE_IDENTIFIER;
	}
	public SchoolBusiness getSchoolBusiness() throws java.rmi.RemoteException {
		return (SchoolBusiness) this.getServiceInstance(SchoolBusiness.class);
	}
	public MessageBusiness getMessageBusiness() throws RemoteException {
		return (MessageBusiness) this.getServiceInstance(MessageBusiness.class);
	}
	
	public MemberFamilyLogic  getMemberFamilyLogic() throws RemoteException {
		return (MemberFamilyLogic) this.getServiceInstance(MemberFamilyLogic.class);
	}
	public CommuneUserBusiness  getUserBusiness() throws RemoteException {
		return (CommuneUserBusiness) this.getServiceInstance(CommuneUserBusiness.class);
	}
	public SchoolHome getSchoolHome() throws java.rmi.RemoteException {
		return getSchoolBusiness().getSchoolHome();
	}
	public SchoolChoiceHome getSchoolChoiceHome() throws java.rmi.RemoteException {
		return (SchoolChoiceHome) this.getIDOHome(SchoolChoice.class);
	}

	public SchoolClassMemberHome getSchoolClassMemberHome () throws RemoteException {
		return (SchoolClassMemberHome) this.getIDOHome (SchoolClassMember.class);
	}

	public SchoolYearHome getSchoolYearHome () throws RemoteException {
		return (SchoolYearHome) this.getIDOHome (SchoolYear.class);
	}

	public UserHome getUserHome () throws RemoteException {
		return (UserHome) this.getIDOHome (User.class);
	}

	public CurrentSchoolSeasonHome getCurrentSchoolSeasonHome() throws java.rmi.RemoteException {
		return (CurrentSchoolSeasonHome) this.getIDOHome(CurrentSchoolSeason.class);
	}
	public SchoolSeasonHome getSchoolSeasonHome() throws java.rmi.RemoteException {
		return (SchoolSeasonHome) this.getIDOHome(SchoolSeason.class);
	}
	public SchoolSeason getCurrentSeason() throws java.rmi.RemoteException, javax.ejb.FinderException {
		CurrentSchoolSeason season = getCurrentSchoolSeasonHome().findCurrentSeason();
		return getSchoolSeasonHome().findByPrimaryKey(season.getCurrent());
	}

	public int getPreviousSeasonId () throws RemoteException, FinderException {
        final int currentSeasonId
                = getCommuneSchoolBusiness().getCurrentSchoolSeasonID();
        return getCommuneSchoolBusiness() .getPreviousSchoolSeasonID
                (currentSeasonId);
	}

	public SchoolChoice getSchoolChoice(int schoolChoiceId) throws FinderException, RemoteException {
		return getSchoolChoiceHome().findByPrimaryKey(new Integer(schoolChoiceId));
	}
	public School getSchool(int school) throws RemoteException {
		return getSchoolBusiness().getSchool(new Integer(school));
	}
	
	public SchoolChoice createSchoolChangeChoice(int userId, int childId, int school_type_id, int current_school, int chosen_school, int grade, int method, int workSituation1, int workSituation2, String language, String message, boolean keepChildrenCare, boolean autoAssign, boolean custodiansAgree, boolean schoolCatalogue)throws IDOCreateException{
		try {
			java.sql.Timestamp time = new java.sql.Timestamp(System.currentTimeMillis());
			CaseStatus unHandledStatus = getCaseStatus(super.getCaseStatusInactive().getStatus());
			SchoolChoice choice = createSchoolChoice(userId, childId, school_type_id, current_school,chosen_school, grade, 1, method, workSituation1, workSituation2, language, message, time, true, keepChildrenCare, autoAssign, custodiansAgree, schoolCatalogue, unHandledStatus, null);
			ArrayList choices = new ArrayList(1);
			choices.add(choice);
			handleSeparatedParentApplication(childId,userId,choices,true);
			handleSchoolChangeHeadMasters(getUser(childId),current_school,chosen_school);
			return choice;
		}
		catch (Exception ex) {
			throw new IDOCreateException(ex.getMessage());
		}
	}
	public List createSchoolChoices(int userId, int childId, int school_type_id, int current_school, int chosen_school_1, int chosen_school_2, int chosen_school_3, int grade, int method, int workSituation1, int workSituation2, String language, String message, boolean changeOfSchool, boolean keepChildrenCare, boolean autoAssign, boolean custodiansAgree, boolean schoolCatalogue) throws IDOCreateException {
		if(changeOfSchool){
			SchoolChoice choice = createSchoolChangeChoice(userId,childId,school_type_id,current_school,chosen_school_1,grade,method,workSituation1,workSituation2,language,message,keepChildrenCare,autoAssign,custodiansAgree,schoolCatalogue);
			ArrayList list = new ArrayList(1);
			list.add(choice);
			return list;
		}else{
			int caseCount = 3;
			java.sql.Timestamp time = new java.sql.Timestamp(System.currentTimeMillis());
			List returnList = new Vector(3);
			javax.transaction.UserTransaction trans = this.getSessionContext().getUserTransaction();
			try {
				trans.begin();
				CaseStatus first = getCaseStatus("PREL");
				CaseStatus other = getCaseStatus(super.getCaseStatusInactive().getStatus());
				int[] schoolIds = { chosen_school_1, chosen_school_2, chosen_school_3 };
				SchoolChoice choice = null;
				for (int i = 0; i < caseCount; i++) {
					choice = createSchoolChoice(userId, childId, school_type_id, current_school, schoolIds[i], grade, i + 1, method, workSituation1, workSituation2, language, message, time, changeOfSchool, keepChildrenCare, autoAssign, custodiansAgree, schoolCatalogue, i == 0 ? first : other, choice);
					returnList.add(choice);
				}
				handleSeparatedParentApplication(childId,userId,returnList,false);
				trans.commit();
				
				int previousSeasonID = getCommuneSchoolBusiness().getPreviousSchoolSeasonID(getCommuneSchoolBusiness().getCurrentSchoolSeasonID());
				if (previousSeasonID != -1)
					getCommuneSchoolBusiness().setNeedsSpecialAttention(childId, previousSeasonID,true);
				
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
	
	public SchoolChoice createSchoolChoice(int userId, int childId, int school_type_id, int current_school, int chosen_school, int grade, int choiceOrder, int method, int workSituation1, int workSituation2, String language, String message, java.sql.Timestamp choiceDate, boolean changeOfSchool, boolean keepChildrenCare, boolean autoAssign, boolean custodiansAgree, boolean schoolCatalogue, CaseStatus caseStatus, Case parentCase) throws CreateException, RemoteException, FinderException {
		SchoolSeason season = null;
		try {
			season = getCurrentSeason();
		}
		catch(FinderException fex) {
			season = null;
		}

		SchoolChoice choice = null;
		boolean hasPrevious = false;

		if (season != null) {
			Collection choices = this.findByStudentAndSeason(childId, ((Integer)season.getPrimaryKey()).intValue());
			if (!choices.isEmpty()) {
				Iterator iter = choices.iterator();
				while (iter.hasNext()) {
					SchoolChoice element = (SchoolChoice) iter.next();
					if (element.getChoiceOrder() == choiceOrder) {
						choice = element;
						hasPrevious = true;
						continue;	
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
		if (current_school > 0)
			choice.setCurrentSchoolId(current_school);
		if (chosen_school != -1)
			choice.setChosenSchoolId(chosen_school);
		choice.setGrade(grade);
		choice.setChoiceOrder(choiceOrder);
		choice.setMethod(method);
		choice.setWorksituation1(workSituation1);
		choice.setWorksituation2(workSituation2);
		choice.setLanguageChoice(language);
		choice.setChangeOfSchool(changeOfSchool);
		choice.setKeepChildrenCare(keepChildrenCare);
		choice.setAutoAssign(autoAssign);
		choice.setCustodiansAgree(custodiansAgree);
		choice.setSchoolCatalogue(schoolCatalogue);
		choice.setSchoolChoiceDate(choiceDate);
		choice.setSchoolTypeId(school_type_id);
		choice.setMessage(message);
		if (season != null) {
			Integer seasonId = (Integer) season.getPrimaryKey();
			choice.setSchoolSeasonId(seasonId.intValue());
		}
		IWTimestamp stamp = new IWTimestamp();
		stamp.addSeconds((10 - (choiceOrder * 10)));
		choice.setCreated(stamp.getTimestamp());
		choice.setCaseStatus(caseStatus);
		if (caseStatus.getStatus().equalsIgnoreCase("PREL")) {
			getMessageBusiness().createUserMessage(choice.getOwner(), getPreliminaryMessageSubject(), getPreliminaryMessageBody(choice));
		}
		if (parentCase != null)
			choice.setParentCase(parentCase);
		try {
			choice.store();
		}
		catch (IDOStoreException idos) {
			throw new IDOCreateException(idos);
		}
		return choice;
	}
	
	private void handleSchoolChangeHeadMasters(User child,int oldSchoolID,int newSchoolID)throws RemoteException{
		try{
		getMessageBusiness().createUserMessage(getSchoolBusiness().getHeadmaster(oldSchoolID),getOldHeadmasterSubject(),getOldHeadmasterBody(child));
		getMessageBusiness().createUserMessage(getSchoolBusiness().getHeadmaster(newSchoolID),getNewHeadMasterSubject(),getNewHeadmasterBody(child));
		}
		catch(Exception ex){throw new RemoteException(ex.getMessage());}
	}
	
	private void handleSeparatedParentApplication(int childID,int applicationParentID,List choices,boolean isSchoolChangeApplication)throws RemoteException{
		try{
		if(choices!=null){
			SchoolChoice choice = (SchoolChoice)choices.get(0);
			MemberFamilyLogic familyLogic = getMemberFamilyLogic();
			Collection parents = familyLogic.getCustodiansFor(getUser(childID));
			User appParent = getUser(applicationParentID);
			Iterator iter = parents.iterator();		
			User otherParent = null;
			while(iter.hasNext()){
				User parent = (User) iter.next();
				if( !parent.getPrimaryKey().equals(appParent.getPrimaryKey() )){
					otherParent = parent;
					break;
				}
			}
			if(otherParent != null){
				Address appAddress = getUserBusiness().getUsersMainAddress(appParent);
				Address otherAddress = getUserBusiness().getUsersMainAddress(otherParent);
				if(!appAddress.getStreetAddress().equalsIgnoreCase(otherAddress.getStreetAddress() )){
					// We need to let the other parent know about the application
					// If the parent has an Citizen Account we can send a message 
					// else we senda a letter
					
					// send message
					
						if(getUserBusiness().hasUserLogin(otherParent)){
							if(isSchoolChangeApplication)
								getMessageBusiness().createUserMessage(otherParent,getSeparateParentSubjectChange(),getSeparateParentMessageBodyChange(choice)); 
							else
								getMessageBusiness().createUserMessage(otherParent,getSeparateParentSubjectAppl(),getSeparateParentMessageBodyAppl(choices,otherParent)); 
						}
						// send letter
						else{
							if(isSchoolChangeApplication)
								getMessageBusiness().createPrintedLetterMessage(otherParent,getSeparateParentSubjectChange(),getSeparateParentMessageBodyChange(choice));
							else
								getMessageBusiness().createPrintedLetterMessage(otherParent,getSeparateParentSubjectAppl(),getSeparateParentMessageBodyAppl(choices,otherParent));
						}
					
					
				}
			}
		}
		}catch(Exception ex){throw new RemoteException(ex.getMessage());}
	}
	
	public void createCurrentSchoolSeason(Integer newKey, Integer oldKey) throws java.rmi.RemoteException {
		CurrentSchoolSeasonHome shome = getCurrentSchoolSeasonHome();
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
			is.idega.idegaweb.member.business.MemberFamilyLogic ml = (is.idega.idegaweb.member.business.MemberFamilyLogic) getServiceInstance(is.idega.idegaweb.member.business.MemberFamilyLogic.class);
			se.idega.idegaweb.commune.business.CommuneUserBusiness ub = (se.idega.idegaweb.commune.business.CommuneUserBusiness) getServiceInstance(se.idega.idegaweb.commune.business.CommuneUserBusiness.class);
			familyMembers[0] = ub.createCitizenByPersonalIDIfDoesNotExist("Gunnar", "P?ll", "Dan?elsson", "0101");
			familyMembers[1] = ub.createCitizenByPersonalIDIfDoesNotExist("P?ll", "", "Helgason", "0202");
			familyMembers[2] = ub.createCitizenByPersonalIDIfDoesNotExist("Tryggvi", "", "L?russon", "0303");
			familyMembers[3] = ub.createCitizenByPersonalIDIfDoesNotExist("??rhallur", "", "Dan?elsson", "0404");
			familyMembers[4] = ub.createCitizenByPersonalIDIfDoesNotExist("J?n", "Karl", "J?nsson", "0505");
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
			super.changeCaseStatus(choice, getCaseStatusInactive().getPrimaryKey().toString(), performer);
			choice.store();
			Iterator children = choice.getChildren();
			if (children.hasNext()) {
				Case child = (Case) children.next();
				super.changeCaseStatus(child, "PREL", performer);
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
	
	public void rejectApplication(int applicationID, int seasonID, User performer, String messageSubject, String messageBody) throws RemoteException {
		try {
			SchoolChoice choice = this.getSchoolChoiceHome().findByPrimaryKey(new Integer(applicationID));
			super.changeCaseStatus(choice, getCaseStatusInactive().getPrimaryKey().toString(), performer);
			choice.store();
			
			Collection coll = findByStudentAndSeason(choice.getChildId(), seasonID);
			Iterator iter = coll.iterator();
			while (iter.hasNext()) {
				SchoolChoice element = (SchoolChoice) iter.next();
				if (element.getChoiceOrder() == (choice.getChoiceOrder() + 1)) {
					super.changeCaseStatus(element, "PREL", performer);
					continue;
				}
			}
		}
		catch (FinderException fe) {
		}
	}
	
	public boolean preliminaryAction(Integer pk, User performer) {
		try {
			SchoolChoice choice = getSchoolChoiceHome().findByPrimaryKey(pk);
			super.changeCaseStatus(choice, "PREL", performer);
			choice.store();
			getMessageBusiness().createUserMessage(choice.getOwner(), getPreliminaryMessageSubject(), getPreliminaryMessageBody(choice));
			return true;
		}
		catch (Exception e) {
		}
		return false;
	}

	public void setAsPreliminary(SchoolChoice choice, User performer) throws RemoteException {
		try {
			super.changeCaseStatus(choice, "PREL", performer);
		}
		catch (Exception e) {
		}
	}
	
	public void setChildcarePreferences(User performer, int childID, boolean freetimeInThisSchool, String otherMessage, String messageSubject, String messageBody) throws RemoteException {
		try {
			SchoolSeason season = getCurrentSeason();
			if (season != null) {
				Collection choices = this.findByStudentAndSeason(childID, ((Integer)season.getPrimaryKey()).intValue());
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
									vpb.createViewpoint(performer, messageSubject, messageBody, subCategory.getName(), getUserBusiness().getRootAdministratorGroupID());
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

	public boolean groupPlaceAction(Integer pk, User performer) {
		try {
			SchoolChoice choice = getSchoolChoiceHome().findByPrimaryKey(pk);
			super.changeCaseStatus(choice, "PLAC", performer);
			return true;
		}
		catch (Exception e) {
		}
		return false;
	}

	protected String getPreliminaryMessageBody(SchoolChoice theCase) throws RemoteException, FinderException {
		Object[] arguments = { theCase.getChosenSchool().getName(), theCase.getOwner().getNameLastFirst(true) };
		String body = MessageFormat.format(getLocalizedString("school_choice.prelim_mesg_body", "Dear mr./ms./mrs. {1}\n Your child has been preliminary accepted in: {0}."), arguments);
		
		/*StringBuffer body = new StringBuffer(this.getLocalizedString("school_choice.prelim_mesg_body1", "Dear mr./ms./mrs. "));
		body.append().append("\n");
		body.append(this.getLocalizedString("school_choice.prelim_mesg_body2", "Your child has been preliminary accepted in: ."));
		body.append(getSchool(theCase.getChosenSchoolId()).getSchoolName()).append("\n");*/
		return body;
	}
	protected String getGroupedMessageBody(SchoolChoice theCase) throws RemoteException, FinderException {
		StringBuffer body = new StringBuffer(this.getLocalizedString("acc.app.acc.body1", "Dear mr./ms./mrs. "));
		body.append(theCase.getOwner().getNameLastFirst()).append("\n");
		body.append(this.getLocalizedString("school_choice.group_mesg_body2", "Your child has been grouped  in ."));
		body.append(theCase.getGroupPlace()).append("\n");
		body.append(this.getLocalizedString("school_choice.group_mesg_body3", "in school ."));
		body.append(getSchool(theCase.getChosenSchoolId()).getSchoolName()).append("\n");
		return body.toString();
	}
	
	protected String getSeparateParentMessageBodyAppl(List choices,User parent) throws RemoteException, FinderException {
		Object[] arguments = new Object[4];
		arguments[0] = parent.getNameLastFirst(true);
		Iterator iter = choices.iterator();
		int count = 1;
		while(iter.hasNext()){
			SchoolChoice choice = (SchoolChoice) iter.next();
			arguments[count++] = getSchool(choice.getChosenSchoolId()).getSchoolName();
		}
		if (choices.size() == 1) {
			arguments[2] = "";
			arguments[3] = "";
		}
		
		String body = MessageFormat.format(getLocalizedString("school_choice.sep_parent_appl_mesg_body", "Dear mr./ms./mrs. "), arguments);
		/*StringBuffer body = new StringBuffer(this.getLocalizedString("school_choice.sep_parent_appl_mesg_body1", "Dear mr./ms./mrs. "));
		body.append(parent.getNameLastFirst()).append("\n");
		body.append(this.getLocalizedString("school_choice.separate_parent_appl_mesg_body2", "School application for your child has been received \n The schools are: "));
		body.append(this.getLocalizedString("school_choice.separate_parent_appl_mesg_body3", "You can comment on this within 14 days from now."));*/
		return body;
	}
	
	protected String getSeparateParentMessageBodyChange(SchoolChoice theCase) throws RemoteException, FinderException {
		StringBuffer body = new StringBuffer(this.getLocalizedString("school_choice.sep_parent_change_mesg_body1", "Dear mr./ms./mrs. "));
		body.append(theCase.getOwner().getNameLastFirst()).append("\n");
		body.append(this.getLocalizedString("school_choice.sep_parent_change_mesg_body2", "School change application for your child has been received"));
		body.append(getSchool(theCase.getChosenSchoolId()).getSchoolName()).append("\n");
		body.append(this.getLocalizedString("school_choice.sep_parent_change_mesg_body3", "You can comment on this within 14 days from now."));
		return body.toString();
	}
	
	protected String getOldHeadmasterBody(User student) throws RemoteException, FinderException {
		StringBuffer body = new StringBuffer(this.getLocalizedString("school_choice.old_headmaster_body1", "Dear headmaster "));
		body.append(student.getName()).append("\n");
		body.append(this.getLocalizedString("school_choice.old_headmaster_body2", "Wishes to change to another school."));
		return body.toString();
	}
	
	protected String getNewHeadmasterBody(User student) throws RemoteException, FinderException {
		StringBuffer body = new StringBuffer(this.getLocalizedString("school_choice.old_headmaster_body1", "Dear headmaster "));
		body.append(student.getName()).append("\n");
		body.append(this.getLocalizedString("school_choice.old_headmaster_body2", "Wishes to change to your school."));
		return body.toString();
	}
	
	public String getPreliminaryMessageSubject() {
		return this.getLocalizedString("school_choice.prelim_mesg_subj", "Prelimininary school acceptance");
	}
	public String getGroupedMessageSubject() {
		return this.getLocalizedString("school_choice.group_mesg_subj", "School grouping");
	}
	
	public String getSeparateParentSubjectAppl() {
		return this.getLocalizedString("school_choice.sep_parent_appl_subj", "School application received for your child");
	}
	public String getSeparateParentSubjectChange() {
		return this.getLocalizedString("school_choice.sep_parent_change_subj", "School change application received for your child");
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
			if (SCHOOL_CHOICE_CASECODE.equals(caseCode)) {
				int caseID = ((Integer) theCase.getPrimaryKey()).intValue();
				return this.getSchoolChoice(caseID);
			}
		}
		catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		throw new ClassCastException("Case with casecode: " + caseCode + " cannot be converted to a schoolchoice");
	}
	public String getLocalizedCaseDescription(Case theCase, Locale locale) throws RemoteException {
		SchoolChoice choice = getSchoolChoiceInstance(theCase);
		Object[] arguments = { getUserBusiness().getUser(choice.getChildId()).getFirstName(), String.valueOf(choice.getChoiceOrder()), choice.getChosenSchool().getName() };
		
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
			return getSchoolChoiceHome().findBySchoolAndFreeTime(schoolId,schoolSeasonID,freeTimeInSchool);
		}
		catch (FinderException fe) {
			return new Vector();
		}
	}
		
	public Collection findByStudentAndSeason(int studentID, int seasonID) throws RemoteException {
		try {
			return getSchoolChoiceHome().findByChildAndSeason(studentID, seasonID);
		}
		catch (FinderException fe) {
			return new Vector();
		}
	}
	
	public int getNumberOfApplications(int schoolID, int schoolSeasonID) throws RemoteException {
		try {
			return getSchoolChoiceHome().getNumberOfApplications("PREL", schoolID, schoolSeasonID);
		}
		catch (IDOException ie) {
			return 0;
		}
		catch (FinderException fe) {
			return 0;
		}
	}	

	public int getNumberOfApplicationsForStudents(int userID, int schoolSeasonID) throws RemoteException {
		try {
			return getSchoolChoiceHome().getNumberOfChoices(userID, schoolSeasonID);
		}
		catch (IDOException ie) {
			return 0;
		}
	}	

	public int getNumberOfApplications(int schoolID, int schoolSeasonID, int grade) throws RemoteException {
		try {
			return getSchoolChoiceHome().getNumberOfApplications("PREL", schoolID, schoolSeasonID, grade);
		}
		catch (IDOException ie) {
			return 0;
		}
		catch (FinderException fe) {
			return 0;
		}
	}	

	private CommuneUserBusiness getCommuneUserBusiness() throws RemoteException {
		return (CommuneUserBusiness) getServiceInstance(CommuneUserBusiness.class);
	}

	private SchoolCommuneBusiness getCommuneSchoolBusiness() throws RemoteException {
		return (SchoolCommuneBusiness) getServiceInstance(SchoolCommuneBusiness.class);
	}

	/**
	 * Method getFirstProviderForUser.
	 * If there is no school that the user then the method throws a FinderException.
	 * @param user a user
	 * @return School that is the first school that the user is a manager for.
	 * @throws javax.ejb.FinderException if ther is no school that the user manages.
	 */
	public School getFirstProviderForUser(User user) throws FinderException, RemoteException {
		CommuneUserBusiness commBuiz = getCommuneUserBusiness();

		try {
			Group rootGroup = commBuiz.getRootProviderAdministratorGroup();
			// if user is a SchoolAdministrator
			if (user.hasRelationTo(rootGroup)) {
				Collection schools = getSchoolHome().findAllBySchoolGroup(user);
				if (!schools.isEmpty()) {
					Iterator iter = schools.iterator();
					while (iter.hasNext()) {
						School school = (School) iter.next();
						return school;
					}
				}
			}
		}
		catch (CreateException e) {
			e.printStackTrace();
		}

		throw new FinderException("No school found that " + user.getName() + " manages");
	}
	

	public Collection getApplicantsForSchool(int schoolID,int seasonID,int grade, String[] validStatuses, String searchString) throws RemoteException {
		return getApplicantsForSchool(schoolID, seasonID, grade, null, validStatuses, searchString);
	}
	
	public Collection getApplicantsForSchool(int schoolID,int seasonID,int grade, int[] choiceOrder, String[] validStatuses, String searchString) throws RemoteException {
		try {
			return getSchoolChoiceHome().findChoices(schoolID, seasonID, grade, choiceOrder, validStatuses, searchString);	
		}
		catch (FinderException fe) {
			fe.printStackTrace(System.err);
			return new Vector();
		}
	}


	public Collection getApplicantsForSchoolAndSeasonAndGrade(int schoolID,int seasonID,int grade) throws RemoteException {
		try {
			return getSchoolChoiceHome().findBySchoolAndSeasonAndGrade(schoolID, seasonID, grade);	
		}
		catch (FinderException fe) {
			return new Vector();
		}
	}

    public void createSchoolChoiceReminder
        (final String text, final Date eventDate, final Date reminderDate,
         final User user)
        throws CreateException, RemoteException {
        final SchoolChoiceReminder reminder
                = getSchoolChoiceReminderHome ().create ();
        reminder.setText (text);
        reminder.setEventDate (eventDate);
        reminder.setReminderDate (reminderDate);
        reminder.setUser (user);
        reminder.setHandler (getUserBusiness().getRootAdministratorGroupID());
        reminder.store ();
    }

    public SchoolChoiceReminder [] findAllSchoolChoiceReminders ()
        throws RemoteException, FinderException {
        return getSchoolChoiceReminderHome ().findAll ();
    }

    public SchoolChoiceReminder [] findUnhandledSchoolChoiceReminders
        (final Group [] groups) throws RemoteException, FinderException {
        return getSchoolChoiceReminderHome ().findUnhandled (groups);
    }

    public SchoolChoiceReminder findSchoolChoiceReminder (final int id)
        throws RemoteException, FinderException {
		return (SchoolChoiceReminder) getSchoolChoiceReminderHome ()
                .findByPrimaryKeyIDO (new Integer (id));
	}

    private SchoolChoiceReminderHome getSchoolChoiceReminderHome ()
        throws RemoteException {
        return (SchoolChoiceReminderHome) IDOLookup.getHome
                (SchoolChoiceReminder.class);
    }

	/**
	 * @return int id of document
	 */
    public int generateReminderLetter
        (final int reminderId, final SchoolChoiceReminderReceiver [] receivers)
        throws RemoteException {
        try{
            final MemoryFileBuffer buffer = new MemoryFileBuffer ();
            final OutputStream outStream = new MemoryOutputStream (buffer);
            final Document document = new Document
                    (PageSize.A4, mmToPoints (30), mmToPoints (30),
                     mmToPoints (0), mmToPoints (0));
            final PdfWriter writer = PdfWriter.getInstance(document, outStream);
            writer.setViewerPreferences
                    (PdfWriter.PageModeUseThumbs | PdfWriter.HideMenubar
                     | PdfWriter.PageLayoutOneColumn |PdfWriter.FitWindow
                     |PdfWriter.CenterWindow);
            document.addTitle("Påminnelse " + reminderId);
            document.addCreationDate();
            document.open();
            final SchoolChoiceReminder reminder
                    = findSchoolChoiceReminder (reminderId);
            final PdfPCell emptyCell = new PdfPCell (new Phrase (""));
            emptyCell.setBorder (0);
            emptyCell.setNoWrap (true);
            final String rawReminderString = reminder.getText ();
            final Chunk subjectChunk = getSubjectChunk (rawReminderString);
            final Chunk bodyChunk = getBodyChunk (rawReminderString);
            final Phrase reminderPhrase = new Phrase (mmToPoints (20),
                                                      subjectChunk);
            reminderPhrase.add (bodyChunk);
            final PdfPCell reminderCell = new PdfPCell (reminderPhrase);
            reminderCell.setBorder (0);
            reminderCell.setVerticalAlignment (Element.ALIGN_MIDDLE);
            reminderCell.setMinimumHeight (mmToPoints (125));
            final PdfPTable reminderText = new PdfPTable (1);
            reminderText.setWidthPercentage (100f);
            reminderText.addCell (reminderCell);
            final PdfPCell spaceCell = new PdfPCell (new Phrase (" "));
            spaceCell.setBorder (0);
            spaceCell.setNoWrap (true);
            spaceCell.setMinimumHeight (mmToPoints (65));
            PdfPTable spaceTable = new PdfPTable (1);
            spaceTable.addCell (spaceCell);
            final PdfPTable footer
                    = new PdfPTable (new float [] {1, 1, 1.5f, 1});
            footer.getDefaultCell ().setBorder (0);
            footer.setWidthPercentage (100f);
            footer.addCell (new Phrase (new Chunk ("Postadress:\nNacka Kommun\n131 81 Nacka", SANSSERIF_FONT)));
            footer.addCell (new Phrase (new Chunk ("Besöksadress:\nStadshuset\nGranitvägen 15\nNacka", SANSSERIF_FONT)));
            footer.addCell (new Phrase (new Chunk ("Tel växel:\n718 80 00\n"
                            + "Hemsida:\nwww.nacka24.nacka.se", SANSSERIF_FONT)));
            footer.addCell (new Phrase (new Chunk ("Organisationsnr:\n212000-0167", SANSSERIF_FONT)));
            final String logoPath = getIWApplicationContext().getApplication()
                    .getBundle("se.idega.idegaweb.commune")
                    .getResourcesRealPath() + "/shared/nacka_logo.jpg";
            PdfPCell logoCell;
            try {
                final Image logo = Image.getInstance (logoPath);
                logo.scaleToFit (mmToPoints (30), mmToPoints (15));
                logoCell = new PdfPCell (logo);
                logoCell.setVerticalAlignment (logoCell.ALIGN_MIDDLE);
                logoCell.setHorizontalAlignment (logoCell.ALIGN_LEFT);
            } catch (Exception e) {
                logoCell = new PdfPCell
                        (new Phrase ("The file '" + logoPath
                                     + "' is missing. Please contact the system"
                                     + " administartor."));
            }
            logoCell.setBorder (0);
            final PdfPTable header = new PdfPTable (new float [] {1, 1});
            header.setWidthPercentage (100f);
            final PdfPCell defaultCell = header.getDefaultCell ();
            defaultCell.setBorder (0);
            defaultCell.setFixedHeight (mmToPoints (30));
            defaultCell.setPadding (0);
            defaultCell.setNoWrap (true);
            defaultCell.setVerticalAlignment (Element.ALIGN_MIDDLE);
            header.addCell (logoCell);
            header.addCell (new Phrase (getDateChunk ()));
            for (int i = 0; i < receivers.length; i++) {
                if (i != 0) { document.newPage (); }
                document.add (header);
                document.add (getAddressTable (receivers [i]));
                document.add (reminderText);
                document.add (spaceTable);
                final PdfContentByte cb = writer.getDirectContent();
                cb.setLineWidth (1);
                cb.moveTo (mmToPoints(30), mmToPoints(22));
                cb.lineTo (mmToPoints(180), mmToPoints(22));
                cb.stroke (); 
                document.add (footer);
            }
            document.close();
            final ICFileHome icFileHome
                    = (ICFileHome) getIDOHome(ICFile.class);
            final ICFile file = icFileHome.create();
            final InputStream inStream = new MemoryInputStream (buffer);
            file.setFileValue(inStream);
            file.setMimeType("application/x-pdf");
            file.setName("reminder_" + reminderId + ".pdf");
            file.setFileSize(buffer.length());
            file.store();
            return file.getID ();
        } catch (Exception e) {
            e.printStackTrace ();
            throw new RemoteException ("Couldn't generate reminder "
                                       + reminderId, e);
        }
    }

    public SchoolChoiceReminderReceiver []
        findAllStudentsThatMustDoSchoolChoice ()
        throws RemoteException, FinderException {
        com.idega.util.Timer timer = new com.idega.util.Timer ();
        timer.start ();

        final Set ids = findStudentsInFinalClassesThatMustDoSchoolChoice ();
        System.err.println ("ids.size=" + ids.size ());
        ids.addAll (findSchoolStartersNotInChildCare ());
        System.err.println ("ids.size=" + ids.size ());
        ids.addAll (findChildCareStarters ());
        System.err.println ("ids.size=" + ids.size ());
        ids.removeAll (findStudentIdsWhoChosedForCurrentSeason ());
        System.err.println ("ids.size=" + ids.size ());
        timer.stop ();
        System.err.println ("Total search time=" + timer.getTime () + " msec");
        timer.start ();

        final int idCount = ids.size ();
        final Map receivers = new TreeMap ();
        final Iterator iter = ids.iterator ();
        final MemberFamilyLogic familyLogic = getMemberFamilyLogic();
        final UserBusiness userBusiness = getUserBusiness();
        for (int i = 0; i < idCount; i++) {
            final Integer id = (Integer) iter.next ();
            final SchoolChoiceReminderReceiver receiver
                    = new SchoolChoiceReminderReceiver (familyLogic,
                                                        userBusiness, id);
            receivers.put (receiver.getSsn (), receiver);
        }
        timer.stop ();
        System.err.println ("Found parents and addresses in "
                            + timer.getTime () + " msec");
        return (SchoolChoiceReminderReceiver [])
                receivers.values ().toArray
                (new SchoolChoiceReminderReceiver [receivers.size ()]);
    }

	private Set findStudentIdsWhoChosedForCurrentSeason ()
        throws RemoteException, FinderException {
        com.idega.util.Timer timer = new com.idega.util.Timer ();
        timer.start ();
        final Integer currentSeasonId
                = (Integer) getCurrentSeason ().getPrimaryKey ();
        final Collection choices = getSchoolChoiceHome().findBySeason
                (currentSeasonId.intValue ());
        final Set ids = new HashSet ();
        for (Iterator i = choices.iterator (); i.hasNext ();) {
            final SchoolChoice choice = (SchoolChoice) i.next ();
            ids.add (new Integer (choice.getChildId ()));
        }
        timer.stop ();
        System.err.println ("Found " + choices.size () + " chosedstudents in "
                            + timer.getTime () + " msec");
        return ids;
	}

    private Set findStudentsInFinalClassesThatMustDoSchoolChoice ()
        throws RemoteException, FinderException {
        com.idega.util.Timer timer = new com.idega.util.Timer ();
        timer.start ();

        final int previousSeasonId = getPreviousSeasonId ();
        System.err.println ("Previous season=" + previousSeasonId);
        final Collection students
                = getSchoolClassMemberHome().findAllBySeasonAndMaximumAge
                (previousSeasonId, 14);
        final Set ids = new HashSet ();
        for (Iterator i = students.iterator (); i.hasNext ();) {
            final SchoolClassMember student = (SchoolClassMember) i.next ();
            ids.add (new Integer (student.getClassMemberId ()));
        }
        timer.stop ();
        System.err.println ("Found " + students.size () + " finalstudents in "
                            + timer.getTime () + " msec");
        return ids;        
    }

    private Set findSchoolStartersNotInChildCare ()throws RemoteException,
                                                          FinderException {
        com.idega.util.Timer timer = new com.idega.util.Timer ();
        timer.start ();
        final SchoolYear childCare = getSchoolYearHome ().findByYearName ("1");
        final int currentYear = Calendar.getInstance ().get (Calendar.YEAR);
        final int yearOfBirth = currentYear - childCare.getSchoolYearAge();
        final Collection students = getUserHome().findUsersByYearOfBirth
                (yearOfBirth, yearOfBirth);
        final Set ids = new HashSet ();
        final SchoolClassMemberHome classMemberHome
                = getSchoolClassMemberHome ();
        final int previousSeasonId = getPreviousSeasonId ();
        for (Iterator i = students.iterator (); i.hasNext ();) {
            final User student = (User) i.next ();
            final Integer studentId = (Integer) student.getPrimaryKey ();
            SchoolClassMember member = null;
            try {
                member = classMemberHome.findByUserAndSeason
                        (studentId.intValue (), previousSeasonId);
            } catch (Exception e) {
                // not a school class member - handle in 'finally' clause
            } finally {
                if (null == member) {
                    ids.add (studentId);
                }
            }
        }
        timer.stop ();
        System.err.println ("Found " + ids.size () + " (" + students.size () + ") schoolstartersnotinchildcare in "
                            + timer.getTime () + " msec (" + yearOfBirth + ")");
        return ids;        
    }

    private Set findChildCareStarters () throws RemoteException,
                                                FinderException {
        com.idega.util.Timer timer = new com.idega.util.Timer ();
        timer.start ();
        final SchoolYear childCare = getSchoolYearHome ().findByYearName ("F");
        final int currentYear = Calendar.getInstance ().get (Calendar.YEAR);
        final int yearOfBirth = currentYear - childCare.getSchoolYearAge();
        final Collection students = getUserHome().findUsersByYearOfBirth
                (yearOfBirth, yearOfBirth);
        final Set ids = new HashSet ();
        for (Iterator i = students.iterator (); i.hasNext ();) {
            final User student = (User) i.next ();
            ids.add (student.getPrimaryKey ());
        }
        timer.stop ();
        System.err.println ("Found " + students.size () + " childcarestarters in "
                            + timer.getTime () + " msec (" + yearOfBirth + ")");
        return ids;        
    }

    private static PdfPTable getAddressTable
        (final SchoolChoiceReminderReceiver receiver) {
        final PdfPTable address = new PdfPTable (new float [] {1, 1});
        address.setWidthPercentage (100f);
        final PdfPCell defaultCell = address.getDefaultCell ();
        defaultCell.setBorder (0);
        defaultCell.setFixedHeight (mmToPoints (55));
        defaultCell.setPadding (0);
        defaultCell.setNoWrap (true);
        defaultCell.setVerticalAlignment (Element.ALIGN_MIDDLE);
        address.addCell (new Phrase (""));
        address.addCell (new Phrase (getReceiverChunk (receiver)));
        return address;
    }

    private static Chunk getSubjectChunk (final String rawString) {
        final int newlineIndex = rawString.indexOf ('\n');
        final Font font = FontFactory.getFont (FontFactory.HELVETICA_BOLD,
                                               REMINDER_FONTSIZE);
        return new Chunk (newlineIndex != -1 ? rawString.substring
                          (0, newlineIndex) : "", font);
    }

    private static Chunk getBodyChunk (final String rawString) {
        final int newlineIndex = rawString.indexOf ('\n');
        final String bodyString
                = rawString.substring (newlineIndex != -1 ? newlineIndex : 0);
        return new Chunk (bodyString, SERIF_FONT);
    }

    private static Chunk getReceiverChunk
        (final SchoolChoiceReminderReceiver receiver) {
        final String longSsn = receiver.getSsn ();
        final String ssn = longSsn.substring (2, 8) + "-"
                + longSsn.substring (8);
        final String address = receiver.getParentName () + "\n"
                + receiver.getStreetAddress () + "\n"
                + receiver.getPostalAddress () + "\n"
                + "\n\n\n\n\n\n\nVårdnadshavare för:\n" + ssn + " "
                + receiver.getStudentName ();
        return new Chunk (address, SERIF_FONT);
    }

    private static Chunk getDateChunk () {
        final SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd");
        return new Chunk (formatter.format(new Date ()), SERIF_FONT);
    }

    private static float mmToPoints (final float mm) {
        return mm*72/25.4f;
    }
}
