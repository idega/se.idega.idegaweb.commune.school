package se.idega.idegaweb.commune.school.data;


public class SchoolChoiceReminderHomeImpl extends com.idega.data.IDOFactory implements SchoolChoiceReminderHome
{
 protected Class getEntityInterfaceClass(){
  return SchoolChoiceReminder.class;
 }


 public SchoolChoiceReminder create() throws javax.ejb.CreateException{
  return (SchoolChoiceReminder) super.createIDO();
 }


public java.util.Collection findAll()throws javax.ejb.FinderException,java.rmi.RemoteException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolChoiceReminderBMPBean)entity).ejbFindAll();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findUnhandled(com.idega.user.data.Group[] p0)throws javax.ejb.FinderException,java.rmi.RemoteException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolChoiceReminderBMPBean)entity).ejbFindUnhandled(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public SchoolChoiceReminder findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (SchoolChoiceReminder) super.findByPrimaryKeyIDO(pk);
 }



}