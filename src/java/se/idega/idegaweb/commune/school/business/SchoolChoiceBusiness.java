/*
 * $Id: SchoolChoiceBusiness.java,v 1.75 2004/11/18 19:18:38 aron Exp $
 * Created on 18.11.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.school.business;

import is.idega.block.family.business.FamilyLogic;

import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import se.idega.idegaweb.commune.business.CommuneUserBusiness;
import se.idega.idegaweb.commune.message.business.MessageBusiness;
import se.idega.idegaweb.commune.school.data.SchoolChoice;
import se.idega.idegaweb.commune.school.data.SchoolChoiceHome;
import se.idega.idegaweb.commune.school.data.SchoolChoiceReminder;

import com.idega.block.process.business.CaseBusiness;
import com.idega.block.process.data.Case;
import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolClass;
import com.idega.block.school.data.SchoolClassMemberHome;
import com.idega.block.school.data.SchoolSeason;
import com.idega.block.school.data.SchoolYear;
import com.idega.block.school.data.SchoolYearHome;
import com.idega.business.IBOService;
import com.idega.data.IDOCreateException;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;

/**
 * 
 *  Last modified: $Date: 2004/11/18 19:18:38 $ by $Author: aron $
 * 
 * @author <a href="mailto:aron@idega.com">aron</a>
 * @version $Revision: 1.75 $
 */
public interface SchoolChoiceBusiness extends IBOService, CaseBusiness {
    /**
     * @see se.idega.idegaweb.commune.school.business.SchoolChoiceBusinessBean#getBundleIdentifier
     */
    public String getBundleIdentifier() throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.school.business.SchoolChoiceBusinessBean#getSchoolBusiness
     */
    public SchoolBusiness getSchoolBusiness() throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.school.business.SchoolChoiceBusinessBean#getMessageBusiness
     */
    public MessageBusiness getMessageBusiness() throws RemoteException;

    /**
     * @see se.idega.idegaweb.commune.school.business.SchoolChoiceBusinessBean#getMemberFamilyLogic
     */
    public FamilyLogic getMemberFamilyLogic() throws RemoteException;

    /**
     * @see se.idega.idegaweb.commune.school.business.SchoolChoiceBusinessBean#getUserBusiness
     */
    public CommuneUserBusiness getUserBusiness()
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.school.business.SchoolChoiceBusinessBean#getSchoolChoiceHome
     */
    public SchoolChoiceHome getSchoolChoiceHome()
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.school.business.SchoolChoiceBusinessBean#getSchoolClassMemberHome
     */
    public SchoolClassMemberHome getSchoolClassMemberHome()
            throws RemoteException;

    /**
     * @see se.idega.idegaweb.commune.school.business.SchoolChoiceBusinessBean#getSchoolYearHome
     */
    public SchoolYearHome getSchoolYearHome() throws RemoteException;

    /**
     * @see se.idega.idegaweb.commune.school.business.SchoolChoiceBusinessBean#getPreviousSeasonId
     */
    public int getPreviousSeasonId() throws RemoteException;

    /**
     * @see se.idega.idegaweb.commune.school.business.SchoolChoiceBusinessBean#getSchoolChoice
     */
    public SchoolChoice getSchoolChoice(int schoolChoiceId)
            throws FinderException, RemoteException;

    /**
     * @see se.idega.idegaweb.commune.school.business.SchoolChoiceBusinessBean#getSchool
     */
    public School getSchool(int school) throws RemoteException;

