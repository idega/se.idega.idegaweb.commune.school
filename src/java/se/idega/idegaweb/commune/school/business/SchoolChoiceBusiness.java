package se.idega.idegaweb.commune.school.business;

import java.rmi.RemoteException;
import javax.ejb.*;

import com.idega.block.school.data.School;
//import com.idega.core.user.data.User;

public interface SchoolChoiceBusiness extends com.idega.business.IBOService,com.idega.block.process.business.CaseBusiness
{
 public com.idega.block.school.data.SchoolSeason getCurrentSeason()throws javax.ejb.FinderException,java.rmi.RemoteException, java.rmi.RemoteException;
 public se.idega.idegaweb.commune.school.data.CurrentSchoolSeasonHome getCurrentSchoolSeasonHome()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public se.idega.idegaweb.commune.school.data.SchoolChoiceHome getSchoolChoiceHome()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.List createSchoolChoices(int p0,int p1,int p2,int p3,int p4,int p5,int p6,int p7,int p8,int p9,java.lang.String p10,java.lang.String p11,boolean p12,boolean p13,boolean p14,boolean p15,boolean p16)throws com.idega.data.IDOCreateException, java.rmi.RemoteException;
 public com.idega.block.school.data.SchoolSeasonHome getSchoolSeasonHome()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void createCurrentSchoolSeason(java.lang.Integer NewId,java.lang.Integer OldId)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public se.idega.idegaweb.commune.school.data.SchoolChoice getSchoolChoice(int p0)throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 public boolean preliminaryAction(Integer PK)throws java.rmi.RemoteException;
 public boolean noRoomAction(Integer PK)  throws java.rmi.RemoteException;
 public boolean groupPlaceAction(Integer pk , String group) throws java.rmi.RemoteException;
 
	/**
	 * Method getFirstManagingSchoolForUser.
	 * If there is no school that the user manages then the method throws a FinderException.
	 * @param user a user
	 * @return School that is the first school that the user is a manager for.
	 * @throws javax.ejb.FinderException if ther is no school that the user manages.
	 */
	public School getFirstManagingSchoolForUser(com.idega.user.data.User user)throws FinderException,RemoteException;

}
