package se.idega.idegaweb.commune.school.report.business;


public interface ReportBusiness extends com.idega.business.IBOService
{
 public se.idega.idegaweb.commune.school.report.business.ReportModel createReportModel(java.lang.Class p0) throws java.rmi.RemoteException;
 public int getAfterSchool6TypeId() throws java.rmi.RemoteException;
 public int getAfterSchool7_9TypeId() throws java.rmi.RemoteException;
 public java.util.Collection getAllStudyPaths() throws java.rmi.RemoteException;
 public java.util.Collection getAllStudyPathsIncludingDirections() throws java.rmi.RemoteException;
 public int getCompulsoryHighSchoolTypeId() throws java.rmi.RemoteException;
 public java.util.Collection getCompulsoryHighSchools() throws java.rmi.RemoteException;
 public java.util.Collection getCompulsorySchoolAreas() throws java.rmi.RemoteException;
 public int getCompulsorySchoolTypeId() throws java.rmi.RemoteException;
 public java.util.Collection getCompulsorySchools(com.idega.block.school.data.SchoolArea p0) throws java.rmi.RemoteException;
 public com.idega.block.school.data.SchoolSeason getCurrentSchoolSeason() throws java.rmi.RemoteException;
 public java.util.Collection getElementarySchoolAreas() throws java.rmi.RemoteException;
 public int getElementarySchoolTypeId() throws java.rmi.RemoteException;
 public java.util.Collection getElementarySchools(com.idega.block.school.data.SchoolArea p0) throws java.rmi.RemoteException;
 public int getFamilyAfterSchool6TypeId() throws java.rmi.RemoteException;
 public int getFamilyAfterSchool7_9TypeId() throws java.rmi.RemoteException;
 public int getFamilyDayCareSchoolTypeId() throws java.rmi.RemoteException;
 public int getGeneralFamilyDaycareSchoolTypeId() throws java.rmi.RemoteException;
 public int getGeneralPreSchoolTypeId() throws java.rmi.RemoteException;
 public int getHighSchoolStudentAgeFrom(int p0) throws java.rmi.RemoteException;
 public int getHighSchoolStudentAgeTo(int p0) throws java.rmi.RemoteException;
 public int getHighSchoolTypeId() throws java.rmi.RemoteException;
 public java.util.Collection getNackaCommuneHighSchools() throws java.rmi.RemoteException;
 public int getPreSchoolClassTypeId() throws java.rmi.RemoteException;
 public int getPreSchoolTypeId() throws java.rmi.RemoteException;
 public java.util.Collection getPrivateHighSchools() throws java.rmi.RemoteException;
 public java.util.Collection getPrivateSchoolAreas() throws java.rmi.RemoteException;
 public java.util.Collection getPrivateSchools(com.idega.block.school.data.SchoolArea p0) throws java.rmi.RemoteException;
 public int getSchoolSeasonId() throws java.rmi.RemoteException;
 public int getSchoolSeasonStartYear() throws java.rmi.RemoteException;
 public void log(java.lang.String p0) throws java.rmi.RemoteException;
}
