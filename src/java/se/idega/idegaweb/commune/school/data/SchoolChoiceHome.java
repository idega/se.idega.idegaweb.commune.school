package se.idega.idegaweb.commune.school.data;


public interface SchoolChoiceHome extends com.idega.data.IDOHome
{
 public SchoolChoice create() throws javax.ejb.CreateException;
 public SchoolChoice findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAll()throws javax.ejb.FinderException;
 public SchoolChoice findByChildAndChoiceNumberAndSeason(java.lang.Integer p0,java.lang.Integer p1,java.lang.Integer p2)throws javax.ejb.FinderException;
 public java.util.Collection findByChildAndSchoolAndSeason(int p0,int p1,int p2)throws javax.ejb.FinderException;
 public java.util.Collection findByChildAndSeason(int p0,int p1)throws javax.ejb.FinderException;
 public java.util.Collection findByChildId(int p0)throws javax.ejb.FinderException;
 public java.util.Collection findByChildId(int p0,int p1)throws javax.ejb.FinderException;
 public java.util.Collection findByChosenSchoolId(int p0,int p1)throws javax.ejb.FinderException;
 public java.util.Collection findByCodeAndStatus(java.lang.String p0,java.lang.String[] p1,int p2,int p3)throws javax.ejb.FinderException;
 public java.util.Collection findByCodeAndStatus(java.lang.String p0,java.lang.String[] p1,int p2,int p3,java.lang.String p4)throws javax.ejb.FinderException;
 public java.util.Collection findBySchoolAndFreeTime(int p0,int p1,boolean p2)throws javax.ejb.FinderException;
 public java.util.Collection findBySchoolAndSeasonAndGrade(int p0,int p1,int p2)throws javax.ejb.FinderException;
 public java.util.Collection findBySeason(int p0)throws javax.ejb.FinderException;
 public java.util.Collection findChoices(int p0,int p1,int p2,java.lang.String[] p3,java.lang.String p4,int p5,int p6,int p7)throws javax.ejb.FinderException,java.rmi.RemoteException;
 public java.util.Collection findChoices(int p0,int p1,int p2,int[] p3,java.lang.String[] p4,java.lang.String p5,int p6,int p7,int p8)throws javax.ejb.FinderException,java.rmi.RemoteException;
 public java.util.Collection findChoicesInClassAndSeasonAndSchool(int p0,int p1,int p2,boolean p3)throws javax.ejb.FinderException;
 public int getChoices(int p0,int p1,int p2)throws com.idega.data.IDOException;
 public int getCount(java.lang.String[] p0)throws com.idega.data.IDOException;
 public int getCount(int p0,java.lang.String[] p1)throws com.idega.data.IDOException;
 public int getCount(int p0,int p1,int p2,int[] p3,java.lang.String[] p4,java.lang.String p5)throws javax.ejb.FinderException,java.rmi.RemoteException,com.idega.data.IDOException;
 public int getMoveChoices(int p0,int p1,int p2)throws com.idega.data.IDOException;
 public int getNumberOfApplications(java.lang.String p0,int p1,int p2)throws javax.ejb.FinderException,com.idega.data.IDOException;
 public int getNumberOfApplications(java.lang.String p0,int p1,int p2,int p3)throws javax.ejb.FinderException,com.idega.data.IDOException;
 public int getNumberOfChoices(int p0,int p1)throws com.idega.data.IDOException;

}