package se.idega.idegaweb.commune.school.report.business;


public interface ReportBusiness extends com.idega.business.IBOService
{
 public se.idega.idegaweb.commune.school.report.business.ReportModel createReportModel(java.lang.Class p0) throws java.rmi.RemoteException;
 public java.util.Collection getAllStudyPaths() throws java.rmi.RemoteException;
 public java.util.Collection getCompulsorySchoolAreas() throws java.rmi.RemoteException;
 public java.util.Collection getCompulsorySchools(com.idega.block.school.data.SchoolArea p0) throws java.rmi.RemoteException;
 public com.idega.block.school.data.SchoolSeason getCurrentSchoolSeason() throws java.rmi.RemoteException;
 public java.util.Collection getElementarySchoolAreas() throws java.rmi.RemoteException;
 public java.util.Collection getElementarySchools(com.idega.block.school.data.SchoolArea p0) throws java.rmi.RemoteException;
 public int getHighSchoolStudentAgeFrom(int p0) throws java.rmi.RemoteException;
 public int getHighSchoolStudentAgeTo(int p0) throws java.rmi.RemoteException;
 public int getSchoolSeasonId() throws java.rmi.RemoteException;
 public int getSchoolSeasonStartYear() throws java.rmi.RemoteException;
 public void log(java.lang.String p0) throws java.rmi.RemoteException;
}