    /**
     * @see se.idega.idegaweb.commune.school.business.SchoolChoiceBusinessBean#createSchoolChangeChoice
     */
    public SchoolChoice createSchoolChangeChoice(int userId, int childId,
            int school_type_id, int current_school, int chosen_school,
            int schoolYearID, int currentYearID, int method,
            int workSituation1, int workSituation2, String language,
            String message, boolean keepChildrenCare, boolean autoAssign,
            boolean custodiansAgree, boolean schoolCatalogue,
            Date placementDate, SchoolSeason season, String extraMessage)
            throws IDOCreateException, java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.school.business.SchoolChoiceBusinessBean#createSchoolChoices
     */
    public List createSchoolChoices(int userId, int childId,
            int school_type_id, int current_school, int chosen_school_1,
            int chosen_school_2, int chosen_school_3, int schoolYearID,
            int currentYearID, int method, int workSituation1,
            int workSituation2, String language, String message,
            boolean changeOfSchool, boolean keepChildrenCare,
            boolean autoAssign, boolean custodiansAgree,
            boolean schoolCatalogue, Date placementDate, SchoolSeason season,
            String[] extraMessages) throws IDOCreateException,
            java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.school.business.SchoolChoiceBusinessBean#createSchoolChoices
     */
    public List createSchoolChoices(int userId, int childId,
            int school_type_id, int current_school, int chosen_school_1,
            int chosen_school_2, int chosen_school_3, int schoolYearID,
            int currentYearID, int method, int workSituation1,
            int workSituation2, String language, String message,
            boolean changeOfSchool, boolean keepChildrenCare,
            boolean autoAssign, boolean custodiansAgree,
            boolean schoolCatalogue, Date placementDate, SchoolSeason season,
            boolean nativeLangIsChecked, int nativeLang,
            String[] extraMessages, boolean useAsAdmin)
            throws IDOCreateException, java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.school.business.SchoolChoiceBusinessBean#createCurrentSchoolSeason
     */
    public void createCurrentSchoolSeason(Integer newKey, Integer oldKey)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.school.business.SchoolChoiceBusinessBean#createTestFamily
     */
    public void createTestFamily() throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.school.business.SchoolChoiceBusinessBean#noRoomAction
     */
    public boolean noRoomAction(Integer pk, User performer)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.school.business.SchoolChoiceBusinessBean#sendMessageToParents
     */
    public void sendMessageToParents(SchoolChoice application,
            String nonApplyingSubject, String nonApplyingBody,
            String nonApplyingCode, String applyingSubject,
            String applyingBody, String applyingCode,
            boolean isChangeApplication) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.school.business.SchoolChoiceBusinessBean#reactivateApplication
     */
    public void reactivateApplication(int applicationID, int removedSchoolID,
            boolean hasReceivedPlacementMessage) throws RemoteException;

    /**
     * @see se.idega.idegaweb.commune.school.business.SchoolChoiceBusinessBean#reactivateApplication
     */
    public void reactivateApplication(int applicationID, int removedSchoolID,
            boolean hasReceivedPlacementMessage, String code)
            throws RemoteException;

    /**
     * @see se.idega.idegaweb.commune.school.business.SchoolChoiceBusinessBean#preliminaryAction
     */
    public boolean preliminaryAction(Integer pk, User performer)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.school.business.SchoolChoiceBusinessBean#setAsPreliminary
     */
    public void setAsPreliminary(SchoolChoice choice, User performer)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.school.business.SchoolChoiceBusinessBean#setAsInactive
     */
    public void setAsInactive(SchoolChoice choice, User performer)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.school.business.SchoolChoiceBusinessBean#setChildcarePreferences
     */
    public void setChildcarePreferences(User performer, int childID,
            boolean freetimeInThisSchool, String otherMessage,
            String messageSubject, String messageBody) throws RemoteException;

    /**
     * @see se.idega.idegaweb.commune.school.business.SchoolChoiceBusinessBean#changeSchoolYearForChoice
     */
    public void changeSchoolYearForChoice(int userID, int schoolSeasonID,
            int schoolYearID) throws RemoteException;

