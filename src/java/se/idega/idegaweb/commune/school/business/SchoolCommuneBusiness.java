/*
 * $Id: SchoolCommuneBusiness.java 1.1 10.1.2005 laddi Exp $
 * Created on 10.1.2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.school.business;


import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import se.idega.idegaweb.commune.business.CommuneUserBusiness;
import se.idega.idegaweb.commune.school.data.SchoolChoice;

import com.idega.block.datareport.util.ReportableCollection;
import com.idega.block.process.business.CaseBusiness;
import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolClass;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.block.school.data.SchoolSeason;
import com.idega.block.school.data.SchoolStudyPath;
import com.idega.block.school.data.SchoolType;
import com.idega.block.school.data.SchoolYear;
import com.idega.business.IBOService;
import com.idega.data.IDOException;
import com.idega.data.IDOQuery;
import com.idega.user.data.User;


/**
 * Last modified: $Date: 2004/06/28 09:09:50 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.1 $
 */
public interface SchoolCommuneBusiness extends IBOService, CaseBusiness {

	/**
	 * @see se.idega.idegaweb.commune.school.business.SchoolCommuneBusinessBean#getYearClassMap
	 */
	public Map getYearClassMap(Collection schoolYears, int schoolID, int seasonID, String emptyString, boolean showSubGroups) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.SchoolCommuneBusinessBean#getSchoolBusiness
	 */
	public SchoolBusiness getSchoolBusiness() throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.SchoolCommuneBusinessBean#getSchoolChoiceBusiness
	 */
	public SchoolChoiceBusiness getSchoolChoiceBusiness() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.SchoolCommuneBusinessBean#isOngoingSeason
	 */
	public boolean isOngoingSeason(int seasonID) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.SchoolCommuneBusinessBean#isAlreadyInSchool
	 */
	public boolean isAlreadyInSchool(int userID, int schoolID, int seasonID) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.SchoolCommuneBusinessBean#isPlacedAtSchool
	 */
	public boolean isPlacedAtSchool(int userID, int schoolID) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.SchoolCommuneBusinessBean#getUserMapFromChoices
	 */
	public Map getUserMapFromChoices(IDOQuery query) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.SchoolCommuneBusinessBean#getUserMapFromChoices
	 */
	public Map getUserMapFromChoices(Collection choices) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.SchoolCommuneBusinessBean#getUserAddressesMapFromChoices
	 */
	public Map getUserAddressesMapFromChoices(Collection choices) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.SchoolCommuneBusinessBean#getUserAddressesMapFromChoices
	 */
	public Map getUserAddressesMapFromChoices(IDOQuery query) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.SchoolCommuneBusinessBean#getUserPhoneMapFromChoicesUserIdPK
	 */
	public Map getUserPhoneMapFromChoicesUserIdPK(Collection choices) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.SchoolCommuneBusinessBean#getUserAddressMapFromChoicesUserIdPK
	 */
	public Map getUserAddressMapFromChoicesUserIdPK(Collection choices) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.SchoolCommuneBusinessBean#getStudentChoices
	 */
	public Map getStudentChoices(Collection students, int seasonID) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.SchoolCommuneBusinessBean#hasChosenOtherSchool
	 */
	public boolean hasChosenOtherSchool(Collection choices, int schoolID) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.SchoolCommuneBusinessBean#hasSchoolChoices
	 */
	public boolean[] hasSchoolChoices(int userID, int seasonID) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.SchoolCommuneBusinessBean#hasMoveChoiceToOtherSchool
	 */
	public boolean hasMoveChoiceToOtherSchool(int userID, int schoolID, int seasonID) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.SchoolCommuneBusinessBean#hasChoicesForSeason
	 */
	public boolean hasChoicesForSeason(int userID, int seasonID) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.SchoolCommuneBusinessBean#hasChoiceToThisSchool
	 */
	public boolean hasChoiceToThisSchool(int userID, int schoolID, int seasonID) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.SchoolCommuneBusinessBean#getChosenSchoolID
	 */
	public int getChosenSchoolID(Collection choices) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.SchoolCommuneBusinessBean#getPreviousSchoolSeason
	 */
	public SchoolSeason getPreviousSchoolSeason(SchoolSeason schoolSeason) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.SchoolCommuneBusinessBean#getPreviousSchoolSeason
	 */
	public SchoolSeason getPreviousSchoolSeason(int schoolSeasonID) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.SchoolCommuneBusinessBean#getPreviousSchoolSeasonID
	 */
	public int getPreviousSchoolSeasonID(int schoolSeasonID) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.SchoolCommuneBusinessBean#getCurrentSchoolSeasonID
	 */
	public int getCurrentSchoolSeasonID() throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.SchoolCommuneBusinessBean#getSchoolYear
	 */
	public SchoolYear getSchoolYear(int age) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.SchoolCommuneBusinessBean#getSchoolYear
	 */
	public SchoolYear getSchoolYear(int schoolYear, int test) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.SchoolCommuneBusinessBean#getPreviousSchoolYear
	 */
	public int getPreviousSchoolYear(int schoolYearID) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.SchoolCommuneBusinessBean#getNextSchoolYear
	 */
	public SchoolYear getNextSchoolYear(SchoolYear schoolYear) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.SchoolCommuneBusinessBean#getGradeForYear
	 */
	public int getGradeForYear(int schoolYearID) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.SchoolCommuneBusinessBean#getPreviousSchoolClasses
	 */
	public Collection getPreviousSchoolClasses(School school, SchoolSeason schoolSeason, SchoolYear schoolYear) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.SchoolCommuneBusinessBean#finalizeGroup
	 */
	public void finalizeGroup(SchoolClass schoolClass, String subject, String body, boolean confirmation) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.SchoolCommuneBusinessBean#setStudentAsSpeciallyPlaced
	 */
	public void setStudentAsSpeciallyPlaced(SchoolClassMember schoolMember) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.SchoolCommuneBusinessBean#importStudentInformationToNewClass
	 */
	public void importStudentInformationToNewClass(SchoolClassMember schoolMember, SchoolSeason previousSeason) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.SchoolCommuneBusinessBean#setNeedsSpecialAttention
	 */
	public void setNeedsSpecialAttention(int studentID, int schoolSeasonID, boolean needsAttention) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.SchoolCommuneBusinessBean#setNeedsSpecialAttention
	 */
	public void setNeedsSpecialAttention(SchoolClassMember schoolMember, SchoolSeason schoolSeason, boolean needsAttention) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.SchoolCommuneBusinessBean#setNeedsSpecialAttention
	 */
	public void setNeedsSpecialAttention(SchoolClassMember schoolMember, boolean needsAttention) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.SchoolCommuneBusinessBean#addSchoolAdministrator
	 */
	public void addSchoolAdministrator(User user) throws RemoteException, FinderException, CreateException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.SchoolCommuneBusinessBean#markSchoolClassReady
	 */
	public void markSchoolClassReady(SchoolClass schoolClass) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.SchoolCommuneBusinessBean#markSchoolClassLocked
	 */
	public void markSchoolClassLocked(SchoolClass schoolClass) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.SchoolCommuneBusinessBean#canMarkSchoolClass
	 */
	public boolean canMarkSchoolClass(SchoolClass schoolClass, String propertyName) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.SchoolCommuneBusinessBean#moveToGroup
	 */
	public void moveToGroup(int studentID, int schoolClassID, int oldSchoolClassID, int schoolYearID, User performer) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.SchoolCommuneBusinessBean#getUserBusiness
	 */
	public CommuneUserBusiness getUserBusiness() throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.SchoolCommuneBusinessBean#getLocalizedSchoolTypeKey
	 */
	public String getLocalizedSchoolTypeKey(SchoolType type) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.SchoolCommuneBusinessBean#resetSchoolClassStatus
	 */
	public void resetSchoolClassStatus(int schoolClassID) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.SchoolCommuneBusinessBean#removeSubGroupPlacements
	 */
	public boolean removeSubGroupPlacements(int userID, int schoolID, int seasonID) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.SchoolCommuneBusinessBean#getCurrentMembersWithInvoiceInterval
	 */
	public SchoolClassMember[] getCurrentMembersWithInvoiceInterval(String operationalField) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.SchoolCommuneBusinessBean#getCurrentSchoolClassMembership
	 */
	public SchoolClassMember getCurrentSchoolClassMembership(User user) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.SchoolCommuneBusinessBean#getCurrentSchoolClassMembership
	 */
	public SchoolClassMember getCurrentSchoolClassMembership(User user, int schoolId) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.SchoolCommuneBusinessBean#getStudyPath
	 */
	public SchoolStudyPath getStudyPath(SchoolClassMember student) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.SchoolCommuneBusinessBean#getAllStudyPaths
	 */
	public SchoolStudyPath[] getAllStudyPaths() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.SchoolCommuneBusinessBean#getReportOfUsersNotRegisteredInAnyClass
	 */
	public ReportableCollection getReportOfUsersNotRegisteredInAnyClass(Locale currentLocale, Date selectedDate, SchoolSeason currentSeason) throws IDOException, RemoteException, CreateException, FinderException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.SchoolCommuneBusinessBean#sendMessageToParents
	 */
	public void sendMessageToParents(SchoolChoice choice, String subject, String body) throws java.rmi.RemoteException;

}
