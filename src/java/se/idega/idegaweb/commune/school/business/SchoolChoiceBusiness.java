package se.idega.idegaweb.commune.school.business;

import javax.ejb.*;

public interface SchoolChoiceBusiness extends com.idega.business.IBOService,com.idega.block.process.business.CaseBusiness
{
//<<<<<<< SchoolChoiceBusiness.java
// public se.idega.idegaweb.commune.school.data.SchoolChoice getSchoolChoice(int p0)throws javax.ejb.FinderException,java.rmi.RemoteException, java.rmi.RemoteException;
// public com.idega.block.school.data.School getFirstProviderForUser(com.idega.user.data.User p0)throws javax.ejb.FinderException,java.rmi.RemoteException, java.rmi.RemoteException;
// public java.lang.String getLocalizedCaseDescription(com.idega.block.process.data.Case p0,java.util.Locale p1)throws java.rmi.RemoteException, java.rmi.RemoteException;
// public java.util.List createSchoolChoices(int p0,int p1,int p2,int p3,int p4,int p5,int p6,int p7,int p8,int p9,java.lang.String p10,java.lang.String p11,boolean p12,boolean p13,boolean p14,boolean p15,boolean p16)throws com.idega.data.IDOCreateException, java.rmi.RemoteException;
// public se.idega.idegaweb.commune.message.business.MessageBusiness getMessageBusiness()throws java.rmi.RemoteException, java.rmi.RemoteException;
// public void createTestFamily() throws java.rmi.RemoteException;
// public se.idega.idegaweb.commune.school.data.CurrentSchoolSeasonHome getCurrentSchoolSeasonHome()throws java.rmi.RemoteException, java.rmi.RemoteException;
// public void createCurrentSchoolSeason(java.lang.Integer p0,java.lang.Integer p1)throws java.rmi.RemoteException, java.rmi.RemoteException;
// public boolean groupPlaceAction(java.lang.Integer p0,java.lang.String p1) throws java.rmi.RemoteException;
// public com.idega.block.school.data.SchoolSeason getCurrentSeason()throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
// public com.idega.block.school.data.School getSchool(int p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
// public com.idega.block.school.data.SchoolHome getSchoolHome()throws java.rmi.RemoteException, java.rmi.RemoteException;
// public boolean preliminaryAction(java.lang.Integer p0) throws java.rmi.RemoteException;
// public com.idega.block.school.business.SchoolBusiness getSchoolBusiness()throws java.rmi.RemoteException, java.rmi.RemoteException;
// public java.lang.String getPreliminaryMessageSubject() throws java.rmi.RemoteException;
// public boolean noRoomAction(java.lang.Integer p0) throws java.rmi.RemoteException;
// public com.idega.block.school.data.School getFirstManagingSchoolForUser(com.idega.user.data.User p0)throws javax.ejb.FinderException,java.rmi.RemoteException, java.rmi.RemoteException;
// public java.lang.String getGroupedMessageSubject() throws java.rmi.RemoteException;
// public se.idega.idegaweb.commune.school.data.SchoolChoiceHome getSchoolChoiceHome()throws java.rmi.RemoteException, java.rmi.RemoteException;
// public com.idega.block.school.data.SchoolSeasonHome getSchoolSeasonHome()throws java.rmi.RemoteException, java.rmi.RemoteException;
// public java.lang.String getBundleIdentifier() throws java.rmi.RemoteException;
//=======
 public se.idega.idegaweb.commune.school.data.SchoolChoice getSchoolChoice(int p0)throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 public java.lang.String getLocalizedCaseDescription(com.idega.block.process.data.Case p0,java.util.Locale p1)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.List createSchoolChoices(int p0,int p1,int p2,int p3,int p4,int p5,int p6,int p7,int p8,int p9,java.lang.String p10,java.lang.String p11,boolean p12,boolean p13,boolean p14,boolean p15,boolean p16)throws com.idega.data.IDOCreateException, java.rmi.RemoteException;
 public se.idega.idegaweb.commune.message.business.MessageBusiness getMessageBusiness()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void createTestFamily() throws java.rmi.RemoteException;
 public se.idega.idegaweb.commune.school.data.CurrentSchoolSeasonHome getCurrentSchoolSeasonHome()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void createCurrentSchoolSeason(java.lang.Integer p0,java.lang.Integer p1)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public boolean groupPlaceAction(java.lang.Integer p0,java.lang.String p1) throws java.rmi.RemoteException;
 public com.idega.block.school.data.SchoolSeason getCurrentSeason()throws javax.ejb.FinderException,java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.school.data.School getSchool(int p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.school.data.SchoolHome getSchoolHome()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public boolean preliminaryAction(java.lang.Integer p0) throws java.rmi.RemoteException;
 public com.idega.block.school.business.SchoolBusiness getSchoolBusiness()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.lang.String getPreliminaryMessageSubject() throws java.rmi.RemoteException;
 public boolean noRoomAction(java.lang.Integer p0) throws java.rmi.RemoteException;
 public java.lang.String getGroupedMessageSubject() throws java.rmi.RemoteException;
 public se.idega.idegaweb.commune.school.data.SchoolChoiceHome getSchoolChoiceHome()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.school.data.SchoolSeasonHome getSchoolSeasonHome()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.lang.String getBundleIdentifier() throws java.rmi.RemoteException;
 public com.idega.block.school.data.School getFirstProviderForUser(com.idega.user.data.User p0)throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
//>>>>>>> 1.6
}
