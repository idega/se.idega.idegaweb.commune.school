/*
 * $Id: CommuneSchoolBusiness.java,v 1.6 2005/10/07 13:17:28 laddi Exp $
 * Created on Oct 7, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.school.business;

import java.util.Collection;
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
 * Last modified: $Date: 2005/10/07 13:17:28 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.6 $
 */
public interface CommuneSchoolBusiness extends IBOService, CaseBusiness {

	/**
	 * @see se.idega.idegaweb.commune.school.business.CommuneSchoolBusinessBean#canDisplaySchoolImages
	 */
	public boolean canDisplaySchoolImages(User child) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.CommuneSchoolBusinessBean#canDisplayAfterSchoolCareImages
	 */
	public boolean canDisplayAfterSchoolCareImages(User child) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.CommuneSchoolBusinessBean#getAfterSchoolCareOtherInformation
	 */
	public String getAfterSchoolCareOtherInformation(User child) throws java.rmi.RemoteException;

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
	public SchoolClass getDefaultGroup(School school, SchoolSeason season, SchoolYear year)
			throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.CommuneSchoolBusinessBean#getSchoolYears
	 */
	public Collection getSchoolYears() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.CommuneSchoolBusinessBean#saveHomeSchoolChoice
	 */
	public boolean saveHomeSchoolChoice(User user, User child, Object schoolPK, Object seasonPK, Object yearPK,
			String language, String message) throws IDOCreateException, java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.CommuneSchoolBusinessBean#getChoices
	 */
	public Collection getChoices(User child, SchoolSeason season) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.CommuneSchoolBusinessBean#getChoice
	 */
	public SchoolChoice getChoice(User child, SchoolSeason season, int choiceNumber) throws FinderException,
			java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.CommuneSchoolBusinessBean#saveChoices
	 */
	public boolean saveChoices(User user, User child, Collection schools, Object seasonPK, Object yearPK,
			String language, String message) throws IDOCreateException, java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.CommuneSchoolBusinessBean#storeChildSchoolInformation
	 */
	public void storeChildSchoolInformation(User child, boolean canDisplayImage) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.CommuneSchoolBusinessBean#storeChildAfterSchoolCareInformation
	 */
	public void storeChildAfterSchoolCareInformation(User child, boolean canDisplayImage,
			String otherAfterSchoolCareInformation) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.CommuneSchoolBusinessBean#getSchoolYearForUser
	 */
	public SchoolYear getSchoolYearForUser(User user) throws FinderException, java.rmi.RemoteException;
}
