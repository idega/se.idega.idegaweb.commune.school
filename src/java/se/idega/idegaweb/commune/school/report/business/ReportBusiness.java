package se.idega.idegaweb.commune.school.report.business;

import java.util.Collection;

import com.idega.block.school.data.SchoolArea;


public interface ReportBusiness extends com.idega.business.IBOService
{
 public se.idega.idegaweb.commune.school.report.business.ReportModel createReportModel(java.lang.Class p0) throws java.rmi.RemoteException;
 public Collection getAfterSchool6Areas() throws java.rmi.RemoteException;
 public int getAfterSchool6TypeId() throws java.rmi.RemoteException;
 public Collection getAfterSchool7_9Areas() throws java.rmi.RemoteException;
 public int getAfterSchool7_9TypeId() throws java.rmi.RemoteException;
 public Collection getAllStudyPaths() throws java.rmi.RemoteException;
 public Collection getAllStudyPathsIncludingDirections() throws java.rmi.RemoteException;
 public Collection getChildCareProviders() throws java.rmi.RemoteException;
 public Collection getCommuneSchools(int p0,Collection p1,Collection p2) throws java.rmi.RemoteException;
 public int getCompulsoryHighSchoolTypeId() throws java.rmi.RemoteException;
 public Collection getCompulsoryHighSchools() throws java.rmi.RemoteException;
 public Collection getCompulsorySchoolAreas() throws java.rmi.RemoteException;
 public int getCompulsorySchoolTypeId() throws java.rmi.RemoteException;
 public Collection getCompulsorySchools(com.idega.block.school.data.SchoolArea p0) throws java.rmi.RemoteException;
 public com.idega.block.school.data.SchoolSeason getCurrentSchoolSeason() throws java.rmi.RemoteException;
 public Collection<SchoolArea> getElementarySchoolAreas() throws java.rmi.RemoteException;
 public int getElementarySchoolTypeId() throws java.rmi.RemoteException;
 public Collection getElementarySchools(com.idega.block.school.data.SchoolArea p0) throws java.rmi.RemoteException;
 public int getFamilyAfterSchool6TypeId() throws java.rmi.RemoteException;
 public int getFamilyAfterSchool7_9TypeId() throws java.rmi.RemoteException;
 public int getFamilyDayCareSchoolTypeId() throws java.rmi.RemoteException;
 public int getGeneralFamilyDaycareSchoolTypeId() throws java.rmi.RemoteException;
 public int getGeneralPreSchoolTypeId() throws java.rmi.RemoteException;
 public int getHighSchoolStudentAgeFrom(int p0) throws java.rmi.RemoteException;
 public int getHighSchoolStudentAgeTo(int p0) throws java.rmi.RemoteException;
 public int getHighSchoolTypeId() throws java.rmi.RemoteException;
 public Collection getNackaCommuneHighSchools() throws java.rmi.RemoteException;
 public int getPreSchoolClassTypeId() throws java.rmi.RemoteException;
 public Collection getPreSchoolOperationAreas() throws java.rmi.RemoteException;
 public int getPreSchoolTypeId() throws java.rmi.RemoteException;
 public Collection getPrivateHighSchools() throws java.rmi.RemoteException;
 public Collection getPrivateSchoolAreas() throws java.rmi.RemoteException;
 public Collection getPrivateSchools(com.idega.block.school.data.SchoolArea p0) throws java.rmi.RemoteException;
 public int getSchoolSeasonId() throws java.rmi.RemoteException;
 public int getSchoolSeasonStartYear() throws java.rmi.RemoteException;
 public void log(java.lang.String p0) throws java.rmi.RemoteException;
}
