package se.idega.idegaweb.commune.school.business;

import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import se.idega.idegaweb.commune.school.data.SchoolChoice;

import com.idega.block.process.data.Case;
import com.idega.block.process.data.CaseStatus;

public interface SchoolChoiceBusiness extends com.idega.business.IBOService,com.idega.block.process.business.CaseBusiness
{
 public se.idega.idegaweb.commune.school.data.SchoolChoice getSchoolChoice(int p0)throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 public java.lang.String getLocalizedCaseDescription(com.idega.block.process.data.Case p0,java.util.Locale p1)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.List createSchoolChoices(int p0,int p1,int schoolTypeID,int p2,int p3,int p4,int p5,int p6,int p7,int p8,int p9,java.lang.String p10,java.lang.String p11,boolean p12,boolean p13,boolean p14,boolean p15,boolean p16)throws com.idega.data.IDOCreateException, java.rmi.RemoteException;
 public se.idega.idegaweb.commune.message.business.MessageBusiness getMessageBusiness()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void createTestFamily() throws java.rmi.RemoteException;
 public se.idega.idegaweb.commune.school.data.CurrentSchoolSeasonHome getCurrentSchoolSeasonHome()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void createCurrentSchoolSeason(java.lang.Integer p0,java.lang.Integer p1)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public boolean groupPlaceAction(java.lang.Integer p0,com.idega.user.data.User p1) throws java.rmi.RemoteException;
 public com.idega.block.school.data.SchoolSeason getCurrentSeason()throws javax.ejb.FinderException,java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.school.data.School getSchool(int p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.school.data.SchoolHome getSchoolHome()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public boolean preliminaryAction(java.lang.Integer p0, com.idega.user.data.User p1) throws java.rmi.RemoteException;
 public com.idega.block.school.business.SchoolBusiness getSchoolBusiness()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.lang.String getPreliminaryMessageSubject() throws java.rmi.RemoteException;
 public boolean noRoomAction(java.lang.Integer p0, com.idega.user.data.User p1) throws java.rmi.RemoteException;
 public java.lang.String getGroupedMessageSubject() throws java.rmi.RemoteException;
 public se.idega.idegaweb.commune.school.data.SchoolChoiceHome getSchoolChoiceHome()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.school.data.SchoolSeasonHome getSchoolSeasonHome()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.lang.String getBundleIdentifier() throws java.rmi.RemoteException;
 public com.idega.block.school.data.School getFirstProviderForUser(com.idega.user.data.User p0)throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection getApplicantsForSchoolAndSeasonAndGrade(int schoolID,int seasonID,int grade) throws java.rmi.RemoteException; 
 public int getNumberOfApplications(int schoolID, int schoolSeasonID, int grade) throws java.rmi.RemoteException;
 public int getNumberOfApplications(int schoolID, int schoolSeasonID) throws java.rmi.RemoteException;
 public se.idega.idegaweb.commune.school.data.SchoolChoice findByStudentAndSchoolAndSeason(int studentID, int schoolID, int seasonID) throws java.rmi.RemoteException;
 public void setAsPreliminary(se.idega.idegaweb.commune.school.data.SchoolChoice choice, com.idega.user.data.User performer) throws java.rmi.RemoteException;
 public java.util.Collection findByStudentAndSeason(int studentID, int seasonID) throws java.rmi.RemoteException;
 public void rejectApplication(int applicationID, int seasonID, com.idega.user.data.User performer, String messageSubject, String messageBody) throws java.rmi.RemoteException;
 public SchoolChoice createSchoolChoice(int userId, int childId, int school_type_id, int current_school, int chosen_school, int grade, int choiceOrder, int method, int workSituation1, int workSituation2, String language, String message, java.sql.Timestamp choiceDate, boolean changeOfSchool, boolean keepChildrenCare, boolean autoAssign, boolean custodiansAgree, boolean schoolCatalogue, CaseStatus caseStatus, Case parentCase) throws CreateException, RemoteException, FinderException;
 public Collection getApplicantsForSchool(int schoolID,int seasonID,int grade, String[] validStatuses, String searchString) throws RemoteException;
 public Collection getApplicantsForSchool(int schoolID,int seasonID,int grade, int[] choiceOrder, String[] validStatuses, String searchString) throws RemoteException;
}
