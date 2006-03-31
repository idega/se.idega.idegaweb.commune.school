/*
 * $Id: CommuneSchoolBusiness.java,v 1.16 2006/03/31 12:28:04 laddi Exp $
 * Created on Mar 30, 2006
 *
 * Copyright (C) 2006 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.school.business;


import java.sql.Date;
import java.util.Collection;
import java.util.Locale;

import javax.ejb.FinderException;

import se.idega.idegaweb.commune.school.data.SchoolChoice;

import com.idega.block.process.business.CaseBusiness;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolClass;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.block.school.data.SchoolSeason;
import com.idega.block.school.data.SchoolYear;
import com.idega.business.IBOService;
import com.idega.core.location.data.Address;
import com.idega.data.IDOCreateException;
import com.idega.user.data.User;


/**
 * <p>
 * TODO laddi Describe Type CommuneSchoolBusiness
 * </p>
 *  Last modified: $Date: 2006/03/31 12:28:04 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.16 $
 */
public interface CommuneSchoolBusiness extends IBOService, CaseBusiness {

	/**
	 * @see se.idega.idegaweb.commune.school.business.CommuneSchoolBusinessBean#getNumberOfApplications
	 */
	public int getNumberOfApplications(SchoolSeason season, int choiceNumber) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.CommuneSchoolBusinessBean#getSchoolYearsInSchool
	 */
	public Collection getSchoolYearsInSchool(School school) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.CommuneSchoolBusinessBean#getHomeSchoolForUser
	 */
	public School getHomeSchoolForUser(User user) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.CommuneSchoolBusinessBean#hasSchoolPlacing
	 */
	public boolean hasSchoolPlacing(User user, SchoolSeason season) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.CommuneSchoolBusinessBean#hasSchoolChoice
	 */
	public boolean hasSchoolChoice(User user, SchoolSeason season) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.CommuneSchoolBusinessBean#hasAfterSchoolCarePlacing
	 */
	public boolean hasAfterSchoolCarePlacing(User user, SchoolSeason season) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.CommuneSchoolBusinessBean#getSchoolPlacing
	 */
	public SchoolClassMember getSchoolPlacing(User user, SchoolSeason season) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.CommuneSchoolBusinessBean#getAfterSchoolProviderForPlacement
	 */
	public School getAfterSchoolProviderForPlacement(SchoolClassMember placement) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.CommuneSchoolBusinessBean#hasHomeSchool
	 */
	public boolean hasHomeSchool(User user) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.CommuneSchoolBusinessBean#getHomeSchoolForAddress
	 */
	public School getHomeSchoolForAddress(User user, Address address) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.CommuneSchoolBusinessBean#getDefaultGroup
	 */
	public SchoolClass getDefaultGroup(Object schoolPK, Object seasonPK, Object yearPK) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.CommuneSchoolBusinessBean#getDefaultGroup
	 */
	public SchoolClass getDefaultGroup(School school, SchoolSeason season, SchoolYear year) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.CommuneSchoolBusinessBean#getSchoolYears
	 */
	public Collection getSchoolYears() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.CommuneSchoolBusinessBean#approveApplications
	 */
	public boolean approveApplications(Object[] pks, School school, SchoolSeason season, String subject, String body, User performer) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.CommuneSchoolBusinessBean#rejectApplications
	 */
	public boolean rejectApplications(Object[] pks, String subject, String body, User performer) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.CommuneSchoolBusinessBean#saveHomeSchoolChoice
	 */
	public boolean saveHomeSchoolChoice(User user, User child, Object schoolPK, Object seasonPK, Object yearPK, String language, String message) throws IDOCreateException, java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.CommuneSchoolBusinessBean#getChoices
	 */
	public Collection getChoices(User child, SchoolSeason season) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.CommuneSchoolBusinessBean#getAllChoices
	 */
	public Collection getAllChoices(User child, SchoolSeason season) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.CommuneSchoolBusinessBean#getChoice
	 */
	public SchoolChoice getChoice(User child, SchoolSeason season, int choiceNumber) throws FinderException, java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.CommuneSchoolBusinessBean#getSchoolChoice
	 */
	public SchoolChoice getSchoolChoice(Object primaryKey) throws FinderException, java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.CommuneSchoolBusinessBean#saveChoices
	 */
	public SchoolChoice saveChoices(User user, User child, Collection schools, Object seasonPK, Object yearPK, String language, String message, Date placementDate) throws IDOCreateException, java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.CommuneSchoolBusinessBean#sendMessageToProvider
	 */
	public void sendMessageToProvider(SchoolChoice application, String subject, String message, Locale locale, User sender) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.CommuneSchoolBusinessBean#sendMessageToProvider
	 */
	public void sendMessageToProvider(SchoolChoice application, String subject, String message, Locale locale) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.CommuneSchoolBusinessBean#getSchoolYearForUser
	 */
	public SchoolYear getSchoolYearForUser(User user) throws FinderException, java.rmi.RemoteException;

}
