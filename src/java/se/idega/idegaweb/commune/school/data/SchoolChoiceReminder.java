package se.idega.idegaweb.commune.school.data;


public interface SchoolChoiceReminder extends com.idega.data.IDOEntity,com.idega.block.process.data.Case
{
	String CASE_CODE_KEY = "SCREMIN";
 public java.lang.String getCaseCodeDescription();
 public java.lang.String getCaseCodeKey();
 public java.lang.String[] getCaseStatusDescriptions();
 public java.lang.String[] getCaseStatusKeys();
 public java.util.Date getEventDate();
 public java.util.Date getReminderDate();
 public java.lang.String getText();
 public int getUserId();
 public void initializeAttributes();
 public void setEventDate(java.util.Date p0);
 public void setReminderDate(java.util.Date p0);
 public void setText(java.lang.String p0);
 public void setUser(com.idega.user.data.User p0)throws java.rmi.RemoteException;
}
