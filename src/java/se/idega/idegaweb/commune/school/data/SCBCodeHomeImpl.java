package se.idega.idegaweb.commune.school.data;


public class SCBCodeHomeImpl extends com.idega.data.IDOFactory implements SCBCodeHome
{
 protected Class getEntityInterfaceClass(){
  return SCBCode.class;
 }


 public SCBCode create() throws javax.ejb.CreateException{
  return (SCBCode) super.createIDO();
 }


public java.util.Collection findAll()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SCBCodeBMPBean)entity).ejbFindAll();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public SCBCode findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (SCBCode) super.findByPrimaryKeyIDO(pk);
 }



}