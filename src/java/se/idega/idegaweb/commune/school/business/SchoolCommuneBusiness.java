package se.idega.idegaweb.commune.school.business;

import javax.ejb.*;

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
 public com.idega.block.school.business.SchoolClassBusiness getSchoolClassBusiness()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.school.business.SchoolClassMemberBusiness getSchoolClassMemberBusiness()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.school.business.SchoolSeasonBusiness getSchoolSeasonBusiness()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.school.data.SchoolYear getSchoolYear(int p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.school.business.SchoolYearBusiness getSchoolYearBusiness()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.Map getStudentChoices(java.util.Collection p0,int p1)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.Map getStudentList(java.util.Collection p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public boolean hasChosenOtherSchool(java.util.Collection p0,int p1)throws java.rmi.RemoteException, java.rmi.RemoteException;
}
