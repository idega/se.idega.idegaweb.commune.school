package se.idega.idegaweb.commune.school.data;


public interface SchoolChoiceHome extends com.idega.data.IDOHome
{
 public SchoolChoice create() throws javax.ejb.CreateException, java.rmi.RemoteException;
 public SchoolChoice findByPrimaryKey(Object pk) throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findByCodeAndStatus(java.lang.String p0,java.lang.String[] p1,int p2,int p3)throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findAll()throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findByChildId(int p0,int p1)throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findByChildId(int p0)throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findByChosenSchoolId(int p0,int p1)throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findChoices(int p0,int p1,int p2,java.lang.String[] p3,java.lang.String p4)throws javax.ejb.FinderException,java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.Collection findChoices(int p0,int p1,int p2,int[] p3,java.lang.String[] p4,java.lang.String p5)throws javax.ejb.FinderException,java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.Collection findByChildAndSchoolAndSeason(int p0,int p1,int p2)throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findByCodeAndStatus(java.lang.String p0,java.lang.String[] p1,int p2,int p3,java.lang.String p4)throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findBySchoolAndSeasonAndGrade(int p0,int p1,int p2)throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findByChildAndSeason(int p0,int p1)throws javax.ejb.FinderException, java.rmi.RemoteException;
 public int getNumberOfApplications(java.lang.String p0,int p1,int p2,int p3)throws javax.ejb.FinderException,com.idega.data.IDOException, java.rmi.RemoteException;
 public int getNumberOfApplications(java.lang.String p0,int p1,int p2)throws javax.ejb.FinderException,com.idega.data.IDOException, java.rmi.RemoteException;

}