    /**
     * @see se.idega.idegaweb.commune.school.business.SchoolChoiceBusinessBean#groupPlaceAction
     */
    public SchoolChoice groupPlaceAction(Integer pk, User performer)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.school.business.SchoolChoiceBusinessBean#getPreliminaryMessageBody
     */
    public String getPreliminaryMessageBody(SchoolChoice theCase)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.school.business.SchoolChoiceBusinessBean#getReactivatedMessageSubject
     */
    public String getReactivatedMessageSubject()
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.school.business.SchoolChoiceBusinessBean#getPreliminaryMessageSubject
     */
    public String getPreliminaryMessageSubject()
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.school.business.SchoolChoiceBusinessBean#getPreliminaryMessageSubjectNew
     */
    public String getPreliminaryMessageSubjectNew()
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.school.business.SchoolChoiceBusinessBean#getGroupedMessageSubject
     */
    public String getGroupedMessageSubject() throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.school.business.SchoolChoiceBusinessBean#getApplyingSeparateParentSubjectAppl
     */
    public String getApplyingSeparateParentSubjectAppl()
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.school.business.SchoolChoiceBusinessBean#getNonApplyingSeparateParentSubjectAppl
     */
    public String getNonApplyingSeparateParentSubjectAppl()
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.school.business.SchoolChoiceBusinessBean#getApplyingSeparateParentSubjectChange
     */
    public String getApplyingSeparateParentSubjectChange()
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.school.business.SchoolChoiceBusinessBean#getNonApplyingSeparateParentSubjectChange
     */
    public String getNonApplyingSeparateParentSubjectChange()
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.school.business.SchoolChoiceBusinessBean#getApplyingSeparateParentSubjectApplNew
     */
    public String getApplyingSeparateParentSubjectApplNew()
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.school.business.SchoolChoiceBusinessBean#getNonApplyingSeparateParentSubjectApplNew
     */
    public String getNonApplyingSeparateParentSubjectApplNew()
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.school.business.SchoolChoiceBusinessBean#getOldHeadmasterSubject
     */
    public String getOldHeadmasterSubject() throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.school.business.SchoolChoiceBusinessBean#getNewHeadMasterSubject
     */
    public String getNewHeadMasterSubject() throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.school.business.SchoolChoiceBusinessBean#getSchoolChoiceCaseCode
     */
    public String getSchoolChoiceCaseCode() throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.school.business.SchoolChoiceBusinessBean#getLocalizedCaseDescription
     */
    public String getLocalizedCaseDescription(Case theCase, Locale locale)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.school.business.SchoolChoiceBusinessBean#findByStudentAndSchoolAndSeason
     */
    public SchoolChoice findByStudentAndSchoolAndSeason(int studentID,
            int schoolID, int seasonID) throws RemoteException;

    /**
     * @see se.idega.idegaweb.commune.school.business.SchoolChoiceBusinessBean#findBySchoolAndFreeTime
     */
    public Collection findBySchoolAndFreeTime(int schoolId, int schoolSeasonID,
            boolean freeTimeInSchool) throws RemoteException;

    /**
     * @see se.idega.idegaweb.commune.school.business.SchoolChoiceBusinessBean#findByStudentAndSeason
     */
    public Collection findByStudentAndSeason(int studentID, int seasonID)
            throws RemoteException;

    /**
     * @see se.idega.idegaweb.commune.school.business.SchoolChoiceBusinessBean#getNumberOfApplicationsByUserAndSchool
     */
    public int getNumberOfApplicationsByUserAndSchool(int userID, int schoolID)
            throws RemoteException;

    /**
     * @see se.idega.idegaweb.commune.school.business.SchoolChoiceBusinessBean#findByStudentAndSchool
     */
    public Collection findByStudentAndSchool(int studentID, int schoolID)
            throws RemoteException;

    /**
     * @see se.idega.idegaweb.commune.school.business.SchoolChoiceBusinessBean#getNumberOfApplications
     */
    public int getNumberOfApplications(int schoolID, int schoolSeasonID)
            throws RemoteException;

    /**
     * @see se.idega.idegaweb.commune.school.business.SchoolChoiceBusinessBean#getNumberOfApplicationsForStudents
     */
    public int getNumberOfApplicationsForStudents(int userID, int schoolSeasonID)
            throws RemoteException;

    /**
     * @see se.idega.idegaweb.commune.school.business.SchoolChoiceBusinessBean#getNumberOfApplications
     */
    public int getNumberOfApplications(int schoolID, int schoolSeasonID,
            int schoolYearID) throws RemoteException;

