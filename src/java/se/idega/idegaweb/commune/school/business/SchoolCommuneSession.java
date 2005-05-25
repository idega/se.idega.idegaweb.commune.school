/*
 * Created on 2005-maj-18
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package se.idega.idegaweb.commune.school.business;

import java.rmi.RemoteException;


import se.idega.idegaweb.commune.business.CommuneUserBusiness;

import com.idega.block.school.data.School;
import com.idega.business.IBOSession;

/**
 * @author Malin
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public interface SchoolCommuneSession extends IBOSession {
	/**
	 * @see se.idega.idegaweb.commune.school.business.SchoolCommuneSessionBean#getCommuneUserBusiness
	 */
	public CommuneUserBusiness getCommuneUserBusiness() throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.SchoolCommuneSessionBean#getSchoolCommuneBusiness
	 */
	public SchoolCommuneBusiness getSchoolCommuneBusiness()
			throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.SchoolCommuneSessionBean#getSchoolID
	 */
	public int getSchoolID() throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.SchoolCommuneSessionBean#getSchoolClassID
	 */
	public int getSchoolClassID() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.SchoolCommuneSessionBean#getSchoolSeasonID
	 */
	public int getSchoolSeasonID() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.SchoolCommuneSessionBean#getSchoolYearID
	 */
	public int getSchoolYearID() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.SchoolCommuneSessionBean#setSchoolClassID
	 */
	public void setSchoolClassID(int schoolClassID)
			throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.SchoolCommuneSessionBean#setSchoolID
	 */
	public void setSchoolID(int schoolID) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.SchoolCommuneSessionBean#setSchoolSeasonID
	 */
	public void setSchoolSeasonID(int schoolSeasonID)
			throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.SchoolCommuneSessionBean#setSchoolYearID
	 */
	public void setSchoolYearID(int schoolYearID)
			throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.SchoolCommuneSessionBean#getParameterSchoolID
	 */
	public String getParameterSchoolID() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.SchoolCommuneSessionBean#getParameterSchoolClassID
	 */
	public String getParameterSchoolClassID() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.SchoolCommuneSessionBean#getParameterSchoolSeasonID
	 */
	public String getParameterSchoolSeasonID() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.SchoolCommuneSessionBean#getParameterSchoolYearID
	 */
	public String getParameterSchoolYearID() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.SchoolCommuneSessionBean#getParameterStudentID
	 */
	public String getParameterStudentID() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.SchoolCommuneSessionBean#getParameterStudentUniqueID
	 */
	public String getParameterStudentUniqueID() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.SchoolCommuneSessionBean#getParameterSchoolGroupIDs
	 */
	public String getParameterSchoolGroupIDs() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.SchoolCommuneSessionBean#getStudentID
	 */
	public int getStudentID() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.SchoolCommuneSessionBean#getStudentUniqueID
	 */
	public String getStudentUniqueID() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.SchoolCommuneSessionBean#setStudentID
	 */
	public void setStudentID(int studentID) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.SchoolCommuneSessionBean#setStudentUniqueID
	 */
	public void setStudentUniqueID(String studentUniqueID)
			throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.SchoolCommuneSessionBean#getSchoolGroupIDs
	 */
	public String[] getSchoolGroupIDs() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.SchoolCommuneSessionBean#setSchoolGroupIDs
	 */
	public void setSchoolGroupIDs(String[] schoolGroupIDs)
			throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.business.SchoolCommuneSessionBean#getSchool
	 */
	public School getSchool() throws java.rmi.RemoteException;

}
