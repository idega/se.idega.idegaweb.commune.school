package se.idega.idegaweb.commune.school.business;


public interface CentralPlacementBusiness extends com.idega.business.IBOService
{
 public se.idega.idegaweb.commune.business.CommuneUserBusiness getCommuneUserBusiness()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.school.data.SchoolSeason getCurrentSeason() throws java.rmi.RemoteException;
 public java.lang.String getDateString(java.sql.Timestamp p0,java.lang.String p1) throws java.rmi.RemoteException;
 public com.idega.block.school.data.SchoolClassMember getLatestPlacementFromElemAndHighSchool(com.idega.user.data.User p0,com.idega.block.school.data.SchoolSeason p1)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.school.data.SchoolClassMember getLatestPlacementFromElemAndHighSchool(com.idega.user.data.User p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.school.data.SchoolCategoryHome getSchoolCategoryHome()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public se.idega.idegaweb.commune.school.business.SchoolChoiceBusiness getSchoolChoiceBusiness()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.user.business.UserBusiness getUserBusiness()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void removeSchoolClassMember(java.lang.Integer p0) throws java.rmi.RemoteException;
}
