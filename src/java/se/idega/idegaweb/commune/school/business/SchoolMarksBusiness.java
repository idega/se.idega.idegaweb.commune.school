package se.idega.idegaweb.commune.school.business;


public interface SchoolMarksBusiness extends com.idega.business.IBOService
{
 public void calculateStatistics(java.lang.String p0) throws java.rmi.RemoteException;
 public java.util.Collection findAllSchoolIDsWithStatistics() throws java.rmi.RemoteException;
 public se.idega.idegaweb.commune.school.business.SchoolStatistics findCommuneStatistics(com.idega.presentation.IWContext p0) throws java.rmi.RemoteException;
 public java.util.Collection findSchoolMarksStatistics(com.idega.presentation.IWContext p0) throws java.rmi.RemoteException;
 public se.idega.idegaweb.commune.school.business.PercentValue getAuthPoints(se.idega.idegaweb.commune.school.data.SchoolStatisticsData p0) throws java.rmi.RemoteException;
 public java.lang.String getBundleIdentifier() throws java.rmi.RemoteException;
 public java.lang.String getCommuneMeanAuthPoints() throws java.rmi.RemoteException;
 public java.lang.String getCommuneMeanMarksFailed() throws java.rmi.RemoteException;
 public java.lang.String getCommuneMeanMarksNoGoal() throws java.rmi.RemoteException;
 public java.lang.String getCommuneMeanMarksNumberShare() throws java.rmi.RemoteException;
 public java.lang.String getCommuneMeanMarksPoints() throws java.rmi.RemoteException;
 public java.lang.String getCommuneMeanMeriteValue() throws java.rmi.RemoteException;
 public se.idega.idegaweb.commune.school.business.SchoolMarkValues getEnglishMarks(se.idega.idegaweb.commune.school.data.SchoolStatisticsData p0) throws java.rmi.RemoteException;
 public se.idega.idegaweb.commune.school.business.PercentValue getMarksFailed(se.idega.idegaweb.commune.school.data.SchoolStatisticsData p0) throws java.rmi.RemoteException;
 public se.idega.idegaweb.commune.school.business.PercentValue getMarksNoGoal(se.idega.idegaweb.commune.school.data.SchoolStatisticsData p0) throws java.rmi.RemoteException;
 public se.idega.idegaweb.commune.school.business.PercentValue getMarksNumberShare(se.idega.idegaweb.commune.school.data.SchoolStatisticsData p0) throws java.rmi.RemoteException;
 public se.idega.idegaweb.commune.school.business.PercentValue getMarksPoints(se.idega.idegaweb.commune.school.data.SchoolStatisticsData p0) throws java.rmi.RemoteException;
 public se.idega.idegaweb.commune.school.business.SchoolMarkValues getMathsMarks(se.idega.idegaweb.commune.school.data.SchoolStatisticsData p0) throws java.rmi.RemoteException;
 public se.idega.idegaweb.commune.school.business.PercentValue getMeritValue(se.idega.idegaweb.commune.school.data.SchoolStatisticsData p0) throws java.rmi.RemoteException;
 public se.idega.idegaweb.commune.school.data.SchoolStatisticsData getStoredValues(java.lang.String p0) throws java.rmi.RemoteException;
 public se.idega.idegaweb.commune.school.business.SchoolMarkValues getSumAuthMarks(se.idega.idegaweb.commune.school.data.SchoolStatisticsData p0) throws java.rmi.RemoteException;
 public java.lang.String getSumAuthPoints() throws java.rmi.RemoteException;
 public java.lang.String getSumMarksFailed() throws java.rmi.RemoteException;
 public java.lang.String getSumMarksNoGoal() throws java.rmi.RemoteException;
 public java.lang.String getSumMarksNumberShare() throws java.rmi.RemoteException;
 public java.lang.String getSumMarksPoints() throws java.rmi.RemoteException;
 public java.lang.String getSumMeriteValue() throws java.rmi.RemoteException;
 public se.idega.idegaweb.commune.school.business.SchoolMarkValues getSwedish2Marks(se.idega.idegaweb.commune.school.data.SchoolStatisticsData p0) throws java.rmi.RemoteException;
 public se.idega.idegaweb.commune.school.business.SchoolMarkValues getSwedishMarks(se.idega.idegaweb.commune.school.data.SchoolStatisticsData p0) throws java.rmi.RemoteException;
 public se.idega.idegaweb.commune.school.business.SchoolMarkValues getTotalMarks(se.idega.idegaweb.commune.school.data.SchoolStatisticsData p0) throws java.rmi.RemoteException;
 public int getTotalMerit() throws java.rmi.RemoteException;
 public void resetMeanValues() throws java.rmi.RemoteException;
 public void resetSchoolValues() throws java.rmi.RemoteException;
 public void sumAuthPoints(java.lang.String p0) throws java.rmi.RemoteException;
 public void sumMarksFailed(java.lang.String p0) throws java.rmi.RemoteException;
 public void sumMarksNoGoal(java.lang.String p0) throws java.rmi.RemoteException;
 public void sumMarksNumberShare(java.lang.String p0) throws java.rmi.RemoteException;
 public void sumMarksPoints(java.lang.String p0) throws java.rmi.RemoteException;
 public void sumMeriteValue(java.lang.String p0) throws java.rmi.RemoteException;
}
