package se.idega.idegaweb.commune.school.data;


public interface SchoolStatisticsDataHome extends com.idega.data.IDOHome
{
 public SchoolStatisticsData create() throws javax.ejb.CreateException;
 public SchoolStatisticsData findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAllSchoolStats()throws javax.ejb.FinderException;
 public SchoolStatisticsData findBySCBCode(java.lang.String p0)throws javax.ejb.FinderException;

}