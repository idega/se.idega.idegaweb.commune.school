package se.idega.idegaweb.commune.school.business;

import javax.ejb.*;

public interface SchoolCommuneBusiness extends com.idega.business.IBOService,com.idega.block.process.business.CaseBusiness
{
 public void addSchoolAdministrator(com.idega.user.data.User p0)throws java.rmi.RemoteException,javax.ejb.FinderException,javax.ejb.CreateException, java.rmi.RemoteException;
 public boolean canMarkSchoolClass(com.idega.block.school.data.SchoolClass p0,java.lang.String p1)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void finalizeGroup(com.idega.block.school.data.SchoolClass p0,java.lang.String p1,java.lang.String p2, boolean confirmation)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public int getChosenSchoolID(java.util.Collection p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public int getCurrentSchoolSeasonID()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public int getGradeForYear(int p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.Collection getPreviousSchoolClasses(com.idega.block.school.data.School p0,com.idega.block.school.data.SchoolSeason p1,com.idega.block.school.data.SchoolYear p2)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.school.data.SchoolSeason getPreviousSchoolSeason(com.idega.block.school.data.SchoolSeason p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.school.data.SchoolSeason getPreviousSchoolSeason(int p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public int getPreviousSchoolSeasonID(int p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public int getPreviousSchoolYear(int p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.school.business.SchoolBusiness getSchoolBusiness()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public se.idega.idegaweb.commune.school.business.SchoolChoiceBusiness getSchoolChoiceBusiness()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.school.data.SchoolYear getSchoolYear(int p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.Map getStudentChoices(java.util.Collection p0,int p1)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.Map getStudentList(java.util.Collection p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.Map getUserAddressesMapFromChoices(java.util.Collection p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.Map getUserMapFromChoices(java.util.Collection p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public boolean hasChosenOtherSchool(java.util.Collection p0,int p1)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void importStudentInformationToNewClass(com.idega.block.school.data.SchoolClassMember p0,com.idega.block.school.data.SchoolSeason p1)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void markSchoolClassLocked(com.idega.block.school.data.SchoolClass p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void markSchoolClassReady(com.idega.block.school.data.SchoolClass p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void moveToGroup(int p0,int p1,int p2)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void setNeedsSpecialAttention(int p0,int p1,boolean p2)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void setNeedsSpecialAttention(com.idega.block.school.data.SchoolClassMember p0,boolean p2) throws java.rmi.RemoteException;
 public void setNeedsSpecialAttention(com.idega.block.school.data.SchoolClassMember p0,com.idega.block.school.data.SchoolSeason p1,boolean p2)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void setStudentAsSpeciallyPlaced(com.idega.block.school.data.SchoolClassMember p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public boolean[] hasSchoolChoices(int userID, int seasonID) throws java.rmi.RemoteException;
 public String getLocalizedSchoolTypeKey(com.idega.block.school.data.SchoolType type);
 public void resetSchoolClassStatus(int schoolClassID) throws java.rmi.RemoteException;
}
