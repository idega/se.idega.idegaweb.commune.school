package se.idega.idegaweb.commune.school.data;


public interface SchoolMarksHome extends com.idega.data.IDOHome
{
 public SchoolMarks create() throws javax.ejb.CreateException;
 public SchoolMarks findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAllSchoolMarks()throws javax.ejb.FinderException;
 public java.util.Collection findBySCBCode(java.lang.String p0)throws javax.ejb.FinderException;
 public java.util.Collection findNumberOfSchools()throws javax.ejb.FinderException;

}