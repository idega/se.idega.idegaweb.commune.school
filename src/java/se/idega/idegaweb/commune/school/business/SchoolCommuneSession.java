package se.idega.idegaweb.commune.school.business;

import javax.ejb.*;

public interface SchoolCommuneSession extends com.idega.business.IBOSession
{
 public se.idega.idegaweb.commune.business.CommuneUserBusiness getCommuneUserBusiness()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public int getSchoolClassID() throws java.rmi.RemoteException;
 public int getSchoolID()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public int getSchoolSeasonID() throws java.rmi.RemoteException;
 public int getSchoolYearID() throws java.rmi.RemoteException;
 public void setSchoolClassID(int p0) throws java.rmi.RemoteException;
 public void setSchoolID(int p0) throws java.rmi.RemoteException;
 public void setSchoolSeasonID(int p0) throws java.rmi.RemoteException;
 public void setSchoolYearID(int p0) throws java.rmi.RemoteException;
}
