package se.idega.idegaweb.commune.school.data;


public class ProcapitaSchoolHomeImpl extends com.idega.data.IDOFactory implements ProcapitaSchoolHome
{
 protected Class getEntityInterfaceClass(){
  return ProcapitaSchool.class;
 }


 public ProcapitaSchool create() throws javax.ejb.CreateException{
  return (ProcapitaSchool) super.createIDO();
 }


public java.util.Collection findAll()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ProcapitaSchoolBMPBean)entity).ejbFindAll();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public ProcapitaSchool findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (ProcapitaSchool) super.findByPrimaryKeyIDO(pk);
 }



}