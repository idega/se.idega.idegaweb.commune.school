package se.idega.idegaweb.commune.school.business;


public interface SchoolCommuneSession extends com.idega.business.IBOSession
{
 public se.idega.idegaweb.commune.business.CommuneUserBusiness getCommuneUserBusiness()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.lang.String getParameterSchoolClassID() throws java.rmi.RemoteException;
 public java.lang.String getParameterSchoolID() throws java.rmi.RemoteException;
 public java.lang.String getParameterSchoolSeasonID() throws java.rmi.RemoteException;
 public java.lang.String getParameterSchoolYearID() throws java.rmi.RemoteException;
 public java.lang.String getParameterStudentID() throws java.rmi.RemoteException;
 public int getSchoolClassID() throws java.rmi.RemoteException;
 public int getSchoolID()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public int getSchoolSeasonID() throws java.rmi.RemoteException;
 public int getSchoolYearID() throws java.rmi.RemoteException;
 public void setSchoolClassID(int p0) throws java.rmi.RemoteException;
 public void setSchoolID(int p0) throws java.rmi.RemoteException;
 public void setSchoolSeasonID(int p0) throws java.rmi.RemoteException;
 public void setSchoolYearID(int p0) throws java.rmi.RemoteException;
}
