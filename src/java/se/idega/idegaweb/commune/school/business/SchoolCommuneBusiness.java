package se.idega.idegaweb.commune.school.business;

import javax.ejb.*;

public interface SchoolCommuneBusiness extends com.idega.business.IBOService,com.idega.block.process.business.CaseBusiness
{
 public com.idega.block.school.data.SchoolSeason getPreviousSchoolSeason(com.idega.block.school.data.SchoolSeason p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.school.business.SchoolBusiness getSchoolBusiness()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public se.idega.idegaweb.commune.school.business.SchoolChoiceBusiness getSchoolChoiceBusiness()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.school.business.SchoolClassBusiness getSchoolClassBusiness()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.school.business.SchoolClassMemberBusiness getSchoolClassMemberBusiness()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.school.business.SchoolSeasonBusiness getSchoolSeasonBusiness()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.school.business.SchoolYearBusiness getSchoolYearBusiness()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.List getStudentList(java.util.Collection p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
}