    /**
     * @see se.idega.idegaweb.commune.school.business.SchoolChoiceBusinessBean#getApplicantsForSchool
     */
    public Collection getApplicantsForSchool(int schoolID, int seasonID,
            int schoolYearID, String[] validStatuses, String searchString,
            int orderBy, int numberOfEntries, int startingEntry)
            throws RemoteException;

    /**
     * @see se.idega.idegaweb.commune.school.business.SchoolChoiceBusinessBean#getApplicantsForSchool
     */
    public Collection getApplicantsForSchool(int schoolID, int seasonID,
            int schoolYearID, int[] choiceOrder, String[] validStatuses,
            String searchString, int orderBy, int numberOfEntries,
            int startingEntry) throws RemoteException;

    /**
     * @see se.idega.idegaweb.commune.school.business.SchoolChoiceBusinessBean#getApplicantsForSchool
     */
    public Collection getApplicantsForSchool(int schoolID, int seasonID,
            int schoolYearID, String[] validStatuses, String searchString,
            int orderBy, int numberOfEntries, int startingEntry,
            int placementType) throws RemoteException;

    /**
     * @see se.idega.idegaweb.commune.school.business.SchoolChoiceBusinessBean#getNumberOfApplicantsForSchool
     */
    public int getNumberOfApplicantsForSchool(int schoolID, int seasonID,
            int schoolYearID, int[] choiceOrder, String[] validStatuses,
            String searchString) throws RemoteException;

    /**
     * @see se.idega.idegaweb.commune.school.business.SchoolChoiceBusinessBean#getNumberOfApplicantsForSchool
     */
    public int getNumberOfApplicantsForSchool(int schoolID, int seasonID,
            int schoolYearID, int[] choiceOrder, String[] validStatuses,
            String searchString, int placementType) throws RemoteException;

    /**
     * @see se.idega.idegaweb.commune.school.business.SchoolChoiceBusinessBean#getNumberOfApplicants
     */
    public int getNumberOfApplicants(String[] validStatuses)
            throws RemoteException;

    /**
     * @see se.idega.idegaweb.commune.school.business.SchoolChoiceBusinessBean#getNumberOfApplicants
     */
    public int getNumberOfApplicants() throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.school.business.SchoolChoiceBusinessBean#getSchoolChoiceStartDate
     */
    public IWTimestamp getSchoolChoiceStartDate() throws RemoteException,
            FinderException;

    /**
     * @see se.idega.idegaweb.commune.school.business.SchoolChoiceBusinessBean#getSchoolChoiceEndDate
     */
    public IWTimestamp getSchoolChoiceEndDate() throws RemoteException,
            FinderException;

    /**
     * @see se.idega.idegaweb.commune.school.business.SchoolChoiceBusinessBean#getSchoolChoiceCriticalDate
     */
    public IWTimestamp getSchoolChoiceCriticalDate() throws RemoteException,
            FinderException;

    /**
     * @see se.idega.idegaweb.commune.school.business.SchoolChoiceBusinessBean#getApplicantsForSchoolAndSeasonAndGrade
     */
    public Collection getApplicantsForSchoolAndSeasonAndGrade(int schoolID,
            int seasonID, int schoolYearID) throws RemoteException;

    /**
     * @see se.idega.idegaweb.commune.school.business.SchoolChoiceBusinessBean#getApplicationsInClass
     */
    public Collection getApplicationsInClass(SchoolClass schoolClass,
            boolean confirmation) throws RemoteException;

    /**
     * @see se.idega.idegaweb.commune.school.business.SchoolChoiceBusinessBean#createSchoolChoiceReminder
     */
    public void createSchoolChoiceReminder(String text,
            java.util.Date eventDate, java.util.Date reminderDate, User user)
            throws CreateException, RemoteException;

    /**
     * @see se.idega.idegaweb.commune.school.business.SchoolChoiceBusinessBean#findAllSchoolChoiceReminders
     */
    public SchoolChoiceReminder[] findAllSchoolChoiceReminders()
            throws RemoteException, FinderException;

