package se.idega.idegaweb.commune.school.data;


public interface ProcapitaSchool extends com.idega.data.IDOEntity
{
 public java.lang.String getIDColumnName();
 public com.idega.block.school.data.School getSchool();
 public int getSchoolId();
 public java.lang.String getSchoolName();
 public void initializeAttributes();
 public void setSchoolId(int p0);
 public void setSchoolName(java.lang.String p0);
}
