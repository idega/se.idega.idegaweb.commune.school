package se.idega.idegaweb.commune.school.business;


public interface SchoolExportBusiness extends com.idega.business.IBOService
{
 public void exportFile(java.util.Collection p0,java.lang.String p1) throws java.rmi.RemoteException;
 public java.util.Collection findAllElementarySchoolPlacements() throws java.rmi.RemoteException;
 public java.util.Collection findAllHighSchoolPlacements() throws java.rmi.RemoteException;
 public java.util.Collection findAllPlacementsByCategory(com.idega.block.school.data.SchoolCategory p0) throws java.rmi.RemoteException;
 public java.util.Iterator getAllExportFiles() throws java.rmi.RemoteException;
}