    /**
     * @see se.idega.idegaweb.commune.school.business.SchoolChoiceBusinessBean#findUnhandledSchoolChoiceReminders
     */
    public SchoolChoiceReminder[] findUnhandledSchoolChoiceReminders(
            Group[] groups) throws RemoteException, FinderException;

    /**
     * @see se.idega.idegaweb.commune.school.business.SchoolChoiceBusinessBean#findSchoolChoiceReminder
     */
    public SchoolChoiceReminder findSchoolChoiceReminder(int id)
            throws RemoteException, FinderException;

    /**
     * @see se.idega.idegaweb.commune.school.business.SchoolChoiceBusinessBean#generateReminderLetter
     */
    public int generateReminderLetter(int reminderId, MailReceiver[] receivers)
            throws RemoteException;

    /**
     * @see se.idega.idegaweb.commune.school.business.SchoolChoiceBusinessBean#findAllStudentsThatMustDoSchoolChoiceButHaveNot
     */
    public MailReceiver[] findAllStudentsThatMustDoSchoolChoiceButHaveNot(
            SchoolSeason season, SchoolYear[] years, boolean isOnlyInCommune,
            boolean onlyLastGrade) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.school.business.SchoolChoiceBusinessBean#getNumberOfStudentsThatMustDoSchoolChoiceButHaveNot
     */
    public int getNumberOfStudentsThatMustDoSchoolChoiceButHaveNot(
            SchoolSeason season, SchoolYear year, boolean isOnlyInCommune,
            boolean onlyLastGrade) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.school.business.SchoolChoiceBusinessBean#countStudentsWithoutSchoolChoice
     */
    public int countStudentsWithoutSchoolChoice(SchoolSeason season,
            SchoolYear year, boolean onlyInCommune, boolean onlyLastGrade)
            throws RemoteException, SQLException;

    /**
     * @see se.idega.idegaweb.commune.school.business.SchoolChoiceBusinessBean#getStudentsWithoutSchoolChoice
     */
    public MailReceiver[] getStudentsWithoutSchoolChoice(SchoolSeason season,
            SchoolYear year, boolean onlyInCommune, boolean onlyLastGrade)
            throws RemoteException, FinderException;

    /**
     * @see se.idega.idegaweb.commune.school.business.SchoolChoiceBusinessBean#findAllStudentsThatMustDoSchoolChoiceButHaveNot
     */
    public MailReceiver[] findAllStudentsThatMustDoSchoolChoiceButHaveNot(
            SchoolSeason season, SchoolYear year, boolean isOnlyInCommune,
            boolean isOnlyInSchoolsLastGrade) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.school.business.SchoolChoiceBusinessBean#findAllStudentsThatMustDoSchoolChoice
     */
    public Collection findAllStudentsThatMustDoSchoolChoice(
            SchoolSeason season, SchoolYear year,
            boolean isOnlyInSchoolsLastGrade) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.school.business.SchoolChoiceBusinessBean#sendMessageToParentOrChild
     */
    public void sendMessageToParentOrChild(SchoolChoice choice, User parent,
            User child, String subject, String body, String contentCode)
            throws RemoteException;

    /**
     * @see se.idega.idegaweb.commune.school.business.SchoolChoiceBusinessBean#getReceiver
     */
    public User getReceiver(User parent, User child)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.school.business.SchoolChoiceBusinessBean#isInSchoolChoicePeriod
     */
    public boolean isInSchoolChoicePeriod() throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.school.business.SchoolChoiceBusinessBean#getMandatorySchoolChoiceYears
     */
    public Collection getMandatorySchoolChoiceYears()
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.school.business.SchoolChoiceBusinessBean#getConnectedSchoolchoices
     */
    public List getConnectedSchoolchoices(SchoolChoice selectedChoice)
            throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.school.business.SchoolChoiceBusinessBean#importLanguageToPlacement
     */
    public void importLanguageToPlacement() throws java.rmi.RemoteException;

}
