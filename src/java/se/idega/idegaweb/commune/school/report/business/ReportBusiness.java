package se.idega.idegaweb.commune.school.report.business;


public interface ReportBusiness extends com.idega.business.IBOService
{
 public se.idega.idegaweb.commune.school.report.business.ReportModel createReportModel(java.lang.Class p0) throws java.rmi.RemoteException;
 public java.util.Collection getAllStudyPaths() throws java.rmi.RemoteException;
 public int getCompulsoryNackaCommunePlacementCount(java.lang.String p0) throws java.rmi.RemoteException;
 public int getCompulsoryOtherCommunesPlacementCount(java.lang.String p0) throws java.rmi.RemoteException;
 public int getCompulsoryPrivateSchoolPlacementCount(java.lang.String p0) throws java.rmi.RemoteException;
 public com.idega.block.school.data.SchoolSeason getCurrentSchoolSeason() throws java.rmi.RemoteException;
 public int getElementaryForeignSchoolPlacementCount(java.lang.String p0) throws java.rmi.RemoteException;
 public int getElementaryNackaCommunePlacementCount(java.lang.String p0) throws java.rmi.RemoteException;
 public int getElementaryOtherCommunesPlacementCount(java.lang.String p0) throws java.rmi.RemoteException;
 public int getElementaryPrivateSchoolPlacementCount(java.lang.String p0) throws java.rmi.RemoteException;
 public java.util.Collection getElementarySchoolAreas() throws java.rmi.RemoteException;
 public int getElementarySchoolOCCPlacementCount(int p0,java.lang.String p1) throws java.rmi.RemoteException;
 public int getElementarySchoolPlacementCount(int p0,java.lang.String p1) throws java.rmi.RemoteException;
 public java.util.Collection getElementarySchools(com.idega.block.school.data.SchoolArea p0) throws java.rmi.RemoteException;
 public int getHighSchoolCountyCouncilPlacementCount(java.lang.String p0,java.lang.String p1) throws java.rmi.RemoteException;
 public int getHighSchoolCountyCouncilPlacementCount(int p0,java.lang.String p1,boolean p2) throws java.rmi.RemoteException;
 public int getHighSchoolNackaCommunePlacementCount(java.lang.String p0,java.lang.String p1) throws java.rmi.RemoteException;
 public int getHighSchoolNackaCommunePlacementCount(int p0,java.lang.String p1,boolean p2) throws java.rmi.RemoteException;
 public int getHighSchoolOtherCommunesPlacementCount(java.lang.String p0,java.lang.String p1) throws java.rmi.RemoteException;
 public int getHighSchoolOtherCommunesPlacementCount(int p0,java.lang.String p1,boolean p2) throws java.rmi.RemoteException;
 public int getHighSchoolPrivatePlacementCount(java.lang.String p0,java.lang.String p1) throws java.rmi.RemoteException;
 public int getHighSchoolPrivatePlacementCount(int p0,java.lang.String p1,boolean p2) throws java.rmi.RemoteException;
 public int getSchoolSeasonId() throws java.rmi.RemoteException;
 public void log(java.lang.String p0) throws java.rmi.RemoteException;
}
