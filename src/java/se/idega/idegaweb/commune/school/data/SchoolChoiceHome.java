package se.idega.idegaweb.commune.school.data;


public interface SchoolChoiceHome extends com.idega.data.IDOHome
{
 public SchoolChoice create() throws javax.ejb.CreateException, java.rmi.RemoteException;
 public SchoolChoice findByPrimaryKey(Object pk) throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findByChildId(int p0)throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findByChildId(int childId,int seasonId)throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findByChosenSchoolId(int schoolId,int seasonId)throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findByCodeAndStatus(String caseCode,String[] caseStatus,int schoolId,int seasonId)throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findByCodeAndStatus(String caseCode,String[] caseStatus,int schoolId,int seasonId,String ordered)throws javax.ejb.FinderException, java.rmi.RemoteException;

}