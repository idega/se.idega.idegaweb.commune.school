package se.idega.idegaweb.commune.school.data;

import se.idega.idegaweb.commune.care.data.ChildCareApplication;


public interface AfterSchoolChoice extends ChildCareApplication
{
 public java.lang.String getCaseCodeDescription();
 public java.lang.String getCaseCodeKey();
 public int getSchoolSeasonId();
 public void initializeAttributes();
 public void setSchoolSeasonId(int p0);
}
