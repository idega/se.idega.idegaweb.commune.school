package se.idega.idegaweb.commune.school.data;

import javax.ejb.*;

public interface SchoolChoice extends com.idega.data.IDOEntity,com.idega.block.process.data.Case
{
 public int getSchoolSeasonId() throws java.rmi.RemoteException;
 public int getChosenSchoolId() throws java.rmi.RemoteException;
 public int getWorkSituation2() throws java.rmi.RemoteException;
 public boolean getCustodiansAgree() throws java.rmi.RemoteException;
 public int getWorkSituation1() throws java.rmi.RemoteException;
 public boolean getSchoolCatalogue() throws java.rmi.RemoteException;
 public void setMessage(java.lang.String p0) throws java.rmi.RemoteException;
 public void setWorksituation2(int p0) throws java.rmi.RemoteException;
 public java.lang.String getGroupPlace() throws java.rmi.RemoteException;
 public void setWorksituation1(int p0) throws java.rmi.RemoteException;
 public boolean getAutoAssign() throws java.rmi.RemoteException;
 public void setCustodiansAgree(boolean p0) throws java.rmi.RemoteException;
 public void setSchoolCatalogue(boolean p0) throws java.rmi.RemoteException;
 public void setChoiceOrder(int p0) throws java.rmi.RemoteException;
 public boolean getChangeOfSchool() throws java.rmi.RemoteException;
 public java.lang.String getCaseCodeKey() throws java.rmi.RemoteException;
 public java.lang.String getCaseStatusCreated() throws java.rmi.RemoteException;
 public void setSchoolChoiceDate(java.sql.Timestamp p0) throws java.rmi.RemoteException;
 public void setLanguageChoice(java.lang.String p0) throws java.rmi.RemoteException;
 public int getCurrentSchoolId() throws java.rmi.RemoteException;
 public java.sql.Timestamp getSchoolChoiceDate() throws java.rmi.RemoteException;
 public void setSchoolSeasonId(int p0) throws java.rmi.RemoteException;
 public java.lang.String[] getCaseStatusDescriptions() throws java.rmi.RemoteException;
 public int getChildId() throws java.rmi.RemoteException;
 public int getMethod() throws java.rmi.RemoteException;
 public com.idega.block.school.data.School setCurrentSchool() throws java.rmi.RemoteException;
 public void setChosenSchoolId(int p0) throws java.rmi.RemoteException;
 public void setKeepChildrenCare(boolean p0) throws java.rmi.RemoteException;
 public void setGrade(int p0) throws java.rmi.RemoteException;
 public boolean getKeepChildrenCare() throws java.rmi.RemoteException;
 public int getChoiceOrder() throws java.rmi.RemoteException;
 public void setMethod(int p0) throws java.rmi.RemoteException;
 public void setGroupPlace(java.lang.String p0) throws java.rmi.RemoteException;
 public int getGrade() throws java.rmi.RemoteException;
 public java.lang.String getMessage() throws java.rmi.RemoteException;
 public void setCurrentSchoolId(int p0) throws java.rmi.RemoteException;
 public void setAutoAssign(boolean p0) throws java.rmi.RemoteException;
 public void initializeAttributes() throws java.rmi.RemoteException;
 public void setChangeOfSchool(boolean p0) throws java.rmi.RemoteException;
 public java.lang.String getCaseStatusQuiet() throws java.rmi.RemoteException;
 public java.lang.String getCaseStatusPreliminary() throws java.rmi.RemoteException;
 public com.idega.block.school.data.School getChosenSchool() throws java.rmi.RemoteException;
 public java.lang.String getLanguageChoice() throws java.rmi.RemoteException;
 public java.lang.String[] getCaseStatusKeys() throws java.rmi.RemoteException;
 public void setChildId(int p0) throws java.rmi.RemoteException;
 public java.lang.String getCaseStatusPlaced() throws java.rmi.RemoteException;
 public java.lang.String getCaseCodeDescription() throws java.rmi.RemoteException;
}
