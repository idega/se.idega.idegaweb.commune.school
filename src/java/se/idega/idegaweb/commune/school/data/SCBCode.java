package se.idega.idegaweb.commune.school.data;


public interface SCBCode extends com.idega.data.IDOEntity
{
 public java.lang.String getCode();
 public java.lang.String getIDColumnName();
 public com.idega.block.school.data.School getSchool();
 public void initializeAttributes();
}
