package se.idega.idegaweb.commune.school.data;


public interface SchoolChoiceReminderHome extends com.idega.data.IDOHome
{
 public SchoolChoiceReminder create() throws javax.ejb.CreateException;
 public SchoolChoiceReminder findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAll()throws javax.ejb.FinderException;
 public java.util.Collection findUnhandled(com.idega.user.data.Group[] p0)throws javax.ejb.FinderException;

}