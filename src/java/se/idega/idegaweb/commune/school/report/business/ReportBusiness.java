package se.idega.idegaweb.commune.school.report.business;


public interface ReportBusiness extends com.idega.business.IBOService
{
 public se.idega.idegaweb.commune.school.report.business.ReportModel createReportModel(java.lang.Class p0) throws java.rmi.RemoteException;
 public int getCompulsoryNackaCommunePlacementCount(java.lang.String p0) throws java.rmi.RemoteException;
 public int getCompulsoryOtherCommunesPlacementCount(java.lang.String p0) throws java.rmi.RemoteException;
 public int getCompulsoryPrivateSchoolPlacementCount(java.lang.String p0) throws java.rmi.RemoteException;
 public int getElementaryForeignSchoolPlacementCount(java.lang.String p0) throws java.rmi.RemoteException;
 public int getElementaryNackaCommunePlacementCount(java.lang.String p0) throws java.rmi.RemoteException;
 public int getElementaryOtherCommunesPlacementCount(java.lang.String p0) throws java.rmi.RemoteException;
 public int getElementaryPrivateSchoolPlacementCount(java.lang.String p0) throws java.rmi.RemoteException;
 public void log(java.lang.String p0) throws java.rmi.RemoteException;
}
