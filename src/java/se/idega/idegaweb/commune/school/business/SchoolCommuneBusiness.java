package se.idega.idegaweb.commune.school.business;

import java.rmi.RemoteException;
import javax.ejb.*;

import com.idega.user.data.User;

public interface SchoolCommuneBusiness extends com.idega.business.IBOService,com.idega.block.process.business.CaseBusiness
{
 public void finalizeGroup(int p0,java.lang.String p1,java.lang.String p2)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public int getCurrentSchoolSeasonID()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public int getGradeForYear(int p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.Collection getPreviousSchoolClasses(com.idega.block.school.data.School p0,com.idega.block.school.data.SchoolSeason p1,com.idega.block.school.data.SchoolYear p2)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.school.data.SchoolSeason getPreviousSchoolSeason(int p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.school.data.SchoolSeason getPreviousSchoolSeason(com.idega.block.school.data.SchoolSeason p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public int getPreviousSchoolSeasonID(int p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public int getPreviousSchoolYear(int p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.school.business.SchoolBusiness getSchoolBusiness()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public se.idega.idegaweb.commune.school.business.SchoolChoiceBusiness getSchoolChoiceBusiness()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.school.data.SchoolYear getSchoolYear(int p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.Map getStudentChoices(java.util.Collection p0,int p1)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.Map getStudentList(java.util.Collection p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public boolean hasChosenOtherSchool(java.util.Collection p0,int p1)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void moveToGroup(int studentID, int newSchoolClassID, int oldSchoolClassID) throws java.rmi.RemoteException;
 public int getChosenSchoolID(java.util.Collection choices) throws java.rmi.RemoteException;
 public void addSchoolAdministrator(User user) throws RemoteException, FinderException, CreateException;
 public void markSchoolClassReady(com.idega.block.school.data.SchoolClass schoolClass) throws RemoteException;
 public void markSchoolClassLocked(com.idega.block.school.data.SchoolClass schoolClass) throws RemoteException;
 public boolean canMarkSchoolClass(com.idega.block.school.data.SchoolClass schoolClass, String propertyName) throws RemoteException;
}
