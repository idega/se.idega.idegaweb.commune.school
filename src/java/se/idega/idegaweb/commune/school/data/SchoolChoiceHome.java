package se.idega.idegaweb.commune.school.data;


public interface SchoolChoiceHome extends com.idega.data.IDOHome
{
 public SchoolChoice create() throws javax.ejb.CreateException, java.rmi.RemoteException;
 public SchoolChoice findByPrimaryKey(Object pk) throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findByChildId(int p0)throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findByChosenSchoolId(int p0)throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findByCodeAndStatus(String caseCode,String[] caseStatus,int schoolId)throws javax.ejb.FinderException, java.rmi.RemoteException;

}