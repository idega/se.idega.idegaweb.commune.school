package se.idega.idegaweb.commune.school.data;

import java.rmi.RemoteException;
import javax.ejb.*;

import com.idega.block.school.data.School;

public interface SchoolChoice extends com.idega.data.IDOEntity, com.idega.block.process.data.Case {
	public int getChosenSchoolId() throws java.rmi.RemoteException;
	public int getWorkSituation2() throws java.rmi.RemoteException;
	public boolean getCustodiansAgree() throws java.rmi.RemoteException;
	public int getWorkSituation1() throws java.rmi.RemoteException;
	public boolean getSchoolCatalogue() throws java.rmi.RemoteException;
	public void setMessage(java.lang.String p0) throws java.rmi.RemoteException;
	public void setWorksituation2(int p0) throws java.rmi.RemoteException;
	public void setWorksituation1(int p0) throws java.rmi.RemoteException;
	public boolean getAutoAssign() throws java.rmi.RemoteException;
	public void setCustodiansAgree(boolean p0) throws java.rmi.RemoteException;
	public void setSchoolCatalogue(boolean p0) throws java.rmi.RemoteException;
	public void setChoiceOrder(int p0) throws java.rmi.RemoteException;
	public java.lang.String getCaseCodeKey() throws java.rmi.RemoteException;
	public java.lang.String getCaseStatusCreated() throws java.rmi.RemoteException;
	public boolean getChangeOfSchool() throws java.rmi.RemoteException;
	public void setSchoolChoiceDate(java.sql.Timestamp p0) throws java.rmi.RemoteException;
	public void setLanguageChoice(java.lang.String p0) throws java.rmi.RemoteException;
	public int getCurrentSchoolId() throws java.rmi.RemoteException;
	public java.sql.Timestamp getSchoolChoiceDate() throws RemoteException;
	public java.lang.String[] getCaseStatusDescriptions() throws RemoteException;
	public int getChildId() throws RemoteException;
	public int getMethod() throws RemoteException;
	public void setChosenSchoolId(int p0) throws RemoteException;
	public void setKeepChildrenCare(boolean p0) throws RemoteException;
	public void setGrade(int p0) throws RemoteException;
	public boolean getKeepChildrenCare() throws RemoteException;
	public void setMethod(int p0) throws RemoteException;
	public int getChoiceOrder() throws RemoteException;
	public int getGrade() throws RemoteException;
	public java.lang.String getMessage() throws RemoteException;
	public void setCurrentSchoolId(int p0) throws RemoteException;
	public void setAutoAssign(boolean p0) throws RemoteException;
	public void setChangeOfSchool(boolean p0) throws RemoteException;
	public java.lang.String getCaseStatusQuiet();
	public java.lang.String getCaseStatusPreliminary();
	public java.lang.String getLanguageChoice() throws RemoteException;
	public java.lang.String[] getCaseStatusKeys() throws RemoteException;
	public void setChildId(int p0) throws RemoteException;
	public java.lang.String getCaseStatusPlaced();
	public java.lang.String getCaseCodeDescription();
	public String getGroupPlace() throws RemoteException;
	public void setGroupPlace(String place) throws RemoteException;
	public int getSchoolSeasonId() throws RemoteException;
  	public void setSchoolSeasonId(int id) throws RemoteException;
  	public School getChosenSchool() throws RemoteException;
  	public School setCurrentSchool() throws RemoteException;
  
}
