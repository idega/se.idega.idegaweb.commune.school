package se.idega.idegaweb.commune.school.business;

import javax.ejb.*;

import se.idega.idegaweb.commune.business.CommuneUserBusiness;

public interface SchoolChoiceBusiness extends com.idega.business.IBOService,com.idega.block.process.business.CaseBusiness
{
 public void createCurrentSchoolSeason(java.lang.Integer p0,java.lang.Integer p1)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public se.idega.idegaweb.commune.school.data.SchoolChoice createSchoolChangeChoice(int p0,int p1,int p2,int p3,int p4,int p5,int p6,int p7,int p8,java.lang.String p9,java.lang.String p10,boolean p11,boolean p12,boolean p13,boolean p14)throws com.idega.data.IDOCreateException, java.rmi.RemoteException;
 public se.idega.idegaweb.commune.school.data.SchoolChoice createSchoolChoice(int p0,int p1,int p2,int p3,int p4,int p5,int p6,int p7,int p8,int p9,java.lang.String p10,java.lang.String p11,java.sql.Timestamp p12,boolean p13,boolean p14,boolean p15,boolean p16,boolean p17,com.idega.block.process.data.CaseStatus p18,com.idega.block.process.data.Case p19)throws javax.ejb.CreateException,java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.List createSchoolChoices(int p0,int p1,int p2,int p3,int p4,int p5,int p6,int p7,int p8,int p9,int p10,java.lang.String p11,java.lang.String p12,boolean p13,boolean p14,boolean p15,boolean p16,boolean p17)throws com.idega.data.IDOCreateException, java.rmi.RemoteException;
 public void createTestFamily() throws java.rmi.RemoteException;
 public se.idega.idegaweb.commune.school.data.SchoolChoice findByStudentAndSchoolAndSeason(int p0,int p1,int p2)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.Collection findByStudentAndSeason(int p0,int p1)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.Collection getApplicantsForSchool(int p0,int p1,int p2,int[] p3,java.lang.String[] p4,java.lang.String p5)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.Collection getApplicantsForSchool(int p0,int p1,int p2,java.lang.String[] p3,java.lang.String p4)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.Collection getApplicantsForSchoolAndSeasonAndGrade(int p0,int p1,int p2)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.lang.String getBundleIdentifier() throws java.rmi.RemoteException;
 public se.idega.idegaweb.commune.school.data.CurrentSchoolSeasonHome getCurrentSchoolSeasonHome()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.school.data.SchoolSeason getCurrentSeason()throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 public com.idega.block.school.data.School getFirstProviderForUser(com.idega.user.data.User p0)throws javax.ejb.FinderException,java.rmi.RemoteException, java.rmi.RemoteException;
 public java.lang.String getGroupedMessageSubject() throws java.rmi.RemoteException;
 public java.lang.String getLocalizedCaseDescription(com.idega.block.process.data.Case p0,java.util.Locale p1)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public is.idega.idegaweb.member.business.MemberFamilyLogic getMemberFamilyLogic()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public se.idega.idegaweb.commune.message.business.MessageBusiness getMessageBusiness()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.lang.String getNewHeadMasterSubject() throws java.rmi.RemoteException;
 public int getNumberOfApplications(int p0,int p1,int p2)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public int getNumberOfApplications(int p0,int p1)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public int getNumberOfApplicationsForStudents(int p0,int p1)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.lang.String getOldHeadmasterSubject() throws java.rmi.RemoteException;
 public java.lang.String getPreliminaryMessageSubject() throws java.rmi.RemoteException;
 public com.idega.block.school.data.School getSchool(int p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.school.business.SchoolBusiness getSchoolBusiness()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public se.idega.idegaweb.commune.school.data.SchoolChoice getSchoolChoice(int p0)throws javax.ejb.FinderException,java.rmi.RemoteException, java.rmi.RemoteException;
 public se.idega.idegaweb.commune.school.data.SchoolChoiceHome getSchoolChoiceHome()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.school.data.SchoolHome getSchoolHome()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.school.data.SchoolSeasonHome getSchoolSeasonHome()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.lang.String getSeparateParentSubjectAppl() throws java.rmi.RemoteException;
 public java.lang.String getSeparateParentSubjectChange() throws java.rmi.RemoteException;
 public CommuneUserBusiness getUserBusiness()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public boolean groupPlaceAction(java.lang.Integer p0,com.idega.user.data.User p1) throws java.rmi.RemoteException;
 public boolean noRoomAction(java.lang.Integer p0,com.idega.user.data.User p1) throws java.rmi.RemoteException;
 public boolean preliminaryAction(java.lang.Integer p0,com.idega.user.data.User p1) throws java.rmi.RemoteException;
 public void rejectApplication(int p0,int p1,com.idega.user.data.User p2,java.lang.String p3,java.lang.String p4)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void setAsPreliminary(se.idega.idegaweb.commune.school.data.SchoolChoice p0,com.idega.user.data.User p1)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void setChildcarePreferences(int p0,boolean p1,java.lang.String p2)throws java.rmi.RemoteException, java.rmi.RemoteException;
}
